package com.voole.player.lib.core.ad;

import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.gntv.tv.common.ap.Ad;
import com.gntv.tv.common.ap.AdParser;
import com.gntv.tv.common.utils.LogUtil;
import com.vad.sdk.core.VAdSDK;
import com.vad.sdk.core.base.AdEvent;
import com.vad.sdk.core.base.interfaces.IAdPlayer;
import com.vad.sdk.core.base.interfaces.IAdPlayerEventListener;
import com.vad.sdk.core.base.interfaces.IAdPlayerOperationHandler;
import com.vad.sdk.core.base.interfaces.IAdPlayerUIController;
import com.voole.player.lib.core.VooleMediaPlayer;
import com.voole.player.lib.core.VooleMediaPlayerListener;
import com.voole.player.lib.core.base.BasePlayer;
import com.voole.player.lib.core.interfaces.IPlayReport;

public abstract class AdPlayer extends BasePlayer{
	protected IAdPlayerOperationHandler mVooleAdOperationHandler = null; 
	protected IAdPlayerEventListener mVooleAdEventListener = null;
	
	@Override
	public void initPlayer(VooleMediaPlayer vmp, Context context, VooleMediaPlayerListener l, IPlayReport report) {
		if(isLive()){
	        VAdSDK.getInstance().initLivePlayerAd(context, mVooleAdPlayer, mAdUIController);
		}else{
	        VAdSDK.getInstance().initEpgPlayerAd(context, mVooleAdPlayer, mAdUIController);
		}
		super.initPlayer(vmp, context, l, report);
	}
	
	private IAdPlayerUIController mAdUIController = new IAdPlayerUIController() {

		@Override
		public ViewGroup getAdView() {
			return mVooleMediaPlayer;
		}

		@Override
		public void setCanSeek(boolean seekable) {
			notifyCanSeek(seekable);
		}

		@Override
		public void setCanExit(boolean canExit) {
			notifyCanExit(canExit);
		}

		@Override
		public void onSeek(int pos) {
			notifyOnSeek(pos);
		}

		@Override
		public void onAdEvent(AdEvent adEvent) {
			notifyOnAdEvent(adEvent);
		}

		@Override
		public void onExit() {
			notifyOnExit();
		}

		@Override
		public void onMovieStart() {
			notifyOnMovieStart();
		}

	};
	
