package com.voole.android.client.UpAndAu.constants;

/**
 * 地址转换
 * 
 * @version 1.0
 * @author LEE
 * @date 2013-4-7 下午12:26:53
 */
public class AuxiliaryConstants {

	/**
	 * 获取版本检测地址
	 * 
	 * @param host
	 * @return String
	 */
	public static String getVersionCheckUrl(String host) {
		StringBuffer bf = new StringBuffer();
		bf.append(host).append("?").append("appId=APPID")
				.append("&oemid=OEMID").append("&packagename=PACKAGENAME")
				.append("&version=VERSION")
				.append("&dependpackagename=DEPENDPN")
				.append("&dependpackegeversion=DEPENDPV").append("&uid=UID")
				.append("&hid=HID");
		return bf.toString();
	}

	/**
	 * 获得异常上报url
	 * 
	 * @param host
	 * @return String
	 */
	public static String getExceptionReportUrl(String host) {
		StringBuffer bf = new StringBuffer();
		bf.append(host).append("?").append("operation=SaveExceptionLog")
				.append("&oemid=OEMID").append("&packagename=PACKAGENAME")
				.append("&version=VERSION").append("&uid=UID")
				.append("&hid=HID").append("&appId=APPID");
		return bf.toString();
	}

	/**
	 * 意见反馈上报url
	 * 
	 * @param host
	 * @return String
	 */
	public static String getAdviceFeedBackReportUrl(String host) {
		StringBuffer bf = new StringBuffer();
		bf.append(host).append("?").append("operation=SaveFeedBackInfo")
				.append("&oemid=OEMID").append("&packagename=PACKAGENAME")
				.append("&version=VERSION").append("&uid=UID")
				.append("&hid=HID").append("&optionCode=OPTIONCODE")
				.append("&appId=APPID");
		return bf.toString();
	}

	/**
	 * 获取反馈选项url
	 * 
	 * @param host
	 * @return String
	 */
	public static String getFeedBackOptionsUrl(String host) {
		StringBuffer bf = new StringBuffer();
		bf.append(host).append("?").append("operation=GetFeedBackType")
				.append("&oemid=OEMID").append("&packagename=PACKAGENAME")
				.append("&version=VERSION").append("&uid=UID")
				.append("&hid=HID").append("&appId=APPID");
		return bf.toString();
	}

	/**
	 * 获取帮助信息url
	 * 
	 * @param host
	 * @return String
	 */
	public static String getAboutUrl(String host) {
		StringBuffer bf = new StringBuffer();
		bf.append(host).append("?").append("type=TYPE").append("&oemid=OEMID")
				.append("&packagename=PACKAGENAME").append("&version=VERSION")
				.append("&uid=UID").append("&hid=HID").append("&appId=APPID");
		return bf.toString();
	}

	/**
	 * 获取帮助信息url
	 * 
	 * @param host
	 * @return String
	 */
	public static String getHelpUrl(String host) {
		StringBuffer bf = new StringBuffer();
		bf.append(host).append("?").append("type=TYPE").append("&oemid=OEMID")
				.append("&packagename=PACKAGENAME").append("&version=VERSION")
				.append("&uid=UID").append("&hid=HID").append("&appId=APPID");
		return bf.toString();
	}

	/**
	 * 统计url
	 * 
	 * @param host
	 * @return String
	 */
	public static String getReportUrl(String host) {
		StringBuffer bf = new StringBuffer();
		bf.append(host).append("?").append("oemid=OEMID")
				.append("&packagename=PACKAGENAME").append("&uid=UID")
				.append("&hid=HID").append("&appId=APPID");
		return bf.toString();
	}
	
	/** 应用版本更新处理后广播 **/ 
	public static final String BROADCAST_APP_UPDATE = "com.voole.android.client.AppUpdateBroadcastReceiver";
	
	public static String apkCurrentVersion="";
	public static String oemId;
	public static String packagename;
	public static String appId;
	public static String hid;
	public static String sn;
	public static String localIp;
	public static String uid;
	public static String checkVersionUrl;
	//作为追加sendtime参数后，再次请求服务器的地址。
	public static String checkVersionUrlReRequest;
	public static String downLoadUrl;
	public static String vooleVersion;
	/**
	 * 2014.10.31下载地址 ip
	 */
	public static String downLoadIp;
	/***
	 * server current time
	 * 服务端返回数据的时间，作为唯一标示本次版本检测，下载成功失败 会把次参数再次提交服务器.
	 */
	public static String sendtime;
	/***
	 * 开始下载时，计数累加，直到下载成功或者失败，退出下载程序。
	 */
	public static int DLcount;
	
	//public static String Host="http://appinterface.voole.com";
	public static String Host="";
	//public static String HostLog="http://epglog.voole.com";
	//是否需要检测代理服务器速度。
	public static boolean isCheckProxySpeed=true;
	
	
	/**
	 * 
	 * @return string
	 * @description something
	 * @version 1.0
	 * @author LEE
	 * @date 2013-12-30 下午3:48:07 
	 * @update 2013-12-30 下午3:48:07
	 */
	public static String getVersionCheckUrl() {
		StringBuffer bf = new StringBuffer();
		bf.append(Host)
		.append("/interface/appplatform_UpdateCheck_updateCheck.htm")
		.append("?")
		.append("oemid=OEMID")
		.append("&packagename=PACKAGENAME")
		.append("&appId=APPID")
		.append("&version=VERSION")
		.append("&hid=HID")
		.append("&sn=SN")
		.append("&ip=IP")
		.append("&uid=UID")
		.append("&upgradeModelVn=").append(VersionConstants.currentVersion);
		
		
		return bf.toString();
	}

}
