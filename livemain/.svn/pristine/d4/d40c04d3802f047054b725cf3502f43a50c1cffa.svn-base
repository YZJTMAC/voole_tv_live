package com.gntv.tv.receiver;

import com.gntv.tv.Config;
import com.gntv.tv.common.ap.ProxyManager;
import com.gntv.tv.common.utils.LocalManager;
import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.view.StartupActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootStartPlayReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		LogUtil.i("BootStartPlayReceiver/" + context.getPackageName() + "/boot start setting/" + LocalManager.GetInstance().getLocal("autostart", "0"));
		if ("1".equals(LocalManager.GetInstance().getLocal("autostart", "0"))) {
			gotoPlay(context);
		}
		if("1".equals(Config.GetInstance().getBootStartP2P())){
			ProxyManager.GetInstance().startP2pProxy();
		}
	}
	
	private void gotoPlay(Context context){
		Intent intent = new Intent(context, StartupActivity.class);
		LogUtil.i("BootStartPlayReceiver/to play/" + context.getPackageName());
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
}
