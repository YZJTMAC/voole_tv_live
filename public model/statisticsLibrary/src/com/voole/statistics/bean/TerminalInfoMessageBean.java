package com.voole.statistics.bean;

/**
 * 终端信息bean
 * @author Jacky
 *
 */
public class TerminalInfoMessageBean { 
	 
	public int getMessageType() {
		return messageType;
	}


	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}


	/**
	 * 	终端唯一id
	 */
	private String hid;
	 
	/**
	 * 终端类型
	 */
	private String oemid;
	/**
	 * 	用户唯一id
	 */
	private String uid;
	
	/**
	 * 	认证版本号
	 */
	private String vooleauth;
	
	/**
	 * 	认证编译时间
	 */
	private String authcompile;
	
	
	/**
	 * 	代理版本号	
	 */
	private String vooleagent;
	
	
	/**
	 * 	代理编译时间
	 */
	private String agentcompile;
	/**
	 * 	代理库版本号
	 */
	private String agentlibs;
		 
	/**
	 * 	APP的properties配置文件		 
	 */
	private String properties;  
		
	
	/**
	 * 	voolert.conf配置文件	 
	 */
	private String voolert;


	/**
	 * 消息类型
	 */
	private int messageType;
	
	/**
	 * 版本信息
	 */
	public static final int EDITION_TYPE=1;
	
	/**
	 * 配置文件
	 */
	public static final int CONFIGURE_TYPE=2;
	
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


	public String getVooleauth() {
		return vooleauth;
	}


	public void setVooleauth(String vooleauth) {
		this.vooleauth = vooleauth;
	}


	public String getAuthcompile() {
		return authcompile;
	}


	public void setAuthcompile(String authcompile) {
		this.authcompile = authcompile;
	}


	public String getVooleagent() {
		return vooleagent;
	}


	public void setVooleagent(String vooleagent) {
		this.vooleagent = vooleagent;
	}


	public String getAgentcompile() {
		return agentcompile;
	}


	public void setAgentcompile(String agentcompile) {
		this.agentcompile = agentcompile;
	}


	public String getAgentlibs() {
		return agentlibs;
	}


	public void setAgentlibs(String agentlibs) {
		this.agentlibs = agentlibs;
	}


	public String getProperties() {
		return properties;
	}


	public void setProperties(String properties) {
		this.properties = properties;
	}


	public String getVoolert() {
		return voolert;
	}


	public void setVoolert(String voolert) {
		this.voolert = voolert;
	} 
				 
}
