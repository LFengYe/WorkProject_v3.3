package cn.com.goodsowner.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.goodsowner.base.BaseActivity;

public class BenefitActivityActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;

    @Override
    protected int getLayout() {
        return cn.com.goodsowner.R.layout.activity_benefit_activity;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(cn.com.goodsowner.R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(cn.com.goodsowner.R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(cn.com.goodsowner.R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(cn.com.goodsowner.R.id.iv_left_white);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        tv_title.setText(cn.com.goodsowner.R.string.benefit_activity);
        tv_right.setVisibility(View.GONE);
        iv_left_white.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case cn.com.goodsowner.R.id.iv_left_white:
                finish();
                break;
        }
    }
}
