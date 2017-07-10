package cn.com.goodsowner.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.com.goodsowner.bean.BalanceInfo;

/**
 * Created by gpw on 2016/11/5.
 * --加油
 */

public class ChargeScheduleAdapter extends RecyclerView.Adapter<ChargeScheduleAdapter.ViewHolder> {

    private ArrayList<BalanceInfo.ListBean> listBeen;


    public ChargeScheduleAdapter(ArrayList<BalanceInfo.ListBean> listBeen) {
        super();
        this.listBeen = listBeen;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(cn.com.goodsowner.R.layout.item_charge_schedule, viewGroup, false);
        return new ViewHolder(view);
    }


    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final BalanceInfo.ListBean listBean = listBeen.get(position);
        viewHolder.tv_time.setText(listBean.getTime());
        viewHolder.tv_pay_type.setText(listBean.getWay());
        viewHolder.tv_orderId.setText(String.format("订单号:%s", listBean.getSerialNO()));
        String money = null;
        if (listBean.getType() == 1) {
            money = "+ ¥ ";
        } else {
            money = "- ¥ ";
        }
        money = money + String.valueOf(listBean.getAmount());

        viewHolder.tv_money.setText(money);
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return listBeen.size();
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_time;
        TextView tv_pay_type;
        TextView tv_money;
        TextView tv_orderId;


        public ViewHolder(View view) {
            super(view);
            tv_time = (TextView) view.findViewById(cn.com.goodsowner.R.id.tv_time);
            tv_pay_type = (TextView) view.findViewById(cn.com.goodsowner.R.id.tv_pay_type);
            tv_money = (TextView) view.findViewById(cn.com.goodsowner.R.id.tv_money);
            tv_orderId = (TextView) view.findViewById(cn.com.goodsowner.R.id.tv_orderId);
        }
    }

}
