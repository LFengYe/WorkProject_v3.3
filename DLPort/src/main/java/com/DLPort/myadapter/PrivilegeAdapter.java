package com.DLPort.myadapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.mydata.Privilege;

import java.util.ArrayList;

/**
 * Created by fuyzh on 16/5/19.
 */
public class PrivilegeAdapter extends BaseAdapter {
    private int resourceId;
    private Context context;
    private ArrayList<Privilege> list;

    public ArrayList<Privilege> getList() {
        return list;
    }

    public void setList(ArrayList<Privilege> list) {
        this.list = list;
    }

    public PrivilegeAdapter(int resourceId, Context context) {
        this.resourceId = resourceId;
        this.context = context;
    }

    public PrivilegeAdapter(int resourceId, Context context, ArrayList<Privilege> list) {
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
            LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.privilege_linear);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.privilege_amount);
            viewHolder.describe = (TextView) convertView.findViewById(R.id.privilege_describe);
            viewHolder.deadline = (TextView) convertView.findViewById(R.id.privilege_deadline);

            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            manager.getDefaultDisplay().getMetrics(metrics);
            RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) layout.getLayoutParams();
            linearParams.height = 171 * metrics.widthPixels / 615;
            layout.setLayoutParams(linearParams);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Privilege privilege = list.get(position);
        viewHolder.amount.setText(String.valueOf(privilege.getAmount()) + context.getResources().getString(R.string.Yuan));
        viewHolder.describe.setText(privilege.getDescribe());
        viewHolder.deadline.setText(privilege.getDeadline());

        return convertView;
    }

    class ViewHolder {
        private TextView amount;
        private TextView describe;
        private TextView deadline;
    }
}
