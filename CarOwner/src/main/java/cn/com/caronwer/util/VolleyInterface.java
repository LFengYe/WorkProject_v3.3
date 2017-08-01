package cn.com.caronwer.util;

import android.content.Context;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import cn.com.caronwer.bean.BaseInfo;

public abstract  class VolleyInterface {

    public Context mContext;
    public static Listener<String> mListener;
    public static ErrorListener mErrorListener;

    public VolleyInterface(Context context, Listener<String> listener, ErrorListener errorListener) {
        this.mContext = context;
        mListener = listener;
        mErrorListener = errorListener;
    }
    public abstract void onSuccess(JsonElement result);
    public abstract void onError(VolleyError error);
    public abstract void onStateError(int sta,String msg);

    Listener<String> loadingListener() {
        mListener = new Listener<String>() {
            @Override
            public void onResponse(String result) {
                result = result.substring(1);
                result = result.substring(0,result.length()-1);
                try {
                    result = EncryptUtil.decryptDES(result);
                    LogUtil.i("服务器返回数据:", result);
                    Gson gson = new Gson();
                    BaseInfo baseInfo = gson.fromJson(result, BaseInfo.class);

                    if (baseInfo.getStatus() == 1) {
                        onSuccess(baseInfo.getData());
                    } else {
                        onStateError(baseInfo.getStatus(),baseInfo.getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        return mListener;
    }

    ErrorListener errorListener() {
        mErrorListener = new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onError(error);
            }
        };
        return mErrorListener;
    }
}
