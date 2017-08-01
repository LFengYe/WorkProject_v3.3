package cn.com.goodsowner.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.com.goodsowner.bean.OrderAddressBean;
import cn.com.goodsowner.util.LogUtil;

/**
 * Created by Administrator on 2016/11/21.
 * ---个人专属
 */

public class OrderAddInfoAdapter extends RecyclerView.Adapter<OrderAddInfoAdapter.ViewHolder> {
    private ArrayList<OrderAddressBean> mOrderAddressInfos;
    private Context mContext;
    private int size;
    private boolean color;

    public OrderAddInfoAdapter(ArrayList<OrderAddressBean> mOrderAddressInfos, Context mContext,boolean color) {
        super();
        this.mOrderAddressInfos = mOrderAddressInfos;
        this.mContext = mContext;
        this.size = mOrderAddressInfos.size();
        this.color = color;
        LogUtil.i("size" + size);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(cn.com.goodsowner.R.layout.item_order_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        OrderAddressBean orderAddressInfo = mOrderAddressInfos.get(position);
        if (color){
            viewHolder.tv_address.setTextColor(ContextCompat.getColor(mContext, cn.com.goodsowner.R.color.color_white_font));
            viewHolder.tv_contact.setTextColor(ContextCompat.getColor(mContext, cn.com.goodsowner.R.color.color_white_font));
            if (position == 0) {
                viewHolder.iv_state.setImageResource(cn.com.goodsowner.R.mipmap.start_gray);
            } else if (position == size - 1) {
                viewHolder.iv_state.setImageResource(cn.com.goodsowner.R.mipmap.arrive_gray);

            } else {
                viewHolder.iv_state.setImageResource(cn.com.goodsowner.R.mipmap.pass_gray);
            }
        }else {
            if (position == 0) {
                viewHolder.iv_state.setImageResource(cn.com.goodsowner.R.mipmap.start);
            } else if (position == size - 1) {
                viewHolder.iv_state.setImageResource(cn.com.goodsowner.R.mipmap.arrive);

            } else {
                viewHolder.iv_state.setImageResource(cn.com.goodsowner.R.mipmap.pass);
            }
        }
        viewHolder.tv_address.setText(orderAddressInfo.getAddress());
        viewHolder.tv_contact.setText(String.format("%s %s", orderAddressInfo.getReceipter(), orderAddressInfo.getTel()));
        viewHolder.view_line.setBackgroundColor(ContextCompat.getColor(mContext, cn.com.goodsowner.R.color.color_line));
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
            iv_state = (ImageView) view.findViewById(cn.com.goodsowner.R.id.iv_state);
            tv_address = (TextView) view.findViewById(cn.com.goodsowner.R.id.tv_address);
            tv_contact = (TextView) view.findViewById(cn.com.goodsowner.R.id.tv_contact);
            view_line = view.findViewById(cn.com.goodsowner.R.id.view_line);


        }
    }

}
