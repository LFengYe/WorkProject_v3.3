package com.guugoo.jiapeistudent.MinorActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.guugoo.jiapeistudent.App.Constant;
import com.guugoo.jiapeistudent.Data.Package;
import com.guugoo.jiapeistudent.Data.ReturnData;
import com.guugoo.jiapeistudent.MainActivity.BaseActivity;
import com.guugoo.jiapeistudent.Tools.PayResult;
import com.guugoo.jiapeistudent.Tools.DES;
import com.guugoo.jiapeistudent.Tools.MyThread;
import com.guugoo.jiapeistudent.Tools.MyToast;
import com.guugoo.jiapeistudent.Tools.Utils;
import com.guugoo.jiapeistudent.Views.TitleView;
import com.guugoo.jiapeistudent.R;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.Map;

public class PayActivity extends BaseActivity {
    private static final String TAG ="PayActivity";
    private RadioGroup radioGroup;
    private int PayType; //0:表示支付宝支付，1:表示微信支付
    private Button button;
    private String Name;
    private String PackageId;
    private String CardNo;
    private int Sex;
    private String RecommendId;
    private String Address;
    private Package pack;
    private String Nickname;
    private SharedPreferences sp;
    private TextView[] textViews;
    private IWXAPI api;
    /**
     * 支付宝支付
     */

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            Log.d(TAG, "handleMessage: "+msg.obj.toString());
            @SuppressWarnings("unchecked")
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            /**
             对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                MyToast.makeText(PayActivity.this, "支付成功");
                finish();
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                MyToast.makeText(PayActivity.this, "支付失败");
            }
        }
    };
    @Override
    protected void processingData(ReturnData data) {
        Log.d(TAG,data.getData());
        sp.edit().putString("Nickname",Nickname);
        Log.d(TAG, "processingData: "+PayType);
        switch (PayType){
            case 0:
                final String orderInfo = DES.decryptDES(data.getData());
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(PayActivity.this);
                        Map<String, String> result = alipay.payV2(orderInfo, true);
                        Log.i("msp", result.toString());
                        Message msg = new Message();
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };
                Thread payThread = new Thread(payRunnable);
                payThread.start();
                break;
            case 1:
                api = WXAPIFactory.createWXAPI(this,Constant.APP_ID,false);
                Log.d(TAG, "processingData: "+Constant.APP_ID);
                JSONObject json = (JSONObject) JSONObject.parse(data.getData());
                PayReq req = new PayReq();
                req.appId			= json.getString("appId");
                req.partnerId		= json.getString("partnerId");
                req.prepayId		= json.getString("prepayId");
                req.nonceStr		= json.getString("nonceStr");
                req.timeStamp		= json.getString("timeStamp");
                req.packageValue	= json.getString("package");
                req.sign			= json.getString("sign");
                Log.d(TAG, "processingData: "+json.getString("appId"));
                Log.d(TAG, "processingData: "+json.getString("prepayId"));
                Log.d(TAG, "processingData: "+json.getString("partnerId"));
                Log.d(TAG, "processingData: "+json.getString("nonceStr"));
                Log.d(TAG, "processingData: "+json.getString("timeStamp"));
                Log.d(TAG, "processingData: "+json.getString("package"));
                Log.d(TAG, "processingData: "+json.getString("sign"));
//                Toast.makeText(PayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
//                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                api.registerApp(Constant.APP_ID);
                api.sendReq(req);
                break;
        }
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pay);
    }

    @Override
    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.pay_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setMiddleText(R.string.pay);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void findView() {
        radioGroup = (RadioGroup) findViewById(R.id.pay_radioGroup);
        button = (Button) findViewById(R.id.pay_button);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        textViews = new TextView[3];
        textViews[0] = (TextView) findViewById(R.id.Pay_price);
        textViews[1] = (TextView) findViewById(R.id.pay_name);
        textViews[2] = (TextView) findViewById(R.id.pay_content);
    }

    @Override
    protected void init() {

        Bundle bundle= getIntent().getExtras();
        Name = bundle.getString("Name");
        pack = bundle.getParcelable("package");
        CardNo = bundle.getString("CardNo");
        Sex= bundle.getInt("Sex");
        RecommendId = bundle.getString("RecommendId");
        Address = bundle.getString("Address");
        Nickname = bundle.getString("Nickname");
        PackageId = pack.getId();
        PayType=1;
        textViews[0].setText(String.format(this.getString(R.string.pay_money),
                Float.valueOf(pack.getPrice())));
        textViews[1].setText(pack.getPackageName());
        textViews[2].setText(pack.getIntroduction());
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                PayType =checkedId==R.id.pay_radio2 ? 0:1;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }
    private void signUp(){
        if(Utils.isNetworkAvailable(PayActivity.this)) {
                JSONObject json = new JSONObject(true);
                json.put("Name", Name);
                json.put("PackageId",PackageId);
                json.put("Id",sp.getInt("Id",0));
                json.put("CardNo",CardNo);
                json.put("Sex",Sex);
                json.put("RecommendId","");
//                json.put("SchoolId", sp.getInt("SchoolId",0));
                json.put("SchoolId", sp.getInt("SchoolId",1));
                json.put("PayType",PayType);
                json.put("Address",Address);
                json.put("RecommendId",RecommendId);
                Log.d(TAG, "signUp: "+json.toString());
                new MyThread(Constant.URL_SignUp, handler, DES.encryptDES(json.toString())).start();
        }else {
            MyToast.makeText(PayActivity.this,R.string.Toast_internet);
        }
    }



}
