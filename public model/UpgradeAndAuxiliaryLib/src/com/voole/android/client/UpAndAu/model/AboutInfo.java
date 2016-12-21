package com.voole.android.client.UpAndAu.model;

import java.io.Serializable;


/**
 * 
 *关于信息
 * @version 1.0
 * @author LEE
 * @date 2013-4-3 下午2:30:12 
 * @update 2013-4-3 下午2:30:12
 */
public class AboutInfo implements Serializable{
	private static final long serialVersionUID = 115516516313L;
	// Text/Html	标示返回类型
	private String type;
	private DataResult dataResult;
	// 文字描述或链接地址	   信息介绍
	private String introduction;
	
	public AboutInfo() {
		
	}
	public AboutInfo(String type, DataResult dataResult, String introduction) {
		super();
		this.type = type;
		this.dataResult = dataResult;
		this.introduction = introduction;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public DataResult getDataResult() {
		return dataResult;
	}
	public void setDataResult(DataResult dataResult) {
		this.dataResult = dataResult;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	/**
	 *  返回结果是否为html
	 * @return boolean
	 */
	public boolean isHtml() {
		if("html".equals(type)) {
			return true;
		}
		return false;
	}
	

}
