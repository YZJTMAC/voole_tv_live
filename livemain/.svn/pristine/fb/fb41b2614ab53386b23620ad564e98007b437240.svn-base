package com.gntv.tv.common.base;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.gntv.tv.receiver.NetStateReceiver;

public abstract class BaseActivity extends Activity{
	protected NetStateReceiver mReceiver = null;
	private boolean registerNetReceiverFlag = true;
	/*private HomeKeyBroadcastReceiver mHomeKeyReceiver = new HomeKeyBroadcastReceiver();
	private boolean hasRigisterHomeKeyReceiver = false;*/
	
	
	protected abstract void doHandleMessage(Message msg);
	
	protected MyHandler handler = new MyHandler(this) {
		public void handleMessage(Message msg) {
			doHandleMessage(msg);
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(0x80000000, 0x80000000);
	}

	@Override
	protected void onPause() {
		super.onPause();
//		unregisterHomeKeyReceiver();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		registerHomeKeyReceiver();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(registerNetReceiverFlag){
			registerNetReceiver();
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if(registerNetReceiverFlag){
			unRegisterNetReceiver();
		}
	}
	
	private void registerNetReceiver(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mReceiver = new NetStateReceiver();
        this.registerReceiver(mReceiver, filter);
	}
	
	private void unRegisterNetReceiver(){
		this.unregisterReceiver(mReceiver);
	}
	
	/*private void registerHomeKeyReceiver(){
		IntentFilter hFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		hFilter.addAction(HomeKeyBroadcastReceiver.CHANGHONG_HOME);
		this.registerReceiver(mHomeKeyReceiver, hFilter);
		hasRigisterHomeKeyReceiver = true;
	}
	
	private void unregisterHomeKeyReceiver(){
		if(hasRigisterHomeKeyReceiver){
			this.unregisterReceiver(mHomeKeyReceiver);
			hasRigisterHomeKeyReceiver = false;
		}
	}*/
	
	protected void sendMessage(int what) {
		Message message = Message.obtain();
		message.what = what;
		handler.sendMessage(message);
	}

	protected void sendMessage(int what, int arg1) {
		Message message = Message.obtain();
		message.what = what;
		message.arg1 = arg1;
		handler.sendMessage(message);
	}

	protected void sendMessage(int what, int arg1, int arg2) {
		Message message = Message.obtain();
		message.what = what;
		message.arg1 = arg1;
		message.arg2 = arg2;
		handler.sendMessage(message);
	}

	protected void sendMessage(int what, Object obj) {
		Message message = Message.obtain();
		message.what = what;
		message.obj = obj;
		handler.sendMessage(message);
	}
	
	public static class MyHandler extends Handler {
		public final WeakReference<BaseActivity> mActivity;

		public MyHandler(BaseActivity activity) {
			mActivity = new WeakReference<BaseActivity>(activity);
		}
	}

}
