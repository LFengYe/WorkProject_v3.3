package com.DLPort.myactivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.myfragment.Fragment_carlogin;
import com.DLPort.myfragment.Fragment_huologin;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/3/25.
 */
public class LoginIn extends BaseActivity {
    private static final String TAG="LoginIn";
    private Fragment_carlogin Carlogin;
    private Fragment_huologin Huologin;
    private ImageView cursor;
    private int index;    //选择卡片编号
    private int currIndex= 0;  //当前页卡编号
    private int bmpW=0;   //图片长度
    private List<Fragment> fragments;
    private ViewPager viewPager;
    private TextView[] textViews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SetTitlecolor(R.color.writer);
        setContentView(R.layout.login_in);
        InitImage();
        InitViewPager();
    }

    private void InitImage() {
        cursor = (ImageView) findViewById(R.id.cursor);
        textViews = new TextView[2];
        textViews[0] = (TextView) findViewById(R.id.In_car);
        textViews[1] = (TextView) findViewById(R.id.In_huo);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)
                cursor.getLayoutParams();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        /*
        获取手机屏幕的宽度
         */
        int screenW = dm.widthPixels;
        layoutParams.width=screenW/2;
        bmpW =screenW/2;
    }

    private void InitViewPager() {

        viewPager = (ViewPager) findViewById(R.id.content);
        fragments =new ArrayList<Fragment>();
        Carlogin = new Fragment_carlogin();
        Huologin = new Fragment_huologin();
        fragments.add(Carlogin);
        fragments.add(Huologin);
        FragAdapter adapter = new FragAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        initListener();
    }

    private void initListener() {

        textViews[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 0;
                viewPager.setCurrentItem(index);

            }
        });
        textViews[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 1;
                viewPager.setCurrentItem(index);

            }
        });
    }

    public class FragAdapter extends FragmentStatePagerAdapter {


        public FragAdapter(FragmentManager fm) {

            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


    }

    private void MapMove(int arg0){

        Animation animation = new TranslateAnimation(bmpW * currIndex,
                    bmpW * arg0, 0, 0);
        currIndex = arg0;
        animation.setFillAfter(true);/* True:图片停在动画结束位置 */
        animation.setDuration(300);
        cursor.startAnimation(animation);
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        @Override
        public void onPageSelected(int arg0) {
            String s = String.valueOf(arg0);
            MapMove(arg0);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}
