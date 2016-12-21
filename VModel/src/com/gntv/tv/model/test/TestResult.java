package com.gntv.tv.model.test;

import com.gntv.tv.common.ap.User;
/**
 * 返回故障检测的bean
 * @author voole
 *
 */
public class TestResult {
	private String status = "";
	private String otherStatus = "";
	private String otherDesc = "";
	private double speed = 0;
	private User user = null;
	private int times = 0;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOtherStatus() {
		return otherStatus;
	}
	public void setOtherStatus(String otherStatus) {
		this.otherStatus = otherStatus;
	}
	public String getOtherDesc() {
		return otherDesc;
	}
	public void setOtherDesc(String otherDesc) {
		this.otherDesc = otherDesc;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
}
