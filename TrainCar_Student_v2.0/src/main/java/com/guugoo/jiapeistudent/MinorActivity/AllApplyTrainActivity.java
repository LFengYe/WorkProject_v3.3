package com.guugoo.jiapeistudent.MinorActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.guugoo.jiapeistudent.App.Constant;
import com.guugoo.jiapeistudent.Data.ReturnData;
import com.guugoo.jiapeistudent.MainActivity.BaseActivity;
import com.guugoo.jiapeistudent.R;
import com.guugoo.jiapeistudent.Tools.DES;
import com.guugoo.jiapeistudent.Tools.MyThread;
import com.guugoo.jiapeistudent.Tools.MyToast;
import com.guugoo.jiapeistudent.Tools.Utils;
import com.guugoo.jiapeistudent.Views.TitleView;

/**
 * Created by Administrator on 2016/8/4.
 */
public class AllApplyTrainActivity extends BaseActivity {
    private static final String TAG = "AllApplyTrainActivity";
    private Integer Type;
    private TextView textView;
    private String url;
    private SharedPreferences sp;

    @Override
    protected void processingData(ReturnData data) {


//        Log.d(TAG, "processingData: "+DES.decryptDES(data.getData()));
////        log(data.getData());
//        ;
//        if(Type==6){
//            textView.setText(Html.fromHtml(DES.decryptDES(data.getData())));
//        }else {
        if(Type==4){

            textView.setText(data.getData());
        }else {
            textView.setText(DES.decryptDES(data.getData()));
        }

//        }

    }

//
//    private void log(String responseInfo){
//        if (responseInfo.length() > 4000) {
//            Log.v(TAG, "sb.length = " + responseInfo.length());
//            int chunkCount = responseInfo.length() / 4000;     // integer division
//            for (int i = 0; i <= chunkCount; i++) {
//                int max = 4000 * (i + 1);
//                if (max >= responseInfo.length()) {
//                    Log.v(TAG, "chunk " + i + " of " + chunkCount + ":" + responseInfo.substring(4000 * i));
//                } else {
//                    Log.v(TAG, "chunk " + i + " of " + chunkCount + ":" + responseInfo.substring(4000 * i, max));
//                }
//            }
//        } else {
//            Log.v(TAG, responseInfo.toString());
//        }
//    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_apply_train_textmain);
        Type=getIntent().getIntExtra("Type",0);
    }

    @Override
    protected void initTitle() {


        TitleView titleView = (TitleView) findViewById(R.id.apply_train_textmain_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        switch (Type){
            case 1:
                titleView.setMiddleText(R.string.apply_train_text1);
                break;
            case 2:
                titleView.setMiddleText(R.string.apply_train_text2);
                break;
            case 3:
                titleView.setMiddleText(R.string.apply_train_text3);
                break;
            case 4:
                titleView.setMiddleText(R.string.my_recommend_text2);
                break;
            case 5:
                titleView.setMiddleText(R.string.apply_train_text4);
                break;

        }
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void findView() {
        textView= (TextView) findViewById(R.id.apply_all_text);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    @Override
    protected void init() {
        getDate();

    }

    private void getDate(){
        if(Utils.isNetworkAvailable(AllApplyTrainActivity.this)) {
            JSONObject json = new JSONObject(true);
            json.put("SchoolId", sp.getInt("SchoolId",0));
            switch (Type){
                case 1:
                    url= Constant.URL_RegistrationNotice;
                    break;
                case 2:
                    url = Constant.URL_LearningProcess;
                    break;
                case 3:
                    url = Constant.URL_PhysicalExamination;
                    break;
                case 4:
                    json.put("Type",1);
                    url = Constant.URL_InvitationRules;
                    break;
                case 5:
                    url = Constant.URL_DetailsOfCharges;
                    break;
            }
            Log.d(TAG, "getDate: "+json.toString());
            new MyThread(url, handler, DES.encryptDES(json.toString())).start();
        }else {
            MyToast.makeText(AllApplyTrainActivity.this,R.string.Toast_internet);
        }
    }
}
