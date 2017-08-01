package com.guugoo.jiapeiteacher.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guugoo.jiapeiteacher.R;
import com.guugoo.jiapeiteacher.bean.BlanchInfo;
import com.guugoo.jiapeiteacher.bean.Booking;

import java.util.List;

/**
 * Created by LFeng on 2017/5/31.
 */

public class BlanchInfoAdapter extends BaseAdapter {

    private List<BlanchInfo> blanchInfos;
    private Context context;

    public BlanchInfoAdapter(List<BlanchInfo> students, Context context) {
        this.blanchInfos = students;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (blanchInfos != null)
            return blanchInfos.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (blanchInfos != null)
            return blanchInfos.get(position);
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
            convertView = View.inflate(context, R.layout.layout_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.student_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BlanchInfo blanchInfo = blanchInfos.get(position);
        viewHolder.textView.setText(blanchInfo.getBranchSchoolName());
        return convertView;
    }

    class ViewHolder{
        public TextView textView;
    }

    public interface OnItemClickListener {
        void onItemClick(Booking booking, int position);
    }

    private BlanchInfoAdapter.OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(BlanchInfoAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
