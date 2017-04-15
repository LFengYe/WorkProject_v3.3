package cn.guugoo.jiapeistudent.MinorActivity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.Map;

import cn.guugoo.jiapeistudent.App.Constant;
import cn.guugoo.jiapeistudent.Data.ReturnData;
import cn.guugoo.jiapeistudent.MainActivity.BaseActivity;
import cn.guugoo.jiapeistudent.R;
import cn.guugoo.jiapeistudent.Tools.DES;
import cn.guugoo.jiapeistudent.Tools.MyHandler;
import cn.guugoo.jiapeistudent.Tools.MyThread;
import cn.guugoo.jiapeistudent.Tools.MyToast;
import cn.guugoo.jiapeistudent.Tools.PayResult;
import cn.guugoo.jiapeistudent.Tools.Utils;
import cn.guugoo.jiapeistudent.Views.TitleView;

public class CommentPayActivity extends BaseActivity {
    private static final String TAG = "CommentPayActivity";

    private RatingBar[] ratingBars;
    private String BookingId;
    private TextView cut, add;
    private EditText number;
    private EditText comment;
    private RadioGroup radioGroup;
    private int PayType=1;
    private CheckBox checkBox;
    private Button button;
    private TextView hours;
    private int hoursNumber;
    private SharedPreferences sp;
    private float PayAmount;
    private IWXAPI api;
    private TextView money,error_money;
    private LinearLayout layout,layout2;
    private float ActMinute;
    private float MinuteFee;
    private float Pay=1;

