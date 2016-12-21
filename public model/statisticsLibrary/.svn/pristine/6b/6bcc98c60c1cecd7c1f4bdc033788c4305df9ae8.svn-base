package com.voole.statistics.terminalinfo;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.voole.statistics.report.ReportBaseInfo;
import com.voole.statistics.report.ReportConfig;
import com.voole.statistics.report.ReportInfo;
import com.voole.statistics.report.ReportMessageUtil;
import com.voole.statistics.terminalinfo.util.DefaultDiscovery;
import com.voole.statistics.terminalinfo.util.HostBean;
import com.voole.statistics.terminalinfo.util.NetInfo;

public class TerminalinfoManager {

	private Handler handler = new Handler(Looper.getMainLooper()){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case APK_START_GET_DATA:
				if (isLiveReport) {
					Log.e(TAG, "TerminalinfoManager----START_GETIP------->");
					discovery = new DefaultDiscovery();
					discovery.setNetwork(NetInfo.getUnsignedLongFromIp(ip));
					discovery.execute();
					isFinish = false;
				}
				break;
			default:
				break;
			}
		};
	};

	private String urlAppLive = null;
	private String mac = null; 
	private String versionCode = null;
	private String packageNamed = null;
	private String systemTemp = null;
	private String product = null;
	private String sendTime = null;
	private String chip = null;
	private String sessid = null;
	private String ip = null;
	private NetInfo netInfo = null;

	private static final int APK_START_GET_DATA = 1;

	//	private Timer timer = null;
	//	private ReportLiveInfoTimeTask task = null;
	private DefaultDiscovery discovery = null;
	private static final String TAG ="VooleEpg2.0";

	protected static final long APP_LIVEINFO_TIME = 60*30*1000;

	private long time = 0L;
	private boolean isFinish = false;
	private boolean isLiveReport = true;
	private boolean isPause = false;

	//	private AppReportInfo reportInfo;

	private static TerminalinfoManager instance = new TerminalinfoManager();

	private TerminalinfoManager(){
	}

	public static TerminalinfoManager getInstance(){
		return instance;
	}

	//	public void setReportInfo(AppReportInfo reportInfo){
	//		this.reportInfo = reportInfo;
	//	}
	//	
	//	public AppReportInfo getReportInfo() {
	//		return reportInfo;
	//	}

	public void transferAppinfoReportMessage(final Context context,final String urlBase,final AppReportInfo reportInfo, boolean isLiveReport){
		this.isLiveReport = isLiveReport;
		new Thread(){
			public void run() {

				if (sendTime == null) {
					sendTime = String.valueOf(System.currentTimeMillis());
				}

				ReportBaseInfo baseInfo = reportInfo.getInfo();

				String urlAppReport = getReportUrl(ReportConfig.REPORT_TYPE_APP, context, urlBase, baseInfo.getOemid(), baseInfo.getAppId(), baseInfo.getAppversion(), reportInfo.getPackageName());

				if (TextUtils.isEmpty(sessid)) {
					try {
						byte[] hash = null;
						hash = MessageDigest.getInstance("MD5").digest((mac+sendTime).getBytes("UTF-8"));
						sessid = String.valueOf(hash);
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} 
				}
				String memery = GetContentUtil.getMemorySize();
				String gpu = GetContentUtil.getOpengl(context);
				String cpu = null;
				cpu = GetContentUtil.getNumberOfCores() + ":" + GetContentUtil
						.getSystemProperties("ro.product.cpu.abi","") + ":" + GetContentUtil.isLinker64();
				String xmlAppReport = CreateXmlUtil.createApkInfoXml(reportInfo, sessid, memery, gpu, cpu);
				//				PageMessageParse parse = new PageMessageParse();
				Log.e(TAG, "TerminalinfoManager---------urlAppReport------->" + urlAppReport);
				Log.e(TAG, "TerminalinfoManager---------xmlAppReport------->" + xmlAppReport);
				//				try {
				//					String result = parse.parse(urlAppReport, xmlAppReport);
				ReportInfo msg = new ReportInfo();
				msg.setUrl(urlAppReport);
				msg.setData(xmlAppReport);
				ReportMessageUtil.getInstance().addMessage(msg);
				transferLiveinfoReportMessage(context,urlBase,reportInfo);
			};
		}.start();
	}

	private void transferLiveinfoReportMessage(Context context, String urlBase,AppReportInfo reportInfo){
		netInfo = new NetInfo();
		if (sendTime == null) {
			sendTime = String.valueOf(System.currentTimeMillis());
		}
		ReportBaseInfo baseInfo = reportInfo.getInfo();
		urlAppLive = getReportUrl(ReportConfig.REPORT_TYPE_LIVE, context, urlBase, baseInfo.getOemid(), baseInfo.getAppId(), baseInfo.getAppversion(), reportInfo.getPackageName());
		netInfo.getIp();
		ip = netInfo.ip;
		handler.sendEmptyMessage(APK_START_GET_DATA);
	}


	private String getReportUrl(String type, Context context, String urlBase, String oemid, String appId, String appversion,String packageName){

		String reportUrl;

		StringBuffer httpUrlSendSB = new StringBuffer();
		if (null != urlBase && !"".equals(urlBase.trim())) {
			httpUrlSendSB.append(urlBase.trim() + "?");
		}
		httpUrlSendSB.append("action=" + type);

		httpUrlSendSB.append("&version=" + ReportConfig.appVersion);

		if (null != oemid && !"".equals(oemid.trim())) {
			httpUrlSendSB.append("&oemid=" + oemid.trim());
		}
		if (mac == null) {
			//			mac = StatisticsTerminalInfoService.getMacAddress();
			mac = GetContentUtil.getMac();
		}
		if (null != mac && !"".equals(mac.trim())) {
			httpUrlSendSB.append("&mac=" + mac.trim());
		}
		if (null != appId && !"".equals(appId.trim())) {
			httpUrlSendSB.append("&appid=" + appId.trim());
		}else {
			if(!TextUtils.isEmpty(oemid) && !TextUtils.isEmpty(packageNamed)){
				httpUrlSendSB.append("&appid=" + "-1");
			}
		}
		// VersionCode取值本地
		if (versionCode == null) {
			versionCode = GetContentUtil.getVersion(context);
		}
		if (null != versionCode && !"".equals(versionCode)) {
			httpUrlSendSB.append("&versioncode=" + versionCode.trim());
		}

		// VersionCode取值本地
		if (packageNamed == null) {
			if (TextUtils.isEmpty(packageName)) {
				packageNamed = GetContentUtil.getPackageName(context);
			} else {
				packageNamed = packageName;
			}
		}
		if (null != packageNamed && !"".equals(packageNamed.trim())) {
			httpUrlSendSB.append("&packagename=" + packageNamed.trim());
		}


		if (null != appversion && !"".equals(appversion.trim())) {
			httpUrlSendSB.append("&appversion=" + appversion.trim());
		} 
		// VersionCode取值本地
		if (systemTemp == null) {
			systemTemp = GetContentUtil.getSystem();
		}
		if (null != systemTemp && !"".equals(systemTemp.trim())) {
			httpUrlSendSB.append("&system=" + systemTemp.trim());
		}
		if (chip == null) {
			//			chip = GetContentUtil.getSystemProperties("ro.product.model","");
			//			chip = GetContentUtil.getSystemProperties("ro.hardware","");
			chip = GetContentUtil.getChip();
		}
		if (null != chip && !"".equals(chip.trim())) {
			httpUrlSendSB.append("&chip=" + chip);
		}

		if (product == null) {
			product = GetContentUtil.getModel();
		}
		if (null != product && !"".equals(product.trim())) {
			httpUrlSendSB.append("&product=" + product.trim());
		}

		httpUrlSendSB.append("&sendtime=" + sendTime);
		reportUrl = httpUrlSendSB.toString();
		Log.e(TAG, "TerminalinfoManager---ACTIONTYPE-->"+type+"----reportUrl------->" + reportUrl);
		return reportUrl;
	}

	public void release(){
		if (discovery != null) {
			discovery.onCancelledPortAsync();
			if (discovery.getStatus() != AsyncTask.Status.FINISHED) {
				discovery.cancel(true);
			}
		}
		//		cancelLiveInfoTimer();
		handler.removeCallbacksAndMessages(null);
		Log.e(TAG, "TerminalinfoManager---release-->");

	}

	public void pause() {
		Log.i(ReportConfig.REPORTTAG,"TerminalinfoManager---->appLiveReport---->pause");
		isPause = true;
		if (discovery != null && isLiveReport) {
			discovery.onCancelledPortAsync();
			if (discovery.getStatus() != AsyncTask.Status.FINISHED) {
				discovery.cancel(true);
			}
		}
		//		canReport = false;
		handler.removeMessages(APK_START_GET_DATA);
	}

	public void resume(){
		Log.i(ReportConfig.REPORTTAG,"TerminalinfoManager---->appLiveReport---->resume");
		if (isLiveReport && isPause) {
			final long currentTime = System.currentTimeMillis();
			if (currentTime - time >= APP_LIVEINFO_TIME ) {
				handler.sendEmptyMessage(APK_START_GET_DATA);
			} else {
				if (!isFinish) {
					new Thread(){
						public void run() {
							handler.sendEmptyMessageDelayed(APK_START_GET_DATA, APP_LIVEINFO_TIME - (currentTime - time));
						};
					}.start();
				}
			}
			isPause = false;
		}
	}

	public void transferLiveinfoReportMessage(final List<HostBean> beans){
		new Thread(){
			public void run() {

				Log.e(TAG, "TerminalinfoManager----TimerTask------->START");		
				StringBuffer intranetHost = new StringBuffer();
				for (HostBean bean : beans) {
					intranetHost.append(bean.ipAddress).append(":").append(bean.hardwareAddress.replaceAll(":", ""));
					if (bean.portsOpen != null && bean.portsOpen.size() != 0) {
						for (int i = 0; i < bean.portsOpen.size(); i++) {
							intranetHost.append("@").append(bean.portsOpen.get(i));
						}
					}
					intranetHost.append(";");
				}
				if (intranetHost.length() >= 1) {
					intranetHost.delete(intranetHost.length()-1, intranetHost.length());
				}
				//				String ip = GetContentUtil.getIp();
				String gatwayInfo = null;
				if (TextUtils.isEmpty(mac)) {
					mac = GetContentUtil.getMac();
				}
				if (TextUtils.isEmpty(ip)) {
					netInfo.getIp();
					ip = netInfo.ip;
				}
				if (!TextUtils.isEmpty(ip) && !TextUtils.isEmpty(mac)) {
					gatwayInfo = ip + ":" + mac;
				}
				if (TextUtils.isEmpty(sessid)) {
					try {
						byte[] hash = null;
						hash = MessageDigest.getInstance("MD5").digest((mac+sendTime).getBytes("UTF-8"));
						sessid = String.valueOf(hash);
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} 
				}
				String xmlLiveReport = CreateXmlUtil.createLiveInfoXml(sessid, intranetHost.toString(), gatwayInfo);
				ReportInfo msg = new ReportInfo();
				msg.setUrl(urlAppLive);
				msg.setData(xmlLiveReport);
				ReportMessageUtil.getInstance().addMessage(msg);
				time = System.currentTimeMillis();
				isFinish = true;
				handler.sendEmptyMessageDelayed(APK_START_GET_DATA, APP_LIVEINFO_TIME);
				//				PageMessageParse parse = new PageMessageParse();
				//				try {
				//					Log.e(TAG, "TerminalinfoManager----------urlAppLive------>" + urlAppLive);
				//					Log.e(TAG, "TerminalinfoManager---------xmlLiveReport------->" + xmlLiveReport);
				//					String result = parse.parse(urlAppLive, xmlLiveReport);
				//					if (null != result && !"".equals(result.trim())) {
				//						if (-1 != result.indexOf("\"status\":0")) {
				//							// 表示成功
				//							Log.e(TAG, "TerminalinfoManager---------LiveinfoReport------->SUCCESS");
				//						} else {
				//							// 失败
				//							Log.e(TAG, "TerminalinfoManager---------LiveinfoReport------->FAIL");
				//						}
				//					} else {
				//						// 失败
				//						Log.e(TAG, "TerminalinfoManager---------LiveinfoReport------->FAIL");
				//					}
				//					Log.e(TAG, "TerminalinfoManager---------LiveinfoReport------->result-->" + result);
				//				} catch (Exception e) {
				//					e.printStackTrace();
				//				} finally {
				//					time = System.currentTimeMillis();
				//					isFinish = true;
				//					handler.sendEmptyMessageDelayed(APK_START_GET_DATA, APP_LIVEINFO_TIME);
				//				}

			};
		}.start();
	}

	//	private class ReportLiveInfoTimeTask extends TimerTask{
	//		@Override
	//		public void run() {
	//				Log.e(TAG, "TerminalinfoManager----TimerTask------->START");
	//				StringBuffer intranetHost = new StringBuffer();
	//				for (HostBean bean : beans) {
	//					intranetHost.append(bean.ipAddress).append(":").append(bean.hardwareAddress);
	//					if (bean.portsOpen != null && bean.portsOpen.size() != 0) {
	//						for (int i = 0; i < bean.portsOpen.size(); i++) {
	//							intranetHost.append("@").append(bean.portsOpen.get(i));
	//						}
	//					}
	//					intranetHost.append(";");
	//				}
	//				if (intranetHost.length() >= 1) {
	//					intranetHost.delete(intranetHost.length()-1, intranetHost.length());
	//				}
	//				//				String ip = GetContentUtil.getIp();
	//				String gatwayInfo = null;
	//				if (TextUtils.isEmpty(mac)) {
	//					mac = StatisticsTerminalInfoService.getMacAddress();
	//				}
	//				if (TextUtils.isEmpty(ip)) {
	//					netInfo.getIp();
	//					ip = netInfo.ip;
	//				}
	//				if (!TextUtils.isEmpty(ip) && !TextUtils.isEmpty(mac)) {
	//					gatwayInfo = ip + ":" + mac;
	//				}
	//				if (TextUtils.isEmpty(sessid)) {
	//					try {
	//						byte[] hash = null;
	//						hash = MessageDigest.getInstance("MD5").digest((mac+sendTime).getBytes("UTF-8"));
	//						sessid = String.valueOf(hash);
	//					} catch (NoSuchAlgorithmException e) {
	//						e.printStackTrace();
	//					} catch (UnsupportedEncodingException e) {
	//						e.printStackTrace();
	//					} 
	//				}
	//				String xmlLiveReport = CreateXmlUtil.createLiveInfoXml(sessid, intranetHost.toString(), gatwayInfo);
	//				PageMessageParse parse = new PageMessageParse();
	//				try {
	//					Log.e(TAG, "TerminalinfoManager----------xmlLiveReport------>" + urlAppLive);
	//					Log.e(TAG, "TerminalinfoManager---------xmlLiveReport------->" + xmlLiveReport);
	//					String result = parse.parse(urlAppLive, xmlLiveReport);
	//					Log.e(TAG, "TerminalinfoManager----TimerTask------->END");
	//				} catch (IOException e) {
	//					e.printStackTrace();
	//				} catch (Exception e) {
	//					e.printStackTrace();
	//				}
	//		}
	//	}

}
