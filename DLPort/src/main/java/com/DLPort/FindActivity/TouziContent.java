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
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;
import com.DLPort.myview.MyDialog;
import com.DLPort.myview.TitleView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/19.
 */
public class TouziContent extends BaseActivity{
    private static final String TAG="TouziContent";
    private View[] views;

    private TextView[] textViews;
    private int Id;
    private int index;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.licai_content);
        initTitle();
        findById();
        init();
    }
    private Handler handler =new MyHandler(this) {
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
                        MyToast.makeText(TouziContent.this, jsonUser.getString("Message"));
                        finish();
                    } else {
                        MyToast.makeText(TouziContent.this, jsonUser.getString("Message"));
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            }else {
                Log.d(TAG,msg.what+" 服务器异常");
                MyToast.makeText(TouziContent.this, msg.what+" 服务器异常");
            }
        }
    };

    private void init() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Id = bundle.getInt("Id");
        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(TouziContent.this, 1);
                myDialog.setContent("姓名");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("例如：张三");
                myDialog.show();
                index = 0;
            }
        });
        views[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(TouziContent.this, 1);
                myDialog.setContent("电话");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("例如：18745612378");
                myDialog.show();
                index = 1;
            }
        });
        views[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(TouziContent.this, 1);
                myDialog.setContent("理财金额");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("例如：12");
                myDialog.show();
                index = 2;
            }
        });
        views[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(TouziContent.this, 1);
                myDialog.setContent("推荐人");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("例如：张三");
                myDialog.show();
                index = 3;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalParams.isNetworkAvailable(TouziContent.this)) {
                    if (isKong()) {

                        JSONObject json = new JSONObject();
                        try {
                            json.put("Id", Id);
                            json.put("UserName", textViews[0].getText().toString());
                            json.put("Tel", textViews[1].getText().toString());
                            json.put("Amount", textViews[2].getText().toString());
                            json.put("Inviter", textViews[3].getText().toString());
                            Log.d(TAG, json.toString());
                            new MyThread(Constant.URL_PostFinance, handler, json,TouziContent.this).start();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        MyToast.makeText(TouziContent.this, "有必填数据为空，请填写完整！！");
                    }
                } else {
                    MyToast.makeText(TouziContent.this, "亲,请连接网络！！！");
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


    private void findById() {
        views = new View[4];
        textViews = new TextView[4];
        views[0] = findViewById(R.id.L_A_UserName);
        views[1] = findViewById(R.id.L_A_Tel);
        views[2] = findViewById(R.id.L_A_Amount);
        views[3] = findViewById(R.id.L_A_Inviter);

        textViews[0] = (TextView) findViewById(R.id.L_UserName);
        textViews[1] = (TextView) findViewById(R.id.L_Tel);
        textViews[2] = (TextView) findViewById(R.id.L_Amount);
        textViews[3] = (TextView) findViewById(R.id.L_Inviter);

        button = (Button) findViewById(R.id.licai_Button);
    }



    private void initTitle() {

            TitleView titleView = (TitleView) findViewById(R.id.licaicontent_title);
            titleView.setLeftViewVisible(true);
            titleView.setMiddleTextVisible(true);
            titleView.setWineText(R.string.find5);
            titleView.setMiddleText(R.string.licai);
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
        for(int i=0;i<=3;i++){
            if(textViews[i].getText().equals("")){
                kong =false;
                return kong;
            }
        }
        return kong;
    }
}
