package cn.com.goodsowner.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nineoldandroids.view.ViewHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import cn.com.goodsowner.R;
import cn.com.goodsowner.adapter.OrderAddressAdapter;
import cn.com.goodsowner.base.BaseActivity;
import cn.com.goodsowner.base.Contants;
import cn.com.goodsowner.bean.ADInfo;
import cn.com.goodsowner.bean.CarInfo;
import cn.com.goodsowner.bean.FreightInfo;
import cn.com.goodsowner.bean.GoodsInfo;
import cn.com.goodsowner.bean.OrderAddressInfo;
import cn.com.goodsowner.bean.UidInfo;
import cn.com.goodsowner.bean.UserInfo;
import cn.com.goodsowner.util.DateUtil;
import cn.com.goodsowner.util.DensityUtil;
import cn.com.goodsowner.util.EncryptUtil;
import cn.com.goodsowner.util.HttpUtil;
import cn.com.goodsowner.util.LogUtil;
import cn.com.goodsowner.util.MD5Util;
import cn.com.goodsowner.util.VolleyInterface;
import cn.com.goodsowner.view.CircleImageView;
import cn.com.goodsowner.view.CustomDatePicker;
import cn.com.goodsowner.view.CustomProgressDialog;
import cn.com.goodsowner.view.ImageCycleView;
import cn.com.goodsowner.view.MyDialog;


public class MainActivity extends BaseActivity implements OrderAddressAdapter.OnItemClickListener, MyDialog.LoginListener {

    private DrawerLayout mDrawerLayout;
    private ImageCycleView icv_banner;
    private CircleImageView civ_head;
    private ImageView iv_below;
    private LinearLayout iv_confirm_order;
    private ImageView iv_query;
    private CircleImageView iv_cir_head;
    private View view_status;

    private RecyclerView rv_main_address;
    private OrderAddressAdapter mOrderAddressAdapter;
    private Button bt_query;

    private UserInfo userInfo;
    private ArrayList<ADInfo> adInfos;
    private ArrayList<CarInfo.VehicleTypeListBean> carInfos;
    private ArrayList<TextView> tvs_car;
    private ArrayList<OrderAddressInfo> mOrderAddressInfos;

    private TextView tv_car_1;
    private TextView tv_tel;
    private TextView tv_myOrder;
    private TextView tv_myOrder1;
    private TextView tv_myWallet;
    private TextView tv_myConvoy;
    private TextView tv_common_address;
    private TextView tv_myInvoice;
    private TextView tv_news;
    private TextView tv_news_num;
    private TextView tv_benefit_activity;
    private TextView tv_setting;
    private TextView tv_volume;
    private TextView tv_start_money;
    private TextView tv_kg;
    private TextView tv_after;
    private TextView tv_money;
    private TextView tv_money_detail;
    private TextView tv_goods;

    private RelativeLayout rl_head;
    private RelativeLayout rl_car;
    private RelativeLayout rl_volume;
    private RelativeLayout rl_kg;
    private RelativeLayout rl_amount;
    private RelativeLayout rl_insurance;

    private EditText et_volume;
    private EditText et_volume1;
    private EditText et_kg;
    private EditText et_kg1;
    private EditText et_amount;
    private EditText et_toPayFreightTel;
    private EditText et_remark;


    private LinearLayout ll_total_car;
    private ImageView ll_booking_delivery;
    private ScrollView dialog_car;
    private LinearLayout ll_car_1;
    private LinearLayout ll_my_order;
    private LinearLayout ll_0;
    private LinearLayout ll_1;
    private LinearLayout ll_2;
    private LinearLayout ll_isToPayFreight;
    private ImageView iv_above_gray;
    private TextView tv_remark;

    private String orderAddress;
    private int vehideTypeId;
    private double payment;
    private boolean isPublish;
    private String startLatLng;
    private String endLatLng;
    private double premiums;
    private double freight;
    private double surcharge;
    private double ReturnPayRate;

    private String volume;
    private String kg;
    private String quantity;
    private String goodsType;
    private String goodsName;
    private String goodsPack;


    private boolean isRemove;
    private boolean isMove;
    private boolean isToPayFreight;

    private boolean isMyFleet;
    private boolean isSurcharge;
    private boolean isStart;

    private CheckBox cb_isRemove;
    private CheckBox cb_isMove;
    private CheckBox cb_isToPayFreight;
    private CheckBox cb_isSurcharge;
    private CheckBox cb_isMyFleet;
    private CheckBox cb_insurance;

    private EditText et_name;
    private EditText et_pack;
    private EditText et_name1;
    private EditText et_pack1;
    private RadioButton rb_type1;
    private RadioButton rb_type2;
    private RadioButton rb_type3;
    private RadioButton rb_type4;


    private Button bt_ok;
    private int cofirmTypeId;
    private String payFreightTel;
    private String remark;
    private long mExitTime;
    private CustomDatePicker timePickerView;
    //private DecimalFormat df;
    private MyDialog loginDialog;
    private MyDialog registerDialog;

    private boolean isLogin = false;
    private boolean isFirstLogin;

    private boolean isAgainLogin = false;
    private TextView tv_getCode;


    private SharedPreferences prefs;
    private String password;
    private String account;

    private MyCountDownTimer myCountDownTimer;


    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void findById() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        rv_main_address = (RecyclerView) findViewById(R.id.rv_main_address);
        bt_query = (Button) findViewById(R.id.bt_query);

        ll_car_1 = (LinearLayout) findViewById(R.id.ll_car_1);
        ll_total_car = (LinearLayout) findViewById(R.id.ll_total_car);
        ll_my_order = (LinearLayout) findViewById(R.id.ll_my_order);
        ll_booking_delivery = (ImageView) findViewById(R.id.ll_booking_delivery);
        rl_car = (RelativeLayout) findViewById(R.id.rl_car);
        rl_insurance = (RelativeLayout) findViewById(R.id.rl_insurance);

        cb_insurance = (CheckBox) findViewById(R.id.cb_insurance);

        iv_confirm_order = (LinearLayout) findViewById(R.id.iv_confirm_order);
        iv_cir_head = (CircleImageView) findViewById(R.id.iv_cir_head);
        icv_banner = (ImageCycleView) findViewById(R.id.icv_banner);
        iv_below = (ImageView) findViewById(R.id.iv_below);

        tv_car_1 = (TextView) findViewById(R.id.tv_car_1);


