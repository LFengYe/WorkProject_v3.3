package com.DLPort.myactivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

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
 * Created by Administrator on 2016/4/17.
 */
public class ShopReqActivity extends BaseActivity {
    private static final String TAG = "ShopReqActivity";
    private View[] views;
    private TextView[] textViews;
    private int index;
    private Button button;
    private String companyone;
    private boolean chick = false;
    private String[] company, name, db, no;
    private boolean CompanyOk = false;
    private Handler handler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    JSONObject jsonUser = null;
                    jsonUser = new JSONObject((String) msg.obj);

                    int status = jsonUser.getInt("Status");
                    if (0 == status) {
                        String data = jsonUser.getString("Data");
                        Intent intent = new Intent(ShopReqActivity.this, InquireActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putString("data", data);
                        Bundle bundle1 = getIntent().getExtras();
                        String type = bundle1.getString("Type");
                        bundle.putString("Type", type);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    } else if (1 == status || -1 == status) {

                        MyToast.makeText(ShopReqActivity.this, "查询失败");
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");

                    e.printStackTrace();
                }
            } else {
                Log.i(TAG, "响应不正常");
                MyToast.makeText(ShopReqActivity.this, "服务器异常");
            }
        }
    };
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
                        diaplaydata(data);

                    } else {
                        MyToast.makeText(ShopReqActivity.this, "加载失败");
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, msg.what + " 服务器异常");
                MyToast.makeText(ShopReqActivity.this, msg.what + " 服务器异常");
            }
        }
    };

    private void diaplaydata(String data) {


        try {
            JSONArray jsonArray = new JSONObject(data).getJSONArray("ShipCompanyList");
            company = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {

                company[i] = jsonArray.getString(i);
            }
            JSONArray jsonArray1 = new JSONObject(data).getJSONArray("DestinationPortList");
            db = new String[jsonArray1.length()];
            for (int i = 0; i < jsonArray1.length(); i++) {
                db[i] = jsonArray1.getString(i);
            }
            JSONArray jsonArray2 = new JSONObject(data).getJSONArray("ShipNameList");
            name = new String[jsonArray2.length()];
            for (int i = 0; i < jsonArray2.length(); i++) {
                name[i] = jsonArray2.getString(i);
            }
            JSONArray jsonArray3 = new JSONObject(data).getJSONArray("ShipOrderList");
            no = new String[jsonArray3.length()];
            for (int i = 0; i < jsonArray3.length(); i++) {
                no[i] = jsonArray3.getString(i);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        CompanyOk = true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ship_inquire);
        InitTitle();
        findById();
        init();


    }

    private void findById() {
        views = new View[7];
        textViews = new TextView[7];
        views[0] = findViewById(R.id.S_A_InPortTime);
        views[1] = findViewById(R.id.S_A_ShipCompany);
        views[2] = findViewById(R.id.S_A_DestinationPort);
        views[3] = findViewById(R.id.S_A_ShipName);
        views[4] = findViewById(R.id.S_A_ShipOrder);
//        views[5] = findViewById(R.id.S_A_ShipLine);
//        views[6] = findViewById(R.id.S_A_BookingNumber);

        textViews[0] = (TextView) findViewById(R.id.S_InPortTime);
        textViews[1] = (TextView) findViewById(R.id.S_ShipCompany);
        textViews[2] = (TextView) findViewById(R.id.S_DestinationPort);
        textViews[3] = (TextView) findViewById(R.id.S_ShipName);
        textViews[4] = (TextView) findViewById(R.id.S_ShipOrder);
//        textViews[5] = (TextView) findViewById(R.id.S_ShipLine);
//        textViews[6] = (TextView) findViewById(R.id.S_BookingNumber);
        button = (Button) findViewById(R.id.inquire_Button);

    }

    private void init() {
        finddata();
        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(ShopReqActivity.this, 2);
                myDialog.setContent("货物入港时间");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.show();
                index = 0;
            }
        });

        views[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalParams.isNetworkAvailable(ShopReqActivity.this)) {
                    if (CompanyOk) {
                        mydedialog("船务公司", company, 1);
                    } else {
                        MyToast.makeText(ShopReqActivity.this, "加载失败");
                    }
                } else {
                    MyToast.makeText(ShopReqActivity.this, "亲,网络未连接");
                }
            }

        });
        views[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalParams.isNetworkAvailable(ShopReqActivity.this)) {
                    if (CompanyOk) {
                        mydedialog("目的港", db, 2);
                    } else {
                        MyToast.makeText(ShopReqActivity.this, "加载失败");
                    }
                } else {
                    MyToast.makeText(ShopReqActivity.this, "亲,网络未连接");
                }
            }
        });
        views[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (GlobalParams.isNetworkAvailable(ShopReqActivity.this)) {
                    if (CompanyOk) {
                        mydedialog("船名", name, 3);
                    } else {
                        MyToast.makeText(ShopReqActivity.this, "加载失败");
                    }
                } else {
                    MyToast.makeText(ShopReqActivity.this, "亲,网络未连接");
                }

            }
        });
        views[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalParams.isNetworkAvailable(ShopReqActivity.this)) {
                    if (CompanyOk) {
                        mydedialog("船次", no, 4);
                    } else {
                        MyToast.makeText(ShopReqActivity.this, "加载失败");
                    }
                } else {
                    MyToast.makeText(ShopReqActivity.this, "亲,网络未连接");
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalParams.isNetworkAvailable(ShopReqActivity.this)) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject = getData();
                    new MyThread(Constant.URL_PostQuerySailSchedule, handler, jsonObject, ShopReqActivity.this).start();
                } else {
                    MyToast.makeText(ShopReqActivity.this, "亲,网络未连接");

                }

            }
        });

    }

    private void finddata() {
        if (GlobalParams.isNetworkAvailable(ShopReqActivity.this)) {

            JSONObject jsonObject = new JSONObject();
            Log.d(TAG, jsonObject.toString());
            new MyThread(Constant.URL_PostInitShip, handler1, jsonObject, ShopReqActivity.this).start();

        } else {
            MyToast.makeText(ShopReqActivity.this, "亲,网络未连接");
        }

    }

    MyDialog.Dialogcallback dialogcallback = new MyDialog.Dialogcallback() {
        @Override
        public void dialogdo(String string) {
            textViews[index].setText(string);
        }
    };

    private void InitTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.title_ship_inquire);
        titleView.setBackImageVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setMiddleText(R.string.O_pic1);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public JSONObject getData() {

        JSONObject json = new JSONObject();
        try {
            json.put("InPortTime", textViews[0].getText().toString());
            json.put("ShipCompany", textViews[1].getText().toString());
            json.put("DestinationPort", textViews[2].getText().toString());
            json.put("ShipName", textViews[3].getText().toString());
            json.put("ShipOrder", textViews[4].getText().toString());
//            json.put("ShipLine",textViews[5].getText().toString());
//            json.put("BookingNumber",textViews[6].getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    public void mydedialog(String string, final String[] strings, final int i) {


        AlertDialog dialog = new AlertDialog.Builder(ShopReqActivity.this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(string)
                .setSingleChoiceItems(strings, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "i=============" + which);
                                companyone = strings[which];
                                chick = true;
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


}
