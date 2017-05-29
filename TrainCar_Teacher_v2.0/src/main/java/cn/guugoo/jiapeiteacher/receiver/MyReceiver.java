package cn.guugoo.jiapeiteacher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import cn.guugoo.jiapeiteacher.activity.CirclesSharingActivity;
import cn.guugoo.jiapeiteacher.activity.MainActivity;
import cn.guugoo.jiapeiteacher.activity.MyNewsActivity;
import cn.guugoo.jiapeiteacher.activity.SplashActivity;
import cn.guugoo.jiapeiteacher.activity.StudentDetailsActivity;
import cn.guugoo.jiapeiteacher.activity.WorkbenchActivity;
import cn.guugoo.jiapeiteacher.base.Constants;
import cn.guugoo.jiapeiteacher.bean.MessageExtras;
import cn.jpush.android.api.JPushInterface;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private String extras;
    private SharedPreferences prefs;

    public MyReceiver() {
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Gson gson = new Gson();
        MessageExtras messageExtras = gson.fromJson(extras, MessageExtras.class);
        prefs = context.getSharedPreferences(Constants.SHARED_NAME, Context.MODE_PRIVATE);
        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Toast.makeText(context, bundle.getString(JPushInterface.EXTRA_MESSAGE), Toast.LENGTH_SHORT).show();
            if (messageExtras.getType() == 8) {
                Constants.ballState = true;
            }


        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction()))

        {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            Log.d(TAG, "[MyReceiver0] extras" + extras);
            if (messageExtras.getType() == 8) {
                Constants.ballState = true;
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()))

        {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            Log.d(TAG, "[MyReceiver1] extras" + extras);
            Intent i;
            switch (messageExtras.getType()) {
                case 1:
                    if (Constants.MainActivityState == 1) {
                        i = new Intent(context, CirclesSharingActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    } else {
                        i = new Intent(context, SplashActivity.class);
                        i.putExtra("startState", 1);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                    break;
                case 2:

                    if (Constants.MainActivityState == 1) {
                        i = new Intent(context, CirclesSharingActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    } else {
                        i = new Intent(context, SplashActivity.class);
                        i.putExtra("startState", 1);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                    break;

                case 7:

                    if (Constants.MainActivityState == 1) {
                        if (Constants.WorkActivityState == 1) {
                            i = new Intent(context, StudentDetailsActivity.class);
                            i.putExtra("bookingId", messageExtras.getId());
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        } else {
                            i = new Intent(context, WorkbenchActivity.class);
                            i.putExtra("startState", 7);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("bookingId", messageExtras.getId());
                            editor.apply();
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }
                    } else {
                        i = new Intent(context, SplashActivity.class);
                        i.putExtra("startState", 7);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("bookingId", messageExtras.getId());
                        editor.apply();
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                    break;

                case 8:

                    if (Constants.MainActivityState == 1) {
                        i = new Intent(context, MyNewsActivity.class);
                        i.putExtra("bookingId", messageExtras.getId());
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    } else {
                        i = new Intent(context, SplashActivity.class);
                        i.putExtra("startState", 8);
                        i.putExtra("bookingId", messageExtras.getId());
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                    break;
            }

        }

    }

}
