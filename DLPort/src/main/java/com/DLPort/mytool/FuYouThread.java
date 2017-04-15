package com.DLPort.mytool;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by fuyzh on 16/5/31.
 */
public class FuYouThread extends Thread {
    private static final String TAG = "FuYouThread";
    private static HttpClient httpClinet;
    public static final int SUCCESS = 1;
    public static final int UNKNOWN = 2;
    public static final int ENCODINGEXE = 3;
    public static final int PROTOCOLEXE = 4;
    public static final int IOEXE = 5;
    public static final int ISNULL = 6;
    private Handler handler;
    private String path;
    private String result;
    private Message msg;
    private Map<String, String> data;

    private static HttpClient getDefaultHttpClient() {
        if (httpClinet == null) {
            httpClinet = new DefaultHttpClient();
        }
        return httpClinet;
    }

    public FuYouThread(String path, Handler handler, Map<String, String> data) {
        this.path = path;
        this.handler = handler;
        this.data = data;
    }

    @Override
    public void run() {
        super.run();
        msg = new Message();
        try {
            HttpPost post = new HttpPost(path);
            HttpConnectionParams.setConnectionTimeout(post.getParams(), 8000);
            List<NameValuePair> params = new ArrayList<>();

            Set<String> keySet = data.keySet();
            for (String key : keySet) {
                params.add(new BasicNameValuePair(key, data.get(key)));
            }

            if (params.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
                post.setEntity(entity);
            }

            HttpResponse response = getDefaultHttpClient().execute(post);
            Log.i(TAG, String.valueOf(response.getStatusLine().getStatusCode()));
            if (response.getStatusLine().getStatusCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

                StringBuilder builder = new StringBuilder();
                while (null != (result = reader.readLine())) {
                    builder.append(result);
                }
                Log.i(TAG, builder.toString());

                if (TextUtils.isEmpty(result)) {
                    msg.what = SUCCESS;
                    msg.obj = result;
                } else {
                    msg.what = ISNULL;
                }

                //handler.sendMessage(msg);
            } else {
                msg.what = UNKNOWN;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            msg.what = ENCODINGEXE;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            msg.what = PROTOCOLEXE;
        } catch (IOException e) {
            e.printStackTrace();
            msg.what = IOEXE;
        } finally {
            handler.sendMessage(msg);
        }
    }
}
