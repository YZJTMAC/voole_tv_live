package com.voole.android.client.UpAndAu.util;

import java.util.List;
import java.util.Map;


import com.voole.android.client.UpAndAu.constants.AuxiliaryConstants;
import com.voole.android.client.UpAndAu.model.AppInfo;
import com.voole.android.client.UpAndAu.model.parser.VooleData;

/**
 * 获取操作url
 * 
 * @version 1.0
 * @author LEE
 * @date 2012-5-23 下午4:34:46
 * @version V1.1
 * @author wusong
 * @update 2013-4-16下午2:47:42
 */
public class UrlListUtil {
	/**
	 * defaultIntfaceUrl : 默认的提供所有业务接口的接口地址
	 */
	private static final String defaultIntfaceUrl = "http://119.97.153.67:35121/RegularVideoDataService/mobile/service.html?action=UrlList&epgid=100446&oemid=436";

	/**
	 * 获得返回结果的类型 text
	 */
	public static final String TEYP_TEXT = "Text";
	/**
	 * 获得返回结果的类型 html
	 */
	public static final String TEYP_HTML = "Html";
	/**
	 * versionCheckHostUrl : 版本检测host地址
	 */
	private static String versionCheckHostUrl;
	/**
	 * helpHostUrl : 帮助host地址
	 */
	private static String helpHostUrl;
	/**
	 * aboutHostUrl :关于host地址
	 */
	private static String aboutHostUrl;
	/**
	 * feedBackTypeHostUrl : 获取反馈类型host地址
	 */
	private static String feedBackTypeHostUrl;
	/**
	 * feedBackInfoHostUrl : 提交反馈host地址
	 */
	private static String feedBackInfoHostUrl;
	/**
	 * exceptionLogHostUrl : 异常提交host地址
	 */
	private static String exceptionLogHostUrl;
	/**
	 * reporHostUrl : 行为统计提交地址
	 */
	private static String reporHostUrl;

	/**
	 * 提供版本检测接口地址 获取本应用的检测地址
	 * 
	 * @param host
	 *            ex : http://www.voole.com/
	 * @param appId
	 *            应用唯一编号
	 * @param oemid
	 * @param packageName
	 * @param uid
	 * @param hid
	 * @param dependPackageName
	 *            本应用依赖包的报名
	 * @param dependPackegeVersion
	 *            依赖包的 版本
	 * @param version
	 *            版本
	 * @return String 本应用的升级检测地址
	 * 
	 */
	public static String getVersionCheckUrl(String host, String appId,
			String oemid, String packageName, String uid, String hid,
			String dependPackageName, String dependPackegeVersion,
			String version) {
		String url = AuxiliaryConstants.getVersionCheckUrl(host)
				.replace("APPID", null2Space(appId))
				.replace("OEMID", null2Space(oemid))
				.replace("VERSION", null2Space(version))
				.replace("PACKAGENAME", null2Space(packageName))
				.replace("DEPENDPN", null2Space(dependPackageName))
				.replace("DEPENDPV", null2Space(dependPackegeVersion))
				.replace("UID", null2Space(uid))
				.replace("HID", null2Space(hid));
		return url;
	}
	
	/**
	 * 提供版本检测接口地址 获取本应用的检测地址
	 * 
	 * @param host
	 *            ex : http://www.voole.com/
	 * @param appInfo 
	 * @return String 本应用的升级检测地址
	 * 
	 */
	public static String getVersionCheckUrl(String host, AppInfo appInfo) {
		String url = AuxiliaryConstants.getVersionCheckUrl(host)
				.replace("APPID", null2Space(appInfo.getAppId()))
				.replace("OEMID", null2Space(appInfo.getOemid()))
				.replace("VERSION", null2Space(appInfo.getAppVersion()))
				.replace("PACKAGENAME", null2Space(appInfo.getAppPackagename()))
				.replace("DEPENDPN", null2Space(appInfo.getDependPackageName()))
				.replace("DEPENDPV", null2Space(appInfo.getDependPackegeVersion()))
				.replace("UID", null2Space(appInfo.getUid()))
				.replace("HID", null2Space(appInfo.getHid()));
		return url;
	}

