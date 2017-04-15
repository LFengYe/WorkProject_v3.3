package cn.com.caronwer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.com.caronwer.R;
import cn.com.caronwer.bean.BankCardInfo;

public class BankCardInfoAdapter extends RecyclerView.Adapter<BankCardInfoAdapter.ViewHolder> {

    private ArrayList<BankCardInfo> newsInfos;



    public BankCardInfoAdapter(ArrayList<BankCardInfo> newsInfos) {
        super();
        this.newsInfos = newsInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bankcar, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final BankCardInfo newsInfo = newsInfos.get(position);

//        viewHolder.tv_bankUserTel.setText(newsInfo.get);
        viewHolder.tv_bankCarName.setText(newsInfo.getAccountName());
        viewHolder.tv_bankZhiName.setText(newsInfo.getBankBranch());
        viewHolder.tv_bankNameNo.setText(newsInfo.getAccountNumber());


    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return newsInfos.size();
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_bankUserTel;
        TextView tv_bankCarName;
        TextView tv_bankZhiName;
        TextView tv_bankNameNo;


        public ViewHolder(View view) {
            super(view);
            tv_bankUserTel = (TextView) view.findViewById(R.id.tv_bankUserTel);
            tv_bankCarName = (TextView) view.findViewById(R.id.tv_bankCarName);
            tv_bankZhiName = (TextView) view.findViewById(R.id.tv_bankZhiName);
            tv_bankNameNo = (TextView) view.findViewById(R.id.tv_bankNameNo);

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
