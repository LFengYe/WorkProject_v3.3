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
import com.DLPort.myview.MyDialog;
import com.DLPort.myview.TitleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/7/4.
 */
public class TiXiangActivity extends BaseActivity {

    private static final String TAG="TiXiangActivity";
    private View[] views;
    private TextView[] textViews;


    private String[] name,no;
    private CheckBox Privilege,Balance;
    private Button payButton;
    private TextView containerTypeValueText;
    private String[] CarNo, ContainerType,Mnumber;
    private int[] containerTypeValue;
    private int[] price;
    private boolean CompanyOk = false;
    private boolean CompanOk = false;
    private boolean Ok = false;
    private boolean xiang = false;
    private boolean chick = false;

    private String companyone;
    private int Type;
    private String BillofLading;
    private String ShopId;
    private SharedPreferences preferences;
    private int Maxnumber=0;  //最大办理放箱量
    private String UserID;
    private boolean UseBalance=false;
    private int privilegeId;
    private Float Oneprice;
    private boolean MaxOK=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tixiang);
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
            if(msg.what==1){
                try {
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
                            bundle.putString("tag", "TiXiang");
                            bundle.putInt("Type", Type);
                            Intent intent = new Intent();
                            intent.putExtras(bundle);
                            intent.setClass(TiXiangActivity.this, PayInfoWriteActivity.class);
                            startActivity(intent);
                        }else {
                            MyToast.makeText(TiXiangActivity.this,"办理成功");
                        }

                        setTextViewsEmpty();
                    } else {
                        MyToast.makeText(TiXiangActivity.this, "提单号错误，查询失败");
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            }else {
                Log.d(TAG,msg.what+" 服务器异常");
                MyToast.makeText(TiXiangActivity.this, msg.what+" 服务器异常");
            }
        }
    };

    private Handler handler2 = new MyHandler(this){
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
                    } else {
                        MyToast.makeText(TiXiangActivity.this, jsonUser.getString("Message"));
                    }
//                    if(Integer.valueOf(textViews[4].getText().toString())>Maxnumber){
//                        MyToast.makeText(TiXiangActivity.this, "你的办理数量错误！");
//                    }else { finddata3();
//
//                    }

                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            }else {
                Log.d(TAG,msg.what+" 服务器异常");
                MyToast.makeText(TiXiangActivity.this, msg.what+" 服务器异常");
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

                    } else {
                        MyToast.makeText(TiXiangActivity.this, "加载失败");
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, msg.what + " 服务器异常");
                MyToast.makeText(TiXiangActivity.this, msg.what + " 服务器异常");
            }
        }
    };

    /*
    private Handler handler1 =new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
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
                        diaplaydata(data);

                    } else {
                        MyToast.makeText(TiXiangActivity.this, "加载失败");
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            }else {
                Log.d(TAG, msg.what + " 服务器异常");
                MyToast.makeText(TiXiangActivity.this, msg.what + " 服务器异常");
            }
        }
    };


    private void diaplaydata(String data) {
        try {

            JSONArray jsonArray2 = new JSONObject(data).getJSONArray("ShipNameList");
            name = new String[jsonArray2.length()];
            for (int i = 0; i < jsonArray2.length(); i++) {
                name[i]= jsonArray2.getString(i);
            }
            JSONArray jsonArray3 = new JSONObject(data).getJSONArray("ShipOrderList");
            no = new String[jsonArray3.length()];
            for (int i = 0; i < jsonArray3.length(); i++) {
                no[i]= jsonArray3.getString(i);
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
        CompanyOk =true;
    }
    */

    private void init() {
        //finddata();
        finddata2();
        Bundle bundle = getIntent().getExtras();
        Type = bundle.getInt("Type");

        if (Type == 0) {
            preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        } else if (Type == 1) {
            preferences = getSharedPreferences("huo", Context.MODE_PRIVATE);
        }

        UserID =preferences.getString("UserId", null);

        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(TiXiangActivity.this, 1);
                myDialog.setContent("船名");
                myDialog.setDialogCallback(new MyDialog.Dialogcallback() {
                    @Override
                    public void dialogdo(String string) {
                        textViews[0].setText(string);
                    }
                });
                myDialog.sethineText("请输入船名");
                myDialog.show();
            }
        });
        views[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(TiXiangActivity.this, 1);
                myDialog.setContent("船次");
                myDialog.setDialogCallback(new MyDialog.Dialogcallback() {
                    @Override
                    public void dialogdo(String string) {
                        textViews[1].setText(string);
                    }
                });
                myDialog.sethineText("请输入船次");
                myDialog.show();
            }
        });
        views[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(TiXiangActivity.this, 1);
                myDialog.setContent("提单号");
                myDialog.setDialogCallback(new MyDialog.Dialogcallback() {
                    @Override
                    public void dialogdo(String string) {
                        textViews[2].setText(string);
                    }
                });
                myDialog.sethineText("请输入提单号");
                myDialog.show();

            }
        });
        views[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalParams.isNetworkAvailable(TiXiangActivity.this)) {
                    if (CompanOk) {
                        finddata1();
                        mydedialog("箱型", ContainerType, 3);
                        xiang = true;
                    } else {
                        MyToast.makeText(TiXiangActivity.this, "加载失败");
                    }
                } else {
                    MyToast.makeText(TiXiangActivity.this, "亲,网络未连接");
                }
            }
        });
