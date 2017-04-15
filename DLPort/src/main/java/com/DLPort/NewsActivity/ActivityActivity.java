package com.DLPort.NewsActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.BaseActivity;
import com.DLPort.myadapter.MessageAdapter;
import com.DLPort.mydata.ActivityMessage;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;
import com.DLPort.mytool.VeDate;
import com.DLPort.myview.PullToRefreshLayout;
import com.DLPort.myview.PullableListView;
import com.DLPort.myview.TitleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fuyzh on 16/5/20.
 */
public class ActivityActivity extends BaseActivity {
    private static final String TAG = "ActivityActivity";
    private static final int pageSize = 6;

    private PullToRefreshLayout refreshLayout;
    private PullableListView pullableListView;

    private boolean isCompleted = true;
    private int currentPage = 1;
    private int requestType = 0;
    private ArrayList<ActivityMessage> messageList;
    private MessageAdapter adapter;

    private MyHandler handler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    JSONObject obj = new JSONObject((String) msg.obj);
                    Log.i(TAG, (String) msg.obj);
                    int status = obj.getInt("Status");
                    if (status == 0) {
                        JSONObject data = new JSONObject(obj.getString("Data"));
                        JSONArray listData = new JSONArray(data.getString("MessageList"));
                        if (listData.length() > 0) {
                            if (requestType == 0)
                                messageList.clear();
                            ArrayList<ActivityMessage> increaseList = progressListData(listData);
                            if (null != increaseList)
                                messageList.addAll(increaseList);
                            currentPage += 1;
                            adapter.notifyDataSetChanged();
                            if (requestType == 1) {
                                refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                            }
                            if (requestType == 2) {
                                if (increaseList.size() > 0)
                                    pullableListView.finishLoading();
                                else
                                    pullableListView.noMoreLoading();
                            }
                        } else {
                            if (requestType == 1) {
                                refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                            }
                            if (requestType == 2) {
                                pullableListView.noMoreLoading();
                            }
                            MyToast.makeText(ActivityActivity.this, getResources().getString(R.string.no_data));
                        }
                        isCompleted = true;
                    } else {
                        isCompleted = true;
                        if (requestType == 1) {
                            refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        }
                        if (requestType == 2) {
                            pullableListView.noMoreLoading();
                        }
                        MyToast.makeText(ActivityActivity.this, getResources().getString(R.string.no_data));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private ArrayList<ActivityMessage> progressListData(JSONArray jsonData) {
            ArrayList<ActivityMessage> list = new ArrayList<>();
            try {
                for (int i = 0; i < jsonData.length(); i++) {
                    ActivityMessage tmp = new ActivityMessage();
                    JSONObject object = jsonData.getJSONObject(i);
                    tmp.setMsgId(object.getString("Id"));
                    tmp.setMsgTypeA(object.getString("MsgTypeA"));
                    tmp.setMsgTypeB(object.getString("MsgTypeB"));
                    tmp.setMsgContent(object.getString("MsgContent"));
                    tmp.setCreateTime(VeDate.getFormatDateString(object.getString("CreateTime").replace("T", " "), 1, 1));
                    list.add(tmp);
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
        setContentView(R.layout.activity_notice_children);
        initTitle();
        initView();
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.notice_children_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.news3);
        titleView.setMiddleText(R.string.activity);
        View view = findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        /*加载更多*/
        pullableListView = (PullableListView) findViewById(R.id.notice_children_list);
        pullableListView.setOnLoadListener(new PullableListView.OnLoadListener() {
            @Override
            public void onLoad(PullableListView pullableListView) {
                if (GlobalParams.isNetworkAvailable(ActivityActivity.this)) {
                    if (isCompleted) {
                        requestType = 2;
                        getMessageList(currentPage, pageSize);
                    } else {
                        pullableListView.finishLoading();
                        MyToast.makeText(ActivityActivity.this, getResources().getString(R.string.loading));
                    }
                } else {
                    pullableListView.finishLoading();
                    MyToast.makeText(ActivityActivity.this, getResources().getString(R.string.no_network_promote));
                }
            }
        });

        /*下拉刷新*/
        refreshLayout = (PullToRefreshLayout) findViewById(R.id.notice_children_view);
        refreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (GlobalParams.isNetworkAvailable(ActivityActivity.this)) {
                    if (isCompleted) {
                        messageList.clear();
                        adapter.notifyDataSetChanged();
                        currentPage = 1;
                        requestType = 1;
                        getMessageList(currentPage, pageSize);
                        pullableListView.finishLoading();
                    } else {
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                        MyToast.makeText(ActivityActivity.this, getResources().getString(R.string.loading));
                    }
                } else {
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    MyToast.makeText(ActivityActivity.this, getResources().getString(R.string.no_network_promote));
                }
            }
        });


        messageList = new ArrayList<>();
        adapter = new MessageAdapter(this, messageList, R.layout.message_item);
        pullableListView.setAdapter(adapter);
        if (GlobalParams.isNetworkAvailable(ActivityActivity.this)) {
            getMessageList(1, pageSize);
        } else {
            MyToast.makeText(ActivityActivity.this, getResources().getString(R.string.no_network_promote));
        }
    }

    private void getMessageList(int pageIndex, int pageSize) {
        JSONObject json = new JSONObject();
        try {
            json.put("PageIndex", pageIndex);
            json.put("PageSize", pageSize);
            isCompleted = false;

            new MyThread(Constant.URL_MessagePostActivity, handler, json,ActivityActivity.this).start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
