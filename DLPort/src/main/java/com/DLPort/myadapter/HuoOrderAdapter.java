package com.DLPort.myadapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.myactivity.HuoInquireActivity;
import com.DLPort.mydata.HuoOrder;

import java.util.List;

/**
 * Created by Administrator on 2016/5/8.
 */
public class HuoOrderAdapter extends BaseAdapter{
    private int resourceId;
    private Context context;
    List<HuoOrder> orders;

    public HuoOrderAdapter(Context context, int resource,List<HuoOrder> objects) {

        this.context=context;
        resourceId=resource;
        orders=objects;
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


        ViewHolder viewHolder;
        if (convertView == null){
            convertView = View.inflate(context,resourceId,null);
            viewHolder =new ViewHolder();
            viewHolder.CreateTimeY = (TextView) convertView.findViewById(R.id.huo_LoadTimeY);
            viewHolder.CreateTimeX = (TextView) convertView.findViewById(R.id.huo_LoadTimeX);
            viewHolder.ContainerType = (TextView) convertView.findViewById(R.id.huo_ContainerType);
            viewHolder.Amount = (TextView) convertView.findViewById(R.id.huo_Remain);

            viewHolder.BussinessType = (TextView) convertView.findViewById(R.id.huo_BussinessType);
            viewHolder.Status = (TextView) convertView.findViewById(R.id.huo_Status);
            viewHolder.Price = (TextView) convertView.findViewById(R.id.huo_Price);
            viewHolder.Destination = (TextView) convertView.findViewById(R.id.huo_Destination);
            viewHolder.StartAddress = (TextView) convertView.findViewById(R.id.huo_StartAddress);
            viewHolder.huoqiang = (Button) convertView.findViewById(R.id.huo_qiang);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.CreateTimeY.setText(orders.get(position).getCreateTimeY());
        viewHolder.CreateTimeX.setText(orders.get(position).getCreateTimeX());
        viewHolder.ContainerType.setText(orders.get(position).getContainerType());
        viewHolder.Amount.setText(orders.get(position).getAmount());
        viewHolder.BussinessType.setText(orders.get(position).getBussinessType());
        viewHolder.Status.setText(orders.get(position).getStatus());
        viewHolder.Price.setText(orders.get(position).getPrice());
        viewHolder.Destination.setText(orders.get(position).getDestination());
        viewHolder.StartAddress.setText(orders.get(position).getStartAddress());
        if (orders.get(position).getIsFinish() == 1) {
            viewHolder.huoqiang.setText("历史订单");
            viewHolder.huoqiang.setBackgroundResource(R.drawable.old_order_button);
        } else {
            viewHolder.huoqiang.setText("订单详情");
            viewHolder.huoqiang.setBackgroundResource(R.drawable.order_button);
        }
        viewHolder.huoqiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HuoInquireActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ID", orders.get(position).getCargoId());
                bundle.putString("Price",orders.get(position).getPrice());
                bundle.putInt("ChargeType", orders.get(position).getChargeType());
                bundle.putString("Amount",orders.get(position).getAmount());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        return convertView;
    }


    class ViewHolder{


        public TextView CreateTimeY;
        public TextView CreateTimeX;

        public TextView ContainerType;
        public TextView Amount;

        public TextView BussinessType;
        public TextView Status;
        public TextView Price;
        public TextView StartAddress;
        public TextView Destination;
        public Button huoqiang;


    }
}
