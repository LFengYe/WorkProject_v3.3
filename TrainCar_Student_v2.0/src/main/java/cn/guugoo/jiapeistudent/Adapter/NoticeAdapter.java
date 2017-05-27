package cn.guugoo.jiapeistudent.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.guugoo.jiapeistudent.Data.Notice;
import cn.guugoo.jiapeistudent.R;

import java.util.List;

/**
 * Created by gpw on 2016/8/7.
 * --加油
 */
public class NoticeAdapter extends BaseAdapter {
    private int resourceId;
    private List<Notice> mNotices;
    private Context mContext;

    public NoticeAdapter(List<Notice> mNotices, Context mContext, int resource) {
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
            viewHolder.notice_title = (TextView) convertView.findViewById(R.id.notice_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.notice_title.setText("【通知】"+mNotices.get(position).getTitle());
        return convertView;
    }



    private final class ViewHolder {
        TextView notice_title;
    }
}
