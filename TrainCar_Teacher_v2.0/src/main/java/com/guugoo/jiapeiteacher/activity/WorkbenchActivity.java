package com.guugoo.jiapeiteacher.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.guugoo.jiapeiteacher.R;
import com.guugoo.jiapeiteacher.adapter.StatuteAdapter;
import com.guugoo.jiapeiteacher.adapter.StudentAdapter;
import com.guugoo.jiapeiteacher.adapter.StudentRecAdapter;
import com.guugoo.jiapeiteacher.base.BaseAsyncTask;
import com.guugoo.jiapeiteacher.base.Constants;
import com.guugoo.jiapeiteacher.bean.Booking;
import com.guugoo.jiapeiteacher.bean.ImageData;
import com.guugoo.jiapeiteacher.bean.ReservationStudent;
import com.guugoo.jiapeiteacher.bean.TotalData;
import com.guugoo.jiapeiteacher.bean.WorkBeachInfo;
import com.guugoo.jiapeiteacher.util.EncryptUtils;
import com.guugoo.jiapeiteacher.util.HttpUtil;
import com.guugoo.jiapeiteacher.util.Utils;
import com.guugoo.jiapeiteacher.view.CHScrollView2;
import com.guugoo.jiapeiteacher.view.RecycleViewDivider;
import com.guugoo.jiapeiteacher.view.XRecyclerView;

/**
 * Created by gpw on 2016/8/3.
 * --加油
 */
public class WorkbenchActivity extends CHScrollViewActivity {
    private TextView tv_select;
    private boolean selectState = true;
//    private LinearLayout ll_work;
    private LinearLayout ll_select;
//    private LinearLayout ll_day;
    private LinearLayout ll_edit;
    private int width;
    private ArrayList<TextView> contentViews;
    private int teacherId;
    private int schoolId;
    private EditText et_reason;
    private Button bt_edit;
    private Button bt_cancel;
    private boolean isNull;
    private String lastTime;
    private final static int refreshActivity = 44;
    private int startState;
    private String token;

    private Dialog dialog;
    private ListView listView;
    private List<CHScrollView2> mHScrollViews = new ArrayList<CHScrollView2>();
    private HashMap<String, ArrayList<Booking>> data;
    private PopupWindow popupWindow;
    private static Paint BLACK_PAINT = new Paint();
    static {
        BLACK_PAINT.setColor(Color.BLACK);
    }
    private String[] cols = new String[]{"6:00", "7:00", "8:00", "9:00", "10:00",
            "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
            "19:00", "20:00", "21:00", "22:00"};
    private ScrollAdapter mAdapter;
    private int hourWidth = 200;
    private float alpha;

    @Override
    protected int getLayout() {
        return R.layout.activity_workbench;
    }

    @Override
    protected void initData() {
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_NAME, MODE_PRIVATE);
        teacherId = prefs.getInt("Id", 0);
        schoolId = prefs.getInt("SchoolId", 0);
        token = prefs.getString("token", "");
        contentViews = new ArrayList<>();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        isNull = false;
        width = metric.widthPixels;
        startState = getIntent().getIntExtra("startState", 0);
        Constants.WorkActivityState = 1;
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
//        ll_work = (LinearLayout) findViewById(R.id.ll_work);
//        ll_day = (LinearLayout) findViewById(R.id.ll_day);
        ll_edit = (LinearLayout) findViewById(R.id.ll_edit);
        ll_select = (LinearLayout) findViewById(R.id.ll_select);
        et_reason = (EditText) findViewById(R.id.et_reason);

        CHScrollView2 headerScroll = (CHScrollView2) findViewById(R.id.item_scroll_title);
        LinearLayout layout = (LinearLayout) findViewById(R.id.item_scroll_title_content);
        //layout.removeAllViews();
        for (int i = 0; i < cols.length; i++) {
            MyTextView textView = new MyTextView(this);
            textView.setText(cols[i]);
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    hourWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            layout.addView(textView, layoutParams);
        }
        //添加头滑动事件
        mHScrollViews.add(0, headerScroll);
        listView = (ListView) findViewById(R.id.hlistview_scroll_list);

        tv_center.setText(R.string.workbench);
        tv_right.setText(R.string.refresh);
        tv_select.setText(R.string.select);


        tv_select.setOnClickListener(this);
        bt_edit.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        dialog = Utils.proDialog(this);

        getServletData();

