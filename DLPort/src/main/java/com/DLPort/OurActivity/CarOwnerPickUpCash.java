package com.DLPort.OurActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.BaseActivity;
import com.DLPort.mydata.PickUpCashRecord;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;
import com.DLPort.myview.MyDialog;
import com.DLPort.myview.TitleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fuyzh on 16/6/17.
 */
public class CarOwnerPickUpCash extends BaseActivity {
    private static final String TAG = "CarOwnerPickUpCash";

    private RelativeLayout amountLayout;
    private RelativeLayout bankNameLayout;
    private RelativeLayout cardNumLayout;
    private RelativeLayout nameLayout;
    private RelativeLayout telLayout;

    private TextView promoteText;
    private TextView recordText;
    private TextView amountText;
    private TextView bankNameText;
    private TextView cardNumText;
    private TextView nameText;
    private TextView telText;

    private Button confirmBtn;

    private LinearLayout bankListLayout;
    private ListView bankRecordList;
    private ArrayAdapter adapter;
    private String[] bankList;
    private ArrayList<PickUpCashRecord> bankListData;

    private float maxPickCashValue;
    private String userId;

    private Handler bankListHandler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    JSONObject jsonUser = null;
                    jsonUser = new JSONObject((String) msg.obj);
                    Log.d(TAG, "响应数据:" + jsonUser.toString());
                    int status = jsonUser.getInt("Status");
                    if (status == 0) {
                        progressBankList(jsonUser.getString("Data"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                MyToast.makeText(CarOwnerPickUpCash.this, "服务器异常");
            }
        }

        private void progressBankList(String jsonData) {
            try {
                JSONArray array = new JSONArray(jsonData);
                bankListData = new ArrayList<>();
                bankList = new String[array.length()];
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);

                    PickUpCashRecord record = new PickUpCashRecord();
                    record.setBankName(object.getString("BankName"));
                    record.setName(object.getString("Name"));
                    record.setTel(object.getString("Tel"));
                    record.setCardNumber(object.getString("CardNumber"));
                    bankListData.add(record);

                    String tmp = record.getName() + "\n" +
                            record.getBankName() + "\n" +
                            record.getCardNumber();
                    bankList[i] = tmp;
                }

                if (bankList.length == 0) {
                    bankListLayout.setVisibility(View.GONE);
                } else if (bankList.length > 0) {
                    adapter = new ArrayAdapter(CarOwnerPickUpCash.this,
                            android.R.layout.simple_list_item_single_choice, bankList);
                    bankRecordList.setAdapter(adapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Handler pickUpHandler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    JSONObject jsonUser = null;
                    jsonUser = new JSONObject((String) msg.obj);
                    Log.d(TAG, "响应数据:" + jsonUser.toString());
                    int status = jsonUser.getInt("Status");
                    if (status == 0) {
                        MyToast.makeText(CarOwnerPickUpCash.this, jsonUser.getString("Message"));
                        finish();
                    } else {
                        MyToast.makeText(CarOwnerPickUpCash.this, jsonUser.getString("Message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                MyToast.makeText(CarOwnerPickUpCash.this, "服务器异常");
            }
        }
    };

    //region 页面点击事件
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pickup_cash_record:
                {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("UserId", userId);
                    intent.putExtras(bundle);
                    intent.setClass(CarOwnerPickUpCash.this, CarOwnerPickUpRecord.class);
                    startActivity(intent);
                    break;
                }
                case R.id.pickup_cash_amount_layout:
                {
                    MyDialog myDialog = new MyDialog(CarOwnerPickUpCash.this, 1);
                    myDialog.setContent(getString(R.string.pickup_cash_amount));
                    myDialog.setDialogCallback(new MyDialog.Dialogcallback() {
                        @Override
                        public void dialogdo(String string) {
                            amountText.setText(string);
                        }
                    });
                    myDialog.sethineText(getString(R.string.pickup_cash_amount_input));
                    myDialog.show();
                    break;
                }
                case R.id.pickup_cash_bank_name_layout:
                {
                    MyDialog myDialog = new MyDialog(CarOwnerPickUpCash.this, 1);
                    myDialog.setContent(getString(R.string.pickup_cash_bank_name));
                    myDialog.setDialogCallback(new MyDialog.Dialogcallback() {
                        @Override
                        public void dialogdo(String string) {
                            bankNameText.setText(string);
                        }
                    });
                    myDialog.sethineText(getString(R.string.pickup_cash_bank_name_input));
                    myDialog.show();
                    break;
                }
                case R.id.pickup_cash_card_num_layout:
                {
                    MyDialog myDialog = new MyDialog(CarOwnerPickUpCash.this, 1);
                    myDialog.setContent(getString(R.string.pickup_cash_card_num));
                    myDialog.setDialogCallback(new MyDialog.Dialogcallback() {
                        @Override
                        public void dialogdo(String string) {
                            cardNumText.setText(string);
                        }
                    });
                    myDialog.sethineText(getString(R.string.pickup_cash_card_num_input));
                    myDialog.show();
                    break;
                }
                case R.id.pickup_cash_name_layout:
                {
                    MyDialog myDialog = new MyDialog(CarOwnerPickUpCash.this, 1);
                    myDialog.setContent(getString(R.string.pickup_cash_name));
                    myDialog.setDialogCallback(new MyDialog.Dialogcallback() {
                        @Override
                        public void dialogdo(String string) {
                            nameText.setText(string);
                        }
                    });
                    myDialog.sethineText(getString(R.string.pickup_cash_name_input));
                    myDialog.show();
                    break;
                }
                case R.id.pickup_cash_tel_layout:
                {
                    MyDialog myDialog = new MyDialog(CarOwnerPickUpCash.this, 1);
                    myDialog.setContent(getString(R.string.pickup_cash_tel));
                    myDialog.setDialogCallback(new MyDialog.Dialogcallback() {
                        @Override
                        public void dialogdo(String string) {
                            telText.setText(string);
                        }
                    });
                    myDialog.sethineText(getString(R.string.pickup_cash_tel_input));
                    myDialog.show();
                    break;
                }
                case R.id.pickup_cash_confirm:
                {
                    if (checkHasEmpty()) {
                        pickUpCash();
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
        setContentView(R.layout.activity_pick_up_cash);
        initTitle();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.pickup_cash_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setRightTextVisible(true);
        titleView.setWineText(R.string.statistics);
        titleView.setMiddleText(R.string.pickup_cash);
        titleView.setRightText(R.string.pickup_record);
        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleView.setRightTextListenter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("UserId", userId);
                intent.putExtras(bundle);
                intent.setClass(CarOwnerPickUpCash.this, CarOwnerPickUpRecord.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        amountLayout = (RelativeLayout) findViewById(R.id.pickup_cash_amount_layout);
        amountLayout.setOnClickListener(clickListener);
        bankNameLayout = (RelativeLayout) findViewById(R.id.pickup_cash_bank_name_layout);
        bankNameLayout.setOnClickListener(clickListener);
        cardNumLayout = (RelativeLayout) findViewById(R.id.pickup_cash_card_num_layout);
        cardNumLayout.setOnClickListener(clickListener);
        nameLayout = (RelativeLayout) findViewById(R.id.pickup_cash_name_layout);
        nameLayout.setOnClickListener(clickListener);
        telLayout = (RelativeLayout) findViewById(R.id.pickup_cash_tel_layout);
        telLayout.setOnClickListener(clickListener);

        promoteText = (TextView) findViewById(R.id.pickup_cash_promote);
        recordText = (TextView) findViewById(R.id.pickup_cash_record);
        recordText.setOnClickListener(clickListener);
        amountText = (TextView) findViewById(R.id.pickup_cash_amount);
        bankNameText = (TextView) findViewById(R.id.pickup_cash_bank_name);
        cardNumText = (TextView) findViewById(R.id.pickup_cash_card_num);
        nameText = (TextView) findViewById(R.id.pickup_cash_name);
        telText = (TextView) findViewById(R.id.pickup_cash_tel);

        confirmBtn = (Button) findViewById(R.id.pickup_cash_confirm);
        confirmBtn.setOnClickListener(clickListener);

        bankListLayout = (LinearLayout) findViewById(R.id.pickup_cash_bank_list_layout);
        bankRecordList = (ListView) findViewById(R.id.pickup_cash_bank_list);
        bankRecordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PickUpCashRecord record = bankListData.get(position);
                bankNameText.setText(record.getBankName());
                cardNumText.setText(record.getCardNumber());
                nameText.setText(record.getName());
                telText.setText(record.getTel());
            }
        });
        bankList = new String[0];
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, bankList);
        bankRecordList.setAdapter(adapter);

        Bundle bundle = getIntent().getExtras();
        maxPickCashValue = bundle.getFloat("PickUpValue");
        userId = bundle.getString("UserId");
        promoteText.setText(String.format(getResources().getString(R.string.pickup_cash_promote), maxPickCashValue));
        getBankList();
    }

    private void getBankList() {
        if (GlobalParams.isNetworkAvailable(CarOwnerPickUpCash.this)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Id", userId);
                new MyThread(Constant.URL_PaymentPostWithdrawCashList, bankListHandler,
                        jsonObject, CarOwnerPickUpCash.this).start();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            MyToast.makeText(CarOwnerPickUpCash.this, "亲,网络未连接");
        }
    }

    private boolean checkHasEmpty() {
        if (TextUtils.isEmpty(amountText.getText())) {
            MyToast.makeText(this, R.string.pickup_cash_amount_input);
            return false;
        }
        if (TextUtils.isEmpty(bankNameText.getText())) {
            MyToast.makeText(this, R.string.pickup_cash_bank_name_input);
            return false;
        }
        if (TextUtils.isEmpty(cardNumText.getText())) {
            MyToast.makeText(this, R.string.pickup_cash_card_num_input);
            return false;
        }
        if (TextUtils.isEmpty(nameText.getText())) {
            MyToast.makeText(this, R.string.pickup_cash_name_input);
            return false;
        }
        if (TextUtils.isEmpty(telText.getText())) {
            MyToast.makeText(this, R.string.pickup_cash_tel_input);
            return false;
        }
        return true;
    }

    private void pickUpCash() {
        if (GlobalParams.isNetworkAvailable(CarOwnerPickUpCash.this)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Amount", Float.valueOf(amountText.getText().toString()));
                jsonObject.put("BankNmae", bankNameText.getText().toString());
                jsonObject.put("CardNumber", cardNumText.getText().toString());
                jsonObject.put("Name", nameText.getText().toString());
                jsonObject.put("Tel", telText.getText().toString());
                jsonObject.put("UserId", userId);
                new MyThread(Constant.URL_PaymentWithdrawCash, pickUpHandler,
                        jsonObject, CarOwnerPickUpCash.this).start();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            MyToast.makeText(CarOwnerPickUpCash.this, "亲,网络未连接");
        }
    }
}
