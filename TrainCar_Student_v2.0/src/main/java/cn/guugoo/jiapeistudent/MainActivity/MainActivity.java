package cn.guugoo.jiapeistudent.MainActivity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.guugoo.jiapeistudent.App.Constant;
import cn.guugoo.jiapeistudent.App.MyApplication;
import cn.guugoo.jiapeistudent.Data.ReturnData;
import cn.guugoo.jiapeistudent.Fragment.HomeFragment;
import cn.guugoo.jiapeistudent.Fragment.MyFragment;
import cn.guugoo.jiapeistudent.R;
import cn.guugoo.jiapeistudent.Tools.DES;
import cn.guugoo.jiapeistudent.Tools.LocationService;
import cn.guugoo.jiapeistudent.Tools.MyThread;
import cn.guugoo.jiapeistudent.Tools.MyToast;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private HomeFragment homefragment;
    private MyFragment myfragment;
    private ImageView[] imagebuttons;
    private Fragment[] fragments;
    private View[] views;
    private int index;
    private int currentTabIndex=0;
    private LocationService locationService;
    public static boolean isForeground = false;
    private SharedPreferences sp;
    private String BookingId;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";
    private MessageReceiver mMessageReceiver;
    public static final String KEY_EXTRAS = "extras";
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void processingData(ReturnData data) {
        Log.d(TAG, "processingData: "+data.getData());
    }

    @Override
    protected void initTitle() {
        setBarStyle();
    }

    public void setBarStyle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            Window window = getWindow();
            FlymeSetStatusBarLightMode(getWindow(), true);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }
    protected void findView() {
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        imagebuttons =new ImageView[2];
        imagebuttons[0] =(ImageView) findViewById(R.id.ib_home);
        imagebuttons[1] = (ImageView) findViewById(R.id.ib_myself);
        views = new View[2];
        views[0] = findViewById(R.id.home);
        views[1] =findViewById(R.id.myself);
        homefragment =new HomeFragment();
        myfragment = new MyFragment();
        fragments =new Fragment[]{homefragment,myfragment};
        registerMessageReceiver();  // used for receive msg

    }

    protected void init() {
//        getData();
//            getLocation();
//            open(true);
        imagebuttons[0].setSelected(true);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_content, homefragment)
                .add(R.id.main_content, myfragment)
                .hide(myfragment).show(homefragment).commit();
        views[0].setOnClickListener(this);
        views[1].setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home:
                index = 0;
                homefragment.setBall(Constant.state);
                break;
            case R.id.myself:
                index = 1;
                myfragment.getData();
                break;
        }
        BarChange();
    }

    private void BarChange() {
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(fragments[currentTabIndex]);

            if (!fragments[index].isAdded()) {
                trx.add(R.id.main_content, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        //  把当前tab设为选中状态
        imagebuttons[currentTabIndex].setSelected(false);
        imagebuttons[index].setSelected(true);
        currentTabIndex = index;

    }

    private long exitTime = 0;
    private Toast toast;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                toast = Toast.makeText(getApplicationContext(), "再按一次退出",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                exitTime = System.currentTimeMillis();
            } else {
                moveTaskToBack(false);
                finish();

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        isForeground = true;
        JPushInterface.onResume(MainActivity.this);
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        JPushInterface.onPause(MainActivity.this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();

    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                if(!TextUtils.isEmpty(messge)){
                    JSONObject json = JSON.parseObject(messge);
                    BookingId = json.getString("BookingId");
                    int type =Integer.valueOf(json.getString("type"));
                    switch (type){
                        case 5:
                            getLocation();
                            open(true);
                            break;
                        case 3:
                            getLocation();
                            open(false);
                            break;
                        case 8:
                            homefragment.setBall(true);
                            break;
                    }
                }

            }
        }
    }

    private void  getLocation(){
        // -----------location config ------------
        locationService = ((MyApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
    }

    private void open(boolean type){
        if (type){
            locationService.start();// 定位SDK
        } else{
            locationService.stop();
        }
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                double Latitude = location.getLatitude();// 纬度
                double Longitude = location.getLongitude();// 经度
                logMsg(Longitude,Latitude);
            }
        }
    };

    private void getData(){
        String data =getIntent().getStringExtra("json");

    }

    /**
     * 显示请求字符串
     *
     */
    public void logMsg(final double Longitude, final double Latitude) {

//        JSONObject json = new JSONObject();
//        json.put("Longitude",Longitude);
//        json.put("Latitude",Latitude);
//        json.put("BookingId",BookingId);
//        json.put("SchoolId",sp.getInt("SchoolId",0));
//        json.put("UserType",1);
//        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        String date = sDateFormat.format(new java.util.Date());
//        json.put("CreateTime",date);
//        new MyThread(Constant.URL_GpsStorage, handler, DES.encryptDES(json.toString())).start();

        Log.d(TAG, "onReceiveLocation:Error "+Latitude);
        Log.d(TAG, "onReceiveLocation:Error "+Longitude);
        open(false);
    }


}
