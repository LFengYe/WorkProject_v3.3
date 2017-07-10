package cn.com.goodsowner.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import cn.com.goodsowner.adapter.OrderAddAdapter;
import cn.com.goodsowner.base.BaseActivity;
import cn.com.goodsowner.base.Contants;
import cn.com.goodsowner.bean.OrderAddressBean;
import cn.com.goodsowner.bean.OrderAddressInfo;
import cn.com.goodsowner.bean.OrderFeeInfo;
import cn.com.goodsowner.util.EncryptUtil;
import cn.com.goodsowner.util.HttpUtil;
import cn.com.goodsowner.util.LogUtil;
import cn.com.goodsowner.util.VolleyInterface;

public class OrderPayActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_right;
    private TextView tv_orderType;
    private TextView tv_money;
    private TextView tv_time;
    private ImageView iv_left_white;

    private RelativeLayout rl_wallet;
    private RelativeLayout rl_wechat;
    private RelativeLayout rl_alipay;
    private RelativeLayout rl_card;
    private CheckBox cb_wallet;
    private CheckBox cb_wechat;
    private CheckBox cb_alipay;
    private CheckBox cb_card;
    private Button bt_recharge;
    private TextView tv_balance;
    private TextView money_detail;
    private ListView lv_address;
    private OrderAddAdapter orderAddAdapter;
    private int orderType;
    private int carType;
    private JsonObject jsonObject;
    private String money;
    private String time;
    private int payType = 1;
    private boolean isAfterPay;
    private String orderId = "0";
    private double freight;
    private double premiums;
    private double isSurcharge;

    @Override
    protected int getLayout() {
        return cn.com.goodsowner.R.layout.activity_order_pay;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(cn.com.goodsowner.R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(cn.com.goodsowner.R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(cn.com.goodsowner.R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(cn.com.goodsowner.R.id.iv_left_white);

        tv_balance = (TextView) findViewById(cn.com.goodsowner.R.id.tv_balance);
        rl_wallet = (RelativeLayout) findViewById(cn.com.goodsowner.R.id.rl_wallet);
        rl_wechat = (RelativeLayout) findViewById(cn.com.goodsowner.R.id.rl_wechat);
        rl_alipay = (RelativeLayout) findViewById(cn.com.goodsowner.R.id.rl_alipay);
        rl_card = (RelativeLayout) findViewById(cn.com.goodsowner.R.id.rl_card);
        cb_wallet = (CheckBox) findViewById(cn.com.goodsowner.R.id.cb_wallet);
        cb_wechat = (CheckBox) findViewById(cn.com.goodsowner.R.id.cb_wechat);
        cb_alipay = (CheckBox) findViewById(cn.com.goodsowner.R.id.cb_alipay);
        cb_card = (CheckBox) findViewById(cn.com.goodsowner.R.id.cb_card);
        bt_recharge = (Button) findViewById(cn.com.goodsowner.R.id.bt_recharge);
        lv_address = (ListView) findViewById(cn.com.goodsowner.R.id.lv_address);
        tv_orderType = (TextView) findViewById(cn.com.goodsowner.R.id.tv_orderType);
        tv_money = (TextView) findViewById(cn.com.goodsowner.R.id.tv_money);
        tv_time = (TextView) findViewById(cn.com.goodsowner.R.id.tv_time);
        money_detail = (TextView) findViewById(cn.com.goodsowner.R.id.money_detail);


    }

    @Override
    protected void initData() {

        isAfterPay = getIntent().getBooleanExtra("isAfterPay", false);

        ArrayList<OrderAddressBean> orderAddressBeen = new ArrayList<>();
        if (isAfterPay) {
            orderAddressBeen = getIntent().getParcelableArrayListExtra("orderAddressBeen");
            orderId = getIntent().getStringExtra("orderId");
        } else {
            ArrayList<OrderAddressInfo> orderAddressInfos = getIntent().getParcelableArrayListExtra("OrderAddressInfos");
            int size = orderAddressInfos.size();
            for (int i = 0; i < size; i++) {
                OrderAddressBean orderAddressBean = new OrderAddressBean();
                OrderAddressInfo orderAddressInfo = orderAddressInfos.get(i);
                orderAddressBean.setAddress(orderAddressInfo.getReceiptAddress());
                orderAddressBean.setReceipter(orderAddressInfo.getReceipter());
                orderAddressBean.setTel(orderAddressInfo.getReceiptTel());
                orderAddressBeen.add(orderAddressBean);
            }
            carType = getIntent().getIntExtra("carType", 0);
            String mapJson = getIntent().getStringExtra("mapJson");
            isSurcharge = getIntent().getDoubleExtra("IsSurcharge", 0);
            jsonObject = new JsonParser().parse(mapJson).getAsJsonObject();
        }


        orderType = getIntent().getIntExtra("type", 0);
        money = getIntent().getStringExtra("money");
        time = getIntent().getStringExtra("time");
        freight = getIntent().getDoubleExtra("freight", 0);
        premiums = getIntent().getDoubleExtra("premiums", 0);

        orderAddAdapter = new OrderAddAdapter(orderAddressBeen, this);
    }

    @Override
    protected void initView() {

        if (orderType == 1) {
            tv_orderType.setText("即");
        } else if (orderType == 2) {
            tv_orderType.setText("预");
        }

        DecimalFormat df = new DecimalFormat("#00.00");
        tv_money.setText(money);
        tv_time.setText(time);
        lv_address.setAdapter(orderAddAdapter);
        tv_balance.setText(String.format("可用余额%s元", df.format(Contants.Balance)));
        tv_title.setText(cn.com.goodsowner.R.string.order_pay);
        tv_right.setVisibility(View.GONE);
        iv_left_white.setOnClickListener(this);

        rl_wallet.setOnClickListener(this);
        rl_wechat.setOnClickListener(this);
        rl_alipay.setOnClickListener(this);
        rl_card.setOnClickListener(this);

        cb_wallet.setOnClickListener(this);
        cb_wechat.setOnClickListener(this);
        cb_alipay.setOnClickListener(this);
        cb_card.setOnClickListener(this);
        bt_recharge.setOnClickListener(this);
        money_detail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case cn.com.goodsowner.R.id.iv_left_white:
                if (isAfterPay) {
                    setResult(RESULT_OK, getIntent());
                }
                finish();
                break;
            case cn.com.goodsowner.R.id.money_detail:
                if (isAfterPay) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("UserId", Contants.userId);
                    jsonObject.addProperty("OrderNo", orderId);
                    Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
                    HttpUtil.doPost(OrderPayActivity.this, Contants.url_getOrderFeeDetail, "obtainMessage", map, new VolleyInterface(OrderPayActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                        @Override
                        public void onSuccess(JsonElement result) {
                            LogUtil.i(result.toString());
                            Gson gson = new Gson();
                            Type listType = new TypeToken<ArrayList<OrderFeeInfo>>() {
                            }.getType();
                            ArrayList<OrderFeeInfo> orderFeeInfos = gson.fromJson(result, listType);
                            double freight1 = 0;
                            for (OrderFeeInfo orderFeeInfo : orderFeeInfos) {
                                if (orderFeeInfo.getFeeType().equals("运费")) {
                                    freight1 = orderFeeInfo.getAmount();
                                }
                                if (orderFeeInfo.getFeeType().equals("回单费")) {
                                    isSurcharge = orderFeeInfo.getAmount();
                                }
                            }
                            Intent intent = new Intent(OrderPayActivity.this, MoneyDetailActivity.class);
                            intent.putExtra("freight", freight1);
                            intent.putExtra("premiums", premiums);
                            intent.putExtra("amount", 0);
                            intent.putExtra("IsSurcharge", isSurcharge);
                            startActivity(intent);

                        }

                        @Override
                        public void onError(VolleyError error) {
                            showShortToastByString(getString(cn.com.goodsowner.R.string.timeoutError));
//                LogUtil.i("hint",error.networkResponse.headers.toString());
//                LogUtil.i("hint",error.networkResponse.statusCode+"");
                        }

                        @Override
                        public void onStateError() {
                        }
                    });
                } else {
                    Intent intent = new Intent(OrderPayActivity.this, MoneyDetailActivity.class);
                    intent.putExtra("freight", freight);
                    intent.putExtra("premiums", premiums);
                    intent.putExtra("amount", 0);
                    intent.putExtra("IsSurcharge", isSurcharge);
                    startActivity(intent);
                }

                break;
            case cn.com.goodsowner.R.id.rl_wallet:
            case cn.com.goodsowner.R.id.cb_wallet:
                initRadio();
                cb_wallet.setChecked(true);
                payType = 1;
                break;
            case cn.com.goodsowner.R.id.rl_wechat:
            case cn.com.goodsowner.R.id.cb_wechat:
                initRadio();
                cb_wechat.setChecked(true);
                payType = 2;
                break;
            case cn.com.goodsowner.R.id.rl_alipay:
            case cn.com.goodsowner.R.id.cb_alipay:
                initRadio();
                cb_alipay.setChecked(true);
                payType = 3;
                break;
            case cn.com.goodsowner.R.id.rl_card:
            case cn.com.goodsowner.R.id.cb_card:
                initRadio();
                cb_card.setChecked(true);
                payType = 4;
                break;
            case cn.com.goodsowner.R.id.bt_recharge:
                if (isAfterPay) {
                    String amount = money.substring(1);
                    payOrder(Double.valueOf(amount), 2);
                } else {
                    publishOrder();
                }
                break;
        }
    }

    private void publishOrder() {
        jsonObject.addProperty("PayWay", payType);
        LogUtil.i(jsonObject.toString());
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        if (carType == 1) {

            HttpUtil.doPost(OrderPayActivity.this, Contants.url_sendOrder, "sendOrder", map, new VolleyInterface(OrderPayActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                @Override
                public void onSuccess(JsonElement result) {
                    LogUtil.i(result.toString());
                    showShortToastByString("货源发布成功");
                    setResult(RESULT_OK, getIntent());
                    finish();

                }

                @Override
                public void onError(VolleyError error) {
                    showShortToastByString(getString(cn.com.goodsowner.R.string.timeoutError));
                }

                @Override
                public void onStateError() {
                    if (payType != 1) {
                        showShortToastByString("支付失败，订单已产生");
                    }
                }
            });
        } else if (carType == 2) {
            HttpUtil.doPost(OrderPayActivity.this, Contants.url_publishCarpool, "publishCarpool", map, new VolleyInterface(OrderPayActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                @Override
                public void onSuccess(JsonElement result) {
                    LogUtil.i(result.toString());
                    showShortToastByString("货源发布成功");
                    setResult(RESULT_OK, getIntent());
                    finish();
                }

                @Override
                public void onError(VolleyError error) {
                    showShortToastByString(getString(cn.com.goodsowner.R.string.timeoutError));
                }

                @Override
                public void onStateError() {
                    if (payType != 1) {
                        showShortToastByString("支付失败，订单已产生");
                    }
                }
            });
        }
    }

    private void payOrder(double money, int type) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", Contants.userId);
        jsonObject.addProperty("UserType", 1);
        jsonObject.addProperty("Amount", money);
        jsonObject.addProperty("PayWay", payType);
        jsonObject.addProperty("PayType", type);
        jsonObject.addProperty("AIndex", 0);
        jsonObject.addProperty("OrderNo", orderId);

        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(OrderPayActivity.this, Contants.url_payAmount, "payAmount", map, new VolleyInterface(OrderPayActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                LogUtil.i(result.toString());
                showShortToastByString("支付成功");
                setResult(RESULT_OK, getIntent());
                finish();
            }

            @Override
            public void onError(VolleyError error) {
                showShortToastByString(getString(cn.com.goodsowner.R.string.timeoutError));

            }

            @Override
            public void onStateError() {
                showShortToastByString("支付失败");
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isAfterPay) {
            setResult(RESULT_OK, getIntent());
            System.out.println("Asdsad");
        }
        super.onBackPressed();

    }

    private void initRadio() {
        cb_wallet.setChecked(false);
        cb_wechat.setChecked(false);
        cb_alipay.setChecked(false);
        cb_card.setChecked(false);
    }
}
