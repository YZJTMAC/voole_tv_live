package com.voole.player.lib.core.standard;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.CountDownTimer;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.gntv.tv.common.ap.Ad;
import com.gntv.tv.common.ap.ProxyManager;
import com.gntv.tv.common.utils.DisplayManager;
import com.gntv.tv.common.utils.LogUtil;
import com.voole.player.lib.core.VooleMediaPlayer;
import com.voole.player.lib.core.VooleMediaPlayerListener;
import com.voole.player.lib.core.ad.AdPlayer;
import com.voole.player.lib.core.interfaces.IPlayReport;

public abstract class StandardPlayer extends AdPlayer{
	private MediaPlayer mPlayer = null;
	private SurfaceView mSurfaceView = null;
	private SurfaceHolder mSurfaceHolder = null;
	private Context context;
	
	private RelativeLayout.LayoutParams mSurfaceViewLayoutParams = null;
//	private VooleMediaPlayer mVooleMediaPlayer = null;
	
	protected abstract String getPlayUrl(String url);
	
	@Override
	public void initPlayer(VooleMediaPlayer vmp, Context context, VooleMediaPlayerListener l, IPlayReport report) {
		initMediaPlayer();
		this.context = context;
//		if (mVooleMediaPlayer == null) {
//			mVooleMediaPlayer = vmp;
//		}
		super.initPlayer(vmp, context, l, report);
		ProxyManager.GetInstance().startProxyCheckTimer();
	}
	
	private void initMediaPlayer(){
		mPlayer = new MediaPlayer();
		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mPlayer.setScreenOnWhilePlaying(true);
	}

	@Override
	protected void su_originalSeek(int msec) {
		if(mPlayer != null){
			LogUtil.d("StandardPlayer-->su_originalSeek");
			mPlayer.seekTo(msec);
		}
	}

	@Override
	protected void su_originalStop() {
		if(mPlayer != null){
			LogUtil.d("StandardPlayer-->su_originalStop");
			mPlayer.stop();
		}
	}

	@Override
	protected void su_originalInit(Context context) {
		if(mPlayer != null){
			LogUtil.d("StandardPlayer-->su_originalInit");
			StandardPlayerListener l = new StandardPlayerListener();
			mPlayer.setOnPreparedListener(l);
			mPlayer.setOnCompletionListener(l);
			mPlayer.setOnSeekCompleteListener(l);
			mPlayer.setOnBufferingUpdateListener(l);
			mPlayer.setOnErrorListener(l);
			mPlayer.setOnInfoListener(l);
		}
	}
	
	@Override
	protected void su_originalPrepare(final Ad ad) {
		if (ad != null) {
			su_originalPrepare(ad.getPlayUrl());
		}
	}

