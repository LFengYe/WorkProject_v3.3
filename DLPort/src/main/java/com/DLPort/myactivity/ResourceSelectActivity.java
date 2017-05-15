package com.DLPort.myactivity;


import android.os.Bundle;
import android.view.View;

import com.DLPort.R;
import com.DLPort.myview.TitleView;

/**
 * Created by LFeng on 2017/5/15.
 */

public class ResourceSelectActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_resource);
        initTitle();
        initView();
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.resource_select_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.home);
        titleView.setMiddleText(R.string.find_resource);
        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {

    }
}
