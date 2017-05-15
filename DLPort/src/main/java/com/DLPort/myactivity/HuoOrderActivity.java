package com.DLPort.myactivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myadapter.HuoOrderAdapter;
import com.DLPort.mydata.HuoOrder;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/5/12.
 */
public class HuoOrderActivity extends BaseActivity {
    private static final String TAG="HuoOrderActivity";
    private List<HuoOrder> mlist =new ArrayList<HuoOrder>();

    private PullableListView listView;
    private PullToRefreshLayout xiala;

    private PullToRefreshLayout recordPullToRefreshLayout;// 刷新完成
    private PullableListView recordPullableListView;// 控制加载完成
    private boolean isCompleated = true;
    private int currentPage = 1;
    private int requestType = 0;
    private HuoOrderAdapter adapter;
    private SharedPreferences sp;

    private MyHandler handler =new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what ==1) {

                try {
                    String result = (String) msg.obj;

                    JSONObject obj = new JSONObject((String) msg.obj);
                    int status = obj.getInt("Status");
                    Log.i(TAG, "当前的响应状态" + status + "======" + result.toString());

                    if (status == 0) {
                        String data = obj.getString("Data");
                        JSONObject json = new JSONObject(data);
                        if (json.getString("Cargos").equals("[]")){
                            //MyToast.makeText(HuoOrderActivity.this, "暂时没有抢单记录");
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
                            List<HuoOrder> orderList =pullUnOrderList(obj);
                            //Log.d(TAG,"List========"+orderList.toString());
                            mlist.addAll(orderList);
                            Collections.sort(mlist, comparator);

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
                    }else {
                        isCompleated = true;
                        MyToast.makeText(HuoOrderActivity.this, "亲、目前没有订单");
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
                isCompleated = true;
                MyToast.makeText(HuoOrderActivity.this, msg.what+"服务器异常");
                switch (requestType){
                    case 1:
                        recordPullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                        break;
                    case 2:
                        recordPullableListView.finishLoading();
                        break;
                }
            }
        }
    };

    private Comparator<HuoOrder> comparator = new Comparator<HuoOrder>() {
        @Override
        public int compare(HuoOrder lhs, HuoOrder rhs) {
            try {
                if (rhs.getRemainValue() == lhs.getRemainValue()) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date dateRhs = format.parse(rhs.getCreateTimeY() + " " + rhs.getCreateTimeX());
                    Date dateLhs = format.parse(lhs.getCreateTimeY() + " " + lhs.getCreateTimeX());
                    long rhsTime = dateRhs.getTime();
                    long lhsTime = dateLhs.getTime();
                    return (int) (rhsTime - lhsTime);
                } else {
                    return rhs.getRemainValue() - lhs.getRemainValue();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.huo_order);
        initTitle();
        init();
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.huo_order_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setRightTextVisible(true);
        titleView.setWineText(R.string.my_order);
        titleView.setRightText("刷新");
        titleView.setMiddleText(R.string.Huo);
        titleView.setRightTextListenter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlist.clear();
                adapter.notifyDataSetChanged();
                currentPage = 1;
                firstadd();
            }
        });
        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void init() {
        xiala = (PullToRefreshLayout) findViewById(R.id.huoOrder_view);
        listView = (PullableListView) findViewById(R.id.HuoOrder_list);

        sp = getSharedPreferences("huo", Context.MODE_PRIVATE);
        adapter = new HuoOrderAdapter(this, R.layout.huo_one_order,mlist);
        listView.setAdapter(adapter);
        firstadd();
        xiala.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

                recordPullToRefreshLayout = pullToRefreshLayout;
                if (GlobalParams.isNetworkAvailable(HuoOrderActivity.this)) {
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
                        MyToast.makeText(HuoOrderActivity.this, "正在加载中。。。");
                    }
                } else {
                    isCompleated = true;
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    Toast.makeText(HuoOrderActivity.this, "亲,请连接网络！！！", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });
        listView.setOnLoadListener(new PullableListView.OnLoadListener() {
            @Override
            public void onLoad(PullableListView pullableListView) {
                recordPullableListView = pullableListView;
                if (GlobalParams.isNetworkAvailable(HuoOrderActivity.this)) {
                    if (isCompleated) {
                        requestType = 2;
                        NetRefresh();
                    } else {
                        pullableListView.finishLoading();
                        MyToast.makeText(HuoOrderActivity.this, "正在加载中。。。。");
                    }

                } else {
                    pullableListView.finishLoading();
                    MyToast.makeText(HuoOrderActivity.this, "亲,请连接网络！！！");

                }

            }
        });

    }

    private void firstadd(){
        if (GlobalParams.isNetworkAvailable(HuoOrderActivity.this)) {
            NetRefresh();
        } else {
            Toast.makeText(HuoOrderActivity.this, "亲,请连接网络！！！", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void NetRefresh(){

        JSONObject json = new JSONObject();
        try {
            json.put("UserId",sp.getString("UserId", ""));
            json.put("PageIndex", currentPage);
            json.put("PageSize", Integer.MAX_VALUE);
            Log.d(TAG, json.toString());
            isCompleated=false;
            new MyThread(Constant.URL_PostGetCarOrder,handler,json,HuoOrderActivity.this).start();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<HuoOrder> pullUnOrderList (JSONObject obj){
        List<HuoOrder> Orders = new ArrayList<HuoOrder>();
        String data = null;
        try {
            data = obj.getString("Data");
            JSONObject jsonObject = new JSONObject(data);
            String s = jsonObject.getString("Cargos");
            JSONArray jsonArray = new JSONArray(s);

            for (int i= 0 ;i<jsonArray.length();i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String CreateTime =json.getString("LoadTime");
                //Log.d(TAG,CreateTime);
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
                }
                String OrderStatus =null;
                int statusValue = json.getInt("Status");
                /*
                switch (statusValue) {
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
                */
                String Price=json.getString("Price");
                String CargoId = json.getString("CargoId");

                //Log.d(TAG,Price);
                String Destination = json.getString("Destination");
                String StartAddress = json.getString("StartAddress");
                int ChargeType = json.getInt("ChargeType");
                int isFinish = json.getInt("IsFinish");
                if (isFinish == 1) {
                    OrderStatus = "已完成";
                }
                if (isFinish == 0) {
                    OrderStatus = "未完成";
                }
                int amountValue = json.getInt("Amount");
                int remainValue = json.getInt("Remain");

                Orders.add(new HuoOrder(CreateTimeY,CreateTimeX,CargoId,ContainerType,Remain,
                        BussinessType,OrderStatus,statusValue, Price,StartAddress,Destination,
                        ChargeType, isFinish, remainValue, amountValue));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Orders;
    }

}
