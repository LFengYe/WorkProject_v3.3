package cn.com.caronwer.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import cn.com.caronwer.NaviMainActivity;
import cn.com.caronwer.R;
import cn.com.caronwer.base.BaseActivity;
import cn.com.caronwer.base.Contants;
import cn.com.caronwer.bean.MeAllOrderInfo;
import cn.com.caronwer.bean.OrderAddressBean;
import cn.com.caronwer.util.EncryptUtil;
import cn.com.caronwer.util.HttpUtil;
import cn.com.caronwer.util.SPtils;
import cn.com.caronwer.util.VolleyInterface;
import cn.com.caronwer.view.MessageDialog;
import cn.com.caronwer.view.MyDialog;

public class DetailsActivity extends BaseActivity {


    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_left_white;

    private LinearLayout ll_deta1;
    private LinearLayout ll_deta2;
    private LinearLayout ll_deta3;
    private LinearLayout ll_deta4;
    private LinearLayout ll_deta5;

    private TextView tv_state;
    private TextView tv_date;
    private TextView tv_hujao;
    private TextView tv_leixin;
    private TextView tv_fangshi;
    private TextView tv_caizuo;
    private ImageView iv_lianxi;
    private TextView tv_qian;
    private Button bt_dhqidian;
    private Button bt_cedan;

    private TextView tv_zhuangche;
    private TextView tv_didian;
    private TextView tv_zhuangchedate;
    private Button bt_zhuanghuo;
    private Button bt_dhxiayidian;

    private TextView tv_zhongtudian;
    private TextView zhongtudiandate;
    private TextView tv_zhongtudiandizi;
    private Button bt_dhzhongdian;

    private TextView tv_jiedan;
    private TextView jiedandate;
    private TextView tv_jiedandizi;
    private TextView tv_daofu;

    private TextView tv_pjia;
    private TextView tv_lianxi;
    private RatingBar rb_leftdata;

    private ImageView mIv_sta;
    private ImageView mIv_cong;
    private ImageView mIv_jing;
    private ImageView mIv_dao;
    private ImageView mIv_x1;
    private ImageView mIv_x2;
    private ImageView mIv_x3;
    private ImageView mIv_x4;
    private View mView_d1;
    private View mView_d2;
    private View mView_d3;
    private View mView_d4;
    private ImageView mIv_end;
    private MeAllOrderInfo mInfo1;
    private List<OrderAddressBean> mOrderAddress;
    private MessageDialog mNickDialog;
    private MyDialog endDialog;
    private MyDialog playDialog;
    private MyDialog zhuangDialog;
    private String mCedanYuanyin;
    private Double mFeiyong;

    private Gson mMgson;
    private int maxSta = 5;
    private TextView mTv_daisou3;
    private TextView mTv_daisou2;
    private TextView mTv_daisou1;

    @Override
    protected int getLayout() {
        return R.layout.activity_details;
    }

