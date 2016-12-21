package com.voole.android.client.UpAndAu.model;

import java.io.Serializable;

/**
 *  当前应用的信息,检测升级所需必要信息 
 * @version V1.0
 * @author wusong
 * @time 2013-4-9上午10:11:34
 */
public class AppInfo implements Serializable{

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
	public String getDependPackageName() {
		return dependPackageName;
	}
	public void setDependPackageName(String dependPackageName) {
		this.dependPackageName = dependPackageName;
	}
	public String getDependPackegeVersion() {
		return dependPackegeVersion;
	}
	public void setDependPackegeVersion(String dependPackegeVersion) {
		this.dependPackegeVersion = dependPackegeVersion;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private static final long serialVersionUID = 4391519393501342828L;
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
	 * 包名
	 */
	private String appPackagename;
	public void setAppPackagename(String appPackagename) {
		this.appPackagename = appPackagename;
	}
	public String getAppPackagename() {
		return appPackagename;
	}
	/**
	 * 当前应用的版本
	 */
	private String appVersion;
	
	/**
	 * dependPackageName : 当前App依赖的应用，例如如果遥控入口Service升级，
	 * 							手机端的遥控器应用可能也需要被强制升级
	 */
	private String dependPackageName;
	/**
	 * dependPackegeVersion :依赖包的版本号
	 */
	private String dependPackegeVersion;
	
}
