package com.voole.player.lib.core.normal;

import java.util.Map;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import com.gntv.tv.common.utils.LogUtil;
import com.voole.player.lib.core.VooleMediaPlayer;
import com.voole.player.lib.core.VooleMediaPlayerListener;
import com.voole.player.lib.core.base.BasePlayer;
import com.voole.player.lib.core.interfaces.IPlayReport;

public class NormalPlayer extends BasePlayer {
	private MediaPlayer mMediaPlayer = null;
	private SurfaceView mSurfaceView = null;
	private SurfaceHolder mSurfaceHolder = null;

	@Override
	public void initPlayer(VooleMediaPlayer vmp, Context context,
			VooleMediaPlayerListener l, IPlayReport report) {
		initMediaPlayer();
		super.initPlayer(vmp, context, l, report);
	}

	private void initMediaPlayer() {
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mMediaPlayer.setScreenOnWhilePlaying(true);
		StandardPlayerListener l = new StandardPlayerListener();
		mMediaPlayer.setOnPreparedListener(l);
		mMediaPlayer.setOnCompletionListener(l);
		mMediaPlayer.setOnSeekCompleteListener(l);
		mMediaPlayer.setOnBufferingUpdateListener(l);
		mMediaPlayer.setOnErrorListener(l);
		mMediaPlayer.setOnInfoListener(l);
	}