        tv_tel = (TextView) findViewById(R.id.tv_tel);
        tv_myOrder = (TextView) findViewById(R.id.tv_myOrder);
        tv_myOrder1 = (TextView) findViewById(R.id.tv_myOrder1);
        tv_myWallet = (TextView) findViewById(R.id.tv_myWallet);
        tv_myConvoy = (TextView) findViewById(R.id.tv_myConvoy);
        tv_common_address = (TextView) findViewById(R.id.tv_common_address);
        tv_myInvoice = (TextView) findViewById(R.id.tv_myInvoice);
        tv_news = (TextView) findViewById(R.id.tv_news);
        tv_news_num = (TextView) findViewById(R.id.tv_news_num);
        tv_benefit_activity = (TextView) findViewById(R.id.tv_benefit_activity);
        tv_setting = (TextView) findViewById(R.id.tv_setting);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_money_detail = (TextView) findViewById(R.id.tv_money_detail);
        tv_goods = (TextView) findViewById(R.id.tv_goods);

        rl_head = (RelativeLayout) findViewById(R.id.rl_head);
        iv_query = (ImageView) findViewById(R.id.iv_query);
        civ_head = (CircleImageView) findViewById(R.id.civ_head);
        view_status = findViewById(R.id.view_status);


        dialog_car = (ScrollView) findViewById(R.id.dialog_car);

        et_volume = (EditText) dialog_car.findViewById(R.id.et_volume);
        et_volume1 = (EditText) dialog_car.findViewById(R.id.et_volume1);
        et_kg = (EditText) dialog_car.findViewById(R.id.et_kg);
        et_kg1 = (EditText) dialog_car.findViewById(R.id.et_kg1);
        et_name = (EditText) dialog_car.findViewById(R.id.et_name);
        et_name1 = (EditText) dialog_car.findViewById(R.id.et_name1);

        et_pack = (EditText) dialog_car.findViewById(R.id.et_pack);
        et_pack1 = (EditText) dialog_car.findViewById(R.id.et_pack1);

        rb_type1 = (RadioButton) dialog_car.findViewById(R.id.rb_type1);
        rb_type2 = (RadioButton) dialog_car.findViewById(R.id.rb_type2);
        rb_type3 = (RadioButton) dialog_car.findViewById(R.id.rb_type3);
        rb_type4 = (RadioButton) dialog_car.findViewById(R.id.rb_type4);

        et_amount = (EditText) dialog_car.findViewById(R.id.et_amount);
        et_toPayFreightTel = (EditText) dialog_car.findViewById(R.id.et_toPayFreightTel);
        et_remark = (EditText) dialog_car.findViewById(R.id.et_remark);

        tv_volume = (TextView) dialog_car.findViewById(R.id.tv_volume);
        tv_start_money = (TextView) dialog_car.findViewById(R.id.tv_start_money);
        tv_kg = (TextView) dialog_car.findViewById(R.id.tv_kg);
        tv_after = (TextView) dialog_car.findViewById(R.id.tv_after);
        tv_remark = (TextView) dialog_car.findViewById(R.id.tv_remark);
        iv_above_gray = (ImageView) dialog_car.findViewById(R.id.iv_above_gray);

        rl_volume = (RelativeLayout) dialog_car.findViewById(R.id.rl_volume);
        rl_kg = (RelativeLayout) dialog_car.findViewById(R.id.rl_kg);
        rl_amount = (RelativeLayout) dialog_car.findViewById(R.id.rl_amount);
        ll_0 = (LinearLayout) dialog_car.findViewById(R.id.ll_0);
        ll_1 = (LinearLayout) dialog_car.findViewById(R.id.ll_1);
        ll_2 = (LinearLayout) dialog_car.findViewById(R.id.ll_2);
        ll_isToPayFreight = (LinearLayout) dialog_car.findViewById(R.id.ll_isToPayFreight);

        cb_isRemove = (CheckBox) dialog_car.findViewById(R.id.cb_isRemove);
        cb_isMove = (CheckBox) dialog_car.findViewById(R.id.cb_isMove);
        cb_isToPayFreight = (CheckBox) dialog_car.findViewById(R.id.cb_isToPayFreight);
        cb_isMyFleet = (CheckBox) dialog_car.findViewById(R.id.cb_isMyFleet);
        cb_isSurcharge = (CheckBox) dialog_car.findViewById(R.id.cb_isSurcharge);

