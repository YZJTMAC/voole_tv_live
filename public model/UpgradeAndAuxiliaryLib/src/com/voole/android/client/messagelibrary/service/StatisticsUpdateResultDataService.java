package com.voole.android.client.messagelibrary.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.voole.android.client.UpAndAu.constants.AuxiliaryConstants;
import com.voole.android.client.UpAndAu.downloader.Utility;
import com.voole.android.client.messagelibrary.model.DataResult;
import com.voole.android.client.messagelibrary.model.Header;
import com.voole.android.client.messagelibrary.model.Message;
import com.voole.android.client.messagelibrary.model.MessageArray;
import com.voole.android.client.messagelibrary.model.parser.DataConstants;
import com.voole.android.client.messagelibrary.model.parser.VooleData;
import com.voole.android.client.messagelibrary.utils.HttpUtil;
import com.voole.android.client.messagelibrary.utils.Logger;
import com.voole.android.client.messagelibrary.utils.NetUtil;
import com.voole.android.client.messagelibrary.utils.StringUtil;

/**
 *  统计升级相关数据服务类 
 * @author wujian
 *
 */
public class StatisticsUpdateResultDataService {

	private static StatisticsUpdateResultDataService instance = null;
	private Context context;
	
	/** 存放所有产生的消息 **/
	private static List<Message> messageList = new ArrayList<Message>();
	/** 存放待发送的消息 **/
	private  List<Message> tempMessageList = new ArrayList<Message>();
	private  Header header = new Header();
	/** 提交消息数据的接口地址 **/
	private  String URL; 
	private static final int MAX_MESSAGES_COUNT = 1; 
	
	/** 存放消息文件的文件夹名称 **/
	private static final String MESSAGES_FOLDER_NAME = "updateResultMessages";
	private File messagesDirPath = null;
	
	/**开始下载时间*/
	private long startDownLoadTime=0;
	/**结束下载时间*/
	private long endDownLoadTime = 0;
	private long downLoadTime=0;
	
	

	private StatisticsUpdateResultDataService(){
		initBasicData();
	}
	
	private StatisticsUpdateResultDataService(Context context){
		this.context = context;
		initBasicData();
	}
	
	public static StatisticsUpdateResultDataService getInstance(){
		if(instance == null){
			instance = new StatisticsUpdateResultDataService();
		} else {
			instance.initBasicData();
		}
		return instance;
	}
	
	public static StatisticsUpdateResultDataService getInstance(Context context){
		if(instance == null){
			instance = new StatisticsUpdateResultDataService(context);
		}
		return instance;
	}
	
	/**
	 * @param messageList
	 * @param header
	 * @return MessageArray
	 * @description 初始化统计数据
	 * @version 1.0
	 * @author wujian
	 * @date 2013-8-7 上午9:53:19
	 * @update 2013-8-7 上午9:53:19
	 */
	public MessageArray initStatisticsData(List<Message> messageList, Header header) {

		MessageArray messages = new MessageArray();
		messages.setHeader(header);
		messages.setMessageArray(messageList);
		messages.setCount(messageList.size());
		return messages;
	}

	/**
	 * @description 初始化基本必须参数（在生成message之前执行）
	 * @version 1.0
	 * @author wujian
	 * @date 2013-8-7 下午5:19:14
	 * @update 2013-8-7 下午5:19:14
	 */
	private void initBasicData() {

		header.setAppId(AuxiliaryConstants.appId == null ? "" : AuxiliaryConstants.appId);
		header.setAppName("appName");
		header.setAppVersion(AuxiliaryConstants.apkCurrentVersion == null ? "" : AuxiliaryConstants.apkCurrentVersion);
		header.setHid(AuxiliaryConstants.hid == null ? "" : AuxiliaryConstants.hid);
		header.setOemid(AuxiliaryConstants.oemId == null ? "" : AuxiliaryConstants.oemId);
		header.setPackageName(AuxiliaryConstants.packagename == null ? "" : AuxiliaryConstants.packagename);
		header.setUid(AuxiliaryConstants.uid==null ? "":AuxiliaryConstants.uid);
		prepareUrl(AuxiliaryConstants.Host);
	}
	
	
	private void prepareUrl(String host) {
		URL = AuxiliaryConstants.Host + "/interface/appplatform_PageActionReport_actionReport.htm?" +
				"oemid=" + header.getOemid() + "&type=text&" + "version=" + header.getAppVersion() + 
				"&packagename=" + header.getPackageName() + "&hid=" + header.getHid() + "&appId=" + header.getAppId()+"&sendtime="+AuxiliaryConstants.sendtime;
		
		/*if(host.equals(AuxiliaryConstants.Host)) {
			URL = host + "/interface/appplatform_PageActionReport_actionReport.htm?" +
					"oemid=" + header.getOemid() + "&type=text&" + "version=" + header.getAppVersion() + 
					"&packagename=" + header.getPackageName() + "&hid=" + header.getHid() + "&appId=" + header.getAppId()+"&sendtime="+AuxiliaryConstants.sendtime;
		} else {
			URL = host + "/mobilelog.do?" +
					"oemid=" + header.getOemid() + "&type=text&" + "version=" + header.getAppVersion() + 
					"&packagename=" + header.getPackageName() + "&hid=" + header.getHid() + "&appId=" + header.getAppId()+"&sendtime="+AuxiliaryConstants.sendtime;
		}*/
		
	}
	
