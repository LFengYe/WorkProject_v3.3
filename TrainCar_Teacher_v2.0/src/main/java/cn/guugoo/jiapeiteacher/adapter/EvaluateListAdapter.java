package cn.guugoo.jiapeiteacher.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.guugoo.jiapeiteacher.R;
import cn.guugoo.jiapeiteacher.bean.StudentDetails;

import java.util.ArrayList;

/**
 * Created by gpw on 2016/8/14.
 * --加油
 */
public class EvaluateListAdapter extends BaseAdapter {

    private ArrayList<StudentDetails.EvaluateListBean> mEvaluateListBeens;
    private Context mContext;

    public EvaluateListAdapter(ArrayList<StudentDetails.EvaluateListBean> mEvaluateListBeens, Context mContext) {
        super();
        this.mEvaluateListBeens = mEvaluateListBeens;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mEvaluateListBeens.size();
    }

    @Override
    public Object getItem(int position) {
        return mEvaluateListBeens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.evalute_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_subject = (TextView) convertView.findViewById(R.id.tv_subject);
            viewHolder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
            viewHolder.tv_subjectItem = (TextView) convertView.findViewById(R.id.tv_subjectItem);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_time.setText(mEvaluateListBeens.get(position).getLianjuTime());
        viewHolder.tv_subject.setText(getStringBookingAccount(mEvaluateListBeens.get(position).getSubject()));
        String SubjectItem = mEvaluateListBeens.get(position).getSubjectItem();
        String newSubjectItem = SubjectItem.replace("#","   ");
        viewHolder.tv_subjectItem.setText(newSubjectItem);
        viewHolder.tv_comment.setText(mEvaluateListBeens.get(position).getComment());

        return convertView;
    }

    private String getStringBookingAccount(String BookingAccount) {
        String state = null;
        switch (BookingAccount) {
            case "1":
                state = "科目一";
                break;
            case "2":
                state = "科目二";
                break;
            case "3":
                state = "科目三";
                break;
            case "4":
                state = "科目四";
                break;
        }
        return state;
    }

    private final class ViewHolder {
        TextView tv_time;
        TextView tv_subject;
        TextView tv_comment;
        TextView tv_subjectItem;

    }

}
