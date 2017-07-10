package cn.com.caronwer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.kyleduo.switchbutton.SwitchButton;
import com.nineoldandroids.view.ViewHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import cn.com.caronwer.GengxinService;
import cn.com.caronwer.R;
import cn.com.caronwer.activiBu.ConfirmOrderActivity;
import cn.com.caronwer.adapter.OrderAddressAdapter;
import cn.com.caronwer.adapter.QiangOrderAdapter;
import cn.com.caronwer.base.BaseActivity;
import cn.com.caronwer.base.Contants;
import cn.com.caronwer.bean.ADInfo;
import cn.com.caronwer.bean.CarInfo;
import cn.com.caronwer.bean.NewsInfo;
import cn.com.caronwer.bean.OrderAddressInfo;
import cn.com.caronwer.bean.OrderInfo;
import cn.com.caronwer.bean.QiangOrderInfo;
import cn.com.caronwer.bean.UserInfo;
import cn.com.caronwer.util.DensityUtil;
import cn.com.caronwer.util.EncryptUtil;
import cn.com.caronwer.util.ExitUtils;
import cn.com.caronwer.util.HttpUtil;
import cn.com.caronwer.util.LogUtil;
import cn.com.caronwer.util.NetworkUtil;
import cn.com.caronwer.util.SPtils;
import cn.com.caronwer.util.VolleyInterface;
import cn.com.caronwer.view.BaoJiaDialog;
import cn.com.caronwer.view.CircleImageView;
import cn.com.caronwer.view.CustomProgressDialog;
import cn.com.caronwer.view.ImageCycleView;
import cn.com.caronwer.view.MyDialog;
import cn.com.caronwer.view.XRecyclerView;

public class MainActivity extends BaseActivity implements OrderAddressAdapter.OnItemClickListener {

    private DrawerLayout mDrawerLayout;
    private ImageCycleView icv_banner;
    private CircleImageView civ_head;
    private ImageView iv_cir_head;
    private TextView mTv_shuaxin;
    private OrderAddressAdapter mOrderAddressAdapter;
    public UserInfo userInfo;
    private ArrayList<ADInfo> adInfos;
    private ArrayList<CarInfo> carInfos;
    private ArrayList<TextView> tvs_car;
    private ArrayList<OrderAddressInfo> mOrderAddressInfos;
    private TextView tv_car_1;
    private TextView tv_tel;
    private TextView tv_myOrder;
    private TextView tv_myInfo;
    private TextView tv_myWallet;
    private TextView tv_news;
    private TextView tv_news_num;
    private TextView tv_benefit_activity;
    private TextView tv_setting;
    private RelativeLayout rl_head;

    private ExitUtils exit = new ExitUtils();
    private LinearLayout ll_total_car;
    private String orderAddress;
    private OrderInfo orderInfo;
    private double payment;
    private FragmentTransaction ft;
    private SwitchButton mSb_content;
    private LinearLayout mLl_me;
    private TextView mTv_rz;

    private QiangOrderInfo mQiangOrderInfo;
    private TextView mTv_mz;
    private RatingBar mRb_leftdata;
    private TextView tv_fenshu;
    private XRecyclerView mRv_order;
    private QiangOrderAdapter mQiangOrderAdapter;
    private ArrayList<QiangOrderInfo> mQiangOrderInfoList;
    private int CurrentPage = 1;
    private String mNo = "";
    private String mTime = "";
    private Handler mHandler;
    private BaoJiaDialog mNickDialog;
    private TextView mTv_myCar;

    private String mAccount = "";
    private String mPassword = "";
    private boolean isDenglu = false;
    private UserInfo mUserInfo = new UserInfo("00000000-0000-0000-0000-000000000000", "", "", "", "", "", 0, 0, "", "", "",
            0, "", 0, "");

    private LocationClient mLocationClient = null;
    private BDLocation mLocation = null;
    private CustomProgressDialog progressDialog;

    private SharedPreferences prefs;
    private String password;
    private String account;
    //private ImageView mIv_root;
    private RelativeLayout mRl_xiuxi;
    private LinearLayout mLl_main;
    private MyDialog endDialog;
    public static int newsSize = 0;
    public static int nSize = 0;

