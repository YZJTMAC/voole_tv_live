package com.voole.statistics.report;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.gntv.report.service.ReportMessageService;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.MobclickAgent.EScenarioType;
import com.umeng.analytics.MobclickAgent.UMAnalyticsConfig;
import com.voole.statistics.terminalinfo.AppReportInfo;
import com.voole.statistics.useraction.ActionInfo;
import com.voole.statistics.useraction.ActionInfo.Epg;
import com.voole.statistics.useraction.ActionReportManager;

public class ReportManager {

	private static ReportManager reportManager = new ReportManager();
	private ReportBaseInfo baseInfo = null;
	private String actionBaseUrl = null;
	private String appBaseUrl = null;
	private Context mContext = null;
	private long actionReportTime = 0;
	private  Map<String, String> map_ekv = null;
	
	private boolean hasUmengReport = false;
	
	private String lastId = null;

	private ReportManager(){

	};

	public static ReportManager getInstance(){
		return reportManager;
	}
	
	private void initUmeng(Context context, String appKey, String channelId){
		if (context != null && !TextUtils.isEmpty(appKey) && !TextUtils.isEmpty(channelId)) {
			UMAnalyticsConfig config = new UMAnalyticsConfig(context, appKey, channelId);
			MobclickAgent.startWithConfigure(config);
			MobclickAgent.setDebugMode(true);
			MobclickAgent.openActivityDurationTrack(true);
			MobclickAgent.setCheckDevice(false);
			MobclickAgent.setSessionContinueMillis(Integer.MAX_VALUE);
			MobclickAgent.setScenarioType(mContext, EScenarioType.E_UM_NORMAL);
			hasUmengReport = true;
		}
	}

	public void init(Context context, String channelId, String appKey){
		mContext = context;
		if( mContext != null ){
			initUmeng(context, appKey, channelId);
			Intent intent = new Intent(mContext,ReportMessageService.class);
			mContext.startService(intent);
		}
	}
	public void init(Context context){
		this.mContext = context;
		if( mContext != null ){
			Intent intent = new Intent(mContext,ReportMessageService.class);
			mContext.startService(intent);
		}
	}

	public void release(){
		if( mContext != null ){
			Intent intent = new Intent(mContext,ReportMessageService.class);
			mContext.stopService(intent);
			mContext = null;
		}
	}

	public synchronized void doPostReport(String url,String data){
		if(mContext!=null){
			Log.i(ReportConfig.REPORTTAG,"ReportManager---->doPostReport------>url:" + url +"****data:" + data);
			Log.i(ReportConfig.TAG,"ReportManager---->doPostReport------>data:" + url +"****data:" + data);
			doIntent(ReportConfig.INTENT_ACTION_DOPOST, url, data, null);
		}
	}

	public synchronized void doGetReport(String msg){
		if(mContext!=null){
			Log.i(ReportConfig.REPORTTAG,"ReportManager---->doGetReport------>msg:" + msg);
			Log.i(ReportConfig.TAG,"ReportManager---->doGetReport------>msg:" + msg);
			doIntent( ReportConfig.INTENT_ACTION_DOGET, null, null, msg);
		}
	}

	public void setActionReportBaseUrl(String actionBaseUrl){
		this.actionBaseUrl = actionBaseUrl;
	}

	public void setAppReportBaseUrl(String appBaseUrl){
		this.appBaseUrl = appBaseUrl;
	}

	public void setReportBaseInfo(ReportBaseInfo baseInfo){
		this.baseInfo = baseInfo ;
	}

	public synchronized void actionReport(Epg info){
		Log.i(ReportConfig.REPORTTAG,"ReportManager---->actionReport------>actionBaseUrl:" + actionBaseUrl);
		Log.i(ReportConfig.TAG,"ReportManager---->actionReport------>actionBaseUrl:" + actionBaseUrl);
		if(mContext != null && baseInfo != null && !TextUtils.isEmpty(actionBaseUrl) && info != null){
			Gson gson = new Gson();
			ActionInfo actionInfo = new ActionInfo();
			String id = System.currentTimeMillis()+""+baseInfo.getHid();
			info.setId(id);
			if (TextUtils.isEmpty(lastId)) {
				info.setLastid(id);
			} else {
				info.setLastid(lastId);
			}
			lastId = id;
			actionInfo.setEpg(info);
			String url = ActionReportManager.getInstance().getActionUrl(mContext, baseInfo, actionBaseUrl);
			if (actionReportTime != 0) {
				info.setExposuretime((System.currentTimeMillis() - actionReportTime)+"");
			} else {
				info.setExposuretime(0+"");
			}
			actionReportTime = System.currentTimeMillis();
			doIntent(ReportConfig.INTENT_ACTION_DOPOST, url, gson.toJson(actionInfo), null);
		}
	}

	public synchronized void actionReport(Epg info,String action){
		if(mContext != null && baseInfo != null && !TextUtils.isEmpty(actionBaseUrl) && info != null){
			info.setAction(action);
			actionReport(info);
		}
	}

	public synchronized void appReport(AppReportInfo info, boolean isLiveReport){
		if(mContext != null && baseInfo != null){
			info.setInfo(baseInfo);
			doAppIntent(info, appBaseUrl, isLiveReport);
		}
	}

