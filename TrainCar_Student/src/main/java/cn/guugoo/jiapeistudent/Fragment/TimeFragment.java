package cn.guugoo.jiapeistudent.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.asm.Type;

import cn.guugoo.jiapeistudent.App.Constant;
import cn.guugoo.jiapeistudent.Data.ReserveTime;
import cn.guugoo.jiapeistudent.Data.ReturnData;
import cn.guugoo.jiapeistudent.Data.TimeData;
import cn.guugoo.jiapeistudent.Interface.TimeRefreshListenter;
import cn.guugoo.jiapeistudent.MinorActivity.SelectCoachActivity;
import cn.guugoo.jiapeistudent.R;
import cn.guugoo.jiapeistudent.Tools.DES;
import cn.guugoo.jiapeistudent.Tools.DensityUtil;
import cn.guugoo.jiapeistudent.Tools.MyHandler;
import cn.guugoo.jiapeistudent.Tools.MyThread;
import cn.guugoo.jiapeistudent.Tools.MyToast;
import cn.guugoo.jiapeistudent.Tools.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/8/8.
 */
public class TimeFragment extends Fragment implements TimeRefreshListenter {
    private static final String TAG = "TimeFragment";
    private View fragmentView;
    private SharedPreferences sp;
    private LinearLayout ll_day,ll_content;
    private int width;//屏幕宽度
    private String[] crossTitles; //横向标题
    private String[] columnTitles; //纵向标题
    private String[] bookingLists; //表格中的内容
    private List<TextView> textViews;
    private int Type;
    private int BranchId=0;
    private Handler handler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView =inflater.inflate(R.layout.fragment_time,container,false);
        findById();
        handler = new MyHandler(this.getActivity()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == 1){
                    Log.d(TAG, "handleMessage: "+msg.obj);
                    ReturnData data= JSONObject.parseObject((String) msg.obj,ReturnData.class);
                    if(data.getStatus()==0){

                        ReserveTime reserveTime = JSONObject.parseObject(data.getData(),ReserveTime.class);
                        if(!TextUtils.isEmpty(reserveTime.getColumnTitle())&&!TextUtils.isEmpty(reserveTime.getCrossTitle())
                                &&!TextUtils.isEmpty(reserveTime.getBookingList())){
                            crossTitles = reserveTime.getCrossTitle().split(",");
                            columnTitles = reserveTime.getColumnTitle().split(",");
                            bookingLists = reserveTime.getBookingList().split("#");
                            createView(); //画界面
                            AddListener(); // 添加监听
                        }
                    }else {
                        MyToast.makeText(getContext(),data.getMessage());
                    }
                }
            }
        };
        init();
        return fragmentView;
    }


    private void findById() {

        ll_day = (LinearLayout) fragmentView.findViewById(R.id.layout_time_day);
        ll_content = (LinearLayout) fragmentView.findViewById(R.id.layout_time_content);
        sp = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        Bundle bundle =getArguments();
        if(bundle!=null){
            Type = bundle.getInt("type");
            switch (Type){
                case 0:break;
                case 1:
                    BranchId =bundle.getInt("BranchId");
                    Log.d(TAG, "findById: "+BranchId);
                    break;
            }
        }
    }

    private void init() {
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
                            Intent intent = new Intent(getActivity(), SelectCoachActivity.class);
//                            Log.d(TAG, "onClick: X"+timeData.getX());
//                            Log.d(TAG, "onClick: Y"+timeData.getY());
//                            Log.d(TAG, "onClick: D"+crossTitles[timeData.getX()-1]);
//                            Log.d(TAG, "onClick: T"+columnTitles[timeData.getY()]);
                            Bundle bundle= new Bundle();
                            String[] str = crossTitles[timeData.getX()-1].split(" ");
//                            Log.d(TAG, "onClick: BookingDay"+str[1]);
                            bundle.putString("BookingDay",str[1]);
                            bundle.putString("year",timeData.getYear());
                            bundle.putString("TimeSlot",columnTitles[timeData.getY()]);
                            bundle.putFloat("Price",timeData.getPrice());
                            bundle.putInt("BranchId",BranchId);
//                            Log.d(TAG, "onClick: 45646   ++"+BranchId);
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
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;

        for (int i = 0; i <= crossTitles.length; i++) {
            TextView textView = new TextView(getContext());
            LinearLayout.LayoutParams tv_params = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            LinearLayout.LayoutParams tv_params1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, width/8);

            tv_params1.setMargins(0, 0, DensityUtil.dip2px(getContext(),1),
                    DensityUtil.dip2px(getContext(),1));
            textView.setGravity(Gravity.CENTER);

            LinearLayout ll_vertical = new LinearLayout(getContext());
            ll_vertical.setOrientation(LinearLayout.VERTICAL);
            ll_vertical.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.TimeFragment));
            if (i == 0) {
                textView.setText("");
                for (int j = 0; j < columnTitles.length; j++) {
                    TextView text_vertical1 = new TextView(getContext());
                    text_vertical1.setGravity(Gravity.CENTER);
                    text_vertical1.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
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
                for (int j = 0; j < columnTitles.length; j++) {
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
        float price =Float.valueOf(Str[0]);
        int number = Integer.valueOf(Str[1]);
        String year = Str[2];
        if(number==0){
            TextView text = new TextView(getContext());
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
            text.setGravity(Gravity.CENTER);
            text.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            text.setText("已约满");
            TimeData timeData =new TimeData();
            timeData.setType(2);
            text.setTag(timeData);
            ll_vertical.addView(text,tv_params1);
            textViews.add(text);
        }else if(number==-1){
            TextView text = new TextView(getContext());
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
            text.setGravity(Gravity.CENTER);
            text.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            text.setText("已过期");
            TimeData timeData =new TimeData();
            timeData.setType(1);
            text.setTag(timeData);
            ll_vertical.addView(text,tv_params1);
            textViews.add(text);
        }else if(number==-2){
            TextView text = new TextView(getContext());
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
            text.setGravity(Gravity.CENTER);
            text.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            text.setText("不可预约");
            TimeData timeData =new TimeData();
            timeData.setType(1);
            text.setTag(timeData);
            ll_vertical.addView(text,tv_params1);
            textViews.add(text);
        }else {
            TextView text = new TextView(getContext());
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
            text.setGravity(Gravity.CENTER);
            text.setTextColor(0xFF4a8e0e);
            TimeData timeData =new TimeData();
            timeData.setType(3);
            timeData.setPrice(price);
            timeData.setYear(year);
            timeData.setX(x);
            timeData.setY(y);
            text.setTag(timeData);
            text.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            text.setText(String.format(getContext().getString(R.string.time_fragment_text3),price,number));
            ll_vertical.addView(text,tv_params1);
            textViews.add(text);
        }
    }
    private void getTimeTable(){
        if(Utils.isNetworkAvailable(getContext())){
            JSONObject json= new JSONObject(true);
            json.put("StudentId",sp.getInt("Id",0));
            json.put("Subject",sp.getInt("CurrentSubject", 0));
            json.put("SchoolId",sp.getInt("SchoolId",0));
            json.put("TeacherId",0);
            json.put("BranchId", 0);
            json.put("QueryTime", "");
            switch (Type){
                case 0:
                    json.put("BranchId",0);
                    json.put("Type","SJ");
                    break;
                case 1:
                    json.put("BranchId",BranchId);
                    json.put("Type","CD");
                    break;
            }
            Log.d(TAG, "getTimeTable: "+json.toString());
            new MyThread(Constant.URL_Timetable, handler, DES.encryptDES(json.toString())).start();

        }else {
            MyToast.makeText(getContext(),R.string.Toast_internet);
        }

    }

    @Override
    public void onMainAction() {
        ll_day.removeAllViews();
        ll_content.removeAllViews();
        getTimeTable();
    }
}
