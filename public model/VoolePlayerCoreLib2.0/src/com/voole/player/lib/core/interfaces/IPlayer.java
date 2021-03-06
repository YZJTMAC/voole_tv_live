package com.voole.player.lib.core.interfaces;

import java.util.Map;

import android.content.Context;

import com.voole.player.lib.core.VooleMediaPlayer;
import com.voole.player.lib.core.VooleMediaPlayerListener;

public interface IPlayer {
//	public void initPlayer(String param, IAdUIController adUIController, VooleMediaPlayer vmp, Context context, VooleMediaPlayerListener l);
//	public void setDataSource(String param);
	public void initPlayer(VooleMediaPlayer vmp, Context context, VooleMediaPlayerListener l, IPlayReport report);
	public void prepare(String param, Map<String, String> extMap);
	public void start();
	public void start(int msec);
	public void seekTo(int msec);
	public void pause();
	public void pauseNoAd();
	public void stop();
	public void reset();
	public void release();
	public void setLooping(boolean looping);
	public int getCurrentPosition();
	public int getDuration();
	public boolean onKeyDown(int keyCode);
	public Status getCurrentStatus();
	
	public int getVideoHeight();
	public int getVideoWidth();
	
	public void setVolume(float leftVolume, float rightVolume);
	
	public void setSurfaceHolderFixedSize(int width, int height);
	
	public void changeVideoSize(boolean isFullScreen);
	public boolean isFullScreen();
//	public void setVideoSize(int width, int height);
	
	public void recycle();
	
	public enum Status{
		IDLE, Preparing, Prepared, Playing, Pause, Error
	}
}
