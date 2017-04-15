package cn.com.caronwer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.com.caronwer.base.BaseApplication;
import cn.com.caronwer.bean.MeAllOrderInfo;
import cn.com.caronwer.bean.OrderAddressBean;
import cn.com.caronwer.util.LogUtil;
import cn.com.caronwer.view.CustomProgressDialog;

/**
 * Created by LFeng on 16/12/25.
 */
public class NaviMainActivity extends Activity {

    public static final String APP_FOLDER_NAME = "BNSDKSimpleDemo";
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    public static final int NAVI_INIT_MSG = 0;

    protected BaseApplication MyApp;

    private List<BNRoutePlanNode> bnRoutePlanNodeList;
    private LocationClient mLocationClient;
    private BDLocation mLocation;
    private CustomProgressDialog progressDialog;

    private String mSDCardPath = null;
    private String authinfo = null;
    private boolean isLocationSuccess = false;
    private boolean isNaviInitSuccess = false;

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NAVI_INIT_MSG: {
                    if (isLocationSuccess && isNaviInitSuccess) {
                        //progressDialog.dismiss();
                        initData();
                    }
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp = BaseApplication.getInstance();
        MyApp.addActivity(this);

        setContentView(R.layout.activity_navi_main);

        progressDialog = new CustomProgressDialog(this);
        progressDialog.show();
        progressDialog.setText(getString(R.string.navi_initing));
        initLocationClient();
        if (initDirs()) {
            initNavi();
        }
        //initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mLocationClient.isStarted())
            mLocationClient.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationClient.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApp.removeActivity(this);
    }

    private void initLocationClient() {
        /****************百度地图定位客户端初始化*****************/
        mLocationClient = new LocationClient(getApplicationContext());
        //初始化定位参数
        LocationClientOption mLocationOption = new LocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        mLocationOption.setScanSpan(2000);//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setOpenGps(true);//可选，默认false,设置是否使用gps
        mLocationOption.setLocationNotify(false);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        mLocationOption.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mLocationOption.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationOption.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mLocationOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        mLocationOption.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        //设置定位参数
        mLocationClient.setLocOption(mLocationOption);

        //设置回调监听器
        BDLocationListener mLocationListener = new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation != null) {
                    mLocation = bdLocation;
                    if (!isLocationSuccess) {
                        isLocationSuccess = true;
                        myHandler.sendEmptyMessage(NAVI_INIT_MSG);
                    }
                    //initData();
                }
            }
        };
        mLocationClient.registerLocationListener(mLocationListener);
        /****************百度地图定位客户端初始化结束****************/
    }

    private void initData() {
        Gson gson = new Gson();
        Intent intent = getIntent();
        bnRoutePlanNodeList = new ArrayList<>();
        if (intent != null) {
            String json = intent.getStringExtra("mefragmentall");
            LogUtil.i(APP_FOLDER_NAME, json);
            int index = intent.getIntExtra("index", 0);
            MeAllOrderInfo mInfo1 = gson.fromJson(json, MeAllOrderInfo.class);
            OrderAddressBean curAddress = mInfo1.getOrderAddress().get(index);

            BNRoutePlanNode startNode = new BNRoutePlanNode(mLocation.getLongitude(), mLocation.getLatitude(),
                    mLocation.getAddrStr(), null, BNRoutePlanNode.CoordinateType.BD09LL);
            BNRoutePlanNode endNode = new BNRoutePlanNode(curAddress.getLng(), curAddress.getLat(),
                    curAddress.getReceiptAddress(), null, BNRoutePlanNode.CoordinateType.BD09LL);

            bnRoutePlanNodeList.add(startNode);
            bnRoutePlanNodeList.add(endNode);
            BaiduNaviManager.getInstance().launchNavigator(this, bnRoutePlanNodeList, 1, true, new MyRoutePlanListener(startNode));
        }
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        LogUtil.i("NaviMainActivity", "sdCardPath:" + mSDCardPath);
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private void initNavi() {

        BNOuterTTSPlayerCallback ttsCallback = null;

        BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + msg;
                }
                NaviMainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(NaviMainActivity.this, authinfo, Toast.LENGTH_LONG).show();
                    }
                });
            }

            public void initSuccess() {
                Toast.makeText(NaviMainActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                initSetting();
                isNaviInitSuccess = true;
                myHandler.sendEmptyMessage(NAVI_INIT_MSG);
            }

            public void initStart() {
                Toast.makeText(NaviMainActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
            }

            public void initFailed() {
                Toast.makeText(NaviMainActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
            }


        }, null, ttsHandler, ttsPlayStateListener);

    }

    public class MyRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public MyRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            progressDialog.dismiss();
            for (Activity ac : MyApp.getActivityList()) {
                if (ac.getClass().getName().endsWith("NaviGuideActivity")) {
                    return;
                }
            }

            Intent intent = new Intent(NaviMainActivity.this, NaviGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }

        @Override
        public void onRoutePlanFailed() {
            Toast.makeText(NaviMainActivity.this, "算路失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 内部TTS播报状态回传handler
     */
    private Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
                    showToastMsg("Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
                    showToastMsg("Handler : TTS play end");
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 内部TTS播报状态回调接口
     */
    private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {

        @Override
        public void playEnd() {
            showToastMsg("TTSPlayStateListener : TTS play end");
        }

        @Override
        public void playStart() {
            showToastMsg("TTSPlayStateListener : TTS play start");
        }
    };

    public void showToastMsg(final String msg) {
        NaviMainActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(NaviMainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSetting() {
        // 设置是否双屏显示
        BNaviSettingManager.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        // 设置导航播报模式
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // 是否开启路况
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
    }
}
