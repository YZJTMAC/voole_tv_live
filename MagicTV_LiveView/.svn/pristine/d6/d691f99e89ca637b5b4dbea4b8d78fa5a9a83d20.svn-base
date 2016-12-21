package com.gntv.tv.report;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.gntv.tv.common.ap.AuthManager;
import com.gntv.tv.common.ap.UrlMap;
import com.gntv.tv.common.ap.User;
import com.gntv.tv.common.utils.LogUtil;
import com.voole.statistics.report.ReportBaseInfo;
import com.voole.statistics.report.ReportManager;
import com.voole.statistics.useraction.ActionInfo.Epg;

import android.content.Context;
import android.text.TextUtils;

public class PageActionReport {
	public static enum PageType{
		StartPage,PlayPage,PlaybackPage,ExitPage;
	}
	
	public static enum ModuleType{
		ChannelList,SetFunc,ExitGuide;
	}
	
	public static enum FocusType{
		ChannelListSwitch,UpDownKeySwitch,NumKeySwitch,FaultDetection,
		ChannelPlayBack,GuideSwitch,SwitchSource,PictureRatio,BootBoot;
	}
	
	public static enum Action{
		OKKey,MenuKey,ExitKey;
	}
	
	private static PageActionReport instance = new PageActionReport();
	//private StringBuilder sb = new StringBuilder();
	//private Context context = null;
	//private String uid = null;
	
	private PageActionReport() {
		
	}

	public static PageActionReport GetInstance() {
		return instance;
	}
	
	public void init(String versionCode,String appId){
		/*UrlMap urlMap = AuthManager.GetInstance().getUrlMap();
		Map<String,String> map = urlMap!=null?urlMap.getUrlMap():null;
		String reportUrl = map!=null?map.get("playReport"):null;*/
		String reportUrl = AuthManager.GetInstance().getUrlList().getPlayReport();
		User user = AuthManager.GetInstance().getUser();
		if(!TextUtils.isEmpty(reportUrl)){
			reportUrl = reportUrl.substring(0,reportUrl.indexOf("?"));
		}
		LogUtil.i("PageActionReport--->init--->reportUrl::"+reportUrl);
		ReportBaseInfo baseInfo = new ReportBaseInfo(user.getOemid(), appId, versionCode, user.getHid(), user.getUid());
		ReportManager.getInstance().setActionReportBaseUrl(reportUrl);
		ReportManager.getInstance().setReportBaseInfo(baseInfo);
	}
	
	public void reportPageAction(PageType pagetype){
		reportPageAction(pagetype, null, null, null, null);
	}
	
	public void reportPageAction(PageType pagetype,ModuleType moduletype,String focusid,FocusType focustype,Action action){
/*		ReportModel model = new ReportModel();
		model.setType("PageBrowsing");
		model.setName("epg");
		Epg epg = new Epg();
		if(pagetype!=null){
			epg.setPagetype(pagetype.toString());
		}
		if(moduletype!=null){
			epg.setModuletype(moduletype.toString());
		}
		if(focusid!=null){
			epg.setFocusid(focusid);
		}
		if(focustype!=null){
			epg.setFocustype(focustype.toString());
		}
		if(action!=null){
			epg.setAction(action.toString());
		}
		long time = System.currentTimeMillis();
		epg.setEpgid("-1");
		epg.setId(time+uid);
		model.setEpg(epg);
		String jsonStr = JSON.toJSONString(model);
		//sb.append("&");
		//sb.append("stamp=" + time);
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(sb.toString());
		strBuilder.append("&");
		strBuilder.append("stamp=" + time);
		//String mBaseUrl = sb.toString();
		LogUtil.i("PageActionReport-->reportPageAction-->url::"+ strBuilder.toString()+",param::"+jsonStr);
		if(context!=null){
			ReportManager.getInstance().init(context).reportPlayer(strBuilder.toString(), jsonStr);
		}*/
		Epg epg = new Epg();
		if(pagetype!=null){
			epg.setPagetype(pagetype.toString());
		}
		if(moduletype!=null){
			epg.setModuletype(moduletype.toString());
		}
		if(focusid!=null){
			epg.setFocusid(focusid);
		}
		if(focustype!=null){
			epg.setFocustype(focustype.toString());
		}
		if(action!=null){
			epg.setAction(action.toString());
		}
		epg.setEpgid("-1");
		ReportManager.getInstance().actionReport(epg);
	}
	
	
}
