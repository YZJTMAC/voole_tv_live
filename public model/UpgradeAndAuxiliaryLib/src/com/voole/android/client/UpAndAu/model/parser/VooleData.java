package com.voole.android.client.UpAndAu.model.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.voole.android.client.UpAndAu.constants.DataConstants;
import com.voole.android.client.UpAndAu.model.AboutInfo;
import com.voole.android.client.UpAndAu.model.AdviceFeedBackInfo;
import com.voole.android.client.UpAndAu.model.DataResult;
import com.voole.android.client.UpAndAu.model.ExceptionFeedBackInfo;
import com.voole.android.client.UpAndAu.model.FeedBackOptionInfo;
import com.voole.android.client.UpAndAu.model.HelpInfo;
import com.voole.android.client.UpAndAu.model.LocalProxyInfo;
import com.voole.android.client.UpAndAu.model.ReportInfo;
import com.voole.android.client.UpAndAu.model.ReportXmlHelpInfo;
import com.voole.android.client.UpAndAu.model.UpdateInfo;
import com.voole.android.client.UpAndAu.util.Logger;


/**
 * 通用数据解析与生成入口
 * @author LEE 解析器
 * 
 */
public class VooleData {

	private String TAG = VooleData.class.getSimpleName();
	private static VooleData instance = null;

	public static VooleData getInstance() {
		if (instance == null) {
			instance = new VooleData();
		}

		return instance;
	}

	/**
	 * 解析检测升级返回结果xml
	 * @param url
	 * @return UpdateInfo
	 */
	public UpdateInfo parseUpdateInfo(String url) {
		UpdateInfoParser parser = new UpdateInfoParser();
		return parser.ParseUrl(url);
	}

	/**
	 *  解析 dataResult
	 * @param url
	 * @return DataResult
	 */
	public DataResult parseDataResult(String url) {
		DataResultParser parser = new DataResultParser();
		return parser.ParseUrl(url);
	}

	/**
	 *  解析 dataResult
	 * @param in
	 * @return  DataResult
	 */
	public DataResult ParseDataResult(InputStream in) {
		DataResultParser parser = new DataResultParser();
		return parser.ParseInputStreamByPull(in);
	}

	/**
	 *  解析反馈选项信息
	 * @param url
	 * @return FeedBackOptionInfo
	 */
	public FeedBackOptionInfo parseFeedBackOption(String url) {
		FeedBackOptionParser parser = new FeedBackOptionParser();
		return parser.ParseUrl(url);
	}

	/**
	 *  解析关于信息
	 * @param url
	 * @return AboutInfo
	 */
	public AboutInfo parseAboutInfo(String url) {
		AboutInfoParser parser = new AboutInfoParser();
		return parser.ParseUrl(url);
	}

	/**
	 * 解析帮助信息
	 * @param url
	 * @return HelpInfo
	 */
	public HelpInfo parseHelpInfo(String url) {
		HelpInfoParser parser = new HelpInfoParser();
		return parser.ParseUrl(url);
	}

	/**
	 *  生成异常xml
	 * @param exceptionFeedBackInfo
	 * @return String
	 */
	public String generateExceptionFeedBackInfo(
			ExceptionFeedBackInfo exceptionFeedBackInfo) {
		if (exceptionFeedBackInfo != null) {
			List<NameValuePair> rootAttrs = new ArrayList<NameValuePair>();
			rootAttrs.add(new BasicNameValuePair(DataConstants.MSG_VERSION,
					exceptionFeedBackInfo.getVersion()));
			rootAttrs.add(new BasicNameValuePair(DataConstants.MSG_APPNAME,
					exceptionFeedBackInfo.getAppName()));
			rootAttrs.add(new BasicNameValuePair(DataConstants.MSG_ERRCODE,
					exceptionFeedBackInfo.getErrCode()));
			rootAttrs.add(new BasicNameValuePair(DataConstants.MSG_PRIORITY,
					exceptionFeedBackInfo.getPriority()));

			List<NameValuePair> nodes = new ArrayList<NameValuePair>();
			nodes.add(new BasicNameValuePair(DataConstants.MSG_EXCEPINFO,
					exceptionFeedBackInfo.getExcepInfo()));
			String str = CommonXml.common(DataConstants.MSG_FEEDBACK,
					rootAttrs, nodes, true);

			Logger.debug(TAG, "生成的xml" + str);
			return str;
		}

		return null;

	}

