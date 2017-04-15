package com.DLPort.myactivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;
import com.DLPort.myview.TitleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/4/17.
 */
public class TuiGuangActivity extends BaseActivity {

    private static final String TAG="TuiGuangActivity";
    private String data;
    private TextView[] textViews;
    private boolean OK=false;
    private String companyone;
    private boolean chick=false;
    private String[] money,name,Address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tui_window);
        InitTitle();
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
                        data = jsonUser.getString("Data");
                        JSONArray jsonArray = new JSONArray(data);
                        name = new String[jsonArray.length()];
                        Address = new String[jsonArray.length()];
                        money = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.getJSONObject(i);
                            name[i]= json.getString("StorageYardNmae");
                            Address[i] = json .getString("Address");
                            money[i] =json.getString("Price");
                            OK=true;
                        }
                    } else {
                        MyToast.makeText(TuiGuangActivity.this, "加载失败");
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            }else {
                Log.d(TAG,msg.what+" 服务器异常");
                MyToast.makeText(TuiGuangActivity.this, msg.what + " 服务器异常");
            }
        }
    };



    private void init() {
        OK=false;
        View view= findViewById(R.id.dui_name);
        View view1 =findViewById(R.id.dui_luo);
        View view2 = findViewById(R.id.dui_A_price);
        View view3 = findViewById(R.id.dui_tixiang);
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TuiGuangActivity.this, LuoxiangActivity.class);
                intent.putExtras(getIntent().getExtras());
                startActivity(intent);
            }
        });
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalParams.isNetworkAvailable(TuiGuangActivity.this)) {
                    if (OK) {
                        mydedialog("场地装箱价格查询", money, 2);
                    } else {
                        MyToast.makeText(TuiGuangActivity.this, "加载失败");
                    }
                } else {
                    MyToast.makeText(TuiGuangActivity.this, "亲,网络未连接");
                }


            }
        });

        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TuiGuangActivity.this, TiXiangActivity.class);
                intent.putExtras(getIntent().getExtras());
                startActivity(intent);

            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalParams.isNetworkAvailable(TuiGuangActivity.this)) {
                    if (OK) {
                        mydedialog("选择堆场", name, 0);
                    } else {
                        MyToast.makeText(TuiGuangActivity.this, "加载失败");
                    }
                } else {
                    MyToast.makeText(TuiGuangActivity.this, "亲,网络未连接");
                }
            }
        });
        textViews = new TextView[4];
        textViews[0] = (TextView) findViewById(R.id.dui_StorageYardNmae);
        textViews[1] = (TextView) findViewById(R.id.dui_address);
        textViews[2] = (TextView) findViewById(R.id.dui_Price);



        JSONObject json = new JSONObject();
        if(GlobalParams.isNetworkAvailable(TuiGuangActivity.this)) {

            new MyThread(Constant.URL_PostQueryStorageYard, handler, json,TuiGuangActivity.this).start();
        }else{

            MyToast.makeText(TuiGuangActivity.this, "亲,网络未连接");

        }
    }

    private void InitTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.tui_window_title);
        titleView.setBackImageVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setMiddleText(R.string.O_pic2);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       switch (requestCode){
           case 1:
               if(resultCode == RESULT_OK){
                   Bundle bundle = data.getExtras();
                   textViews[0].setText(bundle.getString("StorageYardNmae"));
                   textViews[1].setText(bundle.getString("Address"));
                   textViews[2].setText(bundle.getString("Price"));
               }

       }
    }

    public void mydedialog(String string,final String[] strings, final int i){


        AlertDialog dialog= new  AlertDialog.Builder(TuiGuangActivity.this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(string)
                .setSingleChoiceItems(strings, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG,"i============="+which);
                                companyone=strings[which];
                                chick=true;
                                if (i == 0) {
                                    textViews[1].setText(Address[which]);
                                }
                            }
                        }
                )
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (chick) {
                                    textViews[i].setText(companyone);
                                } else {
                                    textViews[i].setText(strings[0]);
                                    if (i == 0) {
                                        textViews[1].setText(Address[0]);
                                    }
                                }
                                chick=false;
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("取消", null)
                .show();
        int no = strings.length;

        if(no>5){
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.height = 1000;
            dialog.getWindow().setAttributes(params);
        }

    }

}
