package cn.com.caronwer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
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
import cn.com.caronwer.util.NetworkUtil;
import cn.com.caronwer.util.VolleyInterface;
import cn.com.caronwer.view.CustomProgressDialog;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class Login2Activity extends BaseActivity {

    private EditText et_account;
    private EditText et_password;
    private ImageView iv_close;
    private ImageView iv_login_eye;
    private TextView tv_forget_psd;
    private TextView tv_register;
    private Button bt_login;
    private SharedPreferences prefs;
    private String password;
    private String account;
    private LinearLayout mActivity_login;
    private Bitmap mBitmap;

    private EditText et_account22;
    private EditText et_password22;
    private EditText et_validate_code22;
    private ImageView iv_login_eye22;
    private TextView tv_get_code22;
    private Button bt_ok22;
    private Bitmap mBitmap22;
    private RelativeLayout mRl_login;
    private RelativeLayout mRl_login22;
    private int recLen = 120;//倒计时时间

    private static boolean isHuoqu = false;

    @Override
    protected int getLayout() {
        return R.layout.activity_login2;
    }

    @Override
    protected void findById() {
        et_account = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        iv_login_eye = (ImageView) findViewById(R.id.iv_login_eye);
        tv_forget_psd = (TextView) findViewById(R.id.tv_forget_psd);
        tv_register = (TextView) findViewById(R.id.tv_register);
        bt_login = (Button) findViewById(R.id.bt_login);
        mActivity_login = (LinearLayout) findViewById(R.id.activity_login);
        mRl_login = (RelativeLayout) findViewById(R.id.rl_login);


        mRl_login22 = (RelativeLayout) findViewById(R.id.rl_login22);
        et_account22 = (EditText) findViewById(R.id.et_account22);
        et_password22 = (EditText) findViewById(R.id.et_password22);
        et_validate_code22 = (EditText) findViewById(R.id.et_validate_code22);
        tv_get_code22 = (TextView) findViewById(R.id.tv_get_code22);

        iv_login_eye22 = (ImageView) findViewById(R.id.iv_login_eye22);
        bt_ok22 = (Button) findViewById(R.id.bt_ok22);


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initData() {
        prefs = getSharedPreferences(Contants.SHARED_NAME, MODE_PRIVATE);
        // startActivity(new Intent(LoginActivity.this,BankUserActivity.class));
        account = prefs.getString("account", "");
        password = prefs.getString("password", "");

//        Intent intent = getIntent();
//        byte[] bis =intent.getExtras().getByteArray("bitmap");
//        mBitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
//        if (mBitmap != null) {
//            mBitmap = UtilsGaosi.BoxBlurFilter(mBitmap);
//            getWindow().getDecorView().setBackgroundDrawable(new BitmapDrawable(mBitmap));
////            Drawable drawable = new BitmapDrawable(bitmap);
////            mActivity_login.setBackground(drawable);
//        }

    }

    @Override
    protected void initView() {
        et_password.setText(password);
        et_account.setText(account);
        if (isHuoqu) {
            tv_get_code22.setText("");
            tv_get_code22.setClickable(false);
        }

        iv_close.setOnClickListener(this);
        iv_login_eye.setOnClickListener(this);
        tv_forget_psd.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        bt_login.setOnClickListener(this);

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!et_password.getText().toString().isEmpty() && !et_account.getText().toString().isEmpty()) {
                    bt_login.setBackgroundResource(R.drawable.shap_selector2);
                } else {
                    bt_login.setBackgroundResource(R.drawable.shape_button2);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!et_password.getText().toString().isEmpty() && !et_account.getText().toString().isEmpty()) {
                    bt_login.setBackgroundResource(R.drawable.shap_selector2);
                } else {
                    bt_login.setBackgroundResource(R.drawable.shape_button2);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (!et_account.getText().toString().isEmpty()) {
            bt_login.setBackgroundResource(R.drawable.shap_selector2);
        } else {
            bt_login.setBackgroundResource(R.drawable.shape_button2);
        }


        iv_login_eye22.setOnClickListener(this);
        tv_get_code22.setOnClickListener(this);
        bt_ok22.setOnClickListener(this);
        et_password22.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!et_password22.getText().toString().isEmpty() && !et_account22.getText().toString().isEmpty()
                        && !et_validate_code22.getText().toString().isEmpty()) {
                    bt_ok22.setBackgroundResource(R.drawable.shap_selector2);
                } else {
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
                if (!et_password22.getText().toString().isEmpty() && !et_account22.getText().toString().isEmpty()
                        && !et_validate_code22.getText().toString().isEmpty()) {
                    bt_ok22.setBackgroundResource(R.drawable.shap_selector2);
                } else {
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
                if (!et_password22.getText().toString().isEmpty() && !et_account22.getText().toString().isEmpty()
                        && !et_validate_code22.getText().toString().isEmpty()) {
                    bt_ok22.setBackgroundResource(R.drawable.shap_selector2);
                } else {
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
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.bt_login:
                if (NetworkUtil.isConnected(Login2Activity.this)) {
                    login();
                } else {
                    showShortToastByString(getString(R.string.Neterror));
                }
                break;
            case R.id.iv_login_eye:
                if (et_password.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_login_eye.setImageResource(R.mipmap.login_eye);
                } else {
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_login_eye.setImageResource(R.mipmap.biyan);
                }
                break;
            case R.id.iv_close:
                if (mRl_login.getVisibility() == View.VISIBLE) {
                    finish();
                } else {
                    mRl_login22.setVisibility(View.GONE);
                    mRl_login.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_forget_psd://忘记密码
                intent.setClass(Login2Activity.this, RebuildPsdActivity.class);
                startActivity(intent);

                break;
            case R.id.tv_register://注册
//                intent.setClass(Login2Activity.this, RegisterActivity.class);
//                startActivityForResult(intent, 0);
//                tiaozhuan();
                mRl_login.setVisibility(View.GONE);
                mRl_login22.setVisibility(View.VISIBLE);
                break;


            case R.id.bt_ok22:
                if (NetworkUtil.isConnected(Login2Activity.this)) {
                    register();
                } else {
                    showShortToastByString(getString(R.string.Neterror));
                }
                break;
            case R.id.iv_login_eye22:
                if (et_password22.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    et_password22.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_login_eye22.setImageResource(R.mipmap.login_eye);
                } else {
                    et_password22.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_login_eye22.setImageResource(R.mipmap.biyan);
                }
                break;
            case R.id.tv_get_code22:
                getCheckCode();
                break;

        }

    }

    private void login() {
        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(Login2Activity.this);
        account = et_account.getText().toString();
        String time = DateUtil.getPsdCurrentDate();
        password = et_password.getText().toString();
        if (account.isEmpty() || password.isEmpty()) {
            showShortToastByString("信息不完整");
            return;
        }
        customProgressDialog.show();
        customProgressDialog.setText("登录中..");
        String finalPassword = MD5Util.encrypt(time + password);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("LoginName", account);
        jsonObject.addProperty("Time", time);
        jsonObject.addProperty("Password", finalPassword);


        jsonObject.addProperty("UserType", 2);//货主

        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(Login2Activity.this, Contants.url_userLogin, "login", map, new VolleyInterface(Login2Activity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                customProgressDialog.dismiss();
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

                JPushInterface.setAliasAndTags(Login2Activity.this, account, null, new TagAliasCallback() {
                    @Override
                    public void gotResult(int arg0, String arg1, Set<String> arg2) {
                    }
                });

                Intent intent = new Intent();
                intent.setClass(Login2Activity.this, MainActivity.class);
                intent.putExtra("userInfo", userInfo);
                intent.putExtra("isDenglu", true);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(VolleyError error) {
                customProgressDialog.dismiss();
                showShortToastByString(getString(R.string.timeoutError));
            }

            @Override
            public void onStateError(int sta, String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    showShortToastByString(msg);
                }
                customProgressDialog.dismiss();
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            String account = data.getStringExtra("account");
            String password = data.getStringExtra("password");
            et_account.setText(account);
            et_password.setText(password);
        }
    }


    public void tiaozhuan() {
        //传递bitmap
        Bitmap bitmap = mBitmap;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bitmapByte = baos.toByteArray();
        Bundle bundle = new Bundle();
        bundle.putByteArray("bitmap2", bitmapByte);
        Intent intent = new Intent();
        intent.setClass(Login2Activity.this, RegisterActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    private void getCheckCode() {
        String account = et_account22.getText().toString();
        if (account.isEmpty()) {
            showShortToastByString("信息不完整");
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Tel", account);
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(Login2Activity.this, Contants.url_obtainCheckCode, "obtainCheckCode", map, new VolleyInterface(Login2Activity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {

                if (et_validate_code22 != null)
                    Toast.makeText(Login2Activity.this,
                            "获取验证码成功", Toast.LENGTH_SHORT).show();
                recLen = 120;
                Message message = handler.obtainMessage(1);     // Message
                handler.sendMessageDelayed(message, 1000); //倒计时
//                et_validate_code.setText(result.toString().substring(1,result.toString().length()-1));
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(Login2Activity.this,
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
        if (account.isEmpty() || password.isEmpty() || validateCode.isEmpty()) {
            showShortToastByString("信息不完整");
            return;
        }

        zhuce(account, password, validateCode);


    }

    private void zhuce(final String account, final String password, String validateCode) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Phone", account);
        jsonObject.addProperty("VerificationCode", validateCode);
        jsonObject.addProperty("Password", password);
        jsonObject.addProperty("UserType", 2);

        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(Login2Activity.this, Contants.url_register, "register", map, new VolleyInterface(Login2Activity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                showShortToastByString("注册成功");
                getIntent().putExtra("Phone", account);
                getIntent().putExtra("Password", password);
                setResult(RESULT_OK, getIntent());
                finish();
            }

            @Override
            public void onError(VolleyError error) {
                showShortToastByString(getString(R.string.timeoutError));
                Toast.makeText(Login2Activity.this,
                        "用户已存在", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStateError(int sta, String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    showShortToastByString(msg);
                }
            }
        });
    }


    //倒计时
//    final Handler handler = new Handler(){
//
//        public void handleMessage(Message msg){         // handle message
//            switch (msg.what) {
//                case 1:
//                    recLen--;
//                    txtView.setText("" + recLen);
//
//                    if(recLen > 0){
//                        Message message = handler.obtainMessage(1);
//                        handler.sendMessageDelayed(message, 1000);      // send message
//                    }else{
//                        txtView.setVisibility(View.GONE);
//                    }
//            }
//
//            super.handleMessage(msg);
//        }
//    };
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    recLen--;
                    tv_get_code22.setText("" + recLen);

                    if (recLen > 0) {
                        tv_get_code22.setClickable(false);
                        Message message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message, 1000);      // send message
                    } else {
                        tv_get_code22.setClickable(true);
                        tv_get_code22.setText("获取验证码");
                    }
            }
            super.handleMessage(msg);
        }
    };


}
