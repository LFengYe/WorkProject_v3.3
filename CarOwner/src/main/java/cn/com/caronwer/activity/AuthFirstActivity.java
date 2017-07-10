package cn.com.caronwer.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import cn.com.caronwer.R;
import cn.com.caronwer.base.BaseActivity;
import cn.com.caronwer.base.Contants;
import cn.com.caronwer.util.EncryptUtil;
import cn.com.caronwer.util.HttpUtil;
import cn.com.caronwer.util.VolleyInterface;
import cn.com.caronwer.view.BorderTextView;
import cn.com.caronwer.widget.OnWheelChangedListener;
import cn.com.caronwer.widget.WheelView;
import cn.com.caronwer.widget.adapters.ArrayWheelAdapter;

/**
 * Created by LFeng on 2017/7/9.
 */

public class AuthFirstActivity extends BaseActivity implements OnWheelChangedListener {

    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;

    private BorderTextView carNumberSelect;
    private WheelView provinceView;
    private WheelView abcView;
    private String[] items = new String[]{"零担", "小面包车", "中面包车", "小型货车", "中型货车"};
    private int VehType = 0;

    @Override
    protected int getLayout() {
        return R.layout.activity_auth_first;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);

        carNumberSelect = (BorderTextView) findViewById(R.id.select_carNumber);
        carNumberSelect.setOnClickListener(this);

        BorderTextView button = (BorderTextView) findViewById(R.id.bv_next);
        button.setOnClickListener(this);

        Spinner mDdlCity = (Spinner) findViewById(R.id.ddlCity);
        ArrayAdapter<String> source = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        mDdlCity.setAdapter(source);

        mDdlCity.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                VehType = arg2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
    }

    @Override
    protected void initData() {
        getVehicleTypes();
    }

    @Override
    protected void initView() {
        tv_title.setText("车主认证");
        tv_right.setVisibility(View.GONE);
        iv_left_white.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_carNumber:
                showCarNumberSelectDialog();
                break;
            case R.id.bv_next:
                Intent intent = new Intent(AuthFirstActivity.this, AuthSecondActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_left_white:
                finish();
                break;
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
    }

    private void getVehicleTypes() {

        JsonObject mJsonObject = new JsonObject();
        Map<String, String> map = EncryptUtil.encryptDES(mJsonObject.toString());

        HttpUtil.doPost(AuthFirstActivity.this, Contants.url_getvehicletypes, "getvehicletypes", map, new VolleyInterface(AuthFirstActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
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

    private void showCarNumberSelectDialog() {
        final String[] provinces = getResources().getStringArray(R.array.province);
        final String[] abc = getResources().getStringArray(R.array.ABC);

        final Dialog bottomDialog = new Dialog(this, R.style.bottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.layout_car_number_select, null);
        provinceView = (WheelView) contentView.findViewById(R.id.id_province);
        provinceView.setViewAdapter(new ArrayWheelAdapter<>(this, provinces));
        provinceView.setCurrentItem(0);
        abcView = (WheelView) contentView.findViewById(R.id.id_abc);
        abcView.setViewAdapter(new ArrayWheelAdapter<>(this, abc));
        abcView.setCurrentItem(0);

        TextView confirm = (TextView) contentView.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String province = provinces[provinceView.getCurrentItem()];
                String selectChar = abc[abcView.getCurrentItem()];
                carNumberSelect.setText(province + selectChar);
                bottomDialog.dismiss();
            }
        });
        TextView cancel = (TextView) contentView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
            }
        });
        bottomDialog.setContentView(contentView);

        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.bottomDialog_Animation);
        bottomDialog.show();
    }
}
