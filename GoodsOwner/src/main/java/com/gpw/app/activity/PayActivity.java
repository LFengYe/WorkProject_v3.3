package com.gpw.app.activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.Map;

import com.gpw.app.R;
import com.gpw.app.base.BaseActivity;
import com.gpw.app.base.Contants;
import com.gpw.app.util.EncryptUtil;
import com.gpw.app.util.HttpUtil;
import com.gpw.app.util.LogUtil;
import com.gpw.app.util.VolleyInterface;

public class PayActivity extends BaseActivity {


    private CheckBox cb_wallet;
    private CheckBox cb_wechat;
    private CheckBox cb_alipay;
    private CheckBox cb_card;
    private RelativeLayout rl_wallet;
    private RelativeLayout rl_wechat;
    private RelativeLayout rl_alipay;
    private RelativeLayout rl_card;
    private TextView tv_money;
    private TextView tv_money1;
    private TextView tv_balance;
    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private Button bt_ok;
    private int payType = 1;
    private String orderNo;
    private double money;
    private int type;


    @Override
    protected int getLayout() {
        return R.layout.activity_pay;
    }

    @Override
    protected void findById() {

        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);
        tv_money1 = (TextView) findViewById(R.id.tv_money1);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_balance = (TextView) findViewById(R.id.tv_balance);
        rl_wallet = (RelativeLayout) findViewById(R.id.rl_wallet);
        rl_wechat = (RelativeLayout) findViewById(R.id.rl_wechat);
        rl_alipay = (RelativeLayout) findViewById(R.id.rl_alipay);
        rl_card = (RelativeLayout) findViewById(R.id.rl_card);

        cb_wallet = (CheckBox) findViewById(R.id.cb_wallet);
        cb_wechat = (CheckBox) findViewById(R.id.cb_wechat);
        cb_alipay = (CheckBox) findViewById(R.id.cb_alipay);
        cb_card = (CheckBox) findViewById(R.id.cb_card);

        bt_ok = (Button) findViewById(R.id.bt_ok);


    }

    @Override
    protected void initData() {

        orderNo = getIntent().getStringExtra("orderNo");
        money = getIntent().getDoubleExtra("money", 0);
        type = getIntent().getIntExtra("type", 0);

    }

    @Override
    protected void initView() {
        tv_right.setVisibility(View.GONE);
        tv_title.setText("支付");
        if (type != 5) {
            tv_money.setText("金额");
        }
        DecimalFormat df = new DecimalFormat("#00.00");
        tv_money1.setText(String.format("¥%s", df.format(money)));
        tv_balance.setText(String.format("可用余额%s元", df.format(Contants.Balance)));
        iv_left_white.setOnClickListener(this);
        bt_ok.setOnClickListener(this);

        rl_wallet.setOnClickListener(this);
        rl_wechat.setOnClickListener(this);
        rl_alipay.setOnClickListener(this);
        rl_card.setOnClickListener(this);


        cb_wallet.setOnClickListener(this);
        cb_wechat.setOnClickListener(this);
        cb_alipay.setOnClickListener(this);
        cb_card.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_white:
                finish();
                break;
            case R.id.bt_ok:
                payOrder();
                break;

            case R.id.rl_wallet:
            case R.id.cb_wallet:
                initRadio();
                cb_wallet.setChecked(true);
                payType = 1;
                break;
            case R.id.rl_wechat:
            case R.id.cb_wechat:
                initRadio();
                cb_wechat.setChecked(true);
                payType = 2;
                break;
            case R.id.rl_alipay:
            case R.id.cb_alipay:
                initRadio();
                cb_alipay.setChecked(true);
                payType = 3;
                break;
            case R.id.rl_card:
            case R.id.cb_card:
                initRadio();
                cb_card.setChecked(true);
                payType = 4;
                break;
        }
    }

    private void initRadio() {
        cb_wallet.setChecked(false);
        cb_wechat.setChecked(false);
        cb_alipay.setChecked(false);
        cb_card.setChecked(false);
    }

    private void payOrder() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", Contants.userId);
        jsonObject.addProperty("UserType", 1);
        jsonObject.addProperty("Amount", money);
        jsonObject.addProperty("PayWay", payType);
        jsonObject.addProperty("AIndex", 0);
        jsonObject.addProperty("PayType", type);
        jsonObject.addProperty("OrderNo", orderNo);
        LogUtil.i(jsonObject.toString());
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(PayActivity.this, Contants.url_payAmount, "payAmount", map, new VolleyInterface(PayActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                LogUtil.i(result.toString());
                showShortToastByString(result.toString());
                setResult(RESULT_OK, getIntent());
                finish();
            }

            @Override
            public void onError(VolleyError error) {
                showShortToastByString(getString(R.string.timeoutError));
//                LogUtil.i("hint",error.networkResponse.headers.toString());
//                LogUtil.i("hint",error.networkResponse.statusCode+"");
            }

            @Override
            public void onStateError() {
            }
        });
    }
}
