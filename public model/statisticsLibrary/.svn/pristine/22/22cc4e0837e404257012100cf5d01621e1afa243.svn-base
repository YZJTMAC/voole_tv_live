package com.voole.statistics.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.Enumeration;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.voole.statistics.bean.ConfigFileBean;
import com.voole.statistics.bean.HeaderBean;
import com.voole.statistics.bean.TerminalInfoMessageBean;
import com.voole.statistics.config.Config;
import com.voole.statistics.parse.PageMessageParse;
import com.voole.statistics.reback.StatisticsTerminalInfoBack;
import com.voole.statistics.userinfo.IndexParse;
import com.voole.statistics.userinfo.Proxy;
import com.voole.statistics.userinfo.User;
import com.voole.statistics.util.MyFileRead;
import com.voole.statistics.util.StringPrint;
import com.voole.statistics.util.StringXMLUtil;

/**
 * 终端信息统计服务
 * 
 * @author Jacky
 * 
 */
public class StatisticsTerminalInfoService {
	/**
	 * 实例页面统计服务类对象
	 */
	private static StatisticsTerminalInfoService instance = null;
	/**
	 * 头信息BEAN
	 */
	private HeaderBean headerBean = null;
	
	
	
	/** 提交消息数据的接口地址 **/
	private String url1 = null;
	//private String url2 = null;
	
	private ConfigFileBean cfb;
	

	private static final String TYPE1 = "AppinfoReport";// 版本信息
	private static final String TYPE2 = "AppconfigReport";// 配置文件信息
	/**
	 * 获得页面统计服务类的实例
	 * 
	 * @return StatisticsPageService的实例
	 */
	public static StatisticsTerminalInfoService getInstance() {
		if (instance == null) {
			instance = new StatisticsTerminalInfoService();
		}
		return instance;
	}

	/**
	 * 构造方法
	 */
	private StatisticsTerminalInfoService() {

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
			final String appversion, final String chip,final Context context,final StatisticsTerminalInfoBack statisticsTerminalInfoBack,final String configFileSystemAddress) {
 	
		   
	       
		
		new Thread(){
 		
 		
 		
 		
 		
	   public void run() {
		   
		   
//		   读取配置文件取得端口号
		   
		   MyFileRead mf=new MyFileRead();
		   
			StringPrint.print("初始化方法被调用了");
			StringPrint.print("configFileSystemAddress 参数="+configFileSystemAddress);
		   
		   
		   if(null==configFileSystemAddress||"".equals(configFileSystemAddress.trim()))
		   {
			   StringPrint.print("进入了默认的读取方法 也就是configFileSystemAddress参数是空的方法");
		       cfb=mf.getConfigFileAssets("voolert.conf", context);
			
		   }
		   else
		   {

			   StringPrint.print("进入了读取 configFileSystemAddress 方法" +
			   		"");
			   
			   
		        cfb=mf.getConfigFileAssets("voolert.conf", context,configFileSystemAddress);
			  /*  if(null!=cfb)
			    {
			    	try {
			    	 	StringPrint.print("从配置文件读取到 Local_agent_http_port"+cfb.getLocal_agent_http_port());
				    	StringPrint.print("从配置文件读取到 Local_http_port"+cfb.getLocal_http_port());
				   
					} catch (Exception e) {
						e.printStackTrace();
					}
			    }   */
		   }
		   
		   
		   User userinfoTemp=null;
		   try {
			   IndexParse ip=new IndexParse();
			   userinfoTemp=ip.parse(null,cfb);
		   } catch (IOException e) {
			       
				e.printStackTrace();
			} catch (Exception e) {
			       
				e.printStackTrace();
			}
			String oemid=null;
		   if(null==userinfoTemp||null==userinfoTemp.getOemid()||"".equals(userinfoTemp.getOemid()))
			{
			   oemid="-1";
			}
		   else
		   {
			   oemid=userinfoTemp.getOemid();
		   }
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
				// url2 = httpUrlSendSB2.toString();
			 
				statisticsTerminalInfoBack.reBack("{\"status\":\"0\",\"message\":\"初始化成功\"}");
	   }
	 }.start();
	
	}
	