//        views[4].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (xiang) {
//                    MyDialog myDialog = new MyDialog(TiXiangActivity.this, 1);
//                    myDialog.setContent("箱量");
//                    myDialog.setDialogCallback(new MyDialog.Dialogcallback() {
//                        @Override
//                        public void dialogdo(String string) {
//                            if (string.length() > 0 && string.matches("^[0-9]*$")) {
//                                Float price = Float.valueOf(textViews[5].getText().toString()) / Integer.valueOf(textViews[4].getText().toString());
//                                textViews[4].setText(string);
//                                float in = Integer.parseInt(string) * price;
//                                textViews[5].setText(String.valueOf(in));
//                            } else {
//                                MyToast.makeText(TiXiangActivity.this, "请输入数字");
//                            }
//
//                        }
//                    });
//                    myDialog.sethineText("请输入数字！");
//                    myDialog.show();
//                } else {
//                    MyToast.makeText(TiXiangActivity.this, "请先选择箱型");
//                }
//
//            }
//        });

        views[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mnumber = new String[Maxnumber];
                for(int i =0 ;i<Maxnumber;i++){
                    int J=i+1;
                    Mnumber[i]=String.valueOf(J);
                }
                if (MaxOK) {
                    mydedialog("数量", Mnumber, 4);
                    xiang = true;
                } else {
                    MyToast.makeText(TiXiangActivity.this, "您的最大放箱量为0！");
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
                    intent.setClass(TiXiangActivity.this, PrivilegeActivity.class);
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

                if(checkOrderData()){
                    finddata3();
                }

            }
        });


    }

    private void InitTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.title_tixiang);
        titleView.setBackImageVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setMiddleText(R.string.tixiang);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void findById() {
        views = new View[5];
        textViews = new TextView[6];

        views[0] =findViewById(R.id.T_A_ShipName);
        views[1] = findViewById(R.id.T_A_ShipOrder);
        views[2] = findViewById(R.id.T_A_BookingNumber);
        views[3] = findViewById(R.id.T_A_BoxType);
        views[4] = findViewById(R.id.T_A_Number);

        textViews[0] = (TextView) findViewById(R.id.T_ShipName);
        textViews[1] = (TextView) findViewById(R.id.T_ShipOrder);
        textViews[2] = (TextView) findViewById(R.id.T_BookingNumber);
        textViews[3] = (TextView) findViewById(R.id.T_BoxType);
        textViews[4] = (TextView) findViewById(R.id.T_Number);
        textViews[5] = (TextView) findViewById(R.id.T_Amount);
        Privilege = (CheckBox) findViewById(R.id.T_Privilege);
        Balance = (CheckBox) findViewById(R.id.T_Balance);
        payButton = (Button) findViewById(R.id.T_Button);
        containerTypeValueText = (TextView) findViewById(R.id.luo_ContainerType_value);


    }
    //获取船次
