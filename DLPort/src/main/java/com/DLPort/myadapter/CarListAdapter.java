package com.DLPort.myadapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.mydata.CarInfo;

import java.util.ArrayList;

/**
 * Created by fuyzh on 16/8/5.
 */
public class CarListAdapter extends BaseAdapter {
    private int resourceId;
    private Context context;
    private Handler handler;
    private ArrayList<CarInfo> list;
    private DeleteBtnClick deleteBtnClick;

    public CarListAdapter(Context context, ArrayList<CarInfo> list, int resourceId, Handler handler, DeleteBtnClick deleteBtnClick) {
        this.context = context;
        this.list = list;
        this.handler = handler;
        this.resourceId = resourceId;
        this.deleteBtnClick = deleteBtnClick;
    }

    @Override
    public int getCount() {
        if (null != list) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != list) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = View.inflate(context, resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.carNo = (TextView) convertView.findViewById(R.id.car_number);
            viewHolder.deleteBtn = (ImageView) convertView.findViewById(R.id.car_number_arrow);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final CarInfo carInfo = list.get(position);
        viewHolder.carNo.setText(carInfo.getVehNof());
        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBtnClick.deleteClick(position);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView carNo;
        ImageView deleteBtn;
    }

    public interface DeleteBtnClick {
        void deleteClick(int position);
    }
}
