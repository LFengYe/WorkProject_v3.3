package cn.com.caronwer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.caronwer.R;
import cn.com.caronwer.base.BaseActivity;
import cn.com.caronwer.base.Contants;
import cn.com.caronwer.bean.UserInfo;
import cn.com.caronwer.util.SPtils;

public class SettingActivity extends BaseActivity {


    private RelativeLayout rl_account_management;
    private RelativeLayout rl_fee_scale;
    private RelativeLayout rl_faq;
    private RelativeLayout rl_feed_back;
    private RelativeLayout rl_about_us;
    private CheckBox cb_vibrates;
    private CheckBox cb_sound;
    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private UserInfo userInfo;
    private boolean isChange;
    private TextView mTv_sji;
    private Button mBt_exit_login;
    private SharedPreferences mPrefs;

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

        mTv_sji = (TextView) findViewById(R.id.tv_sji);
        mBt_exit_login = (Button) findViewById(R.id.bt_exit_out);
    }

    @Override
    protected void initData() {
        isChange = false;
        userInfo = getIntent().getParcelableExtra("userInfo");
    }

    @Override
    protected void initView() {
        tv_right.setVisibility(View.GONE);
        tv_title.setText(R.string.setting);
        rl_account_management.setOnClickListener(this);
        mBt_exit_login.setOnClickListener(this);
        rl_fee_scale.setOnClickListener(this);
        rl_feed_back.setOnClickListener(this);
        rl_faq.setOnClickListener(this);
        rl_about_us.setOnClickListener(this);
        iv_left_white.setOnClickListener(this);
        cb_vibrates.setOnClickListener(this);
        cb_sound.setOnClickListener(this);
        cb_vibrates.setChecked(SPtils.getBoolean(SettingActivity.this, "vibrates", true));
        cb_sound.setChecked(SPtils.getBoolean(SettingActivity.this,"sound",true));
        SharedPreferences prefs = getSharedPreferences(Contants.SHARED_NAME, MODE_PRIVATE);
        String account = prefs.getString("account", "");
        String password = prefs.getString("password", "");
        mTv_sji.setText(account);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_left_white:
                if (isChange) {
                    getIntent().putExtra("userInfo", userInfo);
                    setResult(RESULT_OK, getIntent());
                }
                finish();
                break;
            case R.id.rl_account_management://修改密码
                intent.setClass(SettingActivity.this, XiuMimaPsdActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_fee_scale://收费标准++
                intent.setClass(SettingActivity.this, FeeScaleActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_faq://常见问题++
                intent.setClass(SettingActivity.this, FAQActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_feed_back://建议++
                intent.setClass(SettingActivity.this, FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_about_us://关于我们++
                intent.setClass(SettingActivity.this, AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_exit_out:
                mPrefs = getSharedPreferences(Contants.SHARED_NAME, MODE_PRIVATE);
                mPrefs.edit().putString("account", "").commit();
                mPrefs.edit().putString("password", "").commit();
                startActivity(new Intent(SettingActivity.this,MainActivity.class));
                finish();
                break;
            case R.id.cb_sound://声音提醒
                if (cb_sound.isChecked()){
                    SPtils.putBoolean(SettingActivity.this,"sound",true);
                    cb_sound.setChecked(true);
                }else {
                    SPtils.putBoolean(SettingActivity.this,"sound",false);
                    cb_sound.setChecked(false);
                }
                break;
            case R.id.cb_vibrates://整动提醒
                if (cb_vibrates.isChecked()){
                    cb_vibrates.setChecked(true);
                    SPtils.putBoolean(SettingActivity.this,"vibrates",true);
                }else {
                    SPtils.putBoolean(SettingActivity.this,"vibrates",false);
                    cb_vibrates.setChecked(false);
                }
                break;

        }
    }


    @Override
    public void onBackPressed() {
        if (isChange) {
            getIntent().putExtra("userInfo", userInfo);
            setResult(RESULT_OK, getIntent());
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            isChange = true;
            userInfo = data.getParcelableExtra("userInfo");
        }
    }
}
