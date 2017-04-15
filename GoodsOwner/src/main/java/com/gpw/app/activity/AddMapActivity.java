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
import com.gpw.app.util.DensityUtil;
import com.gpw.app.util.LogUtil;

public class AddMapActivity extends BaseActivity {

    private ImageView iv_left_black;
    private ImageView iv_location1;
    private EditText et_search;
    private ListView lv_search;
    private LinearLayout ll_search;
    private SuggestionSearch mSuggestionSearch;
    private AddressNameAdapter mAddressNameAdapter;
    private ArrayList<SuggestionResult.SuggestionInfo> mSuggestionInfos;
    private BaiduMap mBaiduMap;
    private TextView tv_map_name;
    private TextView tv_map_detail;
    private LinearLayout ll_location;
    private SharedPreferences prefs;
    private int pst;
    private int type;
    private String city;
    private boolean isSuggest = false;
    private CommonAdInfo commonAdInfo;
    private GeoCoder mSearch;
    private String receiptAddress;
    private LatLng latLng;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    MapView mMapView = null;

    OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
        public void onGetSuggestionResult(SuggestionResult res) {
            if (res == null || res.getAllSuggestions() == null) {
                Toast.makeText(AddMapActivity.this, "没有检索到结果", Toast.LENGTH_SHORT).show();
                return;
            }
            lv_search.setVisibility(View.VISIBLE);
            System.out.println(res.getAllSuggestions().toString());
            mSuggestionInfos.clear();
            mSuggestionInfos.addAll(res.getAllSuggestions());
            mAddressNameAdapter.notifyDataSetChanged();
        }
    };

    OnGetGeoCoderResultListener listener1 = new OnGetGeoCoderResultListener() {
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(AddMapActivity.this, "没有检索到结果", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(AddMapActivity.this, "没有检索到结果", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!result.getAddressDetail().city.equals(city)){
                city = result.getAddressDetail().city;
                showShortToastByString("已切换至:"+city);
            }
            LatLng location = result.getLocation();
            final String address = result.getAddress();
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
                isSuggest = false;
            }
            receiptAddress = receiptAddress + "   " + "(" + address + ")";
            tv_map_detail.setText(address);
            MapStatus mapStatus = new MapStatus.Builder()
                    .target(location)
                    .build();
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
                    .newMapStatus(mapStatus);
            mBaiduMap.animateMapStatus(mapStatusUpdate);
            tv_map_detail.setText(address);
            commonAdInfo.setReceiptAddress(receiptAddress);
            commonAdInfo.setLat(location.latitude);
            commonAdInfo.setLng(location.longitude);
            city = result.getAddressDetail().city;
            final String province =  result.getAddressDetail().province;

            ll_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == 3) {
                        getIntent().putExtra("City",city);
                        getIntent().putExtra("Province",province);
                        getIntent().putExtra("Address",address);
                        setResult(RESULT_OK,getIntent());
                        finish();
                    } else {
                        Intent intent = new Intent(AddMapActivity.this, EditAddressActivity.class);
                        intent.putExtra("position", pst);
                        intent.putExtra("commonAdInfo", commonAdInfo);
                        intent.putExtra("userId", getIntent().getStringExtra("userId"));
                        intent.putExtra("type", type);
                        startActivityForResult(intent, 5);
                    }

                }
            });
        }
    };


    @Override
    protected int getLayout() {
        return R.layout.activity_add_map;
    }

    @Override
    protected void findById() {
        mMapView = (MapView) findViewById(R.id.map_view);
        iv_left_black = (ImageView) findViewById(R.id.iv_left_black);
        iv_location1 = (ImageView) findViewById(R.id.iv_location1);
        et_search = (EditText) findViewById(R.id.et_search);
        lv_search = (ListView) findViewById(R.id.lv_search);
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        tv_map_name = (TextView) findViewById(R.id.tv_map_name);
        tv_map_detail = (TextView) findViewById(R.id.tv_map_detail);
        ll_location = (LinearLayout) findViewById(R.id.ll_location);
        View view_status = findViewById(R.id.view_status);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(AddMapActivity.this, 50.0f));
            layoutParams.setMargins(DensityUtil.dip2px(AddMapActivity.this, 5.0f), DensityUtil.dip2px(AddMapActivity.this, 15.0f), DensityUtil.dip2px(AddMapActivity.this, 5.0f), 0);
            ll_search.setLayoutParams(layoutParams);
            int color = 0xffffff;
            view_status.setBackgroundColor(color);
        }
    }

    @Override
    protected void initData() {
        mSuggestionInfos = new ArrayList<>();
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSearch = GeoCoder.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(listener);
        mSearch.setOnGetGeoCodeResultListener(listener1);
        mAddressNameAdapter = new AddressNameAdapter(mSuggestionInfos, this);

        prefs = getSharedPreferences(Contants.SHARED_NAME, MODE_PRIVATE);
        city = prefs.getString("city", "深圳市");
        type = getIntent().getIntExtra("type", 0);
        if (type == 1) {
            commonAdInfo = getIntent().getParcelableExtra("commonAdInfo");
            latLng = new LatLng(commonAdInfo.getLat(), commonAdInfo.getLng());
            pst = getIntent().getIntExtra("position", 0);
        } else {
            commonAdInfo = new CommonAdInfo();
            pst = -1;
        }

        mLocationClient = new LocationClient(AddMapActivity.this);     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();
    }

    private void initLocation() {
        LocationClientOption mOption = new LocationClientOption();
        mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        mOption.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
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
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        lv_search.setAdapter(mAddressNameAdapter);
        mBaiduMap.setOnMapStatusChangeListener(listener2);
        if (type == 1) {
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
            ll_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AddMapActivity.this, EditAddressActivity.class);
                    intent.putExtra("position", pst);
                    intent.putExtra("commonAdInfo", commonAdInfo);
                    intent.putExtra("type", type);
                    startActivityForResult(intent, 5);
                }
            });
        } else {

            if (ContextCompat.checkSelfPermission(AddMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(AddMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AddMapActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_PHONE_STATE}, 200);
            } else {
                mLocationClient.start();
            }
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
                mBaiduMap.clear();
                LatLng pt = mSuggestionInfos.get(position).pt;
                receiptAddress = mSuggestionInfos.get(position).key;
                if (pt==null){
                    showShortToastByString("地图上无法查询到该地点");
                    return;
                }
                isSuggest = true;
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(pt));


            }
        });
        iv_left_black.setOnClickListener(this);
        iv_location1.setOnClickListener(this);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationClient.start();
            } else {
                Toast.makeText(AddMapActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_black:
                finish();
                break;
            case R.id.iv_location1:
                mLocationClient.requestLocation();
                break;
            case R.id.tv_address:
                Intent intent = new Intent(AddMapActivity.this, CommonAddressActivity.class);
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
        mSuggestionSearch.destroy();
        mLocationClient.unRegisterLocationListener(myListener);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 5) {
            int position = data.getIntExtra("position", 0);
            int type = data.getIntExtra("type", 0);
            CommonAdInfo adInfo = data.getParcelableExtra("commonAdInfo");
            getIntent().putExtra("position", position);
            getIntent().putExtra("commonAdInfo", adInfo);
            getIntent().putExtra("type", type);
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }

    BaiduMap.OnMapStatusChangeListener listener2 = new BaiduMap.OnMapStatusChangeListener() {
        public void onMapStatusChangeStart(MapStatus status) {
        }

        public void onMapStatusChange(MapStatus status) {
        }

        public void onMapStatusChangeFinish(MapStatus status) {
            if (status != null) {
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(status.target));
            }
        }
    };


    class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            city = location.getCity();
            if (city == null) {
                mLocationClient.stop();
                mLocationClient.start();
                return;
            }
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