    private boolean isFirstLoading = false;


    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void findById() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mSb_content = (SwitchButton) findViewById(R.id.sb_content);
        iv_cir_head = (ImageView) findViewById(R.id.iv_cir_head);
        icv_banner = (ImageCycleView) findViewById(R.id.icv_banner);
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        tv_myOrder = (TextView) findViewById(R.id.tv_myOrder);
        tv_myInfo = (TextView) findViewById(R.id.tv_myInfo);
        tv_myWallet = (TextView) findViewById(R.id.tv_myWallet);
        tv_news = (TextView) findViewById(R.id.tv_news);
        tv_news_num = (TextView) findViewById(R.id.tv_news_num);
        tv_benefit_activity = (TextView) findViewById(R.id.tv_benefit_activity);
        tv_setting = (TextView) findViewById(R.id.tv_setting);
        rl_head = (RelativeLayout) findViewById(R.id.rl_head);
        civ_head = (CircleImageView) findViewById(R.id.civ_head);
        mLl_me = (LinearLayout) findViewById(R.id.ll_me);
        mTv_rz = (TextView) findViewById(R.id.tv_rz);
        mTv_shuaxin = (TextView) findViewById(R.id.tv_shuaxin);
        mTv_mz = (TextView) findViewById(R.id.tv_mz);
        mTv_myCar = (TextView) findViewById(R.id.tv_myCar);

        tv_fenshu = (TextView) findViewById(R.id.tv_fenshu);
        mRb_leftdata = (RatingBar) findViewById(R.id.rb_leftdata);

        mRv_order = (XRecyclerView) findViewById(R.id.rv_order);

        //mIv_root = (ImageView) findViewById(R.id.iv_root);

        mRl_xiuxi = (RelativeLayout) findViewById(R.id.rl_xiuxi);
        mLl_main = (LinearLayout) findViewById(R.id.ll_main);


