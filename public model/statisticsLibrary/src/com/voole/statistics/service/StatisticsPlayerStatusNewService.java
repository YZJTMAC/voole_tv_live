package com.voole.statistics.service;

import java.io.IOException;
import java.util.List;

import com.voole.statistics.bean.HeaderBean;
import com.voole.statistics.bean.PlayerMessageArrayBean;
import com.voole.statistics.bean.PlayerMessageBean;
import com.voole.statistics.config.Config;
import com.voole.statistics.parse.PageMessageParse;
import com.voole.statistics.util.StringPrint;
import com.voole.statistics.util.StringUtil;

/**
 * 播放状态统计 此类是最新的播放统计状态服务类 2014-12-19
 * 
 * @author Jacky
 * 
 */
public class StatisticsPlayerStatusNewService {
	/**
	 * 实例页面统计服务类对象
	 */
	private static StatisticsPlayerStatusNewService instance = null;
	/**
	 * 头信息BEAN
	 */
	private HeaderBean headerBean = null;

	/** 提交消息数据的接口地址 **/
	private String url1 = null; // 创建对话
	private String url2 = null; // 心跳
	private String url3 = null; // 转换状态
	private int seqno = 0;

	/**
	 * 获得页面统计服务类的实例
	 * 
	 * @return StatisticsPageService的实例
	 */
	public static StatisticsPlayerStatusNewService getInstance() {
		if (instance == null) {
			instance = new StatisticsPlayerStatusNewService();
		}
		return instance;
	}

