package com.senter.mobilereader;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guugoo.jiapeiteacher.R;
import com.guugoo.jiapeiteacher.base.BaseActivity;
import com.guugoo.jiapeiteacher.base.Constants;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;

import cn.com.senter.helper.ConsantHelper;
import cn.com.senter.helper.ShareReferenceSaver;
import cn.com.senter.sdkdefault.helper.Error;

/**
 * Created by LFeng on 2017/5/31.
 */

public class ReadCardInfoActivity extends BaseActivity {

    private final static String SERVER_KEY1 = "CN.COM.SENTER.SERVER_KEY1";
    private final static String PORT_KEY1 = "CN.COM.SENTER.PORT_KEY1";
    private final static String SERVER_KEY2 = "CN.COM.SENTER.SERVER_KEY2";
    private final static String PORT_KEY2 = "CN.COM.SENTER.PORT_KEY2";

    private final static String SERVER_KEY3 = "CN.COM.SENTER.SERVER_KEY3";
    private final static String PORT_KEY3 = "CN.COM.SENTER.PORT_KEY3";

    private final static String SERVER_KEY4 = "CN.COM.SENTER.SERVER_KEY4";
    private final static String PORT_KEY4 = "CN.COM.SENTER.PORT_KEY4";

    private final static String Server_Selected = "CN.COM.SENTER.SelIndex";

    private ImageView imageView;
    private TextView bluetoothInfo;
    private String blueAddress = null;
    private SharedPreferences prefs;
    private BlueReaderHelper mBlueReaderHelper;
    private BluetoothAdapter mBluetoothAdapter = null;

    private String server_address = "";
    private int server_port = 0;
    private String Server_sel;
    private boolean isReading = false;
    private boolean isSelectedReturn = false;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_OPEN_BLUETOOTH = 2;
    private static final String BLUE_ADDRESSKEY = "CN.GUUGOO.TEACHER.BLUEADDRESS";
    private MyHandler myHandler;
    private String studentCardId;
    private int type;

    @Override
    protected int getLayout() {
        return R.layout.activity_read_card_info;
    }

