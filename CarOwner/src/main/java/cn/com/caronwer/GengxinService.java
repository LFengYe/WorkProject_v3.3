package cn.com.caronwer;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import cn.com.caronwer.base.Contants;
import cn.com.caronwer.util.EncryptUtil;
import cn.com.caronwer.util.HttpUtil;
import cn.com.caronwer.util.LogUtil;
import cn.com.caronwer.util.SPtils;
import cn.com.caronwer.util.VolleyInterface;

public class GengxinService extends Service implements SensorEventListener{

//    public Location mLocation = new Location("");
    private static final int TOP_NEWS_CHANGE_TIME = 4000;// 顶部新闻切换事件

    // 记录指南针图片转过的角度
    int currentDegree = 0;
    // 定义Sensor管理器
    SensorManager mSensorManager;

    private LocationClient mLocationClient = null;
    private BDLocation mLocation = null;
    private LatLng mLatLng = null;

    private Handler mHandler;

    public GengxinService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        initLocationClient();
        updateVehicleLocationDelay();
        // 获取传感器管理服务
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        // 为系统的方向传感器注册监听器
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onDestroy() {
        // 取消注册
        mSensorManager.unregisterListener(this);
        mLocationClient.stop();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
        // 为系统的方向传感器注册监听器

    }

    private void initLocationClient() {
        /****************百度地图定位客户端初始化*****************/
        mLocationClient = new LocationClient(getApplicationContext());
        //初始化定位参数
        LocationClientOption mLocationOption = new LocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        mLocationOption.setScanSpan(5000);//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setOpenGps(false);//可选，默认false,设置是否使用gps
        mLocationOption.setLocationNotify(false);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        mLocationOption.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mLocationOption.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationOption.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mLocationOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        mLocationOption.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        //设置定位参数
        mLocationClient.setLocOption(mLocationOption);

        //设置回调监听器
        BDLocationListener mLocationListener = new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation != null) {
                    mLocation = bdLocation;
//                    LogUtil.i("GengXinService", "最新定位:lat:" + bdLocation.getLatitude() + ",lng:" + bdLocation.getLongitude());
                }
            }
        };
        mLocationClient.registerLocationListener(mLocationListener);
        /****************百度地图定位客户端初始化结束****************/

        mLocationClient.start();
    }
    /**
     * 定时更新位置信息
     */
    private void updateVehicleLocationDelay(){
        if (mHandler == null) {
            mHandler = new Handler() {
                public void handleMessage(Message msg) {
                    if (mLocation != null) {
                        updateVehicleLocation();
                    }
                    mHandler.sendMessageDelayed(Message.obtain(),
                            TOP_NEWS_CHANGE_TIME);
                };
            };
            mHandler.sendMessageDelayed(Message.obtain(), TOP_NEWS_CHANGE_TIME);// 延时4s发送消息
        }
    }
    /**
     * 更新位置信息
     */
    private void updateVehicleLocation()  {

        String vehicleNo = SPtils.getString(GengxinService.this, "VehicleNo", "0000");
        String userId = SPtils.getString(GengxinService.this, "UserId", "00000000-0000-0000-0000-000000000000");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", userId);
        jsonObject.addProperty("VehicleNo", vehicleNo);//车牌号

        jsonObject.addProperty("Lat", mLocation.getLatitude());
        jsonObject.addProperty("Lng", mLocation.getLongitude());
        jsonObject.addProperty("Speed", mLocation.getSpeed());
        jsonObject.addProperty("Angle", currentDegree);//角度
        jsonObject.addProperty("IsLocation", 1);//是否定位
//        LogUtil.i("GengXinService", "上传的数据:" + jsonObject.toString());
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(GengxinService.this, Contants.url_updatevehicleLocation, "updatevehicleLocation", map, new VolleyInterface(GengxinService.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                LogUtil.i("定位更新成功" + result.toString());
            }

            @Override
            public void onError(VolleyError error) {
                LogUtil.i("定位更新失败" + error.toString());
            }

            @Override
            public void onStateError(int sta, String msg) {
                LogUtil.i("定位更新失败" + msg.toString());
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 获取触发event的传感器类型
        int sensorType = event.sensor.getType();
        switch (sensorType)
        {
            case Sensor.TYPE_ORIENTATION:
                // 获取绕Z轴转过的角度
                float degree = event.values[0];
                currentDegree = (int)degree;
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
