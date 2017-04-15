package cn.guugoo.jiapeiteacher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.guugoo.jiapeiteacher.R;
import cn.guugoo.jiapeiteacher.bean.EvaluateInfo;
import cn.guugoo.jiapeiteacher.view.CircleImageView;

import java.util.ArrayList;

/**
 * Created by gpw on 2016/8/3.
 * --加油
 */

public class EvaluateAdapter extends RecyclerView.Adapter<EvaluateAdapter.ViewHolder> {
    private ArrayList<EvaluateInfo> mEvaluateInfos;
    private Context mContext;


    public EvaluateAdapter(ArrayList<EvaluateInfo> mEvaluateInfos, Context context) {
        this.mEvaluateInfos = mEvaluateInfos;
        this.mContext=context;

    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_evaluation_item,viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        EvaluateInfo evaluateInfo = mEvaluateInfos.get(position);

        viewHolder.tv_time.setText(evaluateInfo.getCreateTime());
        viewHolder.tv_content.setText(evaluateInfo.getComment());
        viewHolder.tv_nickname.setText(evaluateInfo.getStudentName());
        Glide.with(mContext)
                .load(evaluateInfo.getStudentHeadImg())
                .crossFade()
                .skipMemoryCache(false)
                .into(viewHolder.civ_head);



    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return mEvaluateInfos.size();
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_time;
        public TextView tv_content;
        public CircleImageView civ_head;
        public TextView tv_nickname;

        public ViewHolder(View view) {
            super(view);
            tv_time = (TextView) view.findViewById(R.id.tv_createTime);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            civ_head = (CircleImageView) view.findViewById(R.id.civ_head);
            tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);

        }
    }


}
