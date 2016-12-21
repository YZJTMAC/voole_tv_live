package com.voole.statistics.util;

import java.util.List;

import com.voole.statistics.bean.HeaderBean;
import com.voole.statistics.bean.PageMessageArrayBean;
import com.voole.statistics.bean.PageMessageBean;
import com.voole.statistics.bean.PlayerMessageArrayBean;
import com.voole.statistics.bean.PlayerMessageBean;
import com.voole.statistics.bean.TeriminalInfoMessageArrayBean;
import com.voole.statistics.bean.TerminalInfoMessageBean;
import com.voole.statistics.config.Config;


/**
 * 生成xml字符串类型
 * @author Jacky
 *
 */
public class StringXMLUtil {
	
	/**
	 * 页面计数器
	 */
	public static int pageNumber=1;
	
	
	
	public static String createPageXml(PageMessageArrayBean pageMessageArrayBean)
	{
		pageNumber++;
		HeaderBean	headerBean=pageMessageArrayBean.getHeaderBean();
		List<PageMessageBean> pageMessageBeanlist=	pageMessageArrayBean.getPageMessageBeanList();
		StringBuffer stringBuffer=new  StringBuffer();
		stringBuffer.append("<?xml version='1.0' encoding='utf-8' standalone='no'?>");
		stringBuffer.append("<MessageArray count=\""+pageMessageArrayBean.getCount()+"\" >");
		stringBuffer.append("<Header ");
		
		stringBuffer.append("version=\""+Config.version+"\" ");
		stringBuffer.append("hid=\""+headerBean.getHid()+"\" ");
		stringBuffer.append("oemid=\""+headerBean.getOemid()+"\" ");
		stringBuffer.append("uid=\""+headerBean.getUid()+"\" ");
		stringBuffer.append("appid=\""+headerBean.getAppId()+"\" ");
		stringBuffer.append("appversion=\""+headerBean.getAppVersion()+"\" ");
		stringBuffer.append("packagename=\""+headerBean.getPackageName()+"\" ");
		stringBuffer.append("/>");
		stringBuffer.append("<Array>");
		
		if(null!=pageMessageBeanlist&&pageMessageBeanlist.size()>0)
		{
		
			for (PageMessageBean pageMessageBean:pageMessageBeanlist) {
				stringBuffer.append("<Message type=\"PageBrowsing\" value=\"epg\" >");
				stringBuffer.append("<Body>");
				stringBuffer.append("CDATA[");
				stringBuffer.append("<epg ");
				stringBuffer.append(" epgid=\""+pageMessageBean.getEpgid()+"\" ");
				stringBuffer.append(" id=\""+pageNumber+"\" ");
				stringBuffer.append(" lastid=\""+"\" ");
				if(null==pageMessageBean.getPagetype()||"".equals(pageMessageBean.getPagetype().trim()))
				{
					stringBuffer.append(" pagetype=\""+"\" ");
				}
				else
				{
					stringBuffer.append(" pagetype=\""+pageMessageBean.getPagetype()+"\" ");
				}
				
				
				if(null==pageMessageBean.getModuletype()||"".equals(pageMessageBean.getModuletype().trim()))
				{
					stringBuffer.append(" moduletype=\""+"\" ");
				}
				else
				{
					stringBuffer.append(" moduletype=\""+pageMessageBean.getModuletype()+"\" ");
				}
				
				
				if(null==pageMessageBean.getFocustype()||"".equals(pageMessageBean.getFocustype().trim()))
				{
					stringBuffer.append(" focustype=\""+"\" ");
				}
				else
				{
					stringBuffer.append(" focustype=\""+pageMessageBean.getFocustype()+"\" ");
				}
				

				if(null==pageMessageBean.getFocusid()||"".equals(pageMessageBean.getFocusid().trim()))
				{
					stringBuffer.append(" focusid=\""+"\" ");
				}
				else
				{
					stringBuffer.append(" focusid=\""+pageMessageBean.getFocusid()+"\" ");
				}
				
				
				
				
				
				if(null==pageMessageBean.getPagenum()||"".equals(pageMessageBean.getPagenum().trim()))
				{
					stringBuffer.append(" pagenum=\""+"\" ");
				}
				else
				{
					stringBuffer.append(" pagenum=\""+pageMessageBean.getPagenum()+"\" ");
				}
			 
				if(null==pageMessageBean.getAction()||"".equals(pageMessageBean.getAction().trim()))
				{
					stringBuffer.append(" action=\""+"\" ");
				}
				else
				{
					stringBuffer.append(" action=\""+pageMessageBean.getAction()+"\" ");
				}
				
				if(null==pageMessageBean.getInput()||"".equals(pageMessageBean.getInput().trim()))
				{
					stringBuffer.append(" input=\""+"\" ");
					
				}
				else
				{
					stringBuffer.append(" input=\""+StringXMLUtil.textToXML(pageMessageBean.getInput())+"\" ");
				}
				
				
				if(null==pageMessageBean.getInput2()||"".equals(pageMessageBean.getInput2().trim()))
				{
					stringBuffer.append(" input2=\""+"\" ");
				}
				else
				{
					stringBuffer.append(" input2=\""+StringXMLUtil.textToXML(pageMessageBean.getInput2())+"\" ");
				}
				stringBuffer.append(">");
				
				if(null==pageMessageBean.getMidlist()||"".equals(pageMessageBean.getMidlist().trim()))
				{
					stringBuffer.append("<midlist></midlist>");
				}
				else
				{
					stringBuffer.append("<midlist>"+pageMessageBean.getMidlist()+"</midlist>");
				}
				stringBuffer.append("</epg>");
				stringBuffer.append("]");
				stringBuffer.append("</Body>");
				stringBuffer.append("</Message>");
			}
			
		}
		
		stringBuffer.append("</Array>");
		stringBuffer.append("</MessageArray>");
		
		return stringBuffer.toString();
	}
	
	
	
	
	public static String textToXML(String text) {
		if (text == null)
			return "null";

		StringBuffer html = new StringBuffer();
		for (int i = 0; i < text.length(); ++i) {
			html.append(charToHTML(text.charAt(i)));
		}
		return html.toString();
	}
	
	
	private static String charToHTML(char ch) {
		
		if (ch == '<')
			return "&lt;";
		if (ch == '>')
			return "&gt;";
		if (ch == '&')
			return "&amp;";
		if (ch == '"')
			return "&quot;";
		if (ch == '\'')
			return "&#39;";
		
		return String.valueOf(ch);
	}




