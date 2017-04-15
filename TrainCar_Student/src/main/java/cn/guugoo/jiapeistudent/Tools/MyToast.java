package cn.guugoo.jiapeistudent.Tools;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/5/14.
 */
public class MyToast {
    private static String oldMsg;
    protected static Toast toast   = null;
    private static long oneTime=0;
    private static long twoTime=0;

    public static void makeText(Context context, String s){
        if(toast==null){
            toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
            toast.show();
            oneTime= System.currentTimeMillis();
        }else{
            twoTime= System.currentTimeMillis();
            if(s.equals(oldMsg)){
                if(twoTime-oneTime> Toast.LENGTH_SHORT){
                    toast.show();
                }
            }else{
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }
        oneTime=twoTime;
    }


    public static void makeText(Context context, int resId){
        makeText(context, context.getString(resId));
    }

}
