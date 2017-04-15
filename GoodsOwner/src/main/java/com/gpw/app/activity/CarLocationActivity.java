package com.gpw.app.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.android.volley.VolleyError;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import com.gpw.app.R;
import com.gpw.app.base.BaseActivity;
import com.gpw.app.base.Contants;
import com.gpw.app.bean.CarLoactionInfo;
import com.gpw.app.util.EncryptUtil;
import com.gpw.app.util.HttpUtil;
import com.gpw.app.util.LogUtil;
import com.gpw.app.util.VolleyInterface;

public class CarLocationActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private BaiduMap mBaiduMap;
    private LatLng latLng;
    MapView mMapView = null;
    private String TransporterId;
    private String TransporterName;
    private ImageView iv_location1;


    @Override
    protected int getLayout() {
        return R.layout.activity_car_location;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);
        mMapView = (MapView) findViewById(R.id.map_view);
        iv_location1 = (ImageView) findViewById(R.id.iv_location1);

    }

    @Override
    protected void initData() {
        TransporterId = getIntent().getStringExtra("TransporterId");
        TransporterName = getIntent().getStringExtra("TransporterName");
    }

    @Override
    protected void initView() {
        mBaiduMap = mMapView.getMap();
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);

        iv_left_white.setOnClickListener(this);
        iv_location1.setOnClickListener(this);
        tv_title.setText(R.string.car_location);
        tv_right.setVisibility(View.GONE);
        carLocation();

    }

    private void carLocation() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("TransporterId", TransporterId);
        LogUtil.i(jsonObject.toString());
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(CarLocationActivity.this, Contants.url_getVehicleLocation, "getVehicleLocation", map, new VolleyInterface(CarLocationActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                LogUtil.i(result.toString());
                Gson gson = new Gson();
                CarLoactionInfo carLoactionInfo = gson.fromJson(result,CarLoactionInfo.class);

                mBaiduMap.clear();
                LatLng latLng = new LatLng(carLoactionInfo.getLat(), carLoactionInfo.getLng());
            //    BitmapDescriptor bitmap = getBitmapDescriptor(carLoactionInfo.getAngle());
//                OverlayOptions option = new MarkerOptions()
//                        .position(latLng)
//                        .icon(bitmap)
//                        .alpha(0.7f);
//
//                mBaiduMap.addOverlay(option);

                LinearLayout linearLayout = (LinearLayout) View.inflate(CarLocationActivity.this, R.layout.view_car_location, null);
                TextView tv_map_name = (TextView) linearLayout.findViewById(R.id.tv_map_name);
                ImageView iv_location = (ImageView) linearLayout.findViewById(R.id.iv_location);

                tv_map_name.setText(TransporterName+"  "+carLoactionInfo.getVehicleNo());
                iv_location.setImageResource(getBitmapId(carLoactionInfo.getAngle()));

                InfoWindow mInfoWindow = new InfoWindow(linearLayout, latLng, -20);
                mBaiduMap.showInfoWindow(mInfoWindow);

                MapStatus mapStatus = new MapStatus.Builder()
                        .target(latLng)
                        .zoom(15.0f)
                        .build();
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
                        .newMapStatus(mapStatus);
                mBaiduMap.animateMapStatus(mapStatusUpdate);

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

    private BitmapDescriptor getBitmapDescriptor(int angle) {
        int resource = 0;
        switch (angle){
            case 0:
                resource=R.mipmap.r_vehicle0;
                break;
            case 22:
                resource=R.mipmap.r_vehicle0_45;
                break;
            case 45:
                resource=R.mipmap.r_vehicle45;
                break;
            case 67:
                resource=R.mipmap.r_vehicle45_90;
                break;
            case 90:
                resource=R.mipmap.r_vehicle90;
                break;
            case 112:
                resource=R.mipmap.r_vehicle90_135;
                break;
            case 135:
                resource=R.mipmap.r_vehicle135;
                break;
            case 157:
                resource=R.mipmap.r_vehicle135_180;
                break;
            case 180:
                resource=R.mipmap.r_vehicle180;
                break;
            case 202:
                resource=R.mipmap.r_vehicle180_225;
                break;
            case 225:
                resource=R.mipmap.r_vehicle225;
                break;
            case 247:
                resource=R.mipmap.r_vehicle225_270;
                break;
            case 270:
                resource=R.mipmap.r_vehicle270;
                break;
            case 292:
                resource=R.mipmap.r_vehicle270_315;
                break;
            case 315:
                resource=R.mipmap.r_vehicle315;
                break;
            case 337:
                resource=R.mipmap.r_vehicle315_0;
                break;
        }
        return BitmapDescriptorFactory.fromResource(resource);
    }


    private int getBitmapId(int angle) {
        int resource = 0;
        switch (angle){
            case 0:
                resource=R.mipmap.r_vehicle0;
                break;
            case 22:
                resource=R.mipmap.r_vehicle0_45;
                break;
            case 45:
                resource=R.mipmap.r_vehicle45;
                break;
            case 67:
                resource=R.mipmap.r_vehicle45_90;
                break;
            case 90:
                resource=R.mipmap.r_vehicle90;
                break;
            case 112:
                resource=R.mipmap.r_vehicle90_135;
                break;
            case 135:
                resource=R.mipmap.r_vehicle135;
                break;
            case 157:
                resource=R.mipmap.r_vehicle135_180;
                break;
            case 180:
                resource=R.mipmap.r_vehicle180;
                break;
            case 202:
                resource=R.mipmap.r_vehicle180_225;
                break;
            case 225:
                resource=R.mipmap.r_vehicle225;
                break;
            case 247:
                resource=R.mipmap.r_vehicle225_270;
                break;
            case 270:
                resource=R.mipmap.r_vehicle270;
                break;
            case 292:
                resource=R.mipmap.r_vehicle270_315;
                break;
            case 315:
                resource=R.mipmap.r_vehicle315;
                break;
            case 337:
                resource=R.mipmap.r_vehicle315_0;
                break;
        }
        return resource;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_white:
                finish();
                break;
            case R.id.iv_location1:
                carLocation();
                break;
            case R.id.tv_address:
                Intent intent = new Intent(CarLocationActivity.this, CommonAddressActivity.class);
                startActivity(intent);
                break;

        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApp.removeActivity(this);
        mMapView.onDestroy();
    }


}
