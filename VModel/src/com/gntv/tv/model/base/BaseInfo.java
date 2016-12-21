package com.gntv.tv.model.base;

import java.io.Serializable;

public class BaseInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 269764883429000405L;
	private String status;
	private String newStatus;
	private String resultDesc;
	private String newResultDesc;
	private String requestTime;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public String getNewResultDesc() {
		return newResultDesc;
	}

	public void setNewResultDesc(String newResultDesc) {
		this.newResultDesc = newResultDesc;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

}