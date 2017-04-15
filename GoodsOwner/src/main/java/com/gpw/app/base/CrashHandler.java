package com.gpw.app.base;

import android.content.Context;
import android.os.Looper;
import android.os.Process;
import android.widget.Toast;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static Object lock = new Object();

    private CrashHandler() {
        // Empty Constractor
    }

    private static CrashHandler mCrashHandler;
    private Context mContext;
    private Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;

    public static CrashHandler getInstance() {
        synchronized (lock) {
            if (mCrashHandler == null) {
                synchronized (lock) {
                    if (mCrashHandler == null) {
                        mCrashHandler = new CrashHandler();
                    }
                }
            }

            return mCrashHandler;
        }
    }

    /* 初始化 */
    public void init(Context context) {
        this.mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable ex) {
        if (isHandler(ex)) {
            handlerException(ex);
        } else {
            defaultUncaughtExceptionHandler.uncaughtException(thread, ex);
        }
    }

    private boolean isHandler(Throwable ex) {
        // 排序不需要捕获的情况
        if (ex == null) {
            return false;
        }

        // ...

        return true;

    }

    private void handlerException(final Throwable ex) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Looper.prepare();

                Toast.makeText(mContext, "程序发生了点小意外，即将关闭... " +
                        ex.getMessage(), Toast.LENGTH_SHORT).show();
                Looper.loop();


                // 将Activity的栈清空

                BaseApplication.getInstance().finishAllActivities();
                // 退出程序
                Process.killProcess(Process.myPid());
                // 关闭虚拟机，彻底释放内存空间
                System.exit(0);

            }

        }).start();
    }

}