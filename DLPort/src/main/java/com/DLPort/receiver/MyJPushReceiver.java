package com.DLPort.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.DLPort.NewsActivity.ActivityActivity;
import com.DLPort.NewsActivity.CarGoBusinessChangeActivity;
import com.DLPort.NewsActivity.CarOwnerBusinessChangeActivity;
import com.DLPort.NewsActivity.GangKouYujing;
import com.DLPort.NewsActivity.GangKouZuoye;
import com.DLPort.NewsActivity.InformActivity;
import com.DLPort.NewsActivity.JiaoTonggaosu;
import com.DLPort.NewsActivity.JiaoTongguodao;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.HuoInquireActivity;
import com.DLPort.myactivity.MainActivity;
import com.DLPort.myactivity.OrderActivity;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by fuyzh on 16/5/20.
 */
public class MyJPushReceiver extends BroadcastReceiver {
    private static final String TAG = "MyJPushReceiver";
    private Handler carOrderHandler;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

            /**
             * SDK 向 JPush Server 注册所得到的注册 ID可以在这里获取到。一般来说，可不处理此广播信息。
             * 要深入地集成极光推送，开发者想要自己保存App用户与JPush 用户关系时，则接受此广播，取得 Registration ID 并保存与App uid 的关系到开发者自己的应用服务器上。
             */
        
        }else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            /**
             * 收到自定义消息。自定义消息不会展示在通知栏，完全要开发者写代码去处理
             */
            Log.i(TAG, "收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            //MyToast.makeText(context, "收到推送的消息, 消息内容是:" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            //progressMessageData(context, bundle.getString(JPushInterface.EXTRA_MESSAGE));

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            /**
             * 收到通知。在这里可以做些统计，或者做些其他工作
             */
            Log.i(TAG, "收到了通知,通知的标题为:" + bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
            Log.i(TAG, "通知的内容为:" + bundle.getString(JPushInterface.EXTRA_ALERT));
            Log.i(TAG, "通知的附加内容为:" + bundle.getString(JPushInterface.EXTRA_EXTRA));

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            /**
             * 用户点击了通知。在这里可以自己写代码去定义用户点击后的行为
             */
            Log.i(TAG, "用户点击打开了通知");
            Log.i(TAG, "通知的标题为:" + bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
            Log.i(TAG, "通知的内容为:" + bundle.getString(JPushInterface.EXTRA_ALERT));
            Log.i(TAG, "通知的附加内容为:" + bundle.getString(JPushInterface.EXTRA_EXTRA));
            progressNoticeData(context, bundle.getString(JPushInterface.EXTRA_EXTRA));
        } else {
            // 不能处理的Action
        }
    }

    private void progressMessageData(Context context, String messageData) {
        String[] data = messageData.split("-");
        Intent intent = new Intent();
        switch (Integer.valueOf(data[0])) {
            case 0:
            {
                intent.setClass(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            }
            case 1:
            {
//                intent.setClass(context, RechargeInfoWriteActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
                ((Activity)context).finish();
                break;
            }
        }
    }

    private void progressNoticeData(Context context, String jsonData) {
        try {
            JSONObject object = new JSONObject(jsonData);
            int type = object.getInt("type");
            String id = object.getString("id");
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("messageId", id);
            switch (type) {
                case 0:
                {
                    /** type=0表示车主向货主申请取消订单，id为消息表编号*/
                    intent.setClass(context, CarGoBusinessChangeActivity.class);
                    break;
                }
                case 1:
                {
                    /** type=1表示车主通知货主某笔订单已经到达，id为消息表编号*/
                    intent.setClass(context, HuoInquireActivity.class);
                    break;
                }
                case 2:
                {
                    /** type=2表示货主发布了货源，通知所有的车主，id为货源主键*/
                    bundle.putInt("Type", 0);
                    intent.setClass(context, OrderActivity.class);
                    //getCarOrder(context, id);
                    break;
                }
                case 3:
                {
                    /** type=3表示货主同意车主申请取消订单，id为消息表编号*/
                    intent.setClass(context, CarOwnerBusinessChangeActivity.class);
                    break;
                }
                case 4:
                {
                    /** type=4表示货主不同意车主申请取消订单，id为消息表编号*/
                    intent.setClass(context, CarOwnerBusinessChangeActivity.class);
                    break;
                }
                case 5:
                {
                    /** type=5表示后台发布平台业务变更信息，id表示后台发布信息编号*/
                    //判断车主还是货主
                    break;
                }
                case 6:
                {
                    /** type=6表示后台发布平台活动信息，id表示后台发布信息编号*/
                    intent.setClass(context, ActivityActivity.class);
                    break;
                }
                case 7:
                {
                    /** type=7表示后台发布公司业务信息，id表示后台发布信息编号*/
                    intent.setClass(context, InformActivity.class);
                    break;
                }
                case 8:
                {
                    /** type=8表示后台发布作业信息信息，id表示后台发布信息编号*/
                    intent.setClass(context, GangKouZuoye.class);
                    break;
                }
                case 9:
                {
                    /** type=9表示后台发布作业预警信息，id表示后台发布信息编号*/
                    intent.setClass(context, GangKouYujing.class);
                    break;
                }
                case 10:
                {
                    /** type=10表示后台发布高速公路路况信息，id表示后台发布信息编号*/
                    intent.setClass(context, JiaoTonggaosu.class);
                    break;
                }
                case 11:
                {
                    /** type=11表示后台发布国道信息，id表示信息编号*/
                    intent.setClass(context, JiaoTongguodao.class);
                    break;
                }
                default:
                    MyToast.makeText(context, "错误的通知类型");
            }

            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCarOrder(Context context, String id) {
        if (GlobalParams.isNetworkAvailable(context)) {
            try {
                carOrderHandler = new MyHandler(context) {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 1) {
                            try {
                                JSONObject jsonUser = null;
                                jsonUser = new JSONObject((String) msg.obj);
                                Log.d(TAG, "响应数据:" + jsonUser.toString());
                                int status = jsonUser.getInt("Status");
                                if (status == 0) {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Id", id);
                new MyThread(Constant.URL_OrderPostCarOrder, carOrderHandler,
                        jsonObject, context).start();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            MyToast.makeText(context, "亲,网络未连接");
        }
    }
}
