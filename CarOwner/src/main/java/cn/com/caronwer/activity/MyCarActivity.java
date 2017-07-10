package cn.com.caronwer.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import cn.com.caronwer.R;
import cn.com.caronwer.base.BaseActivity;
import cn.com.caronwer.base.Contants;
import cn.com.caronwer.bean.MyCarInfo;
import cn.com.caronwer.bean.UserInfo;
import cn.com.caronwer.util.EncryptUtil;
import cn.com.caronwer.util.HttpUtil;
import cn.com.caronwer.util.SPtils;
import cn.com.caronwer.util.VolleyInterface;

public class MyCarActivity extends BaseActivity {

    private UserInfo userInfo;
    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private Button mBt_huan;
    private ImageView mIv_car;
    private TextView mTv_cartype;
    private TextView mTv_carno;
    private TextView mTv_username;
    private MyCarInfo mMycar;
    private String[] items=new String[]{"零担","小面包车","中面包车","小型货车","中型货车"};

    @Override
    protected int getLayout() {
        return R.layout.activity_my_car;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);

        mIv_car = (ImageView) findViewById(R.id.iv_car);
        mBt_huan = (Button) findViewById(R.id.bt_huan);
        mTv_cartype = (TextView) findViewById(R.id.tv_cartype);
        mTv_carno = (TextView) findViewById(R.id.tv_carno);
        mTv_username = (TextView) findViewById(R.id.tv_username);
    }

    @Override
    protected void initData() {
        userInfo = getIntent().getParcelableExtra("userInfo");
    }

    @Override
    protected void initView() {
        tv_title.setText("我的车辆");
        tv_right.setVisibility(View.GONE);
        iv_left_white.setOnClickListener(this);
        mBt_huan.setOnClickListener(this);

        mTv_cartype.setText(userInfo.getVehicleTypeName());
        mTv_carno.setText(userInfo.getVehicleNo());
        mTv_username.setText(userInfo.getUserName());

        getDatafromNet();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_white:
                finish();
                break;
            case R.id.bt_huan://换车
                startActivity(new Intent(MyCarActivity.this,CertificationActivity.class));
                break;
        }
    }

    public void getDatafromNet() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", SPtils.getString(this, "UserId", "00000000-0000-0000-0000-000000000000"));
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(MyCarActivity.this, Contants.url_getvehMsg, "getvehMsg", map, new VolleyInterface(MyCarActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                Gson gson = new Gson();
                mMycar = gson.fromJson(result, MyCarInfo.class);

                mTv_cartype.setText(items[Integer.parseInt(mMycar.getVehicleType())]);
                mTv_carno.setText(mMycar.getVehicleNo());
                mTv_username.setText(mMycar.getName());

                HttpUtil.setImageLoader(Contants.imagehost + mMycar.getImages(),
                        mIv_car, R.mipmap.che, R.mipmap.che);

            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(MyCarActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStateError(int sta, String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    showShortToastByString(msg);
                }
            }
        });

    }
}
