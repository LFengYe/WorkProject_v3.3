package cn.com.goodsowner.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.com.goodsowner.R;
import cn.com.goodsowner.bean.OrderDetailInfo;

/**
 * Created by gpw on 2016/11/6.
 * --加油
 */

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    private ArrayList<OrderDetailInfo.OrderAddressBean> orderAddressBeens;
    private Context mContext;
    private OrderDetailInfo orderDetailInfo;
    private int size;


    public OrderDetailAdapter(OrderDetailInfo orderDetailInfo, Context mContext,ArrayList<OrderDetailInfo.OrderAddressBean> orderAddressBeens) {
        super();
        this.orderAddressBeens = orderAddressBeens;
        this.mContext = mContext;
        this.orderDetailInfo = orderDetailInfo;
        this.size = orderAddressBeens.size();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        OrderDetailInfo.OrderAddressBean orderAddressBean = orderAddressBeens.get(position);
        if (position == 0) {
            if (orderAddressBean.getDischargeTime().equals("")) {
                viewHolder.iv_state.setImageResource(R.mipmap.start);
                viewHolder.bt_location.setBackgroundResource(R.drawable.button_red_bg);
                viewHolder.bt_location.setClickable(true);
                viewHolder.bt_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button button = (Button) v;
                        String name = button.getText().toString();
                        mOnBtnClickListener.onBtnClick(position, name);
                    }
                });
            } else {
                if (isFinish(size - 1) == 1) {
                    viewHolder.iv_state.setImageResource(R.mipmap.start_red);
                } else {
                    viewHolder.iv_state.setImageResource(R.mipmap.start_gray);
                }
                viewHolder.bt_location.setBackgroundResource(R.drawable.button_gray_bg);
                viewHolder.bt_location.setClickable(false);
            }
            viewHolder.tv_state.setText("到达起点，装货发车");
            viewHolder.tv_time.setText(orderAddressBean.getDischargeTime());
            viewHolder.bt_confirm.setVisibility(View.GONE);

        } else if (position == size - 1) {
            if (!orderAddressBean.getDischargeTime().equals("")) {
                if (isFinish(position) == -1 && orderDetailInfo.getSendScore()==0) {
                    viewHolder.iv_state.setImageResource(R.mipmap.arrive_red);
                } else {
                    viewHolder.iv_state.setImageResource(R.mipmap.arrive_gray);
                }
                viewHolder.bt_confirm.setBackgroundResource(R.drawable.button_gray_bg);
                viewHolder.bt_confirm.setClickable(false);
                viewHolder.bt_location.setBackgroundResource(R.drawable.button_gray_bg);
                viewHolder.bt_location.setClickable(false);
            } else {
                viewHolder.iv_state.setImageResource(R.mipmap.arrive);
                viewHolder.bt_confirm.setBackgroundResource(R.drawable.button_red_bg);
                viewHolder.bt_confirm.setClickable(true);
                viewHolder.bt_location.setBackgroundResource(R.drawable.button_red_bg);
                viewHolder.bt_location.setClickable(true);
                viewHolder.bt_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button button = (Button) v;
                        String name = button.getText().toString();
                        mOnBtnClickListener.onBtnClick(position, name);
                    }
                });
                viewHolder.bt_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button button = (Button) v;
                        String name = button.getText().toString();
                        mOnBtnClickListener.onBtnClick(position, name);
                    }
                });


            }
            viewHolder.tv_state.setText("到达终点，已卸货");
            viewHolder.tv_time.setText(orderAddressBean.getDischargeTime());
            viewHolder.bt_confirm.setVisibility(View.VISIBLE);
            viewHolder.bt_confirm.setText("确认收货");

        } else {
            if (!orderAddressBean.getDischargeTime().equals("")) {
                if (isFinish(size - 1) == position + 1) {
                    viewHolder.iv_state.setImageResource(R.mipmap.pass_red);
                } else {
                    viewHolder.iv_state.setImageResource(R.mipmap.pass_gray);
                }
                viewHolder.bt_confirm.setBackgroundResource(R.drawable.button_gray_bg);
                viewHolder.bt_confirm.setClickable(false);
                viewHolder.bt_location.setBackgroundResource(R.drawable.button_gray_bg);
                viewHolder.bt_location.setClickable(false);
            } else {
                viewHolder.iv_state.setImageResource(R.mipmap.pass);
                viewHolder.bt_confirm.setBackgroundResource(R.drawable.button_red_bg);
                viewHolder.bt_confirm.setClickable(true);
                viewHolder.bt_location.setBackgroundResource(R.drawable.button_red_bg);
                viewHolder.bt_location.setClickable(true);
                viewHolder.bt_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button button = (Button) v;
                        String name = button.getText().toString();
                        mOnBtnClickListener.onBtnClick(position, name);
                    }
                });
                viewHolder.bt_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button button = (Button) v;
                        String name = button.getText().toString();
                        mOnBtnClickListener.onBtnClick(position, name);
                    }
                });
            }
            viewHolder.tv_state.setText("到达中途点，卸货发车");
            viewHolder.tv_time.setText(orderAddressBean.getDischargeTime());
            viewHolder.bt_confirm.setVisibility(View.VISIBLE);
            viewHolder.bt_confirm.setText("确认卸货");
        }

        viewHolder.tv_address.setText(String.format("%s  %s  %s", orderAddressBean.getAddress(), orderAddressBean.getReceipter(), orderAddressBean.getTel()));
    }

    private int isFinish(int pos) {
        for (int i = 0; i <= pos; i++) {
            OrderDetailInfo.OrderAddressBean orderAddressBean1 = orderAddressBeens.get(i);
            if (orderAddressBean1.getDischargeTime().equals("")) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return orderAddressBeens.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_state;
        TextView tv_time;
        TextView tv_address;
        Button bt_confirm;
        Button bt_location;
        ImageView iv_state;


        public ViewHolder(View view) {
            super(view);
            tv_state = (TextView) view.findViewById(R.id.tv_state);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            iv_state = (ImageView) view.findViewById(R.id.iv_state);
            tv_address = (TextView) view.findViewById(R.id.tv_address);
            bt_confirm = (Button) view.findViewById(R.id.bt_confirm);
            bt_location = (Button) view.findViewById(R.id.bt_location);

        }
    }


    public interface OnBtnClickListener {
        void onBtnClick(int position, String viewName);

    }

    private OnBtnClickListener mOnBtnClickListener;


    public void setOnBtnClickListener(OnBtnClickListener mOnBtnClickListener) {
        this.mOnBtnClickListener = mOnBtnClickListener;
    }

    public OrderDetailInfo getOrderDetailInfo() {
        return orderDetailInfo;
    }

    public void setOrderDetailInfo(OrderDetailInfo orderDetailInfo) {
        this.orderDetailInfo = orderDetailInfo;
    }
}
