package com.voole.statistics.userinfo;


import java.io.IOException;
import java.io.StringReader; 

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


import com.voole.statistics.config.Config;
import com.voole.statistics.util.BaseHttpInputString;
import com.voole.statistics.util.StringPrint;

public class UserParser {
	private String httpMessage;
	private User user;

	public UserParser() {
		super();
	}

	public String getHttpMessage() {
		return httpMessage;
	}

	public void setHttpMessage(String httpMessage) {
		this.httpMessage = httpMessage;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User parse_()
	{
		try {
			String s=BaseHttpInputString.read_web(Config.auth);
			StringPrint.print(s);
			setHttpMessage(s);
			return parser();
		} catch (IOException e) {
			StringPrint.print("异常了"+e.getMessage());
			e.printStackTrace();
		}
		return user;
	}
	
	public User parser() {
		user = new User();
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
					break;
				case XmlPullParser.START_TAG:
					tag = xpp.getName();
					// Log.d("ACCOUNTINFOPARSER","Start:" + xpp.getName());
					break;
				case XmlPullParser.END_TAG:
					// Log.d("ACCOUNTINFOPARSER","End:" + xpp.getName());
					break;
				case XmlPullParser.TEXT:
					if ("user".equals(tag)) {
					} else if ("status".equals(tag)) {
						user.setStatus(xpp.getText());
					} else if ("uid".equals(tag)) {
						user.setUid(xpp.getText());
					} else if ("hid".equals(tag)) {
						user.setHid(xpp.getText());
					} else if ("oemid".equals(tag)) {
						user.setOemid(xpp.getText());
					} else if ("sid".equals(tag)) {
						user.setSid(xpp.getText());
					} else if ("portal".equals(tag)) {
						user.setPortal(xpp.getText());
					}else if ("version".equals(tag)) {
						user.setVersion(xpp.getText());
					}else if ("buildtime".equals(tag)) {
						user.setBuildtime(xpp.getText());
					}
					break;
				}
				eventType = xpp.next();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		StringPrint.print("omeid u="+user.getOemid());
		return user;
	}
}
