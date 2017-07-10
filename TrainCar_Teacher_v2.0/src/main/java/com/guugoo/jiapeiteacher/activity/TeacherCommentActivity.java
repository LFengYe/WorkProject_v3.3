package com.guugoo.jiapeiteacher.activity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;

import com.guugoo.jiapeiteacher.R;
import com.guugoo.jiapeiteacher.adapter.StudentAdapter;
import com.guugoo.jiapeiteacher.base.BaseActivity;
import com.guugoo.jiapeiteacher.bean.ReservationStudent;
import com.guugoo.jiapeiteacher.bean.StudentDetails;
import com.guugoo.jiapeiteacher.bean.TotalData;
import com.guugoo.jiapeiteacher.base.BaseAsyncTask;
import com.guugoo.jiapeiteacher.util.EncryptUtils;
import com.guugoo.jiapeiteacher.util.HttpUtil;
import com.guugoo.jiapeiteacher.view.CircleImageView;

import static com.guugoo.jiapeiteacher.R.id.change_student;


/**
 * Created by gpw on 2016/8/13.
 * --加油
 */
public class TeacherCommentActivity extends BaseActivity {

    private RadioGroup rg_0;
    private RadioGroup rg_1;
    private RadioGroup rg_2;
    private RadioGroup rg_3;
    private RadioGroup rg_4;
    private CheckBox checkbox_0;
    private CheckBox checkbox_1;
    private CheckBox checkbox_2;
    private CheckBox checkbox_3;
    private CheckBox checkbox_4;
    private StudentDetails studentDetails;

    private CircleImageView civ_head;
    private ImageView studentChange;
    private TextView student_name1;
    private TextView student_name;
    private TextView student_sex;
    private TextView student_tel;
    private TextView student_cardId;
    private TextView student_driverType;
    private TextView student_bookingCourse;
    private TextView student_bookingTime;
    private EditText et_other;
    private TextView tv_ok;
    private String comment;

    private PopupWindow popupWindow;
    private int selectedIndex;
    private String bookingId;
    private int studentId;
    private String token;
    private float alpha;

    private ArrayList<ReservationStudent> students;

    @Override
    protected int getLayout() {
        return R.layout.activity_teacher_comment;
    }

    @Override
    protected void initView() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.head);
        TextView tv_center = (TextView) rl_head.findViewById(R.id.tv_center);
        ImageView iv_back = (ImageView) rl_head.findViewById(R.id.iv_back);

        civ_head = (CircleImageView) findViewById(R.id.civ_head);
        civ_head.setOnClickListener(this);
        studentChange = (ImageView) findViewById(change_student);
        student_name1 = (TextView) findViewById(R.id.student_name1);
        student_name = (TextView) findViewById(R.id.student_name);
        student_sex = (TextView) findViewById(R.id.student_sex);
        student_tel = (TextView) findViewById(R.id.student_tel);
        student_cardId = (TextView) findViewById(R.id.student_cardId);
        student_driverType = (TextView) findViewById(R.id.student_driverType);
        student_bookingCourse = (TextView) findViewById(R.id.student_bookingCourse);
        student_bookingTime = (TextView) findViewById(R.id.student_bookingTime);
        et_other = (EditText) findViewById(R.id.et_other);

        rg_0 = (RadioGroup) findViewById(R.id.rg_0);
        rg_1 = (RadioGroup) findViewById(R.id.rg_1);
        rg_2 = (RadioGroup) findViewById(R.id.rg_2);
        rg_3 = (RadioGroup) findViewById(R.id.rg_3);
        rg_4 = (RadioGroup) findViewById(R.id.rg_4);


        checkbox_0 = (CheckBox) findViewById(R.id.checkbox_0);
        checkbox_0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    enableRadioGroup(rg_0);
//                    setRadioGroupChecked(rg_0, 0);
                } else {
                    rg_0.clearCheck();
                    disableRadioGroup(rg_0);
                }
            }
        });
        checkbox_1 = (CheckBox) findViewById(R.id.checkbox_1);
        checkbox_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    enableRadioGroup(rg_1);
