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
import com.DLPort.myactivity.NewsPublishActivty;
import com.DLPort.myactivity.PositionActivity;
import com.DLPort.myactivity.ShipQueryActivity;
import com.DLPort.myadapter.PictureAdapter;
import com.DLPort.mytool.MyToast;

/**
 * Created by Administrator on 2016/3/25.
 */
public class Fragment_huozhu extends Fragment {

    public static final String TAG = "Fragment_huozhu";
    private GetType type;

    private GridView gridView;
    //图片的文字标题
    private int[] titles = new int[]{
            R.string.ship_query, R.string.public_resource,
            R.string.O_pic4, R.string.O_pic5, R.string.O_pic6
    };
    //图片ID数组
    private int[] images = new int[]{
            R.mipmap.home_icon1, R.mipmap.home_icon3_1,
            R.mipmap.home_icon4, R.mipmap.home_icon5,
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
                if(type.getdata()) {
                    switch (i) {
                        case 0:
                            Intent intent0 = new Intent(getActivity(), ShipQueryActivity.class);
                            Bundle bundle0 = new Bundle();
                            bundle0.putInt("Type", 1);//设置用户类型为货主
                            intent0.putExtras(bundle0);
                            startActivity(intent0);
                            break;
                        /*
                        case 1:
                            Intent intent1 = new Intent(getActivity(), TuiGuangActivity.class);
                            Bundle bundle1 = new Bundle();
                            bundle1.putInt("Type", 1);//设置用户类型为货主
                            intent1.putExtras(bundle1);
                            startActivity(intent1);
                            break;
                            */
                        case 1:
                            Intent intent2 = new Intent(getActivity(), NewsPublishActivty.class);
                            Bundle bundle2 = new Bundle();
                            bundle2.putInt("Type", 0);//设置订单类型为正常订单
                            intent2.putExtras(bundle2);
                            startActivity(intent2);
                            break;
                        case 2:
                            Intent intent3 = new Intent(getActivity(), PositionActivity.class);
                            Bundle bundle3 = new Bundle();
                            bundle3.putInt("Type", 1);//设置用户类型为货主
                            intent3.putExtras(bundle3);
                            startActivity(intent3);
                            break;
                        case 3:
                            Intent intent4 = new Intent(getActivity(), NewsPublishActivty.class);
                            Bundle bundle4 = new Bundle();
                            bundle4.putInt("Type", 1);//设置订单类型为拼车窗口订单
                            intent4.putExtras(bundle4);
                            startActivity(intent4);
                            break;

                        default:
                            Toast.makeText(getActivity(), "错误", Toast.LENGTH_LONG).show();
                            break;
                    }
                }else {
                    MyToast.makeText(getActivity(), "请登陆后点击");
                }

            }
        });
        return view;
    }
    public interface GetType{
        public boolean getdata();
    }
    public void gettype(GetType getType){
        this.type=getType;
    }

}
