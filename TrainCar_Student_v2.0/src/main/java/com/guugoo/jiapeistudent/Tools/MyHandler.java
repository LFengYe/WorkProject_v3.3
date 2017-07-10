package com.guugoo.jiapeistudent.Tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Set;

import com.guugoo.jiapeistudent.App.ActivityCollector;
import com.guugoo.jiapeistudent.App.MyApplication;
import com.guugoo.jiapeistudent.MainActivity.LoginActivity;
import com.guugoo.jiapeistudent.R;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by fuyzh on 16/5/31.
 */
public class MyHandler extends Handler {
    private static final String TAG = "MyHandler";
    private Context context=null;
    private Activity activity;
    private SharedPreferences sp;
    public MyHandler(Context context) {
        this.context = context;
        activity = (Activity) context;
    }


    @Override
    public void handleMessage(Message msg) {
//        super.handleMessage(msg);
        switch (msg.what) {
            case MyThread.SUCCESS:
            {
                break;
            }
            case MyThread.ENCODINGEXE:
            {
                MyToast.makeText(context, R.string.Toast_internet_no);
                break;
            }
            case MyThread.IOEXE:
            {
                MyToast.makeText(context, R.string.Toast_internet_no);
                break;
            }
            case MyThread.ISNULL:
            {
                MyToast.makeText(context, R.string.Toast_internet_no);
                break;
            }
            case MyThread.PROTOCOLEXE:
            {
                MyToast.makeText(context, R.string.Toast_internet_no);
                break;
            }
            case MyThread.UNAuthorization:
            {
                sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                builder.setMessage("您的账号已经在另外一个端口登录, 如非本人操作请尽快修改密码!");
                builder.setTitle("系统提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!activity.isFinishing()) {

                            sp.edit().putBoolean("AUTO_ISCHECK", false).apply();
                            sp.edit().putBoolean("LOGINOK",false).apply();
                            //退出极光推送绑定
                            JPushInterface.setAlias(context, "", new TagAliasCallback() {
                                @Override
                                public void gotResult(int i, String s, Set<String> set) {

                                }
                            });
                            JPushInterface.stopPush(context.getApplicationContext());
                            Intent intent = new Intent();
                            intent.setClass(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                            context.startActivity(intent);
                        }
                    }
                });
                if (!activity.isFinishing()) {
                    builder.create().show();
                }
                break;
            }
            case MyThread.UNKNOWN:
            {
                MyToast.makeText(context, R.string.Toast_internet_no);
                break;
            }
            default:
            {
                MyToast.makeText(context, R.string.Toast_internet_no);
                break;
            }
        }
    }
}
