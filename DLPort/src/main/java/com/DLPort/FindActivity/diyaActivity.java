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
public class diyaActivity extends BaseActivity {

    private static final String TAG="diyaActivity";
    private View[] views;
    private TextView[] textViews;
    private Button button;
    private int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diyahuo);
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
                    Log.d(TAG, jsonUser.toString());
                    int status = jsonUser.getInt("Status");

                    if (0 == status) {
                        MyToast.makeText(diyaActivity.this, jsonUser.getString("Message"));

                    } else {
                        MyToast.makeText(diyaActivity.this, jsonUser.getString("Message"));
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }else {

                MyToast.makeText(diyaActivity.this, msg.what + " 服务器异常");
            }
        }
    };

    private void init() {
        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(diyaActivity.this,1);
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
                MyDialog myDialog = new MyDialog(diyaActivity.this,1);
                myDialog.setContent("联系电话ַ");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("例如 18745678912");
                myDialog.show();
                index = 1;
            }
        });
        views[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(diyaActivity.this,1);
                myDialog.setContent("抵押人姓名ַ");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("例如 张三");
                myDialog.show();
                index = 2;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (GlobalParams.isNetworkAvailable(diyaActivity.this)) {

                    if (isKong()) {
                        JSONObject json = new JSONObject();
                        try {
                            json.put("Tel", textViews[1].getText());
                            json.put("CarNo", textViews[0].getText());
                            json.put("UserName",textViews[2].getText());
                            new MyThread(Constant.URL_PostMortgage, handler, json,diyaActivity.this).start();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        MyToast.makeText(diyaActivity.this, "请把数据补充完整！！");
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



    private void findById() {
        views= new View[3];
        textViews = new TextView[3];
        views[0] =findViewById(R.id.diya_A_CarNo);
        views[1] = findViewById(R.id.diya_A_Tel);
        views[2] = findViewById(R.id.diya_A_UserName);

        textViews[0] = (TextView) findViewById(R.id.diya_CarNo);
        textViews[1] = (TextView) findViewById(R.id.diya_Tel);
        textViews[2] = (TextView) findViewById(R.id.diya_UserName);
        button = (Button) findViewById(R.id.diya_Button);

    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.diya_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.find);
        titleView.setMiddleText(R.string.find4);
        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean isKong(){
        boolean kong = true;
        for(int i=0;i<=2;i++){
            if(textViews[i].getText().equals("")){
                kong =false;
                return kong;
            }
        }
        return kong;
    }
}
