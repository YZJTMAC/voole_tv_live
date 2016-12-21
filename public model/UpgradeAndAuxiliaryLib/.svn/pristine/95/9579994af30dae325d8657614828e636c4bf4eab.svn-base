package com.voole.android.client.UpAndAu.upgrade;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;

import com.voole.android.client.UpAndAu.constants.AuxiliaryConstants;
import com.voole.android.client.UpAndAu.constants.VersionConstants;
import com.voole.android.client.UpAndAu.downloader.Utility;
import com.voole.android.client.UpAndAu.model.DataResult;
import com.voole.android.client.UpAndAu.model.UpdateInfo;
import com.voole.android.client.UpAndAu.model.parser.VooleData;
import com.voole.android.client.UpAndAu.util.Logger;
import com.voole.android.client.UpAndAu.util.StringUtil;
import com.voole.android.client.UpAndAu.util.UrlListUtil;

/**
 * 
 * @author LEE 升级帮助类
 * 
 */
public class AppUpgradeAuxiliaryer {
	private String checkVersionUrl;
	
	private UpgradeVersionCheckCallBack upgradeVersionCheckCallBack;
	private boolean isChecking = false;  
	
	public static int httpConnTimeOut=20000;
	public static int httpReadTimeOut=20000; 
	

	public interface UpgradeVersionCheckCallBack {
		public void onHasUpgrade(UpdateInfo updateInfo);

		public void onError(String errorMsg);

		public void onNOUpgrade(UpdateInfo updateInfo);
	}

	/**
	 * 升级助手 类
	 * 
	 * @param upgradeVersionCheckCallBack
	 *//*
	public AppUpgradeAuxiliaryer(
			UpgradeVersionCheckCallBack upgradeVersionCheckCallBack) {
		this.upgradeVersionCheckCallBack = upgradeVersionCheckCallBack;
	}
	*//**
	 *升级辅助类
	 * @param checkVersionUrl  包含所有请求参数的url
	 * @param upgradeVersionCheckCallBack 升级检测完成后的回调
	 *//*
	public AppUpgradeAuxiliaryer(String checkVersionUrl,UpgradeVersionCheckCallBack upgradeVersionCheckCallBack){
		this.checkVersionUrl = checkVersionUrl;
		this.upgradeVersionCheckCallBack = upgradeVersionCheckCallBack;
		
	}*/
	
	/**
	 *升级辅助类
	 *http://appinterface.voole.com/interface/appplatform_UpdateCheck_updateCheck.htm?&oemid=&appId=&packagename=&version=&hid=
	 * @param oemid 
	 * @param appId 
	 * @param packageName 
	 * @param version 
	 * @param hid 
	 * @param upgradeVersionCheckCallBack 升级检测完成后的回调
	 */
/*	public AppUpgradeAuxiliaryer(String oemid,String appId,String packageName,String version,String hid,UpgradeVersionCheckCallBack upgradeVersionCheckCallBack){
		
		if(!StringUtil.isNotNull(appId)) {
			appId = "-1";
		}
		Utility.log("YP -->> local apk version :"+version);
		
		this.checkVersionUrl = UrlListUtil.getVersionCheckUrl(oemid, packageName, hid, version, appId);
		Utility.log("YP -->> checkVersionUrl :"+checkVersionUrl);
		this.upgradeVersionCheckCallBack = upgradeVersionCheckCallBack;
		AuxiliaryConstants.oemId = oemid;
		AuxiliaryConstants.appId = appId;
		AuxiliaryConstants.packagename = packageName;
		AuxiliaryConstants.hid = hid;
	}*/
	
