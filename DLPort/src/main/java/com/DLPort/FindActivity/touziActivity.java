package com.DLPort.FindActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.BaseActivity;
import com.DLPort.myadapter.BaoxianListAdapter;
import com.DLPort.myadapter.TouziAdapter;
import com.DLPort.mydata.Touzi;
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
public class touziActivity extends BaseActivity {
    private int currentPage =1;
    private static final String TAG="touziActivity";
    private TouziAdapter adapter;
    private List<Touzi> mlist = new ArrayList<Touzi>();

    private Handler handler =new MyHandler(this) {
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

                        List<Touzi> orderList =pullUnOrderList(jsonUser);
                        Log.d(TAG, "List========" + orderList.toString());
                        mlist.addAll(orderList);
                        currentPage +=1;
                        Log.d(TAG, String.valueOf(mlist.size()));
                        adapter.notifyDataSetChanged();

                    }else {
                        MyToast.makeText(touziActivity.this, "获取失败");
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            }else {
                Log.d(TAG,msg.what+" 服务器异常");
                MyToast.makeText(touziActivity.this, msg.what+" 服务器异常");
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.licai);
        initTitle();
        initView();
    }

    private void initView() {
        ListView listView = (ListView) findViewById(R.id.licai_list);
        adapter = new TouziAdapter(this,R.layout.licar_list,mlist);
        listView.setAdapter(adapter);
        if(GlobalParams.isNetworkAvailable(touziActivity.this)) {
            JSONObject json = new JSONObject();
            try {
                json.put("PageIndex", currentPage);
                json.put("PageSize", 10);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new MyThread(Constant.URL_PostGetlicai,handler,json,this).start();

        } else{
            MyToast.makeText(touziActivity.this, "亲,网络未连接");
        }

    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.licai_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.find);
        titleView.setMiddleText(R.string.find5);
        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public List<Touzi> pullUnOrderList (JSONObject obj){

        List<Touzi> Orders = new ArrayList<Touzi>();
        String data = null;
        try {
            data = obj.getString("Data");
            Log.d(TAG,"Data==="+data);
            JSONObject jsonObject = new JSONObject(data);
            String s = jsonObject.getString("ManageMoneys");
            JSONArray jsonArray = new JSONArray(s);
            Log.d(TAG, "Array==="+jsonArray.toString());
            for (int i= 0 ;i<jsonArray.length();i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                int Id = json.getInt("Id");
                String ManageMoneyName = json.getString("ManageMoneyName");
                String ManageMoneyIntro = json.getString("ManageMoneyIntro");
                Orders.add(new Touzi(ManageMoneyName,ManageMoneyIntro,Id));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Orders;
    }

}