	/**
	 * 创建播放消息xml
<Message type=Player value=PlayOperation >
　　<Body>
　　CDATA[
         <PlayOperation 
         operationType=“Play/Pause/Stop/seek”  
         fid=”sdfwewe2334fde” playtime=20 
         seektime=45 
         sessionid=”12345678901234567890123456789012”>
          <VideoName>CDATA[虎胆龙威]</VideoName>
　　　　　</ PlayOperation >
　　]
　　</Body>
</ Message >
	 * @param playerMessageArrayBean
	 * @return
	 */
	public static String createPlayerXml(
			PlayerMessageArrayBean playerMessageArrayBean) {
		HeaderBean	headerBean=playerMessageArrayBean.getHeaderBean();
		List<PlayerMessageBean> playerMessageBeanlist=	playerMessageArrayBean.getPlayerMessageBeanList();
		StringBuffer stringBuffer=new  StringBuffer();
		stringBuffer.append("<?xml version='1.0' encoding='utf-8' standalone='no'?>");
		stringBuffer.append("<MessageArray count=\""+playerMessageArrayBean.getCount()+"\" >");
		stringBuffer.append("<Header ");
		
		stringBuffer.append("version=\""+Config.version+"\" ");
		stringBuffer.append("hid=\""+headerBean.getHid()+"\" ");
		stringBuffer.append("oemid=\""+headerBean.getOemid()+"\" ");
		stringBuffer.append("uid=\""+headerBean.getUid()+"\" ");
		stringBuffer.append("appid=\""+headerBean.getAppId()+"\" ");
		stringBuffer.append("appversion=\""+headerBean.getAppVersion()+"\" ");
		stringBuffer.append("packagename=\""+headerBean.getPackageName()+"\" ");
		stringBuffer.append("/>");
		stringBuffer.append("<Array>");
		
		if(null!=playerMessageBeanlist&&playerMessageBeanlist.size()>0)
		{
			
		
			for (PlayerMessageBean playerMessageBean:playerMessageBeanlist) {
				stringBuffer.append("<Message type=\"Player\" value=\"PlayOperation\" >");
				stringBuffer.append("<Body>");
				stringBuffer.append("CDATA[");
				stringBuffer.append("<PlayOperation ");
				
				if(null!=playerMessageBean.getAction()&&!"".equals(playerMessageBean.getAction().trim()))
				{
					stringBuffer.append(" operationType=\""+playerMessageBean.getAction()+"\" ");
				}
				
				if(null!=playerMessageBean.getFid()&&!"".equals(playerMessageBean.getFid().trim()))
				{
					stringBuffer.append(" fid=\""+playerMessageBean.getFid()+"\" ");
				}
				if(null!=playerMessageBean.getPlaytime()&&!"".equals(playerMessageBean.getPlaytime().trim()))
				{
					stringBuffer.append(" playtime=\""+playerMessageBean.getPlaytime()+"\" ");
				}
				if(null!=playerMessageBean.getSeektime()&&!"".equals(playerMessageBean.getSeektime().trim()))
				{
					stringBuffer.append(" seektime=\""+playerMessageBean.getSeektime()+"\" ");
				}
				if(null!=playerMessageBean.getSessionid()&&!"".equals(playerMessageBean.getSessionid().trim()))
				{
					stringBuffer.append(" sessionid=\""+playerMessageBean.getSessionid()+"\" ");
				}
				stringBuffer.append("> ");
				stringBuffer.append("<VideoName>CDATA[");
				if(null!=playerMessageBean.getVideoName()&&!"".equals(playerMessageBean.getVideoName().trim()))
				{
					stringBuffer.append(playerMessageBean.getVideoName());
				}
				stringBuffer.append("]</VideoName>");
				stringBuffer.append("</PlayOperation>");
				stringBuffer.append("]");
				stringBuffer.append("</Body>");
				stringBuffer.append("</Message>");
			}
			
		}
		
		stringBuffer.append("</Array>");
		stringBuffer.append("</MessageArray>");
		
		return stringBuffer.toString();
	}




