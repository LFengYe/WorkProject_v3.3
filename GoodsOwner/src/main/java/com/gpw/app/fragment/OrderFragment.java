package com.gpw.app.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.gpw.app.R;
import com.gpw.app.activity.OrderDetailActivity;
import com.gpw.app.activity.OrderOffersActivity;
import com.gpw.app.activity.OrderPayActivity;
import com.gpw.app.activity.PayActivity;
import com.gpw.app.adapter.MyOrderAdapter;
import com.gpw.app.base.Contants;
import com.gpw.app.bean.OrderAddressBean;
import com.gpw.app.bean.OrderInfo;
import com.gpw.app.bean.PayFeeInfo;
import com.gpw.app.util.EncryptUtil;
import com.gpw.app.util.HttpUtil;
import com.gpw.app.util.LogUtil;
import com.gpw.app.util.VolleyInterface;
import com.gpw.app.view.MyDialog;
import com.gpw.app.view.XRecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment implements MyOrderAdapter.OnBtnClickListener {


    private int status;
    private XRecyclerView rv_my_order;
    private TextView tv_empty;
    private int CurrentPage = 1;
    private ArrayList<OrderInfo> orderInfos;
    private MyOrderAdapter myOrderAdapter;
    private MyDialog endDialog;

    public OrderFragment() {

    }

    public static OrderFragment newInstance(int status) {
        OrderFragment ofg = new OrderFragment();
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
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        rv_my_order = (XRecyclerView) view.findViewById(R.id.rv_my_order);
        tv_empty = (TextView) view.findViewById(R.id.tv_empty);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_my_order.setLayoutManager(layoutManager);
        rv_my_order.setEmptyView(tv_empty);
        rv_my_order.setLoadingListener(new XRecyclerView.LoadingListener() {
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
        myOrderAdapter = new MyOrderAdapter(getActivity(), orderInfos);
        myOrderAdapter.setOnBtnClickListener(this);
        rv_my_order.setAdapter(myOrderAdapter);
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
        HttpUtil.doPost(getActivity(), Contants.url_getSendOrderList, "getSendOrderList", map, new VolleyInterface(getActivity(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<OrderInfo>>() {
                }.getType();

                ArrayList<OrderInfo> newOrderInfos = gson.fromJson(result, listType);

                if (ways == 0) {
                    rv_my_order.refreshComplete("success");
                    CurrentPage = 1;
                    orderInfos.clear();
                    orderInfos.addAll(newOrderInfos);
                } else {
                    rv_my_order.loadMoreComplete();
                    if (newOrderInfos.size() < 10) {
                        rv_my_order.setNoMore(true);
                    }
                    orderInfos.addAll(newOrderInfos);
                }
                myOrderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(VolleyError error) {

                if (ways == 0) {
                    rv_my_order.refreshComplete("fail");

                } else {
                    rv_my_order.loadMoreComplete();
                }

            }

            @Override
            public void onStateError() {
                if (ways == 0) {
                    rv_my_order.refreshComplete("fail");
                } else {
                    rv_my_order.loadMoreComplete();
                }

            }
        });

    }


    @Override
    public void onBtnClick(int position, String viewName) {
        OrderInfo orderInfo = orderInfos.get(position);
        Intent intent = new Intent();
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<OrderAddressBean>>() {
        }.getType();
        ArrayList<OrderAddressBean> orderAddressBeen = gson.fromJson(orderInfo.getJsonElement(), listType);
        switch (viewName) {
            case "查询报价":
                intent = new Intent(getActivity(), OrderOffersActivity.class);
                intent.putExtra("orderId", orderInfo.getOrderNo());
                intent.putExtra("type", orderInfo.getOrderType());
                intent.putParcelableArrayListExtra("orderAddressBeen", orderAddressBeen);
                intent.putExtra("time", orderInfo.getPlanSendTime());
                intent.putExtra("isAfterPay", true);
                getActivity().startActivityForResult(intent,1);
                break;
            case "取消订单":
                confirmCancel(orderInfo);
                break;
            case "支付":
                if (orderInfo.getCancelFee() > 0) {
                    intent.setClass(getActivity(), PayActivity.class);
                    intent.putExtra("orderNo", orderInfo.getOrderNo());
                    intent.putExtra("money", orderInfo.getCancelFee());
                    intent.putExtra("type", 5);
                    getActivity().startActivityForResult(intent,1);
                } else {
                    double allMoney = orderInfo.getFreight()+orderInfo.getPremiums();
                    String money = String.format("¥%s", allMoney);

                    intent.setClass(getActivity(), OrderPayActivity.class);
                    intent.putExtra("orderId", orderInfo.getOrderNo());
                    intent.putExtra("type", orderInfo.getOrderType());
                    intent.putParcelableArrayListExtra("orderAddressBeen", orderAddressBeen);
                    intent.putExtra("money", money);
                    intent.putExtra("time", orderInfo.getPlanSendTime());
                    intent.putExtra("isAfterPay", true);
                    getActivity().startActivityForResult(intent,1);
                }
                break;
            case "查看详情":
                intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra("orderId", orderInfo.getOrderNo());
                getActivity().startActivity(intent);
                break;
        }

    }

    private void confirmCancel(final OrderInfo orderInfo) {
        endDialog = MyDialog.endDialog(getActivity());
        endDialog.show();
        endDialog.setOnSettingListener(new MyDialog.EndListener() {
            @Override
            public void onSetting(String content) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("UserId", Contants.userId);
                jsonObject.addProperty("UserType", 1);
                jsonObject.addProperty("OrderNo", orderInfo.getOrderNo());
                jsonObject.addProperty("Reason", content);
                final Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
                HttpUtil.doPost(getActivity(), Contants.url_confirmCancel, "confirmCancel", map, new VolleyInterface(getActivity(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                    @Override
                    public void onSuccess(JsonElement result) {
                        Gson gson = new Gson();
                        PayFeeInfo payFeeInfo = gson.fromJson(result, PayFeeInfo.class);
                        if (payFeeInfo.getPayFee() > 0) {
                            new AlertDialog.Builder(getActivity()).
                                    setTitle("温馨提示").
                                    setMessage(String.format("取消此订单，会产生%s元费用,是否确认取消？", payFeeInfo.getPayFee())).
                                    setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            cancelOrder(map);
                                        }
                                    }).
                                    setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            endDialog.dismiss();
                                        }
                                    }).show();
                        } else {
                            endDialog.dismiss();
                            Toast.makeText(mContext, "取消成功", Toast.LENGTH_SHORT).show();
                            getOrderList(1, 0);

                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
//                LogUtil.i("hint",error.networkResponse.headers.toString());
//                LogUtil.i("hint",error.networkResponse.statusCode+"");
                    }

                    @Override
                    public void onStateError() {
                    }
                });
            }
        });
    }


    private void cancelOrder(Map<String, String> map) {

        HttpUtil.doPost(getActivity(), Contants.url_cancelOrder, "cancelOrder", map, new VolleyInterface(getActivity(), VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                endDialog.dismiss();
                Toast.makeText(mContext, "取消成功", Toast.LENGTH_SHORT).show();
                getOrderList(1, 0);
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(mContext, "连接超时", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStateError() {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){
            getOrderList(1, 0);
        }
    }
}
