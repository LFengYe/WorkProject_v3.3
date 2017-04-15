package com.DLPort.myactivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.DLPort.OurActivity.CarOwnerStatisticsActivity;
import com.DLPort.OurActivity.CargoStatisticsActivity;
import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.mytool.FuYouThread;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyToast;
import com.DLPort.mytool.XmlOperate;
import com.DLPort.myview.TitleView;
import com.fuiou.mpay.encrypt.DESCoderFUIOU;
import com.fuiou.util.MD5;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by LFeng on 16/6/7.
 * 显示支付结果
 */
public class PayConfirmActivity extends BaseActivity {
    private static final String TAG = "PayConfirmActivity";
    private static final int PROGRESS_ID = 1;

    private ProgressDialog progressDialog;
    private String parentTag;
    private int userType;

    private Handler fuYouHandler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_confirm);

        initTitle();
        init();
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.pay_confirm_title);
        titleView.setBackImageVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setMiddleText(R.string.pay_confirm);
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        final WebView webView = (WebView) findViewById(R.id.webView_test);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//4.4及其以上系统最好加上这句,禁用硬件加速
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                /*没有这一句, 打开后界面一片空白, 什么都没有*/
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //Log.i(TAG, "url:" + url);
                progressDialog.setMessage(getResources().getString(R.string.pay_create_order));
                progressDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!isFinishing() && PayConfirmActivity.this != null)
                    progressDialog.dismiss();

                if (url.contains(Constant.URL_PaymentPaySuccess)) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("Type", userType);
                    bundle.putString("parentTag", "PayConfirm");

                    if (null != parentTag && parentTag.equalsIgnoreCase("NewsPublish")) {
                        intent.setClass(PayConfirmActivity.this, MainActivity.class);
                    }
                    if (null != parentTag && parentTag.equalsIgnoreCase("LuoXiang")) {
                        intent.setClass(PayConfirmActivity.this, LuoxiangActivity.class);
                    }
                    if (null != parentTag && parentTag.equalsIgnoreCase("TiXiang")) {
                        intent.setClass(PayConfirmActivity.this, TiXiangActivity.class);
                    }
                    if (null != parentTag && parentTag.equalsIgnoreCase("FangXiang")) {
                        intent.setClass(PayConfirmActivity.this, FangXiangActivity.class);
                    }
                    if (null != parentTag && parentTag.equalsIgnoreCase("CarOwnerRecharge")) {
                        intent.setClass(PayConfirmActivity.this, CarOwnerStatisticsActivity.class);
                    }
                    if (null != parentTag && parentTag.equalsIgnoreCase("CargoRecharge")) {
                        intent.setClass(PayConfirmActivity.this, CargoStatisticsActivity.class);
                    }

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
                if (url.contains(Constant.URL_PaymentPayFailure)) {
                    MyToast.makeText(PayConfirmActivity.this, "支付信息填写有误!");
                    finish();
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();
                Log.i(TAG, "sslError");
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        parentTag = bundle.getString("tag");
        userType = bundle.getInt("Type");
        int amt = (int) (bundle.getFloat("amt") * 100);

        String postData = payActionDataCreate("11", "2.0", bundle.getString("mchntorderId"),
                bundle.getString("loginName"), String.valueOf(amt), bundle.getString("bankNo"),
                Constant.URL_PaymentPostFinishPay, bundle.getString("username"),
                bundle.getString("cerNo"), String.valueOf(bundle.getInt("cerType")), "1",
                Constant.URL_PaymentPaySuccess, Constant.URL_PaymentPayFailure);

        if (null != postData) {
            webView.postUrl(Constant.Fuiou_PayAction, postData.getBytes());
        } else {
            MyToast.makeText(this, "生成支付订单数据错误!");
            finish();
        }
    }

    protected String payActionDataCreate(String type, String version, String mchntorderId, String userId, String amt,
                                         String bankCard, String backUrl, String name, String idNO, String idType,
                                         String logoTp, String homeUrl, String reUrl) {
        if (GlobalParams.isNetworkAvailable(this)) {
            try {
                Map<String, String> params = new HashMap<>();
                params.put("ENCTP", "1");
                params.put("VERSION", version);
                params.put("MCHNTCD", Constant.Fuiou_MchntCd);

                HashMap<String, String> xmlData = new HashMap<>();
                xmlData.put("MCHNTCD", Constant.Fuiou_MchntCd);
                xmlData.put("TYPE", type);
                xmlData.put("VERSION", version);
                xmlData.put("LOGOTP", logoTp);
                xmlData.put("MCHNTORDERID", mchntorderId);
                xmlData.put("USERID", userId);
                xmlData.put("AMT", amt);
                xmlData.put("BANKCARD", bankCard);
                xmlData.put("BACKURL", backUrl);
                xmlData.put("REURL", reUrl);
                xmlData.put("HOMEURL", homeUrl);
                xmlData.put("NAME", name);
                xmlData.put("IDTYPE", idType);
                xmlData.put("IDNO", idNO);
                xmlData.put("SIGNTP", "md5");
                String signContent = type + "|" + version + "|" + Constant.Fuiou_MchntCd +
                        "|" + mchntorderId + "|" + userId + "|" + amt + "|" + bankCard + "|" + backUrl +
                        "|" + name + "|" + idNO + "|" + idType + "|" + logoTp + "|" + homeUrl +
                        "|" + reUrl + "|" + Constant.Fuiou_MchntKey;
                //Log.i(TAG, "签名明文:" + signContent);
                String md5Sign = MD5.MD5Encode(signContent);
                //Log.i(TAG, "MD5签名:" + md5Sign);
                xmlData.put("SIGN", md5Sign);

                XmlOperate operate = new XmlOperate();
                String xmlPack = operate.createXmlStr(xmlData, "ORDER");
                int position = xmlPack.indexOf("?>");
                xmlPack = xmlPack.substring(position + 2);
                //Log.i(TAG, "XML报文明文:" + xmlPack);

                String desEncrypt = DESCoderFUIOU.desEncrypt(xmlPack, DESCoderFUIOU.getKeyLength8(Constant.Fuiou_MchntKey));
                //Log.i(TAG, "XML报文密文:" + desEncrypt);
                params.put("FM", URLEncoder.encode(desEncrypt, "UTF-8"));//对于加密后的内容, 需要先进行URL编码

                String result = "";
                Set<String> keySet = params.keySet();
                for (String key : keySet) {
                    result += key + "=" + params.get(key) + "&";
                }

                return result.substring(0, result.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MyToast.makeText(PayConfirmActivity.this, "亲,网络未连接");
        }
        return null;
    }

    protected String payActionTestDataCreate(String type, String version, String mchntorderId, String userId, String amt,
                                         String bankCard, String backUrl, String name, String idNO, String idType,
                                         String logoTp, String homeUrl, String reUrl) {
        if (GlobalParams.isNetworkAvailable(this)) {
            try {
                Map<String, String> params = new HashMap<>();
                params.put("ENCTP", "1");
                params.put("VERSION", version);
                params.put("MCHNTCD", Constant.FuYou_TestMchntCd);

                HashMap<String, String> xmlData = new HashMap<>();
                xmlData.put("MCHNTCD", Constant.FuYou_TestMchntCd);
                xmlData.put("TYPE", type);
                xmlData.put("VERSION", version);
                xmlData.put("LOGOTP", logoTp);
                xmlData.put("MCHNTORDERID", mchntorderId);
                xmlData.put("USERID", userId);
                xmlData.put("AMT", amt);
                xmlData.put("BANKCARD", bankCard);
                xmlData.put("BACKURL", backUrl);
                xmlData.put("REURL", reUrl);
                xmlData.put("HOMEURL", homeUrl);
                xmlData.put("NAME", name);
                xmlData.put("IDTYPE", idType);
                xmlData.put("IDNO", idNO);
                xmlData.put("SIGNTP", "md5");
                String signContent = type + "|" + version + "|" + Constant.FuYou_TestMchntCd +
                        "|" + mchntorderId + "|" + userId + "|" + amt + "|" + bankCard + "|" + backUrl +
                        "|" + name + "|" + idNO + "|" + idType + "|" + logoTp + "|" + homeUrl +
                        "|" + reUrl + "|" + Constant.FuYou_TestMchntKey;
                //Log.i(TAG, "签名明文:" + signContent);
                String md5Sign = MD5.MD5Encode(signContent);
                //Log.i(TAG, "MD5签名:" + md5Sign);
                xmlData.put("SIGN", md5Sign);

                XmlOperate operate = new XmlOperate();
                String xmlPack = operate.createXmlStr(xmlData, "ORDER");
                int position = xmlPack.indexOf("?>");
                xmlPack = xmlPack.substring(position + 2);
                //Log.i(TAG, "XML报文明文:" + xmlPack);

                String desEncrypt = DESCoderFUIOU.desEncrypt(xmlPack, DESCoderFUIOU.getKeyLength8(Constant.FuYou_TestMchntKey));
                //Log.i(TAG, "XML报文密文:" + desEncrypt);
                params.put("FM", URLEncoder.encode(desEncrypt, "UTF-8"));//对于加密后的内容, 需要先进行URL编码

                String result = "";
                Set<String> keySet = params.keySet();
                for (String key : keySet) {
                    result += key + "=" + params.get(key) + "&";
                }

                return result.substring(0, result.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MyToast.makeText(PayConfirmActivity.this, "亲,网络未连接");
        }
        return null;
    }

    private void payAction(String type, String version, String mchntorderId, String userId, String amt,
                           String bankCard, String backUrl, String name, String idNO, String idType,
                           String logoTp, String homeUrl, String reUrl) {
        if (GlobalParams.isNetworkAvailable(this)) {
            try {
                Map<String, String> params = new HashMap<>();
                params.put("ENCTP", "1");
                params.put("VERSION", "2.0");
                params.put("MCHNTCD", Constant.FuYou_TestMchntCd);

                HashMap<String, String> fmData = new HashMap<>();
                fmData.put("MCHNTCD", Constant.FuYou_TestMchntCd);
                fmData.put("TYPE", type);
                fmData.put("VERSION", version);
                fmData.put("LOGOTP", logoTp);
                fmData.put("MCHNTORDERID", mchntorderId);
                fmData.put("USERID", userId);
                fmData.put("AMT", amt);
                fmData.put("BANKCARD", bankCard);
                fmData.put("BACKURL", backUrl);
                fmData.put("REURL", reUrl);
                fmData.put("HOMEURL", homeUrl);
                fmData.put("NAME", name);
                fmData.put("IDTYPE", idType);
                fmData.put("IDNO", idNO);
                fmData.put("SIGNTP", "md5");
                String signContent = type + "|" + version + "|" + Constant.FuYou_TestMchntCd +
                        "|" + mchntorderId + "|" + userId + "|" + amt + "|" + bankCard + "|" + backUrl +
                        "|" + name + "|" + idNO + "|" + idType + "|" + logoTp + "|" + homeUrl +
                        "|" + reUrl + "|" + Constant.FuYou_TestMchntKey;
                //Log.i(TAG, "签名明文:" + signContent);
                String md5Sign = MD5.MD5Encode(signContent);
                //Log.i(TAG, "MD5签名:" + md5Sign);
                fmData.put("SIGN", md5Sign);

                XmlOperate operate = new XmlOperate();
                String xmlPack = operate.createXmlStr(fmData, "ORDER");
                int position = xmlPack.indexOf("?>");
                xmlPack = xmlPack.substring(position + 2);
                //Log.i(TAG, "XML报文明文:" + xmlPack);

                String desEncrypt = DESCoderFUIOU.desEncrypt(xmlPack, DESCoderFUIOU.getKeyLength8(Constant.FuYou_TestMchntKey));
                //Log.i(TAG, "XML报文密文:" + desEncrypt);
                params.put("FM", desEncrypt);

                new FuYouThread(Constant.FuYou_TestPayAction, fuYouHandler, params).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MyToast.makeText(PayConfirmActivity.this, "亲,网络未连接");
        }
    }

    private void queryOrder(String orderId) {
        if (GlobalParams.isNetworkAvailable(this)) {
            HashMap<String, String> data = new HashMap<>();
            data.put("MchntCd", Constant.FuYou_TestMchntCd);
            data.put("OrderId", orderId);
            data.put("Sign", MD5.MD5Encode(Constant.FuYou_TestMchntCd + "|"
                    + orderId + "|" + Constant.FuYou_TestMchntKey));

            XmlOperate operate = new XmlOperate();
            String xmlPack = operate.createXmlStr(data, "FM");
            int position = xmlPack.indexOf("?>");
            xmlPack = xmlPack.substring(position + 2);

            Map<String, String> params = new HashMap<>();
            params.put("FM", xmlPack);
            new FuYouThread(Constant.FuYou_TestQueryOrder, fuYouHandler, params).start();
        } else {
            MyToast.makeText(PayConfirmActivity.this, "亲,网络未连接");
        }
    }

    private void carBinQuery(String cardNo) {
        XmlOperate operate = new XmlOperate();
        HashMap<String, String> data = new HashMap<>();
        data.put("MchntCd", Constant.FuYou_TestMchntCd);
        data.put("Ono", cardNo);
        data.put("Sign", MD5.MD5Encode(Constant.FuYou_TestMchntCd + "|"
                + cardNo + "|" + Constant.FuYou_TestMchntKey));

        String xmlPack = operate.createXmlStr(data, "FM");
        int position = xmlPack.indexOf("?>");
        xmlPack = xmlPack.substring(position + 2);
        Log.i(TAG, "xmlPack:" + xmlPack);

        Map<String, String> params = new HashMap<>();
        params.put("FM", xmlPack);
        if (GlobalParams.isNetworkAvailable(this)) {
            new FuYouThread(Constant.FuYou_TestCardBinQuery, fuYouHandler, params).start();
        } else {
            MyToast.makeText(PayConfirmActivity.this, "亲,网络未连接");
        }
    }
}
