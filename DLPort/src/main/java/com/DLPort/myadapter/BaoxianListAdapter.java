package com.DLPort.myadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.mydata.Duichang;
import com.DLPort.mydata.baoxian;

import java.util.List;

/**
 * Created by fuyzh on 16/5/17.
 */
public class BaoxianListAdapter extends BaseAdapter{
    private int resourceId;
    private Context context;
    private List<baoxian> datas;

    public BaoxianListAdapter(Context context, int resourceId,List<baoxian> objects) {
        this.resourceId = resourceId;
        this.context = context;
        datas=objects;
    }

    @Override
    public int getCount() {
        return datas.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, resourceId, null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
            viewHolder.name = (TextView) convertView.findViewById(R.id.baoxian_name);
            viewHolder.type = (TextView) convertView.findViewById(R.id.baoxian_type);
            viewHolder.price = (TextView) convertView.findViewById(R.id.baoxian_price);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(datas.get(position).getInsuranceName());
        viewHolder.type.setText(datas.get(position).getInsuranceType());
        viewHolder.price.setText(datas.get(position).getMoney()+"Ԫ");


        return convertView;
    }

    class ViewHolder{
        public TextView name;
        public TextView  type;
        public TextView  price;

    }
}