	/**
	 * <?xml version='1.0' encoding='utf-8' standalone='no' ?>
	 * <MessageArray count="1">
	 * <Header hid="j8fewjwei" oemid="772" uid="uid" appId="60" appName="appName" appVersion="" packageName="com.voole.magictv" />
	 * <Array>
	 * <Message type="updateDownload" value="DownloadSuccess" createdTime="1388396308509">
	 * <Body><![CDATA[<?xml version='1.0' encoding='utf-8' standalone='no' ?>
	 * <DownloadSuccess newVersion="" />]]>
	 * </Body></Message>
	 * </Array>
	 * </MessageArray>
	 * @param apkCurrentVersion
	 * @param oemId
	 * @param packagename
	 * @param appId
	 * @param hid
	 * @param sn
	 * @param localIp
	 * @description something
	 * @version 1.0
	 * @author LEE
	 * @date 2014-3-19 上午11:52:40 
	 * @update 2014-3-19 上午11:52:40
	 */
/*	public void initBasicData(String apkCurrentVersion,String oemId,String packagename,String appId,String hid,String sn,String localIp) {
		header.setAppId(appId == null ? "" : appId);
		header.setAppName("appName");
		header.setAppVersion(apkCurrentVersion == null ? "" : apkCurrentVersion);
		header.setHid(hid == null ? "" : hid);
		header.setOemid(oemId == null ? "" : oemId);
		header.setPackageName(packagename == null ? "" : packagename);
		header.setUid("");
		prepareUrl(AuxiliaryConstants.HostLog);
	}*/
	
/*	public String getRequestUrl() {
		prepareUrl(AuxiliaryConstants.HostLog);
		return URL;
	}*/
	
	
	/**
	 * @param value  Body节点中的xml的根节点（InstallSuccess,UnInstallSuccess, ActivateSuccess）
	 * @description 传递生成的消息进行处理 激活，安装，卸载。
	 * @version 1.0
	 * @author LEE
	 * @date 2013-8-7 下午5:10:45
	 * @update 2013-8-7 下午5:10:45
	 */
	public void transferMessageForInstActiUnInst(String value) {
		//prepareUrl(AuxiliaryConstants.HostLog);
		if(!StringUtil.isNotNull(value)) return;
		Message message = new Message();
		message.setType(DataConstants.INSTALLANDACTIVATE_ROOT);
		message.setValue(value);
		message.setCreatedTime(System.currentTimeMillis());
		message.setDownLoadSize(-1);
		String bodyContent = VooleData.getInstance().generateBodyContent(value,
				AuxiliaryConstants.apkCurrentVersion, "",
				null);
		message.setBodyContent(bodyContent == null ? "" : bodyContent);
		messageList.add(message);
		
		//message个数超过规定的最大值将发送messageLst中的消息
		if(messageList.size() >= MAX_MESSAGES_COUNT){
			tempMessageList.addAll(messageList);
			messageList.clear();
			reportMessages();
		}
	}
	
