package com.gpw.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import com.gpw.app.R;
import com.gpw.app.bean.ConvoyInfo;

/**
 * Created by gpw on 2016/11/6.
 * --加油
 */

public class ConvoyAdapter extends BaseAdapter {
    private ArrayList<ConvoyInfo> mConvoyInfos;
    private Context mContext;

    public ConvoyAdapter(ArrayList<ConvoyInfo> mConvoyInfos, Context mContext) {
        super();
        this.mConvoyInfos = mConvoyInfos;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mConvoyInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mConvoyInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_my_convoy, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_tel = (TextView) convertView.findViewById(R.id.tv_tel);
            viewHolder.tv_vehicleNo = (TextView) convertView.findViewById(R.id.tv_vehicleNo);
            viewHolder.rb_score = (RatingBar) convertView.findViewById(R.id.rb_score);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ConvoyInfo convoyInfo  =mConvoyInfos.get(position);
        viewHolder.tv_name.setText(convoyInfo.getTransporterName());
        viewHolder.tv_tel.setText(convoyInfo.getTel());
        viewHolder.tv_vehicleNo.setText(convoyInfo.getVehicleNo());
        viewHolder.tv_time.setText(convoyInfo.getCreateTime());
        viewHolder.rb_score.setProgress(convoyInfo.getScore());

        return convertView;
    }


    private final class ViewHolder {
        TextView tv_name;
        TextView tv_tel;
        TextView tv_vehicleNo;
        RatingBar rb_score;
        TextView tv_time;
    }
}
