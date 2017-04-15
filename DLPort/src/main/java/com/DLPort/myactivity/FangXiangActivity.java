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
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;
import com.DLPort.myview.TitleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/6/27.
 */
public class FangXiangActivity  extends BaseActivity{
    private static final String TAG = "FangXiangActivity";
    private View[] views;
    private TextView[] textViews;
    private CheckBox Privilege,Balance;
    private Button payButton;
    private String[]  ContainerType,Mnumber;
    private int[] containerTypeValue;
    private int[] price;
    private boolean CompanyOk = false;
    private boolean CompanOk = false;
    private boolean Ok = false;
    private boolean xiang = false;
    private boolean chick = false;
    private TextView containerTypeValueText;
    private String companyone;
    private int Type;
    private String BillofLading;
    private String ShopId;
    private SharedPreferences preferences;
    private int Maxnumber=0;  //最大办理放箱量
    private String UserID;
    private int privilegeId;
    private boolean UseBalance=false;
    private boolean MaxOK=false;
    private Float Oneprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fangxiang);
        InitTitle();
        findById();
        init();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private Handler handler3 = new MyHandler(this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1) try {
                JSONObject jsonUser = null;
                jsonUser = new JSONObject((String) msg.obj);
                Log.d(TAG, "Login的响应" + jsonUser.toString());
                int status = jsonUser.getInt("Status");
                Log.i(TAG, "状态" + status);
                if (0 == status) {
                    String data = jsonUser.getString("Data");
                    JSONObject json = new JSONObject(data);
                    String flg = json.getString("flg");
                    int Amount = json.getInt("Amount");
                    int CouPonSum = json.getInt("CouPonSum");
                    int Balance = json.getInt("Balance");

                    if (Amount > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putString("mchntorderId", flg);
                        bundle.putString("loginName", preferences.getString("LoginName", null));
                        bundle.putFloat("amt", Float.valueOf(Amount));
                        bundle.putString("tag", "FangXiang");
                        bundle.putInt("Type", Type);
                        Intent intent = new Intent();
                        intent.putExtras(bundle);
                        intent.setClass(FangXiangActivity.this, PayInfoWriteActivity.class);
                        startActivity(intent);
                    }else {
                        MyToast.makeText(FangXiangActivity.this,"办理成功");
                    }
                    setTextViewsEmpty();
                } else {
                    MyToast.makeText(FangXiangActivity.this, "查询失败");
                }
            } catch (JSONException e) {
                Log.d(TAG, "异常——————————————————————————————");
                e.printStackTrace();
            }
            else {
                Log.d(TAG,msg.what+" 服务器异常");
                MyToast.makeText(FangXiangActivity.this, msg.what+" 服务器异常");
            }
        }
    };

    private Handler handler1 = new MyHandler(this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                try {
                    JSONObject jsonUser = null;
                    jsonUser = new JSONObject((String) msg.obj);
                    Log.d(TAG, "Login的响应" + jsonUser.toString());
                    int status = jsonUser.getInt("Status");
                    Log.i(TAG, "状态" + status);
                    if (0 == status) {
                         String data = jsonUser.getString("Data");
                         Maxnumber = Integer.valueOf(data);
                        if(Maxnumber>0){
                            MaxOK=true;
                        }
                        Log.d(TAG,"======="+Maxnumber);
                    } else {
                        MyToast.makeText(FangXiangActivity.this, jsonUser.getString("Message"));
//                        finish();
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            }else {
                Log.d(TAG,msg.what+" 服务器异常");
                MyToast.makeText(FangXiangActivity.this, msg.what+" 服务器异常");
            }
        }
    };

    private Handler handler = new MyHandler(this) {
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
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("UserID", UserID);
                        jsonObject.put("BillofLading", BillofLading);
                        jsonObject.put("Type",0 );
                        Log.d(TAG,jsonObject.toString());
                        new MyThread(Constant.URL_PaymentPostGetCount, handler1, jsonObject, FangXiangActivity.this).start();


                    } else {
                        MyToast.makeText(FangXiangActivity.this, "加载失败");
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, msg.what + " 服务器异常");
                MyToast.makeText(FangXiangActivity.this, msg.what + " 服务器异常");
            }
        }
    };

    private void init() {

        Bundle bundle = getIntent().getExtras();
        Type = bundle.getInt("Type");
        BillofLading = bundle.getString("BillofLading");
        ShopId = bundle.getString("ShopId");


        if (Type == 0) {
            preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        } else if (Type == 1) {
            preferences = getSharedPreferences("huo", Context.MODE_PRIVATE);
        }
        UserID =preferences.getString("UserId", null);
        Log.d(TAG,"UserID==" + UserID);
        Log.d(TAG,"BillofLading==="+BillofLading);
        Log.d(TAG,"ShopId==="+ShopId);


        finddata();
        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalParams.isNetworkAvailable(FangXiangActivity.this)) {
                    if (CompanOk) {
                        mydedialog("箱型", ContainerType, 0);
                        xiang = true;
                    } else {
                        MyToast.makeText(FangXiangActivity.this, "加载失败");
                    }
                } else {
                    MyToast.makeText(FangXiangActivity.this, "亲,网络未连接");
                }
            }
        });
        views[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mnumber = new String[Maxnumber];
                for(int i =0 ;i<Maxnumber;i++){
                    int J=i+1;
                    Mnumber[i]=String.valueOf(J);
                }
                if (MaxOK) {
                    mydedialog("数量", Mnumber, 1);
                    xiang = true;
                } else {
                    MyToast.makeText(FangXiangActivity.this, "您的最大放箱量为0！");
                }

            }
        });
