package com.voole.statistics.service;

import java.io.IOException;

import com.voole.statistics.bean.HeaderBean;
import com.voole.statistics.config.Config;
import com.voole.statistics.constans.PageStatisticsConstants;
import com.voole.statistics.parse.PageMessageParse;
import com.voole.statistics.util.StringPrint;
import com.voole.statistics.util.StringUtil;

/**
 * @author Jacky
 * @update 2015-5-31日 修改成最新的 json格式 并且加入时间戳参数
 */
public class StatisticsPageService {
	/**
	 * 实例页面统计服务类对象
	 */
	private static StatisticsPageService instance = null;
	/**
	 * 头信息BEAN
	 */
	private HeaderBean headerBean = null;

	/** 提交消息数据的接口地址 **/
	private String url1 = null; // 创建对话

	/**
	 * 本次消息汇报的唯一ID 从1开始每次增加
	 */
	public static int messageID = 0;

	/**
	 * 获得页面统计服务类的实例
	 * 
	 * @return StatisticsPageService的实例
	 */
	public static StatisticsPageService getInstance() {
		if (instance == null) {
			instance = new StatisticsPageService();
		}
		return instance;
	}

	/**
	 * 构造方法
	 */
	private StatisticsPageService() {

	}

	/**
	 * 初始化页面统计服务类
	 * 
	 * @param hid
	 *            终端硬件设备编号
	 * @param oemid
	 *            项目编号
	 * @param uid
	 *            用户唯一编号
	 * @param appId
	 *            应用唯一编号
	 * @param appVersion
	 *            应用版本号
	 * @param packageName
	 *            标识应用 url 请求地址 例如：http://epglog.voole.com/
	 */

