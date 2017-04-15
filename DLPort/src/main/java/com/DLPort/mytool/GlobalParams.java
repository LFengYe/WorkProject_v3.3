package com.DLPort.mytool;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.DLPort.myactivity.LoginIn;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/5/4.
 */
public class GlobalParams {
    private static final String TAG = "GlobalParams";

    // 判断是否有可用网
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String GetContainerType(int i){
        String s[]={ "20GP", "20OT", "20灌装","20G柜架","20冷藏",
                "40GP", "40HT", "40冷藏", "40高冷", "40柜架", "40灌箱",
                "45GP", "45HT", "45冷灌", "45高冷", "特殊箱型"};
        if (i >= 0 && i <= 15)
            return s[i];
        return String.valueOf(i);
    }

    public static void alertLoginOut(final Context context) {
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

    }

}


