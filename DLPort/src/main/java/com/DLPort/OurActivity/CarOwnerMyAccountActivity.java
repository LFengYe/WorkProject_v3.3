package com.DLPort.OurActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.BaseActivity;
import com.DLPort.mydata.CarInfo;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;
import com.DLPort.myview.MyDialog;
import com.DLPort.myview.TitleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.DLPort.R.id.insurance_date_layout;

/**
 * Created by fuyzh on 16/5/18.
 */
public class CarOwnerMyAccountActivity extends BaseActivity {
    private static final String TAG = "MyAccountCarOwner";

    private RelativeLayout carNumberLayout;
    private RelativeLayout gpsNumberLayout;
    private RelativeLayout boxTypeLayout;
    private RelativeLayout maintainDateLayout;
    private RelativeLayout insuranceDateLayout;
    private RelativeLayout gpsExpiredDateLayout;

    private TextView username;
    private TextView contact;
    private TextView phoneNumber;
    private TextView address;
    private TextView carNumber;
    private TextView gpsNumber;
    private TextView boxType;
    private TextView maintainDate;
    private TextView insuranceDate;
    private TextView gpsExpiredDate;

    private ArrayList<CarInfo> carInfoList;
    private String[] carNumberList;
    private int curCarId;
    private int selectedPosition = 0;

