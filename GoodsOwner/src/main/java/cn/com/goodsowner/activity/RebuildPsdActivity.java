package cn.com.goodsowner.activity;


import android.os.CountDownTimer;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import cn.com.goodsowner.base.BaseActivity;
import cn.com.goodsowner.base.Contants;
import cn.com.goodsowner.util.EncryptUtil;
import cn.com.goodsowner.util.HttpUtil;
import cn.com.goodsowner.util.LogUtil;
import cn.com.goodsowner.util.VolleyInterface;


public class RebuildPsdActivity extends BaseActivity {



    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private EditText et_account;
    private EditText et_password;
    private TextView get_validate_code;
    private EditText et_validate_code;
    private CheckBox cb_eye;
    private MyCountDownTimer myCountDownTimer;

    @Override
    protected int getLayout() {
        return cn.com.goodsowner.R.layout.activity_rebuild_psd;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(cn.com.goodsowner.R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(cn.com.goodsowner.R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(cn.com.goodsowner.R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(cn.com.goodsowner.R.id.iv_left_white);
        cb_eye = (CheckBox) findViewById(cn.com.goodsowner.R.id.cb_eye);
        et_password = (EditText) findViewById(cn.com.goodsowner.R.id.et_password);
        et_account = (EditText) findViewById(cn.com.goodsowner.R.id.et_account);
        get_validate_code = (TextView) findViewById(cn.com.goodsowner.R.id.get_validate_code);
        et_validate_code = (EditText) findViewById(cn.com.goodsowner.R.id.et_validate_code);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        tv_title.setText(cn.com.goodsowner.R.string.rebuild_psd);
        tv_right.setText("提交");
        iv_left_white.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        get_validate_code.setOnClickListener(this);
        cb_eye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

    }


    private void getCheckCode() {
        String account = et_account.getText().toString();
        if (account.isEmpty()){
            showShortToastByString("信息不完整");
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Tel",account);
        Map<String,String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(RebuildPsdActivity.this, Contants.url_obtainCheckCode, "obtainCheckCode", map, new VolleyInterface(RebuildPsdActivity.this,VolleyInterface.mListener,VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                showShortToastByString("获取成功");
                LogUtil.i("register",result.toString());
                myCountDownTimer = new MyCountDownTimer(120 * 1000, 1000);
                myCountDownTimer.start();
            }

            @Override
            public void onError(VolleyError error) {
                LogUtil.i("hint", error.toString());
//                LogUtil.i("register",error.networkResponse.headers.toString());
//                LogUtil.i("register",error.networkResponse.statusCode+"");
            }

            @Override
            public void onStateError() {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case cn.com.goodsowner.R.id.iv_left_white:
                finish();
                break;
            case cn.com.goodsowner.R.id.tv_right:
                editPassWord();
                break;
            case cn.com.goodsowner.R.id.get_validate_code:
                getCheckCode();
                break;

        }

    }

    private void editPassWord() {
        final String account = et_account.getText().toString();
        final String password = et_password.getText().toString();
        String validateCode = et_validate_code.getText().toString();
        if (account.isEmpty()||password.isEmpty()||validateCode.isEmpty()){
            showShortToastByString("信息不完整");
            return;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Tel",account);
        jsonObject.addProperty("PassWord",password);
        jsonObject.addProperty("UserType",1);
        jsonObject.addProperty("CheckCode",validateCode);
        LogUtil.i(jsonObject.toString());
        Map<String,String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(RebuildPsdActivity.this, Contants.url_updatePassWord, "updatePassWord", map, new VolleyInterface(RebuildPsdActivity.this,VolleyInterface.mListener,VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                LogUtil.i(result.toString());
                showShortToastByString("修改成功");
                getIntent().putExtra("Phone",account);
                getIntent().putExtra("Password",password);
                setResult(RESULT_OK,getIntent());
                finish();
            }

            @Override
            public void onError(VolleyError error) {
                showShortToastByString(getString(cn.com.goodsowner.R.string.timeoutError));
//                LogUtil.i("hint",error.networkResponse.headers.toString());
//                LogUtil.i("hint",error.networkResponse.statusCode+"");
            }

            @Override
            public void onStateError() {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
        }
    }

    class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onFinish() {
            get_validate_code.setText(cn.com.goodsowner.R.string.get_validate_code);
            get_validate_code.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            get_validate_code.setText(String.format("%ds", millisUntilFinished / 1000));
            get_validate_code.setClickable(false);
        }
    }
}
