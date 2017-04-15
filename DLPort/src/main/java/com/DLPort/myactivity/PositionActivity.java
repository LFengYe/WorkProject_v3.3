package com.DLPort.myactivity;

import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.mydata.CarLocation;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;
import com.DLPort.myview.TitleView;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/17.
 */
public class PositionActivity extends BaseActivity {
    private static final String TAG = "PositionActivity";

    private MapView mapView = null;
    private BaiduMap mBaiduMap = null;
    private RelativeLayout carNoListlayout;
    private ImageView carNoListlayoutImg;
    private ListView carNoListView;
    private ArrayList<String> carNoList;
    private ArrayAdapter carNoListAdapter;

    private ArrayList<CarLocation> carLocationList;

    private SharedPreferences preferences;
    private int userType;
    private boolean isFirst;

    private MyHandler getLocationHandler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    JSONObject jsonData = new JSONObject((String) msg.obj);
                    Log.d(TAG, "响应数据:" + jsonData.toString());
                    int status = jsonData.getInt("Status");
                    if (status == 0) {
                        progressData(jsonData.getString("Data"));
                        if (carLocationList.size() > 0) {
                            mBaiduMap.clear();
                            for (CarLocation location: carLocationList) {
                                addMarker(location);
                            }
                        }
                    } else {
                        MyToast.makeText(PositionActivity.this, jsonData.getString("Message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private void progressData(String locationJson) {
            try {
                if (null != carLocationList)
                    carLocationList.clear();
                else carLocationList = new ArrayList<>();

                if (null != carNoList)
                    carNoList.clear();
                else carNoList = new ArrayList<>();

                JSONArray jsonArray = new JSONArray(locationJson);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    CarLocation location = new CarLocation();
                    location.setCarId(object.getInt("CarId"));
                    location.setCarOwnerId(object.getString("CarOwnerId"));
                    location.setCarNumber(object.getString("VehNof"));
                    location.setCarType(object.getString("CarType"));
                    location.setTunnage(object.getString("Tunnage"));
                    location.setCarMessage(object.getString("CarMessage"));
                    location.setUpkeepTime(object.getString("UpkeepTime"));
                    location.setInsuranceTime(object.getString("InsuranceTime"));
                    location.setGpsExpire(object.getString("GpsExpire"));
                    location.setIsMortgage(object.getBoolean("IsMortgage"));
                    location.setBoxType(object.getString("BoxType"));
                    location.setGpsNo(object.getString("GpsNo"));
                    location.setLatitude(Float.valueOf(object.getString("latitude")));
                    location.setLongitude(Float.valueOf(object.getString("longitude")));
                    location.setStatus(object.getString("Staues"));
                    location.setAddress(object.getString("Address"));
                    carLocationList.add(location);
                    carNoList.add(location.getCarNumber());
                }
                carNoListAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_positon);
        initTitle();
        initView();
        initData();
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.car_positon_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.home);
        titleView.setMiddleText(R.string.O_pic4);
        View view = findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        mapView = (MapView) findViewById(R.id.mapView);
        mBaiduMap = mapView.getMap();
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                CarLocation location = (CarLocation) marker.getExtraInfo().get("location");
                showInfoWindow(location);
                return true;
            }
        });
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mBaiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        carNoListlayout = (RelativeLayout) findViewById(R.id.car_list_layout);
        carNoListlayout.setOnClickListener(new View.OnClickListener() {
            boolean isExpand;

            @Override
            public void onClick(View v) {
                isExpand = !isExpand;
                carNoListView.clearAnimation();
                int durationMillis = 350;
                Animation describeAnimation;
                if (isExpand) {
                    RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(durationMillis);
                    animation.setFillAfter(true);
                    carNoListlayoutImg.startAnimation(animation);

                    describeAnimation = new Animation() {
                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t) {
                            carNoListView.setVisibility(View.VISIBLE);
                        }
                    };
                } else {
                    RotateAnimation animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(durationMillis);
                    animation.setFillAfter(true);
                    carNoListlayoutImg.startAnimation(animation);

                    describeAnimation = new Animation() {
                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t) {
                            carNoListView.setVisibility(View.GONE);
                        }
                    };
                }

                describeAnimation.setDuration(durationMillis);
                carNoListView.startAnimation(describeAnimation);
            }
        });
        carNoListlayoutImg = (ImageView) findViewById(R.id.car_list_img);

        carNoListView = (ListView) findViewById(R.id.car_list);
        carNoList = new ArrayList<>();
        carNoListAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, carNoList);
        carNoListView.setAdapter(carNoListAdapter);
        carNoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (carNoListView.isItemChecked(position)) {
                    Log.i(TAG, "item click checked carNo:" + carNoList.get(position));
                    mBaiduMap.clear();
                    CarLocation location = carLocationList.get(position);
                    addMarker(location);
                    showInfoWindow(location);
                } else {
                    Log.i(TAG, "item click unchecked carNo:" + carNoList.get(position));
                }
            }
        });
    }

    private void initData() {
        userType = getIntent().getExtras().getInt("Type");
        if (userType == 0) {
            preferences = getSharedPreferences("user", MODE_PRIVATE);
        }
        if (userType == 1) {
            preferences = getSharedPreferences("huo", MODE_PRIVATE);
        }
        isFirst = true;
        getAllCarLocation();
    }

    private void getAllCarLocation() {
        if (GlobalParams.isNetworkAvailable(this)) {
            try {
                JSONObject object = new JSONObject();
                object.put("Id", preferences.getString("UserId", ""));
                if (userType == 0)
                    new MyThread(Constant.URL_CarOwnerPostVehPosition, getLocationHandler, object, this).start();
                if (userType == 1)
                    new MyThread(Constant.URL_CargoOwnerPostVehPosition, getLocationHandler, object, this).start();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "亲,网络未连接", Toast.LENGTH_SHORT).show();
        }
    }

    private void addMarker(CarLocation location) {
//        CoordinateConverter converter = new CoordinateConverter();
//        converter.from(CoordinateConverter.CoordType.GPS);
//        LatLng sourceLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//        converter.coord(sourceLatLng);
//        LatLng desLatLng = converter.convert();
        LatLng desLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.car_icon);
        OverlayOptions options = new MarkerOptions().position(desLatLng).icon(bitmap).anchor(0.5f, 0.5f);
        Marker marker = (Marker) mBaiduMap.addOverlay(options);
        Bundle bundle = new Bundle();
        bundle.putSerializable("location", location);
        marker.setExtraInfo(bundle);

        MapStatusUpdate statusUpdate = MapStatusUpdateFactory.newLatLng(desLatLng);
        mBaiduMap.setMapStatus(statusUpdate);
    }

    private void showInfoWindow(CarLocation location) {
        View view = View.inflate(PositionActivity.this, R.layout.car_mark_info, null);
        TextView carNum = (TextView) view.findViewById(R.id.car_mark_carnum);
        TextView status = (TextView) view.findViewById(R.id.car_mark_status);
        TextView latLng = (TextView) view.findViewById(R.id.car_mark_latlng);
        TextView address = (TextView) view.findViewById(R.id.car_mark_address);
        carNum.setText(location.getCarNumber());
        status.setText("状态:" + location.getStatus());
        latLng.setText("经纬度:" + location.getLongitude() + ", " + location.getLatitude());
        address.setText("地址:" + location.getAddress());

        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        LatLng sourceLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        converter.coord(sourceLatLng);
        LatLng desLatLng = converter.convert();
        Point point = mBaiduMap.getProjection().toScreenLocation(desLatLng);
        point.y = -5;

        LatLng infoPosition = mBaiduMap.getProjection().fromScreenLocation(point);
        InfoWindow infoWindow = new InfoWindow(view, desLatLng, -45 / 2);
        mBaiduMap.showInfoWindow(infoWindow);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
}
