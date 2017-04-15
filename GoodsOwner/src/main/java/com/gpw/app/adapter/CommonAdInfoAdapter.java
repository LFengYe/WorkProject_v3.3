package com.gpw.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.gpw.app.R;
import com.gpw.app.bean.CommonAdInfo;

/**
 * Created by gpw on 2016/11/5.
 * --加油
 */

public class CommonAdInfoAdapter extends RecyclerView.Adapter<CommonAdInfoAdapter.ViewHolder> {

    private ArrayList<CommonAdInfo> commonAdInfos;

    private Context context;


    public CommonAdInfoAdapter(Context context,ArrayList<CommonAdInfo> commonAdInfos) {
        super();
        this.commonAdInfos = commonAdInfos;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_common_address, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final CommonAdInfo commonAdInfo = commonAdInfos.get(position);
        viewHolder.tv_address.setText(commonAdInfo.getReceiptAddress());
        viewHolder.tv_contact.setText(commonAdInfo.getReceipter()+" "+commonAdInfo.getReceiptTel());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(position);
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mOnItemClickListener.onItemLongClick(position);
                return true;
            }
        });
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return commonAdInfos.size();
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_address;
        TextView tv_contact;

        public ViewHolder(View view) {
            super(view);
            tv_address = (TextView) view.findViewById(R.id.tv_address);
            tv_contact = (TextView) view.findViewById(R.id.tv_contact);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }

    private OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
