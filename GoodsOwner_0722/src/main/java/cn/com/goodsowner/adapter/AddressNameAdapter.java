package cn.com.goodsowner.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.sug.SuggestionResult;

import java.util.ArrayList;

/**
 * Created by gpw on 2016/11/6.
 * --加油
 */

public class AddressNameAdapter extends BaseAdapter {
    private ArrayList<SuggestionResult.SuggestionInfo> mSuggestionInfos;
    private Context mContext;

    public AddressNameAdapter(ArrayList<SuggestionResult.SuggestionInfo> mSuggestionInfos, Context mContext) {
        super();
        this.mSuggestionInfos = mSuggestionInfos;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mSuggestionInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mSuggestionInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, cn.com.goodsowner.R.layout.item_address_name, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_address = (TextView) convertView.findViewById(cn.com.goodsowner.R.id.tv_address);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_address.setText(mSuggestionInfos.get(position).key);

        return convertView;
    }


    private final class ViewHolder {
        TextView tv_address;
    }
}
