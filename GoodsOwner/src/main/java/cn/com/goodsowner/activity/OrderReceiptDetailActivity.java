package cn.com.goodsowner.activity;

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

import cn.com.goodsowner.base.BaseActivity;
import cn.com.goodsowner.base.Contants;
import cn.com.goodsowner.bean.ReceiptOrderDetailInfo;
import cn.com.goodsowner.util.EncryptUtil;
import cn.com.goodsowner.util.HttpUtil;
import cn.com.goodsowner.util.LogUtil;
import cn.com.goodsowner.util.VolleyInterface;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class OrderReceiptDetailActivity extends BaseActivity {
    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private ImageView iv_start;
    private ImageView iv_arrive;
    private String orderId;
    private TextView tv_time;
    private TextView tv_time1;
    private TextView tv_address;
    private TextView tv_address1;
    private Button bt_call;
    private Button bt_location;
    private Button bt_confirm;
    private Button bt_apliy;
    private double Premium;
    private double Freight;
    private double Amount;
    private int AIndex = 0;
    private ReceiptOrderDetailInfo receiptOrderDetailInfo;

    @Override
    protected int getLayout() {
        return cn.com.goodsowner.R.layout.activity_order_receipt_detail;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(cn.com.goodsowner.R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(cn.com.goodsowner.R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(cn.com.goodsowner.R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(cn.com.goodsowner.R.id.iv_left_white);

        bt_call = (Button) findViewById(cn.com.goodsowner.R.id.bt_call);
        bt_apliy = (Button) findViewById(cn.com.goodsowner.R.id.bt_apliy);
        bt_confirm = (Button) findViewById(cn.com.goodsowner.R.id.bt_confirm);
        bt_location = (Button) findViewById(cn.com.goodsowner.R.id.bt_location);
        tv_time = (TextView) findViewById(cn.com.goodsowner.R.id.tv_time);
        tv_time1 = (TextView) findViewById(cn.com.goodsowner.R.id.tv_time1);
        tv_address = (TextView) findViewById(cn.com.goodsowner.R.id.tv_address);
        tv_address1 = (TextView) findViewById(cn.com.goodsowner.R.id.tv_address1);
        iv_start = (ImageView) findViewById(cn.com.goodsowner.R.id.iv_start);
        iv_arrive = (ImageView) findViewById(cn.com.goodsowner.R.id.iv_arrive);

    }

    @Override
    protected void initData() {
        orderId = getIntent().getStringExtra("orderId");
    }


    @Override
    protected void initView() {

        tv_title.setText(cn.com.goodsowner.R.string.order_detail1);
        tv_right.setText("刷新");
        refresh();
        iv_left_white.setOnClickListener(this);
        bt_call.setOnClickListener(this);
        bt_apliy.setOnClickListener(this);
        bt_confirm.setOnClickListener(this);
        bt_location.setOnClickListener(this);
        tv_right.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case cn.com.goodsowner.R.id.iv_left_white:
                finish();
                break;
            case cn.com.goodsowner.R.id.tv_right:
                refresh();
                break;
            case cn.com.goodsowner.R.id.bt_apliy:
                int type = 0;
                if (receiptOrderDetailInfo.getIsToPay().equals("True")) {
                    Premium = receiptOrderDetailInfo.getPremium();
                    Freight = receiptOrderDetailInfo.getFreight();
                }
                if (receiptOrderDetailInfo.getIsToPay().equals("True") && Amount <= 0) {
                    type = 2;
                }
                if (receiptOrderDetailInfo.getIsToPay().equals("True") && Amount > 0) {
                    type = 4;
                }
                if (receiptOrderDetailInfo.getIsToPay().equals("False") && Amount > 0) {
                    type = 3;
                }
                double money = Premium + Freight + Amount;

                if (receiptOrderDetailInfo.getOrderAddress().get(1).getPayStatus() == 1 || receiptOrderDetailInfo.getIsToPay().equals("True")) {
                    Intent intent = new Intent(OrderReceiptDetailActivity.this, PayActivity.class);
                    intent.putExtra("money", money);
                    intent.putExtra("Premium", Premium);
                    intent.putExtra("Freight", Freight);
                    intent.putExtra("Amount", Amount);
                    intent.putExtra("AIndex", AIndex);
                    intent.putExtra("orderNo", receiptOrderDetailInfo.getOrderNo());
                    intent.putExtra("type", type);
                    startActivityForResult(intent, 1);
                }
                break;
            case cn.com.goodsowner.R.id.bt_confirm:
                updateOrder();
                break;
            case cn.com.goodsowner.R.id.bt_call:
                call();
                break;
            case cn.com.goodsowner.R.id.bt_location:
                Intent intent = new Intent(OrderReceiptDetailActivity.this, CarLocationActivity.class);
                intent.putExtra("TransporterId", receiptOrderDetailInfo.getTransporterId());
                intent.putExtra("TransporterName", receiptOrderDetailInfo.getTransporterName());
                startActivity(intent);
                break;
        }
    }

    private void refresh() {
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
                tv_time.setText(orderAddressBeen.get(0).getDischargeTime());
                tv_time1.setText(orderAddressBeen.get(1).getDischargeTime());
                tv_address.setText(String.format("%s  %s  %s", orderAddressBeen.get(0).getAddress(), orderAddressBeen.get(0).getReceipter(), orderAddressBeen.get(0).getTel()));
                tv_address1.setText(String.format("%s  %s  %s", orderAddressBeen.get(1).getAddress(), orderAddressBeen.get(1).getReceipter(), orderAddressBeen.get(1).getTel()));
                if (receiptOrderDetailInfo.getOrderAddress().get(1).getPayStatus() == 1 || receiptOrderDetailInfo.getIsToPay().equals("True")) {
                    bt_apliy.setBackgroundResource(cn.com.goodsowner.R.drawable.button_red_bg);
                    bt_apliy.setClickable(true);
                } else {
                    bt_apliy.setBackgroundResource(cn.com.goodsowner.R.drawable.button_gray_bg);
                    bt_apliy.setClickable(false);
                }
                Amount = receiptOrderDetailInfo.getOrderAddress().get(1).getAmount();
                AIndex = receiptOrderDetailInfo.getOrderAddress().get(1).getAIndex();
                if (orderAddressBeen.get(0).getDischargeTime().equals("")) {
                    iv_start.setImageResource(cn.com.goodsowner.R.mipmap.start);
                } else {
                    if (orderAddressBeen.get(1).getDischargeTime().equals("")) {
                        iv_start.setImageResource(cn.com.goodsowner.R.mipmap.start_red);
                    } else {
                        iv_start.setImageResource(cn.com.goodsowner.R.mipmap.start_gray);
                    }
                    bt_location.setBackgroundResource(cn.com.goodsowner.R.drawable.button_gray_bg);
                    bt_location.setClickable(false);
                }

                if (orderAddressBeen.get(1).getDischargeTime().equals("")) {
                    iv_arrive.setImageResource(cn.com.goodsowner.R.mipmap.arrive);
                } else {
                    iv_arrive.setImageResource(cn.com.goodsowner.R.mipmap.arrive_red);
                    bt_confirm.setBackgroundResource(cn.com.goodsowner.R.drawable.button_gray_bg);
                    bt_confirm.setClickable(false);
                }


                if (receiptOrderDetailInfo.getOrderStatus() == 4) {
                    iv_start.setImageResource(cn.com.goodsowner.R.mipmap.start_gray);
                    iv_arrive.setImageResource(cn.com.goodsowner.R.mipmap.arrive_gray);
                    bt_call.setBackgroundResource(cn.com.goodsowner.R.drawable.button_gray_bg);
                    bt_location.setBackgroundResource(cn.com.goodsowner.R.drawable.button_gray_bg);
                    bt_confirm.setBackgroundResource(cn.com.goodsowner.R.drawable.button_gray_bg);
                    bt_apliy.setBackgroundResource(cn.com.goodsowner.R.drawable.button_gray_bg);
                    bt_confirm.setClickable(false);
                    bt_location.setClickable(false);
                    bt_call.setClickable(false);
                    bt_apliy.setClickable(false);
                }


            }

            @Override
            public void onError(VolleyError error) {
                showShortToastByString(getString(cn.com.goodsowner.R.string.timeoutError));
//                LogUtil.i("hint",error.networkResponse.headers.toString());
//                LogUtil.i("hint",error.networkResponse.statusCode+"");
            }

            @Override
            public void onStateError() {
            }
        });
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
                        // TODO Auto-generated method stub
                    }
                }).show();
    }

    private void updateOrder() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("OrderNo", receiptOrderDetailInfo.getOrderNo());
        jsonObject.addProperty("UserId", Contants.userId);
        jsonObject.addProperty("UserType", 1);
        jsonObject.addProperty("Aindex", AIndex);
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(OrderReceiptDetailActivity.this, Contants.url_updateOrder, "updateOrder", map, new VolleyInterface(OrderReceiptDetailActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                LogUtil.i(result.toString());
                showShortToastByString("确认成功");
                refresh();
            }

            @Override
            public void onError(VolleyError error) {
                showShortToastByString(getString(cn.com.goodsowner.R.string.timeoutError));
//                LogUtil.i("hint",error.networkResponse.headers.toString());
//                LogUtil.i("hint",error.networkResponse.statusCode+"");
            }

            @Override
            public void onStateError() {
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            refresh();
        }
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
