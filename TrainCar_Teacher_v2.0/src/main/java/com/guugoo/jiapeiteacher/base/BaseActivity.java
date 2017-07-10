package com.guugoo.jiapeiteacher.base;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {


    protected Resources res;
    protected MyApplication MyApp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayout());

        res = this.getApplicationContext().getResources();

        MyApp = MyApplication.getInstance();

        initData();

        initView();

        MyApp.addActivity(this);
    }




    @Override
    protected void onPause() {
        JPushInterface.onPause(this);
        super.onPause();
    }


    @Override
    protected void onResume() {
        JPushInterface.onResume(this);
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApp.removeActivity(this);
    }


    protected abstract int getLayout();

    protected abstract void initView();

    protected abstract void initData();



    protected void showLongToastByString(int string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        callback(requestCode,permissions,grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected void callback(int requestCode, String[] permissions, int[] grantResults) {
    }


}