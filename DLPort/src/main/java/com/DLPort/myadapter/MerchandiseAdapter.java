package com.DLPort.myadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.mydata.Merchandise;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by fuyzh on 16/5/18.
 */
public class MerchandiseAdapter extends BaseAdapter {
    private int resourceId;
    private Context context;
    private ArrayList<Merchandise> list;

    public ArrayList<Merchandise> getList() {
        return list;
    }

    public void setList(ArrayList<Merchandise> list) {
        this.list = list;
    }

    public MerchandiseAdapter(int resourceId, Context context) {
        this.resourceId = resourceId;
        this.context = context;
    }

    public MerchandiseAdapter(int resourceId, Context context, ArrayList<Merchandise> list) {
        this.resourceId = resourceId;
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (null != list)
            return list.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = View.inflate(context, resourceId, null);
            viewHolder = new ViewHolder();

            viewHolder.merchandiseName = (TextView) convertView.findViewById(R.id.merchandise_name);
            viewHolder.merchandiseDescribe = (TextView) convertView.findViewById(R.id.merchandise_describe);
            viewHolder.merchandisePrice = (TextView) convertView.findViewById(R.id.merchandise_price);
            viewHolder.merchandiseImage = (ImageView) convertView.findViewById(R.id.merchandise_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Merchandise merchandise = list.get(position);
        viewHolder.merchandiseName.setText(merchandise.getMerchandiseName());
        viewHolder.merchandiseDescribe.setText(merchandise.getMerchandiseDescribe());
        viewHolder.merchandisePrice.setText(String.valueOf(merchandise.getMerchandisePrice()));

        ImageLoader.getInstance().displayImage(merchandise.getMerchandiseImage(), viewHolder.merchandiseImage);

        return convertView;
    }

    class ViewHolder {
        TextView merchandiseName;
        TextView merchandiseDescribe;
        TextView merchandisePrice;
        ImageView merchandiseImage;
    }
}
