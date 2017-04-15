package com.gpw.app.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.gpw.app.R;
import com.gpw.app.adapter.OrderDetailAdapter;
import com.gpw.app.base.BaseActivity;
import com.gpw.app.base.Contants;
import com.gpw.app.bean.OrderDetailInfo;
import com.gpw.app.bean.PayFeeInfo;
import com.gpw.app.util.EncryptUtil;
import com.gpw.app.util.HttpUtil;
import com.gpw.app.util.LogUtil;
import com.gpw.app.util.VolleyInterface;
import com.gpw.app.view.MyDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class OrderDetailActivity extends BaseActivity implements RatingBar.OnRatingBarChangeListener {
    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private String orderId;
    private RecyclerView rv_order_detail;
    private OrderDetailInfo orderDetailInfo;
    private OrderDetailAdapter orderDetailAdapter;
    private MyDialog endDialog;
    private OrderDetailInfo.OrderAddressBean orderAddressBean;
    private TextView tv_money;
    private TextView tv_name;
    private TextView tv_name1;
    private RatingBar rb_score;
    private RatingBar rb_score1;
    private TextView tv_vehicleNo;
    private TextView tv_vehicleNo1;
    private TextView tv_start_time;
    private TextView tv_startState;
    private Button bt_call;
    private Button bt_cancel;
    private Button bt_keepConvey;

    @Override
    protected int getLayout() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);
        rv_order_detail = (RecyclerView) findViewById(R.id.rv_order_detail);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name1 = (TextView) findViewById(R.id.tv_name1);
        tv_vehicleNo = (TextView) findViewById(R.id.tv_vehicleNo);
        tv_vehicleNo1 = (TextView) findViewById(R.id.tv_vehicleNo1);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        tv_startState = (TextView) findViewById(R.id.tv_startState);
        rb_score = (RatingBar) findViewById(R.id.rb_score);
        rb_score1 = (RatingBar) findViewById(R.id.rb_score1);
        bt_call = (Button) findViewById(R.id.bt_call);
        bt_keepConvey = (Button) findViewById(R.id.bt_keepConvey);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        rb_score1.setOnRatingBarChangeListener(this);

    }

    @Override
    protected void initData() {
        orderId = getIntent().getStringExtra("orderId");
    }


    @Override
    protected void initView() {

        tv_title.setText(R.string.order_detail);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", Contants.userId);
        jsonObject.addProperty("OrderNo", orderId);
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(OrderDetailActivity.this, Contants.url_getSendOrderDetail, "getSendOrderDetail", map, new VolleyInterface(OrderDetailActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                LogUtil.i(result.toString());
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<OrderDetailInfo>>() {
                }.getType();
                ArrayList<OrderDetailInfo> OrderDetailInfos = gson.fromJson(result, listType);
                orderDetailInfo = OrderDetailInfos.get(0);
                tv_start_time.setText(orderDetailInfo.getGrabTime());
                tv_name.setText(orderDetailInfo.getTransporterName());
                tv_vehicleNo.setText(orderDetailInfo.getVehicleNo());
                rb_score.setProgress(orderDetailInfo.getTransporterScore());
                tv_name1.setText(orderDetailInfo.getTransporterName());
                tv_vehicleNo1.setText(orderDetailInfo.getVehicleNo());
                double money = orderDetailInfo.getFreight()+orderDetailInfo.getPremium();
                tv_money.setText(String.format("¥ %s", money));

                String startState = orderDetailInfo.getVehicleTypeName();
                if (orderDetailInfo.getRemove().equals("True")) {
                    if (startState.equals("小型货车") || startState.equals("中型货车")) {
                        startState = startState + " 开顶";
                    } else if (startState.equals("小面包车") || startState.equals("中面包车")) {
                        startState = startState + "  全拆座";
                    } else {
                        startState = startState + orderDetailInfo.getVolume() + "km  " + orderDetailInfo.getWeight() + "kg";
                    }
                }
                if (orderDetailInfo.getMove().equals("True")) {
                    startState = startState + "  搬运";
                }
                if (orderDetailInfo.getIsToPay().equals("True")) {
                    startState = startState + "  货到付款";
                }
                tv_startState.setText(startState);


                orderDetailAdapter = new OrderDetailAdapter((ArrayList<OrderDetailInfo.OrderAddressBean>) orderDetailInfo.getOrderAddress(), OrderDetailActivity.this);
                LinearLayoutManager layoutManager = new LinearLayoutManager(OrderDetailActivity.this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                rv_order_detail.setLayoutManager(layoutManager);
                rv_order_detail.setAdapter(orderDetailAdapter);
                orderDetailAdapter.setOnBtnClickListener(new OrderDetailAdapter.OnBtnClickListener() {
                    @Override
                    public void onBtnClick(int position, String viewName) {
                        if (orderDetailInfo.getOrderStatus() == 4) {
                            showShortToastByString("订单已完成");
                            return;
                        }
                        switch (viewName) {
                            case "确认卸货":
                                orderAddressBean = orderDetailInfo.getOrderAddress().get(position);
                                updateOrder(2, orderAddressBean.getAIndex());
                                break;
                            case "确认收货":
                                orderAddressBean = orderDetailInfo.getOrderAddress().get(position);
                                updateOrder(3, orderAddressBean.getAIndex());
                                break;
                            case "车辆定位":
                                Intent intent = new Intent(OrderDetailActivity.this, CarLocationActivity.class);
                                intent.putExtra("TransporterId", orderDetailInfo.getTransporterId());
                                intent.putExtra("TransporterName", orderDetailInfo.getTransporterName());
                                startActivity(intent);
                                break;

                        }
                    }
                });

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
        tv_right.setVisibility(View.GONE);
        iv_left_white.setOnClickListener(this);
        bt_call.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
        bt_keepConvey.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_white:
                finish();
                break;
            case R.id.bt_cancel:
                if (orderDetailInfo.getOrderStatus() == 4) {
                    showShortToastByString("订单已完成");
                    return;
                }
                confirmCancel();
                break;
            case R.id.bt_call:
                if (orderDetailInfo.getOrderStatus() == 4) {
                    showShortToastByString("订单已完成");
                    return;
                }
                if (ContextCompat.checkSelfPermission(OrderDetailActivity.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(OrderDetailActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE}, 200);
                } else {
                    call();
                }


                break;
            case R.id.bt_keepConvey:
                keepTransporter(orderDetailInfo.getTransporterId());
                break;
        }
    }

    private void call() {
        new AlertDialog.Builder(OrderDetailActivity.this).
                setTitle("提示").
                setMessage("是否拨打电话:" + orderDetailInfo.getTransporterTel()).
                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Uri data = Uri.parse("tel:" + orderDetailInfo.getTransporterTel());
                        intent.setData(data);
                        if (ActivityCompat.checkSelfPermission(OrderDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(intent);
                    }
                }).
                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                call();
            } else {
                Toast.makeText(OrderDetailActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void confirmCancel() {
        endDialog = MyDialog.endDialog(OrderDetailActivity.this);
        endDialog.show();
        endDialog.setOnSettingListener(new MyDialog.EndListener() {
            @Override
            public void onSetting(String content) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("UserId", Contants.userId);
                jsonObject.addProperty("UserType", 1);
                jsonObject.addProperty("OrderNo", orderDetailInfo.getOrderNo());
                jsonObject.addProperty("Reason", content);
                final Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
                HttpUtil.doPost(OrderDetailActivity.this, Contants.url_confirmCancel, "confirmCancel", map, new VolleyInterface(OrderDetailActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                    @Override
                    public void onSuccess(JsonElement result) {
                        Gson gson = new Gson();
                        PayFeeInfo payFeeInfo = gson.fromJson(result, PayFeeInfo.class);
                        if (payFeeInfo.getPayFee() > 0) {
                            new AlertDialog.Builder(OrderDetailActivity.this).
                                    setTitle("温馨提示").
                                    setMessage(String.format("取消此订单，会产生%s元费用,是否确认取消？", payFeeInfo.getPayFee())).
                                    setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            cancelOrder(map);
                                        }
                                    }).
                                    setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            endDialog.dismiss();
                                        }
                                    }).show();
                        } else {
                            cancelOrder(map);
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
//                LogUtil.i("hint",error.networkResponse.headers.toString());
//                LogUtil.i("hint",error.networkResponse.statusCode+"");
                    }

                    @Override
                    public void onStateError() {
                    }
                });
            }
        });
    }


    private void cancelOrder(Map<String, String> map) {

        HttpUtil.doPost(OrderDetailActivity.this, Contants.url_cancelOrder, "cancelOrder", map, new VolleyInterface(OrderDetailActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                endDialog.dismiss();
                Toast.makeText(mContext, "取消成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(mContext, "连接超时", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStateError() {

            }
        });
    }


    private void updateOrder(int type, int index) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("OrderNo", orderDetailInfo.getOrderNo());
        jsonObject.addProperty("UserId", Contants.userId);
        jsonObject.addProperty("UserType", 1);
        jsonObject.addProperty("Aindex", index);
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(OrderDetailActivity.this, Contants.url_updateOrder, "updateOrder", map, new VolleyInterface(OrderDetailActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                LogUtil.i(result.toString());
                showShortToastByString("确认成功");
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

    private void keepTransporter(String transportUserId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("SendUserId", Contants.userId);
        jsonObject.addProperty("TransportUserId", transportUserId);
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(OrderDetailActivity.this, Contants.url_keepTransporter, "keepTransporter", map, new VolleyInterface(OrderDetailActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                LogUtil.i(result.toString());
                showShortToastByString("收藏成功");
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

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("OrderNo", orderDetailInfo.getOrderNo());
        jsonObject.addProperty("UserId", Contants.userId);
        jsonObject.addProperty("TransporterScore", (int) rating);
        jsonObject.addProperty("TransporterComment", "");
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(OrderDetailActivity.this, Contants.url_commentTransporter, "commentTransporter", map, new VolleyInterface(OrderDetailActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                LogUtil.i(result.toString());
                showShortToastByString("评论成功");
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
}
