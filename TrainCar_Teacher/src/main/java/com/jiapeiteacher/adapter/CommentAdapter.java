package cn.guugoo.jiapeiteacher.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.guugoo.jiapeiteacher.R;
import cn.guugoo.jiapeiteacher.bean.ForumCommentInfo;

import java.util.ArrayList;

/**
 * Created by gpw on 2016/8/7.
 * --加油
 */
public class CommentAdapter extends BaseAdapter {

    private ArrayList<ForumCommentInfo.CommentInfo> mCommentInfo;
    private Context mContext;
    private String nickname;

    public CommentAdapter(ArrayList<ForumCommentInfo.CommentInfo> mCommentInfo, Context mContext,String nickname) {
        super();
        this.mCommentInfo = mCommentInfo;
        this.mContext = mContext;
        this.nickname = nickname;
    }

    @Override
    public int getCount() {
        return mCommentInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return mCommentInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.comment_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_theReviewers = (TextView) convertView.findViewById(R.id.tv_theReviewers);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ForumCommentInfo.CommentInfo commentInfo = mCommentInfo.get(position);
        String name;
        if (!commentInfo.getReplyobject().equals(commentInfo.getTheReviewers())&&!commentInfo.getTheReviewers().equals(nickname)) {
            name = commentInfo.getTheReviewers() + " 回复 "+mCommentInfo.get(position).getReplyobject()+ ": ";
        }else {
            name = commentInfo.getTheReviewers() + ": ";
        }
        viewHolder.tv_content.setText(commentInfo.getContent());
        viewHolder.tv_theReviewers.setText(name);

        return convertView;
    }


    private final class ViewHolder {
        TextView tv_theReviewers;
        TextView tv_content;
    }
}
