package com.voole.android.client.messagelibrary.model;

import java.io.Serializable;


public class Message implements Serializable{
	private static final long serialVersionUID = -898818190318900734L;
	
	/**
	 * updateDownload	汇报数据的类型，在升级数据汇报中，取值updateDownload
	 */
	private String type;
	
	/**
	 * 升级状态文本描述 或 属性和子节点保存汇报数据特有的内容
	 */
	private String bodyContent;
	
	private String value;
	
	private long createdTime;
	
	private int downLoadSize;
	//下载时长
	private long downLoadTime;
	// 下载地址
	private String downLoadUrl;
	
	private String downLoadIp;
	
	private int DLcount;
	
	
	
	public Message() {
		
	}
	
	public long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBodyContent() {
		return bodyContent;
	}
	public void setBodyContent(String bodyContent) {
		this.bodyContent = bodyContent;
	}

	public int getDownLoadSize() {
		return downLoadSize;
	}

	public void setDownLoadSize(int downLoadSize) {
		this.downLoadSize = downLoadSize;
	}

	public long getDownLoadTime() {
		return downLoadTime;
	}

	public void setDownLoadTime(long downLoadTime) {
		this.downLoadTime = downLoadTime;
	}

	public String getDownLoadUrl() {
		return downLoadUrl;
	}

	public void setDownLoadUrl(String downLoadUrl) {
		this.downLoadUrl = downLoadUrl;
	}

	public String getDownLoadIp() {
		return downLoadIp;
	}

	public void setDownLoadIp(String downLoadIp) {
		this.downLoadIp = downLoadIp;
	}

	public int getDLcount() {
		return DLcount;
	}

	public void setDLcount(int dLcount) {
		DLcount = dLcount;
	}
	
	
	

}
