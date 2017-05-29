package cn.guugoo.jiapeiteacher.activity;


import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.guugoo.jiapeiteacher.R;
import cn.guugoo.jiapeiteacher.base.BaseActivity;
import cn.guugoo.jiapeiteacher.base.Constants;
import cn.guugoo.jiapeiteacher.fragment.ReservationFragment;

public class MyReservationActivity extends BaseActivity {


    private TextView tv_has_ended;
    private TextView tv_has_reservation;
    private TextView tv_total;
    private TextView tv_pending_payment;
    private ReservationFragment fg_total;
    private ReservationFragment fg_has_ended;
    private ReservationFragment fg_pending_payment;
    private ReservationFragment fg_has_reservation;
    private int teacherId;
    private int schoolId;
    private String token;

    @Override
    protected int getLayout() {
        return R.layout.activity_my_reservation;
    }


    @Override
    protected void initData() {
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_NAME, MODE_PRIVATE);
        teacherId = prefs.getInt("Id",0);
        schoolId = prefs.getInt("SchoolId", 0);
        token = prefs.getString("token","");
    }

    @Override
    protected void initView() {

        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.head);
        TextView tv_center = (TextView) rl_head.findViewById(R.id.tv_center);
        ImageView iv_back = (ImageView) rl_head.findViewById(R.id.iv_back);

        tv_has_ended = (TextView) findViewById(R.id.tv_has_ended);
        tv_has_reservation = (TextView) findViewById(R.id.tv_has_reservation);
        tv_total = (TextView) findViewById(R.id.tv_total);
        tv_pending_payment = (TextView) findViewById(R.id.tv_pending_payment);


        tv_center.setText(R.string.my_reservation);
        iv_back.setOnClickListener(this);

        tv_has_ended.setOnClickListener(this);
        tv_has_reservation.setOnClickListener(this);
        tv_total.setOnClickListener(this);
        tv_pending_payment.setOnClickListener(this);


        choiceState(0);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_pending_payment:
                choiceState(2);
                break;
            case R.id.tv_total:
                choiceState(0);
                break;
            case R.id.tv_has_reservation:
                choiceState(1);
                break;
            case R.id.tv_has_ended:
                choiceState(3);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, getIntent());
        super.onBackPressed();
    }


    private void initTextColor() {
        tv_has_ended.setBackgroundColor(ContextCompat.getColor(this, R.color.color_LightGray));
        tv_has_reservation.setBackgroundColor(ContextCompat.getColor(this, R.color.color_LightGray));
        tv_total.setBackgroundColor(ContextCompat.getColor(this, R.color.color_LightGray));
        tv_pending_payment.setBackgroundColor(ContextCompat.getColor(this, R.color.color_LightGray));
    }

    private void choiceState(int i) {
        initTextColor();
        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction transaction = fManager.beginTransaction();
        hideFragments(transaction);
        switch (i) {
            case 0:
                tv_total.setBackgroundColor(ContextCompat.getColor(this, R.color.color_White));
                if (fg_total == null) {

                    fg_total = ReservationFragment.newInstance(teacherId, schoolId, 0, token);
                    transaction.add(R.id.fl_content, fg_total);
                } else {
                    transaction.show(fg_total);
                }
                break;
            case 1:
                tv_has_reservation.setBackgroundColor(ContextCompat.getColor(this, R.color.color_White));
                if (fg_has_reservation == null) {
                    fg_has_reservation = ReservationFragment.newInstance(teacherId, schoolId, 1, token);
                    transaction.add(R.id.fl_content, fg_has_reservation);
                } else {
                    transaction.show(fg_has_reservation);
                }
                break;
            case 2:
                tv_pending_payment.setBackgroundColor(ContextCompat.getColor(this, R.color.color_White));
                if (fg_pending_payment == null) {
                    fg_pending_payment = ReservationFragment.newInstance(teacherId, schoolId, 2, token);
                    transaction.add(R.id.fl_content, fg_pending_payment);
                } else {
                    transaction.show(fg_pending_payment);
                }
                break;
            case 3:
                tv_has_ended.setBackgroundColor(ContextCompat.getColor(this, R.color.color_White));
                if (fg_has_ended == null) {

                    fg_has_ended = ReservationFragment.newInstance(teacherId, schoolId, 3, token);
                    transaction.add(R.id.fl_content, fg_has_ended);
                } else {
                    transaction.show(fg_has_ended);
                }
                break;
        }
        transaction.commit();
    }



    private void hideFragments(FragmentTransaction transaction) {
        if (fg_total != null) {
            transaction.hide(fg_total);
        }
        if (fg_has_ended!= null) {
            transaction.hide(fg_has_ended);
        }
        if (fg_has_reservation!= null) {
            transaction.hide(fg_has_reservation);
        }
        if (fg_pending_payment!= null) {
            transaction.hide(fg_pending_payment);
        }
    }

}
