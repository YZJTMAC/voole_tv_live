package com.voole.statistics.report;


/**
 * 配置类
 * @author Jacky
 * 2014-3-5下午1:47:02
 *
 */
public class ReportConfig {

	/**
	 * 统计模块版本
	 */
	public static final String statisticsVersion="3.0"; 
	/**
	 * 行为统计版本
	 */
	public static final String actionVersion="3.3"; 
	/**
	 * 终端信息统计版本
	 */

	public static final String appVersion="3.0"; 
	
	/**
	 * 行为汇报action
	 */
	public static final String REPORT_TYPE_ACTION="EpgReport"; 
	/**
	 * 终端统计上报action
	 */
	public static final String REPORT_TYPE_APP="appinfoReport"; 
	/**
	 * 心跳汇报action
	 */
	public static final String REPORT_TYPE_LIVE="appinfoLive";
	
	public static final String INTENT_ACTION_DOPOST="doPost";
	
	public static final String INTENT_ACTION_DOGET="doGet";
	
	public static final String INTENT_ACTION_APPREPORT="appReport";
	
	public static final String INTENT_ACTION_APPLIVE_PAUSE="appLivePause";
	
	public static final String INTENT_ACTION_APPLIVE_RESUME="appLiveResume";
	
	public static final String INTENT_EXTRA_URL="url";
	
	public static final String INTENT_EXTRA_DATA="data";
	
	public static final String INTENT_EXTRA_MSG="msg";
	
	public static final String INTENT_EXTRA_INFO="info";
	
	public static final String INTENT_EXTRA_APPBASEURL="appBaseUrl";
	
	public static final String INTENT_EXTRA_ISLIVEREPORT="isLiveReport";
	
	public static final String TAG="VooleEpg2.0";
	
	public static final String REPORTTAG="VooleReport";
	
	public static final String UMENG_COUNT_START_APK = "start_apk";
	
	public static final String UMENG_COUNT_START_PLAY = "start_ply";
	
	public static final String UMENG_COUNT_PLAY_SUCCESS = "ply_suc";
	
	public static final String UMENG_COMPUTE_START_TIME = "play_start";
	
	public static final String UMENG_COMPUTE_CARLTON_TIME = "carlton";
	
	public static final String UMENG_COMPUTE_PLAY_TIME = "play_time";
	
}
