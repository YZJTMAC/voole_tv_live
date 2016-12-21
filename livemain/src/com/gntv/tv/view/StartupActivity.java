package com.gntv.tv.view;

import java.util.Map;

import com.gntv.tv.Config;
import com.gntv.tv.Const;
import com.gntv.tv.LauncherApplication;
import com.gntv.tv.R;
import com.gntv.tv.common.ap.AuthManager;
import com.gntv.tv.common.ap.ProxyInfo;
import com.gntv.tv.common.ap.ProxyManager;
import com.gntv.tv.common.ap.UrlList;
import com.gntv.tv.common.ap.UrlMap;
import com.gntv.tv.common.ap.User;
import com.gntv.tv.common.base.BaseActivity;
import com.gntv.tv.common.utils.LocalManager;
import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.common.utils.MD5Util;
import com.gntv.tv.common.utils.NetUtil;
import com.gntv.tv.model.base.VLService;
import com.gntv.tv.model.cache.CacheManager;
import com.gntv.tv.model.channel.ProgramManager;
import com.gntv.tv.model.channel.ResourceItem;
import com.gntv.tv.model.channel.TodayProgramInfo;
import com.gntv.tv.model.error.ErrorManager;
import com.gntv.tv.model.time.TimeManager;
import com.gntv.tv.receiver.NetStateReceiver.NetListener;
import com.gntv.tv.report.PageActionReport;
import com.gntv.tv.report.PageActionReport.Action;
import com.gntv.tv.report.PageActionReport.FocusType;
import com.gntv.tv.report.PageActionReport.ModuleType;
import com.gntv.tv.report.PageActionReport.PageType;
import com.gntv.tv.upgrade.UpgradeService;
import com.gntv.tv.view.base.TVAlertDialog;
import com.umeng.analytics.MobclickAgent;
import com.vad.sdk.core.VAdSDK;
import com.vad.sdk.core.base.MediaInfo;
import com.vad.sdk.core.base.interfaces.IAdStartupListener;
import com.vad.sdk.core.view.v30.ExitAdView.OnItemClickListener;
import com.voole.android.client.UpAndAu.constants.VersionConstants;
import com.voole.statistics.reback.StatisticsTerminalInfoBack;
import com.voole.statistics.report.ReportBaseInfo;
import com.voole.statistics.report.ReportManager;
import com.voole.statistics.service.StatisticsTerminalInfoService;
import com.voole.statistics.terminalinfo.AppReportInfo;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StartupActivity extends BaseActivity{
	private static final int START_AUTH_FAIL = 0;
	private static final int GET_USER_FAIL = 1;
	private static final int GET_USER_ERROR = 2;
	private static final int GET_URLLIST_FAIL = 3;
	private static final int GET_URLLIST_ERROR = 4;
	private static final int GET_CHANNEL_FAIL = 5;
	private static final int GET_CHANNEL_ERROR = 6;
	private static final int START_PROXY_FAIL = 7;
	private static final int GET_CHANNEL_SUCCESS = 8;
	private static final int GET_URLLIST_SUCCESS = 10;
	private static final int REPEAT_DOINIT = 9;
	
	private static String APK_MD5 = "apk_md5";
	private RelativeLayout fl_parent = null;
	private PlayerView playerView = null;
	private VProgressBar vpb = null;
	private TVMenuView menuView = null;
	
	private User user = null;
	//private UrlMap urlMap = null;
	private UrlList urlList = null;
	private TodayProgramInfo todayProgramInfo = null;
	
	private boolean mHasStartupAd = false;
	private boolean mAdEnd = false;
	private boolean mInitEnd = false;
	private boolean mFirstStart = true;
	
	private PopupWindow mExitDialog = null, mDetectViewPopuWindow;//退出对话框
	private View mExitDialogView = null;//退出对话框View
	private LinearLayout exitAdView = null; //导流对话框
	private boolean isInit = true;
	private int successCount = 0;
	private volatile boolean isCancelError = false;  //是否取之前一个init的消错误框
	private String channelID = null; //第三方应用启动apk传进来
	private boolean isHasUpgrade = false;//是否有升级
	private LauncherApplication launcherApplication = null;
	
	@Override
	protected void doHandleMessage(Message msg) {
		switch (msg.what) {
			case START_AUTH_FAIL:
				LogUtil.e("StartupActivity----->mHandler--->START_AUTH_FAIL");
				showError(ErrorManager.ERROR_START_AUTH);
				break;
			case GET_USER_FAIL:
				LogUtil.e("StartupActivity----->mHandler--->GET_USER_FAIL");
				showError(ErrorManager.ERROR_GET_USER_NULL);
				break;
			case GET_USER_ERROR:
				LogUtil.e("StartupActivity----->mHandler--->GET_USER_ERROR");
				showError(ErrorManager.ERROR_GET_USER, user.getStatus(), user.getResultDesc());
				break;
			case GET_URLLIST_FAIL:
				LogUtil.e("StartupActivity----->mHandler--->GET_URLLIST_FAIL");
				showError(ErrorManager.ERROR_URLLIST_NULL);
				break;
			case GET_URLLIST_ERROR:
				LogUtil.e("StartupActivity----->mHandler--->GET_URLLIST_ERROR");
				showError(ErrorManager.ERROR_URLLIST, urlList.getStatus(), urlList.getResultDesc());
				break;
			case GET_CHANNEL_FAIL:
				LogUtil.e("StartupActivity----->mHandler--->GET_CHANNEL_NULL");
				showError(ErrorManager.ERROR_GET_CHANNEL_NULL);
				break;
			case GET_CHANNEL_ERROR:
				LogUtil.e("StartupActivity----->mHandler--->GET_CHANNEL_ERROR");
				showError(ErrorManager.ERROR_GET_CHANNEL, todayProgramInfo.getStatus(), todayProgramInfo.getResultDesc());
				break;
			case START_PROXY_FAIL:
				LogUtil.e("StartupActivity----->mHandler--->START_PROXY_FAIL");
				showError(ErrorManager.ERROR_START_PROXY);
				break;
/*			case GET_CHANNEL_SUCCESS:
				LogUtil.i("StartupActivity----->mHandler--->GET_CHANNEL_SUCCESS");
				startUpgradeCheck();
				mInitEnd = true;
//				setPlayerView();
				gotoPlay();
				initTerminalReport();
				initPageActionReport();
				//进行开机启动页面汇报
				PageActionReport.GetInstance().reportPageAction(PageType.StartPage);
				isInit = true;
				successCount++;
				break;*/
			case GET_URLLIST_SUCCESS:
				LogUtil.i("StartupActivity----->mHandler--->GET_URLLIST_SUCCESS");
				startUpgradeCheck();
				mInitEnd = true;
				setPlayerView();
				gotoPlay();
				initTerminalReport();
				initPageActionReport();
				//进行开机启动页面汇报
				PageActionReport.GetInstance().reportPageAction(PageType.StartPage);
				isInit = true;
				successCount++;
				break;
			case REPEAT_DOINIT:
				LogUtil.i("StartupActivity----->mHandler--->REPEAT_DOINIT");
				doInit(false);
				break;
			default:
				break;
		}
	}
	
	private void showError(String errorCode){
		String errorMsg = ErrorManager.GetInstance().getErrorMsg(errorCode).getErroeMessageAndCode();
		showErrorDialog(errorMsg);
	}
	
	private void showError(String errorCode, String errorCodeOther, String messageOther){
		String errorMsg = ErrorManager.GetInstance().getErrorMsg(errorCode, errorCodeOther, messageOther).getErroeMessageAndCode();
		showErrorDialog(errorMsg);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LogUtil.i("StartupActivity--->onCreate");
		super.onCreate(savedInstanceState);
		getData(getIntent());
		setContentView(R.layout.main);
		fl_parent = (RelativeLayout)findViewById(R.id.layout);
		LogUtil.i("StartupActivity--->onCreate--->playerView--->init");
		String code = Config.GetInstance().getVooleVersion();
		String appid = Config.GetInstance().getAppid();
		String isShowTimer = Config.GetInstance().getIsShowTimer();
		playerView = new PlayerView(this,code,appid,isShowTimer,Config.GetInstance().getOemType());
		RelativeLayout.LayoutParams pv_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		playerView.setLayoutParams(pv_params);
		playerView.setApkPacks(Config.GetInstance().getApkPacks());
		playerView.setDownloadUrl(Config.GetInstance().getDownloadUrl());
//		playerView.setVisibility(View.GONE);
		playerView.setVisibility(View.INVISIBLE);
		fl_parent.addView(playerView);
//		playerView = (PlayerView)fiOndViewById(R.id.playerview);
		ProgramManager.GetInstance().GetInstance().setChannelType(Config.GetInstance().getChannelType());

		LogUtil.i("StartupActivity--->onCreate--->menuView--->init");
		menuView = new TVMenuView(this, playerView);
		menuView.setVersion(Config.GetInstance().getVooleVersion());
		menuView.setLayoutParams(pv_params);
		menuView.setVisibility(View.GONE);
		fl_parent.addView(menuView);
//		menuView = (TVMenuView)findViewById(R.id.menuview);
		
		LogUtil.i("StartupActivity--->onCreate--->VProgressBar--->init");
		vpb = (VProgressBar)findViewById(R.id.imageView1);

		LogUtil.i("StartupActivity--->showApkStartAd--->start");
		mHasStartupAd = VAdSDK.getInstance().showApkStartAd(fl_parent, iAdStartupListener);
		LogUtil.d("PlayerActivity------->onCreate----->mHasStartupAd--->"+mHasStartupAd);
		if(!mHasStartupAd){
			mAdEnd = true;
			fl_parent.setBackgroundResource(R.drawable.cs_tv_loadingpage);
			vpb.setVisibility(View.VISIBLE);
			//playerView.setVisibility(View.VISIBLE);
		}
		
		playerView.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				LogUtil.i("PlayerView获取了焦点hasFocus::"+hasFocus);
				if(mExitDialog!=null&&mExitDialog.isShowing()){
					LogUtil.i("PlayerView获取了焦点  showDialog");
					isBackClick = false;
					mExitDialog.dismiss();
					/*mExitDialog.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
					isBackClick = true;*/
				}
			}
		});
		launcherApplication = (LauncherApplication) getApplication();
		doInit(false);
	}
	
	/**
	 * 判断是否第三方应用启动apk
	 * @param intent
	 */
	private void getData(Intent intent){
		Bundle bundle = intent.getExtras();
		channelID = null;
		if(bundle != null){
			channelID = bundle.getString("channelID");
		}
		LogUtil.d("init channel id from other app/" + channelID);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		getData(intent);
		if(playerView != null && channelID != null){
			playerView.changeChannelById(channelID);
			mFirstStart = true;
		}
		LogUtil.d("StartupActivity--->onNewIntent--->" + channelID);
	}
	
	private void doInit(final boolean isGetUser){
		new Thread(new Runnable() {
			@Override
			public void run() {
//				checkAppFirstStart();
				isFirstStart();
				LogUtil.i("StartupActivity--->doInit--->开始启动auth");
				if(AuthManager.GetInstance().startAuth()){
					if(!ProxyManager.GetInstance().startProxy()){
						sendMessage(START_PROXY_FAIL);
						return;
					}
					//AuthManager.GetInstance().clear();
					if(isGetUser){
						if(launcherApplication!=null&&launcherApplication.auth!=null){
							LogUtil.i("StartupActivity--->doInit--->重新获取user");
							launcherApplication.auth.getUser();
						}
					}
					user = AuthManager.GetInstance().getUser();
					if(user!= null){
						initErrorCode(AuthManager.GetInstance().getUser().getOemid());
						if("0".equals(user.getStatus())){
							LogUtil.i("StartupActivity--->doInit--->启动auth成功");
							//urlMap = AuthManager.GetInstance().getUrlMap();
							urlList = AuthManager.GetInstance().getUrlList();
							if(urlList!=null){
								if("0".equals(urlList.getStatus()) || urlList.getStatus() == null || "1".equals(urlList.getStatus())){
									LogUtil.i("StartupActivity--->doInit--->获取动态入口成功");
									sendMessage(GET_URLLIST_SUCCESS);
									TimeManager.GetInstance().getServerTime();
									LogUtil.i("StartupActivity--->doInit--->getServerTime--->end");
									startVLService();
									LogUtil.i("StartupActivity--->doInit--->startVLService--->end");
									new Thread(new Runnable() {
										@Override
										public void run() {
											initAdAndUpdateStartAd();
											LogUtil.i("StartupActivity--->doInit--->initAdAndUpdateStartAd--->end");
											if(mExitDialogView!=null){
												handler.post(new Runnable() {
													@Override
													public void run() {
														initExitDialog();
													}
												});
											}
											LogUtil.i("StartupActivity--->doInit--->initExitDialog--->end");
										}
									}).start();
									//todayProgramInfo = ProgramManager.GetInstance().getTodayProgramInfo(ProgramManager.ProgramType.ALL);
									todayProgramInfo = playerView.initProgramInfo(channelID);
									LogUtil.i("StartupActivity--->doInit--->initProgramInfo--->end");
									if(todayProgramInfo!=null){
										/*if("0".equals(todayProgramInfo.getStatus()) && todayProgramInfo.getAssortList().size()>0){
											preUrl();
											LogUtil.i("StartupActivity--->doInit--->preUrl--->end");
											//sendMessage(GET_CHANNEL_SUCCESS);
										}else{
											sendMessage(GET_CHANNEL_ERROR);
										}*/
										if(!"0".equals(todayProgramInfo.getStatus()) || todayProgramInfo.getAssortList().size()<=0){
											sendMessage(GET_CHANNEL_ERROR);
										}
									}else{
										sendMessage(GET_CHANNEL_FAIL);
									}
								}else{
									sendMessage(GET_URLLIST_ERROR);
								}
							}else{
								sendMessage(GET_URLLIST_FAIL);
							}
						}else{
							sendMessage(GET_USER_ERROR);
						}
					}else{
						sendMessage(GET_USER_FAIL);
					}
				}else{
					sendMessage(START_AUTH_FAIL);
				}
			}
		}).start();
	}
	
	/**
	 * 判断是否是第一次启动
	 */
	private void isFirstStart(){
		String vooleVersion = LocalManager.GetInstance().getLocal("vooleVersion", "");
		String curVersion = Config.GetInstance().getVooleVersion();
		LogUtil.d("StartupActivity------->isFirstStart----->vooleVersion--->"+vooleVersion);
		LogUtil.d("StartupActivity------->isFirstStart----->curVersion--->"+curVersion);
		if (!vooleVersion.equals(curVersion)) {
			ProxyManager.GetInstance().exitProxy();
			AuthManager.GetInstance().exitAuth();
			ProxyManager.GetInstance().deleteProxyFiles();
			AuthManager.GetInstance().deleteAuthFiles();
			CacheManager.GetInstance().clear();
			LocalManager.GetInstance().saveLocal("vooleVersion", curVersion);
		}
		
	}
	
	private void preUrl(){
		String cid = playerView.getCurrentPlayId();
		LogUtil.d("preUrl----->cid------>" + cid);
		for(int i=0; i<todayProgramInfo.getAssortList().size(); i++){
			if(todayProgramInfo.getAssortList().get(i).getChannelList() != null && 
					todayProgramInfo.getAssortList().get(i).getChannelList().size() > 0){
				for (int j = 0; j < todayProgramInfo.getAssortList().get(i).getChannelList().size(); j++) {
					if(todayProgramInfo.getAssortList().get(i).getChannelList().get(j).getResourceList() != null
							&& todayProgramInfo.getAssortList().get(i).getChannelList().get(j).getResourceList().size() > 0){
						for (int j2 = 0; j2 < todayProgramInfo.getAssortList().get(i).getChannelList().get(j).getResourceList().size(); j2++) {
							ResourceItem r = todayProgramInfo.getAssortList().get(i).getChannelList().get(j).getResourceList().get(j2);
							if("0".equals(r.getIs3rd()) && (TextUtils.isEmpty(cid) || cid.equals(todayProgramInfo.getAssortList().get(i).getChannelList().get(j).getChannelId()))){
								AuthManager.GetInstance().preAuth(todayProgramInfo.getAssortList().get(i).getChannelList().get(j).getChannelId(), r.getResourceId(), r.getIs3rd(), r.getTracker(), r.getBkeUrl(), "2.0", "3.0"
										,r.getDataType(), r.getProto());
								return;
							}
						}
					}
				}
			}
		}
	}
	
	
	@Override
	protected void onStop() {
		LogUtil.d("StartupActivity-->onStop");
		super.onStop();
		//unRegisterScreenReceiver();
		//unRegisterUpgradeReceiver();
		if(mExitDialog != null && mExitDialog.isShowing()){
			mExitDialog.dismiss();
		}
	}
	
	private void checkAppFirstStart(){
		//String versionCodeInShare = LocalManager.GetInstance().getVersionCode();
		//String versionCodeInProp = Config.get(Key.vooleVersion);
		String md5 = LocalManager.GetInstance().getLocal(APK_MD5, "");
		String curMD5 = MD5Util.getApkMD5(getApplicationContext());
		//LogUtil.d("versionCodeInShare--->" + versionCodeInShare + "----versionCodeInProp--->" + versionCodeInProp);
		LogUtil.d("PlayerActivity------->checkAppFirstStart----->MD5--->"+md5);
		LogUtil.d("PlayerActivity------->checkAppFirstStart----->curMD5--->"+curMD5);
		if (!md5.equals(curMD5)) {
			
			LocalManager.GetInstance().deleteFiles(getApplicationContext());
			LocalManager.GetInstance().saveLocal(APK_MD5, curMD5);
		}
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtil.i("StartupActivity--------->onKeyDown----->"+keyCode);
		switch (keyCode) {
//		case KeyEvent.KEYCODE_DPAD_CENTER:
//		case KeyEvent.KEYCODE_ENTER:
//			return true;
		case KeyEvent.KEYCODE_ESCAPE:
		case KeyEvent.KEYCODE_BACK:
			if(menuView.onKeyDown(keyCode, event)){
				return true;
			}
			toggleExitDialog();
			PageActionReport.GetInstance().reportPageAction(PageType.ExitPage, null,
					null, null, Action.ExitKey);
			return true;
		case KeyEvent.KEYCODE_MENU:
			menuView.toggle();
			return true;
		/*case KeyEvent.KEYCODE_HOME:
//			doExit();
			break;*/
		case KeyEvent.KEYCODE_0:
		case KeyEvent.KEYCODE_1:
		case KeyEvent.KEYCODE_2:
		case KeyEvent.KEYCODE_3:
		case KeyEvent.KEYCODE_4:
		case KeyEvent.KEYCODE_5:
		case KeyEvent.KEYCODE_6:
		case KeyEvent.KEYCODE_7:
		case KeyEvent.KEYCODE_8:
		case KeyEvent.KEYCODE_9:
			menuView.hide();
			playerView.onKeyDown(keyCode, event);
			return true;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		LogUtil.i("StartupActivity--------->onKeyUp----->"+keyCode);
		switch(keyCode){
		case KeyEvent.KEYCODE_MENU:
			//UploadLogControler.onMenuKeyUp(oemid);
			break;
		}
		
		return super.onKeyUp(keyCode, event);
	}
	
	
	private void setPlayerView(){
		//playerView.setData(todayProgramInfo);
		playerView.showBufferViewWithText("系统正在初始化");
		playerView.preparePlay();
	}
	
	private synchronized void gotoPlay(){
		LogUtil.d("StartupActivity----->gotoPlay");
		if(!mHasStartupAd || (mHasStartupAd && mInitEnd && mAdEnd)){
			fl_parent.setBackgroundDrawable(null);
		/*	String useRatio_4_3 = ResourceManager.GetInstance().getResourceScale();
			playerView.changeAspectRatio(useRatio_4_3);*/
			vpb.setVisibility(View.GONE);
			playerView.setVisibility(View.VISIBLE);
			playerView.requestFocus();
			//setPlayerView();
			playerView.startPlay();
			menuView.setInitFinish(true);
		}else if(mHasStartupAd && mAdEnd){
			LogUtil.d("mHasStartupAd && mAdEnd----->");
			playerView.setVisibility(View.VISIBLE);
			playerView.showBufferViewWithText("系统正在初始化");
		}
	}
	
	IAdStartupListener iAdStartupListener = new IAdStartupListener() {
		@Override
		public void onAdEnd() {
			LogUtil.d("iAdStartupListener----->onAdEnd");
			mAdEnd = true;
			gotoPlay();
		}
	};
	private boolean isRegister;

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogUtil.i("StartupActivity--------->onDestroy");
		unRegisterScreenReceiver();
		unRegisterUpgradeReceiver();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		LogUtil.i("StartupActivity--------->onPause");
		MobclickAgent.onPageEnd("StartupActivity");
		MobclickAgent.onPause(this);
		mFirstStart = false;
		playerView.release();
		if(upgradeService != null){
			LogUtil.d("StartupActivity------->onPause()------>upgradeService.cancleDialog()");
			isHasUpgrade = upgradeService.cancleDialog();
		}
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		LogUtil.i("StartupActivity--------->onRestoreInstanceState");
		LogUtil.i("StartupActivity--------->重新启动应用");
		final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());  
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
        startActivity(intent); 
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		LogUtil.i("StartupActivity--------->onSaveInstanceState");
		outState.putString("channelID", null);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onStart() {
		LogUtil.i("StartupActivity--------->onStart");
		super.onStart();
		registerScreenReceiver();
		registerUpgradeReceiver();
		if(mReceiver!=null){
			mReceiver.setListener(new NetListener() {
				
				@Override
				public void showExitDialog() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void disconnect() {
					if (upgradeService != null) {
						isForceStopUpgrade = upgradeService.cancleDialog();
					}
					isInit = false;
				}
				
				@Override
				public void connect() {
					if (isForceStopUpgrade) {
						if(mDetectViewPopuWindow != null){
							if(!mDetectViewPopuWindow.isShowing()){
								upgradeService.reCheck();
							}
						}else {
							upgradeService.reCheck();
						}
					}
					if(!isInit&&successCount==0){
						isCancelError = true;
						handler.removeCallbacksAndMessages(null);
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									Thread.sleep(1000);
									if(!isInit&&successCount==0){
										sendMessage(REPEAT_DOINIT);
									}
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								
							}
						}).start();
					}
				}
			});
		}
	}

	@Override
	protected void onResume() {
		LogUtil.i("StartupActivity--------->onResume");
		super.onResume();
		MobclickAgent.onPageStart("StartupActivity");
		MobclickAgent.onResume(this);
		if(!mFirstStart){
			if(mExitDialog!=null && mExitDialog.isShowing()){
				mExitDialog.dismiss();
			}
			menuView.stopDetectAndHide();
			LogUtil.i("StartupActivity--------->onResume--->preparePlay");
			playerView.preparePlay(true);
			if(isHasUpgrade){
				if(upgradeService != null){
					if(NetUtil.isNetWorkAvailable(StartupActivity.this)){
						upgradeService.reCheck();
					}
				}
			}
		}
	}
	
	private void doExit(){
		playerView.release();
		playerView.showBufferViewWithText("正在退出应用");
		new Thread(new Runnable() {
			@Override
			public void run() {
				AuthManager.GetInstance().exitAuth();
				ProxyManager.GetInstance().exitProxy();
				stopVLService();
				stopUpgradeCheck();
				//进入桌面进行应用退出
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				try {
					Thread.sleep(500); // wait 500 for report status
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
				ReportManager.getInstance().release();
				LogUtil.i("StartupActivity--->doExit--->杀死当前进程");
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		}).start();
	}
	
	private void startVLService(){
		Intent timeIntent = new Intent(this,VLService.class);
		startService(timeIntent); //启动获取时间服务
	}
	
	private void stopVLService(){
		Intent timeIntent = new Intent(this,VLService.class);
		stopService(timeIntent); //停止获取时间服务
	}
	
	private void doTerminalReport() {
		new Thread() {
			public void run() {
				StatisticsTerminalInfoService.getInstance().transferAppinfoReportMessage(
								"", "",	"", new StatisticsTerminalInfoBack() {
									@Override
									public void reBack(String result) {
										LogUtil.d("StartupActivity StatisticsTerminalInfoBack-- >" + result);
									}
								});
			};
		}.start();
	}
	
	private void initTerminalReport() {
/*		new Thread() {
			public void run() {
				String reportUrl = Config.GetInstance().getTerminalReportUrl();
				String appId = Config.GetInstance().getAppid();
				String appversion = Config.GetInstance().getVooleVersion();
				String chip = "";
				if (!TextUtils.isEmpty(AuthManager.GetInstance().getUrlList().getTerminalInfoStat())) {
					String url = AuthManager.GetInstance().getUrlList().getTerminalInfoStat();
					if (url.contains("?")) {
						url = url.substring(0, url.indexOf("?"));
						if(!TextUtils.isEmpty(url)){
							reportUrl = url;
						}
					}
				}
				LogUtil.i("StartupActivity--->initTerminalReport--->reportUrl::"+reportUrl);
				if (!TextUtils.isEmpty(reportUrl)) {
					LogUtil.d("StartupActivity -- >  call init StatisticsTerminalInfoService");
					LogUtil.d(reportUrl + "," + appId + "," + appversion);
					StatisticsTerminalInfoService.getInstance().initBasicData(
							reportUrl, appId, appversion, chip, StartupActivity.this, new StatisticsTerminalInfoBack() {
								@Override
								public void reBack(String result) {
									LogUtil.d("StartupActivity initBasicData StatisticsTerminalInfoBack result-- > " + result);
									if (!TextUtils.isEmpty(result) && result.contains("0")) {
										doTerminalReport();
									}
								}
							}, null);
				}

			};
		}.start();*/
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String reportUrl = Config.GetInstance().getTerminalReportUrl();
		/*		if (!TextUtils.isEmpty(getUrlMap()!=null?getUrlMap().get("terminal_info_stat"):null)) {
					String url = getUrlMap().get("terminal_info_stat");
					if (url.contains("?")) {
						url = url.substring(0, url.indexOf("?"));
						if(!TextUtils.isEmpty(url)){
							reportUrl = url;
						}
					}
				}*/
				
				if (!TextUtils.isEmpty(AuthManager.GetInstance().getUrlList().getTerminalInfoStat())) {
					String url = AuthManager.GetInstance().getUrlList().getTerminalInfoStat();
					if (url.contains("?")) {
						url = url.substring(0, url.indexOf("?"));
						if(!TextUtils.isEmpty(url)){
							reportUrl = url;
						}
					}
				}
				LogUtil.i("StartupActivity--->initTerminalReport--->reportUrl::"+reportUrl);
				User user = AuthManager.GetInstance().getUser();
				if(user!=null){
					ReportManager.getInstance().setReportBaseInfo(new ReportBaseInfo(user.getOemid(),
							Config.GetInstance().getAppid(), Config.GetInstance().getVooleVersion(),user.getHid(),user.getUid()));
					ReportManager.getInstance().setAppReportBaseUrl(reportUrl);
					AppReportInfo appReportInfo = new AppReportInfo();
					
					appReportInfo.setVooleAuth(user.getVersion());
					appReportInfo.setAuthCompile(user.getBuildtime());
					ProxyInfo proxyInfo = ProxyManager.GetInstance().getProxyInfo();
					if (proxyInfo != null) {
						LogUtil.e("proxyInfo---->NOT NULL");
						appReportInfo.setVooleAgent(proxyInfo.getVersion());
						appReportInfo.setAgentCompile(proxyInfo.getBuildTime());
						appReportInfo.setAgentLibs(proxyInfo.getM3u8Version());
					} else {
						LogUtil.e("proxyInfo----> NULL");
					}
					appReportInfo.setUpgradeVersion(VersionConstants.currentVersion);
					//appReportInfo.setTerminaLogVersion(com.voole.logsdk.config.LogConfig.logVersionCode);
					appReportInfo.setApkStartType("");
					//appReportInfo.setIsAuth(isauth);
					/*if (ottResult != null) {
						appReportInfo.setSn(ottResult.getDeviceId());
					}*/
					//appReportInfo.setSdkModuleVersion("");
					//appReportInfo.setSdkModuleType("");
					Log.i("ha",""+ Config.GetInstance().isScan());
					ReportManager.getInstance().appReport(appReportInfo, Config.GetInstance().isScan());
				}
			}
		}).start();

	}
	
	/*private Map<String,String> getUrlMap(){
		UrlMap urlMap = AuthManager.GetInstance().getUrlMap();
		return urlMap!=null?urlMap.getUrlMap():null;
	}*/
	
	//初始化用户行为统计上报
	private void initPageActionReport(){
		String appId = Config.GetInstance().getAppid();
		String appversion = Config.GetInstance().getVooleVersion();
		PageActionReport.GetInstance().init(appversion, appId);
	}

	private boolean isForceStopUpgrade = false;
	private boolean upGradeFlag = false;
	private void startUpgradeCheck(){
		if (!upGradeFlag) {
			new Thread() {
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//String baseUrl = getUrlMap()!=null?getUrlMap().get("ad_upgrade_url"):null;
					String baseUrl = AuthManager.GetInstance().getUrlList().getUpgradeCheck();
					String versionCode = Config.GetInstance().getVooleVersion();
					String appid = Config.GetInstance().getAppid();
					String oemid = AuthManager.GetInstance().getUser().getOemid();
					String uid = AuthManager.GetInstance().getUser().getUid();
					String hid = AuthManager.GetInstance().getUser().getHid();
					Intent i = new Intent();
					i.setClass(StartupActivity.this, UpgradeService.class);
					i.putExtra("baseUrl", baseUrl);
					i.putExtra("appId", appid);
					i.putExtra("versionCode", versionCode);
					i.putExtra("oemid", oemid);
					i.putExtra("uid", uid);
					i.putExtra("hid", hid);
					startService(i);
					bindService(i, conn, Context.BIND_AUTO_CREATE);
				};
			}.start();
		}
	}
	
	private void stopUpgradeCheck(){
		if (upGradeFlag) {
			LogUtil.d("StartupActivity------->stopUpgradeCheck---->unbindService(conn)");
			unbindService(conn);
		}
		Intent i = new Intent();
		i.setClass(this, UpgradeService.class);
		stopService(i);
	}
	
	/**
	 * 退出对话框开关
	 */
	boolean isBackClick = true;
	private void toggleExitDialog(){
		if(mExitDialog != null){
			if(mExitDialog.isShowing()){
				mExitDialog.dismiss();
			}else {
				setExitDialogAutoStartView();
				mExitDialog.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
				isBackClick = true;
			}
		}else {
			initExitDialog();
			toggleExitDialog();
		}
	}
	/**
	 * 初始化退出对话框
	 */
	private synchronized void initExitDialog(){
		
		if(mExitDialogView==null){
			mExitDialogView = LayoutInflater.from(this).inflate(R.layout.exit_layout, null);
			
			exitAdView = (LinearLayout) mExitDialogView.findViewById(R.id.dialog_exit_layout_ad_ll);
			mExitDialog = new PopupWindow(mExitDialogView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			mExitDialog.setBackgroundDrawable(new BitmapDrawable());
			mExitDialog.setFocusable(true);
			
			mExitDialogView.findViewById(R.id.dialog_exit_layout_sure_bt).requestFocus();
			mExitDialogView.findViewById(R.id.dialog_exit_layout_sure_bt).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mExitDialog.dismiss();
					doExit();
				}
			});
			mExitDialogView.findViewById(R.id.dialog_exit_layout_cancel_bt).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					isBackClick = false;
					mExitDialog.dismiss();
					PageActionReport.GetInstance().reportPageAction(PageType.ExitPage, ModuleType.ExitGuide,
							null, null, Action.OKKey);
				}
			});
			mExitDialog.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss() {
					if(isBackClick){
						PageActionReport.GetInstance().reportPageAction(PageType.PlayPage, ModuleType.ExitGuide,
								null, null, Action.ExitKey);
					}
				}
			});
			
			mExitDialogView.findViewById(R.id.dialog_exit_layout_cancel_bt).setNextFocusDownId(R.id.dialog_exit_layout_cancel_bt);
			
		}
	
		LogUtil.i("initExitDialog--->isRegister::"+isRegister+",exitAdView::"+exitAdView);
		if(isRegister&&exitAdView!=null){
			LogUtil.i("显示导流信息");
			VAdSDK.getInstance().showExitAd(exitAdView, "", "", "", "",new OnItemClickListener() {
				
				@Override
				public void onItemClick(int position, MediaInfo mediaInfo) {
					LogUtil.i("StartupActivity--->initExitDialog--->position::"+position);
					PageActionReport.GetInstance().reportPageAction(PageType.ExitPage, ModuleType.ExitGuide,
							"EGF"+(position+1), FocusType.GuideSwitch, Action.OKKey);
				}
			});
		}
		
	}
	private synchronized void showErrorDialog(String errorStr){
		ConnectivityManager mConnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
		if (mNetworkInfo != null) { 
			boolean available = mNetworkInfo.isAvailable(); 
			LogUtil.i("StartupActivity--->showErrorDialog--->isCancelError::"+isCancelError+",available::"+available);
			//TVToast.show(this, "StartupActivity--->showErrorDialog--->isCancelError::"+isCancelError+",available::"+available, 1000);
			if(available&&!isCancelError){
				new TVAlertDialog.Builder(this).setCancelable(false).setTitle(errorStr)
				 .setNegativeButton("故障检测", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						showDetectView();//打开故障检测界面
						
					}
				})
				 .setPositiveButton("确定", new AlertDialog.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						doExit();
					}
				}).create().show();
			}
			isCancelError = false;
			LogUtil.i("StartupActivity--->showErrorDialog--->showover--->isCancelError::"+isCancelError);
		} 
	}
	
	private void initErrorCode(final String oemid){
		new Thread(new Runnable() {
			@Override
			public void run() {
				ErrorManager.GetInstance().init(oemid, Config.GetInstance().getQq());
			}
		}).start();
	}
	
	/**
	 * 设置退出对话框是否开启启动按钮的显示与否
	 */
	private void setExitDialogAutoStartView(){
		String startcount_str = LocalManager.GetInstance().getLocal("startcount", "0");
		mExitDialogView.findViewById(R.id.dialog_exit_layout_sure_bt).requestFocus();
		int startcount_int = Integer.valueOf(startcount_str);
		final TextView autoStartTv = (TextView) mExitDialogView.findViewById(R.id.dialog_exit_layout_autostart_bt);
		autoStartTv.setFocusable(true);
		autoStartTv.setText("开机启动");
		autoStartTv.setCompoundDrawablePadding(5);
		final Drawable right_icon_off = getResources().getDrawable(R.drawable.auto_start_button_off_light);
		final Drawable right_icon_on = getResources().getDrawable(R.drawable.auto_start_button_on_light);
		if("0".equals(LocalManager.GetInstance().getLocal("autostart", "0"))){
			right_icon_off.setBounds(0, 0, right_icon_off.getMinimumWidth(), right_icon_off.getMinimumHeight());  
			autoStartTv.setCompoundDrawables(null, null, right_icon_off, null);
		}else {
			right_icon_on.setBounds(0, 0, right_icon_on.getMinimumWidth(), right_icon_on.getMinimumHeight());  
			autoStartTv.setCompoundDrawables(null, null, right_icon_on, null);
		}
		autoStartTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if("0".equals(LocalManager.GetInstance().getLocal("autostart", "0"))){
					right_icon_on.setBounds(0, 0, right_icon_on.getMinimumWidth(), right_icon_on.getMinimumHeight());  
					autoStartTv.setCompoundDrawables(null, null, right_icon_on, null);
					LocalManager.GetInstance().saveLocal("autostart", "1");
				}else {
					right_icon_off.setBounds(0, 0, right_icon_off.getMinimumWidth(), right_icon_off.getMinimumHeight());  
					autoStartTv.setCompoundDrawables(null, null, right_icon_off, null);
					LocalManager.GetInstance().saveLocal("autostart", "0");
				}
			}
		});
		if("0".equals(LocalManager.GetInstance().getLocal("autostart", "0")) && startcount_int >= 3){
			autoStartTv.setVisibility(View.VISIBLE);
		}else {
			autoStartTv.setVisibility(View.INVISIBLE);
		}
	}
	
	private void initAdAndUpdateStartAd(){
		VAdSDK.getInstance().setAuthPort(AuthManager.GetInstance().getAuthPort());  //将端口改成从auth获得
		isRegister = VAdSDK.getInstance().register(Const.AD_APK_ID);
		LogUtil.d("PlayerActivity------->initAdAndUpdateStartAd----->isRegister--->"+isRegister);
		if(isRegister){
			VAdSDK.getInstance().updateApkStartAd(StartupActivity.this);
		}
	}

	private void showDetectView(){
		final DetectView mDetectView = new DetectView(this, this, Config.GetInstance().getVooleVersion(),user);
		mDetectViewPopuWindow = new PopupWindow(mDetectView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mDetectViewPopuWindow.setBackgroundDrawable(new BitmapDrawable());
		mDetectViewPopuWindow.setFocusable(true);
		mDetectViewPopuWindow.showAtLocation(fl_parent, Gravity.CENTER, 0, 0);
		mDetectViewPopuWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				mDetectView.stopDetect();
				doInit(true);
			}
		});
		mDetectView.requestButtonFocus();
	}
	
	BroadcastReceiver screenReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
                LogUtil.d("Screen ON");
               
            }else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            	LogUtil.d("Screen off");
            	doExit();
            }
		}
	};
	
	private void registerScreenReceiver(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(screenReceiver, filter);
	}
	
	private void unRegisterScreenReceiver(){
		if(screenReceiver!=null){
			try {
				unregisterReceiver(screenReceiver);
			} catch (Exception e) {
				LogUtil.e("unRegisterScreenReceiver---->"+e);
			}
		}
		
	}
	
	BroadcastReceiver upgradeReceiver = new BroadcastReceiver(){
		public void onReceive(Context context, Intent intent) {
			LogUtil.i("StartupActivity---->upgradeReceiver");
			playerView.release();
		}
	};
	
	private void registerUpgradeReceiver(){
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.voole.magictv.upgradereceiver");
		registerReceiver(upgradeReceiver, filter);
	}
	
	private void unRegisterUpgradeReceiver(){
		if(upgradeReceiver!=null){
			try {
				unregisterReceiver(upgradeReceiver);
			} catch (Exception e) {
				LogUtil.e("unRegisterUpgradeReceiver---->"+e);
			}
		}
	}
	
	private UpgradeService upgradeService;
	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			LogUtil.d("StartupActivity------->onServiceConnected");
			upgradeService = ((UpgradeService.MyBinder) service).getService();
			upGradeFlag = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			LogUtil.d("StartupActivity------->onServiceDisconnected");
			upgradeService = null;
			upGradeFlag = false;
		}

	};
	
}
