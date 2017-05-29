package cn.guugoo.jiapeiteacher.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import cn.guugoo.jiapeiteacher.R;
import cn.guugoo.jiapeiteacher.base.BaseActivity;
import cn.guugoo.jiapeiteacher.base.Constants;
import cn.guugoo.jiapeiteacher.bean.Exist;
import cn.guugoo.jiapeiteacher.bean.TotalData;
import cn.guugoo.jiapeiteacher.base.BaseAsyncTask;
import cn.guugoo.jiapeiteacher.util.EncryptUtils;
import cn.guugoo.jiapeiteacher.util.HttpUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gpw on 2016/8/4.
 * --加油
 */
public class EditActivity extends BaseActivity {
    private TimePickerView time_select;
    private EditText et_start_time;
    private EditText et_end_time;
    private EditText et_reason;
    private int teacherId;
    private int state = 0;
    private Button bt_cancel;
    private String start;
    private String end;
    private Date lasttime;
    private RadioButton rb_leave;
    private String type;
    private String token;
    @Override
    protected int getLayout() {
        return R.layout.activity_edit;
    }

    @Override
    protected void initView() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.head);
        TextView tv_center = (TextView) rl_head.findViewById(R.id.tv_center);
        ImageView iv_back = (ImageView) rl_head.findViewById(R.id.iv_back);

        et_start_time = (EditText) findViewById(R.id.et_start_time);
        et_end_time = (EditText) findViewById(R.id.et_end_time);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        et_reason = (EditText) findViewById(R.id.et_reason);
        rb_leave = (RadioButton) findViewById(R.id.rb_leave);

        time_select = new TimePickerView(this, TimePickerView.Type.ALL);

        tv_center.setText(R.string.edit);


        iv_back.setOnClickListener(this);

        et_start_time.setOnClickListener(this);
        et_end_time.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
        time_select.setTime(lasttime);
        time_select.setCancelable(true);

        time_select.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {

                if (state == 1) {
                    start = getStringTime(date);
                    if (start.isEmpty()){
                        showLongToastByString(R.string.time_null);
                        time_select.show();
                    }else if (lasttime.after(date)){
                        Toast.makeText(EditActivity.this, "请设置明天以后的时间", Toast.LENGTH_SHORT).show();
                        start="";
                        et_start_time.setText("");
                    }
                    else{
                        et_start_time.setText(start);
                    }
                }
                if (state == 2) {
                    end = getStringTime(date);
                    if (start.isEmpty()){
                        Toast.makeText(EditActivity.this, "请先设置开始的时间", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (end.isEmpty()){
                        showLongToastByString(R.string.time_null);
                        time_select.show();
                    }else if (getDateTime(start).after(date)){
                        showLongToastByString(R.string.time_error);
                        end="";
                        et_end_time.setText("");
                    }
                    else{
                        et_end_time.setText(end);
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {

        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_NAME, MODE_PRIVATE);
        teacherId = prefs.getInt("Id",0);
        token = prefs.getString("token","");
        String lastTime = getIntent().getStringExtra("lastTime");
        lasttime = getMonthTime(lastTime);
        start="";
        end="";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_start_time:
                state = 1;
                time_select.show();
                break;
            case R.id.et_end_time:
                state = 2;
                time_select.show();
                break;
            case R.id.bt_cancel:

                if (start.isEmpty() || end.isEmpty()) {
                    showLongToastByString(R.string.time_null);
                    return;
                }
                if (getDateTime(start).after(getDateTime(end))) {
                Toast.makeText(EditActivity.this, R.string.time_error, Toast.LENGTH_SHORT).show();
                return;
            }
                if (et_reason.getText().toString().isEmpty()) {
                    Toast.makeText(EditActivity.this, "请输入原因", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (rb_leave.isChecked()){
                    type="1";
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("TeacherId", teacherId);
                    jsonObject.addProperty("StartTime", start);
                    jsonObject.addProperty("EndTime", end);
                    jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
                    new IsCancelBookingsAsyncTask(EditActivity.this,HttpUtil.url_isCancelBookings,token).execute(jsonObject);
                }else {
                    type="2";
                    CancelClassByTime();
                }

                break;
            case R.id.iv_back:
                finish();
                break;
        }

    }

    private void CancelClassByTime() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("TeacherId", teacherId);
        jsonObject.addProperty("StartTime", start);
        jsonObject.addProperty("Type", type);
        jsonObject.addProperty("Reason", et_reason.getText().toString());
        jsonObject.addProperty("EndTime", end);
        jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
        new CancelClassByTimeAsyncTask(EditActivity.this,HttpUtil.url_cancelClassByTime,token).execute(jsonObject);
    }

    public static String getStringTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public static Date getDateTime(String date) {

        Date time = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            time = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static Date getMonthTime(String date) {
        Date time = null;
        try {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            String bookingId = date.substring(date.indexOf(" ") + 1, date.length());
            String stringtime = year+"-"+bookingId;
            System.out.println(stringtime);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            time = format.parse(stringtime);
            c.setTime(time);
            c.add(Calendar.DATE, 1); //日期加1天
            time = c.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    class CancelClassByTimeAsyncTask extends BaseAsyncTask {

        public CancelClassByTimeAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }
        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(EditActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                System.out.println(totalData.getData());
                Toast.makeText(EditActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK,getIntent());
                finish();

            } else {
                Toast.makeText(EditActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class IsCancelBookingsAsyncTask extends BaseAsyncTask {


        public IsCancelBookingsAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(EditActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                System.out.println(totalData.getData());
                Exist exist = gson.fromJson(totalData.getData(), Exist.class);

                if (("true").equals(exist.getIsExist())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("当前时间段有预约的课程，是否确定取消?");
                    builder.setCancelable(false);
                    builder.setIcon(R.mipmap.hint);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CancelClassByTime();

                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("取消");
                        }
                    });
                    builder.show();
                }else {
                    CancelClassByTime();
                }
            } else {
                Toast.makeText(EditActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
