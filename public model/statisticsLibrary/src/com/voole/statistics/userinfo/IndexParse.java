package com.voole.statistics.userinfo;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.voole.statistics.bean.ConfigFileBean;
import com.voole.statistics.config.Config;
import com.voole.statistics.util.BaseHttpInputStream;
import com.voole.statistics.util.StringPrint;
/**
 * @author jacky
 *
 */
public class IndexParse {


	 
	public User parse(String urlPath,ConfigFileBean cfb)
			throws IOException, Exception {
		
		
		String address=Config.auth;
		if(null!=cfb)
		{
			String httpport=cfb.getLocal_http_port();
			if(null!=httpport&&!"".equals(httpport.trim()))
			{
				//替换
				address= address.replaceAll("18080",  httpport.trim());
			}
		}
		StringPrint.print("address ca="+address +"   cfb="+cfb);
		InputStream is=BaseHttpInputStream.getInstance().getInputStream(address, null);
	      
		
		User user=new User();
		try {
			XmlPullParser pull = Xml.newPullParser();
			pull.setInput(is, "UTF-8");
			for (int eventType = pull.next(); eventType != XmlPullParser.END_DOCUMENT; eventType = pull
					.next()) {
				String name = pull.getName();
				switch (eventType) {
				case XmlPullParser.START_TAG:
					   if ("status".equals(name)) {
						   pull.next();
						user.setStatus( pull.getText());
					} else if ("uid".equals(name)) {
						 pull.next();
						user.setUid(pull.getText());
						break;
					} else if ("hid".equals(name)) {
						 pull.next();
						user.setHid(pull.getText());
						break;
					} else if ("oemid".equals(name)) {
						 pull.next();
						user.setOemid(pull.getText());
						break;
					} else if ("sid".equals(name)) {
						 pull.next();
						user.setSid(pull.getText());
						break;
					} else if ("portal".equals(name)) {
						 pull.next();
						user.setPortal(pull.getText());
						break;
					}else if ("version".equals(name)) {
						 pull.next();
						user.setVersion(pull.getText());
						break;
					}else if ("buildtime".equals(name)) {
						 pull.next();
						user.setBuildtime(pull.getText());
						break;
					}
						
				case XmlPullParser.END_TAG:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		return user;
	}
	
	
	 
		public Proxy parsedaili(ConfigFileBean cfb)
				throws IOException, Exception {
			
			
			String address=Config.daili;
			if(null!=cfb)
			{
				String httpport=cfb.getLocal_agent_http_port();
				if(null!=httpport&&!"".equals(httpport.trim()))
				{
					//替换
					address= address.replaceAll("5656",  httpport.trim());
				}
			}
			StringPrint.print("address="+address);
			InputStream is=BaseHttpInputStream.getInstance().getInputStream(address, null);
		      
			
			Proxy proxy=new Proxy();
			try {
				XmlPullParser pull = Xml.newPullParser();
				pull.setInput(is, "UTF-8");
				for (int eventType = pull.next(); eventType != XmlPullParser.END_DOCUMENT; eventType = pull
						.next()) {
					String name = pull.getName();
					switch (eventType) {
					case XmlPullParser.START_TAG:
						
						 
						
						   if ("version".equals(name)) {
							   pull.next();
							   proxy.setVersion(pull.getText());
						} else if ("build_time".equals(name)) {
							 pull.next();
								proxy.setBuildTime(pull.getText());
							break;
						} else if ("vlive_version".equals(name)) {
							 pull.next();
								proxy.setVliveVersion(pull.getText());
							break;
						} else if ("p2plive_version".equals(name)) {
							 pull.next();
								proxy.setP2pliveVersion(pull.getText());
							break;
						} else if ("vooletv_version".equals(name)) {
							 pull.next();
								proxy.setVooletvVersion(pull.getText());
							break;
						} else if ("m3u8_version".equals(name)) {
							 pull.next();
								proxy.setM3u8Version(pull.getText());
							break;
						}else if ("vbr_version".equals(name)) {
							 pull.next();
							 proxy.setVbrVersion(pull.getText());
							break;
						} 
					case XmlPullParser.END_TAG:
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				if (is != null)
					try {
						is.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
			return proxy;
		}
}
