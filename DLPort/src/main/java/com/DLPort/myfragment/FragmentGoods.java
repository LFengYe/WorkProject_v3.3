package com.DLPort.myfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.DLPort.NewsActivity.TrafficActivity;
import com.DLPort.OurActivity.CargoStatisticsActivity;
import com.DLPort.R;
import com.DLPort.myactivity.CarGoOrderActivity;
import com.DLPort.myactivity.CarOwnerOrderActivty;
import com.DLPort.myactivity.ResourceSelectActivity;
import com.DLPort.myactivity.ShipQueryActivity;

/**
 * Created by LFeng on 2017/5/15.
 */

public class FragmentGoods extends Fragment {

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putInt("userType", 1);//设置用户类型为货主
            bundle.putInt("dataType", 0);

            switch (v.getId()) {
                case R.id.publish_resource: {
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(getActivity(), ResourceSelectActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.my_order: {
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(getActivity(), CarGoOrderActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.resource_info: {
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(getActivity(), TrafficActivity.class);
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
                    intent.putExtras(bundle);
                    intent.setClass(getActivity(), CargoStatisticsActivity.class);
                    startActivity(intent);
                    break;
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_goods,container,false);
        view.findViewById(R.id.publish_resource).setOnClickListener(clickListener);
        view.findViewById(R.id.my_order).setOnClickListener(clickListener);
        view.findViewById(R.id.resource_info).setOnClickListener(clickListener);
        view.findViewById(R.id.ship_query).setOnClickListener(clickListener);
        view.findViewById(R.id.recharge_cash).setOnClickListener(clickListener);
        return view;
    }
}
