package com.gntv.tv.model.channel;

import java.io.Serializable;
import java.util.List;

import com.gntv.tv.model.base.BaseInfo;


public class TodayProgramInfo extends BaseInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2345465407870823969L;
	private List<AssortItem> assortList;

	public List<AssortItem> getAssortList() {
		return assortList;
	}

	public void setAssortList(List<AssortItem> assortList) {
		this.assortList = assortList;
	}

	
}
