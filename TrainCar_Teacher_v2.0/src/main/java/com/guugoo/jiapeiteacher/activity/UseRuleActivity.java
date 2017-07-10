package com.guugoo.jiapeiteacher.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guugoo.jiapeiteacher.R;
import com.guugoo.jiapeiteacher.base.BaseActivity;

/**
 * Created by gpw on 2016/8/8.
 * --加油
 */
public class UseRuleActivity extends BaseActivity {
    @Override
    protected int getLayout() {
        return R.layout.activity_use_rule;
    }

    @Override
    protected void initView() {

        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.head);
        TextView tv_center = (TextView) rl_head.findViewById(R.id.tv_center);
        ImageView iv_back = (ImageView) rl_head.findViewById(R.id.iv_back);
        tv_center.setText("使用规则");
        iv_back.setOnClickListener(this);

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish();
        }
    }
}
