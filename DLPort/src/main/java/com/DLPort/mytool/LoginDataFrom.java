package com.DLPort.mytool;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Administrator on 2016/4/28.
 */
public class LoginDataFrom {
    private String path;
    private SharedPreferences sp;
    private JSONObject json;
    Context context;
    public LoginDataFrom(Context context,String url,JSONObject json){
        this.context=context;
        this.path=url;
        this.json=json;

    }

    public void getData(final DataCallBack dataCallBack){
        sp = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        final Handler handler = new MyHandler(context) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1&& dataCallBack !=null){

                    try {
                        JSONObject jsonUser = new JSONObject((String) msg.obj);
                        int status = json.getInt("Status");
                        if (0 == status) {
                            JSONObject jsondata = jsonUser.getJSONObject("data");
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putString("UserId",jsondata.getString("UserId"));
                            edit.putString("Companyname",jsondata.getString("Companyname"));
                            edit.putString("Principal",jsondata.getString("Principal"));
                            edit.putString("LoginName",jsondata.getString("LoginName"));
                            edit.putString("Integral",jsondata.getString("Integral"));
                            edit.putString("Address",jsondata.getString("Address"));
                            edit.putString("Token",jsondata.getString("Token"));
                            edit.commit();
                        }
                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        };



        new MyThread(path,handler,json,context).start();
    }


    /**
     * 网路访问调接口
     *
     */
    public interface DataCallBack {
        void onDataCallBack(JSONObject data);
    }



}
