package com.gntv.tv.view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.gntv.tv.R;
import com.gntv.tv.common.ap.ProxyManager;
import com.gntv.tv.common.utils.DeviceUtil;
import com.gntv.tv.common.utils.LocalManager;
import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.download.Download;
import com.gntv.tv.model.channel.AssortItem;
import com.gntv.tv.model.channel.ChannelItem;
import com.gntv.tv.model.channel.ProgramItem;
import com.gntv.tv.model.channel.ProgramManager;
import com.gntv.tv.model.channel.ProgramManager.UpdateChannel;
import com.gntv.tv.model.channel.ResourceItem;
import com.gntv.tv.model.channel.ResourceManager;
import com.gntv.tv.model.channel.TodayProgramInfo;
import com.gntv.tv.model.error.ErrorInfo;
import com.gntv.tv.model.error.ErrorManager;
import com.gntv.tv.model.time.TimeManager;
import com.gntv.tv.model.time.TimeManager.IKTListener;
import com.gntv.tv.report.PageActionReport;
import com.gntv.tv.report.PageActionReport.Action;
import com.gntv.tv.report.PageActionReport.FocusType;
import com.gntv.tv.report.PageActionReport.ModuleType;
import com.gntv.tv.report.PageActionReport.PageType;
import com.gntv.tv.view.base.DisplayManager;
import com.gntv.tv.view.base.TVAlertDialog;
import com.gntv.tv.view.base.TVToast;
import com.gntv.tv.view.base.ViewConst;
import com.vad.sdk.core.base.AdEvent;
import com.voole.player.lib.core.VooleMediaPlayer;
import com.voole.player.lib.core.VooleMediaPlayerListener;
import com.voole.player.lib.core.interfaces.IPlayer.Status;

public class PlayerView extends RelativeLayout {
	private static final String CHANNEL_ID = "CHANNEL_ID";
	private static final String CHANNEL_NAME = "CHANNEL_NAME";
	private static final String CHANNEL_NO = "CHANNEL_NO";
	private static final String CHANNEL_TYPE = "CHANNEL_TYPE";
	private static final String RESOURCE_ID = "RESOURCE_ID";
	private static final String RESOURCE_IS3RD = "RESOURCE_IS3RD";
	private static final String RESOURCE_TRACKER = "RESOURCE_TRACKER";
	private static final String RESOURCE_BKEURL = "RESOURCE_BKEURL";
	private static final String RESOURCE_DATATYPE = "RESOURCE_DATATYPE";
	private static final String RESOURCE_PROTO = "RESOURCE_PROTO";
	
	private static final String CHANNEL_SORT_NAME = "CHANNEL_SORT_NAME";
	
	private static final String FIRST_CHANNEL_ID = "FIRST_CHANNEL_ID";
	private static final String FIRST_CHANNEL_NAME = "FIRST_CHANNEL_NAME";
	private static final String FIRST_CHANNEL_NO = "FIRST_CHANNEL_NO";
	private static final String FIRST_CHANNEL_TYPE = "FIRST_CHANNEL_TYPE";
	private static final String FIRST_RESOURCE_ID = "FIRST_RESOURCE_ID";
	private static final String FIRST_RESOURCE_IS3RD = "FIRST_RESOURCEIS3RD";
	private static final String FIRST_RESOURCE_TRACKER = "FIRST_RESOURCE_TRACKER";
	private static final String FIRST_RESOURCE_BKEURL = "FIRST_RESOURCE_BKEURL";
	private static final String FIRST_RESOURCE_DATATYPE = "FIRST_RESOURCE_DATATYPE";
	private static final String FIRST_RESOURCE_PROTO = "FIRST_RESOURCE_PROTO";
	
	private static final String ILLEGAL_CODE = "0402030130";
	private static final String EURO = "euro"; //oemtype为euro
	private static final String STANDARD = "Standard";  //oemtype为Standard
	private static final int SPEED_SUCCESS = 6;
	private static final int START_PLAY = 7;
	private static final int PAUSE_PLAY = 8;
	public static long startTime = 0;
	
	
	private String oemType = STANDARD;
	private NumChangeChannelView numChangeChannelView = null;
	private ErrorView errorView = null;
	private ProgramInfoView programInfoView = null;
	private ChannelView channelView = null;
	public ChannelView getChannelView() {
		return channelView;
	}

	private BackPlayView backPlayView = null;
	private Context context = null;
	private String showNum = "";
	private Activity activity = null;
	private String aspectRatio = "0";
	private boolean isFirstStartUp = false; //是不是首次安装启动
	//private String contactInfo = "可加入QQ群反馈问题：469218743";
	private VooleMediaPlayer vooleMediaPlayer = null;
	public VooleMediaPlayer getVooleMediaPlayer() {
		return vooleMediaPlayer;
	}

	private TVBufferView bufferView = null;
	private TodayProgramInfo todayProgramInfo = null;
	private ChannelItem curChannelItem = null;
	private boolean isBufferShow = true;

	private Timer downspeedTimer;
	private long mRxBytes;
	private int DOWNSPEED_TIME_INTERVAL = 1000;
	private String downspeed;
	private boolean isStart = false;
	private TVAlertDialog backplayDialog = null;
	//private String actions = null;
	private String apkPacks = null;
	private String downloadUrl = null;
	private boolean isBackError = false;
	//public boolean isSeeking = false;
	private boolean isSeekComplete = true;
	private List<AssortItem> assortItemList = null;
	private static final int SPANTIME = 60*1000;
	private LinkedList<Long> timelist = new LinkedList<Long>();
	private boolean mCanExit = true;
	private boolean isLongPressKey = false;//是否长按 

