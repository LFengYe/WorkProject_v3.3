package cn.com.goodsowner.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map;
import cn.com.goodsowner.R;
import cn.com.goodsowner.adapter.AddreAdapter;
import cn.com.goodsowner.base.BaseActivity;
import cn.com.goodsowner.base.Contants;
import cn.com.goodsowner.bean.GoodsInfo;
import cn.com.goodsowner.util.EncryptUtil;
import cn.com.goodsowner.util.HttpUtil;
import cn.com.goodsowner.util.LogUtil;
import cn.com.goodsowner.util.VolleyInterface;
import cn.com.goodsowner.view.CustomLinearLayoutManager;
import cn.com.goodsowner.view.CustomProgressDialog;

public class ConfirmActivity extends BaseActivity {

    private TextView tv_goods_type;
    private TextView tv_goods_name;
    private TextView tv_goods_pack;
    private TextView tv_goods_volume;
    private TextView tv_goods_kg;
    private TextView tv_goods_time;
    private TextView tv_remark;
    private TextView tv_money;
    private TextView tv_yuan;
    private RadioButton rb_have;
    private RadioButton rb_no;
    private Button bt_ok;
    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private RecyclerView rv_ad;

    private GoodsInfo mGoodsInfo;
    private String money;
    private int orderType;
    private int carType;
    private boolean isToPayFreight;
    private String mapJson;
    private CustomProgressDialog customProgressDialog;


    @Override
    protected int getLayout() {
        return R.layout.activity_confirm;
    }

