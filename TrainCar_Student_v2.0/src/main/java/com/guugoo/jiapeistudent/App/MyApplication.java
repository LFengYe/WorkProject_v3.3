package com.guugoo.jiapeistudent.App;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.guugoo.jiapeistudent.Data.ReturnData;
import com.guugoo.jiapeistudent.R;
import com.guugoo.jiapeistudent.Tools.DES;
import com.guugoo.jiapeistudent.Tools.LocationService;
import com.guugoo.jiapeistudent.Tools.MyThread;
import com.guugoo.jiapeistudent.Tools.MyToast;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.text.SimpleDateFormat;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/8/12.
 */
public class MyApplication extends Application{
    private static final String TAG = "MyApplication";
    public LocationService locationService;
    public Vibrator mVibrator;
    private static Context context;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MyThread.SUCCESS: {
                    try {
                        ReturnData data = JSONObject.parseObject((String) msg.obj, ReturnData.class);
                        if (data.getStatus() == 0) {
                            //Log.d(TAG, "位置信息上传成功!");
                        } else {
                            //MyToast.makeText(MyApplication.this, data.getMessage());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //MyToast.makeText(MyApplication.this, "数据出错");
                    }
                    break;
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.icon_stub) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
//                 .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);

        JPushInterface.setDebugMode(false); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);


    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        locationService.stop();
        if (locationService != null) {
            locationService.unregisterListener(mListener);
        }
    }

    public static Context getContext() {
        return context;
    }

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
        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        JSONObject json = new JSONObject();
        json.put("Longitude",Longitude);
        json.put("Latitude",Latitude);
        json.put("BookingId", sp.getString("BookingId", ""));
        json.put("SchoolId",sp.getInt("SchoolId",0));
        json.put("UserType",1);
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        json.put("CreateTime",date);
        new MyThread(Constant.URL_GpsStorage, handler, DES.encryptDES(json.toString())).start();

        //Log.d(TAG, "onReceiveLocation:Error "+Latitude);
        //Log.d(TAG, "onReceiveLocation:Error "+Longitude);
    }
}
