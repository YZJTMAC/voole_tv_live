package com.voole.statistics.report;

import android.os.Parcel;
import android.os.Parcelable;

public class ReportInfo implements Parcelable{
	private String url;
	private String data;
	private String msg;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	@Override
	public String toString() {
		return "ReportInfo [url=" + url + ", data=" + data + ", msg=" + msg
				+ "]";
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(url);
		dest.writeString(data);
		dest.writeString(msg);
	}
	
	public static final Parcelable.Creator<ReportInfo> CREATOR = new Creator<ReportInfo>() {

		@Override
		public ReportInfo createFromParcel(Parcel source) {
			ReportInfo info = new ReportInfo();
			info.url = source.readString();
			info.data = source.readString();
			info.msg = source.readString();
			return info;
		}

		@Override
		public ReportInfo[] newArray(int size) {
			return new ReportInfo[size];
		}
	};
	
}
