package cn.guugoo.jiapeistudent.MinorActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.guugoo.jiapeistudent.App.ActivityCollector;
import cn.guugoo.jiapeistudent.App.Constant;
import cn.guugoo.jiapeistudent.Data.ReserveDetails;
import cn.guugoo.jiapeistudent.Data.ReturnData;
import cn.guugoo.jiapeistudent.MainActivity.BaseActivity;
import cn.guugoo.jiapeistudent.MainActivity.LoginActivity;
import cn.guugoo.jiapeistudent.R;
import cn.guugoo.jiapeistudent.Tools.DES;
import cn.guugoo.jiapeistudent.Tools.MyHandler;
import cn.guugoo.jiapeistudent.Tools.MyThread;
import cn.guugoo.jiapeistudent.Tools.MyToast;
import cn.guugoo.jiapeistudent.Tools.Utils;
import cn.guugoo.jiapeistudent.Views.TitleView;

import static cn.guugoo.jiapeistudent.R.id.ll_text2;

public class ReserveDetailsActivity extends BaseActivity {
    private static final String TAG = "ReserveDetailsActivity";
    private TextView[] textViews;
    private String BookingId;
    private SharedPreferences sp;
    private LinearLayout layout1,layout2;
    private TextView cancel;
    private RelativeLayout map;
    private String Lat;  //维度
    private String Lon;  //经度
    private String Location; //场地名
    private String RefId;
    private int PayType;

