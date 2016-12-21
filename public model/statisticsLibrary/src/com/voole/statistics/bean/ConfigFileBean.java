package com.voole.statistics.bean;

/**
 * 配置文件BEAN
 * @author jacky
 *
 */
public class ConfigFileBean {
	/**
	 * auth
	 */
	private String local_http_port;
	/**
	 * 代理
	 */
	private String local_agent_http_port;
	public String getLocal_http_port() {
		return local_http_port;
	}
	public void setLocal_http_port(String local_http_port) {
		this.local_http_port = local_http_port;
	}
	public String getLocal_agent_http_port() {
		return local_agent_http_port;
	}
	public void setLocal_agent_http_port(String local_agent_http_port) {
		this.local_agent_http_port = local_agent_http_port;
	}
	
	
}
