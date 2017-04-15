package com.DLPort.mytool;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.DLPort.myactivity.LoginIn;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/4/29.
 */
public class MyThread extends Thread {
    private static HttpClient httpClinet;
    private String TAG = "MyThread";
    public static final int SUCCESS = 1;
    public static final int UNKNOWN = 2;
    public static final int ENCODINGEXE = 3;
    public static final int PROTOCOLEXE = 4;
    public static final int IOEXE = 5;
    public static final int ISNULL = 6;
    public static final int UNAuthorization = 7;
    private Handler handler;
    private JSONObject json;
    private String path;
    private String result;
    private Message msg;
    private String token;
    private SharedPreferences sp,sp1;
    private Context context;

    public MyThread(String path, Handler handler, JSONObject json,Context context) {
        this.path = path;
        this.handler = handler;
        this.json = json;
        this.context = context;
        sp=context.getSharedPreferences("user", Context.MODE_PRIVATE);
        sp1 = context.getSharedPreferences("huo", Context.MODE_PRIVATE);
        if(sp.getBoolean("LOGINOK",false)){
            this.token=sp.getString("Token", null);
            Log.d(TAG,"car user token==="+this.token);
        }
        if(sp1.getBoolean("LOGINOK",false)){
            this.token=sp1.getString("Token", null);
            Log.d(TAG,"goods user token============="+this.token);
        }
    }

    /*
    public MyThread(String path, Handler handler, JSONObject json) {
        this.path = path;
        this.handler = handler;
        this.json = json;
    }
    */
    /*
    public MyThread(String path, Handler handler, JSONObject json, String token) {
        this.path = path;
        this.handler = handler;
        this.json = json;
        this.token = token;
    }
    */

    private static HttpClient getDefaultHttpClient() {
        if (httpClinet == null) {
            httpClinet = new DefaultHttpClient();
        }
        return httpClinet;
    }

    @Override
    public void run() {

        super.run();
        try {
//            Log.i(TAG, "当前的请求是" + path);
            HttpPost post = new HttpPost(path);
            HttpConnectionParams.setConnectionTimeout(post.getParams(), 8000);

            post.addHeader("Content-Type", "application/json");
            post.addHeader("Accept", "application/json");
            if (null != token) {
                post.addHeader("Authorization", "Basic " + Base64.encode(token.getBytes()));
            }

            if (json != null) {
                StringEntity entity = new StringEntity(json.toString(), "UTF-8");
//                entity.setContentType("application/json");
//                Log.i(TAG, json.toString());
                post.setEntity(entity);
            }
            Log.i(TAG, path);

            HttpResponse response = getDefaultHttpClient().execute(post);

            Log.i(TAG, String.valueOf(response.getStatusLine().getStatusCode()));
            if (response.getStatusLine().getStatusCode() == 200) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                response.getEntity().getContent(), "UTF-8"));
                result = br.readLine();

                if (!TextUtils.isEmpty(result)) {
                    msg = new Message();
                    msg.what = SUCCESS;
                    msg.obj = result;
                    handler.sendMessage(msg);
                } else {
                    msg = new Message();
                    msg.what = ISNULL;
                    handler.sendMessage(msg);
                }
            } else if (response.getStatusLine().getStatusCode() == 401) {
                /*
                new AlertDialog.Builder(context).setTitle("登录提醒")
                        .setMessage("您的账号在其他的设备登录, 如非本人操作请尽快修改登录密码!")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setClass(context, LoginIn.class);
                                context.startActivity(intent);
                            }
                        }).show();
                        */
                msg = new Message();
                msg.what = UNAuthorization;
                handler.sendMessage(msg);
            } else {
                msg = new Message();
                msg.what = response.getStatusLine().getStatusCode();
                handler.sendMessage(msg);
            }
        } catch (UnsupportedEncodingException e) {

            msg = new Message();
            msg.what = ENCODINGEXE;
            handler.sendMessage(msg);
            Log.i(TAG,
                    "UnsupportedEncodingException————————————————————————————————");
            e.printStackTrace();
        } catch (ClientProtocolException e) {

            Log.i(TAG,
                    "ClientProtocolException————————————————————————————————");
            msg = new Message();
            msg.what = PROTOCOLEXE;
            handler.sendMessage(msg);
            e.printStackTrace();
        } catch (IOException e) {
            Log.i(TAG, "IOException————————————————————————————————");
            msg = new Message();
            msg.what = IOEXE;
            handler.sendMessage(msg);
            e.printStackTrace();
        }
    }

}
