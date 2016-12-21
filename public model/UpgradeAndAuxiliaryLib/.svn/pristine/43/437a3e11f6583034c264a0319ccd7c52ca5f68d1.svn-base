package com.voole.android.client.messagelibrary.model;

import java.io.Serializable;

/**
 * 
 * @author wujian
 *
 */
public class Header implements Serializable{
	private static final long serialVersionUID = -8818190318903770734L;
	
	/**
	 * 标示硬件唯一id
	 */
	private String hid;
	/**
	 * 项目编号
	 */
	private String oemid;
	/**
	 * 标示用户唯一id
	 */
	private String uid;
	/**
	 * 应用唯一标示
	 */
	private String appId;
	/**
	 * 应用名称
	 */
	private String appName;
	/**
	 * 当前应用的版本
	 */
	private String appVersion;
	
	/**
	 * 包名
	 */
	private String packageName;
	
	public Header() {
		
	}
	
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getHid() {
		return hid;
	}
	public void setHid(String hid) {
		this.hid = hid;
	}
	public String getOemid() {
		return oemid;
	}
	public void setOemid(String oemid) {
		this.oemid = oemid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	
	public Header(String type, String hid, String oemid, String uid,
			String appId, String appName, String appVersion,
			String newAppVersion, String resultCode, String bodyContent) {
		super();
		this.hid = hid;
		this.oemid = oemid;
		this.uid = uid;
		this.appId = appId;
		this.appName = appName;
		this.appVersion = appVersion;
	}
	
	public Header(String type, String hid, String oemid, String uid,
			String appId, String appName, String appVersion,
			String newAppVersion, String resultCode, String bodyContent,String packageName,String value) {
		super();
		this.hid = hid;
		this.oemid = oemid;
		this.uid = uid;
		this.appId = appId;
		this.appName = appName;
		this.appVersion = appVersion;
		this.packageName = packageName;
	}
	
}
