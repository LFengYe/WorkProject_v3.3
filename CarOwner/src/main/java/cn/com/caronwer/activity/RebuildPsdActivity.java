package cn.com.caronwer.activity;


import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import cn.com.caronwer.R;
import cn.com.caronwer.base.BaseActivity;
import cn.com.caronwer.base.Contants;
import cn.com.caronwer.util.EncryptUtil;
import cn.com.caronwer.util.HttpUtil;
import cn.com.caronwer.util.NetworkUtil;
import cn.com.caronwer.util.VolleyInterface;

public class RebuildPsdActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;



    private EditText et_account;
    private EditText et_password;
    private EditText et_validate_code;
    private ImageView iv_login_eye;
    private TextView tv_get_code;

    @Override
    protected int getLayout() {
        return R.layout.activity_rebuild_psd;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);


        et_account = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        et_validate_code = (EditText) findViewById(R.id.et_validate_code);
        tv_get_code = (TextView) findViewById(R.id.tv_get_code);
        iv_login_eye = (ImageView) findViewById(R.id.iv_login_eye);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        tv_title.setText(R.string.rebuild_psd);
        tv_right.setText("提交");
        tv_right.setVisibility(View.VISIBLE);
        iv_left_white.setOnClickListener(this);
        tv_right.setOnClickListener(this);

        tv_get_code.setOnClickListener(this);
        iv_login_eye.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left_white:
                finish();
                break;
            case R.id.tv_right:
                //提交
                if (NetworkUtil.isConnected(RebuildPsdActivity.this)) {
                    register();
                }else {
                    showShortToastByString(getString(R.string.Neterror));
                }
                break;
            case R.id.iv_login_eye:
                if (et_password.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_login_eye.setImageResource(R.mipmap.eye_close);
                }

                else {
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_login_eye.setImageResource(R.mipmap.eye_open);
                }
                break;
            case R.id.tv_get_code:
                //获取验证码
                getCheckCode();
                break;

        }

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

        HttpUtil.doPost(RebuildPsdActivity.this, Contants.url_obtainCheckCode, "obtainCheckCode", map, new VolleyInterface(RebuildPsdActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                showShortToastByString("获取成功");
                if (et_validate_code != null)
                    et_validate_code.setText(result.toString().substring(1, result.toString().length() - 1));
            }

            @Override
            public void onError(VolleyError error) {
            }

            @Override
            public void onStateError(int sta, String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    showShortToastByString(msg);
                }
            }
        });


    }

    private void register() {
        final String account = et_account.getText().toString();
        final String password = et_password.getText().toString();
        String validateCode = et_validate_code.getText().toString();
        if (account.isEmpty()||password.isEmpty()||validateCode.isEmpty()){
            showShortToastByString("信息不完整");
            return;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Phone",account);
        jsonObject.addProperty("VerificationCode",validateCode);
        jsonObject.addProperty("Password",password);

//        jsonObject.addProperty("UserType",1);
        jsonObject.addProperty("UserType",2);

        Map<String,String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(RebuildPsdActivity.this, Contants.url_register, "register", map, new VolleyInterface(RebuildPsdActivity.this,VolleyInterface.mListener,VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                showShortToastByString("注册成功");
                getIntent().putExtra("Phone",account);
                getIntent().putExtra("Password",password);
                setResult(RESULT_OK,getIntent());
                finish();
            }

            @Override
            public void onError(VolleyError error) {
                showShortToastByString(getString(R.string.timeoutError));
            }

            @Override
            public void onStateError(int sta,String msg) {
                if (!TextUtils.isEmpty(msg)){
                    showShortToastByString(msg);
                }
            }
        });

    }

}
