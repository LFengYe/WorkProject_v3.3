package com.DLPort.myadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.mydata.Handbook;

import java.util.ArrayList;

/**
 * Created by fuyzh on 16/5/19.
 */
public class HandbookAdapter extends BaseAdapter {
    private int resourceId;
    private Context context;
    private ArrayList<Handbook> list;

    public ArrayList<Handbook> getList() {
        return list;
    }

    public void setList(ArrayList<Handbook> list) {
        this.list = list;
    }

    public HandbookAdapter(int resourceId, Context context) {
        this.resourceId = resourceId;
        this.context = context;
    }

    public HandbookAdapter(int resourceId, Context context, ArrayList<Handbook> list) {
        this.resourceId = resourceId;
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (null != list)
            return list.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = View.inflate(context, resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.problem = (TextView) convertView.findViewById(R.id.handbook_problem);
            viewHolder.answer = (TextView) convertView.findViewById(R.id.handbook_answer);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Handbook handbook = list.get(position);
        viewHolder.problem.setText(handbook.getProblem());
        viewHolder.answer.setText(handbook.getAnswer());

        return convertView;
    }

    class ViewHolder {
        private TextView problem;
        private TextView answer;
    }
}
