package cn.guugoo.jiapeiteacher.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.guugoo.jiapeiteacher.R;
import cn.guugoo.jiapeiteacher.bean.StatuteInfo;

import java.util.ArrayList;

/**
 * Created by gpw on 2016/8/3.
 * --加油
 */

public class StatuteAdapter extends RecyclerView.Adapter<StatuteAdapter.ViewHolder> {
    private ArrayList<StatuteInfo> mStatuteInfos;


    public StatuteAdapter(ArrayList<StatuteInfo> mStatuteInfos) {
        this.mStatuteInfos = mStatuteInfos;

    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.statute_item, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        StatuteInfo statuteInfo = mStatuteInfos.get(position);
        viewHolder.tv_title.setText(statuteInfo.getTitle());

        if (mOnItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(viewHolder.itemView, pos);
                }
            });

        }

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return mStatuteInfos.size();
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title;

        public ViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

}