        tvs_car = new ArrayList<>();


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(45, 45);
            layoutParams.setMargins(DensityUtil.dip2px(MainActivity.this, 10.0f), DensityUtil.dip2px(MainActivity.this, 15.0f), 0, 0);
            iv_cir_head.setLayoutParams(layoutParams);
        }
    }

    @Override
    protected void initData() {

        isFirstLoading = true;
        progressDialog = new CustomProgressDialog(this);
        progressDialog.show();
        progressDialog.setText("获取定位中...");
        initLocationClient();

        prefs = getSharedPreferences(Contants.SHARED_NAME, MODE_PRIVATE);

        mAccount = prefs.getString("account", "");
        mPassword = prefs.getString("password", "");

        isDenglu = getIntent().getBooleanExtra("isDenglu", false);

        if (isDenglu) {
            userInfo = getIntent().getParcelableExtra("userInfo");
            startService(new Intent(MainActivity.this, GengxinService.class));
            getNews(1);
        } else {
            userInfo = mUserInfo;
        }

        adInfos = getIntent().getParcelableArrayListExtra("adInfos");
        orderInfo = new OrderInfo();
        mOrderAddressInfos = new ArrayList<>();
        OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
        orderAddressInfo.setAction(0);
        orderAddressInfo.setState(1);
        orderAddressInfo.setReceiptAddress("start");
        OrderAddressInfo orderAddressInfo1 = new OrderAddressInfo();
        orderAddressInfo1.setAction(1);
        orderAddressInfo1.setState(3);
        orderAddressInfo1.setReceiptAddress("start");
        mOrderAddressInfos.add(orderAddressInfo);
        mOrderAddressInfos.add(orderAddressInfo1);
        mOrderAddressAdapter = new OrderAddressAdapter(mOrderAddressInfos, this);
        mQiangOrderInfoList = new ArrayList<>();
        mQiangOrderAdapter = new QiangOrderAdapter(mQiangOrderInfoList);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mLocationClient.isStarted())
            mLocationClient.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationClient.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isFirstLoading = true;
        getNews(1);
    }

    @Override
    protected void initView() {

        if (isDenglu) {
            mSb_content.setChecked(true);
            change(true);//登录后默认开始工作
        }

        tv_tel.setText(userInfo.getTel());
        mTv_mz.setText(userInfo.getUserName());
        tv_fenshu.setText(userInfo.getScore() + "分");
        mRb_leftdata.setRating(userInfo.getScore());
        if (isDenglu == true) {
            SPtils.putString(MainActivity.this, "VehicleNo", userInfo.getVehicleNo());
        }

        String authenticateStatus = userInfo.getAuthenticateStatus();

        if (isDenglu == true) {
            switch (authenticateStatus) {
                case "1":
                    mTv_rz.setText("未认证");
                    break;
                case "2":
                    mTv_rz.setText("审核中");
                    break;
                case "3":
                    mTv_rz.setText("通过认证");
                    mTv_rz.setClickable(false);
                    break;
                case "4":
                    mTv_rz.setText("审核失败");
                    break;

            }
        } else {
            mTv_rz.setText("未登录");
        }


        HttpUtil.setImageLoader(Contants.imagehost + userInfo.getHeadIco(), civ_head, R.mipmap.cir_head, R.mipmap.cir_head);
        HttpUtil.setImageLoader(Contants.imagehost + userInfo.getHeadIco(), iv_cir_head, R.mipmap.cir_head, R.mipmap.cir_head);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                Gravity.LEFT);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mContent = mDrawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                if (drawerView.getTag().equals("LEFT")) {
                    ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
                    ViewHelper.setTranslationX(mContent, mMenu.getMeasuredWidth() * (1 - scale));
                    mContent.invalidate();

                } else {
                    ViewHelper.setTranslationX(mContent, -mMenu.getMeasuredWidth() * slideOffset);
                    mContent.invalidate();
                }

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                iv_cir_head.setVisibility(View.GONE);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                iv_cir_head.setVisibility(View.VISIBLE);
            }
        });

        mOrderAddressAdapter.setOnItemClickListener(this);
        iv_cir_head.setOnClickListener(this);
        tv_tel.setOnClickListener(this);
        tv_myOrder.setOnClickListener(this);
        tv_myInfo.setOnClickListener(this);
        tv_myWallet.setOnClickListener(this);
        tv_news.setOnClickListener(this);
        tv_benefit_activity.setOnClickListener(this);
        tv_setting.setOnClickListener(this);
        mTv_shuaxin.setOnClickListener(this);
        mTv_myCar.setOnClickListener(this);

        mSb_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.i("Click");
                if (isDenglu == false) {
                    mSb_content.setChecked(false);
                    tiaozhuan();
                } else {
                    //此处点击时, 状态已经改变
                    change(mSb_content.isChecked());
                }
            }
        });
        mLl_me.setOnClickListener(this);
        civ_head.setOnClickListener(this);
        mTv_rz.setOnClickListener(this);
        setTuPian();

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);

        mRv_order.setLayoutManager(layoutManager2);
        mRv_order.setAdapter(mQiangOrderAdapter);

        mRv_order.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                CurrentPage = 1;
                getOrders(CurrentPage, 0);
            }

            @Override
            public void onLoadMore() {
                CurrentPage++;
                getOrders(CurrentPage, 1);
            }
        });

        mQiangOrderAdapter.setOnBtnClickListener(new QiangOrderAdapter.OnBtnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onBtnClick(String time, String no, String viewName) {
                final String viewName2 = viewName;
                final String time2 = time;
                final String no2 = no;

                if (isDenglu == true) {
                    switch (viewName2) {
                        case "抢单承运":
                        {
                            mNo = no2;
                            mTime = time2;
                            endDialog = MyDialog.sureDialog(MainActivity.this);
                            endDialog.show();
                            endDialog.setOnSettingListener(new MyDialog.EndListener() {
                                @Override
                                public void onSetting(String content) {
                                    endDialog.dismiss();
                                    qdd();
                                }
                            });
                            break;
                        }
                        case "预约抢单":
                        {
                            mNo = no2;
                            mTime = time2;
                            endDialog = MyDialog.sureDialog(MainActivity.this);
                            endDialog.show();
                            endDialog.setOnSettingListener(new MyDialog.EndListener() {
                                @Override
                                public void onSetting(String content) {
                                    endDialog.dismiss();
                                    qdd();
                                }
                            });
                            break;
                        }
                        case "报价抢单":
                        {
                            mNo = no2;
                            baojia();
                            break;
                        }
                    }
                } else {
                    tiaozhuan();
                }
            }
        });

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
                    //在这里获取当前位置
                    mLocation = bdLocation;
                    LogUtil.i("MainActivity", "最新定位:lat:" + bdLocation.getLatitude() + ",lng:" + bdLocation.getLongitude());
                    if (isFirstLoading) {
                        getOrders(CurrentPage, 0);
                        isFirstLoading = false;
                    }
                }
            }
        };
        mLocationClient.registerLocationListener(mLocationListener);
        /****************百度地图定位客户端初始化结束****************/
        mLocation = new BDLocation();
    }

    /**
     * 报价抢单弹窗
     */
    private void baojia() {
        mNickDialog = BaoJiaDialog.nickDialog(MainActivity.this);
        mNickDialog.show();
        mNickDialog.setOnSettingListener(new BaoJiaDialog.NickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onSetting(String name) {
                if (isDenglu == true) {
                    if (name.isEmpty()) {
                        showShortToastByString("报价不能为空");
                        return;
                    }
                    baojiaqiangdan(name);
                } else {
                    tiaozhuan();
                }
            }
        });
    }

    /**
     * 报价抢单
     */
    private void baojiaqiangdan(String baojia) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", SPtils.getString(this, "UserId", "00000000-0000-0000-0000-000000000000"));
        jsonObject.addProperty("OrderNo", mNo);
        jsonObject.addProperty("Offer", baojia);
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(MainActivity.this, Contants.url_transporteroffer, "transporteroffer", map, new VolleyInterface(MainActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                mNickDialog.dismiss();
                showShortToastByString(getString(R.string.transporter_offer));
                LogUtil.i("报价返回:", result.toString());
                /*
                Intent intent = new Intent(MainActivity.this, MyOrderActivityMe.class);
                intent.putExtra("selectNo", "2");//跳转全部
                startActivityForResult(intent, 11);
                */
            }

            @Override
            public void onError(VolleyError error) {
                showShortToastByString(getString(R.string.timeoutError));
                mNickDialog.dismiss();
                Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStateError(int sta, String msg) {
                mNickDialog.dismiss();
                if (!TextUtils.isEmpty(msg)) {
                    showShortToastByString(msg);
                }
            }
        });
    }

    /**
     * 抢单承运
     */
    private void qdd() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", SPtils.getString(this, "UserId", "00000000-0000-0000-0000-000000000000"));
        jsonObject.addProperty("OrderNo", mNo);
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(MainActivity.this, Contants.url_graborder, "graborder", map, new VolleyInterface(MainActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                Toast.makeText(MainActivity.this, "抢单成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, MyOrderActivityMe.class);
                intent.putExtra("selectNo", "2");
                startActivity(intent);
            }

            @Override
            public void onError(VolleyError error) {
                showShortToastByString(getString(R.string.timeoutError));
                Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStateError(int sta, String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    showShortToastByString(msg);
                }
            }
        });
    }


    private void setTuPian() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("AdvertisingType", 2);
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(MainActivity.this, Contants.url_getAdvertisings, "getAdvertisings", map, new VolleyInterface(MainActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<ADInfo>>() {
                }.getType();
                adInfos = gson.fromJson(result, listType);
                icv_banner.setImageResources(adInfos, new ImageCycleView.ImageCycleViewListener() {
                    @Override
                    public void displayImage(String imageURL, ImageView imageView) {
                        imageURL = Contants.imagehost + imageURL;

                        HttpUtil.setImageLoader(imageURL, imageView, R.mipmap.ic_default, R.mipmap.ic_error_page);
                    }

                    @Override
                    public void onImageClick(ADInfo info, int position, View imageView) {

                    }
                });

            }

            @Override
            public void onError(VolleyError error) {
                showShortToastByString(getString(R.string.timeoutError));
            }

            @Override
            public void onStateError(int sta, String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    showShortToastByString(msg);
                }
            }
        });
    }


    /**
     * 修改工作状态
     *
     * @param isChecked
     */
    private void change(final boolean isChecked) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", SPtils.getString(this, "UserId", "00000000-0000-0000-0000-000000000000"));
        jsonObject.addProperty("IsWork", isChecked);
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(MainActivity.this, Contants.url_editWorkstatus, "editWorkstatus", map, new VolleyInterface(MainActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
//                Toast.makeText(MainActivity.this, isChecked == true?"开始工作":"开始休息", Toast.LENGTH_SHORT).show();
                if (isChecked) {
                    mRl_xiuxi.setVisibility(View.GONE);
                    mLl_main.setVisibility(View.VISIBLE);
                } else {
                    mRl_xiuxi.setVisibility(View.VISIBLE);
                    mLl_main.setVisibility(View.GONE);
                }
                mSb_content.setChecked(isChecked);
            }

            @Override
            public void onError(VolleyError error) {
                showShortToastByString(getString(R.string.timeoutError));
                mSb_content.setChecked(!isChecked);
            }

            @Override
            public void onStateError(int sta, String msg) {
                mSb_content.setChecked(!isChecked);
                if (!TextUtils.isEmpty(msg)) {
                    showShortToastByString(msg);
                }
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (isDenglu == true) {
            switch (v.getId()) {

                case R.id.bt_query://马上发货
                    Intent intent1 = new Intent(MainActivity.this, ConfirmOrderActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.iv_cir_head:
                    mDrawerLayout.openDrawer(GravityCompat.START);
                    break;


                case R.id.tv_myOrder://我的订单
                    intent = new Intent(MainActivity.this, MyOrderActivityMe.class);
                    intent.putExtra("selectNo", "1");
                    startActivity(intent);
                    break;
                case R.id.tv_myInfo:
                    intent = new Intent(MainActivity.this, PersonalInfoActivity.class);
                    intent.putExtra("userInfo", userInfo);
                    startActivityForResult(intent, 3);
                    break;
                case R.id.tv_myWallet://我的钱包-
                    intent = new Intent(MainActivity.this, MyWalletActivity.class);
                    startActivity(intent);
                    break;

                case R.id.tv_news://我的消息+
                    intent = new Intent(MainActivity.this, MyNewsActivity.class);

                    startActivityForResult(intent, 10);
                    break;
                case R.id.tv_benefit_activity://优惠活动
                    intent = new Intent(MainActivity.this, BenefitActivityActivity.class);
                    startActivity(intent);
                    break;
                case R.id.tv_setting://设置
                    intent = new Intent(MainActivity.this, SettingActivity.class);
                    intent.putExtra("userInfo", userInfo);
                    startActivityForResult(intent, 3);
                    break;
                case R.id.civ_head:   //个人信息
                    intent = new Intent(MainActivity.this, PersonalInfoActivity.class);
                    intent.putExtra("userInfo", userInfo);
                    startActivityForResult(intent, 3);
                    break;
                case R.id.tv_rz:    //c车主认证
                    if (mTv_rz.getText().equals("通过认证")) {
                        Toast.makeText(this, "用户已认证", Toast.LENGTH_SHORT).show();
                    } else {
                        intent = new Intent(MainActivity.this, AuthFirstActivity.class);
                        startActivity(intent);
                    }
                    break;

                case R.id.ll_me://抢单
                    intent = new Intent(MainActivity.this, MyOrderActivityMe.class);
                    intent.putExtra("selectNo", "1");
                    startActivityForResult(intent, 5);
                    break;

                case R.id.tv_shuaxin:
                    getOrders(CurrentPage, 0);
                    Toast.makeText(this, "刷新了", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_myCar:
                    //我的车辆
                    intent = new Intent(MainActivity.this, MyCarActivity.class);
                    intent.putExtra("userInfo", userInfo);
                    startActivityForResult(intent, 3);
                    break;


            }
        } else {
            tiaozhuan();
        }
    }


    @Override
    public void onItemClick(int position) {

    }


    @Override
    public void onActionClick(int position) {
        mOrderAddressInfos.remove(position);
        mOrderAddressAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {

            int position = data.getIntExtra("position", 0);
            int type = data.getIntExtra("type", 0);
            OrderAddressInfo orderAddressInfo = data.getParcelableExtra("orderAddressInfo");

            if (type == 2) {
                OrderAddressInfo old = mOrderAddressInfos.get(position);
                mOrderAddressInfos.set(position, orderAddressInfo);
                mOrderAddressInfos.add(old);
                mOrderAddressAdapter.notifyDataSetChanged();
            } else {
                mOrderAddressInfos.set(position, orderAddressInfo);
            }
            mOrderAddressAdapter.notifyDataSetChanged();


            int size = mOrderAddressInfos.size();
            for (int i = 0; i < size; i++) {
                orderAddressInfo = mOrderAddressInfos.get(i);
                payment += orderAddressInfo.getMoney();
                orderAddress = orderAddress + "|" + orderAddressInfo.toString();
                if (i == size - 1) {
                    orderAddress = orderAddress + orderAddressInfo.toString();
                }
            }

        }
        if (resultCode == RESULT_OK && requestCode == 3) {

            userInfo = data.getParcelableExtra("userInfo");
            tv_tel.setText(userInfo.getUserName());
            HttpUtil.setImageLoader(Contants.imagehost + userInfo.getHeadIco(), civ_head, R.mipmap.cir_head, R.mipmap.cir_head);
            HttpUtil.setImageLoader(Contants.imagehost + userInfo.getHeadIco(), iv_cir_head, R.mipmap.cir_head, R.mipmap.cir_head);
        }
        if (requestCode == 5 || requestCode == 11) {
            getOrders(CurrentPage, 0);
        }
        if (requestCode == 9) {
            getOrders(CurrentPage, 0);
            //mIv_root.setVisibility(View.GONE);
        }
        if (requestCode == 10) {
            newsSize = nSize;
            getNews(1);
        }
    }


    /**
     * 获取抢单列表
     *
     * @param PageIndex
     * @param ways
     */
    private void getOrders(int PageIndex, final int ways) {
        if (!NetworkUtil.isConnected(MainActivity.this)) {
            showShortToastByString(getString(R.string.Neterror));
            if (ways == 0) {
                mRv_order.refreshComplete("fail");
            } else {
                mRv_order.loadMoreComplete();
            }
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", SPtils.getString(this, "UserId", "00000000-0000-0000-0000-000000000000"));
        jsonObject.addProperty("Lat", mLocation.getLatitude());
        jsonObject.addProperty("Lng", mLocation.getLongitude());
        jsonObject.addProperty("PageIndex", PageIndex);
        jsonObject.addProperty("PageSize", 10);
        jsonObject.addProperty("VehType", userInfo.getVehicleType());
        jsonObject.addProperty("SortType", 1);

        if (progressDialog != null) {
            if (!progressDialog.isShowing())
                progressDialog.show();
        } else {
            progressDialog =new CustomProgressDialog(this);
            progressDialog.show();
        }
        progressDialog.setText(getString(R.string.loading));

        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(MainActivity.this, Contants.url_getorders, "getorders", map, new VolleyInterface(MainActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                progressDialog.dismiss();
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<QiangOrderInfo>>() {
                }.getType();
                ArrayList<QiangOrderInfo> newNewsInfo = gson.fromJson(result, listType);
                if (ways == 0) {
                    mRv_order.refreshComplete("success");
                    CurrentPage = 1;
                    mQiangOrderInfoList.clear();
                    mQiangOrderInfoList.addAll(newNewsInfo);
                } else {
                    mRv_order.loadMoreComplete();
                    if (newNewsInfo.size() < 15) {
                        mRv_order.setNoMore(true);
                    }
                    mQiangOrderInfoList.addAll(newNewsInfo);
                }
                mQiangOrderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.dismiss();
                if (ways == 0) {
                    mRv_order.refreshComplete("fail");

                } else {
                    mRv_order.loadMoreComplete();
                }

            }

            @Override
            public void onStateError(int sta, String msg) {
                progressDialog.dismiss();
                if (!TextUtils.isEmpty(msg)) {
                    showShortToastByString(msg);
                }
                if (ways == 0) {
                    mRv_order.refreshComplete("fail");
                } else {
                    mRv_order.loadMoreComplete();
                }
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            pressAgainExit();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 双击返回键离开
     */
    private void pressAgainExit() {
        if (exit.isExit()) {
            removeAll();
        } else {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            exit.doExitAction();
        }
    }

    /**
     * 獲取消息
     *
     * @param PageIndex
     */
    private void getNews(int PageIndex) {
        if (!NetworkUtil.isConnected(MainActivity.this)) {
            showShortToastByString(getString(R.string.Neterror));
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", SPtils.getString(this, "UserId", "00000000-0000-0000-0000-000000000000"));
        jsonObject.addProperty("UserType", 2);
        jsonObject.addProperty("PageIndex", PageIndex);
        jsonObject.addProperty("PageSize", 15);
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(MainActivity.this, Contants.url_getUserMessages, "getUserMessages", map, new VolleyInterface(MainActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<NewsInfo>>() {
                }.getType();
                ArrayList<NewsInfo> newNewsInfo = gson.fromJson(result, listType);
                nSize = newNewsInfo.size();
                if (newNewsInfo.size() - newsSize > 0) {
                    tv_news_num.setVisibility(View.VISIBLE);
                    tv_news_num.setText(newNewsInfo.size() - newsSize + "");
                } else {
                    tv_news_num.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onError(VolleyError error) {

            }

            @Override
            public void onStateError(int sta, String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    showShortToastByString(msg);
                }
            }
        });
    }


    public void tiaozhuan() {
        /*
        getWindow().getDecorView().setDrawingCacheEnabled(true);
        Bitmap bmp = getWindow().getDecorView().getDrawingCache();
        //高斯变换
        bmp = UtilsGaosi.doBlur(bmp, 5, false, MainActivity.this);
        BitmapDrawable bd = new BitmapDrawable(getResources(), bmp);

        mIv_root.setVisibility(View.VISIBLE);
        mIv_root.setBackground(bd);
        */

        Intent intent = new Intent();
        intent.setClass(MainActivity.this, Login2Activity.class);
        startActivityForResult(intent, 9);
    }

}