	/**
	 * 获取接受统本应用计信息的url
	 * 
	 * @param host
	 * @param appId
	 * @param oemid
	 * @param packageName
	 * @param uid
	 * @param hid
	 * @return String
	 * 
	 * 
	 */
	public static String getReportUrl(String host, String appId, String oemid,
			String packageName, String uid, String hid) {
		String url = AuxiliaryConstants.getVersionCheckUrl(host)
				.replace("OEMID", null2Space(oemid))
				.replace("PACKAGENAME", null2Space(packageName))
				.replace("UID", null2Space(uid))
				.replace("HID", null2Space(hid))
				.replace("APPID", null2Space(appId));
		return url;
	}

	/**
	 * 根据提供的公司服务接口 动态获取用户统计的接口地址
	 * 
	 * @param intfaceUrl
	 *            公司服务的接口地址
	 * @param appId
	 * @param oemid
	 * @param packageName
	 * @param uid
	 * @param hid
	 * @return String
	 * 
	 * 
	 */
	public static String dynamicGetReportUrl(String intfaceUrl, String appId,
			String oemid, String packageName, String uid, String hid) {
		if (reporHostUrl == null) {
			dynamicGetAllHostUrl(intfaceUrl);
		}

		return getReportUrl(reporHostUrl, appId, oemid, packageName, uid, hid);
	}

	/**
	 * 根据默认的公司服务接口 动态获取用户统计的接口地址
	 * 
	 * @param appId
	 * @param oemid
	 * @param packageName
	 * @param uid
	 * @param hid
	 * @return String
	 */
	public static String dynamicGetReportUrl(String appId, String oemid,
			String packageName, String uid, String hid) {
		return dynamicGetReportUrl(defaultIntfaceUrl, appId, oemid,
				packageName, uid, hid);
	}

	/**
	 * 本应用上报异常的url
	 * 
	 * @param host
	 *            异常收集服务地址
	 * @param appId
	 * @param oemid
	 * @param packageName
	 * @param uid
	 * @param hid
	 * @param version
	 * @return String
	 */
	public static String getExceptionReportUrl(String host, String appId,
			String oemid, String packageName, String uid, String hid,
			String version) {
		String url = AuxiliaryConstants.getExceptionReportUrl(host)
				.replace("OEMID", null2Space(oemid))
				.replace("PACKAGENAME", null2Space(packageName))
				.replace("VERSION", null2Space(version))
				.replace("UID", null2Space(uid))
				.replace("HID", null2Space(hid))
				.replace("APPID", null2Space(appId));
		return url;
	}

	/**
	 * 根据提供的接口动态获取 异常提交地址
	 * 
	 * @param intfaceUrl
	 *            公司服务接口地址
	 * @param appId
	 * @param oemid
	 * @param packageName
	 * @param uid
	 * @param hid
	 * @param version
	 * @return String
	 */
	public static String dynamicGetExceptionReportUrl(String intfaceUrl,
			String appId, String oemid, String packageName, String uid,
			String hid, String version) {
		if (exceptionLogHostUrl == null) {
			dynamicGetAllHostUrl(intfaceUrl);
		}

		return getExceptionReportUrl(exceptionLogHostUrl, appId, oemid,
				packageName, uid, hid, version);
	}

	/**
	 * 根据默认的接口地址,,动态获取异常提交地址
	 * 
	 * @param appId
	 * @param oemid
	 * @param packageName
	 * @param uid
	 * @param hid
	 * @param version
	 * @return String
	 */
	public static String dynamicGetExceptionReportUrl(String appId,
			String oemid, String packageName, String uid, String hid,
			String version) {

		return dynamicGetExceptionReportUrl(defaultIntfaceUrl, appId, oemid,
				packageName, uid, hid, version);
	}

	/**
	 * 提交反馈信息地址
	 * 
	 * @param host
	 *            ex : http://www.voole.com/
	 * @param appId
	 * @param oemid
	 * @param packageName
	 * @param uid
	 * @param hid
	 * @param optionCode
	 * @param version
	 * @return String
	 */
	public static String getAdviceFeedBackReportUrl(String host, String appId,
			String oemid, String packageName, String uid, String hid,
			String optionCode, String version) {
		String url = AuxiliaryConstants.getAdviceFeedBackReportUrl(host)
				.replace("OEMID", null2Space(oemid))
				.replace("PACKAGENAME", null2Space(packageName))
				.replace("VERSION", null2Space(version))
				.replace("UID", null2Space(uid))
				.replace("HID", null2Space(hid))
				.replace("OPTIONCODE", null2Space(optionCode))
				.replace("APPID", null2Space(appId));
		return url;
	}
	
	
	/**
	 * 版本检测url
	 * 
	 * @param appId
	 * @param oemid
	 * @param packageName
	 * @param hid
	 * @param version
	 * @return String
	 */
	public static String getVersionCheckUrl(
			String oemid, String packageName,String hid,
			String version,String appId) {
		String url = AuxiliaryConstants.getVersionCheckUrl()
				.replace("OEMID", null2Space(oemid))
				.replace("PACKAGENAME", null2Space(packageName))
				.replace("VERSION", null2Space(version))
				.replace("APPID", null2Space(appId))
				.replace("HID", null2Space(hid))
				.replace("SN", "")
				.replace("IP", "")
				.replace("UID", "");
		return url;

	}
	
