package cn.guugoo.jiapeiteacher.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import cn.guugoo.jiapeiteacher.R;
import cn.guugoo.jiapeiteacher.base.BaseActivity;
import cn.guugoo.jiapeiteacher.base.BaseAsyncTask;
import cn.guugoo.jiapeiteacher.base.Constants;
import cn.guugoo.jiapeiteacher.bean.ImageData;
import cn.guugoo.jiapeiteacher.bean.TotalData;
import cn.guugoo.jiapeiteacher.bean.WorkBeachInfo;
import cn.guugoo.jiapeiteacher.util.DensityUtil;
import cn.guugoo.jiapeiteacher.util.EncryptUtils;
import cn.guugoo.jiapeiteacher.util.HttpUtil;

/**
 * Created by gpw on 2016/8/3.
 * --加油
 */
public class WorkbenchActivity1 extends BaseActivity {
    private TextView tv_select;
    private boolean selectState = true;
    private LinearLayout ll_work;
    private LinearLayout ll_select;
    private LinearLayout ll_day;
    private LinearLayout ll_edit;
    private int width;
    private ArrayList<ImageView> ivs;
    private int teacherId;
    private EditText et_reason;
    private Button bt_edit;
    private Button bt_cancel;
    private boolean isNull;
    private String lastTime;
    private final static int refreshActivity = 44;
    private int startState;
    private String token;


    @Override
    protected int getLayout() {
        return R.layout.activity_workbench;
    }

