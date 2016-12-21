package com.gntv.tv.model.time;

import org.xmlpull.v1.XmlPullParser;

import com.gntv.tv.common.base.BaseParser;

public class TimeParser extends BaseParser{
private String time = null;
	
	@Override
	public void parse(XmlPullParser xpp) throws Exception {
		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				if ("LiveLookbackList".equals(xpp.getName())) {
					int size = xpp.getAttributeCount();
					for (int i = 0; i < size; i++) {
						if ("Time".equals(xpp.getAttributeName(i))){
							time = xpp.getAttributeValue(i);
							return;
						}
					}
				} 
				break;
			case XmlPullParser.END_TAG:
				break;
			}
			eventType = xpp.next();
		}
	}

	public String getTime() {
		return time;
	}
	
}
