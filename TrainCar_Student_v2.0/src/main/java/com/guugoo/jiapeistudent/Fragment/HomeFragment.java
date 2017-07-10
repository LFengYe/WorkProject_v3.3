package com.guugoo.jiapeistudent.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.guugoo.jiapeistudent.App.Constant;
import com.guugoo.jiapeistudent.Data.ADInfo;
import com.guugoo.jiapeistudent.Data.ReturnData;
import com.guugoo.jiapeistudent.MainActivity.ApplyTrainActivity;
import com.guugoo.jiapeistudent.MainActivity.CircleShareActivity;
import com.guugoo.jiapeistudent.MainActivity.MyMessageActivity;
import com.guugoo.jiapeistudent.MainActivity.MyRecommendActivity;
import com.guugoo.jiapeistudent.MainActivity.MyReserveActivity;
import com.guugoo.jiapeistudent.MainActivity.ReserveTrainActivity;
import com.guugoo.jiapeistudent.MainActivity.StudyActivity;
import com.guugoo.jiapeistudent.R;
import com.guugoo.jiapeistudent.Tools.DES;
import com.guugoo.jiapeistudent.Tools.MyHandler;
import com.guugoo.jiapeistudent.Tools.MyThread;
import com.guugoo.jiapeistudent.Tools.MyToast;
import com.guugoo.jiapeistudent.Tools.Utils;
import com.guugoo.jiapeistudent.Views.ImageCycleView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/3.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private View fragmentView;
    private View[] views;
    private ImageCycleView mAdView;
    private SharedPreferences sp;
    private ArrayList<ADInfo> infos = new ArrayList<ADInfo>();
    private String[] imageUrls;
    private ImageView ball;
    protected Handler handler = new MyHandler(getContext()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                Log.d(TAG, "handleMessage: "+msg.obj);
                ReturnData data= JSONObject.parseObject((String) msg.obj,ReturnData.class);
                if(data.getStatus()==0){
                    Log.d(TAG, "handleMessage: "+data.getData());
                    getIamge(data.getData());
                }else {
                    MyToast.makeText(getContext(),data.getMessage());
                }

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        fragmentView =inflater.inflate(R.layout.fragment_home,container,false);
        findById();
        init();
        return fragmentView;
    }

    private void findById() {
        views = new View[7];
        views[0] = fragmentView.findViewById(R.id.home_one);
        views[1] = fragmentView.findViewById(R.id.home_two);
        views[2] = fragmentView.findViewById(R.id.home_three);
        views[3] = fragmentView.findViewById(R.id.home_four);
        views[4] = fragmentView.findViewById(R.id.home_five);
        views[5] = fragmentView.findViewById(R.id.home_six);
        views[6] = fragmentView.findViewById(R.id.home_seven);
        mAdView = (ImageCycleView) fragmentView.findViewById(R.id.fragment_home_image);
        sp = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        ball = (ImageView) fragmentView.findViewById(R.id.home_ball);
        setBall(Constant.state);
    }
    private void init() {
        getImage();
        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ApplyTrainActivity.class);
                startActivity(intent);
            }
        });
        views[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StudyActivity.class);
                startActivity(intent);
            }
        });
        views[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReserveTrainActivity.class);
                startActivity(intent);
            }
        });
        views[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyReserveActivity.class);
                startActivity(intent);
            }
        });
        views[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CircleShareActivity.class);
                startActivity(intent);

            }
        });
        views[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyRecommendActivity.class);
                startActivity(intent);
            }
        });
        views[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyMessageActivity.class);
                startActivityForResult(intent, Activity.RESULT_FIRST_USER);
            }
        });
    }

    private void getImage(){
        if(Utils.isNetworkAvailable(getContext())){
            JSONObject json= new JSONObject(true);
            json.put("SchoolId", sp.getInt("SchoolId",0));
            new MyThread(Constant.URL_GetPicture, handler, DES.encryptDES(json.toString())).start();
        }else {
            MyToast.makeText(getActivity(),R.string.Toast_internet);
        }
    }

    private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {

        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {

        }

        @Override
        public void displayImage(String imageURL, ImageView imageView) {
            ImageLoader.getInstance().displayImage(imageURL, imageView);// 使用ImageLoader对图片进行加装！
        }
    };
    private void getIamge(String data) {

        JSONArray jsonArray = null;
        try {
            jsonArray = JSONArray.parseArray(data);
            if(jsonArray.size()>0){
                imageUrls = new String[jsonArray.size()];
                Log.d(TAG, "getIamge: "+jsonArray.size());
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    imageUrls[i] = json.getString("Img");
                }

                for (int i = 0; i < imageUrls.length; i++) {
                    Log.d(TAG, imageUrls[i]);
                    ADInfo info = new ADInfo();
                    info.setUrl(imageUrls[i]);
                    info.setContent("top-->" + i);
                    infos.add(info);
                    Log.d(TAG, "getIamge: "+imageUrls[i]);
                }
                mAdView.setImageResources(infos, mAdCycleViewListener);
            }

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

    public void setBall(boolean state){
        if(state){
            ball.setVisibility(View.VISIBLE);
        }else {
            ball.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: "+123);
        switch (requestCode){
            case  Activity.RESULT_FIRST_USER:
                setBall(false);
                break;
        }
    }

}
