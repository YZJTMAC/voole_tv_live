package com.voole.statistics.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.voole.statistics.bean.DataResultBean;
import com.voole.statistics.bean.HeaderBean;
import com.voole.statistics.bean.PlayerMessageArrayBean;
import com.voole.statistics.bean.PlayerMessageBean;
import com.voole.statistics.parse.PageMessageParse;
import com.voole.statistics.util.StringPrint;
import com.voole.statistics.util.StringXMLUtil;



/**
 * 播放状态统计
 * @author Jacky
 *
 */
public class StatisticsPlayerStatusService 
{
	/**
	 * 实例页面统计服务类对象
	 */
	private static StatisticsPlayerStatusService instance = null;
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
	public static StatisticsPlayerStatusService getInstance(){
		if (instance == null) {
			instance = new StatisticsPlayerStatusService();
		}
		return instance;
	}
	/**
	 * 构造方法
	 */
	private StatisticsPlayerStatusService (){
		
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
		   this.url=url;
	  }
	  
	  
	  /**
	   * 发送播放统计消息
	   * @param action
	   * @param fid
	   * @param playtime
	   * @param sessionid
	   */
	  public void transferPlayerMessage(String action, String fid, String playtime,
			  String sessionid ) {
		  
		  
		  PlayerMessageBean  playerMessageBean=new PlayerMessageBean();
		  playerMessageBean.setAction(action);
		  playerMessageBean.setFid(fid);
		  playerMessageBean.setPlaytime(playtime);
		  playerMessageBean.setSessionid(sessionid);
			
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
			 xml=StringXMLUtil.createPlayerStatusXml(playerMessageArrayBean);
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
			
			
			StringPrint.print("结果=="+strResult);
		
		
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
