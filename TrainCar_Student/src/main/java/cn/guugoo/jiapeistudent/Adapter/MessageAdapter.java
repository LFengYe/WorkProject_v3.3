package cn.guugoo.jiapeistudent.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.guugoo.jiapeistudent.Data.Message;
import cn.guugoo.jiapeistudent.R;

import java.util.List;

/**
 * Created by Administrator on 2016/8/14.
 */
public class MessageAdapter extends BaseAdapter {
    private static final String TAG = "MessageAdapter";

    private int resourceId;
    private List<Message> mNotices;
    private Context mContext;

    public MessageAdapter(List<Message> mNotices, Context mContext, int resource) {
        this.mNotices = mNotices;
        this.mContext = mContext;
        resourceId=resource;
    }

    @Override
    public int getCount() {
        return mNotices.size();
    }

    @Override
    public Object getItem(int position) {
        return mNotices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.content = (TextView) convertView.findViewById(R.id.message_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.content.setText("【通知】"+mNotices.get(position).getMessageContent());
        return convertView;
    }


    private final class ViewHolder {
        TextView content;
    }
}
