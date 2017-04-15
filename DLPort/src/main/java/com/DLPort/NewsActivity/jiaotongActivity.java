package com.DLPort.NewsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.DLPort.R;
import com.DLPort.myactivity.BaseActivity;
import com.DLPort.myview.TitleView;

/**
 * Created by Administrator on 2016/5/18.
 */
public class jiaotongActivity extends BaseActivity {
    private View view1,view2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiaotong);
        initTitle();
        init();
    }
    private void init() {
        view1= findViewById(R.id.jiaotong_gaosu);
        view2= findViewById(R.id.jiaotong_guodao);
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(jiaotongActivity.this, JiaoTonggaosu.class);
                startActivity(intent);
            }
        });
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(jiaotongActivity.this, JiaoTongguodao.class);
                startActivity(intent);
            }
        });

    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.jiaotong_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.news);
        titleView.setMiddleText(R.string.news2);
        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
