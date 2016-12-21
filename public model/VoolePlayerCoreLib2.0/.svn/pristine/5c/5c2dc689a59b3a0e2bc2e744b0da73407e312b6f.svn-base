//package com.voole.player.lib.core.letv;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.Surface;
//import android.view.SurfaceHolder;
//import android.view.SurfaceHolder.Callback;
//import android.view.SurfaceView;
//import android.widget.RelativeLayout;
//
//import com.gntv.tv.common.ap.Ad;
//import com.gntv.tv.common.ap.ProxyManager;
//import com.gntv.tv.common.utils.LogUtil;
////import com.lecloud.entity.ActionInfo;
////import com.lecloud.entity.LiveInfo;
////import com.letv.controller.LetvPlayer;
////import com.letv.controller.PlayContext;
////import com.letv.universal.iplay.EventPlayProxy;
////import com.letv.universal.iplay.ISplayer;
////import com.letv.universal.iplay.OnPlayStateListener;
////import com.letv.universal.play.util.PlayerParamsHelper;
////import com.letv.universal.widget.ReSurfaceView;
//import com.voole.player.lib.core.VooleMediaPlayer;
//import com.voole.player.lib.core.VooleMediaPlayerListener;
//import com.voole.player.lib.core.ad.AdPlayer;
//import com.voole.player.lib.core.interfaces.IPlayReport;
//
//public class LePlayer extends AdPlayer{
//	private ISplayer mPlayer = null;
//	private PlayContext mPlayContext = null;
//    private SurfaceView mVideoView;
//    
//    private boolean isLive = false;
//    
//    @Override
//    public void initPlayer(VooleMediaPlayer vmp, Context context, VooleMediaPlayerListener l, IPlayReport report) {
//		LogUtil.d("LePlayer-->initPlayer");
//    	initMediaPlayer(context);
//    	super.initPlayer(vmp, context, l, report);
//    }
//    
//    @Override
//    public void release() {
//    	ProxyManager.GetInstance().stopProxyCheckTimer();
//    	if(mPlayer != null){
//			mPlayer.release();
//			LogUtil.d("LePlayer-->release");
//			mPlayer = null;
//		}
//    	super.release();
//    }
//    
//    private void initMediaPlayer(Context context){
//    	mPlayer = new LetvPlayer();
//    	mPlayContext = new PlayContext(context);
//    	mPlayer.setPlayContext(mPlayContext);
//    	mPlayer.init();
//    }
//
//	@Override
//	public void setLooping(boolean looping) {
//	}
//
//	@Override
//	public int getVideoHeight() {
//		LogUtil.d("LePlayer-->getVideoHeight");
//		if(mPlayer != null){
//			return mPlayer.getVideoHeight();
//		}
//		return 0;
//	}
//
//	@Override
//	public int getVideoWidth() {
//		LogUtil.d("LePlayer-->getVideoWidth");
//		if(mPlayer != null){
//			return mPlayer.getVideoWidth();
//		}
//		return 0;
//	}
//
//	@Override
//	public void setVolume(float leftVolume, float rightVolume) {
//		LogUtil.d("LePlayer-->setVolume");
//		if(mPlayer != null){
//			mPlayer.setVolume(leftVolume, rightVolume);
//		}
//	}
//
//	@Override
//	public void recycle() {
//	}
//
//	@Override
//	protected void su_originalSeek(int msec) {
//		LogUtil.d("LePlayer-->su_originalSeek，msec = " + msec + ", 差值 = " + (mPlayer.getDuration() - msec));
//		if(mPlayer != null && !isLive){
//			mPlayer.seekTo(msec);
//		}
//	}
//
//	@Override
//	protected void su_originalStop() {
//		LogUtil.d("LePlayer-->su_originalStop");
//		if(mPlayer != null){
//			mPlayer.stop();
//		}
//	}
//
//	@Override
//	protected void su_originalInit(Context context) {
//		LogUtil.d("LePlayer-->su_originalInit");
//		if(mPlayer != null){
//			mPlayer.setOnPlayStateListener(new LePlayerListener());
//		}
//	}
//	
//	@Override
//	protected void su_originalPrepare(final Ad ad) {
//		LogUtil.d("LePlayer-->su_originalPrepare--");
//		if(mVideoView == null){
//			LogUtil.d("LePlayer-->su_originalPrepare-->mVideoView == null");
//			mVideoView = new ReSurfaceView(mContext);
//			mVideoView.getHolder().addCallback(new Callback() {
//				@Override
//				public void surfaceDestroyed(SurfaceHolder holder) {
//					LogUtil.d("LePlayer-->surfaceDestroyed" );
//				}
//				
//				@Override
//				public void surfaceCreated(SurfaceHolder holder) {
//					LogUtil.d("LePlayer-->surfaceCreated" );
//					/*if (mPlayer != null) {
//						mPlayer.setDisplay(mVideoView.getHolder().getSurface());
//		            } */
//					if (mPlayer != null) {
//						mPlayer.setDisplay(holder.getSurface());
//					}
//					doPrepare(ad, holder.getSurface());
//				}
//				
//				@Override
//				public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//					LogUtil.d("LePlayer-->surfaceChanged" );
//					if (mPlayer != null) {
//		                PlayerParamsHelper.setViewSizeChange(mPlayer, width, height);
//		            }
//				}
//			});
//			mVooleMediaPlayer.addView(mVideoView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
//		}else{
//			LogUtil.d("LePlayer-->su_originalPrepare-->mSurfaceView != null");
//			doPrepare(ad, null);
//		}
//	}
//	//activityId, true, P, UUID
//	private void doPrepare(Ad ad, Surface surface){
//		Bundle bundle = null;
//		LogUtil.d("LePlayer:isLive? " + ad.getIsLiveShow());
//		if(mPlayer != null && mPlayContext != null){
//			if ("0".equals(ad.getIsLiveShow())) {
//				// dian bo
//				bundle = LetvParamsUtils.setVodParams(ad.getLeuu(), ad.getLefid(), "", "", "", ad.getLep());
//			}else if ("2".equals(ad.getIsLiveShow())) {
//				// live 
//				isLive = true;
//				bundle = LetvParamsUtils.setActionLiveParams(ad.getLefid(), false, ad.getLep(), ad.getLeuu());
//			}else {
//				// dian bo
//				bundle = LetvParamsUtils.setVodParams(ad.getLeuu(), ad.getLefid(), "", "", "", ad.getLep());
//			}
//			String path = bundle.getString("path");
//			LogUtil.d("->bundle的内容是：" + bundle.toString());
//			mPlayContext.setVideoContentView(mVideoView);
//			mPlayContext.setUsePlayerProxy(TextUtils.isEmpty(path));
//	        if (bundle.getBoolean(PlayContext.MEIZI_SAAS, false)) {
//	        	mPlayContext.setMeizi(PlayContext.MEIZI_SAAS);
//			}
//	        mPlayer.setParameter(mPlayer.getPlayerId(), bundle);
//	        LogUtil.d("LePlayer-->doPrepare-->playerId = " + mPlayer.getPlayerId() + ", bundle = " + bundle.toString());
//			if (surface != null) {
//				mPlayer.setDisplay(surface);
//			}
//	        mPlayer.setDataSource(path);
//	        if("2".equals(ad.getIsLiveShow())) {
//	        	//设置直播为原画码率的视频，不卡顿
//		        mPlayer.playedByDefination(255);
//		        LogUtil.d("直播视频，码率设置为原画");
//	        }
//	        mPlayer.prepareAsync();
//		}
//		
//	}
//
//	@Override
//	protected void su_originalStart() {
//		LogUtil.d("LePlayer-->su_originalStart--");
//		if(mPlayer != null){
//			mPlayer.start();
//			LogUtil.d("width = " + mPlayer.getVideoWidth() + ", height = " + mPlayer.getVideoHeight());
//		}
//	}
//
//	@Override
//	protected void su_originalPause() {
//		LogUtil.d("LePlayer-->su_originalPause--");
//		if(mPlayer != null){
//			mPlayer.pause();
//		}
//	}
//
//	@Override
//	protected void su_originalReset() {
//		LogUtil.d("LePlayer-->su_originalReset--");
//		if(mPlayer != null){
//			mPlayer.reset();
//		}
//	}
//
//	@Override
//	protected int su_originalGetDuration() {
//		LogUtil.d("LePlayer-->su_originalGetDuration--");
//		if(mPlayer != null && !isLive){
//			LogUtil.d("duration = " + mPlayer.getDuration());
//			return (int)mPlayer.getDuration();
//		}
//		return 0;
//	}
//
//	@Override
//	protected int su_originalGetCurrentPosition() {
//		LogUtil.d("LePlayer-->su_originalGetCurrentPosition--");
//		if(mPlayer != null && !isLive){
//			LogUtil.d("currentPosition = " + mPlayer.getCurrentPosition());
//			return (int)mPlayer.getCurrentPosition();
//		}
//		return 0;
//	}
//
//	@Override
//	protected boolean isLive() {
//		return false;
//	}
//	
//	public class LePlayerListener implements OnPlayStateListener{
//
//		@Override
//		public void videoState(int state, Bundle bundle) {
//			handlePlayerEvent(state, bundle);
//			handleLiveEvent(state, bundle);
//		}
//		
//	}
//	
//	private void handlePlayerEvent(int state, Bundle bundle) {
//		switch (state) {
//		case ISplayer.MEDIA_EVENT_VIDEO_SIZE:
//			if (mVideoView != null && mPlayer != null) {
//				/**
//				 * 获取到视频的宽高的时候，此时可以通过视频的宽高计算出比例，进而设置视频view的显示大小。
//				 * 如果不按照视频的比例进行显示的话，(以surfaceView为例子)内容会填充整个surfaceView。
//				 * 意味着你的surfaceView显示的内容有可能是拉伸的
//				 */
//				if (mVideoView instanceof ReSurfaceView) {
//					((ReSurfaceView) mVideoView).onVideoSizeChange(
//							mPlayer.getVideoWidth(), mPlayer.getVideoHeight());
//				}
//
//				/**
//				 * 获取宽高的另外一种方式
//				 */
//				bundle.getInt("width");
//				bundle.getInt("height");
//			}
//			break;
//
//		case ISplayer.MEDIA_EVENT_PREPARE_COMPLETE:// 播放器准备完成，此刻调用start()就可以进行播放了
//			LogUtil.d("LePlayer-->onPrepared, mPreviewTime = " + mPreviewTime + ", currentPosition = " + mPlayer.getCurrentPosition()
//					+ ", duration = " + mPlayer.getDuration());
//			mCurrentStatus = Status.Prepared;
//			Runnable appTask_prepared = new Runnable() {
//				@Override
//				public void run() {
//					notifyOnPrepared();
//				}
//			};
//
//			if (mVooleAdEventListener != null) {
//				mVooleAdEventListener.onPrepared(appTask_prepared);
//			} else {
//				appTask_prepared.run();
//			}
//			break;
//
//		case ISplayer.MEDIA_EVENT_FIRST_RENDER:// 视频第一帧数据绘制
//			break;
//		case ISplayer.MEDIA_EVENT_PLAY_COMPLETE:// 视频播放完成
//			LogUtil.d("LePlayer-->onCompletionm, PreviewTime = " + mPreviewTime + ", currentPosition = " + mPlayer.getCurrentPosition()
//					+ ", duration = " + mPlayer.getDuration());
//			Runnable appTask_completion = new Runnable() {
//				@Override
//				public void run() {
//					/*
//					 * if (mVooleMediaPlayerListener != null) {
//					 * mVooleMediaPlayerListener.onCompletion(); }
//					 */
//					notifyOnCompletion();
//					mPreviewTime = 0;
//				}
//			};
//
//			if (mVooleAdEventListener != null) {
//				mVooleAdEventListener.onCompletion(appTask_completion);
//			} else {
//				appTask_completion.run();
//				LogUtil.d("LePlayer-->onCompletionm->run, PreviewTime = " + mPreviewTime + ", currentPosition = " + mPlayer.getCurrentPosition()
//						+ ", duration = " + mPlayer.getDuration());
//			}
//			break;
//		case ISplayer.MEDIA_EVENT_BUFFER_START:// 开始缓冲
//			LogUtil.d("LePlayer-->onInfo-->what-->"
//					+ ISplayer.MEDIA_EVENT_BUFFER_START);
//			if (mVooleAdEventListener != null) {
//				mVooleAdEventListener.onInfo(701, 701);
//			}
//			notifyOnInfo(701, 701);
//			break;
//		case ISplayer.MEDIA_EVENT_BUFFER_END:// 缓冲结束
//			LogUtil.d("LePlayer-->onInfo-->what-->"
//					+ ISplayer.MEDIA_EVENT_BUFFER_END);
//			if (mVooleAdEventListener != null) {
//				mVooleAdEventListener.onInfo(702, 702);
//			}
//			notifyOnInfo(702, 702);
//			break;
//		case ISplayer.MEDIA_EVENT_SEEK_COMPLETE:// seek完成
//			LogUtil.d("LePlayer-->onSeekComplete");
//			notifyOnSeekComplete();
//			break;
//		case ISplayer.MEDIA_ERROR_DECODE_ERROR:// 解码错误
//		case ISplayer.MEDIA_ERROR_NO_STREAM:// 播放器尝试连接媒体服务器失败
//		case ISplayer.PLAYER_PROXY_ERROR:
//			int errorCode = bundle.getInt("errorCode");
//			String errorMsg = bundle.getString("errorMsg");
//			if (errorCode == EventPlayProxy.CDE_ERROR_OVERLOAD_PROTECTION) {// 过载保护处理
//			}
//			LogUtil.d("LePlayer-->onError-->what-->" + errorCode
//					+ "-->extra-->" + errorMsg);
//			if (mVooleAdEventListener != null) {
//				mVooleAdEventListener.onError(errorCode, errorCode);
//			}
//			mCurrentStatus = Status.Error;
//			notifyOnError(errorCode, errorCode);
//			break;
//		
//		/*case ISplayer.PLAYER_EVENT_PREPARE_VIDEO_VIEW:
//			boolean pano = bundle != null ? bundle.getBoolean("pano", false)
//					: false;
//			if (pano || mBundle.getBoolean(LetvParamsUtils.IS_LOCAL_PANO)) {
//				initPanoVideoView();
//			} else {
//				initNormalVideoView();
//			}
//			playContext.setVideoContentView(videoView);
//			break;*/
//		 
//		default:
//			break;
//		}
//	}
//	
//	private void handleLiveEvent(int state, Bundle bundle) {
//        switch (state) {
//            case EventPlayProxy.PROXY_SET_ACTION_LIVE_CURRENT_LIVE_ID:// 获取当前活动直播的id
//                String liveId = bundle.getString("liveId");
//                LogUtil.d("handleLiveEvent, state = " + state + ", 获取活动id:" + liveId);
//                break;
//            case EventPlayProxy.PROXY_WATING_SELECT_ACTION_LIVE_PLAY:// 当收到该事件后，用户可以选择优先播放的活动直播           
//            	ActionInfo actionInfo = mPlayContext.getActionInfo();
//            	LiveInfo liveInfo = actionInfo.getFirstCanPlayLiveInfo();
//            	LogUtil.d("handleLiveEvent, state = " + state + ", 选择播放活动直播，liveInfo：" + liveInfo.toString());
//                if (liveInfo != null) {
//                    mPlayContext.setLiveId(liveInfo.getLiveId());
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//}
