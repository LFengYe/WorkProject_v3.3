package com.guugoo.jiapeiteacher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.guugoo.jiapeiteacher.R;
import com.guugoo.jiapeiteacher.bean.ReservationStudent;

/**
 * Created by LFeng on 2017/5/31.
 */

public class StudentRecAdapter extends RecyclerView.Adapter<StudentRecAdapter.ViewHolder> {

    private List<ReservationStudent> students;
    private Context context;

    public StudentRecAdapter(List<ReservationStudent> students, Context context) {
        this.students = students;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ReservationStudent student = students.get(position);
        holder.textView.setText(student.getName());
        holder.textView.setTextColor(context.getResources().getColor(R.color.color_Black));

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (students != null)
            return students.size();
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private StatuteAdapter.OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(StatuteAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