	/**
	 * 版本检测url
	 * 
	 * @param appId
	 * @param oemid
	 * @param packageName
	 * @param hid
	 * @param version
	 * @param sn 
	 * @param localIp 
	 * @return String
	 */
	public static String getVersionCheckUrl(
			String oemid, String packageName,String hid,
			String version,String appId,String sn,String localIp) {
		String url = AuxiliaryConstants.getVersionCheckUrl()
				.replace("OEMID", null2Space(oemid))
				.replace("PACKAGENAME", null2Space(packageName))
				.replace("VERSION", null2Space(version))
				.replace("APPID", null2Space(appId))
				.replace("HID", null2Space(hid))
				.replace("SN", null2Space(sn))
				.replace("IP", null2Space(localIp))
				.replace("UID", "")
				;
		return url;

	}
	
	/**
	 * 版本检测url
	 * 
	 * @param appId
	 * @param oemid
	 * @param packageName
	 * @param hid
	 * @param version
	 * @param uid 
	 * @return String
	 */
	public static String getVersionCheckUrlForReport(
			String oemid, String packageName,String hid,
			String version,String appId,String uid) {
		String versionCheckUrl = "";
		String url = AuxiliaryConstants.getVersionCheckUrl()
				.replace("OEMID", null2Space(oemid))
				.replace("PACKAGENAME", null2Space(packageName))
				.replace("VERSION", null2Space(version))
				.replace("APPID", null2Space(appId))
				.replace("HID", null2Space(hid))
				.replace("SN", null2Space(""))
				.replace("IP", null2Space(""))
				.replace("UID", null2Space(uid));
		versionCheckUrl = url;
		//url = url+"&versionCheckUrl="+versionCheckUrl;
		return url;

	}

	/**
	 * 根据提供的的接口地址 动态获取反馈提交的链接地址
	 * 
	 * @param intfaceUrl
	 *            公司服务接口地址
	 * @param appId
	 * @param oemid
	 * @param packageName
	 * @param uid
	 * @param hid
	 * @param optionCode
	 * @param version
	 * @return String
	 */
	public static String dynamicGetAdviceFeedBackReportUrl(String intfaceUrl,
			String appId, String oemid, String packageName, String uid,
			String hid, String optionCode, String version) {
		if (feedBackInfoHostUrl == null) {
			dynamicGetAllHostUrl(intfaceUrl);
		}
		return getAdviceFeedBackReportUrl(feedBackInfoHostUrl, appId, oemid,
				packageName, uid, hid, optionCode, version);
	}

	/**
	 * 根据默认的接口地址 动态获取反馈提交的链接地址
	 * 
	 * @param appId
	 * @param oemid
	 * @param packageName
	 * @param uid
	 * @param hid
	 * @param optionCode
	 * @param version
	 * @return String
	 */
	public static String dynamicGetAdviceFeedBackReportUrl(String appId,
			String oemid, String packageName, String uid, String hid,
			String optionCode, String version) {

		return dynamicGetAdviceFeedBackReportUrl(defaultIntfaceUrl, appId,
				oemid, packageName, uid, hid, optionCode, version);
	}

	/**
	 * 获取反馈选项url
	 * 
	 * @param host
	 * @param appId
	 * @param oemid
	 * @param packageName
	 * @param uid
	 * @param hid
	 * @param version
	 * @return String
	 */
	public static String getFeedBackOptionsUrl(String host, String appId,
			String oemid, String packageName, String uid, String hid,
			String version) {
		String url = AuxiliaryConstants.getFeedBackOptionsUrl(host)
				.replace("OEMID", null2Space(oemid))
				.replace("PACKAGENAME", null2Space(packageName))
				.replace("VERSION", null2Space(version))
				.replace("UID", null2Space(uid))
				.replace("HID", null2Space(hid))
				.replace("APPID", null2Space(appId));
		return url;

	}

