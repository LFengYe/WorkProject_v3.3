package com.gpw.app.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.gpw.app.R;
import com.gpw.app.bean.OrderAddressBean;
import com.gpw.app.util.LogUtil;

/**
 * Created by Administrator on 2016/11/21.
 * ---个人专属
 */

public class OrderAddInfoAdapter extends RecyclerView.Adapter<OrderAddInfoAdapter.ViewHolder> {
    private ArrayList<OrderAddressBean> mOrderAddressInfos;
    private Context mContext;
    private int size;

    public OrderAddInfoAdapter(ArrayList<OrderAddressBean> mOrderAddressInfos, Context mContext) {
        super();
        this.mOrderAddressInfos = mOrderAddressInfos;
        this.mContext = mContext;
        this.size = mOrderAddressInfos.size();
        LogUtil.i("size" + size);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        OrderAddressBean orderAddressInfo = mOrderAddressInfos.get(position);
        if (position == 0) {
            viewHolder.iv_state.setImageResource(R.mipmap.start);
        } else if (position == size - 1) {
            viewHolder.iv_state.setImageResource(R.mipmap.arrive);

        } else {
            viewHolder.iv_state.setImageResource(R.mipmap.pass);
        }
        viewHolder.tv_address.setText(orderAddressInfo.getAddress());
        viewHolder.tv_contact.setText(String.format("%s %s", orderAddressInfo.getReceipter(), orderAddressInfo.getTel()));
        viewHolder.view_line.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_line));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mOrderAddressInfos.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_state;
        TextView tv_address;
        TextView tv_contact;
        View view_line;


        public ViewHolder(View view) {
            super(view);
            iv_state = (ImageView) view.findViewById(R.id.iv_state);
            tv_address = (TextView) view.findViewById(R.id.tv_address);
            tv_contact = (TextView) view.findViewById(R.id.tv_contact);
            view_line = view.findViewById(R.id.view_line);


        }
    }

}
