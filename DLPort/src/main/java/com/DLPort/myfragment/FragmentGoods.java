package com.DLPort.myfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.DLPort.R;

/**
 * Created by LFeng on 2017/5/15.
 */

public class FragmentGoods extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_goods,container,false);
        return view;
    }
}
