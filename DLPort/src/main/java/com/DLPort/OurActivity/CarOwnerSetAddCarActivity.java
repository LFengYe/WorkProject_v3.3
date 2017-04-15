package com.DLPort.OurActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.BaseActivity;
import com.DLPort.myactivity.NewsPublishActivty;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;
import com.DLPort.myview.MyDialog;
import com.DLPort.myview.TitleView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/14.
 */
public class CarOwnerSetAddCarActivity extends BaseActivity {
    private static final String TAG = "CarOwnerSetAddCar";

    private View[] views;
    private TextView[] textViews;
    private Button add;
    private int index;
    private SharedPreferences sp;
    private int ContainerType;

    private Handler handler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    JSONObject jsonUser = null;
                    jsonUser = new JSONObject((String) msg.obj);

                    Log.d(TAG, "Login的响应" + jsonUser.toString());
                    int status = jsonUser.getInt("Status");
                    Log.i(TAG, "状态" + status);
                    if (0 == status) {
                        MyToast.makeText(CarOwnerSetAddCarActivity.this, "增加成功");

                        finish();
                    } else if (1 == status || -1 == status) {
                        MyToast.makeText(CarOwnerSetAddCarActivity.this, jsonUser.getString("Message"));
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");

                    e.printStackTrace();
                }
            } else if (msg.what == 2) {
                Log.i(TAG, "响应不正常");

                MyToast.makeText(CarOwnerSetAddCarActivity.this, "服务器异常");
            } else if (msg.what == 3) {
                // 编码异常
                Log.i(TAG, "编码异常");
            } else if (msg.what == 4) {
                // ClientProtocolException
                Log.i(TAG, "ClientProtocolException");
            } else if (msg.what == 5) {
                // IO异常
                Log.i(TAG, "IO异常");

                MyToast.makeText(CarOwnerSetAddCarActivity.this, "服务器连接异常");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carowner_set_addcar);
        initTitle();
        findById();
        init();
    }

    private void findById() {
        views = new View[11];
        textViews = new TextView[11];
        views[0] = findViewById(R.id.add_A_VehNof);
        views[1] = findViewById(R.id.add_A_CarType);
        views[2] = findViewById(R.id.add_A_Tunnage);
        views[3] = findViewById(R.id.add_A_CarMessage);
        views[4] = findViewById(R.id.add_A_UpkeepTime);
        views[5] = findViewById(R.id.add_A_InsuranceTime);
        views[6] = findViewById(R.id.add_A_BoxType);
        views[7] = findViewById(R.id.add_A_GpsNo);
        views[8] = findViewById(R.id.add_A_GpsExpire);
        views[9] = findViewById(R.id.add_A_Driver);
        views[10] = findViewById(R.id.add_A_DriTel);


        textViews[0] = (TextView) findViewById(R.id.add_VehNof);
        textViews[1] = (TextView) findViewById(R.id.add_CarType);
        textViews[2] = (TextView) findViewById(R.id.add_Tunnage);
        textViews[3] = (TextView) findViewById(R.id.add_CarMessage);
        textViews[4] = (TextView) findViewById(R.id.add_UpkeepTime);
        textViews[5] = (TextView) findViewById(R.id.add_InsuranceTime);
        textViews[6] = (TextView) findViewById(R.id.add_BoxType);
        textViews[7] = (TextView) findViewById(R.id.add_GpsNo);
        textViews[8] = (TextView) findViewById(R.id.add_GpsExpire);
        textViews[9] = (TextView) findViewById(R.id.add_Driver);
        textViews[10] = (TextView) findViewById(R.id.add_DriTel);
        add = (Button) findViewById(R.id.add_Button);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    private void init() {

        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(CarOwnerSetAddCarActivity.this, 1);
                myDialog.setContent("车牌号");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("例如：辽123456");
                myDialog.show();
                index = 0;
            }
        });

        views[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(CarOwnerSetAddCarActivity.this, 1);
                myDialog.setContent("车辆类型");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("例如：货车");
                myDialog.show();
                index = 1;
            }
        });
        views[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(CarOwnerSetAddCarActivity.this, 1);
                myDialog.setContent("承载吨数");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.show();
                index = 2;
            }
        });
        views[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(CarOwnerSetAddCarActivity.this, 1);
                myDialog.setContent("挂车信息");
                myDialog.setDialogCallback(dialogcallback);

                myDialog.show();
                index = 3;
            }
        });
        views[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(CarOwnerSetAddCarActivity.this, 2);
                myDialog.setContent("保养日期");
                myDialog.setDialogCallback(dialogcallback);

                myDialog.show();
                index = 4;
            }
        });
        views[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(CarOwnerSetAddCarActivity.this, 2);
                myDialog.setContent("保险日期");
                myDialog.setDialogCallback(dialogcallback);

                myDialog.show();
                index = 5;
            }
        });
        views[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(CarOwnerSetAddCarActivity.this, 3);
                myDialog.setContent("承载最大箱型");
                myDialog.setDialogCallback(new MyDialog.Dialogcallback() {
                    @Override
                    public void dialogdo(String string) {
                        String s[] = string.split("-");
                        textViews[6].setText(s[0]);
                        ContainerType = Integer.parseInt(s[1]);
                        Log.d(TAG, "枚举类" + ContainerType);
                    }
                });
                myDialog.sethineText("例如：45GP");
                myDialog.show();
                index = 6;
            }
        });
        views[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(CarOwnerSetAddCarActivity.this, 1);
                myDialog.setContent("GPS编号");
                myDialog.setDialogCallback(dialogcallback);

                myDialog.show();
                index = 7;
            }
        });
        views[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(CarOwnerSetAddCarActivity.this, 2);
                myDialog.setContent("GPS到期时间");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.show();
                index = 8;
            }
        });
        views[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(CarOwnerSetAddCarActivity.this, 1);
                myDialog.setContent("司机");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.show();
                index = 9;
            }
        });
        views[10].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(CarOwnerSetAddCarActivity.this, 1);
                myDialog.setContent("司机联系方式");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.show();
                index = 10;
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (GlobalParams.isNetworkAvailable(CarOwnerSetAddCarActivity.this)) {
                    if (iskong()) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject = getData();
                        Log.d(TAG, jsonObject.toString());
                        new MyThread(Constant.URL_PostAddVehicle, handler, jsonObject,CarOwnerSetAddCarActivity.this).start();
                    } else {
                        MyToast.makeText(CarOwnerSetAddCarActivity.this, "信息有空值！！请补全后增加。");
                    }

                } else {

                    MyToast.makeText(CarOwnerSetAddCarActivity.this, "亲,网络未连接");

                }

            }
        });


    }

    MyDialog.Dialogcallback dialogcallback = new MyDialog.Dialogcallback() {
        @Override
        public void dialogdo(String string) {
            textViews[index].setText(string);
        }
    };

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.set_addcar_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.set);
        titleView.setMiddleText(R.string.addcar);
        View view = findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private JSONObject getData() {

        JSONObject json = new JSONObject();

        try {
            json.put("CarOwnerId", sp.getString("UserId", ""));
            Log.d(TAG, sp.getString("UserId", ""));
            json.put("VehNof", textViews[0].getText().toString());
            json.put("CarType", textViews[1].getText().toString());
            json.put("Tunnage", textViews[2].getText().toString());
            json.put("CarMessage", textViews[3].getText().toString());
            json.put("UpkeepTime", textViews[4].getText().toString());
            json.put("InsuranceTime", textViews[5].getText().toString());
            json.put("BoxType", ContainerType);
            json.put("GpsNo", textViews[7].getText().toString());
            json.put("GpsExpire", textViews[8].getText().toString());
            json.put("Driver", textViews[9].getText().toString());
            json.put("DriTel", textViews[10].getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    private boolean iskong() {
        boolean kong = true;
        for (int i = 0; i <= 10; i++) {
            if (textViews[i].getText().toString().equals("")) {
                kong = false;
                return kong;
            }
        }
        return kong;
    }
}
