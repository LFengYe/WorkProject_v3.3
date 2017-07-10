package cn.com.goodsowner.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Map;

import cn.com.goodsowner.R;
import cn.com.goodsowner.adapter.OrderAddAdapter;
import cn.com.goodsowner.base.BaseActivity;
import cn.com.goodsowner.base.Contants;
import cn.com.goodsowner.bean.OrderAddressBean;
import cn.com.goodsowner.bean.OrderAddressInfo;
import cn.com.goodsowner.util.EncryptUtil;
import cn.com.goodsowner.util.HttpUtil;
import cn.com.goodsowner.util.LogUtil;
import cn.com.goodsowner.util.VolleyInterface;

public class ConfirmOrderActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_right;
    private TextView tv_orderType;
    private TextView tv_money;
    private TextView tv_time;
    private ImageView iv_left_white;
    private Button bt_recharge;
    private ListView lv_address;
    private OrderAddAdapter orderAddAdapter;
    private int orderType;
    private int carType;
    private String money;
    private String time;
    private JsonObject jsonObject;

    @Override
    protected int getLayout() {
        return R.layout.activity_confirm_order;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);


        bt_recharge = (Button) findViewById(R.id.bt_recharge);
        lv_address = (ListView) findViewById(R.id.lv_address);
        tv_orderType = (TextView) findViewById(R.id.tv_orderType);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_time = (TextView) findViewById(R.id.tv_time);


    }

    @Override
    protected void initData() {
        ArrayList<OrderAddressInfo> orderAddressInfos = getIntent().getParcelableArrayListExtra("OrderAddressInfos");
        ArrayList<OrderAddressBean> orderAddressBeen = new ArrayList<>();
        int size = orderAddressInfos.size();
        for (int i = 0; i < size; i++) {
            OrderAddressBean orderAddressBean = new OrderAddressBean();
            OrderAddressInfo orderAddressInfo = orderAddressInfos.get(i);
            orderAddressBean.setAddress(orderAddressInfo.getReceiptAddress());
            orderAddressBean.setReceipter(orderAddressInfo.getReceipter());
            orderAddressBean.setTel(orderAddressInfo.getReceiptTel());
            orderAddressBeen.add(orderAddressBean);
        }
        orderType = getIntent().getIntExtra("type", 0);
        carType = getIntent().getIntExtra("carType", 0);
        money = getIntent().getStringExtra("money");
        time = getIntent().getStringExtra("time");
        String mapJson = getIntent().getStringExtra("mapJson");
        jsonObject = new JsonParser().parse(mapJson).getAsJsonObject();
        jsonObject.addProperty("PayWay",0);
        orderAddAdapter = new OrderAddAdapter(orderAddressBeen, this);
    }

    @Override
    protected void initView() {

        if (orderType == 1) {
            tv_orderType.setText("即");
        } else if (orderType == 2) {
            tv_orderType.setText("预");
        }

        tv_money.setText(money);
        tv_time.setText(time);
        lv_address.setAdapter(orderAddAdapter);
        tv_title.setText(R.string.confirm_order);
        tv_right.setVisibility(View.GONE);
        iv_left_white.setOnClickListener(this);


        bt_recharge.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_white:
                finish();
                break;
            case R.id.bt_recharge:
                publishOrder();
                break;
        }
    }

    private void publishOrder() {
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        if (carType == 1) {
            HttpUtil.doPost(ConfirmOrderActivity.this, Contants.url_sendOrder, "sendOrder", map, new VolleyInterface(ConfirmOrderActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                @Override
                public void onSuccess(JsonElement result) {
                    LogUtil.i(result.toString());
                    showShortToastByString("货源发布成功");
                    setResult(RESULT_OK,getIntent());
                    finish();
                }

                @Override
                public void onError(VolleyError error) {
                    showShortToastByString(getString(R.string.timeoutError));
                }

                @Override
                public void onStateError() {
                }
            });
        } else if (carType == 2) {
            HttpUtil.doPost(ConfirmOrderActivity.this, Contants.url_publishCarpool, "publishCarpool", map, new VolleyInterface(ConfirmOrderActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                @Override
                public void onSuccess(JsonElement result) {
                    LogUtil.i(result.toString());
                    showShortToastByString("货源发布成功");
                    setResult(RESULT_OK,getIntent());
                    finish();
                }

                @Override
                public void onError(VolleyError error) {
                    showShortToastByString(getString(R.string.timeoutError));
                }

                @Override
                public void onStateError() {
                }
            });
        }
    }


}
