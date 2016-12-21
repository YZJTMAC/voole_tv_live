package com.gntv.tv.model.channel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.model.base.BaseTodayProgramParser;

public class TodayProgramInfoParser extends BaseTodayProgramParser{
	private AssortItem assortItem = null;
	private ChannelItem channelItem = null;
	private ResourceItem resourceItem = null;
	private DateItem dateItem = null;
	private ProgramItem programItem = null;
	
	private List<AssortItem> assortList = null;
	private List<ChannelItem> channelList = null;
	private List<ResourceItem> resourceList = null;
	private List<DateItem> dateList = null;
	private List<ProgramItem> programList = null;
	 
	private void parse(XmlPullParser xpp) throws Exception {
		LogUtil.i("TodayProgramInfoParser--->parse------>start");
		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				info = new TodayProgramInfo();
				assortList = new ArrayList<AssortItem>();
				
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
				} else if("LiveTvSort".equalsIgnoreCase(xpp.getName())){
					assortItem = new AssortItem();
					channelList = new ArrayList<ChannelItem>();
					int size = xpp.getAttributeCount();
					for (int i =0; i< size; i++){
						if("AssortName".equals(xpp.getAttributeName(i))){
							assortItem.setAssortName(xpp.getAttributeValue(i));
						}else if("AssortValue".equals(xpp.getAttributeName(i))){
							assortItem.setAssortValue(xpp.getAttributeValue(i));
						}
					}
					assortList.add(assortItem);
				} else if("LiveTv".equalsIgnoreCase(xpp.getName())){
					channelItem = new ChannelItem();
					dateList = new ArrayList<DateItem>();
					int size = xpp.getAttributeCount();
					for (int i =0; i< size; i++){
						if("TvId".equalsIgnoreCase(xpp.getAttributeName(i))){
							channelItem.setChannelId(xpp.getAttributeValue(i));
						}else if("Title".equalsIgnoreCase(xpp.getAttributeName(i))){
							channelItem.setTitle(xpp.getAttributeValue(i));
						}else if("Sequence".equalsIgnoreCase(xpp.getAttributeName(i))){
							channelItem.setSequence(xpp.getAttributeValue(i));
						}else if("backupenable".equalsIgnoreCase(xpp.getAttributeName(i))){
							channelItem.setBackEnable(xpp.getAttributeValue(i));
						}else if("isHDTV".equalsIgnoreCase(xpp.getAttributeName(i))){
							channelItem.setHasHD(xpp.getAttributeValue(i));
						}else if("channelNo".equalsIgnoreCase(xpp.getAttributeName(i))){
							channelItem.setChannelNo(xpp.getAttributeValue(i));
						}else if("IsBackView".equalsIgnoreCase(xpp.getAttributeName(i))){
							channelItem.setIsBackView(xpp.getAttributeValue(i));
						}
						
						/*else if("IsBackView".equalsIgnoreCase(xpp.getAttributeName(i))){
							channelItem.setIsBackView(xpp.getAttributeValue(i));
						}else if("isLive".equalsIgnoreCase(xpp.getAttributeName(i))){
							channelItem.setIsLive(xpp.getAttributeValue(i));
						}else if("cp".equalsIgnoreCase(xpp.getAttributeName(i))){
							channelItem.setCp(xpp.getAttributeValue(i));
						}*/
					}
					channelList.add(channelItem);
				} else if("MediaSources".equalsIgnoreCase(xpp.getName())){
					resourceList = new ArrayList<ResourceItem>();
				} else if("Source".equalsIgnoreCase(xpp.getName())){
					resourceItem = new ResourceItem();
					int size = xpp.getAttributeCount();
					for (int i =0;i<size;i++){
						if("ResourceId".equalsIgnoreCase(xpp.getAttributeName(i))){
							resourceItem.setResourceId(xpp.getAttributeValue(i));
						}else if("Name".equalsIgnoreCase(xpp.getAttributeName(i))){
							resourceItem.setResourceName(xpp.getAttributeValue(i));
//						}else if("resourcetype".equalsIgnoreCase(xpp.getAttributeName(i))){
//							resourceItem.setResourceType(xpp.getAttributeValue(i));
						}else if("tracker".equalsIgnoreCase(xpp.getAttributeName(i))){
							resourceItem.setTracker(xpp.getAttributeValue(i));
						}else if("bke_url".equalsIgnoreCase(xpp.getAttributeName(i))){
							resourceItem.setBkeUrl(xpp.getAttributeValue(i));
						}else if("isThirdParty".equalsIgnoreCase(xpp.getAttributeName(i))){
							resourceItem.setIs3rd(xpp.getAttributeValue(i));
						}else if("datatype".equalsIgnoreCase(xpp.getAttributeName(i))){
							resourceItem.setDataType(xpp.getAttributeValue(i));
						}else if("proto".equalsIgnoreCase(xpp.getAttributeName(i))){
							resourceItem.setProto(xpp.getAttributeValue(i));
						}else if("scale".equalsIgnoreCase(xpp.getAttributeName(i))){
							resourceItem.setScale(xpp.getAttributeValue(i));
						}
					}
					resourceList.add(resourceItem);
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
							/*long sTimeL =DateUtil.string2Msec(s, "yyyy-MM-dd HH:mm:ss");
							LogUtil.d("TodayProgramInfoParser-->sTimeL-->" + sTimeL);
							programItem.setlStartTime(sTimeL);*/
//							programItem.setlStartTime(DateUtil.string2Msec(s));
						}else if("StopTime".equalsIgnoreCase(xpp.getAttributeName(i))){
							String s = xpp.getAttributeValue(i);
							programItem.setStopTime(s);
							/*long eTimeL =DateUtil.string2Msec(s, "yyyy-MM-dd HH:mm:ss");
							LogUtil.d("TodayProgramInfoParser-->sTimeL-->" + eTimeL);
							programItem.setlStopTime(eTimeL);*/
//							programItem.setlStopTime(DateUtil.string2Msec(s));
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
					info.setAssortList(assortList);
				}else if("LiveTvSort".equalsIgnoreCase(xpp.getName())){
					assortItem.setChannelList(channelList);
				}else if("LiveTv".equalsIgnoreCase(xpp.getName())){
					channelItem.setDateList(dateList);
				}else if("MediaSources".equalsIgnoreCase(xpp.getName())){
					channelItem.setResourceList(resourceList);
				}else if("ContentSet".equalsIgnoreCase(xpp.getName())){
					dateItem.setProgramItemList(programList);
				}
				break;
			}
			eventType = xpp.next();
		}

		LogUtil.i("TodayProgramInfoParser--->parse------>end");
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
