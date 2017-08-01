package com.guugoo.jiapeiteacher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.guugoo.jiapeiteacher.R;
import com.guugoo.jiapeiteacher.bean.Reservation;
import com.guugoo.jiapeiteacher.bean.ReservationStudent;
import com.guugoo.jiapeiteacher.util.Utils;
import com.guugoo.jiapeiteacher.view.CircleImageView;

/**
 * Created by gpw on 2016/8/3.
 * --加油
 */
public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {
    private ArrayList<Reservation> mReservations;
    private Context mContext;
    private commentClickListener mCommentClickListener;

    public ReservationAdapter(ArrayList<Reservation> mReservations, Context mContext) {
        this.mReservations = mReservations;
        this.mContext = mContext;

    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reservation_msg_item, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final Reservation reservation = mReservations.get(position);

        String time = reservation.getBookingTime();
        //String hour = time.substring(time.indexOf(" ") + 1, time.length());
        //String year = time.substring(0, time.indexOf(" ") + 1);
        String status = getStringStatus(reservation.getStatus());
        //final String bookingId = reservation.getBookingId();

        String name = "";
        boolean isAllIsComment = true;
        boolean isDisplayComment = false;
        boolean isDisplayLogout = false;
        ArrayList<ReservationStudent> students = reservation.getStudentList();
        for (ReservationStudent student : students) {
            if (TextUtils.isEmpty(name)) {
                name += student.getName();
            } else {
                name += "\n" + student.getName();
            }
            if (student.getIsComment() == 0 && student.getStatus() == 2 && isAllIsComment)
                isAllIsComment = false;
            if (student.getStatus() == 2 && !isDisplayComment) {
                isDisplayComment = true;
            }
            if (student.getStatus() == 2 && !isDisplayLogout) {
                isDisplayLogout = true;
            }
        }

        if (reservation.getStatus() == 2) {
            if (isDisplayComment) {
                viewHolder.tv_status.setVisibility(View.GONE);
                viewHolder.ll_status.setVisibility(View.VISIBLE);
                viewHolder.tv_status1.setText(status);
            } else {
                viewHolder.tv_status.setVisibility(View.VISIBLE);
                viewHolder.ll_status.setVisibility(View.GONE);
                viewHolder.tv_status.setText(status);
            }
            if (isDisplayLogout) {
                viewHolder.tv_studentLogout.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tv_studentLogout.setVisibility(View.GONE);
            }
            if (isAllIsComment) {
                viewHolder.tv_click_comment.setText(R.string.has_comment);
            } else {
                viewHolder.tv_click_comment.setText(R.string.comment);
            }

            viewHolder.tv_click_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCommentClickListener.onClick(0, v, reservation);
                }
            });
            viewHolder.tv_studentLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCommentClickListener.onClick(1, v, reservation);
                }
            });
        } else {
            viewHolder.tv_status.setVisibility(View.VISIBLE);
            viewHolder.ll_status.setVisibility(View.GONE);
            viewHolder.tv_status.setText(status);

            viewHolder.tv_studentLogout.setVisibility(View.GONE);
        }

        viewHolder.tv_name.setText(name);
        viewHolder.tv_year.setText(time);
        //viewHolder.tv_hour.setText(hour);
        viewHolder.tv_driverType.setText(reservation.getDriveType());
        viewHolder.tv_bookingAccount.setText(getStringBookingAccount(reservation.getBookingAccount()));

        /*
        Glide.with(mContext)
                .load(reservation.getHeadImg())
                .crossFade()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(viewHolder.civ_head);
        */
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


    private String getStringStatus(int status) {
        String state = null;
        switch (status) {
            case 1:
                state = mContext.getResources().getString(R.string.has_reservation);
                break;
            case 2:
                state = mContext.getResources().getString(R.string.has_ended);
                break;
            case 3:
                state = mContext.getResources().getString(R.string.has_cancel);
                break;
        }
        return state;
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return mReservations.size();
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_year;
        public TextView tv_hour;
        public TextView tv_amount;
        public TextView tv_name;
        public TextView tv_driverType;
        public TextView tv_bookingAccount;
        public TextView tv_status;
        public TextView tv_status1;
        public TextView tv_studentLogout;
        public TextView tv_click_comment;
        public LinearLayout ll_status;

        public CircleImageView civ_head;

        public ViewHolder(View view) {
            super(view);
            tv_year = (TextView) view.findViewById(R.id.tv_year);
            tv_hour = (TextView) view.findViewById(R.id.tv_hour);
            tv_amount = (TextView) view.findViewById(R.id.tv_amount);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_driverType = (TextView) view.findViewById(R.id.tv_driverType);
            tv_bookingAccount = (TextView) view.findViewById(R.id.tv_bookingAccount);
            tv_status = (TextView) view.findViewById(R.id.tv_status);
            tv_status1 = (TextView) view.findViewById(R.id.tv_status1);
            tv_studentLogout = (TextView) view.findViewById(R.id.student_logout);
            tv_click_comment = (TextView) view.findViewById(R.id.tv_click_comment);
            civ_head = (CircleImageView) view.findViewById(R.id.civ_head);
            ll_status = (LinearLayout) view.findViewById(R.id.ll_status);
        }
    }

    public interface commentClickListener {
        void onClick(int type, View view, Reservation reservation);
    }


    public void setCommentClickListener(commentClickListener commentClickListener) {
        this.mCommentClickListener = commentClickListener;
    }

}
