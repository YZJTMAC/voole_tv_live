package com.voole.statistics.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.voole.statistics.bean.HeaderBean;
import com.voole.statistics.bean.PlayerMessageArrayBean;
import com.voole.statistics.bean.PlayerMessageBean;
import com.voole.statistics.config.Config;
import com.voole.statistics.parse.PageMessageParse;
import com.voole.statistics.util.StringPrint;
import com.voole.statistics.util.StringXMLUtil;

/**
 * 播放服务整合类
 *   里面包含了主要的四个方法
 *     1. 发送播放异常消息 transferPlayerExceptionMessage
 *     2. 发送播放状态统计消息 transferPlayerStatusMessage
 *     3. 发送播放统计消息 transferPlayerMessage
 *     4. 发送起播阶段统计消息 transferStartPlayerMessage
 * @author jacky
 *
 */
public class StatisticsPlayerService {

	/**
	 * 播放服务整合类
	 */
	private static StatisticsPlayerService instance = null;
	/**
	 * 头信息BEAN
	 */
	private HeaderBean headerBean=null;
	/**
	 * 积累发送的条目数
	 */
	private int maxMsgCount=1;
	
	
	
	/** 存放所有产生的消息 **/
	private   List<PlayerMessageBean> playerMessageBeanList = new ArrayList<PlayerMessageBean>();
	/** 存放待发送的消息 **/
	private  List<PlayerMessageBean> tempMessageList = new ArrayList<PlayerMessageBean>();
	
	/** 提交消息数据的接口地址 **/
	private  String url = null; 
	
	
	
	/**
	 * 获得页面统计服务类的实例
	 * @return StatisticsPageService的实例
	 */
	public static StatisticsPlayerService getInstance(){
		if (instance == null) {
			instance = new StatisticsPlayerService();
		}
		return instance;
	}
	/**
	 * 构造方法
	 */
	private StatisticsPlayerService (){
		
	}
	
	
	
	/**
	 * 初始化页面统计服务类
	 * @param hid   终端硬件设备编号
	 * @param oemid 项目编号
	 * @param uid 用户唯一编号
	 * @param appId 应用唯一编号
	 * @param appVersion 应用版本号
	 * @param packageName 标识应用
	 * @param url  提交统计数据的接口地址
	 * @param maxMsgCount 积累多少条数据后发送,例如如果数字为10的话，就是积累够10条页面操作信息了，再请求服务器传送统计信息
	 */
	
	  public void initBasicData(String hid, String oemid, String uid,
				String appId, String appVersion, String packageName, String url, int maxMsgCount)
	  {
		   headerBean=new HeaderBean();
		   headerBean.setHid(hid);
		   headerBean.setOemid(oemid);
		   headerBean.setUid(uid);
		   headerBean.setAppId(appId);
		   headerBean.setAppVersion(appVersion);
		   headerBean.setPackageName(packageName);
		   if(maxMsgCount > 0){
			   this.maxMsgCount=maxMsgCount;
			}
	       if(-1!=url.indexOf("?"))
		   {
			   this.url=url;
		   }
		   else
		   {
			   //拼串
			   StringBuffer httpUrlSendSB = new StringBuffer();
				if (null != url && !"".equals(url.trim())) {
					httpUrlSendSB.append(url.trim() + "?");
				}
				httpUrlSendSB.append("action=PlayerReport");
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
				this.url = httpUrlSendSB.toString();
				StringPrint.print(" url==" + this.url);
			  
		   }
		   
		   
	  }
	  
	  
	  /**
	   *  发送播放异常消息
	   * @param action
	   * @param fid
	   * @param duration
	   * @param sessionid
	   */
	  public void transferPlayerExceptionMessage(String errcode, String info, String fid,String playtime,String sessionid) {
			
		  
		  PlayerMessageBean  playerMessageBean=new PlayerMessageBean();
		  playerMessageBean.setErrcode(errcode);
		  playerMessageBean.setFid(fid);
		  playerMessageBean.setSessionid(sessionid);
		  playerMessageBean.setInfo(info);
		  playerMessageBean.setPlaytime(playtime);
		  playerMessageBean.setPlayerMessageType(PlayerMessageBean.PLAYER_MESSAEG_TYPE_EXCETPION);
		  playerMessageBeanList.add(playerMessageBean);
		 
			//message个数超过规定的最大值将发送messageLst中的消息
			if(playerMessageBeanList.size() >= maxMsgCount){
				tempMessageList.addAll(playerMessageBeanList);
				playerMessageBeanList.clear();
				reportMessages();
			}
		}
	  
	  
	  
