package com.DLPort.myactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;
import com.DLPort.myview.TitleView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/12.
 */
public class OrderParticular extends BaseActivity {
    private static final String TAG = "OrderParticular";
    private TextView[] textViews;
    private String OrderId;
    private Button button1, button2;

    private int OrderStatus = 0;

    private MyHandler handler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    JSONObject jsonUser = new JSONObject((String) msg.obj);
                    Log.d(TAG, "Login的响应" + jsonUser.toString());
                    int status = jsonUser.getInt("Status");
                    Log.i(TAG, "状态" + status);
                    if (0 == status) {
                        String data = jsonUser.getString("Data");
                        JSONObject jsondata = new JSONObject(data);
                        display(jsondata);
                    } else if (1 == status || -1 == status) {
                        Toast.makeText(OrderParticular.this, jsonUser.getString("Message"), Toast.LENGTH_SHORT)
                                .show();
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            } else if (msg.what == 2) {
                Log.i(TAG, "响应不正常");

                Toast.makeText(OrderParticular.this, "服务器异常", Toast.LENGTH_SHORT)
                        .show();
            } else if (msg.what == 3) {
                // 编码异常
                Log.i(TAG, "编码异常");
            } else if (msg.what == 4) {
                // ClientProtocolException
                Log.i(TAG, "ClientProtocolException");
            } else if (msg.what == 5) {
                // IO异常
                Log.i(TAG, "IO异常");
                Toast.makeText(OrderParticular.this, "服务器连接异常",
                        Toast.LENGTH_SHORT).show();
            }

        }

    };

    private MyHandler handler2 = new MyHandler(this) {
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
                        finish();
                    }
                    MyToast.makeText(OrderParticular.this, jsonUser.getString("Message"));
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");

                    e.printStackTrace();
                }
            } else if (msg.what == 2) {
                Log.i(TAG, "响应不正常");

                MyToast.makeText(OrderParticular.this, "服务器异常");
            } else if (msg.what == 3) {
                // 编码异常
                Log.i(TAG, "编码异常");
            } else if (msg.what == 4) {
                // ClientProtocolException
                Log.i(TAG, "ClientProtocolException");
            } else if (msg.what == 5) {
                // IO异常
                Log.i(TAG, "IO异常");

                MyToast.makeText(OrderParticular.this, "服务器连接异常");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_particular);
        initTitle();
        findById();
        init();
    }

    private void init() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        OrderId = bundle.getString("ID");
        getData(Constant.URL_PostGetOrderDetail, handler);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(Constant.URL_PostOrderArrival, handler2);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(Constant.URL_PostCancelOrder, handler2);
                Log.d(TAG, Constant.URL_PostCancelOrder);
            }
        });

    }

    private void findById() {
        textViews = new TextView[16];
        textViews[0] = (TextView) findViewById(R.id.Par_Price);
        textViews[1] = (TextView) findViewById(R.id.Par_OrderStatus);
        textViews[2] = (TextView) findViewById(R.id.Par_ChargeType);
        textViews[3] = (TextView) findViewById(R.id.Par_Principal);
        textViews[4] = (TextView) findViewById(R.id.Par_Telephoen);
        textViews[5] = (TextView) findViewById(R.id.Par_SuitCaseNo);
        textViews[6] = (TextView) findViewById(R.id.Par_LoadTime);
        textViews[7] = (TextView) findViewById(R.id.Par_StartAddress);
        textViews[8] = (TextView) findViewById(R.id.Par_Destination);
        textViews[9] = (TextView) findViewById(R.id.Par_Kilometre);
        textViews[10] = (TextView) findViewById(R.id.Par_CreateTime);
        textViews[11] = (TextView) findViewById(R.id.Par_CarNo);
        textViews[12] = (TextView) findViewById(R.id.Par_ContainerType);

        textViews[13] = (TextView) findViewById(R.id.Par_PutBoxId);
        textViews[14] = (TextView) findViewById(R.id.Par_PresentNumber);
        button1 = (Button) findViewById(R.id.Par_go_button);
        button2 = (Button) findViewById(R.id.Par_chexiao);
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.Par_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.my_order);
        titleView.setMiddleText(R.string.order_par);
        View view = findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getData(String URL, Handler handler) {
        if (GlobalParams.isNetworkAvailable(OrderParticular.this)) {

            JSONObject json = new JSONObject();
            try {
                json.put("OrderId", OrderId);
                Log.d(TAG, json.toString());
                new MyThread(URL, handler, json, OrderParticular.this).start();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(OrderParticular.this, "亲,请连接网络！！！", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    private void display(JSONObject json) {
        try {
            textViews[0].setText(json.getString("Price"));
            switch (json.getInt("OrderStatus")) {
                case 0:
                    textViews[1].setText("进行中");
                    break;
                case 1:
                    textViews[1].setText("已完成");

                    break;
                case 2:
                    textViews[1].setText("申请取消");

                    break;
                case 3:
                    textViews[1].setText("已取消");

                    break;
                case 4:
                    textViews[1].setText("拒绝取消");

                    break;
                case 5:
                    textViews[1].setText("同意取消");
            }
            if (json.getInt("OrderStatus") == 3 || json.getInt("OrderStatus") == 1) {
                button1.setVisibility(View.GONE);
                button2.setVisibility(View.GONE);
            }
            switch (json.getInt("ChargeType")) {
                case 0:
                    textViews[2].setText("平台结费");
                    break;
                case 1:
                    textViews[2].setText("自行结费");
            }
            textViews[3].setText(json.getString("Principal"));
            textViews[4].setText(json.getString("Telephoen"));

            textViews[5].setText(json.getString("PresentNumber"));

            String LoadTime = json.getString("LoadTime");
            String[] Str = LoadTime.split("T");
            textViews[6].setText(Str[0] + " " + Str[1]);

            textViews[7].setText(json.getString("StartAddress"));
            textViews[8].setText(json.getString("Destination"));
            textViews[9].setText(json.getString("Kilometre"));


            String CreateTime = json.getString("CreateTime");
            String[] Str1 = CreateTime.split("T");
            textViews[10].setText(Str1[0] + " " + Str1[1]);
            textViews[11].setText(json.getString("CarNo"));
            String s = GlobalParams.GetContainerType(json.getInt("ContainerType"));
            textViews[12].setText(s);
            textViews[13].setText(json.getString("PutBoxId"));
            textViews[14].setText(json.getString("SuitCaseNo"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
