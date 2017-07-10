package com.guugoo.jiapeistudent.MinorActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.guugoo.jiapeistudent.App.Constant;
import com.guugoo.jiapeistudent.Data.ReturnData;
import com.guugoo.jiapeistudent.MainActivity.BaseActivity;
import com.guugoo.jiapeistudent.R;
import com.guugoo.jiapeistudent.Tools.DES;
import com.guugoo.jiapeistudent.Tools.MyThread;
import com.guugoo.jiapeistudent.Tools.MyToast;
import com.guugoo.jiapeistudent.Tools.Utils;
import com.guugoo.jiapeistudent.Views.TitleView;

public class ChangeNameActivity extends BaseActivity {
    private static final String TAG = "ChangeNameActivity";
    private EditText editText;
    private SharedPreferences sp;

    @Override
    protected void processingData(ReturnData data) {

        MyToast.makeText(ChangeNameActivity.this,data.getMessage());
        Intent intent = new Intent();
        Log.d(TAG, "processingData: "+editText.getText().toString());
        intent.putExtra("change",editText.getText().toString());
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_change_name);
    }

    @Override
    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.change_name_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setRightTextVisible(true);
        titleView.setRightText(R.string.change_oK);
        titleView.setMiddleText(R.string.change_name);
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
        editText = (EditText) findViewById(R.id.et_change_name);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    @Override
    protected void init() {

    }
    private void getData(){
        if(Utils.isNetworkAvailable(ChangeNameActivity.this)){
            if(!TextUtils.isEmpty(editText.getText())){
                JSONObject json= new JSONObject(true);
                json.put("StudentId",sp.getInt("Id",0));
                json.put("NewnNickme",editText.getText().toString().trim());
                new MyThread(Constant.URL_Nickname, handler, DES.encryptDES(json.toString())).start();
            }else {
                MyToast.makeText(ChangeNameActivity.this,R.string.text_New_Nickme);
            }
        }else {
            MyToast.makeText(ChangeNameActivity.this,R.string.Toast_internet);
        }
    }
}
