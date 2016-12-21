package com.gntv.tv;

import android.app.Activity;
import android.os.Bundle;

import com.gntv.tv.auth.ConfigAuth;
import com.gntv.tv.common.ap.AuthManager;
import com.gntv.tv.common.ap.IAuth;
import com.gntv.tv.common.ap.LevelManager;
import com.gntv.tv.common.ap.LevelManager.Level;
import com.gntv.tv.common.ap.LevelManager.LevelManagerListener;
import com.gntv.tv.common.ap.ProxyManager;
import com.gntv.tv.common.utils.LocalManager;
import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.model.cache.CacheManager;
import com.gntv.tv.proxy.ConfigProxy;
import com.gntv.tv.view.base.DisplayManager;
import com.umeng.analytics.MobclickAgent;
import com.vad.sdk.core.VAdSDK;
import com.voole.statistics.report.ReportManager;
import com.voole.tvutils.BaseApplication;

public class LauncherApplication extends BaseApplication{
	public IAuth auth = null;
	@Override
	public void onCreate() {
		LogUtil.setTag("live2.0");
		LogUtil.d("LauncherApplication-->onCreate");
		MobclickAgent.setDebugMode(true);
		LocalManager.GetInstance().init(getApplicationContext());
		auth = new ConfigAuth();
		AuthManager.GetInstance().init(auth, getApplicationContext(), true, "2.0");
		ProxyManager.GetInstance().init(new ConfigProxy(), getApplicationContext());
		CacheManager.GetInstance().setRootPath(getFilesDir().getAbsolutePath() + "/program");
		DisplayManager.GetInstance().init(getApplicationContext());
		ReportManager.getInstance().init(getApplicationContext()); //初始化汇报服务
		Config.GetInstance().init();
		VAdSDK.getInstance().init(getApplicationContext());
		//CrashHandler.getInstance().init(getApplicationContext()); //添加全局异常处理
		increaseStartCount();//记录启动次数
		initLevel();
		initLog();
		registerMyCallbacks();
		super.onCreate();
	}

	private void increaseStartCount() {//记录开启的次数,用于退出界面 控制开机自启按钮的显示与否
		String startcount_str = LocalManager.GetInstance().getLocal("startcount", "0");
		int startcount_int = Integer.valueOf(startcount_str);
		if(startcount_int < 4){
			startcount_int++;
			LocalManager.GetInstance().saveLocal("startcount", String.valueOf(startcount_int));
		}
		
	}
	
	private void initLog(){
		if("1".equals(Config.GetInstance().getCloseLog())){
			LogUtil.setDisplayLog(false);
		}
	}
	
	private void initLevel() {
		LevelManager.GetInstance().setLevelManagerListener(new LevelManagerListener() {
			@Override
			public boolean getSupport3D() {
				return true;
			}

			@Override
			public Level getLevel() {
				return LevelManager.GetInstance().getCurrentLevel();
			}

			@Override
			public String getInterfaceVersionCode() {
				return Config.GetInstance().getInterfaceVersion();
			}
		});
	}

	@Override
	public void onTerminate() {
		LogUtil.d("LauncherApplication-->onTerminate");
		super.onTerminate();
	}
	
	private void registerMyCallbacks(){
		registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks());
	}
	
	public class MyActivityLifecycleCallbacks implements ActivityLifecycleCallbacks { 
	    private int mForegroundActivities = 0;
	    private boolean hasSeenFirstActivity = false;
	    private boolean isChangingConfiguration = false;
	    @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
			LogUtil.d("MyActivityLifecycleCallbacks-->onActivityCreated-->" + activity);
			hasSeenFirstActivity =  false;
	    }
	  
	    @Override public void onActivityStarted(Activity activity) {
			LogUtil.d("MyActivityLifecycleCallbacks-->onActivityStarted-->" + activity);
	        mForegroundActivities++;
	        if (hasSeenFirstActivity && mForegroundActivities == 1 && !isChangingConfiguration) {
	        	new Thread(){
	        		public void run() {
	        			LogUtil.d("MyActivityLifecycleCallbacks-->onActivityStarted-->applicationDidEnterForeground-->");
	    	        	AuthManager.GetInstance().startAuth();
	    				ProxyManager.GetInstance().startProxy();
	        		};
	        	}.start();
	        }
	        hasSeenFirstActivity = true;
	        isChangingConfiguration = false;
	    }
	    
	    @Override public void onActivityResumed(Activity activity) {
			LogUtil.d("MyActivityLifecycleCallbacks-->onActivityResumed-->" + activity);
	    }
	  
	    @Override public void onActivityPaused(Activity activity) {
			LogUtil.d("MyActivityLifecycleCallbacks-->onActivityPaused-->" + activity);
	    }
	  
	    @Override public void onActivityStopped(Activity activity) {
			LogUtil.d("MyActivityLifecycleCallbacks-->onActivityStopped-->" + activity);
	    	mForegroundActivities--;
	        if (mForegroundActivities == 0) {
	        	new Thread(){
	        		public void run() {
	        			LogUtil.d("MyActivityLifecycleCallbacks-->onActivityStopped-->applicationDidEnterBackground-->");
	        			//AuthManager.GetInstance().clear();
	    	        	AuthManager.GetInstance().exitAuth();
	    				ProxyManager.GetInstance().exitProxy();
	        		};
	        	}.start();
	        }
	        isChangingConfiguration = activity.isChangingConfigurations();
	    }
	  
	    @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
			LogUtil.d("MyActivityLifecycleCallbacks-->onActivitySaveInstanceState-->" + activity);
	    }
	  
	    @Override public void onActivityDestroyed(Activity activity) {
			LogUtil.d("MyActivityLifecycleCallbacks-->onActivityDestroyed-->" + activity);
	    }
	}
	
}
