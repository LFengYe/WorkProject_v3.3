package com.DLPort.myactivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.DLPort.OurActivity.PrivilegeActivity;
import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.mytool.FuYouThread;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;
import com.DLPort.mytool.XmlOperate;
import com.DLPort.myview.MyDialog;
import com.DLPort.myview.TitleView;
import com.fuiou.mpay.encrypt.DESCoderFUIOU;
import com.fuiou.util.MD5;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LuoxiangActivity extends BaseActivity {
    private static final String TAG = "LuoxiangActivity";

    private View[] views;
    private TextView[] textViews;
    private TextView containerTypeValueText;
    private Button payButton;
    private boolean CompanyOk = false;
    private boolean CompanOk = false;
    private boolean chick = false;
    private boolean Ok = false;
    private boolean xiang = false;
    private String companyone;

    private String[] CarNo, ContainerType;
    private int[] containerTypeValue;
    private int[] price;
    private int userType;
    private SharedPreferences preferences;
    private CheckBox Privilege,Balance;
    private int privilegeId;

    private boolean UseBalance=false;

    //region 支付前创建支付订单
    Handler payHandler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {
                    try {
                        String data;
                        JSONObject jsonUser = null;
                        jsonUser = new JSONObject((String) msg.obj);
                        Log.d(TAG, "创建订单的响应" + jsonUser.toString());
                        int status = jsonUser.getInt("Status");
                        if (status == 0) {

                            data = jsonUser.getString("Data");
                            JSONObject object = new JSONObject(data);

                            float amount = Float.valueOf(object.getString("Amount"));
                            if (amount > 0) {
                                Bundle bundle = new Bundle();
                                bundle.putString("mchntorderId", object.getString("flg"));
                                bundle.putString("loginName", preferences.getString("LoginName", ""));
                                bundle.putFloat("amt", amount);
                                bundle.putString("tag", "LuoXiang");
                                bundle.putInt("Type", userType);

                                Intent intent = new Intent();
                                intent.putExtras(bundle);
                                intent.setClass(LuoxiangActivity.this, PayInfoWriteActivity.class);
                                startActivity(intent);
                            } else {
                                MyToast.makeText(LuoxiangActivity.this, "办理成功");
                            }
                            setTextViewsEmpty();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    };
    //endregion
    //region 富友支付Handler
    private Handler fuYouHandler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FuYouThread.SUCCESS: {
                    break;
                }
            }
        }
    };
    //endregion
    //region 获取箱型和价格数据得Handler
    private Handler handler1 = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    String data;
                    JSONObject jsonUser = null;
                    jsonUser = new JSONObject((String) msg.obj);
                    Log.d(TAG, "Login的响应" + jsonUser.toString());
                    int status = jsonUser.getInt("Status");
                    Log.i(TAG, "状态" + status);
                    if (0 == status) {
                        data = jsonUser.getString("Data");
                        Log.d(TAG, data);
                        try {
                            JSONArray jsonArray = new JSONArray(data);
                            ContainerType = new String[jsonArray.length()];
                            containerTypeValue = new int[jsonArray.length()];
                            price = new int[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json = jsonArray.getJSONObject(i);
                                ContainerType[i] = GlobalParams.GetContainerType(json.getInt("ContainerType"))
                                        + "\t价格:" + json.getInt("Price");
                                containerTypeValue[i] = json.getInt("ContainerType");
                                price[i] = json.getInt("Price");
                                CompanOk = true;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        MyToast.makeText(LuoxiangActivity.this, "加载失败");
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, msg.what + " 服务器异常");
                MyToast.makeText(LuoxiangActivity.this, msg.what + " 服务器异常");
            }
        }
    };
    //endregion
    //region 获取车辆列表的Handler
    private Handler handler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Ok = true;
            if (msg.what == 1) {
                try {
                    String data;
                    JSONObject jsonUser = null;
                    jsonUser = new JSONObject((String) msg.obj);
                    Log.d(TAG, "Login的响应" + jsonUser.toString());
                    int status = jsonUser.getInt("Status");
                    Log.i(TAG, "状态" + status);
                    if (0 == status) {
                        data = jsonUser.getString("Data");
                        Log.d(TAG, data);
                        try {
                            JSONArray jsonArray = new JSONArray(data);
                            CarNo = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json = jsonArray.getJSONObject(i);
                                CarNo[i] = json.getString("CarNo");
                                CompanyOk = true;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject jsonObject = new JSONObject();
                        new MyThread(Constant.URL_PostHoldBoxList, handler1, jsonObject, LuoxiangActivity.this).start();
                    } else {
                        MyToast.makeText(LuoxiangActivity.this, "加载失败");
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, msg.what + " 服务器异常");
                MyToast.makeText(LuoxiangActivity.this, msg.what + " 服务器异常");
            }
        }
    };
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tui_luoxiang);
        InitTitle();
        findById();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void init() {
        finddata();
        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalParams.isNetworkAvailable(LuoxiangActivity.this)) {
                    MyDialog myDialog = new MyDialog(LuoxiangActivity.this,1);
                    myDialog.setContent("请输入车牌号");
                    myDialog.setDialogCallback(new MyDialog.Dialogcallback() {
                        @Override
                        public void dialogdo(String string) {
                            textViews[0].setText(string);
                        }
                    });

                    myDialog.show();

                } else {
                    MyToast.makeText(LuoxiangActivity.this, "亲,网络未连接");
                }
            }
        });
        views[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalParams.isNetworkAvailable(LuoxiangActivity.this)) {
                    if (CompanOk) {
                        mydedialog("箱型", ContainerType, 1);
                        xiang = true;
                    } else {
                        MyToast.makeText(LuoxiangActivity.this, "加载失败");
                    }
                } else {
                    MyToast.makeText(LuoxiangActivity.this, "亲,网络未连接");
                }
            }
        });
        views[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (xiang) {
                    MyDialog myDialog = new MyDialog(LuoxiangActivity.this, 1);
                    myDialog.setContent("箱量");
                    myDialog.setDialogCallback(new MyDialog.Dialogcallback() {
                        @Override
                        public void dialogdo(String string) {
                            if (string.length() > 0 && string.matches("^[0-9]*$")) {
                                Float price = Float.valueOf(textViews[3].getText().toString()) / Integer.valueOf(textViews[2].getText().toString());
                                textViews[2].setText(string);
                                float in = Integer.parseInt(string) * price;
                                textViews[3].setText(String.valueOf(in));
                            } else {
                                MyToast.makeText(LuoxiangActivity.this, "请输入数字");
                            }

                        }
                    });
                    myDialog.sethineText("请输入数字！");
                    myDialog.show();
                } else {
                    MyToast.makeText(LuoxiangActivity.this, "请先选择箱型");
                }

            }
        });

        Privilege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Privilege.isChecked()) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("Type", 1);
                    intent.putExtras(bundle);
                    intent.setClass(LuoxiangActivity.this, PrivilegeActivity.class);
                    startActivityForResult(intent, 1);
                }

            }
        });
        Balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Balance.isChecked()) {
                    UseBalance = true;
                } else {
                    UseBalance = false;
                }

            }
        });

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkOrderData()) {
                    Log.i(TAG, "boxNumber:" + textViews[2].getText().toString() +
                            "containerType:" + containerTypeValueText.getText().toString() +
                            "amt:" + Float.valueOf(textViews[3].getText().toString()));
                    createOrder(Integer.valueOf(textViews[2].getText().toString()),
                            Integer.valueOf(containerTypeValueText.getText().toString()),
                            textViews[0].getText().toString(),
                            Float.valueOf(textViews[3].getText().toString()));
                }
                /*
                payAction("10", "2.0", "1103234566586380", "user0001", "200", "6222001001127676964",
                        Constant.URL_PaymentPostFinishPay, "张三", "340802197810220010", "0", "1", Constant.URL_BaiduTest,
                        Constant.URL_BaiduTest);
                        */
            }
        });
    }

    private void finddata() {
        if (GlobalParams.isNetworkAvailable(LuoxiangActivity.this)) {
            JSONObject jsonObject = new JSONObject();
            Log.d(TAG, jsonObject.toString());
            new MyThread(Constant.URL_PostCarList, handler, jsonObject, LuoxiangActivity.this).start();
        } else {
            MyToast.makeText(LuoxiangActivity.this, "亲,网络未连接");
        }

    }

    private void findById() {
        views = new View[3];
        textViews = new TextView[4];
        views[0] = findViewById(R.id.luo_A_CarNo);
        views[1] = findViewById(R.id.luo_A_ContainerType);
        views[2] = findViewById(R.id.luo_A_number);
        textViews[0] = (TextView) findViewById(R.id.luo_CarNo);
        textViews[1] = (TextView) findViewById(R.id.luo_ContainerType);
        textViews[2] = (TextView) findViewById(R.id.luo_number);
        textViews[3] = (TextView) findViewById(R.id.luo_manay);
        containerTypeValueText = (TextView) findViewById(R.id.luo_ContainerType_value);
        Privilege = (CheckBox) findViewById(R.id.L_Privilege);
        Balance = (CheckBox) findViewById(R.id.L_Balance);
        payButton = (Button) findViewById(R.id.zhifu);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userType = bundle.getInt("Type");
        if (userType == 0) {
            preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        } else if (userType == 1) {
            preferences = getSharedPreferences("huo", Context.MODE_PRIVATE);
        }
    }

    private void InitTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.tui_luoxiang_title);
        titleView.setBackImageVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setMiddleText(R.string.luoxiang);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setTextViewsEmpty() {
        textViews[0].setText("");
        textViews[1].setText("");
        textViews[2].setText("");
        textViews[3].setText("");
        containerTypeValueText.setText("");
        Privilege.setChecked(false);
        Privilege.setText("使用优惠券");
        Balance.setChecked(false);
    }

    private boolean checkOrderData() {
        if (TextUtils.isEmpty(textViews[0].getText())) {
            MyToast.makeText(this, R.string.car_number_empty);
            return false;
        }
        if (TextUtils.isEmpty(textViews[1].getText())) {
            MyToast.makeText(this, R.string.box_type_empty);
            return false;
        }
        if (TextUtils.isEmpty(containerTypeValueText.getText())) {
            MyToast.makeText(this, R.string.box_type_empty);
            return false;
        }
        if (TextUtils.isEmpty(textViews[2].getText())) {
            MyToast.makeText(this, R.string.box_number_empty);
            return false;
        }
        return true;
    }

    private void createOrder(int boxNumber, int boxType, String vehNof, float amt) {
        if (GlobalParams.isNetworkAvailable(LuoxiangActivity.this)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Number", boxNumber);
                jsonObject.put("BoxType", boxType);
                jsonObject.put("VehNof", vehNof);
                jsonObject.put("UserId", preferences.getString("UserId", null));
                jsonObject.put("Amt", amt);
                jsonObject.put("PrivilegeId",privilegeId);
                jsonObject.put("UseBalance",UseBalance);
                Log.i(TAG, "userId:" + preferences.getString("UserId", null));
                new MyThread(Constant.URL_PaymentPostPay, payHandler, jsonObject, LuoxiangActivity.this).start();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            MyToast.makeText(LuoxiangActivity.this, "亲,网络未连接");
        }
    }

    public void mydedialog(String string, final String[] strings, final int i) {

        AlertDialog dialog = new AlertDialog.Builder(LuoxiangActivity.this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(string)
                .setSingleChoiceItems(strings, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "i=============" + which);
                                companyone = strings[which];
                                chick = true;

                                if (i == 1) {
                                    textViews[2].setText(String.valueOf(1));
                                    textViews[3].setText(String.valueOf(price[which]));
                                    containerTypeValueText.setText(String.valueOf(containerTypeValue[which]));
                                }

                            }
                        }
                )
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (chick) {
                                    textViews[i].setText(companyone);
                                } else {
                                    // 选择列表没有进行选择, 点击确定按钮默认为第一条数据
                                    textViews[i].setText(strings[0]);

                                    if (i == 1) {
                                        textViews[2].setText(String.valueOf(1));
                                        textViews[3].setText(String.valueOf(price[0]));
                                        containerTypeValueText.setText(String.valueOf(containerTypeValue[0]));
                                    }
                                }
                                chick = false;
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("取消", null)
                .show();
        int no = strings.length;

        if (no > 5) {
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.height = 1000;
            dialog.getWindow().setAttributes(params);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            privilegeId = data.getIntExtra("privilegeId", 0);
            Privilege.setText("已优惠:" + data.getFloatExtra("amount", 0) + "元");
        }
    }
}
