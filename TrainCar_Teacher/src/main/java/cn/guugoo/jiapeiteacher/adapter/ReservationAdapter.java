package cn.guugoo.jiapeiteacher.adapter;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cn.guugoo.jiapeiteacher.R;
import cn.guugoo.jiapeiteacher.bean.Reservation;
import cn.guugoo.jiapeiteacher.view.CircleImageView;

import java.util.ArrayList;

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

        System.out.println(reservation.toString());

        String time = reservation.getBookingTime();
        String hour = time.substring(time.indexOf(" ") + 1, time.length());
        String year = time.substring(0, time.indexOf(" ") + 1);
        String money = mContext.getResources().getString(R.string.money) + reservation.getAmount();
        String status = getStringStatus(reservation.getStatus());
        if (reservation.getIsComment() == 0 && reservation.getStatus() != 4) {
            viewHolder.tv_status.setVisibility(View.INVISIBLE);
            viewHolder.ll_status.setVisibility(View.VISIBLE);
            viewHolder.tv_status1.setText(status);
            viewHolder.tv_click_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reservation.getStatus() == 3 || reservation.getStatus() == 2 || reservation.getStatus() == 5) {
                        mCommentClickListener.onClick(reservation, position);
                    } else {
                        Toast.makeText(mContext, R.string.not_comment, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            viewHolder.tv_status.setVisibility(View.VISIBLE);
            viewHolder.ll_status.setVisibility(View.INVISIBLE);
            viewHolder.tv_status.setText(status);
        }


        viewHolder.tv_year.setText(year);
        viewHolder.tv_hour.setText(hour);
        viewHolder.tv_amount.setText(money);
        viewHolder.tv_name.setText(reservation.getName());
        viewHolder.tv_driverType.setText(reservation.getDriverType());
        viewHolder.tv_bookingAccount.setText(getStringBookingAccount(reservation.getBookingAccount()));

        Glide.with(mContext)
                .load(reservation.getHeadImg())
                .crossFade()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(viewHolder.civ_head);


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
                state = mContext.getResources().getString(R.string.pending_payment);
                break;
            case 3:
                state = mContext.getResources().getString(R.string.has_ended);
                break;
            case 4:
                state = mContext.getResources().getString(R.string.has_cancel);
                break;
            case 5:
                state = "待审核";
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
        TextView tv_year;
        TextView tv_hour;
        TextView tv_amount;
        TextView tv_name;
        TextView tv_driverType;
        TextView tv_bookingAccount;
        TextView tv_status;
        TextView tv_status1;
        TextView tv_click_comment;
        LinearLayout ll_status;

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
            tv_click_comment = (TextView) view.findViewById(R.id.tv_click_comment);
            civ_head = (CircleImageView) view.findViewById(R.id.civ_head);
            ll_status = (LinearLayout) view.findViewById(R.id.ll_status);
        }
    }

    public interface commentClickListener {
        void onClick(Reservation reservation, int position);
    }


    public void setCommentClickListener(commentClickListener commentClickListener) {
        this.mCommentClickListener = commentClickListener;
    }

}
