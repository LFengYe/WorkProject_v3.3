package com.DLPort.FindActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.BaseActivity;
import com.DLPort.myactivity.SignIn;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;
import com.DLPort.myview.MyDialog;
import com.DLPort.myview.TitleView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/14.
 */
public class jiuyuanActivity extends BaseActivity {

    private static final String TAG="jiuyuanActivity";
    private View[] views;
    private TextView[] textViews;
    private Button jiuyuan;
    private int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qichejiuyuan);
        initTitle();
        findById();
        init();
    }
    private Handler handler =new MyHandler(this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                try {
                    JSONObject jsonUser = null;
                    jsonUser = new JSONObject((String) msg.obj);
                    Log.d(TAG,jsonUser.toString());
                    int status = jsonUser.getInt("Status");

                    if (0 == status) {
                        MyToast.makeText(jiuyuanActivity.this, jsonUser.getString("Message"));

                    } else {
                        MyToast.makeText(jiuyuanActivity.this, jsonUser.getString("Message"));
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }else {

                MyToast.makeText(jiuyuanActivity.this, msg.what + " 服务器异常");
            }
        }
    };

    private void findById() {
        views= new View[2];
        textViews = new TextView[2];
        views[0] =findViewById(R.id.jiuyuan_A_CarNo);
        views[1] = findViewById(R.id.jiuyuan_A_DriverTel);
        textViews[0] = (TextView) findViewById(R.id.jiuyuan_CarNo);
        textViews[1] = (TextView) findViewById(R.id.jiuyuan_DriverTel);
        jiuyuan = (Button) findViewById(R.id.jiuyuan_Button);

    }

    private void init() {
        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(jiuyuanActivity.this,1);
                myDialog.setContent("车牌号");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("例如：辽12345");
                myDialog.show();
                index = 0;
            }
        });
        views[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(jiuyuanActivity.this,1);
                myDialog.setContent("手机号ַ");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("例如 18745678912");
                myDialog.show();
                index = 1;
            }
        });
        jiuyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (GlobalParams.isNetworkAvailable(jiuyuanActivity.this)) {

                    if (isKong()) {
                        JSONObject json = new JSONObject();
                        try {
                            json.put("CarNo", textViews[0].getText());
                            json.put("DriverTel", textViews[1].getText());
                            new MyThread(Constant.URL_PostAutomotiveRescue, handler, json,jiuyuanActivity.this).start();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        MyToast.makeText(jiuyuanActivity.this,"请把数据补充完整！！");
                    }
                }
            }
        });


    }

    MyDialog.Dialogcallback dialogcallback = new MyDialog.Dialogcallback() {
        @Override
        public void dialogdo(String string) {
            textViews[index].setText(string);
        }
    };


    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.qichejiuyuan_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.find);
        titleView.setMiddleText(R.string.find2);
        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public boolean isKong(){
        boolean kong = true;
        for(int i=0;i<=1;i++){
            if(textViews[i].getText().equals("")){
                kong =false;
                return kong;
            }
        }
        return kong;
    }



}