	private IAdPlayer mVooleAdPlayer = new IAdPlayer(){

		@Override
		public void originalSeek(int pos) {
			su_originalSeek(pos);
		}

		@Override
		public void originalStop() {
			su_originalStop();
		}
		
		@Override
		public void originalInit(Context context,
				IAdPlayerOperationHandler adPlayerOperationHandler,
				IAdPlayerEventListener adPlayerEventListener) {
			mVooleAdOperationHandler = adPlayerOperationHandler;
			mVooleAdEventListener = adPlayerEventListener;
			su_originalInit(context);
		}

		public void originalPrepareUrl(String playUrl) {
			su_originalPrepare(playUrl);
		};

		@Override
		public void originalPrepare(String xmlSrc) {
			AdParser adParser = new AdParser(xmlSrc);
			adParser.parser();
			Ad ad = adParser.getAd();
			if(ad != null){
				mIsLiveShow = ad.getIsLiveShow();
				mIsJumpPlay = ad.getIsJumpPlay();
				if (ad.getPriview() != null && !"0".equals(ad.getPriview())
						&& ad.getPriviewTime() != null && !"0".equals(ad.getPriviewTime())) {
					mIsPreview = true;
					try {
						mPreviewTime = Integer.parseInt(ad.getPriviewTime());
					} catch (Exception e) {
						LogUtil.d("originalPrepare-->Exception-->" + e.toString());
						mPreviewTime = 0;
					}
				}else{
					mIsPreview = false;
					if(!TextUtils.isEmpty(ad.getMpbTime())){
						try {
							mPreviewTime = Integer.parseInt(ad.getMpbTime());
						} catch (Exception e) {
							LogUtil.d("originalPrepare-->Exception-->" + e.toString());
							mPreviewTime = 0;
						}
					}
				}
			}else{
				mIsPreview = false;
				mPreviewTime = 0;
				mIsLiveShow = "0";
				mIsJumpPlay = "0";
			}
			/*if(ad != null){
				mIsLiveShow = ad.getIsLiveShow();
			}
			if (ad != null && !"0".equals(ad.getPriview())
					&& ad.getPriview() != null
					&& !"0".equals(ad.getPriviewTime())) {
				mIsPreview = true;
				mPreviewTime = Integer.parseInt(ad.getPriviewTime());
			}else{
				mIsPreview = false;
				mPreviewTime = 0;
			}*/
			su_originalPrepare(ad);
		}

		@Override
		public void originalStart() {
			if(mPlayReport != null){
				if(mCurrentStatus == Status.Pause){
					mPlayReport.notifyResume(getCurrentPosition());
				}else{
					mPlayReport.notifyStart();
				}
			}
			mCurrentStatus = Status.Playing;
			su_originalStart();
		}
		
		/*@Override
		public void originalStart(int pos) {
			if(mPlayReport != null){
				if(mCurrentStatus == Status.Pause){
					mPlayReport.notifyResume(getCurrentPosition());
				}else{
					mPlayReport.notifyStart();
				}
			}
			mCurrentStatus = Status.Playing;
			su_originalStart(pos);
		}*/
		
		@Override
		public void originalPause() {
			mCurrentStatus = Status.Pause;
			if(mPlayReport != null){
				if(mVooleMediaPlayer != null){
					mPlayReport.notifyPause(mVooleMediaPlayer.getCurrentPosition());
				}
			}
			su_originalPause();
		}

		@Override
		public int originalGetDuration() {
			if(mCurrentStatus != Status.IDLE && mCurrentStatus != Status.Preparing && mCurrentStatus != Status.Error){
				return su_originalGetDuration();
			}
			return 0;
		}

		@Override
		public int originalGetCurrentPosition() {
			if(mCurrentStatus != Status.IDLE && mCurrentStatus != Status.Preparing && mCurrentStatus != Status.Error && mCurrentStatus != Status.Prepared){
				return su_originalGetCurrentPosition();
			}
			return 0;
		}

		@Override
		public boolean isAutoPushCurrentPosition() {
			return false;
		}

		@Override
		public void originalReset() {
			mCurrentStatus = Status.IDLE;
			if(mPlayReport != null){
				mPlayReport.notifyResetStart();
			}
			su_originalReset();
			if(mPlayReport != null){
				mPlayReport.notifyResetEnd();
			}
		}

		/*@Override
		public void originalRelease() {
			mCurrentStatus = Status.IDLE;
			su_originalRelease();
		}*/
		
	};
	
	protected abstract void su_originalSeek(int msec);
	protected abstract void su_originalStop();
//	protected abstract void su_originalInit(Context context, VooleMediaPlayerListener listener);
	protected abstract void su_originalInit(Context context);
	protected abstract void su_originalPrepare(Ad ad);
	protected abstract void su_originalPrepare(String  playUrl);
	protected abstract void su_originalStart();
//	protected abstract void su_originalStart(int pos);
	protected abstract void su_originalPause();
	protected abstract void su_originalReset();
//	protected abstract void su_originalRelease();
	protected abstract int su_originalGetDuration();
	protected abstract int su_originalGetCurrentPosition();
	protected abstract boolean isLive();

	
	
	@Override
	public void prepare(String param, Map<String, String> extMap) {
		super.prepare(param, extMap);
		this.mVooleAdOperationHandler.onPrepare(param, extMap);	
	}
	

	@Override
	public void start() {
		super.start();
		this.mVooleAdOperationHandler.onStart();
	}

	@Override
	public void start(int msec) {
		super.start(msec);
		this.mVooleAdOperationHandler.onStart(msec);
	}

	@Override
	public void seekTo(int msec) {
		super.seekTo(msec);
		this.mVooleAdOperationHandler.onSeek(msec);
	}

	@Override
	public void pause() {
		this.mVooleAdOperationHandler.onPause(true);
		super.pause();
	}

	@Override
	public void pauseNoAd() {
		this.mVooleAdOperationHandler.onPause(false);
		super.pause();
	}

	@Override
	public void stop() {
		super.stop();
		this.mVooleAdOperationHandler.onStop();
	}
	
	@Override
	public void reset() {
		super.reset();
		this.mVooleAdOperationHandler.onReset();
	}
	
	@Override
	public void release() {
		super.release();
		VAdSDK.getInstance().releasePlayerAd();
//		this.mVooleAdOperationHandler.onRelease();
	}
	
	@Override
	public int getCurrentPosition() {
		return this.mVooleAdOperationHandler.getCurrentPosition();
	}

	@Override
	public int getDuration() {
		return this.mVooleAdOperationHandler.getDuration();
	}

	@Override
	public boolean onKeyDown(int keyCode) {
		return this.mVooleAdOperationHandler.onKeyDown(keyCode);
	}
	
}
