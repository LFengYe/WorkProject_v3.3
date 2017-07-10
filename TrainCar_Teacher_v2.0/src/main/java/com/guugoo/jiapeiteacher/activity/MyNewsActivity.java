package com.guugoo.jiapeiteacher.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import com.guugoo.jiapeiteacher.R;
import com.guugoo.jiapeiteacher.adapter.NewsAdapter;
import com.guugoo.jiapeiteacher.base.BaseActivity;
import com.guugoo.jiapeiteacher.base.Constants;
import com.guugoo.jiapeiteacher.bean.NewsInfo;
import com.guugoo.jiapeiteacher.bean.TotalData;
import com.guugoo.jiapeiteacher.base.BaseAsyncTask;
import com.guugoo.jiapeiteacher.util.EncryptUtils;
import com.guugoo.jiapeiteacher.util.HttpUtil;
import com.guugoo.jiapeiteacher.util.NetUtil;
import com.guugoo.jiapeiteacher.view.XRecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by gpw on 2016/8/3.
 * --加油
 */
public class MyNewsActivity extends BaseActivity {
    private XRecyclerView rv_my_news;
    private ArrayList<NewsInfo> mNewsInfos;
    private NewsAdapter mNewsAdapter;
    private int teacherId;
    private String token;

    private int CurrentPage = 1;


    @Override
    protected int getLayout() {
        return R.layout.activity_my_news;
    }

    @Override
    protected void initData() {
        mNewsInfos = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_NAME, MODE_PRIVATE);
        teacherId = prefs.getInt("Id", 0);
        token = prefs.getString("token", "");
    }

    @Override
    protected void initView() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.head);
        TextView tv_center = (TextView) rl_head.findViewById(R.id.tv_center);
        ImageView iv_back = (ImageView) rl_head.findViewById(R.id.iv_back);

        rv_my_news = (XRecyclerView) findViewById(R.id.rv_my_news);

        tv_center.setText(R.string.my_news);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_my_news.setLayoutManager(layoutManager);
        rv_my_news.setRefreshing(true);
        rv_my_news.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (!NetUtil.checkNetworkConnection(MyNewsActivity.this)) {
                    rv_my_news.refreshComplete("fail");
                    Toast.makeText(MyNewsActivity.this, R.string.Net_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                new RefreshNewsAsyncTask(MyNewsActivity.this, HttpUtil.url_information, token).execute(getJsonObject(1));
            }

            @Override
            public void onLoadMore() {
                if (!NetUtil.checkNetworkConnection(MyNewsActivity.this)) {
                    rv_my_news.loadMoreComplete();
                    Toast.makeText(MyNewsActivity.this, R.string.Net_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                CurrentPage++;
                new LoadNewsAsyncTask(MyNewsActivity.this, HttpUtil.url_information, token).execute(getJsonObject(CurrentPage));
            }
        });


        iv_back.setOnClickListener(this);


        if (!NetUtil.checkNetworkConnection(MyNewsActivity.this)) {
            Toast.makeText(MyNewsActivity.this, R.string.Net_error, Toast.LENGTH_SHORT).show();
            return;
        }

        new GetNewsAsyncTask(MyNewsActivity.this, HttpUtil.url_information, token).execute(getJsonObject(1));


    }

    private JsonObject getJsonObject(int PageIndex) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("TeacherId", teacherId);
        jsonObject.addProperty("PageIndex", PageIndex);
        jsonObject.addProperty("PageSize", 15);
        jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, getIntent());
        super.onBackPressed();
    }



    class GetNewsAsyncTask extends BaseAsyncTask {

        public GetNewsAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }


        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(MyNewsActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            System.out.println(s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                Constants.ballState = false;
                Type listType = new TypeToken<ArrayList<NewsInfo>>() {
                }.getType();
                mNewsInfos = gson.fromJson(totalData.getData(), listType);
                mNewsAdapter = new NewsAdapter(mNewsInfos);
                rv_my_news.setAdapter(mNewsAdapter);
                rv_my_news.setEmptyView(findViewById(R.id.tv_empty));
            } else {
                Toast.makeText(MyNewsActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }


    class RefreshNewsAsyncTask extends BaseAsyncTask {

        public RefreshNewsAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                rv_my_news.refreshComplete("fail");
                Toast.makeText(MyNewsActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            System.out.println(s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                rv_my_news.refreshComplete("success");
                Type listType = new TypeToken<ArrayList<NewsInfo>>() {
                }.getType();
                mNewsInfos.clear();
                CurrentPage = 1;
                ArrayList<NewsInfo> refresh_mNewsInfo = gson.fromJson(totalData.getData(), listType);
                mNewsInfos.addAll(refresh_mNewsInfo);
                mNewsAdapter.notifyDataSetChanged();
            } else {
                rv_my_news.refreshComplete("fail");
                Toast.makeText(MyNewsActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    class LoadNewsAsyncTask extends BaseAsyncTask {


        public LoadNewsAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            rv_my_news.loadMoreComplete();
            if (s.isEmpty()) {
                Toast.makeText(MyNewsActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            System.out.println(s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                Type listType = new TypeToken<ArrayList<NewsInfo>>() {
                }.getType();
                ArrayList<NewsInfo> Load_mNewsInfo = gson.fromJson(totalData.getData(), listType);
                if (Load_mNewsInfo.size() < 10) {
                    rv_my_news.setNoMore(true);
                }
                mNewsInfos.addAll(Load_mNewsInfo);
                mNewsAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(MyNewsActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
