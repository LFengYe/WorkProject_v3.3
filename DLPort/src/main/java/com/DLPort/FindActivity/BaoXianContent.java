package com.DLPort.FindActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.BaseActivity;
import com.DLPort.myactivity.HuoInquireActivity;
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
 * Created by Administrator on 2016/5/17.
 */
public class BaoXianContent extends BaseActivity{
    private static final String TAG="BaoXianContent";
    private View[] views;

    private TextView[] textViews;
    private String Id;
    private int index;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baoxian_content);
        initTitle();
        findById();
        init();
    }

    private void findById() {
        views = new View[4];
        textViews = new TextView[9];
        views[0] = findViewById(R.id.B_A_UserName);
        views[1] = findViewById(R.id.B_A_TelPhone);
        views[2] = findViewById(R.id.B_A_Amount);
        views[3] = findViewById(R.id.B_A_CarNo);

        textViews[0] = (TextView) findViewById(R.id.B_InsuranceName);
        textViews[1] = (TextView) findViewById(R.id.B_InsuranceType);
        textViews[2] = (TextView) findViewById(R.id.B_Description);
        textViews[3] = (TextView) findViewById(R.id.B_Money);
        textViews[4] = (TextView) findViewById(R.id.B_Discount);

        textViews[5] = (TextView) findViewById(R.id.B_UserName);
        textViews[6] = (TextView) findViewById(R.id.B_TelPhone);
        textViews[7] = (TextView) findViewById(R.id.B_Amount);
        textViews[8] = (TextView) findViewById(R.id.B_CarNo);
        button = (Button) findViewById(R.id.baoxian_Button);
    }
    private MyHandler handler =new MyHandler(this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                try {
                    JSONObject jsonUser = null;
                    jsonUser = new JSONObject((String) msg.obj);
                    Log.d(TAG, "Login的响应" + jsonUser.toString());
                    int status = jsonUser.getInt("Status");
                    Log.i(TAG, "状态" + status);
                    if (0 == status) {
                        MyToast.makeText(BaoXianContent.this, "投保信息发送成功");
                        finish();
                    } else {
                        MyToast.makeText(BaoXianContent.this, "投保信息发送失败");
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            }else {
                Log.d(TAG,msg.what+" 服务器异常");
                MyToast.makeText(BaoXianContent.this, msg.what+" 服务器异常");
            }
        }
    };

    private void init() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Id = bundle.getString("Id");
        textViews[0].setText(bundle.getString("InsuranceName"));
        textViews[1].setText(bundle.getString("InsuranceType"));
        textViews[2].setText(bundle.getString("Description"));
        textViews[3].setText(bundle.getString("Money")+"元");
        textViews[4].setText(bundle.getString("Discount"));

        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(BaoXianContent.this, 1);
                myDialog.setContent("姓名");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("例如：张三");
                myDialog.show();
                index = 5;
            }
        });
        views[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(BaoXianContent.this, 1);
                myDialog.setContent("电话号码");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("例如：18745612378");
                myDialog.show();
                index = 6;
            }
        });
        views[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(BaoXianContent.this, 1);
                myDialog.setContent("区域");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("例如：北京");
                myDialog.show();
                index = 7;
            }
        });
        views[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(BaoXianContent.this, 1);
                myDialog.setContent("车牌号");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("例如：辽123456");
                myDialog.show();
                index = 8;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalParams.isNetworkAvailable(BaoXianContent.this)) {
                    if(isKong()) {

                        JSONObject json = new JSONObject();
                        try {
                            json.put("InsuranceId", Id);
                            json.put("UserName", textViews[5].getText().toString());
                            json.put("TelPhone", textViews[6].getText().toString());
                            json.put("Amount", textViews[7].getText().toString());
                            json.put("CarNo", textViews[8].getText().toString());
                            Log.d(TAG, json.toString());
                            new MyThread(Constant.URL_PostAddInsurance, handler, json,BaoXianContent.this).start();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        MyToast.makeText(BaoXianContent.this, "有必填数据为空，请填写完整！！");
                    }
                }else {
                    Toast.makeText(BaoXianContent.this, "亲,请连接网络！！！", Toast.LENGTH_SHORT)
                            .show();
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
        TitleView titleView = (TitleView) findViewById(R.id.baoxian_content_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.find1);
        titleView.setMiddleText(R.string.baoxian);
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
        for(int i=5;i<=8;i++){
            if(textViews[i].getText().equals("")){
                kong =false;
                return kong;
            }
        }
        return kong;
    }


}
