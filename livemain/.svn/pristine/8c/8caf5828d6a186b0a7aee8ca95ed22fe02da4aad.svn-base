package com.gntv.tv.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gntv.tv.common.ap.AuthManager;
import com.gntv.tv.common.ap.ProxyManager;
import com.gntv.tv.common.utils.LogUtil;

public class HomeKeyBroadcastReceiver extends BroadcastReceiver {
    
    public static final String CHANGHONG_HOME = "com.changhong.system.systemkeyfor3rd"; //播放中点Home返回桌面
    public static final String CHANGHONG_ENTER = "com.changhong.3rd.startlauncher"; //播放中点Home主场景对话框点确认返回桌面 
    public static final int ChangHong_SYSTEM_HOME_KEY = 4124;  
    public static final int ChangHong_SYSTEM_PROGREM_SRC_KEY = 2001;  
    
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();  
		if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {  
			LogUtil.d("onReceive-->action:" + action);
			new Thread(new Runnable() {
				@Override
				public void run() {
					AuthManager.GetInstance().exitAuth();
					ProxyManager.GetInstance().exitProxy();
					
				}
			}).start();
			android.os.Process.killProcess(android.os.Process.myPid());
			
		}
		
	}

}