	public static String createPlayerStatusXml(
			PlayerMessageArrayBean playerMessageArrayBean) {

		HeaderBean	headerBean=playerMessageArrayBean.getHeaderBean();
		List<PlayerMessageBean> playerMessageBeanlist=	playerMessageArrayBean.getPlayerMessageBeanList();
		StringBuffer stringBuffer=new  StringBuffer();
		stringBuffer.append("<?xml version='1.0' encoding='utf-8' standalone='no'?>");
		stringBuffer.append("<MessageArray count=\""+playerMessageArrayBean.getCount()+"\" >");
		stringBuffer.append("<Header ");
		
		stringBuffer.append("version=\""+Config.version+"\" ");
		stringBuffer.append("hid=\""+headerBean.getHid()+"\" ");
		stringBuffer.append("oemid=\""+headerBean.getOemid()+"\" ");
		stringBuffer.append("uid=\""+headerBean.getUid()+"\" ");
		stringBuffer.append("appid=\""+headerBean.getAppId()+"\" ");
		stringBuffer.append("appversion=\""+headerBean.getAppVersion()+"\" ");
		stringBuffer.append("appversion=\""+headerBean.getPackageName()+"\" ");
		stringBuffer.append("/>");
		stringBuffer.append("<Array>");
		
		if(null!=playerMessageBeanlist&&playerMessageBeanlist.size()>0)
		{
			
		
			for (PlayerMessageBean playerMessageBean:playerMessageBeanlist) {
				stringBuffer.append("<Message type=\"Player\" value=\"PlayStatus\" >");
				stringBuffer.append("<Body>");
				stringBuffer.append("CDATA[");
				stringBuffer.append("<PlayStatus ");
				
				if(null!=playerMessageBean.getAction()&&!"".equals(playerMessageBean.getAction().trim()))
				{
					stringBuffer.append(" action=\""+playerMessageBean.getAction()+"\" ");
				}
				
				if(null!=playerMessageBean.getFid()&&!"".equals(playerMessageBean.getFid().trim()))
				{
					stringBuffer.append(" fid=\""+playerMessageBean.getFid()+"\" ");
				}
				if(null!=playerMessageBean.getPlaytime()&&!"".equals(playerMessageBean.getPlaytime().trim()))
				{
					stringBuffer.append(" playtime=\""+playerMessageBean.getPlaytime()+"\" ");
				}
				if(null!=playerMessageBean.getSessionid()&&!"".equals(playerMessageBean.getSessionid().trim()))
				{
					stringBuffer.append(" sessionid=\""+playerMessageBean.getSessionid()+"\" ");
				}
				stringBuffer.append("> ");
				stringBuffer.append("</PlayStatus>");
				stringBuffer.append("]");
				stringBuffer.append("</Body>");
				stringBuffer.append("</Message>");
			}
			
		}
		
		stringBuffer.append("</Array>");
		stringBuffer.append("</MessageArray>");
		
		return stringBuffer.toString();
	
	}
	
	
	/**
	 * 起播xml
	 * @param playerMessageArrayBean
	 * @return
	 */
	public static String createStartPlayerXml(
			PlayerMessageArrayBean playerMessageArrayBean) {
		HeaderBean	headerBean=playerMessageArrayBean.getHeaderBean();
		List<PlayerMessageBean> playerMessageBeanlist=	playerMessageArrayBean.getPlayerMessageBeanList();
		StringBuffer stringBuffer=new  StringBuffer();
		stringBuffer.append("<?xml version='1.0' encoding='utf-8' standalone='no'?>");
		stringBuffer.append("<MessageArray count=\""+playerMessageArrayBean.getCount()+"\" >");
		stringBuffer.append("<Header ");
		
		stringBuffer.append("version=\""+Config.version+"\" ");
		stringBuffer.append("hid=\""+headerBean.getHid()+"\" ");
		stringBuffer.append("oemid=\""+headerBean.getOemid()+"\" ");
		stringBuffer.append("uid=\""+headerBean.getUid()+"\" ");
		stringBuffer.append("appid=\""+headerBean.getAppId()+"\" ");
		stringBuffer.append("appversion=\""+headerBean.getAppVersion()+"\" ");
		stringBuffer.append("packagename=\""+headerBean.getPackageName()+"\" ");
		stringBuffer.append("/>");
		stringBuffer.append("<Array>");
		
		if(null!=playerMessageBeanlist&&playerMessageBeanlist.size()>0)
		{
			
		
			for (PlayerMessageBean playerMessageBean:playerMessageBeanlist) {
				stringBuffer.append("<Message type=\"Player\" value=\"PlayPhrase\" >");
				stringBuffer.append("<Body>");
				stringBuffer.append("CDATA[");
				stringBuffer.append("<PlayPhrase ");
				
				if(null!=playerMessageBean.getAction()&&!"".equals(playerMessageBean.getAction().trim()))
				{
					stringBuffer.append(" action=\""+playerMessageBean.getAction()+"\" ");
				}
				
				if(null!=playerMessageBean.getFid()&&!"".equals(playerMessageBean.getFid().trim()))
				{
					stringBuffer.append(" fid=\""+playerMessageBean.getFid()+"\" ");
				}
				if(null!=playerMessageBean.getDuration()&&!"".equals(playerMessageBean.getDuration().trim()))
				{
					stringBuffer.append(" duration=\""+playerMessageBean.getDuration()+"\" ");
				}
				if(null!=playerMessageBean.getSessionid()&&!"".equals(playerMessageBean.getSessionid().trim()))
				{
					stringBuffer.append(" sessionid=\""+playerMessageBean.getSessionid()+"\" ");
				}
				stringBuffer.append("> ");
				stringBuffer.append("</PlayPhrase>");
				stringBuffer.append("]");
				stringBuffer.append("</Body>");
				stringBuffer.append("</Message>");
			}
			
		}
		
		stringBuffer.append("</Array>");
		stringBuffer.append("</MessageArray>");
		
		return stringBuffer.toString();
	}
	
	
	/**
	 * 播放异常xml
	 * @param playerMessageArrayBean
	 * @return
	 */
	public static String createPlayerExceptionXml(
			PlayerMessageArrayBean playerMessageArrayBean) {
		HeaderBean	headerBean=playerMessageArrayBean.getHeaderBean();
		List<PlayerMessageBean> playerMessageBeanlist=	playerMessageArrayBean.getPlayerMessageBeanList();
		StringBuffer stringBuffer=new  StringBuffer();
		stringBuffer.append("<?xml version='1.0' encoding='utf-8' standalone='no'?>");
		stringBuffer.append("<MessageArray count=\""+playerMessageArrayBean.getCount()+"\" >");
		stringBuffer.append("<Header ");
		
		stringBuffer.append("version=\""+Config.version+"\" ");
		stringBuffer.append("hid=\""+headerBean.getHid()+"\" ");
		stringBuffer.append("oemid=\""+headerBean.getOemid()+"\" ");
		stringBuffer.append("uid=\""+headerBean.getUid()+"\" ");
		stringBuffer.append("appid=\""+headerBean.getAppId()+"\" ");
		stringBuffer.append("appversion=\""+headerBean.getAppVersion()+"\" ");
		stringBuffer.append("packagename=\""+headerBean.getPackageName()+"\" ");
		stringBuffer.append("/>");
		stringBuffer.append("<Array>");
		
		if(null!=playerMessageBeanlist&&playerMessageBeanlist.size()>0)
		{
			for (PlayerMessageBean playerMessageBean:playerMessageBeanlist) {
				stringBuffer.append("<Message type=\"Player\" value=\"PlayException\" >");
				stringBuffer.append("<Body>");
				stringBuffer.append("CDATA[");
				stringBuffer.append("<PlayException ");
				
				if(null!=playerMessageBean.getErrcode()&&!"".equals(playerMessageBean.getErrcode().trim()))
				{
					stringBuffer.append(" errcode=\""+playerMessageBean.getErrcode()+"\" ");
				}
				if(null!=playerMessageBean.getInfo()&&!"".equals(playerMessageBean.getInfo().trim()))
				{
					stringBuffer.append(" info=\""+playerMessageBean.getInfo()+"\" ");
				}
				
				if(null!=playerMessageBean.getFid()&&!"".equals(playerMessageBean.getFid().trim()))
				{
					stringBuffer.append(" fid=\""+playerMessageBean.getFid()+"\" ");
				}
				if(null!=playerMessageBean.getPlaytime()&&!"".equals(playerMessageBean.getPlaytime().trim()))
				{
					stringBuffer.append(" playtime=\""+playerMessageBean.getPlaytime()+"\" ");
				}
				if(null!=playerMessageBean.getSessionid()&&!"".equals(playerMessageBean.getSessionid().trim()))
				{
					stringBuffer.append(" sessionid=\""+playerMessageBean.getSessionid()+"\" ");
				}
				stringBuffer.append("> ");
				stringBuffer.append("</PlayException>");
				stringBuffer.append("]");
				stringBuffer.append("</Body>");
				stringBuffer.append("</Message>");
			}
			
		}
		
		stringBuffer.append("</Array>");
		stringBuffer.append("</MessageArray>");
		
		return stringBuffer.toString();
	}
	
	
	
