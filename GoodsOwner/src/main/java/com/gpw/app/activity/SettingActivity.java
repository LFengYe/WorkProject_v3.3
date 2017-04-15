package com.gpw.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import com.gpw.app.R;
import com.gpw.app.base.BaseActivity;
import com.gpw.app.base.BaseApplication;
import com.gpw.app.base.Contants;
import com.gpw.app.util.EncryptUtil;
import com.gpw.app.util.HttpUtil;
import com.gpw.app.util.LogUtil;
import com.gpw.app.util.NetworkUtil;
import com.gpw.app.util.VolleyInterface;
import com.gpw.app.view.MyDialog;

public class SettingActivity extends BaseActivity {


    private RelativeLayout rl_account_management;
    private RelativeLayout rl_fee_scale;
    private RelativeLayout rl_faq;
    private RelativeLayout rl_feed_back;
    private RelativeLayout rl_about_us;
    private CheckBox cb_vibrates;
    private CheckBox cb_sound;
    private boolean IsVibrates;
    private boolean IsSound;
    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private MyDialog psdDialog;
    private SharedPreferences prefs;
    private Button bt_exit_login;



    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);
        rl_account_management = (RelativeLayout) findViewById(R.id.rl_account_management);
        rl_fee_scale = (RelativeLayout) findViewById(R.id.rl_fee_scale);
        rl_feed_back = (RelativeLayout) findViewById(R.id.rl_feed_back);
        rl_faq = (RelativeLayout) findViewById(R.id.rl_faq);
        rl_about_us = (RelativeLayout) findViewById(R.id.rl_about_us);
        cb_vibrates = (CheckBox) findViewById(R.id.cb_vibrates);
        cb_sound = (CheckBox) findViewById(R.id.cb_sound);
        bt_exit_login = (Button) findViewById(R.id.bt_exit_login);
    }

    @Override
    protected void initData() {
        prefs = getSharedPreferences(Contants.SHARED_NAME, MODE_PRIVATE);
        IsVibrates = prefs.getBoolean("IsVibrates", true);
        IsSound = prefs.getBoolean("IsSound",true);

    }

    @Override
    protected void initView() {
        tv_right.setVisibility(View.GONE);
        tv_title.setText(R.string.setting);
        rl_account_management.setOnClickListener(this);
        rl_fee_scale.setOnClickListener(this);
        rl_feed_back.setOnClickListener(this);
        rl_faq.setOnClickListener(this);
        rl_about_us.setOnClickListener(this);
        iv_left_white.setOnClickListener(this);
        bt_exit_login.setOnClickListener(this);
        cb_vibrates.setChecked(IsVibrates);
        cb_sound.setChecked(IsSound);
        final SharedPreferences.Editor editor = prefs.edit();
        cb_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("IsSound", isChecked);
                editor.apply();
            }
        });

        cb_vibrates.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("IsVibrates", isChecked);
                editor.apply();
            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_left_white:
                finish();
                break;
            case R.id.rl_account_management:
                psdDialog = MyDialog.psdDialog(SettingActivity.this);
                psdDialog.show();
                psdDialog.setOnSettingListener(new MyDialog.PsdListener() {
                    @Override
                    public void onSetting(String old, String new1, String new2) {
                        if (old.isEmpty() || new1.isEmpty() || new2.isEmpty()) {
                            showShortToastByString(getString(R.string.pas_null));
                            return;
                        }
                        if (new1.length()<6||new1.length()>16){
                            showShortToastByString(getString(R.string.psd_error));
                            return;
                        }
                        if (!new1.equals(new2)) {
                            showShortToastByString(getString(R.string.psd_no));
                            return;
                        }
                        if (!NetworkUtil.isConnected(SettingActivity.this)) {
                            showShortToastByString(getString(R.string.Neterror));
                        }

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("Tel",Contants.Tel);
                        jsonObject.addProperty("PassWord", old);
                        jsonObject.addProperty("UserType", 1);
                        jsonObject.addProperty("NewPwd", new1);
                        LogUtil.i(jsonObject.toString());
                        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());

                        HttpUtil.doPost(SettingActivity.this, Contants.url_editPassWord, "editPassWord", map, new VolleyInterface(SettingActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                            @Override
                            public void onSuccess(JsonElement result) {
                                LogUtil.i(result.toString());
                                showShortToastByString(result.toString());
                                psdDialog.dismiss();

                            }

                            @Override
                            public void onError(VolleyError error) {
                                showShortToastByString(getString(R.string.timeoutError));
                            }

                            @Override
                            public void onStateError() {
                            }
                        });
                    }
                });
                break;
            case R.id.rl_fee_scale:
                intent.setClass(SettingActivity.this, FeeScaleActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_faq:
                intent.setClass(SettingActivity.this, FAQActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_feed_back:
                intent.setClass(SettingActivity.this, FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_about_us:
                intent.setClass(SettingActivity.this, AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_exit_login:
                setResult(RESULT_OK,getIntent());
                finish();
                break;

        }
    }


}
