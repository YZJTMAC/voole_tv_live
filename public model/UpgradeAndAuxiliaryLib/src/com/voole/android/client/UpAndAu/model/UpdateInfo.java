package com.voole.android.client.UpAndAu.model;

import java.io.Serializable;
/**
 * 版本升级统计信息
 * @version V1.0
 * @author LEE
 * @author wusong
 * @time 2013-4-17上午10:34:55
 */
public class UpdateInfo implements Serializable{
	private static final long serialVersionUID = -5326659988405894667L;
	/**
	 * 是否有新的版本
	 * YES/NO	
	 */
	private String updateAvailabe;
	/**
	 * 当前版本
	 */
	private String currentVersion;
	/**
	 * 升级选项
	 *	Required:强制升级
	 *	Optional:可选升级	
	 */
	private String updateStyle;
	/**
	 * 升级包的文件fid
	 */
	private String fid;
	/**
	 * 升级包上载时间
	 */
	private String updateTime;
	/**
	 * 升级包的大小
	 */
	private String appSize;
	/**
	 * 升级包需要的最低SDK版本
	 */
	private String requiredSdk;
	/**
	 * 下载升级包的url
	 */
	private String downloadUrl;
	/**
	 * 介绍升级信息的html，由后台编辑人员维护
	 */
	private String detailUrl;
	/**
	 * 文字描述或链接地址	 关于升级版本的介绍
	 */
	private String introduction;
	/**
	 * 返回结果描述
	 */
	private DataResult dataResult;
	/***
	 * server current time
	 * 服务端返回数据的时间，作为唯一标示本次版本检测，下载成功失败 会把次参数再次提交服务器.
	 */
	private String sendtime;
	
	/**包名*/
	private String packageName;
	private String appid;
	private String appname;
	
	public DataResult getDataResult() {
		return dataResult;
	}
	public void setDataResult(DataResult dataResult) {
		this.dataResult = dataResult;
	}
	
	public String getUpdateAvailabe() {
		return updateAvailabe;
	}
	public void setUpdateAvailabe(String updateAvailabe) {
		this.updateAvailabe = updateAvailabe;
	}
	public String getCurrentVersion() {
		return currentVersion;
	}
	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}
	public String getUpdateStyle() {
		return updateStyle;
	}
	public void setUpdateStyle(String updateStyle) {
		this.updateStyle = updateStyle;
	}
	public String getFid() {
		return fid;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getAppSize() {
		return appSize;
	}
	public void setAppSize(String appSize) {
		this.appSize = appSize;
	}
	public String getRequiredSdk() {
		return requiredSdk;
	}
	public void setRequiredSdk(String requiredSdk) {
		this.requiredSdk = requiredSdk;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getDetailUrl() {
		return detailUrl;
	}
	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	public boolean upgradeRequired() {
		if("Required".equals(updateStyle)) {
			return true;
		} else {
			return false;
		}
	}
	public String getSendtime() {
		return sendtime;
	}
	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	
	
	
}