	/**
	 *   生成反馈意见xml
	 * @param adviceFeedBackInfo
	 * @return String
	 */
	public String generateAdviceFeedBackInfo(
			AdviceFeedBackInfo adviceFeedBackInfo) {
		if (adviceFeedBackInfo != null) {
			List<NameValuePair> rootAttrs = new ArrayList<NameValuePair>();
			rootAttrs.add(new BasicNameValuePair(DataConstants.MSG_OPTIONCODE,
					adviceFeedBackInfo.getOptionCode()));
			rootAttrs.add(new BasicNameValuePair(DataConstants.MSG_VERSION,
					adviceFeedBackInfo.getVersion()));
			List<NameValuePair> nodes = new ArrayList<NameValuePair>();
			nodes.add(new BasicNameValuePair(DataConstants.MSG_CONTENT,
					adviceFeedBackInfo.getContent()));
			nodes.add(new BasicNameValuePair(DataConstants.MSG_PHONE,
					adviceFeedBackInfo.getPhone()));
			nodes.add(new BasicNameValuePair(DataConstants.MSG_EMAIL,
					adviceFeedBackInfo.getEmail()));
			String str = CommonXml.common(DataConstants.MSG_FEEDBACK,
					rootAttrs, nodes, true);

			Logger.debug(TAG, "生成的xml" + str);
			return str;
		}

		return null;

	}

	/**
	 *  生成升级报告xml
	 * @param reportInfoList
	 * @return String
	 */
	public String generateUpgradeReportInfo(List<ReportInfo> reportInfoList) {
		if (reportInfoList != null && reportInfoList.size() > 0) {
			List<NameValuePair> rootAttrs = new ArrayList<NameValuePair>();
			rootAttrs.add(new BasicNameValuePair(DataConstants.MSG_COUNT,
					String.valueOf(reportInfoList.size())));
			List<ReportXmlHelpInfo> reportXmlHelpInfoList = new ArrayList<ReportXmlHelpInfo>();
			for (ReportInfo reportInfo : reportInfoList) {
				ReportXmlHelpInfo reportXmlHelpInfo = new ReportXmlHelpInfo();
				reportXmlHelpInfo.setBodyContent(reportInfo.getBodyContent());
				reportXmlHelpInfo.setNodeBodyAttrs(this
						.getUpgradeReportBodyAttrs(
								reportInfo.getNewAppVersion(),
								reportInfo.getResultCode()));
				reportXmlHelpInfo.setNodeMsgAttrs(this
						.getUpgradeReportMsgAttrs(reportInfo.getType(),reportInfo.getValue(),
								reportInfo.getHid(), reportInfo.getUid(),
								reportInfo.getOemid(), reportInfo.getAppId(),
								reportInfo.getAppName(),
								reportInfo.getAppVersion()));
				reportXmlHelpInfoList.add(reportXmlHelpInfo);
			}
			String str = MsgXml.genXML(DataConstants.MSG_ROOT_ARRAY, rootAttrs,
					reportXmlHelpInfoList, true);
			Logger.debug(TAG, "生成的xml" + str);
			return str;
		} else {
			return null;
		}

	}
	
	/**
	 * @param bodyContentRoot
	 * @param bodyContentRootAttr
	 * @param erroCode
	 * @param erroContent
	 * @return String
	 * @description 生成客升级错误报告xml
	 * @version 1.0
	 * @author wujian
	 * @date 2013-6-26 上午10:27:20
	 * @update 2013-6-26 上午10:27:20
	 */
	public String generateBodyContentInfo(String bodyContentRoot, String bodyContentRootAttr,String erroCode, String erroContent){
		
		List<NameValuePair> rootAttrs = new ArrayList<NameValuePair>();
		rootAttrs.add(new BasicNameValuePair(bodyContentRootAttr,erroCode));
		
		return MsgXml.genBodyContentXML(bodyContentRoot, rootAttrs, erroContent);
	}

