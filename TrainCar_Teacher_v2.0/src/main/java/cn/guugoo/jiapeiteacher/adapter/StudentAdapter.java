package cn.guugoo.jiapeiteacher.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.guugoo.jiapeiteacher.R;
import cn.guugoo.jiapeiteacher.bean.Booking;
import cn.guugoo.jiapeiteacher.bean.ReservationStudent;

/**
 * Created by LFeng on 2017/5/31.
 */

public class StudentAdapter extends BaseAdapter {

    private List<ReservationStudent> students;
    private Context context;
    private int selectedIndex;

    public StudentAdapter(List<ReservationStudent> students, Context context) {
        this.students = students;
        this.context = context;
        this.selectedIndex = -1;
    }

    @Override
    public int getCount() {
        if (students != null)
            return students.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (students != null)
            return students.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.layout_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.student_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ReservationStudent student = students.get(position);
        viewHolder.textView.setText(student.getName());
        if (selectedIndex > -1 && position == selectedIndex)
            viewHolder.textView.setTextColor(context.getResources().getColor(R.color.color_Blue));
        else
            viewHolder.textView.setTextColor(context.getResources().getColor(R.color.color_Black));
        return convertView;
    }

    class ViewHolder{
        public TextView textView;
    }

    public interface OnItemClickListener {
        void onItemClick(Booking booking, int position);
    }

    private StudentAdapter.OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(StudentAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
}
