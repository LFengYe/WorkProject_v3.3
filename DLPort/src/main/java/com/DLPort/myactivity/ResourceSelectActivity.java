package com.DLPort.myactivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.DLPort.R;
import com.DLPort.myview.TitleView;

/**
 * Created by LFeng on 2017/5/15.
 */

public class ResourceSelectActivity extends BaseActivity {

    private int userType;
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt("userType", userType);//设置用户类型为货主
            bundle.putInt("dataType", 0);
            intent.putExtras(bundle);
            if (userType == 0)
                intent.setClass(ResourceSelectActivity.this, AttentionOrderActivity.class);
            if (userType == 1)
                intent.setClass(ResourceSelectActivity.this, NewsPublishActivty.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            switch (v.getId()) {
                case R.id.second_import: {
                    break;
                }
                case R.id.second_export: {
                    break;
                }
                case R.id.second_share_car: {
                    break;
                }
                case R.id.second_more: {
                    break;
                }
            }
        }
    };

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
        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userType = getIntent().getExtras().getInt("userType");
        if (userType == 0)
            titleView.setMiddleText(R.string.find_resource);
        if (userType == 1)
            titleView.setMiddleText(R.string.public_resource);

    }

    private void initView() {
        findViewById(R.id.second_import).setOnClickListener(clickListener);
        findViewById(R.id.second_export).setOnClickListener(clickListener);
        findViewById(R.id.second_share_car).setOnClickListener(clickListener);
        findViewById(R.id.second_more).setOnClickListener(clickListener);
    }
}