	/**
	 *生成客户端用户行为报告xml
	 * @param reportInfoList
	 * @param bodyAttrsMap
	 * @return String
	 */
	public String generateClientHehaviorInfo(List<ReportInfo> reportInfoList,
			Map<String, String> bodyAttrsMap) {
		if (reportInfoList != null && reportInfoList.size() > 0) {
			List<NameValuePair> rootAttrs = new ArrayList<NameValuePair>();
			rootAttrs.add(new BasicNameValuePair(DataConstants.MSG_COUNT,
					String.valueOf(reportInfoList.size())));
			List<ReportXmlHelpInfo> reportXmlHelpInfoList = new ArrayList<ReportXmlHelpInfo>();
			for (ReportInfo reportInfo : reportInfoList) {
				ReportXmlHelpInfo reportXmlHelpInfo = new ReportXmlHelpInfo();
				reportXmlHelpInfo.setBodyContent(reportInfo.getBodyContent());
				reportXmlHelpInfo.setNodeBodyAttrs(this
						.getClientHehaviorBodyAttrs(bodyAttrsMap));
				reportXmlHelpInfo.setNodeMsgAttrs(this
						.getUpgradeReportMsgAttrs(reportInfo.getType(),reportInfo.getValue(),
								reportInfo.getHid(), reportInfo.getUid(),
								reportInfo.getOemid(), reportInfo.getAppId(),
								reportInfo.getAppName(),
								reportInfo.getAppVersion()));
				reportXmlHelpInfoList.add(reportXmlHelpInfo);
			}
			String str = MsgXml.genXML(DataConstants.MSG_ROOT_ARRAY, rootAttrs,
					reportXmlHelpInfoList, true);
			Logger.debug(TAG, "生成的xml" + str);
			return str;
		} else {
			return null;
		}

	}

	/**
	 * 获取升级信息反馈的集合
	 * @param type
	 * @param hid
	 * @param uid
	 * @param oemid
	 * @param appId
	 * @param appName
	 * @param appVersion
	 * @return List<NameValuePair>
	 */
	private List<NameValuePair> getUpgradeReportMsgAttrs(String type, String value,
			String hid, String uid, String oemid, String appId, String appName,
			String appVersion) {
		List<NameValuePair> nodeAttrs = new ArrayList<NameValuePair>();
		nodeAttrs.add(new BasicNameValuePair(DataConstants.MSG_TYPE, type));
		nodeAttrs.add(new BasicNameValuePair(DataConstants.MSG_VALUE, value));
		nodeAttrs.add(new BasicNameValuePair(DataConstants.MSG_HID, hid));
		nodeAttrs.add(new BasicNameValuePair(DataConstants.MSG_UID, uid));
		nodeAttrs.add(new BasicNameValuePair(DataConstants.MSG_OEMID, oemid));
		nodeAttrs.add(new BasicNameValuePair(DataConstants.MSG_APPID, appId));
		nodeAttrs
				.add(new BasicNameValuePair(DataConstants.MSG_APPNAME, appName));
		nodeAttrs.add(new BasicNameValuePair(DataConstants.MSG_APPVERSION,
				appVersion));

		return nodeAttrs;
	}

	/**
	 * getUpgradeReportBodyAttrs
	 * @param newAppVersion
	 * @param resultCode
	 * @return  List<NameValuePair>
	 */
	private List<NameValuePair> getUpgradeReportBodyAttrs(String newAppVersion,
			String resultCode) {
		List<NameValuePair> nodeAttrs = new ArrayList<NameValuePair>();
		nodeAttrs.add(new BasicNameValuePair(DataConstants.MSG_NEWAPPVERSION,
				newAppVersion));
		nodeAttrs.add(new BasicNameValuePair(DataConstants.MSG_RESULTCODE,
				resultCode));
		return nodeAttrs;
	}

	/**
	 * getClientHehaviorBodyAttrs
	 * @param bodyAttrsMap
	 * @return List<NameValuePair>
	 */
	private List<NameValuePair> getClientHehaviorBodyAttrs(
			Map<String, String> bodyAttrsMap) {
		List<NameValuePair> nodeAttrs = new ArrayList<NameValuePair>();
		if (bodyAttrsMap != null) {
			for (Entry<String, String> entry : bodyAttrsMap.entrySet()) {
				nodeAttrs.add(new BasicNameValuePair(entry.getKey().toString(),
						entry.getValue().toString()));
			}
		}

		return nodeAttrs;
	}

	/**
	 *获取公司接口地址类所有业务接口的 地址
	 * @param url 公司业务接口地址集合 
	 * @return List<Map<String, String>> 
	 */
	public List<Map<String, String>> getIntfaceUrlList(String url) {
		IntfaceUrlParser parser = new IntfaceUrlParser();

		return parser.ParseUrlByPull(url);
	}
	
	/**
	 * 
	 * @param url
	 * @return LocalProxyInfo
	 * @description 本地代理解析
	 * @version 1.0
	 * @author LEE
	 * @date 2013-12-13 上午10:31:55 
	 * @update 2013-12-13 上午10:31:55
	 */
	public LocalProxyInfo parserLocalProxyInfo(String url) {
		LocalProxyInfoParser localProxyInfoParser = new LocalProxyInfoParser();
		LocalProxyInfo localProxyInfo = localProxyInfoParser.ParseUrlByPull(url);
		return localProxyInfo;
	}
}
