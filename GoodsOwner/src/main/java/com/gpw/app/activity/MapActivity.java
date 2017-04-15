package com.gpw.app.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.ArrayList;

import com.gpw.app.R;
import com.gpw.app.adapter.AddressNameAdapter;
import com.gpw.app.base.BaseActivity;
import com.gpw.app.base.Contants;
import com.gpw.app.bean.CommonAdInfo;
import com.gpw.app.bean.OrderAddressInfo;
import com.gpw.app.util.DensityUtil;
import com.gpw.app.util.LogUtil;


public class MapActivity extends BaseActivity implements OnGetSuggestionResultListener {

    private ImageView iv_left_black;
    private ImageView iv_location1;
    private EditText et_search;
    private TextView tv_address;
    private TextView tv_map_name;
    private TextView tv_map_detail;
    private ListView lv_search;
    private LinearLayout ll_search;
    private LinearLayout ll_location;

    private int pst;
    private int type;
    private boolean isSuggest = false;
    private String city;
    private String receiptAddress;
    private MapView mMapView;
    private GeoCoder mSearch;
    private BaiduMap mBaiduMap;
    private SharedPreferences prefs;
    private LocationClient mLocationClient;
    private OrderAddressInfo mOrderAddressInfo;
    private SuggestionSearch mSuggestionSearch = null;

    private AddressNameAdapter mAddressNameAdapter;
    private ArrayList<SuggestionResult.SuggestionInfo> mSuggestionInfos;
    private BDLocationListener myListener = new MyLocationListener();


    @Override
    protected int getLayout() {
        return R.layout.activity_map;
    }

