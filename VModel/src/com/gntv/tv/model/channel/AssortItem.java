package com.gntv.tv.model.channel;

import java.io.Serializable;
import java.util.List;

public class AssortItem implements Serializable{
	/**
	 * 一个载体，用来封装返回的节目单信息
	 * 
	 */
	private static final long serialVersionUID = 487155651446015076L;
	private String assortValue;
	private String assortName;
	private List<ChannelItem> channelList;
	private boolean isPlaying = false;
	public String getAssortValue() {
		return assortValue;
	}
	public void setAssortValue(String assortValue) {
		this.assortValue = assortValue;
	}
	public String getAssortName() {
		return assortName;
	}
	public void setAssortName(String assortName) {
		this.assortName = assortName;
	}
	public List<ChannelItem> getChannelList() {
		return channelList;
	}
	public void setChannelList(List<ChannelItem> channelList) {
		this.channelList = channelList;
	}
	public boolean isPlaying() {
		return isPlaying;
	}
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	
}