//                    setRadioGroupChecked(rg_1, 0);
                } else {
                    rg_1.clearCheck();
                    disableRadioGroup(rg_1);
                }
            }
        });
        checkbox_2 = (CheckBox) findViewById(R.id.checkbox_2);
        checkbox_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    enableRadioGroup(rg_2);
//                    setRadioGroupChecked(rg_2, 0);
                } else {
                    rg_2.clearCheck();
                    disableRadioGroup(rg_2);
                }
            }
        });
        checkbox_3 = (CheckBox) findViewById(R.id.checkbox_3);
        checkbox_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    enableRadioGroup(rg_3);
//                    setRadioGroupChecked(rg_3, 0);
                } else {
                    rg_3.clearCheck();
                    disableRadioGroup(rg_3);
                }
            }
        });
        checkbox_4 = (CheckBox) findViewById(R.id.checkbox_4);
        checkbox_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    enableRadioGroup(rg_4);
//                    setRadioGroupChecked(rg_4, 0);
                } else {
                    rg_4.clearCheck();
                    disableRadioGroup(rg_4);
                }
            }
        });


        tv_ok = (TextView) findViewById(R.id.tv_ok);
        tv_center.setText("总结点评");
        iv_back.setOnClickListener(this);
        tv_ok.setOnClickListener(this);
        if (students == null || students.size() < 2)
            studentChange.setVisibility(View.GONE);

        resetView();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("BookingId", bookingId);
        jsonObject.addProperty("StudentId", studentId);
        jsonObject.addProperty("BookingStatus", 1);
        jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
        new ClassStudentAsyncTask(TeacherCommentActivity.this,HttpUtil.url_classStudentDetails,token).execute(jsonObject);

    }

    @Override
    protected void initData() {
        studentDetails = new StudentDetails();
        comment = "";
        bookingId = getIntent().getStringExtra("bookingId");
        studentId = getIntent().getIntExtra("studentId", 0);
        token = getIntent().getStringExtra("token");
        students = getIntent().getParcelableArrayListExtra("studentList");
        selectedIndex = getIntent().getIntExtra("selectedIndex", 0);
    }

    @Override
    public void onClick(View v) {
        RadioButton radioButton;
        if (v.getId() == R.id.tv_ok) {
            if (checkbox_0.isChecked()) {
                if (rg_0.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(this, "请点评直角转弯", Toast.LENGTH_LONG).show();
                    return;
                }
                comment += checkbox_0.getText().toString();
                radioButton = (RadioButton) findViewById(rg_0.getCheckedRadioButtonId());
                comment += ":" + radioButton.getText().toString() + "#";
            }
            if (checkbox_1.isChecked()) {
                if (rg_1.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(this, "请点评S弯道行驶", Toast.LENGTH_LONG).show();
                    return;
                }
                comment += checkbox_1.getText().toString();
                radioButton = (RadioButton) findViewById(rg_1.getCheckedRadioButtonId());
                comment += ":" + radioButton.getText().toString() + "#";
            }
            if (checkbox_2.isChecked()) {
                if (rg_2.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(this, "请点评侧方停车", Toast.LENGTH_LONG).show();
                    return;
                }
                comment += checkbox_2.getText().toString();
                radioButton = (RadioButton) findViewById(rg_2.getCheckedRadioButtonId());
                comment += ":" + radioButton.getText().toString() + "#";
            }
            if (checkbox_3.isChecked()) {
                if (rg_3.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(this, "请点评坡道定点停车起步", Toast.LENGTH_LONG).show();
                    return;
                }
                comment += checkbox_3.getText().toString();
                radioButton = (RadioButton) findViewById(rg_3.getCheckedRadioButtonId());
                comment += ":" + radioButton.getText().toString() + "#";
            }
            if (checkbox_4.isChecked()) {
                if (rg_4.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(this, "请点评倒车入库", Toast.LENGTH_LONG).show();
                    return;
                }
                comment += checkbox_4.getText().toString();
                radioButton = (RadioButton) findViewById(rg_4.getCheckedRadioButtonId());
                comment += ":" + radioButton.getText().toString();
            }


            System.out.println(comment);
            if (comment.isEmpty()) {
                Toast.makeText(TeacherCommentActivity.this, "课程评论不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("BookingId", bookingId);
            jsonObject.addProperty("SubjectItem", comment);
            jsonObject.addProperty("Comment", et_other.getText().toString());
            jsonObject.addProperty("StudentId", studentId);

            jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
            new TeacherCommentAsyncTask(TeacherCommentActivity.this,HttpUtil.url_teacherComment, token).execute(jsonObject);

        } else if (v.getId() == R.id.iv_back) {
            finish();
        } else if (v.getId() == R.id.civ_head) {
            System.out.println(Arrays.toString(students.toArray()));
            if (students != null && students.size() > 1)
                showPopupWindow(v, students);
        }
    }

    private void resetView() {
        disableRadioGroup(rg_0);
        disableRadioGroup(rg_1);
        disableRadioGroup(rg_2);
        disableRadioGroup(rg_3);
        disableRadioGroup(rg_4);

        rg_0.clearCheck();
        rg_1.clearCheck();
        rg_2.clearCheck();
        rg_3.clearCheck();
        rg_4.clearCheck();

        checkbox_0.setChecked(false);
        checkbox_0.setClickable(true);
        checkbox_1.setChecked(false);
        checkbox_1.setClickable(true);
        checkbox_2.setChecked(false);
        checkbox_2.setClickable(true);
        checkbox_3.setChecked(false);
        checkbox_3.setClickable(true);
        checkbox_4.setChecked(false);
        checkbox_4.setClickable(true);

        et_other.setText("");
        et_other.setEnabled(true);
        tv_ok.setTextColor(getResources().getColor(R.color.color_Blue));

        tv_ok.setEnabled(true);
        tv_ok.setBackgroundResource(R.drawable.et_login_style);
    }

    private void showPopupWindow(View view, final ArrayList<ReservationStudent> students) {
        View contentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_list_popup, null);

        popupWindow = new PopupWindow(contentView, 300,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        ListView listView = (ListView) contentView.findViewById(R.id.list_content);
        final StudentAdapter adapter = new StudentAdapter(students, TeacherCommentActivity.this);
        adapter.setSelectedIndex(selectedIndex);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                resetView();
                selectedIndex = position;
                adapter.setSelectedIndex(selectedIndex);
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                ReservationStudent student = students.get(position);
                studentId = Integer.valueOf(student.getStudentId());

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("BookingId", bookingId);
                jsonObject.addProperty("StudentId", studentId);
                jsonObject.addProperty("BookingStatus", 1);
                jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
                new ClassStudentAsyncTask(TeacherCommentActivity.this,HttpUtil.url_classStudentDetails,token).execute(jsonObject);

            }
        });

        int[] location = new int[2];
        view.getLocationOnScreen(location);

        popupWindow.update();
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] + view.getHeight());
        popupWindow.setOnDismissListener(new TeacherCommentActivity.PopupDismissListener());

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

    public void disableRadioGroup(RadioGroup radioGroup) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setClickable(false);
        }
    }

    public void enableRadioGroup(RadioGroup radioGroup) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setClickable(true);
        }
    }

    public void setRadioGroupChecked(RadioGroup radioGroup, int checkIndex) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            if (i == checkIndex)
                ((RadioButton)radioGroup.getChildAt(i)).setChecked(true);
            else ((RadioButton)radioGroup.getChildAt(i)).setChecked(false);
        }
    }

    public int getRadioIndex(String subjectItemComment) {
        int selectIndex = -1;
        switch (subjectItemComment) {
            case "较差":
                selectIndex = 0;
                break;
            case "一般":
                selectIndex = 1;
                break;
            case "较好":
                selectIndex = 2;
                break;
            case "熟练":
                selectIndex = 3;
                break;
        }
        return selectIndex;
    }

    class ClassStudentAsyncTask extends BaseAsyncTask {

        public ClassStudentAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(TeacherCommentActivity.this, com.guugoo.jiapeiteacher.R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            //System.out.println("获取详情数据:" + s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                studentDetails = gson.fromJson(totalData.getData(), StudentDetails.class);
                student_name.setText(getResources().getText(com.guugoo.jiapeiteacher.R.string.student_name) + studentDetails.getName());
                student_name1.setText(studentDetails.getName());
                Glide.with(TeacherCommentActivity.this)
                        .load(studentDetails.getHead())
                        .crossFade()
                        .skipMemoryCache(false)
                        .into(civ_head);
                student_sex.setText(getResources().getText(com.guugoo.jiapeiteacher.R.string.student_sex) + studentDetails.getSex());
                student_tel.setText(getResources().getText(com.guugoo.jiapeiteacher.R.string.student_tel) + studentDetails.getTel());
                student_cardId.setText(getResources().getText(com.guugoo.jiapeiteacher.R.string.student_cardId) + studentDetails.getCardId());
                student_driverType.setText(getResources().getText(com.guugoo.jiapeiteacher.R.string.student_driverType) + studentDetails.getDriverType());
                student_bookingTime.setText(getResources().getText(com.guugoo.jiapeiteacher.R.string.student_bookingTime) + studentDetails.getBookingTime());
                student_bookingCourse.setText(getResources().getText(com.guugoo.jiapeiteacher.R.string.student_bookingCourse) + studentDetails.getBookingAccount());

                if (studentDetails.getIsComment() == 1 ||
                        studentDetails.getStatus() != 2) {
                    checkbox_0.setClickable(false);
                    checkbox_1.setClickable(false);
                    checkbox_2.setClickable(false);
                    checkbox_3.setClickable(false);
                    checkbox_4.setClickable(false);
                    tv_ok.setEnabled(false);
                    tv_ok.setBackgroundResource(R.drawable.disable_layout);
                    tv_ok.setTextColor(getResources().getColor(R.color.color_Gray));

                    et_other.setText(studentDetails.getCurrentComment());
                    et_other.setEnabled(false);
                    if (!TextUtils.isEmpty(studentDetails.getCurrentSubjectItem())) {
                        String[] currentSubjects = studentDetails.getCurrentSubjectItem().split("#");
                        for (String string : currentSubjects) {
                            String[] items = string.split(":");
                            if (items[0].compareTo("直角转弯") == 0) {
                                checkbox_0.setChecked(true);
                                enableRadioGroup(rg_0);
                                setRadioGroupChecked(rg_0, getRadioIndex(items[1]));
                                disableRadioGroup(rg_0);
                            }
                            if (items[0].compareTo("S弯道行驶") == 0) {
                                checkbox_1.setChecked(true);
                                enableRadioGroup(rg_1);
                                setRadioGroupChecked(rg_1, getRadioIndex(items[1]));
                                disableRadioGroup(rg_1);
                            }
                            if (items[0].compareTo("侧方停车") == 0) {
                                checkbox_2.setChecked(true);
                                enableRadioGroup(rg_2);
                                setRadioGroupChecked(rg_2, getRadioIndex(items[1]));
                                disableRadioGroup(rg_2);
                            }
                            if (items[0].compareTo("坡道定点停车起步") == 0) {
                                checkbox_3.setChecked(true);
                                enableRadioGroup(rg_3);
                                setRadioGroupChecked(rg_3, getRadioIndex(items[1]));
                                disableRadioGroup(rg_3);
                            }
                            if (items[0].compareTo("倒车入库") == 0) {
                                checkbox_4.setChecked(true);
                                enableRadioGroup(rg_4);
                                setRadioGroupChecked(rg_4, getRadioIndex(items[1]));
                                disableRadioGroup(rg_4);
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(TeacherCommentActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    class TeacherCommentAsyncTask extends BaseAsyncTask {


        public TeacherCommentAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(TeacherCommentActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                setResult(RESULT_OK, getIntent());
                finish();
                Toast.makeText(TeacherCommentActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TeacherCommentActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }
}
