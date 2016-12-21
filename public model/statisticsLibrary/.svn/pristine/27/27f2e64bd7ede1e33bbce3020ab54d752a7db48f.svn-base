package com.voole.statistics.terminalinfo;

import com.voole.statistics.report.ReportBaseInfo;

import android.os.Parcel;
import android.os.Parcelable;

public class AppReportInfo implements Parcelable {

	//	private String oemid = null;
	//	private String appId = null;
	//	private String appversion = null;
	//	private String hid = null;
	//	private String uid = null;
	//	private String urlBase = null;
	//认证版本号		有则赋值
	private String vooleAuth = null;
	//认证编译时间		有则赋值
	private String authCompile = null;
	//代理版本号		有则赋值
	private String vooleAgent = null;
	//代理编译时间		有则赋值
	private String agentCompile = null;
	//agentlibs	代理库版本号	xml字串	有则赋值
	private String agentLibs = null;
	//升级模块版本号	有则赋值
	private String upgradeVersion = null;
	//	private String statisticsVersion = null;
	//终端日志模块版本号	有则赋值
	private String terminaLogVersion = null;
	//apk启动方式		有则赋值
	private String apkStartType = null;
	//是否通过牌照商认证(字符串)	默认：-1 -1 无认证0 否1 是
	private String isAuth = "-1";
	//第三方设备id		有则赋值
	private String deviceid = null;
	//牌照商终端串号（字符串,可用于第三方身份识别ID传递）		有则赋值
	private String sn = null;
	//Sdk模块版本号	有则赋值
	private String sdkModuleVersion = null;
	//Sdk模块类型		有则赋值
	private String sdkModuleType = null;
	//终端的基本信息
	private ReportBaseInfo info = null;

	private String packageName = null;

	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public ReportBaseInfo getInfo() {
		return info;
	}
	public void setInfo(ReportBaseInfo info) {
		this.info = info;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		//		dest.writeString(oemid);
		//		dest.writeString(appId);
		//		dest.writeString(appversion);
		//		dest.writeString(hid);
		//		dest.writeString(uid);
		//		dest.writeString(urlBase);
		dest.writeString(vooleAuth);
		dest.writeString(authCompile);
		dest.writeString(vooleAgent);
		dest.writeString(agentCompile);
		dest.writeString(agentLibs);
		dest.writeString(upgradeVersion);
		dest.writeString(terminaLogVersion);
		dest.writeString(apkStartType);
		dest.writeString(isAuth);
		dest.writeString(deviceid);
		dest.writeString(sn);
		dest.writeString(sdkModuleVersion);
		dest.writeString(sdkModuleType);
		dest.writeString(packageName);
		dest.writeParcelable(info, PARCELABLE_WRITE_RETURN_VALUE);
	}

	public static final Parcelable.Creator<AppReportInfo> CREATOR = new Creator<AppReportInfo>() {

		@Override
		public AppReportInfo createFromParcel(Parcel source) {
			AppReportInfo info = new AppReportInfo();
			//			info.oemid = source.readString();
			//			info.appId = source.readString();
			//			info.appversion = source.readString();
			//			info.hid = source.readString();
			//			info.uid = source.readString();
			//			info.urlBase = source.readString();
			info.vooleAuth = source.readString();
			info.authCompile = source.readString();
			info.vooleAgent = source.readString();
			info.agentCompile = source.readString();
			info.agentLibs = source.readString();
			info.upgradeVersion = source.readString();
			info.terminaLogVersion = source.readString();
			info.apkStartType = source.readString();
			info.isAuth = source.readString();
			info.deviceid = source.readString();
			info.sn = source.readString();
			info.sdkModuleVersion = source.readString();
			info.sdkModuleType = source.readString();
			info.packageName = source.readString();
			info.info = source.readParcelable(ReportBaseInfo.class.getClassLoader());
			return info;
		}

		@Override
		public AppReportInfo[] newArray(int size) {
			return new AppReportInfo[size];
		}
	};

	//	public String getOemid() {
	//		return oemid;
	//	}
	//	public void setOemid(String oemid) {
	//		this.oemid = oemid;
	//	}
	//	public String getAppId() {
	//		return appId;
	//	}
	//	public void setAppId(String appId) {
	//		this.appId = appId;
	//	}
	//	public String getAppversion() {
	//		return appversion;
	//	}
	//	public void setAppversion(String appversion) {
	//		this.appversion = appversion;
	//	}
	//	public String getHid() {
	//		return hid;
	//	}
	//	public void setHid(String hid) {
	//		this.hid = hid;
	//	}
	//	public String getUid() {
	//		return uid;
	//	}
	//	public void setUid(String uid) {
	//		this.uid = uid;
	//	}

	//	public String getUrlBase() {
	//		return urlBase;
	//	}
	//	public void setUrlBase(String urlBase) {
	//		this.urlBase = urlBase;
	//	}
	public String getSdkModuleVersion() {
		return sdkModuleVersion;
	}
	public void setSdkModuleVersion(String sdkModuleVersion) {
		this.sdkModuleVersion = sdkModuleVersion;
	}
	public String getSdkModuleType() {
		return sdkModuleType;
	}
	public void setSdkModuleType(String sdkModuleType) {
		this.sdkModuleType = sdkModuleType;
	}

	public String getVooleAuth() {
		return vooleAuth;
	}
	public void setVooleAuth(String vooleAuth) {
		this.vooleAuth = vooleAuth;
	}
	public String getAuthCompile() {
		return authCompile;
	}
	public void setAuthCompile(String authCompile) {
		this.authCompile = authCompile;
	}
	public String getVooleAgent() {
		return vooleAgent;
	}
	public void setVooleAgent(String vooleAgent) {
		this.vooleAgent = vooleAgent;
	}
	public String getAgentCompile() {
		return agentCompile;
	}
	public void setAgentCompile(String agentCompile) {
		this.agentCompile = agentCompile;
	}
	public String getAgentLibs() {
		return agentLibs;
	}
	public void setAgentLibs(String agentLibs) {
		this.agentLibs = agentLibs;
	}
	public String getUpgradeVersion() {
		return upgradeVersion;
	}
	public void setUpgradeVersion(String upgradeVersion) {
		this.upgradeVersion = upgradeVersion;
	}
	//	public String getStatisticsVersion() {
	//		return statisticsVersion;
	//	}
	//	public void setStatisticsVersion(String statisticsVersion) {
	//		this.statisticsVersion = statisticsVersion;
	//	}
	public String getTerminaLogVersion() {
		return terminaLogVersion;
	}
	public void setTerminaLogVersion(String terminaLogVersion) {
		this.terminaLogVersion = terminaLogVersion;
	}
	public String getApkStartType() {
		return apkStartType;
	}
	public void setApkStartType(String apkStartType) {
		this.apkStartType = apkStartType;
	}
	public String getIsAuth() {
		return isAuth;
	}
	public void setIsAuth(String isAuth) {
		this.isAuth = isAuth;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

}
