package cn.guugoo.jiapeistudent.MinorActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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


public class AddMyNewsActivity extends BaseActivity {
    private static final String TAG= "AddMyNewsActivity";
    private WebView webView;
    private SharedPreferences sp;
    @Override
    protected void processingData(ReturnData data) {
        Log.d(TAG, "processingData: "+DES.decryptDES(data.getData()));
        webView.loadData(DES.decryptDES(data.getData()), "text/html; charset=UTF-8", null);
//        webView.loadData(DES.decryptDES(data.getData()), "text/html", "UTF-8", null);
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_my_news);
    }

    @Override
    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.my_add_news_titles);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setMiddleText(R.string.Agreement);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void findView() {
        webView = (WebView) findViewById(R.id.news_web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8") ;
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
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
        if(Utils.isNetworkAvailable(AddMyNewsActivity.this)) {
            JSONObject json = new JSONObject();
            json.put("SchoolId", sp.getInt("SchoolId",0));
            new MyThread(Constant.URL_Agreement, handler, DES.encryptDES(json.toString())).start();

        }else {
            MyToast.makeText(AddMyNewsActivity.this,R.string.Toast_internet);
        }
    }
}
