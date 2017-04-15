package cn.guugoo.jiapeistudent.MinorActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import cn.guugoo.jiapeistudent.App.Constant;
import cn.guugoo.jiapeistudent.Data.ReserveTime;
import cn.guugoo.jiapeistudent.Data.ReturnData;
import cn.guugoo.jiapeistudent.Data.TimeData;
import cn.guugoo.jiapeistudent.MainActivity.BaseActivity;
import cn.guugoo.jiapeistudent.R;
import cn.guugoo.jiapeistudent.Tools.DES;
import cn.guugoo.jiapeistudent.Tools.DensityUtil;
import cn.guugoo.jiapeistudent.Tools.MyThread;
import cn.guugoo.jiapeistudent.Tools.MyToast;
import cn.guugoo.jiapeistudent.Tools.Utils;
import cn.guugoo.jiapeistudent.Views.TitleView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SelectTimeActivity extends BaseActivity {

    private static final String TAG = "SelectTimeActivity";
    private SharedPreferences sp;
    private LinearLayout ll_day,ll_content;
    private int width;//屏幕宽度
    private String[] crossTitles; //横向标题
    private String[] columnTitles; //纵向标题
    private String[] bookingLists; //表格中的内容
    private List<TextView> textViews;
    private String Branch;
    private String Name;
    private int TeacherId ;
    private String VehNof;
    private String Tel;

    @Override
    protected void processingData(ReturnData data) {
        Log.d(TAG, "handleMessage: "+data.getData());
        ReserveTime reserveTime = JSONObject.parseObject(data.getData(),ReserveTime.class);
        if(!TextUtils.isEmpty(reserveTime.getColumnTitle())&&!TextUtils.isEmpty(reserveTime.getCrossTitle())
                &&!TextUtils.isEmpty(reserveTime.getBookingList())) {
            crossTitles = reserveTime.getCrossTitle().split(",");
            columnTitles = reserveTime.getColumnTitle().split(",");
            bookingLists = reserveTime.getBookingList().split("#");
            createView(); //画界面
            AddListener(); // 添加监听
        }
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_select_time);
    }

    @Override
    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.select_time_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setMiddleText(R.string.select_time);
        titleView.setRightTextVisible(true);
        titleView.setRightText(R.string.refresh);
        titleView.setRightTextListenter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_day.removeAllViews();
                ll_content.removeAllViews();
                getTimeTable();
            }
        });
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void findView() {
        ll_day = (LinearLayout) findViewById(R.id.select_time_day);
        ll_content = (LinearLayout) findViewById(R.id.select_time_content);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        Bundle bundle =intent.getExtras();
        Branch =bundle.getString("Branch");
        Name = bundle.getString("Name");
        TeacherId = bundle.getInt("TeacherId");
        VehNof = bundle.getString("VehNof");
        Tel =bundle.getString("Tel");
        textViews = new ArrayList<TextView>();
        getTimeTable();
    }

    private void AddListener(){
        Iterator<TextView> iterator = textViews.iterator();
        while (iterator.hasNext()){
            TextView textView = iterator.next();
            final TimeData timeData= (TimeData) textView.getTag();
            switch (timeData.getType()){
                case 1:
                case 2:break;
                case 3:
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(SelectTimeActivity.this, ReserveVerifyActivity.class);
                            Bundle bundle= new Bundle();
                            String[] str = crossTitles[timeData.getX()-1].split(" ");
                            Log.d(TAG, "onClick: BookingDay"+str[1]);
                            bundle.putString("BookingDay",str[1]);
                            bundle.putString("TimeSlot",columnTitles[timeData.getY()]);
                            bundle.putFloat("Price",timeData.getPrice());
                            bundle.putString("Branch",Branch);
                            bundle.putString("Name",Name);
                            bundle.putInt("TeacherId",TeacherId);
                            bundle.putString("VehNof",VehNof);
                            bundle.putString("Tel",Tel);
                            bundle.putString("year",timeData.getYear());
                            Log.d(TAG, "onClick: "+TeacherId);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                    break;
            }
        }

    }
    private void createView() {

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;
//
//        String CrossTitle = "周四 07-28,周五 07-29,周六 07-30,周日 07-31,周一 08-01,周二 08-02,周三 08-03";
//        String ColumnTitle = "08:00-09:00,09:10-10:10,10:20-11:20,11:30-12:30,12:40-13:40,13:50-14:50,15:00-16:00,16:10-17:10";
//        String BookingList = "118,1|117,1|120,2|0,0|122,2|123,30|124,30|120,0#120,30|120,30|120,30|120,30|120,30|120,5|120,5|120,30#120,30|120,4|120,30|120,30|120,4|120,30|120,30|120,7#120,30|120,7|120,30|120,30|120,6|120,6|120,3|120,30#120,30|120,6|120,30|120,30|120,7|120,6|120,4|120,30#120,6|120,30|120,30|120,30|120,30|120,4|120,30|120,30#120,30|120,30|120,30|120,30|120,30|120,30|120,30|120,30";
//
//        crossTitles = CrossTitle.split(",");
//        columnTitles = ColumnTitle.split(",");
//        bookingLists = BookingList.split("#");


        for (int i = 0; i <= crossTitles.length; i++) {
            TextView textView = new TextView(SelectTimeActivity.this);
            LinearLayout.LayoutParams tv_params = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            LinearLayout.LayoutParams tv_params1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, width/8);

            tv_params1.setMargins(0, 0, DensityUtil.dip2px(SelectTimeActivity.this,1),
                    DensityUtil.dip2px(SelectTimeActivity.this,1));
            textView.setGravity(Gravity.CENTER);

            LinearLayout ll_vertical = new LinearLayout(SelectTimeActivity.this);
            ll_vertical.setOrientation(LinearLayout.VERTICAL);
            ll_vertical.setBackgroundColor(ContextCompat.getColor(SelectTimeActivity.this, R.color.TimeFragment));

            if (i == 0) {
                textView.setText("");
                for (int j = 0; j < columnTitles.length; j++) {
                    TextView text_vertical1 = new TextView(SelectTimeActivity.this);
                    text_vertical1.setGravity(Gravity.CENTER);
                    text_vertical1.setBackgroundColor(ContextCompat.getColor(SelectTimeActivity.this, R.color.white));
                    String time_before = columnTitles[j];
                    StringBuilder sb = new StringBuilder(time_before);
                    int before = time_before.indexOf("-");
                    int after = time_before.indexOf("-") + 1;
                    sb.replace(before, after, "\n/\n");
                    String time = sb.toString();
                    text_vertical1.setText(time);
                    text_vertical1.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                    ll_vertical.addView(text_vertical1, tv_params1);
                }
            } else {
                textView.setText(crossTitles[i - 1].replace(" ","\n"));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
                String[] contents = bookingLists[i - 1].split("\\|");
                for (int j = 0; j <  columnTitles.length; j++) {
                    createContentView(contents[j],ll_vertical,tv_params1,i,j);
                }
            }
            ll_day.addView(textView, tv_params);
            ll_content.addView(ll_vertical, tv_params);
        }


    }

    private void createContentView(String content, LinearLayout ll_vertical,
                                   LinearLayout.LayoutParams tv_params1,int x,int y) {
        String[] Str =content.split(",");
        float price = Float.valueOf(Str[0]);
        int number = Integer.valueOf(Str[1]);
        String year = Str[2];
        if(number==0){
            TextView text = new TextView(SelectTimeActivity.this);
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
            text.setGravity(Gravity.CENTER);
            text.setBackgroundColor(ContextCompat.getColor(SelectTimeActivity.this, R.color.white));
            text.setText("已约满");
            TimeData timeData =new TimeData();
            timeData.setType(2);
            text.setTag(timeData);
            ll_vertical.addView(text,tv_params1);
            textViews.add(text);
        }else if(number==-1){
            TextView text = new TextView(SelectTimeActivity.this);
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
            text.setGravity(Gravity.CENTER);
            text.setBackgroundColor(ContextCompat.getColor(SelectTimeActivity.this, R.color.white));
            text.setText("已过期");
            TimeData timeData =new TimeData();
            timeData.setType(1);
            text.setTag(timeData);
            ll_vertical.addView(text,tv_params1);
            textViews.add(text);
        }else if(number==-2){
            TextView text = new TextView(SelectTimeActivity.this);
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
            text.setGravity(Gravity.CENTER);
            text.setBackgroundColor(ContextCompat.getColor(SelectTimeActivity.this, R.color.white));
            text.setText("不可预约");
            TimeData timeData =new TimeData();
            timeData.setType(1);
            text.setTag(timeData);
            ll_vertical.addView(text,tv_params1);
            textViews.add(text);
        }else {
            TextView text = new TextView(SelectTimeActivity.this);
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
            text.setGravity(Gravity.CENTER);
            text.setTextColor(0xFF4a8e0e);
            TimeData timeData =new TimeData();
            timeData.setType(3);
            timeData.setPrice(price);
            timeData.setX(x);
            timeData.setYear(year);
            timeData.setY(y);
            text.setTag(timeData);
            text.setBackgroundColor(ContextCompat.getColor(SelectTimeActivity.this, R.color.white));
            text.setText(String.format(SelectTimeActivity.this.getString(R.string.time_fragment_text3),price,number));
            ll_vertical.addView(text,tv_params1);
            textViews.add(text);
        }
    }
    private void getTimeTable(){
        if(Utils.isNetworkAvailable(SelectTimeActivity.this)){
            JSONObject json= new JSONObject(true);
            json.put("Type","JL");
            json.put("Subject",sp.getInt("CurrentSubject", 0));
            json.put("SchoolId",sp.getInt("SchoolId",0));
            json.put("TeacherId",TeacherId);
            json.put("StudentId",sp.getInt("Id",0));
            json.put("BranchId",0);
            Log.d(TAG, "getTimeTable: "+json.toString());
            new MyThread(Constant.URL_Timetable, handler, DES.encryptDES(json.toString())).start();

        }else {
            MyToast.makeText(SelectTimeActivity.this,R.string.Toast_internet);
        }
    }
}
