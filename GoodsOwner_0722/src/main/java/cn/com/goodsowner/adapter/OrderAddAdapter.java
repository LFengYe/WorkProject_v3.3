package cn.com.goodsowner.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.com.goodsowner.R;
import cn.com.goodsowner.bean.OrderAddressBean;

/**
 * Created by Administrator on 2016/11/21.
 * ---个人专属
 */

public class OrderAddAdapter extends BaseAdapter {
    private ArrayList<OrderAddressBean> orderAddressBeen;
    private Context mContext;
    private int size;

    public OrderAddAdapter(ArrayList<OrderAddressBean> orderAddressBeen, Context mContext) {
        super();
        this.orderAddressBeen = orderAddressBeen;
        this.mContext = mContext;
        this.size = orderAddressBeen.size();
    }

    @Override
    public int getCount() {
        return orderAddressBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return orderAddressBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_order_address, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_state = (ImageView) convertView.findViewById(R.id.iv_state);
            viewHolder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            viewHolder.tv_contact = (TextView) convertView.findViewById(R.id.tv_contact);
            viewHolder.view_line = convertView.findViewById(R.id.view_line);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        OrderAddressBean addressBean  =orderAddressBeen.get(position);
        if (position== 0) {
            viewHolder.iv_state.setImageResource(R.mipmap.start);
        } else if (position== size-1) {
            viewHolder.iv_state.setImageResource(R.mipmap.arrive);
        } else {
            viewHolder.iv_state.setImageResource(R.mipmap.pass);
        }


        viewHolder.tv_address.setText(addressBean.getAddress());
        viewHolder.tv_contact.setText(String.format("%s %s", addressBean.getReceipter(), addressBean.getTel()));
        viewHolder.view_line.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
        return convertView;
    }

    private final class ViewHolder {
        ImageView iv_state;
        TextView tv_address;
        TextView tv_contact;
        View view_line;
    }

}