//        views[1].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (xiang) {
//                    MyDialog myDialog = new MyDialog(FangXiangActivity.this, 1);
//                    myDialog.setContent("箱量");
//                    myDialog.setDialogCallback(new MyDialog.Dialogcallback() {
//                        @Override
//                        public void dialogdo(String string) {
//                            if (string.length() > 0 && string.matches("^[0-9]*$")) {
//                                Float price = Float.valueOf(textViews[2].getText().toString()) / Integer.valueOf(textViews[1].getText().toString());
//                                textViews[1].setText(string);
//                                float in = Integer.parseInt(string) * price;
//                                textViews[2].setText(String.valueOf(in));
//                            } else {
//                                MyToast.makeText(FangXiangActivity.this, "请输入数字");
//                            }
//
//                        }
//                    });
//                    myDialog.sethineText("请输入数字！");
//                    myDialog.show();
//                } else {
//                    MyToast.makeText(FangXiangActivity.this, "请先选择箱型");
//                }
//
//            }
//        });

//        payButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checkOrderData()) {
//                    Log.i(TAG, "boxNumber:" + textViews[2].getText().toString() +
//                            "containerType:" + containerTypeValueText.getText().toString() +
//                            "amt:" + Float.valueOf(textViews[3].getText().toString()));
//                    createOrder(Integer.valueOf(textViews[2].getText().toString()),
//                            Integer.valueOf(containerTypeValueText.getText().toString()),
//                            textViews[0].getText().toString(),
//                            Float.valueOf(textViews[3].getText().toString()));
//                }
//
//            }
//        });

        Privilege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Privilege.isChecked()){
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("Type", 1);
                    intent.putExtras(bundle);
                    intent.setClass(FangXiangActivity.this, PrivilegeActivity.class);
                    startActivityForResult(intent, 1);
                }
            }
        });
        Balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Balance.isChecked()){
                    UseBalance=true;
                }else {
                    UseBalance=false;
                }

            }
        });

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkOrderData()){
                    if(Integer.valueOf(textViews[1].getText().toString())>Maxnumber){
                        MyToast.makeText(FangXiangActivity.this, "你的办理数量错误！");
                    }else {
                        finddata2();
                    }

                }
            }
        });
    }

    private void findById() {
        views = new View[2];
        textViews = new TextView[3];

        views[0] = findViewById(R.id.S_A_BoxType);
        views[1] = findViewById(R.id.S_A_Number);

        textViews[0] = (TextView) findViewById(R.id.S_BoxType);
        textViews[1] = (TextView) findViewById(R.id.S_Number);
        textViews[2] = (TextView) findViewById(R.id.S_Amount);
        Privilege = (CheckBox) findViewById(R.id.F_Privilege);
        Balance = (CheckBox) findViewById(R.id.F_Balance);
        payButton = (Button) findViewById(R.id.Fpay_Button);
        containerTypeValueText = (TextView) findViewById(R.id.luo_ContainerType_value);
    }

    private void finddata() {
        if (GlobalParams.isNetworkAvailable(FangXiangActivity.this)) {
            JSONObject jsonObject = new JSONObject();
            Log.d(TAG, jsonObject.toString());
            new MyThread(Constant.URL_PostHoldBoxList, handler, jsonObject, FangXiangActivity.this).start();
        } else {
            MyToast.makeText(FangXiangActivity.this, "亲,网络未连接");
        }

    }

    //放箱办理
    private void finddata2() {
        if (GlobalParams.isNetworkAvailable(FangXiangActivity.this)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("ShipId",ShopId);
                jsonObject.put("BillofLading",BillofLading);
                jsonObject.put("Amount",textViews[2].getText().toString());
                jsonObject.put("UseBalance",UseBalance);
                jsonObject.put("PrivilegeId",privilegeId);
                jsonObject.put("UserID",UserID);
                jsonObject.put("BoxType",Integer.valueOf(containerTypeValueText.getText().toString()));
                jsonObject.put("Number",Integer.valueOf(textViews[1].getText().toString()));

                Log.d(TAG, jsonObject.toString());
                new MyThread(Constant.URL_PaymentPostPutTheBox, handler3, jsonObject, FangXiangActivity.this).start();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            MyToast.makeText(FangXiangActivity.this, "亲,网络未连接");
        }

    }

    public void mydedialog(String string, final String[] strings, final int i) {

        AlertDialog dialog = new AlertDialog.Builder(FangXiangActivity.this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(string)
                .setSingleChoiceItems(strings, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "i=============" + which);
                                companyone = strings[which];
                                chick = true;

                                if (i == 0) {
                                    Oneprice=Float.valueOf(price[which]);
                                    containerTypeValueText.setText(String.valueOf(containerTypeValue[which]));
                                }
                                if(i==1){
                                    float in = Integer.parseInt(companyone) * Oneprice;
                                    textViews[2].setText(String.valueOf(in));
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

                                    if (i == 0) {
                                        Oneprice=Float.valueOf(price[0]);
                                        containerTypeValueText.setText(String.valueOf(containerTypeValue[0]));
                                    }
                                    if(i==1){
                                        textViews[2].setText(String.valueOf(Oneprice));
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

    private boolean checkOrderData() {

        if (TextUtils.isEmpty(textViews[0].getText())) {
            MyToast.makeText(this, R.string.box_type_empty);
            return false;
        }
        if (TextUtils.isEmpty(containerTypeValueText.getText())) {
            MyToast.makeText(this, R.string.box_type_empty);
            return false;
        }
        if (TextUtils.isEmpty(textViews[1].getText())) {
            MyToast.makeText(this, R.string.box_number_empty);
            return false;
        }
        return true;
    }

    private void InitTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.fangxiang);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.query);
        titleView.setMiddleText(R.string.fangxiang);
        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
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
        containerTypeValueText.setText("");
        Privilege.setChecked(false);
        Privilege.setText("使用优惠券");
        Balance.setChecked(false);
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