        if (startState == 7) {
            SharedPreferences prefs = getSharedPreferences(Constants.SHARED_NAME, Context.MODE_PRIVATE);
            String bookingId = prefs.getString("bookingId", "");
            Intent intent = new Intent();
            intent.setClass(WorkbenchActivity.this, StudentDetailsActivity.class);
            intent.putExtra("bookingId", bookingId);
            startActivityForResult(intent, refreshActivity);
            startState = 0;
        }

    }

    private void getServletData() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("TeacherId", teacherId);
        jsonObject.addProperty("SchoolId", schoolId);
        jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
        dialog.show();
        new GetSchedulingAsyncTask(WorkbenchActivity.this,HttpUtil.url_scheduling,token).execute(jsonObject);
    }


    private void createView(String CrossTitle, String BookingList) {
        String[] crossTitles = CrossTitle.split("#");
        if (crossTitles.length > 0) {
            lastTime = crossTitles[0].split(",")[0];
        }
        if (data == null)
            data = new HashMap<>();
        else data.clear();

        String[] bookingLists = BookingList.split("#");
        for (String string : bookingLists) {
            String[] items = string.split(",");//分割课程信息
            if (items.length < 16) {
                System.out.println(items + "数据格式错误!");
                continue;
            }
            Booking booking = new Booking();
            booking.setBookingCode(items[0]);
            booking.setSchoolCode(items[1]);
            booking.setLocationCode(items[2]);
            booking.setLocationName(items[3]);
            booking.setTeacherCode(items[4]);
            booking.setTeacherName(items[5]);
            booking.setCardNo(items[6]);
            booking.setCourseDate(items[7]);
            booking.setCourseTime(items[8]);
            booking.setSubject(items[9]);
            booking.setBookingSum(Integer.valueOf(items[10]));
            booking.setBookAmount(Integer.valueOf(items[11]));
            booking.setWeekStr(items[12]);
            booking.setDateStr(items[13]);
            booking.setStatus(Integer.valueOf(items[14]));
            booking.setCarType(items[15]);
            if (items.length > 16) {
                ArrayList<ReservationStudent> students = new ArrayList<>();
                String[] studentStr = items[16].split("\\|");
                for (String str : studentStr) {
                    String[] stuItem = str.split("&");
                    ReservationStudent reservationStudent = new ReservationStudent();
                    reservationStudent.setStudentId(stuItem[0]);
                    reservationStudent.setName(stuItem[1]);
                    reservationStudent.setStudentTel(stuItem[2]);
                    reservationStudent.setStudentCardNo(stuItem[3]);
                    students.add(reservationStudent);
                }
                booking.setStudents(students);
            } else {
                booking.setStudents(null);
            }

            if (data.containsKey(booking.getDateStr())) {
                data.get(booking.getDateStr()).add(booking);
            } else {
                ArrayList<Booking> bookingList = new ArrayList<>();
                bookingList.add(booking);
                data.put(booking.getDateStr(), bookingList);
            }
        }

        mAdapter = new ScrollAdapter(this, data, R.layout.common_item_my_hlistview);
        listView.setAdapter(mAdapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.WorkActivityState = 0;
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
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
                }
                break;
            case R.id.bt_edit:
                if (selectState) {
                    if (!isNull) {
                        Intent intent = new Intent(WorkbenchActivity.this, EditActivity.class);
                        intent.putExtra("lastTime", lastTime);
                        startActivityForResult(intent, refreshActivity);
                    } else {
                        Toast.makeText(WorkbenchActivity.this, "当前数据为空", Toast.LENGTH_SHORT).show();

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
                for (int i = 0; i < contentViews.size(); i++) {
                    TextView iv = contentViews.get(i);
                    ImageData imageData = (ImageData) iv.getTag();
                    boolean press = imageData.isPress();
                    int state = imageData.getState();

                    if (press && (state == 1 || state == 2)) {
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
                    Toast.makeText(WorkbenchActivity.this, "请先选择时间段", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (restState&&normalState){
                    Toast.makeText(WorkbenchActivity.this, "不能同时选中休息跟可预约课程", Toast.LENGTH_SHORT).show();
                    return;
                }
                String reason = et_reason.getText().toString();
                if (reason.isEmpty()) {
                    Toast.makeText(WorkbenchActivity.this, "请先输入原因", Toast.LENGTH_SHORT).show();
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
//                ll_work.removeAllViews();
//                ll_day.removeAllViews();
                contentViews.clear();
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
        dialog.show();
        new BatchCancelClassAsyncTask(WorkbenchActivity.this,HttpUtil.url_batchCancel,token).execute(jsonObject);
    }

    class ScrollAdapter extends BaseAdapter {

        private HashMap<String, ArrayList<Booking>> data;
        private List<String> listKey;
        private Context context;
        private int res;
        private String maxTime;

        public ScrollAdapter(Context context, HashMap<String, ArrayList<Booking>> data, int res) {
            this.context = context;
            this.data = data;
            this.res = res;
            listKey = new ArrayList<>();
            Iterator iterator = data.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                listKey.add((String) entry.getKey());
            }
            Collections.sort(listKey, new Comparator<String>() {
                @Override
                public int compare(String lhs, String rhs) {
                    return lhs.compareTo(rhs);
                }
            });
        }

        @Override
        public int getCount() {
            if (listKey == null)
                return 0;
            return listKey.size();
        }

        @Override
        public Object getItem(int position) {
            if (data == null)
                return null;
            return data.get(listKey.get(position));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(context).inflate(res, null);

            ViewHolder holder = new ViewHolder();
            holder.itemTitleV = (TextView) convertView.findViewById(R.id.item_titlev);
            holder.itemChScroll = (CHScrollView2) convertView.findViewById(R.id.item_chscroll_scroll);
            holder.itemLayout = (RelativeLayout) convertView.findViewById(R.id.item_chscroll_scroll_content);
            holder.itemLayout.removeAllViews();

            final ArrayList<Booking> bookings = data.get(listKey.get(position));
            holder.itemTitleV.setText(bookings.get(0).getWeekStr() + "\n"
                    + bookings.get(0).getDateStr());



            final Drawable drawable = ContextCompat.getDrawable(WorkbenchActivity.this, R.mipmap.work_state_9);
            for (final Booking booking : bookings) {
                ImageData imageData = new ImageData();
                imageData.setState(booking.getStatus());
                imageData.setPress(false);
                imageData.setBookingId(booking.getBookingCode());

                String[] times = booking.getCourseTime().split("-");
                if (maxTime == null || times[1].compareTo(maxTime) > 0) {
                    maxTime = times[1];
                }

                final MyTextView textView = new MyTextView(context);
                switch (booking.getStatus()){
                    case -1:
                        textView.setBackgroundColor(ContextCompat.getColor(WorkbenchActivity.this, R.color.color_Gray));
                        textView.setGravity(Gravity.CENTER);
                        textView.setText("请假");
                        textView.setTextColor(ContextCompat.getColor(WorkbenchActivity.this, R.color.color_White));
                        break;
                    case 1:
                        if (booking.getBookAmount() == 0) {
                            textView.setBackgroundResource(R.drawable.all_border);
                            textView.setTextColor(ContextCompat.getColor(WorkbenchActivity.this, R.color.color_Blue));

                        }
                        if (booking.getBookAmount() > 0) {
                            textView.setBackgroundColor(ContextCompat.getColor(WorkbenchActivity.this, R.color.color_Blue));
                            textView.setTextColor(ContextCompat.getColor(WorkbenchActivity.this, R.color.color_White));
                        }
                        textView.setGravity(Gravity.CENTER);
                        //textView.setText("未开始");
                        textView.setText(booking.getBookAmount()+"/"+booking.getBookingSum());
                        break;
                    case 2:
                        textView.setBackgroundColor(ContextCompat.getColor(WorkbenchActivity.this, R.color.color_Blue));
                        textView.setGravity(Gravity.CENTER);
                        //textView.setText("进行中");
                        if (getWidth(times[0], times[1]) < 100) {
                            textView.setText(booking.getBookAmount()+"/"+booking.getBookingSum());
                        } else {
                            textView.setText("进行中"+"\n"+booking.getBookAmount()+"/"+booking.getBookingSum());
                        }
                        textView.setTextColor(ContextCompat.getColor(WorkbenchActivity.this, R.color.color_White));
                        break;
                    case 3:
                        textView.setBackgroundColor(ContextCompat.getColor(WorkbenchActivity.this, R.color.color_Gray));
                        textView.setGravity(Gravity.CENTER);
                        //textView.setText("已结束");
                        if (getWidth(times[0], times[1]) < 100) {
                            textView.setText(booking.getBookAmount()+"/"+booking.getBookingSum());
                        } else {
                            textView.setText("已结束"+"\n"+booking.getBookAmount()+"/"+booking.getBookingSum());
                        }
                        textView.setTextColor(ContextCompat.getColor(WorkbenchActivity.this, R.color.color_White));
                        break;
                    case 4:
                        textView.setBackgroundColor(ContextCompat.getColor(WorkbenchActivity.this, R.color.color_Gray));
                        textView.setGravity(Gravity.CENTER);
                        textView.setText("已过期");
                        textView.setTextColor(ContextCompat.getColor(WorkbenchActivity.this, R.color.color_White));
                        break;
                }
                if (booking.getStatus() == -1 || booking.getStatus() == 1 || booking.getStatus() == 2) {
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!selectState) {
                                ImageData data = (ImageData) textView.getTag();
                                boolean press = data.isPress();
                                if (!press) {
                                    press = true;
                                    textView.setCompoundDrawablePadding(-44);
                                    textView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                                } else {
                                    press = false;
                                    textView.setCompoundDrawables(null, null, null, null);
                                }
                                data.setPress(press);
                                textView.setTag(data);
                            } else {
                                if (booking.getStudents() != null && booking.getStudents().size() > 0) {
                                    showPopupWindow(textView, booking);
                                    /*
                                    if (booking.getStudents().size() == 1) {
                                        ReservationStudent student = booking.getStudents().get(0);
                                        Intent intent = new Intent(WorkbenchActivity.this, StudentDetailsActivity.class);
                                        intent.putExtra("studentId", Integer.valueOf(student.getStudentId()));
                                        intent.putExtra("status", booking.getStatus());
                                        intent.putExtra("bookingId", booking.getBookingCode());
                                        startActivity(intent);
                                    } else {
                                        showPopupWindow(textView, booking);
                                    }
                                    */
                                }
                            }
                        }
                    });
                }
                if (booking.getStatus() == 3) {
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(selectState) {
                                if (booking.getStudents() != null && booking.getStudents().size() > 0) {
                                    showPopupWindow(textView, booking);
                                    /*
                                    if (booking.getStudents().size() == 1) {
                                        ReservationStudent student = booking.getStudents().get(0);
                                        Intent intent = new Intent(WorkbenchActivity.this, StudentDetailsActivity.class);
                                        intent.putExtra("studentId", Integer.valueOf(student.getStudentId()));
                                        intent.putExtra("status", booking.getStatus());
                                        intent.putExtra("bookingId", booking.getBookingCode());
                                        startActivity(intent);
                                    } else {
                                        showPopupWindow(textView, booking);
                                    }
                                    */
                                }
                            }
                        }
                    });
                }

                textView.setTag(imageData);
                contentViews.add(textView);

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        getWidth(times[0], times[1]), RelativeLayout.LayoutParams.MATCH_PARENT);
                layoutParams.setMargins(getWidth("6:00", times[0]), 1, 1, 1);
                holder.itemLayout.addView(textView, layoutParams);
                //textView.setOnClickListener();
            }

            if (maxTime.compareTo("23:00") < 0) {
                TextView textView = new TextView(context);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        getWidth(maxTime, "23:00"), RelativeLayout.LayoutParams.MATCH_PARENT);
                layoutParams.setMargins(getWidth("6:00", maxTime), 1, 1, 1);
                holder.itemLayout.addView(textView, layoutParams);
            }

            addHViews(holder.itemChScroll, position + 1);
            return convertView;
        }
    }


    private void showPopupWindow(View view, final Booking booking) {
        final ArrayList<ReservationStudent> students = booking.getStudents();
        View contentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_list_popup, null);

        popupWindow = new PopupWindow(contentView, 300,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        ListView listView = (ListView) contentView.findViewById(R.id.list_content);
        StudentAdapter adapter = new StudentAdapter(students, WorkbenchActivity.this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReservationStudent student = students.get(position);
                Intent intent = new Intent(WorkbenchActivity.this, StudentDetailsActivity.class);
                intent.putExtra("studentId", Integer.valueOf(student.getStudentId()));
                intent.putParcelableArrayListExtra("studentList", students);
                intent.putExtra("selectedIndex", position);
                intent.putExtra("status", booking.getStatus());
                intent.putExtra("bookingId", booking.getBookingCode());
                startActivity(intent);
            }
        });

        int[] location = new int[2];
        view.getLocationOnScreen(location);

        //popupWindow.update();
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());//不添加这一句, popupwindow消失不了
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] + view.getHeight());
        popupWindow.setOnDismissListener(new PopupDismissListener());

        alpha = 1f;
        backgroundChange();
    }

    /**
     * 返回或者点击空白位置的时候将背景透明度改回来
     */
    class PopupDismissListener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            new Thread(new Runnable(){
                @Override
                public void run() {
                    //此处while的条件alpha不能<= 否则会出现黑屏
                    while(alpha < 1f){
                        try {
                            Thread.sleep(4);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        alpha += 0.01f;
                        msg.obj = alpha ;
                        mHandler.sendMessage(msg);
                    }
                }

            }).start();
        }
    }

    private void backgroundChange() {
        new Thread(new Runnable(){
            @Override
            public void run() {
                while(alpha > 0.5f){
                    try {
                        //4是根据弹出动画时间和减少的透明度计算
                        Thread.sleep(4);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    //每次减少0.01，精度越高，变暗的效果越流畅
                    alpha -= 0.01f;
                    msg.obj = alpha ;
                    mHandler.sendMessage(msg);
                }
            }

        }).start();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    backgroundAlpha((float)msg.obj);
                    break;
            }
        }
    };

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    public void addHViews(final CHScrollView2 hScrollView, int index) {
        if (!mHScrollViews.isEmpty()) {
            int size = mHScrollViews.size();
            CHScrollView2 scrollView = mHScrollViews.get(size - 1);
            final int scrollX = scrollView.getScrollX();
            //第一次满屏后，向下滑动，有一条数据在开始时未加入
            if (scrollX != 0) {
                listView.post(new Runnable() {
                    @Override
                    public void run() {
                        //当listView刷新完成之后，把该条移动到最终位置
                        hScrollView.scrollTo(scrollX, 0);
                    }
                });
            }
        }
        mHScrollViews.add(index, hScrollView);
    }

    class ViewHolder {
        public TextView itemTitleV;
        public CHScrollView2 itemChScroll;
        public RelativeLayout itemLayout;
    }

    class MyTextView extends TextView {

        public MyTextView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1,
                    this.getHeight() - 1, BLACK_PAINT);

        }
    }

    private int getWidth(String startTime, String endTime) {
        String[] startStr = startTime.split(":");
        String[] endStr = endTime.split(":");
        int minutes = (Integer.valueOf(endStr[0]) - Integer.valueOf(startStr[0])) * 60
                + (Integer.valueOf(endStr[1]) - Integer.valueOf(startStr[1]));
        return minutes * hourWidth / 60;
    }

    public void onScrollChanged(int l, int t, int oldl, int oldt){
        for(CHScrollView2 scrollView : mHScrollViews) {
            //防止重复滑动
            if(mTouchView != scrollView)
                scrollView.smoothScrollTo(l, t);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == refreshActivity && resultCode == RESULT_OK) {
//            ll_work.removeAllViews();
//            ll_day.removeAllViews();
            contentViews.clear();
            getServletData();
        }

    }

    class GetSchedulingAsyncTask extends BaseAsyncTask {


        public GetSchedulingAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
            //Constants.showLargeLog(s, 3500, "Workbench");
            if (s.isEmpty()) {
                Toast.makeText(WorkbenchActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                if (totalData.getData().toString().equals("\"\"")) {
                    Toast.makeText(WorkbenchActivity.this, "当前数据为空", Toast.LENGTH_SHORT).show();
                    isNull = true;
                    return;
                }
                WorkBeachInfo workBeachInfo = gson.fromJson(totalData.getData(), WorkBeachInfo.class);
                String CrossTitle = workBeachInfo.getTimeCoordinate();
                String BookingList = workBeachInfo.getBookingList();
                createView(CrossTitle, BookingList);

            } else {
                Toast.makeText(WorkbenchActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    class BatchCancelClassAsyncTask extends BaseAsyncTask {


        public BatchCancelClassAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }


        @Override
        protected void dealResults(String s) {
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
            if (s.isEmpty()) {
                Toast.makeText(WorkbenchActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0 || totalData.getStatus() == 2) {
                Toast.makeText(WorkbenchActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
                et_reason.setText("");
                selectState = true;
                tv_select.setText(R.string.select);
                tv_select.setTextColor(ContextCompat.getColor(WorkbenchActivity.this, R.color.color_Dark));
                ll_edit.setVisibility(View.VISIBLE);
                ll_select.setVisibility(View.INVISIBLE);

//                ll_work.removeAllViews();
//                ll_day.removeAllViews();
                contentViews.clear();
                getServletData();


            } else {
                Toast.makeText(WorkbenchActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}