package cn.guugoo.jiapeistudent.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import cn.guugoo.jiapeistudent.Adapter.ReserveAdapter;
import cn.guugoo.jiapeistudent.App.Constant;
import cn.guugoo.jiapeistudent.Data.Reserve;
import cn.guugoo.jiapeistudent.Data.ReturnData;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyReserveActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG ="MyReserveActivity";
    private TextView[] textViews;
    private int index;
    private int currentTabIndex=0;
    private int requestIndex;
    private List<Reserve> listData ;
    private PullableListView listView;
    private PullToRefreshLayout layout;
    private ReserveAdapter adapter;
    private SharedPreferences sp;
    private ExecutorService executorService;
    private boolean isCompleated = true; //是否正在加载
    private int currentPage = 1;  //页数
    private int requestType = 0;   //请求的种类 0:第一次请求 ，1：下拉刷新，2：上拉加载
    private List<Reserve>[] reserves ; //缓存四个数据
    private boolean OK, Wait, End,ALL;

    protected Handler handler = new MyHandler(MyReserveActivity.this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                Log.d(TAG, "handleMessage: "+msg.obj);
                ReturnData data= JSONObject.parseObject((String) msg.obj,ReturnData.class);
                if(data.getStatus()==0){
                    List<Reserve> coachs = JSONObject.parseArray(data.getData(),Reserve.class);
                    Log.d(TAG, "handleMessage: "+coachs.size());
                    /**
                     *   判断是否没有切换
                     */
                    if (requestIndex==index){
                        if (coachs.size()==0){
                            switch (requestType){
                                case 1:
                                    layout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                    break;
                                case 2:
                                    listView.noMoreLoading();
                                    break;
                            }
                        }else {
                            /**
                             * 如果是第一页，则主list刷新，对应的list刷新
                             */
                            if(currentPage==1){
                                listData.clear();
                                Log.d(TAG, "123123handleMessage: ");
                                reserves[index].clear();
                            }

                            listData.addAll(coachs);
                            reserves[index].addAll(coachs);
                            currentPage +=1;
                            adapter.notifyDataSetChanged();
                            switch (requestType){
                                case 1:
                                    layout.refreshFinish(PullToRefreshLayout.SUCCEED);
                                    break;
                                case 2:
                                    if (coachs.size() > 0)
                                        listView.finishLoading();
                                    else
                                        listView.noMoreLoading();
                                    break;
                            }
                        }
                        isCompleated=true;
                    }
                }else {
                    MyToast.makeText(MyReserveActivity.this,data.getMessage());
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
        setContentView(R.layout.activity_my_reserve);
    }

    @Override
    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.my_reserve_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setMiddleText(R.string.home_text4);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void findView() {
        textViews = new TextView[4];
        textViews[0] = (TextView) findViewById(R.id.my_reserve_text1);
        textViews[1] = (TextView) findViewById(R.id.my_reserve_text2);
        textViews[2] = (TextView) findViewById(R.id.my_reserve_text3);
        textViews[3] = (TextView) findViewById(R.id.my_reserve_text4);
        listView  = (PullableListView) findViewById(R.id.my_reserve_list);
        layout = (PullToRefreshLayout) findViewById(R.id.my_reserve_layout);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        listData = new ArrayList<Reserve>();
        reserves = new ArrayList[4];
        reserves[0] = new ArrayList<>();
        reserves[1] = new ArrayList<>();
        reserves[2] = new ArrayList<>();
        reserves[3] = new ArrayList<>();
        adapter = new ReserveAdapter(R.layout.adapter_reserve,MyReserveActivity.this,listData);
        listView.setAdapter(adapter);
        executorService = Executors.newCachedThreadPool();
    }


    @Override
    protected void init() {
        textViews[0].setSelected(true);
        ALL =false;
        OK = true;
        Wait = true;
        End = true;
        textViews[0].setOnClickListener(this);
        textViews[1].setOnClickListener(this);
        textViews[2].setOnClickListener(this);
        textViews[3].setOnClickListener(this);

        firstLoaded();
        layout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (Utils.isNetworkAvailable(MyReserveActivity.this)) {
                    if (isCompleated) {
                        listData.clear();
                        adapter.notifyDataSetChanged();
                        currentPage = 1;
                        requestType = 1;
                        getReserve();
                        if (!listView.mStateTextView.getText().equals(R.string.more)) {
                            listView.mStateTextView.setText(R.string.more);
                        }
                    } else {
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                        MyToast.makeText(MyReserveActivity.this, R.string.Toast_loading);
                    }
                } else {
                    isCompleated = true;
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    MyToast.makeText(MyReserveActivity.this, R.string.Toast_internet);
                }
            }
        });
        listView.setOnLoadListener(new PullableListView.OnLoadListener() {
            @Override
            public void onLoad(PullableListView pullableListView) {

                if (Utils.isNetworkAvailable(MyReserveActivity.this)) {
                    if (isCompleated) {
                        requestType = 2;
                        getReserve();
                    } else {
                        pullableListView.finishLoading();
                        MyToast.makeText(MyReserveActivity.this, R.string.Toast_loading);
                    }

                } else {
                    pullableListView.finishLoading();
                    MyToast.makeText(MyReserveActivity.this, R.string.Toast_internet);
                }

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.my_reserve_text1:
                if(ALL){
                    firstLoaded();
                    ALL=false;
                }
                index = 0;
                break;
            case R.id.my_reserve_text2:
                index = 1;
                if (OK){
                    firstLoaded();
                    OK =false;
                }
                break;
            case R.id.my_reserve_text3:
                index = 2;
                if (Wait){
                    firstLoaded();
                    Wait =false;
                }
                break;
            case R.id.my_reserve_text4:
                index = 3;
                if (End){
                    firstLoaded();
                    End = false;
                }
                break;
        }
        BarChange();
    }

    private void firstLoaded() {
        if (Utils.isNetworkAvailable(MyReserveActivity.this)) {
            currentPage=1;
            requestType=0;
            getReserve();
        } else {
            MyToast.makeText(MyReserveActivity.this, R.string.Toast_internet);
        }
    }


    private void BarChange() {
        if(currentTabIndex!=index){
            listData.clear();
            listData.addAll(reserves[index]);
            adapter.notifyDataSetChanged();
        }
        textViews[currentTabIndex].setSelected(false);
        textViews[index].setSelected(true);
        currentTabIndex = index;

    }

    public void getReserve() {
        JSONObject json= new JSONObject(true);
        Log.d(TAG, "getTimeTable: "+sp.getInt("CurrentSubject", 0));
        json.put("StudentId",sp.getInt("Id",0));
        json.put("SchoolId",sp.getInt("SchoolId",0));
        json.put("PageIndex", currentPage);
        json.put("PageSize", 10);
        json.put("Status",index);
        Log.d(TAG, "getTeacher: "+json.toString());
        isCompleated=false;
        requestIndex=index;
        executorService.execute(new MyThread(Constant.URL_MyBooking, handler, DES.encryptDES(json.toString())));
    }

    /**
     * 停止线程池
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        firstLoaded();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        listData.clear();
        reserves[index].clear();
        adapter.notifyDataSetChanged();
        ALL=true;
    }
}
