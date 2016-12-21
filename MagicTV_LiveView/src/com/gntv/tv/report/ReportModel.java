package com.gntv.tv.report;

public class ReportModel {
	private String type;
	private String name;
	private Epg epg;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Epg getEpg() {
		return epg;
	}

	public void setEpg(Epg epg) {
		this.epg = epg;
	}

	public static class Epg{
		private String epgid;
		private String id;
		private String lastid;
		private String pagetype;
		private String moduletype;
		private String focustype;
		private String focusid;
		private String pagenum;
		private String action;
		private String input;
		private String input2;
		private String midlist;
		private String postion;
		private String did;
		
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
		public String getPostion() {
			return postion;
		}
		public void setPostion(String postion) {
			this.postion = postion;
		}
		public String getDid() {
			return did;
		}
		public void setDid(String did) {
			this.did = did;
		}
		
	}
}
