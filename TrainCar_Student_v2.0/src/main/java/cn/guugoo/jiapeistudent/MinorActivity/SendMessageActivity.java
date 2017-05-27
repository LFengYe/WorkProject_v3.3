package cn.guugoo.jiapeistudent.MinorActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import cn.guugoo.jiapeistudent.App.Constant;
import cn.guugoo.jiapeistudent.Data.ReturnData;
import cn.guugoo.jiapeistudent.MainActivity.BaseActivity;
import cn.guugoo.jiapeistudent.R;
import cn.guugoo.jiapeistudent.Tools.DES;
import cn.guugoo.jiapeistudent.Tools.MyThread;
import cn.guugoo.jiapeistudent.Tools.MyToast;
import cn.guugoo.jiapeistudent.Tools.Utils;
import cn.guugoo.jiapeistudent.Views.TitleView;

public class SendMessageActivity extends BaseActivity {
    private static final String TAG = "SendMessageActivity";
    private EditText send;
    private SharedPreferences sp;


    @Override
    protected void processingData(ReturnData data) {
        MyToast.makeText(SendMessageActivity.this,data.getMessage());
        Intent intent =new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_send_message);
    }

    @Override
    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.send_message_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setRightTextVisible(true);
        titleView.setMiddleText(R.string.post_message);
        titleView.setRightText(R.string.post_message_send);
        titleView.setRightTextListenter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void findView() {
        send = (EditText) findViewById(R.id.send_message_content);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    @Override
    protected void init() {

    }
    private void sendMessage(){
        if(Utils.isNetworkAvailable(SendMessageActivity.this)){
            JSONObject json= new JSONObject(true);
            json.put("StudentId",sp.getInt("Id",0));
            json.put("Content",send.getText());
            json.put("Type",1);
            Log.d(TAG, "getTimeTable: "+json.toString());
            new MyThread(Constant.URL_Publish, handler, DES.encryptDES(json.toString())).start();

        }else {
            MyToast.makeText(SendMessageActivity.this,R.string.Toast_internet);
        }
    }

}
