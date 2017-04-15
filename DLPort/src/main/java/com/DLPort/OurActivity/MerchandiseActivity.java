package com.DLPort.OurActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.BaseActivity;
import com.DLPort.myadapter.MerchandiseAdapter;
import com.DLPort.mydata.Merchandise;
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
 * Created by fuyzh on 16/5/18.
 * 积分商城
 */
public class MerchandiseActivity extends BaseActivity {
    private static final String TAG = "MerchandiseActivity";

    private GridView gridView;
    private MerchandiseAdapter adapter;
    private ArrayList<Merchandise> merchandiseData;

    private Handler handler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    JSONObject jsonUser = null;
                    jsonUser = new JSONObject((String) msg.obj);
//                    Log.d(TAG, "响应数据:" + jsonUser.toString());
                    int status = jsonUser.getInt("Status");
                    if (0 == status) {
                        merchandiseData = progressListData(jsonUser.get("Data").toString());
                        adapter.setList(merchandiseData);
                        adapter.notifyDataSetChanged();
                    }else if(1 == status||-1==status) {
                        MyToast.makeText(MerchandiseActivity.this, jsonUser.getString("Message"));
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            }else if (msg.what == 2){
                Log.i(TAG, "响应不正常");

                MyToast.makeText(MerchandiseActivity.this, "服务器异常");
            }else if (msg.what == 3) {
                // 编码异常
                Log.i(TAG, "编码异常");
            } else if (msg.what == 4) {
                // ClientProtocolException
                Log.i(TAG, "ClientProtocolException");
            } else if (msg.what == 5) {
                // IO异常
                Log.i(TAG, "IO异常");
                MyToast.makeText(MerchandiseActivity.this, "服务器连接异常");
            }
        }

        private ArrayList<Merchandise> progressListData(String jsonData) {
            ArrayList<Merchandise> list = new ArrayList<>();

            try {
                JSONObject object = new JSONObject(jsonData);
                JSONArray jsonArray = object.getJSONArray("MerchandiseList");
                Log.d(TAG,object.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject tmp = new JSONObject(jsonArray.getString(i));
                    Merchandise merchandise = new Merchandise();
                    merchandise.setMerchandiseName(tmp.getString("MerchandiseName"));
                    merchandise.setMerchandiseId(tmp.getString("Id"));
                    merchandise.setMerchandiseDescribe(tmp.getString("Describe"));
                    merchandise.setMerchandisePrice(tmp.getInt("Price"));
                    merchandise.setMerchandiseImage(tmp.getString("Image"));
                    merchandise.setMerchandiseCreateTime(tmp.getString("CreateTime"));

                    list.add(merchandise);
                }
                return list;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchandise);
        initTitle();
        initView();
        getMerchandiseList(1);
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.merchandise_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.My);
        titleView.setMiddleText(R.string.merchandise);
        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        final Bundle bundle = getIntent().getExtras();
        merchandiseData = new ArrayList<>();
        gridView = (GridView) findViewById(R.id.my_grid);
        adapter = new MerchandiseAdapter(R.layout.merchandise_item, this, merchandiseData);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                bundle.putSerializable("merchandise", merchandiseData.get(position));
                intent.putExtras(bundle);
                intent.setClass(MerchandiseActivity.this, MerchandiseExchangeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getMerchandiseList(int pageIndex) {
        if(GlobalParams.isNetworkAvailable(MerchandiseActivity.this)) {
                JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("PageSize", 20);
                jsonObject.put("PageIndex", pageIndex);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new MyThread(Constant.URL_EcshopPostGetMessage, handler, jsonObject,MerchandiseActivity.this).start();
        }else{
            MyToast.makeText(MerchandiseActivity.this, "亲,网络未连接");

        }
    }
}