    @Override
    protected void findById() {

        RelativeLayout rl_head = (RelativeLayout) findViewById(cn.com.goodsowner.R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(cn.com.goodsowner.R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(cn.com.goodsowner.R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(cn.com.goodsowner.R.id.iv_left_white);


        tv_goods_type = (TextView) findViewById(R.id.tv_goods_type);
        tv_goods_name = (TextView) findViewById(R.id.tv_goods_name);
        tv_goods_pack = (TextView) findViewById(R.id.tv_goods_pack);
        tv_goods_volume = (TextView) findViewById(R.id.tv_goods_volume);
        tv_goods_kg = (TextView) findViewById(R.id.tv_goods_kg);
        tv_goods_time = (TextView) findViewById(R.id.tv_goods_time);
        tv_remark = (TextView) findViewById(R.id.tv_remark);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_yuan = (TextView) findViewById(R.id.tv_yuan);

        bt_ok = (Button) findViewById(R.id.bt_ok);
        rb_have = (RadioButton) findViewById(R.id.rb_have);
        rb_no = (RadioButton) findViewById(R.id.rb_no);

        rv_ad = (RecyclerView) findViewById(R.id.rv_ad);

    }

    @Override
    protected void initData() {

        mGoodsInfo = getIntent().getParcelableExtra("goodsInfo");
        money = getIntent().getStringExtra("money");
        orderType = getIntent().getIntExtra("type", 0);
        carType = getIntent().getIntExtra("carType", 0);
        isToPayFreight = getIntent().getBooleanExtra("IsToPayFreight", false);
        mapJson = getIntent().getStringExtra("mapJson");
    }

    @Override
    protected void initView() {

        AddreAdapter adAdapter = new AddreAdapter(mGoodsInfo.getAddres());
        CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setScrollEnabled(false);
        rv_ad.setLayoutManager(layoutManager);
        rv_ad.setAdapter(adAdapter);

        customProgressDialog = new CustomProgressDialog(ConfirmActivity.this);
        tv_right.setVisibility(View.GONE);
        tv_title.setText(R.string.confirm_order);
        iv_left_white.setOnClickListener(this);
        tv_goods_kg.setText(mGoodsInfo.getKg());
        tv_goods_volume.setText(mGoodsInfo.getVolume());
        tv_goods_type.setText(mGoodsInfo.getType());
        tv_goods_pack.setText(mGoodsInfo.getPack());
        tv_goods_name.setText(mGoodsInfo.getName());
        tv_goods_time.setText(mGoodsInfo.getTime());
        tv_remark.setText(String.format("备注:%s", mGoodsInfo.getRemark()));
        if (orderType==3) {
            tv_money.setText("询价");
            tv_yuan.setVisibility(View.INVISIBLE);
        }else {
            tv_money.setText(money);
        }

        if (mGoodsInfo.isMove()) {
            rb_have.setChecked(true);
        } else {
            rb_no.setChecked(true);
        }

        iv_left_white.setOnClickListener(this);
        bt_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_white:
                finish();
                break;
            case R.id.bt_ok:
                JsonObject jsonObject = new JsonParser().parse(mapJson).getAsJsonObject();
                if (orderType == 3) {
                    jsonObject.addProperty("PayWay", 2);
                    customProgressDialog.show();
                    customProgressDialog.setText("发布中..");
                    Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
                    if (carType == 2) {
                        HttpUtil.doPost(ConfirmActivity.this, Contants.url_publishCarpool, "publishCarpool", map, new VolleyInterface(ConfirmActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                            @Override
                            public void onSuccess(JsonElement result) {
                                LogUtil.i(result.toString());
                                customProgressDialog.dismiss();
                                showShortToastByString("货源发布成功，可查看我的订单");
                                setResult(RESULT_OK);
                                finish();
                            }

                            @Override
                            public void onError(VolleyError error) {
                                customProgressDialog.dismiss();
                                showShortToastByString(getString(R.string.timeoutError));

                            }

                            @Override
                            public void onStateError() {
                                customProgressDialog.dismiss();
                            }
                        });
                    } else {
                        HttpUtil.doPost(ConfirmActivity.this, Contants.url_sendOrder, "sendOrder", map, new VolleyInterface(ConfirmActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                            @Override
                            public void onSuccess(JsonElement result) {
                                LogUtil.i(result.toString());
                                customProgressDialog.dismiss();
                                showShortToastByString("货源发布成功，可查看我的订单");
                                setResult(RESULT_OK);
                                finish();
                            }

                            @Override
                            public void onError(VolleyError error) {
                                customProgressDialog.dismiss();
                                showShortToastByString(getString(R.string.timeoutError));

                            }

                            @Override
                            public void onStateError() {
                                customProgressDialog.dismiss();
                            }
                        });
                    }
                } else {
                    if (isToPayFreight) {
                        jsonObject.addProperty("PayWay", 0);
                        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
                        customProgressDialog.show();
                        customProgressDialog.setText("发布中..");
                        if (carType == 1) {
                            HttpUtil.doPost(ConfirmActivity.this, Contants.url_sendOrder, "sendOrder", map, new VolleyInterface(ConfirmActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                                @Override
                                public void onSuccess(JsonElement result) {
                                    LogUtil.i(result.toString());
                                    customProgressDialog.dismiss();
                                    showShortToastByString("货源发布成功，可查看我的订单");
                                    setResult(RESULT_OK, getIntent());
                                    finish();
                                }

                                @Override
                                public void onError(VolleyError error) {
                                    showShortToastByString(getString(R.string.timeoutError));
                                    customProgressDialog.dismiss();
                                }

                                @Override
                                public void onStateError() {
                                    customProgressDialog.dismiss();
                                }
                            });
                        } else if (carType == 2) {
                            HttpUtil.doPost(ConfirmActivity.this, Contants.url_publishCarpool, "publishCarpool", map, new VolleyInterface(ConfirmActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                                @Override
                                public void onSuccess(JsonElement result) {
                                    LogUtil.i(result.toString());
                                    showShortToastByString("货源发布成功，可查看我的订单");
                                    setResult(RESULT_OK, getIntent());
                                    finish();
                                }

                                @Override
                                public void onError(VolleyError error) {
                                    customProgressDialog.dismiss();
                                    showShortToastByString(getString(R.string.timeoutError));
                                }

                                @Override
                                public void onStateError() {
                                    customProgressDialog.dismiss();
                                }
                            });
                        }
                    } else {
                        Intent intent = new Intent(ConfirmActivity.this, OrderPay1Activity.class);
                        intent.putExtra("mapJson", mapJson);
                        intent.putExtra("carType", carType);
                        intent.putExtra("money", money);
                        startActivityForResult(intent, 1);

                    }

                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }
}
