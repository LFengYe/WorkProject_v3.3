package com.DLPort.myadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.mydata.inquire;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/5/14.
 */
public class InquireAdapter extends BaseAdapter{
    private int resourceId;
    private Context context;
    List<inquire> datas;
    // 用于记录每个RadioButton的状态，并保证只可选一个
    HashMap<String, Boolean> states = new HashMap<String, Boolean>();
    private int mCheckedPosition  = 0;

    public InquireAdapter(Context context, int resource,List<inquire> objects) {
        this.context=context;
        resourceId=resource;
        datas=objects;
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }
    public inquire IsClichedPosition(){
        return datas.get(mCheckedPosition);
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, resourceId, null);
            viewHolder =new ViewHolder();
            viewHolder.ShipCompany = (TextView) convertView.findViewById(R.id.I_ShipCompany);
            viewHolder.InPortTime = (TextView) convertView.findViewById(R.id.I_InPortTime);
            viewHolder.DestinationPort = (TextView) convertView.findViewById(R.id.I_DestinationPort);
            viewHolder.ShipLine = (TextView) convertView.findViewById(R.id.I_ShipLine);
            viewHolder.ShipName = (TextView) convertView.findViewById(R.id.I_ShipName);
            viewHolder.ShipOrder = (TextView) convertView.findViewById(R.id.I_ShipOrder);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final RadioButton radio=(RadioButton) convertView.findViewById(R.id.I_state);
        viewHolder.Rb_state = radio;


        viewHolder.ShipName.setText(datas.get(position).getShipName());
        viewHolder.DestinationPort.setText(datas.get(position).getDestinationPort());
        viewHolder.InPortTime.setText(datas.get(position).getInPortTime());
        viewHolder.ShipLine.setText(datas.get(position).getShipLine());
        viewHolder.ShipOrder.setText(datas.get(position).getShipOrder());
        viewHolder.ShipCompany.setText(datas.get(position).getShipCompany());
        viewHolder.Rb_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                for (String key :states.keySet()){
//                    states.put(key,false);
//                }
//
//                states.put(String.valueOf(position), radio.isChecked());
//                InquireAdapter.this.notifyDataSetChanged();
                mCheckedPosition = position;
                notifyDataSetChanged();
            }
        });
        viewHolder.Rb_state.setChecked(position==mCheckedPosition);
//        boolean res = false;
//
//        if(states.get(String.valueOf(position)) == null || states.get(String.valueOf(position)) == false){
//            res=false;
//            states.put(String.valueOf(position), false);
//
//        } else
//            res = true;
//
//        viewHolder.Rb_state.setChecked(res);

        return convertView;
    }

    public class ViewHolder{
        public TextView ShipCompany;
        public TextView ShipLine;
        public TextView ShipName;
        public TextView ShipOrder;
        public TextView DestinationPort;
        public TextView InPortTime;
        public RadioButton Rb_state;
    }
}
