package com.DLPort.myactivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.DLPort.OurActivity.PrivilegeActivity;
import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;
import com.DLPort.myview.MyDialog;
import com.DLPort.myview.TitleView;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Administrator on 2016/4/19.
 */
public class NewsPublishActivty extends BaseActivity {
    private static final String TAG = "NewsPublishActivty";
    private View[] views;
    private TextView[] textViews;
    private int index;
    private RadioGroup radio1, radio2;
    private Button publish_OK;
    private int dataType;
    private SharedPreferences sp;
    //    private EditText number;
    private CheckBox usePrivilegeText;
    private CheckBox use_yue;
    private int privilegeId;
    private float privilegeAmount;
    private boolean UseBalance = false;

    private int ChargeType, BussinessType, ContainerType;

    private Handler handler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    JSONObject jsonUser = null;
                    jsonUser = new JSONObject((String) msg.obj);

                    Log.d(TAG, "Login的响应" + jsonUser.toString());
                    int status = jsonUser.getInt("Status");
//                    Log.i(TAG, "状态" + status);
                    if (0 == status) {
                        setKong();

                        if (ChargeType == 0) {
                            JSONObject data = new JSONObject(jsonUser.getString("Data"));
                            float amount = Float.valueOf(data.getString("Amount"));
                            if (amount > 0) {
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString("mchntorderId", data.getString("CargoId"));
                                bundle.putFloat("amt", Float.valueOf(data.getString("Amount")));
                                bundle.putString("loginName", sp.getString("LoginName", null));
                                bundle.putString("tag", "NewsPublish");
                                bundle.putInt("Type", 1);

                                intent.putExtras(bundle);
                                intent.setClass(NewsPublishActivty.this, PayInfoWriteActivity.class);
                                startActivity(intent);
                            } else {
                                MyToast.makeText(NewsPublishActivty.this, "发布成功!");
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putInt("Type", 1);
                                bundle.putString("parentTag", "NewsPublish");
                                intent.putExtras(bundle);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setClass(NewsPublishActivty.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else if (ChargeType == 1) {
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putInt("Type", 1);
                            bundle.putString("parentTag", "NewsPublish");
                            intent.putExtras(bundle);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setClass(NewsPublishActivty.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    } else if (1 == status || -1 == status) {
                        Toast.makeText(NewsPublishActivty.this, jsonUser.getString("Message"), Toast.LENGTH_SHORT)
                                .show();
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");

                    e.printStackTrace();
                }
            } else if (msg.what == 2) {
                Log.i(TAG, "响应不正常");

                Toast.makeText(NewsPublishActivty.this, "服务器异常", Toast.LENGTH_SHORT)
                        .show();
            } else if (msg.what == 3) {
                // 编码异常
                Log.i(TAG, "编码异常");
            } else if (msg.what == 4) {
                // ClientProtocolException
                Log.i(TAG, "ClientProtocolException");
            } else if (msg.what == 5) {
                // IO异常
                Log.i(TAG, "IO异常");

                Toast.makeText(NewsPublishActivty.this, "服务器连接异常",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_publish);
        InitTitle();
        findById();
        init();
    }

    private void findById() {
        views = new View[13];
        textViews = new TextView[13];
        views[0] = findViewById(R.id.P_A_ContainerType);
        views[1] = findViewById(R.id.P_A_Amount);
        views[2] = findViewById(R.id.P_A_StartAddress);
        views[3] = findViewById(R.id.P_A_Destination);
        views[4] = findViewById(R.id.P_A_Price);
        views[5] = findViewById(R.id.P_A_GoodsSituation);
        views[6] = findViewById(R.id.P_A_InPortTime);
        views[7] = findViewById(R.id.P_A_LoadTime);
        views[8] = findViewById(R.id.P_A_ShipCompany);
        views[11] = findViewById(R.id.P_A_PresentNumber);
        views[10] = findViewById(R.id.P_A_PutBoxID);
        views[12] = findViewById(R.id.P_A_Kilometre);
        views[9] = findViewById(R.id.P_A_Cycle);

        textViews[0] = (TextView) findViewById(R.id.P_ContainerType);
        textViews[1] = (TextView) findViewById(R.id.P_Amount);
        textViews[2] = (TextView) findViewById(R.id.P_StartAddress);
        textViews[3] = (TextView) findViewById(R.id.P_Destination);
        textViews[4] = (TextView) findViewById(R.id.P_Price);
        textViews[5] = (TextView) findViewById(R.id.P_GoodsSituation);
        textViews[6] = (TextView) findViewById(R.id.P_InPortTime);
        textViews[7] = (TextView) findViewById(R.id.P_LoadTime);
        textViews[8] = (TextView) findViewById(R.id.P_ShipCompany);
        textViews[11] = (TextView) findViewById(R.id.P_PresentNumber);
        textViews[10] = (TextView) findViewById(R.id.P_PutBoxID);
        textViews[12] = (TextView) findViewById(R.id.P_Kilometre);

//        number = (EditText) findViewById(R.id.P_Cycle);

        radio1 = (RadioGroup) findViewById(R.id.publish_in_out);

        radio2 = (RadioGroup) findViewById(R.id.publish_platform_out);

        usePrivilegeText = (CheckBox) findViewById(R.id.use_privilege);
        use_yue = (CheckBox) findViewById(R.id.use_yue);
        publish_OK = (Button) findViewById(R.id.publish_Button);
        sp = getSharedPreferences("huo", Context.MODE_PRIVATE);
    }

    private void init() {
        if (dataType == 0) {
            views[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyDialog myDialog = new MyDialog(NewsPublishActivty.this, 3);
                    myDialog.setContent("箱型");
                    myDialog.setDialogCallback(new MyDialog.Dialogcallback() {
                        @Override
                        public void dialogdo(String string) {
                            String s[] = string.split("-");
                            textViews[0].setText(s[0]);
                            ContainerType = Integer.parseInt(s[1]);
                            Log.d(TAG, "枚举类" + ContainerType);
                        }
                    });
                    myDialog.show();
                    index = 0;
                }
            });
        } else {
            views[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyDialog myDialog = new MyDialog(NewsPublishActivty.this, 4);
                    myDialog.setContent("箱型");
                    myDialog.setDialogCallback(new MyDialog.Dialogcallback() {
                        @Override
                        public void dialogdo(String string) {
                            String s[] = string.split("-");
                            textViews[0].setText(s[0]);
                            ContainerType = Integer.parseInt(s[1]);
                            Log.d(TAG, "枚举类" + ContainerType);
                        }
                    });
                    myDialog.show();
                    index = 0;
                }
            });
        }
        views[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(NewsPublishActivty.this, 1);
                myDialog.setContent("数量");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("请输入数字");
                myDialog.show();
                index = 1;
            }
        });
        views[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(NewsPublishActivty.this, 1);
                myDialog.setContent("出发地");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.show();
                index = 2;
            }
        });
        views[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(NewsPublishActivty.this, 1);
                myDialog.setContent("目的地");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.show();
                index = 3;
            }
        });
        views[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(NewsPublishActivty.this, 1);
                myDialog.setContent("每箱价格");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("请输入数字");
                myDialog.show();
                index = 4;
            }
        });
        views[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(NewsPublishActivty.this, 1);
                myDialog.setContent("货物的基本信息");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.show();
                index = 5;
            }
        });
        views[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(NewsPublishActivty.this, 2);
                myDialog.setContent("入港时间");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.show();
                index = 6;
            }
        });
        views[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(NewsPublishActivty.this, 2);
                myDialog.setContent("装货时间");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.show();
                index = 7;
            }
        });
        views[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(NewsPublishActivty.this, 1);
                myDialog.setContent("船务公司");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.show();
                index = 8;
            }
        });

        views[10].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(NewsPublishActivty.this, 1);
                myDialog.setContent("放箱单号");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("选填");
                myDialog.show();
                index = 10;
            }
        });
        views[11].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(NewsPublishActivty.this, 1);
                myDialog.setContent("提单号");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("必填");
                myDialog.show();
                index = 11;
            }
        });
        views[12].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog myDialog = new MyDialog(NewsPublishActivty.this, 1);
                myDialog.setContent("公里数");
                myDialog.setDialogCallback(dialogcallback);
                myDialog.sethineText("选填（请输入数字）");
                myDialog.show();
                index = 12;
            }
        });
        usePrivilegeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usePrivilegeText.isChecked()) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("Type", 1);
                    intent.putExtras(bundle);
                    intent.setClass(NewsPublishActivty.this, PrivilegeActivity.class);
                    startActivityForResult(intent, 1);
                }


            }
        });
        use_yue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (use_yue.isChecked()) {
                    UseBalance = true;
                } else {
                    UseBalance = false;
                }

            }
        });


        publish_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalParams.isNetworkAvailable(NewsPublishActivty.this)) {
                    if (iskong()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(NewsPublishActivty.this);
                        builder.setMessage("确认发布吗？");
                        builder.setTitle("提示");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getpublishData();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();

                    }
                } else {
                    Toast.makeText(NewsPublishActivty.this, "亲,网络未连接",
                            Toast.LENGTH_SHORT).show();
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

    private void InitTitle() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        dataType = bundle.getInt("Type");
        if (dataType == 0) {
            TitleView titleView = (TitleView) findViewById(R.id.title_news_publish);
            titleView.setLeftViewVisible(true);
            titleView.setMiddleTextVisible(true);
            titleView.setWineText(R.string.O_pic32);
            titleView.setMiddleText(R.string.Huo);
            titleView.setLeftViewListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            TitleView titleView = (TitleView) findViewById(R.id.title_news_publish);
            titleView.setLeftViewVisible(true);
            titleView.setMiddleTextVisible(true);
            titleView.setWineText(R.string.O_pic5);
            titleView.setMiddleText(R.string.Huo);
            titleView.setLeftViewListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    private void getpublishData() {
        if (textViews[1].getText().toString().matches("-?[0-9]+.*[0-9]*")
                && textViews[4].getText().toString().matches("-?[0-9]+.*[0-9]*")) {
            JSONObject json = new JSONObject();
            switch (radio1.getCheckedRadioButtonId()) {
                case R.id.publish_in:
                    BussinessType = 1;
                    break;
                case R.id.publish_out:
                    BussinessType = 0;
                    break;
            }
            switch (radio2.getCheckedRadioButtonId()) {
                case R.id.publish_platform:
                    ChargeType = 0;
                    break;
                case R.id.publish_zixing:
                    ChargeType = 1;
                    break;
            }
            try {
                json.put("ContainerType", ContainerType);
                json.put("CargoOwnerId", sp.getString("UserId", ""));
                json.put("ChargeType", ChargeType);
                json.put("BussinessType", BussinessType);
                json.put("Amount", Double.valueOf(textViews[1].getText().toString()));
                json.put("LoadTime", textViews[7].getText().toString());
                json.put("ShipCompany", textViews[8].getText().toString());
                json.put("StartAddress", textViews[2].getText().toString());
                json.put("Destination", textViews[3].getText().toString());
//                Log.d(TAG,number.getText().toString());

                json.put("UseBalance", UseBalance);
                json.put("Price", Double.valueOf(textViews[4].getText().toString()));
                json.put("GoodsSituation", textViews[5].getText().toString());
                if (!textViews[12].getText().toString().equals("")) {
                    if (textViews[12].getText().toString().matches("-?[0-9]+.*[0-9]*")) {
//                        Log.d(TAG,textViews[12].getText().toString());
                        json.put("Kilometre", Double.valueOf(textViews[12].getText().toString()));
                    } else
                        MyToast.makeText(NewsPublishActivty.this, "输入的数据格式有误！请检查后发布");
                }
                if (dataType == 0) {
                    json.put("IsPooling", false);
                } else {
                    json.put("IsPooling", true);
                }
                json.put("PresentNumber", textViews[11].getText().toString());
                json.put("PutBoxID", textViews[10].getText().toString());
//                json.put("Cycle", Integer.valueOf(number.getText().toString()));
                json.put("PrivilegeId", privilegeId);
                json.put("InPortTime", textViews[6].getText().toString());
                new MyThread(Constant.URL_PostPublishCargo, handler, json, NewsPublishActivty.this).start();
//                Log.d(TAG,json.toString());
            } catch (JSONException e) {

                e.printStackTrace();
            }

        } else {
            Toast.makeText(NewsPublishActivty.this, "输入的数据格式有误！请检查后发布",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public boolean iskong() {
        if (TextUtils.isEmpty(textViews[0].getText())) {
            MyToast.makeText(this, "请输入箱型");
            return false;
        }
        if (TextUtils.isEmpty(textViews[1].getText())) {
            MyToast.makeText(this, "请输入箱型数量");
            return false;
        }
        if (TextUtils.isEmpty(textViews[2].getText())) {
            MyToast.makeText(this, "请输入出发地");
            return false;
        }
        if (TextUtils.isEmpty(textViews[3].getText())) {
            MyToast.makeText(this, "请输入目的地");
            return false;
        }
        if (TextUtils.isEmpty(textViews[4].getText())) {
            MyToast.makeText(this, "请输入每箱的价格");
            return false;
        }
        if (TextUtils.isEmpty(textViews[5].getText())) {
            MyToast.makeText(this, "请输入货物基本信息");
            return false;
        }

        if (TextUtils.isEmpty(textViews[8].getText())) {
            MyToast.makeText(this, "请输入船务公司");
            return false;
        }

        /*
        if (TextUtils.isEmpty(textViews[11].getText())) {
            MyToast.makeText(this, "请输入提单号");
            return false;
        }
        if (TextUtils.isEmpty(textViews[12].getText())) {
            MyToast.makeText(this, "请输入距离");
            return false;
        }
        */

        return true;
    }

    public void setKong() {
        for (int i = 0; i <= 8; i++) {
            textViews[i].setText("");
        }
        for (int i = 10; i <= 12; i++) {
            textViews[i].setText("");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            privilegeId = data.getIntExtra("privilegeId", 0);
            usePrivilegeText.setText("已优惠:" + data.getFloatExtra("amount", 0) + "元");
        }
    }
}
