package cn.com.goodsowner.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Map;

import cn.com.goodsowner.adapter.CommonAdInfoAdapter;
import cn.com.goodsowner.base.BaseActivity;
import cn.com.goodsowner.base.Contants;
import cn.com.goodsowner.bean.CommAdTimeInfo;
import cn.com.goodsowner.bean.CommonAdInfo;
import cn.com.goodsowner.util.DateUtil;
import cn.com.goodsowner.util.EncryptUtil;
import cn.com.goodsowner.util.HttpUtil;
import cn.com.goodsowner.util.LogUtil;
import cn.com.goodsowner.util.NetworkUtil;
import cn.com.goodsowner.util.VolleyInterface;
import cn.com.goodsowner.view.XRecyclerView;

public class CommonAddressActivity extends BaseActivity {

    private TextView tv_title;
    private ImageView iv_right;
    private ImageView iv_left_white;

    private XRecyclerView rv_common_ad;
    private CommonAdInfoAdapter commonAdInfoAdapter;
    private ArrayList<CommonAdInfo> commonAdInfos;
    private int CurrentPage = 0;
    private int type;

    @Override
    protected int getLayout() {
        return cn.com.goodsowner.R.layout.activity_common_address;
    }

    @Override
    protected void findById() {
        tv_title = (TextView) findViewById(cn.com.goodsowner.R.id.tv_title);
        iv_right = (ImageView) findViewById(cn.com.goodsowner.R.id.iv_right);
        iv_left_white = (ImageView) findViewById(cn.com.goodsowner.R.id.iv_left_white);
        rv_common_ad = (XRecyclerView) findViewById(cn.com.goodsowner.R.id.rv_common_ad);
    }

    @Override
    protected void initData() {

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
                    Intent intent = new Intent(CommonAddressActivity.this, AddMapActivity.class);
                    intent.putExtra("userId", Contants.userId);
                    intent.putExtra("position", position);
                    intent.putExtra("commonAdInfo", commonAdInfos.get(position));
                    intent.putExtra("type", 1);
                    startActivityForResult(intent, 4);
                } else if (type == 1) {
                    getIntent().putExtra("commonAdInfo", commonAdInfos.get(position));
                    setResult(RESULT_OK, getIntent());
                    finish();
                }
            }

            @Override
            public void onItemLongClick(final int position) {
                new AlertDialog.Builder(CommonAddressActivity.this).
                        setTitle("提示").
                        setMessage("确定删除此信息").
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                JsonObject jsonObject = new JsonObject();
                                jsonObject.addProperty("UserId", Contants.userId);
                                jsonObject.addProperty("AddressId", commonAdInfos.get(position).getAddressId());
                                Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
                                HttpUtil.doPost(CommonAddressActivity.this, Contants.url_deleteUserAddress, "deleteUserAddress", map, new VolleyInterface(CommonAddressActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                                    @Override
                                    public void onSuccess(JsonElement result) {
                                        LogUtil.i(result.toString());
                                        commonAdInfos.remove(position);
                                        commonAdInfoAdapter.notifyDataSetChanged();

                                    }

                                    @Override
                                    public void onError(VolleyError error) {
                                        showShortToastByString(getString(cn.com.goodsowner.R.string.timeoutError));
//                LogUtil.i("hint",error.networkResponse.headers.toString());
//                LogUtil.i("hint",error.networkResponse.statusCode+"");
                                    }

                                    @Override
                                    public void onStateError() {
                                    }
                                });
                            }
                        }).
                        setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        }).show();


            }
        });
        getUserAddress(CurrentPage, 0);
        rv_common_ad.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                CurrentPage = 0;
                getUserAddress(CurrentPage, 0);
            }

            @Override
            public void onLoadMore() {
                getUserAddress(CurrentPage, 1);
            }
        });

        tv_title.setText(cn.com.goodsowner.R.string.common_address);
        iv_left_white.setOnClickListener(this);
        iv_right.setOnClickListener(this);
    }

    private void getUserAddress(int PageIndex, final int ways) {
        if (!NetworkUtil.isConnected(CommonAddressActivity.this)) {
            showShortToastByString(getString(cn.com.goodsowner.R.string.Neterror));
            if (ways == 0) {
                rv_common_ad.refreshComplete("fail");
            } else {
                rv_common_ad.loadMoreComplete();
            }
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", Contants.userId);
        jsonObject.addProperty("DataIndex", PageIndex);
        jsonObject.addProperty("PageSize", 15);
        jsonObject.addProperty("GetTime", DateUtil.getCurrentDate());
        LogUtil.i(jsonObject.toString());
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(CommonAddressActivity.this, Contants.url_getUserAddress, "getUserAddress", map, new VolleyInterface(CommonAddressActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                LogUtil.i("result" + result.toString());
                Gson gson = new Gson();
                CommAdTimeInfo commAdTimeInfo = gson.fromJson(result, CommAdTimeInfo.class);

                ArrayList<CommonAdInfo> newCommonAdInfos = (ArrayList<CommonAdInfo>) commAdTimeInfo.getList();

                if (ways == 0) {
                    rv_common_ad.refreshComplete("success");
                    commonAdInfos.clear();
                    commonAdInfos.addAll(newCommonAdInfos);
                } else {
                    rv_common_ad.loadMoreComplete();
                    if (newCommonAdInfos.size() < 15) {
                        rv_common_ad.setNoMore(true);
                    }
                    commonAdInfos.addAll(newCommonAdInfos);
                }
                int size = newCommonAdInfos.size();
                CurrentPage = newCommonAdInfos.get(size - 1).getAddressId();
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
            public void onStateError() {
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
            case cn.com.goodsowner.R.id.iv_left_white:
                finish();
                break;
            case cn.com.goodsowner.R.id.iv_right:
                Intent intent = new Intent(CommonAddressActivity.this, AddMapActivity.class);
                intent.putExtra("userId", Contants.userId);
                intent.putExtra("type", 0);
                startActivityForResult(intent, 4);
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
