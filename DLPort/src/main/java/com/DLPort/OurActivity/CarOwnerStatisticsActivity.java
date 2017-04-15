package com.DLPort.OurActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.BaseActivity;
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
public class CarOwnerStatisticsActivity extends BaseActivity {
    private static final String TAG = "CarOwnerStatistics";

    private RelativeLayout pickUpCashLayout;
    private RelativeLayout rechargeLayout;

    private TextView number;
    private TextView pay;
    private TextView integral;
    private TextView pickUpCash;

    private Float payValue;
    private SharedPreferences preferences;

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
                        MyToast.makeText(CarOwnerStatisticsActivity.this, jsonUser.getString("Message"));
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            }else if (msg.what == 2){
                Log.i(TAG, "响应不正常");

                MyToast.makeText(CarOwnerStatisticsActivity.this, "服务器异常");
            }else if (msg.what == 3) {
                // 编码异常
                Log.i(TAG, "编码异常");
            } else if (msg.what == 4) {
                // ClientProtocolException
                Log.i(TAG, "ClientProtocolException");
            } else if (msg.what == 5) {
                // IO异常
                Log.i(TAG, "IO异常");
                MyToast.makeText(CarOwnerStatisticsActivity.this, "服务器连接异常");
            }
        }

        private void progressListData(String jsonData) {
            try {
                JSONObject object = new JSONObject(jsonData);
                number.setText(object.getString("Number") + getResources().getString(R.string.times));
                pay.setText(object.getString("Pay") + getResources().getString(R.string.Yuan));
                integral.setText(object.getString("Integral") + getResources().getString(R.string.score));
                pickUpCash.setText(object.getString("MaximumAmount") + getResources().getString(R.string.Yuan));

                payValue = Float.valueOf(object.getString("MaximumAmount"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carowner_statistics);
        initTitle();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCarOwnerStatistics();
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.statistics_car_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.My);
        titleView.setMiddleText(R.string.statistics);
        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        number = (TextView) findViewById(R.id.statistics_number);
        pay = (TextView) findViewById(R.id.statistics_pay);
        integral = (TextView) findViewById(R.id.statistics_integral);
        pickUpCash = (TextView) findViewById(R.id.statistics_pickup_cash);

        pickUpCashLayout = (RelativeLayout) findViewById(R.id.statistics_pickup_cash_layout);
        pickUpCashLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == preferences)
                    preferences = getSharedPreferences("user", Context.MODE_PRIVATE);

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("UserId", preferences.getString("UserId", ""));
                Log.i(TAG, "payValue:" + payValue);
                bundle.putFloat("PickUpValue", payValue);
                intent.putExtras(bundle);
                intent.setClass(CarOwnerStatisticsActivity.this, CarOwnerPickUpCash.class);
                startActivity(intent);
            }
        });

        rechargeLayout = (RelativeLayout) findViewById(R.id.statistics_recharge_layout);
        rechargeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("Type", 0);//用户类型为车主
                intent.putExtras(bundle);
                intent.setClass(CarOwnerStatisticsActivity.this, RechargeInfoWriteActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getCarOwnerStatistics() {
        if (null == preferences)
            preferences = getSharedPreferences("user", Context.MODE_PRIVATE);

        if(GlobalParams.isNetworkAvailable(CarOwnerStatisticsActivity.this)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("UserId", preferences.getString("UserId", null));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new MyThread(Constant.URL_CarOwnerPostStatistics, handler, jsonObject,CarOwnerStatisticsActivity.this).start();
        }else{
            MyToast.makeText(CarOwnerStatisticsActivity.this, "亲,网络未连接");

        }
    }
}
