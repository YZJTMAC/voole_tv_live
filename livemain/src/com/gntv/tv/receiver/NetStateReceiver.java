package com.gntv.tv.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.common.utils.NetUtil;
import com.gntv.tv.model.error.ErrorManager;
import com.gntv.tv.view.base.TVAlertDialog;

public class NetStateReceiver extends BroadcastReceiver{
	private TVAlertDialog alertDialog = null;
	
	private NetListener listener = null;
	public void setListener(NetListener listener) {
		this.listener = listener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		LogUtil.d("NetStateReceiver-->onReceive-->start---action--->" + intent.getAction() );
		if(context == null){
			return;
		}
		if(!NetUtil.isNetWorkAvailable(context)){
			isConnect = false;
			showDialog(context);
			if(listener != null){
				listener.disconnect();
			}
		}else{
//			TVToast.show(context, "您好，网络已连接成功。", Toast.LENGTH_SHORT);
			isConnect = true;
			cancelDialog();
			if(listener != null){
				listener.connect();
			}
		}
		LogUtil.d("NetStateReceiver-->onReceive-->end");
	}
	
	private void showDialog(final Context context){
		if(alertDialog == null){
			if(!TVAlertDialog.isShowing){
				alertDialog = new TVAlertDialog.Builder(context)
				.setTitle("连接网络失败，请检查网络")
				.setCancelable(false)
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						cancelDialog();
					}
				})
				.setNegativeButton("设置", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						cancelDialog();
						Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
						context.startActivity(intent);
					}
				}).create();
				alertDialog.show();
			}
		}
	}
	
	private void cancelDialog(){
		if(alertDialog != null && alertDialog.isShowing()){
			alertDialog.dismiss();
			alertDialog = null;
		}
	}
	
	public interface NetListener{
		public void connect();
		public void disconnect();
		public void showExitDialog();//显示退出框
	}
	
	private static boolean isConnect = true;
	public static boolean isConnect() {
		return isConnect;
	}

}
