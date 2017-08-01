package com.guugoo.jiapeistudent.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.guugoo.jiapeistudent.App.ActivityCollector;
import com.guugoo.jiapeistudent.App.Constant;
import com.guugoo.jiapeistudent.Data.MyInformation;
import com.guugoo.jiapeistudent.Data.ReturnData;
import com.guugoo.jiapeistudent.MainActivity.BaseActivity;
import com.guugoo.jiapeistudent.MainActivity.LoginActivity;
import com.guugoo.jiapeistudent.MinorActivity.MyCouponActivity;
import com.guugoo.jiapeistudent.MinorActivity.MyScheduleActivity;
import com.guugoo.jiapeistudent.MinorActivity.PersonActivity;
import com.guugoo.jiapeistudent.R;
import com.guugoo.jiapeistudent.Tools.DES;
import com.guugoo.jiapeistudent.Tools.MyHandler;
import com.guugoo.jiapeistudent.Tools.MyThread;
import com.guugoo.jiapeistudent.Tools.MyToast;
import com.guugoo.jiapeistudent.Tools.Utils;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Set;

/**
 * Created by Administrator on 2016/8/3.
 */
public class MyFragment extends Fragment {
    private static final String TAG = "MyFragment";
    private View fragmentView;
    private View[] views;
    private SharedPreferences sp;
    private TextView[] textViews;
    private ImageView my_head;
    private TextView my_name;
    private ImageLoader imageLoader = ImageLoader.getInstance();


    protected Handler handler = new MyHandler(getContext()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                //Log.d(TAG, "handleMessage: "+msg.obj);
                ReturnData data= JSONObject.parseObject((String) msg.obj,ReturnData.class);
                if(data.getStatus()==0){
                    MyInformation myInformation =JSONObject.parseObject(data.getData(),MyInformation.class);
                    if(!TextUtils.isEmpty(myInformation.getHeadPortrait())){
                        imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
                        imageLoader.getInstance().displayImage(myInformation.getHeadPortrait(), my_head);
                    }
                    textViews[0].setText(myInformation.getPracticeCount());
                    textViews[1].setText(String.valueOf(myInformation.getCoupon()));
                    textViews[2].setText(myInformation.getInvitationCode());
                    textViews[3].setText(myInformation.getCustomerservice());
                    textViews[4].setText(myInformation.getSchedule());
                    sp.edit().putString("InvitationCode",myInformation.getInvitationCode()).apply();
                    sp.edit().putInt("Hours",myInformation.getCoupon()).apply();
                }else {
                    MyToast.makeText(getContext(),data.getMessage());
                }
            }
        }
    };

    protected Handler handler1 = new MyHandler(getActivity()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                try {
                    //Log.d(TAG, "handleMessage: "+msg.obj);
                    ReturnData data= JSONObject.parseObject((String) msg.obj,ReturnData.class);
                    if(data.getStatus()==0){
                        ActivityCollector.finishAll();
                        Intent intent =new Intent(getActivity(), LoginActivity.class);
                        sp.edit().putBoolean("AUTO_ISCHECK", false).apply();
                        sp.edit().putBoolean("LOGINOK",false).apply();
                        startActivity(intent);
                        //退出极光推送绑定
                        JPushInterface.setAlias(getContext(), "", new TagAliasCallback() {
                            @Override
                            public void gotResult(int i, String s, Set<String> set) {

                            }
                        });
                        JPushInterface.stopPush(getContext().getApplicationContext());
                    }else {
                        MyToast.makeText(getActivity(),data.getMessage());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    MyToast.makeText(getActivity(),"数据出错");
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        fragmentView =inflater.inflate(R.layout.fragment_my,container,false);
        findById();
        init();
        return fragmentView;
    }

    private void findById() {
        views = new View[3];
        textViews = new TextView[5];
        views[0] = fragmentView.findViewById(R.id.my_line3);
        views[1] = fragmentView.findViewById(R.id.my_line7);
        views[2] = fragmentView.findViewById(R.id.my_line1);
        sp = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        textViews[0] = (TextView) fragmentView.findViewById(R.id.my_text1);
        textViews[1] = (TextView) fragmentView.findViewById(R.id.my_text2);
        textViews[2] = (TextView) fragmentView.findViewById(R.id.my_text3);
        textViews[3] = (TextView) fragmentView.findViewById(R.id.my_text4);
        textViews[4] = (TextView) fragmentView.findViewById(R.id.schedule_text);
        my_head = (ImageView) fragmentView.findViewById(R.id.my_hand);
        my_name= (TextView) fragmentView.findViewById(R.id.my_name);
        sp.edit().putString("InvitationCode","").apply();
    }

    private void init() {
        getData();
        my_name.setText(sp.getString("Name","XXX"));
        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyScheduleActivity.class);
                startActivity(intent);
            }
        });
        views[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("确认退出吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(Utils.isNetworkAvailable(getContext())){
                            JSONObject json= new JSONObject(true);
                            json.put("Tel",sp.getString("Tel",""));
                            json.put("UserType",1);
                            new MyThread(Constant.URL_SignOut, handler1, DES.encryptDES(json.toString())).start();
                        }else {
                            MyToast.makeText(getContext(),R.string.Toast_internet);
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();

            }
        });
        views[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyCouponActivity.class);
                startActivity(intent);
            }
        });

        my_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getData(){
        if(Utils.isNetworkAvailable(getContext())){
            JSONObject json= new JSONObject(true);
            json.put("StudentId",sp.getInt("Id",0));
            new MyThread(Constant.URL_MyMessage, handler, DES.encryptDES(json.toString())).start();
        }else {
            MyToast.makeText(getContext(),R.string.Toast_internet);
        }
    }

}
