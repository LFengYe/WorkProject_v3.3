package cn.guugoo.jiapeiteacher.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xys.libzxing.zxing.activity.CaptureActivity;


import cn.guugoo.jiapeiteacher.R;
import cn.guugoo.jiapeiteacher.adapter.EvaluateListAdapter;
import cn.guugoo.jiapeiteacher.base.BaseActivity;
import cn.guugoo.jiapeiteacher.base.Constants;
import cn.guugoo.jiapeiteacher.bean.Start;
import cn.guugoo.jiapeiteacher.bean.StudentDetails;
import cn.guugoo.jiapeiteacher.bean.TotalData;
import cn.guugoo.jiapeiteacher.bean.WhenLongTime;
import cn.guugoo.jiapeiteacher.base.BaseAsyncTask;
import cn.guugoo.jiapeiteacher.util.BitmapUtil;
import cn.guugoo.jiapeiteacher.util.DateUtils;
import cn.guugoo.jiapeiteacher.util.EncryptUtils;
import cn.guugoo.jiapeiteacher.util.HttpUtil;
import cn.guugoo.jiapeiteacher.util.NetUtil;
import cn.guugoo.jiapeiteacher.view.CircleImageView;
import cn.guugoo.jiapeiteacher.view.MyDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gpw on 2016/8/4.
 * --加油
 */
public class StudentDetailsActivity extends BaseActivity implements Chronometer.OnChronometerTickListener {

    private CircleImageView civ_head;
    private TextView student_name1;
    private TextView student_name;
    private TextView student_sex;
    private TextView student_tel;
    private TextView student_cardId;
    private TextView student_driverType;
    private TextView student_bookingCourse;
    private TextView student_bookingTime;
    private TextView student_classHour;
    private ListView lv_student_details;
    private TextView tv_start;
    private TextView tv_end;
    private LinearLayout ll_time;
    private TextView tv_status;
    private String bookingId;
    private StudentDetails studentDetails;
    private EvaluateListAdapter mEvaluateListAdapter;
    private ArrayList<StudentDetails.EvaluateListBean> mEvaluateListBeen;
    private MyDialog endDialog;
    private Chronometer mChronometer;
    private final static int REQUEST_CODE = 45;
    private int CalculationTime;
    private int Whenlong;
    private String BookingDay;
    private String TimeSlot;
    private String token;
    private int StudentId;
    private int teacherId;
    private int SchoolId;
    private static final String IMAGE_FILE_NAME = "student.jpg";// 头像文件名称
    private boolean IsChronometerRun;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    @Override
    protected int getLayout() {
        return R.layout.activity_student_details;
    }

    @Override
    protected void initView() {

        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.head);
        TextView tv_center = (TextView) rl_head.findViewById(R.id.tv_center);
        ImageView iv_back = (ImageView) rl_head.findViewById(R.id.iv_back);
        tv_start = (TextView) findViewById(R.id.tv_start);
        civ_head = (CircleImageView) findViewById(R.id.civ_head);
        student_name1 = (TextView) findViewById(R.id.student_name1);
        student_name = (TextView) findViewById(R.id.student_name);
        student_sex = (TextView) findViewById(R.id.student_sex);
        student_tel = (TextView) findViewById(R.id.student_tel);
        student_cardId = (TextView) findViewById(R.id.student_cardId);
        student_driverType = (TextView) findViewById(R.id.student_driverType);
        student_bookingCourse = (TextView) findViewById(R.id.student_bookingCourse);
        student_bookingTime = (TextView) findViewById(R.id.student_bookingTime);
        student_classHour = (TextView) findViewById(R.id.student_classHour);
        lv_student_details = (ListView) findViewById(R.id.lv_student_details);
        ll_time = (LinearLayout) findViewById(R.id.ll_time);
        tv_end = (TextView) findViewById(R.id.tv_end);
        tv_status = (TextView) findViewById(R.id.tv_status);
        mChronometer = (Chronometer) findViewById(R.id.mChronometer);


        tv_center.setText("学员详情");
        iv_back.setOnClickListener(this);
        tv_start.setOnClickListener(this);
        tv_end.setOnClickListener(this);
        mChronometer.setOnChronometerTickListener(this);

        if (!NetUtil.checkNetworkConnection(StudentDetailsActivity.this)) {
            Toast.makeText(StudentDetailsActivity.this, R.string.Net_error, Toast.LENGTH_SHORT).show();
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("BookingId", bookingId);
        jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
        new ClassStudentDetailsAsyncTask(StudentDetailsActivity.this, HttpUtil.url_classStudentDetails, token).execute(jsonObject);
    }


