package com.DLPort.OurActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.BaseActivity;
import com.DLPort.mydata.Merchandise;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;
import com.DLPort.myview.MyDialog;
import com.DLPort.myview.TitleView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fuyzh on 16/7/12.
 */
public class MerchandiseExchangeActivity extends BaseActivity {
    private static final String TAG = "ExchangeActivity";

    private ImageView exchangeImg;
    private TextView exchangeName;
    private TextView exchangeDescribe;
    private TextView exchangePrice;
    private TextView exchangeAddress;
    private TextView exchangePhoneNum;

    private RelativeLayout addressLayout;
    private RelativeLayout phoneNumLayout;

    private Button exchangeBtn;
    private int userType;

    private MyHandler handler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 1) {
                    JSONObject object = new JSONObject((String) msg.obj);
                    Log.i(TAG, object.toString());
                    MyToast.makeText(MerchandiseExchangeActivity.this, object.getString("Message"));
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchandise_exchange);
        initTitle();
        initView();
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.exchange_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.merchandise);
        titleView.setMiddleText(R.string.exchange);
        View view = findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        userType = bundle.getInt("Type");
        final Merchandise merchandise = (Merchandise) bundle.getSerializable("merchandise");

        exchangeImg = (ImageView) findViewById(R.id.exchange_image);
        ImageLoader.getInstance().displayImage(merchandise.getMerchandiseImage(), exchangeImg);

        exchangeName = (TextView) findViewById(R.id.exchange_name);
        exchangeName.setText(merchandise.getMerchandiseName());

        exchangeDescribe = (TextView) findViewById(R.id.exchange_describe);
        exchangeDescribe.setText(merchandise.getMerchandiseDescribe());

        exchangePrice = (TextView) findViewById(R.id.exchange_price);
        exchangePrice.setText(merchandise.getMerchandisePrice() + "积分");

        exchangeAddress = (TextView) findViewById(R.id.exchange_address);
        addressLayout = (RelativeLayout) findViewById(R.id.exchange_address_layout);
        addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog dialog = new MyDialog(MerchandiseExchangeActivity.this, 1);
                dialog.setContent(getString(R.string.exchange_address));
                dialog.sethineText(getString(R.string.exchange_address_input));
                dialog.setDialogCallback(new MyDialog.Dialogcallback() {
                    @Override
                    public void dialogdo(String string) {
                        exchangeAddress.setText(string);
                    }
                });
                dialog.show();
            }
        });

        exchangePhoneNum = (TextView) findViewById(R.id.exchange_phoneNum);
        phoneNumLayout = (RelativeLayout) findViewById(R.id.exchange_phoneNum_layout);
        phoneNumLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog dialog = new MyDialog(MerchandiseExchangeActivity.this, 1);
                dialog.setContent(getString(R.string.exchange_phoneNum));
                dialog.sethineText(getString(R.string.exchange_phoneNum_input));
                dialog.setDialogCallback(new MyDialog.Dialogcallback() {
                    @Override
                    public void dialogdo(String string) {
                        exchangePhoneNum.setText(string);
                    }
                });
                dialog.show();
            }
        });

        exchangeBtn = (Button) findViewById(R.id.exchange_btn);
        exchangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exchange(merchandise);
            }
        });
    }

    private void exchange(Merchandise merchandise) {
        if (GlobalParams.isNetworkAvailable(this)) {
            try {
                SharedPreferences preferences = null;
                if (userType == 0) {
                    preferences = getSharedPreferences("user", MODE_PRIVATE);
                }
                if (userType == 1) {
                    preferences = getSharedPreferences("huo", MODE_PRIVATE);
                }
                JSONObject object = new JSONObject();
                object.put("UserId", (null != preferences) ? (preferences.getString("UserId", "")) : "");
                object.put("MerchandiseId", merchandise.getMerchandiseId());
                object.put("Address", exchangeAddress.getText().toString());
                object.put("Tel", exchangePhoneNum.getText().toString());
                object.put("Integral", merchandise.getMerchandisePrice());
                new MyThread(Constant.URL_EcshopPostExchange, handler, object, this).start();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            MyToast.makeText(this, "亲,网络未连接");
        }
    }
}
