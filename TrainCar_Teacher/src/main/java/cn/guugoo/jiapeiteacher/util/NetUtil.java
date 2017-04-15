package cn.guugoo.jiapeiteacher.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



/**
 * Created by gpw on 2016/7/31.
 * --加油
 */
public class NetUtil {
    public static boolean checkNetworkConnection(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        return !(info == null || !info.isAvailable());
    }
}
