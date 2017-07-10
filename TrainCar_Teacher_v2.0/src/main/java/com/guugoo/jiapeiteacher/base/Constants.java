package com.guugoo.jiapeiteacher.base;

import android.util.Log;

/**
 * Created by gpw on 2016/9/18.
 * --加油
 */
public class Constants {
    public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化
    public static final String SHARED_NAME = "userInfo";
    public static boolean ballState = false;
    public static int MainActivityState = 0;
    public static int WorkActivityState = 0;

    /**
     * 分段打印出较长log文本
     * @param logContent  打印文本
     * @param showLength  规定每段显示的长度（AndroidStudio控制台打印log的最大信息量大小为4k）
     * @param tag         打印log的标记
     */
    public static void showLargeLog(String logContent, int showLength, String tag){
        if(logContent.length() > showLength){
            String show = logContent.substring(0, showLength);
            i(tag, show);

            /*剩余的字符串如果大于规定显示的长度，截取剩余字符串进行递归，否则打印结果*/
            String partLog = logContent.substring(showLength,logContent.length());
            showLargeLog(partLog, showLength, tag);
        }else{
            i(tag, logContent);
        }
    }

    /**
     * （-4爽约-3学员取消预约-2驾校取消预约, -1:教练取消，0: 已预约,1:进行中，2：已完成, 3: 已过期）
     */
    public static String getStatus(int i){
        String[] strings = new String[]{"爽约","学员取消预约","驾校取消预约","教练取消","已预约", "进行中", "已结束", "已过期"};
        return strings[i + 4];
    }

    public static void i(String tag, String msg)
    {
        if (isDebug)
            Log.i(tag, msg);
    }
}
