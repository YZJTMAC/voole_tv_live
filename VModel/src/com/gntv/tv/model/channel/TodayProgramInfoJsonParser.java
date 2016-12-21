package com.gntv.tv.model.channel;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONReader;
import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.model.base.BaseTodayProgramParser;

public class TodayProgramInfoJsonParser extends BaseTodayProgramParser {
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

	public void setInputStream(InputStream inputStream) throws Exception {
		if (inputStream != null) {
			LogUtil.i("TodayProgramInfoJsonParser--->start parser");
			JSONReader reader = new JSONReader(new InputStreamReader(
					inputStream,"UTF-8"));
			info = new TodayProgramInfo();
			reader.startObject();
			String key;
			while (reader.hasNext()) {
				key = reader.readString();
				if (key.equals("Status")) {
					info.setStatus(reader.readObject().toString());
				} else if (key.equals("Time")) {
					info.setRequestTime(reader.readObject().toString());
				} else if (key.equals("liveTvSorts")) {
					assortList = new ArrayList<AssortItem>();
					reader.startArray();
					while (reader.hasNext()) {
						reader.startObject();
						while (reader.hasNext()) {
							key = reader.readString();
							if (key.equals("LiveTvSort")) {
								assortItem = new AssortItem();
								reader.startObject();
								while (reader.hasNext()) {
									key = reader.readString();
									if (key.equals("AssortName")) {
										assortItem.setAssortName(reader
												.readObject().toString());
									} else if (key.equals("AssortValue")) {
										assortItem.setAssortValue(reader
												.readObject().toString());
									} else if (key.equals("liveTvs")) {
										channelList = new ArrayList<ChannelItem>();
										reader.startArray();
										while (reader.hasNext()) {
											reader.startObject();
											while (reader.hasNext()) {
												key = reader.readString();
												if (key.equals("LiveTv")) {
													channelItem = new ChannelItem();
													reader.startObject();
													while (reader.hasNext()) {
														key = reader
																.readString();
														if (key.equals("Sequence")) {
															channelItem
																	.setSequence(reader
																			.readObject()
																			.toString());
														} else if (key
																.equals("Title")) {
															channelItem
																	.setTitle(reader
																			.readObject()
																			.toString());
														} else if (key
																.equals("TvId")) {
															channelItem
																	.setChannelId(reader
																			.readObject()
																			.toString());
														} else if (key
																.equals("backupenable")) {
															channelItem
																	.setBackEnable(reader
																			.readObject()
																			.toString());
														} else if (key
																.equals("channelNo")) {
															channelItem
																	.setChannelNo(reader
																			.readObject()
																			.toString());
														} else if (key
																.equals("isHDTV")) {
															channelItem
																	.setHasHD(reader
																			.readObject()
																			.toString());
														} else if (key
																.equals("isLive")) {
															channelItem
																	.setIsLive(reader
																			.readObject()
																			.toString());
														} else if (key
																.equals("IsBackView")) {
															channelItem
																	.setIsBackView(reader
																			.readObject()
																			.toString());
														} else if (key
																.equals("ContentSet")) {
															dateList = new ArrayList<DateItem>();
															dateItem = new DateItem();
															reader.startObject();
															while (reader
																	.hasNext()) {
																key = reader
																		.readString();
																if (key.equals("PlayDate")) {
																	dateItem.setDate(reader
																			.readObject()
																			.toString());
																} else if (key
																		.equals("contents")) {
																	programList = new ArrayList<ProgramItem>();
																	reader.startArray();
																	while (reader
																			.hasNext()) {
																		reader.startObject();
																		while (reader
																				.hasNext()) {
																			key = reader
																					.readString();
																			if (key.equals("Content")) {
																				programItem = new ProgramItem();
																				reader.startObject();
																				while (reader
																						.hasNext()) {
																					key = reader
																							.readString();
																					if (key.equals("PlayerTime")) {
																						programItem
																								.setStartTime(reader
																										.readObject()
																										.toString());
																					} else if (key
																							.equals("PlayerTimeLong")) {
																						String s = reader
																								.readObject()
																								.toString();
																						long l = 0;
																						try {
																							l = Long.parseLong(s);
																						} catch (Exception e) {
																							LogUtil.e(e
																									.toString());
																						}
																						programItem
																								.setlStartTime(l);
																					} else if (key
																							.equals("ProgramName")) {
																						programItem
																								.setProgramName(reader
																										.readObject()
																										.toString());
																					} else if (key
																							.equals("StopTime")) {
																						programItem
																								.setStopTime(reader
																										.readObject()
																										.toString());
																					} else if (key
																							.equals("StopTimeLong")) {
																						String s = reader
																								.readObject()
																								.toString();
																						long l = 0;
																						try {
																							l = Long.parseLong(s);
																						} catch (Exception e) {
																							LogUtil.e(e
																									.toString());
																						}
																						programItem
																								.setlStopTime(l);
																					} else {
																						reader.readObject();
																					}
																				}
																				reader.endObject();
																				programList
																						.add(programItem);
																			} else {
																				reader.readObject();
																			}
																		}
																		reader.endObject();
																	}
																	reader.endArray();
																	dateItem.setProgramItemList(programList);
																} else {
																	reader.readObject();
																}
															}
															reader.endObject();
															dateList.add(dateItem);
															channelItem
																	.setDateList(dateList);
														} else if (key
																.equals("MediaSources")) {
															reader.startObject();
															while (reader
																	.hasNext()) {
																key = reader
																		.readString();
																if (key.equals("sources")) {
																	resourceList = new ArrayList<ResourceItem>();
																	reader.startArray();
																	while (reader
																			.hasNext()) {
																		reader.startObject();
																		while (reader
																				.hasNext()) {
																			key = reader
																					.readString();
																			if (key.equals("Source")) {
																				resourceItem = new ResourceItem();
																				reader.startObject();
																				while (reader
																						.hasNext()) {
																					key = reader
																							.readString();
																					if (key.equals("Name")) {
																						resourceItem
																								.setResourceName(reader
																										.readObject()
																										.toString());
																					} else if (key
																							.equals("ResourceId")) {
																						resourceItem
																								.setResourceId(reader
																										.readObject()
																										.toString());
																					} else if (key
																							.equals("bke_url")) {
																						resourceItem
																								.setBkeUrl(reader
																										.readObject()
																										.toString());
																					} else if (key
																							.equals("datatype")) {
																						resourceItem
																								.setDataType(reader
																										.readObject()
																										.toString());
																					} else if (key
																							.equals("isThirdParty")) {
																						resourceItem
																								.setIs3rd(reader
																										.readObject()
																										.toString());
																					} else if (key
																							.equals("proto")) {
																						resourceItem
																								.setProto(reader
																										.readObject()
																										.toString());
																					} else if (key
																							.equals("resourcetype")) {
																						// TODO
																						reader.readObject()
																								.toString();
																					} else if (key
																							.equals("scale")) {
																						resourceItem
																								.setScale(reader
																										.readObject()
																										.toString());
																					} else if (key
																							.equals("tracker")) {
																						resourceItem
																								.setTracker(reader
																										.readObject()
																										.toString());
																					}
																				}
																				reader.endObject();
																				resourceList
																						.add(resourceItem);
																			} else {
																				reader.readObject()
																						.toString();
																			}
																		}
																		reader.endObject();
																	}
																	reader.endArray();
																	channelItem
																			.setResourceList(resourceList);
																} else {
																	reader.readObject()
																			.toString();
																}
															}
															reader.endObject();
														} else {
															reader.readObject();
														}
													}
													reader.endObject();
													channelList
															.add(channelItem);
												} else {
													reader.readObject();
												}
											}
											reader.endObject();
										}
										reader.endArray();
										assortItem.setChannelList(channelList);
									} else {
										reader.readObject();
									}

								}
								reader.endObject();
								assortList.add(assortItem);
							} else {
								reader.readObject();
							}
						}
						reader.endObject();

					}
					reader.endArray();
					info.setAssortList(assortList);
				} else {
					reader.readObject();
				}
			}
			reader.endObject();
			reader.close();
			inputStream.close();
			inputStream = null;
			LogUtil.i("TodayProgramInfoJsonParser--->end parser");
		}
	}

}