	/**
	 * 最新播放xml类
	 * 
	 * @param playerMessageArrayBean
	 * @return
	 */
	public static String createPlayerAllXml(
			PlayerMessageArrayBean playerMessageArrayBean) {
		HeaderBean	headerBean=playerMessageArrayBean.getHeaderBean();
		List<PlayerMessageBean> playerMessageBeanlist=	playerMessageArrayBean.getPlayerMessageBeanList();
		StringBuffer stringBuffer=new  StringBuffer();
		stringBuffer.append("<?xml version='1.0' encoding='utf-8' standalone='no'?>");
		stringBuffer.append("<MessageArray count=\""+playerMessageArrayBean.getCount()+"\" >");
		stringBuffer.append("<Header ");
		
		stringBuffer.append("version=\""+Config.version+"\" ");
		stringBuffer.append("hid=\""+headerBean.getHid()+"\" ");
		stringBuffer.append("oemid=\""+headerBean.getOemid()+"\" ");
		stringBuffer.append("uid=\""+headerBean.getUid()+"\" ");
		stringBuffer.append("appid=\""+headerBean.getAppId()+"\" ");
		stringBuffer.append("appversion=\""+headerBean.getAppVersion()+"\" ");
		stringBuffer.append("packagename=\""+headerBean.getPackageName()+"\" ");
		stringBuffer.append("/>");
		stringBuffer.append("<Array>");
		
		if(null!=playerMessageBeanlist&&playerMessageBeanlist.size()>0)
		{
			for (PlayerMessageBean playerMessageBean:playerMessageBeanlist) {
				//如果是异常消息的话
				if(PlayerMessageBean.PLAYER_MESSAEG_TYPE_EXCETPION==playerMessageBean.getPlayerMessageType())
				{
					stringBuffer.append("<Message type=\"Player\" value=\"PlayException\" >");
					stringBuffer.append("<Body>");
					stringBuffer.append("CDATA[");
					stringBuffer.append("<PlayException ");
					
					if(null!=playerMessageBean.getErrcode()&&!"".equals(playerMessageBean.getErrcode().trim()))
					{
						stringBuffer.append(" errcode=\""+playerMessageBean.getErrcode()+"\" ");
					}
					if(null!=playerMessageBean.getInfo()&&!"".equals(playerMessageBean.getInfo().trim()))
					{
						stringBuffer.append(" info=\""+playerMessageBean.getInfo()+"\" ");
					}
					
					if(null!=playerMessageBean.getFid()&&!"".equals(playerMessageBean.getFid().trim()))
					{
						stringBuffer.append(" fid=\""+playerMessageBean.getFid()+"\" ");
					}
					if(null!=playerMessageBean.getPlaytime()&&!"".equals(playerMessageBean.getPlaytime().trim()))
					{
						stringBuffer.append(" playtime=\""+playerMessageBean.getPlaytime()+"\" ");
					}
					if(null!=playerMessageBean.getSessionid()&&!"".equals(playerMessageBean.getSessionid().trim()))
					{
						stringBuffer.append(" sessionid=\""+playerMessageBean.getSessionid()+"\" ");
					}
					stringBuffer.append("> ");
					stringBuffer.append("</PlayException>");
					stringBuffer.append("]");
					stringBuffer.append("</Body>");
					stringBuffer.append("</Message>");
				}
				else if(PlayerMessageBean.PLAYER_MESSAEG_TYPE_STATUS==playerMessageBean.getPlayerMessageType())
				{

					stringBuffer.append("<Message type=\"Player\" value=\"PlayStatus\" >");
					stringBuffer.append("<Body>");
					stringBuffer.append("CDATA[");
					stringBuffer.append("<PlayStatus ");
					
					if(null!=playerMessageBean.getAction()&&!"".equals(playerMessageBean.getAction().trim()))
					{
						stringBuffer.append(" action=\""+playerMessageBean.getAction()+"\" ");
					}
					
					if(null!=playerMessageBean.getFid()&&!"".equals(playerMessageBean.getFid().trim()))
					{
						stringBuffer.append(" fid=\""+playerMessageBean.getFid()+"\" ");
					}
					if(null!=playerMessageBean.getPlaytime()&&!"".equals(playerMessageBean.getPlaytime().trim()))
					{
						stringBuffer.append(" playtime=\""+playerMessageBean.getPlaytime()+"\" ");
					}
					if(null!=playerMessageBean.getSessionid()&&!"".equals(playerMessageBean.getSessionid().trim()))
					{
						stringBuffer.append(" sessionid=\""+playerMessageBean.getSessionid()+"\" ");
					}
					stringBuffer.append("> ");
					stringBuffer.append("</PlayStatus>");
					stringBuffer.append("]");
					stringBuffer.append("</Body>");
					stringBuffer.append("</Message>");
				}
				else if(PlayerMessageBean.PLAYER_MESSAEG_TYPE_PLAYER==playerMessageBean.getPlayerMessageType())
				{

					stringBuffer.append("<Message type=\"Player\" value=\"PlayOperation\" >");
					stringBuffer.append("<Body>");
					stringBuffer.append("CDATA[");
					stringBuffer.append("<PlayOperation ");
					
					
					if(null!=playerMessageBean.getAction()&&!"".equals(playerMessageBean.getAction().trim()))
					{
						stringBuffer.append(" action=\""+playerMessageBean.getAction()+"\" ");
					}
					
					
					if(null!=playerMessageBean.getAction()&&!"".equals(playerMessageBean.getAction().trim()))
					{
						stringBuffer.append(" operationType=\""+playerMessageBean.getAction()+"\" ");
					}
					
					if(null!=playerMessageBean.getFid()&&!"".equals(playerMessageBean.getFid().trim()))
					{
						stringBuffer.append(" fid=\""+playerMessageBean.getFid()+"\" ");
					}
					if(null!=playerMessageBean.getPlaytime()&&!"".equals(playerMessageBean.getPlaytime().trim()))
					{
						stringBuffer.append(" playtime=\""+playerMessageBean.getPlaytime()+"\" ");
					}
					if(null!=playerMessageBean.getSeektime()&&!"".equals(playerMessageBean.getSeektime().trim()))
					{
						stringBuffer.append(" seektime=\""+playerMessageBean.getSeektime()+"\" ");
					}
					if(null!=playerMessageBean.getSessionid()&&!"".equals(playerMessageBean.getSessionid().trim()))
					{
						stringBuffer.append(" sessionid=\""+playerMessageBean.getSessionid()+"\" ");
					}
					stringBuffer.append("> ");
					stringBuffer.append("<VideoName>CDATA[");
					if(null!=playerMessageBean.getVideoName()&&!"".equals(playerMessageBean.getVideoName().trim()))
					{
						stringBuffer.append(playerMessageBean.getVideoName());
					}
					stringBuffer.append("]</VideoName>");
					stringBuffer.append("</PlayOperation>");
					stringBuffer.append("]");
					stringBuffer.append("</Body>");
					stringBuffer.append("</Message>");
				
				}
				else if(PlayerMessageBean.PLAYER_MESSAEG_TYPE_STARTPLAYER==playerMessageBean.getPlayerMessageType())
				{


					stringBuffer.append("<Message type=\"Player\" value=\"PlayPhrase\" >");
					stringBuffer.append("<Body>");
					stringBuffer.append("CDATA[");
					stringBuffer.append("<PlayPhrase ");
					
					if(null!=playerMessageBean.getAction()&&!"".equals(playerMessageBean.getAction().trim()))
					{
						stringBuffer.append(" action=\""+playerMessageBean.getAction()+"\" ");
					}
					
					if(null!=playerMessageBean.getFid()&&!"".equals(playerMessageBean.getFid().trim()))
					{
						stringBuffer.append(" fid=\""+playerMessageBean.getFid()+"\" ");
					}
					if(null!=playerMessageBean.getDuration()&&!"".equals(playerMessageBean.getDuration().trim()))
					{
						stringBuffer.append(" duration=\""+playerMessageBean.getDuration()+"\" ");
					}
					if(null!=playerMessageBean.getSessionid()&&!"".equals(playerMessageBean.getSessionid().trim()))
					{
						stringBuffer.append(" sessionid=\""+playerMessageBean.getSessionid()+"\" ");
					}
					stringBuffer.append("> ");
					stringBuffer.append("</PlayPhrase>");
					stringBuffer.append("]");
					stringBuffer.append("</Body>");
					stringBuffer.append("</Message>");
				
				}
			}
			
		}
		
		stringBuffer.append("</Array>");
		stringBuffer.append("</MessageArray>");
		
		return stringBuffer.toString();
	}
	
	
	
	
	/**
	 * 终端信息xml生成方法
	 * 
	 * @param teriminalInfoMessageArrayBean
	 * @return
	 */
	
	
	
	
	
