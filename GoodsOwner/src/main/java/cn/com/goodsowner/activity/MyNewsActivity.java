package cn.com.goodsowner.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import cn.com.goodsowner.adapter.NewsInfoAdapter;
import cn.com.goodsowner.base.BaseActivity;
import cn.com.goodsowner.base.Contants;
import cn.com.goodsowner.bean.NewsInfo;
import cn.com.goodsowner.util.EncryptUtil;
import cn.com.goodsowner.util.HttpUtil;
import cn.com.goodsowner.util.LogUtil;
import cn.com.goodsowner.util.NetworkUtil;
import cn.com.goodsowner.util.VolleyInterface;
import cn.com.goodsowner.view.XRecyclerView;

public class MyNewsActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private XRecyclerView rv_news;
    private ArrayList<NewsInfo> newsInfos;
    private NewsInfoAdapter newsInfoAdapter;
    private int CurrentPage = 1;

    @Override
    protected int getLayout() {
        return cn.com.goodsowner.R.layout.activity_my_news;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(cn.com.goodsowner.R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(cn.com.goodsowner.R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(cn.com.goodsowner.R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(cn.com.goodsowner.R.id.iv_left_white);
        rv_news = (XRecyclerView) findViewById(cn.com.goodsowner.R.id.rv_news);
    }

    @Override
    protected void initData() {
        newsInfos = new ArrayList<>();
        newsInfoAdapter = new NewsInfoAdapter(newsInfos);
    }

    @Override
    protected void initView() {
        tv_title.setText(cn.com.goodsowner.R.string.myNews);
        tv_right.setVisibility(View.GONE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_news.setLayoutManager(layoutManager);
        rv_news.setAdapter(newsInfoAdapter);

        getNews(CurrentPage, 0);

        rv_news.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                CurrentPage = 1;
                getNews(CurrentPage, 0);
            }

            @Override
            public void onLoadMore() {
                CurrentPage++;
                getNews(CurrentPage, 1);
            }
        });
        iv_left_white.setOnClickListener(this);
    }

    private void getNews(int PageIndex, final int ways) {
        if (!NetworkUtil.isConnected(MyNewsActivity.this)) {
            showShortToastByString(getString(cn.com.goodsowner.R.string.Neterror));
            if (ways == 0) {
                rv_news.refreshComplete("fail");
            } else {
                rv_news.loadMoreComplete();
            }
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", Contants.userId);
        jsonObject.addProperty("UserType", 1);
        jsonObject.addProperty("PageIndex", PageIndex);
        jsonObject.addProperty("PageSize", 15);
        LogUtil.i(jsonObject.toString());
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(MyNewsActivity.this, Contants.url_getUserMessages, "getUserMessages", map, new VolleyInterface(MyNewsActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                LogUtil.i("result" + result.toString());
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<NewsInfo>>() {
                }.getType();
                ArrayList<NewsInfo> newNewsInfo = gson.fromJson(result, listType);
                if (ways == 0) {
                    rv_news.refreshComplete("success");
                    CurrentPage = 1;
                    newsInfos.clear();
                    newsInfos.addAll(newNewsInfo);
                } else {
                    rv_news.loadMoreComplete();
                    if (newNewsInfo.size() < 15) {
                        rv_news.setNoMore(true);
                    }
                    newsInfos.addAll(newNewsInfo);
                }
                newsInfoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(VolleyError error) {

                if (ways == 0) {
                    rv_news.refreshComplete("fail");

                } else {
                    rv_news.loadMoreComplete();
                }

            }

            @Override
            public void onStateError() {
                if (ways == 0) {
                    rv_news.refreshComplete("fail");
                } else {
                    rv_news.loadMoreComplete();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case cn.com.goodsowner.R.id.iv_left_white:
                finish();
                break;
        }
    }
}
