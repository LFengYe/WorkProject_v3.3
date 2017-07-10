package cn.com.caronwer.activity;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.com.caronwer.NaviMainActivity;
import cn.com.caronwer.R;
import cn.com.caronwer.adapter.OrderDetailAdapter;
import cn.com.caronwer.base.BaseActivity;
import cn.com.caronwer.base.Contants;
import cn.com.caronwer.bean.MeAllOrderInfo;
import cn.com.caronwer.bean.OrderAddressBean;
import cn.com.caronwer.util.EncryptUtil;
import cn.com.caronwer.util.HttpUtil;
import cn.com.caronwer.util.SPtils;
import cn.com.caronwer.util.VolleyInterface;
import cn.com.caronwer.view.CustomProgressDialog;
import cn.com.caronwer.view.MessageDialog;
import cn.com.caronwer.view.MyDialog;
import cn.com.caronwer.view.QueRenCedanDialog;

public class OrderDetailActivity extends BaseActivity {
    private static final long SEVEN_DAY = 7 * 24 * 3600 * 1000;

    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;
    private ListView orderDetailList;

    private MeAllOrderInfo orderInfo;
    private String orderNo;
    private List<OrderAddressBean> orderAddressList;
    private OrderDetailAdapter adapter;
    private CustomProgressDialog progressDialog;

    private TextView tv_state;
    private TextView tv_date;
    private TextView tv_hujao;
    private TextView tv_leixin;
    private TextView tv_fangshi;
    private TextView tv_caizuo;
    private TextView tv_surcharge;
    private TextView tv_toPay;
    private TextView tv_collectionPayment;
    private ImageView iv_lianxi;
    private TextView tv_qian;
    private Button bt_dhqidian;
    private Button bt_cedan;
    private ImageView mIv_sta;

    private TextView tv_pjia;
    private TextView tv_lianxi;
    private RatingBar rb_leftdata;
    private ImageView mIv_end;

    private MessageDialog mNickDialog;
    private MyDialog endDialog;
    private MyDialog playDialog;
    private MyDialog zhuangDialog;
    private QueRenCedanDialog confirmDialog;

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
        orderDetailList = (ListView) findViewById(R.id.order_detail_list);

        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_hujao = (TextView) findViewById(R.id.tv_hujao);
        tv_leixin = (TextView) findViewById(R.id.tv_leixin);
        tv_fangshi = (TextView) findViewById(R.id.tv_fangshi);
        tv_caizuo = (TextView) findViewById(R.id.tv_caizuo);
        tv_surcharge = (TextView) findViewById(R.id.tv_surcharge);
        tv_toPay = (TextView) findViewById(R.id.tv_toPay);
        tv_collectionPayment = (TextView) findViewById(R.id.tv_collectionPayment);
        iv_lianxi = (ImageView) findViewById(R.id.iv_lianxi);
        tv_qian = (TextView) findViewById(R.id.tv_qian);
        bt_dhqidian = (Button) findViewById(R.id.bt_dhqidian);
        bt_cedan = (Button) findViewById(R.id.bt_cedan);
        mIv_sta = (ImageView) findViewById(R.id.iv_sta);

