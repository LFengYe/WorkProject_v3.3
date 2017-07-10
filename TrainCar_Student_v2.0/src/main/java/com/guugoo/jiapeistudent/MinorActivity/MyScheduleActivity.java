package com.guugoo.jiapeistudent.MinorActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import com.guugoo.jiapeistudent.Adapter.ScheduleAdapter;
import com.guugoo.jiapeistudent.App.Constant;
import com.guugoo.jiapeistudent.Data.ReturnData;
import com.guugoo.jiapeistudent.Data.Schedule;
import com.guugoo.jiapeistudent.MainActivity.BaseActivity;
import com.guugoo.jiapeistudent.R;
import com.guugoo.jiapeistudent.Tools.DES;
import com.guugoo.jiapeistudent.Tools.MyThread;
import com.guugoo.jiapeistudent.Tools.MyToast;
import com.guugoo.jiapeistudent.Tools.Utils;
import com.guugoo.jiapeistudent.Views.TitleView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyScheduleActivity extends BaseActivity {
    private static final String TAG = "MyScheduleActivity";
    private ListView listView;
    private SharedPreferences sp;
    private List<Schedule> listData ;
    private ScheduleAdapter adapter;

    @Override
    protected void processingData(ReturnData data) {
        Log.d(TAG ,data.getData());
        List<Schedule> schedules = JSONObject.parseArray(data.getData(),Schedule.class);

        Comparator<Schedule> comparator = new Comparator<Schedule>() {
            @Override
            public int compare(Schedule lhs, Schedule rhs) {
                return lhs.getScheduleNo() - rhs.getScheduleNo();
            }
        };
        Collections.sort(schedules,comparator);
        listData.addAll(schedules);
        adapter.notifyDataSetChanged();

    }
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_schedule);
    }

    @Override
    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.Schedule_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setMiddleText(R.string.my_line3);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void findView() {
        listView = (ListView) findViewById(R.id.Schedule_list);
        listData = new ArrayList<>();
        adapter = new ScheduleAdapter(MyScheduleActivity.this,R.layout.adapter_schedule,listData);
        listView.setAdapter(adapter);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    @Override
    protected void init() {

        if(Utils.isNetworkAvailable(MyScheduleActivity.this)) {
            JSONObject json = new JSONObject();
            json.put("StudentId",sp.getInt("Id",0));
            Log.d(TAG, "getData: "+json.toString());
            new MyThread(Constant.URL_Schedule, handler, DES.encryptDES(json.toString())).start();
        }else {
            MyToast.makeText(MyScheduleActivity.this,R.string.Toast_internet);
        }

    }
}
