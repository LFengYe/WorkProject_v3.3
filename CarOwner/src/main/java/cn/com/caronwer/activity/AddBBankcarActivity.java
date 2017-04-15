package cn.com.caronwer.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import cn.com.caronwer.R;
import cn.com.caronwer.base.BaseActivity;
import cn.com.caronwer.base.Contants;
import cn.com.caronwer.util.EncryptUtil;
import cn.com.caronwer.util.HttpUtil;
import cn.com.caronwer.util.SPtils;
import cn.com.caronwer.util.VolleyInterface;

public class AddBBankcarActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private Button mBt_exit_tianjia;
    private EditText mEt_bankname;
    private EditText mEt_zhihang;
    private EditText mEt_zhanghu;
    private EditText mEt_kahao;
    private EditText mEt_tel;

    @Override
    protected int getLayout() {
        return R.layout.activity_add_bbankcar;
    }

    @Override
    protected void findById() {

        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);


        mBt_exit_tianjia = (Button) findViewById(R.id.bt_exit_tianjia);
        mEt_bankname = (EditText) findViewById(R.id.et_bankname);
        mEt_zhihang = (EditText) findViewById(R.id.et_zhihang);
        mEt_zhanghu = (EditText) findViewById(R.id.et_zhanghu);
        mEt_kahao = (EditText) findViewById(R.id.et_kahao);
        mEt_tel = (EditText) findViewById(R.id.et_tel);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        tv_title.setText("添加银行账号");
        tv_right.setVisibility(View.GONE);
        iv_left_white.setOnClickListener(this);
        mBt_exit_tianjia.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.iv_left_white:
                finish();
                break;
            case R.id.bt_exit_tianjia:
                tianJia();
                break;
        }
    }

    private void tianJia() {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("UserId", SPtils.getString(this, "UserId", "00000000-0000-0000-0000-000000000000"));
        jsonObject.addProperty("UserType", 2);
        jsonObject.addProperty("Tel", mEt_tel.getText().toString());
        jsonObject.addProperty("BankName",mEt_bankname.getText().toString());
        jsonObject.addProperty("BankBranch", mEt_zhihang.getText().toString());
        jsonObject.addProperty("AccountName",mEt_zhanghu.getText().toString());
        jsonObject.addProperty("AccountNumber", mEt_kahao.getText().toString());
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(AddBBankcarActivity.this, Contants.url_addbankcard, "addbankcard", map, new VolleyInterface(AddBBankcarActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                //showShortToastByString(result.toString());
                Toast.makeText(AddBBankcarActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(VolleyError error) {
                showShortToastByString(getString(R.string.timeoutError));
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
