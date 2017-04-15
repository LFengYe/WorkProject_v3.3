package cn.guugoo.jiapeistudent.MinorActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import cn.guugoo.jiapeistudent.App.Constant;
import cn.guugoo.jiapeistudent.Data.ReturnData;
import cn.guugoo.jiapeistudent.MainActivity.BaseActivity;
import cn.guugoo.jiapeistudent.R;
import cn.guugoo.jiapeistudent.Tools.DES;
import cn.guugoo.jiapeistudent.Tools.MyThread;
import cn.guugoo.jiapeistudent.Tools.MyToast;
import cn.guugoo.jiapeistudent.Tools.Utils;
import cn.guugoo.jiapeistudent.Views.TitleView;

public class ChangePasswordActivity extends BaseActivity {
    private static final String TAG ="ChangePasswordActivity";
    private EditText[] text;
    private SharedPreferences sp;

    @Override
    protected void processingData(ReturnData data) {
        MyToast.makeText(ChangePasswordActivity.this,data.getMessage());
        finish();
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_change_password);
    }

    @Override
    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.change_password_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setRightTextVisible(true);
        titleView.setMiddleText(R.string.change_password);
        titleView.setRightText(R.string.change_oK);
        titleView.setRightTextListenter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void findView() {
        text = new EditText[3];
        text[0] = (EditText) findViewById(R.id.et_change_password1);
        text[1] = (EditText) findViewById(R.id.et_change_password2);
        text[2] = (EditText) findViewById(R.id.et_change_password3);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    @Override
    protected void init() {

    }

    private void getData(){
        if(Utils.isNetworkAvailable(ChangePasswordActivity.this)){
            if(isError()){
                JSONObject json= new JSONObject(true);
                json.put("StudentId",sp.getInt("Id",0));
                json.put("PassWord",text[0].getText());
                json.put("NewPass",text[1].getText());
                new MyThread(Constant.URL_UpdatePass, handler, DES.encryptDES(json.toString())).start();
            }
        }else {
            MyToast.makeText(ChangePasswordActivity.this,R.string.Toast_internet);
        }
    }

    private boolean isError(){
        if(TextUtils.isEmpty(text[0].getText())){
            MyToast.makeText(ChangePasswordActivity.this,R.string.change_password_toast1);
            return false;
        }
        if(TextUtils.isEmpty(text[1].getText())){
            MyToast.makeText(ChangePasswordActivity.this,R.string.change_password_toast2);
            return false;
        }
        if(TextUtils.isEmpty(text[2].getText())){
            MyToast.makeText(ChangePasswordActivity.this,R.string.change_password_toast3);
            return false;
        }
        if(!text[1].getText().toString().equals(text[2].getText().toString())){
            MyToast.makeText(ChangePasswordActivity.this,R.string.change_password_toast4);
            return false;
        }
        return true;
    }
}
