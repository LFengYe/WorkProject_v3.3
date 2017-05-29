package cn.guugoo.jiapeistudent.Tools;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import cn.guugoo.jiapeistudent.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/7.
 */
public class Utils {

    private static SimpleDateFormat sf = null;
    private static int MY_PERMISSIONS_REQUEST_CODE = 0;

    // 判断是否有可用网
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /*获取目前时间*/
    public static String getCurrentDate() {
        Date d = new Date();
        sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(d);
    }

    /*时间戳转换成字符窜*/
    public static String getDateToString(long time) {
        Date d = new Date(time);
        sf = new SimpleDateFormat("yyyy年MM月dd日");
        return sf.format(d);
    }

    public static String getDateToString(String timeStr) {
        try {
            sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = sf.parse(timeStr);
            sf = new SimpleDateFormat("yyyy年MM月dd日");
            return sf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*将字符串转为时间戳*/
    public static long getStringToDate(String time) {
        sf = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static String getSubject(int i){
        String[] strings = new String[]{"科目一","科目二","科目三","科目四","科目五","科目六"};
        return strings[i-1];
    }

    public static String getStatus(int i){
        String[] strings = new String[]{"已预约","待支付","已结束","已取消","待审核"};
        return strings[i-1];
    }

    /**
     *
     * @param i :0:人物，1：场地
     * @return
     */

    public static DisplayImageOptions getOption(int i){
        DisplayImageOptions  options;
        if(i==0){
            options = new DisplayImageOptions.Builder()
                    .showStubImage(R.mipmap.people) // 设置图片下载期间显示的图片
                    .showImageForEmptyUri(R.mipmap.people) // 设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.mipmap.people) // 设置图片加载或解码过程中发生错误显示的图片
                    .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                    .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                    .build(); // 创建配置过得DisplayImageOption对象
            return options;
        }else if(i==1){
            options = new DisplayImageOptions.Builder()
                    .showStubImage(R.mipmap.place) // 设置图片下载期间显示的图片
                    .showImageForEmptyUri(R.mipmap.place) // 设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.mipmap.place) // 设置图片加载或解码过程中发生错误显示的图片
                    .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                    .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                    .build(); // 创建配置过得DisplayImageOption对象
            return options;
        }else {
            return null;
        }

    }



    public static Dialog proDialog(Activity activity) {
        Dialog proDialog = new Dialog (activity,R.style.myDialog);

        View v = View.inflate(activity, R.layout.view_dialog_progress, null);
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        int width = (int) (size.x * 0.7);
        int height = (int) (size.y * 0.1);
        proDialog.setContentView(v);
        proDialog.setCancelable(true);
        Window dialogWindow = proDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setGravity(Gravity.CENTER);
        lp.height = height;
        lp.width = width;
        dialogWindow.setAttributes(lp);
        return proDialog;
    }

    public static Dialog loginDialog(Activity activity) {
        Dialog loginDialog = new Dialog(activity,R.style.myDialog);

        View v = View.inflate(activity, R.layout.view_dialog_login, null);
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        int width = (int) (size.x * 0.7);
        int height = (int) (size.y * 0.1);
        loginDialog.setContentView(v);
        loginDialog.setCancelable(true);
        Window dialogWindow = loginDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setGravity(Gravity.CENTER);
        lp.height = height;
        lp.width = width;
        dialogWindow.setAttributes(lp);
        return loginDialog;
    }

    public static void applyPermission(Activity activity, String permissionStr) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity, permissionStr)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    permissionStr)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                        new String[]{permissionStr},
                        MY_PERMISSIONS_REQUEST_CODE);

                // MY_PERMISSIONS_REQUEST_CODE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
}
