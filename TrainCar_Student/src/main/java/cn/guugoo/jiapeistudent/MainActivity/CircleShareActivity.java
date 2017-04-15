package cn.guugoo.jiapeistudent.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import cn.guugoo.jiapeistudent.Adapter.ForumAdapter;
import cn.guugoo.jiapeistudent.Adapter.NoticeAdapter;
import cn.guugoo.jiapeistudent.App.Constant;
import cn.guugoo.jiapeistudent.Data.Forum;
import cn.guugoo.jiapeistudent.Data.Notice;
import cn.guugoo.jiapeistudent.Data.ReturnData;
import cn.guugoo.jiapeistudent.MinorActivity.ForumCommentActivity;
import cn.guugoo.jiapeistudent.MinorActivity.NoticeDetailsActivity;
import cn.guugoo.jiapeistudent.MinorActivity.SendMessageActivity;
import cn.guugoo.jiapeistudent.R;
import cn.guugoo.jiapeistudent.Tools.DES;
import cn.guugoo.jiapeistudent.Tools.MyHandler;
import cn.guugoo.jiapeistudent.Tools.MyThread;
import cn.guugoo.jiapeistudent.Tools.MyToast;
import cn.guugoo.jiapeistudent.Tools.Utils;
import cn.guugoo.jiapeistudent.Views.PullToRefreshLayout;
import cn.guugoo.jiapeistudent.Views.PullableListView;
import cn.guugoo.jiapeistudent.Views.TitleView;

import java.util.ArrayList;
import java.util.List;

public class CircleShareActivity extends BaseActivity {
    private static final String TAG = "CircleShareActivity";
    private ListView listView;
    private List<Notice> listData;
    private NoticeAdapter adapter;
    private PullableListView listView2;
    private PullToRefreshLayout layout;
    private ForumAdapter adapter_forum;
    private List<Forum> listForums;
    private SharedPreferences sp;
    private String FirstTime="";
    private boolean isCompleated = true; //是否正在加载
    private int currentPage = 1;  //页数
    private int requestType = 0;   //请求的种类 0:第一次请求 ，1：下拉刷新，2：上拉加载




