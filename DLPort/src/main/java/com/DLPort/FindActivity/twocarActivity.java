package com.DLPort.FindActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.BaseActivity;
import com.DLPort.myadapter.BaoxianListAdapter;
import com.DLPort.myadapter.TwocarAdapter;
import com.DLPort.mydata.TwoCar;
import com.DLPort.mydata.baoxian;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;
import com.DLPort.myview.TitleView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/14.
 */
public class twocarActivity extends BaseActivity {
    private static final String TAG="twocarActivity";
    private TwocarAdapter adapter;
    private int currentPage =1;
    private List<TwoCar> mlist = new ArrayList<TwoCar>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twocar);
        initTitle();
        findById();
        init();

    }

    private Handler handler =new MyHandler(this){
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

                        List<TwoCar> orderList =pullUnOrderList(jsonUser);
                        Log.d(TAG, "List========" + orderList.toString());
                        mlist.addAll(orderList);
                        currentPage +=1;
                        Log.d(TAG, String.valueOf(mlist.size()));
                        adapter.notifyDataSetChanged();
                    }else {
                        MyToast.makeText(twocarActivity.this, "获取失败");
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            }else {
                Log.d(TAG,msg.what+" 服务器异常");
                MyToast.makeText(twocarActivity.this, msg.what+" 服务器异常");
            }
        }
    };

    private void init() {

    }

    private void findById() {
        ListView listView = (ListView) findViewById(R.id.twocar_list);
        adapter = new TwocarAdapter(this,R.layout.twocar_content,mlist);
        listView.setAdapter(adapter);
        if(GlobalParams.isNetworkAvailable(twocarActivity.this)) {
            JSONObject json = new JSONObject();
            try {
                json.put("PageIndex", currentPage);
                json.put("PageSize", 6);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new MyThread(Constant.URL_PostGetMessage,handler,json,this).start();

        } else{
            MyToast.makeText(twocarActivity.this, "亲,网络未连接");
        }

    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.twocar_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.find);
        titleView.setMiddleText(R.string.find3);
        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public List<TwoCar> pullUnOrderList (JSONObject obj){

        List<TwoCar> Orders = new ArrayList<TwoCar>();
        String data = null;
        try {
            data = obj.getString("Data");
            Log.d(TAG,"Data==="+data);
            JSONObject jsonObject = new JSONObject(data);
            String s = jsonObject.getString("OldCarList");
            JSONArray jsonArray = new JSONArray(s);
            Log.d(TAG, "Array==="+jsonArray.toString());
            for (int i= 0 ;i<jsonArray.length();i++) {
                JSONObject json = jsonArray.getJSONObject(i);

                String CarImage= json.getString("CarImage");
                String CarBrand= json.getString("CarBrand");
                String CarType= json.getString("CarType");
                String Price= json.getString("Price");
                String Telephone= json.getString("Telephone");
                Orders.add(new TwoCar(CarImage,CarBrand, CarType,Price,Telephone));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Orders;
    }



}
