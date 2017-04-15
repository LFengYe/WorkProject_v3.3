package cn.com.caronwer.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
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

import cn.com.caronwer.R;
import cn.com.caronwer.adapter.BankCardInfoAdapter;
import cn.com.caronwer.base.BaseActivity;
import cn.com.caronwer.base.Contants;
import cn.com.caronwer.bean.BankCardInfo;
import cn.com.caronwer.util.EncryptUtil;
import cn.com.caronwer.util.HttpUtil;
import cn.com.caronwer.util.NetworkUtil;
import cn.com.caronwer.util.SPtils;
import cn.com.caronwer.util.VolleyInterface;
import cn.com.caronwer.view.XRecyclerView;


public class BankUserActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;

    private XRecyclerView mRv_ban;
    private ArrayList<BankCardInfo> newsInfos;
    private BankCardInfoAdapter newsInfoAdapter;
    private int CurrentPage = 1;


    @Override
    protected int getLayout() {
        return  R.layout.activity_bank;
    }

    @Override
    protected void findById() {

        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);

        mRv_ban = (XRecyclerView) findViewById(R.id.rv_ban);

    }

    @Override
    protected void initData() {
        newsInfos = new ArrayList<>();
        newsInfoAdapter = new BankCardInfoAdapter(newsInfos);
    }

    @Override
    protected void initView() {
        tv_title.setText("银行账号");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("添加");
        iv_left_white.setOnClickListener(this);
        tv_right.setOnClickListener(this);//添加银行卡信息

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv_ban.setLayoutManager(layoutManager);
        mRv_ban.setAdapter(newsInfoAdapter);



        getNews(CurrentPage, 0);

        mRv_ban.setLoadingListener(new XRecyclerView.LoadingListener() {
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

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.iv_left_white:
                finish();
                break;
            case R.id.tv_right:
                startActivity(new Intent(BankUserActivity.this,AddBBankcarActivity.class));
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getNews(CurrentPage,0);
    }


    private void getNews(int PageIndex, final int ways) {
        if (!NetworkUtil.isConnected(BankUserActivity.this)) {
            showShortToastByString(getString(R.string.Neterror));
            if (ways == 0) {
                mRv_ban.refreshComplete("fail");
            } else {
                mRv_ban.loadMoreComplete();
            }
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", SPtils.getString(this, "UserId", "00000000-0000-0000-0000-000000000000"));
        jsonObject.addProperty("UserType", 2);
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(BankUserActivity.this, Contants.url_getuserbankcard, "getuserbankcard", map, new VolleyInterface(BankUserActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<BankCardInfo>>() {
                }.getType();
                ArrayList<BankCardInfo> newNewsInfo = gson.fromJson(result, listType);
                if (ways == 0) {
                    mRv_ban.refreshComplete("success");
                    CurrentPage = 1;
                    newsInfos.clear();
                    newsInfos.addAll(newNewsInfo);
                } else {
                    mRv_ban.loadMoreComplete();
                    if (newNewsInfo.size() < 15) {
                        mRv_ban.setNoMore(true);
                    }
                    newsInfos.addAll(newNewsInfo);
                }
                newsInfoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(VolleyError error) {

                if (ways == 0) {
                    mRv_ban.refreshComplete("fail");

                } else {
                    mRv_ban.loadMoreComplete();
                }

            }

            @Override
            public void onStateError(int sta, String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    showShortToastByString(msg);
                }

                if (ways == 0) {
                    mRv_ban.refreshComplete("fail");
                } else {
                    mRv_ban.loadMoreComplete();
                }
            }
        });
    }

}
