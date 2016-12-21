package com.voole.android.client.UpAndAu.auxiliary;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.voole.android.client.UpAndAu.constants.DataConstants;
import com.voole.android.client.UpAndAu.model.DataResult;
import com.voole.android.client.UpAndAu.model.ReportInfo;
import com.voole.android.client.UpAndAu.model.parser.VooleData;
import com.voole.android.client.UpAndAu.util.HttpUtil;
import com.voole.android.client.UpAndAu.util.Logger;
import com.voole.android.client.UpAndAu.util.NetUtil;
import com.voole.android.client.UpAndAu.util.StringUtil;

import android.content.Context;
import android.util.Xml;

/**
 * 统计服务类 (所有方法都需要异步调用)
 * 
 * @version 1.0
 * @author LEE
 * @date 2013-4-7 上午10:15:38
 * @version V1.1
 * @author wusong
 * @update 2013-4-16下午2:37:36
 */
public class StatisticsService {

	private static StatisticsService instance = null;
	private Context context;

	private StatisticsService(Context context) {
		this.context = context;
	}

	public static StatisticsService getInstance(Context context) {
		if (instance == null) {
			instance = new StatisticsService(context);
		}
		return instance;
	}
	
	/**
	 * @param 
	 * @return ReportInfo
	 * @description 将提交的信息封装成ReportInfo对象
	 * @version 1.0
	 * @author wujian
	 * @date 2013-6-26 上午10:29:54
	 * @update 2013-6-26 上午10:29:54
	 */
	private ReportInfo initData(String appid,String appname,String appverison,String bodyContentRoot,
			String bodyContentRootAttr, String erroCode, String erroContent,String hid,
			String newVersion,String oemid,String packagename,String resultcode, String type, String uid, String value){
		
		ReportInfo reportInfo = new ReportInfo();
		reportInfo.setAppId(appid);
		reportInfo.setAppName(appname);
		reportInfo.setAppVersion(appverison);
		
		if(bodyContentRoot.trim().length() >0 && bodyContentRootAttr.trim().length() >0 
				&& erroCode.trim().length() >0 && erroContent.trim().length() >0)
		{
			reportInfo.setBodyContent(VooleData.getInstance().generateBodyContentInfo(bodyContentRoot,
					bodyContentRootAttr,erroCode,erroContent));
			
		}else{
			reportInfo.setBodyContent("");
		}
	
		reportInfo.setHid(hid);
		reportInfo.setNewAppVersion(newVersion);
		reportInfo.setOemid(oemid);
		reportInfo.setPackageName(packagename);
		reportInfo.setResultCode(resultcode);
		reportInfo.setType(type);
		reportInfo.setUid(uid);
		reportInfo.setValue(value);
		return reportInfo;

	}
	
	/**
	 * @param appid 应用编号
	 * @param appname 应用名称
	 * @param appverison 应用版本号
	 * @param hid 终端硬件编号
	 * @param oemid 
	 * @param packagename 应用包名
	 * @param uid 用户编号
	 * @param reportUrl  提交报告url地址
	 * @description 开始下载时提交报告
	 * @version 1.0
	 * @author wujian
	 * @date 2013-6-26 上午10:31:01
	 * @update 2013-6-26 上午10:31:01
	 */
	public void startDownloadReport(String appid,String appname,String appverison,String hid,
			String oemid,String packagename, String uid, String reportUrl){
		
		ReportInfo reportInfo = initData(appid, appname, appverison,
				"", "", "", "", 
				hid, "", oemid, packagename, "", "updateDownload", uid, "startDownload");
		
		
		List<ReportInfo> reportInfoList = new ArrayList<ReportInfo>();
		reportInfoList.add(reportInfo);
		
		reportUpgradeInfo(reportInfoList,reportUrl);
	}
	
	/**
	 * @param appid 应用编号
	 * @param appname 应用名称
	 * @param appverison 应用版本号
	 * @param hid 终端硬件编号
	 * @param oemid 
	 * @param packagename 应用包名
	 * @param uid 用户编号
	 * @param reportUrl  提交报告url地址
	 * @description 下载成功时提交报告
	 * @version 1.0
	 * @author wujian
	 * @date 2013-6-26 上午10:31:01
	 * @update 2013-6-26 上午10:31:01
	 */
	public void downloadSuccessReport(String appid,String appname,String appverison,String hid,
		   String oemid,String packagename, String uid, String reportUrl){
		
		ReportInfo reportInfo = initData(appid, appname, appverison,
				"", "", "", "", 
				hid, "", oemid, packagename, "", "updateDownload", uid, "downloadSuccess");
		
		List<ReportInfo> reportInfoList = new ArrayList<ReportInfo>();
		reportInfoList.add(reportInfo);
		
		reportUpgradeInfo(reportInfoList,reportUrl);
	}
	