        tv_pjia = (TextView) findViewById(R.id.tv_pjia);
        tv_lianxi = (TextView) findViewById(R.id.tv_lianxi);
        rb_leftdata = (RatingBar) findViewById(R.id.rb_leftdata);
        mIv_end = (ImageView) findViewById(R.id.iv_end);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        orderNo = intent.getStringExtra("orderNo");
    }

    @Override
    protected void initView() {

        tv_title.setText(R.string.order_detail);
        tv_right.setText("刷新");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setOnClickListener(this);
        iv_left_white.setOnClickListener(this);

        tv_hujao.setOnClickListener(this);
        iv_lianxi.setOnClickListener(this);
        bt_cedan.setOnClickListener(this);
        bt_dhqidian.setOnClickListener(this);

        getOrderDetails();
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void seleStaus() {
        tv_lianxi.setText(orderAddressList.get(0).getReceipter() + " " + orderAddressList.get(0).getReceiptTel());
        tv_date.setText(orderInfo.getPlanSendTime());

        tv_leixin.setText((orderInfo.getVehicleType() == 0) ? "零担" : orderInfo.getVehicleTypeName());
        tv_fangshi.setVisibility(orderInfo.getIsMove().equals("True") ? View.VISIBLE : View.GONE);
        if (orderInfo.getVehicleType() <= 2) {
            if (orderInfo.getIsRemove().compareTo("True") == 0) {
                tv_caizuo.setText("全拆座");
            } else {
                tv_caizuo.setVisibility(View.GONE);
            }
        } else {
            if (orderInfo.getIsRemove().compareTo("True") == 0) {
                tv_caizuo.setText("平顶");
            } else {
                tv_caizuo.setVisibility(View.GONE);
            }
        }
        tv_surcharge.setVisibility(orderInfo.getIsSurcharge().equals("True") ? View.VISIBLE : View.GONE);
        tv_toPay.setVisibility(orderInfo.getIsToPay().equals("True") ? View.VISIBLE : View.GONE);
        tv_collectionPayment.setVisibility(orderInfo.getIsCollectionPayment() ? View.VISIBLE : View.GONE);
        iv_lianxi.setVisibility(TextUtils.isEmpty(orderInfo.getRemark()) ? View.GONE : View.VISIBLE);

        tv_qian.setText("¥" + orderInfo.getFreight());

        bt_dhqidian.setBackgroundResource(R.drawable.shap_selector2);
        bt_dhqidian.setClickable(true);
        if (orderInfo.getOrderStatus() != 4 && orderInfo.getOrderStatus() != -1) {
            tv_date.setVisibility(View.VISIBLE);
            mIv_end.setImageResource(R.mipmap.comment);
            mIv_sta.setImageResource(R.mipmap.heart_blue);
            bt_cedan.setBackgroundResource(R.drawable.shap_selector2);
            bt_cedan.setClickable(true);
            tv_hujao.setClickable(true);
            iv_lianxi.setClickable(true);
            rb_leftdata.setClickable(false);
            tv_state.setTextColor(0xff4e4e4e);
            tv_date.setTextColor(0xff4e4e4e);
            tv_leixin.setTextColor(0xff4e4e4e);
            tv_fangshi.setTextColor(0xff4e4e4e);
            tv_caizuo.setTextColor(0xff4e4e4e);
        }

        //到达起点
        if (!TextUtils.isEmpty(orderAddressList.get(0).getDischargeTime())) {
            mIv_sta.setImageResource(R.mipmap.heart_gray);
            bt_cedan.setBackgroundResource(R.drawable.shape_button2);
            bt_cedan.setClickable(false);
        }

        String createTimeStr = orderAddressList.get(orderAddressList.size() - 1).getDischargeTime();
        if (!TextUtils.isEmpty(createTimeStr)) {

            rb_leftdata.setClickable(true);
            rb_leftdata.setIsIndicator(false);
            //车主评价
            rb_leftdata.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (orderInfo.getTransporterScore() > 0) {
                        ratingBar.setRating(orderInfo.getTransporterScore());
//                        showShortToastByString(getString(R.string.already_comment));
                    } else {
                        commentSender((int) Math.ceil(rating));
                    }
                }
            });

            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            ParsePosition position = new ParsePosition(0);
            Date arriveTime = format.parse(createTimeStr, position);
            Date nowTime = Calendar.getInstance().getTime();

            if (orderInfo.getTransporterScore() > 0) {
                rb_leftdata.setIsIndicator(true);
                rb_leftdata.setRating(orderInfo.getTransporterScore());
                mIv_end.setImageResource(R.mipmap.comment_blue);
                adapter.setIsComment(true);
            } else if (nowTime.getTime() - arriveTime.getTime() >= SEVEN_DAY) {
                mIv_end.setImageResource(R.mipmap.comment_blue);
                adapter.setIsComment(true);
            }
        }

    }

    /**
     * 确认装货/确认卸货
     *
     * @param orderNo
     * @param receiptAddress
     */
    private void zhuanghuo(String orderNo, int receiptAddress) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", SPtils.getString(this, "UserId", "00000000-0000-0000-0000-000000000000"));
        jsonObject.addProperty("UserType", 2);//车主
        jsonObject.addProperty("OrderNo", orderNo);//订单号
        jsonObject.addProperty("Aindex", receiptAddress);//地点编号
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(OrderDetailActivity.this, Contants.url_updateOrder, "updateorder", map, new VolleyInterface(OrderDetailActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                zhuangDialog.dismiss();
                Toast.makeText(OrderDetailActivity.this, "装货成功", Toast.LENGTH_SHORT).show();
                getOrderDetails();
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(OrderDetailActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                zhuangDialog.dismiss();
            }

            @Override
            public void onStateError(int sta, String msg) {
                showShortToastByString("还不能装货哦");
                zhuangDialog.dismiss();
            }
        });
    }

    /**
     * 取消订单前确认是否可以取消
     * @param orderNo
     * @param name
     */
    private void confirmCancel(final String orderNo, final String name) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", SPtils.getString(this, "UserId", "00000000-0000-0000-0000-000000000000"));
        jsonObject.addProperty("UserType", 2);//车主
        jsonObject.addProperty("OrderNo", orderNo);//订单号
        jsonObject.addProperty("Reason", name);//取消的原因
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(OrderDetailActivity.this, Contants.url_OrdersConfirmCancel, "confirmCancel", map, new VolleyInterface(OrderDetailActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                float payFee = result.getAsJsonObject().get("PayFee").getAsFloat();
                if (payFee > 0.0) {
                    endDialog.dismiss();
                    confirmDialog = QueRenCedanDialog.nickDialog(OrderDetailActivity.this, payFee);
                    confirmDialog.setSureListener(new QueRenCedanDialog.SureListener() {
                        @Override
                        public void onSetting() {
                            cancelOrder(orderNo, name);
                        }
                    });
                    confirmDialog.show();
                } else {
                    cancelOrder(orderNo, name);
                }
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(OrderDetailActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                endDialog.dismiss();
            }

            @Override
            public void onStateError(int sta, String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    showShortToastByString(msg);
                }
                endDialog.dismiss();
            }
        });
    }

    /**
     * 撤单
     */
    private void cancelOrder(String orderNo, String name) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", SPtils.getString(this, "UserId", "00000000-0000-0000-0000-000000000000"));
        jsonObject.addProperty("UserType", 2);//车主
        jsonObject.addProperty("OrderNo", orderNo);//订单号
        jsonObject.addProperty("Reason", name);//取消的原因
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(OrderDetailActivity.this, Contants.url_cancelorder, "cancelorder", map, new VolleyInterface(OrderDetailActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                if (endDialog.isShowing())
                    endDialog.dismiss();
                if (null != confirmDialog && confirmDialog.isShowing())
                    confirmDialog.dismiss();
                showShortToastByString("撤销成功");
                Intent intent2 = new Intent(OrderDetailActivity.this, MyOrderActivityMe.class);
                intent2.putExtra("selectNo", "4");
                startActivity(intent2);
                finish();
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(OrderDetailActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                confirmDialog.dismiss();
            }

            @Override
            public void onStateError(int sta, String msg) {
                if (!TextUtils.isEmpty(msg)) {
                    showShortToastByString(msg);
                }
                confirmDialog.dismiss();
            }
        });
    }

    private void commentSender(int orderNo) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", SPtils.getString(this, "UserId", "00000000-0000-0000-0000-000000000000"));
        jsonObject.addProperty("OrderNo", orderInfo.getOrderNo());//订单号
        jsonObject.addProperty("TransporterComment", "");//评语
        jsonObject.addProperty("TransporterScore", orderNo);//评分

        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(OrderDetailActivity.this, Contants.url_commentsender, "commentsender", map, new VolleyInterface(OrderDetailActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                System.out.println(result);
                Toast.makeText(OrderDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                getOrderDetails();
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

    private void getOrderDetails() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", SPtils.getString(this, "UserId", ""));
        jsonObject.addProperty("OrderNo", orderNo);
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        progressDialog = new CustomProgressDialog(this);
        progressDialog.show();
        progressDialog.setText("加载中...");
        HttpUtil.doPost(OrderDetailActivity.this, Contants.url_TransporterGetOrderDetails, "GetOrderDetails", map,
                new VolleyInterface(OrderDetailActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
                    @Override
                    public void onSuccess(JsonElement result) {
                        progressDialog.dismiss();
                        //LogUtil.i("订单详情:", result.toString());
                        Gson gson = new Gson();
                        Type listType = new TypeToken<MeAllOrderInfo>() {}.getType();
                        orderInfo = gson.fromJson(result, listType);
                        orderAddressList = orderInfo.getOrderAddress();

                        adapter = new OrderDetailAdapter(OrderDetailActivity.this, orderAddressList, new OrderDetailAdapter.ItemBtnClick() {
                            @Override
                            public void leftBtnClick(String orderNo, int addressIndex) {
                                zhuangDialog = MyDialog.zhuanghuoDialog(OrderDetailActivity.this);
                                zhuangDialog.show();
                                zhuangDialog.setOnSettingListener(new MyDialog.EndListener() {
                                    @Override
                                    public void onSetting(String content) {
                                        zhuanghuo(orderInfo.getOrderNo(), orderAddressList.get(0).getAIndex());
                                    }
                                });
                            }

                            @Override
                            public void rightBtnClick(int index) {
                                daohang(index);
                            }
                        }, orderInfo);
                        adapter.setIsComment(false);
                        seleStaus();
                        orderDetailList.setAdapter(adapter);
                        setListViewHeightBasedOnChildren(orderDetailList);
                    }

                    @Override
                    public void onError(VolleyError error) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onStateError(int sta, String msg) {
                        progressDialog.dismiss();
                        if (!TextUtils.isEmpty(msg)) {
                            showShortToastByString(msg);
                        }
                    }
                });
    }

    private void daohang(int index) {
        Intent intent = new Intent(OrderDetailActivity.this, NaviMainActivity.class);
        Gson mMgson = new Gson();
        String json = mMgson.toJson(orderInfo);
        intent.putExtra("mefragmentall", json);
        intent.putExtra("index", index);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                getOrderDetails();
                break;
            case R.id.iv_left_white:
                finish();
                break;
            case R.id.tv_hujao://1 呼叫货主
                playDialog = MyDialog.playDialog(OrderDetailActivity.this, orderInfo.getSenderTel());
                playDialog.show();
                playDialog.setOnSettingListener(new MyDialog.EndListener() {
                    @Override
                    public void onSetting(String content) {
                        playDialog.dismiss();
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + orderInfo.getSenderTel()));
                        startActivity(intent);
                    }
                });

                break;
            case R.id.iv_lianxi://1 发消息给货主
                mNickDialog = MessageDialog.nickDialog(OrderDetailActivity.this, orderInfo.getRemark());
                mNickDialog.show();
                break;
            case R.id.bt_cedan://1 申请撤单
                endDialog = MyDialog.endDialog(OrderDetailActivity.this);
                endDialog.show();
                endDialog.setOnSettingListener(new MyDialog.EndListener() {
                    @Override
                    public void onSetting(String content) {
                        if (content.isEmpty()) {
                            showShortToastByString("撤单原因不能为空");
                            return;
                        }
                        confirmCancel(orderInfo.getOrderNo(), content);
                    }
                });
                break;
            case R.id.bt_zhuanghuo: //2 确认装货
                zhuangDialog = MyDialog.zhuanghuoDialog(OrderDetailActivity.this);
                zhuangDialog.show();
                zhuangDialog.setOnSettingListener(new MyDialog.EndListener() {
                    @Override
                    public void onSetting(String content) {
                        zhuanghuo(orderInfo.getOrderNo(), orderAddressList.get(0).getAIndex());
                    }
                });
                break;
            case R.id.bt_dhqidian://1 导航到起点
                daohang(0);
                break;
        }
    }
}
