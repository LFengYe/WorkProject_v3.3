package cn.com.goodsowner.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import cn.com.goodsowner.base.BaseActivity;
import cn.com.goodsowner.base.Contants;
import cn.com.goodsowner.bean.PremiumInfo;
import cn.com.goodsowner.util.EncryptUtil;
import cn.com.goodsowner.util.HttpUtil;
import cn.com.goodsowner.util.LogUtil;
import cn.com.goodsowner.util.VolleyInterface;

public class CargoInsuranceActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private Button bt_ok;
    private TextView tv_premiums_rule;
    private TextView tv_premiums;
    private EditText et_goodsValue;
    private double premium;
    private double goodsValue;
    private boolean isChange;

    @Override
    protected int getLayout() {
        return cn.com.goodsowner.R.layout.activity_cargo_insurance;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(cn.com.goodsowner.R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(cn.com.goodsowner.R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(cn.com.goodsowner.R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(cn.com.goodsowner.R.id.iv_left_white);

        bt_ok = (Button) findViewById(cn.com.goodsowner.R.id.bt_ok);
        tv_premiums_rule = (TextView) findViewById(cn.com.goodsowner.R.id.tv_premiums_rule);
        tv_premiums = (TextView) findViewById(cn.com.goodsowner.R.id.tv_premiums);
        et_goodsValue = (EditText) findViewById(cn.com.goodsowner.R.id.et_goodsValue);


    }

    @Override
    protected void initData() {
        isChange = false;
    }

    @Override
    protected void initView() {
        tv_right.setVisibility(View.GONE);
        tv_title.setText(cn.com.goodsowner.R.string.cargo_insurance);
        iv_left_white.setOnClickListener(this);
        bt_ok.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case cn.com.goodsowner.R.id.iv_left_white:
                if (isChange) {
                    getIntent().putExtra("premium", premium);
                    getIntent().putExtra("goods", goodsValue);
                    setResult(RESULT_OK, getIntent());

                }
                finish();
                break;
            case cn.com.goodsowner.R.id.bt_ok:
                calculatedPremium();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isChange) {
            getIntent().putExtra("premium", premium);
            getIntent().putExtra("goods", goodsValue);
            setResult(RESULT_OK, getIntent());
        }
        super.onBackPressed();
    }

    private void calculatedPremium() {
        String value = et_goodsValue.getText().toString();
        if (value.isEmpty()) {
            showShortToastByString("货物价值不能为空");
            return;
        }
        goodsValue = Double.valueOf(value);
        if (goodsValue <= 10000) {
            showShortToastByString("货物价值小于10000");
            return;
        }


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("GoodsValue", goodsValue);
        LogUtil.i(jsonObject.toString());
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(CargoInsuranceActivity.this, Contants.url_calculatedPremium, "calculatedPremium", map, new VolleyInterface(CargoInsuranceActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                LogUtil.i(result.toString());
                Gson gson = new Gson();
                PremiumInfo premiumInfo = gson.fromJson(result, PremiumInfo.class);
                isChange = true;
                tv_premiums.setText(String.format("%s  元", premiumInfo.getPremium()));
                premium = premiumInfo.getPremium();
            }

            @Override
            public void onError(VolleyError error) {
                showShortToastByString(getString(cn.com.goodsowner.R.string.timeoutError));
//                LogUtil.i("hint",error.networkResponse.headers.toString());
//                LogUtil.i("hint",error.networkResponse.statusCode+"");
            }

            @Override
            public void onStateError() {
            }
        });
    }
}
