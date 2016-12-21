package com.voole.statistics.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.voole.statistics.bean.ConfigFileBean;
import com.voole.statistics.bean.HeaderBean;
import com.voole.statistics.bean.TeriminalInfoMessageArrayBean;
import com.voole.statistics.bean.TerminalInfoMessageBean;
import com.voole.statistics.config.Config;
import com.voole.statistics.parse.PageMessageParse;
import com.voole.statistics.reback.StatisticsTerminalInfoBack;
import com.voole.statistics.userinfo.IndexParse;
import com.voole.statistics.userinfo.Proxy;
import com.voole.statistics.util.MyFileRead;
import com.voole.statistics.util.StringPrint;
import com.voole.statistics.util.StringXMLUtil;

/**
 * B/S 终端信息统计服务
 * 
 * @author Jacky
 * 
 */
public class BSStatisticsTerminalInfoService {
	/**
	 * 实例页面统计服务类对象
	 */
	private static BSStatisticsTerminalInfoService instance = null;
	/**
	 * 头信息BEAN
	 */
	private HeaderBean headerBean = null;
	/**
	 * 积累发送的条目数
	 */
	private static final int maxMsgCount = 1;



	/** 提交消息数据的接口地址 **/
	private String url1 = null;
	private String url2 = null;
	
	private ConfigFileBean cfb;
	

	private static final String TYPE1 = "AppinfoReport";// 版本信息
	private static final String TYPE2 = "AppconfigReport";// 配置文件信息
	
	private String oemid;
	
	/**
	 * 获得页面统计服务类的实例
	 * 
	 * @return StatisticsPageService的实例
	 */
	public static BSStatisticsTerminalInfoService getInstance() {
		if (instance == null) {
			instance = new BSStatisticsTerminalInfoService();
		}
		return instance;
	}

	/**
	 * 构造方法
	 */
	private BSStatisticsTerminalInfoService() {
		
	}

