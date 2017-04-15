package com.DLPort.FindActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.BaseActivity;
import com.DLPort.myadapter.BaoxianListAdapter;
import com.DLPort.mydata.baoxian;
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
 * Created by Administrator on 2016/5/14.
 */
public class baoxianActivity extends BaseActivity {
    private int currentPage =1;
    private static final String TAG="baoxianActivity";
    private BaoxianListAdapter adapter;
    private List<baoxian> mlist = new ArrayList<baoxian>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baoxian);
        initTitle();
        initView();

    }

    private MyHandler handler =new MyHandler(this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    JSONObject jsonUser = null;
                    jsonUser = new JSONObject((String) msg.obj);

                    Log.d(TAG, "Login的响应" + jsonUser.toString());
                    int status = jsonUser.getInt("Status");
                    Log.i(TAG, "状态" + status);
                    if (0 == status) {

                        List<baoxian> orderList =pullUnOrderList(jsonUser);
                        Log.d(TAG, "List========" + orderList.toString());
                        mlist.addAll(orderList);
                        currentPage +=1;
                        Log.d(TAG, String.valueOf(mlist.size()));
                        adapter.notifyDataSetChanged();

                    }else {
                        MyToast.makeText(baoxianActivity.this, "获取失败");
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            }else {
                Log.d(TAG,msg.what+" 服务器异常");
                MyToast.makeText(baoxianActivity.this, msg.what+" 服务器异常");
            }
        }
    };


    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.baoxian_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.find);
        titleView.setMiddleText(R.string.find1);
        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initView() {
        ListView listView = (ListView) findViewById(R.id.baoxian_list);
        adapter = new BaoxianListAdapter(this,R.layout.baoxian_list,mlist);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                baoxian data = mlist.get(position);
                Intent intent = new Intent(baoxianActivity.this,BaoXianContent.class);
                Bundle bundle = new Bundle();
                bundle.putString("Id",data.getId());
                bundle.putString("InsuranceName",data.getInsuranceName());
                bundle.putString("InsuranceType",data.getInsuranceType());
                bundle.putString("Discount",data.getDiscount());
                bundle.putString("Money",data.getMoney());
                bundle.putString("Description",data.getDescription());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        if(GlobalParams.isNetworkAvailable(baoxianActivity.this)) {
            JSONObject json = new JSONObject();
            try {
                json.put("PageIndex", currentPage);
                json.put("PageSize", 6);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new MyThread(Constant.URL_PostGetInsuranceList,handler,json,this).start();

        } else{
            MyToast.makeText(baoxianActivity.this, "亲,网络未连接");
        }

    }

    public List<baoxian> pullUnOrderList (JSONObject obj){

        List<baoxian> Orders = new ArrayList<baoxian>();
        String data = null;
        try {
            data = obj.getString("Data");
            Log.d(TAG,"Data==="+data);
            JSONObject jsonObject = new JSONObject(data);
            String s = jsonObject.getString("insurances");
            JSONArray jsonArray = new JSONArray(s);
            Log.d(TAG, "Array==="+jsonArray.toString());
            for (int i= 0 ;i<jsonArray.length();i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String Id = json.getString("Id");
                String InsuranceName =json.getString("InsuranceName");
                String InsuranceType =json.getString("InsuranceType") ;
                String Discount = json.getString("Discount");
                Log.d(TAG,"youhui++++++"+Discount);
                String Description = json.getString("Description") ;
                String Money = json.getString("Money") ;
                String CreateTime = json.getString("CreateTime") ;
                Orders.add(new baoxian(Id,InsuranceName,InsuranceType,
                        Discount,Description,Money,CreateTime));
                Log.d(TAG,"nihao ====="+Orders.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Orders;
    }

}
