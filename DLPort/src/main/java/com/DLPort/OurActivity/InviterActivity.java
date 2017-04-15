package com.DLPort.OurActivity;

import android.content.Context;
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
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;
import com.DLPort.myview.TitleView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fuyzh on 16/5/18.
 * 邀请活动
 */
public class InviterActivity extends BaseActivity {
    private static final String TAG = "InviterActivity";

    private TextView inviterContent;

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
                        progressData(jsonUser.getString("Data"));
                    }else if(1 == status||-1==status) {
                        MyToast.makeText(InviterActivity.this, jsonUser.getString("Message"));
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            }else if (msg.what == 2){
                Log.i(TAG, "响应不正常");

                MyToast.makeText(InviterActivity.this, "服务器异常");
            }else if (msg.what == 3) {
                // 编码异常
                Log.i(TAG, "编码异常");
            } else if (msg.what == 4) {
                // ClientProtocolException
                Log.i(TAG, "ClientProtocolException");
            } else if (msg.what == 5) {
                // IO异常
                Log.i(TAG, "IO异常");
                MyToast.makeText(InviterActivity.this, "服务器连接异常");
            }
        }

        private void progressData(String jsonData) {
            try {
                JSONObject object = new JSONObject(jsonData);
                inviterContent.setText(object.getString("InviterContent"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inviter);
        initTitle();
        initView();
        getInviterContent();
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.inviter_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.My);
        titleView.setMiddleText(R.string.inviter);
        View view = findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        inviterContent = (TextView) findViewById(R.id.inviter_content);
    }

    private void getInviterContent() {
        SharedPreferences sp = null;
        Bundle bundle = getIntent().getExtras();
        int type = bundle.getInt("Type");
        if (type == 0)
            sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        if (type == 1)
            sp = getSharedPreferences("huo", Context.MODE_PRIVATE);

        if (GlobalParams.isNetworkAvailable(InviterActivity.this)) {
            if (null != sp)
//                new MyThread(Constant.URL_UserPostinviter, handler, null, sp.getString("Token", null)).start();
                new MyThread(Constant.URL_UserPostinviter, handler, null, InviterActivity.this).start();
        } else {
            MyToast.makeText(InviterActivity.this, "亲,网络未连接");

        }
    }
}