//    private void finddata() {
//        if(GlobalParams.isNetworkAvailable(TiXiangActivity.this)) {
//
//            JSONObject jsonObject = new JSONObject();
//            Log.d(TAG, jsonObject.toString());
//            new MyThread(Constant.URL_PostInitShip, handler1, jsonObject,TiXiangActivity.this).start();
//
//        }else{
//            MyToast.makeText(TiXiangActivity.this, "亲,网络未连接");
//        }
//
//    }

    //获取最大值
    private void finddata1() {
        if(GlobalParams.isNetworkAvailable(TiXiangActivity.this)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("UserID", UserID);
                jsonObject.put("Billoflading", textViews[2].getText().toString());
                jsonObject.put("Type", 1);
                new MyThread(Constant.URL_PaymentPostGetCount, handler2, jsonObject, TiXiangActivity.this).start();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            MyToast.makeText(TiXiangActivity.this, "亲,网络未连接");
        }

    }

    //获取箱型
    private void finddata2() {
        if (GlobalParams.isNetworkAvailable(TiXiangActivity.this)) {
            JSONObject jsonObject = new JSONObject();
            Log.d(TAG, jsonObject.toString());
            new MyThread(Constant.URL_PostHoldBoxList, handler, jsonObject, TiXiangActivity.this).start();
        } else {
            MyToast.makeText(TiXiangActivity.this, "亲,网络未连接");
        }

    }

    //提箱办理
    private void finddata3() {
        if (GlobalParams.isNetworkAvailable(TiXiangActivity.this)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("ShipNmae",textViews[0].getText().toString());
                jsonObject.put("ShipOrder",textViews[1].getText().toString());
                jsonObject.put("BillofLading",textViews[2].getText().toString());
                jsonObject.put("Amount",textViews[5].getText().toString());
                jsonObject.put("UseBalance",UseBalance);
                jsonObject.put("PrivilegeId",privilegeId);
                jsonObject.put("UserID",UserID);
                jsonObject.put("BoxType",Integer.valueOf(containerTypeValueText.getText().toString()));
                jsonObject.put("Number",Integer.valueOf(textViews[4].getText().toString()));

                Log.d(TAG, jsonObject.toString());
                new MyThread(Constant.URL_PaymentPostSuitcase, handler3, jsonObject, TiXiangActivity.this).start();
            } catch (JSONException e) {
            e.printStackTrace();
            }
        } else {
            MyToast.makeText(TiXiangActivity.this, "亲,网络未连接");
        }

    }

    private boolean checkOrderData() {
        if (TextUtils.isEmpty(textViews[0].getText())) {
            MyToast.makeText(this, R.string.cheming);
            return false;
        }
        if (TextUtils.isEmpty(containerTypeValueText.getText())) {
            MyToast.makeText(this, R.string.box_type_empty);
            return false;
        }
        if (TextUtils.isEmpty(textViews[1].getText())) {
            MyToast.makeText(this, R.string.chuanci);
            return false;
        }
        if (TextUtils.isEmpty(textViews[2].getText())) {
            MyToast.makeText(this, R.string.tidanhao);
            return false;
        }
        if (TextUtils.isEmpty(textViews[3].getText())) {
            MyToast.makeText(this, R.string.box_type_empty);
            return false;
        }
        if (TextUtils.isEmpty(textViews[4].getText())) {
            MyToast.makeText(this, R.string.box_number_empty);
            return false;
        }
        return true;
    }

    private void setTextViewsEmpty() {
        textViews[0].setText("");
        textViews[1].setText("");
        textViews[2].setText("");
        textViews[3].setText("");
        textViews[4].setText("");
        textViews[5].setText("");
        containerTypeValueText.setText("");
        Privilege.setChecked(false);
        Privilege.setText("使用优惠券");
        Balance.setChecked(false);
    }

    public void mydedialog(String string,final String[] strings, final int i){


        AlertDialog dialog= new  AlertDialog.Builder(TiXiangActivity.this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(string)
                .setSingleChoiceItems(strings, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "i=============" + which);
                                companyone=strings[which];
                                chick=true;

                                if (i == 3) {
                                    Oneprice=Float.valueOf(price[which]);
                                    containerTypeValueText.setText(String.valueOf(containerTypeValue[which]));
                                }
                                if (i == 4){
                                    float in = Integer.parseInt(companyone) * Oneprice;
                                    textViews[5].setText(String.valueOf(in));
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
                                    textViews[i].setText(strings[0]);

                                    if (i == 3) {
                                        Oneprice=Float.valueOf(price[0]);
                                        containerTypeValueText.setText(String.valueOf(containerTypeValue[0]));
                                    }
                                    if( i == 4){
                                        textViews[5].setText(String.valueOf(Oneprice));
                                    }

                                }
                                chick=false;
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("取消", null)
                .show();
        int no = strings.length;

        if(no>5){
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
