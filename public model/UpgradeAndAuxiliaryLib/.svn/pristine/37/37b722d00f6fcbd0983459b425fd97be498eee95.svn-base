package com.voole.android.client.messagelibrary.model.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

import com.voole.android.client.UpAndAu.constants.AuxiliaryConstants;
import com.voole.android.client.UpAndAu.downloader.Utility;
import com.voole.android.client.messagelibrary.model.DataResult;
import com.voole.android.client.messagelibrary.model.Header;
import com.voole.android.client.messagelibrary.model.Message;
import com.voole.android.client.messagelibrary.model.MessageArray;
import com.voole.android.client.messagelibrary.utils.StringUtil;


/**
 * 通用数据解析与生成入口
 * @author wujian
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
	 *  解析 dataResult
	 * @param in
	 * @return  DataResult
	 */
	public DataResult ParseDataResult(InputStream in) {
		DataResultParser parser = new DataResultParser();
		return parser.ParseInputStreamByPull(in);
	}
	
	
	/**
	 * @param messages
	 * @return String
	 * @description 生成统计数据xml
	 * @version 1.0
	 * @author wujian
	 * @date 2013-8-6 下午1:31:07
	 * @update 2013-8-6 下午1:31:07
	 */
	public String generateStatisticsData(MessageArray messages){
		
		if(messages != null && messages.getCount() > 0){
			List<NameValuePair> rootAttrs = new ArrayList<NameValuePair>();
			rootAttrs.add(new BasicNameValuePair(DataConstants.MSG_COUNT,
					String.valueOf(messages.getCount())));
			
			try {
				XmlSerializer xmlSerializer = Xml.newSerializer();
				StringWriter writer = new StringWriter();
				String namespace = null;
				xmlSerializer.setOutput(writer);
				xmlSerializer.startDocument(DataConstants.ENCODING, false);
				
				//开始根节点
				xmlSerializer.startTag(namespace, DataConstants.MSG_ROOT_ARRAY);
				NameValuePair pair;
				if (rootAttrs != null && rootAttrs.size() > 0) {
					int attrCount = rootAttrs.size();
					// 遍历为根节点设置属性
					for (int i = 0; i < attrCount; i++) {
						pair = rootAttrs.get(i);
						xmlSerializer.attribute(namespace, pair.getName(),
								pair.getValue());
					}
				}
				
				//生成Header节点
				preGenerateHeaderXml(messages, xmlSerializer, namespace);
				
				//生成Message节点
				preGenerateMsgXml(messages, xmlSerializer, namespace);
				
				//生成根节点
				xmlSerializer.endTag(namespace, DataConstants.MSG_ROOT_ARRAY);
				xmlSerializer.endDocument();
				
				return writer.toString();
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
			return null;
			
		}else{
			return null;
		}
	}
	
	
	/**
	 * @param 
	 * @return List<NameValuePair>
	 * @description TODO
	 * @version 1.0
	 * @author wujian
	 * @date 2013-8-6 上午10:29:10
	 * @update 2013-8-6 上午10:29:10
	 */
	private List<NameValuePair> getUpgradeReportMsgAttrs(String type, String value, 
			long createdTime, int downLoadSize,long downloadCostTime,String downLoadUrl,String downloadIp,int dlCount){
		
		List<NameValuePair> nodeAttrs = new ArrayList<NameValuePair>();
		nodeAttrs.add(new BasicNameValuePair(DataConstants.MSG_TYPE, type));
		nodeAttrs.add(new BasicNameValuePair(DataConstants.MSG_VALUE, value));
		nodeAttrs.add(new BasicNameValuePair(DataConstants.MSG_CREATEDTIME, createdTime+""));
		nodeAttrs.add(new BasicNameValuePair(DataConstants.MSG_DOWNLOADSIZE, downLoadSize+""));
		nodeAttrs.add(new BasicNameValuePair(DataConstants.MSG_DOWNLOADTIME, downloadCostTime+""));
		nodeAttrs.add(new BasicNameValuePair(DataConstants.MSG_DOWNLOADURL, downLoadUrl+""));
		// LEE 2014.10.31
		nodeAttrs.add(new BasicNameValuePair(DataConstants.MSG_DOWNLOADIP, downloadIp+""));
		// LEE 2015.1.5
		nodeAttrs.add(new BasicNameValuePair(DataConstants.MSG_DLcount, dlCount+""));
		Utility.log("YP -->>  DataConstants.MSG_DLcount:"+dlCount);
		

		return nodeAttrs;
	}
	
	/**
	 * @param 
	 * @return List<NameValuePair>
	 * @description TODO
	 * @version 1.0
	 * @author wujian
	 * @date 2013-8-6 上午11:00:39
	 * @update 2013-8-6 上午11:00:39
	 */
	private List<NameValuePair> getUpgradeReportHeaderAttrs(String hid,String oemid,
			String uid,String appId,String appName,String appVersion,String packageName){
		
		List<NameValuePair> nodeAttrs = new ArrayList<NameValuePair>();
		nodeAttrs.add(new BasicNameValuePair(DataConstants.HEADER_HID, hid));
		nodeAttrs.add(new BasicNameValuePair(DataConstants.HEADER_OEMID, oemid));
		nodeAttrs.add(new BasicNameValuePair(DataConstants.HEADER_UID, uid));
		nodeAttrs.add(new BasicNameValuePair(DataConstants.HEADER_APPID, appId));
		nodeAttrs.add(new BasicNameValuePair(DataConstants.HEADER_APPNAME, appName));
		nodeAttrs.add(new BasicNameValuePair(DataConstants.HEADER_APPVERSION, appVersion));
		nodeAttrs.add(new BasicNameValuePair(DataConstants.HEADER_PACKAGENAME, packageName));

		return nodeAttrs;
		
	}
	
	/**
	 * @param 
	 * @description 生成Header节点xml
	 * @version 1.0
	 * @author wujian
	 * @date 2013-8-6 上午11:18:39
	 * @update 2013-8-6 上午11:18:39
	 */
	private void preGenerateHeaderXml(MessageArray messages,XmlSerializer xmlSerializer,
			String namespace) throws IllegalArgumentException, IllegalStateException, IOException{
		
		MsgXmlHelpInfo msgXmlHelpInfo = new MsgXmlHelpInfo();
		Header header = messages.getHeader();
		msgXmlHelpInfo.setNodeHeaderAttrs(this
				.getUpgradeReportHeaderAttrs(header.getHid(), header.getOemid(), header.getUid(),
						header.getAppId(), header.getAppName(), header.getAppVersion(), header.getPackageName()));
		MsgXml.generateHeaderXML(msgXmlHelpInfo.getNodeHeaderAttrs(), xmlSerializer, namespace);
	}
	
	/**
	 * @param 
	 * @description 生成Array节点xml
	 * @version 1.0
	 * @author wujian
	 * @date 2013-8-6 上午11:20:52
	 * @update 2013-8-6 上午11:20:52
	 */
	private void preGenerateMsgXml(MessageArray messages,XmlSerializer xmlSerializer,
			String namespace) throws Exception{
		
		xmlSerializer.startTag(namespace, DataConstants.MSG_ARRAY);
		for(Message msg : messages.getMessageArray()){
			MsgXmlHelpInfo msgXmlHelpInfo = new MsgXmlHelpInfo();
			msgXmlHelpInfo.setBodyContent(msg.getBodyContent());
			msgXmlHelpInfo.setNodeMsgAttrs(getUpgradeReportMsgAttrs(msg.getType(),
					msg.getValue(),
					msg.getCreatedTime(),
					msg.getDownLoadSize(),
					msg.getDownLoadTime(),
					msg.getDownLoadUrl(),
					msg.getDownLoadIp(),
					msg.getDLcount()
					));
			
			MsgXml.generateMsgXML(msgXmlHelpInfo.getBodyContent(), msgXmlHelpInfo.getNodeMsgAttrs(),
					xmlSerializer, namespace);
		}
		xmlSerializer.endTag(namespace, DataConstants.MSG_ARRAY);
	}
	
		
	/**
	 * @param root Body节点中的xml的根节点（StartDownload,DownloadSuccess, DownloadFailed, UpdateInstalled）
	 * @param newVersion 应用新版本号
	 * @param errorCode 异常状态码
	 * @param errorContent 具体异常内容
	 * @return String
	 * @description 生成Body中包含的xml
	 * @version 1.0
	 * @author wujian
	 * @date 2013-8-6 下午3:02:56
	 * @update 2013-8-6 下午3:02:56
	 */
	public String generateBodyContent(String root, String newVersion, String errorCode, 
			String errorContent){
		List<NameValuePair> rootAttrs = new ArrayList<NameValuePair>();
		rootAttrs.add(new BasicNameValuePair(DataConstants.MSG_BODYCONTENT_NEWVERSION, newVersion));
		if(errorCode != null && errorCode.trim().length() > 0){
			rootAttrs.add(new BasicNameValuePair(DataConstants.MSG_BODYCONTENT_ERRORCODE, errorCode));
		}
		return MsgXml.genBodyContentXML(root, rootAttrs, errorContent);
		
	}	
		
	/**
	 * @param version 应用版本好
	 * @param errCode 异常类型编码
	 * @param priority 异常优先级
	 * @param exceptionInfo 异常信息内容
	 * @return String
	 * @description 生成异常日志的xml
	 * @version 1.0
	 * @author wujian
	 * @date 2013-8-6 下午4:41:17
	 * @update 2013-8-6 下午4:41:17
	 */
	public String generateExceptionXml(String version, String errCode, String priority, String exceptionInfo){

		try {
			
			List<NameValuePair> rootAttrs = new ArrayList<NameValuePair>();
			rootAttrs.add(new BasicNameValuePair(DataConstants.EXCEPTIONTRACE_VERSION, version));
			rootAttrs.add(new BasicNameValuePair(DataConstants.EXCEPTIONTRACE_ERRCODE, errCode));
			rootAttrs.add(new BasicNameValuePair(DataConstants.EXCEPTIONTRACE_PRIORITY, priority));
			
			XmlSerializer xmlSerializer = Xml.newSerializer();
			StringWriter writer = new StringWriter();
			String namespace = null;
			xmlSerializer.setOutput(writer);
			xmlSerializer.startDocument(DataConstants.ENCODING, false);
			
			// 开始根节点
			xmlSerializer.startTag(namespace, DataConstants.EXCEPTIONTRACE_ROOT);
			CommonXml.generateAttrs(namespace, xmlSerializer, rootAttrs);
			
			//ExcepInfo节点
			xmlSerializer.startTag(namespace, DataConstants.EXCEPINFO_ROOT);
//			xmlSerializer.cdsect(exceptionInfo);
			xmlSerializer.text(exceptionInfo);
			xmlSerializer.endTag(namespace, DataConstants.EXCEPINFO_ROOT);
			
			// 结束根节点
			xmlSerializer.endTag(namespace, DataConstants.EXCEPTIONTRACE_ROOT);
			xmlSerializer.endDocument();
			
			return writer.toString();
			
		}catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
		
	/**
	 * @param logInfo Log日志内容
	 * @return String
	 * @description 生成Log日志的xml
	 * @version 1.0
	 * @author wujian
	 * @date 2013-8-6 下午4:51:29
	 * @update 2013-8-6 下午4:51:29
	 */
	public String generateLogTraceXml(String logInfo){
		
		try {
			
			XmlSerializer xmlSerializer = Xml.newSerializer();
			StringWriter writer = new StringWriter();
			String namespace = null;
			xmlSerializer.setOutput(writer);
			xmlSerializer.startDocument(DataConstants.ENCODING, false);
			
			xmlSerializer.startTag(namespace, DataConstants.LOGTRACE_ROOT);
			xmlSerializer.text(logInfo);
			xmlSerializer.endTag(namespace, DataConstants.LOGTRACE_ROOT);
			
			xmlSerializer.endDocument();
			
			return writer.toString();
			
		}catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
}
