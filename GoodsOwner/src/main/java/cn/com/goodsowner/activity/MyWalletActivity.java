package cn.com.goodsowner.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.Map;

import cn.com.goodsowner.R;
import cn.com.goodsowner.base.BaseActivity;
import cn.com.goodsowner.base.Contants;
import cn.com.goodsowner.bean.MoneyInfo;
import cn.com.goodsowner.util.EncryptUtil;
import cn.com.goodsowner.util.HttpUtil;
import cn.com.goodsowner.util.LogUtil;
import cn.com.goodsowner.util.VolleyInterface;

public class MyWalletActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private Button bt_recharge;
    private Button bt_charge_situation;
    private LinearLayout ll_recharge;
    private TextView tv_balance_money;

    @Override
    protected int getLayout() {
        return R.layout.activity_my_wallet;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);
        bt_recharge = (Button) findViewById(R.id.bt_recharge);
        bt_charge_situation = (Button) findViewById(R.id.bt_charge_situation);
        ll_recharge = (LinearLayout) findViewById(R.id.ll_recharge);
        tv_balance_money = (TextView) findViewById(R.id.tv_balance_money);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

        tv_right.setVisibility(View.GONE);
        tv_title.setText(R.string.myWallet);


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", Contants.userId);
        jsonObject.addProperty("UserType", 1);
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(MyWalletActivity.this, Contants.url_getUserBalance, "getUserBalance", map, new VolleyInterface(MyWalletActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                LogUtil.i(result.toString());
                Gson gson = new Gson();
                MoneyInfo moneyInfo = gson.fromJson(result, MoneyInfo.class);
                DecimalFormat df = new DecimalFormat("#00.00");
                tv_balance_money.setText(String.format("¥%s", df.format(moneyInfo.getBalance())));
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

        iv_left_white.setOnClickListener(this);
        bt_charge_situation.setOnClickListener(this);
        bt_recharge.setOnClickListener(this);
        ll_recharge.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.iv_left_white:
                finish();
                break;
            case R.id.bt_charge_situation:
                intent.setClass(MyWalletActivity.this,ChargeScheduleActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_recharge:
                intent.setClass(MyWalletActivity.this,RechargeActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_recharge:
                intent.setClass(MyWalletActivity.this,RechargeHintActivity.class);
                startActivity(intent);
                break;

        }
    }
}