    private int FeeItem;     // FeeItem :1:预约练车费用，2:取消预约单违约金
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
                MyToast.makeText(CommentPayActivity.this, "支付成功");
                finish();
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                MyToast.makeText(CommentPayActivity.this, "支付失败");
            }
        }
    };

    @Override
    protected void processingData(ReturnData data) {
        if(Pay==0){
            MyToast.makeText(CommentPayActivity.this,data.getMessage());
            int  huurs =hoursNumber-Integer.valueOf(number.getText().toString());
            if(huurs<=0){
                sp.edit().putInt("Hours",0).apply();
            }else {
                sp.edit().putInt("Hours",huurs).apply();
            }
            finish();
        }else {
            switch (PayType){
                case 0:
                    final String orderInfo = DES.decryptDES(data.getData());
                    Runnable payRunnable = new Runnable() {
                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(CommentPayActivity.this);
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
//                  Toast.makeText(PayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
//                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    api.registerApp(Constant.APP_ID);
                    api.sendReq(req);
                    break;
            }
        }
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_comment_pay);
    }

    @Override
    protected void initTitle() {

        FeeItem = getIntent().getIntExtra("FeeItem",0);
        TitleView titleView = (TitleView) findViewById(R.id.comment_pay_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        switch (FeeItem){
            case 1:
                titleView.setMiddleText(R.string.comment_pay);
                break;
            case 2:
                titleView.setMiddleText(R.string.comment_pay2);
                break;
            case 3:
                titleView.setMiddleText(R.string.comment_pay3);
                break;
            default:
                titleView.setMiddleText(R.string.comment_pay);

        }

        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void findView() {
        layout = (LinearLayout) findViewById(R.id.pay_comment_content);
        layout2 = (LinearLayout) findViewById(R.id.ll_error_pay);
        BookingId = getIntent().getStringExtra("BookingId");
        Log.d(TAG, "findView: "+BookingId);

        PayAmount = getIntent().getFloatExtra("PayAmount",0);
        ActMinute = getIntent().getIntExtra("ActMinute",0);
        MinuteFee = getIntent().getFloatExtra("MinuteFee",0);
        Log.d(TAG, "findView: 1"+ ActMinute);
        Log.d(TAG, "findView:2 "+MinuteFee);
//        switch (FeeItem){
//            case 1:
//                layout.setVisibility(View.VISIBLE);
//                break;
//            case 2:
//                layout.setVisibility(View.GONE);
//                break;
//            default:
//
//        }
        if(FeeItem==1){
            layout.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.GONE);
        }else {
            layout.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
        }

        money = (TextView) findViewById(R.id.comment_pay_momey);
        error_money = (TextView) findViewById(R.id.tv_error_text);
        money.setText(String.format(this.getString(R.string.pay_money),PayAmount));
        error_money.setText("违约金"+"￥"+PayAmount);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        cut = (TextView) findViewById(R.id.comment_pay_cut);
        add = (TextView) findViewById(R.id.comment_pay_add);
        number = (EditText) findViewById(R.id.comment_pay_number);
        ratingBars = new RatingBar[4];
        ratingBars[0] = (RatingBar) findViewById(R.id.RatingBarId1);
        ratingBars[1] = (RatingBar) findViewById(R.id.RatingBarId2);
        ratingBars[2] = (RatingBar) findViewById(R.id.RatingBarId3);
        ratingBars[3] = (RatingBar) findViewById(R.id.RatingBarId4);
        comment = (EditText) findViewById(R.id.comment_pay_text);
        radioGroup = (RadioGroup) findViewById(R.id.comment_pay_radioGroup);
        checkBox = (CheckBox) findViewById(R.id.comment_pay_checkBox);
        button = (Button) findViewById(R.id.comment_pay_button);
        hours = (TextView) findViewById(R.id.Hours_text);
        /**
         * 获取当前学时券
         */
        getinfo();
    }

    @Override
    protected void init() {

        hoursNumber =sp.getInt("Hours",0);
        hours.setText(String.format(this.getString(R.string.Huors_text),hoursNumber));
        number.setText("0");
        cut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=Integer.valueOf(number.getText().toString().trim());
                int no=count;
                if(count>0){
                    no = count-1;
                    float Paymenoy =  (PayAmount -(no*60*MinuteFee));
                    if(Paymenoy>0){
                        error_money.setText("违约金"+"￥"+Paymenoy);
                        money.setText(String.format(getString(R.string.pay_money),Paymenoy));
                    }else {
                        error_money.setText("违约金"+"￥"+0);
                        money.setText(String.format(getString(R.string.pay_money),0.0));
                    }
                    Log.d(TAG, "Comment: ");
                }else {
                    MyToast.makeText(CommentPayActivity.this,R.string.cut_end);
                }

                number.setText(String.valueOf(no));
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=Integer.valueOf(number.getText().toString().trim());
                int no=count;
                float Max = ActMinute/60;
                if(count>=hoursNumber){
                    MyToast.makeText(CommentPayActivity.this,R.string.add_big);
                }else if(count>Math.ceil(Max)){
                    MyToast.makeText(CommentPayActivity.this,R.string.add_max);
                }else {
                    no = count+1;
                    float Paymenoy =  (PayAmount -(no*60*MinuteFee));
                    if(Paymenoy>0){
                        error_money.setText("违约金"+"￥"+Paymenoy);
                        money.setText(String.format(getString(R.string.pay_money),Paymenoy));
                    }else {
                        error_money.setText("违约金"+"￥"+0);
                        money.setText(String.format(getString(R.string.pay_money),0.0));
                    }

                }
                number.setText(String.valueOf(no));
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                PayType =checkedId==R.id.comment_pay_radio2 ? 0:1;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                AlertDialog.Builder builder = new AlertDialog.Builder(CommentPayActivity.this);
                builder.setMessage("确认支付吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: 34534534");
                        Comment();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    private void Comment(){
        if(Utils.isNetworkAvailable(CommentPayActivity.this)) {
            Log.d(TAG, "Comment999: "+FeeItem);
            if(FeeItem==1){
                JSONObject json = new JSONObject(true);
                json.put("BookingId",BookingId);
                json.put("Attitude",(int)ratingBars[1].getRating());
                json.put("Technology",(int)ratingBars[0].getRating());
                json.put("Appearance",(int)ratingBars[2].getRating());
                json.put("CarCondition",(int)ratingBars[3].getRating());
                json.put("PayType",PayType);
                if(checkBox.isChecked()){
                    json.put("PayHours",number.getText());
                    Pay =PayAmount -(Integer.valueOf(number.getText().toString().trim())*60*MinuteFee);
                    Log.d(TAG, "Comment: ");
                    Log.d(TAG, "Comment: "+Pay);
                    if(Pay<0){
                        Pay=0;
                        json.put("PayAmount",Pay);
                    }else {
                        json.put("PayAmount",Pay);
                    }
                }else {
                    json.put("PayHours",0);
                    json.put("PayAmount",PayAmount);
                }
                json.put("IsRePay",0);
                json.put("StudentId",sp.getInt("Id",0));
                json.put("Comment",comment.getText());
                Log.d(TAG, "Comment: "+json.toString());
                new MyThread(Constant.URL_Comment, handler, DES.encryptDES(json.toString())).start();
            }else {
                JSONObject json1 = new JSONObject(true);
                json1.put("FeeItem",FeeItem);
                json1.put("PayWay",PayType);
                json1.put("StudentId",sp.getInt("Id",0));
                json1.put("BookingId",BookingId);
                if(checkBox.isChecked()) {
                    json1.put("PayHours",number.getText());
                    Pay =PayAmount -(Integer.valueOf(number.getText().toString().trim())*60*MinuteFee);
                    if(Pay<0){
                        Pay=0;
                        json1.put("PayAmount",Pay);
                    }else {
                        json1.put("PayAmount",Pay);
                    }
                }else {
                    json1.put("PayHours",0);
                    json1.put("PayAmount",PayAmount);
                }
                Log.d(TAG, "Comment: "+json1.toString());
                new MyThread(Constant.URL_PayOtherFee,handler, DES.encryptDES(json1.toString())).start();

            }
        }else {
            MyToast.makeText(CommentPayActivity.this,R.string.Toast_internet);
        }
    }

    private void getinfo(){
        JSONObject json2 = new JSONObject(true);
        json2.put("StudentId",sp.getInt("Id",0));
        new MyThread(Constant.MyStudentinfo,handler1, DES.encryptDES(json2.toString())).start();
    }

    protected Handler handler1 = new MyHandler(this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                try {
                    Log.d(TAG, "handleMessage: "+msg.obj);
                    ReturnData data= JSONObject.parseObject((String) msg.obj,ReturnData.class);
                    if(data.getStatus()==0){
                        JSONObject jsonObject = JSON.parseObject(data.getData());
                        sp.edit().putInt("Hours",jsonObject.getInteger("Coupon")).apply();
                    }else {
                        MyToast.makeText(CommentPayActivity.this,data.getMessage());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    MyToast.makeText(CommentPayActivity.this,"数据出错");
                }
            }
        }
    };

}
