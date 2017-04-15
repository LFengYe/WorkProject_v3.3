package com.DLPort.myactivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myadapter.MyOrderAdapter;
import com.DLPort.mydata.CarOrder;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;
import com.DLPort.myview.PullToRefreshLayout;
import com.DLPort.myview.PullableListView;
import com.DLPort.myview.TitleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/18.
 */
public class MyOrderActivty  extends BaseActivity {
    private static final String TAG="MyOrderActivty";
    private List<CarOrder> mlist =new ArrayList<CarOrder>();
    private PullableListView listView;
    private PullToRefreshLayout xiala;
    private TextView number;
    private PullToRefreshLayout recordPullToRefreshLayout;// 刷新完成
    private PullableListView recordPullableListView;// 控制加载完成
    private SharedPreferences sp;
    private boolean isCompleated = true;
    private int currentPage = 1;
    private int requestType = 0;
    private MyOrderAdapter adapter;
    private TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myorder);
        InitTitle();
        findById();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentPage = 1;
        requestType = 0;
        firstadd();
    }

    private MyHandler handler =new MyHandler(this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what ==1) {
                JSONObject obj;
                try {
                    obj = new JSONObject((String) msg.obj);
                    int status = obj.getInt("Status");

                    Log.d(TAG,obj.toString());
                    if (status == 0) {
                        String data = obj.getString("Data");
                        JSONObject json = new JSONObject(data);
                        number.setText(json.getString("Count"));
                        if (json.getString("OrderList").equals("[]")){
                            switch (requestType){
                                case 1:
                                    recordPullToRefreshLayout
                                            .refreshFinish(PullToRefreshLayout.SUCCEED);
                                    break;
                                case 2:
                                    recordPullableListView.noMoreLoading();
                                    break;
                            }
                            isCompleated = true;
                        }else {
                            if(requestType == 0) {
                                mlist.clear();
                            }
                            List<CarOrder> orderList =pullUnOrderList(obj);

                            mlist.addAll(orderList);
                            currentPage +=1;
                            adapter.notifyDataSetChanged();
                            switch (requestType){
                                case 1:
                                    recordPullToRefreshLayout
                                            .refreshFinish(PullToRefreshLayout.SUCCEED);
                                    break;
                                case 2:
                                    if (orderList.size() > 0)
                                        recordPullableListView.finishLoading();
                                    else
                                        recordPullableListView.noMoreLoading();
                                    break;
                            }
                            isCompleated = true;
                        }
                    } else  {
                        number.setText(String.valueOf(mlist.size()));
                        Log.d(TAG, String.valueOf(mlist.size()));
                        MyToast.makeText(MyOrderActivty.this, "亲、暂时没有订单");
                        isCompleated = true;
                        switch (requestType){
                            case 1:
                                recordPullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                                break;
                            case 2:
                                recordPullableListView.finishLoading();
                                break;
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }else{
                Log.d(TAG,String.valueOf(msg.what));
                if (requestType == 1||requestType==0) {
                    isCompleated = true;
                    recordPullToRefreshLayout
                            .refreshFinish(PullToRefreshLayout.FAIL);
                }else if (requestType == 2||requestType==0){
                    isCompleated = true;
                    recordPullableListView.finishLoading();
                    recordPullableListView.mStateTextView.setText("加载失败");
                }
            }

        }
    };

    private void findById() {
        xiala = (PullToRefreshLayout) findViewById(R.id.refresh_view);
        listView = (PullableListView) findViewById(R.id.myorder_list);
        number = (TextView) findViewById(R.id.order_no);
        name = (TextView) findViewById(R.id.order_name);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    private void InitTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.myorder_title);
        titleView.setBackImageVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
//        initOrders();
        adapter= new MyOrderAdapter(this, R.layout.my_old_order,mlist);
        listView.setAdapter(adapter);
        if(!sp.getString("Principal", "").equals("")) {
            name.setText(sp.getString("Principal", ""));
        }else {
            name.setText(sp.getString("LoginName", ""));
        }

        xiala.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

                recordPullToRefreshLayout = pullToRefreshLayout;
                if (GlobalParams.isNetworkAvailable(MyOrderActivty.this)) {
                    if (isCompleated) {
                        mlist.clear();
                        adapter.notifyDataSetChanged();
                        currentPage = 1;
                        requestType = 1;
                        NetRefresh();
                        if (!listView.mStateTextView.getText().equals("加载更多")) {
                            listView.mStateTextView.setText("加载更多");
                        }
                    } else {
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                        Toast.makeText(MyOrderActivty.this, "亲,请连接网络！！！", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    isCompleated = true;
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    Toast.makeText(MyOrderActivty.this, "亲,请连接网络！！！", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });
        listView.setOnLoadListener(new PullableListView.OnLoadListener() {
            @Override
            public void onLoad(PullableListView pullableListView) {
                recordPullableListView = pullableListView;
                if (GlobalParams.isNetworkAvailable(MyOrderActivty.this)) {
                    if (isCompleated) {
                        requestType = 2;
                        NetRefresh();
                    } else {
                        pullableListView.finishLoading();
                        Toast.makeText(MyOrderActivty.this, "亲,请连接网络！！！", Toast.LENGTH_SHORT)
                                .show();
                    }

                } else {
                    pullableListView.finishLoading();
                    Toast.makeText(MyOrderActivty.this, "亲,请连接网络！！！", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });


    }

    private void firstadd(){
        if (GlobalParams.isNetworkAvailable(MyOrderActivty.this)) {
            NetRefresh();
        } else {
            Toast.makeText(MyOrderActivty.this, "亲,请连接网络！！！", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void NetRefresh(){

        JSONObject json = new JSONObject();

        try {
            json.put("UserId",sp.getString("UserId", ""));
            json.put("PageIndex", String.valueOf(currentPage));
            json.put("PageSize", 10);
            Log.d(TAG, json.toString());
            isCompleated=false;
            new MyThread(Constant.URL_PostGetMyOrder,handler,json,MyOrderActivty.this).start();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public List<CarOrder> pullUnOrderList (JSONObject obj){
        List<CarOrder> carOrders = new ArrayList<CarOrder>();
        try {
            String data = obj.getString("Data");

            JSONArray jsonArray = new JSONObject(data).getJSONArray("OrderList");

            Log.d(TAG,jsonArray.toString());
            for (int i = 0; i < jsonArray.length();i++){

                JSONObject json = jsonArray.getJSONObject(i);
                String CreateTime =json.getString("CreateTime");
                String[] Str =CreateTime.split("T");
                String CreateTimeY = Str[0];
                String CreateTimeX = Str[1];
                String CarNo =json.getString("CarNo");
                String TripLong = json.getString("Kilometre")+"公里";
                String OrderStatus =null;
                int orderStatusValue = json.getInt("OrderStatus");
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
                String Price = json.getString("Price");
                String StartAddress = json.getString("StartAddress");
                String Destination = json.getString("Destination");
                String OrderId = json.getString("OrderId");
                String Trl = json.getString("Tel");

                carOrders.add(new CarOrder(CreateTimeY,CreateTimeX,CarNo,TripLong,
                        orderStatusValue, OrderStatus,Price,StartAddress,Destination,OrderId,Trl));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return carOrders;
    }
}
