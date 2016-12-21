package com.voole.android.client.UpAndAu.model;

import java.io.Serializable;

/**
 * 
 * @author LEE
 * 异常信息类
 *
 */
public class ExceptionFeedBackInfo implements Serializable{
	private static final long serialVersionUID = -6178944088449612934L;
	
	
	private String version;
	private String appName;
	/** 错误code 
	 * erroCode	含义
		100	常规Log
		101	内存泄露错误
		102	访问空指针造成的异常
		103	参数无效造成的异常
	 * */
	private String errCode;
	/** 严重级别(可能包括调测日志) 
	 * Priority	含义
		0	严重问题，必须立刻升级解决
		1	常规Exception，可以等到下个版本修改
		2	常规Log，无需修改

	 * */
	private String priority;
	// 错误信息（以xml方式封装，采用POST方式提交）
	private String excepInfo;
	
	public ExceptionFeedBackInfo() {
		
	}
	
	public ExceptionFeedBackInfo(String version, String appName,
			String errCode, String priority, String excepInfo) {
		super();
		this.version = version;
		this.appName = appName;
		this.errCode = errCode;
		this.priority = priority;
		this.excepInfo = excepInfo;
	}
	
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getExcepInfo() {
		return excepInfo;
	}
	public void setExcepInfo(String excepInfo) {
		this.excepInfo = excepInfo;
	}
	
	
	
	
	

}
