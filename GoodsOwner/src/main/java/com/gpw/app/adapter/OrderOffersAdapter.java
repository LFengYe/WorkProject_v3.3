package com.gpw.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import com.gpw.app.R;
import com.gpw.app.activity.ConfirmOrderActivity;
import com.gpw.app.bean.OrderOfferInfo;

/**
 * Created by gpw on 2016/11/6.
 * --加油
 */

public class OrderOffersAdapter extends BaseAdapter {
    private ArrayList<OrderOfferInfo> orderOfferInfos;
    private Context mContext;

    public OrderOffersAdapter(ArrayList<OrderOfferInfo> orderOfferInfos, Context mContext) {
        super();
        this.orderOfferInfos = orderOfferInfos;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return orderOfferInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return orderOfferInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_order_offers, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_tel = (TextView) convertView.findViewById(R.id.tv_tel);
            viewHolder.tv_vehicleNo = (TextView) convertView.findViewById(R.id.tv_vehicleNo);
            viewHolder.tv_distances = (TextView) convertView.findViewById(R.id.tv_distances);
            viewHolder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            viewHolder.bt_ok = (Button) convertView.findViewById(R.id.bt_ok);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        OrderOfferInfo offerInfo  =orderOfferInfos.get(position);
        viewHolder.tv_name.setText(offerInfo.getTransporterName());
        viewHolder.tv_tel.setText(offerInfo.getTel());
        viewHolder.tv_vehicleNo.setText(offerInfo.getVehicleNo());
        viewHolder.tv_distances.setText(String.format("距您%s", offerInfo.getDistance()));
        viewHolder.tv_money.setText(String.format("¥ %s", offerInfo.getOffer()));
        viewHolder.bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ConfirmOrderActivity.class);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }


    private final class ViewHolder {
        TextView tv_money;
        TextView tv_name;
        TextView tv_distances;
        TextView tv_vehicleNo;
        TextView tv_tel;
        Button bt_ok;
    }
}