        bt_ok = (Button) dialog_car.findViewById(R.id.bt_ok);


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(DensityUtil.dip2px(MainActivity.this, 10.0f), DensityUtil.dip2px(MainActivity.this, 15.0f), 0, 0);
            iv_cir_head.setLayoutParams(layoutParams);
            view_status.setVisibility(View.GONE);
        }


    }

    @Override
    protected void initData() {
        tvs_car = new ArrayList<>();
        mOrderAddressInfos = new ArrayList<>();

        adInfos = new ArrayList<>();
        carInfos = new ArrayList<>();
        //df = new DecimalFormat("#00.00");
        isStart = true;
        initStartData();
        prefs = getSharedPreferences(Contants.SHARED_NAME, MODE_PRIVATE);
        isFirstLogin = prefs.getBoolean("isFirstLogin", true);

        //LogUtil.i("isFirstLogin" + isFirstLogin);
    }


    private void initStartData() {
        premiums = 0;
        cofirmTypeId = -1;
        freight = 0;
        kg = "";
        quantity = "";
        volume = "";
        goodsName = "";
        goodsPack = "";
        goodsType = "";


        mOrderAddressInfos.clear();
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

        if (isStart) {
            mOrderAddressAdapter = new OrderAddressAdapter(mOrderAddressInfos, this);
            isStart = false;
        } else {
            tv_money.setText("¥00.00");
            mOrderAddressAdapter.notifyDataSetChanged();
        }
        getOrderInfo();

        isRemove = false;
        isMove = false;
        isToPayFreight = false;
        isMyFleet = false;
        isSurcharge = false;
    }

    private void initOrderData() {
        premiums = 0;
        cofirmTypeId = -1;
        freight = 0;
        kg = "";
        quantity = "";
        volume = "";
        goodsName = "";
        goodsPack = "";
        goodsType = "";


        cb_insurance.setChecked(false);
        rl_insurance.setVisibility(View.GONE);

        mOrderAddressInfos.clear();
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

        if (isStart) {
            mOrderAddressAdapter = new OrderAddressAdapter(mOrderAddressInfos, this);
            isStart = false;
        } else {
            tv_money.setText("¥00.00");
            mOrderAddressAdapter.notifyDataSetChanged();
        }
        getOrderInfo();

        ll_isToPayFreight.setVisibility(View.VISIBLE);
        cb_isMove.setChecked(false);
        cb_isMyFleet.setChecked(false);
        cb_isSurcharge.setChecked(false);
        cb_isToPayFreight.setChecked(false);
        cb_isToPayFreight.setVisibility(View.VISIBLE);
        cb_isRemove.setChecked(false);
        isRemove = false;
        isMove = false;
        isToPayFreight = false;
        isMyFleet = false;
        isSurcharge = false;
    }


    @Override
    protected void initView() {


        initCarType();
        initAdvertisings();
        if (!isFirstLogin) {
            account = prefs.getString("account", "");
            password = prefs.getString("password", "");
            login(account, password);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(true);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_main_address.setLayoutManager(layoutManager);
        rv_main_address.setAdapter(mOrderAddressAdapter);
        tv_tel.setText("未登录");
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.LEFT);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mContent = mDrawerLayout.getChildAt(0);
                float scale = 1 - slideOffset;
                if (drawerView.getTag().equals("LEFT")) {
                    ViewHelper.setAlpha(drawerView, 0.6f + 0.4f * (1 - scale));
                    ViewHelper.setTranslationX(mContent, drawerView.getMeasuredWidth() * (1 - scale));
                    mContent.invalidate();

                } else {
                    ViewHelper.setTranslationX(mContent, -drawerView.getMeasuredWidth() * slideOffset);
                    mContent.invalidate();
                }

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                iv_cir_head.setVisibility(View.GONE);
                if (Contants.newsNum > 0) {
                    tv_news_num.setVisibility(View.VISIBLE);
                    tv_news_num.setText(Contants.newsNum);
                } else {
                    tv_news_num.setVisibility(View.GONE);
                }

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                iv_cir_head.setVisibility(View.VISIBLE);
            }
        });

        mOrderAddressAdapter.setOnItemClickListener(this);
        ll_my_order.setOnClickListener(this);
        bt_query.setOnClickListener(this);
        iv_cir_head.setOnClickListener(this);
        iv_below.setOnClickListener(this);
        ll_car_1.setOnClickListener(this);
        iv_confirm_order.setOnClickListener(this);
        tv_tel.setOnClickListener(this);
        tv_myOrder.setOnClickListener(this);
        tv_myOrder1.setOnClickListener(this);
        tv_myWallet.setOnClickListener(this);
        tv_myConvoy.setOnClickListener(this);
        tv_common_address.setOnClickListener(this);
        tv_myInvoice.setOnClickListener(this);
        tv_news.setOnClickListener(this);
        tv_benefit_activity.setOnClickListener(this);
        tv_setting.setOnClickListener(this);
        rl_head.setOnClickListener(this);
        iv_above_gray.setOnClickListener(this);
        rl_volume.setOnClickListener(this);
        rl_kg.setOnClickListener(this);
        rl_amount.setOnClickListener(this);
        rl_insurance.setOnClickListener(this);
        tv_money_detail.setOnClickListener(this);
        bt_ok.setOnClickListener(this);
        ll_booking_delivery.setOnClickListener(this);
        iv_query.setOnClickListener(this);


        cb_insurance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rl_insurance.setVisibility(View.VISIBLE);
                    premiums = 100;
                    if (isPublish) {
                        if (isSurcharge) {
                            surcharge = freight * ReturnPayRate / 100;
                        } else {
                            surcharge = 0;
                        }
                        double money = freight + premiums + surcharge;
                        tv_money.setText(String.format("¥%.2f", money));
                    }

                } else {
                    rl_insurance.setVisibility(View.GONE);
                    premiums = 0;
                    if (isPublish) {
                        if (isSurcharge) {
                            surcharge = freight * ReturnPayRate / 100;
                        } else {
                            surcharge = 0;
                        }
                        double money = freight + premiums + surcharge;
                        tv_money.setText(String.format("¥%.2f", money));
                        //tv_money.setText(String.format("¥%s", df.format(money)));
                    }
                }
            }
        });

        initTimePick();


    }

    private void initAdvertisings() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("AdvertisingType", 1);
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
                        HttpUtil.setImageLoader(imageURL, imageView, R.mipmap.ic_default, R.mipmap.ic_default);
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
            public void onStateError() {
            }
        });
    }

    private void initCarType() {
        JsonObject jsonObject = new JsonObject();
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(MainActivity.this, Contants.url_getVehicleTypes, "getVehicleTypes", map, new VolleyInterface(MainActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                LogUtil.i(result.toString());
                Gson gson = new Gson();
                CarInfo carInfo1 = gson.fromJson(result, CarInfo.class);
                ReturnPayRate = carInfo1.getReturnPayRate();
                carInfos = (ArrayList<CarInfo.VehicleTypeListBean>) carInfo1.getVehicleTypeList();
                int size = carInfos.size();
                for (int i = 0; i < size; i++) {
                    LinearLayout.LayoutParams car_params = new LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                    LinearLayout.LayoutParams view_params = new LinearLayout.LayoutParams(
                            DensityUtil.dip2px(MainActivity.this, 0.5f), LinearLayout.LayoutParams.MATCH_PARENT);
                    view_params.setMargins(0, DensityUtil.dip2px(MainActivity.this, 5.0f), 0, DensityUtil.dip2px(MainActivity.this, 5.0f));
                    final CarInfo.VehicleTypeListBean carInfo = carInfos.get(i);
                    LinearLayout linearLayout = (LinearLayout) View.inflate(MainActivity.this, R.layout.view_ll_car, null);
                    ImageView iv_car = (ImageView) linearLayout.findViewById(R.id.iv_car);
                    TextView tv_car = (TextView) linearLayout.findViewById(R.id.tv_car);
                    tv_car.setText(carInfo.getVehicleTypeName());
                    tvs_car.add(tv_car);
                    String imgUrl = Contants.imagehost + carInfo.getImg();
                    // LogUtil.i(imgUrl);
                    HttpUtil.setImageLoader(imgUrl, iv_car, 0, 0);
                    View view = View.inflate(MainActivity.this, R.layout.view_line, null);
                    ll_total_car.addView(view, view_params);
                    ll_total_car.addView(linearLayout, car_params);

                    linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int type = Integer.valueOf(carInfo.getTypeCode());
                            choiceCar(type - 1);
                        }
                    });
                }
                choiceCar(4);
            }

            @Override
            public void onError(VolleyError error) {
                showShortToastByString(getString(R.string.timeoutError));
            }

            @Override
            public void onStateError() {
            }
        });
    }

    private void initTimePick() {
        timePickerView = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                // LogUtil.i(time);
                if (cofirmTypeId == 0) {
                    publishCarpool(2, time);
                } else {
                    sendOrder(2, time);
                }
            }
        }, "2010-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行

        timePickerView.showSpecificTime(true); // 显示时和分
        timePickerView.setIsLoop(true); // 允许循环滚动
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (!isLogin && v.getId() != R.id.ll_car_1) {
            showShortToastByString("请您先登录！");

            if (loginDialog != null) {
                loginDialog.dismiss();
            }
            loginDialog = MyDialog.loginDialog(MainActivity.this, "", "");
            loginDialog.setOnLoginListener(MainActivity.this);
            loginDialog.show();
            return;
        }
        switch (v.getId()) {
            case R.id.ll_car_1:
                choiceCar(4);
                break;
            case R.id.ll_my_order:
                intent = new Intent(MainActivity.this, MyOrderActivity.class);
                intent.putExtra("UserId", userInfo.getUserId());
                startActivity(intent);
                break;
            case R.id.bt_query:
                if (cofirmTypeId == 0) {
                    publishCarpool(3, DateUtil.getCurrentDates());
                } else {
                    sendOrder(3, DateUtil.getCurrentDates());
                }
                break;
            case R.id.rl_insurance:
                intent = new Intent(MainActivity.this, CargoInsuranceActivity.class);
                startActivityForResult(intent, 2);
                break;
            case R.id.iv_cir_head:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.iv_query:
                intent = new Intent(MainActivity.this, QueryRuleActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_ok:

                if (vehideTypeId==0) {
                    goodsPack = et_pack.getText().toString().trim();
                    goodsName = et_name.getText().toString().trim();
                    volume = et_volume.getText().toString().trim();
                    kg = et_kg.getText().toString().trim();
                    if (rb_type1.isChecked()) {
                        goodsType = "泡货";
                    } else {
                        goodsType = "重货";
                    }
                }else {
                    goodsPack = et_pack1.getText().toString().trim();
                    goodsName = et_name1.getText().toString().trim();
                    volume = et_volume1.getText().toString().trim();
                    kg = et_kg1.getText().toString().trim();
                    if (rb_type3.isChecked()) {
                        goodsType = "泡货";
                    } else {
                        goodsType = "重货";
                    }
                }


                if (goodsPack.isEmpty()) {
                    showShortToastByString("货物打包方式不能为空");
                    return;
                }
                if (goodsName.isEmpty()) {
                    showShortToastByString("货物名字不能为空");
                    return;
                }
                if (volume.isEmpty()) {
                    showShortToastByString("货物的体积不能为空");
                    return;
                }
                if (kg.isEmpty()) {
                    showShortToastByString("货物的重量不能为空");
                    return;
                }
                if (Double.valueOf(volume) < 0) {
                    showShortToastByString("货物体积不得小于0");
                    return;
                }
                if (Double.valueOf(kg) < 0) {
                    showShortToastByString("货物重量不得大小于0");
                    return;
                }
                if (vehideTypeId == 0) {
                    quantity = et_amount.getText().toString().trim();
                    if (quantity.isEmpty()) {
                        showShortToastByString("货物数量不得为空");
                        return;
                    }
                    if (Double.valueOf(quantity) < 0) {
                        showShortToastByString("货物数量不得大小于0");
                        return;
                    }
                    cb_isRemove.setChecked(false);
                } else if (vehideTypeId > 0 && vehideTypeId < 5) {
                    if (Double.valueOf(volume) > carInfos.get(vehideTypeId - 1).getVolume()) {
                        showShortToastByString("货物体积不得大于车辆的体积，否则请更换车辆类型发布资源");
                        return;
                    }
                    if (Double.valueOf(kg) > carInfos.get(vehideTypeId - 1).getLoadWeight()) {
                        showShortToastByString("货物重量不得大于车辆的载重量，否则请更换车辆类型发布资源");
                        return;
                    }
                }

                payFreightTel = et_toPayFreightTel.getText().toString();
                isRemove = cb_isRemove.isChecked();
                isMove = cb_isMove.isChecked();
                isToPayFreight = cb_isToPayFreight.isChecked();
                isMyFleet = cb_isMyFleet.isChecked();
                isSurcharge = cb_isSurcharge.isChecked();
                remark = et_remark.getText().toString();

                if (isToPayFreight && payFreightTel.isEmpty()) {
                    showShortToastByString("号码不能为空");
                    return;
                }
                cofirmTypeId = vehideTypeId;
                int white = 0x00000000;
                rl_car.setBackgroundColor(white);
                rl_car.setClickable(false);
                dialog_car.setVisibility(View.GONE);
                calculateFreight();
                break;

            case R.id.tv_money_detail:
                if (!isPublish) {
                    showShortToastByString("地址信息不完整");
                    return;
                }
                if (cofirmTypeId == -1) {
                    showShortToastByString("未确定规格");
                    return;
                }
                intent = new Intent(MainActivity.this, MoneyDetailActivity.class);
                intent.putExtra("freight", freight);
                intent.putExtra("premiums", premiums);
                intent.putExtra("amount", 0);
                if (isSurcharge) {
                    intent.putExtra("IsSurcharge", freight * ReturnPayRate / 100);
                } else {
                    intent.putExtra("IsSurcharge", 0);
                }
                startActivity(intent);
                break;
            case R.id.iv_confirm_order:
                if (cofirmTypeId == 0) {
                    publishCarpool(1, DateUtil.getCurrentDates());
                } else {
                    sendOrder(1, DateUtil.getCurrentDates());
                }
                break;
            case R.id.ll_booking_delivery:
                timePickerView.show(DateUtil.getCurrentDates());
                break;
            case R.id.iv_below:
                int gray = 0x77000000;
                /*
                if (cofirmTypeId == -1) {
                    cb_isRemove.setChecked(false);
                    cb_isMove.setChecked(false);
                    cb_isToPayFreight.setChecked(false);
                    cb_isMyFleet.setChecked(false);
                    cb_isSurcharge.setChecked(false);
                    et_remark.setText("");
                    et_toPayFreightTel.setText("");
                    et_amount.setText("");
                    et_kg.setText("");
                    et_volume.setText("");
                    et_name.setText("");
                    et_name1.setText("");
                    et_pack.setText("");
                    et_pack1.setText("");
                }
                */
                rl_car.setBackgroundColor(gray);
                rl_car.setClickable(true);
                dialog_car.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_above_gray:
                int white1 = 0x00000000;
                rl_car.setBackgroundColor(white1);
                rl_car.setClickable(false);
                dialog_car.setVisibility(View.GONE);
                break;
            case R.id.tv_myOrder:
                intent = new Intent(MainActivity.this, MyOrderActivity.class);
                intent.putExtra("UserId", userInfo.getUserId());
                startActivity(intent);
                break;
            case R.id.tv_myOrder1:
                intent = new Intent(MainActivity.this, OrderActivity.class);
                intent.putExtra("UserId", userInfo.getUserId());
                startActivity(intent);
                break;
            case R.id.tv_myWallet:
                intent = new Intent(MainActivity.this, MyWalletActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_myConvoy:
                intent = new Intent(MainActivity.this, MyConvoyActivity.class);
                intent.putExtra("UserId", userInfo.getUserId());
                startActivity(intent);
                break;
            case R.id.tv_common_address:
                intent = new Intent(MainActivity.this, CommonAddressActivity.class);
                intent.putExtra("userId", userInfo.getUserId());
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
            case R.id.tv_myInvoice:
                intent = new Intent(MainActivity.this, MyInvoiceActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_news:
                intent = new Intent(MainActivity.this, MyNewsActivity.class);
                Contants.newsNum = 0;
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("newsNum", Contants.newsNum);
                editor.apply();
                startActivity(intent);
                break;
            case R.id.tv_benefit_activity:
                intent = new Intent(MainActivity.this, BenefitActivityActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_setting:
                intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivityForResult(intent, 6);
                break;
            case R.id.rl_head:
                intent = new Intent(MainActivity.this, PersonalInfoActivity.class);
                intent.putExtra("userInfo", userInfo);
                startActivityForResult(intent, 3);
                break;
        }
    }


    private void initColor() {
        tv_car_1.setTextColor(ContextCompat.getColor(this, R.color.color_gary_font));
        int size = tvs_car.size();
        for (int i = 0; i < size; i++) {
            tvs_car.get(i).setTextColor(ContextCompat.getColor(this, R.color.color_gary_font));
        }
    }

    private void choiceCar(int i) {
        initColor();
        CarInfo.VehicleTypeListBean carInfo = null;
        if (i != 4) {
            carInfo = carInfos.get(i);
        }
        switch (i) {
            case 4:
                tv_car_1.setTextColor(ContextCompat.getColor(this, R.color.color_red));
                Visible1();
                vehideTypeId = 0;
                break;
            case 0:
                tvs_car.get(0).setTextColor(ContextCompat.getColor(this, R.color.color_red));
                Visible(carInfo, 1);
                vehideTypeId = 1;
                cb_isRemove.setText("全拆座");
                break;
            case 1:
                tvs_car.get(1).setTextColor(ContextCompat.getColor(this, R.color.color_red));
                Visible(carInfo, 2);
                vehideTypeId = 2;
                cb_isRemove.setText("全拆座");
                break;
            case 2:
                tvs_car.get(2).setTextColor(ContextCompat.getColor(this, R.color.color_red));
                Visible(carInfo, 3);
                vehideTypeId = 3;
                cb_isRemove.setText("开顶");
                break;
            case 3:
                tvs_car.get(3).setTextColor(ContextCompat.getColor(this, R.color.color_red));
                Visible(carInfo, 4);
                vehideTypeId = 4;
                cb_isRemove.setText("开顶");
                break;
        }

    }

    private void Visible(CarInfo.VehicleTypeListBean carInfo, int type) {
        ll_0.setVisibility(View.GONE);
        ll_1.setVisibility(View.VISIBLE);
        ll_2.setVisibility(View.VISIBLE);
        cb_isRemove.setVisibility(View.VISIBLE);

        if (cofirmTypeId != type) {
            cb_isRemove.setChecked(false);
            cb_isMove.setChecked(false);
            cb_isToPayFreight.setChecked(false);
            cb_isMyFleet.setChecked(false);
            cb_isSurcharge.setChecked(false);
            et_remark.setText("");
            et_toPayFreightTel.setText("");
            et_kg1.setText("");
            et_volume1.setText("");
            et_name1.setText("");
            et_pack1.setText("");
            rb_type3.setChecked(true);
        } else {
            cb_isRemove.setChecked(isRemove);
            cb_isMove.setChecked(isMove);
            cb_isToPayFreight.setChecked(isToPayFreight);
            cb_isMyFleet.setChecked(isMyFleet);
            cb_isSurcharge.setChecked(isSurcharge);
            et_remark.setText(remark);

            et_kg1.setText(kg);
            et_volume1.setText(volume);
            et_name1.setText(goodsName);
            et_pack1.setText(goodsPack);
            if (goodsType.equals("泡货")){
                rb_type3.setChecked(true);
            }else {
                rb_type4.setChecked(false);
            }
            if (!isToPayFreight) {
                et_toPayFreightTel.setText("");
            }
            et_toPayFreightTel.setText(payFreightTel);
        }
        tv_volume.setText(String.format("运输体积:%sm³", carInfo.getVolume()));
        tv_kg.setText(String.format("载重:%skg", carInfo.getLoadWeight()));
        tv_after.setText(String.format("后续:%s元/Km", carInfo.getFollowPrice()));
        tv_start_money.setText(String.format("起步价:%s元/5Km", carInfo.getStartingPrice()));
        tv_remark.setText(String.format("注:%s", carInfo.getRemark()));
    }

    private void Visible1() {
        ll_0.setVisibility(View.VISIBLE);
        ll_1.setVisibility(View.GONE);
        ll_2.setVisibility(View.GONE);
        cb_isRemove.setVisibility(View.GONE);
        if (cofirmTypeId != 0) {
            cb_isRemove.setChecked(false);
            cb_isMove.setChecked(false);
            cb_isToPayFreight.setChecked(false);
            cb_isMyFleet.setChecked(false);
            cb_isSurcharge.setChecked(false);
            et_remark.setText("");
            et_toPayFreightTel.setText("");
            et_amount.setText("");
            et_kg.setText("");
            et_volume.setText("");
            et_name.setText("");
            et_pack.setText("");
            rb_type3.setChecked(true);
        } else {
            cb_isRemove.setChecked(isRemove);
            cb_isMove.setChecked(isMove);
            cb_isToPayFreight.setChecked(isToPayFreight);
            cb_isMyFleet.setChecked(isMyFleet);
            cb_isSurcharge.setChecked(isSurcharge);
            et_remark.setText(remark);
            et_toPayFreightTel.setText(payFreightTel);
            et_amount.setText(quantity);
            et_kg.setText(kg);
            et_volume.setText(volume);
            et_name.setText(goodsName);
            et_pack.setText(goodsPack);
            if (goodsType.equals("泡货")){
                rb_type3.setChecked(true);
            }else {
                rb_type4.setChecked(false);
            }
            if (!isToPayFreight) {
                et_toPayFreightTel.setText("");
            }
        }
    }


    private void sendOrder(int type, String time) {

        if (!isPublish) {
            showShortToastByString("地址信息不完整");
            return;
        }
        if (cofirmTypeId == -1) {
            showShortToastByString("未确定规格");
            return;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("SendUserId", userInfo.getUserId());
        jsonObject.addProperty("VehideTypeId", cofirmTypeId);
        jsonObject.addProperty("IsRemove", isRemove);
        jsonObject.addProperty("IsMove", isMove);
        jsonObject.addProperty("IsSurcharge", isSurcharge);
        jsonObject.addProperty("IsToPayFreight", isToPayFreight);
        jsonObject.addProperty("ToPayFreightTel", payFreightTel);
        jsonObject.addProperty("Payment", payment);
        jsonObject.addProperty("IsMyFleet", isMyFleet);
        jsonObject.addProperty("OrderAddress", orderAddress);
        jsonObject.addProperty("Premiums", premiums);
        jsonObject.addProperty("Remark", remark);
        jsonObject.addProperty("OrderType", type);
        jsonObject.addProperty("PlanSendTime", time);
        jsonObject.addProperty("GoodsName", goodsName);
        jsonObject.addProperty("GoodsType", goodsType);
        jsonObject.addProperty("GoodsWay", goodsPack);
        jsonObject.addProperty("GoodsWeight", Double.valueOf(volume));
        jsonObject.addProperty("GoodsSize", Double.valueOf(kg));
        String mapJson = jsonObject.toString();
        //LogUtil.i(jsonObject.toString());

        int size = mOrderAddressInfos.size();
        ArrayList<GoodsInfo.Addre> addres = new ArrayList<>();
        for (int i=0;i<size;i++){
            String[] address=mOrderAddressInfos.get(i).getReceiptAddress().split("  ");
            String ad_place = address[0];
            String ad_city =address[1].substring(2,address[1].length()-1);
            String ad_tel =mOrderAddressInfos.get(i).getReceiptTel();
            String ad_name =mOrderAddressInfos.get(i).getReceipter();
            GoodsInfo.Addre addre = new GoodsInfo.Addre();
            addre.setAd_city(ad_city);
            addre.setAd_name(ad_name);
            addre.setAd_place(ad_place);
            addre.setAd_tel(ad_tel);
            addres.add(addre);
        }
        GoodsInfo goodsInfo = new GoodsInfo();
        goodsInfo.setAddres(addres);
        goodsInfo.setMove(isMove);
        goodsInfo.setKg(kg);
        goodsInfo.setVolume(volume);
        goodsInfo.setName(goodsName);
        goodsInfo.setType(goodsType);
        goodsInfo.setPack(goodsPack);
        goodsInfo.setRemark(remark);
        goodsInfo.setTime(time);

        String money = tv_money.getText().toString();
        Intent intent = new Intent(MainActivity.this, ConfirmActivity.class);
        intent.putExtra("goodsInfo",goodsInfo);
        intent.putExtra("type",type);
        intent.putExtra("IsToPayFreight",isToPayFreight);
        intent.putExtra("mapJson", mapJson);
        intent.putExtra("money", money);
        intent.putExtra("carType", 1);
        startActivityForResult(intent,14);

    }

    private void publishCarpool(int type, String time) {
        if (!isPublish) {
            showShortToastByString("地址信息不完整");
            return;
        }
        if (cofirmTypeId == -1) {
            showShortToastByString("未确定规格");
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("SendUserId", userInfo.getUserId());
        jsonObject.addProperty("Volume", Double.valueOf(volume));
        jsonObject.addProperty("Weight", Double.valueOf(kg));
        jsonObject.addProperty("Quantity", Integer.valueOf(quantity));
        jsonObject.addProperty("IsMove", isMove);
        jsonObject.addProperty("IsSurcharge", isSurcharge);
        jsonObject.addProperty("IsToPayFreight", isToPayFreight);
        jsonObject.addProperty("ToPayFreightTel", payFreightTel);
        jsonObject.addProperty("Payment", payment);
        jsonObject.addProperty("IsMyFleet", isMyFleet);
        jsonObject.addProperty("OrderAddress", orderAddress);
        jsonObject.addProperty("Premiums", premiums);
        jsonObject.addProperty("GoodsName", goodsName);
        jsonObject.addProperty("GoodsType", goodsType);
        jsonObject.addProperty("GoodsWay", goodsPack);
        jsonObject.addProperty("Remark", remark);
        jsonObject.addProperty("OrderType", type);
        jsonObject.addProperty("PlanSendTime", time);

        //LogUtil.i(jsonObject.toString());
        String mapJson = jsonObject.toString();

        GoodsInfo goodsInfo = new GoodsInfo();
        int size = mOrderAddressInfos.size();
        ArrayList<GoodsInfo.Addre> addres = new ArrayList<>();
        for (int i=0;i<size;i++){
            String[] address=mOrderAddressInfos.get(i).getReceiptAddress().split("  ");
            String ad_place = address[0];
            String ad_city =address[1].substring(2,address[1].length()-1);
            String ad_tel =mOrderAddressInfos.get(i).getReceiptTel();
            String ad_name =mOrderAddressInfos.get(i).getReceipter();
            GoodsInfo.Addre addre = new GoodsInfo.Addre();
            addre.setAd_city(ad_city);
            addre.setAd_name(ad_name);
            addre.setAd_place(ad_place);
            addre.setAd_tel(ad_tel);
            addres.add(addre);
        }
        goodsInfo.setAddres(addres);
        goodsInfo.setMove(isMove);
        goodsInfo.setKg(kg);
        goodsInfo.setVolume(volume);
        goodsInfo.setName(goodsName);
        goodsInfo.setType(goodsType);
        goodsInfo.setPack(goodsPack);
        goodsInfo.setRemark(remark);
        goodsInfo.setTime(time);

        String money = tv_money.getText().toString();
        Intent intent = new Intent(MainActivity.this, ConfirmActivity.class);
        intent.putExtra("goodsInfo",goodsInfo);
        intent.putExtra("type",type);
        intent.putExtra("IsToPayFreight",isToPayFreight);
        intent.putExtra("mapJson", mapJson);
        intent.putExtra("money", money);
        intent.putExtra("carType", 2);
        startActivityForResult(intent,13);
    }


    @Override
    public void onItemClick(int position) {
        if (!isLogin) {
            showShortToastByString("请您先登录！");
            if (loginDialog != null) {
                loginDialog.dismiss();
            }
            loginDialog = MyDialog.loginDialog(MainActivity.this, "", "");
            loginDialog.setOnLoginListener(MainActivity.this);
            loginDialog.show();
            return;
        }
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("orderAddressInfo", mOrderAddressInfos.get(position));
        intent.putExtra("type", 3);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActionClick(int position, int type) {

        if (!isLogin) {
            showShortToastByString("请您先登录！");
            if (loginDialog != null) {
                loginDialog.dismiss();
            }
            loginDialog = MyDialog.loginDialog(MainActivity.this, "", "");
            loginDialog.setOnLoginListener(MainActivity.this);
            loginDialog.show();
            return;
        }
        if (type == 1) {
            if (isToPayFreight && cofirmTypeId != -1) {
                showShortToastByString("您已勾选运费到付，不可以增设中途点");
            } else {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("orderAddressInfo", mOrderAddressInfos.get(position));
                intent.putExtra("type", 2);
                startActivityForResult(intent, 1);
            }
        }
        if (type == 2) {
            mOrderAddressInfos.remove(position);
            mOrderAddressAdapter.notifyDataSetChanged();
            getOrderInfo();
            if (cofirmTypeId != -1) {
                calculateFreight();
            }

            if (mOrderAddressInfos.size() > 2) {
                ll_isToPayFreight.setVisibility(View.GONE);
            } else {
                ll_isToPayFreight.setVisibility(View.VISIBLE);
            }
        }
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
            if (mOrderAddressInfos.size() > 2) {
                ll_isToPayFreight.setVisibility(View.GONE);
            } else {
                ll_isToPayFreight.setVisibility(View.VISIBLE);
            }
            LogUtil.i(mOrderAddressInfos.toString());
            getOrderInfo();
            if (cofirmTypeId != -1)
                calculateFreight();
        }

        if (resultCode == RESULT_OK && requestCode == 2) {
            premiums = data.getDoubleExtra("premium", 0);
            double goods = data.getDoubleExtra("goods", 0);
            tv_goods.setText(String.format("%s元", goods));
            if (isSurcharge) {
                surcharge = freight * ReturnPayRate / 100;
            } else {
                surcharge = 0;
            }
            double money = freight + premiums + surcharge;
            if (isPublish) {
                tv_money.setText(String.format("¥%.2f", money));
                //tv_money.setText(String.format("¥%s", df.format(money)));
            }
        }


        if (resultCode == RESULT_OK && requestCode == 3) {
            userInfo = data.getParcelableExtra("userInfo");
            tv_tel.setText(userInfo.getUserName());
            HttpUtil.setImageLoader(Contants.imagehost + userInfo.getHeadIco(), civ_head, R.mipmap.account1, R.mipmap.account1);
            HttpUtil.setImageLoader(Contants.imagehost + userInfo.getHeadIco(), iv_cir_head, R.mipmap.account1, R.mipmap.account1);
        }

        if (resultCode == RESULT_OK && requestCode == 5) {
            initOrderData();
            Intent intent = new Intent(MainActivity.this, MyOrderActivity.class);
            startActivity(intent);
        }

        if (resultCode == RESULT_OK && requestCode == 6) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDrawerLayout.closeDrawers();
                }
            }, 500);
            showShortToastByString("已退出");
            isLogin = false;
            isAgainLogin = true;
            civ_head.setImageResource(R.mipmap.account1);
            iv_cir_head.setImageResource(R.mipmap.account1);
            initOrderData();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isFirstLogin", true);
            editor.apply();
        }

        if (resultCode == RESULT_OK && requestCode == 10) {
            account = data.getStringExtra("Phone");
            password = data.getStringExtra("Password");
            if (loginDialog != null) {
                loginDialog.dismiss();
            }
            loginDialog = MyDialog.loginDialog(MainActivity.this, account, password);
            loginDialog.setOnLoginListener(MainActivity.this);
            loginDialog.show();
        }

        if (resultCode == RESULT_OK && requestCode == 12) {
            isLogin = true;
            String resName = data.getStringExtra("Name");
            String resSex = data.getStringExtra("Sex");
            userInfo.setUserName(resName);
            userInfo.setSex(resSex);
            tv_tel.setText(resName);

        }

        if (resultCode == RESULT_OK && requestCode == 13) {
            initOrderData();
            Intent intent = new Intent(MainActivity.this, MyOrderActivity.class);
            intent.putExtra("UserId", userInfo.getUserId());
            intent.putExtra("pageIndex", 2);//转到我的订单->新发布
            startActivity(intent);
        }
        if (resultCode == RESULT_OK && requestCode == 14) {
            initOrderData();
            Intent intent = new Intent(MainActivity.this, MyOrderActivity.class);
            intent.putExtra("UserId", userInfo.getUserId());
            intent.putExtra("pageIndex", 2);//转到我的订单->新发布
            startActivity(intent);
        }


    }


    private void getOrderInfo() {
        orderAddress = "";
        startLatLng = "";
        endLatLng = "";
        payment = 0;
        isPublish = false;

        OrderAddressInfo orderAddressInfo;
        int size = mOrderAddressInfos.size();
        if (!mOrderAddressInfos.get(0).getReceiptAddress().equals("start") && !mOrderAddressInfos.get(size - 1).getReceiptAddress().equals("start")) {
            isPublish = true;
        }

        for (int i = 0; i < size; i++) {
            orderAddressInfo = mOrderAddressInfos.get(i);
            payment += orderAddressInfo.getMoney();
            if (i == 0) {
                orderAddress = orderAddressInfo.toString() + "|";
                startLatLng = String.valueOf(orderAddressInfo.getLat()) + "," + String.valueOf(orderAddressInfo.getLng());

            } else if (i == size - 1) {
                orderAddress = orderAddress + orderAddressInfo.toString();
                endLatLng = endLatLng + String.valueOf(orderAddressInfo.getLat()) + "," + String.valueOf(orderAddressInfo.getLng());
            } else {
                orderAddress = orderAddress + orderAddressInfo.toString() + "|";
                startLatLng = startLatLng + "|" + String.valueOf(orderAddressInfo.getLat()) + "," + String.valueOf(orderAddressInfo.getLng());
                endLatLng = endLatLng + String.valueOf(orderAddressInfo.getLat()) + "," + String.valueOf(orderAddressInfo.getLng()) + "|";
            }
        }
    }


    /**
     * 计算运费
     */
    private void calculateFreight() {

        if (isPublish) {
            if (cofirmTypeId == -1) {
                showShortToastByString("未确定规格");
            } else if (cofirmTypeId == 0) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("Volume", Double.valueOf(volume));
                jsonObject.addProperty("Weight", Double.valueOf(kg));
                jsonObject.addProperty("StartLatLng", startLatLng);
                jsonObject.addProperty("EndLatLng", endLatLng);
                LogUtil.i(jsonObject.toString());
                Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
                HttpUtil.doPost(MainActivity.this, Contants.url_lingDanFreight, "lingDanFreight", map, new VolleyInterface(MainActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                    @Override
                    public void onSuccess(JsonElement result) {
                        Gson gson = new Gson();
                        FreightInfo freightInfo = gson.fromJson(result, FreightInfo.class);
                        freight = freightInfo.getFreight();
                        if (isSurcharge) {
                            surcharge = freight * ReturnPayRate / 100;
                        } else {
                            surcharge = 0;
                        }
                        double money = freight + premiums + surcharge;
                        tv_money.setText(String.format("¥%.2f", money));
                        //tv_money.setText(String.format("¥%s", df.format(money)));

                        //LogUtil.i("lingDanFreight" + result.toString());
                    }

                    @Override
                    public void onError(VolleyError error) {
                        showShortToastByString(getString(R.string.timeoutError));
                    }

                    @Override
                    public void onStateError() {
                    }
                });
            } else {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("VehicleType", vehideTypeId);
                jsonObject.addProperty("StartLatLng", startLatLng);
                jsonObject.addProperty("EndLatLng", endLatLng);
                LogUtil.i("CalculationFreight" + jsonObject.toString());
                Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
                HttpUtil.doPost(MainActivity.this, Contants.url_calculationFreight, "calculationFreight", map, new VolleyInterface(MainActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                    @Override
                    public void onSuccess(JsonElement result) {
                        LogUtil.i("CalculationFreight" + result.toString());
                        Gson gson = new Gson();
                        FreightInfo freightInfo = gson.fromJson(result, FreightInfo.class);
                        freight = freightInfo.getFreight();
                        if (isSurcharge) {
                            surcharge = freight * ReturnPayRate / 100;
                        } else {
                            surcharge = 0;
                        }
                        double money = freight + premiums + surcharge;
                        tv_money.setText(String.format("¥%.2f", money));
                        //tv_money.setText(String.format("¥%s", df.format(money)));
                    }

                    @Override
                    public void onError(VolleyError error) {
                        LogUtil.i("CalculationFreight" + error.toString());
                        showShortToastByString(getString(R.string.timeoutError));
                    }

                    @Override
                    public void onStateError() {
                    }
                });
            }
        }
    }


    private void login(final String account, final String password) {

        this.account = account;
        this.password = password;
        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(MainActivity.this);
        String time = DateUtil.getPsdCurrentDate();
        if (account.equals("") || password.equals("")) {
            showShortToastByString(getString(R.string.hint_message));
            return;
        }
        customProgressDialog.show();
        customProgressDialog.setText("登录中..");
        String finalPassword = MD5Util.encrypt(time + password);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("LoginName", account);
        jsonObject.addProperty("Time", time);
        jsonObject.addProperty("Password", finalPassword);
        jsonObject.addProperty("UserType", 1);
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(MainActivity.this, Contants.url_userLogin, "login", map, new VolleyInterface(MainActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {

                customProgressDialog.dismiss();

                if (isFirstLogin) {
                    loginDialog.dismiss();
                } else {
                    if (isAgainLogin) {
                        loginDialog.dismiss();
                    }
                }

                Gson gson = new Gson();
                userInfo = gson.fromJson(result, UserInfo.class);

                //保存用户账号，密码和状态
                SharedPreferences.Editor editor = prefs.edit();
                Contants.userId = userInfo.getUserId();
                Contants.Balance = userInfo.getBalance();
                Contants.Tel = userInfo.getTel();
                Contants.CompanyTel = userInfo.getCompanyTel();
                editor.putBoolean("isFirstLogin", false);
                editor.putString("account", account);
                editor.putString("password", password);
                editor.putString("UserId", userInfo.getUserId());
                editor.apply();
                if (carInfos.size() <= 0) {
                    initCarType();
                }
                if (adInfos.size() <= 0) {
                    initAdvertisings();
                }
                tv_tel.setText(userInfo.getUserName());
                HttpUtil.setImageLoader(Contants.imagehost + userInfo.getHeadIco(), civ_head, R.mipmap.account1, R.mipmap.account1);
                HttpUtil.setImageLoader(Contants.imagehost + userInfo.getHeadIco(), iv_cir_head, R.mipmap.account1, R.mipmap.account1);
                if (userInfo.getAuthenticateStatus().equals("1")) {
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    intent.putExtra("Uid", userInfo.getUserId());
                    intent.putExtra("state", 1);
                    startActivityForResult(intent, 12);
                    return;
                }

                isLogin = true;
                showShortToastByString("登录成功");
            }

            @Override
            public void onError(VolleyError error) {
                customProgressDialog.dismiss();
                showShortToastByString(getString(R.string.timeoutError));
                if (!isFirstLogin) {
                    if (loginDialog == null) {
                        loginDialog = MyDialog.loginDialog(MainActivity.this, account, password);
                        loginDialog.setOnLoginListener(MainActivity.this);
                    }
                    loginDialog.show();
                    isAgainLogin = true;
                }
                LogUtil.i("hint", error.toString());
            }

            @Override
            public void onStateError() {
                customProgressDialog.dismiss();
                if (!isFirstLogin) {
                    if (loginDialog == null) {
                        loginDialog = MyDialog.loginDialog(MainActivity.this, account, password);
                        loginDialog.setOnLoginListener(MainActivity.this);
                    }
                    isAgainLogin = true;
                    loginDialog.show();
                }
            }
        });
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if ((System.currentTimeMillis() - mExitTime) > 2000) {

                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onSetting(String name, String psd) {
        login(name, psd);
    }

    @Override
    public void onLoginClick(int type) {
        Intent intent = new Intent();
        if (type == 2) {
            loginDialog.dismiss();
        }
        if (type == 1) {
            intent.setClass(MainActivity.this, RebuildPsdActivity.class);
            startActivityForResult(intent, 10);
        }
        if (type == 0) {
            loginDialog.dismiss();
            new Handler().postDelayed(null, 500);
            registerDialog = MyDialog.registerDialog(MainActivity.this);
            registerDialog.setRegisterListener(new MyDialog.RegisterListener() {
                @Override
                public void onRegister(String name, String psd, String code) {
                    register(name, psd, code);
                }

                @Override
                public void onRegisterCode(String name, TextView textView) {
                    getCheckCode(name);
                    tv_getCode = textView;
                }

                @Override
                public void onRegisterClose() {
                    registerDialog.dismiss();
                    if (myCountDownTimer != null) {
                        myCountDownTimer.cancel();
                    }
                    new Handler().postDelayed(null, 500);
                    loginDialog.show();
                }
            });
            registerDialog.show();
        }
    }


    private void getCheckCode(String name) {
        if (name.isEmpty()) {
            showShortToastByString("信息不完整");
            return;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Tel", name);
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(MainActivity.this, Contants.url_obtainCheckCode, "obtainCheckCode", map, new VolleyInterface(MainActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                showShortToastByString("获取成功");
                LogUtil.i("register", result.toString());

                myCountDownTimer = new MyCountDownTimer(120 * 1000, 1000);
                myCountDownTimer.start();

            }

            @Override
            public void onError(VolleyError error) {
                LogUtil.i("hint", error.toString());
            }

            @Override
            public void onStateError() {
            }
        });


    }

    private void register(String name, String psd, String code) {
        if (name.isEmpty() || psd.isEmpty() || code.isEmpty()) {
            showShortToastByString(getString(R.string.hint_message));
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Phone", name);
        jsonObject.addProperty("VerificationCode", code);
        jsonObject.addProperty("Password", psd);
        jsonObject.addProperty("UserType", 1);
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());

        HttpUtil.doPost(MainActivity.this, Contants.url_register, "register", map, new VolleyInterface(MainActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                LogUtil.i(result.toString());

                Gson gson = new Gson();
                UidInfo uidInfo = gson.fromJson(result, UidInfo.class);
                LogUtil.i("hint", uidInfo.toString());

                //showShortToastByString("注册成功");
                if (myCountDownTimer != null) {
                    myCountDownTimer.cancel();
                }
                registerDialog.dismiss();
                loginDialog.show();

                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                intent.putExtra("Uid", uidInfo.getUid());
                intent.putExtra("state", 0);
                startActivity(intent);

            }

            @Override
            public void onError(VolleyError error) {
                showShortToastByString(getString(R.string.timeoutError));
            }

            @Override
            public void onStateError() {

            }
        });

    }


    class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            tv_getCode.setText(R.string.get_validate_code);
            tv_getCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_getCode.setText(String.format("%ds", millisUntilFinished / 1000));
            tv_getCode.setClickable(false);
        }
    }


}