	public String getReportData(String value) {
		if(!StringUtil.isNotNull(value)) return null;
		Message message = new Message();
		message.setType(DataConstants.INSTALLANDACTIVATE_ROOT);
		message.setValue(value);
		message.setCreatedTime(System.currentTimeMillis());
		message.setDownLoadSize(-1);
		String bodyContent = VooleData.getInstance().generateBodyContent(value,
				AuxiliaryConstants.apkCurrentVersion, "",
				null);
		message.setBodyContent(bodyContent == null ? "" : bodyContent);
		messageList.add(message);
		
		//message个数超过规定的最大值将发送messageLst中的消息
		if(messageList.size() >= MAX_MESSAGES_COUNT){
			tempMessageList.addAll(messageList);
			messageList.clear();
			reportMessages();
		}
		MessageArray messages = initStatisticsData(tempMessageList,header);
		
		String params = VooleData.getInstance().generateStatisticsData(messages);
		
		tempMessageList.clear();
		return params;
	}
	
	
	/**
	 * @param value  Body节点中的xml的根节点（StartDownload,DownloadSuccess, DownloadFailed, UpdateInstalled）
	 * @param errorContent 具体异常内容
	 * @param downLoadSize 已下载文件的大小（下载新版本时传值，其他消息为-1）
	 * @description 传递生成的消息进行处理
	 * @version 1.0
	 * @author wujian
	 * @date 2013-8-7 下午5:10:45
	 * @update 2013-8-7 下午5:10:45
	 */
	public void transferMessage(String value, String errorContent, int downLoadSize) {
		AuxiliaryConstants.DLcount+=1;
		Utility.log("YP -->>  transferMessage type:"+value);
		Utility.log("YP -->>  DLcount:"+AuxiliaryConstants.DLcount);
		if(DataConstants.STARTDOWNLOAD_ROOT.equals(value)) {
			startDownLoadTime = System.currentTimeMillis();
			downLoadTime = 0;
		}
		if(DataConstants.DOWNLOADSUCCESS_ROOT.equals(value) 
				|| DataConstants.DOWNLOADFAILED_ROOT.equals(value)
				|| DataConstants.DOWNLOADFAILED_NODEVICE.equals(value)) {
			endDownLoadTime = System.currentTimeMillis();
			downLoadTime = endDownLoadTime - startDownLoadTime;
		}
		
		
		prepareUrl(AuxiliaryConstants.Host);
		if(!StringUtil.isNotNull(value)) return;
		Message message = new Message();
		message.setType("updateDownload");
		message.setValue(value);
		message.setDownLoadTime(downLoadTime);
		message.setDownLoadUrl(AuxiliaryConstants.downLoadUrl);
		message.setDownLoadIp(AuxiliaryConstants.downLoadIp);
		message.setDLcount(AuxiliaryConstants.DLcount);
		message.setCreatedTime(System.currentTimeMillis());
		message.setDownLoadSize(downLoadSize);
		String bodyContent = VooleData.getInstance().generateBodyContent(value,
				AuxiliaryConstants.apkCurrentVersion, "",
				errorContent);
		message.setBodyContent(bodyContent == null ? "" : bodyContent);
		messageList.add(message);
		
		//message个数超过规定的最大值将发送messageLst中的消息
		if(messageList.size() >= MAX_MESSAGES_COUNT){
			tempMessageList.addAll(messageList);
			reportMessages();
		}
	}
	
	/**
	 * @param 
	 * @description 发送messages
	 * @version 1.0
	 * @author wujian
	 * @date 2013-8-7 下午5:32:13
	 * @update 2013-8-7 下午5:32:13
	 */
	private void reportMessages(){
		
		if(tempMessageList != null && tempMessageList.size() > 0){
			Utility.log("YP -->> report msg size: "+tempMessageList.size());
//			Bundle bundle = new Bundle();
//			bundle.putSerializable("MessageArray", initStatisticsData(tempMessageList,header));
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					Utility.log("YP -->> start thread and report msg........ ");
					MessageArray messages = initStatisticsData(tempMessageList,header);
					DataResult result = reportStatisticsData(messages, URL);
					tempMessageList.clear();
					messageList.clear();
				}
			}).start();
