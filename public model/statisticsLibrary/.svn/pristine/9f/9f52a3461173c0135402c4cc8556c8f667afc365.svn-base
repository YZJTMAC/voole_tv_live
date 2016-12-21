package com.voole.statistics.report;

import android.os.Parcel;
import android.os.Parcelable;

public class ReportBaseInfo implements Parcelable {
	//终端类型	必须赋值
	private String oemid = null;
	//应用唯一id	有oemid和packagename时，appid可以为-1
	private String appId = "-1";
	//应用的版本	必须赋值
	private String appversion = null;
	//终端唯一id	必须赋值
	private String hid = null;
	//用户唯一id	必须赋值
	private String uid = null;
	
	public ReportBaseInfo(){
		
	}

	public ReportBaseInfo(String oemid, String appId, String appversion,
			String hid, String uid) {
		super();
		this.oemid = oemid;
		this.appId = appId;
		this.appversion = appversion;
		this.hid = hid;
		this.uid = uid;
	}
	public String getOemid() {
		return oemid;
	}
	public void setOemid(String oemid) {
		this.oemid = oemid;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppversion() {
		return appversion;
	}
	public void setAppversion(String appversion) {
		this.appversion = appversion;
	}
	public String getHid() {
		return hid;
	}
	public void setHid(String hid) {
		this.hid = hid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(oemid);
		dest.writeString(appId);
		dest.writeString(appversion);
		dest.writeString(hid);
		dest.writeString(uid);
	}
	
	public static final Parcelable.Creator<ReportBaseInfo> CREATOR = new Creator<ReportBaseInfo>() {

		@Override
		public ReportBaseInfo createFromParcel(Parcel source) {
			ReportBaseInfo info = new ReportBaseInfo();
			info.oemid = source.readString();
			info.appId = source.readString();
			info.appversion = source.readString();
			info.hid = source.readString();
			info.uid = source.readString();
			return info;
		}

		@Override
		public ReportBaseInfo[] newArray(int size) {
			return new ReportBaseInfo[size];
		}
	};

	@Override
	public String toString() {
		return "ReportBaseInfo [oemid=" + oemid + ", appId=" + appId
				+ ", appversion=" + appversion + ", hid=" + hid + ", uid="
				+ uid + "]";
	}
}
