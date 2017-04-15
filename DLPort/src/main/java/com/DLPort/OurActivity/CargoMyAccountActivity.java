package com.DLPort.OurActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.BaseActivity;
import com.DLPort.myactivity.HuoOrderActivity;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;
import com.DLPort.myview.TitleView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fuyzh on 16/5/18.
 */
public class CargoMyAccountActivity extends BaseActivity {
    private static final String TAG = "CargoMyAccountActivity";

    private TextView username;
    private TextView contact;
    private TextView phoneNumber;
    private TextView address;
    private TextView goodsTran;


    private Handler handler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    JSONObject jsonUser = null;
                    jsonUser = new JSONObject((String) msg.obj);
                    Log.d(TAG, "响应数据:" + jsonUser.toString());
                    int status = jsonUser.getInt("Status");
                    if (0 == status) {
                        progressListData(jsonUser.get("Data").toString());
                    }else if(1 == status||-1==status) {
                        MyToast.makeText(CargoMyAccountActivity.this, jsonUser.getString("Message"));
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            }else if (msg.what == 2){
                Log.i(TAG, "响应不正常");

                MyToast.makeText(CargoMyAccountActivity.this, "服务器异常");
            }else if (msg.what == 3) {
                // 编码异常
                Log.i(TAG, "编码异常");
            } else if (msg.what == 4) {
                // ClientProtocolException
                Log.i(TAG, "ClientProtocolException");
            } else if (msg.what == 5) {
                // IO异常
                Log.i(TAG, "IO异常");
                MyToast.makeText(CargoMyAccountActivity.this, "服务器连接异常");
            }
        }

        private void progressListData(String jsonData) {
            try {
                JSONObject object = new JSONObject(jsonData);
                username.setText(object.getString("UserName"));
                contact.setText(object.getString("AccountName"));
                phoneNumber.setText(object.getString("Telephoen"));
                address.setText(object.getString("Address"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account_cargo);
        initTitle();
        initView();
        getMyAccountCarGo();
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.account_cargo_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.My);
        titleView.setMiddleText(R.string.account);
        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        username = (TextView) findViewById(R.id.account_username);
        contact = (TextView) findViewById(R.id.account_contact);
        phoneNumber = (TextView) findViewById(R.id.account_phone_number);
        address = (TextView) findViewById(R.id.account_address);
        goodsTran = (TextView) findViewById(R.id.account_goods_tran);
        goodsTran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CargoMyAccountActivity.this, HuoOrderActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getMyAccountCarGo() {
        SharedPreferences sp = getSharedPreferences("huo", Context.MODE_PRIVATE);
        if(GlobalParams.isNetworkAvailable(CargoMyAccountActivity.this)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Id", sp.getString("UserId", null));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new MyThread(Constant.URL_CargoOwnerPostGetAccount, handler, jsonObject,this).start();
        }else{
            MyToast.makeText(CargoMyAccountActivity.this, "亲,网络未连接");

        }
    }
}
