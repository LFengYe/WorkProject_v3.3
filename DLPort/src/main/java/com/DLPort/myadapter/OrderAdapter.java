package com.DLPort.myadapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.DLPort.R;
import com.DLPort.myactivity.GrabOrderActivity;
import com.DLPort.mydata.Order;
import com.DLPort.mytool.GlobalParams;

import java.util.List;

/**
 * Created by Administrator on 2016/4/18.
 */
public class OrderAdapter extends BaseAdapter {
    private static final String TAG = "OrderAdapter";
    List<Order> list;
    private int resourceId;
    private Context context;

    public OrderAdapter(Context context, int resource, List<Order> objects) {
        this.context = context;
        resourceId = resource;
        list = objects;

    }

    public void addItem(List<Order> Orders) {
        list.addAll(Orders);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
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

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.LoadTimeY = (TextView) convertView.findViewById(R.id.order_LoadTimeY);
            viewHolder.LoadTimeX = (TextView) convertView.findViewById(R.id.order_LoadTimeX);
            viewHolder.ContainerType = (TextView) convertView.findViewById(R.id.order_ContainerType);
            viewHolder.Remain = (TextView) convertView.findViewById(R.id.order_Remain);
            viewHolder.BussinessType = (TextView) convertView.findViewById(R.id.order_BussinessType);
            viewHolder.Price = (TextView) convertView.findViewById(R.id.order_Price);
            viewHolder.Destination = (TextView) convertView.findViewById(R.id.order_Destination);
            viewHolder.StartAddress = (TextView) convertView.findViewById(R.id.order_StartAddress);
            viewHolder.qiangorder = (Button) convertView.findViewById(R.id.order_qiang);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.LoadTimeY.setText(list.get(position).getLoadTimeY());
        viewHolder.LoadTimeX.setText(list.get(position).getLoadTimeX());
        viewHolder.ContainerType.setText(list.get(position).getContainerType());
        viewHolder.Remain.setText(list.get(position).getRemain());
        viewHolder.BussinessType.setText(list.get(position).getBussinessType());
        viewHolder.Price.setText(list.get(position).getPrice());
        viewHolder.Destination.setText(list.get(position).getDestination());
        viewHolder.StartAddress.setText(list.get(position).getStartAddress());
        if (list.get(position).getIsFinish() == 0) {
            viewHolder.qiangorder.setBackgroundResource(R.drawable.order_button);
            viewHolder.qiangorder.setText("抢单承运");
            viewHolder.qiangorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (GlobalParams.isNetworkAvailable(context)) {
                        Intent intent = new Intent(context, GrabOrderActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("CargoId", list.get(position).getCargoId());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "亲,请连接网络！！！", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            viewHolder.qiangorder.setBackgroundResource(R.drawable.old_order_button);
            viewHolder.qiangorder.setText("历史订单");
            viewHolder.qiangorder.setOnClickListener(null);
        }
        return convertView;
    }

    public class ViewHolder {
        public TextView LoadTimeY;
        public TextView LoadTimeX;
        public TextView ContainerType;
        public TextView Remain;
        public TextView BussinessType;
        public TextView Price;
        public TextView Destination;
        public TextView StartAddress;
        public Button qiangorder;

    }


}
