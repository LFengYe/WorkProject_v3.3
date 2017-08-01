package cn.com.goodsowner.util;


import android.content.Context;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import cn.com.goodsowner.base.BaseApplication;

/**
 * Created by gpw on 2016/8/1.
 * --加油
 */
public class HttpUtil {

    public static StringRequest stringRequest;

    public static void doPost(Context context, String url, String tag, final Map<String, String> params,
                              VolleyInterface vif) {

        //获取全局的请求队列并把基于Tag标签的请求全部取消，防止重复请求
        BaseApplication.getHttpQueues().cancelAll(tag);
        stringRequest = new StringRequest(Request.Method.POST, url, vif.loadingListener(), vif.errorListener()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                HashMap<String, String> headers = new HashMap<String, String>();
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 1, 1.0f));

        // 设置标签
        stringRequest.setTag(tag);
        // 加入队列
        BaseApplication.getHttpQueues().add(stringRequest);
        // 启动
       // BaseApplication.getHttpQueues().start();
    }

    public static void setImageLoader(String url, ImageView imageView, int defaultImageResId, int errorImageResId) {
        ImageLoader loader = new ImageLoader(BaseApplication.getHttpQueues(), new BitmapCache());
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageView, defaultImageResId, errorImageResId);
        loader.get(url, imageListener);
    }




}