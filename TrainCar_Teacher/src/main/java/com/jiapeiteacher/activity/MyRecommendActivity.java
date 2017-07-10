package cn.guugoo.jiapeiteacher.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import cn.guugoo.jiapeiteacher.R;
import cn.guugoo.jiapeiteacher.adapter.RecommendAdapter;
import cn.guugoo.jiapeiteacher.base.BaseActivity;
import cn.guugoo.jiapeiteacher.base.Constants;
import cn.guugoo.jiapeiteacher.bean.RecommendInfo;
import cn.guugoo.jiapeiteacher.bean.TotalData;
import cn.guugoo.jiapeiteacher.base.BaseAsyncTask;
import cn.guugoo.jiapeiteacher.util.EncryptUtils;
import cn.guugoo.jiapeiteacher.util.HttpUtil;
import cn.guugoo.jiapeiteacher.util.NetUtil;
import cn.guugoo.jiapeiteacher.view.XRecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by gpw on 2016/8/3.
 * --加油
 */
public class MyRecommendActivity extends BaseActivity {
    private XRecyclerView lv_recommend;
    private RecommendAdapter mRecommendAdapter;
    private ArrayList<RecommendInfo> mRecommendInfos;
    private int teacherId;
    private String invitationCode;
    private String token;
    private int CurrentIndex = 1;

    @Override
    protected int getLayout() {
        return R.layout.activity_my_recommend;
    }

    @Override
    protected void initView() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.head);
        TextView tv_center = (TextView) rl_head.findViewById(R.id.tv_center);
        ImageView iv_back = (ImageView) rl_head.findViewById(R.id.iv_back);

        LinearLayout ll_use = (LinearLayout) findViewById(R.id.ll_use);
        TextView tv_recommendCode = (TextView) findViewById(R.id.tv_recommendCode);

        lv_recommend = (XRecyclerView) findViewById(R.id.lv_recommend);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv_recommend.setLayoutManager(layoutManager);
        lv_recommend.setRefreshing(true);
        lv_recommend.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (!NetUtil.checkNetworkConnection(MyRecommendActivity.this)) {
                    Toast.makeText(MyRecommendActivity.this, R.string.Net_error, Toast.LENGTH_SHORT).show();
                    lv_recommend.refreshComplete("fail");
                    return;
                }
             new RefreshRecommendAsyncTask(MyRecommendActivity.this,HttpUtil.url_recommend,token).execute(getJsonObject(1));

            }

            @Override
            public void onLoadMore() {
                if (!NetUtil.checkNetworkConnection(MyRecommendActivity.this)) {
                    lv_recommend.loadMoreComplete();
                    Toast.makeText(MyRecommendActivity.this, R.string.Net_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                CurrentIndex++;
                new LoadRecommendAsyncTask(MyRecommendActivity.this,HttpUtil.url_recommend,token).execute(getJsonObject(CurrentIndex));
            }
        });


        tv_center.setText(R.string.my_recommend);
        tv_recommendCode.setText(invitationCode);
        iv_back.setOnClickListener(this);
        ll_use.setOnClickListener(this);

        if (!NetUtil.checkNetworkConnection(this)) {
            Toast.makeText(MyRecommendActivity.this, R.string.Net_error, Toast.LENGTH_SHORT).show();
            return;
        }

        new GetRecommendAsyncTask(MyRecommendActivity.this,HttpUtil.url_recommend,token).execute(getJsonObject(1));

    }

    private JsonObject getJsonObject(int PageIndex) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("PageSize", 15);
        jsonObject.addProperty("PageIndex", PageIndex);
        jsonObject.addProperty("TeacherId", teacherId);
        System.out.println(jsonObject.toString());
        jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
        return jsonObject;
    }

    @Override
    protected void initData() {
        mRecommendInfos = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_NAME, MODE_PRIVATE);
        teacherId = prefs.getInt("Id",0);
        invitationCode = prefs.getString("invitationCode","");
        token = prefs.getString("token","");
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, getIntent());
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            setResult(RESULT_OK, getIntent());
            finish();
        } else if (v.getId() == R.id.ll_use) {
            Intent intent = new Intent(MyRecommendActivity.this, UseRuleActivity.class);
            startActivity(intent);
        }
    }

    class GetRecommendAsyncTask extends BaseAsyncTask {


        public GetRecommendAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(MyRecommendActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            System.out.println(s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                Type listType = new TypeToken<ArrayList<RecommendInfo>>() {
                }.getType();
                mRecommendInfos = gson.fromJson(totalData.getData(), listType);
                mRecommendAdapter = new RecommendAdapter(mRecommendInfos, MyRecommendActivity.this);
                lv_recommend.setAdapter(mRecommendAdapter);

            } else {
                Toast.makeText(MyRecommendActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    class RefreshRecommendAsyncTask extends BaseAsyncTask {


        public RefreshRecommendAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }


        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                lv_recommend.refreshComplete("fail");
                Toast.makeText(MyRecommendActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            System.out.println(s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                lv_recommend.refreshComplete("success");
                Type listType = new TypeToken<ArrayList<RecommendInfo>>() {
                }.getType();
                ArrayList<RecommendInfo> new_RecommendInfos = gson.fromJson(totalData.getData(), listType);
                mRecommendInfos.clear();
                CurrentIndex = 1;
                mRecommendInfos.addAll(new_RecommendInfos);
                mRecommendAdapter.notifyDataSetChanged();

            } else {
                lv_recommend.refreshComplete("fail");
                Toast.makeText(MyRecommendActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }


    class LoadRecommendAsyncTask extends BaseAsyncTask {


        public LoadRecommendAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            lv_recommend.loadMoreComplete();
            if (s.isEmpty()) {
                Toast.makeText(MyRecommendActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            System.out.println(s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                Type listType = new TypeToken<ArrayList<RecommendInfo>>() {
                }.getType();
                ArrayList<RecommendInfo> new_RecommendInfos = gson.fromJson(totalData.getData(), listType);
                if (new_RecommendInfos.size()<15){
                    lv_recommend.setNoMore(true);
                }
                mRecommendInfos.addAll(new_RecommendInfos);
                mRecommendAdapter.notifyDataSetChanged();

            } else {
                Toast.makeText(MyRecommendActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
