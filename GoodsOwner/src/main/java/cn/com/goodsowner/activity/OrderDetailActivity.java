package cn.com.goodsowner.activity;

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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import cn.com.goodsowner.R;
import cn.com.goodsowner.adapter.OrderDetailAdapter;
import cn.com.goodsowner.base.BaseActivity;
import cn.com.goodsowner.base.Contants;
import cn.com.goodsowner.bean.OrderDetailInfo;
import cn.com.goodsowner.bean.PayFeeInfo;
import cn.com.goodsowner.util.EncryptUtil;
import cn.com.goodsowner.util.HttpUtil;
import cn.com.goodsowner.util.LogUtil;
import cn.com.goodsowner.util.VolleyInterface;
import cn.com.goodsowner.view.MyDialog;

public class OrderDetailActivity extends BaseActivity implements RatingBar.OnRatingBarChangeListener {
    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private ImageView iv_state;
    private ImageView iv_comment;
    private ImageView iv_comment1;
    private String orderId;
    private RecyclerView rv_order_detail;
    private OrderDetailInfo orderDetailInfo;
    private OrderDetailAdapter orderDetailAdapter;
    private MyDialog endDialog;
    private OrderDetailInfo.OrderAddressBean orderAddressBean;
    private ArrayList<OrderDetailInfo.OrderAddressBean> orderAddressBeens;
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
    private MyDialog remarkDialog;

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
        iv_state = (ImageView) findViewById(R.id.iv_state);
        iv_comment = (ImageView) findViewById(R.id.iv_comment);
        iv_comment1 = (ImageView) findViewById(R.id.iv_comment1);


    }

    @Override
    protected void initData() {
        orderId = getIntent().getStringExtra("orderId");
        orderAddressBeens=new ArrayList<>();
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
                double money = orderDetailInfo.getFreight() + orderDetailInfo.getPremium();
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
                if (orderDetailInfo.getSurcharge().equals("True")) {
                    startState = startState + "  回单";
                }

                if (orderDetailInfo.getIsToPay().equals("True")) {
                    startState = startState + "  运费到付";
                }
                if (orderDetailInfo.getIsCollectionPayment().equals("True")) {
                    startState = startState + "  代收贷款";
                }
                if (orderDetailInfo.getRemark().equals("")){
                    iv_comment1.setVisibility(View.GONE);
                }else {
                    iv_comment1.setVisibility(View.VISIBLE);
                }

                tv_startState.setText(startState);

                rb_score1.setProgress(orderDetailInfo.getSendScore());

                if (!orderDetailInfo.getOrderAddress().get(0).getDischargeTime().equals("")) {
                    iv_state.setImageResource(R.mipmap.heart_gray);
                    bt_cancel.setBackgroundResource(R.drawable.button_gray_bg);
                    bt_cancel.setClickable(false);
                } else {
                    iv_state.setImageResource(R.mipmap.heart_red);
                }

                if (orderDetailInfo.getSendScore()!=0){
                    iv_comment.setImageResource(R.mipmap.comment_red);
                    rb_score1.setIsIndicator(true);
                }else {
                    iv_comment.setImageResource(R.mipmap.comment_black);
                    rb_score1.setIsIndicator(false);

                }
                rb_score1.setOnRatingBarChangeListener(OrderDetailActivity.this);

                orderAddressBeens.addAll(orderDetailInfo.getOrderAddress());
                orderDetailAdapter = new OrderDetailAdapter(orderDetailInfo, OrderDetailActivity.this,orderAddressBeens);
                LinearLayoutManager layoutManager = new LinearLayoutManager(OrderDetailActivity.this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                rv_order_detail.setLayoutManager(layoutManager);
                rv_order_detail.setAdapter(orderDetailAdapter);
                orderDetailAdapter.setOnBtnClickListener(new OrderDetailAdapter.OnBtnClickListener() {
                    @Override
                    public void onBtnClick(int position, String viewName) {
                        switch (viewName) {
                            case "确认卸货":
                                orderAddressBean = orderDetailInfo.getOrderAddress().get(position);
                                updateOrder(orderAddressBean.getAIndex());
                                break;
                            case "确认收货":
                                orderAddressBean = orderDetailInfo.getOrderAddress().get(position);
                                updateOrder(orderAddressBean.getAIndex());
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
            }

            @Override
            public void onStateError() {
            }
        });
        tv_right.setText("刷新");
        iv_left_white.setOnClickListener(this);
        bt_call.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
        bt_keepConvey.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        iv_comment1.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_white:
                finish();
                break;
            case R.id.bt_cancel:
                confirmCancel();
                break;
            case R.id.tv_right:
                refresh();
                break;
            case R.id.iv_comment1:
                remarkDialog =MyDialog.remarkDialog(OrderDetailActivity.this,orderDetailInfo.getRemark());
                remarkDialog.show();
                break;
            case R.id.bt_call:
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

    private void refresh() {
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
                orderDetailAdapter.setOrderDetailInfo(orderDetailInfo);
                if (!orderDetailInfo.getOrderAddress().get(0).getDischargeTime().equals("")) {
                    iv_state.setImageResource(R.mipmap.heart_gray);
                    bt_cancel.setBackgroundResource(R.drawable.button_gray_bg);
                    bt_cancel.setClickable(false);
                } else {
                    iv_state.setImageResource(R.mipmap.heart_red);
                }
                rb_score1.setProgress(orderDetailInfo.getSendScore());
                if (orderDetailInfo.getSendScore()!=0){
                    iv_comment.setImageResource(R.mipmap.comment_red);
                    rb_score1.setIsIndicator(true);
                }else {
                    iv_comment.setImageResource(R.mipmap.comment_black);
                    rb_score1.setIsIndicator(false);
                }
                rb_score1.setOnRatingBarChangeListener(OrderDetailActivity.this);
                orderAddressBeens.clear();
                orderAddressBeens.addAll(orderDetailInfo.getOrderAddress());
                orderDetailAdapter.notifyDataSetChanged();
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
                        // TODO Auto-generated method stub
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
                                            // TODO Auto-generated method stub
                                            endDialog.dismiss();
                                        }
                                    }).show();
                        } else {
                            cancelOrder(map);
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
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
                setResult(RESULT_OK,getIntent());
                finish();
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


    private void updateOrder(int index) {
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
                refresh();
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
                refresh();
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
        int size = orderDetailInfo.getOrderAddress().size();
        if (orderDetailInfo.getOrderAddress().get(size-1).getDischargeTime().equals("")){
            showShortToastByString("请达到终点后，再评论！");
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("OrderNo", orderDetailInfo.getOrderNo());
        jsonObject.addProperty("UserId", Contants.userId);
        jsonObject.addProperty("TransporterScore", (int) rating);
        jsonObject.addProperty("TransporterComment", "评论");
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(OrderDetailActivity.this, Contants.url_commentTransporter, "commentTransporter", map, new VolleyInterface(OrderDetailActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                LogUtil.i(result.toString());
                showShortToastByString("评论成功");
                rb_score1.setIsIndicator(true);
                refresh();
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
