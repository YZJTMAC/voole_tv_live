package com.gntv.tv.model.channel;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONReader;
import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.model.base.BaseChannelProgramInfoParser;

public class ChannelProgramInfoJsonParser extends BaseChannelProgramInfoParser {
	private DateItem dateItem = null;
	private ProgramItem programItem = null;

	private List<DateItem> dateList;
	private List<ProgramItem> programList;

	@Override
	public void setInputStream(InputStream inputStream) throws Exception {
		if (inputStream != null) {
			LogUtil.i("ChannelProgramInfoJsonParser--->start parser");
			JSONReader reader = new JSONReader(new InputStreamReader(
					inputStream,"UTF-8"));
			info = new ChannelProgramInfo();
			reader.startObject();
			String key;
			while (reader.hasNext()) {
				key = reader.readString();
				if (key.equals("Status")) {
					info.setStatus(reader.readObject().toString());
				} else if (key.equals("Time")) {
					info.setRequestTime(reader.readObject().toString());
				} else if (key.equals("LiveTv")) {
					reader.startObject();
					while (reader.hasNext()) {
						key = reader.readString();
						if (key.equals("contentSets")) {
							dateList = new ArrayList<DateItem>();
							reader.startArray();
							while (reader.hasNext()) {
								reader.startObject();
								while (reader.hasNext()) {
									key = reader.readString();
									if (key.equals("ContentSet")) {
										dateItem = new DateItem();
										reader.startObject();
										while (reader.hasNext()) {
											key = reader.readString();
											if (key.equals("PlayDate")) {
												dateItem.setDate(reader
														.readObject()
														.toString());
											} else if (key.equals("contents")) {
												programList = new ArrayList<ProgramItem>();
												reader.startArray();
												while (reader.hasNext()) {
													reader.startObject();
													while (reader.hasNext()) {
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
									} else {
										reader.readObject();
									}
								}
								reader.endObject();
							}
							reader.endArray();
							info.setDateList(dateList);
						} else {
							reader.readObject();
						}

					}
					reader.endObject();

				} else {
					reader.readObject();
				}
			}
			reader.endObject();
			reader.close();
			inputStream.close();
			inputStream = null;
			LogUtil.i("ChannelProgramInfoJsonParser--->start parser");
		}
	}
}
