package cn.com.caronwer.util;

import android.content.Context;

/**
 * 处理网络缓存
 * 
 * @author Kevin
 * 
 */
public class CacheUtils {

	/**
	 * 设置缓存
	 * 
	 * @param key
	 *            缓存表示: 可以使用url来标示一段json数据
	 * @param value
	 *            缓存内容是json数据  也可以将数据缓存到文件里面
	 *            SP中用url当key用json当value
	 *            当网页地址带有参数则以整体地址为key
	 *            文件量文件名为MD5（url）将url用MD5运算，文件内容为json
	 */
	public static void setCache(Context ctx, String key, String value) {
		SPtils.putString(ctx, key, value);
	}

	/**
	 * 获取缓存
	 * 
	 * @param key
	 * @return
	 */
	public static String getCache(Context ctx, String key) {
		return SPtils.getString(ctx, key, null);
	}

}
