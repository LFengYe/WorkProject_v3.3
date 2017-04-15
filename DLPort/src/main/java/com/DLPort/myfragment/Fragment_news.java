package com.DLPort.myfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.DLPort.FindActivity.baoxianActivity;
import com.DLPort.FindActivity.diyaActivity;
import com.DLPort.FindActivity.jiuyuanActivity;
import com.DLPort.FindActivity.twocarActivity;
import com.DLPort.NewsActivity.TongzhiActivity;
import com.DLPort.NewsActivity.gankouActivity;
import com.DLPort.NewsActivity.guanyuActivity;
import com.DLPort.NewsActivity.jiaotongActivity;
import com.DLPort.R;
import com.DLPort.myview.TitleView;

/**
 * Created by Administrator on 2016/3/23.
 */
public class Fragment_news extends Fragment {
    private View view;
    private View[] views;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.news_fragment,container,false);
        initTitle();
        findById();
        initView();
        return view;
    }

    private void findById() {
        views = new View[4];
        views[0] = view.findViewById(R.id.gangkou);
        views[1] = view.findViewById(R.id.jiaotong);
        views[2] = view.findViewById(R.id.tongzhi);
        views[3] = view.findViewById(R.id.guanyu);

    }


    private void initTitle() {
        TitleView titleView = (TitleView) view.findViewById(R.id.title_news);
        titleView.setMiddleTextVisible(true);
        titleView.setMiddleText(R.string.news);

    }
    private void initView() {
        final int type = getArguments().getInt("Type");
        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), gankouActivity.class);
                startActivity(intent);
            }
        });
        views[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), jiaotongActivity.class);
                startActivity(intent);
            }
        });
        views[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TongzhiActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Type", type);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        views[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), guanyuActivity.class);
                startActivity(intent);
            }
        });


    }

}
