package com.gntv.tv.model.test;

import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import com.gntv.tv.common.base.BaseParser;


public class SpeedParser extends BaseParser {
	
	private SpeedModel speedModel;
	private ArrayList<String> hostList;

	@Override
	public void parse(XmlPullParser xpp) throws Exception {
		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				speedModel = new SpeedModel();
				break;
			case XmlPullParser.START_TAG:
				if ("status".equals(xpp.getName())) {
					speedModel.setStatus(xpp.nextText());
				}else if ("resultdesc".equals(xpp.getName())) {
					speedModel.setResultdesc(xpp.nextText());
				}else if("hostlist".equals(xpp.getName())){
					hostList = new ArrayList<String>();
					speedModel.setHostList(hostList);
				}else if ("host".equals(xpp.getName())) {
					int size = xpp.getAttributeCount();
					for (int i = 0; i < size; i++) {
						if ("servicepath".equals(xpp.getAttributeName(i))){
							hostList.add(xpp.getAttributeValue(i));
							break;
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

	public SpeedModel getSpeedModel() {
		return speedModel;
	}

	public void setSpeedModel(SpeedModel speedModel) {
		this.speedModel = speedModel;
	}
}
