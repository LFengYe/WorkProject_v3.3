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

import com.DLPort.FindActivity.TouziContent;
import com.DLPort.R;
import com.DLPort.myactivity.OrderParticular;
import com.DLPort.mydata.Touzi;
import com.DLPort.mydata.TwoCar;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyToast;

import java.util.List;

/**
 * Created by Administrator on 2016/5/19.
 */
public class TouziAdapter extends BaseAdapter{
    private int resourceId;
    private Context context;
    List<Touzi> orders;

    public TouziAdapter(Context context, int resource,List<Touzi> objects) {

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
        if (convertView == null) {
            convertView = View.inflate(context, resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.ManageMoneyName = (TextView) convertView.findViewById(R.id.li_one);
            viewHolder.ManageMoneyIntro = (TextView) convertView.findViewById(R.id.li_two);
            viewHolder.button = (Button) convertView.findViewById(R.id.licai_Button);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ManageMoneyName.setText(orders.get(position).getManageMoneyName());
        viewHolder.ManageMoneyIntro.setText(orders.get(position).getManageMoneyIntro());
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalParams.isNetworkAvailable(context)) {
                    Intent intent = new Intent(context, TouziContent.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("ID", orders.get(position).getId());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }else {
                    MyToast.makeText(context, "亲,请连接网络！！！");
                }

            }
        });
        return convertView;
    }

    class ViewHolder{
        public TextView ManageMoneyName;
        public TextView ManageMoneyIntro;
        public Button button;
    }




}
