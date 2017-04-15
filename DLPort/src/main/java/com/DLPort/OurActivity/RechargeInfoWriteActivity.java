package com.DLPort.OurActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.BaseActivity;
import com.DLPort.myactivity.PayConfirmActivity;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;
import com.DLPort.myview.MyDialog;
import com.DLPort.myview.TitleView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 我要充值
 */
public class RechargeInfoWriteActivity extends BaseActivity {
    private static final String TAG = "RechargeActivity";

    private RelativeLayout bankNoLayout;
    private RelativeLayout usernameLayout;
    private RelativeLayout cerTypeLayout;
    private RelativeLayout cerNoLayout;
    private RelativeLayout rechargeAmtLayout;

    private TextView rechargeAmt;
    private TextView bankNoText;
    private TextView usernameText;
    private TextView cerTypeText;
    private TextView cerNoText;

    private Button confirmPayBtn;
    private Bundle orderBundle;
    private SharedPreferences preferences;

    //证件类型限定为身份证(0)
    private int cerTypeValue = 0;
    private int userType;

    //region 充值请求返回数据处理Handler
    private MyHandler handler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try{
                if (msg.what == 1) {
                    JSONObject object = new JSONObject((String) msg.obj);
                    Log.i(TAG, object.toString());
                    int status = object.getInt("Status");
                    if (status == 0) {
                        orderBundle.putString("mchntorderId", object.getString("Data"));
                        orderBundle.putString("loginName", preferences.getString("LoginName", ""));
                        orderBundle.putFloat("amt", Float.valueOf(rechargeAmt.getText().toString()));
                        if (userType == 0)
                            orderBundle.putString("tag", "CarOwnerRecharge");
                        if (userType == 1)
                            orderBundle.putString("tag", "CargoRecharge");
                        orderBundle.putString("bankNo", bankNoText.getText().toString());
                        orderBundle.putString("username", usernameText.getText().toString());
                        orderBundle.putInt("cerType", cerTypeValue);
                        orderBundle.putString("cerNo", cerNoText.getText().toString());
                        //orderBundle.putFloat("amt", 0.1f);//正式发布去掉

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("bankNo", bankNoText.getText().toString());
                        editor.putString("username", usernameText.getText().toString());
                        editor.putString("cerNo", cerNoText.getText().toString());
                        editor.commit();

                        Intent intent = new Intent();
                        intent.putExtras(orderBundle);
                        intent.setClass(RechargeInfoWriteActivity.this, PayConfirmActivity.class);
                        startActivity(intent);
                    } else {
                        MyToast.makeText(RechargeInfoWriteActivity.this, object.getString("Message"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    //endregion

    //region 页面点击事件
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.recharge_amt_layout: {
                    MyDialog dialog = new MyDialog(RechargeInfoWriteActivity.this, 1);
                    dialog.setContent(getString(R.string.recharge_amt));
                    dialog.sethineText(getString(R.string.recharge_amt_input));
                    dialog.setDialogCallback(new MyDialog.Dialogcallback() {
                        @Override
                        public void dialogdo(String string) {
                            rechargeAmt.setText(string);
                        }
                    });
                    dialog.show();
                    break;
                }
                case R.id.pay_info_bankNo_layout: {
                    MyDialog myDialog = new MyDialog(RechargeInfoWriteActivity.this, 1);
                    myDialog.setContent(getString(R.string.pay_info_bankNo));
                    myDialog.setDialogCallback(new MyDialog.Dialogcallback() {
                        @Override
                        public void dialogdo(String string) {
                            bankNoText.setText(string);
                        }
                    });
                    myDialog.sethineText(getString(R.string.pay_info_bankNo_input));
                    myDialog.show();
                    break;
                }
                case R.id.pay_info_username_layout: {
                    MyDialog myDialog = new MyDialog(RechargeInfoWriteActivity.this, 1);
                    myDialog.setContent(getString(R.string.pay_info_username));
                    myDialog.setDialogCallback(new MyDialog.Dialogcallback() {
                        @Override
                        public void dialogdo(String string) {
                            usernameText.setText(string);
                        }
                    });
                    myDialog.sethineText(getString(R.string.pay_info_username_input));
                    myDialog.show();
                    break;
                }
                case R.id.pay_info_cer_type_layout: {
                    final String[] cerTypeNames = getResources().getStringArray(R.array.cer_type_array);
                    new AlertDialog.Builder(RechargeInfoWriteActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setTitle(R.string.pay_info_cer_type)
                            .setSingleChoiceItems(cerTypeNames, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cerTypeText.setText(cerTypeNames[which]);
                                    cerTypeValue = which;
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    break;
                }
                case R.id.pay_info_cer_no_layout: {
                    MyDialog myDialog = new MyDialog(RechargeInfoWriteActivity.this, 1);
                    myDialog.setContent(getString(R.string.pay_info_cer_no));
                    myDialog.setDialogCallback(new MyDialog.Dialogcallback() {
                        @Override
                        public void dialogdo(String string) {
                            cerNoText.setText(string);
                        }
                    });
                    myDialog.sethineText(getString(R.string.pay_info_cer_no_input));
                    myDialog.show();
                    break;
                }
                case R.id.recharge_confirm: {
                    if (TextUtils.isDigitsOnly(rechargeAmt.getText().toString().trim())) {
                        if (Float.valueOf(rechargeAmt.getText().toString().trim()) >= 500.0f) {
                            recharge();
                        } else {
                            MyToast.makeText(RechargeInfoWriteActivity.this, R.string.recharge_amt_input);
                        }
                    } else {
                        MyToast.makeText(RechargeInfoWriteActivity.this, R.string.recharge_amt_wrong);
                    }

                    break;
                }
            }
        }
    };
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_info);
        initTitle();
        init();
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.recharge_title);
        titleView.setBackImageVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setMiddleText(R.string.statistics_recharge);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        bankNoLayout = (RelativeLayout) findViewById(R.id.pay_info_bankNo_layout);
        bankNoLayout.setOnClickListener(onClickListener);
        usernameLayout = (RelativeLayout) findViewById(R.id.pay_info_username_layout);
        usernameLayout.setOnClickListener(onClickListener);
        cerTypeLayout = (RelativeLayout) findViewById(R.id.pay_info_cer_type_layout);
        cerTypeLayout.setOnClickListener(onClickListener);
        cerTypeLayout.setVisibility(View.GONE);//类型暂时不用选择, 限定为0(身份证)
        cerNoLayout = (RelativeLayout) findViewById(R.id.pay_info_cer_no_layout);
        cerNoLayout.setOnClickListener(onClickListener);
        rechargeAmtLayout = (RelativeLayout) findViewById(R.id.recharge_amt_layout);
        rechargeAmtLayout.setOnClickListener(onClickListener);

        rechargeAmt = (TextView) findViewById(R.id.recharge_amt);
        bankNoText = (TextView) findViewById(R.id.pay_info_bankNo);
        usernameText = (TextView) findViewById(R.id.pay_info_username);
        cerTypeText = (TextView) findViewById(R.id.pay_info_cer_type);
        cerNoText = (TextView) findViewById(R.id.pay_info_cer_no);

        confirmPayBtn = (Button) findViewById(R.id.recharge_confirm);
        confirmPayBtn.setOnClickListener(onClickListener);

        orderBundle = getIntent().getExtras();
        userType = orderBundle.getInt("Type");
        if (userType == 0)
            preferences = getSharedPreferences("user", MODE_PRIVATE);
        if (userType == 1)
            preferences = getSharedPreferences("huo", MODE_PRIVATE);

        bankNoText.setText(preferences.getString("bankNo", ""));
        usernameText.setText(preferences.getString("username", ""));
        cerNoText.setText(preferences.getString("cerNo", ""));

    }

    private void recharge() {
        if(GlobalParams.isNetworkAvailable(RechargeInfoWriteActivity.this)) {
            if (null != preferences) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("UserId", preferences.getString("UserId", ""));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new MyThread(Constant.URL_PaymentPostRecharge, handler, jsonObject, RechargeInfoWriteActivity.this).start();
            }
        }else{
            MyToast.makeText(RechargeInfoWriteActivity.this, "亲,网络未连接");

        }
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(bankNoText.getText())) {
            MyToast.makeText(this, R.string.pay_info_bankNo_input);
            return false;
        }
        if (TextUtils.isEmpty(usernameText.getText())) {
            MyToast.makeText(this, R.string.pay_info_username_input);
            return false;
        }
        if (TextUtils.isEmpty(cerNoText.getText())) {
            MyToast.makeText(this, R.string.pay_info_cer_no_input);
            return false;
        }
        if (cerTypeValue == -1) {
            MyToast.makeText(this, R.string.pay_info_cer_type_input);
            return false;
        }
        return true;
    }
}
