package com.DLPort.myfragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.DLPort.R;
import com.DLPort.myactivity.ShipQueryActivity;
import com.DLPort.myactivity.TuiGuangActivity;
import com.DLPort.myadapter.PictureAdapter;

/**
 * Created by Administrator on 2016/3/25.
 */
public class Fragment_youke extends Fragment {

    private GridView gridView;
    /*
    图片的文字标题
     */

    private int[] titles = new int[]{
            R.string.ship_query, R.string.O_pic6

    };
    /*
    图片ID数组
     */

    private int[] images = new int[]{
            R.mipmap.home_icon1
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.owner_fragment,container,false);
        gridView = (GridView) view.findViewById(R.id.owner_gradview);
        PictureAdapter adapter = new PictureAdapter(titles,images,getActivity());
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {
                    case 0:
                        Intent intent0 = new Intent(getActivity(), ShipQueryActivity.class);
                        Bundle bundle0 = new Bundle();
                        bundle0.putInt("Type", 2);
                        intent0.putExtras(bundle0);
                        startActivity(intent0);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getActivity(), TuiGuangActivity.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putInt("Type", 2);
                        intent1.putExtras(bundle1);
                        startActivity(intent1);
                        break;
                    default:
                        Toast.makeText(getActivity(), "错误", Toast.LENGTH_LONG).show();
                        break;
                }


            }
        });
        return view;
    }
}
