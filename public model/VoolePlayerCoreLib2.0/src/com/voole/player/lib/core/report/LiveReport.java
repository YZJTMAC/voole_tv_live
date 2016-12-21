package com.voole.player.lib.core.report;

import com.gntv.tv.common.utils.LogUtil;

public class LiveReport extends BaseReport{
	private final static String ACTION_SESSION = "player_create_live_sess";
	private final static String ACTION_STATE = "player_change_live_state";
	private final static String ACTION_HEARTBEAT = "player_live_heartbeat";
	private String mChannelId;
	
	private long mAuthStartTime = 0;
	
	@Override
	public void notifyPlayAuthStart(String mid, String fid, String epgid,
			String cpid, String cdnType, String channelId, String sTime,
			String eTime) {
		mChannelId = channelId;
		mAuthStartTime = System.currentTimeMillis();
		LogUtil.d("LiveReport--->notifyPlayAuthStart----->mAuthStartTime--->" + mAuthStartTime);
		super.notifyPlayAuthStart(mid, fid, epgid, cpid, cdnType, channelId, sTime, eTime);
	}
	
	@Override
	public void notifyPlayAuthEnd(String playUrl, String authCode, int authTimeMsec) {
		ReportModel.Player player = new ReportModel.Player();
		player.setUrl(playUrl);
		player.setCid(mChannelId);
		player.setTypecode(SessionType.CHANNEL_SWITCH.toString());
		long time = System.currentTimeMillis();
		LogUtil.d("LiveReport--->notifyPlayAuthEnd---->mAuthStartTime--->" + mAuthStartTime);
		LogUtil.d("LiveReport--->notifyPlayAuthEnd---->mAuthEndTime--->" + time);
		long authTime = time - mAuthStartTime ;
		LogUtil.d("LiveReport--->notifyPlayAuthEnd---->authTime--->" + authTime);
		player.setAuthtime(authTime+"");
		createSession(authCode, player);
		super.notifyPlayAuthEnd(playUrl, authCode, authTimeMsec);
	}
	
	@Override
	public void notifyPrepare(String param) {
		/*AdParser adParser = new AdParser(param);
		adParser.parser();
		String playUrl = adParser.getAd().getPlayUrl();
		String authCode = adParser.getAd().getAuthCode();
		ReportModel.Player player = new ReportModel.Player();
		player.setUrl(playUrl);
		player.setCid(mChannelId);
		player.setTypecode(SessionType.CHANNEL_SWITCH.toString());
		long time = System.currentTimeMillis();
		LogUtil.d("LiveReport--->notifyPrepare---->mAuthStartTime--->" + mAuthStartTime);
		LogUtil.d("LiveReport--->mAuthEndTime--->" + time);
		long authTime = time - mAuthStartTime ;
		LogUtil.d("LiveReport--->authTime--->" + authTime);
		player.setAuthtime(authTime+"");
		createSession(authCode, player);*/
		
		ReportModel.Player player_state_prepare = new ReportModel.Player();
		player_state_prepare.setState(PlayStateType.PREPARE.toString());
		reportState(player_state_prepare);
		
		/*ReportModel.Player player_state_bufferstart = new ReportModel.Player();
		player_state_bufferstart.setState(PlayStateType.BUFFERSTART.toString());
		reportState(player_state_bufferstart);*/
		
		super.notifyPrepare(param);
	}
	
	@Override
	public void notifyOnPrePared(int durationMsec) {
		/*ReportModel.Player player_state_bufferend = new ReportModel.Player();
		player_state_bufferend.setState(PlayStateType.BUFFEREND.toString());
		reportState(player_state_bufferend);*/
		super.notifyOnPrePared(durationMsec);
	}
	
	@Override
	public void notifyStart() {
		ReportModel.Player player_state_playstart = new ReportModel.Player();
		player_state_playstart.setState(PlayStateType.PLAYSTART.toString());
		reportState(player_state_playstart);
		super.notifyStart();
	}
	
	@Override
	public void notifyBufferStart(int currentPlayTimeMsec) {
		ReportModel.Player player_state_bufferstart = new ReportModel.Player();
		player_state_bufferstart.setState(PlayStateType.BUFFERSTART.toString());
		reportState(player_state_bufferstart);
		super.notifyBufferStart(currentPlayTimeMsec);
	}

	@Override
	public void notifyBufferEnd(int currentPlayTimeMsec) {
		ReportModel.Player player_state_bufferend = new ReportModel.Player();
		player_state_bufferend.setState(PlayStateType.BUFFEREND.toString());
		reportState(player_state_bufferend);
		super.notifyBufferEnd(currentPlayTimeMsec);
	}
	
	@Override
	public void notifyError(int currentPlayTimeMsec) {
		ReportModel.Player player_state_error = new ReportModel.Player();
		player_state_error.setState(PlayStateType.ERROR.toString());
		reportState(player_state_error);
		super.notifyError(currentPlayTimeMsec);
	}
	
	/*@Override
	public void notifyReset() {
		ReportModel.Player player_state_reset = new ReportModel.Player();
		player_state_reset.setState(PlayStateType.PLAYSTOP.toString());
		reportState(player_state_reset);
		super.notifyReset();
	}*/
	
	@Override
	public void notifyResetStart() {
		ReportModel.Player player_state_reset = new ReportModel.Player();
		player_state_reset.setState(PlayStateType.RESETSTART.toString());
		reportState(player_state_reset);
		super.notifyResetStart();
	}
	
	@Override
	public void notifyResetEnd() {
		ReportModel.Player player_state_reset = new ReportModel.Player();
		player_state_reset.setState(PlayStateType.RESETEND.toString());
		reportState(player_state_reset);
		super.notifyResetEnd();
	}
	
	@Override
	public void notifyStop(int currentPlayTimeMsec) {
		ReportModel.Player player_state_stop = new ReportModel.Player();
		player_state_stop.setState(PlayStateType.PLAYSTOP.toString());
		reportState(player_state_stop);
		super.notifyStop(currentPlayTimeMsec);
	}
	
	@Override
	public void notifyRelease() {
		ReportModel.Player player_state_release = new ReportModel.Player();
		player_state_release.setState(PlayStateType.STOP.toString());
		reportState(player_state_release);
		super.notifyRelease();
	}

	@Override
	public String getSessionAction() {
		return ACTION_SESSION;
	}

	@Override
	public String getPlayState() {
		return ACTION_STATE;
	}

	@Override
	public String getReportVersion() {
		return "3.2";
	}

	@Override
	public String getHeartBeatAction() {
		return ACTION_HEARTBEAT;
	}
}