	/**
	 * 初始化接口
	 * 
	 * @param url
	 *            请求地址 例如：http://epglog.voole.com/apklog-vooleplay-1.do
	 * @param oemid
	 *            终端类型 435 必须赋值
	 * @param mac
	 *            当前mac地址，优先取有线 asdfg123456 必须赋值
	 * @param appId
	 *            应用唯一id abc12345 有oemid和packagename时，appid可以为-1
	 * @param versioncode
	 *            manifest.xml配置的VersionCode 必须赋值
	 * @param packageName
	 *            应用包名 com.voole.debugTraceReport 必须赋值
	 * @param appversion
	 *            应用的版本 2.1.0 必须赋值
	 * @param system
	 *            操作系统版本 android 4.0 必须赋值
	 * @param chip
	 *            芯片 Hi3716m 必须赋值
	 * @param product
	 *            机型 有则赋值
	 * @param configFileSystemAddress
	 *            创维的配置文件地址 如果没有请传null
	 *            
	 */
	public void initBasicData(final String urlBase,
			final String appId,
			final String appversion, final String chip,final Context context,final StatisticsTerminalInfoBack statisticsTerminalInfoBack,final String oemid) {
 	
		   
	       setOemid(oemid);
		
		new Thread(){
 		
 		
 		
 		
 		
	   public void run() {
		   
		   
//		   读取配置文件取得端口号
		   
		   MyFileRead mf=new MyFileRead();
		   
		   
		       cfb=mf.getConfigFileAssets("voolert.conf", context);
	
		   
		   
				StringBuffer httpUrlSendSB1 = new StringBuffer();
				StringBuffer httpUrlSendSB2 = new StringBuffer();
				if (null != urlBase && !"".equals(urlBase.trim())) {
					httpUrlSendSB1.append(urlBase.trim() + "?");
					httpUrlSendSB2.append(urlBase.trim() + "?");
				}
				httpUrlSendSB1.append("action=" + TYPE1);
				httpUrlSendSB2.append("action=" + TYPE2);
				
			
				
				if (null != oemid && !"".equals(oemid.trim())) {
					httpUrlSendSB1.append("&oemid=" + oemid.trim());
					httpUrlSendSB2.append("&oemid=" + oemid.trim());
				}
				String mac=getMacAddress();
				/*String[] di=getDeviceInfo(context);
				if(null!=di&&di.length>0)
				{
					mac=di[0];
				}*/
				if (null != mac && !"".equals(mac.trim())) {
					httpUrlSendSB1.append("&mac=" + mac.trim());
					httpUrlSendSB2.append("&mac=" + mac.trim());
				}
				if (null != appId && !"".equals(appId.trim())) {
					httpUrlSendSB1.append("&appid=" + appId.trim());
					httpUrlSendSB2.append("&appid=" + appId.trim());
				}
		 
				 
					//VersionCode取值本地
					String versionCode=getVersion(context);
					if(null!=versionCode&&!"".equals(versionCode))
					{ 
						httpUrlSendSB1.append("&versioncode=" + versionCode.trim());
						httpUrlSendSB2.append("&versioncode=" + versionCode.trim());
					}
					
				 
					//VersionCode取值本地
					String packageNamed=getPackageName(context);
					if(null!=packageNamed&&!"".equals(packageNamed.trim()))
					{ 
						httpUrlSendSB1.append("&packagename=" + packageNamed.trim());
						httpUrlSendSB2.append("&packagename=" + packageNamed.trim());
					}
				
				
				
				if (null != appversion && !"".equals(appversion.trim())) {
					httpUrlSendSB1.append("&appversion=" + appversion.trim());
					httpUrlSendSB2.append("&appversion=" + appversion.trim());
				}
			 
					//VersionCode取值本地
					String systemTemp=getSystem();
					if(null!=systemTemp&&!"".equals(systemTemp.trim()))
					{ 
						httpUrlSendSB1.append("&system=" + systemTemp.trim());
						httpUrlSendSB2.append("&system=" + systemTemp.trim());
					}
				 
				if (null != chip && !"".equals(chip.trim())) {
					//httpUrlSendSB1.append("&chip=" + chip.trim());
					//httpUrlSendSB2.append("&chip=" + chip.trim());
					
					String chipTemp=null;
					try {
						chipTemp = URLEncoder.encode(chip,"UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					httpUrlSendSB1.append("&chip=" + chipTemp);
					httpUrlSendSB2.append("&chip=" + chipTemp);
				}
				
				
				
				
				 String product=getModel();
				if (null != product && !"".equals(product.trim())) {
					httpUrlSendSB1.append("&product=" + product.trim());
					httpUrlSendSB2.append("&product=" + product.trim());
				}
				
				
				
				httpUrlSendSB1.append("&version=" + Config.version);
				httpUrlSendSB2.append("&version=" + Config.version);
				 url1 = httpUrlSendSB1.toString();
				 url2 = httpUrlSendSB2.toString();
			 
				 
			    StringPrint.print("  BSStatisticsTerminalInfoService init url1  ==  "
							+ url1);
			    StringPrint.print("  BSStatisticsTerminalInfoService init url2  ==  "
						+ url2);
				 
			    StringPrint.print("{\"status\":\"0\",\"message\":\"初始化成功\"}");
			    
			    
				statisticsTerminalInfoBack.reBack("{\"status\":\"0\",\"message\":\"初始化成功\"}");
	   }
	 }.start();
	
	}
	
	private String getSystem() {
		try {
			String version=android.os.Build.VERSION.RELEASE;
			 if(null==version||"".equals(version)||"null".equals(version))
		      {
		        	return "";
		      }
			 return version;
		} catch (Exception e) {
			return "";
		}
	}
	
	   /**
     * 获得手机型号
     * @return 
     */
	private  static String getModel() {
		try {
			String str = android.os.Build.MODEL + "";
			str = str.replaceAll(" ", "");
			str.trim();
			return str;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String getMacAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				if (intf.getName().equals("eth0")) {
					StringBuffer sb = new StringBuffer();
					byte[] macBytes = intf.getHardwareAddress();
					for (int i = 0; i < macBytes.length; i++) {
						String sTemp = Integer.toHexString(0xFF & macBytes[i]);
						if (sTemp.length() == 1) {
			            	sb.append("0");
			            }
						sb.append(sTemp);
					}
					String rb=sb.toString();
					if(null!=rb&&!"".equals(rb.trim()))
					{
						return rb;
					}
					else
					{
						return "-1";
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
			return "-1";
		}
		return "-1";
	}
	

	public static String getVersion(Context context)//获取版本号  
    {  
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0); 
             StringPrint.print(pi.versionCode+"");
            return pi.versionCode+"";  
        } catch (NameNotFoundException e) {  
             return "";
        }  
    } 
	
	
	
	
	
	public static String getPackageName(Context context)//获取版本号  
    {  
        try {
        	String baoming=context.getPackageName();
            return baoming;  
        } catch (Exception e) {  
             return "";
        }  
    } 
	
	
	
	
	
	/**
	 * 发送终端统计信息
	* @author Jacky   
	 */
	public void transferAppinfoReportMessage(final String apkStartType,final String UpgradeVersion,final String TerminaLogVersion, final StatisticsTerminalInfoBack statisticsTerminalInfoBack,final String hid_SN) {
		StringPrint.print("transferAppinfoReportMessage >> apkStartType="+apkStartType+"  UpgradeVersion="+UpgradeVersion+"  TerminaLogVersion="+TerminaLogVersion+"   hid_SN="+ hid_SN);
		new Thread(){
			public void run() {
				
				
				   
				   
				   final String tempurl = url1;
					final TerminalInfoMessageBean terminalInfoMessageBean = new TerminalInfoMessageBean();
					terminalInfoMessageBean.setHid("");
					
						terminalInfoMessageBean.setHid(hid_SN);
						terminalInfoMessageBean.setOemid(oemid);
						terminalInfoMessageBean.setUid("");
						terminalInfoMessageBean.setVooleauth("");
						terminalInfoMessageBean.setAuthcompile("");
						
						/** 存放所有产生的消息 **/
						  final List<TerminalInfoMessageBean> pageMessageBeanList = new ArrayList<TerminalInfoMessageBean>();
					 
						  final  List<TerminalInfoMessageBean> tempMessageList = new ArrayList<TerminalInfoMessageBean>();
					
					new Thread(){
						public void run() {
							Proxy proxy=null;
							try {
								proxy=new IndexParse().parsedaili(cfb);
							} catch (IOException e) {
								e.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							if(null!=proxy&&null!=proxy.getM3u8Version()&&!"".equals(proxy.getM3u8Version().trim()))
							{
								StringPrint.print("getM3u8Version"+proxy.getM3u8Version());
								terminalInfoMessageBean.setVooleagent(proxy.getVersion());
								terminalInfoMessageBean.setAgentcompile(proxy.getBuildTime());
								terminalInfoMessageBean.setAgentlibs(proxy.getM3u8Version());
							}
							else
							{
								terminalInfoMessageBean.setVooleagent("");
								terminalInfoMessageBean.setAgentcompile("");
								terminalInfoMessageBean.setAgentlibs("");
							}
							

							terminalInfoMessageBean
									.setMessageType(TerminalInfoMessageBean.EDITION_TYPE);

							pageMessageBeanList.add(terminalInfoMessageBean);

							// message个数超过规定的最大值将发送messageLst中的消息
							if (pageMessageBeanList.size() >= maxMsgCount) {
								tempMessageList.addAll(pageMessageBeanList);
								pageMessageBeanList.clear();
								final TeriminalInfoMessageArrayBean teriminalInfoMessageArrayBean = initStatisticsData(
										tempMessageList, headerBean);

								/**
								 * 统计模块版本号
								 */
								final String tongjiVersion=Config.version;
								
								// 启动一个线程发送消息
								new Thread() {
									public void run() {
										String strResult = sendMessage(apkStartType,
												UpgradeVersion,
												TerminaLogVersion,
												tongjiVersion,
												
												
												teriminalInfoMessageArrayBean, tempurl);
										
										
										
										if (null != strResult && !"".equals(strResult.trim())) {
											if (-1 != strResult.indexOf("\"status\":0")) {
												// 表示成功
												statisticsTerminalInfoBack.reBack("{\"status\":\"0\",\"message\":\"成功!\"}");
											} else {
												// 失败
												statisticsTerminalInfoBack.reBack("{\"status\":\"1\",\"message\":\"失败!\"}");
											}
										} else {
											// 失败
											statisticsTerminalInfoBack.reBack("{\"status\":\"1\",\"message\":\"失败!\"}");
										}
										StringPrint.print("  transferAppinfoReportMessage reback ==  "
												+ strResult);
									}
								}.start();

							}
						}
					}.start();
				   
			}
		}.start();
		 

			
			
		

	}
	
	
	
	/**
	 * 发送版本消息统计系信息
	 *   历史版本已经弃用
	 * 
	 * @param hid
	 * @param oemid
	 * @param uid
	 * @param vooleauth
	 * @param authcompile
	 * @param vooleagent
	 * @param agentcompile
	 * @param agentlibs
	 * 
	 * 
	 *            hid 终端唯一id asdfg123456 必须赋值 oemid 终端类型 435 必须赋值 uid 用户唯一id
	 *            12345678 有则赋值 vooleauth 认证版本号 有则赋值 authcompile 认证编译时间 有则赋值
	 *            vooleagent 代理版本号 有则赋值 agentcompile 代理编译时间 有则赋值 agentlibs
	 *            代理库版本号 xml字串 有则赋值
	 */
	public void transferAppinfoReportMessage( final StatisticsTerminalInfoBack statisticsTerminalInfoBack,final String hid_SN) {

		new Thread(){
			public void run() {
				
				   
				   
				   final String tempurl = url1;
					final TerminalInfoMessageBean terminalInfoMessageBean = new TerminalInfoMessageBean();
					terminalInfoMessageBean.setHid("");
					
						terminalInfoMessageBean.setHid(hid_SN);
						terminalInfoMessageBean.setOemid(oemid);
						terminalInfoMessageBean.setUid("");
						terminalInfoMessageBean.setVooleauth("");
						terminalInfoMessageBean.setAuthcompile("");
						
						/** 存放所有产生的消息 **/
						  final List<TerminalInfoMessageBean> pageMessageBeanList = new ArrayList<TerminalInfoMessageBean>();
					 
						  final  List<TerminalInfoMessageBean> tempMessageList = new ArrayList<TerminalInfoMessageBean>();
					
					new Thread(){
						public void run() {
							Proxy proxy=null;
							try {
								proxy=new IndexParse().parsedaili(cfb);
							} catch (IOException e) {
								e.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							if(null!=proxy&&null!=proxy.getM3u8Version()&&!"".equals(proxy.getM3u8Version().trim()))
							{
								StringPrint.print("getM3u8Version"+proxy.getM3u8Version());
								terminalInfoMessageBean.setVooleagent(proxy.getVersion());
								terminalInfoMessageBean.setAgentcompile(proxy.getBuildTime());
								terminalInfoMessageBean.setAgentlibs(proxy.getM3u8Version());
							}
							else
							{
								terminalInfoMessageBean.setVooleagent("");
								terminalInfoMessageBean.setAgentcompile("");
								terminalInfoMessageBean.setAgentlibs("");
							}
							

							terminalInfoMessageBean
									.setMessageType(TerminalInfoMessageBean.EDITION_TYPE);

							pageMessageBeanList.add(terminalInfoMessageBean);

							// message个数超过规定的最大值将发送messageLst中的消息
							if (pageMessageBeanList.size() >= maxMsgCount) {
								tempMessageList.addAll(pageMessageBeanList);
								pageMessageBeanList.clear();
								final TeriminalInfoMessageArrayBean teriminalInfoMessageArrayBean = initStatisticsData(
										tempMessageList, headerBean);

								// 启动一个线程发送消息
								new Thread() {
									public void run() {
										String strResult = sendMessage(null,null,null,null,
												teriminalInfoMessageArrayBean, tempurl);
										if (null != strResult && !"".equals(strResult.trim())) {
											if (-1 != strResult.indexOf("\"status\":0")) {
												// 表示成功
												statisticsTerminalInfoBack.reBack("{\"status\":\"0\",\"message\":\"成功!\"}");
											} else {
												// 失败
												statisticsTerminalInfoBack.reBack("{\"status\":\"1\",\"message\":\"失败!\"}");
											}
										} else {
											// 失败
											statisticsTerminalInfoBack.reBack("{\"status\":\"1\",\"message\":\"失败!\"}");
										}
										StringPrint.print("  transferAppinfoReportMessage  ==  "
												+ strResult);
									}
								}.start();

							}
						}
					}.start();
				   
			}
		}.start();
		 

			
			
		

	}

	/**
	 * 配置统计信息接口
	 * 
	 * @param properties
	 * @param voolert
	 */
	public void transferAppconfigReportMessage(final String properties,
			final String voolert,
			final StatisticsTerminalInfoBack statisticsTerminalInfoBack) {
		final String tempurl = url2;

		StringPrint.print("配置文件统计被调用");
		StringPrint.print("  properties  ==  "
				+ properties);
		StringPrint.print("  voolert  ==  "
				+ voolert);

			// 启动一个线程发送消息
			new Thread() {
				public void run() {
					String strResult = sendMessage2(
							properties,voolert, tempurl);
					StringPrint.print("  transferAppconfigReportMessage  ==  "
							+ strResult);
					if (null != strResult && !"".equals(strResult.trim())) {
						if (-1 != strResult.indexOf("\"status\":0")) {
							// 表示成功
							statisticsTerminalInfoBack.reBack("{\"status\":\"0\",\"message\":\"成功!\"}");
						} else {
							// 失败
							statisticsTerminalInfoBack.reBack("{\"status\":\"1\",\"message\":\"失败!\"}");
						}
					} else {
						// 失败
						statisticsTerminalInfoBack.reBack("{\"status\":\"1\",\"message\":\"失败!\"}");
					}

				}
			}.start();

		//}

	}

	/**
	 * 生成xml数据
	 */
	private String initXMLData(final String apkStartType,final String UpgradeVersion,
			final String TerminaLogVersion, 
			final String tongjiVersion,
			TeriminalInfoMessageArrayBean teriminalInfoMessageArrayBean) {
		String xml = null;
		if (null != teriminalInfoMessageArrayBean) {
			xml = StringXMLUtil
					.createTerminalInfoXmlBS(apkStartType ,UpgradeVersion ,TerminaLogVersion,tongjiVersion,teriminalInfoMessageArrayBean);
		}
		return xml;
	}

	/**
	 * 发送数据
	 */
	private String sendMessage(final String apkStartType,final String UpgradeVersion,
			final String TerminaLogVersion, 
			final String tongjiVersion,
			TeriminalInfoMessageArrayBean teriminalInfoMessageArrayBean,
			String tempurl) {
		String strResult = null;
		PageMessageParse pageMessageParse = new PageMessageParse();
		try {
			String xmlParam = initXMLData(apkStartType,UpgradeVersion,TerminaLogVersion,tongjiVersion,teriminalInfoMessageArrayBean);
			// StringPrint.print("tempurl   "+tempurl);
			// StringPrint.print("xmlParam   "+xmlParam);
			strResult = pageMessageParse.parse(tempurl, xmlParam);

			StringPrint.print("结果==" + strResult);
			return strResult;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 
		}
		return strResult;
	}

	
	private String sendMessage2(String properties,
			String voolert,
			String tempurl) {
		String strResult = null;
		PageMessageParse pageMessageParse = new PageMessageParse();
		try {
			String xmlParam = initXMLDataBanben(properties,
					  voolert);
			// StringPrint.print("tempurl   "+tempurl);
			// StringPrint.print("xmlParam   "+xmlParam);
			strResult = pageMessageParse.parse(tempurl, xmlParam);

			StringPrint.print("结果==" + strResult);
			return strResult;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		return strResult;
	}
	

	/**
	 * 生成xml数据
	 */
	private String initXMLDataBanben( String properties,String voolert) {
		String xml = null;
			xml = StringXMLUtil
					.createTerminalInfoBanbenXml(properties,voolert);
		return xml;
	}
	
	
	/**
	 * 初始化数据进入bean
	 * 
	 * @param tempMessageList
	 * @param headerBean
	 * @return
	 */
	public TeriminalInfoMessageArrayBean initStatisticsData(
			List<TerminalInfoMessageBean> tempMessageList, HeaderBean headerBean) {

		TeriminalInfoMessageArrayBean teriminalInfoMessageArrayBean = new TeriminalInfoMessageArrayBean();
		teriminalInfoMessageArrayBean.setHeaderBean(headerBean);
		teriminalInfoMessageArrayBean
				.setTerminalInfoMessageBeanList(tempMessageList);
		teriminalInfoMessageArrayBean.setCount(tempMessageList.size());

		return teriminalInfoMessageArrayBean;
	}

	public String getOemid() {
		return oemid;
	}

	public void setOemid(String oemid) {
		this.oemid = oemid;
	}
	
	
}
