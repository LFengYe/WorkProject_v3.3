package com.gpw.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gpw.app.R;
import com.gpw.app.activity.OrderReceiptDetailActivity;
import com.gpw.app.bean.OrderAddressBean;
import com.gpw.app.bean.ReceiptOrderInfo;
import com.gpw.app.util.LogUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by gpw on 2016/11/5.
 * --加油
 */

public class ReceiptOrderAdapter extends RecyclerView.Adapter<ReceiptOrderAdapter.ViewHolder> {

    private ArrayList<ReceiptOrderInfo> receiptOrderInfos;

    private Context context;


    public ReceiptOrderAdapter(Context context, ArrayList<ReceiptOrderInfo> receiptOrderInfos) {
        super();
        this.receiptOrderInfos = receiptOrderInfos;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_receipte_order, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final ReceiptOrderInfo receiptOrderInfo = receiptOrderInfos.get(position);
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<OrderAddressBean>>() {
        }.getType();
        ArrayList<OrderAddressBean> orderAddressBeen = gson.fromJson(receiptOrderInfo.getJsonElement(), listType);

        LogUtil.i(orderAddressBeen.toString());
        OrderAddInfoAdapter addInfoAdapter = new OrderAddInfoAdapter(orderAddressBeen, context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewHolder.rv_order_address.setLayoutManager(layoutManager);
        viewHolder.rv_order_address.setAdapter(addInfoAdapter);
        viewHolder.tv_money.setText(String.format("¥%s", receiptOrderInfo.getFreight()));
        viewHolder.tv_orderId.setText(String.format("订单号：%s", receiptOrderInfo.getOrderNo()));
        viewHolder.tv_time.setText(receiptOrderInfo.getPlanSendTime());
        viewHolder.bt_query.setText("查看详情");

        viewHolder.bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderReceiptDetailActivity.class);
                intent.putExtra("orderId", receiptOrderInfo.getOrderNo());
                context.startActivity(intent);
            }
        });

    }


    //获取数据的数量
    @Override
    public int getItemCount() {
        return receiptOrderInfos.size();
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        Button bt_query;
        TextView tv_money;
        TextView tv_time;
        TextView tv_orderId;
        RecyclerView rv_order_address;

        public ViewHolder(View view) {
            super(view);
            bt_query = (Button) view.findViewById(R.id.bt_query);
            tv_money = (TextView) view.findViewById(R.id.tv_money);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_orderId = (TextView) view.findViewById(R.id.tv_orderId);
            rv_order_address = (RecyclerView) view.findViewById(R.id.rv_order_address);
        }

    }
}
