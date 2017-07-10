package cn.com.goodsowner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import cn.com.goodsowner.bean.InvoiceInfo;

/**
 * Created by gpw on 2016/11/5.
 * --加油
 */

public class InvoiceInfoAdapter extends RecyclerView.Adapter<InvoiceInfoAdapter.ViewHolder> {

    private ArrayList<InvoiceInfo> invoiceInfos;

    private Context context;


    public InvoiceInfoAdapter(Context context, ArrayList<InvoiceInfo> invoiceInfos) {
        super();
        this.invoiceInfos = invoiceInfos;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(cn.com.goodsowner.R.layout.item_my_invoice, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final InvoiceInfo invoiceInfo = invoiceInfos.get(position);
        viewHolder.tv_money.setText(String.format("¥%s", invoiceInfo.getOrderAmount()));
        viewHolder.tv_arrive.setText(String.format("到   %s", invoiceInfo.getEndAddress()));
        viewHolder.tv_start.setText(String.format("从   %s", invoiceInfo.getStartAddress()));
        viewHolder.tv_time.setText(invoiceInfo.getCreateTime());
        viewHolder.cb_order.setText(String.format("订单号:%s", invoiceInfo.getOrderNo()));
        if (invoiceInfo.ischeck()) {
            viewHolder.cb_order.setChecked(true);
        } else {
            viewHolder.cb_order.setChecked(false);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (invoiceInfo.ischeck()) {
                    invoiceInfo.setIscheck(false);
                    viewHolder.cb_order.setChecked(false);
                } else {
                    invoiceInfo.setIscheck(true);
                    viewHolder.cb_order.setChecked(true);
                }
            }
        });

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return invoiceInfos.size();
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cb_order;
        TextView tv_money;
        TextView tv_time;
        TextView tv_start;
        TextView tv_arrive;

        public ViewHolder(View view) {
            super(view);
            cb_order = (CheckBox) view.findViewById(cn.com.goodsowner.R.id.cb_order);
            tv_money = (TextView) view.findViewById(cn.com.goodsowner.R.id.tv_money);
            tv_time = (TextView) view.findViewById(cn.com.goodsowner.R.id.tv_time);
            tv_start = (TextView) view.findViewById(cn.com.goodsowner.R.id.tv_start);
            tv_arrive = (TextView) view.findViewById(cn.com.goodsowner.R.id.tv_arrive);

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