	/**
	 * 根据传入接口地址 动态获取反馈类型地址
	 * 
	 * @param intfaceUrl
	 *            公司服务接口
	 * @param appId
	 * @param oemid
	 * @param packageName
	 * @param uid
	 * @param hid
	 * @param version
	 * @return String
	 */
	public static String dynamicGetFeedBackOptionsUrl(String intfaceUrl,
			String appId, String oemid, String packageName, String uid,
			String hid, String version) {
		if (feedBackTypeHostUrl == null) {
			dynamicGetAllHostUrl(intfaceUrl);
		}
		return getFeedBackOptionsUrl(feedBackTypeHostUrl, appId, oemid,
				packageName, uid, hid, version);
	}

	/**
	 * 根据默认接口地址 动态获取反馈类型接口地址
	 * 
	 * @param appId
	 * @param oemid
	 * @param packageName
	 * @param uid
	 * @param hid
	 * @param version
	 * @return String
	 */
	public static String dynamicGetFeedBackOptionsUrl(String appId,
			String oemid, String packageName, String uid, String hid,
			String version) {
		return dynamicGetFeedBackOptionsUrl(defaultIntfaceUrl, appId, oemid,
				packageName, uid, hid, version);
	}

	/**
	 * 获取关于url
	 * 
	 * @param host
	 *            ex : http://www.voole.com/
	 * @param appId
	 * @param oemid
	 * @param packageName
	 * @param uid
	 * @param hid
	 * @param version
	 * @param type
	 *            Text:返回文本 Html：返回html连接地址
	 * @return String
	 */
	public static String getAboutUrl(String host, String appId, String oemid,
			String packageName, String uid, String hid, String version,
			String type) {
		if (!StringUtil.isNotNull(type)) {// 默认返回文本
			type = "Text";
		}
		String url = AuxiliaryConstants.getAboutUrl(host)
				.replace("OEMID", null2Space(oemid))
				.replace("PACKAGENAME", null2Space(packageName))
				.replace("VERSION", null2Space(version))
				.replace("TYPE", null2Space(type))
				.replace("UID", null2Space(uid))
				.replace("HID", null2Space(hid))
				.replace("APPID", null2Space(appId));
		return url;

	}

	/**
	 * 根据传入的接口地址 动态获取关于地址
	 * 
	 * @param intfaceUrl
	 *            公司服务的接口地址
	 * @param appId
	 * @param oemid
	 * @param packageName
	 * @param uid
	 * @param hid
	 * @param version
	 * @param type
	 * @return String
	 */
	public static String dynamicGetAboutUrl(String intfaceUrl, String appId,
			String oemid, String packageName, String uid, String hid,
			String version, String type) {
		if (aboutHostUrl == null) {
			dynamicGetAllHostUrl(intfaceUrl);
		}

		return getAboutUrl(aboutHostUrl, appId, oemid, packageName, uid, hid,
				version, type);

	}

	/**
	 * 更加默认的接口地址 动态获取关于信息地址
	 * 
	 * @param appId
	 * @param oemid
	 * @param packageName
	 * @param uid
	 * @param hid
	 * @param version
	 * @param type
	 * @return String
	 */
	public static String dynamicGetAboutUrl(String appId, String oemid,
			String packageName, String uid, String hid, String version,
			String type) {

		return dynamicGetAboutUrl(defaultIntfaceUrl, appId, oemid, packageName,
				uid, hid, version, type);
	}

	/**
	 * 获取帮助信息的地址
	 * 
	 * @param host
	 *            帮助服务host地址
	 * @param appId
	 * @param oemid
	 * @param packageName
	 * @param uid
	 * @param hid
	 * @param version
	 * @param type
	 *            需要的 返回类型,,"Text" 或者"Html"首字母大写,默认值为Text Text:返回文本
	 *            Html：返回html连接地址
	 * @return String
	 */
	public static String getHelpUrl(String host, String appId, String oemid,
			String packageName, String uid, String hid, String version,
			String type) {
		if (!StringUtil.isNotNull(type)) {// 默认返回文本
			type = "Text";
		}
		String url = AuxiliaryConstants.getHelpUrl(host)
				.replace("OEMID", null2Space(oemid))
				.replace("PACKAGENAME", null2Space(packageName))
				.replace("VERSION", null2Space(version))
				.replace("TYPE", null2Space(type))
				.replace("UID", null2Space(uid))
				.replace("HID", null2Space(hid))
				.replace("APPID", null2Space(appId));
		return url;

	}

