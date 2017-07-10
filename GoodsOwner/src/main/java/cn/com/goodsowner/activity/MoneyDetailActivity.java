package cn.com.goodsowner.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.goodsowner.R;
import cn.com.goodsowner.base.BaseActivity;

import java.text.DecimalFormat;

public class MoneyDetailActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private TextView tv_freight;
    private TextView tv_premiums;
    private TextView tv_total_money;
    private TextView tv_Payment;
    private LinearLayout ll_payment;
    private LinearLayout ll_freight;
    private LinearLayout ll_premiums;
    private LinearLayout ll_surcharge;
    private TextView tv_surcharge;
    private double IsSurcharge;
    private double freight;
    private double premiums;
    private double amount;


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
        tv_Payment = (TextView) findViewById(R.id.tv_Payment);
        ll_payment = (LinearLayout) findViewById(R.id.ll_payment);
        ll_surcharge = (LinearLayout) findViewById(R.id.ll_surcharge);
        tv_surcharge = (TextView) findViewById(R.id.tv_surcharge);
        ll_freight = (LinearLayout) findViewById(R.id.ll_freight);
        ll_premiums = (LinearLayout) findViewById(R.id.ll_premiums);
    }

    @Override
    protected void initData() {
        freight = getIntent().getDoubleExtra("freight", 0);
        premiums = getIntent().getDoubleExtra("premiums", 0);
        amount = getIntent().getDoubleExtra("amount", 0);
        IsSurcharge = getIntent().getDoubleExtra("IsSurcharge", 0);
    }

    @Override
    protected void initView() {
        tv_title.setText(R.string.money_detail);
        DecimalFormat df = new DecimalFormat("#00.00");
        double total_money = freight + premiums + amount+IsSurcharge;
        if (freight > 0) {
            tv_freight.setText(String.format("¥%s元", df.format(freight)));
        } else {
            ll_freight.setVisibility(View.GONE);
        }

        if (premiums > 0) {
            tv_premiums.setText(String.format("¥%s元", df.format(premiums)));
        } else {
            ll_premiums.setVisibility(View.GONE);
        }
        tv_total_money.setText(String.format("¥%s元", df.format(total_money)));
        if (IsSurcharge > 0) {
            ll_surcharge.setVisibility(View.VISIBLE);
            tv_surcharge.setVisibility(View.VISIBLE);
            tv_surcharge.setText(String.format("¥%s元", df.format(IsSurcharge)));
        } else {
            ll_surcharge.setVisibility(View.GONE);
            tv_surcharge.setVisibility(View.GONE);
        }
        if (amount > 0) {
            ll_payment.setVisibility(View.VISIBLE);
            tv_Payment.setText(String.format("¥%s元", df.format(amount)));
        }
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
