package com.DLPort.OurActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.BaseActivity;
import com.DLPort.myadapter.PickUpRecordAdapter;
import com.DLPort.mydata.PickUpCashRecord;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;
import com.DLPort.myview.TitleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fuyzh on 16/6/17.
 */
public class CarOwnerPickUpRecord extends BaseActivity {
    private static final String TAG = "CarOwnerPickUpRecord";

    private ListView recordList;
    private ArrayList<PickUpCashRecord> listData;
    private PickUpRecordAdapter adapter;
    private String userId;

    private Handler listHandler = new MyHandler(this) {
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
                        progressRecordList(jsonUser.getString("Data"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                MyToast.makeText(CarOwnerPickUpRecord.this, "服务器异常");
            }
        }

        private void progressRecordList(String jsonData) {
            try {
                JSONArray array = new JSONArray(jsonData);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    PickUpCashRecord record = new PickUpCashRecord();
                    record.setBankName(object.getString("BankName"));
                    record.setName(object.getString("Name"));
                    record.setTel(object.getString("Tel"));
                    record.setCardNumber(object.getString("CardNumber"));
                    record.setPickUpId(object.getInt("Id"));
                    record.setPickUpAmount(Float.valueOf(object.getString("Amount")));
                    record.setCreateTime(object.getString("CreateTime"));
                    record.setState(object.getString("State"));
                    record.setUserId(object.getString("UserId"));
                    listData.add(record);
                }

                adapter.setList(listData);
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up_record);
        initTitle();
        initView();
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.pickup_record_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.pickup_cash);
        titleView.setMiddleText(R.string.pickup_record);
        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        recordList = (ListView) findViewById(R.id.pickup_record_list);
        listData = new ArrayList<>();
        adapter = new PickUpRecordAdapter(this, listData, R.layout.pick_up_record_item);
        recordList.setAdapter(adapter);

        Bundle bundle = getIntent().getExtras();
        userId = bundle.getString("UserId");
        getPickUpRecordList();
    }

    private void getPickUpRecordList() {
        if (GlobalParams.isNetworkAvailable(CarOwnerPickUpRecord.this)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Id", userId);
                Log.e(TAG, "userId:" + userId);
                new MyThread(Constant.URL_PaymentPostHistory, listHandler,
                        jsonObject, CarOwnerPickUpRecord.this).start();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            MyToast.makeText(CarOwnerPickUpRecord.this, "亲,网络未连接");
        }
    }
}
