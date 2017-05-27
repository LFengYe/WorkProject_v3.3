package cn.guugoo.jiapeistudent.MinorActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import cn.guugoo.jiapeistudent.App.Constant;
import cn.guugoo.jiapeistudent.Data.ReturnData;
import cn.guugoo.jiapeistudent.MainActivity.BaseActivity;
import cn.guugoo.jiapeistudent.R;
import cn.guugoo.jiapeistudent.Tools.DES;
import cn.guugoo.jiapeistudent.Tools.MyThread;
import cn.guugoo.jiapeistudent.Tools.MyToast;
import cn.guugoo.jiapeistudent.Tools.Utils;
import cn.guugoo.jiapeistudent.Views.TitleView;

public class ReserveVerifyActivity extends BaseActivity {
    private static final String TAG = "ReserveVerifyActivity";
    private TextView[] textViews;
    private Button button;
    private String TimeSlot;
    private String BookingDay;
//    private float price;
    private String Branch;
    private String Name;
    private String VehNof;
    private String TeacherId;
    private String Tel;
    private SharedPreferences sp;
    private String year;

    @Override
    protected void processingData(ReturnData data) {
        MyToast.makeText(ReserveVerifyActivity.this,data.getMessage());
        finish();
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_reserve_verify);
    }

    @Override
    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.reserve_verify_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setMiddleText(R.string.reserve_verify);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void findView() {
        textViews = new TextView[7];
        textViews[0] = (TextView) findViewById(R.id.reserve_verify_text1);
        textViews[1] = (TextView) findViewById(R.id.reserve_verify_text2);
        textViews[2] = (TextView) findViewById(R.id.reserve_verify_text3);
        textViews[3] = (TextView) findViewById(R.id.reserve_verify_text4);
        textViews[4] = (TextView) findViewById(R.id.reserve_verify_text5);
        textViews[5] = (TextView) findViewById(R.id.reserve_verify_text6);

        button = (Button) findViewById(R.id.reserve_verify_button);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        Bundle bundle =intent.getExtras();
        TimeSlot =bundle.getString("TimeSlot");
        BookingDay = bundle.getString("BookingDay");

        Branch =bundle.getString("Branch");
        Name = bundle.getString("Name");
        VehNof =bundle.getString("VehNof");
        Tel= bundle.getString("Tel");
        TeacherId  = bundle.getString("TeacherId");


        textViews[0].setText(Utils.getDateToString(BookingDay) + TimeSlot);
        textViews[1].setText(Branch);
        textViews[2].setText(Name);
        textViews[3].setText(Tel);
        textViews[4].setText(VehNof);
        textViews[5].setText(Utils.getSubject(sp.getInt("CurrentSubject", 1)));



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isNetworkAvailable(ReserveVerifyActivity.this)){
                    JSONObject json= new JSONObject(true);
                    json.put("SchoolId",sp.getInt("SchoolId",0));
                    json.put("TeacherId",TeacherId);//教练ID
                    json.put("StudentId",sp.getInt("Id",0));
                    json.put("Subject",sp.getInt("CurrentSubject", 0));
                    json.put("BookingDay",BookingDay);
                    json.put("TimeSlot",TimeSlot);
                    json.put("Amount", 0);
                    new MyThread(Constant.URL_DetermineBooking, handler, DES.encryptDES(json.toString())).start();

                }else {
                    MyToast.makeText(ReserveVerifyActivity.this,R.string.Toast_internet);
                }
            }
        });
    }
}
