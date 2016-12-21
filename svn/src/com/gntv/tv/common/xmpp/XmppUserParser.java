package com.gntv.tv.common.xmpp;

import org.xmlpull.v1.XmlPullParser;

import com.gntv.tv.common.base.BaseParser;

public class XmppUserParser extends BaseParser {

	private XmppUser user = null;

	public XmppUser getUser() {
		return user;
	}

	@Override
	public void parse(XmlPullParser xpp) throws Exception {
		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				user = new XmppUser();
				break;
			case XmlPullParser.START_TAG:
				if ("status".equalsIgnoreCase(xpp.getName())) {
					user.status = xpp.nextText();
				} else if ("uid".equalsIgnoreCase(xpp.getName())) {
					user.uid = xpp.nextText();
				} else if ("password".equalsIgnoreCase(xpp.getName())) {
					user.pwd = xpp.nextText();
				}
				break;
			}
			eventType = xpp.next();
		}
	}

}
