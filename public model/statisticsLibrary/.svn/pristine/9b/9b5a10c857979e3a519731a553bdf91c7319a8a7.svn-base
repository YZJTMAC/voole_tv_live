package com.voole.statistics.userinfo;


import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class ProxyParser {

	private String httpMessage;
	private Proxy proxy;
	public ProxyParser(String httpMessage) {
		super();
		this.httpMessage = httpMessage;
	}
	
	
	public void parser() {
		XmlPullParserFactory xmlPullParserFactory;
		try {
			xmlPullParserFactory = XmlPullParserFactory.newInstance();
			xmlPullParserFactory.setNamespaceAware(true);
			XmlPullParser xpp = xmlPullParserFactory.newPullParser();
			xpp.setInput(new StringReader(httpMessage));
			int eventType = xpp.getEventType();
			String tag = "";
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					proxy = new Proxy();
					break;
				case XmlPullParser.START_TAG:
					tag = xpp.getName();
					// Log.d("ACCOUNTINFOPARSER","Start:" + xpp.getName());
					break;
				case XmlPullParser.END_TAG:
					// Log.d("ACCOUNTINFOPARSER","End:" + xpp.getName());
					break;
				case XmlPullParser.TEXT:
					if ("info".equals(tag)) {
					} else if ("version".equals(tag)) {
						proxy.setVersion(xpp.getText());
					} else if ("build_time".equals(tag)) {
						proxy.setBuildTime(xpp.getText());
					} else if ("vlive_version".equals(tag)) {
						proxy.setVliveVersion(xpp.getText());
					} else if ("p2plive_version".equals(tag)) {
						proxy.setP2pliveVersion(xpp.getText());
					} else if ("vooletv_version".equals(tag)) {
						proxy.setVooletvVersion(xpp.getText());
					} else if ("m3u8_version".equals(tag)) {
						proxy.setM3u8Version(xpp.getText());
					}else if ("vbr_version".equals(tag)) {
						proxy.setVbrVersion(xpp.getText());
					} 
					break;
				}
				eventType = xpp.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Proxy getProxy() {
		return proxy;
	}
	
	
	
}
