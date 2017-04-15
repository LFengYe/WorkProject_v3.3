package com.DLPort.myadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.mydata.Duichang;

import java.util.List;

/**
 * Created by Administrator on 2016/5/16.
 */
public class DuiAdapter extends BaseAdapter{
    private int resourceId;
    private Context context;
    List<Duichang> datas;

    public DuiAdapter(Context context, int resource, List<Duichang> objects) {

        this.context=context;
        resourceId=resource;
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
            viewHolder.no= (TextView) convertView.findViewById(R.id.name_one);
            viewHolder.StorageYardNmae = (TextView) convertView.findViewById(R.id.name_two);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.no.setText(String.valueOf(datas.get(position).getNo()));
        viewHolder.StorageYardNmae.setText(datas.get(position).getStorageYardNmae());

        return convertView;
    }

    class ViewHolder {
        public TextView  no;
        public TextView  StorageYardNmae;
        public TextView  Address;
        public TextView  Price;
    }

}