	@Override
	protected void su_originalPrepare(final String playUrl) {
		LogUtil.d("StandardPlayer-->su_originalPrepare, mSurfaceView " + ((mSurfaceView == null) ? "= null" : "!= null"));
		if(mSurfaceView == null){
			LogUtil.d("StandardPlayer-->su_originalPrepare-->mSurfaceView == null");
			mSurfaceView = new SurfaceView(mContext);
//			mSurfaceView = mVooleMediaPlayer.getSurfaceView();
			mSurfaceHolder = mSurfaceView.getHolder();
			mSurfaceHolder.setSizeFromLayout();
			mSurfaceHolder.addCallback(new Callback() {
				@Override
				public void surfaceDestroyed(SurfaceHolder arg0) {
					LogUtil.d("StandardPlayer-->surfaceDestroyed" );
					mSurfaceHolder = null;
				}
				
				@Override
				public void surfaceCreated(SurfaceHolder sh) {
					LogUtil.d("StandardPlayer-->surfaceCreated");
					mSurfaceHolder = sh;
					LogUtil.d("StandardPlayer-->surfaceCreated, mPlayer " + ((mPlayer == null) ? "= null" : "!= null"));
					if(mPlayer == null){
						initMediaPlayer();
					}
					mPlayer.setDisplay(mSurfaceHolder);
					doPrepare(playUrl);
				}
				
				@Override
				public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
					LogUtil.d("StandardPlayer-->surfaceChanged" );
				}
			});
			mSurfaceViewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 
					RelativeLayout.LayoutParams.MATCH_PARENT);
			mVooleMediaPlayer.addView(mSurfaceView, mSurfaceViewLayoutParams);
		}else{
			LogUtil.d("StandardPlayer-->su_originalPrepare-->mSurfaceView != null");
//			mSurfaceView.setVisibility(View.VISIBLE);
			doPrepare(playUrl);
		}
		
	}
	
	private void doPrepare(String playUrl){
		try {
			String url = getPlayUrl(playUrl);
			LogUtil.d("StandardPlayer-->setDataSource-->url-->" + url );
			mPlayer.setDataSource(url);
			mPlayer.prepareAsync();
		} catch (Exception e) {
			LogUtil.d("StandardPlayer-->setDataSource-->exception-->" + e.toString());
		} 
	}

	@Override
	protected void su_originalStart() {
		if(mPlayer != null){
			LogUtil.d("StandardPlayer-->su_originalStart");
			mPlayer.start();
		}
	}
	
	/*@Override
	protected void su_originalStart(int pos) {
		if(mPlayer != null){
			LogUtil.d("StandardPlayer-->su_originalStart-->pos-->" + pos);
			notifyOnSeek(pos);
		}
	}*/

	@Override
	protected void su_originalPause() {
		if(mPlayer != null){
			LogUtil.d("StandardPlayer-->su_originalPause");
			mPlayer.pause();
		}
	}

	@Override
	protected int su_originalGetDuration() {
		if(mPlayer != null){
			int time = mPlayer.getDuration();
			LogUtil.d("StandardPlayer-->su_originalGetDuration-->status-->" + mCurrentStatus + "-->time-->" + time);
			return time;
		}
		return 0;
	}

	@Override
	protected int su_originalGetCurrentPosition() {
		if(mPlayer != null){
			int time = mPlayer.getCurrentPosition();
//			LogUtil.d("StandardPlayer-->su_originalGetCurrentPosition-->status-->" + mCurrentStatus + "-->time-->" + time);
			return time;
		}
		return 0;
	}

	@Override
	public void setLooping(boolean looping) {
		if(mPlayer != null){
			mPlayer.setLooping(looping);
		}
	}
	
	@Override
	protected void su_originalReset() {
		LogUtil.d("StandardPlayer-->su_originalReset--->mCurrentStatus---->" + mCurrentStatus);
		endCounter();
		if(mPlayer != null){
			LogUtil.d("StandardPlayer-->su_originalReset---->start--->mCurrentStatus---->" + mCurrentStatus);
			mPlayer.reset();
			LogUtil.d("StandardPlayer-->su_originalReset---->end----->mCurrentStatus---->" + mCurrentStatus);
		}
	}
	
	/*@Override
	protected void su_originalRelease() {
		LogUtil.d("StandardPlayer-->su_originalRelease");
		if(mPlayer != null){
			mPlayer.reset();
			mPlayer.release();
			mPlayer = null;
		}
	}*/
	
	/*@Override
	public void reset() {
		LogUtil.d("StandardPlayer-->reset");
		if(mPlayer != null){
			mPlayer.reset();
		}
		super.reset();
	}*/
	
	@Override
	public void release() {
		new Thread(){
			public void run() {
				ProxyManager.GetInstance().finishPlay();
			};
		}.start();
		ProxyManager.GetInstance().stopProxyCheckTimer();
		super.release();
		LogUtil.d("StandardPlayer-->release");
		if(mPlayer != null){
			mPlayer.reset();
			mPlayer.release();
			mPlayer = null;
		}
		
		recycle();
	}
	
	@Override
	public void recycle() {
		LogUtil.d("StandardPlayer-->recycle");
		if(mVooleMediaPlayer != null){
			mVooleMediaPlayer.removeAllViews();
		}
		mSurfaceView = null;
		mSurfaceHolder = null;
//		release();
		/*mSurfaceView = null;*/
//		mSurfaceView.setVisibility(View.GONE);
		
	}
	
	@Override
	public int getVideoHeight() {
		if(mPlayer != null){
			return mPlayer.getVideoHeight();
		}
		return 0;
	}

	@Override
	public int getVideoWidth() {
		if(mPlayer != null){
			return mPlayer.getVideoWidth();
		}
		return 0;
	}
	
	
	public class Counter extends CountDownTimer {
		public Counter(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			LogUtil.d("Counter-->onFinish-->onError-->Status-->" + mCurrentStatus.toString());
			if(mCurrentStatus == com.voole.player.lib.core.interfaces.IPlayer.Status.Preparing){
				mCurrentStatus = com.voole.player.lib.core.interfaces.IPlayer.Status.Error;
				if (mVooleAdEventListener != null) {
					mVooleAdEventListener.onError(0, 0);
				}

				notifyOnError(0, 0);
			}
		}

		@Override
		public void onTick(long millisUntilFinished) {
			LogUtil.d("Counter-->onTick-->onError-->Status-->" + mCurrentStatus.toString());
			if(mCurrentStatus != com.voole.player.lib.core.interfaces.IPlayer.Status.Preparing){
				cancel();
			}
		}
	}
	
	private Counter mCounter = null;
	private void startErrorCounter(){
		if(mCounter == null){
			mCounter = new Counter(8000, 500);
		}
		mCounter.start();
	}
	
	private void endCounter(){
		if(mCounter != null){
			mCounter.cancel();
			mCounter = null;
		}
	}
	
	public class StandardPlayerListener implements 
			OnPreparedListener, OnSeekCompleteListener, OnInfoListener,
			OnErrorListener, OnBufferingUpdateListener, OnCompletionListener {

		@Override
		public void onCompletion(MediaPlayer arg0) {
			LogUtil.d("MediaPlayer-->onCompletion");
			Runnable appTask = new Runnable() {
				@Override
				public void run() {
					/*if (mVooleMediaPlayerListener != null) {
						mVooleMediaPlayerListener.onCompletion();
					}*/
					notifyOnCompletion();
				}
			};

			if (mVooleAdEventListener != null) {
				mVooleAdEventListener.onCompletion(appTask);
			} else {
				appTask.run();
			}
		}

		@Override
		public void onBufferingUpdate(MediaPlayer arg0, int percent) {
//			LogUtil.d("MediaPlayer-->onBufferingUpdate-->percent-->" + percent);
			/*if (mVooleMediaPlayerListener != null) {
				mVooleMediaPlayerListener.onBufferingUpdate(percent);
			}*/
			notifyOnBufferingUpdate(percent);
		}

		@Override
		public boolean onError(MediaPlayer arg0, int what, int extra) {
			LogUtil.d("MediaPlayer-->onError-->what-->" + what + "-->extra-->" + extra + "-->Status-->" + mCurrentStatus.toString());
			if(mCurrentStatus == Status.IDLE || mCurrentStatus == Status.Prepared || mCurrentStatus == Status.Playing){
				return true;
			}
			
			if(mCurrentStatus == Status.Preparing){
				startErrorCounter();
			}
			
			/*mCurrentStatus = Status.Error;
			if (mVooleAdEventListener != null) {
				mVooleAdEventListener.onError(what, extra);
			}*/

			/*if (mVooleMediaPlayerListener != null) {
				return mVooleMediaPlayerListener.onError(what, extra);
			}*/
			/*notifyOnError(what, extra);*/

			return true;
		}

		@Override
		public boolean onInfo(MediaPlayer arg0, int what, int extra) {
			if(what == 701 || what == 702){
				LogUtil.d("MediaPlayer-->onInfo-->what-->" + what + "-->extra-->" + extra + "-->mCurrentStatus--->" + mCurrentStatus);
				if(mCurrentStatus == Status.Playing){
					if (mVooleAdEventListener != null) {
						mVooleAdEventListener.onInfo(what, extra);
					}
					
					return notifyOnInfo(what, extra);
				}
			}

			return true;
		}

		@Override
		public void onSeekComplete(MediaPlayer arg0) {
			LogUtil.d("MediaPlayer-->onSeekComplete");
			/*if (mVooleMediaPlayerListener != null) {
				mVooleMediaPlayerListener.onSeekComplete();
			}*/
			
			notifyOnSeekComplete();
		}

		@Override
		public void onPrepared(MediaPlayer arg0) {
			LogUtil.d("MediaPlayer-->onPrepared");
			mCurrentStatus = Status.Prepared;
			endCounter();
			Runnable appTask = new Runnable() {
				@Override
				public void run() {
					/*if (mVooleMediaPlayerListener != null) {
						mVooleMediaPlayerListener.onPrepared();
					}*/
					notifyOnPrepared();
				}
			};

			if (mVooleAdEventListener != null) {
				mVooleAdEventListener.onPrepared(appTask);
			} else {
				appTask.run();
			}
		}

	}
	
	@Override
	public void setVolume(float leftVolume, float rightVolume) {
		if(mPlayer != null){
			mPlayer.setVolume(leftVolume, rightVolume);
		}
	}
	
	@Override
	public void setSurfaceHolderFixedSize(int width, int height) {
		if(mSurfaceHolder != null){
			mSurfaceHolder.setFixedSize(width, height);
		}
	}
	
	@Override
	public void changeVideoSize(boolean isFullScreen) {
		int height = DisplayManager.GetInstance().getScreenHeight();
		int width = 0;
		if (!isFullScreen) {
			width = height * 4/3;
		} else {
			width = DisplayManager.GetInstance().getScreenWidth();
		}
		LogUtil.d("StandardPlayer->changeVideoSize, width = " + width + ", height = " + height);
		changeVideoSize(width, height);
	}
	
	private void changeVideoSize(int width, int height) {
		if (mSurfaceView != null) {
			mSurfaceViewLayoutParams = new RelativeLayout.LayoutParams(width, height);
			mSurfaceViewLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			mSurfaceView.setLayoutParams(mSurfaceViewLayoutParams);
		}
	}
	
	@Override
	public boolean isFullScreen() {
		int width = DisplayManager.GetInstance().getScreenWidth();
		int height = DisplayManager.GetInstance().getScreenHeight();
		LogUtil.d("StandardPlayer->isFullScreen, mSurfaceViewLayoutParams.width = " + mSurfaceViewLayoutParams.width + 
				", mSurfaceViewLayoutParams.height = " + mSurfaceViewLayoutParams.height + ", width = " + width + ", height = " + height);
		return (width == mSurfaceViewLayoutParams.width && height == mSurfaceViewLayoutParams.height) || 
				(LayoutParams.MATCH_PARENT == mSurfaceViewLayoutParams.width && LayoutParams.MATCH_PARENT == mSurfaceViewLayoutParams.height);
	}
}
