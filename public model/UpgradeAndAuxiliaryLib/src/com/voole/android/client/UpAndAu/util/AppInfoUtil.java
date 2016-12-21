package com.voole.android.client.UpAndAu.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 读取manifest 文件信息
 * 
 * @version 1.0
 * @author lvzhenwei
 * @date 2013-3-4 下午2:29:46
 * @update 2013-3-4 下午2:29:46
 */

public class AppInfoUtil {

	/**
	 *  获取版本号
	 * @param context
	 *            Context
	 * @return int类型数值
	 */
	public static int getAppVersionCode(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 获取版本名称
	 * @param context
	 *            Context
	 * @return String
	 */
	public static String getAppVersionName(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "-1.0";
	}

	/**
	 * 获取应用名称
	 * @param context
	 *            Context
	 * @return String
	 */
	public static String getAppName(Context context) {
		PackageManager pm = context.getPackageManager();
		CharSequence cs = pm.getApplicationLabel(context.getApplicationInfo());
		String appName = cs.toString();
		return appName;
	}

}
