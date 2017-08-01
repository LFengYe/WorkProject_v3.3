package cn.com.caronwer.util;

import android.util.Log;

public class LogUtil
    {

        private LogUtil()
        {
            /* cannot be instantiated */
            throw new UnsupportedOperationException("cannot be instantiated");
        }

        private static boolean isDebug = true;
        private static final String TAG = "hint";

        // 下面四个是默认tag的函数
        public static void i(String msg)
        {
            if (isDebug)
                Log.i(TAG, msg);
        }

        public static void d(String msg)
        {
            if (isDebug)
                Log.d(TAG, msg);
        }

        public static void e(String msg)
        {
            if (isDebug)
                Log.e(TAG, msg);
        }

        public static void v(String msg)
        {
            if (isDebug)
                Log.v(TAG, msg);
        }

        // 下面是传入自定义tag的函数
        public static void i(String tag, String msg)
        {
            if (isDebug)
                Log.i(tag, msg);
        }

        public static void d(String tag, String msg)
        {
            if (isDebug)
                Log.i(tag, msg);
        }

        public static void e(String tag, String msg)
        {
            if (isDebug)
                Log.i(tag, msg);
        }

        public static void v(String tag, String msg)
        {
            if (isDebug)
                Log.i(tag, msg);
        }

        /**
         * 分段打印出较长log文本
         * @param log        原log文本
         * @param showCount  规定每段显示的长度（最好不要超过eclipse限制长度）
         */
        public static void showLogCompletion(String log,int showCount){
            if(log.length() >showCount){
                String show = log.substring(0, showCount);
                Log.i("TAG", show+"");
                if((log.length() - showCount)>showCount){//剩下的文本还是大于规定长度
                    String partLog = log.substring(showCount,log.length());
                    showLogCompletion(partLog, showCount);
                }else{
                    String surplusLog = log.substring(showCount, log.length());
                    Log.i("TAG", surplusLog+"");
                }

            }else{
                Log.i("TAG", log+"");
            }
        }
    }
