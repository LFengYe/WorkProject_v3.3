package cn.com.caronwer.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.com.caronwer.R;

public class DescriptionFragment extends Fragment {


    private int state;
    private RelativeLayout rl_volume;
    private RelativeLayout rl_kg;
    private RelativeLayout rl_amount;
    private LinearLayout ll_1;
    private LinearLayout ll_2;
    private CheckBox cb_type;
    public DescriptionFragment() {

    }

    public static DescriptionFragment newInstance(int state) {
        DescriptionFragment dfg = new DescriptionFragment();
        Bundle args = new Bundle();
        args.putInt("state", state);
        dfg.setArguments(args);
        return dfg;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            state = args.getInt("state");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_description, container, false);
        rl_volume = (RelativeLayout) view.findViewById(R.id.rl_volume);
        rl_kg = (RelativeLayout) view.findViewById(R.id.rl_kg);
        rl_amount = (RelativeLayout) view.findViewById(R.id.rl_amount);
        ll_1 = (LinearLayout) view.findViewById(R.id.ll_1);
        ll_2 = (LinearLayout) view.findViewById(R.id.ll_2);
        cb_type = (CheckBox) view.findViewById(R.id.cb_type);
        switch (state){
            case 1:
                ll_1.setVisibility(View.GONE);
                ll_2.setVisibility(View.GONE);
                cb_type.setVisibility(View.GONE);
                break;
            case 2:
                Visiblity(rl_volume, rl_kg, rl_amount);
                cb_type.setText("全拆座");
                break;
            case 3:
                Visiblity(rl_volume, rl_kg, rl_amount);
                cb_type.setText("全拆座1");
                break;
            case 4:
                Visiblity(rl_volume, rl_kg, rl_amount);
                cb_type.setText("开顶");
                break;
            case 5:
                Visiblity(rl_volume, rl_kg, rl_amount);
                cb_type.setText("开顶1");
                break;
        }
        return view;
    }

    private static void Visiblity(RelativeLayout rl_volume, RelativeLayout rl_kg, RelativeLayout rl_amount) {
        rl_volume.setVisibility(View.GONE);
        rl_kg.setVisibility(View.GONE);
        rl_amount.setVisibility(View.GONE);
    }

}