    private Handler handler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    JSONObject jsonUser = null;
                    jsonUser = new JSONObject((String) msg.obj);
                    Log.d(TAG, "响应数据:" + jsonUser.toString());
                    int status = jsonUser.getInt("Status");
                    if (0 == status) {
                        progressListData(jsonUser.get("Data").toString());
                    } else if (1 == status || -1 == status) {
                        MyToast.makeText(CarOwnerMyAccountActivity.this, jsonUser.getString("Message"));
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            } else if (msg.what == 2) {
                Log.i(TAG, "响应不正常");

                MyToast.makeText(CarOwnerMyAccountActivity.this, "服务器异常");
            } else if (msg.what == 3) {
                // 编码异常
                Log.i(TAG, "编码异常");
            } else if (msg.what == 4) {
                // ClientProtocolException
                Log.i(TAG, "ClientProtocolException");
            } else if (msg.what == 5) {
                // IO异常
                Log.i(TAG, "IO异常");
                MyToast.makeText(CarOwnerMyAccountActivity.this, "服务器连接异常");
            }
        }

        private void progressListData(String jsonData) {
            try {
                JSONObject object = new JSONObject(jsonData);
                username.setText(object.getString("LoginName"));
                contact.setText(object.getString("AccountName"));
                phoneNumber.setText(object.getString("Telephoen"));
                address.setText(object.getString("Address"));

                carInfoList = new ArrayList<>();
                JSONArray array = new JSONArray(object.getString("Cars"));
                carNumberList = new String[array.length()];
                for (int i = 0; i < array.length(); i++) {
                    JSONObject tmp = new JSONObject(array.getString(i));
                    CarInfo carInfo = new CarInfo();
                    carInfo.setCarId(tmp.getInt("CarId"));
                    carInfo.setCarOwnerId(tmp.getString("CarOwnerId"));
                    carInfo.setVehNof(tmp.getString("VehNof"));
                    carInfo.setCarType(tmp.getString("CarType"));
                    carInfo.setUpKeepTime(tmp.getString("UpkeepTime").replace("T", " "));
                    carInfo.setInsuranceTime(tmp.getString("InsuranceTime").replace("T", " "));
                    carInfo.setGpsExpiredTime(tmp.getString("GpsExpire").replace("T", " "));
                    carInfo.setGpsNumber(tmp.getString("GpsNo"));
                    carInfo.setBoxType(GlobalParams.GetContainerType(tmp.getInt("BoxType")));

                    carInfoList.add(carInfo);
                    carNumberList[i] = carInfo.getVehNof();
                }

                if (carInfoList.size() > selectedPosition) {
                    CarInfo carInfo = carInfoList.get(selectedPosition);
                    displayCarInfo(carInfo);
                } else {
                    CarInfo carInfo = carInfoList.get(0);
                    displayCarInfo(carInfo);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Handler editHandler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    JSONObject object = new JSONObject((String) msg.obj);
                    Log.i(TAG, object.toString());
                    MyToast.makeText(CarOwnerMyAccountActivity.this, object.getString("Message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    //region 页面点击事件
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.gps_number_layout: {
                    MyDialog dialog = new MyDialog(CarOwnerMyAccountActivity.this, 1);
                    dialog.setContent(getString(R.string.account_gps_number));
                    dialog.sethineText(getString(R.string.account_gps_number));
                    dialog.setDialogCallback(new MyDialog.Dialogcallback() {
                        @Override
                        public void dialogdo(String string) {
                            try {
                                gpsNumber.setText(string);
                                JSONObject object = new JSONObject();
                                object.put("GpsNo", string);
                                editCarInfo(object);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    dialog.show();
                    break;
                }
                case R.id.box_type_layout: {
                    MyDialog dialog = new MyDialog(CarOwnerMyAccountActivity.this, 3);
                    dialog.setContent(getString(R.string.account_box_type));
                    dialog.sethineText(getString(R.string.account_box_type));
                    dialog.setDialogCallback(new MyDialog.Dialogcallback() {
                        @Override
                        public void dialogdo(String string) {
                            try {
                                boxType.setText(string);
                                JSONObject object = new JSONObject();
                                object.put("BoxType", string);
                                editCarInfo(object);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    dialog.show();
                    break;
                }
                case R.id.maintain_date_layout: {MyDialog dialog = new MyDialog(CarOwnerMyAccountActivity.this, 2);
                    dialog.setContent(getString(R.string.account_maintain_date));
                    dialog.sethineText(getString(R.string.account_maintain_date));
                    dialog.setDialogCallback(new MyDialog.Dialogcallback() {
                        @Override
                        public void dialogdo(String string) {
                            try {
                                maintainDate.setText(string);
                                JSONObject object = new JSONObject();
                                object.put("UpkeepTime", string);
                                editCarInfo(object);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    dialog.show();
                    break;
                }
                case R.id.insurance_date_layout: {
                    MyDialog dialog = new MyDialog(CarOwnerMyAccountActivity.this, 2);
                    dialog.setContent(getString(R.string.account_insurance_date));
                    dialog.sethineText(getString(R.string.account_insurance_date));
                    dialog.setDialogCallback(new MyDialog.Dialogcallback() {
                        @Override
                        public void dialogdo(String string) {
                            try {
                                insuranceDate.setText(string);
                                JSONObject object = new JSONObject();
                                object.put("InsuranceTime", string);
                                editCarInfo(object);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    dialog.show();
                    break;
                }
                case R.id.gps_expired_date_layout: {
                    MyDialog dialog = new MyDialog(CarOwnerMyAccountActivity.this, 2);
                    dialog.setContent(getString(R.string.account_gps_expired_date));
                    dialog.sethineText(getString(R.string.account_gps_expired_date));
                    dialog.setDialogCallback(new MyDialog.Dialogcallback() {
                        @Override
                        public void dialogdo(String string) {
                            try {
                                gpsExpiredDate.setText(string);
                                JSONObject object = new JSONObject();
                                object.put("GpsExpire", string);
                                editCarInfo(object);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    dialog.show();
                    break;
                }
            }
        }
    };
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account_car);
        initTitle();
        initView();
//        getMyAccountCarOwner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyAccountCarOwner();
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.account_car_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.My);
        titleView.setMiddleText(R.string.account);
        View view = findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        username = (TextView) findViewById(R.id.account_username);
        contact = (TextView) findViewById(R.id.account_contact);
        phoneNumber = (TextView) findViewById(R.id.account_phone_number);
        address = (TextView) findViewById(R.id.account_address);
        carNumber = (TextView) findViewById(R.id.account_car_number);
        carNumberLayout = (RelativeLayout) findViewById(R.id.car_number_layout);
        carNumberLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("carNumberList", carNumberList);
                intent.putParcelableArrayListExtra("carInfoList", carInfoList);
                intent.setClass(CarOwnerMyAccountActivity.this, CarOwnerCarList.class);
                startActivityForResult(intent, 1);
            }
        });
        gpsNumber = (TextView) findViewById(R.id.account_gps_number);
        gpsNumberLayout = (RelativeLayout) findViewById(R.id.gps_number_layout);
        gpsNumberLayout.setOnClickListener(onClickListener);
        boxType = (TextView) findViewById(R.id.account_box_type);
        boxTypeLayout = (RelativeLayout) findViewById(R.id.box_type_layout);
        boxTypeLayout.setOnClickListener(onClickListener);
        maintainDate = (TextView) findViewById(R.id.account_maintain_date);
        maintainDateLayout = (RelativeLayout) findViewById(R.id.maintain_date_layout);
        maintainDateLayout.setOnClickListener(onClickListener);
        insuranceDate = (TextView) findViewById(R.id.account_insurance_date);
        insuranceDateLayout = (RelativeLayout) findViewById(insurance_date_layout);
        insuranceDateLayout.setOnClickListener(onClickListener);
        gpsExpiredDate = (TextView) findViewById(R.id.account_gps_expired_date);
        gpsExpiredDateLayout = (RelativeLayout) findViewById(R.id.gps_expired_date_layout);
        gpsExpiredDateLayout.setOnClickListener(onClickListener);
    }

    private void getMyAccountCarOwner() {
        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        if (GlobalParams.isNetworkAvailable(CarOwnerMyAccountActivity.this)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("UserId", sp.getString("UserId", null));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new MyThread(Constant.URL_CarOwnerPostGetMyAccount, handler, jsonObject, this).start();
        } else {
            MyToast.makeText(CarOwnerMyAccountActivity.this, "亲,网络未连接");

        }
    }

    private void editCarInfo(JSONObject object) {
        if (GlobalParams.isNetworkAvailable(CarOwnerMyAccountActivity.this)) {
            try {
                object.put("CarId", curCarId);
                new MyThread(Constant.URL_CarOwnerPostEditCar, editHandler, object, this).start();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            MyToast.makeText(CarOwnerMyAccountActivity.this, "亲,网络未连接");

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            selectedPosition = data.getIntExtra("position", 0);
            if (selectedPosition < carInfoList.size())
                displayCarInfo(carInfoList.get(selectedPosition));
        }
    }

    private void displayCarInfo(CarInfo carInfo) {
        curCarId = carInfo.getCarId();
        carNumber.setText(carInfo.getVehNof());
        gpsNumber.setText(carInfo.getGpsNumber());
        boxType.setText(carInfo.getBoxType());
        maintainDate.setText(carInfo.getUpKeepTime());
        insuranceDate.setText(carInfo.getInsuranceTime());
        gpsExpiredDate.setText(carInfo.getGpsExpiredTime());
    }
}
