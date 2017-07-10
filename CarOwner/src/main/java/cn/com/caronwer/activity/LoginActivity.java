package cn.com.caronwer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.Set;

import cn.com.caronwer.R;
import cn.com.caronwer.base.BaseActivity;
import cn.com.caronwer.base.Contants;
import cn.com.caronwer.bean.UserInfo;
import cn.com.caronwer.util.DateUtil;
import cn.com.caronwer.util.EncryptUtil;
import cn.com.caronwer.util.HttpUtil;
import cn.com.caronwer.util.MD5Util;
import cn.com.caronwer.util.VolleyInterface;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


public class LoginActivity extends BaseActivity {

    private SharedPreferences prefs;

    private String password;
    private String account;


    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void findById() {
    }

    @Override
    protected void initData() {
        prefs = getSharedPreferences(Contants.SHARED_NAME, MODE_PRIVATE);
        account = prefs.getString("account", "");
        password = prefs.getString("password", "");
    }

    @Override
    protected void initView() {
        login();
    }

    @Override
    public void onClick(View v) {


    }

    private void login() {
        final long startTime = System.currentTimeMillis();//获取当前系统的时间
        String time = DateUtil.getPsdCurrentDate();
        if (account.isEmpty() || password.isEmpty()) {
            showShortToastByString("未登录");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    intent.putExtra("userInfo", "");
                    intent.putExtra("isDenglu", false);
                    startActivity(intent);
                    finish();
                }
            }, 1500);
            return;
        }

        String finalPassword = MD5Util.encrypt(time + password);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("LoginName", account);
        jsonObject.addProperty("Time", time);
        jsonObject.addProperty("Password", finalPassword);


        jsonObject.addProperty("UserType", 2);//货主

        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(LoginActivity.this, Contants.url_userLogin, "login", map, new VolleyInterface(LoginActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
//                customProgressDialog.dismiss();
                showShortToastByString("登录成功");
                Gson gson = new Gson();
                final UserInfo userInfo = gson.fromJson(result, UserInfo.class);
                SharedPreferences.Editor editor = prefs.edit();
                Contants.userId = userInfo.getUserId();
                editor.putString("account", account);
                editor.putString("password", password);
                editor.putString("UserId", userInfo.getUserId());
                editor.putString("VehicleNo", userInfo.getVehicleNo());
                editor.commit();

                JPushInterface.setAliasAndTags(LoginActivity.this, account, null, new TagAliasCallback() {
                    @Override
                    public void gotResult(int arg0, String arg1, Set<String> arg2) {
                    }
                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        intent.putExtra("userInfo", userInfo);
                        intent.putExtra("isDenglu", true);
                        startActivity(intent);
                        finish();
                    }
                }, 1500);
            }

            @Override
            public void onError(VolleyError error) {
//                showShortToastByString("未登录");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        intent.putExtra("userInfo", "");
                        intent.putExtra("isDenglu", false);
                        startActivity(intent);
                        finish();
                    }
                }, 1500);
            }

            @Override
            public void onStateError(int sta, String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    showShortToastByString(msg);
                }
//                customProgressDialog.dismiss();

            }
        });
        /*
        long endTime = System.currentTimeMillis();
        long timeUsed = endTime - startTime;
        if (timeUsed < 2500) {
            // 强制休眠一段时间,保证闪屏页展示2秒钟
            try {
                Thread.sleep(2500 - timeUsed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}
