package com.DLPort.myactivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.mytool.MyToast;
import com.DLPort.myview.MyDialog;
import com.DLPort.myview.TitleView;

/**
 * Created by fuyzh on 16/6/7.
 */
public class PayInfoWriteActivity extends BaseActivity {

    private RelativeLayout bankNoLayout;
    private RelativeLayout usernameLayout;
    private RelativeLayout cerTypeLayout;
    private RelativeLayout cerNoLayout;

    private TextView bankNoText;
    private TextView usernameText;
    private TextView cerTypeText;
    private TextView cerNoText;
    private TextView amtText;

    private Button confirmPayBtn;
    private Bundle orderBundle;
    private SharedPreferences preferences;

    //证件类型限定为身份证(0)
    private int cerTypeValue = 0;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pay_info_bankNo_layout: {
                    MyDialog myDialog = new MyDialog(PayInfoWriteActivity.this, 1);
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
                    MyDialog myDialog = new MyDialog(PayInfoWriteActivity.this, 1);
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
                    new AlertDialog.Builder(PayInfoWriteActivity.this)
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
                    MyDialog myDialog = new MyDialog(PayInfoWriteActivity.this, 1);
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
                case R.id.pay_info_confirm: {
//                    if (checkInput()) {
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
                    intent.setClass(PayInfoWriteActivity.this, PayConfirmActivity.class);
                    startActivity(intent);
//                    }
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_info);
        initTitle();
        init();
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.pay_info_title);
        titleView.setBackImageVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setMiddleText(R.string.pay_confirm);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        preferences = getSharedPreferences("payInfo", MODE_PRIVATE);

        bankNoLayout = (RelativeLayout) findViewById(R.id.pay_info_bankNo_layout);
        bankNoLayout.setOnClickListener(onClickListener);
        usernameLayout = (RelativeLayout) findViewById(R.id.pay_info_username_layout);
        usernameLayout.setOnClickListener(onClickListener);
        cerTypeLayout = (RelativeLayout) findViewById(R.id.pay_info_cer_type_layout);
        cerTypeLayout.setOnClickListener(onClickListener);
        cerTypeLayout.setVisibility(View.GONE);//类型暂时不用选择, 限定为0(身份证)
        cerNoLayout = (RelativeLayout) findViewById(R.id.pay_info_cer_no_layout);
        cerNoLayout.setOnClickListener(onClickListener);

        bankNoText = (TextView) findViewById(R.id.pay_info_bankNo);
        usernameText = (TextView) findViewById(R.id.pay_info_username);
        cerTypeText = (TextView) findViewById(R.id.pay_info_cer_type);
        cerNoText = (TextView) findViewById(R.id.pay_info_cer_no);
        amtText = (TextView) findViewById(R.id.pay_info_amt);

        confirmPayBtn = (Button) findViewById(R.id.pay_info_confirm);
        confirmPayBtn.setOnClickListener(onClickListener);

        orderBundle = getIntent().getExtras();
        amtText.setText("¥" + orderBundle.getFloat("amt"));

        bankNoText.setText(preferences.getString("bankNo", ""));
        usernameText.setText(preferences.getString("username", ""));
        cerNoText.setText(preferences.getString("cerNo", ""));

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
