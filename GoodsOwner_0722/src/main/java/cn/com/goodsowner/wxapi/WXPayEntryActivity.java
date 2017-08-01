package cn.com.goodsowner.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.com.goodsowner.R;
import cn.com.goodsowner.base.Contants;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";
    public static final String ACTION_INTENT_PAY_SUCCESS = "com.goodsOwner.pay.success";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);
        api = WXAPIFactory.createWXAPI(this, Contants.weChatAPPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

        if (resp.errCode == 0) {
            Toast.makeText(WXPayEntryActivity.this, "支付成功", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ACTION_INTENT_PAY_SUCCESS);
            sendBroadcast(intent);
            finish();
        } else if (resp.errCode == -1) {
            Toast.makeText(WXPayEntryActivity.this, "支付出错，已取消支付", Toast.LENGTH_LONG).show();
            //MyToast.makeText(WXPayEntryActivity.this, "支付出错，已取消支付");
            finish();
        } else if (resp.errCode == -2) {
            Toast.makeText(WXPayEntryActivity.this, "支付已经取消", Toast.LENGTH_LONG).show();
            //MyToast.makeText(WXPayEntryActivity.this, "支付已经取消");
            finish();
        }
    }
}