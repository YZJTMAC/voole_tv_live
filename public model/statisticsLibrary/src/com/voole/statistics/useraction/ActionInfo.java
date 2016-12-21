package com.voole.statistics.useraction;

public class ActionInfo {
	
	private String type = "PageBrowsing";
	private String name = "epg";
	private Epg epg;
	
	public String getType() {
		return type;
	}

//	public void setType(String type) {
//		this.type = type;
//	}

	public String getName() {
		return name;
	}

//	public void setName(String name) {
//		this.name = name;
//	}

	public Epg getEpg() {
		return epg;
	}

	public void setEpg(Epg epg) {
		this.epg = epg;
	}

	public static class Epg { 
		
		public Epg(){
			super();
		}
		
		public Epg(String pagetype){
			this.pagetype = pagetype;
		}
		
		public Epg(String id,String pagetype){
			this.id = id;
			this.pagetype = pagetype;
		}
		
		//epg片单ID，如果没有填写-1	必须赋值
		private String epgid = "-1";		
		//本次消息汇报的唯一ID		必须赋值
		private String id;
		//上次消息汇报的唯一ID		可以为空
		private String lastid;
		//页面类型编码		可以为空
		private String pagetype;
		//页面模块编码		可以为空
		private String moduletype;
		//焦点类型，目前分为内容、功能两类。	content/function	可以为空
		private String focustype;
		//焦点所代表的的信息内容ID，如节目ID、栏目ID		可以为空
		private String focusid;
		//当前页码		可以为空
		private String pagenum;
		//动作类型编码.		可以为空
		private String action;
		//搜索时，用户输入数据		可以为空
		private String input;
		//组合条件搜索时，用户输入如：Type1@k1=v1&k2=v2# Type2@k1=v1&k2=v2		可以为空
		private String input2;
		//算法id和mid具体需求查看文档
		private String midlist;
		//position	海报安放位置	（字符串）”1”或者 “R1”	可以为空
		private String position;
		//桌面模块		可以为空
		private String did;
		//节目列表曝光时间(页面停留时间)	毫秒数	可以为空(不用设置)
		private String exposuretime;
		
		public String getEpgid() {
			return epgid;
		}
		public void setEpgid(String epgid) {
			this.epgid = epgid;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getLastid() {
			return lastid;
		}
		public void setLastid(String lastid) {
			this.lastid = lastid;
		}
		public String getPagetype() {
			return pagetype;
		}
		public void setPagetype(String pagetype) {
			this.pagetype = pagetype;
		}
		public String getModuletype() {
			return moduletype;
		}
		public void setModuletype(String moduletype) {
			this.moduletype = moduletype;
		}
		public String getFocustype() {
			return focustype;
		}
		public void setFocustype(String focustype) {
			this.focustype = focustype;
		}
		public String getFocusid() {
			return focusid;
		}
		public void setFocusid(String focusid) {
			this.focusid = focusid;
		}
		public String getPagenum() {
			return pagenum;
		}
		public void setPagenum(String pagenum) {
			this.pagenum = pagenum;
		}
		public String getAction() {
			return action;
		}
		public void setAction(String action) {
			this.action = action;
		}
		public String getInput() {
			return input;
		}
		public void setInput(String input) {
			this.input = input;
		}
		public String getInput2() {
			return input2;
		}
		public void setInput2(String input2) {
			this.input2 = input2;
		}
		public String getMidlist() {
			return midlist;
		}
		public void setMidlist(String midlist) {
			this.midlist = midlist;
		}
		public String getPosition() {
			return position;
		}
		public void setPosition(String position) {
			this.position = position;
		}
		public String getDid() {
			return did;
		}
		public void setDid(String did) {
			this.did = did;
		}
		public String getExposuretime() {
			return exposuretime;
		}
		public void setExposuretime(String exposuretime) {
			this.exposuretime = exposuretime;
		}
	}
}
