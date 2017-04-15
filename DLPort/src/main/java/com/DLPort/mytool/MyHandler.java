package com.DLPort.mytool;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.DLPort.myactivity.LoginIn;

/**
 * Created by fuyzh on 16/5/31.
 */
public class MyHandler extends Handler {
    private Context context;

    public MyHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case MyThread.SUCCESS:
            {
                break;
            }
            case MyThread.ENCODINGEXE:
            {
                break;
            }
            case MyThread.IOEXE:
            {
                break;
            }
            case MyThread.ISNULL:
            {
                break;
            }
            case MyThread.PROTOCOLEXE:
            {
                break;
            }
            case MyThread.UNAuthorization:
            {
                Log.i("MyHandler", "UnAuthorization");
                new AlertDialog.Builder(context).setTitle("系统提示")
                        .setMessage("您的账号已经在另外一个端口登录, 如非本人操作请尽快修改密码!")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(context, LoginIn.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }
                        }).show();
                break;
            }
            case MyThread.UNKNOWN:
            {
                break;
            }
            default:
            {
                break;
            }
        }
    }
}
