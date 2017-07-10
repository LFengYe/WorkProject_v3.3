package com.guugoo.jiapeiteacher.activity;

import android.content.Context;
import android.content.Intent;
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
import com.guugoo.jiapeiteacher.adapter.StatuteAdapter;
import com.guugoo.jiapeiteacher.base.BaseActivity;
import com.guugoo.jiapeiteacher.base.Constants;
import com.guugoo.jiapeiteacher.bean.StatuteInfo;
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
public class LawsActivity extends BaseActivity {


    private XRecyclerView rv_laws;
    private ArrayList<StatuteInfo> mStatuteInfos;
    private StatuteAdapter mStatuteAdapter;
    private int schoolId;
    private int CurrentPage = 1;
    private String token;

    @Override
    protected int getLayout() {
        return R.layout.activity_laws;
    }


    @Override
    protected void initData() {
        mStatuteInfos = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_NAME, MODE_PRIVATE);
        schoolId = prefs.getInt("SchoolId",0);
        token = prefs.getString("token","");
    }

    @Override
    protected void initView() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.head);
        TextView tv_center = (TextView) rl_head.findViewById(R.id.tv_center);
        ImageView iv_back = (ImageView) rl_head.findViewById(R.id.iv_back);

        rv_laws = (XRecyclerView) findViewById(R.id.rv_laws);

        tv_center.setText(R.string.laws);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_laws.setLayoutManager(layoutManager);
        rv_laws.setEmptyView(findViewById(R.id.tv_empty));
        rv_laws.setRefreshing(true);


        rv_laws.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (!NetUtil.checkNetworkConnection(LawsActivity.this)){
                    rv_laws.refreshComplete("fail");
                    Toast.makeText(LawsActivity.this, R.string.Net_error, Toast.LENGTH_SHORT).show();
                    return;
                }
               new GetRefreshLawsAsyncTask(LawsActivity.this,HttpUtil.url_statute,token).execute(getJsonObject(1));
            }

            @Override
            public void onLoadMore() {
                if (!NetUtil.checkNetworkConnection(LawsActivity.this)){
                    rv_laws.loadMoreComplete();
                    Toast.makeText(LawsActivity.this, R.string.Net_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                CurrentPage++;
                new GetLoadLawsAsyncTask(LawsActivity.this,HttpUtil.url_statute,token).execute(getJsonObject(CurrentPage));
            }
        });

        iv_back.setOnClickListener(this);

        if (!NetUtil.checkNetworkConnection(LawsActivity.this)){
            Toast.makeText(LawsActivity.this, R.string.Net_error, Toast.LENGTH_SHORT).show();
            return;
        }
        new GetLawsAsyncTask(LawsActivity.this,HttpUtil.url_statute,token).execute(getJsonObject(1));
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



    private JsonObject getJsonObject(int pageIndex) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("SchoolId", schoolId);
        jsonObject.addProperty("PageIndex", pageIndex);
        jsonObject.addProperty("PageSize", 20);
        jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
        return jsonObject;
    }



    class GetLawsAsyncTask extends BaseAsyncTask {

        public GetLawsAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            System.out.println(s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                Type listType = new TypeToken<ArrayList<StatuteInfo>>() {
                }.getType();

                mStatuteInfos = gson.fromJson(totalData.getData(), listType);
                mStatuteAdapter = new StatuteAdapter(mStatuteInfos);
                rv_laws.setAdapter(mStatuteAdapter);

                mStatuteAdapter.setOnItemClickListener(new StatuteAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(LawsActivity.this, LawsDetailsActivity.class);
                        intent.putExtra("StatuteId", mStatuteInfos.get(position-1).getId());
                        intent.putExtra("token", token);
                        startActivity(intent);
                    }
                });
            } else {
                Toast.makeText(LawsActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class GetRefreshLawsAsyncTask extends BaseAsyncTask {


        public GetRefreshLawsAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }


        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(LawsActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                rv_laws.refreshComplete("fail");
                return;
            }
            System.out.println(s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                rv_laws.refreshComplete("success");
                Type listType = new TypeToken<ArrayList<StatuteInfo>>() {
                }.getType();
                ArrayList<StatuteInfo> refresh_mStatuteInfo = gson.fromJson(totalData.getData(), listType);
                mStatuteInfos.clear();
                CurrentPage = 1;
                mStatuteInfos.addAll(refresh_mStatuteInfo);
                mStatuteAdapter.notifyDataSetChanged();

            } else {
                Toast.makeText(LawsActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
                rv_laws.refreshComplete("fail");
            }
        }
    }

    class GetLoadLawsAsyncTask extends BaseAsyncTask {


        public GetLoadLawsAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }



        @Override
        protected void dealResults(String s) {
            rv_laws.loadMoreComplete();
            if (s.isEmpty()) {
                Toast.makeText(LawsActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            System.out.println(s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                Type listType = new TypeToken<ArrayList<StatuteInfo>>() {
                }.getType();
                ArrayList<StatuteInfo> load_mStatuteInfo = gson.fromJson(totalData.getData(), listType);
                if (load_mStatuteInfo.size()<10){
                    rv_laws.setNoMore(true);
                }
                mStatuteInfos.addAll(load_mStatuteInfo);
                mStatuteAdapter.notifyDataSetChanged();

            } else {
                Toast.makeText(LawsActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
