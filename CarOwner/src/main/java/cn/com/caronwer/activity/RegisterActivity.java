package cn.com.caronwer.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class RegisterActivity extends BaseActivity {


    private EditText et_account22;
    private EditText et_password22;
    private EditText et_validate_code22;
    private ImageView iv_login_eye22;
    private TextView tv_get_code22;
    private ImageView iv_close22;
    private Button bt_ok22;
    private Bitmap mBitmap22;

    @Override
    protected int getLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void findById() {
        et_account22 = (EditText) findViewById(R.id.et_account22);
        et_password22 = (EditText) findViewById(R.id.et_password22);
        et_validate_code22 = (EditText) findViewById(R.id.et_validate_code22);
        tv_get_code22 = (TextView) findViewById(R.id.tv_get_code22);

        iv_close22 = (ImageView) findViewById(R.id.iv_close22);
        iv_login_eye22 = (ImageView) findViewById(R.id.iv_login_eye22);
        bt_ok22 = (Button) findViewById(R.id.bt_ok22);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        byte[] bis =intent.getExtras().getByteArray("bitmap2");
        mBitmap22 = BitmapFactory.decodeByteArray(bis, 0, bis.length);
        if (mBitmap22 != null) {
            getWindow().getDecorView().setBackgroundDrawable(new BitmapDrawable(mBitmap22));
        }
    }

    @Override
    protected void initView() {

        iv_close22.setOnClickListener(this);
        iv_login_eye22.setOnClickListener(this);
        tv_get_code22.setOnClickListener(this);
        bt_ok22.setOnClickListener(this);

        et_password22.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!et_password22.getText().toString().isEmpty()&&!et_account22.getText().toString().isEmpty()
                        &&!et_validate_code22.getText().toString().isEmpty()){
                    bt_ok22.setBackgroundResource(R.drawable.shap_selector);
                }else {
                    bt_ok22.setBackgroundResource(R.drawable.shape_button2);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_account22.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!et_password22.getText().toString().isEmpty()&&!et_account22.getText().toString().isEmpty()
                        &&!et_validate_code22.getText().toString().isEmpty()){
                    bt_ok22.setBackgroundResource(R.drawable.shap_selector);
                }else {
                    bt_ok22.setBackgroundResource(R.drawable.shape_button2);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_validate_code22.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!et_password22.getText().toString().isEmpty()&&!et_account22.getText().toString().isEmpty()
                        &&!et_validate_code22.getText().toString().isEmpty()){
                    bt_ok22.setBackgroundResource(R.drawable.shap_selector);
                }else {
                    bt_ok22.setBackgroundResource(R.drawable.shape_button2);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_ok22:
                if (NetworkUtil.isConnected(RegisterActivity.this)) {
                    register();
                }else {
                    showShortToastByString(getString(R.string.Neterror));
                }
                break;
            case R.id.iv_login_eye22:
                if (et_password22.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
                    et_password22.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_login_eye22.setImageResource(R.mipmap.login_eye);
                }
                else {
                    et_password22.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_login_eye22.setImageResource(R.mipmap.biyan);
                }
                break;
            case R.id.iv_close22:
                finish();
                break;
            case R.id.tv_get_code22:
                getCheckCode();
                break;

        }
    }

    private void getCheckCode() {
        String account = et_account22.getText().toString();
        if (account.isEmpty()){
            showShortToastByString("信息不完整");
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Tel",account);
        Map<String,String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(RegisterActivity.this, Contants.url_obtainCheckCode, "obtainCheckCode", map, new VolleyInterface(RegisterActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {

                if (et_validate_code22 != null)
                    Toast.makeText(RegisterActivity.this,
                            "获取验证码成功", Toast.LENGTH_SHORT).show();
//                et_validate_code.setText(result.toString().substring(1,result.toString().length()-1));
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(RegisterActivity.this,
                        "获取失败", Toast.LENGTH_SHORT).show();
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
        final String account = et_account22.getText().toString();
        final String password = et_password22.getText().toString();
        final String validateCode = et_validate_code22.getText().toString();
        if (account.isEmpty()||password.isEmpty()||validateCode.isEmpty()){
            showShortToastByString("信息不完整");
            return;
        }

        zhuce(account, password, validateCode);



    }

    private void zhuce(final String account, final String password, String validateCode) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Phone",account);
        jsonObject.addProperty("VerificationCode",validateCode);
        jsonObject.addProperty("Password",password);
        jsonObject.addProperty("UserType",2);

        Map<String,String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(RegisterActivity.this, Contants.url_register, "register", map, new VolleyInterface(RegisterActivity.this,VolleyInterface.mListener,VolleyInterface.mErrorListener) {
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
                Toast.makeText(RegisterActivity.this,
                        "用户已存在", Toast.LENGTH_SHORT).show();
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
