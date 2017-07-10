package cn.com.goodsowner.util;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import cn.com.goodsowner.bean.BaseInfo;

/**
 * Created by gpw on 2016/11/1.
 * --加油
 */
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
    public abstract void onStateError();

    Listener<String> loadingListener() {
        mListener = new Listener<String>() {
            @Override
            public void onResponse(String result) {
                result = result.substring(1);
                result = result.substring(0,result.length()-1);
                try {
                    result = EncryptUtil.decryptDES(result);
                    Gson gson = new Gson();
                    BaseInfo baseInfo = gson.fromJson(result,BaseInfo.class);
                    LogUtil.i(baseInfo.toString());
                    if (baseInfo.getStatus()!=1){
                        Toast.makeText(mContext, baseInfo.getMessage(), Toast.LENGTH_SHORT).show();
                        onStateError();
                    }else {
                        onSuccess(baseInfo.getData());
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
             //   JPushInterface.setAlias(mContext, //上下文对象
//                    "", //别名
//                    new TagAliasCallback() {//回调接口,i=0表示成功,其它设置失败
//                        @Override
//                        public void gotResult(int i, String s, Set<String> set) {
//                            if(i==0){
//                                System.out.println("绑定成功");}
//                        }
//                    });
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//            builder.setTitle("提示");
//            builder.setMessage("当前账号在另一台设备登录，请重新登录");
//            builder.setCancelable(false);
//            builder.setIcon(R.mipmap.hint);
//            builder.setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Intent intent = new Intent(mContext,LoginActivity.class);
//                    mContext.startActivity(intent);
//                    mActivity.finish();
//                }
//            });
//            builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    MyApplication.getInstance().exit();
//                }
//            });
//            builder.show();
//            return;
//        }
                onError(error);
            }
        };
        return mErrorListener;
    }
}
