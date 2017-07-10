package cn.guugoo.jiapeiteacher.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.guugoo.jiapeiteacher.R;
import cn.guugoo.jiapeiteacher.activity.CirclesSharingActivity;
import cn.guugoo.jiapeiteacher.activity.LawsActivity;
import cn.guugoo.jiapeiteacher.activity.MyNewsActivity;
import cn.guugoo.jiapeiteacher.activity.MyReservationActivity;
import cn.guugoo.jiapeiteacher.activity.MyRecommendActivity;
import cn.guugoo.jiapeiteacher.activity.WorkbenchActivity;
import cn.guugoo.jiapeiteacher.base.Constants;
import cn.guugoo.jiapeiteacher.receiver.MyReceiver;

import static android.app.Activity.RESULT_OK;


public class HomeFragment extends Fragment implements View.OnClickListener{


    private ImageView iv_point;
    private int startState;

    public HomeFragment() {
        super();
    }

    public static HomeFragment newInstance(int startState) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt("startState", startState);
        homeFragment.setArguments(args);
        return homeFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            startState = args.getInt("startState");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        LinearLayout ll_my_reservation = (LinearLayout) view.findViewById(R.id.ll_my_reservation);
        LinearLayout ll_workbench = (LinearLayout) view.findViewById(R.id.ll_workbench);
        LinearLayout ll_circles_sharing = (LinearLayout) view.findViewById(R.id.ll_circles_sharing);
        LinearLayout ll_my_news = (LinearLayout) view.findViewById(R.id.ll_my_news);
        LinearLayout ll_my_recommend = (LinearLayout) view.findViewById(R.id.ll_my_recommend);
        LinearLayout ll_laws = (LinearLayout) view.findViewById(R.id.ll_laws);
        iv_point = (ImageView) view.findViewById(R.id.iv_point);
        setBallState(Constants.ballState);
        ll_my_reservation.setOnClickListener(this);
        ll_workbench.setOnClickListener(this);
        ll_circles_sharing.setOnClickListener(this);
        ll_my_news.setOnClickListener(this);
        ll_my_recommend.setOnClickListener(this);
        ll_laws.setOnClickListener(this);


        if (startState == 1) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), CirclesSharingActivity.class);
            startActivity(intent);
        }
        if (startState == 7) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), WorkbenchActivity.class);
            intent.putExtra("startState", startState);
            startActivity(intent);
        }
        if (startState == 8) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), MyNewsActivity.class);
            startActivity(intent);
        }
    }

    public void setBallState(boolean ballState) {
        if (!ballState) {
            iv_point.setVisibility(View.INVISIBLE);
        } else {
            iv_point.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ll_my_reservation:
                intent.setClass(getActivity(), MyReservationActivity.class);
                break;
            case R.id.ll_workbench:
                intent.setClass(getActivity(), WorkbenchActivity.class);
                intent.putExtra("startState", startState);
                break;
            case R.id.ll_circles_sharing:
                intent.setClass(getActivity(), CirclesSharingActivity.class);
                break;
            case R.id.ll_my_news:
                intent.setClass(getActivity(), MyNewsActivity.class);
                break;
            case R.id.ll_my_recommend:
                intent.setClass(getActivity(), MyRecommendActivity.class);
                break;
            case R.id.ll_laws:
                intent.setClass(getActivity(), LawsActivity.class);
                break;
        }
        startActivityForResult(intent,19);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 19 && resultCode == RESULT_OK) {
            setBallState(Constants.ballState);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}
