package cn.com.goodsowner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.com.goodsowner.R;
import cn.com.goodsowner.bean.GoodsInfo;
import cn.com.goodsowner.bean.NewsInfo;

/**
 * Created by gpw on 2016/11/5.
 * --加油
 */

public class AddreAdapter extends RecyclerView.Adapter<AddreAdapter.ViewHolder> {

    private ArrayList<GoodsInfo.Addre> mAddres;
    private Context mContext;


    public AddreAdapter(ArrayList<GoodsInfo.Addre> mAddres) {
        super();
        this.mAddres = mAddres;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ad, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.tv_city.setText(mAddres.get(position).getAd_city());
        viewHolder.tv_place.setText(mAddres.get(position).getAd_place());
        viewHolder.tv_tel.setText(mAddres.get(position).getAd_tel());
        viewHolder.tv_name.setText(mAddres.get(position).getAd_name());
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return mAddres.size();
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_city;
        TextView tv_place;
        TextView tv_tel;
        TextView tv_name;

        public ViewHolder(View view) {
            super(view);
            tv_city = (TextView) view.findViewById(R.id.tv_city);
            tv_place = (TextView) view.findViewById(R.id.tv_place);
            tv_tel = (TextView) view.findViewById(R.id.tv_tel);
            tv_name = (TextView) view.findViewById(R.id.tv_name);

        }
    }

}
