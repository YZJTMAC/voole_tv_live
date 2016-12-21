package com.gntv.tv;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.view.StartupActivity;

public class CrashHandler implements UncaughtExceptionHandler{
	private volatile static CrashHandler instance = null;
	private Context context;
	
	private CrashHandler() {  
    }  
  
    public static CrashHandler getInstance() {
    	if (instance == null) {
    		synchronized(CrashHandler.class){
    			if(instance==null){
    				instance = new CrashHandler();
    			}
    		}
		}
        return instance;
    }  
    
    public void init(Context context) {
    	this.context = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    } 
    
    
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		LogUtil.e("CrashHandler--->uncaughtException");
		if(ex != null){
			
			StackTraceElement[] elements = ex.getStackTrace();
			for(StackTraceElement el:elements){
				LogUtil.e("msg::"+el.toString());
			}
			Intent intent = new Intent(context, StartupActivity.class);  
            PendingIntent restartIntent = PendingIntent.getActivity(    
                    context, 0, intent,    
                    Intent.FLAG_ACTIVITY_NEW_TASK);                                                 
            //退出程序                                          
            AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);    
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,    
                    restartIntent); // 1秒钟后重启应用   
            android.os.Process.killProcess(android.os.Process.myPid());
		}
	}
	
}
