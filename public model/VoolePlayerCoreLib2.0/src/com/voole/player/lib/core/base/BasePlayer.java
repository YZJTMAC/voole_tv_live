package com.voole.player.lib.core.base;

import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;

import com.gntv.tv.common.ap.ProxyError;
import com.gntv.tv.common.ap.ProxyManager;
import com.gntv.tv.common.utils.LogUtil;
import com.vad.sdk.core.base.AdEvent;
import com.voole.player.lib.core.VooleMediaPlayer;
import com.voole.player.lib.core.VooleMediaPlayerListener;
import com.voole.player.lib.core.interfaces.IPlayReport;
import com.voole.player.lib.core.interfaces.IPlayer;

public abstract class BasePlayer implements IPlayer{
	protected Status mCurrentStatus = Status.IDLE;
	protected Context mContext = null;
	protected VooleMediaPlayer mVooleMediaPlayer = null;
	private VooleMediaPlayerListener mVooleMediaPlayerListener = null;
	protected IPlayReport mPlayReport = null;
	
	protected boolean mIsPreview = false;
	protected int mPreviewTime = 0;
	protected String mIsLiveShow = "0";
	protected String mIsJumpPlay = "0";
	
	private int duration = 0;
	
	/*@Override
	public void initPlayer(String param, IAdUIController adUIController,
			VooleMediaPlayer vmp, Context context, VooleMediaPlayerListener l) {
		this.mContext = context;
		this.mCurrentStatus = Status.IDLE;
		this.mVooleMediaPlayer = vmp;
	}*/
	
	@Override
	public void initPlayer(VooleMediaPlayer vmp, Context context, VooleMediaPlayerListener l, IPlayReport report) {
		this.mContext = context;
		this.mCurrentStatus = Status.IDLE;
		this.mVooleMediaPlayer = vmp;
		this.mVooleMediaPlayerListener = l;
		this.mPlayReport = report;
	}
	
	@Override
	public void reset() {
		mCurrentStatus = Status.IDLE;
		/*if(mPlayReport != null){
			mPlayReport.notifyReset();
		}*/
	}
	
	@Override
	public void release() {
		mCurrentStatus = Status.IDLE;
		if(mPlayReport != null){
			mPlayReport.notifyRelease();
		}
	}
	
	@Override
	public Status getCurrentStatus() {
		return mCurrentStatus;
	}

	@Override
	public void prepare(String param, Map<String, String> extMap) {
		mCurrentStatus = Status.Preparing;
		if(mPlayReport != null){
			mPlayReport.notifyPrepare(param);
		}
	}

	@Override
	public void start() {
	}

	@Override
	public void start(int msec) {
	}

