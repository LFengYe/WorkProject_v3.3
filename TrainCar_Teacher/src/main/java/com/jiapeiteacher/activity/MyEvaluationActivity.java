package cn.guugoo.jiapeiteacher.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import cn.guugoo.jiapeiteacher.R;
import cn.guugoo.jiapeiteacher.adapter.EvaluateAdapter;
import cn.guugoo.jiapeiteacher.base.BaseActivity;
import cn.guugoo.jiapeiteacher.base.Constants;
import cn.guugoo.jiapeiteacher.bean.EvaluateInfo;
import cn.guugoo.jiapeiteacher.bean.TotalData;
import cn.guugoo.jiapeiteacher.base.BaseAsyncTask;
import cn.guugoo.jiapeiteacher.util.EncryptUtils;
import cn.guugoo.jiapeiteacher.util.HttpUtil;
import cn.guugoo.jiapeiteacher.util.NetUtil;
import cn.guugoo.jiapeiteacher.view.CircleImageView;
import cn.guugoo.jiapeiteacher.view.XRecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by gpw on 2016/8/10.
 * --加油
 */
public class MyEvaluationActivity extends BaseActivity {

    private XRecyclerView rv_my_evaluation;
    private ArrayList<EvaluateInfo> mEvaluateInfos;
    private EvaluateAdapter mEvaluateAdapter;
    private int schoolId;
    private CircleImageView civ_head;
    private TextView tv_nickName;
    private TextView tv_num;


    private String headImg;
    private String name;
    private String token;
    private int teacherId;
    private int CurrentPage = 1;

    @Override
    protected int getLayout() {
        return R.layout.activity_my_evaluation;
    }


    @Override
    protected void initData() {

//        headImg = getIntent().getStringExtra("headImg");
//        name = getIntent().getStringExtra("name");
//        teacherId = getIntent().getIntExtra("teacherId", 0);
        mEvaluateInfos = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_NAME, MODE_PRIVATE);
        headImg = prefs.getString("Icon","");
        teacherId = prefs.getInt("Id",0);
        name = prefs.getString("NicKname","");
        token = prefs.getString("token","");
    }

    @Override
    protected void initView() {

        rv_my_evaluation = (XRecyclerView) findViewById(R.id.rv_my_evaluation);
        civ_head = (CircleImageView) findViewById(R.id.civ_head);
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_nickName = (TextView) findViewById(R.id.tv_nickName);
        tv_num = (TextView) findViewById(R.id.tv_num);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_my_evaluation.setLayoutManager(layoutManager);
        rv_my_evaluation.setRefreshing(true);

        Glide.with(this)
                .load(headImg)
                .crossFade()
                .into(civ_head);

        tv_nickName.setText(name);


        rv_my_evaluation.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new GetRefreshEvaluateAsyncTask(MyEvaluationActivity.this,HttpUtil.url_evaluate,token).execute(getJsonObject(1));
            }

            @Override
            public void onLoadMore() {
                CurrentPage++;
                new GetLoadEvaluateAsyncTask(MyEvaluationActivity.this,HttpUtil.url_evaluate,token).execute(getJsonObject(CurrentPage));
            }
        });

        assert iv_back != null;
        iv_back.setOnClickListener(this);


        if (!NetUtil.checkNetworkConnection(MyEvaluationActivity.this)){
            Toast.makeText(MyEvaluationActivity.this, R.string.Net_error, Toast.LENGTH_SHORT).show();
            return;
        }
        new GetEvaluateAsyncTask(MyEvaluationActivity.this,HttpUtil.url_evaluate,token).execute(getJsonObject(1));



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
            finish();
        }
    }


    class GetEvaluateAsyncTask extends BaseAsyncTask {


        public GetEvaluateAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(MyEvaluationActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            System.out.println(s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);

            if (totalData.getStatus() == 0) {

                Type listType = new TypeToken<ArrayList<EvaluateInfo>>() {
                }.getType();
                mEvaluateInfos = gson.fromJson(totalData.getData(), listType);
                mEvaluateAdapter = new EvaluateAdapter(mEvaluateInfos,MyEvaluationActivity.this);
                rv_my_evaluation.setAdapter(mEvaluateAdapter);
                if (mEvaluateInfos.size()==0){
                    tv_num.setText("0条");
                }else {
                    tv_num.setText(mEvaluateInfos.get(0).getCount() + "条");
                }

            } else {
                Toast.makeText(MyEvaluationActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    class GetRefreshEvaluateAsyncTask extends BaseAsyncTask{


        public GetRefreshEvaluateAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }


        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                rv_my_evaluation.refreshComplete("fail");
                Toast.makeText(MyEvaluationActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            System.out.println(s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                rv_my_evaluation.refreshComplete("success");
                Type listType = new TypeToken<ArrayList<EvaluateInfo>>() {
                }.getType();
                ArrayList<EvaluateInfo> refresh_mEvaluateInfo = gson.fromJson(totalData.getData(), listType);
                mEvaluateInfos.clear();
                CurrentPage = 1;
                mEvaluateInfos.addAll(refresh_mEvaluateInfo);
                mEvaluateAdapter.notifyDataSetChanged();

            } else {
                Toast.makeText(MyEvaluationActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
                rv_my_evaluation.refreshComplete("fail");
            }

        }
    }


    class GetLoadEvaluateAsyncTask extends BaseAsyncTask{


        public GetLoadEvaluateAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }


        @Override
        protected void dealResults(String s) {
            rv_my_evaluation.loadMoreComplete();
            if (s.isEmpty()) {
                Toast.makeText(MyEvaluationActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            System.out.println(s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                Type listType = new TypeToken<ArrayList<EvaluateInfo>>() {
                }.getType();
                ArrayList<EvaluateInfo> load_mEvaluateInfo = gson.fromJson(totalData.getData(), listType);
                if (load_mEvaluateInfo.size()<10){
                    rv_my_evaluation.setNoMore(true);
                }
                mEvaluateInfos.addAll(load_mEvaluateInfo);
                mEvaluateAdapter.notifyDataSetChanged();

            } else {
                Toast.makeText(MyEvaluationActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
