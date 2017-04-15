package cn.guugoo.jiapeistudent.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.guugoo.jiapeistudent.App.Constant;
import cn.guugoo.jiapeistudent.MainActivity.CircleShareActivity;
import cn.guugoo.jiapeistudent.MainActivity.MainActivity;
import cn.guugoo.jiapeistudent.MainActivity.MyMessageActivity;
import cn.guugoo.jiapeistudent.MainActivity.MyReserveActivity;
import cn.guugoo.jiapeistudent.MinorActivity.PersonActivity;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	private Bundle bundle;

	@Override
	public void onReceive(Context context, Intent intent) {
        bundle = intent.getExtras();
		
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
			/**
			 * SDK 向 JPush Server 注册所得到的注册 ID可以在这里获取到。一般来说，可不处理此广播信息。
			 * 要深入地集成极光推送，开发者想要自己保存App用户与JPush 用户关系时，则接受此广播，取得 Registration ID
			 * 并保存与App uid 的关系到开发者自己的应用服务器上。
			 */
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
			progressMessageData(context, bundle.getString(JPushInterface.EXTRA_MESSAGE));

        
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {

			/**
			 * 收到通知。在这里可以做些统计，或者做些其他工作
			 */
			Log.i(TAG, "收到了通知,通知的标题为:" + bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
			Log.i(TAG, "通知的内容为:" + bundle.getString(JPushInterface.EXTRA_ALERT));
			Log.i(TAG, "通知的附加内容为:" + bundle.getString(JPushInterface.EXTRA_EXTRA));
			progressNotaFicationData(context, bundle.getString(JPushInterface.EXTRA_EXTRA));

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
			/**
			 * 用户点击了通知。在这里可以自己写代码去定义用户点击后的行为
			 */
			Log.i(TAG, "用户点击打开了通知");
			Log.i(TAG, "通知的标题为:" + bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
			Log.i(TAG, "通知的内容为:" + bundle.getString(JPushInterface.EXTRA_ALERT));
			Log.i(TAG, "通知的附加内容为:" + bundle.getString(JPushInterface.EXTRA_EXTRA));
			progressNoticeData(context, bundle.getString(JPushInterface.EXTRA_EXTRA));

        	
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
	}

	private void progressNotaFicationData(Context context, String string) {
		Log.d(TAG, "progressNoticeData: "+string);
		JSONObject json = JSON.parseObject(string);
		int type =Integer.valueOf(json.getString("type"));
		switch (type) {
			case 5:
				//打开主界面
				Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
				msgIntent.putExtra(MainActivity.KEY_MESSAGE, string);
				context.sendBroadcast(msgIntent);
			case 3:
				//打开主界面
				Intent msgIntent2 = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
				msgIntent2.putExtra(MainActivity.KEY_MESSAGE, string);
				context.sendBroadcast(msgIntent2);
		}

	}

	private void progressNoticeData(Context context, String string) {
		Log.d(TAG, "progressNoticeData: "+string);
		JSONObject json = JSON.parseObject(string);
		int type =Integer.valueOf(json.getString("type"));
		switch (type){
			case 1:
				//帖子点赞 点击打开圈子
				Intent intent1 = new Intent(context, CircleShareActivity.class);
				intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent1);
				break;
			case 2:
				//帖子评论 点击打开圈子
				Intent intent2 = new Intent(context, CircleShareActivity.class);
				intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
				context.startActivity(intent2);
				break;
			case 3:
				//课程已结束 点击打开待付款
				Intent intent3 = new Intent(context, MyReserveActivity.class);
				intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
				context.startActivity(intent3);
				break;
			case 4:
				// 课程已付费 点击打开已结束
				Intent intent4 = new Intent(context, MyReserveActivity.class);
				intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  );
				context.startActivity(intent4);
				break;
			case 5:
				//练车已开始 点击打开已预约
				Intent intent5 = new Intent(context, MyReserveActivity.class);
				intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
				context.startActivity(intent5);
				break;
			case 6:
				//报名已付费 点击打开个人中心
				Intent intent6 = new Intent(context, PersonActivity.class);
				intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent6);
				break;
			case 7:
				//下课提醒 点击打开正在进行中的详情页面
				Intent intent7 = new Intent(context, MainActivity.class);
				intent7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent7);
				break;
			case 8:
				// 表示消息 点击打开我的消息页面
				Intent intent8 = new Intent(context, MyMessageActivity.class);
				intent8.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
				context.startActivity(intent8);
				break;
			default:
		}

//		//打开主界面
//		Intent i = new Intent(context, MainActivity.class);
//		//		i.putExtras(bundle);
//		//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//		context.startActivity(i);
	}

	private void progressMessageData(Context context, String string) {
		//检查当前主界面是否在前台
		if (MainActivity.isForeground) {
			Log.d(TAG, "progressMessageData: ");
			String message = string;
			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
			context.sendBroadcast(msgIntent);
		}else {
			Constant.state =true;
		}

	}
}
