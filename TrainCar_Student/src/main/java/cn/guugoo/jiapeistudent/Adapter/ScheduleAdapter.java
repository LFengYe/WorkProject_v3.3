package cn.guugoo.jiapeistudent.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.guugoo.jiapeistudent.Data.Schedule;
import cn.guugoo.jiapeistudent.R;

import java.util.List;

/**
 * Created by Administrator on 2016/8/13.
 */
public class ScheduleAdapter extends BaseAdapter{
    private static final String TAG = "ScheduleAdapter";
    private int resourceId;
    private Context context;
    private List<Schedule> list;

    public ScheduleAdapter(Context context,int resourceId,  List<Schedule> list) {
        this.resourceId = resourceId;
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private int getNumber(List<Schedule> list){
        boolean New= true;
        Log.d(TAG, "getNumber: "+list.size());
        for (int i =0 ; i<list.size();i++){
            Log.d(TAG, "getNumber: "+list.get(i).isComplete());
            Log.d(TAG, "getNumber: "+list.get(i).getScheduleName());
            if(New!=list.get(i).isComplete()){
                Log.d(TAG, "getNumber: "+i);
                if(i==0){
                    return 0;
                }else {
                    Log.d(TAG, "getNumber: "+i);
                    return i-1;
                }
            }
            New = list.get(i).isComplete();
        }

        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = View.inflate(context, resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.schedule_image);
            viewHolder.Name = (TextView) convertView.findViewById(R.id.schedule_name);
            viewHolder.Day = (TextView) convertView.findViewById(R.id.schedule_time);
            viewHolder.view = convertView.findViewById(R.id.schedule_line);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Schedule data = list.get(position);
        Log.d(TAG, "getView: "+data.getCreateTime());
//        if(!TextUtils.isEmpty(data.getCreateTime())){
//            Log.d(TAG, "getView: "+data.getCreateTime());
//            String[] s = data.getCreateTime().split(" ");
//            Log.d(TAG, "getView: "+s[0]);
//            Log.d(TAG, "getView: "+s[1]);
//            String[] time = s[0].split("/");
//            Log.d(TAG, "getView: "+time[1]);
//            viewHolder.Day.setText(time[0]+"年"+time[1]+"月"+time[2]+"日");
//        }
        viewHolder.Day.setText(data.getCreateTime());
        viewHolder.Name.setText(data.getScheduleName());
        if (position==getNumber(list)){
                viewHolder.image.setImageResource(R.mipmap.couse_new);
                viewHolder.Name.setTextColor(ContextCompat.getColor(context,R.color.login_color));
                viewHolder.Day.setTextColor(ContextCompat.getColor(context,R.color.login_color));
        }else {
            if(data.isComplete()){
                viewHolder.image.setImageResource(R.mipmap.couse_old);
                viewHolder.Name.setTextColor(ContextCompat.getColor(context,R.color.login_color));
                viewHolder.Day.setTextColor(ContextCompat.getColor(context,R.color.login_color));
            }
        }
        if(position==list.size()-1){
            viewHolder.view.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        private ImageView image;
        private TextView Name;
        private TextView Day;
        private View view;
    }
}
