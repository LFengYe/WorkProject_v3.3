package cn.com.caronwer.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.caronwer.R;
import cn.com.caronwer.base.BaseActivity;

/**
 * Created by LFeng on 2017/7/10.
 */

public class AuthSecondActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;

    @Override
    protected int getLayout() {
        return R.layout.activity_auth_second;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);

        Button button = (Button) findViewById(R.id.bt_ok);
        button.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        tv_title.setText("车主认证");
        tv_right.setVisibility(View.GONE);
        iv_left_white.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_ok:
                Intent intent = new Intent(AuthSecondActivity.this, AuthThirdActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_left_white:
                finish();
                break;
        }
    }
}
