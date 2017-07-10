package com.guugoo.jiapeistudent.MainActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guugoo.jiapeistudent.App.Constant;
import com.guugoo.jiapeistudent.Data.ReturnData;
import com.guugoo.jiapeistudent.R;
import com.guugoo.jiapeistudent.Tools.DES;
import com.guugoo.jiapeistudent.Tools.MyThread;
import com.guugoo.jiapeistudent.Tools.MyToast;
import com.guugoo.jiapeistudent.Tools.Utils;
import com.guugoo.jiapeistudent.Views.TitleView;

public class StudyActivity extends BaseActivity {
    private static final String TAG = "StudyActivity";
    private WebView webView;

    private String Url=null;


    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_study);
    }

    @Override
    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.study_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setMiddleText(R.string.home_text2);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void findView() {
        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    protected void init() {
        getUrl();
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }

    private void getUrl(){
        if(Utils.isNetworkAvailable(StudyActivity.this)) {
            JSONObject json = new JSONObject();
            new MyThread(Constant.URL_TheoryStudy, handler, DES.encryptDES(json.toString())).start();

        }else {
            MyToast.makeText(StudyActivity.this,R.string.Toast_internet);
        }
    }

    @Override
    protected void processingData(ReturnData data) {
        Log.d(TAG,data.getData());
        JSONArray jsonArray = JSONObject.parseArray(data.getData());
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        Url = jsonObject.getString("Link");
        webView.loadUrl(Url);
    }
}