	/**
	 *升级辅助类
	 *http://appinterface.voole.com/interface/appplatform_UpdateCheck_updateCheck.htm?&oemid=&appId=&packagename=&version=&hid=
	 * @param host 
	 * @param oemid 
	 * @param appId 
	 * @param packageName 
	 * @param version 
	 * @param hid 
	 * @param upgradeVersionCheckCallBack 升级检测完成后的回调
	 */
	public AppUpgradeAuxiliaryer(String host,String oemid,String appId,String packageName,String version,String hid,UpgradeVersionCheckCallBack upgradeVersionCheckCallBack){
		if(StringUtil.isNotNull(host)) {
			AuxiliaryConstants.Host = host;
		}
		if(!StringUtil.isNotNull(appId)) {
			appId = "-1";
		}
		Utility.log("YP -->> local apk version :"+version);
		this.checkVersionUrl = UrlListUtil.getVersionCheckUrl(oemid, packageName, hid, version, appId);
		Utility.log("YP -->> checkVersionUrl :"+checkVersionUrl);
		this.upgradeVersionCheckCallBack = upgradeVersionCheckCallBack;
		AuxiliaryConstants.oemId = oemid;
		AuxiliaryConstants.appId = appId;
		AuxiliaryConstants.packagename = packageName;
		AuxiliaryConstants.hid = hid;
		
	}
	public AppUpgradeAuxiliaryer(String host,String oemid,String appId,String packageName,String version,String hid,String uid,String ip,UpgradeVersionCheckCallBack upgradeVersionCheckCallBack){
		if(StringUtil.isNotNull(host)) {
			AuxiliaryConstants.Host = host;
		}
		if(!StringUtil.isNotNull(appId)) {
			appId = "-1";
		}
		Utility.log("YP -->> local apk version :"+version);
		this.checkVersionUrl = UrlListUtil.getVersionCheckUrlForReport(oemid, packageName, hid, version, appId,uid);
		//UrlListUtil.getVersionCheckUrl(oemid, packageName, hid, version, appId);
		Utility.log("YP -->> checkVersionUrl :"+checkVersionUrl);
		this.upgradeVersionCheckCallBack = upgradeVersionCheckCallBack;
		AuxiliaryConstants.oemId = oemid;
		AuxiliaryConstants.appId = appId;
		AuxiliaryConstants.packagename = packageName;
		AuxiliaryConstants.hid = hid;
		AuxiliaryConstants.uid = uid;
		AuxiliaryConstants.localIp = ip;
	}
	
	public AppUpgradeAuxiliaryer(String host,String oemid,String appId,String packageName,String version,String hid,String uid,String ip,String sn,UpgradeVersionCheckCallBack upgradeVersionCheckCallBack){
		if(StringUtil.isNotNull(host)) {
			AuxiliaryConstants.Host = host;
		}
		if(!StringUtil.isNotNull(appId)) {
			appId = "-1";
		}
		Utility.log("YP -->> local apk version :"+version);
		this.checkVersionUrl = UrlListUtil.getVersionCheckUrlForReport(oemid, packageName, hid, version, appId,uid);
		//UrlListUtil.getVersionCheckUrl(oemid, packageName, hid, version, appId);
		Utility.log("YP -->> checkVersionUrl :"+checkVersionUrl);
		this.upgradeVersionCheckCallBack = upgradeVersionCheckCallBack;
		AuxiliaryConstants.oemId = oemid;
		AuxiliaryConstants.appId = appId;
		AuxiliaryConstants.packagename = packageName;
		AuxiliaryConstants.hid = hid;
		AuxiliaryConstants.uid = uid;
		AuxiliaryConstants.localIp = ip;
		AuxiliaryConstants.sn = sn;
	}
	
	
	
