package com.voole.player.lib.core;

import com.vad.sdk.core.base.AdEvent;

public interface VooleMediaPlayerListener{
//	public abstract void onInitPlayerComplete();
	
	public abstract void onPrepared(boolean isPreview, int previewTime, String isLiveShow, String mIsJumpPlay);

	public abstract boolean onInfo(int what, int extra);

	public abstract boolean onError(int what, int extra, String errorCode, String errorCodeOther, String errorMsgOther);

	public abstract void onCompletion();

	public abstract void onSeekComplete();
	
	public abstract void onBufferingUpdate(int percent);
	
	// ad
	public abstract void canSeek(boolean canSeek);
	
	public abstract void canExit(boolean canExit);

	public abstract void onSeek(int pos);
	
	public abstract void onExit();
	
	public abstract void onAdEvent(AdEvent e);
	
	public abstract void onMovieStart();
}
