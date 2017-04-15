package com.DLPort.myadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.mydata.PickUpCashRecord;

import java.util.ArrayList;

/**
 * Created by fuyzh on 16/6/17.
 */
public class PickUpRecordAdapter extends BaseAdapter {
    private int resourceId;
    private Context context;
    private ArrayList<PickUpCashRecord> list;

    public ArrayList<PickUpCashRecord> getList() {
        return list;
    }

    public void setList(ArrayList<PickUpCashRecord> list) {
        this.list = list;
    }

    public PickUpRecordAdapter(Context context, ArrayList<PickUpCashRecord> list, int resourceId) {
        this.context = context;
        this.list = list;
        this.resourceId = resourceId;
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
            viewHolder.amount = (TextView) convertView.findViewById(R.id.pickup_record_amount);
            viewHolder.time = (TextView) convertView.findViewById(R.id.pickup_record_time);
            viewHolder.bankName = (TextView) convertView.findViewById(R.id.pickup_record_bank_name);
            viewHolder.cardNum = (TextView) convertView.findViewById(R.id.pickup_record_card_num);
            viewHolder.name = (TextView) convertView.findViewById(R.id.pickup_record_name);
            viewHolder.tel = (TextView) convertView.findViewById(R.id.pickup_record_tel);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PickUpCashRecord record = list.get(position);
        viewHolder.amount.setText(String.format(context.getString(R.string.pickup_record_amount), record.getPickUpAmount()));
        viewHolder.time.setText(String.format(context.getString(R.string.pickup_record_time), record.getCreateTime()));
        viewHolder.bankName.setText(String.format(context.getString(R.string.pickup_record_bank_name), record.getBankName()));
        viewHolder.cardNum.setText(String.format(context.getString(R.string.pickup_record_card_num), record.getCardNumber()));
        viewHolder.name.setText(String.format(context.getString(R.string.pickup_record_name), record.getName()));
        viewHolder.tel.setText(String.format(context.getString(R.string.pickup_record_tel), record.getTel()));

        return convertView;
    }

    class ViewHolder {
        private TextView amount;
        private TextView time;
        private TextView bankName;
        private TextView cardNum;
        private TextView name;
        private TextView tel;
    }
}
