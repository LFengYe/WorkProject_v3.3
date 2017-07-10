package cn.guugoo.jiapeiteacher.activity;

import android.app.backup.FullBackupDataOutput;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import cn.guugoo.jiapeiteacher.R;
import cn.guugoo.jiapeiteacher.base.BaseActivity;
import cn.guugoo.jiapeiteacher.bean.TotalData;

import cn.guugoo.jiapeiteacher.util.EncryptUtils;
import cn.guugoo.jiapeiteacher.util.HttpUtil;


/**
 * Created by gpw on 2016/8/15.
 * --加油
 */
public class RetrievePsdActivity extends BaseActivity {
    private EditText sign_user;
    private EditText sign_password;
    private EditText sign_code;
    private TextView sign_tv_code;
    private String code;

    private MyCountDownTimer myCountDownTimer;

    @Override
    protected int getLayout() {
        return R.layout.activity_retrieve_psd;
    }

    @Override
    protected void initView() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.head);
        TextView tv_center = (TextView) rl_head.findViewById(R.id.tv_center);
        ImageView iv_back = (ImageView) rl_head.findViewById(R.id.iv_back);
        sign_tv_code = (TextView) findViewById(R.id.sign_tv_code);
        Button sign_button_send = (Button) findViewById(R.id.sign_button_send);
        sign_user = (EditText) findViewById(R.id.sign_user);
        sign_password = (EditText) findViewById(R.id.sign_password);
        sign_code = (EditText) findViewById(R.id.sign_code);


        tv_center.setText("找回密码");
        sign_button_send.setOnClickListener(this);
        sign_tv_code.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish();
        } else if (v.getId() == R.id.sign_tv_code && sign_tv_code.getText().toString().equals("发送验证码")) {
            if (sign_user.getText().toString().isEmpty()) {
                Toast.makeText(RetrievePsdActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (sign_user.getText().toString().length() != 11) {
                Toast.makeText(RetrievePsdActivity.this, "请输入11位的手机号", Toast.LENGTH_SHORT).show();
                return;
            }

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Tel", sign_user.getText().toString());
            jsonObject.addProperty("Flage", 1);
            jsonObject.addProperty("UserType", 2);
            jsonObject.addProperty("SchoolId", 2);
            jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
            new GetVerificationCodeAsyncTask().execute(jsonObject);
        } else if (v.getId() == R.id.sign_button_send) {
            if (sign_user.getText().toString().isEmpty()) {
                Toast.makeText(RetrievePsdActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (sign_user.getText().toString().length() != 11) {
                Toast.makeText(RetrievePsdActivity.this, "请输入11位的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            if (sign_password.getText().toString().length() < 6 || sign_password.getText().toString().length() > 16) {
                Toast.makeText(RetrievePsdActivity.this, "密码格式错误，重新输入", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!sign_code.getText().toString().equals(code)) {
                Toast.makeText(RetrievePsdActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                return;
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Tel", sign_user.getText().toString());
            jsonObject.addProperty("NewPass", sign_password.getText().toString());
            jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
            new RetrievePasswordAsyncTask().execute(jsonObject);
        }
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
            sign_tv_code.setText(R.string.sign_in_button_verify);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            sign_tv_code.setText(millisUntilFinished / 1000 + "s");
        }
    }

    class GetVerificationCodeAsyncTask extends AsyncTask<JsonObject, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected String doInBackground(JsonObject... params) {

            JsonObject json_data = params[0];
            return HttpUtil.httpPost1(HttpUtil.url_getVerificationCode, json_data);
        }


        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            System.out.println(s);
            if (s.isEmpty()) {
                Toast.makeText(RetrievePsdActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                MyCountDownTimer myCountDownTimer = new MyCountDownTimer(120 * 1000, 1000);
                myCountDownTimer.start();
                code = totalData.getData().toString().replace("\"", "");
                Toast.makeText(RetrievePsdActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RetrievePsdActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    class RetrievePasswordAsyncTask extends AsyncTask<JsonObject, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected String doInBackground(JsonObject... params) {

            JsonObject json_data = params[0];
            return HttpUtil.httpPost1(HttpUtil.url_retrievePassword, json_data);
        }


        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            System.out.println(s);
            if (s.isEmpty()) {
                Toast.makeText(RetrievePsdActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                finish();
                Toast.makeText(RetrievePsdActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(RetrievePsdActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }
}
