package cn.guugoo.jiapeistudent.Tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.*;
import android.util.Base64;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.guugoo.jiapeistudent.App.MyApplication;

/**
 * Created by Administrator on 2016/4/29.
 */
public class MyThread extends Thread {
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
    private SharedPreferences sp;
    private String Token;
    public MyThread(String path, Handler handler, JSONObject json) {
        this.path = path;
        this.handler = handler;
        this.json = json;
        gettaken();
    }
    public MyThread(String path, Handler handler, JSONObject json,int i) {
        this.path = path;
        this.handler = handler;
        this.json = json;
        Token="";
    }

    private void gettaken(){
        sp =MyApplication.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        Token = sp.getString("token","");
    }

    @Override
    public void run() {

        super.run();
        try {
            Log.i(TAG, "当前的请求是" + path);
            URL url = new URL(path);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            //因为这个是post请求,设立需要设置为true
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            // 设置以POST方式
            urlConn.setRequestMethod("POST");
            // Post 请求不能使用缓存
            if (!Token.equals("")) {
                urlConn.setRequestProperty("Authorization", "Basic " + android.util.Base64.encodeToString(Token.getBytes(),Base64.DEFAULT));
            }

            urlConn.setUseCaches(false);
            urlConn.setInstanceFollowRedirects(true);
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setRequestProperty("Accept", "application/json");
            // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
            // 要注意的是connection.getOutputStream会隐含的进行connect。


            urlConn.connect();

            //DataOutputStream流
            DataOutputStream out = new DataOutputStream(urlConn.getOutputStream());

           
            if (json != null) {
                //将要上传的内容写入流中
                out.write(json.toString().getBytes("UTF-8"));
                //刷新、关闭
                out.flush();
                out.close();
            }

            int code = urlConn.getResponseCode();
            Log.i(TAG, "code="+String.valueOf(code));
            if (code == 200) {
                //获取数据
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(urlConn.getInputStream(),"UTF-8"));

                Log.d(TAG, "run: 获取数据成功");
                result = DES.decryptDES(reader.readLine().replace("\"",""));

                if (!TextUtils.isEmpty(result)) {
                    msg = new Message();
                    Log.i(TAG, "正常响应————————————————————————————————");
                    msg.what = SUCCESS;
                    msg.obj = result;
                    Log.d(TAG,"返回的数据为："+result);
                    handler.sendMessage(msg);
                } else {
                    msg = new Message();
                    msg.what = ISNULL;
                    handler.sendMessage(msg);
                }
            }else if (code == 401) {
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
            }  else {
                msg = new Message();
//                msg.what = response.getStatusLine().getStatusCode();
                handler.sendMessage(msg);
            }
        } catch (UnsupportedEncodingException e) {

            msg = new Message();
            msg.what = ENCODINGEXE;
            handler.sendMessage(msg);
            Log.i(TAG, "UnsupportedEncodingException————————————————————————————————");
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
