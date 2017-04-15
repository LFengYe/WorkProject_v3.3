package com.DLPort.OurActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.BaseActivity;
import com.DLPort.myadapter.PrivilegeAdapter;
import com.DLPort.mydata.Privilege;
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
 * 我的优惠券
 */
public class PrivilegeActivity extends BaseActivity {
    private static final String TAG = "PrivilegeActivity";

    private ListView privilegeList;
    private PrivilegeAdapter privilegeAdapter;
    private ArrayList<Privilege> privilegeListData;

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
                        privilegeListData = progressListData(jsonUser.get("Data").toString());
                        privilegeAdapter.setList(privilegeListData);
                        privilegeAdapter.notifyDataSetChanged();
                    }else if(1 == status||-1==status) {
                        MyToast.makeText(PrivilegeActivity.this, jsonUser.getString("Message"));
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            }else if (msg.what == 2){
                Log.i(TAG, "响应不正常");

                MyToast.makeText(PrivilegeActivity.this, "服务器异常");
            }else if (msg.what == 3) {
                // 编码异常
                Log.i(TAG, "编码异常");
            } else if (msg.what == 4) {
                // ClientProtocolException
                Log.i(TAG, "ClientProtocolException");
            } else if (msg.what == 5) {
                // IO异常
                Log.i(TAG, "IO异常");
                MyToast.makeText(PrivilegeActivity.this, "服务器连接异常");
            }
        }

        private ArrayList<Privilege> progressListData(String jsonData) {
            ArrayList<Privilege> list = new ArrayList<>();
            try {
                JSONArray object = new JSONArray(jsonData);
                for (int i = 0; i < object.length(); i++) {
                    JSONObject tmp = object.getJSONObject(i);
                    Privilege privilege = new Privilege();
                    privilege.setPrivilegeId(tmp.getInt("Id"));
                    privilege.setAmount(Float.valueOf(tmp.getString("Amount")));
                    privilege.setDescribe(tmp.getString("Describe"));
                    privilege.setDeadline(tmp.getString("Deadline").replace("T", ""));
                    list.add(privilege);
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
        setContentView(R.layout.activity_privilege);
        initTitle();
        init();
        getPrivilegeList();
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.privilege_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.My);
        titleView.setMiddleText(R.string.privilege);
        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        privilegeList = (ListView) findViewById(R.id.privilege_list);
        privilegeListData = new ArrayList<>();
        privilegeAdapter = new PrivilegeAdapter(R.layout.privilege_item, this, privilegeListData);
        privilegeList.setAdapter(privilegeAdapter);
        privilegeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("privilegeId", privilegeListData.get(position).getPrivilegeId());
                intent.putExtra("amount", privilegeListData.get(position).getAmount());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void getPrivilegeList() {
        SharedPreferences sp = null;
        Bundle bundle = getIntent().getExtras();
        int type = bundle.getInt("Type");
        if (type == 0)
            sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        if (type == 1)
            sp = getSharedPreferences("huo", Context.MODE_PRIVATE);

        if(GlobalParams.isNetworkAvailable(PrivilegeActivity.this)) {
            if (null != sp) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("Id", sp.getString("UserId", null));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                new MyThread(Constant.URL_CouponPrivilege, handler, jsonObject, sp.getString("Token", null)).start();
                new MyThread(Constant.URL_CouponPrivilege, handler, jsonObject, PrivilegeActivity.this).start();
            }
        }else{
            MyToast.makeText(PrivilegeActivity.this, "亲,网络未连接");

        }
    }
}