	/**
	 * 构造方法
	 */
	private StatisticsPlayerStatusNewService() {

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
			String appId, String appVersion, String packageName, String url) {
		headerBean = new HeaderBean();
		headerBean.setHid(hid);
		headerBean.setOemid(oemid);
		headerBean.setUid(uid);
		headerBean.setAppId(appId);
		headerBean.setAppVersion(appVersion);
		headerBean.setPackageName(packageName);

		// 过滤url非法字符
		if (StringUtil.isNull(url)) {
			try {

				int uidNumber = url.indexOf("?");
				if (-1 != uidNumber) {
					// 存在uid
					url = url.substring(0, uidNumber);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		this.url1 = url;
		this.url2 = url;
		this.url3 = url;

		// 拼串
		StringBuffer httpUrlSendSB = new StringBuffer();
		// 判断是否存在?号
		if (-1 != url.indexOf("?")) {
			// 存在问号

		} else {
			// 不存在问号
			httpUrlSendSB.append("?testdate=testdate");
		}

		if (null != oemid && !"".equals(oemid.trim())) {
			httpUrlSendSB.append("&oemid=" + oemid.trim());
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

		this.url1 = url1 + httpUrlSendSB.toString()
				+ "&action=player_create_sess";
		this.url2 = url2 + httpUrlSendSB.toString()
				+ "&action=player_heartbeat";
		this.url3 = url3 + httpUrlSendSB.toString()
				+ "&action=player_change_state";

		StringPrint.print("创建会话接口 url 初始化==" + this.url1);
		StringPrint.print("心跳接口url 初始化==" + this.url2);
		StringPrint.print("转换状态url 初始化==" + this.url3);

	}

	/**
	 * 创建会话
	 */
	public void playerCreateSession(String movfid, String movmid, String epgid,
			String sessionid, String url, String width, String height,
			String dpi, String party3, String cpid, String authcode) {
		setSeqno(0);
		final String xmlString = initPlayerCreateSessXML(movfid, movmid, epgid,
				sessionid, url, width, height, dpi, party3, cpid, authcode);

		new Thread() {
			public void run() {
				sendPlayerCreateSession(url1, xmlString);
			}
		}.start();
	}

	/**
	 * 心跳
	 */
	public void playerHeartbeat(String playtime, String sessionid,
			String authcode) {

		final String xmlString = initPlayerHeartbeatXML(playtime, sessionid,
				authcode);

		new Thread() {
			public void run() {
				sendPlayerCreateSession(url2, xmlString);
			}
		}.start();

	}

	/**
	 * 播放状态改变
	 */
	public void playerChangeState(String sessionid, String resno, String state,
			String playtime, String restime, String seekstart, String seekstop,
			String authcode) {

		setSeqno_();
		final String xmlString = initPlayerChangeStateXML(sessionid, resno,
				getSeqno() + "", state, playtime, restime, seekstart, seekstop,
				authcode);
		new Thread() {
			public void run() {
				sendPlayerCreateSession(url3, xmlString);
			}
		}.start();

	}

	private void setSeqno_() {
		this.seqno++;
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
			String stampTemp = getStamp();

			url12 = url12 + "&stamp=" + stampTemp;

			PageMessageParse pageMessageParse = new PageMessageParse();
			StringPrint.print("访问地址=" + url12);
			StringPrint.print("访问参数=" + xmlString);
			if (url12 != null) {
				strResult = pageMessageParse.parseNokeep(url12, xmlString);
			}
			StringPrint.print("结果==" + strResult);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

	}

	/**
	 * 初始化数据进入bean
	 * 
	 * @param tempMessageList
	 * @param headerBean
	 * @return
	 */
	public PlayerMessageArrayBean initStatisticsData(
			List<PlayerMessageBean> tempMessageList, HeaderBean headerBean) {

		PlayerMessageArrayBean playerMessageArrayBean = new PlayerMessageArrayBean();
		playerMessageArrayBean.setHeaderBean(headerBean);
		playerMessageArrayBean.setPlayerMessageBeanList(tempMessageList);
		playerMessageArrayBean.setCount(tempMessageList.size());

		return playerMessageArrayBean;
	}

	private String initPlayerCreateSessXML(String movfid, String movmid,
			String epgid, String sessionid, String url, String width,
			String height, String dpi, String party3, String cpid,
			String authcode) {
		StringPrint.print("创建回话方法xml 被调用");

		StringBuffer stringBuffer = new StringBuffer();
		/*
		 * stringBuffer
		 * .append("<?xml version='1.0' encoding='utf-8' standalone='no'?>");
		 * stringBuffer.append("<MessageArray count=\"" + 1 + "\" >");
		 * 
		 * stringBuffer.append("<Array>");
		 * 
		 * stringBuffer.append("<Message type=\"player_create_sess\" >");
		 * stringBuffer.append("<Body>"); stringBuffer.append("CDATA[");
		 * stringBuffer.append("<player ");
		 * 
		 * if (null != movfid && !"".equals(movfid.trim())) {
		 * stringBuffer.append(" movfid=\"" + movfid + "\" "); }
		 * stringBuffer.append(" movmid=\"" + movmid + "\" ");
		 * stringBuffer.append(" epgid=\"" + epgid + "\" ");
		 * 
		 * if (null != sessionid && !"".equals(sessionid.trim())) {
		 * stringBuffer.append(" sessionid=\"" + sessionid + "\" "); } if (null
		 * != url && !"".equals(url.trim())) { stringBuffer.append(" url=\"" +
		 * url + "\" "); }
		 * 
		 * stringBuffer.append(" width=\"" + width + "\" ");
		 * stringBuffer.append(" height=\"" + height + "\" ");
		 * stringBuffer.append(" dpi=\"" + dpi + "\" ");
		 * stringBuffer.append(" party3=\"" + party3 + "\" ");
		 * stringBuffer.append(" cpid=\"" + cpid + "\" ");
		 * 
		 * stringBuffer.append("> "); stringBuffer.append("</player>");
		 * stringBuffer.append("]"); stringBuffer.append("</Body>");
		 * stringBuffer.append("</Message>");
		 * 
		 * stringBuffer.append("</Array>");
		 * stringBuffer.append("</MessageArray>");
		 */
		stringBuffer.append("{");
		stringBuffer.append("\"type\": \"player_create_sess\",");
		stringBuffer.append("\"name\": \"player\",");
		stringBuffer.append("\"player\": {");
		stringBuffer.append("\"movfid\": \"" + movfid + "\",");
		stringBuffer.append("\"movmid\": \"" + movmid + "\",");

		stringBuffer.append("\"epgid\": \"" + epgid + "\",");
		stringBuffer.append("\"sessionid\": \"" + sessionid + "\",");
		stringBuffer.append("\"url\": \"" + url + "\",");
		stringBuffer.append("\"width\": \"" + width + "\",");
		stringBuffer.append("\"height\": \"" + height + "\",");
		stringBuffer.append("\"dpi\": \"" + dpi + "\",");
		stringBuffer.append("\"party3\": \"" + party3 + "\",");
		stringBuffer.append("\"cpid\": \"" + cpid + "\",");
		stringBuffer.append("\"authcode\": \"" + authcode + "\"");

		stringBuffer.append("}");

		stringBuffer.append("}");
		return stringBuffer.toString();
	}

	/**
	 * 心跳xml初始化
	 * 
	 * @param headerBean2
	 * @param playtime
	 * @param sessionid
	 * @return
	 */
	private String initPlayerHeartbeatXML(String playtime, String sessionid,
			String authcode) {

		StringBuffer stringBuffer = new StringBuffer();
		/*
		 * stringBuffer
		 * .append("<?xml version='1.0' encoding='utf-8' standalone='no'?>");
		 * stringBuffer.append("<MessageArray count=\"" + 1 + "\" >");
		 * stringBuffer.append("<Array>");
		 * 
		 * stringBuffer.append("<Message type=\"player_heartbeat\" >");
		 * stringBuffer.append("<Body>"); stringBuffer.append("CDATA[");
		 * stringBuffer.append("<player ");
		 * 
		 * if (null != sessionid && !"".equals(sessionid.trim())) {
		 * stringBuffer.append(" sessionid=\"" + sessionid + "\" "); }
		 * stringBuffer.append(" playtime=\"" + playtime + "\" ");
		 * 
		 * stringBuffer.append("> "); stringBuffer.append("</player>");
		 * stringBuffer.append("]"); stringBuffer.append("</Body>");
		 * stringBuffer.append("</Message>");
		 * 
		 * stringBuffer.append("</Array>");
		 * stringBuffer.append("</MessageArray>");
		 */
		stringBuffer.append("{");
		stringBuffer.append("\"type\": \"player_heartbeat\",");
		stringBuffer.append("\"name\": \"player\",");
		stringBuffer.append("\"player\": {");
		stringBuffer.append("\"playtime\": \"" + playtime + "\",");
		stringBuffer.append("\"sessionid\": \"" + sessionid + "\",");
		stringBuffer.append("\"authcode\": \"" + authcode + "\"");

		stringBuffer.append("}");

		stringBuffer.append("}");

		return stringBuffer.toString();
	}

	private String initPlayerChangeStateXML(String sessionid, String resno,
			String seqno, String state, String playtime, String restime,
			String seekstart, String seekstop, String authcode) {

		StringBuffer stringBuffer = new StringBuffer();
		/*
		 * stringBuffer
		 * .append("<?xml version='1.0' encoding='utf-8' standalone='no'?>");
		 * stringBuffer.append("<MessageArray count=\"" + 1 + "\" >");
		 * stringBuffer.append("<Array>");
		 * 
		 * stringBuffer.append("<Message type=\"player_change_state\" >");
		 * stringBuffer.append("<Body>"); stringBuffer.append("CDATA[");
		 * stringBuffer.append("<player ");
		 * 
		 * if (null != sessionid && !"".equals(sessionid.trim())) {
		 * stringBuffer.append(" sessionid=\"" + sessionid + "\" "); }
		 * stringBuffer.append(" resno=\"" + resno + "\" ");
		 * stringBuffer.append(" seqno=\"" + seqno + "\" ");
		 * stringBuffer.append(" state=\"" + state + "\" ");
		 * stringBuffer.append(" playtime=\"" + playtime + "\" ");
		 * stringBuffer.append(" restime=\"" + restime + "\" ");
		 * stringBuffer.append(" seekstart=\"" + seekstart + "\" ");
		 * stringBuffer.append(" seekstop=\"" + seekstop + "\" ");
		 * 
		 * stringBuffer.append("> "); stringBuffer.append("</player>");
		 * stringBuffer.append("]"); stringBuffer.append("</Body>");
		 * stringBuffer.append("</Message>");
		 * 
		 * stringBuffer.append("</Array>");
		 * stringBuffer.append("</MessageArray>");
		 */
		stringBuffer.append("{");
		stringBuffer.append("\"type\": \"player_change_state\",");
		stringBuffer.append("\"name\": \"player\",");
		stringBuffer.append("\"player\": {");
		stringBuffer.append("\"sessionid\": \"" + sessionid + "\",");
		stringBuffer.append("\"resno\": \"" + resno + "\",");

		stringBuffer.append("\"seqno\": \"" + seqno + "\",");
		stringBuffer.append("\"state\": \"" + state + "\",");
		stringBuffer.append("\"playtime\": \"" + playtime + "\",");
		stringBuffer.append("\"restime\": \"" + restime + "\",");
		stringBuffer.append("\"seekstart\": \"" + seekstart + "\",");
		stringBuffer.append("\"seekstop\": \"" + seekstop + "\",");
		stringBuffer.append("\"authcode\": \"" + authcode + "\"");

		stringBuffer.append("}");

		stringBuffer.append("}");
		return stringBuffer.toString();

	}

	public int getSeqno() {
		return seqno;
	}

	public void setSeqno(int seqno) {
		this.seqno = seqno;
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

}
