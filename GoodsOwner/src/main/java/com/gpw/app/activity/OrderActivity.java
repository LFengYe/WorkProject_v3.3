package com.gpw.app.activity;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gpw.app.R;
import com.gpw.app.base.BaseActivity;
import com.gpw.app.fragment.OrderFragment;
import com.gpw.app.fragment.ReceiptOrderFragment;

public class OrderActivity extends BaseActivity {
    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;

    private TextView tv_all;
    private TextView tv_have;
    private TextView tv_wait;

    private View view_all;
    private View view_have;
    private View view_wait;


    private ReceiptOrderFragment fg_total;
    private ReceiptOrderFragment fg_wait;
    private ReceiptOrderFragment fg_have;


    @Override
    protected int getLayout() {
        return R.layout.activity_order;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);

        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);

        tv_all = (TextView) findViewById(R.id.tv_all);
        tv_have = (TextView) findViewById(R.id.tv_have);
        tv_wait = (TextView) findViewById(R.id.tv_wait);

        view_all = findViewById(R.id.view_all);
        view_have = findViewById(R.id.view_have);
        view_wait = findViewById(R.id.view_wait);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        tv_title.setText(R.string.myOrder1);
        tv_right.setVisibility(View.GONE);
        iv_left_white.setOnClickListener(this);

        tv_all.setOnClickListener(this);
        tv_have.setOnClickListener(this);
        tv_wait.setOnClickListener(this);


        choiceState(1);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_white:
                finish();
                break;
            case R.id.tv_all:
                choiceState(1);
                break;
            case R.id.tv_wait:
                choiceState(2);
                break;
            case R.id.tv_have:
                choiceState(3);
                break;
        }
    }

    private void choiceState(int i) {
        initState();
        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction transaction = fManager.beginTransaction();
        hideFragments(transaction);
        switch (i) {
            case 1:
                tv_all.setTextColor(ContextCompat.getColor(OrderActivity.this, R.color.color_red));
                view_all.setVisibility(View.VISIBLE);
                if (fg_total == null) {
                    fg_total = ReceiptOrderFragment.newInstance(0);
                    transaction.add(R.id.fl_content, fg_total);
                } else {
                    transaction.show(fg_total);
                }
                break;
            case 2:
                tv_wait.setTextColor(ContextCompat.getColor(OrderActivity.this, R.color.color_red));
                view_wait.setVisibility(View.VISIBLE);
                if (fg_wait == null) {
                    fg_wait = ReceiptOrderFragment.newInstance(1);
                    transaction.add(R.id.fl_content, fg_wait);
                } else {
                    transaction.show(fg_wait);
                }
                break;
            case 3:
                tv_have.setTextColor(ContextCompat.getColor(OrderActivity.this, R.color.color_red));
                view_have.setVisibility(View.VISIBLE);
                if (fg_have == null) {
                    fg_have = ReceiptOrderFragment.newInstance(2);
                    transaction.add(R.id.fl_content, fg_have);
                } else {
                    transaction.show(fg_have);
                }
                break;
        }
        transaction.commit();
    }

    private void initState() {
        view_all.setVisibility(View.INVISIBLE);
        view_wait.setVisibility(View.INVISIBLE);
        view_have.setVisibility(View.INVISIBLE);

        tv_all.setTextColor(ContextCompat.getColor(OrderActivity.this, R.color.color_gary_font));
        tv_have.setTextColor(ContextCompat.getColor(OrderActivity.this, R.color.color_gary_font));
        tv_wait.setTextColor(ContextCompat.getColor(OrderActivity.this, R.color.color_gary_font));

    }

    private void hideFragments(FragmentTransaction transaction) {
        if (fg_total != null) {
            transaction.hide(fg_total);
        }
        if (fg_wait != null) {
            transaction.hide(fg_wait);
        }
        if (fg_have != null) {
            transaction.hide(fg_have);
        }
    }
}