	@SuppressLint("HandlerLeak")
	private Handler viewHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SPEED_SUCCESS:
				LogUtil.i("PlayerView----->mHandler--->SPEED--->" + downspeed);
				if (isBufferShow) {
					showBufferView(downspeed);
				}
				break;
			case START_PLAY:
				LogUtil.i("PlayerView----->mHandler--->START_PLAY");
				Integer seekTime = (Integer) msg.obj;
				LogUtil.i("PlayerView----->seekTime::"+seekTime);
				hideBufferView();
				//changeAspectRatio(aspectRatio);
				if(EURO.equalsIgnoreCase(oemType)){
					vooleMediaPlayer.setSurfaceHolderFixedSize(1920,1080);
				}
				vooleMediaPlayer.start();
				programInfoView.cancelShowTimer(true);
				long subTime = System.currentTimeMillis() - startTime;
				LogUtil.i("切台结束：：时间差为："+subTime);
				if(ViewConst.BACK_STATE == channelView.playState){
					backPlayView.setTotalTime(vooleMediaPlayer.getDuration());
					if(seekTime>0){
						vooleMediaPlayer.seekTo(seekTime*1000);
						backPlayView.setSeek(seekTime);
					}
				}
				hideErrorPage();
				saveNewChannelInfo();
				break;
			case PAUSE_PLAY:
				LogUtil.i("PlayerView----->mHandler--->PAUSE_PLAY");
				if(vooleMediaPlayer.getCurrentStatus() == Status.Playing){
					vooleMediaPlayer.pause();
				}
				break;
			default:
				break;
			}

		};
	};

	public PlayerView(Activity activity,String code,String appid,String isShowTimer,String oemType) {
		super(activity);
		init(getContext(),code,appid,isShowTimer);
		if(!TextUtils.isEmpty(oemType)){
			this.oemType = oemType;
		}
		this.activity = activity;
	}

	public PlayerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context,null,null,null);
	}

	public PlayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,null,null,null);
	}

	private void init(Context context,String code,String appid,String isShowTimer) {
		setFocusable(true);
		setFocusableInTouchMode(true);
		setClickable(true);
		//setBackgroundColor(0xaaff0000);
		this.context = context;
		initMeidaPlayerView(context,code,appid);
		initErrorView(context);
		initNumView(context);
		initBufferView(context);
		initProgramView(context,isShowTimer);
		initBackPlayView(context);
		initChannelView(context);
		initPlayState(context);
	}

	private void initMeidaPlayerView(Context context,String code,String appid) {
		vooleMediaPlayer = new VooleMediaPlayer(context);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		vooleMediaPlayer.setLayoutParams(params);
		vooleMediaPlayer.setApkVersionCode(code);
		vooleMediaPlayer.setAppid(appid);
		addView(vooleMediaPlayer);
		initMediaPlayer();
	}

	private void initErrorView(Context context) {
		errorView = new ErrorView(context);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		errorView.setLayoutParams(params);
		errorView.setVisibility(View.GONE);
		addView(errorView);
	}

	private synchronized void initMediaPlayer() {
		vooleMediaPlayer.setMediaPlayerListener(new VooleMediaPlayerListener() {

			@Override
			public void onSeekComplete() {
				LogUtil.i("PlayerView-->onSeekComplete");
				isSeekComplete = true;
				hideErrorPage();
			}

			@Override
			public void onSeek(int pos) {
				LogUtil.i("PlayerView-->onSeek");
			}

			@Override
			public synchronized void onPrepared(boolean isPreview, final int previewTime,
					String isLiveShow, String mIsJumpPlay) {
				LogUtil.i("PlayerView------>vooleMediaPlayer----->onPrepare");
				LogUtil.i("PlayerView------>vooleMediaPlayer----->previewTime::"+previewTime);
				isSeekComplete = true;
				if(!isStart){
					new Thread(new Runnable() {
						@Override
						public void run() {
							while (!isStart) {
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									LogUtil.e("PlayerView------>vooleMediaPlayer----->onPrepare--->"
											+ e.getMessage());
								}
							}
							Message msg = Message.obtain();
							msg.what = START_PLAY;
							msg.obj = previewTime;
							viewHandler.sendMessage(msg);
						}
					}).start();
				}else{
					Message msg = Message.obtain();
					msg.what = START_PLAY;
					msg.obj = previewTime;
					viewHandler.sendMessage(msg);
				}
				isBackError = false;
				// hideProgramInfoView();
			}

			@Override
			public boolean onInfo(int what, int extra) {
				if (isStart) {
					//LogUtil.i("PlayerView------>onInfo--->isStart::"+isStart);
					if (what == 701 && programInfoView.getVisibility()!=VISIBLE) {
						LogUtil.i("PlayerView------>onInfo--->what::"+what);
						showBufferView();
						//如果当前播放频道是直播状态，记录播放缓冲次数进行容错处理
						if(ViewConst.LIVE_STATE == channelView.playState){
							//进行容错处理
							if(timelist.size()>=10){
								Long t2 = timelist.get(9);
								Long t1 = timelist.pollFirst();
								Long subTime = t2 - t1;
								LogUtil.i("PlayerView------>onInfo--->t1::"+t1+" ,t2::"+t2+" ,size::"+timelist.size());
								if(subTime<=SPANTIME){
									curChannelItem = getCurrentChannel();
									if(curChannelItem!=null){
										String tvId = curChannelItem.getChannelId();
										LogUtil.i("PlayerView------>onInfo--->直播频繁缓冲进行重试");
										if(curChannelItem.getCurrentResourceItem()!=null){
											ResourceItem resourceItem = curChannelItem.getCurrentResourceItem();
											vooleMediaPlayer.reset();
											vooleMediaPlayer.prepareLive(tvId,getChannelType(), resourceItem.getResourceId(), resourceItem.getIs3rd(), resourceItem.getTracker(), resourceItem.getBkeUrl()
													,resourceItem.getDataType(), resourceItem.getProto());
											timelist.clear();
										}
									}
								}
							}
							timelist.addLast(System.currentTimeMillis());
						}
						
					} else if (what == 702) {
						LogUtil.i("PlayerView------>onInfo--->what::"+what);
						hideBufferView();
					}
				}

				return true;
			}

			@Override
			public void onExit() {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean onError(int what, int extra, String errorCode, String errorCodeOther, String errorMsgOther) {
				LogUtil.i("PlayerView------>vooleMediaPlayer--->onerror--->errorCodeOther::"+errorCodeOther);
				
				/*ErrorInfo errorInfo = ErrorManager.GetInstance().getErrorMsg(errorCode,errorCodeOther,errorMsgOther);
				showErrorPage(errorInfo);
				if(channelView.playState == BACK_STATE){
					isBackError = true;
				}*/
				curChannelItem = getCurrentChannel();
				if(curChannelItem!=null){
					String tvId = curChannelItem.getChannelId();
					String tvName = curChannelItem.getTitle();
					if(channelView.playState == ViewConst.BACK_STATE){
						isBackError = true;
						ProgramItem  programItem = channelView.getPlayingProgram();
						long stime = programItem.getlStartTime();
						long etime = programItem.getlStopTime();
						backPlayView.release();
						changeChannelPlayBack(tvId, tvName, stime, etime);
					}else{
						LogUtil.i("PlayerView------>vooleMediaPlayer--->直播报播放出错进行重试");
						
						if(ILLEGAL_CODE.equals(errorCode)){
							String channelId = LocalManager.GetInstance().getLocal(FIRST_CHANNEL_ID, null);
							String channelType = LocalManager.GetInstance().getLocal(FIRST_CHANNEL_TYPE, null);
							String resourceId = LocalManager.GetInstance().getLocal(FIRST_RESOURCE_ID, null);
							String is3rd = LocalManager.GetInstance().getLocal(FIRST_RESOURCE_IS3RD, null);
							String tracker = LocalManager.GetInstance().getLocal(FIRST_RESOURCE_TRACKER, null);
							String bkeUrl = LocalManager.GetInstance().getLocal(FIRST_RESOURCE_BKEURL, null);
							String datatype = LocalManager.GetInstance().getLocal(FIRST_RESOURCE_DATATYPE, null);
							String proto = LocalManager.GetInstance().getLocal(FIRST_RESOURCE_PROTO, null);
							//curChannelItem = getPlayChannel(channelId, resourceId, is3rd, tracker, bkeUrl, datatype, proto);
							if(channelId!=null&&channelType!=null&&resourceId!=null&&is3rd!=null&&tracker!=null&&bkeUrl!=null&&datatype!=null&&proto!=null){
								setData(todayProgramInfo, channelId, false);
								vooleMediaPlayer.reset();
								vooleMediaPlayer.prepareLive(channelId,channelType,resourceId,is3rd, tracker,bkeUrl
										,datatype, proto);
							}else{
								ErrorInfo errorInfo = ErrorManager.GetInstance().getErrorMsg(errorCode,errorCodeOther,errorMsgOther);
								showErrorPage(errorInfo);
							}
						}else{
							if(curChannelItem.getCurrentResourceItem()!=null){
								ResourceItem resourceItem = curChannelItem.getCurrentResourceItem();
								vooleMediaPlayer.reset();
								vooleMediaPlayer.prepareLive(tvId,getChannelType(), resourceItem.getResourceId(), resourceItem.getIs3rd(), resourceItem.getTracker(), resourceItem.getBkeUrl()
										,resourceItem.getDataType(), resourceItem.getProto());
							}
						}
					}
					
					
				}else{
					ErrorInfo errorInfo = ErrorManager.GetInstance().getErrorMsg(errorCode,errorCodeOther,errorMsgOther);
					showErrorPage(errorInfo);
				}
				
				return true;
			}

			@Override
			public void onCompletion() {
				LogUtil.i("PlayerView------>vooleMediaPlayer----->onCompletion");
				if (channelView.playState == ViewConst.BACK_STATE &&channelView.getVisibility()!=VISIBLE) {
					showBackPlayCompleteDialog(false);
					hideBackPlayView();
					isBackError = true;
				}else{
					//ErrorInfo errorInfo = ErrorManager.GetInstance().getErrorMsg(ErrorManager.ERROR_LIVE_COMPLETION);
					//showErrorPage(errorInfo);
					LogUtil.i("PlayerView------>vooleMediaPlayer--->直播报播放完成进行重试");
					String errorCode = ProxyManager.GetInstance().getError().getErrorCode();
					LogUtil.i("PlayerView------>vooleMediaPlayer--->onCompletion--->errorCode::"+errorCode);
					curChannelItem = getCurrentChannel();
					if(curChannelItem!=null){
						if(ILLEGAL_CODE.equals(errorCode)){
							String channelId = LocalManager.GetInstance().getLocal(FIRST_CHANNEL_ID, null);
							String channelType = LocalManager.GetInstance().getLocal(FIRST_CHANNEL_TYPE, null);
							String resourceId = LocalManager.GetInstance().getLocal(FIRST_RESOURCE_ID, null);
							String is3rd = LocalManager.GetInstance().getLocal(FIRST_RESOURCE_IS3RD, null);
							String tracker = LocalManager.GetInstance().getLocal(FIRST_RESOURCE_TRACKER, null);
							String bkeUrl = LocalManager.GetInstance().getLocal(FIRST_RESOURCE_BKEURL, null);
							String datatype = LocalManager.GetInstance().getLocal(FIRST_RESOURCE_DATATYPE, null);
							String proto = LocalManager.GetInstance().getLocal(FIRST_RESOURCE_PROTO, null);
							//curChannelItem = getPlayChannel(channelId, resourceId, is3rd, tracker, bkeUrl, datatype, proto);
							if(channelId!=null&&channelType!=null&&resourceId!=null&&is3rd!=null&&tracker!=null&&bkeUrl!=null&&datatype!=null&&proto!=null){
								setData(todayProgramInfo, channelId, false);
								vooleMediaPlayer.reset();
								vooleMediaPlayer.prepareLive(channelId,channelType,resourceId,is3rd, tracker,bkeUrl
										,datatype, proto);
							}else{
								ErrorInfo errorInfo = ErrorManager.GetInstance().getErrorMsg(ErrorManager.ERROR_LIVE_COMPLETION);
								showErrorPage(errorInfo);
							}
						}else{
							if(curChannelItem.getCurrentResourceItem()!=null){
								ResourceItem resourceItem = curChannelItem.getCurrentResourceItem();
								vooleMediaPlayer.reset();
								vooleMediaPlayer.prepareLive(curChannelItem.getChannelId(),getChannelType(), resourceItem.getResourceId(), resourceItem.getIs3rd(), resourceItem.getTracker(), resourceItem.getBkeUrl()
										,resourceItem.getDataType(), resourceItem.getProto());
							}
						}
					}else{
						ErrorInfo errorInfo = ErrorManager.GetInstance().getErrorMsg(ErrorManager.ERROR_LIVE_COMPLETION);
						showErrorPage(errorInfo);
					}
				}
			}

			@Override
			public void onBufferingUpdate(int percent) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAdEvent(AdEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void canSeek(boolean canSeek) {
				// TODO Auto-generated method stub

			}

			@Override
			public void canExit(boolean canExit) {
				mCanExit = canExit;
			}

			@Override
			public void onMovieStart() {
				// TODO Auto-generated method stub
				
			}

		});
	}

	private void initNumView(Context context) {
		numChangeChannelView = new NumChangeChannelView(context);
		RelativeLayout.LayoutParams param_num = new RelativeLayout.LayoutParams(
				changeWidth(200), changeHeight(90));
		param_num.addRule(RelativeLayout.ALIGN_PARENT_TOP
				| RelativeLayout.ALIGN_PARENT_RIGHT);
		param_num.setMargins(0, changeHeight(20), changeWidth(15), 0);
		numChangeChannelView.setLayoutParams(param_num);
		numChangeChannelView.setVisibility(View.GONE);
		numChangeChannelView
				.setiNumChangeChannelViewListener(new INumChangeChannelViewListener() {

					@Override
					public void onTimeOut() {
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								numChangeChannelView.hide();
							}
						});
					}

					@Override
					public void onChannelChange(final ChannelItem item) {
						if(item!=null){
							if(!item.getChannelId().equals(getCurrentChannel().getChannelId())){
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										hideErrorPage();
										hideProgramInfoView();
										showProgramInfoView(item);
									}
								});

								channelView.numChangeChannel(item);
								PageActionReport.GetInstance().reportPageAction(PageType.PlayPage, null,
										item.getChannelId(), FocusType.NumKeySwitch, null);
							}
							
						}
						
					}

					@Override
					public ChannelItem isChannelAvailable(String num) {
						return findChannelByChannelNo(num);
					}

					@Override
					public void clearNum() {
						showNum = "";
					}
				});
		addView(numChangeChannelView);
	}

	private void initProgramView(Context context,String isShowTimer) {
		programInfoView = new ProgramInfoView(context,this,isShowTimer);
		programInfoView.setVisibility(View.GONE);
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
				DisplayManager.GetInstance().changeWidthSize(600),
				DisplayManager.GetInstance().changeHeightSize(130));
		param.addRule(RelativeLayout.CENTER_IN_PARENT);
		param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		param.bottomMargin = DisplayManager.GetInstance()
				.changeHeightSize(60);
		programInfoView.setLayoutParams(param);
		programInfoView
				.setiProgramInfoViewListener(new IProgramInfoViewListener() {
					@Override
					public void onTimeOut() {
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								programInfoView.hide();
								try {
									if (vooleMediaPlayer.getCurrentStatus() != Status.Playing&&isStart) {
										LogUtil.i("PlayerView--->programInfoView--->onTimeOut");
										showBufferView();
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					}
				});
		//addView(programInfoView);
	}

	private void initBackPlayView(Context contex) {
		backPlayView = new BackPlayView(contex, this);
		backPlayView.setVisibility(GONE);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		backPlayView.setLayoutParams(params);
		backPlayView.setiBackPlayListenner(new IBackPlayListenner() {

			@Override
			public void onTimeOut() {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						backPlayView.hide();
						try {
							if (vooleMediaPlayer.getCurrentStatus() != Status.Playing&&isStart&&channelView.playState == ViewConst.BACK_STATE) {
								LogUtil.i("PlayerView--->backPlayView--->onTimeOut");
								showBufferView();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

			}

			@Override
			public void seekTo(long msec) {
				LogUtil.i("PlayerView--->seekTo");
				if (channelView.playState == ViewConst.BACK_STATE) {
					isSeekComplete = false;
					vooleMediaPlayer.seekTo((int) msec);
				}else{
					ChannelItem item = getCurrentChannel();
					changeChannelTimeShift(item.getChannelId(), item.getCurrentResourceItem(), msec/1000+"");
				}
			}

			@Override
			public void onPause() {
				if (channelView.playState == ViewConst.BACK_STATE||channelView.playState == ViewConst.TIMESHIFT_STATE) {
					hideBufferView();
					LogUtil.i("PlayerView--->pausePlay--->status::"+vooleMediaPlayer.getCurrentStatus());
					new Thread(new Runnable() {
						@Override
						public void run() {
							//移动完成播放出来进行暂停
							while (!isSeekComplete) {
								try {
									Thread.sleep(600);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							Message msg = Message.obtain();
							msg.what = PAUSE_PLAY;
							viewHandler.sendMessage(msg);
							
						}
					}).start();
				}
			}

			@Override
			public void onResume(long stime) {
				try {
					LogUtil.i("PlayerView--->resumePlay");
					if (channelView.playState == ViewConst.BACK_STATE) {
						isBackError = false;
						//tv_backplay.setVisibility(VISIBLE);
						LogUtil.i("PlayerView--->resumePlay--->status::"+vooleMediaPlayer.getCurrentStatus());
						if(vooleMediaPlayer.getCurrentStatus() == Status.Prepared
								||vooleMediaPlayer.getCurrentStatus()==Status.Pause){
							vooleMediaPlayer.start();
						}
						
					}else if (channelView.playState == ViewConst.TIMESHIFT_STATE) {
						if(vooleMediaPlayer.getCurrentStatus() == Status.Prepared
								||vooleMediaPlayer.getCurrentStatus()==Status.Pause){
							ChannelItem item = getCurrentChannel();
							changeChannelTimeShift(item.getChannelId(), item.getCurrentResourceItem(), stime/1000+"");
						}
					}
				} catch (Exception e) {
					LogUtil.e("PlayerView---->resumePlay--->"+e.toString());
				}
			}

			@Override
			public void onBackToLive() {
				backToLiveFromShift();
			}
		});
		addView(backPlayView);
	}

	private void initPlayState(Context context) {
	/*	iv_pause = new ImageView(context);
		iv_pause.setBackgroundResource(R.drawable.cs_back_pause);
		LayoutParams iv_params = new LayoutParams(changeWidth(80),
				changeHeight(80));
		iv_params.bottomMargin = changeHeight(20);
		iv_params.rightMargin = changeWidth(20);
		iv_params.addRule(ALIGN_PARENT_BOTTOM);
		iv_params.addRule(ALIGN_PARENT_RIGHT);
		iv_pause.setLayoutParams(iv_params);
		iv_pause.setVisibility(GONE);
		addView(iv_pause);*/

	/*	tv_backplay = new TextView(context);
		tv_backplay.setBackgroundResource(R.drawable.cs_back_bottom);
		tv_backplay.setGravity(Gravity.CENTER);
		tv_backplay.setTextSize(TypedValue.COMPLEX_UNIT_PX,getTextSize(R.dimen.BACK_SIZE));
		tv_backplay.setText("回看模式");
		tv_backplay.setTextColor(getColor(R.color.light_white));
		LayoutParams tv_params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				changeHeight(50));
		tv_params.bottomMargin = changeHeight(20);
		tv_params.addRule(ALIGN_PARENT_BOTTOM);
		tv_params.addRule(ALIGN_PARENT_RIGHT);
		tv_backplay.setLayoutParams(tv_params);
		tv_backplay.setVisibility(GONE);
		addView(tv_backplay);*/
	}

	private void initChannelView(Context context) {
		channelView = new ChannelView(context, this);
		channelView.setVisibility(View.GONE);
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		channelView.setLayoutParams(param);
		channelView.setIChannelViewListener(new IChannelViewListener() {

			@Override
			public void onTimeOut() {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						channelView.hide();
					}
				});
			}

			@Override
			public void onChannelClick(ChannelItem channelItem) {
				if (channelItem != null) {
					LogUtil.i("PlayerView--------->onChannelClick----->tvid::"
							+ channelItem.getChannelId() + "--tvname::"
							+ channelItem.getTitle());
					changeChannel(channelItem.getChannelId(),
							channelItem.getTitle(),
							channelItem.getCurrentResourceItem());
				}
			}

			@Override
			public void onProgramClick(ChannelItem channelItem,
					ProgramItem programItem) {
					hideErrorPage();
					//tv_backplay.setVisibility(VISIBLE);
					//tv_backplay.setText("回看模式");
					numChangeChannelView.setBackMode();
					backPlayView.setData(channelView.getPlayingProgram());;
					long stime = programItem.getlStartTime();
					long etime = programItem.getlStopTime();
					changeChannelPlayBack(channelItem.getChannelId(),
							channelItem.getTitle(), stime, etime);

			}

			@Override
			public void onClearBackPlayInfo() {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						clearBackPlayInfo();
					}
				});

			}

			@Override
			public void onBackToLiveClick(ChannelItem channelItem) {
				if (channelItem != null) {
					LogUtil.i("PlayerView--------->onBackToLiveClick----->tvid::"
							+ channelItem.getChannelId() + "--tvname::"
							+ channelItem.getTitle());
					//vooleMediaPlayer.reset();
					vooleMediaPlayer.stop();
					vooleMediaPlayer.release();
					changeChannel(channelItem.getChannelId(),
							channelItem.getTitle(),
							channelItem.getCurrentResourceItem());
				}
			}

		});
		addView(channelView);
	}

	private void initBufferView(Context context) {
		bufferView = new TVBufferView(context);
		RelativeLayout.LayoutParams param_buffer = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		param_buffer.addRule(RelativeLayout.CENTER_IN_PARENT);
		bufferView.setLayoutParams(param_buffer);
		bufferView.setVisibility(View.INVISIBLE);
		addView(bufferView);
	}

	private int index = 0;
	private void showBufferView(String speed) {
		int[] ids = new int[]{R.string.buffer_tip1,R.string.buffer_tip2};
		if (bufferView.getVisibility() != View.VISIBLE) {
			bufferView.setVisibility(View.VISIBLE);
			index = (int) (Math.random()*2);
		}
		if("0".equals(ResourceManager.GetInstance().getResourceState())){
			bufferView.setHintInfo(speed, getString(R.string.buffer_tip4));
		}else{
			bufferView.setHintInfo(speed, getString(ids[index]));
		}
	}

	public void showBufferViewWithText(String hint) {
		if (bufferView.getVisibility() != View.VISIBLE) {
			bufferView.setVisibility(View.VISIBLE);
		}
		bufferView.setHintInfo(hint);
	}

	private void hideBufferView() {
		isBufferShow = false;
		if (bufferView.getVisibility() != View.GONE) {
			bufferView.setVisibility(View.GONE);
		}
		stopCheckDownSpeed();
	}

	private int changeWidth(int width) {
		return DisplayManager.GetInstance().changeWidthSize(width);
	}

	private int changeHeight(int height) {
		return DisplayManager.GetInstance().changeHeightSize(height);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtil.i("PlayerView--------->onKeyDown----->" + keyCode);
		if (todayProgramInfo == null||todayProgramInfo.getAssortList().size()==0) {
			TVToast.show(context, "数据获取中,请稍后重试", TVToast.LENGTH_SHORT);
			return super.onKeyDown(keyCode, event);
		}
		 
		switch (keyCode) {
		
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
			/*if(ViewConst.LIVE_STATE == channelView.playState){
				if (event.getRepeatCount() == 0) {//识别长按短按的代码  
		            event.startTracking();  
		            isLongPressKey = false;  
		        }else{  
		            isLongPressKey = true;  
		        } 
			}*/
		    return true; 
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_ESCAPE:
			if (mCanExit||!vooleMediaPlayer.onKeyDown(keyCode)) {
				if (channelView.playState == ViewConst.BACK_STATE) {
					showBackPlayCompleteDialog(true);
					PageActionReport.GetInstance().reportPageAction(PageType.PlaybackPage, null,
							null, null, Action.ExitKey);
					break;
				}else if(channelView.playState == ViewConst.TIMESHIFT_STATE){
					backToLiveFromShift();
					break;
				}
				return super.onKeyDown(keyCode, event);
			}
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if (mCanExit||!vooleMediaPlayer.onKeyDown(keyCode)) {
				if (channelView.playState == ViewConst.LIVE_STATE) {
					showProgramInfoView(channelView.getCurrentChannel());
				} else if (channelView.playState == ViewConst.BACK_STATE){
					if(!isBackError){
						showBackPlayView();
					}
				}else if (channelView.playState == ViewConst.TIMESHIFT_STATE){
					showTimeShiftingView();
				}
			}
			break;
		
		case KeyEvent.KEYCODE_0:
		case KeyEvent.KEYCODE_1:
		case KeyEvent.KEYCODE_2:
		case KeyEvent.KEYCODE_3:
		case KeyEvent.KEYCODE_4:
		case KeyEvent.KEYCODE_5:
		case KeyEvent.KEYCODE_6:
		case KeyEvent.KEYCODE_7:
		case KeyEvent.KEYCODE_8:
		case KeyEvent.KEYCODE_9:
			if(channelView.playState == ViewConst.LIVE_STATE||channelView.playState == ViewConst.TIMESHIFT_STATE){
				int num = keyCode - 7;
				showNum = showNum + num;
				channelView.hide();
				numChangeChannelView.hide();
				numChangeChannelView.show(showNum);
			}
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
		case KeyEvent.KEYCODE_CHANNEL_UP:
			LogUtil.i("上下键切台开始计时");
			if(channelView.playState == ViewConst.LIVE_STATE||channelView.playState == ViewConst.TIMESHIFT_STATE){
				startTime = System.currentTimeMillis();
				hideErrorPage();
				channelView.gotoAllNextChannel();
				hideProgramInfoView();
				showProgramInfoView(channelView.getCurrentChannel());
			}
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
		case KeyEvent.KEYCODE_CHANNEL_DOWN:
			LogUtil.i("上下键切台开始计时");
			if(channelView.playState == ViewConst.LIVE_STATE||channelView.playState == ViewConst.TIMESHIFT_STATE){
				startTime = System.currentTimeMillis();
				hideErrorPage();
				channelView.gotoAllPreChannel();
				hideProgramInfoView();
				showProgramInfoView(channelView.getCurrentChannel());
			}
			break;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
		case KeyEvent.KEYCODE_VOLUME_UP:
			hideProgramInfoView();
			channelView.hide();
			hideBackPlayView();
			return super.onKeyDown(keyCode, event);
		default:
			return super.onKeyDown(keyCode, event);
		}

		return true;
	}
	
	/**
	 * 由时称回到直播
	 */
	private void backToLiveFromShift(){
		LogUtil.i("PlayerView--------->backToLiveFromShift");
		channelView.initToLive();
		hideErrorPage();
		ChannelItem item = getCurrentChannel();
		clearBackPlayInfo();
		showProgramInfoView(item);
		changeChannel(item.getChannelId(), item.getTitle(),
				item.getCurrentResourceItem());
	}

	/*@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		LogUtil.i("PlayerView--------->onKeyLongPress----->" + keyCode);
		isLongPressKey = true;
		
		return super.onKeyLongPress(keyCode, event);
	}
	*/
	/**
	 * 进入时移模式
	 */
	private void enterShiftView(){
		LogUtil.i("PlayerView--->showShiftView");
		channelView.playState = ViewConst.TIMESHIFT_STATE;
		numChangeChannelView.setShiftingMode();
		programInfoView.hide();
		backPlayView.show();
		backPlayView.setData(getCurrentChannel());
		backPlayView.setTotalTime(6*60*60*1000);
		//backPlayView.updateTime();
		backPlayView.setSeek(6*60*60);
		
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		LogUtil.i("PlayerView--------->onKeyUp----->" + keyCode);
		if (todayProgramInfo == null||todayProgramInfo.getAssortList().size()==0) {
			TVToast.show(context, "数据获取中,请稍后重试", TVToast.LENGTH_SHORT);
			return super.onKeyDown(keyCode, event);
		}
		switch (keyCode) {
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
			if (isLongPressKey) {
				isLongPressKey = false;
				ChannelItem playingItem = getCurrentChannel();
				if(playingItem!=null){
					if("1".equalsIgnoreCase(playingItem.getIsBackView())){
						enterShiftView();
					}else{
						TVToast.show(context,R.string.shift_tip, TVToast.LENGTH_SHORT);
					}
				}
				break;
			}
			if (mCanExit||!vooleMediaPlayer.onKeyDown(keyCode)) {
				if (todayProgramInfo != null) {
					if (channelView != null) {
						showChannelView();
						/*if (channelView.playState == BACK_STATE) {
							pausePlay();
						}*/
						post(new Runnable() {
							@Override
							public void run() {
								channelView.requestFocus();
							}
						});
						// showBufferView();
					}
				} else {
					TVToast.show(context, "数据获取中,请稍后重试", TVToast.LENGTH_SHORT);
				}
				if (channelView.playState == ViewConst.BACK_STATE){
					PageActionReport.GetInstance().reportPageAction(PageType.PlaybackPage, null, null, null, Action.OKKey);
				}else{
					PageActionReport.GetInstance().reportPageAction(PageType.PlayPage, null, null, null, Action.OKKey);
				}
			}
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			// programInfoView.requestFocus();
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
		case KeyEvent.KEYCODE_CHANNEL_UP:
		case KeyEvent.KEYCODE_DPAD_DOWN:
		case KeyEvent.KEYCODE_CHANNEL_DOWN:
			if(channelView.playState == ViewConst.LIVE_STATE||channelView.playState == ViewConst.TIMESHIFT_STATE){
				channelView.gotoPlayChannel();
				PageActionReport.GetInstance().reportPageAction(PageType.PlayPage, null,
						getCurrentChannel().getChannelId(), FocusType.UpDownKeySwitch,null);
			}
			break;
		
		default:
			break;
		}
		return super.onKeyUp(keyCode, event);
	}

	private void showChannelView() {
		LogUtil.i("PlayerView--->showChannelView");
		programInfoView.hide();
		long msec = TimeManager.GetInstance().getCurrentTime();
		channelView.show(msec);
	}

	private void showBackPlayView() {
		LogUtil.i("PlayerView--->showBackPlayView");
		programInfoView.hide();
		//hideBackPlayView();
		backPlayView.setData(channelView.getPlayingProgram());
		backPlayView.show();
	}
	
	private void showTimeShiftingView(){
		LogUtil.i("PlayerView--->showTimeShiftingView");
		programInfoView.hide();
		//hideBackPlayView();
		//backPlayView.setData(getCurrentChannel());
		backPlayView.updateTime();
		backPlayView.show();
	}

	private void hideBackPlayView() {
		backPlayView.hide();
	}

	private void changeChannel(final String tvId, final String tvName,
			final ResourceItem resourceItem) {
		hideErrorPage();
		if(resourceItem == null){
			vooleMediaPlayer.reset();
			showErrorPage(getString(R.string.error_resource));
		}else{
			//vooleMediaPlayer.stop();
			LogUtil.i("PlayerView--->changeChannel--->开始预下载播放");
			changeAspectRatio(aspectRatio);
			vooleMediaPlayer.prepareLive(tvId,getChannelType(), resourceItem.getResourceId(), resourceItem.getIs3rd(), resourceItem.getTracker(), resourceItem.getBkeUrl()
					,resourceItem.getDataType(), resourceItem.getProto());
		}
	}

	private void changeChannelPlayBack(final String tvId, final String tvName,
			final long stime, final long etime) {
		showBufferView();
		vooleMediaPlayer.stop();
		vooleMediaPlayer.reset();
		vooleMediaPlayer.preparePlayBack(tvId, tvName, stime/1000+"", etime/1000+"",true);
	}
	
	private void changeChannelTimeShift(String tvId,ResourceItem item,String stime){
		hideErrorPage();
		if(item == null){
			vooleMediaPlayer.reset();
			showErrorPage(getString(R.string.error_resource));
		}else{
			//vooleMediaPlayer.stop();
			LogUtil.i("PlayerView--->changeChannelTimeShift--->开始时移播放");
			vooleMediaPlayer.prepareTimeShift(tvId, item.getResourceId(), stime);
		}
	}

	/*
	 * private void playTimeShift(final String tvID,final String
	 * channelName,final String stime){ showBufferView(); new Thread(new
	 * Runnable() {
	 * 
	 * @Override public void run() { vooleMediaPlayer.reset();
	 * vooleMediaPlayer.prepareTimeShift(tvID, channelName, stime); }
	 * }).start(); }
	 */

	/**
	 * 获取当前直播的播放状态
	 * 
	 * @return public static final int LIVE_STATE = 100; public static final int
	 *         BACK_STATE = 200;
	 */
	public int getPlayState() {
		return channelView.playState;
	}

	/**
	 * 获取当前频道的源的类型
	 * 
	 * @return
	 */
	public ChannelItem getCurrentChannel() {
		return channelView.getCurrentChannel()!=null?channelView.getCurrentChannel():curChannelItem;
	}

	public List<ResourceItem> getCurrentResources() {
		return null;
	}

	/**
	 * 切换源播放频道
	 * 
	 * @param resources
	 */
	public void changeResources(ResourceItem resItem) {
		LogUtil.i("PlayerView--->changeResources");
		ChannelItem channelItem = channelView.getCurrentChannel();
		if (channelItem != null) {
			if(!channelItem.getCurrentResourceItem().getResourceId().equals(resItem.getResourceId())||vooleMediaPlayer.getCurrentStatus() != Status.Playing){
				showBufferView();
				ResourceManager.GetInstance().saveResource(
						channelItem.getChannelId(), resItem.getResourceId());
				changeChannel(channelItem.getChannelId(), channelItem.getTitle(),
						channelItem.getCurrentResourceItem());
			}
			
		}
	}
	
	/**
	 * 根据状态来切换源
	 * @param state
	 */
	public void changeResources(String state){
		LogUtil.i("PlayerView--->changeResources");
		ChannelItem channelItem = channelView.getCurrentChannel();
		if (channelItem != null) {
			if(!state.equals(ResourceManager.GetInstance().getResourceState())||vooleMediaPlayer.getCurrentStatus() != Status.Playing){
				showBufferView();
				ResourceManager.GetInstance().saveResourceState(state);
				changeChannel(channelItem.getChannelId(), channelItem.getTitle(),
						channelItem.getCurrentResourceItem());
			}
			
		}
	}

	/**
	 *  获取退出时播放的频道id
	 * @return
	 */
	public String getCurrentPlayId(){
		return LocalManager.GetInstance().getLocal(CHANNEL_ID, null);
	}
	
	private void setData(TodayProgramInfo todayProgramInfo,String otherId,boolean isUpdateProgram) {
		LogUtil.i("PlayerView--->setData--->isUpdateProgram：："+isUpdateProgram);
		// this.todayProgramInfo = todayProgramInfo;
		assortItemList = todayProgramInfo.getAssortList();
		if(assortItemList!=null&&assortItemList.size()>0){
			
			if(isUpdateProgram){
				ChannelItem firstChannel = null;
				AssortItem firstAssortItem = null;
				try {
					firstAssortItem = assortItemList.get(0);
					firstChannel = firstAssortItem.getChannelList().get(0);
				} catch (Exception e) {
					LogUtil.e("PlayerView--->setData--->"+e.toString());
				}
				if(firstChannel!=null&&firstChannel.getCurrentResourceItem()!=null){
					ResourceItem resourceItem = firstChannel.getCurrentResourceItem();
					LocalManager.GetInstance().saveLocal(FIRST_CHANNEL_ID,
							firstChannel.getChannelId());
					LocalManager.GetInstance().saveLocal(FIRST_CHANNEL_NAME,
							firstChannel.getTitle());
					LocalManager.GetInstance().saveLocal(FIRST_CHANNEL_NO,
							firstChannel.getChannelNo());
					LocalManager.GetInstance().saveLocal(FIRST_RESOURCE_ID,
							resourceItem.getResourceId());
					LocalManager.GetInstance().saveLocal(FIRST_CHANNEL_TYPE, 
							firstAssortItem.getAssortValue());
					LocalManager.GetInstance().saveLocal(FIRST_RESOURCE_IS3RD,
							resourceItem.getIs3rd());
					LocalManager.GetInstance().saveLocal(FIRST_RESOURCE_TRACKER,
							resourceItem.getTracker());
					LocalManager.GetInstance().saveLocal(FIRST_RESOURCE_BKEURL,
							resourceItem.getBkeUrl());
					LocalManager.GetInstance().saveLocal(FIRST_RESOURCE_DATATYPE,
							resourceItem.getDataType());
					LocalManager.GetInstance().saveLocal(FIRST_RESOURCE_PROTO,
							resourceItem.getProto());
				}
			}
			
			int sortIndex = 0;
			String channelId = null;
			if(otherId!=null){
				//第三方应用启动的apk,进行相关初始化
				channelId = otherId;
				sortIndex = getChanelSortIndexByCID(channelId, assortItemList);
			}else{
				String sortName = LocalManager.GetInstance().getLocal(
						CHANNEL_SORT_NAME, null);
				if(sortName == null && !isUpdateProgram){
					sortIndex = 1;
				}else{
					sortIndex = getChannelSortIndex(sortName, assortItemList);
				}
				channelId = LocalManager.GetInstance()
						.getLocal(CHANNEL_ID, null);
			}
		
			int channelIndex = 0;
			if(sortIndex!=-1 && sortIndex<assortItemList.size()){
				List<ChannelItem> channelItems = assortItemList.get(sortIndex).getChannelList();
				channelIndex = getChannelIndex(channelId, channelItems);
				if (channelIndex != -1&&channelItems.size()>0) {
					/*curChannelItem = assortItems.get(sortIndex).getChannelList()
							.get(channelIndex);*/
					//curChannelItem = getAllChannelItems(assortItems).get(channelIndex);
					curChannelItem = channelItems.get(channelIndex);
					if(isUpdateProgram&&isFirstStartUp){
						LogUtil.i("PlayerView--->setData--->preparePlay()");
						showProgramInfoView(curChannelItem);
						preparePlay();
					}
				}
				
				channelView
				.setData(assortItemList, sortIndex,
						channelIndex, TimeManager.GetInstance()
								.getCurrentTime(), false,isUpdateProgram);
			}
		}
	}
	

	private void setKTData(TodayProgramInfo todayProgramInfo) {
		LogUtil.i("playerView--->setKTData");
		assortItemList = todayProgramInfo.getAssortList();
		
		String sortName = LocalManager.GetInstance().getLocal(
				CHANNEL_SORT_NAME, null);
		int sortIndex = getChannelSortIndex(sortName, assortItemList);
		String channelId = LocalManager.GetInstance()
				.getLocal(CHANNEL_ID, null);
		int psIndex = 0;
		int pcIndex = 0;
		
		if(sortIndex!=-1){
			List<ChannelItem> channelItems = assortItemList.get(sortIndex).getChannelList();
			pcIndex = getChannelIndex(channelId, channelItems);
			if (pcIndex != -1) {
				psIndex = sortIndex;
				curChannelItem = channelItems.get(pcIndex);
			}
		}
		channelView.setKTData(todayProgramInfo.getAssortList(), psIndex, pcIndex,
				TimeManager.GetInstance().getCurrentTime(), false);
		//channelView.initKT(psIndex, pcIndex);
	}
	
	public void preparePlay(boolean isGoto){
		LogUtil.i("PlayerView--->preparePlay");
		String channelType = null;
		if(isGoto){
			LogUtil.i("PlayerView--->preparePlay--->isGoto::"+isGoto);
			curChannelItem = getCurrentChannel();
			if(curChannelItem!=null){
				showProgramInfoView(curChannelItem);
			}
			if(backplayDialog!=null){
				backplayDialog.cancel();
			}
			channelType = getChannelType();
			//由点播跳转到直播上报
			PageActionReport.GetInstance().reportPageAction(PageType.PlayPage, null, null, null, null);
		}else{
			
			if(curChannelItem == null){
				LogUtil.i("PlayerView--->preparePlay--->从缓存中获取频道");
				String channelId = LocalManager.GetInstance().getLocal(CHANNEL_ID, null);
				String channelName = LocalManager.GetInstance().getLocal(CHANNEL_NAME, null);
				String channelNo = LocalManager.GetInstance().getLocal(CHANNEL_NO, null);
				channelType = LocalManager.GetInstance().getLocal(CHANNEL_TYPE, null);
				String resourceId = LocalManager.GetInstance().getLocal(RESOURCE_ID, null);
				String is3rd = LocalManager.GetInstance().getLocal(RESOURCE_IS3RD, null);
				String tracker = LocalManager.GetInstance().getLocal(RESOURCE_TRACKER, null);
				String bkeUrl = LocalManager.GetInstance().getLocal(RESOURCE_BKEURL, null);
				String datatype = LocalManager.GetInstance().getLocal(RESOURCE_DATATYPE, null);
				String proto = LocalManager.GetInstance().getLocal(RESOURCE_PROTO, null);
				curChannelItem = getPlayChannel(channelId,channelName,channelNo, resourceId, is3rd, tracker, bkeUrl, datatype, proto);
				if(curChannelItem==null){
					LogUtil.i("PlayerView--->preparePlay--->从缓存中没有频道");
					isFirstStartUp = true;
				}
			}
			
		}
		if(channelView.playState == ViewConst.BACK_STATE){
			hideErrorPage();
			channelView.gotoPlayChannel();
		}else{
			if (curChannelItem != null) {
				ResourceItem curResourceItem = curChannelItem.getCurrentResourceItem();
				if(curResourceItem == null){
					showErrorPage(getString(R.string.error_resource));
				}else{
					changeAspectRatio(aspectRatio);
					vooleMediaPlayer.prepareLive(curChannelItem.getChannelId(),channelType,curResourceItem.getResourceId(), curResourceItem.getIs3rd(), curResourceItem.getTracker(), curResourceItem.getBkeUrl()
							,curResourceItem.getDataType(), curResourceItem.getProto());
				}
			}
		}
	}
	
	private ChannelItem getPlayChannel(String channelId,String channelName,String channelNo,String resourceId,String is3rd,String tracker,
			String bkeUrl,String datatype,String proto){
		ChannelItem channelItem = null;
		if(channelId!=null){
			channelItem = new ChannelItem();
			channelItem.setChannelId(channelId);
			channelItem.setTitle(channelName);
			channelItem.setChannelNo(channelNo);
			if(resourceId!=null&&is3rd!=null&&tracker!=null&&bkeUrl!=null&&datatype!=null&&proto!=null){
				ResourceItem resourceItem = new ResourceItem();
				resourceItem.setResourceId(resourceId);
				resourceItem.setIs3rd(is3rd);
				resourceItem.setTracker(tracker);
				resourceItem.setBkeUrl(bkeUrl);
				resourceItem.setDataType(datatype);
				resourceItem.setProto(proto);
				ArrayList<ResourceItem> items = new ArrayList<ResourceItem>();
				items.add(resourceItem);
				channelItem.setResourceList(items);
			}
		}
		return channelItem;
	}

	public void preparePlay() {
		preparePlay(false);
	}
	
	public void startPlay() {
		// showBufferView();
		LogUtil.i("PlayerView--->startPlay");
		aspectRatio = ResourceManager.GetInstance().getResourceScale();
		isStart = true;
		if(curChannelItem!=null&&curChannelItem.getTitle()!=null){
			showProgramInfoView(curChannelItem);
		}
	}

	private void saveNewChannelInfo() {
		if (todayProgramInfo != null
				&& todayProgramInfo.getAssortList().size() > 0) {
			LogUtil.i("PlayerView--->saveNewChannelInfo");
			AssortItem assort = todayProgramInfo.getAssortList().get(
					channelView.playingSortIndex);
			LocalManager.GetInstance().saveLocal(CHANNEL_SORT_NAME,
					assort.getAssortName());
			ChannelItem curChannelItem = channelView.getCurrentChannel();
			if (curChannelItem != null) {
				/*LocalManager.GetInstance().saveLocal(CHANNEL_NAME,
						curChannelItem.getTitle());*/
				ResourceItem resourceItem = curChannelItem.getCurrentResourceItem();
				LocalManager.GetInstance().saveLocal(CHANNEL_ID,
						curChannelItem.getChannelId());
				LocalManager.GetInstance().saveLocal(CHANNEL_NAME,
						curChannelItem.getTitle());
				LocalManager.GetInstance().saveLocal(CHANNEL_NO,
						curChannelItem.getChannelNo());
				LocalManager.GetInstance().saveLocal(RESOURCE_ID,
						resourceItem.getResourceId());
				LocalManager.GetInstance().saveLocal(CHANNEL_TYPE,
						assort.getAssortValue());
				LocalManager.GetInstance().saveLocal(RESOURCE_IS3RD,
						resourceItem.getIs3rd());
				LocalManager.GetInstance().saveLocal(RESOURCE_TRACKER,
						resourceItem.getTracker());
				LocalManager.GetInstance().saveLocal(RESOURCE_BKEURL,
						resourceItem.getBkeUrl());
				LocalManager.GetInstance().saveLocal(RESOURCE_DATATYPE,
						resourceItem.getDataType());
				LocalManager.GetInstance().saveLocal(RESOURCE_PROTO,
						resourceItem.getProto());
			}
		}
	}

	private boolean isAddView = false;
	public void showProgramInfoView(ChannelItem item) {
		Log.i("ly", "showProgramInfoView");
		programInfoView.setData(item, TimeManager.GetInstance()
				.getCurrentTime());
		hideBufferView();
		channelView.hide();
		if(!isAddView){
			isAddView = true;
			try {
				addView(programInfoView);
			} catch (Exception e) {
				LogUtil.e("ChannelView-->showProgramInfoView-->"+e.toString());
			}
		}
		programInfoView.show();
		post(new Runnable() {
			@Override
			public void run() {
				programInfoView.requestFocus();
			}
		});
	}
	

	private void hideProgramInfoView() {
		programInfoView.hide();
	}

	private void showBufferView() {
		LogUtil.i("PlayerView--->showBufferView");
		if(errorView.getVisibility()!=VISIBLE){
			isBufferShow = true;
			startCheckDownSpeed();
		}
	}

	private void startCheckDownSpeed() {
		if (downspeedTimer == null) {
			downspeedTimer = new Timer();
			downspeedTimer.schedule(new DownspeedTimer(), 0,
					DOWNSPEED_TIME_INTERVAL);
		}
	}

	private void stopCheckDownSpeed() {
		if (downspeedTimer != null) {
			downspeedTimer.cancel();
			downspeedTimer.purge();
			downspeedTimer = null;
		}
	}

	private class DownspeedTimer extends TimerTask {
		@Override
		public void run() {
			int speedReal = 0;
			long total = TrafficStats.getTotalRxBytes();
			if (mRxBytes != 0) {
				if (total > mRxBytes) {
					speedReal = (int) ((total - mRxBytes) /(1024*8));
				} else {
					speedReal = 0;
				}
			}
			mRxBytes = total;
			if (speedReal < 512) {
				downspeed = speedReal + " KB/S";
			} else {
				double mSpeed = speedReal / 1024.0;
				downspeed = (Math.round(mSpeed * 100) / 100.0) + " MB/S";
			}

			viewHandler.sendEmptyMessage(SPEED_SUCCESS);
		}
	}

	public synchronized void release() {
		LogUtil.d("gcMediaPlayer-->start");
		stopCheckDownSpeed();
		hideDialog();
		try {
			if (vooleMediaPlayer != null) {
//				vooleMediaPlayer.reset(); // not need reset 
				vooleMediaPlayer.stop(); //停止之前的心跳上报
				vooleMediaPlayer.release();
				//vooleMediaPlayer = null;
			}
		} catch (Exception e) {
			LogUtil.e("PlayerView-->release-->"+e.toString());
		}
		LogUtil.d("gcMediaPlayer-->end");
	}

	private int getChannelSortIndex(String sortName,
			List<AssortItem> assortItems) {
		if (!TextUtils.isEmpty(sortName)&&assortItems!=null) {
			int size = assortItems.size();
			for (int i = 0; i < size; i++) {
				if (sortName.trim().equals(
						assortItems.get(i).getAssortName().trim())) {
					return i;
				}
			}
		} else {
			return 0;
		}
		return 0;
	}
	
	private int getChanelSortIndexByCID(String cid,List<AssortItem> assortItems){
		if(assortItems!=null){
			int size = assortItems.size();
			for(int i=0;i<size;i++){
				List<ChannelItem> channelItems = assortItems.get(i).getChannelList();
				int len = channelItems.size();
				for(int j=0;j<len;j++){
					if(cid.equals(channelItems.get(j).getChannelId()))
						return i;
				}
			}
		}
		
		return 0; //频道找不到返回第一个值
	}

	private int getChannelIndex(String channelId,List<ChannelItem> channelItems){
		if(!TextUtils.isEmpty(channelId)){
			int size = channelItems.size();
			if(size==0){
				return -1;
			}
			for(int i =0;i<size;i++){
				if(channelId.equals(channelItems.get(i).getChannelId())){
					return i;
				}
			}
		}
		return 0;
	}

	private ChannelItem findChannelByChannelNo(String cno) {
		try {
			List<AssortItem> assortItems = todayProgramInfo.getAssortList();
			for (AssortItem assort : assortItems) {
				for (ChannelItem channelItem : assort.getChannelList()) {
					if (cno.equalsIgnoreCase(channelItem.getChannelNo())) {
						return channelItem;
					}
				}
			}
		} catch (Exception e) {
			LogUtil.e("findChannelByChannelNo--->" + e.toString());
		}
		return null;
	}

	private void clearBackPlayInfo() {
		//tv_backplay.setVisibility(GONE);
		backPlayView.release();
		numChangeChannelView.hide();
		backPlayView.hide();
	}

	private int changeTextSize(int size) {
		return DisplayManager.GetInstance().changeTextSize(size);
	}

	private int getColor(int id) {
		return context.getResources().getColor(id);
	}

	private void showBackPlayCompleteDialog(boolean flag) {
		if (backplayDialog == null) {
			TVAlertDialog.Builder builder = new TVAlertDialog.Builder(
					getContext());
			builder.setTitle(R.string.back_stay);

			builder.setNegativeButton(R.string.back_to_live,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							channelView.initToLive();
							hideErrorPage();
							ChannelItem item = getCurrentChannel();
							clearBackPlayInfo();
							showProgramInfoView(item);
							vooleMediaPlayer.stop();
							vooleMediaPlayer.release();
							changeChannel(item.getChannelId(), item.getTitle(),
									item.getCurrentResourceItem());
							PageActionReport.GetInstance().reportPageAction(PageType.PlayPage, ModuleType.ChannelList,
									item.getChannelId(), FocusType.ChannelListSwitch, Action.OKKey);
							dialog.cancel();
						}
					});
			builder.setPositiveButton(R.string.watch_movie,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							if(skipAction(apkPacks, downloadUrl)){
								/*tv_backplay.setVisibility(INVISIBLE);
								tv_backplay.setText("");*/
							}
						}
					});
			backplayDialog = builder.create();
			backplayDialog.setCancelable(flag);
			backplayDialog.show();
		}else if(!backplayDialog.isShowing()){
			LogUtil.i("PlayerView--->showBackPlayCompleteDialog--->else");
			backplayDialog.findViewById(R.id.negativeButton).requestFocus();
			backplayDialog.setCancelable(flag);
			backplayDialog.show();
		}

	}

	public TodayProgramInfo initProgramInfo(final String channelId) {
		TimeManager.GetInstance().addKTListener(new IKTListener() {
			@Override
			public void onKT() {
				LogUtil.i("PlayerView--->IKTListener");
				
					// 跨天处理
				todayProgramInfo = ProgramManager.GetInstance().getTodayProgramInfoAndMostViewed(ProgramManager.ProgramType.ALL);
				if (todayProgramInfo != null&&"0".equals(todayProgramInfo.getStatus()) && todayProgramInfo.getAssortList().size()>0) {
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							setKTData(todayProgramInfo);
						}
					});
				}
			}

			@Override
			public void onUpdateChannel() {
				LogUtil.i("UpdateChannel--->每隔两小时更新一次片单");
				AssortItem item = null;
				if (isTodayProgramInfoCanUse(todayProgramInfo)){
					item = todayProgramInfo.getAssortList().get(0);
				}
					
				todayProgramInfo = ProgramManager.GetInstance().updateTodayProgramInfo(ProgramManager.ProgramType.ALL);
				if (isTodayProgramInfoCanUse(todayProgramInfo)) {
					if(item!=null && "常用频道".equals(item.getAssortName()) && item.getChannelList()!=null && item.getChannelList().size()>0){
						todayProgramInfo.getAssortList().add(0,item);
					}
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							setKTData(todayProgramInfo);
						}
					});
				}
			}
		});

		todayProgramInfo = ProgramManager.GetInstance().getTodayProgramInfoAndMostViewedAsyn(ProgramManager.ProgramType.ALL,new UpdateChannel() {
			
			@Override
			public void addMostViewed(AssortItem item) {
				if (isTodayProgramInfoCanUse(todayProgramInfo)) {
					if(item!=null&&item.getChannelList()!=null&&item.getChannelList().size()>0){
						todayProgramInfo.getAssortList().add(0,item);
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								setData(todayProgramInfo,channelId,false);
							}
						});
					}
					
				}
			}
		});
		if (isTodayProgramInfoCanUse(todayProgramInfo)) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					setData(todayProgramInfo,channelId,true);
				}
			});

		}
		return todayProgramInfo;
	}
	
	private boolean isTodayProgramInfoCanUse(TodayProgramInfo info){
		return info != null&&"0".equals(info.getStatus()) && info.getAssortList()!=null&&info.getAssortList().size()>0;
	}

	private void showErrorPage(ErrorInfo errorInfo) {
		hideBufferView();
		if (errorView.getVisibility() != VISIBLE) {
			if (errorInfo!=null) {
				errorView.setTipInfo(errorInfo.getErroeMessageAndCode(),"可加入QQ群反馈问题："+errorInfo.getQq());
			}
			errorView.setVisibility(VISIBLE);
		}
	}
	
	private void showErrorPage(String msg) {
		hideBufferView();
		if (errorView.getVisibility() != VISIBLE) {
			if (!TextUtils.isEmpty(msg)) {
				errorView.setTipInfo(msg,null);
			}
			errorView.setVisibility(VISIBLE);
		}
	}

	public void hideErrorPage() {
		errorView.hide();
	}

	private String getString(int id) {
		return context.getResources().getString(id);
	}

	public void setApkPacks(String apkPacks) {
		this.apkPacks = apkPacks;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	
	private boolean skipAction(String apkPacks,
			String downloadUrl) {
		if (tipInstallEpg(apkPacks, downloadUrl)) {
			gotoCSMovie(apkPacks);
			return true;
		}
		return false;
	}

	private boolean tipInstallEpg(String apkPacks, final String downloadUrl) {
		if (!hasEpgPlayer(apkPacks)) {
			new TVAlertDialog.Builder(context)
					.setTitle(R.string.error_movie)
					.setCancelable(false)
					.setNegativeButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									downloadVooleApk(downloadUrl);
									dialog.cancel();
								}
							})
					.setPositiveButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							}).create().show();
			return false;
		}
		return true;
	}

	private void downloadVooleApk(final String downloadUrl) {
		 Download load = new Download(activity);
		 load.startToDownload(downloadUrl,context.getCacheDir().getPath(), "download.apk");
	}

	private boolean hasEpgPlayer(String apkPacks) {
		boolean hasEpg = false;
		String[] packs = apkPacks.split(",");
		for (String pack : packs) {
			hasEpg = hasEpg || DeviceUtil.checkPackageExist(context, pack);
		}

		return hasEpg;
	}

	private void gotoCSMovie(String apkPacks) {

		// String detail_action = Config.get(Key.detail_action);
		String[] packs = apkPacks.split(",");
		for (String pack : packs) {
			try {
				Intent intent = context.getPackageManager().getLaunchIntentForPackage(pack);
				context.startActivity(intent);
				break;
			} catch (Exception e) {
				LogUtil.e("gotoCSMovieDetail--->" + e);
			}
		}

	}
	
	private void hideDialog(){
		if(backplayDialog!=null){
			backplayDialog.cancel();
		}
	}
	
	/**
	 * 根据频道id切台
	 * @param cid
	 */
	public void changeChannelById(String cid){
		ChannelItem item = getChannelByCid(cid);
		if(item!=null){
			hideProgramInfoView();
			showProgramInfoView(item);
			channelView.numChangeChannel(item);
		}
	}
	
	private ChannelItem getChannelByCid(String cid){
		if(assortItemList!=null){
			int size = assortItemList.size();
			for(int i=0;i<size;i++){
				List<ChannelItem> channelItems = assortItemList.get(i).getChannelList();
				int len = channelItems.size();
				for(int j=0;j<len;j++){
					if(cid.equals(channelItems.get(j).getChannelId()))
						return channelItems.get(j);
				}
			}
		}
		return null;
	}
	
	
	
	/**
	 * 切换画面比例
	 * @param useRatio_4_3 
	 */
	private String originScale = "";
	public void changeAspectRatio(String useRatio_4_3){
		LayoutParams mPlayViewParam;
		aspectRatio = useRatio_4_3;
		if ("0".equals(useRatio_4_3)) {//铺满全屏
			if(!originScale.endsWith("9:16")){
				int width = DisplayManager.GetInstance().getScreenWidth();
				int height = width*9/16;
				mPlayViewParam = new LayoutParams(width, height);
				mPlayViewParam.addRule(RelativeLayout.CENTER_IN_PARENT);
				getVooleMediaPlayer().setLayoutParams(mPlayViewParam);
				originScale = "9:16";
			}
		}else {//原始比例
			String scale = getCurrentChannel().getCurrentResourceItem().getScale();
			if(!originScale.endsWith(scale)){
				String[] values = scale.split(":");
				int s1 = Integer.parseInt(values[0]);
				int s2 = Integer.parseInt(values[1]);
				int height = DisplayManager.GetInstance().getScreenHeight();
				int width = height*s1/s2;
				mPlayViewParam = new LayoutParams(width, height);
				mPlayViewParam.addRule(RelativeLayout.CENTER_IN_PARENT);
				getVooleMediaPlayer().setLayoutParams(mPlayViewParam);
				originScale = scale;
			}
		}
	}
	
	public String getChannelType(){
		return channelView.getCurrentChannelType();
	}
	
	private float getTextSize(int id){
		float size = getContext().getResources().getDimension(id);
		Log.i("textsize","textsize::"+ size);
		return size;
	}
	
/*	private List<ChannelItem> getAllChannelItems(List<AssortItem> assorts){
		List<ChannelItem> items = new ArrayList<ChannelItem>();
		for(AssortItem item:assorts){
			items.addAll(item.getChannelList());
		}
		return items; 
	}*/

	/*public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}*/
}