    @Override
    protected void findById() {
        mMapView = (MapView) findViewById(R.id.map_view);
        iv_left_black = (ImageView) findViewById(R.id.iv_left_black);
        iv_location1 = (ImageView) findViewById(R.id.iv_location1);
        et_search = (EditText) findViewById(R.id.et_search);
        tv_address = (TextView) findViewById(R.id.tv_address);
        lv_search = (ListView) findViewById(R.id.lv_search);
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        tv_map_name = (TextView) findViewById(R.id.tv_map_name);
        tv_map_detail = (TextView) findViewById(R.id.tv_map_detail);
        ll_location = (LinearLayout) findViewById(R.id.ll_location);
        View view_status = findViewById(R.id.view_status);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(MapActivity.this, 50.0f));
            layoutParams.setMargins(DensityUtil.dip2px(MapActivity.this, 5.0f), DensityUtil.dip2px(MapActivity.this, 15.0f), DensityUtil.dip2px(MapActivity.this, 5.0f), 0);
            ll_search.setLayoutParams(layoutParams);
            int color = 0xffffff;
            view_status.setBackgroundColor(color);
        }
    }

    @Override
    protected void initData() {

        mLocationClient = new LocationClient(MapActivity.this);     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();

        mSuggestionInfos = new ArrayList<>();
        mSearch = GeoCoder.newInstance();
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        mSearch.setOnGetGeoCodeResultListener(listener1);
        mAddressNameAdapter = new AddressNameAdapter(mSuggestionInfos, this);
        prefs = getSharedPreferences(Contants.SHARED_NAME, MODE_PRIVATE);
        city = prefs.getString("city", "深圳市");
        pst = getIntent().getIntExtra("position", 0);
        type = getIntent().getIntExtra("type", 0);

        mOrderAddressInfo = getIntent().getParcelableExtra("orderAddressInfo");


    }

    private void initLocation() {

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
        mBaiduMap = mMapView.getMap();

        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        lv_search.setAdapter(mAddressNameAdapter);
        mBaiduMap.setOnMapStatusChangeListener(listener2);
        if (mOrderAddressInfo.getReceiptAddress().equals("start")) {
            if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_PHONE_STATE}, 200);
            } else {
                mLocationClient.start();
            }

        } else {
            LatLng latLng = new LatLng(mOrderAddressInfo.getLat(), mOrderAddressInfo.getLng());
            receiptAddress = mOrderAddressInfo.getReceiptAddress();
            String[] nameAd = receiptAddress.split("  ");
            tv_map_name.setText(nameAd[0]);
            tv_map_detail.setText(nameAd[1]);
            MapStatus mapStatus = new MapStatus.Builder()
                    .target(latLng)
                    .zoom(17.0f)
                    .build();
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
            mBaiduMap.animateMapStatus(mapStatusUpdate);
        }


        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() <= 0) {
                    mSuggestionInfos.clear();
                    mAddressNameAdapter.notifyDataSetChanged();
                    return;
                }
                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                        .keyword(s.toString())
                        .city(city));

            }
        });
        lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LatLng pt = mSuggestionInfos.get(position).pt;
                receiptAddress = mSuggestionInfos.get(position).key;
                if (pt==null){
                    showShortToastByString("地图上无法查询到该地点");
                    return;
                }
                isSuggest = true;

                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(pt));
                lv_search.setVisibility(View.GONE);
            }
        });

        if (mOrderAddressInfo.getState() == 3 && type == 2) {
            mOrderAddressInfo = new OrderAddressInfo();
            mOrderAddressInfo.setState(2);
            mOrderAddressInfo.setAction(2);
        }

        iv_left_black.setOnClickListener(this);
        tv_address.setOnClickListener(this);
        ll_location.setOnClickListener(this);
        iv_location1.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_left_black:
                finish();
                break;
            case R.id.iv_location1:
                mLocationClient.requestLocation();
                break;
            case R.id.ll_location:
                intent = new Intent(MapActivity.this, ImproveDisclosureActivity.class);
                intent.putExtra("position", pst);
                intent.putExtra("orderAddressInfo", mOrderAddressInfo);
                intent.putExtra("type", type);
                startActivityForResult(intent, 4);
                break;
            case R.id.tv_address:
                intent = new Intent(MapActivity.this, CommonAddressActivity.class);
                intent.putExtra("type", 1);
                startActivityForResult(intent, 7);
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
        mSuggestionSearch.destroy();
        mSearch.destroy();

        mLocationClient.unRegisterLocationListener(myListener);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationClient.start();
            } else {
                Toast.makeText(MapActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 4) {
            int position = data.getIntExtra("position", 0);
            int type1 = data.getIntExtra("type", 0);
            OrderAddressInfo orderAddressInfo = data.getParcelableExtra("orderAddressInfo");
            getIntent().putExtra("position", position);
            getIntent().putExtra("orderAddressInfo", orderAddressInfo);
            getIntent().putExtra("type", type1);
            setResult(RESULT_OK, getIntent());
            finish();
        }
        if (resultCode == RESULT_OK && requestCode == 7) {

            CommonAdInfo commonAdInfo = data.getParcelableExtra("commonAdInfo");
            LatLng latLng = new LatLng(commonAdInfo.getLat(), commonAdInfo.getLng());


            receiptAddress = commonAdInfo.getReceiptAddress();
            String[] nameAd = receiptAddress.split("  ");
            tv_map_name.setText(nameAd[0]);
            tv_map_detail.setText(nameAd[1]);

            MapStatus mapStatus = new MapStatus.Builder()
                    .target(latLng)
                    .zoom(17.0f)
                    .build();
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
                    .newMapStatus(mapStatus);
            mBaiduMap.animateMapStatus(mapStatusUpdate);

            lv_search.setVisibility(View.GONE);
            lv_search.setVisibility(View.GONE);


            if (mOrderAddressInfo.getState() == 3 && type == 2) {
                mOrderAddressInfo = new OrderAddressInfo();
                mOrderAddressInfo.setState(2);
                mOrderAddressInfo.setAction(2);

            }

            mOrderAddressInfo.setLat(latLng.latitude);
            mOrderAddressInfo.setLng(latLng.longitude);
            mOrderAddressInfo.setReceiptAddress(receiptAddress);
            mOrderAddressInfo.setReceipter(commonAdInfo.getReceipter());
            mOrderAddressInfo.setReceiptTel(commonAdInfo.getReceiptTel());

        }

    }


    OnGetGeoCoderResultListener listener1 = new OnGetGeoCoderResultListener() {
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                return;
            }
        }


        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                showShortToastByString("未找到该地址");
                return;
            }


            if (!result.getAddressDetail().city.equals(city)) {
                city = result.getAddressDetail().city;
                showShortToastByString("已切换至:" + city);
            }
            LatLng location = result.getLocation();
            String address = result.getAddress();
            if (!isSuggest) {
                if (result.getPoiList() != null) {
                    PoiInfo poiInfo = result.getPoiList().get(0);
                    LatLng poiLatLng = poiInfo.location;
                    double distance = DistanceUtil.getDistance(poiLatLng, location);
                    if (distance > 300) {
                        receiptAddress = address;
                    } else {
                        receiptAddress = poiInfo.name;
                    }
                } else {
                    receiptAddress = address;
                }
                tv_map_name.setText(receiptAddress);
            } else {
                tv_map_name.setText(receiptAddress);

            }
            receiptAddress = receiptAddress + "   " + "(" + address + ")";
            tv_map_detail.setText(address);

            MapStatus mapStatus = new MapStatus.Builder()
                    .target(location)
                    .build();
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
            mBaiduMap.animateMapStatus(mapStatusUpdate);



            mOrderAddressInfo.setReceiptAddress(receiptAddress);
            mOrderAddressInfo.setLat(location.latitude);
            mOrderAddressInfo.setLng(location.longitude);
            isSuggest = false;
        }
    };

    BaiduMap.OnMapStatusChangeListener listener2 = new BaiduMap.OnMapStatusChangeListener() {
        public void onMapStatusChangeStart(MapStatus status) {

        }

        public void onMapStatusChange(MapStatus status) {
            if (status != null) {
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(status.target));
            }
        }

        public void onMapStatusChangeFinish(MapStatus status) {

        }
    };


    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
            return;
        }
        lv_search.setVisibility(View.VISIBLE);
        mSuggestionInfos.clear();
        mSuggestionInfos.addAll(suggestionResult.getAllSuggestions());
        mAddressNameAdapter.notifyDataSetChanged();

    }

    class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            city = location.getCity();
            if (city == null) {
                mLocationClient.stop();
                mLocationClient.start();
                return;
            }
            showShortToastByString("当前城市:" + city);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("city", city);
            editor.apply();

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatus mapStatus = new MapStatus.Builder()
                    .target(latLng)
                    .zoom(17.0f)
                    .build();
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
            mBaiduMap.animateMapStatus(mapStatusUpdate);
            mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));


        }
    }


}
