package com.guugoo.jiapeistudent.MinorActivity;

import android.os.Bundle;
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

public class NoticeDetailsActivity extends BaseActivity {
    private TextView Title, Content;
    private int NoticeId;

    @Override
    protected void processingData(ReturnData data) {
        JSONObject json = JSONObject.parseObject(data.getData());
        Title.setText(json.getString("Title"));
        Content.setText(DES.decryptDES(json.getString("Content")));
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_notice_details);
    }

    @Override
    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.notice_details_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setMiddleText(R.string.inform);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void findView() {
        Title = (TextView) findViewById(R.id.notice_details_text1);
        Content = (TextView) findViewById(R.id.notice_details_text2);
        NoticeId= getIntent().getIntExtra("NoticeId",0);
    }

    @Override
    protected void init() {

        if (Utils.isNetworkAvailable(NoticeDetailsActivity.this)) {
            JSONObject json = new JSONObject(true);
            json.put("NoticeId",NoticeId);
            new MyThread(Constant.URL_NoticeDetails, handler, DES.encryptDES(json.toString())).start();
        } else {
            MyToast.makeText(NoticeDetailsActivity.this, R.string.Toast_internet);
        }
    }
}
