package com.DLPort.myactivity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.DLPort.R;
import com.DLPort.myfragment.Fragment_find;
import com.DLPort.myfragment.Fragment_home;
import com.DLPort.myfragment.Fragment_myself;
import com.DLPort.myfragment.Fragment_news;


public class MainActivity extends BaseActivity  implements OnClickListener{
    private static final String TAG = "MainActivity";

    private Fragment_home homefragment;
    private Fragment_find findfragment;
    private Fragment_news newfragment;
    private Fragment_myself myselffragment;

    private ImageView[] imagebuttons;
    private TextView[] textviews;
    private Fragment[] fragments;
    private View[] views;
    private int index;
    private int currentTabIndex=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        findView();
        init();
    }

    private void findView(){
        imagebuttons =new ImageView[4];
        imagebuttons[0] =(ImageView)findViewById(R.id.ib_home);
        imagebuttons[1] = (ImageView) findViewById(R.id.ib_find);
        imagebuttons[2] = (ImageView) findViewById(R.id.ib_news);
        imagebuttons[3] = (ImageView) findViewById(R.id.ib_myself);
        imagebuttons[0].setSelected(true);

        textviews =new TextView[4];

        textviews[0] = (TextView) findViewById(R.id.iv_home);
        textviews[1] = (TextView) findViewById(R.id.iv_find);
        textviews[2] = (TextView) findViewById(R.id.iv_news);
        textviews[3] = (TextView) findViewById(R.id.iv_myself);

        views = new View[4];
        views[0] = findViewById(R.id.home);
        views[1] = findViewById(R.id.find);
        views[2] = findViewById(R.id.news);
        views[3] =findViewById(R.id.myself);


        homefragment =new Fragment_home();
        findfragment = new Fragment_find();
        newfragment = new Fragment_news();
        myselffragment = new Fragment_myself();

        fragments =new Fragment[]{homefragment,findfragment,newfragment,myselffragment};

    }

    private void init() {
        Intent intent = getIntent();
        Bundle bundle =intent.getExtras();
        homefragment.setArguments(bundle);
        myselffragment.setArguments(bundle);
        newfragment.setArguments(bundle);
        textviews[0].setTextColor(0xFFFF5252);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_content, homefragment)
                .add(R.id.main_content, newfragment)
                .add(R.id.main_content, myselffragment)
                .add(R.id.main_content, findfragment)
                .hide(myselffragment).hide(newfragment)
                .hide(findfragment).show(homefragment).commit();

        views[0].setOnClickListener(this);
        views[1].setOnClickListener(this);
        views[2].setOnClickListener(this);
        views[3].setOnClickListener(this);
    }

    private void BarChange() {
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(fragments[currentTabIndex]);

            if (!fragments[index].isAdded()) {
                trx.add(R.id.main_content, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        //  把当前tab设为选中状态

        imagebuttons[currentTabIndex].setSelected(false);
        imagebuttons[index].setSelected(true);
        textviews[currentTabIndex].setTextColor(0xFF999999);
        textviews[index].setTextColor(0xFFFF5252);
        currentTabIndex = index;
    }


    @Override
     public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home:
                index = 0;
                break;
            case R.id.find:
                index = 1;
                break;
            case R.id.news:
                index = 2;
                break;
            case R.id.myself:
                index = 3;
                break;
        }
        BarChange();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    private long exitTime = 0;
    private Toast toast;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                toast = Toast.makeText(getApplicationContext(), "再按一次退出",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                exitTime = System.currentTimeMillis();
            } else {
                moveTaskToBack(false);
                finish();

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