    protected Handler handler2 = new MyHandler(CircleShareActivity.this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                Log.d(TAG, "handleMessage: "+msg.obj);
                ReturnData data= JSONObject.parseObject((String) msg.obj,ReturnData.class);
                if(data.getStatus()==0){
                    List<Forum> forums = JSONObject.parseArray(data.getData(),Forum.class);
                    Log.d(TAG, "handleMessage: "+forums.size());
                    if (forums.size()==0){
                        switch (requestType){
                            case 1:

                                layout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                break;
                            case 2:
                                listView2.noMoreLoading();
                                break;
                        }
                    }else {
                        if(currentPage==1){
                            listForums.clear();
                        }
                        listForums.addAll(forums);
                        currentPage +=1;
                        adapter_forum.notifyDataSetChanged();
                        FirstTime = forums.get(forums.size()-1).getFirstTime();
                        switch (requestType){
                            case 1:

                                layout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                break;
                            case 2:
                                if (forums.size() > 0)
                                    listView2.finishLoading();
                                else
                                    listView2.noMoreLoading();
                                break;
                        }
                    }
                    isCompleated=true;
                }else {
                    MyToast.makeText(CircleShareActivity.this,data.getMessage());
                    isCompleated = true;
                    switch (requestType){
                        case 1:
                            layout.refreshFinish(PullToRefreshLayout.FAIL);
                            adapter.notifyDataSetChanged();
                            break;
                        case 2:
                            listView2.errorLoading();
                            break;
                    }
                }

            }
        }
    };

    @Override
    protected void processingData(ReturnData data) {
        Log.d(TAG, "processingData: "+data.getData());
        List<Notice> notice =JSONObject.parseArray(data.getData(),Notice.class);
        listData.addAll(notice);
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_circle_share);
    }

    @Override
    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.circle_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setRightTextVisible(true);
        titleView.setMiddleText(R.string.home_text5);
        titleView.setRightText(R.string.post_message);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleView.setRightTextListenter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CircleShareActivity.this, SendMessageActivity.class);
                startActivityForResult(intent,2);
            }
        });

    }

    @Override
    protected void findView() {
        listView = (ListView) findViewById(R.id.circle_hand_list);
        listData = new ArrayList<>();
        adapter = new NoticeAdapter(listData,CircleShareActivity.this,R.layout.adapter_notice_item);
        listView.setAdapter(adapter);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);

        //初始化当前时间
        FirstTime= Utils.getCurrentDate();
        listView2 = (PullableListView) findViewById(R.id.circle_list);
        layout = (PullToRefreshLayout) findViewById(R.id.circle_layout);
        listForums = new ArrayList<>();
        adapter_forum = new ForumAdapter(listForums,CircleShareActivity.this,R.layout.adapter_forum_item);
        listView2.setAdapter(adapter_forum);

    }

    @Override
    protected void init() {
        getNotice();
        firstLoaded();

//        /**
//         * 适配的接口，写着玩的，嘻嘻
//         */
//        adapter_forum.setBack(new ForumAdapter.ForumAdapterBack() {
//            @Override
//            public void click(View item, View parent, int position, int which) {
//                Intent intent =new Intent(CircleShareActivity.this, ForumCommentActivity.class);
//                intent.putExtra("position",position);
//                intent.putExtra("forum",listForums.get(position));
//                intent.putExtra("StudentId",sp.getInt("Id",0));
//                startActivityForResult(intent,1);
//            }
//
//        });
        layout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (Utils.isNetworkAvailable(CircleShareActivity.this)) {
                    if (isCompleated) {
//                        listForums.clear();
                        adapter.notifyDataSetChanged();
                        currentPage = 1;
                        requestType = 1;
                        getForum();
                        if (!listView2.mStateTextView.getText().equals(R.string.more)) {
                            listView2.mStateTextView.setText(R.string.more);
                        }
                    } else {
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                        MyToast.makeText(CircleShareActivity.this, R.string.Toast_loading);
                    }
                } else {
                    isCompleated = true;
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    MyToast.makeText(CircleShareActivity.this, R.string.Toast_internet);
                }
            }
        });
        listView2.setOnLoadListener(new PullableListView.OnLoadListener() {
            @Override
            public void onLoad(PullableListView pullableListView) {

                if (Utils.isNetworkAvailable(CircleShareActivity.this)) {
                    if (isCompleated) {
                        requestType = 2;
                        getForum();
                    } else {
                        pullableListView.finishLoading();
                        MyToast.makeText(CircleShareActivity.this, R.string.Toast_loading);
                    }

                } else {
                    pullableListView.finishLoading();
                    MyToast.makeText(CircleShareActivity.this, R.string.Toast_internet);
                }

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notice notice = listData.get(position);
                Intent intent = new Intent(CircleShareActivity.this, NoticeDetailsActivity.class);
                intent.putExtra("NoticeId",notice.getNoticeId());
                startActivity(intent);
            }
        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent(CircleShareActivity.this, ForumCommentActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("forum",listForums.get(position));
                intent.putExtra("StudentId",sp.getInt("Id",0));
                intent.putExtra("Number",listForums.get(position).getCommentNumber());
                Log.d(TAG, "onItemClick: "+listForums.get(position).getCommentNumber());
                startActivityForResult(intent,1);
            }
        });
    }

    private void getNotice(){
        if (Utils.isNetworkAvailable(CircleShareActivity.this)) {
            JSONObject json = new JSONObject(true);
            json.put("SchoolId",sp.getInt("SchoolId",0));
//            json.put("SchoolId",1);
            new MyThread(Constant.URL_Notice, handler, DES.encryptDES(json.toString())).start();
        } else {
            MyToast.makeText(CircleShareActivity.this, R.string.Toast_internet);
        }
    }

    private void firstLoaded() {
        if (Utils.isNetworkAvailable(CircleShareActivity.this)) {
            currentPage = 1;
            requestType = 0;
            getForum();
        } else {
            MyToast.makeText(CircleShareActivity.this, R.string.Toast_internet);
        }
    }

    private void getForum(){
        JSONObject json= new JSONObject(true);
        json.put("UserId",sp.getInt("Id",0));
        json.put("Type",1);
//        json.put("Type",2);
        json.put("PageIndex", currentPage);
        json.put("PageSize", 10);
        json.put("FirstTime",FirstTime);
        Log.d(TAG, "getForum: "+json.toString());
        isCompleated=false;
        new MyThread(Constant.URL_Forum, handler2, DES.encryptDES(json.toString())).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode ==RESULT_OK){
                    listForums.get(data.getIntExtra("position",0)).
                            setCommentNumber(data.getIntExtra("number",0));
                    adapter_forum.notifyDataSetChanged();
                }
                break;
            case 2:
                if(resultCode==RESULT_OK){
                    Log.d(TAG, "onActivityResult: sfs");
                    firstLoaded();
                    break;
                }

        }
    }
}