	/**
	 *升级辅助类
	 *http://appinterface.voole.com/interface/appplatform_UpdateCheck_updateCheck.htm?&oemid=&appId=&packagename=&version=&hid=
	 * @param oemid 
	 * @param appId 
	 * @param packageName 
	 * @param version 
	 * @param hid 
	 * @param sn 
	 * @param localIp 
	 * @param upgradeVersionCheckCallBack 升级检测完成后的回调
	 */
	/*public AppUpgradeAuxiliaryer(String oemid,String appId,String packageName,String version,String hid,String sn,String localIp,UpgradeVersionCheckCallBack upgradeVersionCheckCallBack){
		
		if(!StringUtil.isNotNull(appId)) {
			appId = "-1";
		}
		this.checkVersionUrl = UrlListUtil.getVersionCheckUrl(oemid, packageName, hid, version, appId,sn,localIp);
		this.upgradeVersionCheckCallBack = upgradeVersionCheckCallBack;
		AuxiliaryConstants.oemId = oemid;
		AuxiliaryConstants.appId = appId;
		AuxiliaryConstants.packagename = packageName;
		AuxiliaryConstants.hid = hid;
		AuxiliaryConstants.sn = sn;
		AuxiliaryConstants.localIp = localIp;
	}*/
	
	
	/**
	 * 升级辅助类  检测地址汇报构造器
	 * @param oemid 
	 * @param appId 
	 * @param packageName 
	 * @param version 
	 * @param hid 
	 * @param uid
	 * @param upgradeVersionCheckCallBack 升级检测完成后的回调
	 */
/*	public AppUpgradeAuxiliaryer(String oemid,int appId,String packageName,
			String version,String hid,String uid,UpgradeVersionCheckCallBack upgradeVersionCheckCallBack){
		Utility.log("YP -->> local apk version :"+version);
		this.checkVersionUrl = UrlListUtil.getVersionCheckUrlForReport(oemid, packageName, hid, version, String.valueOf(appId),uid);
		Utility.log("YP -->> checkVersionUrl :"+checkVersionUrl);
		this.upgradeVersionCheckCallBack = upgradeVersionCheckCallBack;
		AuxiliaryConstants.oemId = oemid;
		AuxiliaryConstants.appId = String.valueOf(appId);
		AuxiliaryConstants.packagename = packageName;
		AuxiliaryConstants.hid = hid;
		AuxiliaryConstants.uid = uid;
		AuxiliaryConstants.checkVersionUrl = checkVersionUrl;
		AuxiliaryConstants.vooleVersion = version;
		
	}*/
	
	/**
	 * 
	 * 
	 * @description 设置 从 UpdateInfo实例中获取此
	 * @version 1.0
	 * @author LEE
	 * @param updateInfo 
	 * @date 2014-7-24 下午2:31:41 
	 * @update 2014-7-24 下午2:31:41
	 */
	public void setSendTime(UpdateInfo updateInfo) {
		if(updateInfo != null) {
			AuxiliaryConstants.sendtime = updateInfo.getSendtime();
		}
		
	}
	
	
	
