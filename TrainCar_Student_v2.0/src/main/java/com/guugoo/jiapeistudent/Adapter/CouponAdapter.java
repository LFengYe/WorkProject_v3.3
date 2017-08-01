package com.guugoo.jiapeistudent.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guugoo.jiapeistudent.Data.CouponDetail;
import com.guugoo.jiapeistudent.R;

import java.util.List;

/**
 * Created by LFeng on 2017/7/26.
 */

public class CouponAdapter extends BaseAdapter {

    private Context context;
    private List<CouponDetail> list;

    public CouponAdapter(Context context, List<CouponDetail> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null)
            return list.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.layout_coupon_item, null);
            viewHolder.cumulativeHours = (TextView) convertView.findViewById(R.id.cumulative_hours);
            viewHolder.currentSubject = (TextView) convertView.findViewById(R.id.current_subject);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CouponDetail detail = list.get(position);
        viewHolder.cumulativeHours.setText(detail.getCumulativeHours());
        viewHolder.currentSubject.setText(detail.getCurrentSubject());
        return convertView;
    }

    class ViewHolder {
        private TextView currentSubject;
        private TextView cumulativeHours;
    }
}
