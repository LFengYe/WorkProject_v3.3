package cn.com.caronwer.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import cn.com.caronwer.R;
import cn.com.caronwer.base.BaseActivity;
import cn.com.caronwer.base.Contants;
import cn.com.caronwer.bean.NewsInfo;
import cn.com.caronwer.bean.UserInfo;
import cn.com.caronwer.util.EncryptUtil;
import cn.com.caronwer.util.HttpUtil;
import cn.com.caronwer.util.LogUtil;
import cn.com.caronwer.util.NetworkUtil;
import cn.com.caronwer.util.SPtils;
import cn.com.caronwer.util.VolleyInterface;
import cn.com.caronwer.view.CircleImageView;

/**
 * Created by LFeng on 2017/7/11.
 */

public class PersonalCenterActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;

    private CircleImageView headImg;
    private TextView userName;
    private TextView tel;
    private TextView authStatus;
    //private TextView newsNum;
    private TextView score;
    private RatingBar scoreRat;
    private UserInfo userInfo;

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_personal_center;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);

        findViewById(R.id.civ_head).setOnClickListener(this);
        findViewById(R.id.tv_auth).setOnClickListener(this);
        findViewById(R.id.tv_myOrder).setOnClickListener(this);
        findViewById(R.id.tv_myWallet).setOnClickListener(this);
        findViewById(R.id.tv_myCar).setOnClickListener(this);
        findViewById(R.id.tv_news).setOnClickListener(this);
        findViewById(R.id.tv_benefit).setOnClickListener(this);
        findViewById(R.id.tv_setting).setOnClickListener(this);

        headImg = (CircleImageView) findViewById(R.id.civ_head);
        tel = (TextView) findViewById(R.id.tv_tel);
        userName = (TextView) findViewById(R.id.tv_mz);
        //newsNum = (TextView) findViewById(R.id.tv_news_num);
        score = (TextView) findViewById(R.id.tv_fenshu);
        scoreRat = (RatingBar) findViewById(R.id.rb_leftdata);
        authStatus = (TextView) findViewById(R.id.tv_auth);
    }

    @Override
    protected void initData() {
        userInfo = getIntent().getParcelableExtra("userInfo");
        //getNews(1);
    }

    @Override
    protected void initView() {
        tv_title.setText("个人中心");
        tv_right.setVisibility(View.GONE);
        iv_left_white.setOnClickListener(this);

        tel.setText(userInfo.getTel());
        userName.setText(userInfo.getUserName());
        score.setText(userInfo.getScore() + "分");
        scoreRat.setRating(userInfo.getScore());

        int authenticateStatus = userInfo.getAuthenticateStatus();
        switch (authenticateStatus) {
            case 1:
                authStatus.setText("未认证");
                break;
            case 2:
                authStatus.setText("审核中");
                authStatus.setClickable(false);
                break;
            case 3:
                authStatus.setText("通过认证");
                authStatus.setClickable(false);
                break;
            case 4:
                authStatus.setText("审核失败");
                break;

        }

        HttpUtil.setImageLoader(Contants.imagehost + userInfo.getHeadIco(), headImg, R.mipmap.cir_head, R.mipmap.cir_head);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_left_white:
                finish();
                break;
            case R.id.tv_myOrder://我的订单
                intent = new Intent(PersonalCenterActivity.this, MyOrderActivityMe.class);
                intent.putExtra("selectNo", "1");
                startActivity(intent);
                break;
            case R.id.tv_myWallet://我的钱包-
                intent = new Intent(PersonalCenterActivity.this, MyWalletActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_myCar://我的车辆
                intent = new Intent(PersonalCenterActivity.this, MyCarActivity.class);
                intent.putExtra("userInfo", userInfo);
                startActivityForResult(intent, 3);
                break;
            case R.id.tv_news://消息通知
                intent = new Intent(PersonalCenterActivity.this, MyNewsActivity.class);
                startActivityForResult(intent, 10);
                break;
            case R.id.tv_benefit://优惠活动
                intent = new Intent(PersonalCenterActivity.this, BenefitActivityActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_setting://设置
                intent = new Intent(PersonalCenterActivity.this, SettingActivity.class);
                intent.putExtra("userInfo", userInfo);
                startActivityForResult(intent, 3);
                break;
            case R.id.civ_head:   //个人信息
                intent = new Intent(PersonalCenterActivity.this, PersonalInfoActivity.class);
                intent.putExtra("userInfo", userInfo);
                startActivityForResult(intent, 3);
                break;
            case R.id.tv_auth:    //c车主认证
                if (userInfo.getAuthenticateStatus() == 1
                        || userInfo.getAuthenticateStatus() == 4) {
                    intent = new Intent(PersonalCenterActivity.this, AuthFirstActivity.class);
                    startActivity(intent);
                } else if (userInfo.getAuthenticateStatus() == 2) {
                    Toast.makeText(this, R.string.authenticate_already_upload, Toast.LENGTH_SHORT).show();
                } else if (userInfo.getAuthenticateStatus() == 3){
                    Toast.makeText(this, R.string.authenticate_already_success, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10: {
                //newsNum.setVisibility(View.INVISIBLE);
                break;
            }
        }
    }

    private void getNews(int PageIndex) {
        if (!NetworkUtil.isConnected(PersonalCenterActivity.this)) {
            showShortToastByString(getString(R.string.Neterror));
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", SPtils.getString(this, "UserId", "00000000-0000-0000-0000-000000000000"));
        jsonObject.addProperty("UserType", 2);
        jsonObject.addProperty("PageIndex", PageIndex);
        jsonObject.addProperty("PageSize", 15);
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(PersonalCenterActivity.this, Contants.url_getUserMessages, "getUserMessages", map,
                new VolleyInterface(PersonalCenterActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<NewsInfo>>() {
                }.getType();
                ArrayList<NewsInfo> newNewsInfo = gson.fromJson(result, listType);
            }

            @Override
            public void onError(VolleyError error) {

            }

            @Override
            public void onStateError(int sta, String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    showShortToastByString(msg);
                }
            }
        });
    }

    private void getUserInfo() {
        if (!NetworkUtil.isConnected(PersonalCenterActivity.this)) {
            showShortToastByString(getString(R.string.Neterror));
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", SPtils.getString(this, "UserId", ""));
        jsonObject.addProperty("UserType", 2);
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(PersonalCenterActivity.this, Contants.url_getUserInfo, "GetUserInfo", map,
                new VolleyInterface(PersonalCenterActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                    @Override
                    public void onSuccess(JsonElement result) {
                        Gson gson = new Gson();
                        userInfo = gson.fromJson(result, UserInfo.class);
                        SPtils.putString(PersonalCenterActivity.this, "UserId", userInfo.getUserId());
                        SPtils.putString(PersonalCenterActivity.this, "VehicleNo", userInfo.getVehicleNo());
                        SPtils.putString(PersonalCenterActivity.this, "CompanyTel", userInfo.getCompanyTel());

                        initView();
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }

                    @Override
                    public void onStateError(int sta, String msg) {

                    }
                });
    }
}