//			DataAsyncTask reportTask = new DataAsyncTask();
//			reportTask.execute(bundle);
		} else {
			Utility.log("YP -->> no msg reported ");
		}
	}
	
	/**
	 * 提交统计数据异步任务
	 * @author wujian
	 *
	 */
	class DataAsyncTask extends AsyncTask<Bundle, Integer, Bundle>{

		@Override
		protected Bundle doInBackground(Bundle... params) {
			MessageArray messages = (MessageArray) params[0].getSerializable("MessageArray");
			DataResult result = reportStatisticsData(messages, URL);
			if(result == null){
				result = new DataResult();
			}
			Bundle b = new Bundle();
			b.putSerializable("DataResult", result);
			
			return b;
		}
		
		@Override
		protected void onPostExecute(Bundle b) {
			DataResult result = (DataResult) b.get("DataResult");
			tempMessageList.clear();
		}
	}
	
	/**
	 * 提交从本地取出的统计数据的异步任务
	 * @author wujian
	 *
	 */
	class FileAsyncTask extends AsyncTask<Bundle, Integer, Bundle>{

		@Override
		protected Bundle doInBackground(Bundle... params) {
			
			String messagesStr = params[0].getString("messagesStr");
			DataResult dataResult = null;
			if (NetUtil.isNetworkConnected(context)) {
				InputStream in = null;
				HttpUtil httpUtil = new HttpUtil();
				try {
					in = httpUtil.doPost(URL, messagesStr, -1, -1, null);
					dataResult = VooleData.getInstance().ParseDataResult(in);
					if(dataResult == null){
						dataResult = new DataResult();
					}
				}finally {
					httpUtil.closeInputStream(in);
				}
			}
			Bundle b = new Bundle();
			b.putSerializable("DataResult", dataResult);
			return b;
		}
		
		@Override
		protected void onPostExecute(Bundle b) {
			DataResult result = (DataResult) b.get("DataResult");
			if("0".equals(result.getResultCode())){
				clearFile();
			}
		}
	}
	
	/**
	 * @param messages
	 * @param url
	 * @return DataResult
	 * @description TODO
	 * @version 1.0
	 * @author wujian
	 * @date 2013-8-5 下午5:55:42
	 * @update 2013-8-5 下午5:55:42
	 */
	public DataResult reportStatisticsData(MessageArray messages,String url){
		if (!StringUtil.isNotNull(url))
			return null;
		// 提交返回的结果
		DataResult dataResult = null;
		if (messages != null){
			InputStream in = null;
				HttpUtil httpUtil = new HttpUtil();
				try {
					//生成要提交的统计数据
					String params = VooleData.getInstance().generateStatisticsData(messages);
					
					Utility.log("YP -->> report upgrade condition url : "+url);
					
					Utility.log("YP -->> report msg is :"+params);
					
					in = httpUtil.doPost(url, params, -1, -1, null);
					dataResult = VooleData.getInstance().ParseDataResult(in);
					//提交数据失败，将数据保存到本地
					if(dataResult == null || !"0".equals(dataResult.getResultCode())){
						saveStatisticsData(params);
						Utility.log("YP -->> report upgrade result failed ");
					} else {
						Utility.log("YP -->> report upgrade result success ");
					}
				} catch (Exception e) {
					Utility.log("YP -->> report upgrade result failed error: ");
					e.printStackTrace();
				}finally {
					httpUtil.closeInputStream(in);
				}
		}
		return dataResult;
				
	}
	
	/**
	 * @param url 提交消息的接口地址
	 * @description 扫描保存在本地的消息文件并提交到服务端
	 * @version 1.0
	 * @author wujian
	 * @date 2013-8-8 上午10:58:34
	 * @update 2013-8-8 上午10:58:34
	 */
	public void scanFileAndSend2Server(String url){
		
		URL = url;
		String messagesStr = readStatisticData();
		if(messagesStr != null && messagesStr.length() > 0){
			Bundle bundle = new Bundle();
			bundle.putString("messagesStr", messagesStr);
			FileAsyncTask fileAsyncTask = new FileAsyncTask();
			fileAsyncTask.execute(bundle);
		}
	} 
	
	
	/**
	 * @param data 未成功发送的统计数据
	 * @description 将统计数据保存到本地
	 * @version 1.0
	 * @author wujian
	 * @date 2013-8-7 上午11:30:16
	 * @update 2013-8-7 上午11:30:16
	 */
	public void saveStatisticsData(String data){
		if(context == null)return ;
		try {
			messagesDirPath = context.getDir(MESSAGES_FOLDER_NAME, Context.MODE_PRIVATE);
			String path = messagesDirPath.getAbsolutePath();
			File messagesFile = new File(path+"/"+System.currentTimeMillis() + ".txt");
			FileOutputStream fos = new FileOutputStream(messagesFile);
			fos.write(data.getBytes());
			fos.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return String
	 * @description 从本地读取统计数据
	 * @version 1.0
	 * @author wujian
	 * @date 2013-8-7 下午2:10:28
	 * @update 2013-8-7 下午2:10:28
	 */
	public String readStatisticData(){
		
		String messagesStr = null;
		try {
			messagesDirPath = context.getDir(MESSAGES_FOLDER_NAME, Context.MODE_PRIVATE);
			if(messagesDirPath != null && messagesDirPath.list().length > 0){
				StringBuilder sb = new StringBuilder("");
				for(int i=0; i<messagesDirPath.list().length; i++){
					File file = new File(messagesDirPath.getAbsolutePath()+"/"+messagesDirPath.list()[i]);
					FileInputStream fis = new FileInputStream(file);
					byte[] buffer = new byte[1024];
					int hasRead = 0;
					while((hasRead = fis.read(buffer)) > 0){
						sb.append(new String(buffer, 0 , hasRead));
					}
				}
				messagesStr = sb.toString();
			}
			
		} catch (Exception e) {
			e.printStackTrace();		
		}
		
		return messagesStr;
	}
	
	/**
	 * @description 删除文件
	 * @version 1.0
	 * @author wujian
	 * @date 2013-8-8 下午2:13:57
	 * @update 2013-8-8 下午2:13:57
	 */
	public void clearFile(){
		messagesDirPath = context.getDir(MESSAGES_FOLDER_NAME, Context.MODE_PRIVATE);
		if(messagesDirPath != null && messagesDirPath.list().length > 0){
			for(int i=0; i<messagesDirPath.list().length; i++){
				File file = new File(messagesDirPath.getAbsolutePath()+"/"+messagesDirPath.list()[0]);
				file.delete();
			}
		}
	}
}