    @Override
    protected void initView() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.head);
        TextView tv_center = (TextView) rl_head.findViewById(R.id.tv_center);
        TextView tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        ImageView iv_back = (ImageView) rl_head.findViewById(R.id.iv_back);

        bt_edit = (Button) findViewById(R.id.bt_edit);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        tv_select = (TextView) rl_head.findViewById(R.id.tv_select);
        ll_work = (LinearLayout) findViewById(R.id.ll_work);
        ll_day = (LinearLayout) findViewById(R.id.ll_day);
        ll_edit = (LinearLayout) findViewById(R.id.ll_edit);
        ll_select = (LinearLayout) findViewById(R.id.ll_select);
        et_reason = (EditText) findViewById(R.id.et_reason);


        tv_center.setText(R.string.workbench);
        tv_right.setText(R.string.refresh);
        tv_select.setText(R.string.select);


        tv_select.setOnClickListener(this);
        bt_edit.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        getServletData();

        if (startState == 7) {
            SharedPreferences prefs = getSharedPreferences(Constants.SHARED_NAME, Context.MODE_PRIVATE);
            String bookingId = prefs.getString("bookingId", "");
            Intent intent = new Intent();
            intent.setClass(WorkbenchActivity1.this, StudentDetailsActivity.class);
            intent.putExtra("bookingId", bookingId);
            startActivityForResult(intent, refreshActivity);
            startState = 0;
        }

    }

    private void getServletData() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("TeacherId", teacherId);
        jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
        new GetSchedulingAsyncTask(WorkbenchActivity1.this,HttpUtil.url_scheduling,token).execute(jsonObject);
    }


    @Override
    protected void initData() {
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_NAME, MODE_PRIVATE);
        teacherId = prefs.getInt("Id", 0);
        token = prefs.getString("token", "");
        ivs = new ArrayList<>();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        isNull = false;
        width = metric.widthPixels;
        startState = getIntent().getIntExtra("startState", 0);
        Constants.WorkActivityState = 1;
    }


    private void createView(String CrossTitle, String ColumnTitle, String BookingList) {


        String[] crossTitles = CrossTitle.split(",");
        String[] columnTitles = ColumnTitle.split(",");
        String[] BookingLists = BookingList.split("#");
        if (crossTitles.length >= 1) {
            lastTime = crossTitles[0];
        }

        LinearLayout.LayoutParams tv_params = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);

        LinearLayout.LayoutParams tv_params1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, width / 7);


        for (int i = 0; i <= crossTitles.length; i++) {
            TextView textView = new TextView(this);
            tv_params1.setMargins(0, 0, DensityUtil.dip2px(WorkbenchActivity1.this, 0.5), DensityUtil.dip2px(WorkbenchActivity1.this, 0.5));
            textView.setGravity(Gravity.CENTER);

            LinearLayout ll_vertical = new LinearLayout(this);
            ll_vertical.setOrientation(LinearLayout.VERTICAL);
            ll_vertical.setBackgroundColor(ContextCompat.getColor(this, R.color.color_Gray));


            if (i == 0) {
                textView.setText("");
                for (String columnTitle : columnTitles) {
                    TextView text_vertical1 = new TextView(this);
                    text_vertical1.setGravity(Gravity.CENTER);
                    text_vertical1.setBackgroundColor(ContextCompat.getColor(this, R.color.color_White));
                    StringBuilder sb = new StringBuilder(columnTitle);
                    int before = columnTitle.indexOf("-");
                    sb.replace(before, before + 1, "\n/\n");

                    text_vertical1.setText(sb.toString());

                    text_vertical1.setTextSize(12);
                    ll_vertical.addView(text_vertical1, tv_params1);
                }


            } else {
                String start = crossTitles[i - 1];
                StringBuilder sb = new StringBuilder(start);
                int before = start.indexOf(" ");
                sb.replace(before, before + 1, "\n");
                textView.setText(sb.toString());
                textView.setTextSize(12);

                String[] contents = BookingLists[i - 1].split("\\|");
                for (int j = 0; j < columnTitles.length; j++) {
                    createContentView(contents[j], ll_vertical, tv_params1);
                }
            }
            ll_day.addView(textView, tv_params);
            ll_work.addView(ll_vertical, tv_params);

        }


    }


    private void createContentView(String content, LinearLayout ll_vertical, LinearLayout.LayoutParams tv_params1) {
        final String bookingId = content.substring(content.indexOf(",") + 1, content.length());
        int state = Integer.parseInt(content.substring(0, content.indexOf(",")));

        final ImageView iv = new ImageView(WorkbenchActivity1.this);
        iv.setBackgroundColor(ContextCompat.getColor(this, R.color.color_White));
        if (state == 0 || state == 2 || state == -1) {
            ImageData imageData = new ImageData();
            imageData.setBookingId(bookingId);
            imageData.setState(state);
            imageData.setPress(false);
            iv.setTag(imageData);
        }

        switch (state) {
            case -1:
                iv.setImageResource(R.mipmap.work_state_6);
                iv.setBackgroundColor(ContextCompat.getColor(this, R.color.color_line));
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!selectState) {
                            ImageData data = (ImageData) iv.getTag();
                            boolean press = data.isPress();
                            if (!press) {
                                press = true;
                                iv.setImageResource(R.mipmap.work_state_10);
                            } else {
                                press = false;
                                iv.setImageResource(R.mipmap.work_state_6);
                            }
                            data.setPress(press);
                            iv.setTag(data);
                        }
                    }
                });
                ivs.add(iv);
                break;
            case 0:
                iv.setImageResource(R.mipmap.work_state_1);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!selectState) {
                            ImageData data = (ImageData) iv.getTag();
                            boolean press = data.isPress();
                            if (!press) {
                                press = true;
                                iv.setImageResource(R.mipmap.work_state_9);
                            } else {
                                press = false;
                                iv.setImageResource(R.mipmap.work_state_1);
                            }
                            data.setPress(press);
                            iv.setTag(data);
                        }
                    }
                });
                ivs.add(iv);
                break;
            case 1:
                iv.setImageResource(R.mipmap.work_state_4);
                if (selectState) {
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(WorkbenchActivity1.this, StudentDetailsActivity.class);
                            intent.putExtra("bookingId", bookingId);
                            startActivityForResult(intent, refreshActivity);
                        }
                    });
                }
                break;
            case 2:
                iv.setImageResource(R.mipmap.work_state_7);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!selectState) {
                            ImageData data = (ImageData) iv.getTag();
                            boolean press = data.isPress();
                            if (!press) {
                                press = true;
                                iv.setImageResource(R.mipmap.work_state_8);
                            } else {
                                press = false;
                                iv.setImageResource(R.mipmap.work_state_7);
                            }
                            data.setPress(press);
                            iv.setTag(data);
                        } else {
                            Intent intent = new Intent(WorkbenchActivity1.this, StudentDetailsActivity.class);
                            intent.putExtra("bookingId", bookingId);
                            startActivityForResult(intent, refreshActivity);
                        }
                    }
                });
                ivs.add(iv);
                break;
            case 3:
                iv.setImageResource(R.mipmap.work_state_2);
                iv.setBackgroundColor(ContextCompat.getColor(this, R.color.color_line));
                break;
            case 4:
                iv.setImageResource(R.mipmap.work_state_3);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectState) {
                            Intent intent = new Intent(WorkbenchActivity1.this, StudentDetailsActivity.class);
                            intent.putExtra("bookingId", bookingId);
                            startActivity(intent);
                        }
                    }
                });
                iv.setBackgroundColor(ContextCompat.getColor(this, R.color.color_line));
                break;

        }
        ll_vertical.addView(iv, tv_params1);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.WorkActivityState = 0;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, getIntent());
        super.onBackPressed();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select:
                if (selectState) {
                    selectState = false;
                    tv_select.setText(R.string.cancel);
                    tv_select.setTextColor(ContextCompat.getColor(this, R.color.color_Blue));
                } else {
                    selectState = true;
                    tv_select.setText(R.string.select);
                    tv_select.setTextColor(ContextCompat.getColor(this, R.color.color_Dark));
                    ll_edit.setVisibility(View.VISIBLE);
                    ll_select.setVisibility(View.INVISIBLE);

                    for (int i = 0; i < ivs.size(); i++) {
                        ImageView iv = ivs.get(i);
                        ImageData imageData = (ImageData) iv.getTag();
                        boolean press = imageData.isPress();
                        int state = imageData.getState();
                        if (press && state == 2) {
                            iv.setImageResource(R.mipmap.work_state_7);
                        } else if (press && state == 0) {
                            iv.setImageResource(R.mipmap.work_state_1);
                        } else if (press && state == -1) {
                            iv.setImageResource(R.mipmap.work_state_6);
                        }
                    }
                }
                break;
            case R.id.bt_edit:
                if (selectState) {
                    if (!isNull) {
                        Intent intent = new Intent(WorkbenchActivity1.this, EditActivity.class);
                        intent.putExtra("lastTime", lastTime);
                        startActivityForResult(intent, refreshActivity);
                    } else {
                        Toast.makeText(WorkbenchActivity1.this, "当前数据为空", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    ll_edit.setVisibility(View.INVISIBLE);
                    ll_select.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.bt_cancel:
                JsonArray jsonArray = new JsonArray();
                int num = 0;
                boolean restState = false;
                boolean normalState = false;
                for (int i = 0; i < ivs.size(); i++) {
                    ImageView iv = ivs.get(i);
                    ImageData imageData = (ImageData) iv.getTag();
                    boolean press = imageData.isPress();
                    int state = imageData.getState();

                    if (press && (state == 0 || state == 2)) {
                        num++;
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("BookId", imageData.getBookingId());
                        jsonArray.add(jsonObject);
                        normalState = true;
                    }
                    if (press && (state == -1)) {
                        num++;
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("BookId", imageData.getBookingId());
                        jsonArray.add(jsonObject);
                        restState = true;
                    }
                }

                if (num == 0) {
                    Toast.makeText(WorkbenchActivity1.this, "请先选择时间段", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (restState&&normalState){
                    Toast.makeText(WorkbenchActivity1.this, "不能同时选中休息跟可预约课程", Toast.LENGTH_SHORT).show();
                    return;
                }
                String reason = et_reason.getText().toString();
                if (reason.isEmpty()) {
                    Toast.makeText(WorkbenchActivity1.this, "请先输入原因", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (restState) {
                    setJson(jsonArray, reason,2);
                }
                if (normalState) {
                    setJson(jsonArray, reason,1);
                }
                break;

            case R.id.tv_right:
                ll_work.removeAllViews();
                ll_day.removeAllViews();
                ivs.clear();
                getServletData();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void setJson(JsonArray jsonArray, String reason,int type) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("TeacherId", teacherId);
        jsonObject.add("BookingListId", jsonArray);
        jsonObject.addProperty("ClearReason", reason);
        jsonObject.addProperty("Type", type);
        jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
        new BatchCancelClassAsyncTask(WorkbenchActivity1.this,HttpUtil.url_batchCancel,token).execute(jsonObject);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == refreshActivity && resultCode == RESULT_OK) {
            ll_work.removeAllViews();
            ll_day.removeAllViews();
            ivs.clear();
            getServletData();
        }

    }

    class GetSchedulingAsyncTask extends BaseAsyncTask {


        public GetSchedulingAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }




        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(WorkbenchActivity1.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                if (totalData.getData().toString().equals("\"\"")) {
                    Toast.makeText(WorkbenchActivity1.this, "当前数据为空", Toast.LENGTH_SHORT).show();
                    isNull = true;
                    return;
                }
                WorkBeachInfo workBeachInfo = gson.fromJson(totalData.getData(), WorkBeachInfo.class);
                String CrossTitle = workBeachInfo.getCrossTitle();
                String ColumnTitle = workBeachInfo.getColumnTitle();
                String BookingList = workBeachInfo.getBookingList();
                createView(CrossTitle, ColumnTitle, BookingList);

            } else {
                Toast.makeText(WorkbenchActivity1.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    class BatchCancelClassAsyncTask extends BaseAsyncTask {


        public BatchCancelClassAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }


        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(WorkbenchActivity1.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0 || totalData.getStatus() == 2) {
                Toast.makeText(WorkbenchActivity1.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
                et_reason.setText("");
                selectState = true;
                tv_select.setText(R.string.select);
                tv_select.setTextColor(ContextCompat.getColor(WorkbenchActivity1.this, R.color.color_Dark));
                ll_edit.setVisibility(View.VISIBLE);
                ll_select.setVisibility(View.INVISIBLE);

                ll_work.removeAllViews();
                ll_day.removeAllViews();
                ivs.clear();
                getServletData();


            } else {
                Toast.makeText(WorkbenchActivity1.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}