package com.gntv.tv.model.channel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.model.base.BaseChannelProgramInfoParser;

public class ChannelProgramInfoParser extends BaseChannelProgramInfoParser{

	private DateItem dateItem = null;
	private ProgramItem programItem = null;
	
	private List<DateItem> dateList;
	private List<ProgramItem> programList;

	private void parse(XmlPullParser xpp) throws Exception {
		LogUtil.i("ChannelProgramInfoParser--->parse------>start");
		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				info = new ChannelProgramInfo();
				dateList = new ArrayList<DateItem>();
				break;
			case XmlPullParser.START_TAG:
				if ("LiveClassList".equalsIgnoreCase(xpp.getName())) {
					int size = xpp.getAttributeCount();
					for (int i = 0; i < size; i++) {
						if ("status".equals(xpp.getAttributeName(i))){
							info.setStatus(xpp.getAttributeValue(i));
						}else if ("Time".equals(xpp.getAttributeName(i)))
							info.setRequestTime(xpp.getAttributeValue(i));
					}
				} else if ("Error".equalsIgnoreCase(xpp.getName())) {
					info.setResultDesc(xpp.nextText());
				} else if("ContentSet".equalsIgnoreCase(xpp.getName())){
					dateItem = new DateItem();
					programList = new ArrayList<ProgramItem>();
					int size = xpp.getAttributeCount();
					for (int i =0;i<size;i++){
						if("PlayDate".equalsIgnoreCase(xpp.getAttributeName(i))){
							dateItem.setDate(xpp.getAttributeValue(i));
						}
					}
					dateList.add(dateItem);
				} else if("Content".equalsIgnoreCase(xpp.getName())){
					programItem = new ProgramItem();
					int size = xpp.getAttributeCount();
					for (int i =0;i<size;i++){
						if("PlayerTime".equalsIgnoreCase(xpp.getAttributeName(i))){
							String s = xpp.getAttributeValue(i);
							programItem.setStartTime(s);
							/*long sTimeL = DateUtil.string2Msec(s, "yyyy-MM-dd kk:mm:ss");
							LogUtil.d("ChannelProgramInfoParser-->sTimeL-->" + sTimeL);
							programItem.setlStartTime(sTimeL);*/
						}else if("StopTime".equalsIgnoreCase(xpp.getAttributeName(i))){
							String s = xpp.getAttributeValue(i);
							programItem.setStopTime(s);
//							programItem.setlStopTime(DateUtil.string2Msec(s, "yyyy-MM-dd kk:mm:ss"));
						}else if("PlayerTimeLong".equalsIgnoreCase(xpp.getAttributeName(i))){
							String s = xpp.getAttributeValue(i);
							long l = 0;
							try {
								l = Long.parseLong(s);
							} catch (Exception e) {
								// TODO: handle exception
							}
							programItem.setlStartTime(l);
						}else if("StopTimeLong".equalsIgnoreCase(xpp.getAttributeName(i))){
							String s = xpp.getAttributeValue(i);
							long l = 0;
							try {
								l = Long.parseLong(s);
							} catch (Exception e) {
								// TODO: handle exception
							}
							programItem.setlStopTime(l);
						}else if("ProgramName".equalsIgnoreCase(xpp.getAttributeName(i))){
							programItem.setProgramName(xpp.getAttributeValue(i));
						}else if("Mid".equalsIgnoreCase(xpp.getAttributeName(i))){
							programItem.setMid(xpp.getAttributeValue(i));
						}else if("Fid".equalsIgnoreCase(xpp.getAttributeName(i))){
							programItem.setFid(xpp.getAttributeValue(i));
						}else if("Sid".equalsIgnoreCase(xpp.getAttributeName(i))){
							programItem.setSid(xpp.getAttributeValue(i));
						}
					}
					programList.add(programItem);
				} 
				break;
			case XmlPullParser.END_TAG:
				if("LiveClassList".equalsIgnoreCase(xpp.getName())){
					info.setDateList(dateList);
				}else if("ContentSet".equalsIgnoreCase(xpp.getName())){
					dateItem.setProgramItemList(programList);
				}
				break;
			}
			eventType = xpp.next();
		}

		LogUtil.i("ChannelProgramInfoParser--->parse------>end");
	}

	@Override
	public void setInputStream(InputStream inputStream) throws Exception {
		if (inputStream != null) {
			XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
			xmlPullParserFactory.setNamespaceAware(true);
			XmlPullParser xpp = xmlPullParserFactory.newPullParser();
			xpp.setInput(inputStream, "UTF-8");
			parse(xpp);
			inputStream.close();
			inputStream = null;
		}
	}
	
}
