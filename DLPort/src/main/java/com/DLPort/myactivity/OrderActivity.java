package com.DLPort.myactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myadapter.OrderAdapter;
import com.DLPort.mydata.Order;
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
 * Created by Administrator on 2016/4/17.
 */
public class OrderActivity extends BaseActivity {
    private static final String TAG="OrderActivity";

    private List<Order> mlist =new ArrayList<Order>();
    private PullableListView listView;
    private PullToRefreshLayout xiala;

    private PullToRefreshLayout recordPullToRefreshLayout;// 刷新完成
    private PullableListView recordPullableListView;// 控制加载完成
    private boolean isCompleated = true;

    private int currentPage = 1;
    private int requestType = 0;
    private OrderAdapter adapter;
    private String time ="";

    private int dataType;

    //region 获取抢单记录Handler
    private Handler handler =new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what ==1) {

                JSONObject obj;
                try {
                    String result = (String) msg.obj;
                    Log.d(TAG,result);
                    obj = new JSONObject((String) msg.obj);
                    int status = obj.getInt("Status");
                    Log.i(TAG, "当前的响应状态" + status + "======" + result.toString());
                    if (status == 0) {
                        String data = obj.getString("Data");
                        JSONObject json = new JSONObject(data);
                        if (json.getString("Cargos").equals("[]")){
                            isCompleated = true;
                            MyToast.makeText(OrderActivity.this, "暂时没有抢单记录");
                            switch (requestType){
                                case 1:
                                    recordPullToRefreshLayout
                                            .refreshFinish(PullToRefreshLayout.SUCCEED);
                                    break;
                                case 2:
                                    recordPullableListView.noMoreLoading();
                                    break;
                            }
                        } else {
                            if(requestType == 0) {
                                mlist.clear();
                            }
                            List<Order> orderList =pullUnOrderList(obj);
                            mlist.addAll(orderList);
                            currentPage +=1;
                            Log.d(TAG, String.valueOf(mlist.size()));
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
                    }else if(1 == status||-1 == status) {
                        MyToast.makeText(OrderActivity.this, "亲、目前没有订单");
                        switch (requestType){
                            case 1:
                                recordPullToRefreshLayout
                                        .refreshFinish(PullToRefreshLayout.SUCCEED);
                                break;
                            case 2:
                                recordPullableListView.finishLoading();
                                break;
                        }
                        isCompleated = true;
                    }

                }catch (JSONException e){

                }
            }else{
                Log.d(TAG,String.valueOf(msg.what));

                isCompleated = true;
                MyToast.makeText(OrderActivity.this, msg.what + "服务器异常");
                switch (requestType){
                    case 1:
                        recordPullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                        break;
                    case 2:
                        recordPullableListView.finishLoading();
                        recordPullableListView.mStateTextView.setText("加载失败");
                        break;
                }
            }

        }
    };
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attention_order);
        initTitle();
        init();
    }

    private void initTitle() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        dataType = bundle.getInt("Type");
        if(dataType==0){
            TitleView titleView = (TitleView) findViewById(R.id.title_order);
            titleView.setLeftViewVisible(true);
            titleView.setMiddleTextVisible(true);
            titleView.setRightTextVisible(false);
            titleView.setWineText(R.string.O_pic3);
//            titleView.setRightText("刷新");
            titleView.setMiddleText(R.string.Car);
            View view =findViewById(R.id.title_back);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }else {
            TitleView titleView = (TitleView) findViewById(R.id.title_order);
            titleView.setLeftViewVisible(true);
            titleView.setMiddleTextVisible(true);
            titleView.setRightTextVisible(false);
            titleView.setWineText(R.string.O_pic5);
//            titleView.setRightText("刷新");
            titleView.setMiddleText(R.string.Car);
            View view =findViewById(R.id.title_back);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

    }

    private void init() {
        xiala = (PullToRefreshLayout) findViewById(R.id.attention_view);
        listView = (PullableListView) findViewById(R.id.attentionorder_list);
//        initOrders();

        adapter = new OrderAdapter(this, R.layout.order,mlist);
        listView.setAdapter(adapter);
        firstadd();
        xiala.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

                recordPullToRefreshLayout = pullToRefreshLayout;
                if (GlobalParams.isNetworkAvailable(OrderActivity.this)) {
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
                        Toast.makeText(OrderActivity.this, "亲,请连接网络！！！", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    isCompleated = true;
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    Toast.makeText(OrderActivity.this, "亲,请连接网络！！！", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });
        listView.setOnLoadListener(new PullableListView.OnLoadListener() {
            @Override
            public void onLoad(PullableListView pullableListView) {
                recordPullableListView = pullableListView;
                if (GlobalParams.isNetworkAvailable(OrderActivity.this)) {
                    if (isCompleated) {
                        requestType = 2;
                        NetRefresh();
                    } else {
                        // 没有网
                        pullableListView.finishLoading();
                        Toast.makeText(OrderActivity.this, "亲,请连接网络！！！", Toast.LENGTH_SHORT)
                                .show();
                    }

                } else {
                    pullableListView.finishLoading();
                    Toast.makeText(OrderActivity.this, "亲,请连接网络！！！", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });
    }

    private void firstadd(){
        if (GlobalParams.isNetworkAvailable(OrderActivity.this)) {
            NetRefresh();

        } else {

            Toast.makeText(OrderActivity.this, "亲,请连接网络！！！", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void NetRefresh(){

        JSONObject json = new JSONObject();
        try {
            if(dataType==0){
                json.put("IsPooling",false);
            }else {
                json.put("IsPooling",true);
            }
            json.put("FirstQueryTime",time);
            json.put("PageIndex", currentPage);
            json.put("PageSize", 10);

            Log.d(TAG, json.toString());

            isCompleated=false;
            new MyThread(Constant.URL_PostGrabOrder,handler,json,this).start();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public List<Order> pullUnOrderList (JSONObject obj){
        List<Order> Orders = new ArrayList<Order>();
        String data = null;
        try {
            data = obj.getString("Data");
            JSONArray jsonArray = new JSONObject(data).getJSONArray("Cargos");
            time =new JSONObject(data).getString("FirstQueryTime");
            Log.d(TAG,jsonArray.toString());
            for (int i= 0 ;i<jsonArray.length();i++) {
                JSONObject json = jsonArray.getJSONObject(i);

                String CreateTime =json.getString("LoadTime");
                String[] Str =CreateTime.split("T");
                String CreateTimeY = Str[0];
                String CreateTimeX = Str[1];
                String ContainerType = GlobalParams.GetContainerType(json.getInt("ContainerType"));
                String Remain = json.getString("Remain")+"/"+json.getString("Amount");
                String BussinessType = null;
                switch (json.getInt("BussinessType")){
                    case 0:
                        BussinessType="出口";
                        break;
                    case 1:
                        BussinessType="进口";
                        break;
                }

                int IsFinish = json.getInt("IsFinish");
                String Price=json.getString("Price");
                String CargoId = json.getString("CargoId");
                String Destination = json.getString("Destination");
                String StartAddress = json.getString("StartAddress");
                String ShipCompany = json.getString("ShipCompany");
                String InPortTime = json.getString("InPortTime");
                int ChargeType = json.getInt("ChargeType");
                int status = json.getInt("Status");

                Orders.add(new Order(CreateTimeY,CreateTimeX,ContainerType,Remain,
                        BussinessType,Price,Destination,StartAddress,CargoId,IsFinish,
                        ShipCompany,InPortTime,ChargeType, status));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Orders;
    }
}
