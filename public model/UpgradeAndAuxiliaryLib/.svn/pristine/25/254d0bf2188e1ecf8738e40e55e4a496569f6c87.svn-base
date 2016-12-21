package com.voole.android.client.UpAndAu.model.parser;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.voole.android.client.UpAndAu.model.LocalProxyInfo;
import com.voole.android.client.UpAndAu.util.HttpUtil;


/**
 * 解析本地代理信息
 * @author LEE
 * 
 *
 */
public class LocalProxyInfoParser extends CommonParser{
	
	public LocalProxyInfo ParseUrl(String url) {
		return ParseUrlByPull(url);
	}
	
	/**
	 *  通过url 解析信息
	 * @param url String
	 * @return UpdateInfo
	 * 
	 * 
	 */
	public LocalProxyInfo ParseUrlByPull(String url) {
		LocalProxyInfo localProxyInfo = null;
		HttpUtil httpUtil = new HttpUtil();
		InputStream in = null;
		try {
			in = httpUtil.doGet(url, null, 2000, 5000, null);
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(in, HttpUtil.Encoding_UTF8);
			localProxyInfo = ParseByPull(parser);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			httpUtil.closeInputStream(in);
		}
		
		return localProxyInfo;
	}
	
	
	/**
	 *  通过解析对象解析
	 * @param parser XmlPullParser
	 * @return UpdateInfo
	 *  
	 * 
	 */
	private LocalProxyInfo ParseByPull(XmlPullParser parser) {
		LocalProxyInfo localProxyInfo = null;
		String tagName;
		try {
			int eventType=parser.getEventType();
			
			while(eventType != XmlPullParser.END_DOCUMENT) {
				switch(eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					tagName = parser.getName();
					if(!"info".equals(tagName) && parser.getDepth() == 1) {
						return null;
					} else if("info".equals(tagName)) {
						localProxyInfo = new LocalProxyInfo();
					} else if("version".equals(tagName)) {
						localProxyInfo.setVersion(parser.nextText());
					} else if("build_time".equals(tagName)) {
						localProxyInfo.setBuildVersion(parser.nextText());
					} else if("vlive_version".equals(tagName)) {
						localProxyInfo.setVliveVersion(parser.nextText());
					} else if("bufratio".equals(tagName)) {
						localProxyInfo.setBufratio(parser.nextText());
					} else if("downspeed".equals(tagName)) {
						localProxyInfo.setDownspeed(parser.nextText());
					} else if("vooletv_version".equals(tagName)) {
						localProxyInfo.setVooletvVersion(parser.nextText());
					} else if("agent_version".equals(tagName)) {
						localProxyInfo.setAgentVersion(parser.nextText());
					}else if("realtime_speed".equals(tagName)) {
						localProxyInfo.setRealtimeSpeed(parser.nextText());
					}else if("average_speed".equals(tagName)) {
						localProxyInfo.setAverageSpeed(parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					tagName = parser.getName();
					break;
				}
				
				eventType = parser.next();  
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return localProxyInfo;
	}

}
