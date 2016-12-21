package com.voole.player.lib.core.sohu;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.RelativeLayout;

import com.gntv.tv.common.ap.Ad;
import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.common.utils.MResource;
import com.sohutv.tv.player.interfaces.ISohuTVPlayerCallback;
import com.sohutv.tv.player.interfaces.impl.SohuTVPlayFragment;
import com.voole.player.lib.core.VooleMediaPlayer;
import com.voole.player.lib.core.VooleMediaPlayerListener;
import com.voole.player.lib.core.ad.AdPlayer;
import com.voole.player.lib.core.interfaces.IPlayReport;

public class SohuPlayer extends AdPlayer{
	private SohuTVPlayFragment mPlayer = null;
	private FragmentManager mFragmentManager = null;
	private View mLayout = null;
	
//	boolean isSurCreated = false;
	
	@Override
	public void initPlayer(VooleMediaPlayer vmp, Context context, VooleMediaPlayerListener l, IPlayReport report) {
		if(mLayout == null){
			LogUtil.d("SohuPlayer-->initPlayer-->mLayout == null");
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			int layoutId = MResource.getIdByName(context, "layout", "cs_vooleplayer_sohu");
			LogUtil.d("SohuPlayer-->initPlayer-->layoutId-->" + layoutId);
//			mLayout = inflater.inflate(R.layout.cs_vooleplayer_sohu, null);
			mLayout = inflater.inflate(layoutId, null);
			vmp.addView(mLayout, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
		}else{
			LogUtil.d("SohuPlayer-->initPlayer-->mLayout != null");
		}
		mLayout.setVisibility(View.VISIBLE);
		mFragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
		int playerId = MResource.getIdByName(context, "id", "play_fragment");
		LogUtil.d("SohuPlayer-->initPlayer-->playerId-->" + playerId);
//		mPlayer = (SohuTVPlayFragment) mFragmentManager.findFragmentById(R.id.play_fragment);
		mPlayer = (SohuTVPlayFragment) mFragmentManager.findFragmentById(playerId);
		
		/*mFragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
		mPlayer = new SohuTVPlayFragment();
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
//		transaction.add(vmp.getId(), mPlayer);
		transaction.replace(vmp.getId(), mPlayer);
		transaction.commit();*/
		
		
		/*mFragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
		mPlayer = new SohuTVPlayFragment();
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		transaction.add(vmp.getId(), mPlayer);
		transaction.commit();*/
		
		super.initPlayer(vmp, context, l, report);
	}
	

	@Override
	protected void su_originalSeek(int msec) {
		if(mPlayer != null){
			LogUtil.d("SohuPlayer-->su_originalSeek");
			mPlayer.seekTo(msec);
		}
	}

	@Override
	protected void su_originalStop() {
		if(mPlayer != null){
			LogUtil.d("SohuPlayer-->su_originalStop");
			mPlayer.stop();
		}
	}

	@Override
	protected void su_originalInit(Context context) {
		if(mPlayer != null){
			LogUtil.d("SohuPlayer-->su_originalInit");
			mPlayer.setPlayerCallback(new SohuPlayerListener());
		}
	}

	@Override
	protected void su_originalPrepare(Ad ad) {
		if(mPlayer != null){
			try {
				String url = ad.getPlayUrl();
				LogUtil.d("SohuPlayer-->su_originalPrepare-->url-->" + url );
				/*
				if(isSurCreated){
					LogUtil.d("SohuPlayer-->su_originalPrepare-->isSurCreated-->true");
					mPlayer.setVideoParam(url);		
				}else{
					while (!isSurCreated) {
						LogUtil.d("SohuPlayer-->su_originalPrepare-->isSurCreated-->false");
					}
					mPlayer.setVideoParam(url);		
				}*/
				mPlayer.setVideoParam(url);		
			} catch (Exception e) {
				LogUtil.d("SohuPlayer-->su_originalPrepare-->exception-->" + e.toString());
			} 
		}
	}

	@Override
	protected void su_originalStart() {
		if(mPlayer != null){
			LogUtil.d("SohuPlayer-->su_originalStart");
			mPlayer.start();
		}
	}

	/*@Override
	protected void su_originalStart(int pos) {
		if(mPlayer != null){
			LogUtil.d("SohuPlayer-->su_originalStart-->pos-->" + pos);
			notifyOnSeek(pos);
		}
	}*/

	@Override
	protected void su_originalPause() {
		if(mPlayer != null){
			LogUtil.d("SohuPlayer-->su_originalPause");
			mPlayer.pause();
		}
	}

	@Override
	protected int su_originalGetDuration() {
		if(mPlayer != null){
			int time = mPlayer.getDuration();
			LogUtil.d("SohuPlayer-->su_originalGetDuration-->status-->" + mCurrentStatus + "-->time-->" + time);
			return time;
		}
		return 0;
	}

	@Override
	protected int su_originalGetCurrentPosition() {
		if(mPlayer != null){
			int time = mPlayer.getCurrentPosition();
//			LogUtil.d("SohuPlayer-->su_originalGetCurrentPosition-->status-->" + mCurrentStatus + "-->time-->" + time);
			return time;
		}
		return 0;
	}
	
	@Override
	protected void su_originalReset() {
		LogUtil.d("SohuPlayer-->su_originalReset");
		if(mPlayer != null){
			mPlayer.onRelease();
		}
	}

	/*@Override
	protected void su_originalRelease() {
		if(mPlayer != null){
			LogUtil.d("SohuPlayer-->su_originalRelease");
			mPlayer.onRelease();
			mPlayer = null;
		}
	}*/
	
	@Override
	protected boolean isLive() {
		return false;
	}
	
	
	/*@Override
	public void reset() {
		LogUtil.d("SohuPlayer-->reset");
		if(mPlayer != null){
			mPlayer.onRelease();
		}
		super.reset();
	}*/
	
	@Override
	public void release() {
		if(mPlayer != null){
			LogUtil.d("SohuPlayer-->release");
			mPlayer.onRelease();
			mPlayer = null;
		}
		super.release();
	}
	
	
	@Override
	public void recycle() {
		LogUtil.d("SohuPlayer-->recycle");
		if(mPlayer != null){
			mPlayer.onRelease();
			/*FragmentTransaction transaction = mFragmentManager.beginTransaction();
			transaction.remove(mPlayer);
			transaction.commit();*/
			/*mPlayer = null;
			mLayout.setVisibility(View.GONE);*/
		}
		mFragmentManager = null;
	}
	
	@Override
	public void setLooping(boolean looping) {
		if(mPlayer != null){
			mPlayer.setRetainInstance(true);
		}
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
	
	
	
	public class SohuPlayerListener implements ISohuTVPlayerCallback{
		@Override
		public void OnSeekCompleteListener() {
			LogUtil.d("SohuPlayer-->onSeekComplete");
			/*if (mVooleMediaPlayerListener != null) {
				mVooleMediaPlayerListener.onSeekComplete();
			}*/
			notifyOnSeekComplete();
		}

		@Override
		public void onBufferingUpdate(MediaPlayer arg0, int percent) {
			LogUtil.d("SohuPlayer-->onBufferingUpdate-->percent-->" + percent);
			/*if (mVooleMediaPlayerListener != null) {
				mVooleMediaPlayerListener.onBufferingUpdate(percent);
			}*/
			notifyOnBufferingUpdate(percent);
		}

		@Override
		public void onCompletion(MediaPlayer arg0) {
			LogUtil.d("SohuPlayer-->onCompletion");
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
		public boolean onError(MediaPlayer arg0, int what, int extra) {
			LogUtil.d("SohuPlayer-->onError-->what-->" + what + "-->extra-->" + extra);
			if (mVooleAdEventListener != null) {
				return mVooleAdEventListener.onError(what, extra);
			}
//			mCurrentStatus = Status.Error;

			/*if (mVooleMediaPlayerListener != null) {
				return mVooleMediaPlayerListener.onError(what, extra);
			}*/

			return notifyOnError(what, extra);
		}

		@Override
		public boolean onInfo(MediaPlayer arg0, int what, int extra) {
			LogUtil.d("SohuPlayer-->onInfo-->what-->" + what + "-->extra-->" + extra);
			if (mVooleAdEventListener != null) {
				mVooleAdEventListener.onInfo(what, extra);
			}

			/*if (mVooleMediaPlayerListener != null) {
				return mVooleMediaPlayerListener.onInfo(what, extra);
			}*/
			
			return notifyOnInfo(what, extra);

		}

		@Override
		public void onPrepared(MediaPlayer arg0) {
			LogUtil.d("SohuPlayer-->onPrepared");
			mCurrentStatus = Status.Prepared;
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

		@Override
		public void onVideoSizeChanged(MediaPlayer arg0, int arg1, int arg2) {
			LogUtil.d("SohuPlayer-->onVideoSizeChanged");
		}

		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
				int arg3) {
			LogUtil.d("SohuPlayer-->surfaceChanged");
		}

		@Override
		public void surfaceCreated(SurfaceHolder arg0) {
			LogUtil.d("SohuPlayer-->surfaceCreated");
//			isSurCreated = true;
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			LogUtil.d("SohuPlayer-->surfaceDestroyed");
//			isSurCreated = false;
		}
	}
	
	@Override
	public void setVolume(float leftVolume, float rightVolume) {
		if(mPlayer != null){
			mPlayer.setVolume(Math.round(leftVolume));
		}
	}


	@Override
	protected void su_originalPrepare(String playUrl) {
		// TODO Auto-generated method stub
		
	}
}