	/**
	 * 检测版本更新
	 * 
	 * @return AppUpgradeHelper
	 */
	public AppUpgradeAuxiliaryer checkVersion() {
		AuxiliaryConstants.checkVersionUrlReRequest = "";
		Utility.log("YP -->> upgrade module current version :"+VersionConstants.currentVersion);
		if(!isChecking){
			startCheckVersion();
		}
		return this;
	}
	
	
	/////////////////////////////////////////////////////////////////
	private static  final  int SUCCESS = 1;
	private static final int FAILED = 0;
	Handler versionHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch(msg.what) {
				case SUCCESS:
					UpdateInfo updateInfo = (UpdateInfo) msg.obj;
					if(updateInfo ==null) {
						responseError("服务器接口异常");
						isChecking = false;
						return;
					}
					DataResult dataResult = updateInfo.getDataResult();
					if (dataResult != null) {
						String code = dataResult.getResultCode();
						if ("0".equals(code)) {
							AuxiliaryConstants.checkVersionUrlReRequest=AuxiliaryConstants.checkVersionUrlReRequest+"&sendtime="+updateInfo.getSendtime();
							//下载地址
							AuxiliaryConstants.downLoadUrl = updateInfo.getDownloadUrl();
							Logger.debug("AppUpgradeAuxiliaryer", "AuxiliaryConstants.downLoadUrl is "  + AuxiliaryConstants.downLoadUrl);
							Utility.log("YP -->> server apk version :"+updateInfo.getCurrentVersion());
							upgradeVersionCheckCallBack.onHasUpgrade(updateInfo);
							cancelTimerTask();
						} else {
							Utility.log("YP -->> server apk version :"+updateInfo.getCurrentVersion());
							upgradeVersionCheckCallBack.onNOUpgrade(updateInfo);
							cancelTimerTask();
						}
					}
					break;
				case FAILED:
					isChecking = false;
					responseError("服务器接口异常");
					break;
			}
			
		};
	};
	private class CheckVersionThread extends Thread {
		public CheckVersionThread() {
			isChecking = true;
		}
		public void run() {
			UpdateInfo updateInfo = null;
			Logger.debug("AppUpgradeAuxiliaryer","DataAsyncTask");
			Message msg = versionHandler.obtainMessage();
			try {
				String versionCheckUrl = checkVersionUrl;
				AuxiliaryConstants.checkVersionUrlReRequest = checkVersionUrl;
				Logger.debug("AppUpgradeAuxiliaryer", "checkVersionUrl is " + checkVersionUrl);
				updateInfo = VooleData.getInstance()
						.parseUpdateInfo(versionCheckUrl);
				
				msg.obj = updateInfo;
				msg.what=SUCCESS;
				versionHandler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				msg.obj = updateInfo;
				msg.what=FAILED;
				versionHandler.sendMessage(msg);
			}
		}
	}
	////////////////////////////////////////////////////////////////
	
	private void responseError(String error) {
		if(currentCheckCount>=checkUrlMaxCount) {
			cancelTimerTask();
			upgradeVersionCheckCallBack.onError(error);
			
		}
		
	}
	
	
	
	Timer timer = new Timer();
	TimerTask task;
	boolean isFirstTime=true;
	int currentCheckCount=0;
	private int checkUrlMaxCount=2;
	int delayTime=5000;
	int delayTime2=5000;
	public void startCheckVersion(){
		task = new TimerTask() {
			@Override
			public void run() {
					if(isFirstTime) {
						isFirstTime = false;
						CheckVersionThread checkVersionThread = new CheckVersionThread();
						checkVersionThread.start();
					}
					synchronized(this) {
						if(currentCheckCount<checkUrlMaxCount) {
							if(!isChecking) {
								currentCheckCount++;
								delayTime = delayTime2*currentCheckCount;
								CheckVersionThread checkVersionThread = new CheckVersionThread();
								checkVersionThread.start();
								Utility.log("reCheck..........................");
							}
						} else {
							cancelTimerTask();
						}
					}
			}
		};
		timer.schedule(task, 1, delayTime);
	}
	/**
	 * 
	 * 
	 * @description 当下载完成后 取消定时任务
	 * @version 1.0
	 * @author LEE
	 * @date 2013-12-11 下午7:12:54 
	 * @update 2013-12-11 下午7:12:54
	 */
	private void cancelTimerTask() {
		timer.cancel();
		task.cancel();
		isChecking = false;
		System.out.println("restart count:"+currentCheckCount);
	}
	/**
	 * 
	 * @param checkTimes
	 * @description 设置版本检测次数
	 * @version 1.0
	 * @author LEE
	 * @date 2014-10-31 上午11:12:48 
	 * @update 2014-10-31 上午11:12:48
	 */
	public void setUpgradeCheckTimes(int checkTimes) {
		checkUrlMaxCount = checkTimes;
	}
	/**
	 * 
	 * @param httpConnTimeOut
	 * @description 设置版本检测连接超时时间
	 * @version 1.0
	 * @author LEE
	 * @date 2014-10-31 上午11:18:34 
	 * @update 2014-10-31 上午11:18:34
	 */
	public  void setHttpConnTimeOut(int httpConnTimeOut) {
		AppUpgradeAuxiliaryer.httpConnTimeOut = httpConnTimeOut;
	}
	/**
	 * 
	 * @param httpReadTimeOut
	 * @description 设置http读超时时间
	 * @version 1.0
	 * @author LEE
	 * @date 2014-10-31 上午11:18:54 
	 * @update 2014-10-31 上午11:18:54
	 */
	public  void setHttpReadTimeOut(int httpReadTimeOut) {
		AppUpgradeAuxiliaryer.httpReadTimeOut = httpReadTimeOut;
	}
	
	
	
	////////////////////////////////////////////////////////

	/**
	 * 
	 * @return boolean
	 * @description 检测是否正在检测版本
	 * @version 1.0
	 * @author LEE
	 * @date 2015-11-4 上午11:06:02 
	 * @update 2015-11-4 上午11:06:02
	 */
	public boolean isChecking() {
	return isChecking;
	}

	
}
