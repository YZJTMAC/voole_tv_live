package com.voole.statistics.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 播放消息结合bean
 * 
 * @author Jacky
 * 
 */
public class PlayerMessageArrayBean implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5970787373742947213L;

	/**
	 * 消息总数
	 */
	private int count;

	/**
	 * 消息头bean
	 */
	private HeaderBean headerBean;

	/**
	 * 播放消息集合bean
	 */
	private List<PlayerMessageBean> playerMessageBeanList;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public HeaderBean getHeaderBean() {
		return headerBean;
	}

	public void setHeaderBean(HeaderBean headerBean) {
		this.headerBean = headerBean;
	}

	public List<PlayerMessageBean> getPlayerMessageBeanList() {
		return playerMessageBeanList;
	}

	public void setPlayerMessageBeanList(
			List<PlayerMessageBean> playerMessageBeanList) {
		this.playerMessageBeanList = playerMessageBeanList;
	}



}