	public static String createTerminalInfoXml(
			TerminalInfoMessageBean terminalInfoMessageBean,HeaderBean headerBean, String  apkStartType,String   UpgradeVersion,  String TerminaLogVersion,  String tongjiVersion,String sdkModuleVersion,String sdkModuleType,String isauth,String sn,String deviceid) {
		StringPrint.print("正在创建 createTerminalInfoXml");
		StringBuffer stringBuffer=new  StringBuffer();
		stringBuffer.append("<?xml version='1.0' encoding='utf-8' standalone='no'?>");
		stringBuffer.append("<MessageArray count=\""+1+"\" >");
		stringBuffer.append("<Header ");
		//stringBuffer.append("version=\""+Config.version+"\" ");
		//stringBuffer.append("hid=\""+"j8fewjwei"+"\" ");
		//stringBuffer.append("oemid=\""+"2.0.1"+"\" ");
		//stringBuffer.append("uid=\""+"237983458"+"\" ");
		//stringBuffer.append("appid=\""+"200"+"\" ");
		//stringBuffer.append("appversion=\""+"2.0.1"+"\" ");
		//stringBuffer.append("packagename=\""+"com.voole.debugTraceReport"+"\" ");
		stringBuffer.append("/>");
		stringBuffer.append("<Array>");
		
				//如果是异常消息的话
					stringBuffer.append("<Message type=\"appinfo\" >");
					stringBuffer.append("<Body>");
					stringBuffer.append("CDATA[");
					stringBuffer.append("<info ");
					
					if(null!=terminalInfoMessageBean.getHid()&&!"".equals(terminalInfoMessageBean.getHid().trim()))
					{
						stringBuffer.append(" hid=\""+terminalInfoMessageBean.getHid()+"\" ");
					}
					if(null!=terminalInfoMessageBean.getOemid()&&!"".equals(terminalInfoMessageBean.getOemid().trim()))
					{
						stringBuffer.append(" oemid=\""+terminalInfoMessageBean.getOemid()+"\" ");
					}
					
					if(null!=terminalInfoMessageBean.getUid()&&!"".equals(terminalInfoMessageBean.getUid().trim()))
					{
						stringBuffer.append(" uid=\""+terminalInfoMessageBean.getUid()+"\" ");
					}
					if(null!=terminalInfoMessageBean.getVooleauth()&&!"".equals(terminalInfoMessageBean.getVooleauth().trim()))
					{
						stringBuffer.append(" vooleauth=\""+terminalInfoMessageBean.getVooleauth()+"\" ");
					}
					if(null!=terminalInfoMessageBean.getAuthcompile ()&&!"".equals(terminalInfoMessageBean.getAuthcompile().trim()))
					{
						stringBuffer.append(" authcompile=\""+terminalInfoMessageBean.getAuthcompile()+"\" ");
					}
					
					if(null!=terminalInfoMessageBean.getVooleagent ()&&!"".equals(terminalInfoMessageBean.getVooleagent().trim()))
					{
						stringBuffer.append(" vooleagent=\""+terminalInfoMessageBean.getVooleagent()+"\" ");
					} 
					
					if(null!=terminalInfoMessageBean.getAgentcompile ()&&!"".equals(terminalInfoMessageBean.getAgentcompile().trim()))
					{
						stringBuffer.append(" agentcompile=\""+terminalInfoMessageBean.getAgentcompile()+"\" ");
					} 
					 
					
					if (!StringUtil.isNull(apkStartType)) {
						apkStartType = "";
					}
					if (!StringUtil.isNull(UpgradeVersion)) {
						UpgradeVersion = "";
					}
					if (!StringUtil.isNull(TerminaLogVersion)) {
						TerminaLogVersion = "";
					}
					if (!StringUtil.isNull(tongjiVersion)) {
						tongjiVersion = "";
					}
					
					
					
						stringBuffer.append(" apkStartType=\""+apkStartType+"\" ");
						stringBuffer.append(" UpgradeVersion=\""+UpgradeVersion+"\" ");
						stringBuffer.append(" TerminaLogVersion=\""+TerminaLogVersion+"\" ");
						stringBuffer.append(" statisticsVersion=\""+tongjiVersion+"\" ");
						
						
						stringBuffer.append(" sdkmoduleversion=\""+sdkModuleVersion+"\" ");
						stringBuffer.append(" sdkmoduletype=\""+sdkModuleType+"\" ");
						
						 
						
						stringBuffer.append(" isauth=\""+isauth+"\" ");
						stringBuffer.append(" sn=\""+sn+"\" ");
						stringBuffer.append(" deviceid=\""+deviceid+"\" ");
						
						
						
					stringBuffer.append("> ");
				 
					stringBuffer.append("<agentlibs><![CDATA[");
					if(null!=terminalInfoMessageBean.getAgentlibs()&&!"".equals(terminalInfoMessageBean.getAgentlibs().trim()))
					{
						stringBuffer.append(terminalInfoMessageBean.getAgentlibs());
					}
					stringBuffer.append("]]></agentlibs>");
					
					
					
					stringBuffer.append("</info>");
					stringBuffer.append("]");
					stringBuffer.append("</Body>");
					stringBuffer.append("</Message>");
					
					
		
		stringBuffer.append("</Array>");
		stringBuffer.append("</MessageArray>");
		
		return stringBuffer.toString();
	}
	
	
	
	
	/**
	 * 终端信息xml生成方法
	 * 
	 * @param teriminalInfoMessageArrayBean
	 * @return
	 */
	public static String createTerminalInfoXmlBS(  String apkStartType,  String UpgradeVersion,
			  String TerminaLogVersion, 
			  String tongjiVersion,
			TeriminalInfoMessageArrayBean teriminalInfoMessageArrayBean) {
		
		List<TerminalInfoMessageBean> terminalInfoMessageBeanList=	teriminalInfoMessageArrayBean.getTerminalInfoMessageBeanList();
		StringBuffer stringBuffer=new  StringBuffer();
		stringBuffer.append("<?xml version='1.0' encoding='utf-8' standalone='no'?>");
		stringBuffer.append("<MessageArray count=\""+teriminalInfoMessageArrayBean.getCount()+"\" >");
		stringBuffer.append("<Header ");
		//stringBuffer.append("version=\""+Config.version+"\" ");
		//stringBuffer.append("hid=\""+"j8fewjwei"+"\" ");
		//stringBuffer.append("oemid=\""+"2.0.1"+"\" ");
		//stringBuffer.append("uid=\""+"237983458"+"\" ");
		//stringBuffer.append("appid=\""+"200"+"\" ");
		//stringBuffer.append("appversion=\""+"2.0.1"+"\" ");
		//stringBuffer.append("packagename=\""+"com.voole.debugTraceReport"+"\" ");
		stringBuffer.append("/>");
		stringBuffer.append("<Array>");
		
		if(null!=terminalInfoMessageBeanList&&terminalInfoMessageBeanList.size()>0)
		{
			for (TerminalInfoMessageBean terminalInfoMessageBean:terminalInfoMessageBeanList) {
				//如果是异常消息的话
				if(TerminalInfoMessageBean.EDITION_TYPE==terminalInfoMessageBean.getMessageType())
				{
					stringBuffer.append("<Message type=\"appinfo\" >");
					stringBuffer.append("<Body>");
					stringBuffer.append("CDATA[");
					stringBuffer.append("<info ");
					
					if(null!=terminalInfoMessageBean.getHid()&&!"".equals(terminalInfoMessageBean.getHid().trim()))
					{
						stringBuffer.append(" hid=\""+terminalInfoMessageBean.getHid()+"\" ");
					}
					if(null!=terminalInfoMessageBean.getOemid()&&!"".equals(terminalInfoMessageBean.getOemid().trim()))
					{
						stringBuffer.append(" oemid=\""+terminalInfoMessageBean.getOemid()+"\" ");
					}
					
					if(null!=terminalInfoMessageBean.getUid()&&!"".equals(terminalInfoMessageBean.getUid().trim()))
					{
						stringBuffer.append(" uid=\""+terminalInfoMessageBean.getUid()+"\" ");
					}
					if(null!=terminalInfoMessageBean.getVooleauth()&&!"".equals(terminalInfoMessageBean.getVooleauth().trim()))
					{
						stringBuffer.append(" vooleauth=\""+terminalInfoMessageBean.getVooleauth()+"\" ");
					}
					if(null!=terminalInfoMessageBean.getAuthcompile ()&&!"".equals(terminalInfoMessageBean.getAuthcompile().trim()))
					{
						stringBuffer.append(" authcompile=\""+terminalInfoMessageBean.getAuthcompile()+"\" ");
					}
					
					if(null!=terminalInfoMessageBean.getVooleagent ()&&!"".equals(terminalInfoMessageBean.getVooleagent().trim()))
					{
						stringBuffer.append(" vooleagent=\""+terminalInfoMessageBean.getVooleagent()+"\" ");
					} 
					
					if(null!=terminalInfoMessageBean.getAgentcompile ()&&!"".equals(terminalInfoMessageBean.getAgentcompile().trim()))
					{
						stringBuffer.append(" agentcompile=\""+terminalInfoMessageBean.getAgentcompile()+"\" ");
					} 
					
					
					
					if (!StringUtil.isNull(apkStartType)) {
						apkStartType = "";
					}
					if (!StringUtil.isNull(UpgradeVersion)) {
						UpgradeVersion = "";
					}
					if (!StringUtil.isNull(TerminaLogVersion)) {
						TerminaLogVersion = "";
					}
					if (!StringUtil.isNull(tongjiVersion)) {
						tongjiVersion = "";
					}
					
					
					
						stringBuffer.append(" apkStartType=\""+apkStartType+"\" ");
						stringBuffer.append(" UpgradeVersion=\""+UpgradeVersion+"\" ");
						stringBuffer.append(" TerminaLogVersion=\""+TerminaLogVersion+"\" ");
						stringBuffer.append(" statisticsVersion=\""+tongjiVersion+"\" ");
					
					
					stringBuffer.append("> ");
				 
					stringBuffer.append("<agentlibs><![CDATA[");
					if(null!=terminalInfoMessageBean.getAgentlibs()&&!"".equals(terminalInfoMessageBean.getAgentlibs().trim()))
					{
						stringBuffer.append(terminalInfoMessageBean.getAgentlibs());
					}
					stringBuffer.append("]]></agentlibs>");
					
					
					
					stringBuffer.append("</info>");
					stringBuffer.append("]");
					stringBuffer.append("</Body>");
					stringBuffer.append("</Message>");
				}
				else if(TerminalInfoMessageBean.CONFIGURE_TYPE==terminalInfoMessageBean.getMessageType())
				{

					stringBuffer.append("<Message type=\"appconf\"   >");
					stringBuffer.append("<Body>");
					stringBuffer.append("CDATA[");
					stringBuffer.append("<conf ");
					stringBuffer.append("> ");
					stringBuffer.append("<properties><![CDATA[");
					if(null!=terminalInfoMessageBean.getProperties()&&!"".equals(terminalInfoMessageBean.getProperties().trim()))
					{
						stringBuffer.append(terminalInfoMessageBean.getProperties());
					}
					stringBuffer.append("]]></properties>");
					
					stringBuffer.append("<voolert><![CDATA[");
					if(null!=terminalInfoMessageBean.getVoolert()&&!"".equals(terminalInfoMessageBean.getVoolert().trim()))
					{
						stringBuffer.append(terminalInfoMessageBean.getVoolert());
					}
					stringBuffer.append("]]></voolert>");
					
					
					
					stringBuffer.append("</conf>");
					stringBuffer.append("]");
					stringBuffer.append("</Body>");
					stringBuffer.append("</Message>");
				}
			}
			
		}
		
		stringBuffer.append("</Array>");
		stringBuffer.append("</MessageArray>");
		
		return stringBuffer.toString();
	}
	
	
	
