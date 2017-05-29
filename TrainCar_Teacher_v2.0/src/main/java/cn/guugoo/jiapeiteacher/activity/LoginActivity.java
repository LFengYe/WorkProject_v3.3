package cn.guugoo.jiapeiteacher.activity;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.Set;
import cn.guugoo.jiapeiteacher.R;
import cn.guugoo.jiapeiteacher.base.BaseActivity;
import cn.guugoo.jiapeiteacher.base.Constants;
import cn.guugoo.jiapeiteacher.base.MyApplication;
import cn.guugoo.jiapeiteacher.bean.LoginInfo;
import cn.guugoo.jiapeiteacher.bean.TotalData;
import cn.guugoo.jiapeiteacher.util.EncryptUtils;
import cn.guugoo.jiapeiteacher.util.HttpUtil;
import cn.guugoo.jiapeiteacher.util.NetUtil;
import cn.guugoo.jiapeiteacher.view.MyDialog;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText et_account;
    private EditText et_password;
    private Button bt_login;
    private TextView tv_back_psd;
    private MyDialog dialog;
    private CheckBox cb_login;
    private SharedPreferences prefs;
    private String account;
    private String password;
    private boolean autoState;
    private int startState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MyApplication.getInstance().addActivity(this);
        startState = getIntent().getIntExtra("startState",0);
        initView();

    }


    private void initView() {
        et_account = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        bt_login = (Button) findViewById(R.id.bt_login);
        tv_back_psd = (TextView) findViewById(R.id.tv_back_psd);
        cb_login = (CheckBox) findViewById(R.id.cb_login);
        prefs = getSharedPreferences(Constants.SHARED_NAME, MODE_PRIVATE);
        account = prefs.getString("account","");
        password = prefs.getString("password","");
        autoState = prefs.getBoolean("autoState",false);

        if (autoState||startState!=0){
            et_account.setText(account);
            et_password.setText(password);
            loginLogic();
        }else {
            if (!account.isEmpty()) {
                et_account.setText(account);
                et_account.setSelection(account.length());
            }
        }
        bt_login.setOnClickListener(this);
        tv_back_psd.setOnClickListener(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loginLogic();
                } else {
                    Toast.makeText(LoginActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_login) {
            if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        100);
            } else
            {
                loginLogic();
            }

        } else if (v.getId() == R.id.tv_back_psd) {
            backPsdLogic();
        }

    }

    private void backPsdLogic() {
        Intent intent = new Intent(LoginActivity.this,RetrievePsdActivity.class);
        startActivity(intent);
    }

    private void loginLogic() {
        account = et_account.getText().toString();
        password = et_password.getText().toString();
        if (account.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, R.string.login_null, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!NetUtil.checkNetworkConnection(LoginActivity.this)) {
            Toast.makeText(LoginActivity.this, R.string.Net_error, Toast.LENGTH_SHORT).show();
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Tel", account);
        jsonObject.addProperty("PassWord", password);
        jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
        new LoginAsyncTask().execute(jsonObject);


    }


    class LoginAsyncTask extends AsyncTask<JsonObject, String, String> {


        @Override
        protected void onPreExecute() {
            dialog = MyDialog.loginDialog(LoginActivity.this);
            dialog.show();
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(JsonObject... params) {
            JsonObject json_data = params[0];
            return HttpUtil.httpPost1(HttpUtil.url_login, json_data);
        }


        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            super.onPostExecute(s);
            if (s.isEmpty()) {
                Toast.makeText(LoginActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            System.out.println(s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                LoginInfo loginInfo = gson.fromJson(totalData.getData(), LoginInfo.class);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("account", account);
                editor.putString("password", password);
                editor.putInt("Id", loginInfo.getId());
                editor.putString("NicKname", loginInfo.getNicKname());
                editor.putInt("SchoolId", loginInfo.getSchoolId());
                editor.putString("Icon", loginInfo.getIcon());
                editor.putString("invitationCode", loginInfo.getInvitationCode());
                editor.putString("token", loginInfo.getToken());
                editor.putString("tel", loginInfo.getTel());
                if (cb_login.isChecked()){
                    editor.putBoolean("autoState", true);
                }else {
                    editor.putBoolean("autoState", false);
                }
                editor.apply();  //存入
                JPushInterface.setAlias(LoginActivity.this, //上下文对象
                        loginInfo.getTel(), //别名
                        new TagAliasCallback() {//回调接口,i=0表示成功,其它设置失败
                            @Override
                            public void gotResult(int i, String s, Set<String> set) {
                                if(i==0){
                                    System.out.println("绑定成功");}
                            }
                        });

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("loginInfo", loginInfo);
                intent.putExtra("startState",startState);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(LoginActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }
    @Override
    protected void onResume() {
        JPushInterface.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        JPushInterface.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }
}
