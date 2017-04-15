package com.DLPort.myadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.mydata.HuoOrder;
import com.DLPort.mydata.TwoCar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
public class TwocarAdapter extends BaseAdapter{
    private int resourceId;
    private Context context;
    List<TwoCar> orders;

    public TwocarAdapter(Context context, int resource,List<TwoCar> objects) {

        this.context=context;
        resourceId=resource;
        orders=objects;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.CarBrand = (TextView) convertView.findViewById(R.id.two_CarBrand);
            viewHolder.CarType = (TextView) convertView.findViewById(R.id.two_CarType);
            viewHolder.Price = (TextView) convertView.findViewById(R.id.two_Price);
            viewHolder.Telephone = (TextView) convertView.findViewById(R.id.two_Telephone);
            viewHolder.CarImage = (ImageView) convertView.findViewById(R.id.two_image);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.CarBrand.setText(orders.get(position).getCarBrand());
        viewHolder.CarType.setText(orders.get(position).getCarType());
        viewHolder.Price.setText(orders.get(position).getPrice());
        viewHolder.Telephone.setText(orders.get(position).getTelephone());
        ImageLoader.getInstance().displayImage(orders.get(position).getCarImage(),viewHolder.CarImage);
        return convertView;
    }


    class ViewHolder{
        public ImageView CarImage;
        public TextView CarBrand;
        public TextView CarType;
        public TextView Price;
        public TextView Telephone;
    }



}