	@Override
	public void seekTo(int msec) {
		if(mPlayReport != null){
			if(mVooleMediaPlayer != null){
				mPlayReport.notifySeek(mVooleMediaPlayer.getCurrentPosition(), msec);
			}
		}
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void stop() {
		if(mPlayReport != null){
			if(mVooleMediaPlayer != null){
				LogUtil.d("BasePlayer->stop, duration = " + mVooleMediaPlayer.getDuration() + 
						", currentPosition = " + mVooleMediaPlayer.getCurrentPosition());
				//duration != 0 && currentPosition == 0，5.1的乐播盒子在正常播放完成时会出现currentPosition为0的问题
				if (duration != 0 && mVooleMediaPlayer.getCurrentPosition() == 0) {
					mPlayReport.notifyStop(duration);
				} else {
					mPlayReport.notifyStop(mVooleMediaPlayer.getCurrentPosition());
				}
			}
		}
		mCurrentStatus = Status.IDLE;
	}
	
	@Override
	public void setSurfaceHolderFixedSize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void changeVideoSize(boolean isFullScreen) {
		
	}
	
	@Override
	public boolean isFullScreen() {
		return false;
	}
	
//	@Override
//	public void setVideoSize(int width, int height) {
//		
//	}
	
	protected void notifyOnPrepared(){
		if(mPlayReport != null){
			duration = mVooleMediaPlayer.getDuration();
			mPlayReport.notifyOnPrePared(duration);
		}
		if(mVooleMediaPlayerListener != null){
			mVooleMediaPlayerListener.onPrepared(mIsPreview, mPreviewTime, mIsLiveShow, mIsJumpPlay);
		}
	}

	protected boolean notifyOnInfo(int what, int extra){
		if(mPlayReport != null){
			if(what == 701){
				mPlayReport.notifyBufferStart(mVooleMediaPlayer.getCurrentPosition());
			}else if(what == 702){
				mPlayReport.notifyBufferEnd(mVooleMediaPlayer.getCurrentPosition());
			}
		}
		if(mVooleMediaPlayerListener != null){
			return mVooleMediaPlayerListener.onInfo(what, extra);
		}
		return true;
	}

	protected boolean notifyOnError(int what, int extra){
		if(mPlayReport != null){
			mPlayReport.notifyError(mVooleMediaPlayer.getCurrentPosition());
		}
		mCurrentStatus = Status.Error;
		GetProxyErrorTask getProxyErrorTask = new GetProxyErrorTask();
		getProxyErrorTask.execute();
		return true;
	}

	protected void notifyOnCompletion(){
		if(mPlayReport != null){
			LogUtil.d("BasePlayer->notifyOnCompletion, currentPosition = " + mVooleMediaPlayer.getCurrentPosition() + 
					", duration = " + mVooleMediaPlayer.getDuration());
//			mPlayReport.notifyCompletion(mVooleMediaPlayer.getCurrentPosition());
			//duration != 0 && currentPosition == 0，4.4.4的高清播放盒在正常播放完成时会出现currentPosition为0的问题
			if (duration != 0 && mVooleMediaPlayer.getCurrentPosition() == 0) {
				mPlayReport.notifyStop(duration);
			} else {
				mPlayReport.notifyStop(mVooleMediaPlayer.getCurrentPosition());
			}
		}
		if(mVooleMediaPlayerListener != null){
			mVooleMediaPlayerListener.onCompletion();
		}
	}

	protected void notifyOnSeekComplete(){
		if(mVooleMediaPlayerListener != null){
			mVooleMediaPlayerListener.onSeekComplete();
		}
	}
	
	protected void notifyOnBufferingUpdate(int percent){
		if(mVooleMediaPlayerListener != null){
			mVooleMediaPlayerListener.onBufferingUpdate(percent);
		}
	}
	
	// ad
	protected void notifyCanSeek(boolean canSeek){
		if(mVooleMediaPlayerListener != null){
			mVooleMediaPlayerListener.canSeek(canSeek);
		}
	}
	
	protected void notifyCanExit(boolean canExit){
		if(mVooleMediaPlayerListener != null){
			mVooleMediaPlayerListener.canExit(canExit);
		}
	}

	protected void notifyOnSeek(int pos){
		if(mVooleMediaPlayerListener != null){
			mVooleMediaPlayerListener.onSeek(pos);
		}
	}
	
	protected void notifyOnAdEvent(AdEvent e){
		if(mVooleMediaPlayerListener != null){
			mVooleMediaPlayerListener.onAdEvent(e);
		}
	}
	
	protected void notifyOnExit(){
		if(mVooleMediaPlayerListener != null){
			mVooleMediaPlayerListener.onExit();
		}
	}
	
	protected void notifyOnMovieStart(){
		if(mVooleMediaPlayerListener != null){
			mVooleMediaPlayerListener.onMovieStart();
		}
	}
	
	private class GetProxyErrorTask extends AsyncTask<String, Integer, ProxyError> {  
        @Override  
        protected void onPreExecute() {
        	LogUtil.d("GetProxyErrorTask-->onPreExecute-->onPreExecute");
        }  
          
        @Override  
        protected ProxyError doInBackground(String... params) {  
        	LogUtil.d("GetProxyErrorTask-->doInBackground");
        	return ProxyManager.GetInstance().getError();
        }  
          
        @Override  
        protected void onProgressUpdate(Integer... progresses) {  
        	LogUtil.d("GetProxyErrorTask-->onProgressUpdate");
        }  
          
        @Override  
        protected void onPostExecute(ProxyError result) {  
        	LogUtil.d("GetProxyErrorTask-->onPostExecute-->onPostExecute");
        	if(mVooleMediaPlayerListener != null){
        		if(result == null){
        			mVooleMediaPlayerListener.onError(-1, 0, "0194100003", "0000000000", "");
        		}else{
        			mVooleMediaPlayerListener.onError(-1, 0, "0194100001", result.getErrorCode(), "");
        		}
    		}
        }  
          
        @Override  
        protected void onCancelled() {  
        	LogUtil.d("GetProxyErrorTask-->onCancelled");
        }  
    }
}
