package com.DLPort.myadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.mydata.ActivityMessage;

import java.util.ArrayList;

/**
 * Created by fuyzh on 16/5/21.
 */
public class MessageAdapter extends BaseAdapter {
    private int resourceId;
    private Context context;
    private ArrayList<ActivityMessage> list;

    public MessageAdapter(Context context, ArrayList<ActivityMessage> list, int resourceId) {
        this.context = context;
        this.list = list;
        this.resourceId = resourceId;
    }

    public ArrayList<ActivityMessage> getList() {
        return list;
    }

    public void setList(ArrayList<ActivityMessage> list) {
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
            viewHolder.msgContent = (TextView) convertView.findViewById(R.id.msg_content);
            viewHolder.createTime = (TextView) convertView.findViewById(R.id.msg_create_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ActivityMessage msg = list.get(position);
        viewHolder.msgContent.setText(msg.getMsgContent());
        viewHolder.createTime.setText(msg.getCreateTime());
        return convertView;
    }

    class ViewHolder {
        TextView msgContent;
        TextView createTime;
    }
}
