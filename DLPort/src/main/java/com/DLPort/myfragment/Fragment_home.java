package com.DLPort.myfragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.HuoOrderActivity;
import com.DLPort.myactivity.MyOrderActivty;
import com.DLPort.mydata.ADInfo;
import com.DLPort.mytool.MyThread;
import com.DLPort.myview.ImageCycleView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/23.
 */
public class Fragment_home extends Fragment {
    public static final String TAG = "Fragment_home";
    /*
    private Fragment_owner fragment_owner;
    private Fragment_huozhu fragment_huozhu;
    private Fragment_youke fragment_youke;
    */
    private FragmentGoods fragmentGoods;
    private FragmentCarOwner fragmentCarOwner;

    private Fragment[] fragments;
    private Button[] buttons;
    private int index;
    private int currentTabIndex;
//    private View order;
    private int dataType;
    private String[] imageUrls;
    private ImageCycleView mAdView;
    private ArrayList<ADInfo> infos = new ArrayList<ADInfo>();
    private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {


        @Override
        public void displayImage(String imageURL, ImageView imageView) {
            ImageLoader.getInstance().displayImage(imageURL, imageView);// 使用ImageLoader对图片进行加装！
        }

        @Override
        public void onImageClick(ADInfo info, int postion, View imageView) {

        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    JSONObject jsonObject = new JSONObject((String) msg.obj);
                    Log.d(TAG, "Login的响应" + jsonObject.toString());
                    int status = jsonObject.getInt("Status");
                    Log.i(TAG, "状态" + status);
                    if (0 == status) {
                        String string = jsonObject.getString("Data");
                        getIamge(string);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        findView(view);
        init();
        String parentTag = getArguments().getString("parentTag");
        if (null != parentTag &&
                (parentTag.equalsIgnoreCase("PayConfirm")
                        || parentTag.equalsIgnoreCase("NewsPublish"))) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), HuoOrderActivity.class);
            startActivity(intent);
        }
        return view;
    }

    private void findView(View view) {
        buttons = new Button[3];
        buttons[0] = (Button) view.findViewById(R.id.chezhu);
        buttons[1] = (Button) view.findViewById(R.id.huozhu);
        /*
        fragment_huozhu = new Fragment_huozhu();
        fragment_owner = new Fragment_owner();
        fragment_youke = new Fragment_youke();
        */

        fragmentCarOwner = new FragmentCarOwner();
        fragmentGoods = new FragmentGoods();
        fragments = new Fragment[]{fragmentCarOwner, fragmentGoods};
//        order = view.findViewById(R.id.myorder_he);
        mAdView = (ImageCycleView) view.findViewById(R.id.home_image);

        JSONObject json = new JSONObject();
//        new MyThread(Constant.URL_UserGETIMAGE, handler, json, getContext()).start();
    }

    private void init() {
        dataType = getArguments().getInt("Type", 0);
        index = dataType;
        buttons[dataType].setTextColor(0xFFFF5252);
        buttons[dataType].setSelected(true);
        currentTabIndex = dataType;
        switch (dataType) {
            case 0:
                getFragmentManager().beginTransaction()
                        .add(R.id.grid_view, fragmentCarOwner)
                        .add(R.id.grid_view, fragmentGoods)
                        .hide(fragmentGoods)
                        .show(fragmentCarOwner).commit();
                break;
            case 1:
                getFragmentManager().beginTransaction()
                        .add(R.id.grid_view, fragmentCarOwner)
                        .add(R.id.grid_view, fragmentGoods)
                        .hide(fragmentCarOwner)
                        .show(fragmentGoods).commit();
                break;
        }


        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 0;
                setButtons();
            }
        });
        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 1;
                setButtons();

            }
        });
        /*
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataType == 0 && index == 0) {
                    Intent intent = new Intent(getActivity(), MyOrderActivty.class);
                    startActivity(intent);
                } else if (dataType == 1 && index == 1) {
                    Intent intent = new Intent(getActivity(), HuoOrderActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "请登陆后点击",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });
        fragment_owner.gettype(new Fragment_owner.GetType() {
            @Override
            public boolean getdata() {
                if (dataType == 0 && index == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        fragment_huozhu.gettype(new Fragment_huozhu.GetType() {
            @Override
            public boolean getdata() {
                if (dataType == 1 && index == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        */
    }

    private void setButtons() {
        if (currentTabIndex != index) {
            FragmentTransaction trx = getFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.main_content, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        // 把当前tab设为选中状态
        if (currentTabIndex != index) {
            buttons[index].setTextColor(0xFFFF5252);
            buttons[index].setSelected(true);
            buttons[currentTabIndex].setSelected(false);
            buttons[currentTabIndex].setTextColor(0xFFFFFFFF);
            currentTabIndex = index;
        }
    }

    private void getIamge(String data) {

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(data);
            imageUrls = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                imageUrls[i] = json.getString("Image");
            }

            for (int i = 0; i < imageUrls.length; i++) {
                Log.d(TAG, imageUrls[i]);
                ADInfo info = new ADInfo();
                info.setUrl(imageUrls[i]);
                info.setContent("top-->" + i);
                infos.add(info);
            }
            mAdView.setImageResources(infos, mAdCycleViewListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mAdView.startImageCycle();
    }

    ;

    @Override
    public void onPause() {
        super.onPause();
        mAdView.pushImageCycle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdView.pushImageCycle();
    }


}
