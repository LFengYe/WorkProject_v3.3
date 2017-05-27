package cn.guugoo.jiapeistudent.MainActivity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import cn.guugoo.jiapeistudent.R;
import cn.jpush.android.api.JPushInterface;

public class SplashActivity extends BaseActivity {
    private final int SPLASH_DISPLAY_LENGHT = 2000; //延迟

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void findView() {

        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {

                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }

        }, SPLASH_DISPLAY_LENGHT);
    }

    @Override
    protected void init() {

    }


    @Override
    protected void onResume() {

        JPushInterface.onResume(SplashActivity.this);
        super.onResume();
    }


    @Override
    protected void onPause() {

        JPushInterface.onPause(SplashActivity.this);
        super.onPause();
    }
}