	public static String getSystem() {
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
	public  static String getModel() {
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
	 * 终端信息统计接口
	* @author Jacky   
	* @date 2015-5-31 下午1:26:21
	 */
	public void transferAppinfoReportMessage( final StatisticsTerminalInfoBack statisticsTerminalInfoBack) {
		new Thread(){
			public void run() {
				   
				 User userinfoTemp=null;
				   try {
					   IndexParse ip=new IndexParse();
					   userinfoTemp=ip.parse(null,cfb);
				   } catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
					       
						e.printStackTrace();
					}
				   
				   final String tempurl = url1;
					final TerminalInfoMessageBean terminalInfoMessageBean = new TerminalInfoMessageBean();
					
					if(null==userinfoTemp)
					{
						terminalInfoMessageBean.setHid("");
						terminalInfoMessageBean.setOemid("-1");
						terminalInfoMessageBean.setUid("");
						terminalInfoMessageBean.setVooleauth("");
						terminalInfoMessageBean.setAuthcompile("");
					}
					else
					{
						terminalInfoMessageBean.setHid(userinfoTemp.getHid());
						terminalInfoMessageBean.setOemid(userinfoTemp.getOemid());
						terminalInfoMessageBean.setUid(userinfoTemp.getUid());
						terminalInfoMessageBean.setVooleauth(userinfoTemp.getVersion());
						terminalInfoMessageBean.setAuthcompile(userinfoTemp.getBuildtime());
					}
					
					
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
							

        
							/**
							 * 统计模块版本号
							 */
							final String tongjiVersion=Config.version;
								// 启动一个线程发送消息
								new Thread() {
									public void run() {
										String strResult = sendMessage(
												terminalInfoMessageBean,headerBean, tempurl,  "",  "",  "",tongjiVersion,"","","","","");
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
					}.start();
				   
			}
		}.start();
		 

			
			
		

	}
	
	
	/**
	 * 终端信息统计接口 
	 *   新增加
   isauth	是否通过认证(字符串)	默认：-1
	-1 无认证
0 否
1 是
sn	终端串号（字符串,可用于第三方身份识别ID传递）		有则赋值
	 *  
	 *  
	 *  2016-05-04日第一次修改
	 *  2016-5-11 最后 修改
	 *  
	 *  
       
	 */
	public void transferAppinfoReportMessage(final String apkStartType,final String UpgradeVersion,final String TerminaLogVersion,  String sdkModuleVersion,  String sdkModuleType,  String  isauth,  String sn,String    deviceid,final StatisticsTerminalInfoBack statisticsTerminalInfoBack) {

		
		
		if(null==isauth||"".equals(isauth.trim()))
		{
			isauth="-1";
		}
		 
		if(null==sn||"".equals(sn.trim()))
		{
			sn="";
		}
		
		
		if(null==sdkModuleVersion||"".equals(sdkModuleVersion.trim()))
		{
			sdkModuleVersion="";
		}
		else
		{
		 
		}
		
		if(null==deviceid||"".equals(deviceid.trim()))
		{
			deviceid="";
		}
		else
		{
		 
		}
		
		
		
		if(null==sdkModuleType||"".equals(sdkModuleType.trim()))
		{
			sdkModuleType="";
		}
		else
		{
		 
		}
		 
		final String sdkModuleVersion_=sdkModuleVersion;
		final String sdkModuleType_=sdkModuleType;
		final String isauth_=isauth;
		final String sn_=sn;
		final String deviceid_=deviceid;
		
		new Thread(){
			public void run() {
				   
				 User userinfoTemp=null;
				   try {
					   IndexParse ip=new IndexParse();
					   userinfoTemp=ip.parse(null,cfb);
				   } catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
					       
						e.printStackTrace();
					}
				   
				   final String tempurl = url1;
					final TerminalInfoMessageBean terminalInfoMessageBean = new TerminalInfoMessageBean();
					
					if(null==userinfoTemp)
					{
						terminalInfoMessageBean.setHid("");
						terminalInfoMessageBean.setOemid("-1");
						terminalInfoMessageBean.setUid("");
						terminalInfoMessageBean.setVooleauth("");
						terminalInfoMessageBean.setAuthcompile("");
					}
					else
					{
						terminalInfoMessageBean.setHid(userinfoTemp.getHid());
						terminalInfoMessageBean.setOemid(userinfoTemp.getOemid());
						terminalInfoMessageBean.setUid(userinfoTemp.getUid());
						terminalInfoMessageBean.setVooleauth(userinfoTemp.getVersion());
						terminalInfoMessageBean.setAuthcompile(userinfoTemp.getBuildtime());
					}
					
					
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
							
						
							/**
							 * 统计模块版本号
							 */
							final String tongjiVersion=Config.version;
								// 启动一个线程发送消息
								new Thread() {
									public void run() {
										
										String strResult = sendMessage(
												terminalInfoMessageBean,headerBean, tempurl,  apkStartType,  UpgradeVersion,  TerminaLogVersion,tongjiVersion,  sdkModuleVersion_,  sdkModuleType_,isauth_,sn_,deviceid_);
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
					}.start();
				   
			}
		}.start();
		 

	}
	
	
	/**
	 * 终端信息统计接口 
	 * 2015-11-16日最新
       
	 */
	public void transferAppinfoReportMessage(final String apkStartType,final String UpgradeVersion,final String TerminaLogVersion,  String sdkModuleVersion,  String sdkModuleType,final StatisticsTerminalInfoBack statisticsTerminalInfoBack) {

		
		
		
		if(null==sdkModuleVersion||"".equals(sdkModuleVersion.trim()))
		{
			sdkModuleVersion="";
		}
		else
		{
		 
		}
		
		
		
		if(null==sdkModuleType||"".equals(sdkModuleType.trim()))
		{
			sdkModuleType="";
		}
		else
		{
		 
		}
		 
		final String sdkModuleVersion_=sdkModuleVersion;
		final String sdkModuleType_=sdkModuleType;
		
		
		
		new Thread(){
			public void run() {
				   
				 User userinfoTemp=null;
				   try {
					   IndexParse ip=new IndexParse();
					   userinfoTemp=ip.parse(null,cfb);
				   } catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
					       
						e.printStackTrace();
					}
				   
				   final String tempurl = url1;
					final TerminalInfoMessageBean terminalInfoMessageBean = new TerminalInfoMessageBean();
					
					if(null==userinfoTemp)
					{
						terminalInfoMessageBean.setHid("");
						terminalInfoMessageBean.setOemid("-1");
						terminalInfoMessageBean.setUid("");
						terminalInfoMessageBean.setVooleauth("");
						terminalInfoMessageBean.setAuthcompile("");
					}
					else
					{
						terminalInfoMessageBean.setHid(userinfoTemp.getHid());
						terminalInfoMessageBean.setOemid(userinfoTemp.getOemid());
						terminalInfoMessageBean.setUid(userinfoTemp.getUid());
						terminalInfoMessageBean.setVooleauth(userinfoTemp.getVersion());
						terminalInfoMessageBean.setAuthcompile(userinfoTemp.getBuildtime());
					}
					
					
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
							
						
							/**
							 * 统计模块版本号
							 */
							final String tongjiVersion=Config.version;
								// 启动一个线程发送消息
								new Thread() {
									public void run() {
										
										String strResult = sendMessage(
												terminalInfoMessageBean,headerBean, tempurl,  apkStartType,  UpgradeVersion,  TerminaLogVersion,tongjiVersion,  sdkModuleVersion_,  sdkModuleType_,"","","");
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
					}.start();
				   
			}
		}.start();
		 

	}
	
	
	
	/**
	 * 终端信息统计接口 
       
	 */
	public void transferAppinfoReportMessage(final String apkStartType,final String UpgradeVersion,final String TerminaLogVersion     ,final StatisticsTerminalInfoBack statisticsTerminalInfoBack) {

		new Thread(){
			public void run() {
				   
				 User userinfoTemp=null;
				   try {
					   IndexParse ip=new IndexParse();
					   userinfoTemp=ip.parse(null,cfb);
				   } catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
					       
						e.printStackTrace();
					}
				   
				   final String tempurl = url1;
					final TerminalInfoMessageBean terminalInfoMessageBean = new TerminalInfoMessageBean();
					
					if(null==userinfoTemp)
					{
						terminalInfoMessageBean.setHid("");
						terminalInfoMessageBean.setOemid("-1");
						terminalInfoMessageBean.setUid("");
						terminalInfoMessageBean.setVooleauth("");
						terminalInfoMessageBean.setAuthcompile("");
					}
					else
					{
						terminalInfoMessageBean.setHid(userinfoTemp.getHid());
						terminalInfoMessageBean.setOemid(userinfoTemp.getOemid());
						terminalInfoMessageBean.setUid(userinfoTemp.getUid());
						terminalInfoMessageBean.setVooleauth(userinfoTemp.getVersion());
						terminalInfoMessageBean.setAuthcompile(userinfoTemp.getBuildtime());
					}
					
					
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
							

        
							/**
							 * 统计模块版本号
							 */
							final String tongjiVersion=Config.version;
								// 启动一个线程发送消息
								new Thread() {
									public void run() {
										String strResult = sendMessage(
												terminalInfoMessageBean,headerBean, tempurl,  apkStartType,  UpgradeVersion,  TerminaLogVersion,tongjiVersion, "","", "","","");
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
		//final String tempurl = url2;

		StringPrint.print("配置文件统计被调用");
		StringPrint.print("  properties  ==  "
				+ properties);
		StringPrint.print("  voolert  ==  "
				+ voolert);
		
		
		StringPrint.print("配置文件统计方法被移除了>>>>>>");
		
		 
		//pageMessageBeanList.add(terminalInfoMessageBean);

		// message个数超过规定的最大值将发送messageLst中的消息
		//if (pageMessageBeanList.size() >= maxMsgCount) {
		//	tempMessageList.addAll(pageMessageBeanList);
		//	pageMessageBeanList.clear();
			//final TeriminalInfoMessageArrayBean teriminalInfoMessageArrayBean = initStatisticsData(
				//	tempMessageList, headerBean);

			// 启动一个线程发送消息
		
		/**
		 * 2015年 11月14日 移除了配置文件信息统计
		 */
		
			/*new Thread() {
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
*/
		//}

	}

	/**
	 * 生成xml数据
	 */
	private String initXMLData(TerminalInfoMessageBean terminalInfoMessageBean,HeaderBean headerBean,String apkStartType, String UpgradeVersion,String TerminaLogVersion,String tongjiVersion,	 String sdkModuleVersion,String sdkModuleType,String isauth,String sn,String deviceid) {
		String xml = null;
		if (null != terminalInfoMessageBean) {
			xml = StringXMLUtil
					.createTerminalInfoXml(terminalInfoMessageBean,headerBean,  apkStartType,   UpgradeVersion,  TerminaLogVersion,  tongjiVersion,	   sdkModuleVersion,  sdkModuleType,  isauth,  sn,deviceid);
		}

		
		
		return xml;
	}

	/**
	 * 发送数据
	 */
	private String sendMessage( TerminalInfoMessageBean terminalInfoMessageBean,
			   HeaderBean headerBean,
			String tempurl,String apkStartType, String UpgradeVersion,String TerminaLogVersion,String tongjiVersion,	 String sdkModuleVersion,String sdkModuleType,String isauth,String sn,String deviceid) {
		String strResult = null;
		PageMessageParse pageMessageParse = new PageMessageParse();
		try {
			 
			String xmlParam = initXMLData(terminalInfoMessageBean,headerBean,  apkStartType,   UpgradeVersion,  TerminaLogVersion,  tongjiVersion,  sdkModuleVersion,  sdkModuleType , isauth,  sn,deviceid);
		  StringPrint.print("tempurl   "+tempurl);
			 StringPrint.print("xmlParam   "+xmlParam);
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
	
	
	
	
	
	
/*	
 *  配置文件发送方法被移除了 在2015-11-14日
 * 
 * private String sendMessage2(
			String properties,String voolert ,
			String tempurl) {
		String strResult = null;
		PageMessageParse pageMessageParse = new PageMessageParse();
		try {
			String xmlParam = initXMLDataBanben(properties,voolert);
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
	*/
	/**
	 * 生成xml数据
	 */
/*	private String initXMLDataBanben( String properties,String voolert) {
		String xml = null;
			xml = StringXMLUtil
					.createTerminalInfoBanbenXml(properties,voolert);
		return xml;
	}*/
	
 
}
