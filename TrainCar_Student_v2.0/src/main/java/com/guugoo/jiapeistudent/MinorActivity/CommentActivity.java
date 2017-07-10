package com.guugoo.jiapeistudent.MinorActivity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import com.guugoo.jiapeistudent.App.Constant;
import com.guugoo.jiapeistudent.Data.ReturnData;
import com.guugoo.jiapeistudent.MainActivity.BaseActivity;
import com.guugoo.jiapeistudent.R;
import com.guugoo.jiapeistudent.Tools.DES;
import com.guugoo.jiapeistudent.Tools.MyHandler;
import com.guugoo.jiapeistudent.Tools.MyThread;
import com.guugoo.jiapeistudent.Tools.MyToast;
import com.guugoo.jiapeistudent.Tools.Utils;
import com.guugoo.jiapeistudent.Views.TitleView;

public class CommentActivity extends BaseActivity {
    private static final String TAG = "CommentActivity";

    private RatingBar[] ratingBars;
    private String BookingId;
    private EditText comment;
    private Button button;

    private SharedPreferences sp;

    @Override
    protected void processingData(ReturnData data) {
        studentEvaluate();
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_comment);
    }

    @Override
    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.comment_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setMiddleText(R.string.comment);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void findView() {

        BookingId = getIntent().getStringExtra("BookingId");

        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        ratingBars = new RatingBar[4];
        ratingBars[0] = (RatingBar) findViewById(R.id.RatingBarId1);
        ratingBars[1] = (RatingBar) findViewById(R.id.RatingBarId2);
        ratingBars[2] = (RatingBar) findViewById(R.id.RatingBarId3);
        ratingBars[3] = (RatingBar) findViewById(R.id.RatingBarId4);
        comment = (EditText) findViewById(R.id.comment_text);
        button = (Button) findViewById(R.id.comment_button);
    }

    @Override
    protected void init() {
        studentEvaluate();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CommentActivity.this);
                builder.setMessage("确认提交吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Comment();
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
        });
    }

    private void Comment() {
        if (Utils.isNetworkAvailable(CommentActivity.this)) {

            JSONObject json = new JSONObject(true);
            json.put("BookingId", BookingId);
            json.put("Attitude", (int) ratingBars[1].getRating());
            json.put("Technology", (int) ratingBars[0].getRating());
            json.put("Appearance", (int) ratingBars[2].getRating());
            json.put("CarCondition", (int) ratingBars[3].getRating());
            json.put("StudentId", sp.getInt("Id", 0));
            json.put("Comment", comment.getText());

            new MyThread(Constant.URL_Comment, handler, DES.encryptDES(json.toString())).start();

        } else {
            MyToast.makeText(CommentActivity.this, R.string.Toast_internet);
        }
    }

    private void studentEvaluate() {
        JSONObject json = new JSONObject(true);
        json.put("BookingId", BookingId);
        json.put("StudentId", sp.getInt("Id", 0));
        new MyThread(Constant.URL_StudentEvaluate, handler1, DES.encryptDES(json.toString())).start();
    }

    protected Handler handler1 = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                System.out.println(msg.obj);
                try {
                    ReturnData data = JSONObject.parseObject((String) msg.obj, ReturnData.class);
                    if (data.getStatus() == 0) {
                        JSONObject jsonObject = JSON.parseObject(data.getData());
                        ratingBars[0].setRating(jsonObject.getFloat("Technology"));
                        ratingBars[0].setEnabled(false);
                        ratingBars[1].setRating(jsonObject.getFloat("Attitude"));
                        ratingBars[1].setEnabled(false);
                        ratingBars[2].setRating(jsonObject.getFloat("Appearance"));
                        ratingBars[2].setEnabled(false);
                        ratingBars[3].setRating(jsonObject.getFloat("CarCondition"));
                        ratingBars[3].setEnabled(false);

                        comment.setText(jsonObject.getString("Comment"));
                        comment.setEnabled(false);

                        button.setEnabled(false);
                        button.setBackgroundResource(R.drawable.disable_layout);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    MyToast.makeText(CommentActivity.this, "数据出错");
                }
            }
        }
    };

}
