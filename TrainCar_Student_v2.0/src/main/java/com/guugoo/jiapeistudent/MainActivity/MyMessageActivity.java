package com.guugoo.jiapeistudent.MainActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.guugoo.jiapeistudent.Adapter.MessageAdapter;
import com.guugoo.jiapeistudent.App.Constant;
import com.guugoo.jiapeistudent.Data.Message;
import com.guugoo.jiapeistudent.Data.ReturnData;
import com.guugoo.jiapeistudent.R;
import com.guugoo.jiapeistudent.Tools.DES;
import com.guugoo.jiapeistudent.Tools.MyHandler;
import com.guugoo.jiapeistudent.Tools.MyThread;
import com.guugoo.jiapeistudent.Tools.MyToast;
import com.guugoo.jiapeistudent.Tools.Utils;
import com.guugoo.jiapeistudent.Views.PullToRefreshLayout;
import com.guugoo.jiapeistudent.Views.PullableListView;
import com.guugoo.jiapeistudent.Views.TitleView;

import java.util.ArrayList;
import java.util.List;

public class MyMessageActivity extends BaseActivity {
    private static final String TAG ="MyMessageActivity";
    private PullableListView listView;
    private PullToRefreshLayout layout;
    private SharedPreferences sp;
    private boolean isCompleated = true; //是否正在加载
    private int currentPage = 1;  //页数
    private int requestType = 0;   //请求的种类 0:第一次请求 ，1：下拉刷新，2：上拉加载
    private List<Message> listData ;
    private MessageAdapter adapter;


    protected Handler handler = new MyHandler(MyMessageActivity.this){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                Log.d(TAG, "handleMessage: "+msg.obj);
                ReturnData data= JSONObject.parseObject((String) msg.obj,ReturnData.class);
                if(data.getStatus()==0){
                    List<Message> messages = JSONObject.parseArray(data.getData(),Message.class);
                    Log.d(TAG, "handleMessage: "+messages.size());
                    if (messages.size()==0){
                        switch (requestType){
                            case 1:
                                layout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                break;
                            case 2:
                                listView.noMoreLoading();
                                break;
                        }
                    }else {
                        if(currentPage==1){
                            listData.clear();
                        }
                        listData.addAll(messages);
                        currentPage +=1;
                        adapter.notifyDataSetChanged();
                        switch (requestType){
                            case 1:
                                layout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                break;
                            case 2:
                                if (messages.size() > 0)
                                    listView.finishLoading();
                                else
                                    listView.noMoreLoading();
                                break;
                        }
                    }
                    isCompleated=true;
                }else {
                    MyToast.makeText(MyMessageActivity.this,data.getMessage());
                    isCompleated = true;
                    switch (requestType){
                        case 1:
                            layout.refreshFinish(PullToRefreshLayout.FAIL);
                            break;
                        case 2:
                            listView.errorLoading();
                            break;
                    }
                }

            }
        }
    };

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_message);
    }

    @Override
    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.my_message_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setMiddleText(R.string.home_text7);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                Constant.state =false;
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        Constant.state =false;
        finish();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void findView() {
        listData = new ArrayList<>();
        listView  = (PullableListView) findViewById(R.id.my_message_list);
        layout = (PullToRefreshLayout) findViewById(R.id.my_message_layout);
        adapter = new MessageAdapter(listData,MyMessageActivity.this,R.layout.adapter_message);
        listView.setAdapter(adapter);
        sp = getSharedPreferences("user", MODE_PRIVATE);
    }

    @Override
    protected void init() {
        firstLoaded();
        layout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (Utils.isNetworkAvailable(MyMessageActivity.this)) {
                    if (isCompleated) {
                        listData.clear();
                        adapter.notifyDataSetChanged();
                        currentPage = 1;
                        requestType = 1;
                        getData();
                        if (!listView.mStateTextView.getText().equals(R.string.more)) {
                            listView.mStateTextView.setText(R.string.more);
                        }
                    } else {
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                        MyToast.makeText(MyMessageActivity.this, R.string.Toast_loading);
                    }
                } else {
                    isCompleated = true;
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    MyToast.makeText(MyMessageActivity.this, R.string.Toast_internet);
                }
            }
        });
        listView.setOnLoadListener(new PullableListView.OnLoadListener() {
            @Override
            public void onLoad(PullableListView pullableListView) {

                if (Utils.isNetworkAvailable(MyMessageActivity.this)) {
                    if (isCompleated) {
                        requestType = 2;
                        getData();
                    } else {
                        pullableListView.finishLoading();
                        MyToast.makeText(MyMessageActivity.this, R.string.Toast_loading);
                    }

                } else {
                    pullableListView.finishLoading();
                    MyToast.makeText(MyMessageActivity.this, R.string.Toast_internet);
                }

            }
        });
    }


    private void firstLoaded() {
        if (Utils.isNetworkAvailable(MyMessageActivity.this)) {
            getData();
        } else {
            MyToast.makeText(MyMessageActivity.this, R.string.Toast_internet);
        }
    }


    private void getData(){
        JSONObject json= new JSONObject(true);
        json.put("StudentId",sp.getInt("Id",0));
        json.put("PageIndex", currentPage);
        json.put("PageSize", 10);
        new MyThread(Constant.URL_Message, handler, DES.encryptDES(json.toString())).start();
    }


}
