
 
package com.voole.android.client.UpAndAu.upgrade;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;


 /**
 *  新版本安装
 * @version 1.0
 * @author lvzhenwei
 * @date 2013-3-4 下午2:33:27 
 */

public class UpgradeManager {
	private static UpgradeManager instance = null;

	private UpgradeManager() {

	}

	/**
	 * 获取实例
	 * @return UpgradeManager
	 */
	public static UpgradeManager GetInstance() {
		if (instance == null) {
			instance = new UpgradeManager();
		}
		return instance;
	}
	
	/**
	 *  安装apk
	 * @param context 
	 * @param apkFile File
	 */
	public void install(Context context, File apkFile){
		Intent intent = new Intent(Intent.ACTION_VIEW);
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
    	context.startActivity(intent);    	
	}
	
}