    protected Handler handler2 = new MyHandler(this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                Log.d(TAG, "handleMessage: "+msg.obj);
                ReturnData data= JSONObject.parseObject((String) msg.obj,ReturnData.class);
                if(data.getStatus()==0){
                    MyToast.makeText(ReserveDetailsActivity.this,data.getMessage());
                    cancel.setVisibility(View.GONE);
                    textViews[7].setText("已取消");
                }else {
                    MyToast.makeText(ReserveDetailsActivity.this,data.getMessage());
                }
            }
        }
    };

    protected Handler handler1 = new MyHandler(this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                Log.d(TAG, "handleMessage: "+msg.obj);
                ReturnData data= JSONObject.parseObject((String) msg.obj,ReturnData.class);
                if(data.getStatus()==0){
                    JSONObject jsonObject = JSON.parseObject(data.getData());
                    int CancelType= jsonObject.getInteger("CancelType");
                    switch (CancelType){
                        case 0:
                            MyToast.makeText(ReserveDetailsActivity.this,data.getMessage());
                            break;
                        case 1:
                            MyToast.makeText(ReserveDetailsActivity.this,data.getMessage());
                            cancel.setVisibility(View.GONE);
                            break;
                        case 2:
                            AlertDialog.Builder builder = new AlertDialog.Builder(ReserveDetailsActivity.this);
                            builder.setMessage(data.getMessage());
                            builder.setTitle("提示");
                            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ClearBooking();
                                }
                            });
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();
                            break;
                        default:
                            break;

                    }
                }else {
                    MyToast.makeText(ReserveDetailsActivity.this,data.getMessage());
                }
            }
        }
    };

    @Override
    protected void processingData(ReturnData data) {
        System.out.println(data.getData());

        ReserveDetails reserveDetails = JSONObject.parseObject(data.getData(),ReserveDetails.class);
        textViews[0].setText(reserveDetails.getTimeSlot());
        Location = reserveDetails.getBranch();
        textViews[1].setText(reserveDetails.getBranch());
        textViews[2].setText(reserveDetails.getTeacherNmae());
        textViews[3].setText(reserveDetails.getTeacherTel());
        textViews[4].setText(reserveDetails.getVehNof());
        textViews[5].setText(Utils.getSubject(reserveDetails.getSubject()));
        textViews[6].setText(String.valueOf(reserveDetails.getAmount())+"元");
        textViews[7].setText(reserveDetails.getStatus() + "");
        textViews[8].setText(reserveDetails.getBookingTime());
        Lat = reserveDetails.getLat();
        Lon = reserveDetails.getLon();
        /*
        Log.d(TAG, "processingData: "+reserveDetails.getStatus());
        if(reserveDetails.getStatus()!=1&&reserveDetails.getStatus()!=2
                &&reserveDetails.getStatus()!=4&&reserveDetails.getStatus() != 5){
            if(PayType!=-1){
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                textViews[9].setText(reserveDetails.getPayTime());
                switch (reserveDetails.getPayType()){
                    case 0:
                        textViews[10].setText("支付宝支付");
                        break;
                    case 1:
                        textViews[10].setText("微信支付");
                        break;
                    case 2:
                        textViews[10].setText("学时券支付");
                        break;
                }
            }
        }
        */
        if(reserveDetails.getStatus() == 0){
            cancel.setVisibility(View.VISIBLE);
        }else {
            cancel.setVisibility(View.GONE);
        }
        textViews[11].setText(reserveDetails.getComment());
        if(!TextUtils.isEmpty(reserveDetails.getSubjectItem())){
            textViews[12].setText(reserveDetails.getSubjectItem().replace("#","\n"));
        }

    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_reserve_details);
    }

    @Override
    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.reserve_details_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setRightTextVisible(true);
        titleView.setLeftText(R.string.home_text4);
        titleView.setMiddleText(R.string.details);
        titleView.setRightText(R.string.cancel_details);
        titleView.setLeftViewListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cancel = (TextView) findViewById(R.id.title_right_text);
        titleView.setRightTextListenter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    @Override
    protected void findView() {
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        textViews = new TextView[13];
        textViews[0] = (TextView) findViewById(R.id.reserve_details_text1);
        textViews[1] = (TextView) findViewById(R.id.reserve_details_text2);
        textViews[2] = (TextView) findViewById(R.id.reserve_details_text3);
        textViews[3] = (TextView) findViewById(R.id.reserve_details_text4);
        textViews[4] = (TextView) findViewById(R.id.reserve_details_text5);
        textViews[5] = (TextView) findViewById(R.id.reserve_details_text6);
        textViews[6] = (TextView) findViewById(R.id.reserve_details_text7);
        textViews[7] = (TextView) findViewById(R.id.reserve_details_text8);
        textViews[8] = (TextView) findViewById(R.id.reserve_details_text9);
        textViews[9] = (TextView) findViewById(R.id.reserve_details_text10);
        textViews[10] = (TextView) findViewById(R.id.reserve_details_text11);
        textViews[11] = (TextView) findViewById(R.id.reserve_details_text12);
        textViews[12] = (TextView) findViewById(R.id.reserve_details_text13);
        layout1 = (LinearLayout) findViewById(R.id.ll_text1);
        layout2 = (LinearLayout) findViewById(ll_text2);
        map = (RelativeLayout) findViewById(R.id.map);
    }

    @Override
    protected void init() {
        BookingId = getIntent().getStringExtra("BookingId");
        RefId = getIntent().getStringExtra("RefId");
        PayType = getIntent().getIntExtra("PayType",0);
        findDetails();

    }
    private void findDetails(){
        if(Utils.isNetworkAvailable(ReserveDetailsActivity.this)) {
            JSONObject json = new JSONObject();
            json.put("StudentId", sp.getInt("Id",0));
            json.put("BookingId", BookingId);
//            json.put("RefId",RefId);
            System.out.println(json.toJSONString());
            new MyThread(Constant.URL_BookingDetails, handler, DES.encryptDES(json.toString())).start();

        }else {
            MyToast.makeText(ReserveDetailsActivity.this,R.string.Toast_internet);
        }

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReserveDetailsActivity.this,MapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Lat",Lat);
                bundle.putString("Lon",Lon);
                bundle.putString("Location",Location);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void cancel(){
        if(Utils.isNetworkAvailable(ReserveDetailsActivity.this)) {
            JSONObject json = new JSONObject();
            json.put("BookingId",BookingId);
            json.put("StudentId",sp.getInt("Id",0));
            new MyThread(Constant.URL_CheckClearBooking, handler1, DES.encryptDES(json.toString())).start();
        }else {
            MyToast.makeText(ReserveDetailsActivity.this,R.string.Toast_internet);
        }
    }

    private void ClearBooking(){
        if(Utils.isNetworkAvailable(ReserveDetailsActivity.this)) {
            JSONObject json = new JSONObject();
            json.put("BookingsId",BookingId);
            json.put("StudentId",sp.getInt("Id",0));
            new MyThread(Constant.URL_ClearBooking, handler2, DES.encryptDES(json.toString())).start();
        }else {
            MyToast.makeText(ReserveDetailsActivity.this,R.string.Toast_internet);
        }
    }
}
