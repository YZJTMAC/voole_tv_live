package com.voole.statistics.service;

import java.io.IOException;

import com.voole.statistics.config.Config;
import com.voole.statistics.parse.PageMessageParse;
import com.voole.statistics.util.StringPrint;

/**
 * 这个类 面向大视野/桌面等容器类产品, 具有下载或安装其他app功能
* @author Jacky   
* @date 2015-3-24 上午9:46:18
 */
public class StatisticsAppinstallReportService {
	/**
	 * 实例页面统计服务类对象
	 */
	private static StatisticsAppinstallReportService instance = null;

	/** 提交消息数据的接口地址 **/
	private String url1 = null; // 接口地址

	/**
	 * 获得页面统计服务类的实例
	 * 
	 * @return StatisticsPageService的实例
	 */
	public static StatisticsAppinstallReportService getInstance() {
		if (instance == null) {
			instance = new StatisticsAppinstallReportService();
		}
		return instance;
	}

	/**
	 * 构造方法
	 */
	private StatisticsAppinstallReportService() {

	}

    /**
     * 初始化访问地址方法
     * @param oemid oemid
     * @param uid   用户id 
     * @param appId appid
     * @param appVersion 应用的版本 (字符串)	2.1.0
     * @param packageName 包名
     * @param mac             mac
     * @param versioncode   manifest.xml配置的VersionCode (字符串)
     * @param system  android版本 (字符串)
     * @param chip 芯片(字符串)
     * @param product 机型 有则赋值
     * @param url 访问url
     */
	public void initBasicData( String oemid, String uid,
			String appId, String appVersion, String packageName,String mac,String versioncode,String system,String chip,String product,String url) {
		

		this.url1 = url;

		// 拼串
		StringBuffer httpUrlSendSB = new StringBuffer();
		if (null != oemid && !"".equals(oemid.trim())) {
			httpUrlSendSB.append("?oemid=" + oemid.trim());
		}
		if (null != uid && !"".equals(uid.trim())) {
			httpUrlSendSB.append("&uid=" + uid.trim());
		}

		if (null != packageName && !"".equals(packageName.trim())) {
			httpUrlSendSB.append("&packagename=" + packageName.trim());
		}
		if (null != appVersion && !"".equals(appVersion.trim())) {
			httpUrlSendSB.append("&appversion=" + appVersion.trim());
		}

		if (null != appId && !"".equals(appId.trim())) {
			httpUrlSendSB.append("&appid=" + appId.trim());
		}
		
		if (null != mac && !"".equals(mac.trim())) {
			httpUrlSendSB.append("&mac=" + mac.trim());
		}
		if (null != versioncode && !"".equals(versioncode.trim())) {
			httpUrlSendSB.append("&versioncode=" + versioncode.trim());
		}
		if (null != system && !"".equals(system.trim())) {
			httpUrlSendSB.append("&system=" + system.trim());
		}
		if (null != chip && !"".equals(chip.trim())) {
			httpUrlSendSB.append("&chip=" + chip.trim());
		}
		if (null != product && !"".equals(product.trim())) {
			httpUrlSendSB.append("&product=" + product.trim());
		}
		
		httpUrlSendSB.append("&version=" + Config.version);

		this.url1 = url1 + httpUrlSendSB.toString()
				+ "&action=appinstallReport";

		StringPrint.print("创建会话接口 url 初始化==" + this.url1);

	}
	
	/**
	 * 发送安装信息
	 * @param infotype 信息类型	0	0 下载  1安装
	 * @param packageName app包名
	 * @param version app版本	
	 * @param time 消耗时长 	 单位:秒
	 * @param result 结果	0	0成功 1失败
	 */
	public void transferAppinstallMessage(int infotype, String packageName , String version, int time,int result) {
		final String xmlString = initXML( infotype,   packageName ,   version,   time,  result);
		new Thread() {
			public void run() {
				sendPlayerCreateSession(url1, xmlString);
			}
		}.start();
	}

	 

	/**
	 * 发送播放串
	 * 
	 * @param url12
	 * @param xmlString
	 */
	private void sendPlayerCreateSession(String url12, String xmlString) {

		String strResult = null;
		try {

			PageMessageParse pageMessageParse = new PageMessageParse();
			StringPrint.print("访问地址=" + url12);
			StringPrint.print("访问参数=" + xmlString);
			strResult = pageMessageParse.parseNokeep(url12, xmlString);
			StringPrint.print("结果==" + strResult);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

	}


	private String initXML(int infotype, String packageName , String version, int time,int result) {
		StringPrint.print("大视野参数xml创建 被调用");

		

		
		StringBuffer stringBuffer=new  StringBuffer();
		stringBuffer.append("<?xml version='1.0' encoding='utf-8' standalone='no'?>");
		stringBuffer.append("<MessageArray count=\""+"1"+"\" >");
		stringBuffer.append("<Header ");
		stringBuffer.append("/>");
		stringBuffer.append("<Array>");
		
		 
		  
		
	 

					stringBuffer.append("<Message type=\"appinstall\"   >");
					stringBuffer.append("<Body>");
					
					stringBuffer.append("CDATA[");
					stringBuffer.append("<info infotype=\""+infotype+"\" appinstall_package= \""+packageName+"\" appinstall_version=\""+version+"\" time=\""+time+"\" result =\""+result+"\"/>");
					
					
					
					stringBuffer.append("]");
					stringBuffer.append("</Body>");
					stringBuffer.append("</Message>");
					
					
		
		stringBuffer.append("</Array>");
		stringBuffer.append("</MessageArray>");
		
	
		 
		return stringBuffer.toString();
	}

}
