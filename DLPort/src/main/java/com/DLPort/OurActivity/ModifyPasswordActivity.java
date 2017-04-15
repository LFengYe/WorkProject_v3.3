package com.DLPort.OurActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
 * Created by fuyzh on 16/5/23.
 */
public class ModifyPasswordActivity extends BaseActivity {
    private static final String TAG = "ModifyPassword";

    private EditText oldPassword;
    private EditText newPassword;
    private EditText confirmPassword;
    private Button modifyBtn;

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
                    }
                    MyToast.makeText(ModifyPasswordActivity.this, jsonUser.getString("Message"));
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            } else if (msg.what == 2) {
                Log.i(TAG, "响应不正常");

                MyToast.makeText(ModifyPasswordActivity.this, "服务器异常");
            } else if (msg.what == 3) {
                // 编码异常
                Log.i(TAG, "编码异常");
            } else if (msg.what == 4) {
                // ClientProtocolException
                Log.i(TAG, "ClientProtocolException");
            } else if (msg.what == 5) {
                // IO异常
                Log.i(TAG, "IO异常");
                MyToast.makeText(ModifyPasswordActivity.this, "服务器连接异常");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        initTitle();
        initView();
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.modify_password_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.user_info);
        titleView.setMiddleText(R.string.my_set_info_modify_password);
        View view = findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        final Bundle bundle = getIntent().getExtras();
        if (bundle.getInt("Type") == 0)
            preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        if (bundle.getInt("Type") == 1)
            preferences = getSharedPreferences("huo", Context.MODE_PRIVATE);

        oldPassword = (EditText) findViewById(R.id.old_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
        modifyBtn = (Button) findViewById(R.id.modify_btn);
        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    modifyPassword(oldPassword.getText().toString(),
                            newPassword.getText().toString(), bundle.getInt("Type"));
                }
            }
        });
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(oldPassword.getText().toString().trim())) {
            MyToast.makeText(this, R.string.my_set_info_old_empty);
            return false;
        }
        if (TextUtils.isEmpty(newPassword.getText().toString().trim())) {
            MyToast.makeText(this, R.string.my_set_info_new_empty);
            return false;
        }
        if (TextUtils.isEmpty(confirmPassword.getText().toString().trim())) {
            MyToast.makeText(this, R.string.my_set_info_confirm_empty);
            return false;
        }
        if (!newPassword.getText().toString().equalsIgnoreCase(confirmPassword.getText().toString())) {
            MyToast.makeText(this, R.string.my_set_info_new_confirm_diff);
            return false;
        }

        return true;
    }

    private void modifyPassword(String oldPass, String newPass, int type) {
        if (null == preferences) {
            if (type == 0)
                preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
            if (type == 1)
                preferences = getSharedPreferences("huo", Context.MODE_PRIVATE);
        }

        if (GlobalParams.isNetworkAvailable(ModifyPasswordActivity.this)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("LoginName", preferences.getString("LoginName", null));
                jsonObject.put("PassWork", oldPass);
                jsonObject.put("NewPwd", newPass);
                jsonObject.put("UpdateType", type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new MyThread(Constant.URL_UserPostUpdatePwd, handler, jsonObject,ModifyPasswordActivity.this).start();
        } else {
            MyToast.makeText(ModifyPasswordActivity.this, "亲,网络未连接");

        }
    }
}
