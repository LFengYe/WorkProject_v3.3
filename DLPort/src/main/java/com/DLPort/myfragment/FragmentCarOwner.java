package com.DLPort.myfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.DLPort.R;
import com.DLPort.myactivity.ResourceSelectActivity;

/**
 * Created by LFeng on 2017/5/15.
 */

public class FragmentCarOwner extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_car_owner,container,false);
        view.findViewById(R.id.find_resource).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ResourceSelectActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
