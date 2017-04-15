package com.DLPort.myfragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.DLPort.FindActivity.BaoXianMyActivity;
import com.DLPort.FindActivity.baoxianActivity;
import com.DLPort.FindActivity.diyaActivity;
import com.DLPort.FindActivity.jiuyuanActivity;
import com.DLPort.FindActivity.touziActivity;
import com.DLPort.FindActivity.twocarActivity;
import com.DLPort.R;
import com.DLPort.myview.TitleView;


/**
 * Created by Administrator on 2016/3/23.
 */
public class Fragment_find extends Fragment {
    private View view;
    private View[] views;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.find_fragment,container,false);
        initTitle();
        findById();
        init();
        return view;
    }

    private void findById() {
        views = new View[5];
        views[0] = view.findViewById(R.id.baoqian);
        views[1] = view.findViewById(R.id.jiuyuan);
        views[2] = view.findViewById(R.id.ershouche);
        views[3] = view.findViewById(R.id.diya);
        views[4] = view.findViewById(R.id.touzi);
    }

    private void init() {
        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BaoXianMyActivity.class);
                startActivity(intent);
            }
        });
        views[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), jiuyuanActivity.class);
                startActivity(intent);
            }
        });
        views[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), twocarActivity.class);
                startActivity(intent);
            }
        });
        views[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), diyaActivity.class);
                startActivity(intent);
            }
        });
        views[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), touziActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initTitle() {
        TitleView titleView = (TitleView) view.findViewById(R.id.title_find);
        titleView.setMiddleTextVisible(true);
        titleView.setMiddleText(R.string.find);

    }
}
