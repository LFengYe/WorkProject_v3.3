package cn.com.caronwer.activiBu;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Map;

import cn.com.caronwer.R;
import cn.com.caronwer.adapter.CommonAdInfoAdapter;
import cn.com.caronwer.base.BaseActivity;
import cn.com.caronwer.base.Contants;
import cn.com.caronwer.bean.CommAdTimeInfo;
import cn.com.caronwer.bean.CommonAdInfo;
import cn.com.caronwer.util.DateUtil;
import cn.com.caronwer.util.EncryptUtil;
import cn.com.caronwer.util.HttpUtil;
import cn.com.caronwer.util.NetworkUtil;
import cn.com.caronwer.util.VolleyInterface;
import cn.com.caronwer.view.XRecyclerView;


public class CommonAddressActivity extends BaseActivity {

    private TextView tv_title;
    private ImageView iv_right;
    private ImageView iv_left_white;
    private String userId;
    private XRecyclerView rv_common_ad;
    private CommonAdInfoAdapter commonAdInfoAdapter;
    private ArrayList<CommonAdInfo> commonAdInfos;
    private int CurrentPage = 1;
    private int type;

    @Override
    protected int getLayout() {
        return R.layout.activity_common_address;
    }

    @Override
    protected void findById() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        iv_left_white = (ImageView) findViewById(R.id.iv_left_white);
        rv_common_ad = (XRecyclerView) findViewById(R.id.rv_common_ad);
    }

    @Override
    protected void initData() {
        userId = getIntent().getStringExtra("userId");
        type = getIntent().getIntExtra("type", 0);
        commonAdInfos = new ArrayList<>();
        commonAdInfoAdapter = new CommonAdInfoAdapter(this, commonAdInfos);
    }

    @Override
    protected void initView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_common_ad.setLayoutManager(layoutManager);
        rv_common_ad.setAdapter(commonAdInfoAdapter);
        commonAdInfoAdapter.setOnItemClickListener(new CommonAdInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (type == 2) {

//                    Intent intent = new Intent(CommonAddressActivity.this, AddMapActivity.class);
//                    intent.putExtra("userId", userId);
//                    intent.putExtra("position", position);
//                    intent.putExtra("commonAdInfo", commonAdInfos.get(position));
//                    intent.putExtra("type", 1);
//                    startActivityForResult(intent, 4);
                } else if (type == 1) {
                    getIntent().putExtra("commonAdInfo", commonAdInfos.get(position));
                    setResult(RESULT_OK, getIntent());
                    finish();
                }
            }
        });
        getUserAddress(1, 0);
        rv_common_ad.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                CurrentPage = 1;
                getUserAddress(CurrentPage, 0);
            }

            @Override
            public void onLoadMore() {
                CurrentPage++;
                getUserAddress(CurrentPage, 1);
            }
        });

        tv_title.setText(R.string.common_address);
        iv_left_white.setOnClickListener(this);
        iv_right.setOnClickListener(this);
    }

    private void getUserAddress(int PageIndex, final int ways) {
        if (!NetworkUtil.isConnected(CommonAddressActivity.this)) {
            showShortToastByString(getString(R.string.Neterror));
            if (ways == 0) {
                rv_common_ad.refreshComplete("fail");
            } else {
                rv_common_ad.loadMoreComplete();
            }
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", userId);
        jsonObject.addProperty("DataIndex", PageIndex);
        jsonObject.addProperty("PageSize", 15);
        jsonObject.addProperty("GetTime", DateUtil.getCurrentDate());
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(CommonAddressActivity.this, Contants.url_getUserAddress, "getUserAddress", map, new VolleyInterface(CommonAddressActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                Gson gson = new Gson();
                CommAdTimeInfo commAdTimeInfo = gson.fromJson(result, CommAdTimeInfo.class);

                ArrayList<CommonAdInfo> newCommonAdInfos = (ArrayList<CommonAdInfo>) commAdTimeInfo.getList();
                if (ways == 0) {
                    rv_common_ad.refreshComplete("success");
                    CurrentPage = 1;
                    commonAdInfos.clear();
                    commonAdInfos.addAll(newCommonAdInfos);
                } else {
                    rv_common_ad.loadMoreComplete();
                    if (newCommonAdInfos.size() < 15) {
                        rv_common_ad.setNoMore(true);
                    }
                    commonAdInfos.addAll(newCommonAdInfos);
                }
                commonAdInfoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(VolleyError error) {

                if (ways == 0) {
                    rv_common_ad.refreshComplete("fail");

                } else {
                    rv_common_ad.loadMoreComplete();
                }

            }

            @Override
            public void onStateError(int sta, String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    showShortToastByString(msg);
                }
                if (ways == 0) {
                    rv_common_ad.refreshComplete("fail");
                } else {
                    rv_common_ad.loadMoreComplete();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_white:
                finish();
                break;
            case R.id.iv_right:
//                Intent intent = new Intent(CommonAddressActivity.this, AddMapActivity.class);
//                intent.putExtra("userId", userId);
//                intent.putExtra("type", 0);
//                startActivityForResult(intent, 4);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 4) {
            int position = data.getIntExtra("position", 0);
            int type = data.getIntExtra("type", 0);
            CommonAdInfo commonAdInfo = data.getParcelableExtra("commonAdInfo");
            if (type == 0) {
                commonAdInfos.add(commonAdInfo);
            } else {
                commonAdInfos.set(position, commonAdInfo);
            }
            commonAdInfoAdapter.notifyDataSetChanged();
        }
    }
}
