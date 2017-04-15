package cn.com.caronwer.activity;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.caronwer.R;
import cn.com.caronwer.base.BaseActivity;
import cn.com.caronwer.fragment.mefragmentall;
import cn.com.caronwer.fragment.mefragmentjiedan;
import cn.com.caronwer.fragment.mefragmentjiesu;
import cn.com.caronwer.fragment.mefragmentjingxin;

public class MyOrderActivityMe extends BaseActivity {
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

    private mefragmentall fg_all;//全部
    private mefragmentjiedan fg_jiedan;//已接单
    private mefragmentjingxin fg_doing;//进行中
    private mefragmentjiesu fg_end;//已结束
    private String mSelectNo;


    @Override
    protected int getLayout() {
        return R.layout.activity_my_orderme;
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

        mSelectNo = getIntent().getStringExtra("selectNo");

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

        choiceState(Integer.parseInt(mSelectNo));
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
                tv_all.setTextColor(ContextCompat.getColor(MyOrderActivityMe.this, R.color.color_red));
                view_all.setVisibility(View.VISIBLE);
                if (fg_all == null) {
                    fg_all = new mefragmentall();
                    transaction.add(R.id.fl_content, fg_all);
                } else {
                    transaction.show(fg_all);
                }
                break;
            case 2:
                tv_new.setTextColor(ContextCompat.getColor(MyOrderActivityMe.this, R.color.color_red));
                view_new.setVisibility(View.VISIBLE);
                if (fg_jiedan == null) {
                    fg_jiedan = new mefragmentjiedan();
                    transaction.add(R.id.fl_content, fg_jiedan);
                } else {
                    transaction.show(fg_jiedan);
                }
                break;
            case 3:
                tv_doing.setTextColor(ContextCompat.getColor(MyOrderActivityMe.this, R.color.color_red));
                view_doing.setVisibility(View.VISIBLE);
                if (fg_doing == null) {
                    fg_doing = new mefragmentjingxin();
                    transaction.add(R.id.fl_content, fg_doing);
                } else {
                    transaction.show(fg_doing);
                }
                break;
            case 4:
                tv_end.setTextColor(ContextCompat.getColor(MyOrderActivityMe.this, R.color.color_red));
                view_end.setVisibility(View.VISIBLE);
                if (fg_end == null) {
                    fg_end = new mefragmentjiesu();
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
        tv_all.setTextColor(ContextCompat.getColor(MyOrderActivityMe.this, R.color.color_gary_font));
        tv_new.setTextColor(ContextCompat.getColor(MyOrderActivityMe.this, R.color.color_gary_font));
        tv_doing.setTextColor(ContextCompat.getColor(MyOrderActivityMe.this, R.color.color_gary_font));
        tv_end.setTextColor(ContextCompat.getColor(MyOrderActivityMe.this, R.color.color_gary_font));
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (fg_all != null) {
            transaction.hide(fg_all);
        }
        if (fg_jiedan != null) {
            transaction.hide(fg_jiedan);
        }
        if (fg_doing != null) {
            transaction.hide(fg_doing);
        }
        if (fg_end != null) {
            transaction.hide(fg_end);
        }
    }

}
