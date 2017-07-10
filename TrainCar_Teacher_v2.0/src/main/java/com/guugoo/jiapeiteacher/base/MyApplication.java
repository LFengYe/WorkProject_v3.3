package com.guugoo.jiapeiteacher.base;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import java.util.Stack;

import com.guugoo.jiapeiteacher.R;
import com.guugoo.jiapeiteacher.activity.LoginActivity;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by gpw on 2016/8/3.
 * --加油
 */
public class MyApplication extends Application {

    private static MyApplication instance;
    /*Activity堆*/
    private Stack<Activity> activityStack = new Stack<Activity>();

    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);  // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);          // 初始化 JPush
    }

    public void addActivity(final Activity curAT) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(curAT);
    }

    public void removeActivity(final Activity curAT) {
        if ( activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.remove(curAT);
    }

    //获取最后一个Activity
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    //返回堆内Activity的总数
    public int howManyActivities() {
        return activityStack.size();
    }


    //关闭所有Activity
    public void finishAllActivities() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }


    public void exit() {
        finishAllActivities();
        //android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }



}
