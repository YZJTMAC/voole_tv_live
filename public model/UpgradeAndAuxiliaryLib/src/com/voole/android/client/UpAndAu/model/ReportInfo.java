package com.voole.android.client.UpAndAu.model;

import java.io.Serializable;

/**
 * 统计数据汇报类型 
 * @version V1.0
 * @author wusong
 * @time 2013-4-17上午10:33:41
 */
public class ReportInfo implements Serializable{
	private static final long serialVersionUID = -8818190318903770734L;
	
	/**
	 * updateDownload	汇报数据的类型，在升级数据汇报中，取值updateDownload
	 */
	private String type;
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
	 * parseUpdateInfo
	 */
	private String newAppVersion;
	/**
	 * 升级状态代码
	 */
	private String resultCode;
	/**
	 * 升级状态文本描述 或 属性和子节点保存汇报数据特有的内容
	 */
	private String bodyContent;
	
	/**
	 * 包名
	 */
	private String packageName;
	
	private String value;
	
	
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getNewAppVersion() {
		return newAppVersion;
	}
	public void setNewAppVersion(String newAppVersion) {
		this.newAppVersion = newAppVersion;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getBodyContent() {
		return bodyContent;
	}
	public void setBodyContent(String bodyContent) {
		this.bodyContent = bodyContent;
	}
	public ReportInfo() {
		
	}
	
	public ReportInfo(String type, String hid, String oemid, String uid,
			String appId, String appName, String appVersion,
			String newAppVersion, String resultCode, String bodyContent) {
		super();
		this.type = type;
		this.hid = hid;
		this.oemid = oemid;
		this.uid = uid;
		this.appId = appId;
		this.appName = appName;
		this.appVersion = appVersion;
		this.newAppVersion = newAppVersion;
		this.resultCode = resultCode;
		this.bodyContent = bodyContent;
	}
	
	public ReportInfo(String type, String hid, String oemid, String uid,
			String appId, String appName, String appVersion,
			String newAppVersion, String resultCode, String bodyContent,String packageName,String value) {
		super();
		this.type = type;
		this.hid = hid;
		this.oemid = oemid;
		this.uid = uid;
		this.appId = appId;
		this.appName = appName;
		this.appVersion = appVersion;
		this.newAppVersion = newAppVersion;
		this.resultCode = resultCode;
		this.bodyContent = bodyContent;
		this.packageName = packageName;
		this.value = value;
	}
	
	
	
	
	
	
	
	
	

}
