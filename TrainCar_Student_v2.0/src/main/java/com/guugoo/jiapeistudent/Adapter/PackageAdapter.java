package com.guugoo.jiapeistudent.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.guugoo.jiapeistudent.Data.Package;
import com.guugoo.jiapeistudent.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/8/7.
 */
public class PackageAdapter extends BaseAdapter {
    private int resourceId;
    private Context context;
    List<Package> list;

    // 用于记录每个RadioButton的状态，并保证只可选一个
    HashMap<String, Boolean> states = new HashMap<String, Boolean>();
    private int mCheckedPosition  = 0;

    public PackageAdapter (Context context, int resource, List<Package> objects) {

        this.context=context;
        resourceId=resource;
        list=objects;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public Package IsClichedPosition(){
        return list.get(mCheckedPosition);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = View.inflate(context, resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.PackageName= (TextView) convertView.findViewById(R.id.package_text1);
            viewHolder.Introduction = (TextView) convertView.findViewById(R.id.package_text2);
            viewHolder.Price = (TextView) convertView.findViewById(R.id.package_text3);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Package data = list.get(position);
        final RadioButton radio=(RadioButton) convertView.findViewById(R.id.I_state);
        viewHolder.Rb_state = radio;

        viewHolder.PackageName.setText(data.getPackageName()+":");
        viewHolder.Introduction.setText(data.getIntroduction());
        viewHolder.Price.setText(String.format(context.getString(R.string.pay_money),
                Float.valueOf(data.getPrice())));

        viewHolder.Rb_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCheckedPosition = position;
                notifyDataSetChanged();
            }
        });
        viewHolder.Rb_state.setChecked(position==mCheckedPosition);

        return convertView;
    }
    class ViewHolder {
        public TextView  Price;
        public TextView PackageName;
        public TextView Introduction;
        public RadioButton Rb_state;
    }

}
