package com.gntv.tv.model.channel;

import java.io.Serializable;
import java.util.List;

import android.text.TextUtils;
/**
 * 节目单的item,相当于bean
 * @author voole
 *
 */
public class ChannelItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -626460634818172410L;
	private String channelId;
	private String title;
	private String sequence;
	private String backEnable;
	private List<DateItem> dateList;
	private List<ResourceItem> resourceList;
	private boolean isPlaying = false;
	private String hasHD = "0"; //是否有高清频道 1有高清 0 无
	private String channelNo; //选台编号字段
	private String isLive;
	private String isBackView; //是否支持时移 1.支持  0.不支持
	
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public List<DateItem> getDateList() {
		return dateList;
	}
	public void setDateList(List<DateItem> dateList) {
		this.dateList = dateList;
	}
	
	
	public String getBackEnable() {
		return backEnable;
	}
	public void setBackEnable(String backEnable) {
		this.backEnable = backEnable;
	}
	public List<ResourceItem> getResourceList() {
		return resourceList;
	}
	public void setResourceList(List<ResourceItem> resourceList) {
		this.resourceList = resourceList;
	}
	public boolean isPlaying() {
		return isPlaying;
	}
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	public String getHasHD() {
		return hasHD;
	}
	public void setHasHD(String hasHD) {
		this.hasHD = hasHD;
	}
	
	public String getChannelNo() {
		//return channelNo;
		return channelNo!=null?channelNo:sequence;
	}
	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}
	
	public ResourceItem getCurrentResourceItem(){
		if(resourceList == null || resourceList.size() < 1){
			return null;
		}
		//String resourceId = ResourceManager.GetInstance().getResource(channelId);
		String state = ResourceManager.GetInstance().getResourceState();
		/*if(!TextUtils.isEmpty(resourceId)){
			for(ResourceItem r : resourceList){
				if(resourceId.equals(r.getResourceId())){
					return r;
				}
			}
		}*/
		int size = resourceList.size();
		if(size>1){
			ResourceItem item = null;
			if("0".equals(state)){
				item = resourceList.get(0);
			}else{
				item = resourceList.get(size-1);
			}
			return item;
		}
		
		return resourceList.get(0);
	}
	public String getIsLive() {
		return isLive;
	}
	public void setIsLive(String isLive) {
		this.isLive = isLive;
	}
	public String getIsBackView() {
		//return isBackView;
		return TextUtils.isEmpty(isBackView)?backEnable:isBackView;
	}
	public void setIsBackView(String isBackView) {
		this.isBackView = isBackView;
	}
	
	
	
}
