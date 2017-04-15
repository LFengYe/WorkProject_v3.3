package com.DLPort.OurActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.BaseActivity;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;
import com.DLPort.myview.MyDialog;
import com.DLPort.myview.TitleView;

import org.json.JSONException;
import org.json.JSONObject;

import static com.DLPort.R.id.account_contact_layout;

/**
 * Created by Administrator on 2016/5/14.
 */
public class CarOwnerSetInfoActivity extends BaseActivity {
    private static final String TAG = "CarOwnerSetInfo";

//    private RelativeLayout usernameLayout;
    private RelativeLayout contactLayout;
    private RelativeLayout phoneNumberLayout;
    private RelativeLayout addressLayout;
    private RelativeLayout companyLayout;
    private RelativeLayout passwordLayout;

    private TextView username;
    private TextView contact;
    private TextView phoneNumber;
    private TextView address;
    private TextView company;
    private TextView password;

    private int modifyType;
    private String modifyInfo;
    private SharedPreferences preferences;

    private Handler handler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    JSONObject jsonUser = null;
                    jsonUser = new JSONObject((String) msg.obj);
                    Log.d(TAG, "响应数据:" + jsonUser.toString());
                    int status = jsonUser.getInt("Status");
                    if (0 == status) {
                        modifyLocalUserInfo(modifyType, modifyInfo);
                    }
                    MyToast.makeText(CarOwnerSetInfoActivity.this, jsonUser.getString("Message"));
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            }else if (msg.what == 2){
                Log.i(TAG, "响应不正常");

                MyToast.makeText(CarOwnerSetInfoActivity.this, "服务器异常");
            }else if (msg.what == 3) {
                // 编码异常
                Log.i(TAG, "编码异常");
            } else if (msg.what == 4) {
                // ClientProtocolException
                Log.i(TAG, "ClientProtocolException");
            } else if (msg.what == 5) {
                // IO异常
                Log.i(TAG, "IO异常");
                MyToast.makeText(CarOwnerSetInfoActivity.this, "服务器连接异常");
            }
        }

        /**
         * 修改本地车主用户信息
         * @param type 0表示修改联系方式，1表示修改地址，2表示修改公司名称，3表示修改负责人
         * @param info
         */
        private void modifyLocalUserInfo(int type, String info) {
            if (null == preferences)
                preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            switch (type) {
                case 0:
                    editor.putString("Telephoen", info);
                    break;
                case 1:
                    editor.putString("Address", info);
                    break;
                case 2:
                    editor.putString("Companyname", info);
                    break;
                case 3:
                    editor.putString("Principal", info);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carowner_set_info);
        initTitle();
        initView();
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.my_info_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.set);
        titleView.setMiddleText(R.string.user_info);
        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        if (null == preferences)
            preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        modifyType = -1;

        username = (TextView) findViewById(R.id.account_username);
        username.setText(preferences.getString("LoginName", null));
//        usernameLayout = (RelativeLayout) findViewById(R.id.account_username_layout);

        contact = (TextView) findViewById(R.id.account_contact);
        contact.setText(preferences.getString("Principal", null));
        contactLayout = (RelativeLayout) findViewById(account_contact_layout);
        contactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyType = 3;
                MyDialog dialog = new MyDialog(CarOwnerSetInfoActivity.this, 1);
                dialog.setContent(getResources().getString(R.string.account_contact));
                dialog.sethineText(getResources().getString(R.string.account_contact));
                dialog.setDialogCallback(new MyDialog.Dialogcallback() {
                    @Override
                    public void dialogdo(String string) {
                        contact.setText(string);
                        modifyInfo = string;
                        modifyUserInfo(string, 3);
                    }
                });
                dialog.show();
            }
        });

        phoneNumber = (TextView) findViewById(R.id.account_phone_number);
        phoneNumber.setText(preferences.getString("Telephoen", null));
        phoneNumberLayout = (RelativeLayout) findViewById(R.id.account_phone_number_layout);
        phoneNumberLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyType = 0;
                MyDialog dialog = new MyDialog(CarOwnerSetInfoActivity.this, 1);
                dialog.setContent(getResources().getString(R.string.account_phone_number));
                dialog.sethineText(getResources().getString(R.string.account_phone_number));
                dialog.setDialogCallback(new MyDialog.Dialogcallback() {
                    @Override
                    public void dialogdo(String string) {
                        phoneNumber.setText(string);
                        modifyInfo = string;
                        modifyUserInfo(string, 0);
                    }
                });
                dialog.show();
            }
        });

        address = (TextView) findViewById(R.id.account_address);
        address.setText(preferences.getString("Address", null));
        addressLayout = (RelativeLayout) findViewById(R.id.account_address_layout);
        addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyType = 1;
                MyDialog dialog = new MyDialog(CarOwnerSetInfoActivity.this, 1);
                dialog.setContent(getResources().getString(R.string.account_address));
                dialog.sethineText(getResources().getString(R.string.account_address));
                dialog.setDialogCallback(new MyDialog.Dialogcallback() {
                    @Override
                    public void dialogdo(String string) {
                        address.setText(string);
                        modifyInfo = string;
                        modifyUserInfo(string, 1);
                    }
                });
                dialog.show();
            }
        });

        company = (TextView) findViewById(R.id.my_set_info_company);
        company.setText(preferences.getString("Companyname", null));
        companyLayout = (RelativeLayout) findViewById(R.id.my_set_info_company_layout);
        companyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyType = 2;
                MyDialog dialog = new MyDialog(CarOwnerSetInfoActivity.this, 1);
                dialog.setContent(getResources().getString(R.string.my_set_info_company));
                dialog.sethineText(getResources().getString(R.string.my_set_info_company));
                dialog.setDialogCallback(new MyDialog.Dialogcallback() {
                    @Override
                    public void dialogdo(String string) {
                        company.setText(string);
                        modifyInfo = string;
                        modifyUserInfo(string, 2);
                    }
                });
                dialog.show();
            }
        });

        password = (TextView) findViewById(R.id.my_set_info_password);
        password.setText(R.string.my_set_info_modify_password);
        passwordLayout = (RelativeLayout) findViewById(R.id.my_set_info_password_layout);
        passwordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("Type", 0);//用户类型为车主
                intent.putExtras(bundle);
                intent.setClass(CarOwnerSetInfoActivity.this, ModifyPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 修改用戶信息
     * @param modifyInfo 修改后的信息
     * @param modifyType 0表示修改联系方式，1表示修改地址，2表示修改公司名称，3表示修改负责人，
     */
    private void modifyUserInfo(String modifyInfo, int modifyType) {
        if (null == preferences)
            preferences = getSharedPreferences("user", Context.MODE_PRIVATE);

        if(GlobalParams.isNetworkAvailable(CarOwnerSetInfoActivity.this)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("UserId", preferences.getString("UserId", null));
                jsonObject.put("TelOrAdd", modifyInfo);
                jsonObject.put("UpdateType", modifyType);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new MyThread(Constant.URL_UserPostUpdateTelOrAdd, handler, jsonObject,this).start();
        }else{
            MyToast.makeText(CarOwnerSetInfoActivity.this, "亲,网络未连接");

        }
    }
}
