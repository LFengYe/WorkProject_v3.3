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

public class MyOrderActivity extends BaseActivity {
    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;

    private TextView tv_all;
    private TextView tv_new;
    private TextView tv_doing;
    private TextView tv_end;
    private View view_all;
    private View view_new;
    private View view_doing;
    private View view_end;

    private OrderFragment fg_total;
    private OrderFragment fg_new;
    private OrderFragment fg_doing;
    private OrderFragment fg_end;

    @Override
    protected int getLayout() {
        return R.layout.activity_my_order;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);

        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);

        tv_all = (TextView) findViewById(R.id.tv_all);
        tv_new = (TextView) findViewById(R.id.tv_new);
        tv_doing = (TextView) findViewById(R.id.tv_doing);
        tv_end = (TextView) findViewById(R.id.tv_end);
        view_all = findViewById(R.id.view_all);
        view_new = findViewById(R.id.view_new);
        view_doing = findViewById(R.id.view_doing);
        view_end = findViewById(R.id.view_end);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        tv_title.setText(R.string.myOrder);
        tv_right.setVisibility(View.GONE);
        iv_left_white.setOnClickListener(this);

        tv_all.setOnClickListener(this);
        tv_new.setOnClickListener(this);
        tv_doing.setOnClickListener(this);
        tv_end.setOnClickListener(this);

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
            case R.id.tv_new:
                choiceState(2);
                break;
            case R.id.tv_doing:
                choiceState(3);
                break;
            case R.id.tv_end:
                choiceState(4);
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
                tv_all.setTextColor(ContextCompat.getColor(MyOrderActivity.this, R.color.color_red));
                view_all.setVisibility(View.VISIBLE);
                if (fg_total == null) {
                    fg_total = OrderFragment.newInstance(0);
                    transaction.add(R.id.fl_content, fg_total);
                } else {
                    transaction.show(fg_total);
                }
                break;
            case 2:
                tv_new.setTextColor(ContextCompat.getColor(MyOrderActivity.this, R.color.color_red));
                view_new.setVisibility(View.VISIBLE);
                if (fg_new == null) {
                    fg_new = OrderFragment.newInstance(1);
                    transaction.add(R.id.fl_content, fg_new);
                } else {
                    transaction.show(fg_new);
                }
                break;
            case 3:
                tv_doing.setTextColor(ContextCompat.getColor(MyOrderActivity.this, R.color.color_red));
                view_doing.setVisibility(View.VISIBLE);
                if (fg_doing == null) {
                    fg_doing = OrderFragment.newInstance(2);
                    transaction.add(R.id.fl_content, fg_doing);
                } else {
                    transaction.show(fg_doing);
                }
                break;
            case 4:
                tv_end.setTextColor(ContextCompat.getColor(MyOrderActivity.this, R.color.color_red));
                view_end.setVisibility(View.VISIBLE);
                if (fg_end == null) {
                    fg_end = OrderFragment.newInstance(3);
                    transaction.add(R.id.fl_content, fg_end);
                } else {
                    transaction.show(fg_end);
                }

                break;
        }
        transaction.commit();
    }

    private void initState() {
        view_all.setVisibility(View.INVISIBLE);
        view_new.setVisibility(View.INVISIBLE);
        view_doing.setVisibility(View.INVISIBLE);
        view_end.setVisibility(View.INVISIBLE);
        tv_all.setTextColor(ContextCompat.getColor(MyOrderActivity.this, R.color.color_gary_font));
        tv_new.setTextColor(ContextCompat.getColor(MyOrderActivity.this, R.color.color_gary_font));
        tv_doing.setTextColor(ContextCompat.getColor(MyOrderActivity.this, R.color.color_gary_font));
        tv_end.setTextColor(ContextCompat.getColor(MyOrderActivity.this, R.color.color_gary_font));
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (fg_total != null) {
            transaction.hide(fg_total);
        }
        if (fg_new != null) {
            transaction.hide(fg_new);
        }
        if (fg_doing != null) {
            transaction.hide(fg_doing);
        }
        if (fg_end != null) {
            transaction.hide(fg_end);
        }
    }
}
