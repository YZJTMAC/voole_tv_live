package com.gntv.tv.model.channel;

import java.io.Serializable;

public class ResourceItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3741340042700864800L;
	private String resourceId;
	private String resourceName;
//	private String resourceType;
	private String tracker;
	private String bkeUrl;
	private String is3rd;
	private String proto;
	private String dataType;
	private String scale = "16:9"; //缩放比例
	
	public String getProto() {
		return proto;
	}
	public void setProto(String proto) {
		this.proto = proto;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getIs3rd() {
		return is3rd;
	}
	public void setIs3rd(String is3rd) {
		this.is3rd = is3rd;
	}
	public String getTracker() {
		return tracker;
	}
	public void setTracker(String tracker) {
		this.tracker = tracker;
	}
	public String getBkeUrl() {
		return bkeUrl;
	}
	public void setBkeUrl(String bkeUrl) {
		this.bkeUrl = bkeUrl;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	/*public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}*/
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	
	
}
