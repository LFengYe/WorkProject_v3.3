package com.guugoo.jiapeiteacher.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.guugoo.jiapeiteacher.R;
import com.guugoo.jiapeiteacher.base.BaseActivity;
import com.guugoo.jiapeiteacher.base.Constants;
import com.guugoo.jiapeiteacher.bean.ScoreInfo;
import com.guugoo.jiapeiteacher.bean.TotalData;
import com.guugoo.jiapeiteacher.base.BaseAsyncTask;
import com.guugoo.jiapeiteacher.util.EncryptUtils;
import com.guugoo.jiapeiteacher.util.HttpUtil;
import com.guugoo.jiapeiteacher.util.NetUtil;
import com.guugoo.jiapeiteacher.view.CircleImageView;

/**
 * Created by gpw on 2016/8/10.
 * --加油
 */
public class MyScoreActivity extends BaseActivity {

    private CircleImageView civ_head;
    private TextView tv_nickName;
    private TextView tv_score;
    private TextView tv_teaching_ability;
    private TextView tv_teaching_attitude;
    private TextView tv_appearance;
    private TextView tv_car_condition;

    private RatingBar rb_teaching_ability;
    private RatingBar rb_teaching_attitude;
    private RatingBar rb_appearance;
    private RatingBar rb_car_condition;



    private String headImg;
    private String name;
    private String token;
    private int teacherId;

    @Override
    protected int getLayout() {
        return R.layout.activity_my_score;
    }

    @Override
    protected void initView() {
        civ_head = (CircleImageView) findViewById(R.id.civ_head);
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_nickName = (TextView) findViewById(R.id.tv_nickName);
        tv_score = (TextView) findViewById(R.id.tv_score);

        tv_teaching_ability = (TextView) findViewById(R.id.tv_teaching_ability);
        tv_teaching_attitude = (TextView) findViewById(R.id.tv_teaching_attitude);
        tv_appearance = (TextView) findViewById(R.id.tv_appearance);
        tv_car_condition = (TextView) findViewById(R.id.tv_car_condition);

        rb_teaching_ability = (RatingBar) findViewById(R.id.rb_teaching_ability);
        rb_teaching_attitude = (RatingBar) findViewById(R.id.rb_teaching_attitude);
        rb_appearance = (RatingBar) findViewById(R.id.rb_appearance);
        rb_car_condition = (RatingBar) findViewById(R.id.rb_car_condition);

        Glide.with(this)
                .load(headImg)
                .crossFade()
                .into(civ_head);

        tv_nickName.setText(name);
        assert iv_back != null;
        iv_back.setOnClickListener(this);
        if (!NetUtil.checkNetworkConnection(MyScoreActivity.this)){
            Toast.makeText(MyScoreActivity.this, R.string.Net_error, Toast.LENGTH_SHORT).show();
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("TeacherId", teacherId);
        jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
        new GetScoreAsyncTask(MyScoreActivity.this,HttpUtil.url_myScore,token).execute(jsonObject);

    }

    @Override
    protected void initData() {
//        headImg = getIntent().getStringExtra("headImg");
//        name = getIntent().getStringExtra("name");
//        teacherId = getIntent().getIntExtra("teacherId", 0);
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_NAME, MODE_PRIVATE);
        headImg = prefs.getString("Icon","");
        teacherId = prefs.getInt("Id",0);
        name = prefs.getString("NicKname","");
        token = prefs.getString("token","");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish();
        }
    }


    class GetScoreAsyncTask extends BaseAsyncTask {


        public GetScoreAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(MyScoreActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            System.out.println("totalData:"+s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                ScoreInfo ScoreInfo = gson.fromJson(totalData.getData(), ScoreInfo.class);
                tv_score.setText(ScoreInfo.getComprehensiveLevel()+"分");
                tv_teaching_attitude.setText(ScoreInfo.getAttitude()+"分");
                tv_appearance.setText(ScoreInfo.getAppearance()+"分");
                tv_car_condition.setText(ScoreInfo.getCarCondition()+"分");
                tv_teaching_ability.setText(ScoreInfo.getTechnology()+"分");
                rb_appearance.setRating(ScoreInfo.getAppearance());
                rb_teaching_attitude.setRating(ScoreInfo.getAttitude());
                rb_car_condition.setRating(ScoreInfo.getCarCondition());
                rb_teaching_ability.setRating(ScoreInfo.getTechnology());
            } else {
                Toast.makeText(MyScoreActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


}
