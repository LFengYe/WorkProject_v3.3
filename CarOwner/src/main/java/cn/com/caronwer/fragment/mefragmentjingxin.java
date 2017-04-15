package cn.com.caronwer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import cn.com.caronwer.R;
import cn.com.caronwer.activity.MyOrderActivityMe;
import cn.com.caronwer.activity.OrderDetailActivity;
import cn.com.caronwer.activity.PayActivity;
import cn.com.caronwer.adapter.MeAllOrderInfoAdapter;
import cn.com.caronwer.base.Contants;
import cn.com.caronwer.bean.MeAllOrderInfo;
import cn.com.caronwer.util.EncryptUtil;
import cn.com.caronwer.util.HttpUtil;
import cn.com.caronwer.util.LogUtil;
import cn.com.caronwer.util.NetworkUtil;
import cn.com.caronwer.util.SPtils;
import cn.com.caronwer.util.VolleyInterface;
import cn.com.caronwer.view.XRecyclerView;

public class mefragmentjingxin extends android.support.v4.app.Fragment {

    private View mView;
    private MyOrderActivityMe myActivity;
    private XRecyclerView mRv_meAllOrder;
    private ArrayList<MeAllOrderInfo> allInfos;
    private MeAllOrderInfoAdapter allInfoAdapter;
    private int CurrentPage = 1;
    private Gson mMgson;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData();
        findById();
        initView();
        return mView;
    }

    private void initData() {
        allInfos = new ArrayList<>();
        allInfoAdapter = new MeAllOrderInfoAdapter(allInfos, 2);
    }

    private void findById() {
        myActivity = (MyOrderActivityMe) getActivity();
        mView = View.inflate(getActivity(), R.layout.fraagment_me_songda,null);
        mRv_meAllOrder = (XRecyclerView) mView.findViewById(R.id.rv_meAllOrder);
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(myActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv_meAllOrder.setLayoutManager(layoutManager);
        mRv_meAllOrder.setAdapter(allInfoAdapter);
        getAllOrders(CurrentPage, 0);
        mRv_meAllOrder.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                CurrentPage = 1;
                getAllOrders(CurrentPage, 0);
            }

            @Override
            public void onLoadMore() {
                CurrentPage++;
                getAllOrders(CurrentPage, 1);
            }
        });

        allInfoAdapter.setOnBtnClickListener(new MeAllOrderInfoAdapter.OnBtnClickListener() {
            @Override
            public void onBtnClick(MeAllOrderInfo info, String viewName) {
                switch (viewName){
                    case "查看详情":
                        Intent intent = new Intent(myActivity, OrderDetailActivity.class);
                        if (mMgson == null){
                            mMgson = new Gson();
                        }
                        String json = mMgson.toJson(info);
                        intent.putExtra("orderNo", info.getOrderNo());
                        startActivity(intent);
                        break;
                    case "支付":
                        Intent intent2 = new Intent(myActivity,PayActivity.class);
                        intent2.putExtra("money", info.getCancelFee());
                        intent2.putExtra("orderNo", info.getOrderNo());
//                        startActivityForResult(intent2, 3);
                        startActivity(intent2);
                        break;
                }
            }
        });
    }

    private void getAllOrders(int PageIndex, final int ways) {
        if (!NetworkUtil.isConnected(myActivity)) {
            //showShortToastByString(getString(R.string.Neterror));

            if (ways == 0) {
                mRv_meAllOrder.refreshComplete("fail");
            } else {
                mRv_meAllOrder.loadMoreComplete();
            }
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", SPtils.getString(getActivity(), "UserId", "00000000-0000-0000-0000-000000000000"));
        jsonObject.addProperty("Status", 2);//表示已进行中
        jsonObject.addProperty("PageIndex", PageIndex);//页码
        jsonObject.addProperty("PageSize", 10);//页容量
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(myActivity, Contants.url_gettransporterorderlist, "gettransporterorderList", map, new VolleyInterface(myActivity, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<MeAllOrderInfo>>() {
                }.getType();
                ArrayList<MeAllOrderInfo> newNewsInfo = gson.fromJson(result, listType);
                for (MeAllOrderInfo info : newNewsInfo) {
                    LogUtil.i(info.getOrderNo(), gson.toJson(info));
                }
                if (ways == 0) {
                    mRv_meAllOrder.refreshComplete("success");
                    CurrentPage = 1;
                    allInfos.clear();
                    allInfos.addAll(newNewsInfo);
                } else {
                    mRv_meAllOrder.loadMoreComplete();
                    if (newNewsInfo.size() < 15) {
                        mRv_meAllOrder.setNoMore(true);
                    }
                    allInfos.addAll(newNewsInfo);
                }
                allInfoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(VolleyError error) {

                if (ways == 0) {
                    mRv_meAllOrder.refreshComplete("fail");

                } else {
                    mRv_meAllOrder.loadMoreComplete();
                }
            }

            @Override
            public void onStateError(int sta, String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    Toast.makeText(myActivity, msg, Toast.LENGTH_SHORT).show();
                }
                if (ways == 0) {
                    mRv_meAllOrder.refreshComplete("fail");
                } else {
                    mRv_meAllOrder.loadMoreComplete();
                }
            }
        });
    }

}
