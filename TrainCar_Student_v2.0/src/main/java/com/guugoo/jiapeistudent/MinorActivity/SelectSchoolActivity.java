package com.guugoo.jiapeistudent.MinorActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

import com.guugoo.jiapeistudent.App.Constant;
import com.guugoo.jiapeistudent.Data.ReturnData;
import com.guugoo.jiapeistudent.Data.School;
import com.guugoo.jiapeistudent.MainActivity.BaseActivity;
import com.guugoo.jiapeistudent.MainActivity.SignInActivity;
import com.guugoo.jiapeistudent.R;
import com.guugoo.jiapeistudent.Tools.DES;
import com.guugoo.jiapeistudent.Tools.MyThread;
import com.guugoo.jiapeistudent.Tools.MyToast;
import com.guugoo.jiapeistudent.Tools.Utils;
import com.guugoo.jiapeistudent.Views.TitleView;

public class SelectSchoolActivity extends BaseActivity {
    private static final  String TAG = "SelectSchoolActivity";
    private EditText text;
    private Button button;
    private SharedPreferences sp;

    @Override
    protected void processingData(ReturnData data) {
        if(data.getData()!=null){
            Log.d(TAG, "processingData: "+data.getData());
//            Log.d(TAG, "processingData: "+DES.decryptDES(data.getData()));

            List<School> schools = JSONObject.parseArray(data.getData(),School.class);
            School school = schools.get(0);
            Intent intent = new Intent(SelectSchoolActivity.this, SignInActivity.class);
            intent.putExtra("Type",1);
            intent.putExtra("ID",DES.decryptDES(school.getId()));
            Log.d(TAG, "processingData: "+DES.decryptDES(school.getId()));
            Log.d(TAG, "processingData: "+DES.decryptDES(school.getSchoolName()));
            intent.putExtra("SchoolName",DES.decryptDES(school.getSchoolName()));
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_select_school);
    }

    @Override
    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.select_school);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setMiddleText(R.string.sign_in_title);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void findView() {
        text = (EditText) findViewById(R.id.select_school_text);
        button = (Button) findViewById(R.id.select_school_button);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    @Override
    protected void init() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.isNetworkAvailable(SelectSchoolActivity.this)){
                    Log.d(TAG, "onClick: ");
                    if(TextUtils.isEmpty(text.getText())){
                        MyToast.makeText(SelectSchoolActivity.this,R.string.Toast_select_school);
                    }else {
                        JSONObject json= new JSONObject(true);
                        json.put("SchoolCode",text.getText());
                        Log.d(TAG, "onClick: "+ json.toString());
                        new MyThread(Constant.URL_GetSchoolByCode, handler, DES.encryptDES(json.toString())).start();
                    }
                }else {
                    MyToast.makeText(SelectSchoolActivity.this,R.string.Toast_internet);
                }

            }
        });
    }
}