	/**
	 * @param appid 应用编号
	 * @param appname 应用名称
	 * @param appverison 应用版本号
	 * @param erroCode 错误信息代码
	 * @param erroContent 错误信息内容
	 * @param hid 终端硬件编号
	 * @param oemid
	 * @param packagename 应用包名
	 * @param uid 用户编号
	 * @param reportUrl 提交报告url地址
	 * @description 下载失败时提交报告
	 * @version 1.0
	 * @author wujian
	 * @date 2013-6-26 上午10:31:01
	 * @update 2013-6-26 上午10:31:01
	 */
	public void downloadFaileReport(String appid,String appname,String appverison, String erroCode, String erroContent,String hid,
			String oemid,String packagename, String uid, String reportUrl){
		
		ReportInfo reportInfo = initData(appid, appname, appverison,
				DataConstants.MSG_DOWNLOADFAILINFO, DataConstants.MSG_ERROCODE, erroCode, erroContent, 
				hid, "", oemid, packagename, "", "updateDownload", uid, "downloadFaile");
		
		
		List<ReportInfo> reportInfoList = new ArrayList<ReportInfo>();
		reportInfoList.add(reportInfo);
		
		reportUpgradeInfo(reportInfoList,reportUrl);
	}
	
	/**
	 * @param appid 应用编号
	 * @param appname 应用名称
	 * @param appverison 应用版本号
	 * @param hid 终端硬件编号
	 * @param oemid 
	 * @param packagename 应用包名
	 * @param uid 用户编号 
	 * @param reportUrl 提交报告url地址
	 * @description 安装时提交报告
	 * @version 1.0
	 * @author wujian
	 * @date 2013-6-26 上午10:31:01
	 * @update 2013-6-26 上午10:31:01
	 */
	public void updateInstalledReport(String appid,String appname,String appverison,String hid,
			String oemid,String packagename, String uid, String reportUrl){
		
		ReportInfo reportInfo = initData(appid, appname, appverison,
				"", "", "", "", 
				hid, "", oemid, packagename, "", "updateDownload", uid, "updateInstalled");
		
		List<ReportInfo> reportInfoList = new ArrayList<ReportInfo>();
		reportInfoList.add(reportInfo);
		
		reportUpgradeInfo(reportInfoList,reportUrl);
	}
	
	
	
	
	

	
	/**
	 * 升级操作以及客户端行为数据收集上报
	 * 
	 * @param reportInfoList
	 *            上报的信息列表
	 * @param url
	 *            ：若本身为空则http请求出现错误。 升级以及客户端行为数据收集上报
	 * @return DataResult 因为网络不可靠等原因,使用结果请非空判断
	 */
	public DataResult reportUpgradeInfo(List<ReportInfo> reportInfoList,
			String url) {
		if (!StringUtil.isNotNull(url))
			return null;
		// 提交返回的结果
		DataResult dataResult = null;
		if (reportInfoList != null && reportInfoList.size() > 0
				&& StringUtil.isNotNull(url)) {
			InputStream in = null;
			if (NetUtil.isNetworkConnected(context)) {
				HttpUtil httpUtil = new HttpUtil();
				try {
					String params = VooleData.getInstance()
							.generateUpgradeReportInfo(reportInfoList);
					in = httpUtil.doPost(url, params, -1, -1, null);
					dataResult = VooleData.getInstance().ParseDataResult(in);
				} finally {
					httpUtil.closeInputStream(in);
				}
			}
		}

		return dataResult;
	}

	/**
	 * 上报客户端用户行为信息
	 * 
	 * @param reportInfoList
	 * @param bodyAttrsMap
	 *            为了动态生成生成body节点的 属性 <body attr1="value1",attr2="value2" ...
	 *            attrn="valuen">
	 * @param url
	 *            请求url
	 * @return DataResult
	 */
	public DataResult reportClientHehaviorInfo(List<ReportInfo> reportInfoList,
			Map<String, String> bodyAttrsMap, String url) {
		if (!StringUtil.isNotNull(url))
			return null;
		// 提交返回的结果
		DataResult dataResult = null;
		if (reportInfoList != null && reportInfoList.size() > 0) {
			InputStream in = null;
			if (NetUtil.isNetworkConnected(context)) {
				HttpUtil httpUtil = new HttpUtil();
				try {
					String params = VooleData.getInstance()
							.generateClientHehaviorInfo(reportInfoList,
									bodyAttrsMap);
					in = httpUtil.doPost(url, params, -1, -1, null);

					XmlPullParser parser = Xml.newPullParser();
					parser.setInput(in, HttpUtil.Encoding_UTF8);
					dataResult = VooleData.getInstance().ParseDataResult(in);

				} catch (XmlPullParserException e) {
					e.printStackTrace();
					return null;
				} finally {
					httpUtil.closeInputStream(in);
				}
			}
		}

		return dataResult;
	}

}
