package com.gpw.app.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import com.gpw.app.R;
import com.gpw.app.adapter.OrderOffersAdapter;
import com.gpw.app.base.BaseActivity;
import com.gpw.app.base.Contants;
import com.gpw.app.bean.OrderAddressBean;
import com.gpw.app.bean.OrderOfferInfo;
import com.gpw.app.bean.PayAmount;
import com.gpw.app.util.EncryptUtil;
import com.gpw.app.util.HttpUtil;
import com.gpw.app.util.LogUtil;
import com.gpw.app.util.VolleyInterface;

public class OrderOffersActivity extends BaseActivity {
    private TextView tv_title;
    private TextView tv_right;
    private TextView tv_empty;
    private ImageView iv_left_white;
    private ListView lv_order_offers;
    private ArrayList<OrderOfferInfo> orderOfferInfos;
    private OrderOffersAdapter orderOffersAdapter;
    private String orderId;
    private String money;
    private String time;
    private int orderType;
    private boolean isChange = false;
    private ArrayList<OrderAddressBean> orderAddressBeen;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    @Override
    protected int getLayout() {
        return R.layout.activity_order_offers;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);
        lv_order_offers = (ListView) findViewById(R.id.lv_order_offers);
        tv_empty = (TextView) findViewById(R.id.tv_empty);


    }


    @Override
    protected void initData() {
        orderOfferInfos = new ArrayList<>();
        orderOffersAdapter = new OrderOffersAdapter(orderOfferInfos, this);
        orderId = getIntent().getStringExtra("orderId");
        orderType = getIntent().getIntExtra("type", 0);
        time = getIntent().getStringExtra("time");
        orderAddressBeen = getIntent().getParcelableArrayListExtra("orderAddressBeen");
        initLocation();
    }
    private void initLocation() {
        mLocationClient = new LocationClient(OrderOffersActivity.this);     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        LocationClientOption mOption = new LocationClientOption();
        mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        mOption.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        mOption.setOpenGps(true);
        mOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mLocationClient.setLocOption(mOption);
    }

    @Override
    protected void initView() {
        tv_title.setText(R.string.order_offers);
        lv_order_offers.setAdapter(orderOffersAdapter);
        lv_order_offers.setEmptyView(tv_empty);
        lv_order_offers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("UserId", Contants.userId);
                jsonObject.addProperty("OrderNo", orderId);
                jsonObject.addProperty("TransporterId", orderOfferInfos.get(position).getTransporterId());
                LogUtil.i(jsonObject.toString());
                Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());

                HttpUtil.doPost(OrderOffersActivity.this, Contants.url_confirmTransporter, "confirmTransporter", map, new VolleyInterface(OrderOffersActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                    @Override
                    public void onSuccess(JsonElement result) {
                        LogUtil.i(result.toString());
                        showShortToastByString(result.toString());
                        isChange = true;
                        Gson gson = new Gson();
                        PayAmount payAmount = gson.fromJson(result, PayAmount.class);
                        money = String.format("¥%s", payAmount.getPayAmount());
                        Intent intent = new Intent(OrderOffersActivity.this, OrderPayActivity.class);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("type", orderType);
                        intent.putParcelableArrayListExtra("orderAddressBeen", orderAddressBeen);
                        intent.putExtra("time", time);
                        intent.putExtra("money", money);
                        intent.putExtra("isAfterPay", true);
                        startActivityForResult(intent, 1);
                    }

                    @Override
                    public void onError(VolleyError error) {
                        showShortToastByString(getString(R.string.timeoutError));
//                LogUtil.i("hint",error.networkResponse.headers.toString());
//                LogUtil.i("hint",error.networkResponse.statusCode+"");
                    }

                    @Override
                    public void onStateError() {
                    }
                });
            }
        });
        tv_right.setVisibility(View.GONE);
        iv_left_white.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(OrderOffersActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(OrderOffersActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(OrderOffersActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_PHONE_STATE}, 200);
        } else {
            mLocationClient.start();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left_white:
                if (isChange) {
                    setResult(RESULT_OK, getIntent());
                }
                finish();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationClient.start();
            } else {
                Toast.makeText(OrderOffersActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isChange) {
            setResult(RESULT_OK, getIntent());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApp.removeActivity(this);
        mLocationClient.unRegisterLocationListener(myListener);
    }

    class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            String city = location.getCity();
            LogUtil.i(city);
            if (city == null) {
                mLocationClient.stop();
                mLocationClient.start();
                return;
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("UserId", Contants.userId);
            jsonObject.addProperty("OrderNo", orderId);
            jsonObject.addProperty("Lat", location.getLatitude());
            jsonObject.addProperty("Lng", location.getLongitude());
            Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
            HttpUtil.doPost(OrderOffersActivity.this, Contants.url_getOrderOffers, "getUserBalance", map, new VolleyInterface(OrderOffersActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                @Override
                public void onSuccess(JsonElement result) {
                    LogUtil.i("offer" + result.toString());
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<OrderOfferInfo>>() {
                    }.getType();
                    ArrayList<OrderOfferInfo> newOrderOfferInfos = gson.fromJson(result, listType);
                    orderOfferInfos.clear();
                    orderOfferInfos.addAll(newOrderOfferInfos);
                    orderOffersAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(VolleyError error) {
                    showShortToastByString(getString(R.string.timeoutError));
//                        LogUtil.i("hint",error.networkResponse.headers.toString());
//                        LogUtil.i("hint",error.networkResponse.statusCode+"");
                }

                @Override
                public void onStateError() {
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }
}
