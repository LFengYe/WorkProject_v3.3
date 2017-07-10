package com.guugoo.jiapeistudent.MainActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.guugoo.jiapeistudent.Adapter.RecommendAdapter;
import com.guugoo.jiapeistudent.App.Constant;
import com.guugoo.jiapeistudent.Data.Recommend;
import com.guugoo.jiapeistudent.Data.ReturnData;
import com.guugoo.jiapeistudent.MinorActivity.AllApplyTrainActivity;
import com.guugoo.jiapeistudent.R;
import com.guugoo.jiapeistudent.Tools.DES;
import com.guugoo.jiapeistudent.Tools.MyThread;
import com.guugoo.jiapeistudent.Tools.MyToast;
import com.guugoo.jiapeistudent.Tools.Utils;
import com.guugoo.jiapeistudent.Views.TitleView;

import java.util.ArrayList;
import java.util.List;

public class MyRecommendActivity extends BaseActivity {
    private static final String TAG = "MyRecommendActivity";
    private ListView listView;
    private SharedPreferences sp;
    private List<Recommend> listData ;
    private RecommendAdapter adapter;
    private LinearLayout layout;
    private TextView myRecommend;

    @Override
    protected void processingData(ReturnData data) {
        Log.d(TAG, "processingData:123123 "+data.getData());
        List<Recommend> recommends = JSONObject.parseArray(data.getData(),Recommend.class);
        listData.addAll(recommends);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_recommend);
    }

    @Override
    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.my_recommend_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setMiddleText(R.string.home_text6);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void findView() {
        listView = (ListView) findViewById(R.id.my_recommend_list);
        listData = new ArrayList<>();
        adapter = new RecommendAdapter(MyRecommendActivity.this,R.layout.adapter_recommend,listData);
        listView.setAdapter(adapter);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        layout = (LinearLayout) findViewById(R.id.recommend_rule);
        myRecommend= (TextView) findViewById(R.id.my_recommend_text);
    }

    @Override
    protected void init() {

        myRecommend.setText(sp.getString("InvitationCode",""));
        getData();
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyRecommendActivity.this, AllApplyTrainActivity.class);
                intent.putExtra("Type",4);
                startActivity(intent);
            }
        });
    }

    private void getData(){
        if(Utils.isNetworkAvailable(MyRecommendActivity.this)){
            JSONObject json= new JSONObject(true);
            json.put("StudentId",sp.getInt("Id",0));
            Log.d(TAG, "getData: "+json.toString());
            new MyThread(Constant.URL_Recommend, handler, DES.encryptDES(json.toString())).start();
        }else {
            MyToast.makeText(MyRecommendActivity.this,R.string.Toast_internet);
        }

    }

}
