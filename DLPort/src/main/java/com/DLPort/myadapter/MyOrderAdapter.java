package com.DLPort.myadapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.DLPort.R;
import com.DLPort.myactivity.OrderParticular;
import com.DLPort.mydata.CarOrder;
import com.DLPort.mytool.GlobalParams;

import java.util.List;

/**
 * Created by Administrator on 2016/4/18.
 */
public class MyOrderAdapter extends BaseAdapter{
    private int resourceId;
    private Context context;
    List<CarOrder> orders;

    public MyOrderAdapter(Context context, int resource,List<CarOrder> objects) {

        this.context=context;
        resourceId=resource;
        orders=objects;
    }
    public void addItem(CarOrder carOrder){
        orders.add(carOrder);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view;
        final ViewHolder viewHolder;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.my_old_order, null);
            view=convertView;
            viewHolder =new ViewHolder();
            viewHolder.CreateTimeY = (TextView) view.findViewById(R.id.old_t_one);
            viewHolder.CreateTimeX = (TextView) view.findViewById(R.id.old_t_two);
            viewHolder.CarNo = (TextView) view.findViewById(R.id.old_t_three);
            viewHolder.TripLong = (TextView) view.findViewById(R.id.old_t_four);
            viewHolder.OrderStatus = (TextView) view.findViewById(R.id.old_t_five);
            viewHolder.Price = (TextView) view.findViewById(R.id.old_t_six);
            viewHolder.StartAddress = (TextView) view.findViewById(R.id.old_t_seven);
            viewHolder.Destination = (TextView) view.findViewById(R.id.old_t_eight);
            viewHolder.order_xiang = (Button) view.findViewById(R.id.old_five);
            view.setTag(viewHolder);
        }else {
            view =convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.CreateTimeY.setText(orders.get(position).getCreateTimeY());
        viewHolder.CreateTimeX.setText(orders.get(position).getCreateTimeX());
        viewHolder.CarNo.setText(orders.get(position).getCarNo());
        viewHolder.TripLong.setText(orders.get(position).getTripLong());
        viewHolder.OrderStatus.setText(orders.get(position).getOrderStatus());
        viewHolder.Price.setText(orders.get(position).getPrice());
        viewHolder.StartAddress.setText(orders.get(position).getStartAddress());
        viewHolder.Destination.setText(orders.get(position).getDestination());
        if (orders.get(position).getOrderStatusValue() == 1 ||
                orders.get(position).getOrderStatusValue() == 3) {
            viewHolder.order_xiang.setText("历史订单");
            viewHolder.order_xiang.setBackgroundResource(R.drawable.old_order_button);
        } else {
            viewHolder.order_xiang.setText("订单详情");
            viewHolder.order_xiang.setBackgroundResource(R.drawable.order_button);
        }

        viewHolder.order_xiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalParams.isNetworkAvailable(context)) {
                    Intent intent = new Intent(context, OrderParticular.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("ID", orders.get(position).getOrderId());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }else {
                    Toast.makeText(context, "亲,请连接网络！！！", Toast.LENGTH_SHORT)
                                .show();
                }
            }
        });
        return view;

    }

    class ViewHolder{

        public TextView CreateTimeY;
        public TextView CreateTimeX;
        public TextView CarNo;
        public TextView TripLong;
        public TextView OrderStatus;
        public TextView Price;
        public TextView StartAddress;
        public TextView Destination;
        public Button order_xiang;

    }

}
