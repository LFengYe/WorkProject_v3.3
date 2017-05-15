package com.DLPort.myactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myadapter.huoInquireAdapter;
import com.DLPort.mydata.HuoInquire;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;
import com.DLPort.myview.TitleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/15.
 */
public class HuoInquireActivity extends BaseActivity {
    private String OrderId;
    private static final String TAG="HuoInquireActivity";
    private List<HuoInquire> mlist = new ArrayList<HuoInquire>();
    private TextView[] textViews;
    private ListView listView;
    private huoInquireAdapter adapter;
    private TextView delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.huo_order_xiangqing);
        initTitle();
        init();
    }
    private MyHandler handler =new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    JSONObject jsonUser = new JSONObject((String) msg.obj);
                    Log.d(TAG, "Login的响应" + jsonUser.toString());
                    int status = jsonUser.getInt("Status");
                    Log.i(TAG, "状态" + status);
                    if (0 == status) {
                        String data = jsonUser.getString("Data");
                        if (data.equals("[]")) {
                            MyToast.makeText(HuoInquireActivity.this, "目前订单没被抢");
                        } else {
                            display(data);
                        }
                    } else if (1 == status || -1 == status) {
                        MyToast.makeText(HuoInquireActivity.this, "获取失败");
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            } else {
                Log.i(TAG, "IO异常");
                MyToast.makeText(HuoInquireActivity.this, "服务器连接异常");
            }
        }

    };

    private MyHandler handler1 =new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                try {
                    JSONObject jsonUser = null;
                    jsonUser = new JSONObject((String) msg.obj);
                    Log.d(TAG, jsonUser.toString());
                    int status = jsonUser.getInt("Status");
                    Log.d(TAG, jsonUser.getInt("Status") + jsonUser.getString("Message"));
                    MyToast.makeText(HuoInquireActivity.this, jsonUser.getString("Message"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {

                MyToast.makeText(HuoInquireActivity.this, msg.what + " 服务器异常");
            }
        }
    };
    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.xiangqing_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setRightTextVisible(true);
        titleView.setWineText(R.string.my_order);
        titleView.setRightText("刷新");
        titleView.setMiddleText(R.string.order_par);
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
                refreshData();
            }
        });

    }
    private void init() {
        textViews = new TextView[3];
        textViews[0]= (TextView) findViewById(R.id.huo_Price);
        textViews[1] = (TextView) findViewById(R.id.huo_Remain);
        textViews[2] = (TextView) findViewById(R.id.huo_ChargeType);
        listView = (ListView) findViewById(R.id.huo_list);
        adapter = new huoInquireAdapter(this,R.layout.huo_details,mlist);
        listView.setAdapter(adapter);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        OrderId = bundle.getString("ID");
        textViews[0].setText(bundle.getString("Price"));
        textViews[1].setText(bundle.getString("Amount"));
        if(bundle.getInt("ChargeType")==0){
            textViews[2].setText("平台结算");
        }else {
            textViews[2].setText("自行结算");
        }

        delete = (TextView) findViewById(R.id.delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalParams.isNetworkAvailable(HuoInquireActivity.this)) {

                    JSONObject json = new JSONObject();
                    try {
                        json.put("Id", OrderId);
                        new MyThread(Constant.URL_CargoOwnerPostRemove, handler1, json,HuoInquireActivity.this).start();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    MyToast.makeText(HuoInquireActivity.this, "请把数据补充完整！！");
                }
            }
        });

        getdata();

    }

    public void refreshData() {
        mlist.clear();
        adapter.notifyDataSetChanged();
        getdata();
    }

    public void getdata() {
        if (GlobalParams.isNetworkAvailable(HuoInquireActivity.this)) {

            JSONObject json = new JSONObject();
            try {
                json.put("Cargoid", OrderId);
                Log.d(TAG, json.toString());
                new MyThread(Constant.URL_GetOrderDetails, handler, json,HuoInquireActivity.this).start();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(HuoInquireActivity.this, "亲,请连接网络！！！", Toast.LENGTH_SHORT)
                    .show();
        }
    }
    private void display(String data){

        try {
            JSONArray jsonArray = new JSONArray(data);
            Log.d(TAG, jsonArray.toString());
            for (int i= 0 ;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String Principal =jsonObject.getString("Principal");
                String CarNo = jsonObject.getString("CarNo");
                String SuitCaseNo = jsonObject.getString("SuitCaseNo");
                String Tel = jsonObject.getString("Tel");
                String ChargeStatus="'";
                String OrderStatus ="";
                int chargeStatusValue = jsonObject.getInt("ChargeStatus");
                int orderStatusValue = jsonObject.getInt("OrderStatus");

                switch (orderStatusValue) {
                    case 0:
                        OrderStatus = "进行中";
                        break;
                    case 1:
                        OrderStatus = "已完成";
                        break;
                    case 2:
                        OrderStatus = "申请取消";
                        break;
                    case 3:
                        OrderStatus = "已取消";
                        break;
                    case 4:
                        OrderStatus = "拒绝取消";
                        break;
                    case 5:
                        OrderStatus = "同意取消";
                }
                switch (chargeStatusValue){
                    case 0:
                        ChargeStatus="未结算";
                        break;
                    case 1:
                        ChargeStatus="已结算";
                }

                String OrderId = jsonObject.getString("OrderId");
                String PresentNumber = jsonObject.getString("PresentNumber");
                String PutBoxID = jsonObject.getString("PutBoxID");
                mlist.add(new HuoInquire(Principal,CarNo,SuitCaseNo,Tel,OrderStatus,ChargeStatus,
                        OrderId, orderStatusValue, chargeStatusValue,PutBoxID,PresentNumber));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
    }

}
