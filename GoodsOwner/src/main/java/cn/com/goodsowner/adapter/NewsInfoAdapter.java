package cn.com.goodsowner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.com.goodsowner.bean.NewsInfo;

/**
 * Created by gpw on 2016/11/5.
 * --加油
 */

public class NewsInfoAdapter extends RecyclerView.Adapter<NewsInfoAdapter.ViewHolder> {

    private ArrayList<NewsInfo> newsInfos;



    public NewsInfoAdapter(ArrayList<NewsInfo> newsInfos) {
        super();
        this.newsInfos = newsInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(cn.com.goodsowner.R.layout.item_my_news, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final NewsInfo newsInfo = newsInfos.get(position);
        viewHolder.tv_news_content.setText(newsInfo.getMsgContent());
        viewHolder.tv_news_time.setText(newsInfo.getCreateTime());

//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return newsInfos.size();
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_news_content;
        TextView tv_news_time;


        public ViewHolder(View view) {
            super(view);
            tv_news_content = (TextView) view.findViewById(cn.com.goodsowner.R.id.tv_news_content);
            tv_news_time = (TextView) view.findViewById(cn.com.goodsowner.R.id.tv_news_time);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