	public synchronized void appReport(String baseUrl, String oemid, String appVersion, boolean isLiveReport){
		if(mContext != null){
			AppReportInfo info = new AppReportInfo();
			ReportBaseInfo baseInfo = new ReportBaseInfo();
			baseInfo.setOemid(oemid);
			baseInfo.setAppversion(appVersion);
			info.setInfo(baseInfo);
			doAppIntent(info, baseUrl, isLiveReport);
		}
	}

	public void onAppLivePause(){
		Intent intent = new Intent(mContext,ReportMessageService.class);
		intent.setAction(ReportConfig.INTENT_ACTION_APPLIVE_PAUSE);
		mContext.startService(intent);
	}

	public void onAppLiveRosuem(){
		Intent intent = new Intent(mContext,ReportMessageService.class);
		intent.setAction(ReportConfig.INTENT_ACTION_APPLIVE_RESUME);
		mContext.startService(intent);
	}

	/**
	 * Umeng计数事件统计
	 * @param context
	 * @param countType
	 */
	public void onCountReport(Context context, UmCountType countType){
		if (context != null && hasUmengReport && countType != null) {
			Log.i(ReportConfig.REPORTTAG,"ReportManager---->umneg:onCountReport------>eventId:" + countType.getValue());
			Log.i(ReportConfig.TAG,"ReportManager---->umneg:onCountReport------>eventId:" + countType.getValue());
			MobclickAgent.onEvent(context, countType.getValue());
		}
	}

	/**
	 *  Umeng计算事件统计
	 * @param context
	 * @param computeType
	 * @param duration
	 */
	public void onComputeReport(Context context, UmComputeType computeType, int duration){
		if (map_ekv == null) {
			map_ekv = new HashMap<String, String>();
		}
		if (context != null && hasUmengReport && computeType != null) {
			Log.i(ReportConfig.REPORTTAG,"ReportManager---->umneg:onComputeReport------>eventId:"
					+ computeType.getValue() + "--->duration:" + duration);
			Log.i(ReportConfig.TAG,"ReportManager---->umneg:onComputeReport------>eventId:" 
					+ computeType.getValue() + "--->duration:" + duration);
			MobclickAgent.onEventValue(context, computeType.getValue(), map_ekv, duration);
		}
	}

	/**
	 * 友盟页面统计 进入页面
	 * @param context
	 * @param pageName
	 */
	public void onUmengResume(Context context, String pageName) {
		if (context != null && hasUmengReport && !TextUtils.isEmpty(pageName)) {
			Log.i(ReportConfig.REPORTTAG,"ReportManager---->umneg:onUmengResume------>pageName:" + pageName);
			Log.i(ReportConfig.TAG,"ReportManager---->umneg:onUmengResume------>pageName:" + pageName);
			MobclickAgent.onResume(context);
			MobclickAgent.onPageStart(pageName);
		}
	}

	/**
	 * 友盟页面统计 离开页面
	 * @param context
	 * @param pageName
	 */
	public void onUmengPause(Context context, String pageName){
		if (context != null && hasUmengReport && !TextUtils.isEmpty(pageName)) {
			Log.i(ReportConfig.REPORTTAG,"ReportManager---->umneg:onUmengPause------>pageName:" + pageName);
			Log.i(ReportConfig.TAG,"ReportManager---->umneg:onUmengPause------>pageName:" + pageName);
			MobclickAgent.onPageEnd(pageName);
			MobclickAgent.onPause(context);
		}
	}

	private void doAppIntent(AppReportInfo info, String baseUrl, boolean isLiveReport){
		if(mContext != null){
			Intent intent = new Intent(mContext,ReportMessageService.class);
			intent.setAction(ReportConfig.INTENT_ACTION_APPREPORT);
			intent.putExtra(ReportConfig.INTENT_EXTRA_INFO, info);
			intent.putExtra(ReportConfig.INTENT_EXTRA_ISLIVEREPORT, isLiveReport);
			intent.putExtra(ReportConfig.INTENT_EXTRA_APPBASEURL, baseUrl);
			mContext.startService(intent);
		}
	}

	private void doIntent(String action, String url, String data, String msg){
		if(mContext != null){
			Intent intent = new Intent(mContext, ReportMessageService.class);
			intent.setAction(action);
			if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(data)) {
				intent.putExtra(ReportConfig.INTENT_EXTRA_URL, url);
				intent.putExtra(ReportConfig.INTENT_EXTRA_DATA, data);
			}
			if (!TextUtils.isEmpty(msg)) {
				intent.putExtra(ReportConfig.INTENT_EXTRA_MSG, msg);
			}
			mContext.startService(intent);
		}
	}
	

	public enum UmCountType{
		COUNT_START(ReportConfig.UMENG_COUNT_START_APK),
		COUNT_PLAY(ReportConfig.UMENG_COUNT_START_PLAY),
		COUNT_PLAYSUCCESS(ReportConfig.UMENG_COUNT_PLAY_SUCCESS);

		private String value;

		private UmCountType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public enum UmComputeType{
		COMPUTE_START(ReportConfig.UMENG_COMPUTE_START_TIME),
		COMPUTE_CARLTON(ReportConfig.UMENG_COMPUTE_CARLTON_TIME),
		COMPUTE_PLAY(ReportConfig.UMENG_COMPUTE_PLAY_TIME);

		private String value;

		private UmComputeType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

}
