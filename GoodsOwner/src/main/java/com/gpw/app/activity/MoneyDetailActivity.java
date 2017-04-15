package com.gpw.app.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gpw.app.R;
import com.gpw.app.base.BaseActivity;

public class MoneyDetailActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private TextView tv_freight;
    private TextView tv_premiums;
    private TextView tv_total_money;
    private double freight;
    private double premiums;


    @Override
    protected int getLayout() {
        return R.layout.activity_money_detail;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);
        tv_freight = (TextView) findViewById(R.id.tv_freight);
        tv_total_money = (TextView) findViewById(R.id.tv_total_money);
        tv_premiums = (TextView) findViewById(R.id.tv_premiums);
    }

    @Override
    protected void initData() {
        freight = getIntent().getDoubleExtra("freight", 0);
        premiums = getIntent().getDoubleExtra("premiums", 0);
    }

    @Override
    protected void initView() {
        tv_title.setText(R.string.money_detail);

        double total_money = freight + premiums;

        tv_freight.setText(String.format("¥%s元", freight));

        tv_total_money.setText(String.format("¥%s元", total_money));

        tv_premiums.setText(String.format("¥%s元", premiums));
        tv_right.setVisibility(View.GONE);
        iv_left_white.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_white:
                finish();
                break;
        }
    }
}