	public void initBasicData(String hid, String oemid, String uid,
			String appId, String appVersion, String packageName, String url,int maxMsgCount) {

		headerBean = new HeaderBean();
		headerBean.setHid(hid);
		headerBean.setOemid(oemid);
		headerBean.setUid(uid);
		headerBean.setAppId(appId);
		headerBean.setAppVersion(appVersion);
		headerBean.setPackageName(packageName);
		headerBean.setVersion(Config.version);


		this.url1 = url;

		// 拼串
		StringBuffer httpUrlSendSB = new StringBuffer();
		if (null != oemid && !"".equals(oemid.trim())) {
			httpUrlSendSB.append("?oemid=" + oemid.trim());
		}
		if (null != hid && !"".equals(hid.trim())) {
			httpUrlSendSB.append("&hid=" + hid.trim());
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
		httpUrlSendSB.append("&version=" + Config.version);


		this.url1 = url1 + httpUrlSendSB.toString() + "&action=EpgReport";

		StringPrint.print("页面统计接口url地址  初始化成功==" + this.url1);

	}

	/**
	 * 发送消息
	 */
	public void sendMessage(String epgid, String pagetype, String moduletype,
			String focustype, String focusid, String pagenum, String action,
			String input, String input2, String midlist, String position,
			String did) {

		// 设置本次消息ID
		setMessageID();
		String messageID = getMessageID() + "";
		String lastID = "";

		String stampTemp = getStamp();
		
		final String tempUrl=url1+"&stamp=" + stampTemp;
		
		
		final String jsonString = initJson(epgid, messageID, lastID, pagetype,
				moduletype, focustype, focusid, pagenum, action, input, input2,
				midlist, position, did);

		new Thread() {
			public void run() {
				sendMessageJson(tempUrl, jsonString);
			}
		}.start();
	}


	
	private String initJson(String epgid, String messageID2, String lastID,
			String pagetype, String moduletype, String focustype,
			String focusid, String pagenum, String action, String input,
			String input2, String midlist, String position, String did) {

		if (!StringUtil.isNull(epgid)) {
			epgid = "";
		}
		if (!StringUtil.isNull(messageID2)) {
			messageID2 = "";
		}
		if (!StringUtil.isNull(lastID)) {
			lastID = "";
		}
		if (!StringUtil.isNull(pagetype)) {
			pagetype = "";
		}
		if (!StringUtil.isNull(moduletype)) {
			moduletype = "";
		}
		if (!StringUtil.isNull(focustype)) {
			focustype = "";
		}
		if (!StringUtil.isNull(focusid)) {
			focusid = "";
		}
		if (!StringUtil.isNull(pagenum)) {
			pagenum = "";
		}
		if (!StringUtil.isNull(action)) {
			action = "";
		}
		if (!StringUtil.isNull(input)) {
			input = "";
		}
		if (!StringUtil.isNull(input2)) {
			input2 = "";
		}
		if (!StringUtil.isNull(midlist)) {
			midlist = "";
		}
		if (!StringUtil.isNull(position)) {
			position = "";
		}
		if (!StringUtil.isNull(did)) {
			did = "";
		}

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("{");
		stringBuffer.append("\"type\": \"PageBrowsing\",");
		stringBuffer.append("\"name\": \"epg\",");
		stringBuffer.append("\"epg\": {");
		stringBuffer.append("\"epgid\": \"" + epgid + "\",");
		stringBuffer.append("\"id\": \"" + messageID2 + "\",");

		stringBuffer.append("\"lastid\": \"" + lastID + "\",");

		stringBuffer.append("\"pagetype\": \"" + pagetype + "\",");

		stringBuffer.append("\"moduletype\": \"" + moduletype + "\",");
		stringBuffer.append("\"focustype\": \"" + focustype + "\",");
		stringBuffer.append("\"focusid\": \"" + focusid + "\",");
		stringBuffer.append("\"pagenum\": \"" + pagenum + "\",");
		stringBuffer.append("\"action\": \"" + action + "\",");
		stringBuffer.append("\"input\": \"" + input + "\",");
		stringBuffer.append("\"input2\": \"" + input2 + "\",");
		stringBuffer.append("\"midlist\": \"" + midlist + "\",");
		stringBuffer.append("\"position\": \"" + position + "\",");
		stringBuffer.append("\"did\": \"" + did + "\"");
		stringBuffer.append("}");

		stringBuffer.append("}");

		StringPrint.print("初始化json为 " + stringBuffer.toString());
		return stringBuffer.toString();

	}

	/**
	 * 时间戳
	 * 
	 * @author Jacky
	 * @date 2015-5-31 下午12:16:38
	 */
	private String getStamp() {
		try {
			return System.currentTimeMillis() + "";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 发送串
	 * 
	 * @author Jacky
	 * @date 2015-5-31 下午12:40:56
	 */
	private void sendMessageJson(String url12, String xmlString) {

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

	/**
	 * 发送一般页面操作动作汇报（不带辅助参数pagemum，midlist，input）
	 * 
	 * @param epgid
	 * @param pagetype
	 * @param moduletype
	 * @param focusid
	 * @param focustype
	 * @param action
	 */
	public void transferActionMessage(String epgid, String pagetype,
			String moduletype, String focusid, String focustype, String action) {
		sendMessage(epgid, pagetype, moduletype, focustype, focusid, "",
				action, "", "", "", "", "");
	}

	/**
	 * 发送列表页内容区操作动作汇报（focustype都是content，action都是enter）
	 * 
	 * @param epgid
	 * @param pagetype
	 * @param moduletype
	 * @param focusid
	 * @param pagenum
	 * @param midlist
	 */
	public void transferActionContentMessage(String epgid, String pagetype,
			String moduletype, String focusid, String pagenum, String midlist) {
		sendMessage(epgid, pagetype, moduletype,
				PageStatisticsConstants.FOCUS_TYPE_CONTENT, focusid, pagenum,
				PageStatisticsConstants.ACTION_TYPE_ENTER, "", "", midlist, "",
				"");
	}

	/**
	 * 发送搜索和检索汇报（focustype都是content，action都是enter）
	 * 
	 * @param epgid
	 * @param pagetype
	 * @param moduletype
	 * @param focusid
	 * @param input
	 * @param input2
	 */
	public void transferSearchMessage(String epgid, String pagetype,
			String moduletype, String focusid, String input, String input2) {

		sendMessage(epgid, pagetype, moduletype,
				PageStatisticsConstants.FOCUS_TYPE_CONTENT, focusid, "",
				PageStatisticsConstants.ACTION_TYPE_ENTER, input2, input2, "",
				"", "");

	}

	/**
	 * 发送直播统计信息
	 * 
	 * @param moduletype
	 * @param focusid
	 * @param focustype
	 * @param action
	 */
	public void transferMagictvActionMessage(String epgid, String moduletype,
			String focusid, String focustype, String action) {
		sendMessage(epgid, PageStatisticsConstants.PAGE_TYPE_MAGICTV,
				moduletype, focustype, focusid, "", action, "", "", "", "", "");
	}

	public static int getMessageID() {
		return messageID;
	}

	public static void setMessageID() {
		messageID = messageID + 1;
	}

}
