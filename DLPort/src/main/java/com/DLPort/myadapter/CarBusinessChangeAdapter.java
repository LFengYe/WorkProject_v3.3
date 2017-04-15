package com.DLPort.myadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.mydata.Inform;

import java.util.ArrayList;

/**
 * Created by fuyzh on 16/5/20.
 */
public class CarBusinessChangeAdapter extends BaseAdapter {
    private int resourceId;
    private Context context;
    private ArrayList<Inform> list;

    public CarBusinessChangeAdapter(Context context, ArrayList<Inform> list, int resourceId) {
        this.context = context;
        this.list = list;
        this.resourceId = resourceId;
    }

    public CarBusinessChangeAdapter(Context context, ArrayList<Inform> list) {

        this.context = context;
        this.list = list;
    }

    public ArrayList<Inform> getList() {
        return list;
    }

    public void setList(ArrayList<Inform> list) {
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
            convertView = View.inflate(context,resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.msgContent = (TextView) convertView.findViewById(R.id.inform_content);
            viewHolder.carNumber = (TextView) convertView.findViewById(R.id.inform_car_number);
            viewHolder.carOwnerName = (TextView) convertView.findViewById(R.id.inform_car_owner_name);
            viewHolder.carOwnerTel = (TextView) convertView.findViewById(R.id.inform_car_owner_tel);
            viewHolder.presentNo = (TextView) convertView.findViewById(R.id.inform_present_no);
            viewHolder.carGoName = (TextView) convertView.findViewById(R.id.inform_car_go_name);
            viewHolder.carGoTel = (TextView) convertView.findViewById(R.id.inform_car_go_tel);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Inform inform = list.get(position);
        viewHolder.msgContent.setText(inform.getMsgContent());
        viewHolder.carNumber.setText(inform.getVehNof());
        viewHolder.carOwnerName.setText(inform.getCarOwnerName());
        viewHolder.carOwnerTel.setText(inform.getCarOwnerTel());
        viewHolder.presentNo.setText(context.getResources().getString(R.string.present_no) + inform.getPresentNo());
        viewHolder.carGoName.setText(inform.getCarGoOwnerName());
        viewHolder.carGoTel.setText(inform.getCarGoOwnerTel());

        return convertView;
    }

    class ViewHolder {
        private TextView msgContent;
        private TextView carNumber;
        private TextView carOwnerName;
        private TextView carOwnerTel;
        private TextView presentNo;
        private TextView carGoName;
        private TextView carGoTel;
    }
}
