package com.guugoo.jiapeiteacher.activity;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.guugoo.jiapeiteacher.R;

import com.guugoo.jiapeiteacher.base.BaseActivity;
import com.guugoo.jiapeiteacher.bean.StatuteDetailsInfo;

import com.guugoo.jiapeiteacher.bean.TotalData;
import com.guugoo.jiapeiteacher.base.BaseAsyncTask;
import com.guugoo.jiapeiteacher.util.EncryptUtils;
import com.guugoo.jiapeiteacher.util.HttpUtil;
import com.guugoo.jiapeiteacher.util.NetUtil;


/**
 * Created by gpw on 2016/8/10.
 * --加油
 */
public class LawsDetailsActivity extends BaseActivity {
    private TextView tv_title;
    private TextView tv_content;
    private int StatuteId;
    private String token;

    @Override
    protected int getLayout() {
        return R.layout.activity_laws_details;
    }

    @Override
    protected void initView() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.head);
        TextView tv_center = (TextView) rl_head.findViewById(R.id.tv_center);
        ImageView iv_back = (ImageView) rl_head.findViewById(R.id.iv_back);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);

        tv_center.setText(R.string.laws_details);

        iv_back.setOnClickListener(this);


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("StatuteId", StatuteId);
        jsonObject = EncryptUtils.encryptDES(jsonObject.toString());

        if (!NetUtil.checkNetworkConnection(LawsDetailsActivity.this)){
            Toast.makeText(LawsDetailsActivity.this, R.string.Net_error, Toast.LENGTH_SHORT).show();
            return;
        }
        new GetLawsDetailsAsyncTask(LawsDetailsActivity.this,HttpUtil.url_statuteDetails,token).execute(jsonObject);
    }

    @Override
    protected void initData() {
        StatuteId = getIntent().getIntExtra("StatuteId", 0);
        token = getIntent().getStringExtra("token");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish();
        }

    }


    class GetLawsDetailsAsyncTask extends BaseAsyncTask {


        public GetLawsDetailsAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(LawsDetailsActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            System.out.println(s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                StatuteDetailsInfo statuteDetailsInfo = gson.fromJson(totalData.getData(), StatuteDetailsInfo.class);
                tv_title.setText(statuteDetailsInfo.getTitle());
                tv_content.setText(statuteDetailsInfo.getNewContent());
            } else {
                Toast.makeText(LawsDetailsActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
