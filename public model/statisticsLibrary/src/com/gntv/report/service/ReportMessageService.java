package com.gntv.report.service;


import com.voole.statistics.report.ReportConfig;
import com.voole.statistics.report.ReportInfo;
import com.voole.statistics.report.ReportMessageUtil;
import com.voole.statistics.terminalinfo.AppReportInfo;
import com.voole.statistics.terminalinfo.TerminalinfoManager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ReportMessageService extends Service {
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(ReportConfig.REPORTTAG,"ReportMessageService---->onCreate");
		Log.i(ReportConfig.TAG,"ReportMessageService---->onCreate");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(ReportConfig.REPORTTAG,"ReportMessageService---->onStartCommand");
		Log.i(ReportConfig.TAG,"ReportMessageService---->onStartCommand");
		if(intent!=null){
			ReportInfo reportInfo = new ReportInfo();
			String actionType = intent.getAction();
			Log.i(ReportConfig.REPORTTAG,"ReportMessageService---->onStartCommand----->actionType:" + actionType);
			if(ReportConfig.INTENT_ACTION_DOPOST.equalsIgnoreCase(actionType)){
				String url = intent.getStringExtra(ReportConfig.INTENT_EXTRA_URL);
				String data = intent.getStringExtra(ReportConfig.INTENT_EXTRA_DATA);
				Log.i(ReportConfig.REPORTTAG,"ReportMessageService---->onStartCommand--->post---->reportInfo:url::::" + url);
				Log.i(ReportConfig.REPORTTAG,"ReportMessageService---->onStartCommand---->post---->reportInfo:url::::" + data);
				reportInfo.setUrl(url);
				reportInfo.setData(data);
				if(ReportMessageUtil.getInstance() != null){
					ReportMessageUtil.getInstance().addMessage(reportInfo);
				}
			}else if (ReportConfig.INTENT_ACTION_DOGET.equalsIgnoreCase(actionType)) {
				String msg = intent.getStringExtra(ReportConfig.INTENT_EXTRA_MSG);
				reportInfo.setMsg(msg);
				Log.i(ReportConfig.REPORTTAG,"ReportMessageService---->onStartCommand--->get---->reportInfo:msg::::" + msg);
				if(ReportMessageUtil.getInstance() != null){
					ReportMessageUtil.getInstance().addMessage(reportInfo);
				}
			} else if(ReportConfig.INTENT_ACTION_APPREPORT.equalsIgnoreCase(actionType)){
				AppReportInfo appInfo = intent.getParcelableExtra(ReportConfig.INTENT_EXTRA_INFO);
				boolean isLiveReport = intent.getBooleanExtra(ReportConfig.INTENT_EXTRA_ISLIVEREPORT, true);
				String appBaseUrl = intent.getStringExtra(ReportConfig.INTENT_EXTRA_APPBASEURL);
				Log.i(ReportConfig.REPORTTAG,"ReportMessageService---->onStartCommand--->appReport---->appBaseUrl::" + appBaseUrl);
				Log.i(ReportConfig.REPORTTAG,"ReportMessageService---->onStartCommand--->appReport---->isLiveReport::" + isLiveReport);
				TerminalinfoManager.getInstance().transferAppinfoReportMessage(getApplicationContext(), appBaseUrl,appInfo, isLiveReport);
			} else if (ReportConfig.INTENT_ACTION_APPLIVE_PAUSE.equalsIgnoreCase(actionType)) {
				Log.i(ReportConfig.REPORTTAG,"ReportMessageService---->onStartCommand--->appLiveReport---->onpause");
				TerminalinfoManager.getInstance().pause();
			} else if (ReportConfig.INTENT_ACTION_APPLIVE_RESUME.equalsIgnoreCase(actionType)) {
				Log.i(ReportConfig.REPORTTAG,"ReportMessageService---->onStartCommand--->appLiveReport---->onresume");
				TerminalinfoManager.getInstance().resume();
			}
			//ReportInfo reportInfo = intent.getParcelableExtra("reportInfo");
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		TerminalinfoManager.getInstance().release();
		Log.i(ReportConfig.REPORTTAG,"ReportMessageService---->onDestroy");
		Log.i(ReportConfig.TAG,"ReportMessageService---->onDestroy");
		super.onDestroy();
	}
}
