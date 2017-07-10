package cn.guugoo.jiapeiteacher.activity;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import cn.guugoo.jiapeiteacher.R;
import cn.guugoo.jiapeiteacher.base.BaseActivity;
import cn.guugoo.jiapeiteacher.bean.Reservation;
import cn.guugoo.jiapeiteacher.bean.StudentDetails;
import cn.guugoo.jiapeiteacher.bean.TotalData;
import cn.guugoo.jiapeiteacher.base.BaseAsyncTask;
import cn.guugoo.jiapeiteacher.util.EncryptUtils;
import cn.guugoo.jiapeiteacher.util.HttpUtil;
import cn.guugoo.jiapeiteacher.view.CircleImageView;


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
   

    private CircleImageView civ_head;
    private TextView student_name1;
    private TextView student_name;
    private TextView student_sex;
    private TextView student_tel;
    private TextView student_cardId;
    private TextView student_driverType;
    private TextView student_bookingCourse;
    private TextView student_bookingTime;
    private EditText et_other;
    private String comment;

    private Reservation reservation;
    private String token;

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
        checkbox_1 = (CheckBox) findViewById(R.id.checkbox_1);
        checkbox_2 = (CheckBox) findViewById(R.id.checkbox_2);
        checkbox_3 = (CheckBox) findViewById(R.id.checkbox_3);
        checkbox_4 = (CheckBox) findViewById(R.id.checkbox_4);

        TextView tv_ok = (TextView) findViewById(R.id.tv_ok);

        tv_center.setText("总结点评");
        iv_back.setOnClickListener(this);
        assert tv_ok != null;
        tv_ok.setOnClickListener(this);

        student_name.setText(getResources().getText(R.string.student_name) + reservation.getName());
        student_name1.setText(reservation.getName());
        Glide.with(TeacherCommentActivity.this)
                .load(reservation.getHeadImg())
                .crossFade()
                .skipMemoryCache(false)
                .into(civ_head);
        student_sex.setText(getResources().getText(R.string.student_sex) + reservation.getSex());
        student_tel.setText(getResources().getText(R.string.student_tel) + reservation.getTel());
        student_cardId.setText(getResources().getText(R.string.student_cardId) + reservation.getCardNo());
        student_driverType.setText(getResources().getText(R.string.student_driverType) + reservation.getDriverType());
        student_bookingTime.setText(getResources().getText(R.string.student_bookingTime) + reservation.getBookingTime());
        student_bookingCourse.setText(getResources().getText(R.string.student_bookingCourse) + reservation.getBookingAccount());
    }

    @Override
    protected void initData() {
        comment = "";
        reservation = (Reservation) getIntent().getSerializableExtra("reservation");
        token = getIntent().getStringExtra("token");
    }

    @Override
    public void onClick(View v) {
        RadioButton radioButton;
        if (v.getId() == R.id.tv_ok) {
            if (checkbox_0.isChecked()) {
                comment += checkbox_0.getText().toString();
                radioButton = (RadioButton) findViewById(rg_0.getCheckedRadioButtonId());
                comment += ":" + radioButton.getText().toString() + "#";
            }
            if (checkbox_1.isChecked()) {
                comment += checkbox_1.getText().toString();
                radioButton = (RadioButton) findViewById(rg_1.getCheckedRadioButtonId());
                comment += ":" + radioButton.getText().toString() + "#";
            }
            if (checkbox_2.isChecked()) {
                comment += checkbox_2.getText().toString();
                radioButton = (RadioButton) findViewById(rg_2.getCheckedRadioButtonId());
                comment += ":" + radioButton.getText().toString() + "#";
            }
            if (checkbox_3.isChecked()) {
                comment += checkbox_3.getText().toString();
                radioButton = (RadioButton) findViewById(rg_3.getCheckedRadioButtonId());
                comment += ":" + radioButton.getText().toString() + "#";
            }
            if (checkbox_4.isChecked()) {
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
            jsonObject.addProperty("BookingId", reservation.getBookingId());
            jsonObject.addProperty("SubjectItem", comment);
            jsonObject.addProperty("Comment", et_other.getText().toString());
            System.out.println(jsonObject.toString());
            jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
            new TeacherCommentAsyncTask(TeacherCommentActivity.this, HttpUtil.url_teacherComment, token).execute(jsonObject);

        } else if (v.getId() == R.id.iv_back) {
            finish();
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
