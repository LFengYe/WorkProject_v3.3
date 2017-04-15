package cn.guugoo.jiapeistudent.MinorActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import cn.guugoo.jiapeistudent.Adapter.CoachAdapter;
import cn.guugoo.jiapeistudent.App.Constant;
import cn.guugoo.jiapeistudent.Data.Coach;
import cn.guugoo.jiapeistudent.Data.ReturnData;
import cn.guugoo.jiapeistudent.MainActivity.BaseActivity;
import cn.guugoo.jiapeistudent.R;
import cn.guugoo.jiapeistudent.Tools.DES;
import cn.guugoo.jiapeistudent.Tools.MyThread;
import cn.guugoo.jiapeistudent.Tools.MyToast;
import cn.guugoo.jiapeistudent.Tools.Utils;
import cn.guugoo.jiapeistudent.Views.TitleView;

import java.util.ArrayList;
import java.util.List;

public class SelectCoachActivity extends BaseActivity {
    private static final String TAG = "SelectCoachActivity";
    private ListView listView;
    private SharedPreferences sp;
    private List<Coach> listData ;
    private CoachAdapter adapter;
    private String TimeSlot;
    private String BookingDay;
    private float price;
    private ImageView search;
    private EditText search_text;
    private String year;
    private int BranchId;
    @Override
    protected void processingData(ReturnData data) {
        Log.d(TAG, "processingData: "+data.getData());
        List<Coach> coaches = JSONObject.parseArray(data.getData(),Coach.class);
        listData.addAll(coaches);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_select_coach);
    }

    @Override
    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.select_coach_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setMiddleText(R.string.select_coach);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void findView() {
        Intent intent = getIntent();
        Bundle bundle =intent.getExtras();
        TimeSlot =bundle.getString("TimeSlot");
        BookingDay = bundle.getString("BookingDay");
        BranchId = bundle.getInt("BranchId");
        price = bundle.getFloat("Price");
        Log.d(TAG, "createContentView: "+price);
        year = bundle.getString("year");
        listView  = (ListView) findViewById(R.id.select_coach_list);
        listData = new ArrayList<>();
        adapter = new CoachAdapter(R.layout.adapte_coach,SelectCoachActivity.this,listData);
        listView.setAdapter(adapter);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        search = (ImageView) findViewById(R.id.search_image);
        search_text = (EditText) findViewById(R.id.search_text);
        search_text.setHint("请输入教练名称");
    }

    private void select(){
        listData.clear();
        JSONObject json= new JSONObject(true);
        json.put("SchoolId",sp.getInt("SchoolId",0));
        json.put("BranchId",0);
        json.put("Subject",sp.getInt("CurrentSubject",0));
        json.put("BookingDay",BookingDay);
        json.put("StudentId",sp.getInt("Id",0));
        json.put("TimeSlot",TimeSlot);
        json.put("TeacherName",search_text.getText());
        Log.d(TAG, "getSiteData: "+json.toString());
        new MyThread(Constant.URL_TeacherList, handler, DES.encryptDES(json.toString())).start();

    }

    @Override
    protected void init() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Coach coach= listData.get(position);
                Intent intent = new Intent(SelectCoachActivity.this,ReserveVerifyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putFloat("Price",price);
                bundle.putString("BookingDay",BookingDay);
                bundle.putString("TimeSlot",TimeSlot);
                bundle.putString("Branch",coach.getBranch());
                bundle.putString("Name",coach.getName());
                bundle.putInt("TeacherId",coach.getTId());
                bundle.putString("Tel",coach.getTel());
                bundle.putString("VehNof",coach.getVehNof());
                bundle.putString("year",year);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                select();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select();
            }
        });
        if(Utils.isNetworkAvailable(SelectCoachActivity.this)){
            JSONObject json= new JSONObject(true);
            json.put("SchoolId",sp.getInt("SchoolId",0));
            json.put("BranchId",BranchId);
            json.put("Subject",sp.getInt("CurrentSubject",0));
            json.put("BookingDay",BookingDay);
            json.put("TimeSlot",TimeSlot);
            json.put("StudentId",sp.getInt("Id",0));
            json.put("TeacherName","");
            Log.d(TAG, "getSiteData: "+json.toString());
            new MyThread(Constant.URL_TeacherList, handler, DES.encryptDES(json.toString())).start();
        }else {
            MyToast.makeText(SelectCoachActivity.this,R.string.Toast_internet);
        }
    }
}