	@Override
	public void prepare(final String param, Map<String, String> extMap) {
		LogUtil.d("NormalPlayer-->prepare-->" + param);
		if (mSurfaceView == null) {
			LogUtil.d("NormalPlayer-->prepare-->mSurfaceView == null");
			mSurfaceView = new SurfaceView(mContext);
			mSurfaceHolder = mSurfaceView.getHolder();
			mSurfaceHolder.addCallback(new Callback() {
				@Override
				public void surfaceDestroyed(SurfaceHolder arg0) {
					LogUtil.d("NormalPlayer-->surfaceDestroyed");
					mSurfaceHolder = null;
				}

				@Override
				public void surfaceCreated(SurfaceHolder sh) {
					LogUtil.d("NormalPlayer-->surfaceCreated");
					mSurfaceHolder = sh;
					mMediaPlayer.setDisplay(mSurfaceHolder);
					doPrepare(param);
				}

				@Override
				public void surfaceChanged(SurfaceHolder arg0, int arg1,
						int arg2, int arg3) {
					LogUtil.d("NormalPlayer-->surfaceChanged");
					mMediaPlayer.setDisplay(mSurfaceHolder);
				}
			});
			mVooleMediaPlayer.addView(mSurfaceView,
					new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.MATCH_PARENT,
							RelativeLayout.LayoutParams.MATCH_PARENT));
		} else {
			LogUtil.d("NormalPlayer-->prepare-->mSurfaceView != null");
			mSurfaceView.setVisibility(View.VISIBLE);
			doPrepare(param);
		}
		super.prepare(param, extMap);
	}

	private void doPrepare(String param) {
		if (mMediaPlayer == null) {
			initMediaPlayer();
		}
		try {
			LogUtil.d("NormalPlayer-->setDataSource-->url-->" + param);
			mMediaPlayer.setDataSource(param);
			mMediaPlayer.prepareAsync();
		} catch (Exception e) {
			LogUtil.d("NormalPlayer-->setDataSource-->exception-->"
					+ e.toString());
		}
	}

	@Override
	public void start() {
		LogUtil.d("NormalPlayer-->start");
		if (mMediaPlayer != null) {
			if(mPlayReport != null){
				if(mCurrentStatus == Status.Pause){
					mPlayReport.notifyResume(getCurrentPosition());
				}else{
					mPlayReport.notifyStart();
				}
			}
			mCurrentStatus = Status.Playing;
			mMediaPlayer.start();
		}
		super.start();
	}

	@Override
	public void start(int msec) {
		LogUtil.d("NormalPlayer-->start msec -->" + msec);
		if (mMediaPlayer != null) {
			if(mPlayReport != null){
				if(mCurrentStatus == Status.Pause){
					mPlayReport.notifyResume(getCurrentPosition());
				}else{
					mPlayReport.notifyStart();
				}
			}
			mMediaPlayer.start();
			mMediaPlayer.seekTo(msec);
			mCurrentStatus = Status.Playing;
		}
		super.start(msec);
	}

	@Override
	public void seekTo(int msec) {
		LogUtil.d("NormalPlayer-->seekTo msec -->" + msec);
		if (mMediaPlayer != null) {
			mMediaPlayer.seekTo(msec);
		}
		super.seekTo(msec);
	}

	@Override
	public void pause() {
		LogUtil.d("NormalPlayer-->pause");
		if (mMediaPlayer != null) {
			mMediaPlayer.pause();
			mCurrentStatus = Status.Pause;
			if(mPlayReport != null){
				if(mVooleMediaPlayer != null){
					mPlayReport.notifyPause(mVooleMediaPlayer.getCurrentPosition());
				}
			}
		}
		super.pause();
	}

	@Override
	public void pauseNoAd() {
		LogUtil.d("NormalPlayer-->pause");
		if (mMediaPlayer != null) {
			mMediaPlayer.pause();
			mCurrentStatus = Status.Pause;
			if(mPlayReport != null){
				if(mVooleMediaPlayer != null){
					mPlayReport.notifyPause(mVooleMediaPlayer.getCurrentPosition());
				}
			}
		}
		super.pause();
	}

	@Override
	public void stop() {
		LogUtil.d("NormalPlayer-->stop");
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
		}
		super.stop();
	}

	@Override
	public void reset() {
		LogUtil.d("NormalPlayer-->reset");
		if (mMediaPlayer != null) {
			mMediaPlayer.reset();
		}
		super.reset();
	}

	@Override
	public void release() {
		LogUtil.d("NormalPlayer-->release");
		if (mMediaPlayer != null) {
			mMediaPlayer.reset();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
		super.release();
	}

	@Override
	public void setLooping(boolean looping) {
		if (mMediaPlayer != null) {
			mMediaPlayer.setLooping(looping);
		}
	}

	@Override
	public int getCurrentPosition() {
		if (mMediaPlayer != null) {
			return mMediaPlayer.getCurrentPosition();
		}
		return 0;
	}

	@Override
	public int getDuration() {
		if (mMediaPlayer != null) {
			return mMediaPlayer.getDuration();
		}
		return 0;
	}

	@Override
	public boolean onKeyDown(int keyCode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void recycle() {
		LogUtil.d("StandardPlayer-->recycle");
		release();
		mSurfaceView.setVisibility(View.GONE);
	}
	
	@Override
	public int getVideoHeight() {
		if(mMediaPlayer != null){
			return mMediaPlayer.getVideoHeight();
		}
		return 0;
	}

	@Override
	public int getVideoWidth() {
		if(mMediaPlayer != null){
			return mMediaPlayer.getVideoWidth();
		}
		return 0;
	}

	public class StandardPlayerListener implements OnPreparedListener,
			OnSeekCompleteListener, OnInfoListener, OnErrorListener,
			OnBufferingUpdateListener, OnCompletionListener {

		@Override
		public void onCompletion(MediaPlayer arg0) {
			LogUtil.d("MediaPlayer-->onCompletion");
			notifyOnCompletion();
		}

		@Override
		public void onBufferingUpdate(MediaPlayer arg0, int percent) {
			// LogUtil.d("MediaPlayer-->onBufferingUpdate-->percent-->" +
			// percent);
			notifyOnBufferingUpdate(percent);
		}

		@Override
		public boolean onError(MediaPlayer arg0, int what, int extra) {
			LogUtil.d("MediaPlayer-->onError-->what-->" + what + "-->extra-->"
					+ extra);
			if (mCurrentStatus == Status.IDLE) {
				return true;
			}
//			mCurrentStatus = Status.Error;
			return notifyOnError(what, extra);
		}

		@Override
		public boolean onInfo(MediaPlayer arg0, int what, int extra) {
			// LogUtil.d("MediaPlayer-->onInfo-->what-->" + what + "-->extra-->"
			// + extra);
			return notifyOnInfo(what, extra);
		}

		@Override
		public void onSeekComplete(MediaPlayer arg0) {
			LogUtil.d("MediaPlayer-->onSeekComplete");
			notifyOnSeekComplete();
		}

		@Override
		public void onPrepared(MediaPlayer arg0) {
			LogUtil.d("MediaPlayer-->onPrepared");
			mCurrentStatus = Status.Prepared;
			notifyOnPrepared();
		}

	}

	@Override
	public void setVolume(float leftVolume, float rightVolume) {
		if(mMediaPlayer != null){
			mMediaPlayer.setVolume(leftVolume, rightVolume);
		}
	}

}