	  /**
	   * 发送播放状态统计消息
	   * @param action
	   * @param fid
	   * @param playtime
	   * @param sessionid
	   */
	  public void transferPlayerStatusMessage(String action, String fid, String playtime,
			  String sessionid ) {
		  
		  
		  PlayerMessageBean  playerMessageBean=new PlayerMessageBean();
		  playerMessageBean.setAction(action);
		  playerMessageBean.setFid(fid);
		  playerMessageBean.setPlaytime(playtime);
		  playerMessageBean.setSessionid(sessionid);
		  playerMessageBean.setPlayerMessageType(PlayerMessageBean.PLAYER_MESSAEG_TYPE_STATUS);
		  playerMessageBeanList.add(playerMessageBean);
			
			//message个数超过规定的最大值将发送messageLst中的消息
			if(playerMessageBeanList.size() >= maxMsgCount){
				tempMessageList.addAll(playerMessageBeanList);
				playerMessageBeanList.clear();
				reportMessages();
			}
		}
	  
	  /**
	   * 发送播放统计消息
	   * @param action
	   * @param fid
	   * @param playtime
	   * @param seektime
	   * @param sessionid
	   * @param videoName
	   */
	  public void transferPlayerMessage(String action, String fid, String playtime, String seektime,
			  String sessionid,String videoName) {
		  
		  
		  PlayerMessageBean  playerMessageBean=new PlayerMessageBean();
		  playerMessageBean.setAction(action);
		  playerMessageBean.setFid(fid);
		  playerMessageBean.setPlaytime(playtime);
		  playerMessageBean.setSeektime(seektime);
		  playerMessageBean.setSessionid(sessionid);
		  playerMessageBean.setVideoName(videoName);
		  playerMessageBean.setPlayerMessageType(PlayerMessageBean.PLAYER_MESSAEG_TYPE_PLAYER);
		  playerMessageBeanList.add(playerMessageBean);
			
			//message个数超过规定的最大值将发送messageLst中的消息
			if(playerMessageBeanList.size() >= maxMsgCount){
				tempMessageList.addAll(playerMessageBeanList);
				playerMessageBeanList.clear();
				reportMessages();
			}
		}

	  /**
	   * 发送起播阶段统计消息
	   * @param action
	   * @param fid
	   * @param duration
	   * @param sessionid
	   */
	  public void transferStartPlayerMessage(String action, String fid, String duration,String sessionid) {
		  

		
		  PlayerMessageBean  playerMessageBean=new PlayerMessageBean();
		  playerMessageBean.setAction(action);
		  playerMessageBean.setFid(fid);
		  playerMessageBean.setSessionid(sessionid);
		  playerMessageBean.setDuration(duration);
		  playerMessageBean.setPlayerMessageType(PlayerMessageBean.PLAYER_MESSAEG_TYPE_STARTPLAYER);
		  playerMessageBeanList.add(playerMessageBean);
			
			//message个数超过规定的最大值将发送messageLst中的消息
			if(playerMessageBeanList.size() >= maxMsgCount){
				tempMessageList.addAll(playerMessageBeanList);
				playerMessageBeanList.clear();
				reportMessages();
			}
		}
	  
	  
	  /**
	   * 发送消息
	   */
	private void reportMessages() {
		
		
		final PlayerMessageArrayBean playerMessageArrayBean=initStatisticsData(tempMessageList, headerBean);
		//启动一个线程发送消息
		new Thread(){
			public void run() {
				sendMessage(playerMessageArrayBean);
			}
		}.start();
	}
	
	
	/**
	 * 生成xml数据
	 */
	private String initXMLData(PlayerMessageArrayBean playerMessageArrayBean) {
		 String xml=null;
		 if(null!=playerMessageArrayBean)
		 {
			 xml=StringXMLUtil.createPlayerAllXml(playerMessageArrayBean);
		 }
		 return xml;
	}
	/**
	 * 发送数据
	 */
	private void sendMessage(PlayerMessageArrayBean playerMessageArrayBean) {
		String strResult=null;
		PageMessageParse pageMessageParse=new PageMessageParse();
		try {
			String xmlParam=initXMLData(playerMessageArrayBean);
			strResult= pageMessageParse.parse(url, xmlParam);
			
			StringPrint.print("播放结果=="+strResult);
		
		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			 tempMessageList.clear();
		}
	}
	
	/**
	 * 初始化数据进入bean
	 * @param tempMessageList
	 * @param headerBean
	 * @return
	 */
	public PlayerMessageArrayBean initStatisticsData(List<PlayerMessageBean> tempMessageList, HeaderBean  headerBean) {

		PlayerMessageArrayBean playerMessageArrayBean = new PlayerMessageArrayBean();
		playerMessageArrayBean.setHeaderBean(headerBean);
		playerMessageArrayBean.setPlayerMessageBeanList(tempMessageList);
		playerMessageArrayBean.setCount(tempMessageList.size());
		
		return playerMessageArrayBean;
	}

}
