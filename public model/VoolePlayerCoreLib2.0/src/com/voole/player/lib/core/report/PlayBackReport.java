package com.voole.player.lib.core.report;

import com.gntv.tv.common.ap.AdParser;

public class PlayBackReport extends BaseReport{
	private final static String ACTION_SESSION = "player_create_lookback_sess";
	private final static String ACTION_STATE = "player_change_lookback_state";
	private String mChannelId;
	private int mDuration;
	@Override
	public void notifyPlayAuthStart(String mid, String fid, String epgid,
			String cpid, String cdnType, String channelId, String sTime,
			String eTime) {
		mChannelId = channelId;
		super.notifyPlayAuthStart(mid, fid, epgid, cpid, cdnType, channelId, sTime, eTime);
	}
	
	@Override
	public void notifyPrepare(String param) {
		AdParser adParser = new AdParser(param);
		adParser.parser();
		String playUrl = adParser.getAd().getPlayUrl();
		String authCode = adParser.getAd().getAuthCode();
		ReportModel.Player player = new ReportModel.Player();
		player.setUrl(playUrl);
		player.setCid(mChannelId);
//		player.setTypecode(SessionType.CHANNEL_SWITCH.toString());
		createSession(authCode, player);
		
		ReportModel.Player player_state_prepare = new ReportModel.Player();
		player_state_prepare.setState(PlayStateType.PREPARE.toString());
		player_state_prepare.setPlaytime("0");
		reportState(player_state_prepare);
		
		ReportModel.Player player_state_bufferstart = new ReportModel.Player();
		player_state_bufferstart.setState(PlayStateType.BUFFERSTART.toString());
		player_state_prepare.setPlaytime("0");
		reportState(player_state_bufferstart);
		
		super.notifyPrepare(param);
	}
	
	@Override
	public void notifyOnPrePared(int durationMsec) {
		mDuration = durationMsec / 1000;
		ReportModel.Player player_state_bufferend = new ReportModel.Player();
		player_state_bufferend.setState(PlayStateType.BUFFEREND.toString());
		player_state_bufferend.setPlaytime("0");
		reportState(player_state_bufferend);
		super.notifyOnPrePared(durationMsec);
	}
	
	@Override
	public void notifyStart() {
		ReportModel.Player player_state_playstart = new ReportModel.Player();
		player_state_playstart.setState(PlayStateType.PLAYSTART.toString());
		player_state_playstart.setPlaytime("0");
		player_state_playstart.setRestime(mDuration + "");
		reportState(player_state_playstart);
		super.notifyStart();
	}
	
	@Override
	public void notifyBufferStart(int currentPlayTimeMsec) {
		ReportModel.Player player_state_bufferstart = new ReportModel.Player();
		player_state_bufferstart.setState(PlayStateType.BUFFERSTART.toString());
		player_state_bufferstart.setPlaytime(currentPlayTimeMsec / 1000 + "");
		reportState(player_state_bufferstart);
		super.notifyBufferStart(currentPlayTimeMsec);
	}

	@Override
	public void notifyBufferEnd(int currentPlayTimeMsec) {
		ReportModel.Player player_state_bufferend = new ReportModel.Player();
		player_state_bufferend.setState(PlayStateType.BUFFEREND.toString());
		player_state_bufferend.setPlaytime(currentPlayTimeMsec / 1000 + "");
		reportState(player_state_bufferend);
		super.notifyBufferEnd(currentPlayTimeMsec);
	}
	
	@Override
	public void notifyError(int currentPlayTimeMsec) {
		ReportModel.Player player_state_error = new ReportModel.Player();
		player_state_error.setState(PlayStateType.ERROR.toString());
		player_state_error.setPlaytime(currentPlayTimeMsec / 1000 + "");
		reportState(player_state_error);
		super.notifyError(currentPlayTimeMsec);
	}
	
	@Override
	public void notifyPause(int currentPlayTimeMsec) {
		ReportModel.Player player_state_pause = new ReportModel.Player();
		player_state_pause.setState(PlayStateType.PAUSE.toString());
		player_state_pause.setPlaytime(currentPlayTimeMsec / 1000 + "");
		reportState(player_state_pause);
		super.notifyPause(currentPlayTimeMsec);
	}
	
	@Override
	public void notifyResume(int currentPlayTimeMsec) {
		ReportModel.Player player_state_resume = new ReportModel.Player();
		player_state_resume.setState(PlayStateType.RESUME.toString());
		player_state_resume.setPlaytime(currentPlayTimeMsec / 1000 + "");
		reportState(player_state_resume);
		super.notifyResume(currentPlayTimeMsec);
	}
	
	@Override
	public void notifySeek(int currentPlayTimeMsec, int seekToMsec) {
		ReportModel.Player player_state_seek = new ReportModel.Player();
		player_state_seek.setState(PlayStateType.SEEK.toString());
		player_state_seek.setPlaytime(currentPlayTimeMsec / 1000 + "");
		player_state_seek.setSeekstart(currentPlayTimeMsec / 1000 + "");
		player_state_seek.setSeekstop(seekToMsec / 1000 + "");
		reportState(player_state_seek);
		super.notifySeek(currentPlayTimeMsec, seekToMsec);
	}
	
	@Override
	public void notifyStop(int currentPlayTimeMsec) {
		ReportModel.Player player_state_stop = new ReportModel.Player();
		player_state_stop.setState(PlayStateType.PLAYSTOP.toString());
		player_state_stop.setPlaytime(currentPlayTimeMsec / 1000 + "");
		reportState(player_state_stop);
		super.notifyStop(currentPlayTimeMsec);
	}
	
	@Override
	public void notifyCompletion(int currentPlayTimeMsec) {
		ReportModel.Player player_state_stop = new ReportModel.Player();
		player_state_stop.setState(PlayStateType.PLAYSTOP.toString());
		player_state_stop.setPlaytime(currentPlayTimeMsec / 1000 + "");
		reportState(player_state_stop);
		super.notifyCompletion(currentPlayTimeMsec);
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
		return "3.1";
	}

	@Override
	public String getHeartBeatAction() {
		return "player_lookback_heartbeat";
	}
}
