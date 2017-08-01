package cn.com.goodsowner.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends Activity {

    private ImageView iv_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(cn.com.goodsowner.R.layout.activity_splash);
        iv_splash = (ImageView) findViewById(cn.com.goodsowner.R.id.iv_splash);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                iv_splash.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this, cn.com.goodsowner.R.anim.splash_in));
            }
        }, 200);




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 1500);

        if(!this.isTaskRoot()){
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }


}