    @Override
    protected void initView() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.head);
        TextView tv_center = (TextView) rl_head.findViewById(R.id.tv_center);
        TextView tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        ImageView iv_back = (ImageView) rl_head.findViewById(R.id.iv_back);

        imageView = (ImageView) findViewById(R.id.read_status_img);
        bluetoothInfo = (TextView) findViewById(R.id.bluetooth_info);

        tv_center.setText(R.string.card_info_verify);
        tv_right.setText(R.string.bluetooth_select);
        tv_right.setTextColor(getResources().getColor(R.color.color_Blue));

        TextView readInfo = (TextView) findViewById(R.id.read_info);
        if (type == 1)
            readInfo.setText(R.string.read_student_card_info);
        if (type == 2)
            readInfo.setText(R.string.read_coach_card_info);

        iv_back.setOnClickListener(this);
        tv_right.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        /*
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
        */
        MPermissions.requestPermissions(this, 4, Manifest.permission.READ_PHONE_STATE);
        prefs = getSharedPreferences(Constants.SHARED_NAME, MODE_PRIVATE);
        studentCardId = getIntent().getStringExtra("studentCardId");
        type = getIntent().getIntExtra("readType", 1);
        myHandler = new MyHandler();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "蓝牙未启用", Toast.LENGTH_LONG).show();
            finish();
        }
        mBlueReaderHelper = new BlueReaderHelper(this, myHandler);
        blueAddress = prefs.getString(BLUE_ADDRESSKEY, "");
        initShareReference();
        if (mBluetoothAdapter.isEnabled())
            readCardBlueTooth();
    }

    private void initShareReference() {

        if (ShareReferenceSaver.getData(this, Server_Selected).trim().length() < 1) {
            this.Server_sel = "0";
        } else {
            this.Server_sel = ShareReferenceSaver.getData(this, Server_Selected);
        }

        if (this.server_address.length() <= 0) {
            if (Server_sel.equals("0")) {
                if (ShareReferenceSaver.getData(this, SERVER_KEY1).trim().length() <= 0) {
                    this.server_address = "senter-online.cn";
                } else {
                    this.server_address = ShareReferenceSaver.getData(this, SERVER_KEY1);
                }
                if (ShareReferenceSaver.getData(this, PORT_KEY1).trim().length() <= 0) {
                    this.server_port = 10002;
                } else {
                    this.server_port = Integer.valueOf(ShareReferenceSaver.getData(this, PORT_KEY1));
                }
            }
            if (Server_sel.equals("1")) {
                if (ShareReferenceSaver.getData(this, SERVER_KEY2).trim().length() <= 0) {
                    this.server_address = "senter-online.cn";
                } else {
                    this.server_address = ShareReferenceSaver.getData(this, SERVER_KEY2);
                }
                if (ShareReferenceSaver.getData(this, PORT_KEY2).trim().length() <= 0) {
                    this.server_port = 10002;
                } else {
                    this.server_port = Integer.valueOf(ShareReferenceSaver.getData(this, PORT_KEY2));
                }
            }
            if (Server_sel.equals("2")) {
                if (ShareReferenceSaver.getData(this, SERVER_KEY3).trim().length() <= 0) {
                    this.server_address = "senter-online.cn";
                } else {
                    this.server_address = ShareReferenceSaver.getData(this, SERVER_KEY3);
                }
                if (ShareReferenceSaver.getData(this, PORT_KEY3).trim().length() <= 0) {
                    this.server_port = 10002;
                } else {
                    this.server_port = Integer.valueOf(ShareReferenceSaver.getData(this, PORT_KEY3));
                }
            }
            if (Server_sel.equals("3")) {
                if (ShareReferenceSaver.getData(this, SERVER_KEY4).trim().length() <= 0) {
                    this.server_address = "senter-online.cn";
                } else {
                    this.server_address = ShareReferenceSaver.getData(this, SERVER_KEY4);
                }
                if (ShareReferenceSaver.getData(this, PORT_KEY4).trim().length() <= 0) {
                    this.server_port = 10002;
                } else {
                    this.server_port = Integer.valueOf(ShareReferenceSaver.getData(this, PORT_KEY4));
                }
            }
        }

        mBlueReaderHelper.setServerAddress(server_address);
        mBlueReaderHelper.setServerPort(server_port);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: {
                finish();
                break;
            }
            case R.id.tv_right: {
                Intent intent = new Intent(ReadCardInfoActivity.this, DeviceListActivity.class);
                startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
                break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_OPEN_BLUETOOTH);
        }
    }

    @PermissionGrant(4)
    public void requestContactSuccess() {
        //Toast.makeText(this, "允许访问手机状态信息!", Toast.LENGTH_SHORT).show();
        if (isSelectedReturn) {
            isSelectedReturn = false;
            initShareReference();
            readCardBlueTooth();
        }
    }

    @PermissionDenied(4)
    public void requestContactFailed() {
        Toast.makeText(this, "拒绝访问手机状态信息!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE: {
                isSelectedReturn = true;
                if (resultCode == Activity.RESULT_OK) {
                    blueAddress = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    if (!blueAddress.matches("([0-9a-fA-F][0-9a-fA-F]:){5}([0-9a-fA-F][0-9a-fA-F])")) {
                        bluetoothInfo.setText("address:" + blueAddress + " is wrong, length = " + blueAddress.length());
                        return;
                    }
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(BLUE_ADDRESSKEY, blueAddress).apply();
                    MPermissions.requestPermissions(this, 4, Manifest.permission.READ_PHONE_STATE);
//                    initShareReference();
//                    readCardBlueTooth();
                }
            }
            case REQUEST_OPEN_BLUETOOTH: {
                //System.out.println("resultCode:" + resultCode);
                if (resultCode == RESULT_OK) {
                    initShareReference();
                    readCardBlueTooth();
                }
            }
        }
    }

    protected void readCardBlueTooth() {

        if (blueAddress == null) {
            Toast.makeText(this, "请选择蓝牙设备，再读卡!", Toast.LENGTH_LONG).show();
            return;
        }

        if (blueAddress.length() <= 0) {
            Toast.makeText(this, "请选择蓝牙设备，再读卡!", Toast.LENGTH_LONG).show();
            return;
        }

        if (!isReading) {
            isReading = true;
            new Thread() {
                @Override
                public void run() {
                    try {
                        boolean registerStatus = mBlueReaderHelper.registerBlueCard(blueAddress);
                        if (registerStatus == true) {
                            new BlueReadTask().executeOnExecutor(Executors.newCachedThreadPool());
                        } else {
                            myHandler.sendEmptyMessage(ConsantHelper.READ_CARD_WARNING);
                        }
                    } catch (Exception e) {
                        //readCardBlueTooth();
                    }
                }
            }.start();
        }

    }

    private class BlueReadTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPostExecute(String strCardInfo) {
            isReading = false;
            if (TextUtils.isEmpty(strCardInfo)) {
                myHandler.sendEmptyMessage(ConsantHelper.READ_CARD_FAILED);
                return;
            }
            if (strCardInfo.length() <= 2) {
                readCardFailed(strCardInfo);
                myHandler.sendEmptyMessage(ConsantHelper.READ_CARD_FAILED);
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(strCardInfo);
                /*
                System.out.println(jsonObject.getString("name"));
                System.out.println(jsonObject.getString("sex"));
                System.out.println(jsonObject.getString("ethnicity"));
                System.out.println(jsonObject.getString("birth"));
                System.out.println(jsonObject.getString("cardNo"));
                System.out.println(jsonObject.getString("authority"));
                System.out.println(jsonObject.getString("address"));
                System.out.println(jsonObject.getString("period"));
                */

                bluetoothInfo.setText("身份证信息读取成功。\n" + jsonObject.getString("cardNo"));
                if (studentCardId.compareTo(jsonObject.getString("cardNo")) == 0) {
                    bluetoothInfo.setText("身份证信息验证成功！\n" + jsonObject.getString("cardNo"));
                    imageView.setImageResource(R.mipmap.read_success);
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    bluetoothInfo.setText("身份证信息验证失败！\n" + jsonObject.getString("cardNo"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            String strCardInfo = mBlueReaderHelper.read();
            return strCardInfo;
        }
    }

    private void readCardFailed(String strcardinfo) {
        int bret = Integer.parseInt(strcardinfo);
        switch (bret) {
            case -1:
                bluetoothInfo.setText("服务器连接失败!");
                break;
            case 1:
                bluetoothInfo.setText("读卡失败,请将卡放入正确区域!");
                break;
            case 2:
                bluetoothInfo.setText("读卡失败!");
                break;
            case 3:
                bluetoothInfo.setText("网络超时!");
                break;
            case 4:
                bluetoothInfo.setText("读卡失败!");
                break;
            case -2:
                bluetoothInfo.setText("读卡失败!");
                break;
            case 5:
                bluetoothInfo.setText("照片解码失败!");
                break;
        }
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            isReading = false;
            switch (msg.what) {
                case ConsantHelper.READ_CARD_SUCCESS:
                    break;

                case ConsantHelper.SERVER_CANNOT_CONNECT:
                    bluetoothInfo.setText("服务器连接失败! 请检查网络。");
                    break;

                case ConsantHelper.READ_CARD_FAILED:
                    try {
                        Thread.sleep(1000);
                        System.out.println("read card failed");
                        readCardBlueTooth();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //bluetoothInfo.setText("无法读取信息请将身份证放入正确区域!");
                    break;

                case ConsantHelper.READ_CARD_WARNING:
                    //Toast.makeText(ReadCardInfoActivity.this, "请确认蓝牙设备已经连接，再读卡!", Toast.LENGTH_LONG).show();
                    break;

                case ConsantHelper.READ_CARD_PROGRESS:
                    int progress_value = (Integer) msg.obj;
                    bluetoothInfo.setText("正在读卡......,进度：" + progress_value + "%");//,进度：+ progress_value + "%"
                    break;

                case ConsantHelper.READ_CARD_START:
                    bluetoothInfo.setText("正在读卡......");
                    break;
                case Error.ERR_CONNECT_SUCCESS:
                    break;
                case Error.ERR_CONNECT_FAILD:
                    bluetoothInfo.setText(msg.obj + "连接失败!");
                    //bluetoothInfo.setText("请确认蓝牙设备已经连接，再读卡!");
                    break;
                case Error.ERR_CLOSE_SUCCESS:
                    break;
                case Error.ERR_CLOSE_FAILD:
                    //bluetoothInfo.setText(msg.obj + "断开连接失败");
                    break;
                case Error.RC_SUCCESS:
                    //String devname12 = (String) msg.obj;
                    break;

            }
        }

    }
}
