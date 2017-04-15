package com.DLPort.myactivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myfragment.Fragment_sginhuo;
import com.DLPort.myfragment.Fragment_signcar;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Administrator on 2016/3/25.
 */
public class SignIn extends BaseActivity {
    private static final String TAG="SignIn";

    private int index;
    private TextView OK;
    private Fragment_sginhuo sginhuo;
    private Fragment_signcar signcar;
    private Fragment[] fragments;
    private Button[] buttons;
    private int currentTabIndex;
    private View back;
    private int dataType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_in);
        init();
      //  initTitle();

        click();
    }

//    private void initTitle() {
//
//
//        TitleView titleView = (TitleView) findViewById(R.id.sign_title);
//        titleView.setLeftTextVisible(true);
//        titleView.setMiddleViewVisible(true);
//        titleView.setRightTextVisible(true);
//        titleView.setLeftText("注册");
//        titleView.setRightText("完成");
//    }

    private void init() {
        Intent intent = getIntent();
        Bundle bundle =intent.getExtras();
        dataType=bundle.getInt("Type");

        OK = (TextView) findViewById(R.id.S_ok);
        back = findViewById(R.id.sign_back);
        sginhuo = new Fragment_sginhuo();
        signcar = new Fragment_signcar();
        buttons = new Button[2];
        buttons[0]= (Button) findViewById(R.id.car_sign_in);
        buttons[1]= (Button) findViewById(R.id.huo_sign_in);
        fragments = new Fragment[]{signcar,sginhuo};
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
                        MyToast.makeText(SignIn.this, "注册成功");
                        finish();
                    } else {
                        MyToast.makeText(SignIn.this, "注册失败");

                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            }else {
                Log.d(TAG,msg.what+" 服务器异常");
                MyToast.makeText(SignIn.this, msg.what+" 服务器异常");
            }
        }
    };
    private void click() {

        buttons[dataType].setTextColor(0xFFFF5252);
        buttons[dataType].setSelected(true);
        if(dataType==0){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.S_contect, signcar)
                    .add(R.id.S_contect, sginhuo)
                    .hide(sginhuo).show(signcar).commit();
        }else if(dataType==1){

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.S_contect, signcar)
                    .add(R.id.S_contect, sginhuo)
                    .hide(signcar).show(sginhuo).commit();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GlobalParams.isNetworkAvailable(SignIn.this)) {
                    if (dataType == 0) {
                        if(signcar.isKong()){
                            JSONObject jsonObject = new JSONObject();
                            jsonObject = signcar.getData();
                            Log.d(TAG, jsonObject.toString());
                            new MyThread(Constant.URL_PostVehicleRegister, handler, jsonObject,SignIn.this).start();
                        }else {
                            MyToast.makeText(SignIn.this,"有必填数据为空，请填写完整！！");
                        }

                    }else if(dataType == 1){
                        if(sginhuo.isKong()) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject = sginhuo.getData();
                            Log.d(TAG, jsonObject.toString());
                            new MyThread(Constant.URL_PostCargoRegister, handler, jsonObject,SignIn.this).start();
                        }else {
                            MyToast.makeText(SignIn.this, "有必填数据为空，请填写完整！！");
                        }
                    }
                }else{

                    Toast.makeText(SignIn.this, "亲,网络未连接",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 0;
                tabchange();
            }
        });
        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 1;
                tabchange();
            }
        });
    }

    private void tabchange(){
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.main_content, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        buttons[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        buttons[index].setSelected(true);
        buttons[currentTabIndex].setTextColor(0xFFFFFFFF);
        buttons[index].setTextColor(0xFFFF5252);
        currentTabIndex = index;
    }



}
