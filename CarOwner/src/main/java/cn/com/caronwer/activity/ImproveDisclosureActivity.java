package cn.com.caronwer.activity;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.caronwer.R;
import cn.com.caronwer.base.BaseActivity;
import cn.com.caronwer.base.Contants;
import cn.com.caronwer.bean.OrderAddressInfo;


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
    private Button bt_ok;
    private String userId;

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
    }

    @Override
    protected void initData() {
        pst = getIntent().getIntExtra("position", 0);
        type = getIntent().getIntExtra("type", 0);
        mOrderAddressInfo = getIntent().getParcelableExtra("orderAddressInfo");
        SharedPreferences prefs = getSharedPreferences(Contants.SHARED_NAME, MODE_PRIVATE);
        userId = prefs.getString("UserId","");

    }

    @Override
    protected void initView() {
        if (!mOrderAddressInfo.getReceiptAddress().equals("start")){
            et_contact_name.setText(mOrderAddressInfo.getReceipter());
            et_contact_tel.setText(mOrderAddressInfo.getReceiptTel());
        }
        tv_address.setText(mOrderAddressInfo.getReceiptAddress());
        switch (mOrderAddressInfo.getState()) {
            case 1:
                iv_address.setImageResource(R.mipmap.start);
                break;
            case 2:
                iv_address.setImageResource(R.mipmap.pass);
                break;
            case 3:
                iv_address.setImageResource(R.mipmap.arrive);
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
            case R.id.iv_left_black:
                finish();
                break;
            case R.id.ll_address:
                finish();
                break;
            case R.id.bt_ok:
                String name = et_contact_name.getText().toString();
                String tel = et_contact_tel.getText().toString();
                String loan_money = et_loan_money.getText().toString();
                System.out.println("loan_money"+loan_money);
                if (!loan_money.equals("")){
                    System.out.println("aaaaa");
                    double money = Double.valueOf(loan_money);
                    mOrderAddressInfo.setMoney(money);
                }
                mOrderAddressInfo.setReceipter(name);
                mOrderAddressInfo.setReceiptTel(tel);
                getIntent().putExtra("position", pst);
                getIntent().putExtra("orderAddressInfo", mOrderAddressInfo);
                getIntent().putExtra("type", type);
                setResult(RESULT_OK, getIntent());
                finish();
                break;
        }
    }
}
