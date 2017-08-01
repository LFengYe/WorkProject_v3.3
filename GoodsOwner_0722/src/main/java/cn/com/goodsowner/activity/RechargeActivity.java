package cn.com.goodsowner.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.com.goodsowner.base.BaseActivity;
import cn.com.goodsowner.base.Contants;
import cn.com.goodsowner.util.EncryptUtil;
import cn.com.goodsowner.util.HttpUtil;
import cn.com.goodsowner.util.LogUtil;
import cn.com.goodsowner.util.VolleyInterface;
import cn.com.goodsowner.view.CustomProgressDialog;
import cn.com.goodsowner.wxapi.WXPayEntryActivity;

public class RechargeActivity extends BaseActivity {


    private CheckBox cb_wechat;
    private CheckBox cb_alipay;
    private CheckBox cb_card;
    private RelativeLayout rl_wechat;
    private RelativeLayout rl_alipay;
    private RelativeLayout rl_card;
    private EditText et_money;
    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private Button bt_ok;
    private int PayWay = 2;

    private CustomProgressDialog customProgressDialog;
    private MyBroadcast myBroadcast;
    private IWXAPI wxApi;

    private void weChatPay(JSONObject returnData) throws JSONException {
        wxApi = WXAPIFactory.createWXAPI(this, Contants.weChatAPPID, false);
        wxApi.registerApp(Contants.weChatAPPID);

        PayReq req = new PayReq();
        req.appId			= returnData.getString("appId");
        req.partnerId		= returnData.getString("partnerId");
        req.prepayId		= returnData.getString("prepayId");
        req.nonceStr		= returnData.getString("nonceStr");
        req.timeStamp		= returnData.getString("timeStamp");
        req.packageValue	= returnData.getString("package");
        req.sign			= returnData.getString("sign");

        wxApi.sendReq(req);
        Gson gson = new Gson();
        Log.i("调用微信支付", gson.toJson(returnData));
    }

    @Override
    protected int getLayout() {
        return cn.com.goodsowner.R.layout.activity_recharge;
    }

    @Override
    protected void findById() {

        RelativeLayout rl_head = (RelativeLayout) findViewById(cn.com.goodsowner.R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(cn.com.goodsowner.R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(cn.com.goodsowner.R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(cn.com.goodsowner.R.id.iv_left_white);
        et_money = (EditText) findViewById(cn.com.goodsowner.R.id.et_money);
        cb_wechat = (CheckBox) findViewById(cn.com.goodsowner.R.id.cb_wechat);
        cb_alipay = (CheckBox) findViewById(cn.com.goodsowner.R.id.cb_alipay);
        cb_card = (CheckBox) findViewById(cn.com.goodsowner.R.id.cb_card);
        bt_ok = (Button) findViewById(cn.com.goodsowner.R.id.bt_ok);
        rl_wechat = (RelativeLayout) findViewById(cn.com.goodsowner.R.id.rl_wechat);
        rl_alipay = (RelativeLayout) findViewById(cn.com.goodsowner.R.id.rl_alipay);
        rl_card = (RelativeLayout) findViewById(cn.com.goodsowner.R.id.rl_card);
    }

    @Override
    protected void initData() {
        customProgressDialog = new CustomProgressDialog(RechargeActivity.this);
        myBroadcast = new MyBroadcast();
        IntentFilter filter = new IntentFilter(WXPayEntryActivity.ACTION_INTENT_PAY_SUCCESS);
        registerReceiver(myBroadcast, filter);
    }

    @Override
    protected void initView() {
        tv_right.setVisibility(View.GONE);
        tv_title.setText(cn.com.goodsowner.R.string.recharge);

        iv_left_white.setOnClickListener(this);
        bt_ok.setOnClickListener(this);

        rl_wechat.setOnClickListener(this);
        rl_alipay.setOnClickListener(this);
        rl_card.setOnClickListener(this);


        cb_wechat.setOnClickListener(this);
        cb_alipay.setOnClickListener(this);
        cb_card.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case cn.com.goodsowner.R.id.iv_left_white:
                finish();
                break;
            case cn.com.goodsowner.R.id.bt_ok:
                recharge();
                break;
            case cn.com.goodsowner.R.id.rl_wechat:
            case cn.com.goodsowner.R.id.cb_wechat:
                initRadio();
                cb_wechat.setChecked(true);
                PayWay = 2;
                break;
            case cn.com.goodsowner.R.id.rl_alipay:
            case cn.com.goodsowner.R.id.cb_alipay:
                initRadio();
                cb_alipay.setChecked(true);
                PayWay = 3;
                break;
            case cn.com.goodsowner.R.id.rl_card:
            case cn.com.goodsowner.R.id.cb_card:
                initRadio();
                cb_card.setChecked(true);
                PayWay =4;
                break;
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(myBroadcast);
        super.onDestroy();
    }

    private void recharge() {

        String money = et_money.getText().toString();
        if (money.isEmpty()){
            showShortToastByString("金额不能为空");
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", Contants.userId);
        jsonObject.addProperty("UserType", 1);
        jsonObject.addProperty("Amount", Double.valueOf(money));
        jsonObject.addProperty("PayWay", PayWay);
        jsonObject.addProperty("PayType",1);
        jsonObject.addProperty("AIndex",0);
        jsonObject.addProperty("OrderNo","");

        customProgressDialog.show();
        customProgressDialog.setText("加载中..");

        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(RechargeActivity.this, Contants.url_payAmount, "payAmount", map, new VolleyInterface(RechargeActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                //LogUtil.i(result.toString());
                //showShortToastByString(result.toString());
                try {
                    JSONObject object = new JSONObject(result.toString());
                    LogUtil.i(result.toString());
                    customProgressDialog.dismiss();
                    if (PayWay == 2) {
                        weChatPay(object.getJSONObject("result"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
    }

    private void initRadio() {
        cb_wechat.setChecked(false);
        cb_alipay.setChecked(false);
        cb_card.setChecked(false);
    }

    class MyBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            RechargeActivity.this.setResult(RESULT_OK, null);
            finish();
        }
    }
}
