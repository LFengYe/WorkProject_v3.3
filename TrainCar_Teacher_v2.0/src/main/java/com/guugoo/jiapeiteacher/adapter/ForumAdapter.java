package com.guugoo.jiapeiteacher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.guugoo.jiapeiteacher.R;
import com.guugoo.jiapeiteacher.bean.Forum;
import com.guugoo.jiapeiteacher.view.CircleImageView;

import java.util.ArrayList;

/**
 * Created by gpw on 2016/8/7.
 * --加油
 */
public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ViewHolder> {
    private ArrayList<Forum> mForums;
    private Context mContext;
    public ForumAdapter(ArrayList<Forum> mForums,Context mContext) {
        this.mForums = mForums;
        this.mContext = mContext;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.forum_item, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        Forum forum = mForums.get(position);
        viewHolder.tv_nickname.setText(forum.getNickname());
        viewHolder.tv_createTime.setText(forum.getCreateTime());
        viewHolder.tv_content.setText(forum.getContent());
        viewHolder.tv_commentNum.setText(forum.getCommentNumber() + "");
        viewHolder.tv_praiseNum.setText(forum.getZambiaNumber() + "");

        if (!forum.getIsZambia()) {
            viewHolder.iv_praise.setImageResource(R.mipmap.praise);
        } else {
            viewHolder.iv_praise.setImageResource(R.mipmap.praise_press);
        }

        if (mOnItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(viewHolder.itemView, pos);
                }
            });


            viewHolder.ll_praise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onPraiseBtnClick(viewHolder.ll_praise, pos);
                }
            });
        }

        Glide.with(mContext)
                .load(forum.getStudentHeadPortrait())
                .crossFade()
                .into(viewHolder.civ_head);


    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return mForums.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_nickname;
        public TextView tv_createTime;
        public TextView tv_content;
        public TextView tv_commentNum;
        public TextView tv_praiseNum;
        public LinearLayout ll_praise;
        public LinearLayout ll_comment;
        public ImageView iv_praise;
        public CircleImageView civ_head;

        public ViewHolder(View view) {
            super(view);
            tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
            tv_createTime = (TextView) view.findViewById(R.id.tv_createTime);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_commentNum = (TextView) view.findViewById(R.id.tv_commentNum);
            tv_praiseNum = (TextView) view.findViewById(R.id.tv_praiseNum);
            ll_praise = (LinearLayout) view.findViewById(R.id.ll_praise);
            ll_comment = (LinearLayout) view.findViewById(R.id.ll_comment);
            iv_praise = (ImageView) view.findViewById(R.id.iv_praise);
            civ_head = (CircleImageView) view.findViewById(R.id.civ_head);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onPraiseBtnClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}