	/**
	 * 终端版本信息xml生成方法
	 * 
	 * @param teriminalInfoMessageArrayBean
	 * @return
	 */
	public static String createTerminalInfoBanbenXml(String properties,String voolert) {
		
		StringBuffer stringBuffer=new  StringBuffer();
		stringBuffer.append("<?xml version='1.0' encoding='utf-8' standalone='no'?>");
		stringBuffer.append("<MessageArray count=\""+1+"\" >");
		stringBuffer.append("<Header ");
 		stringBuffer.append("/>");
		stringBuffer.append("<Array>");
		
				 

					stringBuffer.append("<Message type=\"appconf\"   >");
					stringBuffer.append("<Body>");
					stringBuffer.append("CDATA[");
					stringBuffer.append("<conf ");
					stringBuffer.append("> ");
					stringBuffer.append("<properties><![CDATA[");
					if(null!=properties&&!"".equals(properties))
					{
						stringBuffer.append(properties);
					}
					stringBuffer.append("]]></properties>");
					
					stringBuffer.append("<voolert><![CDATA[");
					if(null!=voolert&&!"".equals(voolert))
					{
						stringBuffer.append(voolert);
					}
					stringBuffer.append("]]></voolert>");
					
					
					
					stringBuffer.append("</conf>");
					stringBuffer.append("]");
					stringBuffer.append("</Body>");
					stringBuffer.append("</Message>");
			
		
		stringBuffer.append("</Array>");
		stringBuffer.append("</MessageArray>");
		
		return stringBuffer.toString();
	}
	
	
}
