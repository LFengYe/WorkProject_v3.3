package com.guugoo.jiapeistudent.MinorActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guugoo.jiapeistudent.Adapter.CouponAdapter;
import com.guugoo.jiapeistudent.App.Constant;
import com.guugoo.jiapeistudent.Data.CouponDetail;
import com.guugoo.jiapeistudent.Data.ReturnData;
import com.guugoo.jiapeistudent.MainActivity.BaseActivity;
import com.guugoo.jiapeistudent.R;
import com.guugoo.jiapeistudent.Tools.DES;
import com.guugoo.jiapeistudent.Tools.MyThread;
import com.guugoo.jiapeistudent.Views.TitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LFeng on 2017/7/26.
 */

public class MyCouponActivity extends BaseActivity {

    private SharedPreferences sp;
    private List<CouponDetail> listData;
    private CouponAdapter adapter;

    @Override
    protected void processingData(ReturnData data) {
        listData.addAll(JSONArray.parseArray(data.getData(), CouponDetail.class));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_coupon);
    }

    @Override
    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.my_message_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setMiddleText(R.string.home_text8);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void findView() {
        ListView listView = (ListView) findViewById(R.id.coupon_listView);
        listData = new ArrayList<>();
        adapter = new CouponAdapter(this, listData);
        listView.setAdapter(adapter);
    }

    @Override
    protected void init() {
        sp = getSharedPreferences("user", MODE_PRIVATE);
        getMyCoupon();
    }

    private void getMyCoupon() {
        JSONObject json = new JSONObject(true);
        json.put("CarNO", sp.getString("CardNo", ""));
        Log.i("获取学时列表", json.toString());
        new MyThread(Constant.URL_S_UserInfoGetSubjectSumHours, handler, DES.encryptDES(json.toString())).start();
    }
}
