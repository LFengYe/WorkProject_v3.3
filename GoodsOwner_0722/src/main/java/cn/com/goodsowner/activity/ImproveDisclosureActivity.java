package cn.com.goodsowner.activity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import cn.com.goodsowner.R;
import cn.com.goodsowner.base.BaseActivity;
import cn.com.goodsowner.base.Contants;
import cn.com.goodsowner.bean.OrderAddressInfo;
import cn.com.goodsowner.util.EncryptUtil;
import cn.com.goodsowner.util.HttpUtil;
import cn.com.goodsowner.util.LogUtil;
import cn.com.goodsowner.util.VolleyInterface;


public class ImproveDisclosureActivity extends BaseActivity {
    private int pst;
    private int type;
    private OrderAddressInfo mOrderAddressInfo;
    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private EditText et_contact_name;
    private EditText et_contact_tel;
    private EditText et_loan_money;
    private LinearLayout ll_address;
    private CheckBox cb_common_address;
    private ImageView iv_address;
    private TextView tv_address;
    private View view_line;
    private Button bt_ok;


    @Override
    protected int getLayout() {
        return R.layout.activity_improve_disclosure;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);
        et_contact_name = (EditText) findViewById(R.id.et_contact_name);
        et_contact_tel = (EditText) findViewById(R.id.et_contact_tel);
        et_loan_money = (EditText) findViewById(R.id.et_loan_money);
        ll_address = (LinearLayout) findViewById(R.id.ll_address);
        cb_common_address = (CheckBox) findViewById(R.id.cb_common_address);
        iv_address = (ImageView) findViewById(R.id.iv_address);
        tv_address = (TextView) findViewById(R.id.tv_address);
        bt_ok = (Button) findViewById(R.id.bt_ok);
        view_line = findViewById(R.id.view_line);
    }

    @Override
    protected void initData() {
        pst = getIntent().getIntExtra("position", 0);
        type = getIntent().getIntExtra("type", 0);
        mOrderAddressInfo = getIntent().getParcelableExtra("orderAddressInfo");
    }

    @Override
    protected void initView() {

        if (mOrderAddressInfo.getReceipter()!=null){
            et_contact_name.setText(mOrderAddressInfo.getReceipter());
        }
        if (mOrderAddressInfo.getReceiptTel()!=null){
            et_contact_tel.setText(mOrderAddressInfo.getReceiptTel());
        }
        if (mOrderAddressInfo.getMoney()!=0)
            et_loan_money.setText(String.format("%s", mOrderAddressInfo.getMoney()));

        tv_address.setText(mOrderAddressInfo.getReceiptAddress());
        switch (mOrderAddressInfo.getState()) {
            case 1:
                iv_address.setImageResource(R.mipmap.start);
                et_loan_money.setVisibility(View.GONE);
                view_line.setVisibility(View.GONE);
                break;
            case 2:
                iv_address.setImageResource(R.mipmap.pass);
                et_contact_name.setHint("请输入收货人姓名");
                et_contact_tel.setHint("请输入收货人电话");
                break;
            case 3:
                iv_address.setImageResource(R.mipmap.arrive);
                et_contact_name.setHint("请输入收货人姓名");
                et_contact_tel.setHint("请输入收货人电话");
                break;
        }
        tv_title.setText(R.string.improve_disclosure);
        tv_right.setVisibility(View.GONE);
        iv_left_white.setOnClickListener(this);
        ll_address.setOnClickListener(this);
        bt_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_white:
                finish();
                break;
            case R.id.ll_address:
                finish();
                break;
            case R.id.bt_ok:
                final String name = et_contact_name.getText().toString();
                final String tel = et_contact_tel.getText().toString();
                String loan_money = et_loan_money.getText().toString();
                System.out.println("loan_money" + loan_money);
                if (!loan_money.equals("")) {
                    double money = Double.valueOf(loan_money);
                    mOrderAddressInfo.setMoney(money);
                }
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("UserId", Contants.userId);
                jsonObject.addProperty("Address", mOrderAddressInfo.getReceiptAddress());
                jsonObject.addProperty("Contacts", name);
                jsonObject.addProperty("Tel", tel);
                jsonObject.addProperty("Lat", mOrderAddressInfo.getLat());
                jsonObject.addProperty("Lng", mOrderAddressInfo.getLng());
                mOrderAddressInfo.setReceipter(name);
                mOrderAddressInfo.setReceiptTel(tel);
                if (cb_common_address.isChecked()) {

                    LogUtil.i(jsonObject.toString());
                    Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
                    HttpUtil.doPost(ImproveDisclosureActivity.this, Contants.url_saveUserAddress, "saveUserAddress", map, new VolleyInterface(ImproveDisclosureActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                        @Override
                        public void onSuccess(JsonElement result) {
                            LogUtil.i(result.toString());
                            showShortToastByString("添加成功");
                            getIntent().putExtra("position", pst);
                            getIntent().putExtra("orderAddressInfo", mOrderAddressInfo);
                            getIntent().putExtra("type", type);
                            setResult(RESULT_OK, getIntent());
                            finish();

                        }

                        @Override
                        public void onError(VolleyError error) {
                            showShortToastByString(getString(R.string.timeoutError));
//                          LogUtil.i("hint",error.networkResponse.headers.toString());
//                          LogUtil.i("hint",error.networkResponse.statusCode+"");
                        }

                        @Override
                        public void onStateError() {

                        }
                    });
                } else {
                    getIntent().putExtra("position", pst);
                    getIntent().putExtra("orderAddressInfo", mOrderAddressInfo);
                    getIntent().putExtra("type", type);
                    setResult(RESULT_OK, getIntent());
                    finish();
                }
                break;
        }
    }
}
