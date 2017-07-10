package com.guugoo.jiapeistudent.MainActivity;

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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guugoo.jiapeistudent.App.Constant;
import com.guugoo.jiapeistudent.Data.OldData;
import com.guugoo.jiapeistudent.Data.Package;
import com.guugoo.jiapeistudent.Data.ReturnData;
import com.guugoo.jiapeistudent.Data.School;
import com.guugoo.jiapeistudent.MinorActivity.AddMyNewsActivity;
import com.guugoo.jiapeistudent.MinorActivity.AllApplyTrainActivity;
import com.guugoo.jiapeistudent.MinorActivity.DriversTypeActivity;
import com.guugoo.jiapeistudent.MinorActivity.PayActivity;
import com.guugoo.jiapeistudent.R;
import com.guugoo.jiapeistudent.Tools.DES;
import com.guugoo.jiapeistudent.Tools.MyHandler;
import com.guugoo.jiapeistudent.Tools.MyThread;
import com.guugoo.jiapeistudent.Tools.MyToast;
import com.guugoo.jiapeistudent.Tools.Utils;
import com.guugoo.jiapeistudent.Views.TitleView;

import java.util.List;


/**
 * Created by Administrator on 2016/8/4.
 */
public class ApplyTrainActivity extends BaseActivity{
    private static final String TAG ="ApplyTrainActivity";
    private View[] views;
    private SharedPreferences sp;
    private EditText[] editTexts;
    private TextView textView;
    private RadioGroup rg;
    private View driversType;
    private CheckBox affirm;
    private Button button;
    private String companyone;
    private int DrivingTypeId;
    private boolean chick = false;
    private ImageView imageView;

    private String[] Types; //报考类型
    private int[] number;
    private View hint;
    private TextView[] select_package;
    private int sex=0; //性别
    private String PackageId =""; //套餐编号
    private Package pack=new Package(); //返回的套餐


