package com.gpw.app.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.gpw.app.R;
import com.gpw.app.base.BaseActivity;
import com.gpw.app.base.Contants;
import com.gpw.app.bean.ReceiptOrderDetailInfo;
import com.gpw.app.util.EncryptUtil;
import com.gpw.app.util.HttpUtil;
import com.gpw.app.util.LogUtil;
import com.gpw.app.util.VolleyInterface;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class OrderReceiptDetailActivity extends BaseActivity {
    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private String orderId;
    private TextView tv_time;
    private TextView tv_time1;
    private TextView tv_address;
    private TextView tv_address1;
    private Button bt_call;
    private Button bt_location;
    private Button bt_confirm;
    private Button bt_apliy;
    private ReceiptOrderDetailInfo receiptOrderDetailInfo;

    @Override
    protected int getLayout() {
        return R.layout.activity_order_receipt_detail;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);

        bt_call = (Button) findViewById(R.id.bt_call);
        bt_apliy = (Button) findViewById(R.id.bt_apliy);
        bt_confirm = (Button) findViewById(R.id.bt_confirm);
        bt_location = (Button) findViewById(R.id.bt_location);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_time1 = (TextView) findViewById(R.id.tv_time1);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_address1 = (TextView) findViewById(R.id.tv_address1);

    }

    @Override
    protected void initData() {
        orderId = getIntent().getStringExtra("orderId");
    }


    @Override
    protected void initView() {

        tv_title.setText(R.string.order_detail1);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", Contants.userId);
        jsonObject.addProperty("OrderNo", orderId);
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(OrderReceiptDetailActivity.this, Contants.url_getReceiptOrderDetail, "getReceiptOrderDetail", map, new VolleyInterface(OrderReceiptDetailActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                LogUtil.i(result.toString());
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<ReceiptOrderDetailInfo>>() {
                }.getType();
                ArrayList<ReceiptOrderDetailInfo> OrderDetailInfos = gson.fromJson(result, listType);
                receiptOrderDetailInfo = OrderDetailInfos.get(0);
                ArrayList<ReceiptOrderDetailInfo.OrderAddressBean> orderAddressBeen = (ArrayList<ReceiptOrderDetailInfo.OrderAddressBean>) receiptOrderDetailInfo.getOrderAddress();
                tv_time.setText(orderAddressBeen.get(0).getArriveTime());
                tv_time1.setText(orderAddressBeen.get(1).getArriveTime());
                tv_address.setText(String.format("%s  %s  %s", orderAddressBeen.get(0).getAddress(), orderAddressBeen.get(0).getReceipter(), orderAddressBeen.get(0).getTel()));
                tv_address1.setText(String.format("%s  %s  %s", orderAddressBeen.get(1).getAddress(), orderAddressBeen.get(1).getReceipter(), orderAddressBeen.get(1).getTel()));
                System.out.println(OrderDetailInfos.size());

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
        bt_apliy.setOnClickListener(this);
        bt_confirm.setOnClickListener(this);
        bt_location.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_white:
                finish();
                break;
            case R.id.bt_apliy:
                double money = 0;
                int type = 0;
                if (receiptOrderDetailInfo.getIsToPay().equals("True")) {
                    money = receiptOrderDetailInfo.getPremium() + receiptOrderDetailInfo.getFreight();
                }
                if (receiptOrderDetailInfo.getIsCollectionPayment().equals("True")) {
                    money = money + receiptOrderDetailInfo.getPayment();
                }
                if (receiptOrderDetailInfo.getIsToPay().equals("True")&&receiptOrderDetailInfo.getIsCollectionPayment().equals("False")) {
                    type =2;
                }
                if (receiptOrderDetailInfo.getIsToPay().equals("True")&&receiptOrderDetailInfo.getIsCollectionPayment().equals("True")) {
                    type =4;
                }
                if (receiptOrderDetailInfo.getIsToPay().equals("False")&&receiptOrderDetailInfo.getIsCollectionPayment().equals("True")) {
                    type =3;
                }

                if (receiptOrderDetailInfo.getIsToPay().equals("True") || receiptOrderDetailInfo.getIsCollectionPayment().equals("True")) {
                    Intent intent = new Intent(OrderReceiptDetailActivity.this,PayActivity.class);
                    intent.putExtra("money",money);
                    intent.putExtra("orderNo",receiptOrderDetailInfo.getOrderNo());
                    intent.putExtra("type",type);
                    startActivity(intent);
                } else {
                    showShortToastByString("无需支付");
                }
                break;
            case R.id.bt_confirm:
                if (receiptOrderDetailInfo.getLogisticStatus()!=1){
                    showShortToastByString("已经收过货");
                    return;
                }
                updateOrder();
                break;
            case R.id.bt_call:
                if (receiptOrderDetailInfo.getOrderStatus()==4){
                    showShortToastByString("订单已完成");
                    return;
                }
                call();
                break;
            case R.id.bt_location:
                if (receiptOrderDetailInfo.getOrderStatus()==4){
                    showShortToastByString("订单已完成");
                    return;
                }
                Intent intent = new Intent(OrderReceiptDetailActivity.this, CarLocationActivity.class);
                intent.putExtra("TransporterId", receiptOrderDetailInfo.getTransporterId());
                intent.putExtra("TransporterName", receiptOrderDetailInfo.getTransporterName());
                startActivity(intent);
                break;
        }
    }


    private void call() {
        new AlertDialog.Builder(OrderReceiptDetailActivity.this).
                setTitle("提示").
                setMessage("是否拨打电话:" + receiptOrderDetailInfo.getTransporterTel()).
                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Uri data = Uri.parse("tel:" + receiptOrderDetailInfo.getTransporterTel());
                        intent.setData(data);
                        if (ActivityCompat.checkSelfPermission(OrderReceiptDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

    private void updateOrder() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("OrderNo", receiptOrderDetailInfo.getOrderNo());
        jsonObject.addProperty("UserId", Contants.userId);
        jsonObject.addProperty("UserType", 1);
        jsonObject.addProperty("Aindex", 1);
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(OrderReceiptDetailActivity.this, Contants.url_updateOrder, "updateOrder", map, new VolleyInterface(OrderReceiptDetailActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                call();
            } else {
                Toast.makeText(OrderReceiptDetailActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
