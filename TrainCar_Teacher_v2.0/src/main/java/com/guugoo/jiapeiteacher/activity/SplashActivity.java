package com.guugoo.jiapeiteacher.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.guugoo.jiapeiteacher.R;
import cn.jpush.android.api.JPushInterface;

public class SplashActivity extends Activity {

    private ImageView iv_splash;
    private int startState=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        if (getIntent()!=null){
            startState = getIntent().getIntExtra("startState",0);
        }

        iv_splash = (ImageView) findViewById(R.id.iv_splash);

        iv_splash.setAnimation(AnimationUtils.loadAnimation(this,R.anim.zoomin));

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                i.putExtra("startState",startState);
                startActivity(i);
                overridePendingTransition(R.anim.splah_in,R.anim.splah_out);
                finish();
            }
        }, 1500);

        if(!this.isTaskRoot()){
            finish();
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
}
