package com.gpw.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.gpw.app.R;
import com.gpw.app.bean.OrderAddressBean;
import com.gpw.app.bean.OrderInfo;
import com.gpw.app.util.LogUtil;

/**
 * Created by gpw on 2016/11/5.
 * --加油
 */

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    private ArrayList<OrderInfo> orderInfos;

    private Context context;


    public MyOrderAdapter(Context context, ArrayList<OrderInfo> orderInfos) {
        super();
        this.orderInfos = orderInfos;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_order, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final OrderInfo orderInfo = orderInfos.get(position);
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<OrderAddressBean>>() {
        }.getType();
        ArrayList<OrderAddressBean> orderAddressBeen = gson.fromJson(orderInfo.getJsonElement(), listType);

        LogUtil.i(orderAddressBeen.toString());
        OrderAddInfoAdapter addInfoAdapter = new OrderAddInfoAdapter(orderAddressBeen, context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewHolder.rv_order_address.setLayoutManager(layoutManager);
        viewHolder.rv_order_address.setAdapter(addInfoAdapter);
        double allMoney = orderInfo.getFreight() + orderInfo.getPremiums();
        viewHolder.tv_money.setText(String.format("¥%s", allMoney));
        viewHolder.tv_orderId.setText(String.format("订单号：%s", orderInfo.getOrderNo()));
        viewHolder.tv_time.setText(orderInfo.getPlanSendTime());


        if (orderInfo.getCancelFee() > 0) {
            viewHolder.tv_state.setVisibility(View.VISIBLE);
            viewHolder.bt_query.setVisibility(View.VISIBLE);
            viewHolder.tv_money.setVisibility(View.VISIBLE);
            viewHolder.bt_pay.setVisibility(View.VISIBLE);
            viewHolder.tv_money.setText(String.format("¥%s", orderInfo.getCancelFee()));
            viewHolder.bt_query.setVisibility(View.GONE);
            viewHolder.tv_state.setText("违约金");
            viewHolder.tv_noDriver.setVisibility(View.GONE);
            viewHolder.tv_addMoney.setVisibility(View.GONE);
        } else {
            switch (orderInfo.getOrderStatus()) {
                case 1:
                    if (orderInfo.getOrderType() == 3) {
                        viewHolder.bt_query.setText("查询报价");
                        viewHolder.tv_state.setText("询价进行中");
                        viewHolder.tv_state.setVisibility(View.VISIBLE);
                        viewHolder.bt_query.setVisibility(View.VISIBLE);
                        viewHolder.tv_noDriver.setVisibility(View.GONE);
                        viewHolder.bt_pay.setVisibility(View.GONE);
                        viewHolder.tv_addMoney.setVisibility(View.GONE);
                        viewHolder.tv_money.setVisibility(View.GONE);
                    } else {
                        viewHolder.bt_query.setText("取消订单");
                        viewHolder.tv_state.setVisibility(View.GONE);
                        viewHolder.bt_query.setVisibility(View.VISIBLE);
                        viewHolder.tv_noDriver.setVisibility(View.VISIBLE);
                        viewHolder.bt_pay.setVisibility(View.GONE);
                        viewHolder.tv_addMoney.setVisibility(View.VISIBLE);
                        viewHolder.tv_money.setVisibility(View.VISIBLE);
                        if (orderInfo.getFinanceStatus() == 1 && !orderInfo.getIsToPay().equals("True")) {
                            viewHolder.bt_pay.setVisibility(View.VISIBLE);
                            viewHolder.tv_state.setText("待支付");
                            viewHolder.tv_state.setVisibility(View.VISIBLE);
                            viewHolder.tv_noDriver.setVisibility(View.GONE);
                            viewHolder.tv_addMoney.setVisibility(View.GONE);
                        }
                    }
                    break;
                case 2:
                    viewHolder.tv_state.setText("进行中");
                    viewHolder.bt_query.setText("查看详情");
                    visible(viewHolder);
                    break;
                case 3:
                    viewHolder.tv_state.setText("已送达");
                    viewHolder.bt_query.setText("查看详情");
                    visible(viewHolder);
                    break;
                case 4:
                    viewHolder.tv_state.setText("已完成");
                    viewHolder.bt_query.setText("查看详情");
                    visible(viewHolder);
                    break;
                case -1:
                    viewHolder.tv_state.setText("已取消");
                    visible(viewHolder);
                    viewHolder.bt_query.setVisibility(View.GONE);
                    break;
            }

        }

        viewHolder.bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = (Button) view;
                String text = button.getText().toString();
                mOnBtnClickListener.onBtnClick(position, text);
            }
        });
        viewHolder.bt_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = (Button) view;
                String text = button.getText().toString();
                mOnBtnClickListener.onBtnClick(position, text);
            }
        });
    }

    private void visible(ViewHolder viewHolder) {
        viewHolder.tv_state.setVisibility(View.VISIBLE);
        viewHolder.bt_query.setVisibility(View.VISIBLE);
        viewHolder.tv_noDriver.setVisibility(View.GONE);
        viewHolder.bt_pay.setVisibility(View.GONE);
        viewHolder.tv_addMoney.setVisibility(View.GONE);
        viewHolder.tv_money.setVisibility(View.VISIBLE);
    }


    //获取数据的数量
    @Override
    public int getItemCount() {
        return orderInfos.size();
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        Button bt_query;
        Button bt_pay;
        TextView tv_money;
        TextView tv_time;
        TextView tv_orderId;
        TextView tv_state;
        TextView tv_noDriver;
        TextView tv_addMoney;
        RecyclerView rv_order_address;

        public ViewHolder(View view) {
            super(view);
            bt_query = (Button) view.findViewById(R.id.bt_query);
            bt_pay = (Button) view.findViewById(R.id.bt_pay);
            tv_money = (TextView) view.findViewById(R.id.tv_money);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_orderId = (TextView) view.findViewById(R.id.tv_orderId);
            rv_order_address = (RecyclerView) view.findViewById(R.id.rv_order_address);
            tv_state = (TextView) view.findViewById(R.id.tv_state);
            tv_noDriver = (TextView) view.findViewById(R.id.tv_noDriver);
            tv_addMoney = (TextView) view.findViewById(R.id.tv_addMoney);
        }
    }

    public interface OnBtnClickListener {
        void onBtnClick(int position, String viewName);
    }

    private OnBtnClickListener mOnBtnClickListener;


    public void setOnBtnClickListener(OnBtnClickListener mOnBtnClickListener) {
        this.mOnBtnClickListener = mOnBtnClickListener;
    }
}
