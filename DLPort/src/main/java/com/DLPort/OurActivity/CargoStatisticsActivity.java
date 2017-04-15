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
public class CargoStatisticsActivity extends BaseActivity {
    private static final String TAG = "CargoStatistics";

    private RelativeLayout rechargeLayout;

    private TextView delivery;
    private TextView cost;
    private TextView integral;
    private TextView TotalBalance;
    private TextView MaximumAmount;

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
                        MyToast.makeText(CargoStatisticsActivity.this, jsonUser.getString("Message"));
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            }else if (msg.what == 2){
                Log.i(TAG, "响应不正常");

                MyToast.makeText(CargoStatisticsActivity.this, "服务器异常");
            }else if (msg.what == 3) {
                // 编码异常
                Log.i(TAG, "编码异常");
            } else if (msg.what == 4) {
                // ClientProtocolException
                Log.i(TAG, "ClientProtocolException");
            } else if (msg.what == 5) {
                // IO异常
                Log.i(TAG, "IO异常");
                MyToast.makeText(CargoStatisticsActivity.this, "服务器连接异常");
            }
        }

        private void progressListData(String jsonData) {
            try {
                JSONObject object = new JSONObject(jsonData);
                Log.d(TAG,"======="+object.getString("Integral"));
                delivery.setText(object.getString("Number") + getResources().getString(R.string.times));
                cost.setText(object.getString("Pay") + getResources().getString(R.string.Yuan));
                integral.setText(object.getString("Integral") + getResources().getString(R.string.score));
                TotalBalance.setText(object.getString("TotalBalance")+"元");
                MaximumAmount.setText(object.getString("MaximumAmount")+"元");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_statistics);
        initTitle();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCarGoStatistics();
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.statistics_cargo_title);
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
        delivery = (TextView) findViewById(R.id.statistics_delivery);
        cost = (TextView) findViewById(R.id.statistics_cost);
        integral = (TextView) findViewById(R.id.statistics_integral);
        TotalBalance = (TextView) findViewById(R.id.TotalBalance);
        MaximumAmount = (TextView)findViewById(R.id.MaximumAmount);

        rechargeLayout = (RelativeLayout) findViewById(R.id.statistics_recharge_layout);
        rechargeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("Type", 1);//用户类型为货主
                intent.putExtras(bundle);
                intent.setClass(CargoStatisticsActivity.this, RechargeInfoWriteActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getCarGoStatistics() {
        SharedPreferences sp = getSharedPreferences("huo", Context.MODE_PRIVATE);
        if(GlobalParams.isNetworkAvailable(CargoStatisticsActivity.this)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Id", sp.getString("UserId", ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new MyThread(Constant.URL_CargoOwnerPostStatistics, handler, jsonObject,this).start();
        }else{
            MyToast.makeText(CargoStatisticsActivity.this, "亲,网络未连接");

        }
    }
}
