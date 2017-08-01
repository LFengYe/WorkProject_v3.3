package cn.com.goodsowner.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import cn.com.goodsowner.activity.MainActivity;
import cn.com.goodsowner.activity.OrderDetailActivity;
import cn.com.goodsowner.activity.OrderOffersActivity;
import cn.com.goodsowner.base.Contants;
import cn.com.goodsowner.bean.MessageExtras;
import cn.com.goodsowner.util.LogUtil;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by gpw on 2017/1/3.
 */

public class MyReceiver extends BroadcastReceiver {
    private String extras;
    private SharedPreferences prefs;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Gson gson = new Gson();
        MessageExtras messageExtras = gson.fromJson(extras, MessageExtras.class);
        //prefs = context.getSharedPreferences(Constants.SHARED_NAME, Context.MODE_PRIVATE);
        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            LogUtil.d("[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Toast.makeText(context, bundle.getString(JPushInterface.EXTRA_MESSAGE), Toast.LENGTH_SHORT).show();
//            if (messageExtras.getType() == 8) {
//                Constants.ballState = true;
//            }


        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction()))

        {
            LogUtil.d("[MyReceiver] 接收到推送下来的通知");
            LogUtil.d("[MyReceiver0] extras" + extras);
//            if (messageExtras.getType() == 8) {
//                Constants.ballState = true;
//            }

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()))

        {
            LogUtil.d("[MyReceiver] 用户点击打开了通知");
            LogUtil.d("[MyReceiver1] extras" + extras);
            Intent i;
            switch (messageExtras.getType()) {
                case 2:
                case 3:
                case 5:
                    if (!isBackground(context)) {
                        i = new Intent(context, OrderDetailActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //  i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("orderId", messageExtras.getId());
                        context.startActivity(i);
                    } else {
                        i = new Intent(context, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        // i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                    newsAdd(context);
                    break;

                case 6:

                    if (!isBackground(context)) {
                        i = new Intent(context, OrderOffersActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //  i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("orderId", messageExtras.getId());
                        i.putExtra("isToPay", "False");
                        context.startActivity(i);
                    } else {
                        i = new Intent(context, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        // i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                    newsAdd(context);
                    break;
                case 9:
                    break;
            }

        }

    }

    private void newsAdd(Context context){
        Contants.newsNum++;
        prefs = context.getSharedPreferences(Contants.SHARED_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("newsNum", Contants.newsNum);
        editor.apply();

    }

    public boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                return appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            }
        }
        return false;
    }
}
