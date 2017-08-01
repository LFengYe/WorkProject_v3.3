package cn.com.caronwer.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import cn.com.caronwer.bean.VehicleAuth1;
import cn.com.caronwer.bean.VehicleType;
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

    private EditText name;
    private RadioGroup sexGroup;
    private EditText cardNo;
    private EditText phone;
    private EditText drivingLicense;
    private EditText roadTransportPermit;
    private EditText gpsSystemNo;
    private BorderTextView carNumberSelect;
    private EditText carNumber;
    private Spinner vehicleType;
    private EditText vehicleWidth;
    private EditText vehicleHeight;
    private EditText vehicleLength;
    private EditText vehicleMaxCapacity;
    private EditText urgentContact;
    private EditText phoneVerify;

    private BorderTextView getVerify;
    private boolean isGetVerify;
    private WheelView provinceView;
    private WheelView abcView;
    private int VehType = -1;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    msg.arg1--;
                    String timeStr = getResources().getString(R.string.get_code_time);
                    getVerify.setText(String.format(timeStr, msg.arg1));
                    if (msg.arg1 > 0) {
                        getVerify.setClickable(false);
                        Message message = handler.obtainMessage(1);
                        message.arg1 = msg.arg1;
                        handler.sendMessageDelayed(message, 1000);      // send message
                    } else {
                        getVerify.setClickable(true);
                        getVerify.setText(getResources().getString(R.string.get_validate_code));
                    }
            }
            super.handleMessage(msg);
        }
    };

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

        name = (EditText) findViewById(R.id.et_name);
        sexGroup = (RadioGroup) findViewById(R.id.sex_select);
        cardNo = (EditText) findViewById(R.id.et_cardNo);
        phone = (EditText) findViewById(R.id.et_phone);
        drivingLicense = (EditText) findViewById(R.id.et_drivingLicense);
        roadTransportPermit = (EditText) findViewById(R.id.et_road_transport_permit);
        gpsSystemNo = (EditText) findViewById(R.id.et_gps_system_no);
        carNumberSelect = (BorderTextView) findViewById(R.id.select_carNumber);
        carNumberSelect.setOnClickListener(this);
        carNumber = (EditText) findViewById(R.id.et_carNumber);
        vehicleType = (Spinner) findViewById(R.id.vehicleType);
        vehicleWidth = (EditText) findViewById(R.id.et_vehicle_width);
        vehicleLength = (EditText) findViewById(R.id.et_vehicle_length);
        vehicleHeight = (EditText) findViewById(R.id.et_vehicle_height);
        vehicleMaxCapacity = (EditText) findViewById(R.id.et_vehicleMaxCapacity);
        urgentContact = (EditText) findViewById(R.id.et_urgentContact);
        phoneVerify = (EditText) findViewById(R.id.et_phoneVerify);
        getVerify = (BorderTextView) findViewById(R.id.get_verify);
        getVerify.setOnClickListener(this);

        BorderTextView button = (BorderTextView) findViewById(R.id.bv_next);
        button.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        getVehicleTypes();
        getAuthInfo();
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
            case R.id.get_verify:
                getCheckCode();
                break;
            case R.id.bv_next:
                if (!uploadAuthInfo()) {
                    showShortToastByString("信息填写不完整");
                }
                break;
            case R.id.iv_left_white:
                finish();
                break;
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
    }

    private boolean uploadAuthInfo() {
        SharedPreferences prefs = getSharedPreferences(Contants.SHARED_NAME, MODE_PRIVATE);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", prefs.getString("UserId", ""));

        String userName = name.getText().toString();
        if (TextUtils.isEmpty(userName))
            return false;
        jsonObject.addProperty("UserName", userName);

        RadioButton radioButton = (RadioButton) findViewById(sexGroup.getCheckedRadioButtonId());
        if (radioButton == null)
            return false;
        String sex = radioButton.getText().toString();
        if (TextUtils.isEmpty(sex))
            return false;
        jsonObject.addProperty("Sex", sex);

        String idNumber = cardNo.getText().toString();
        if (TextUtils.isEmpty(idNumber))
            return false;
        jsonObject.addProperty("IDNumber", idNumber);

        String phoneStr = phone.getText().toString();
        if (TextUtils.isEmpty(phoneStr))
            return false;
        jsonObject.addProperty("Phone", phoneStr);

        String driverId = drivingLicense.getText().toString();
        if (TextUtils.isEmpty(driverId))
            return false;
        jsonObject.addProperty("DriverId", driverId);

        final String vehicleNoSelect = carNumberSelect.getText().toString();
        final String vehicleNo = carNumber.getText().toString();
        if (TextUtils.isEmpty(vehicleNoSelect) || TextUtils.isEmpty(vehicleNo))
            return false;
        jsonObject.addProperty("VehicleNo", vehicleNoSelect + vehicleNo);

        String travelCard = roadTransportPermit.getText().toString();
        if (TextUtils.isEmpty(travelCard))
            return false;
        jsonObject.addProperty("TravelCard", travelCard);

        String gpsNo = gpsSystemNo.getText().toString();
        if (TextUtils.isEmpty(gpsNo))
            return false;
        jsonObject.addProperty("GpsNo", gpsNo);

        if (VehType == -1)
            return false;
        jsonObject.addProperty("VehType", VehType);

        String phone1 = urgentContact.getText().toString();
        if (TextUtils.isEmpty(phone1))
            return false;
        jsonObject.addProperty("Phone1", phone1);

        String width = vehicleWidth.getText().toString();
        if (TextUtils.isEmpty(width))
            return false;
        jsonObject.addProperty("Width", width);

        String height = vehicleHeight.getText().toString();
        if (TextUtils.isEmpty(height))
            return false;
        jsonObject.addProperty("Height", height);

        String length = vehicleLength.getText().toString();
        if (TextUtils.isEmpty(length))
            return false;
        jsonObject.addProperty("Length", length);

        String tons = vehicleMaxCapacity.getText().toString();
        if (TextUtils.isEmpty(tons))
            return false;
        jsonObject.addProperty("Tons", tons);

        String checkCode = phoneVerify.getText().toString();
        if (TextUtils.isEmpty(checkCode))
            return false;
        jsonObject.addProperty("CheckCode", checkCode);

        if (isGetVerify) {
            Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
            HttpUtil.doPost(AuthFirstActivity.this, Contants.url_TransporterVehicleCheck1, "VehicleCheck1", map, new VolleyInterface(AuthFirstActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                @Override
                public void onSuccess(JsonElement result) {
                    Intent intent = new Intent(AuthFirstActivity.this, AuthSecondActivity.class);
                    //intent.putExtra("VehicleNo", vehicleNoSelect + vehicleNo);
                    startActivity(intent);
                    finish();
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

            return true;
        } else {
            showShortToastByString("请先获取验证码");
            return false;
        }
    }

    private void getAuthInfo() {
        SharedPreferences prefs = getSharedPreferences(Contants.SHARED_NAME, MODE_PRIVATE);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserID", prefs.getString("UserId", ""));

        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(AuthFirstActivity.this, Contants.url_TransporterGetVehicleCheck1, "GetVehicleCheck1", map,
                new VolleyInterface(AuthFirstActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                Gson gson = new Gson();
                VehicleAuth1 vehicleAuth = gson.fromJson(result, VehicleAuth1.class);
                setAuthInfo(vehicleAuth);
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

    private void setAuthInfo(VehicleAuth1 vehicleAuth) {
        name.setText(vehicleAuth.getUserName());

        if (vehicleAuth.getSex().compareTo("男") == 0)
            sexGroup.check(R.id.male);
        if (vehicleAuth.getSex().compareTo("女") == 0)
            sexGroup.check(R.id.female);

        cardNo.setText(vehicleAuth.getIDNumber());
        phone.setText(vehicleAuth.getPhone());
        drivingLicense.setText(vehicleAuth.getDriverId());
        carNumberSelect.setText(vehicleAuth.getVehicleNo().substring(0, 2));
        carNumber.setText(vehicleAuth.getVehicleNo().substring(2));
        roadTransportPermit.setText(vehicleAuth.getTravelCard());
        gpsSystemNo.setText(vehicleAuth.getGpsNo());
        vehicleType.setSelection(Integer.valueOf(vehicleAuth.getVehType()) - 1);
        vehicleWidth.setText(String.valueOf(vehicleAuth.getWidth()));
        vehicleHeight.setText(String.valueOf(vehicleAuth.getHeight()));
        vehicleLength.setText(String.valueOf(vehicleAuth.getLength()));
        vehicleMaxCapacity.setText(String.valueOf(vehicleAuth.getTons()));
        urgentContact.setText(vehicleAuth.getPhone1());
    }

    private void getCheckCode() {
        String account = urgentContact.getText().toString();
        if (account.isEmpty()) {
            showShortToastByString("紧急联系人号码未填写");
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Tel", account);
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(AuthFirstActivity.this, Contants.url_obtainCheckCode, "obtainCheckCode", map,
                new VolleyInterface(AuthFirstActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                isGetVerify = true;
                Toast.makeText(AuthFirstActivity.this, "获取验证码成功", Toast.LENGTH_SHORT).show();

                Message message = handler.obtainMessage(1);     // Message
                message.arg1 = 120;
                handler.sendMessageDelayed(message, 1000); //倒计时
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(AuthFirstActivity.this,
                        "获取失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStateError(int sta, String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    showShortToastByString(msg);
                }
            }
        });
    }

    private void getVehicleTypes() {

        JsonObject mJsonObject = new JsonObject();
        Map<String, String> map = EncryptUtil.encryptDES(mJsonObject.toString());

        HttpUtil.doPost(AuthFirstActivity.this, Contants.url_getvehicletypes, "getvehicletypes", map,
                new VolleyInterface(AuthFirstActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<VehicleType>>() {}.getType();
                final ArrayList<VehicleType> vehicleTypes = gson.fromJson(
                        result.getAsJsonObject().get("VehicleTypeList"), listType);
                ArrayAdapter<VehicleType> source = new ArrayAdapter<>(AuthFirstActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, vehicleTypes);
                vehicleType.setAdapter(source);
                vehicleType.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        VehType = vehicleTypes.get(arg2).getTypeCode();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }

                });
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
