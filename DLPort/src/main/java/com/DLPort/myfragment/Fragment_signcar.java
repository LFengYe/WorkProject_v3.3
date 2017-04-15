package com.DLPort.myfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.myview.MyDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/3/28.
 */
public class Fragment_signcar extends Fragment {
    private View view;
    private View[] views;
    private TextView[] textViews;
    private int index;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.sign_in_car,container,false);
        findById();
        init();
        return view;
    }

    private void init() {

        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(getActivity(),1);
                myDialog.setContent("公司名称");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("例如：腾讯");
                myDialog.show();
                index = 0;
            }
        });
        views[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(getActivity(),1);
                myDialog.setContent("公司地址");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("例如：北京朝阳路");
                myDialog.show();
                index = 1;
            }
        });
        views[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(getActivity(),1);
                myDialog.setContent("负责人姓名");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("例如：张三");
                myDialog.show();
                index = 2;
            }
        });
        views[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(getActivity(),1);
                myDialog.setContent("联系电话");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("例如：1871234567");
                myDialog.show();
                index=3;
            }
        });
        views[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(getActivity(),1);
                myDialog.setContent("用户名");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("建议用您的手机号");
                myDialog.show();
                index=4;
            }
        });
        views[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(getActivity(),1);
                myDialog.setContent("密码");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("请使用数字加字母");
                myDialog.show();
                index = 5;
            }
        });
        views[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(getActivity(),1);
                myDialog.setContent("邀请人");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("例如：王五（选填）");
                myDialog.show();
                index = 6;
            }
        });

    }
    MyDialog.Dialogcallback dialogcallback = new MyDialog.Dialogcallback() {
        @Override
        public void dialogdo(String string) {
            textViews[index].setText(string);
        }
    };

    private void findById() {
        views = new View[7];
        textViews = new TextView[7];
        views[0]= view.findViewById(R.id.car_click_Companyname);
        views[1]=view.findViewById(R.id.car_click_Address);
        views[2]=view.findViewById(R.id.car_click_Principal);
        views[3] =view.findViewById(R.id.car_click_Telephoen);
        views[4] = view.findViewById(R.id.car_click_LoginName);
        views[5] = view.findViewById(R.id.car_click_PassWork);
        views[6] = view.findViewById(R.id.car_click_Inviter);

        textViews[0]= (TextView) view.findViewById(R.id.car_Companyname);
        textViews[1] = (TextView) view.findViewById(R.id.car_Address);
        textViews[2] = (TextView) view.findViewById(R.id.car_Principal);
        textViews[3] = (TextView) view.findViewById(R.id.car_Telephoen);
        textViews[4] = (TextView) view.findViewById(R.id.car_LoginName);
        textViews[5] = (TextView) view.findViewById(R.id.car_PassWork);
        textViews[6] = (TextView) view.findViewById(R.id.car_Inviter);

    }

    public JSONObject getData(){

        JSONObject json = new JSONObject();

        try {

            json.put("Companyname",textViews[0].getText().toString());
            json.put("Address",textViews[1].getText().toString());
            json.put("Principal",textViews[2].getText().toString());
            json.put("Telephoen",textViews[3].getText().toString());
            json.put("LoginName",textViews[4].getText().toString());
            json.put("PassWork",textViews[5].getText().toString());
            json.put("Inviter",textViews[6].getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    public boolean isKong(){
        boolean kong = true;
        for(int i=0;i<=5;i++){
            if(textViews[i].getText().equals("")){
                kong =false;
                return kong;
            }
        }
      return kong;
    }




}
