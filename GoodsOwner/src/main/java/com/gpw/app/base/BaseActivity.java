package com.gpw.app.base;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by gpw on 2016/10/12.
 * --加油
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    protected BaseApplication MyApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        MyApp = BaseApplication.getInstance();
        initData();
        findById();
        initView();
        MyApp.addActivity(this);
    }


    @Override
    protected void onPause() {
     //   JPushInterface.onPause(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
       // JPushInterface.onResume(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApp.removeActivity(this);
    }

    protected abstract int getLayout();

    protected abstract void findById();

    protected abstract void initData();

    protected abstract void initView();



    protected void showShortToastByString(CharSequence text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
