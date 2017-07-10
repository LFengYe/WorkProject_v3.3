package com.guugoo.jiapeiteacher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.guugoo.jiapeiteacher.R;
import com.guugoo.jiapeiteacher.bean.RecommendInfo;
import com.guugoo.jiapeiteacher.view.CircleImageView;

import java.util.ArrayList;

/**
 * Created by gpw on 2016/8/8.
 * --加油
 */
public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> {
    private ArrayList<RecommendInfo> mRecommendInfos;
    private Context mContext;


    public RecommendAdapter(ArrayList<RecommendInfo> mRecommendInfos, Context mContext) {
        this.mRecommendInfos = mRecommendInfos;
        this.mContext = mContext;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recommend_msg_item, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        RecommendInfo recommendInfo = mRecommendInfos.get(position);
        String reward = getReward(recommendInfo.isInvitePeopleIsCash(),recommendInfo.getInvitePeopleRewarType());
        viewHolder.tv_name.setText(recommendInfo.getNmae());
        viewHolder.tv_tel.setText(recommendInfo.getTel());
        viewHolder.tv_type.setText(recommendInfo.getType());
        viewHolder.tv_reward.setText(reward);
        viewHolder.tv_amount.setText("数量:"+recommendInfo.getInvitePeopleBonusAmount());
        Glide.with(mContext)
                .load(recommendInfo.getHeadPortrait())
                .crossFade()
                .skipMemoryCache(false)
                .into(viewHolder.civ_head);




    }
    private String getReward(boolean isReward,int type){
        String reward;
        if (isReward){
            reward = "已奖励";
        }else {
            reward = "未奖励";
        }
        if (type==1){
            reward+="现金";
        }else {
            reward+="学时券";
        }
      return reward;
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return mRecommendInfos.size();
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public TextView tv_tel;
        public TextView tv_type;
        public TextView tv_reward;
        public TextView tv_amount;
        public CircleImageView civ_head;

        public ViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_tel = (TextView) view.findViewById(R.id.tv_tel);
            tv_type = (TextView) view.findViewById(R.id.tv_type);
            civ_head = (CircleImageView) view.findViewById(R.id.civ_head);
            tv_reward = (TextView) view.findViewById(R.id.tv_reward);
            tv_amount = (TextView) view.findViewById(R.id.tv_amount);

        }
    }


}