    @Override
    protected void initData() {
        bookingId = getIntent().getStringExtra("bookingId");
        //teacherId = getIntent().getIntExtra("teacherId", 0);
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_NAME, MODE_PRIVATE);
        teacherId = prefs.getInt("Id", 0);
        token = prefs.getString("token", "");
        studentDetails = new StudentDetails();
        mEvaluateListBeen = new ArrayList<>();
        CalculationTime = 0;
        Whenlong = 0;
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(false);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.tv_start) {
            if (!NetUtil.checkNetworkConnection(StudentDetailsActivity.this)) {
                Toast.makeText(StudentDetailsActivity.this, R.string.Net_error, Toast.LENGTH_SHORT).show();
                return;
            }
            if (ContextCompat.checkSelfPermission(StudentDetailsActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(StudentDetailsActivity.this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        100);
            } else {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("BookingId", bookingId);
                jsonObject.addProperty("SchoolId", SchoolId);
                jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
                new CheckCalssStartAsyncTask(StudentDetailsActivity.this, HttpUtil.url_checkCalssStart, token).execute(jsonObject);
            }

        } else if (v.getId() == R.id.tv_end) {
            if (tv_end.getText().equals("结束")) {
                endDialog = MyDialog.endDialog(StudentDetailsActivity.this);
                endDialog.show();
                endDialog.setOnSettingListener(new MyDialog.EndListener() {
                    @Override
                    public void onSetting(String content) {
                        if (!NetUtil.checkNetworkConnection(StudentDetailsActivity.this)) {
                            Toast.makeText(StudentDetailsActivity.this, R.string.Net_error, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("BookingId", bookingId);
                        jsonObject.addProperty("EndPhoneTimeNow", DateUtils.getCurrentDate());
                        jsonObject.addProperty("ExceptionMsg", content);
                        System.out.println(jsonObject.toString());
                        jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
                        new ClassEndAsyncTask(StudentDetailsActivity.this, HttpUtil.url_classEnd, token).execute(jsonObject);
                    }
                });

            } else if (tv_end.getText().equals("请给予点评")) {
                Intent intent = new Intent(StudentDetailsActivity.this, TeacherCommentActivity.class);
                intent.putExtra("bookingId", bookingId);
                intent.putExtra("token", token);
                startActivityForResult(intent, REQUEST_CODE);
            }
        } else if (v.getId() == R.id.iv_back) {
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, getIntent());
        super.onBackPressed();
    }

    protected void callback(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 100: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("BookingId", bookingId);
                    jsonObject.addProperty("SchoolId", SchoolId);
                    jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
                    new CheckCalssStartAsyncTask(StudentDetailsActivity.this, HttpUtil.url_checkCalssStart, token).execute(jsonObject);
                } else {
                    Toast.makeText(StudentDetailsActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            setResult(RESULT_OK, getIntent());
            finish();
        }
        if (requestCode == 54 && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String result = bundle.getString("result");
            String result1 = bookingId + "&" + BookingDay + "&" + TimeSlot + "&" + StudentId + "&" + teacherId;
            System.out.println(result1);
            assert result != null;
            if (result.equals(result1)) {
                Toast.makeText(StudentDetailsActivity.this, "扫码信息匹配成功,请为学员拍照", Toast.LENGTH_SHORT).show();

                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //下面这句指定调用相机拍照后的照片存储的路径
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                startActivityForResult(takeIntent, 56);

            } else {
                Toast.makeText(StudentDetailsActivity.this, "扫码信息匹配不成功", Toast.LENGTH_SHORT).show();
            }

        }
        if (requestCode == 56) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(StudentDetailsActivity.this, "上传学员图片才能开始", Toast.LENGTH_SHORT).show();
                return;
            }

            File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
            Bitmap bitmap = BitmapUtil.qualityCompress(BitmapFactory.decodeFile(String.valueOf(temp)));
            String HeadPortrait = BitmapUtil.getImgStr(bitmap);
            String Suffix = ".JPEG";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("BookingId", bookingId);
            jsonObject.addProperty("SchoolId", SchoolId);
            jsonObject.addProperty("Image", HeadPortrait);
            jsonObject.addProperty("Suffix", Suffix);
            jsonObject.addProperty("PhoneTimeNow", DateUtils.getCurrentDate());
            System.out.println(jsonObject.toString());
            jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
            new ScanCodeStartAsyncTask(StudentDetailsActivity.this, HttpUtil.url_scanCodeStart, token).execute(jsonObject);
        }
    }

    @Override
    protected void onDestroy() {
        if (IsChronometerRun) {
            mChronometer.stop();
        }
        mLocationClient.unRegisterLocationListener(myListener);
        super.onDestroy();
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {

        CalculationTime = Miss(mChronometer.getText().toString());
        chronometer.setText(FormatMiss(CalculationTime));
        System.out.println(CalculationTime);
        if ((CalculationTime % 60) == 0 && CalculationTime < Whenlong * 60) {
            mLocationClient.start();
        }

        if (CalculationTime >= Whenlong * 60) {
            chronometer.setText(FormatMiss(Whenlong * 60));
            mLocationClient.start();
            mChronometer.stop();
            IsChronometerRun = false;
            tv_status.setText("已结束");
            tv_end.setText("请给予点评");
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("BookingId", bookingId);
            jsonObject.addProperty("EndPhoneTimeNow", DateUtils.getCurrentDate());
            jsonObject.addProperty("ExceptionMsg", "");
            jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
            new ClassEndAsyncTask(StudentDetailsActivity.this, HttpUtil.url_classEnd, token).execute(jsonObject);
        }
    }


    public static String FormatMiss(int miss) {
        String hh = miss / 3600 > 9 ? miss / 3600 + "" : "0" + miss / 3600;
        String mm = (miss % 3600) / 60 > 9 ? (miss % 3600) / 60 + "" : "0" + (miss % 3600) / 60;
        String ss = (miss % 3600) % 60 > 9 ? (miss % 3600) % 60 + "" : "0" + (miss % 3600) % 60;
        return hh + ":" + mm + ":" + ss;
    }

    public static int Miss(String miss) {
        String[] misses = miss.split(":");
        int hour;
        int minute;
        int second;
        if (misses.length > 2) {
            hour = Integer.valueOf(misses[0]);
            minute = Integer.valueOf(misses[1]);
            second = Integer.valueOf(misses[2]);
        } else {
            hour = 0;
            minute = Integer.valueOf(misses[0]);
            second = Integer.valueOf(misses[1]);
        }
        return second + minute * 60 + hour * 60 * 60;
    }

    class ClassStudentDetailsAsyncTask extends BaseAsyncTask {

        public ClassStudentDetailsAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(StudentDetailsActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                studentDetails = gson.fromJson(totalData.getData(), StudentDetails.class);
                mEvaluateListBeen = studentDetails.getEvaluateList();
                student_name.setText(getResources().getText(R.string.student_name) + studentDetails.getName());
                student_name1.setText(studentDetails.getName());
                student_sex.setText(getResources().getText(R.string.student_sex) + studentDetails.getSex());
                student_tel.setText(getResources().getText(R.string.student_tel) + studentDetails.getTel());
                student_cardId.setText(getResources().getText(R.string.student_cardId) + studentDetails.getCardId());
                student_driverType.setText(getResources().getText(R.string.student_driverType) + studentDetails.getDriverType());
                student_bookingTime.setText(getResources().getText(R.string.student_bookingTime) + studentDetails.getBookingTime());
                student_bookingCourse.setText(getResources().getText(R.string.student_bookingCourse) + studentDetails.getBookingAccount());
                student_classHour.setText(getResources().getText(R.string.student_classHour) + studentDetails.getClassHour());
                mEvaluateListAdapter = new EvaluateListAdapter(mEvaluateListBeen, StudentDetailsActivity.this);
                lv_student_details.setAdapter(mEvaluateListAdapter);

                Glide.with(StudentDetailsActivity.this)
                        .load(studentDetails.getHead())
                        .crossFade()
                        .skipMemoryCache(false)
                        .into(civ_head);
                int status = studentDetails.getStatus();
                Whenlong = studentDetails.getWhenLong();
                CalculationTime = studentDetails.getCalculationTime();
                BookingDay = studentDetails.getBookingDay();
                StudentId = studentDetails.getStudentId();
                SchoolId = studentDetails.getSchoolId();
                TimeSlot = studentDetails.getTimeSlot();
                int isComment = studentDetails.getIsComment();
                int StudyMinutes = studentDetails.getStudyMinutes();


                if (status == 1) {
                    tv_start.setVisibility(View.INVISIBLE);
                    ll_time.setVisibility(View.VISIBLE);
                    if (CalculationTime >= Whenlong * 60) {
                        mChronometer.setText(FormatMiss(Whenlong * 60));
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("BookingId", bookingId);
                        jsonObject.addProperty("EndPhoneTimeNow", DateUtils.getCurrentDate());
                        jsonObject.addProperty("ExceptionMsg", "");
                        jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
                        new ClassEndAsyncTask(StudentDetailsActivity.this, HttpUtil.url_classEnd, token).execute(jsonObject);
                        tv_status.setText("已结束");
                        if (isComment == 0) {
                            tv_end.setText("请给予点评");
                        } else {
                            tv_end.setText("已点评");
                        }

                    } else {
                        mChronometer.setBase(SystemClock.elapsedRealtime() - 1000 * CalculationTime);
                        mChronometer.setText(FormatMiss(CalculationTime));
                        mChronometer.start();
                        IsChronometerRun = true;
                    }
                }

                if (status == 3) {
                    tv_start.setVisibility(View.INVISIBLE);
                    ll_time.setVisibility(View.VISIBLE);
                    mChronometer.setText(FormatMiss(StudyMinutes * 60));
                    tv_status.setText("已过期");
                    tv_end.setText("无点评");
                }

                if (status == 4) {
                    tv_start.setVisibility(View.INVISIBLE);
                    ll_time.setVisibility(View.VISIBLE);
                    mChronometer.setText(FormatMiss(StudyMinutes * 60));
                    tv_status.setText("已结束");
                    if (isComment == 0) {
                        tv_end.setText("请给予点评");
                    } else {
                        tv_end.setText("已点评");
                    }
                }
            } else {
                Toast.makeText(StudentDetailsActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class CheckCalssStartAsyncTask extends BaseAsyncTask {


        public CheckCalssStartAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(StudentDetailsActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 1) {
                Start start = gson.fromJson(totalData.getData(), Start.class);
                if (("true").equals(start.getIsStart())) {
                    Intent intent = new Intent(StudentDetailsActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, 54);
                } else {
                    Toast.makeText(StudentDetailsActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(StudentDetailsActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class ScanCodeStartAsyncTask extends BaseAsyncTask {

        public ScanCodeStartAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }


        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(StudentDetailsActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                Toast.makeText(StudentDetailsActivity.this, "照片提交成功,预约开始", Toast.LENGTH_SHORT).show();
                tv_start.setVisibility(View.INVISIBLE);
                ll_time.setVisibility(View.VISIBLE);
                WhenLongTime whenLongTime = gson.fromJson(totalData.getData(), WhenLongTime.class);
                Whenlong = whenLongTime.getWhenLong();
                mChronometer.setBase(SystemClock.elapsedRealtime() - 1000 * CalculationTime);
                mChronometer.setText(FormatMiss(CalculationTime));
                mChronometer.start();
                IsChronometerRun = true;

            } else {
                Toast.makeText(StudentDetailsActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class ClassEndAsyncTask extends BaseAsyncTask {


        public ClassEndAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(StudentDetailsActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                if (endDialog != null) {
                    endDialog.dismiss();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StudentDetailsActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("本次课程已结束");
                    builder.setCancelable(false);
                    builder.setIcon(R.mipmap.hint);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("确定");
                        }
                    });
                    builder.show();
                }
                mChronometer.stop();
                IsChronometerRun = false;
                tv_status.setText("已结束");
                tv_end.setText("请给予点评");
            } else {
                Toast.makeText(StudentDetailsActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    class GpsStorageAsyncTask extends BaseAsyncTask {


        public GpsStorageAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(StudentDetailsActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                mLocationClient.stop();
            } else {
                //  Toast.makeText(StudentDetailsActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("上传错误！");
                mLocationClient.stop();
            }
        }
    }

    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Longitude", location.getLongitude());
            jsonObject.addProperty("Latitude", location.getLatitude());
            jsonObject.addProperty("BookingId", bookingId);
            jsonObject.addProperty("SchoolId", SchoolId);
            jsonObject.addProperty("UserType", 2);
            jsonObject.addProperty("CreateTime", DateUtils.getCurrentDate());
            System.out.println(jsonObject.toString());
            jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
            new GpsStorageAsyncTask(StudentDetailsActivity.this, HttpUtil.url_gpsStorage, token).execute(jsonObject);

            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.i("BaiduLocationApiDem", sb.toString());
        }
    }
}
