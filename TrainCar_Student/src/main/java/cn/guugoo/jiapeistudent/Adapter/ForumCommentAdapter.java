package cn.guugoo.jiapeistudent.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.guugoo.jiapeistudent.Data.ForumComment;
import cn.guugoo.jiapeistudent.R;

import java.util.List;

/**
 * Created by Administrator on 2016/8/12.
 */
public class ForumCommentAdapter extends BaseAdapter {
    private static final String TAG = "ForumCommentAdapter";
    private int resourceId;
    private Context context;
    List<ForumComment> list;



    public ForumCommentAdapter (Context context, int resource, List<ForumComment> objects) {
        this.context=context;
        resourceId=resource;
        list=objects;
    }

    @Override
    public int getCount() {
        return list.size();
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
            viewHolder.name = (TextView) convertView.findViewById(R.id.comment_text1);
            viewHolder.content = (TextView) convertView.findViewById(R.id.comment_text2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ForumComment data = list.get(position);
        viewHolder.name.setText(data.getTheReviewers()+"：");
        viewHolder.content.setText(data.getContent());
        return convertView;
    }
    class ViewHolder {
        public TextView name;
        public TextView content;
    }

}
