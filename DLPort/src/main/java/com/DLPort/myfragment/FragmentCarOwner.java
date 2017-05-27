package com.DLPort.myfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.DLPort.NewsActivity.TrafficActivity;
import com.DLPort.OurActivity.CarOwnerStatisticsActivity;
import com.DLPort.R;
import com.DLPort.myactivity.CarGoOrderActivity;
import com.DLPort.myactivity.CarOwnerOrderActivty;
import com.DLPort.myactivity.PositionActivity;
import com.DLPort.myactivity.ResourceSelectActivity;
import com.DLPort.myactivity.ShipQueryActivity;

/**
 * Created by LFeng on 2017/5/15.
 */

public class FragmentCarOwner extends Fragment {

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putInt("userType", 0);//设置用户类型为车主
            bundle.putInt("dataType", 0);

            switch (v.getId()) {
                case R.id.find_resource: {
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(getActivity(), ResourceSelectActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.my_cars: {
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(getActivity(), PositionActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.my_order: {
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(getActivity(), CarOwnerOrderActivty.class);
                    startActivity(intent);
                    break;
                }
                case R.id.ship_query: {
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(getActivity(), ShipQueryActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.recharge_cash: {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), CarOwnerStatisticsActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.traffic: {
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(getActivity(), TrafficActivity.class);
                    startActivity(intent);
                    break;
                }
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_car_owner,container,false);
        view.findViewById(R.id.find_resource).setOnClickListener(clickListener);
        view.findViewById(R.id.my_cars).setOnClickListener(clickListener);
        view.findViewById(R.id.my_order).setOnClickListener(clickListener);
        view.findViewById(R.id.ship_query).setOnClickListener(clickListener);
        view.findViewById(R.id.recharge_cash).setOnClickListener(clickListener);
        view.findViewById(R.id.traffic).setOnClickListener(clickListener);
        return view;
    }
}
