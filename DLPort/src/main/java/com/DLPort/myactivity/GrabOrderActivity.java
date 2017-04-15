package com.DLPort.myactivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.mydata.CarInfo;
import com.DLPort.mydata.Order;
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
 * Created by Administrator on 2016/5/11.
 */
public class GrabOrderActivity extends BaseActivity {
    private static final String TAG = "GrabOrderActivity";

    private SharedPreferences sp;
    private TextView[] textViews;
    private ListView carListView;
    private Button button;

    private String CargoId;
    private ArrayList<String> carNos;
    ArrayAdapter adapter;
    private JSONArray jsonCarId;

    //region 抢单承运结果返回处理
    private MyHandler handler2 = new MyHandler(this) {
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
                        finish();
                    }
                    MyToast.makeText(GrabOrderActivity.this, jsonUser.getString("Message"));
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            } else {
                MyToast.makeText(GrabOrderActivity.this, msg.what + " 服务器异常");
            }
        }
    };
    //endregion

    //region 获取(刷新)订单信息数据返回处理
    private MyHandler handler1 = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    JSONObject jsonUser = null;
                    jsonUser = new JSONObject((String) msg.obj);
                    Log.d(TAG, "Login的响应" + jsonUser.toString());
                    int status = jsonUser.getInt("Status");
                    if (status == 0) {
                        String data = jsonUser.getString("Data");
                        JSONObject object = new JSONObject(data);
                        textViews[0].setText(object.getString("Price"));
                        textViews[1].setText(object.getString("Remain") + "/" + object.getString("Amount"));
                        textViews[2].setText(object.getString("LoadTime").replace("T", " "));
                        textViews[3].setText(object.getString("StartAddress"));
                        textViews[4].setText(object.getString("Destination"));
                        if(object.getInt("ChargeType")==0){
                            textViews[5].setText("平台结算");
                        }else {
                            textViews[5].setText("自行结算");
                        }
                        textViews[6].setText(object.getString("ShipCompany"));
                        String CreateTime =object.getString("InPortTime");
                        String[] Str =CreateTime.split("T");
                        textViews[7].setText(Str[0]+" "+Str[1]);
                    } else {
                        MyToast.makeText(GrabOrderActivity.this, jsonUser.getString("Message"));
                    }
                } catch (JSONException e) {
                    MyToast.makeText(GrabOrderActivity.this, "返回数据异常");
                }

                getMyCar();
            } else {
                MyToast.makeText(GrabOrderActivity.this, "服务器异常");
            }
        }
    };
    //endregion

    //region 获取车辆列表数据返回处理
    private Handler handler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    JSONObject jsonUser = null;
                    jsonUser = new JSONObject((String) msg.obj);
                    Log.d(TAG, "Login的响应" + jsonUser.toString());
                    int status = jsonUser.getInt("Status");
                    if (0 == status) {
                        String data = jsonUser.getString("Data");
                        JSONArray jsonData = new JSONArray(data);

                        if (null != carNos) {
                            carNos.clear();
                        } else {
                            carNos = new ArrayList<>();
                        }
                        for (int i = 0; i < jsonData.length(); i++) {
                            JSONObject json = jsonData.getJSONObject(i);
                            carNos.add(json.getString("VehNof"));
                        }
                        adapter.notifyDataSetChanged();

                    } else if (1 == status || -1 == status) {
                        MyToast.makeText(GrabOrderActivity.this, jsonUser.getString("Message"));
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, msg.what + " 服务器异常");
                MyToast.makeText(GrabOrderActivity.this, "服务器异常");
            }
        }
    };
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grab_order);
        initTitle();
        init();
    }

    private void init() {
        textViews = new TextView[8];
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        textViews[0] = (TextView) findViewById(R.id.Grad_Price);
        textViews[1] = (TextView) findViewById(R.id.Grad_Remain);
        textViews[2] = (TextView) findViewById(R.id.Grad_LoadTime);
        textViews[3] = (TextView) findViewById(R.id.Grad_StartAddres);
        textViews[4] = (TextView) findViewById(R.id.Grad_Destination);
        textViews[5] = (TextView) findViewById(R.id.Grad_ChargeType);
        textViews[6] = (TextView) findViewById(R.id.Grad_ShipCompany);
        textViews[7] = (TextView) findViewById(R.id.Grad_InPortTime);

        carListView = (ListView) findViewById(R.id.grab_car_list);
        carNos = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, carNos);
        carListView.setAdapter(adapter);
        carListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (carListView.isItemChecked(position)) {
                    Log.i(TAG, "item click checked carNo:" + carNos.get(position));
                } else {
                    Log.i(TAG, "item click unchecked carNo:" + carNos.get(position));
                }
            }
        });

        button = (Button) findViewById(R.id.Grad_Button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray booleanArray = carListView.getCheckedItemPositions();
                jsonCarId = new JSONArray();
                for (int i = 0; i < adapter.getCount(); i++) {
                    Log.i(TAG, "item: " + i + "___" + booleanArray.get(i));
                    if (booleanArray.get(i)) {
                        jsonCarId.put(carNos.get(i));
                    }
                }
                if (jsonCarId.length() <= 0) {
                    MyToast.makeText(GrabOrderActivity.this, "未选中车辆, 不能抢单!");
                    return;
                }

                if (GlobalParams.isNetworkAvailable(GrabOrderActivity.this)) {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("CarNoList", jsonCarId);
                        json.put("CargoId", CargoId);
                        Log.d(TAG, "jsong=========" + jsonCarId.toString());
                        json.put("UserId", sp.getString("UserId", ""));

                        new MyThread(Constant.URL_PostGraspOrder, handler2, json, GrabOrderActivity.this).start();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    MyToast.makeText(GrabOrderActivity.this, "亲,网络未连接");
                }

            }
        });

        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        CargoId = bundle.getString("CargoId");
        getOrder();
    }

    private void initTitle() {

        TitleView titleView = (TitleView) findViewById(R.id.grab_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setRightTextVisible(true);
        titleView.setWineText(R.string.O_pic3);
        titleView.setRightText("刷新");
        titleView.setMiddleText(R.string.Grad_order);
        titleView.setRightTextListenter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrder();
            }
        });
        View view = findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getMyCar() {
        if (GlobalParams.isNetworkAvailable(GrabOrderActivity.this)) {
            JSONObject json = new JSONObject();
            try {
                json.put("UserId", sp.getString("UserId", ""));
                new MyThread(Constant.URL_PostGetMyCar, handler, json, GrabOrderActivity.this).start();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            MyToast.makeText(GrabOrderActivity.this, "亲,网络未连接");
        }

    }

    public void getOrder() {
        if (GlobalParams.isNetworkAvailable(GrabOrderActivity.this)) {
            JSONObject json = new JSONObject();
            try {
                json.put("Id", CargoId);
                new MyThread(Constant.URL_PostRefresh, handler1, json, GrabOrderActivity.this).start();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            MyToast.makeText(GrabOrderActivity.this, "亲,网络未连接");
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(1, 1);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
}