    private Handler handler1 = new MyHandler(this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                Log.d(TAG, "handleMessage:111 "+msg.obj);
                ReturnData data=JSONObject.parseObject((String) msg.obj,ReturnData.class);
                if(data.getStatus()==0){
                    JSONArray jsonArray = JSONArray.parseArray(data.getData());
                    Types = new String[jsonArray.size()];
                    number = new int[jsonArray.size()];
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        Types[i] = json.getString("Typename");
                        number[i] =json.getInteger("TypeNo");
                    }
                }else {
                    MyToast.makeText(ApplyTrainActivity.this,data.getMessage());
                }
            }
        }
    };

    private Handler handler2 = new MyHandler(this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                ReturnData data=JSONObject.parseObject((String) msg.obj,ReturnData.class);
                MyToast.makeText(ApplyTrainActivity.this,data.getMessage());
            }
        }
    };

    @Override
    protected void processingData(ReturnData data) {
        OldData oldData= JSONObject.parseObject(data.getData(),OldData.class);
        editTexts[0].setText(oldData.getName());
        editTexts[1].setText(oldData.getTel());
        editTexts[2].setText(oldData.getCardNo());
        editTexts[3].setText(oldData.getAddress());
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_apply_train);
    }

    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.activity_apply_train_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setMiddleText(R.string.home_text1);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void findView() {
        views  = new View[4];
        views[0] = findViewById(R.id.apply_text1);
        views[1] = findViewById(R.id.apply_text2);
        views[2] = findViewById(R.id.apply_text3);
        views[3] = findViewById(R.id.apply_text4);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        editTexts = new EditText[5];
        editTexts[0] = (EditText) findViewById(R.id.my_editText1);
        editTexts[1] = (EditText) findViewById(R.id.my_editText2);
        editTexts[2] = (EditText) findViewById(R.id.my_editText3);
        editTexts[3] = (EditText) findViewById(R.id.my_editText4);
        editTexts[4] = (EditText) findViewById(R.id.my_editText5);
        rg = (RadioGroup) findViewById(R.id.my_add_radioGroup);
        driversType = findViewById(R.id.my_add_line6);
        affirm = (CheckBox) findViewById(R.id.my_add_affirm);
        button = (Button) findViewById(R.id.add_button);
        textView = (TextView) findViewById(R.id.my_add_type);
        imageView = (ImageView) findViewById(R.id.apply_image);
        select_package = new TextView[3];
        select_package[0] = (TextView) findViewById(R.id.apply_package_name);
        select_package[1] = (TextView) findViewById(R.id.apply_package_place);
        select_package[2] = (TextView) findViewById(R.id.apply_package_content);
        hint = findViewById(R.id.Sl_apply_hint);
    }

    /**
     * type 1:报名须知，2：学车流程，3：体检须知
     */
    protected void init() {
        getOlddata();
        getdriversType();

        views[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplyTrainActivity.this, AllApplyTrainActivity.class);
                intent.putExtra("Type",1);
                startActivity(intent);
            }
        });
        views[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplyTrainActivity.this, AllApplyTrainActivity.class);
                intent.putExtra("Type",2);
                startActivity(intent);
            }
        });
        views[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplyTrainActivity.this, AllApplyTrainActivity.class);
                intent.putExtra("Type",3);
                startActivity(intent);
            }
        });
        views[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplyTrainActivity.this, AllApplyTrainActivity.class);
                intent.putExtra("Type",5);
                startActivity(intent);
            }
        });
        driversType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydedialog("报考类型",Types);
            }
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                sex =checkedId==R.id.men ? 0:1;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isNetworkAvailable(ApplyTrainActivity.this)) {
                    if(isEmpty()){
                        JSONObject json = new JSONObject(true);
                        json.put("Name",editTexts[0].getText().toString().trim());
                        json.put("PackageId",pack.getId());
                        json.put("Id",sp.getInt("Id",0));
                        json.put("CardNo",editTexts[2].getText().toString().trim());
                        json.put("Sex",sex);
                        json.put("RecommendId",editTexts[4].getText().toString().trim());
                        json.put("SchoolId", sp.getInt("SchoolId",0));
//                        json.put("PayType",0);
                        json.put("Address",editTexts[2].getText().toString().trim());
                        new MyThread(Constant.URL_SignUp, handler2, DES.encryptDES(json.toString())).start();
                    }
                }else {
                    MyToast.makeText(ApplyTrainActivity.this,R.string.Toast_internet);
                }
                /*
                if(isEmpty()) {
                    Intent intent = new Intent(ApplyTrainActivity.this, PayActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Name", editTexts[0].getText().toString().trim());
                    bundle.putString("Nickname",editTexts[1].getText().toString().trim());
                    bundle.putString("CardNo", editTexts[2].getText().toString().trim());
                    bundle.putInt("Sex", sex);
                    bundle.putString("RecommendId", editTexts[4].getText().toString().trim());
                    bundle.putString("Address", editTexts[3].getText().toString().trim());
                    bundle.putParcelable("package", pack);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                */
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplyTrainActivity.this, AddMyNewsActivity.class);
//                intent.putExtra("Type",6);
                startActivity(intent);
            }
        });



    }

    private void getOlddata(){
        if(Utils.isNetworkAvailable(ApplyTrainActivity.this)) {
            JSONObject json = new JSONObject(true);
            json.put("StudentId", sp.getInt("Id",0));
            new MyThread(Constant.URL_GetSignMessage, handler, DES.encryptDES(json.toString())).start();
        }else {
            MyToast.makeText(ApplyTrainActivity.this,R.string.Toast_internet);
        }
    }


    public void getdriversType() {
        if(Utils.isNetworkAvailable(ApplyTrainActivity.this)) {
            JSONObject json = new JSONObject(true);
            json.put("SchoolId", sp.getInt("SchoolId",0));
            new MyThread(Constant.URL_DriversType, handler1, DES.encryptDES(json.toString())).start();
        }else {
            MyToast.makeText(ApplyTrainActivity.this,R.string.Toast_internet);
        }
    }


    private boolean isEmpty(){
        if(TextUtils.isEmpty(editTexts[0].getText())){
            MyToast.makeText(ApplyTrainActivity.this,R.string.add_toast1);
            return false;
        }
        if(TextUtils.isEmpty(editTexts[1].getText())){
            MyToast.makeText(ApplyTrainActivity.this,R.string.add_toast2);
            return false;
        }
        if(editTexts[1].getText().toString().trim().length()!=11){
            MyToast.makeText(ApplyTrainActivity.this,R.string.Toast_phone1);
            return false;
        }
        if(TextUtils.isEmpty(editTexts[2].getText())){
            MyToast.makeText(ApplyTrainActivity.this,R.string.add_toast3);
            return false;
        }
        if(editTexts[2].getText().toString().trim().length()!=18){
            MyToast.makeText(ApplyTrainActivity.this,R.string.add_toast31);
            return false;
        }
        if(TextUtils.isEmpty(editTexts[3].getText())){
            MyToast.makeText(ApplyTrainActivity.this,R.string.add_toast4);
            return false;
        }
        if(TextUtils.isEmpty(textView.getText())){
            MyToast.makeText(ApplyTrainActivity.this,R.string.add_toast5);
            return false;
        }
        if(!affirm.isChecked()){
            MyToast.makeText(ApplyTrainActivity.this,R.string.add_toast6);
            return false;
        }

        if(TextUtils.isEmpty(PackageId)){
            MyToast.makeText(ApplyTrainActivity.this,R.string.add_toast7);
            return false;
        }
        return true;
    }



    public void mydedialog(String string, final String[] strings) {

        AlertDialog dialog = new AlertDialog.Builder(ApplyTrainActivity.this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(string)
                .setSingleChoiceItems(strings, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "i=============" + which);
                                companyone = strings[which];
                                DrivingTypeId =number[which];
                                chick = true;

                            }
                        }
                )
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (chick) {
                                    textView.setText(companyone);
                                    Intent intent = new Intent(ApplyTrainActivity.this,DriversTypeActivity.class);
                                    intent.putExtra("TypeName",companyone);
                                    intent.putExtra("DrivingTypeId",DrivingTypeId);
                                    startActivityForResult(intent,1);
                                } else {
                                    // 选择列表没有进行选择, 点击确定按钮默认为第一条数据
                                    textView.setText(strings[0]);
                                    DrivingTypeId= number[0];
                                    companyone =strings[0];
                                    Intent intent = new Intent(ApplyTrainActivity.this,DriversTypeActivity.class);
                                    intent.putExtra("TypeName",companyone);
                                    intent.putExtra("DrivingTypeId",DrivingTypeId);
                                    startActivityForResult(intent,1);
                                }
                                chick = false;
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("取消", null)
                .show();
        int no = strings.length;

        if (no > 5) {
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.height = 1000;
            dialog.getWindow().setAttributes(params);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    pack= data.getParcelableExtra("package");
                    hint.setVisibility(View.VISIBLE);
                    select_package[0].setText(pack.getPackageName());
                    select_package[1].setText(String.format(this.getString(R.string.pay_money),
                            Float.valueOf(pack.getPrice())));

                    select_package[2].setText(pack.getIntroduction());
                    PackageId = pack.getId();
                }
        }
    }
}
