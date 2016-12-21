package com.voole.statistics.useraction;

import android.content.Context;
import android.text.TextUtils;

import com.voole.statistics.report.ReportBaseInfo;
import com.voole.statistics.report.ReportConfig;
import com.voole.statistics.terminalinfo.GetContentUtil;

public class ActionReportManager {

	private static ActionReportManager manager = new ActionReportManager();

	private ActionReportManager(){

	}

	public static ActionReportManager getInstance(){
		return manager;
	}

	public String getActionUrl(Context context, ReportBaseInfo info, String baseUrl) {
		StringBuffer urlBuffer = new StringBuffer();
		urlBuffer.append(baseUrl.trim() + "?");
		urlBuffer.append("action=" + ReportConfig.REPORT_TYPE_ACTION);
		urlBuffer.append("&version=" + ReportConfig.actionVersion);
		if(info != null) {
			urlBuffer.append("&oemid=" + info.getOemid().trim());
			urlBuffer.append("&hid=" + info.getHid().trim());
			urlBuffer.append("&uid=" + info.getUid().trim());
			if (TextUtils.isEmpty(info.getAppId())) {
				urlBuffer.append("&appid=-1");
			} else {
				urlBuffer.append("&appid=" + info.getAppId().trim());
			}
			urlBuffer.append("&appversion=" + info.getAppversion().trim());
		}
		urlBuffer.append("&packagename=" + GetContentUtil.getPackageName(context));
		urlBuffer.append("&stamp=" + System.currentTimeMillis());
		return urlBuffer.toString();
	}

}