    @Override
    protected void findById() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.in_head);
        assert rl_head != null;
        tv_title = (TextView) rl_head.findViewById(R.id.tv_title);
        tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        iv_left_white = (ImageView) rl_head.findViewById(R.id.iv_left_white);

        ll_deta1 = (LinearLayout) findViewById(R.id.ll_deta1);
        ll_deta2 = (LinearLayout) findViewById(R.id.ll_deta2);
        ll_deta3 = (LinearLayout) findViewById(R.id.ll_deta3);
        ll_deta4 = (LinearLayout) findViewById(R.id.ll_deta4);
        ll_deta5 = (LinearLayout) findViewById(R.id.ll_deta5);

        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_hujao = (TextView) findViewById(R.id.tv_hujao);
        tv_leixin = (TextView) findViewById(R.id.tv_leixin);
        tv_fangshi = (TextView) findViewById(R.id.tv_fangshi);
        tv_caizuo = (TextView) findViewById(R.id.tv_caizuo);
        iv_lianxi = (ImageView) findViewById(R.id.iv_lianxi);
        tv_qian = (TextView) findViewById(R.id.tv_qian);
        bt_dhqidian = (Button) findViewById(R.id.bt_dhqidian);
        bt_cedan = (Button) findViewById(R.id.bt_cedan);

        tv_zhuangche = (TextView) findViewById(R.id.tv_zhuangche);
        tv_zhuangchedate = (TextView) findViewById(R.id.tv_zhuangchedate);
        tv_didian = (TextView) findViewById(R.id.tv_didian);
        bt_zhuanghuo = (Button) findViewById(R.id.bt_zhuanghuo);
        bt_dhxiayidian = (Button) findViewById(R.id.bt_dhxiayidian);

        tv_zhongtudian = (TextView) findViewById(R.id.tv_zhongtudian);
        zhongtudiandate = (TextView) findViewById(R.id.zhongtudiandate);
        tv_zhongtudiandizi = (TextView) findViewById(R.id.tv_zhongtudiandizi);
        bt_dhzhongdian = (Button) findViewById(R.id.bt_dhzhongdian);

        tv_jiedan = (TextView) findViewById(R.id.tv_jiedan);
        jiedandate = (TextView) findViewById(R.id.jiedandate);
        tv_jiedandizi = (TextView) findViewById(R.id.tv_jiedandizi);
        tv_daofu = (TextView) findViewById(R.id.tv_daofu);

        tv_pjia = (TextView) findViewById(R.id.tv_pjia);
        tv_lianxi = (TextView) findViewById(R.id.tv_lianxi);
        rb_leftdata = (RatingBar) findViewById(R.id.rb_leftdata);


        mIv_sta = (ImageView) findViewById(R.id.iv_sta);
        mIv_cong = (ImageView) findViewById(R.id.iv_cong);
        mIv_jing = (ImageView) findViewById(R.id.iv_jing);
        mIv_dao = (ImageView) findViewById(R.id.iv_dao);

        mIv_x1 = (ImageView) findViewById(R.id.iv_x1);
        mIv_x2 = (ImageView) findViewById(R.id.iv_x2);
        mIv_x3 = (ImageView) findViewById(R.id.iv_x3);
        mIv_x4 = (ImageView) findViewById(R.id.iv_x4);
        mIv_end = (ImageView) findViewById(R.id.iv_end);

        mView_d1 = findViewById(R.id.view_d1);
        mView_d2 = findViewById(R.id.view_d2);
        mView_d3 = findViewById(R.id.view_d3);
        mView_d4 = findViewById(R.id.view_d4);

        mTv_daisou3 = (TextView) findViewById(R.id.tv_daisou3);
        mTv_daisou2 = (TextView) findViewById(R.id.tv_daisou2);
        mTv_daisou1 = (TextView) findViewById(R.id.tv_daisou);


    }

    @Override
    protected void initData() {
        Gson gson = new Gson();
        Intent intent = getIntent();
        String json = intent.getStringExtra("mefragmentall");
        mInfo1 = gson.fromJson(json, MeAllOrderInfo.class);
        mOrderAddress = mInfo1.getOrderAddress();
    }

    @Override
    protected void initView() {
        tv_title.setText("查看详情");
        tv_right.setVisibility(View.GONE);
        iv_left_white.setOnClickListener(this);

        tv_hujao.setOnClickListener(this);
        iv_lianxi.setOnClickListener(this);
        bt_cedan.setOnClickListener(this);
        bt_dhqidian.setOnClickListener(this);

        bt_dhxiayidian.setOnClickListener(this);
        bt_zhuanghuo.setOnClickListener(this);

        bt_dhzhongdian.setOnClickListener(this);

        //车主评价
        rb_leftdata.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (maxSta != 0) {
                    rb_leftdata.setRating(0);
                } else {
                    commentSender(ratingBar.getNumStars());
                }
            }
        });
        seleStaus();

    }

    /**
     * 状态控制
     */
    private void seleStaus() {
        List<OrderAddressBean> address = mInfo1.getOrderAddress();
        if (mInfo1.getOrderStatus() != 4 && mInfo1.getOrderStatus() != -1) {
            stateSelect1(4);
            maxSta = 4;
            tv_date.setText(mInfo1.getPlanSendTime());
        }

        for (int i = 0; i < address.size() - 1; i++) {
            if (i == 0 && (!TextUtils.isEmpty(address.get(0).getDischargeTime()))) {//到达起点
                stateSelect1(3);
                maxSta = 3;
                tv_zhuangchedate.setText(address.get(0).getDischargeTime());

            } else if (i == address.size() - 1 && (!TextUtils.isEmpty(address.get(address.size() - 1).getDischargeTime()))) {//到达终点
                stateSelect1(1);
                maxSta = 1;
                jiedandate.setText(address.get(address.size() - 1).getDischargeTime());

                if (mInfo1.getFinanceStatus() == 2) {//已经付款
                    stateSelect1(0);
                    maxSta = 0;
                }else {
                    tv_jiedandizi.setText(address.get(i).getReceiptAddress() + " " +
                            address.get(i).getReceipter() + " ");
                }
            } else if (!TextUtils.isEmpty(address.get(address.size() - 1).getDischargeTime())) {//中途点
                stateSelect1(2);
                maxSta = 2;
                zhongtudiandate.setText(TextUtils.isEmpty(address.get(i).getDischargeTime()) == true ? getNoeTime() : address.get(i).getDischargeTime());

                tv_zhongtudiandizi.setText(mOrderAddress.get(i).getReceiptAddress() + " " +
                        mOrderAddress.get(i).getReceipter() + " ");

                String szhongtu = mOrderAddress.get(i).getReceiptTel().substring(mOrderAddress.get(i).getReceiptTel().length()-11,
                        mOrderAddress.get(i).getReceiptTel().length()-1);
                SpannableString spanzhongtu = new SpannableString(szhongtu);
                ClickableSpan clickttt2 = new ShuoMClickableSpan(szhongtu, this);
                spanzhongtu.setSpan(clickttt2, 0, szhongtu.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                tv_zhongtudiandizi.append(spanzhongtu);
                tv_zhongtudiandizi.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }

    }

    /**
     * 评价货主
     *
     * @param orderNo
     */
    private void commentSender(int orderNo) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", SPtils.getString(this, "UserId", "00000000-0000-0000-0000-000000000000"));
        jsonObject.addProperty("OrderNo", mInfo1.getOrderNo());//订单号
        jsonObject.addProperty("TransporterComment", mInfo1.getOrderNo());//评语
        jsonObject.addProperty("TransporterScore", orderNo);//评分

        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(DetailsActivity.this, Contants.url_commentsender, "commentsender", map, new VolleyInterface(DetailsActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                Toast.makeText(DetailsActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_white:
                finish();
                break;
            case R.id.tv_hujao://1 呼叫货主

                playDialog = MyDialog.playDialog(DetailsActivity.this,mInfo1.getSenderTel());
                playDialog.show();
                playDialog.setOnSettingListener(new MyDialog.EndListener() {
                    @Override
                    public void onSetting(String content) {
                        playDialog.dismiss();
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + mInfo1.getSenderTel()));
                        startActivity(intent);
                    }
                });

//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_CALL);
//                intent.setData(Uri.parse("tel:" + mInfo1.getSenderTel()));
//                startActivity(intent);
                break;
            case R.id.iv_lianxi://1 发消息给货主
                mNickDialog = MessageDialog.nickDialog(DetailsActivity.this,mInfo1.getRemark());
                mNickDialog.show();
                break;
            case R.id.bt_cedan://1 申请撤单

                endDialog = MyDialog.endDialog(DetailsActivity.this);
                endDialog.show();
                endDialog.setOnSettingListener(new MyDialog.EndListener() {
                    @Override
                    public void onSetting(String content) {
                        if (content.isEmpty()) {
                            showShortToastByString("撤单原因不能为空");
                            return;
                        }
                        mCedanYuanyin = content;
                        cedanQueren(mInfo1.getOrderNo(), content);
                    }
                });

                break;
            case R.id.bt_zhuanghuo: //2 确认装货
                zhuangDialog = MyDialog.zhuanghuoDialog(DetailsActivity.this);
                zhuangDialog.show();
                zhuangDialog.setOnSettingListener(new MyDialog.EndListener() {
                    @Override
                    public void onSetting(String content) {
                        zhuanghuo(mInfo1.getOrderNo(), 1, mOrderAddress.get(0).getAIndex());
                    }
                });
                break;
            case R.id.bt_dhqidian://1 导航到起点
                daohang(1);
                break;
            case R.id.bt_dhxiayidian://2 导航到下一地点
                daohang(2);
                break;
            case R.id.bt_dhzhongdian://3 导航到终点
                daohang(3);
                break;
        }
    }


    /**
     * 导航
     *
     * @param type
     */
    private void daohang(int type) {
        Intent intent = new Intent(DetailsActivity.this, NaviMainActivity.class);
        if (mMgson == null) {
            mMgson = new Gson();
        }
        String json = mMgson.toJson(mInfo1);
        intent.putExtra("mefragmentall", json);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    /**
     * 确认装货/确认卸货
     *
     * @param orderNo
     * @param i
     * @param receiptAddress
     */
    private void zhuanghuo(String orderNo, int i, int receiptAddress) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", SPtils.getString(this, "UserId", "00000000-0000-0000-0000-000000000000"));
        jsonObject.addProperty("UserType", 2);//车主
        jsonObject.addProperty("OrderNo", orderNo);//订单号
        jsonObject.addProperty("Aindex", receiptAddress);//地点编号
//        jsonObject.addProperty("OperationType", i);//操作类型 1.确认装货 2.确认卸货
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(DetailsActivity.this, Contants.url_updateOrder, "updateorder", map, new VolleyInterface(DetailsActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                zhuangDialog.dismiss();
                Toast.makeText(DetailsActivity.this, "装货成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(DetailsActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                zhuangDialog.dismiss();
            }

            @Override
            public void onStateError(int sta, String msg) {
//                if (!TextUtils.isEmpty(msg)){
//                    showShortToastByString(msg);
//                }
                showShortToastByString("还不能装货哦");
                zhuangDialog.dismiss();
            }
        });
    }


    /**
     * 撤单
     */
    private void cedanQueren(String orderNo, String name) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("UserId", SPtils.getString(this, "UserId", "00000000-0000-0000-0000-000000000000"));
        jsonObject.addProperty("UserType", 2);//车主
        jsonObject.addProperty("OrderNo", orderNo);//订单号
        jsonObject.addProperty("Reason", name);//取消的原因
        Map<String, String> map = EncryptUtil.encryptDES(jsonObject.toString());
        HttpUtil.doPost(DetailsActivity.this, Contants.url_cancelorder, "cancelorder", map, new VolleyInterface(DetailsActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onSuccess(JsonElement result) {
                showShortToastByString("撤销成功");
                Intent intent2 = new Intent(DetailsActivity.this, MyOrderActivityMe.class);
                intent2.putExtra("selectNo", "4");
                startActivity(intent2);
                finish();
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(DetailsActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
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
     * 状态选择1全部显示
     *
     * @param state
     */
    public void stateSelect1(int state) {

        rb_leftdata.setClickable(false);

//        tv_hujao.setClickable(false);
        tv_hujao.setClickable(true);//所有状态都能呼叫货主

        iv_lianxi.setClickable(true);
        bt_dhqidian.setClickable(false);
        bt_cedan.setClickable(false);
        bt_zhuanghuo.setClickable(false);
        bt_dhxiayidian.setClickable(false);
        bt_dhzhongdian.setClickable(false);

        /*
        switch (state) {
            case 0:
                mIv_end.setImageResource(R.mipmap.zong3);
                mIv_dao.setImageResource(R.mipmap.dao1);
                mIv_jing.setImageResource(R.mipmap.jing1);
                mIv_cong.setImageResource(R.mipmap.cong1);
                mIv_sta.setImageResource(R.mipmap.sta);
                break;
            case 1:
                mIv_end.setImageResource(R.mipmap.zong1);
                mIv_dao.setImageResource(R.mipmap.dao3);
                mIv_jing.setImageResource(R.mipmap.jing1);
                mIv_cong.setImageResource(R.mipmap.cong1);
                mIv_sta.setImageResource(R.mipmap.sta);
                jiedandate.setVisibility(View.VISIBLE);
                zhongtudiandate.setVisibility(View.GONE);
                tv_zhuangchedate.setVisibility(View.GONE);
                tv_date.setVisibility(View.GONE);
                break;
            case 2:

                jiedandate.setVisibility(View.GONE);
                zhongtudiandate.setVisibility(View.VISIBLE);
                tv_zhuangchedate.setVisibility(View.GONE);
                tv_date.setVisibility(View.GONE);

                mIv_end.setImageResource(R.mipmap.zong1);
                mIv_dao.setImageResource(R.mipmap.dao2);
                mIv_jing.setImageResource(R.mipmap.jing3);
                mIv_cong.setImageResource(R.mipmap.cong1);
                mIv_sta.setImageResource(R.mipmap.sta);
                bt_dhzhongdian.setBackgroundResource(R.drawable.shap_selector);
                bt_dhzhongdian.setClickable(true);

                bt_zhuanghuo.setBackgroundResource(R.drawable.shape_button2);
                bt_dhxiayidian.setBackgroundResource(R.drawable.shape_button2);
                bt_dhqidian.setBackgroundResource(R.drawable.shape_button2);
                bt_cedan.setBackgroundResource(R.drawable.shape_button2);
                bt_cedan.setClickable(false);
                tv_hujao.setClickable(true);
                iv_lianxi.setClickable(true);
                bt_dhqidian.setClickable(false);
                bt_zhuanghuo.setClickable(false);
                bt_dhxiayidian.setClickable(false);

                break;
            case 3:
                jiedandate.setVisibility(View.GONE);
                zhongtudiandate.setVisibility(View.GONE);
                tv_zhuangchedate.setVisibility(View.VISIBLE);
                tv_date.setVisibility(View.GONE);

                mIv_end.setImageResource(R.mipmap.zong1);
                mIv_dao.setImageResource(R.mipmap.dao2);
                mIv_jing.setImageResource(R.mipmap.jing2);
                mIv_cong.setImageResource(R.mipmap.cong3);
                mIv_sta.setImageResource(R.mipmap.sta);
                bt_zhuanghuo.setBackgroundResource(R.drawable.shap_selector);
                bt_dhxiayidian.setBackgroundResource(R.drawable.shap_selector);
                bt_zhuanghuo.setClickable(true);
                bt_dhxiayidian.setClickable(true);

                bt_dhqidian.setBackgroundResource(R.drawable.shape_button2);
                bt_cedan.setBackgroundResource(R.drawable.shape_button2);
                bt_cedan.setClickable(false);
                tv_hujao.setClickable(true);
                iv_lianxi.setClickable(true);
                bt_dhqidian.setClickable(false);
                break;
            case 4:
                jiedandate.setVisibility(View.GONE);
                zhongtudiandate.setVisibility(View.GONE);
                tv_zhuangchedate.setVisibility(View.GONE);
                tv_date.setVisibility(View.VISIBLE);

                mIv_end.setImageResource(R.mipmap.zong1);
                mIv_dao.setImageResource(R.mipmap.dao2);
                mIv_jing.setImageResource(R.mipmap.jing2);
                mIv_cong.setImageResource(R.mipmap.cong2);
                mIv_sta.setImageResource(R.mipmap.sta3);
                bt_dhqidian.setBackgroundResource(R.drawable.shap_selector);
                bt_cedan.setBackgroundResource(R.drawable.shap_selector);
                bt_cedan.setClickable(true);
                tv_hujao.setClickable(true);
                iv_lianxi.setClickable(true);
                bt_dhqidian.setClickable(true);

                bt_zhuanghuo.setBackgroundResource(R.drawable.shap_selector);
                bt_zhuanghuo.setClickable(true);

                break;
        }
        switch (state) {

            case 0:
                //评价
                tv_pjia.setTextColor(0xff4e4e4e);
                tv_lianxi.setTextColor(0xff4e4e4e);

                rb_leftdata.setClickable(true);
                //接单成功到达终点
            case 1:

                mView_d4.setBackgroundResource(R.color.bg_texton);
                tv_jiedan.setTextColor(0xff4e4e4e);
                jiedandate.setTextColor(0xff4e4e4e);
                tv_jiedandizi.setTextColor(0xff4e4e4e);
                tv_daofu.setTextColor(0xff4e4e4e);

                //到达中途点
            case 2:
                mView_d3.setBackgroundResource(R.color.bg_texton);
                tv_zhongtudian.setTextColor(0xff4e4e4e);
                zhongtudiandate.setTextColor(0xff4e4e4e);
                tv_zhongtudiandizi.setTextColor(0xff4e4e4e);
                //到达起点
            case 3:

                mView_d2.setBackgroundResource(R.color.bg_texton);
                tv_zhuangche.setTextColor(0xff4e4e4e);
                tv_zhuangchedate.setTextColor(0xff4e4e4e);
                tv_didian.setTextColor(0xff4e4e4e);

                //接单成功
            case 4:
                mView_d1.setBackgroundResource(R.color.bg_texton);
                tv_state.setTextColor(0xff4e4e4e);
                tv_date.setTextColor(0xff4e4e4e);
                tv_leixin.setTextColor(0xff4e4e4e);
                tv_fangshi.setTextColor(0xff4e4e4e);
                tv_caizuo.setTextColor(0xff4e4e4e);

                break;
        }
        */

        tv_lianxi.setText(mOrderAddress.get(0).getReceipter() + " " + mOrderAddress.get(0).getReceiptTel());

        tv_didian.setText(mOrderAddress.get(0).getReceiptAddress() + " " +
                mOrderAddress.get(0).getReceipter() + " ");

        System.out.println("sdidian:" + mOrderAddress.get(0).getReceiptTel());
//        String sdidian = mOrderAddress.get(0).getReceiptTel().substring(mOrderAddress.get(0).getReceiptTel().length()-11,mOrderAddress.get(0).getReceiptTel().length()-1);
        String sdidian = mOrderAddress.get(0).getReceiptTel();
        SpannableString spandidian = new SpannableString(sdidian);
        ClickableSpan clickttt3 = new ShuoMClickableSpan(sdidian, this);
        spandidian.setSpan(clickttt3, 0, sdidian.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_didian.append(spandidian);
        tv_didian.setMovementMethod(LinkMovementMethod.getInstance());


        tv_jiedandizi.setText(mOrderAddress.get(mOrderAddress.size() - 1).getReceiptAddress() + " " +
                mOrderAddress.get(mOrderAddress.size() - 1).getReceipter() + " ");


        System.out.println("sjiedan:" + mOrderAddress.get(mOrderAddress.size() - 1).getReceiptTel());
//        String sjiedan = mOrderAddress.get(mOrderAddress.size() - 1).getReceiptTel().substring(mOrderAddress.get(mOrderAddress.size() - 1).getReceiptTel().length()-11,
//                mOrderAddress.get(mOrderAddress.size() - 1).getReceiptTel().length()-1);
        String sjiedan = mOrderAddress.get(mOrderAddress.size() - 1).getReceiptTel();
        SpannableString spanjiedan = new SpannableString(sjiedan);
        ClickableSpan clickttt1 = new ShuoMClickableSpan(sjiedan, this);
        spanjiedan.setSpan(clickttt1, 0, sjiedan.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_jiedandizi.append(spanjiedan);
        tv_jiedandizi.setMovementMethod(LinkMovementMethod.getInstance());


        tv_zhongtudiandizi.setText(mOrderAddress.get(1).getReceiptAddress() + " " +
                mOrderAddress.get(1).getReceipter() + " ");

        System.out.println("szhongtu:" + mOrderAddress.get(1).getReceiptTel());
//        String szhongtu = mOrderAddress.get(1).getReceiptTel().substring(mOrderAddress.get(1).getReceiptTel().length()-11,
//                mOrderAddress.get(1).getReceiptTel().length()-1);
        String szhongtu = mOrderAddress.get(1).getReceiptTel();
        SpannableString spanzhongtu = new SpannableString(szhongtu);
        ClickableSpan clickttt2 = new ShuoMClickableSpan(szhongtu, this);
        spanzhongtu.setSpan(clickttt2, 0, szhongtu.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_zhongtudiandizi.append(spanzhongtu);
        tv_zhongtudiandizi.setMovementMethod(LinkMovementMethod.getInstance());




        if (mInfo1.getIsToPay().equals("true") == true) {
            tv_daofu.setText("此订单为货到付款，请确保对方完成支付");
        } else {
            tv_daofu.setText("请等待对方完成支付");
        }
        tv_daofu.append("");

        tv_date.setText(mInfo1.getPlanSendTime());
        tv_leixin.setText(mInfo1.getVehicleTypeName());
        tv_caizuo.setText(mInfo1.getIsRemove().equals("True") == true ? "全拆座" : "");
        tv_fangshi.setText(mInfo1.getIsMove().equals("True") == true ? "搬运" : "");
        tv_qian.setText("¥" + mInfo1.getFreight());

        iv_lianxi.setVisibility(TextUtils.isEmpty(mInfo1.getRemark())==true?View.GONE:View.VISIBLE);
    }



    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }


    /**
     * 获取当前时间
     *
     * @return
     */
    public String getNoeTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }


    class ShuoMClickableSpan extends ClickableSpan {

        String string;
        Context context;
        public ShuoMClickableSpan(String str,Context context){
            super();
            this.string = str;
            this.context = context;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(getResources().getColor(R.color.back_top));
        }

        @Override
        public void onClick(View widget) {

            playDialog = MyDialog.playDialog(DetailsActivity.this,string);
            playDialog.show();
            playDialog.setOnSettingListener(new MyDialog.EndListener() {
                @Override
                public void onSetting(String content) {
                    playDialog.dismiss();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + string));
                    startActivity(intent);
                }
            });

//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_CALL);
//            intent.setData(Uri.parse("tel:" + mInfo1.getSenderTel()));
//            startActivity(intent);
        }

    }


    class ShuoMClickableSpan2 extends ClickableSpan {

        String string;
        Context context;
        public ShuoMClickableSpan2(String str,Context context){
            super();
            this.string = str;
            this.context = context;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(getResources().getColor(R.color.back_top));
        }

        @Override
        public void onClick(View widget) {

        }

    }

}
