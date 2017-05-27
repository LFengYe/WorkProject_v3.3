package cn.guugoo.jiapeistudent.MinorActivity;

import android.content.Context;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import cn.guugoo.jiapeistudent.App.Constant;
import cn.guugoo.jiapeistudent.MainActivity.BaseActivity;
import cn.guugoo.jiapeistudent.R;
import cn.guugoo.jiapeistudent.Tools.DES;
import cn.guugoo.jiapeistudent.Views.TitleView;

public class MapActivity extends BaseActivity {
    private static final String TAG = "MapActivity";
    private WebView webView;
    private String Url=null;
    private String Lon;
    private String Lat;
    private String Location;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_map);
    }

    @Override
    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.map_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setMiddleText(R.string.map);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void findView() {
        Bundle bundle = getIntent().getExtras();
        Lat =bundle.getString("Lat");
        Lon = bundle.getString("Lon");
        Location = bundle.getString("Location");
        try {
            Location= URLEncoder.encode(Location, "utf-8");
            Log.d(TAG, "findView:2342 "+Location);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        webView = (WebView) findViewById(R.id.map_web_view);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //可使滚动条不占位
//        webView.getSettings().setDatabaseEnabled(true); //启用数据库
//        String dir = this.getApplicationContext().getDir("database",
//                Context.MODE_PRIVATE).getPath();
//        webView.getSettings().setGeolocationEnabled(true);//设置定位的数据库路径
//        webView.getSettings().setGeolocationDatabasePath(dir);
        WebSettings settings = webView.getSettings();
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptEnabled(true);

        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setGeolocationEnabled(true);     //允许定位
        settings.setSupportZoom(true);          //允许缩放
        settings.setBuiltInZoomControls(true);  //原网页基础上缩放
        settings.setUseWideViewPort(true);      //任意比例缩放
        webView.setWebChromeClient(new WebChromeClient(){

            //配置权限（同样在WebChromeClient中实现）
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,
                                                           GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });
        Url = Constant.URL_Map+Location+"&lat="+Lat+"&lon="+Lon;
//        Log.d(TAG, "findView: "+Url);
//        webView.getSettings().setDefaultTextEncodingName("UTF-8");
//        Url="http://www.baidu.com";
//        Log.d(TAG, "findView:22 "+Url);
//        webView.loadData(Url,"text/html; charset=UTF-8",null);
//        Url="http://map.baidu.com/?newmap=1&s=con%26wd%3D%E6%B9%96%E5%8C%97%E5%8D%81%E5%A0%B0%E7%99%BE%E5%BA%A6%26c%3D216&from=alamap&tpl=mapdots";
//        webView.loadData(Url, "text/html; charset=UTF-8", null);

//        Url="http://101.201.74.192:8001/ShowMap.Html?n=%E6%B5%8B%E8%AF%95%E5%9C%BA%E5%9C%B0&lat=23.130905&lon=114.430511";
//        webView.loadUrl(Url);
//        webView.loadDataWithBaseURL(null,Url,"text/html","utf-8",null);
    }

    @Override
    protected void init() {
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            //扩充缓存的容量
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
                //handler.cancel(); 默认的处理方式，WebView变成空白页
                //          //接受证书
                handler.proceed();
                //handleMessage(Message msg); 其他处理
            }

        });

        Log.d(TAG, "init: "+Url);
        webView.loadUrl(Url);
    }
}
