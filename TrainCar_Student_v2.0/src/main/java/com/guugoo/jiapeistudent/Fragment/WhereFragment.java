package com.guugoo.jiapeistudent.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.guugoo.jiapeistudent.Adapter.SiteAdapter;
import com.guugoo.jiapeistudent.App.Constant;
import com.guugoo.jiapeistudent.Data.ReturnData;
import com.guugoo.jiapeistudent.Data.Site;
import com.guugoo.jiapeistudent.MainActivity.ReserveTrainActivity;
import com.guugoo.jiapeistudent.R;
import com.guugoo.jiapeistudent.Tools.DES;
import com.guugoo.jiapeistudent.Tools.MyHandler;
import com.guugoo.jiapeistudent.Tools.MyThread;
import com.guugoo.jiapeistudent.Tools.MyToast;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/8/8.
 */
public class WhereFragment extends Fragment {
    private static final String TAG = "WhereFragment";

    private FragmentManager manager;
    private FragmentTransaction ft;

    private ListView listView;
    private SharedPreferences sp;
    private List<Site> listData;
    private SiteAdapter adapter;
    private View fragmentView;
    //纬度
    private double latitude = 0;
    //经度
    private double longitude = 0;
    private ImageView search;
    private EditText search_text;
    private LocationClient client;
    private Handler handler;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "OnCreateView");
        fragmentView = inflater.inflate(R.layout.fragment_where, container, false);
        handler = new MyHandler(getActivity()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    ReturnData data = JSONObject.parseObject((String) msg.obj, ReturnData.class);
                    if (data.getStatus() == 0) {
                        List<Site> sites = JSONObject.parseArray(data.getData(), Site.class);
                        listData.clear();
                        listData.addAll(sites);
                        adapter.notifyDataSetChanged();
                    } else {
                        MyToast.makeText(getContext(), data.getMessage());
                    }
                }
            }
        };
        findById();
        init();
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        MPermissions.requestPermissions(WhereFragment.this, 4, Manifest.permission.ACCESS_FINE_LOCATION);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            init();
        }
    }

    @PermissionGrant(4)
    public void requestContactSuccess()
    {
        //Toast.makeText(getActivity(), "允许访问位置信息!", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied(4)
    public void requestContactFailed()
    {
        Toast.makeText(getActivity(), "拒绝访问位置信息!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void findById() {
        listView = (ListView) fragmentView.findViewById(R.id.where_list);
        listData = new ArrayList<>();
        adapter = new SiteAdapter(getContext(), R.layout.adapter_site, listData);
        listView.setAdapter(adapter);
        sp = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        search = (ImageView) fragmentView.findViewById(R.id.search_image);
        search_text = (EditText) fragmentView.findViewById(R.id.search_text);
        search_text.setHint("请输入场地名称");
    }

    private void init() {
        getWhere();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Site site = listData.get(position);
//                Intent intent = new Intent(getActivity(), WhereSelectTimeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type",1);
                bundle.putInt("BranchId", site.getBid());
                TimeFragment timeFragment = ((ReserveTrainActivity)getActivity()).getTimeFragment();
                //timeFragment.setArguments(bundle);
                timeFragment.setBundle(bundle);

                ft = manager.beginTransaction();
                ft.hide(WhereFragment.this);
                ft.show(timeFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                select();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select();
            }
        });

    }

    private void select() {
        listData.clear();
        JSONObject json = new JSONObject(true);
        json.put("SchoolId", sp.getInt("SchoolId", 0));
        json.put("StudentId", sp.getInt("Id", 0));
        json.put("Subject", sp.getInt("CurrentSubject", 0));
        json.put("Longitude", longitude);
        json.put("Latitude", latitude);
        json.put("AreaName", search_text.getText());
        new MyThread(Constant.URL_SurroundingArea, handler, DES.encryptDES(json.toString())).start();
    }

    private void getSiteData() {
        JSONObject json = new JSONObject(true);
        json.put("SchoolId", sp.getInt("SchoolId", 0));
        json.put("StudentId", sp.getInt("Id", 0));
        json.put("Subject", sp.getInt("CurrentSubject", 0));
        json.put("Longitude", longitude);
        json.put("Latitude", latitude);
        json.put("AreaName", "");
        new MyThread(Constant.URL_SurroundingArea, handler, DES.encryptDES(json.toString())).start();

        client.stop();
        client.unRegisterLocationListener(mListener);
    }

    private void getWhere() {
        LocationClientOption mOption = new LocationClientOption();
        mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        mOption.setScanSpan(3 * 1000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        mOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        mOption.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        mOption.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        client = new LocationClient(getActivity());
        client.setLocOption(mOption);
        client.registerLocationListener(mListener);
        client.start();
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                double Latitude = location.getLatitude();// 纬度
                double Longitude = location.getLongitude();// 经度
                logMsg(Longitude,Latitude);
            }
        }
    };

    public void logMsg(final double Longitude, final double Latitude) {
        if(Latitude != 4.9E-324 && Longitude != 4.9E-324){
            latitude = Latitude;// 纬度
            longitude = Longitude;// 经度
            getSiteData();
        }else {
            Log.d(TAG, "onReceiveLocation:Error "+Latitude);
            Log.d(TAG, "onReceiveLocation:Error "+Longitude);
            MyToast.makeText(getActivity(),"获取地址失败！");
            //getSiteData();
        }
    }
}
