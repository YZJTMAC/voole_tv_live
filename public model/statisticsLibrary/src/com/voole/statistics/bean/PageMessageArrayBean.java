package com.voole.statistics.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 页面消息结合bean
 * 
 * @author Jacky
 * 
 */
public class PageMessageArrayBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5553625615522442742L;

	/**
	 * 消息总数
	 */
	private int count;

	/**
	 * 消息头bean
	 */
	private HeaderBean headerBean;

	/**
	 * 消息集合bean
	 */
	private List<PageMessageBean> pageMessageBeanList;

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

	public List<PageMessageBean> getPageMessageBeanList() {
		return pageMessageBeanList;
	}

	public void setPageMessageBeanList(List<PageMessageBean> pageMessageBeanList) {
		this.pageMessageBeanList = pageMessageBeanList;
	}

}
