package cn.com.goodsowner.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import cn.com.goodsowner.adapter.ReceiptOrderAdapter;
import cn.com.goodsowner.base.Contants;
import cn.com.goodsowner.bean.ReceiptOrderInfo;
import cn.com.goodsowner.util.EncryptUtil;
import cn.com.goodsowner.util.HttpUtil;
import cn.com.goodsowner.util.LogUtil;
import cn.com.goodsowner.util.VolleyInterface;
import cn.com.goodsowner.view.XRecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReceiptOrderFragment extends Fragment{


    private int status;
    private XRecyclerView rv_receipt_order;
    private TextView tv_empty;
    private int CurrentPage = 1;
    private ArrayList<ReceiptOrderInfo> orderInfos;
    private ReceiptOrderAdapter myOrderAdapter;

    public ReceiptOrderFragment() {

    }

    public static ReceiptOrderFragment newInstance(int status) {
        ReceiptOrderFragment ofg = new ReceiptOrderFragment();
        Bundle args = new Bundle();
        args.putInt("status", status);
        ofg.setArguments(args);
        return ofg;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            status = args.getInt("status");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(cn.com.goodsowner.R.layout.fragment_receipt_order, container, false);
        rv_receipt_order= (XRecyclerView) view.findViewById(cn.com.goodsowner.R.id.rv_receipt_order);
        tv_empty = (TextView) view.findViewById(cn.com.goodsowner.R.id.tv_empty);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_receipt_order.setLayoutManager(layoutManager);
        rv_receipt_order.setEmptyView(tv_empty);

        rv_receipt_order.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                CurrentPage = 1;
                getOrderList(CurrentPage, 0);
            }

            @Override
            public void onLoadMore() {
                CurrentPage++;
                getOrderList(CurrentPage, 1);
            }
        });
        orderInfos = new ArrayList<>();
        myOrderAdapter = new ReceiptOrderAdapter(getActivity(), orderInfos);

        rv_receipt_order.setAdapter(myOrderAdapter);
        getOrderList(1, 0);
        return view;
    }

    private void getOrderList(int PageIndex, final int ways) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", Contants.userId);
        jsonObject.addProperty("Status", status);
        jsonObject.addProperty("PageIndex", PageIndex);
        jsonObject.addProperty("PageSize", 10);
        LogUtil.i(jsonObject.toString());
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(getActivity(), Contants.url_getReceiptList, "getReceiptList", map, new VolleyInterface(getActivity(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<ReceiptOrderInfo>>() {
                }.getType();

                ArrayList<ReceiptOrderInfo> newReceiptOrderInfos = gson.fromJson(result, listType);

                if (ways == 0) {
                    rv_receipt_order.refreshComplete("success");
                    CurrentPage = 1;
                    orderInfos.clear();
                    orderInfos.addAll(newReceiptOrderInfos);
                } else {
                    rv_receipt_order.loadMoreComplete();
                    if (newReceiptOrderInfos.size() < 10) {
                        rv_receipt_order.setNoMore(true);
                    }
                    orderInfos.addAll(newReceiptOrderInfos);
                }
                myOrderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(VolleyError error) {

                if (ways == 0) {
                    rv_receipt_order.refreshComplete("fail");

                } else {
                    rv_receipt_order.loadMoreComplete();
                }

            }

            @Override
            public void onStateError() {
                if (ways == 0) {
                    rv_receipt_order.refreshComplete("fail");
                } else {
                    rv_receipt_order.loadMoreComplete();
                }

            }
        });

    }

}
