package cn.com.caronwer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tencent.mm.sdk.openapi.IWXAPI;

import java.util.Map;

import cn.com.caronwer.R;
import cn.com.caronwer.base.BaseActivity;
import cn.com.caronwer.base.Contants;
import cn.com.caronwer.util.EncryptUtil;
import cn.com.caronwer.util.HttpUtil;
import cn.com.caronwer.util.VolleyInterface;

/**
 * Created by LFeng on 2017/7/9.
 */

public class AuthThirdActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private RelativeLayout weChatPay;
    private RelativeLayout alipayPay;
    private CheckBox weChatBox;
    private CheckBox alipayBox;

    private IWXAPI wxApi;

    private int payType;//支付方式: 1--微信 | 2--支付宝

    private void weChatPay(String returnData) {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_auth_third;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);

        weChatPay = (RelativeLayout) findViewById(R.id.rl_wechat);
        weChatBox = (CheckBox) findViewById(R.id.cb_wechat);
        alipayPay = (RelativeLayout) findViewById(R.id.rl_alipay);
        alipayBox = (CheckBox) findViewById(R.id.cb_alipay);

        Button button = (Button) findViewById(R.id.bt_ok);
        button.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        payType = 0;
    }

    @Override
    protected void initView() {
        tv_title.setText("车主认证");
        tv_right.setVisibility(View.GONE);
        iv_left_white.setOnClickListener(this);

        weChatPay.setOnClickListener(this);
        weChatBox.setOnClickListener(this);
        alipayPay.setOnClickListener(this);
        alipayBox.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_ok:
                if (payType == 0) {
                    showShortToastByString(getResources().getString(R.string.pay_type_select_promote));
                } else {
                    payPenalSum();
                }
                break;
            case R.id.iv_left_white:
                finish();
                break;
            case R.id.rl_wechat:
            case R.id.cb_wechat:
                payType = 1;
                weChatBox.setChecked(true);
                alipayBox.setChecked(false);
                break;
            case R.id.rl_alipay:
            case R.id.cb_alipay:
                payType = 2;
                weChatBox.setChecked(false);
                alipayBox.setChecked(true);
                break;
        }
    }

    private void payPenalSum() {
        SharedPreferences prefs = getSharedPreferences(Contants.SHARED_NAME, MODE_PRIVATE);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserID", prefs.getString("UserId", ""));
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(AuthThirdActivity.this, Contants.url_TransporterVehicleCheck3, "VehicleCheck3", map,
                new VolleyInterface(AuthThirdActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                    @Override
                    public void onSuccess(JsonElement result) {
                        showShortToastByString(getResources().getString(R.string.authenticate_already_upload));
                        Intent intent = new Intent(AuthThirdActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(VolleyError error) {
                    }

                    @Override
                    public void onStateError(int sta, String msg) {
                        if (!TextUtils.isEmpty(msg)) {
                            showShortToastByString(msg);
                        }
                    }
                });
    }
}
