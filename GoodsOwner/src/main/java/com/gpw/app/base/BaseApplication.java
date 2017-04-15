package com.gpw.app.base;

import android.app.Activity;
import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;

import java.util.Stack;

/**
 * Created by gpw on 2016/10/12.
 * --加油
 */

public class BaseApplication extends Application {
    private static BaseApplication sBaseApplication;
    public static RequestQueue queues;
    private Stack<Activity> mActivityStack = new Stack<>();


    public static BaseApplication getInstance() {
        if (sBaseApplication == null) {
            sBaseApplication = new BaseApplication();
        }
        return sBaseApplication;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //  JPushInterface.setDebugMode(true);  // 设置开启日志,发布时请关闭日志
        // JPushInterface.init(this);          // 初始化 JPush
        queues = Volley.newRequestQueue(this);
        SDKInitializer.initialize(this);
    }

    public static RequestQueue getHttpQueues() {
        return queues;
    }

    public void addActivity(final Activity curAT) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<Activity>();
        }
        mActivityStack.add(curAT);
    }

    public void removeActivity(final Activity curAT) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<Activity>();
        }
        mActivityStack.remove(curAT);
    }


    //返回堆内Activity的总数
    public int howManyActivities() {
        return mActivityStack.size();
    }


    //关闭所有Activity
    public void finishAllActivities() {
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (null != mActivityStack.get(i)) {
                mActivityStack.get(i).finish();
            }
        }
        mActivityStack.clear();
    }


    public void exit() {
        finishAllActivities();
        //android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

}
