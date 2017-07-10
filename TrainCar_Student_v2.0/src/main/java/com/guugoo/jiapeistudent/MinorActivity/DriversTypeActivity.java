package com.guugoo.jiapeistudent.MinorActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.guugoo.jiapeistudent.Adapter.PackageAdapter;
import com.guugoo.jiapeistudent.App.Constant;
import com.guugoo.jiapeistudent.Data.Package;
import com.guugoo.jiapeistudent.Data.ReturnData;
import com.guugoo.jiapeistudent.MainActivity.BaseActivity;
import com.guugoo.jiapeistudent.R;
import com.guugoo.jiapeistudent.Tools.DES;
import com.guugoo.jiapeistudent.Tools.MyHandler;
import com.guugoo.jiapeistudent.Tools.MyThread;
import com.guugoo.jiapeistudent.Tools.MyToast;
import com.guugoo.jiapeistudent.Tools.Utils;
import com.guugoo.jiapeistudent.Views.TitleView;

import java.util.ArrayList;
import java.util.List;

public class DriversTypeActivity extends BaseActivity {
    private static final String TAG ="DriversTypeActivity";
    private ListView listView;
    private List<Package> listData;
    private PackageAdapter adapter;
    private String TypeName;
    private int DrivingTypeId;
    private TextView textView;
    private Button button;
    private SharedPreferences sp;

    private Handler handler = new MyHandler(this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                Log.d(TAG, "handleMessage: "+msg.obj);
                ReturnData data= JSONObject.parseObject((String) msg.obj,ReturnData.class);
                if(data.getStatus()==0){
                    List<Package> datas =JSONObject.parseArray(data.getData(),Package.class);
                    listData.addAll(datas);
                    adapter.notifyDataSetChanged();
                }
                MyToast.makeText(DriversTypeActivity.this,data.getMessage());
            }
        }
    };
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_drivers_type);
    }

    @Override
    protected void initTitle() {
        DrivingTypeId = getIntent().getIntExtra("DrivingTypeId",0);
        TypeName = getIntent().getStringExtra("TypeName");
        TitleView titleView = (TitleView) findViewById(R.id.drivers_type_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setMiddleText(TypeName);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void findView() {


        listView = (ListView) findViewById(R.id.apply_list);
        listData = new ArrayList<>();
        adapter = new PackageAdapter(this,R.layout.adapter_package,listData);
        listView.setAdapter(adapter);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        textView = (TextView) findViewById(R.id.drivers_type_text);
        button = (Button) findViewById(R.id.type_button);
    }

    @Override
    protected void init() {

        textView.setText(String.format(this.getString(R.string.drivers_type_text),TypeName));
        getPackage();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listData.isEmpty()){
                    finish();
                }else {
                    Intent intent = new Intent();
                    Package data = adapter.IsClichedPosition();
                    intent.putExtra("package",data);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }


    private void getPackage() {
        if(Utils.isNetworkAvailable(DriversTypeActivity.this)){

            JSONObject json= new JSONObject(true);
            json.put("SchoolId",sp.getInt("SchoolId",0));
            json.put("DrivingTypeId",DrivingTypeId);
            new MyThread(Constant.URL_Package, handler, DES.encryptDES(json.toString())).start();
        }else {
            MyToast.makeText(DriversTypeActivity.this,R.string.Toast_internet);
        }
    }
}
