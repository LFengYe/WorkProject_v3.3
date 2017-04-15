package com.DLPort.NewsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.DLPort.OurActivity.CargoStatisticsActivity;
import com.DLPort.R;
import com.DLPort.myactivity.BaseActivity;
import com.DLPort.myview.TitleView;

/**
 * Created by Administrator on 2016/5/18.
 */
public class TongzhiActivity extends BaseActivity {

    private RelativeLayout businessChangeLayout;
    private RelativeLayout activityLayout;
    private RelativeLayout informLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tongzhi);
        initTitle();
        initView();
    }
    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.tongzhi_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.news);
        titleView.setMiddleText(R.string.news3);
        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        final Bundle bundle = getIntent().getExtras();
        final int type = bundle.getInt("Type");
        businessChangeLayout = (RelativeLayout) findViewById(R.id.business_change_layout);
        businessChangeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (type == 0) {
                    intent.setClass(TongzhiActivity.this, CarOwnerBusinessChangeActivity.class);
                }
                if (type == 1) {
                    intent.setClass(TongzhiActivity.this, CarGoBusinessChangeActivity.class);
                }
                startActivity(intent);
            }
        });

        activityLayout = (RelativeLayout) findViewById(R.id.activity_layout);
        activityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TongzhiActivity.this, ActivityActivity.class);
                startActivity(intent);
            }
        });

        informLayout = (RelativeLayout) findViewById(R.id.inform_layout);
        informLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TongzhiActivity.this, InformActivity.class);
                startActivity(intent);
            }
        });
    }
}
