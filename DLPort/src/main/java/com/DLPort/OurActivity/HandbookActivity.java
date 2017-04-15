package com.DLPort.OurActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.BaseActivity;
import com.DLPort.myadapter.HandbookAdapter;
import com.DLPort.mydata.Handbook;
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
 * 平台指南
 */
public class HandbookActivity extends BaseActivity {
    private static final String TAG = "HandbookActivity";

    private ListView handbookList;
    private HandbookAdapter handbookAdapter;
    private ArrayList<Handbook> handbookListData;

    private Handler handler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    JSONObject jsonUser = null;
                    jsonUser = new JSONObject((String) msg.obj);
                    Log.d(TAG, "响应数据:" + jsonUser.toString());
                    int status = jsonUser.getInt("Status");
                    if (0 == status) {
                        handbookListData = progressListData(jsonUser.get("Data").toString());
                        handbookAdapter.setList(handbookListData);
                        handbookAdapter.notifyDataSetChanged();
                    }else if(1 == status||-1==status) {
                        MyToast.makeText(HandbookActivity.this, jsonUser.getString("Message"));
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            }else if (msg.what == 2){
                Log.i(TAG, "响应不正常");

                MyToast.makeText(HandbookActivity.this, "服务器异常");
            }else if (msg.what == 3) {
                // 编码异常
                Log.i(TAG, "编码异常");
            } else if (msg.what == 4) {
                // ClientProtocolException
                Log.i(TAG, "ClientProtocolException");
            } else if (msg.what == 5) {
                // IO异常
                Log.i(TAG, "IO异常");
                MyToast.makeText(HandbookActivity.this, "服务器连接异常");
            }
        }

        private ArrayList<Handbook> progressListData(String jsonData) {
            ArrayList<Handbook> list = new ArrayList<>();
            try {
                JSONArray object = new JSONArray(jsonData);
                for (int i = 0; i < object.length(); i++) {
                    JSONObject tmp = object.getJSONObject(i);
                    Handbook handbook = new Handbook();
                    handbook.setId(tmp.getInt("Id"));
                    handbook.setProblem(tmp.getString("Problem"));
                    handbook.setAnswer(tmp.getString("Answer"));
                    handbook.setCreateTime(tmp.getString("CreateTime"));
                    handbook.setUserType(tmp.getInt("UserType"));
                    list.add(handbook);
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
        setContentView(R.layout.activity_handbook);
        initTitle();
        init();
        getHandbookList();
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.handbook_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.My);
        titleView.setMiddleText(R.string.handbook);
        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        handbookList = (ListView) findViewById(R.id.handbook_list);
        handbookListData = new ArrayList<>();
        handbookAdapter = new HandbookAdapter(R.layout.handbook_item, this, handbookListData);
        handbookList.setAdapter(handbookAdapter);
    }

    private void getHandbookList() {
        SharedPreferences sp = null;
        Bundle bundle = getIntent().getExtras();
        int type = bundle.getInt("Type");
        if (type == 0)
            sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        if (type == 1)
            sp = getSharedPreferences("huo", Context.MODE_PRIVATE);

        if (GlobalParams.isNetworkAvailable(HandbookActivity.this)) {
            if (null != sp) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("Type", type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                new MyThread(Constant.URL_HandbookPostHandbook, handler, jsonObject, sp.getString("Token", null)).start();
                new MyThread(Constant.URL_HandbookPostHandbook, handler, jsonObject, HandbookActivity.this).start();
            }
        } else {
            MyToast.makeText(HandbookActivity.this, "亲,网络未连接");
        }
    }
}
