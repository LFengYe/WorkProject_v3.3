package cn.guugoo.jiapeistudent.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import cn.guugoo.jiapeistudent.App.Constant;
import cn.guugoo.jiapeistudent.Data.ReturnData;
import cn.guugoo.jiapeistudent.R;
import cn.guugoo.jiapeistudent.Tools.CountDownTimerUtil;
import cn.guugoo.jiapeistudent.Tools.DES;
import cn.guugoo.jiapeistudent.Tools.MyHandler;
import cn.guugoo.jiapeistudent.Tools.MyThread;
import cn.guugoo.jiapeistudent.Tools.MyToast;
import cn.guugoo.jiapeistudent.Tools.Utils;
import cn.guugoo.jiapeistudent.Views.TitleView;


/**
 * Created by Administrator on 2016/8/3.
 */
public class SignInActivity extends BaseActivity {
    private static final String TAG = "SignInActivity";
    private EditText user , password, code;
    private TextView code_text;
    private Button send_button;
    private int Type;
    private ImageView iv_see;
    private LinearLayout ll_see;
    private RelativeLayout welcome;
    private String SchoolId="";
    private TextView tv_schoolname;
    private String name;
    private String Code="";  //验证码
    private String photo;//电话
    private String tel="";
    private SharedPreferences sp;


    @Override
    protected void processingData(ReturnData data) {
        MyToast.makeText(SignInActivity.this,data.getMessage());
        Code=data.getData();
        Log.d(TAG, "processingData: "+Code);
        photo=user.getText().toString();
        CountDownTimerUtil mCountDownTimerUtils = new CountDownTimerUtil(code_text, 120000, 1000);
        mCountDownTimerUtils.start();

    }

    private Handler handler2 = new MyHandler(this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                Log.d(TAG, "handleMessage: "+msg.obj);
                ReturnData data=JSONObject.parseObject((String) msg.obj,ReturnData.class);
                if(data.getStatus()==0){
                    MyToast.makeText(SignInActivity.this,data.getMessage());
                    sp.edit().putString("UserName",user.getText().toString().trim());
                    finish();
                }
                MyToast.makeText(SignInActivity.this,data.getMessage());
            }
        }
    };

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sign_in);

    }

    protected void initTitle() {
        Type = getIntent().getIntExtra("Type",0);
        name = getIntent().getStringExtra("SchoolName");
        tv_schoolname = (TextView) findViewById(R.id.Sign_in_school_name);
        TitleView titleView = (TitleView) findViewById(R.id.sign_in_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        welcome = (RelativeLayout) findViewById(R.id.sign_in_welcome);
        switch (Type){
            case 1:
                titleView.setMiddleText(R.string.sign_in_title);
                welcome.setVisibility(View.VISIBLE);
                SchoolId = getIntent().getStringExtra("ID");
                tv_schoolname.setText(String.format(this.getString(R.string.school_name),name));
                break;
            case 2:
                titleView.setMiddleText(R.string.find_title);
                welcome.setVisibility(View.GONE);
                break;
        }
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    protected void findView() {
        user = (EditText) findViewById(R.id.sign_user);
        password = (EditText) findViewById(R.id.sign_password);
        code = (EditText) findViewById(R.id.sign_code);
        code_text = (TextView) findViewById(R.id.sign_button_code);
        send_button = (Button) findViewById(R.id.sign_button_send);
        iv_see = (ImageView) findViewById(R.id.sign_button_see);
        tv_schoolname = (TextView) findViewById(R.id.Sign_in_school_name);
        sp =getSharedPreferences("user", Context.MODE_PRIVATE);
        ll_see = (LinearLayout) findViewById(R.id.sign_button);
    }

    protected void init() {

        code_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.isNetworkAvailable(SignInActivity.this)){
                    if(isEmpty(1)){
                        JSONObject json= new JSONObject(true);
                        json.put("Tel",user.getText());
                        tel=user.getText().toString().trim();
                        switch (Type){
                            case 1:
                                json.put("Flage",2);
                                break;
                            case 2:
                                json.put("Flage",1);
                                break;
                        }
                        json.put("UserType",1);
                        json.put("SchoolId",SchoolId);
                        Log.d(TAG, "onClick: "+json.toString());
                        new MyThread(Constant.URL_Code, handler, DES.encryptDES(json.toString())).start();
                    }
                }else {
                    MyToast.makeText(SignInActivity.this,R.string.Toast_internet);
                }


            }
        });

        ll_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick:1 "+iv_see.isSelected());
                if(iv_see.isSelected()){
                    iv_see.setSelected(false);
                    Log.d(TAG, "onClick:2 "+iv_see.isSelected());
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    iv_see.setSelected(true);
                    Log.d(TAG, "onClick:3 "+iv_see.isSelected());
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isNetworkAvailable(SignInActivity.this)) {
                    if (isEmpty(2)) {
                        JSONObject json = new JSONObject(true);
                        json.put("Tel", user.getText());
                        json.put("PassWord", password.getText());
                        switch (Type){
                            case 1:
                                json.put("SchoolId",SchoolId);
                                new MyThread(Constant.URL_Register, handler2, DES.encryptDES(json.toString())).start();
                                break;
                            case 2:
                                new MyThread(Constant.URL_RetrievePassword, handler2, DES.encryptDES(json.toString())).start();
                                break;
                        }
                    }
                }else {
                    MyToast.makeText(SignInActivity.this,R.string.Toast_internet);
                }
            }
        });
    }

    private boolean isEmpty(int i){
        if(i==1){
            if(TextUtils.isEmpty(user.getText())){
                MyToast.makeText(SignInActivity.this,R.string.Toast_phone);
                return false;
            }
            if(user.getText().toString().trim().length()!=11){
                MyToast.makeText(SignInActivity.this,R.string.Toast_phone1);
                return false;
            }
            if(TextUtils.isEmpty(password.getText())){
                MyToast.makeText(SignInActivity.this,R.string.Toast_password);
                return false;
            }
        }else {
            if(TextUtils.isEmpty(user.getText())){
                MyToast.makeText(SignInActivity.this,R.string.Toast_phone);
                return false;
            }
            if(TextUtils.isEmpty(password.getText())){
                MyToast.makeText(SignInActivity.this,R.string.Toast_password);
                return false;
            }
            if(TextUtils.isEmpty(Code)){
                MyToast.makeText(SignInActivity.this,R.string.Toast_Code_isEmpty);
                return false;
            }
            Log.d(TAG, "isEmpty: "+Code);
            Log.d(TAG, "isEmpty: "+code.getText());
            Log.d(TAG, "isEmpty: "+tel);
            Log.d(TAG, "isEmpty: "+photo);
            if(!Code.equals(code.getText().toString().trim())||!tel.equals(photo)){
                MyToast.makeText(SignInActivity.this,R.string.Toast_Code);
                return false;
            }
        }
        return true;
    }
}