	private static String null2Space(String str) {
		if (str == null) {
			str = "";
		}
		return str;
	}

	/**
	 * 动态获取本应用升级检测地址,
	 * 
	 * @param intfaceUrl
	 * @param appId
	 * @param oemid
	 * @param packageName
	 * @param uid
	 * @param hid
	 * @param dependPackageName
	 * @param dependPackegeVersion
	 * @param version
	 * @return String
	 */
	public static String dynamicGetVersionCheckUrl(String intfaceUrl,
			String appId, String oemid, String packageName, String uid,
			String hid, String dependPackageName, String dependPackegeVersion,
			String version) {

		if (versionCheckHostUrl == null) {

			dynamicGetAllHostUrl(intfaceUrl);
		}
		return getVersionCheckUrl(versionCheckHostUrl, appId, oemid,
				packageName, uid, hid, dependPackageName, dependPackegeVersion,
				version);
	}

	/**
	 * 根据默认的接口地址 动态获取升级检测地址
	 * 
	 * @param appId
	 * @param oemid
	 * @param packageName
	 * @param uid
	 * @param hid
	 * @param dependPackageName
	 * @param dependPackegeVersion
	 * @param version
	 * @return String
	 * 
	 * 
	 */
	public static String dynamicGetVersionCheckUrl(String appId, String oemid,
			String packageName, String uid, String hid,
			String dependPackageName, String dependPackegeVersion,
			String version) {
		return dynamicGetVersionCheckUrl(defaultIntfaceUrl, appId, oemid,
				packageName, uid, hid, dependPackageName, dependPackegeVersion,
				version);
	}

	/**
	 * 根据默认的接口地址动态的获取帮助的地址
	 * 
	 * @param intfaceUrl
	 *            公司服务的接口地址
	 * @param appId
	 * @param oemid
	 * @param packageName
	 * @param uid
	 * @param hid
	 * @param version
	 * @param type
	 * @return String
	 */
	public static String dynamicGetHelpUrl(String intfaceUrl, String appId,
			String oemid, String packageName, String uid, String hid,
			String version, String type) {
		if (versionCheckHostUrl == null) {
			dynamicGetAllHostUrl(intfaceUrl);
		}

		return getHelpUrl(helpHostUrl, appId, oemid, packageName, uid, hid,
				version, type);
	}

	/**
	 * 根据默认接口地址 动态获取帮助地址
	 * 
	 * @param appId
	 * @param oemid
	 * @param packageName
	 * @param uid
	 * @param hid
	 * @param version
	 * @param type
	 *            需要的 返回类型,,"Text" 或者"Html"首字母大写,默认值为Text
	 * @return String
	 */
	public static String dynamicGetHelpUrl(String appId, String oemid,
			String packageName, String uid, String hid, String version,
			String type) {

		return dynamicGetHelpUrl(defaultIntfaceUrl, appId, oemid, packageName,
				uid, hid, version, type);
	}

	/**
	 * 动态的得到所有 服务的入口地址,,为定义好的成员变量赋值
	 * 
	 * @param intfaceUrl
	 *            公司的服务入口地址
	 */
	private static void dynamicGetAllHostUrl(String intfaceUrl) {
		List<Map<String, String>> list = VooleData.getInstance()
				.getIntfaceUrlList(intfaceUrl);

		for (Map<String, String> map : list) {
			if ("Upgrade".equals(map.get("key"))) {
				versionCheckHostUrl = map.get("url");
			} else if ("HelpInfo".equals(map.get("key"))) {
				helpHostUrl = map.get("url");
			} else if ("AboutInfo".equals(map.get("key"))) {
				aboutHostUrl = map.get("url");
			} else if ("FeedBackType".equals(map.get("key"))) {
				feedBackTypeHostUrl = map.get("url");
			} else if ("FeedBackInfo".equals(map.get("key"))) {
				feedBackInfoHostUrl = map.get("url");
			} else if ("ExceptionLog".equals(map.get("key"))) {
				exceptionLogHostUrl = map.get("url");
			} else if ("AuxiliaryService".equals(map.get("key"))) {
				reporHostUrl = map.get("url");
			}
		}

	}
}
