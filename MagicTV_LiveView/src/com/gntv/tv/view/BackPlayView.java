package com.gntv.tv.view;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.gntv.tv.R;
import com.gntv.tv.common.utils.DateUtil;
import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.model.channel.ChannelItem;
import com.gntv.tv.model.channel.ProgramItem;
import com.gntv.tv.model.time.TimeManager;
import com.gntv.tv.view.base.AlwaysMarqueeTextView;
import com.gntv.tv.view.base.DisplayManager;
import com.gntv.tv.view.base.TimerRelativeLayout;
import com.gntv.tv.view.base.ViewConst;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class BackPlayView extends TimerRelativeLayout{
	private Context ctx = null;
	//private TextView tv_time = null;
	private VSeekBar seekBarView = null;
	private int currentProgress = 0; //单位为秒
	private int totalTime = 0;  //单位为秒
	private boolean isFirst = true;
	private boolean isTouchDown = false;
	private ImageView iv_pause = null;
	
	private PlayerView playerView = null;
	private IBackPlayListenner iBackPlayListenner = null;
	private PlayTimeTask timerTask = new PlayTimeTask();
	private Timer timer = null;
	
	private ShiftTimerTask shiftTimerTask = new ShiftTimerTask();
	private Timer shiftTimer = null;
	private int lastWaitTime = 0;
	private boolean isOnKeyDown = false; //是否进行按键操作
	private volatile boolean isThreadLive = false;
	private static final int PLAYING_TIME_INTERVAL = 1 * 1000;
	private final int START_TO_SEEK = 50000; //开发回看标志
	private VTimeView timeView ;
	private int seekWidth = 0;
	private ChannelItem channelItem = null;
	private int PLAY_MODE = ViewConst.BACK_STATE;
	private long msec = 0;
	private long lastMsec = 0;
	
	private Handler seekHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case START_TO_SEEK:
				LogUtil.i("BackPlayView--->handleMessage--->totalTime::"+totalTime+"::msec"+msec);
				if(PLAY_MODE == ViewConst.TIMESHIFT_STATE){
					if(msec > lastMsec){
						if(totalTime - currentProgress >15)
						{
							long playtime = TimeManager.GetInstance().getCurrentTime() - (totalTime - currentProgress)*1000;
							seekBarView.setData(getContent(channelItem,playtime));
							iBackPlayListenner.seekTo(playtime);
						}else{
							if(iBackPlayListenner!=null)
							{
								iBackPlayListenner.onBackToLive();
							}
						}
					}else{
						long playtime = TimeManager.GetInstance().getCurrentTime() - (totalTime - currentProgress)*1000;
						seekBarView.setData(getContent(channelItem,playtime));
						iBackPlayListenner.seekTo(playtime);
					}
				}else{
					iBackPlayListenner.seekTo(msec);
				}
				
				
				lastMsec = msec;
				msec = 0;
				break;

			default:
				break;
			}
		};
		
	};
	
	
	public BackPlayView(Context context,PlayerView playerView) {
		super(context);
		this.playerView = playerView;
		init(context);
	}

	private void init(Context ctx){
		this.ctx = ctx;
		TIME_INTERVAL = 1000*5;
		setFocusable(true);
		setFocusableInTouchMode(true);
		setClickable(true);
		setClipChildren(false);
		setBackgroundResource(0);
		this.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT));
		//initTop(ctx);
		initSeekBar(ctx);
		initTimeView(ctx);
		initPauseView(ctx);
		seekWidth = DisplayManager.GetInstance().getScreenWidth();
	}
	
	private void initPauseView(Context ctx){
		iv_pause = new ImageView(ctx);
		iv_pause.setBackgroundResource(R.drawable.cs_back_pause);
		LayoutParams iv_params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		iv_params.bottomMargin = changeHeight(30);
		iv_params.rightMargin = changeWidth(35);
		iv_params.addRule(ABOVE,1001);
		iv_params.addRule(ALIGN_PARENT_RIGHT);
		iv_pause.setLayoutParams(iv_params);
		iv_pause.setVisibility(GONE);
		addView(iv_pause);
	}
	
	private void initSeekBar(Context context){
		
		seekBarView = new VSeekBar(context);
		//seekBarView.setGravity(Gravity.CENTER);
		seekBarView.setId(1001);
		LayoutParams sb_params = new LayoutParams(LayoutParams.MATCH_PARENT,changeHeight(80));
		sb_params.addRule(ALIGN_PARENT_BOTTOM);
		seekBarView.setLayoutParams(sb_params);
		seekBarView.setTotalTextColor(getColor(R.color.light_white));
		//seekBar.setKeyProgressIncrement(60); //每次移动一分钟
		addView(seekBarView);
	}
	
	/**
	 * 初始化timeview
	 * @param context
	 */
	private void initTimeView(Context context) {
		timeView = new VTimeView(context) ;
		RelativeLayout.LayoutParams timeViewParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,changeHeight(100)) ;
		timeView.setId(1002) ;
		timeView.setTimeTextColor(getColor(R.color.light_white));
		timeViewParams.addRule(ABOVE,1001);
		timeViewParams.bottomMargin = changeHeight(-35);
		timeView.setLayoutParams(timeViewParams) ;
		this.addView(timeView) ;
	}
	
	/**
	 * 回看模式时设置回看总时间
	 * @param duration
	 */
	public void setTotalTime(int duration){
		totalTime = duration/1000;
		LogUtil.i("BackPlayView--->setTotalTime--->totalTime::"+totalTime);
		seekBarView.setMaxSeek(totalTime, 60,PLAY_MODE);
	}
	
	public void setData(ProgramItem programItem){
		if(isFirst){
			isFirst = false;
			PLAY_MODE = ViewConst.BACK_STATE;
			startTimer();
			String content = programItem.getProgramName();
			
			if(TextUtils.isEmpty(content)){
				content = ctx.getResources().getString(R.string.none_program);
			}
			seekBarView.setData(content);
		}
		if(currentProgress>totalTime){
			 currentProgress = totalTime;
		}
		//seekBarView.setVProgress(currentProgress);
		setProgress(currentProgress);
	}
	
	/**
	 * 时移状态下设置
	 * @param item
	 */
	public void setData(ChannelItem item){
		if(isFirst){
			isFirst = false;
			PLAY_MODE = ViewConst.TIMESHIFT_STATE;
			channelItem = item;
			seekBarView.setData(getContent(item,TimeManager.GetInstance().getCurrentTime()));
			pausePlay();
		}
		/*if(currentProgress>totalTime){
			 currentProgress = totalTime;
		}
		setProgress(currentProgress);*/
	}
	
	private String getContent(ChannelItem item,long msec){
		ProgramItem programItem = getCurrentContent(item,msec);
		String content = programItem!=null?programItem.getProgramName():null;
		
		if(TextUtils.isEmpty(content)){
			content = ctx.getResources().getString(R.string.none_program);
		}
		return content;
	}
	
	public void updateTime(){
		long curTime = TimeManager.GetInstance().getCurrentTime();
		String curStr = formatTime(curTime);
		long playtime = curTime - (totalTime - currentProgress)*1000;
		String playStr = formatTime(playtime);
		seekBarView.setData(getContent(channelItem,playtime));
		seekBarView.setTimeStr(playStr, curStr);
		timeView.updateTimeText(playStr);
	}
	
	private String formatTime(long msec){
		return DateUtil.msec2String(msec, "kk:mm:ss");
	}
	
	private ProgramItem getCurrentContent(ChannelItem channelItem,long time){
		if(channelItem!=null&&channelItem.getDateList()!=null&&channelItem.getDateList().size()>0){
			List<ProgramItem> programItems = channelItem.getDateList().get(0).getProgramItemList();
			int index = findCurrentPlayingProgramIndex(programItems, time);
			if(index>=0){
				return programItems.get(index);
			}
		}
		return null;
	}
	
	private int findCurrentPlayingProgramIndex(List<ProgramItem> programItems,long timeMsec) {
		if (programItems != null && programItems.size() > 0) {
			for (int i = 1; i < programItems.size(); i++) {
				//String time = programItems.get(i).getStartTime();
				//long msec = DateUtil.string2Msec(time, "yyyy-MM-dd kk:mm:ss");
				long msec = programItems.get(i).getlStartTime();
				if (timeMsec < msec) {
					return i - 1;
				}
			}
			return programItems.size() - 1;
		} 
		return -1;
	}
	
	
	private int changeWidth(int width){
		return DisplayManager.GetInstance().changeWidthSize(width);
	}
	
	private int changeHeight(int height){
		return DisplayManager.GetInstance().changeHeightSize(height);
	}
	
	private int changeTextSize(int size){
		return DisplayManager.GetInstance().changeTextSize(size);
	}
	
	private int getColor(int id)
	{
		return ctx.getResources().getColor(id);
	}
	
	public void show(){
		LogUtil.i("BackPlayView--->show--->VISIBLE::"+getVisibility());
		if(getVisibility()!=VISIBLE){
			setVisibility(VISIBLE);
			requestFocus();
		}
		LogUtil.i("BackPlayView--->show--->VISIBLE::"+getVisibility());
	}
	
	public void hide(){
		LogUtil.i("BackPlayView--->hideBackPlayView");
		setVisibility(GONE);
		isTouchDown = false;
	}
	
	private void setProgress(int progress){
		seekBarView.setVProgress(progress,PLAY_MODE);
		//int seekWidth = DisplayManager.GetInstance().getScreenWidth();
		float halfWidth = timeView.getVTWidth()/2;
		float left = totalTime!=0?((float)(progress * seekWidth/totalTime)):0;
		if(left<halfWidth){
			left = 0;
		}else if((seekWidth - left)<halfWidth){
			left = seekWidth - halfWidth*2;
		}else{
			left = left - halfWidth;
		}
		String playStr = "";
		if(PLAY_MODE == ViewConst.BACK_STATE){
			playStr = secondToString(progress);
		}else{
			long curTime = TimeManager.GetInstance().getCurrentTime();
			String curStr = formatTime(curTime);
			playStr = formatTime(curTime - (totalTime - currentProgress)*1000);
			seekBarView.setTimeStr(playStr, curStr);
		}
		timeView.setProgress(left,playStr);
	}
	
	private String secondToString(int second){
		int s = second % 60;
		int m = second / 60 % 60;
		int h = second / 60 / 60;
		return (h<10 ? "0" + h : h)  + ":" + (m<10 ? "0" + m : m ) + ":" + (s<10 ? "0" + s : s);  
	}
	
	int speed = 9;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtil.i("BackPlayView--------->onKeyDown----->"+keyCode);
		isTouchDown = true;
		startDisplayTimer();
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(iv_pause.getVisibility() == VISIBLE){
				resumePlay();
			}
			hide();
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if(iv_pause.getVisibility() == VISIBLE){
				resumePlay();
			}
			cancelTimer();
			if(currentProgress >= speed){
				currentProgress -= speed;
			}else{
				currentProgress = 1;
			}
			if(speed<30){
				speed += 9;
			}
			isOnKeyDown = true;
			//playerView.isSeeking = true;
			setProgress(currentProgress);
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if(iv_pause.getVisibility() == VISIBLE){
				resumePlay();
			}
			cancelTimer();
			if(currentProgress < totalTime - speed){
				currentProgress += speed;
			}else{
				currentProgress = totalTime;
			}
			if(speed<30){
				speed += 9;
			}
			isOnKeyDown = true;
			//playerView.isSeeking = true;
			//seekBarView.setVProgress(currentProgress);
			setProgress(currentProgress);
			break;
		default:
			playerView.onKeyDown(keyCode, event);
			break;
		}
		return true;
	}
	
	private volatile boolean threadState = false;
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		LogUtil.i("BackPlayView--------->onKeyUp----->"+keyCode);
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			speed = 9;
			if(isTouchDown){ //屏蔽首次进入回看只掉onkeyup方法
				if(iBackPlayListenner!=null){
					
					if(currentProgress>=totalTime){
						msec = currentProgress*1000-1;
					}else{
						msec = currentProgress*1000;
					}
					startTimer();
				}
				//isOnKeyDown = true;
				isThreadLive = true;
				if(!threadState){
					threadState = true;
					LogUtil.i("BackPlayView--->onKeyUp--->启动seek线程");
					seekThread seekThread = new seekThread();
					seekThread.start();
				}
			}
			
			break;
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
			if(iv_pause.getVisibility() == VISIBLE){
				if(totalTime - currentProgress >15){
					resumePlay();
				}else{
					if(iBackPlayListenner!=null){
						iBackPlayListenner.onBackToLive();
					}
				}
				
			}else{
				pausePlay();
			}
			break;
		default:
			return playerView.onKeyUp(keyCode, event);
		}
		return true;
	}
	
	private class seekThread extends Thread{
		public void run() {
			flag:
			while(isThreadLive){
				while(!isOnKeyDown){
					try {
						Thread.sleep(10);
						lastWaitTime +=10;
						if(lastWaitTime>=1000){
							lastWaitTime = 0;
							isThreadLive = false;
							break flag;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				while(lastWaitTime<1000){
					if(isOnKeyDown){
						isOnKeyDown = false;
						lastWaitTime=0;
					}
					try {
						Thread.sleep(10);
						lastWaitTime+=10;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				lastWaitTime = 0;
				isOnKeyDown = false;
				seekHandler.sendEmptyMessage(START_TO_SEEK);
			}
			threadState = false;
		};
		
	};

	public void setiBackPlayListenner(IBackPlayListenner iBackPlayListenner) {
		this.iBackPlayListenner = iBackPlayListenner;
	}

	@Override
	protected void timeOut() {
		if(iBackPlayListenner!=null){
			iBackPlayListenner.onTimeOut();
		}
	}
	
	public void release(){
		currentProgress = 0;
		lastMsec = 0;
		totalTime = 0;
		isFirst = true;
	}
	
	private void startTimer(){
		if(PLAY_MODE == ViewConst.BACK_STATE){
			LogUtil.d("BackPlayView--->startTimer");
			cancelTimer();
			if(timer == null){
				timer = new Timer();
				timerTask = new PlayTimeTask();
				timer.schedule(timerTask, 1000,PLAYING_TIME_INTERVAL);
			}
		}else{
			LogUtil.d("BackPlayView--->startTimer--->时移状态不启动计时");
		}
	}
	private void cancelTimer(){
		if(PLAY_MODE == ViewConst.BACK_STATE){
			LogUtil.d("BackPlayView--->cancelTimer");
			if (timerTask != null) {
				timerTask.cancel();
				timerTask = null;
			}
			if (timer != null) {
				timer.cancel();
				timer.purge();
				timer = null;
			}
		}else{
			LogUtil.d("BackPlayView--->cancelTimer--->时移状态不启动计时");
		}
		
	}
	
	private class PlayTimeTask extends TimerTask{
		@Override
		public void run() {
			currentProgress +=1;
		}
		
	}
	
	private void startShiftTimer(){
		if(PLAY_MODE == ViewConst.TIMESHIFT_STATE){
			LogUtil.d("BackPlayView--->startShiftTimer");
			cancelShiftTimer();
			if(shiftTimer == null){
				shiftTimer = new Timer();
				shiftTimerTask = new ShiftTimerTask();
				shiftTimer.schedule(shiftTimerTask, 1000,PLAYING_TIME_INTERVAL);
			}
		}else{
			LogUtil.d("BackPlayView--->startShiftTimer--->回看状态不启动计时");
		}
	}
	
	private void cancelShiftTimer(){
		if(PLAY_MODE == ViewConst.TIMESHIFT_STATE){
			LogUtil.d("BackPlayView--->cancelShiftTimer");
			if (shiftTimerTask != null) {
				shiftTimerTask.cancel();
				shiftTimerTask = null;
			}
			if (shiftTimer != null) {
				shiftTimer.cancel();
				shiftTimer.purge();
				shiftTimer = null;
			}
		}else{
			LogUtil.d("BackPlayView--->cancelShiftTimer--->回看状态不启动计时");
		}
		
	}
	
	private class ShiftTimerTask extends TimerTask{
		@Override
		public void run() {
			if(currentProgress>0){
				currentProgress -=1;
			}
		}
		
	}
	
	
	private void pausePlay(){
		cancelTimer();
		cancelDisplayTimer();
		startShiftTimer();
		timeView.setVisibility(GONE);
		iv_pause.setVisibility(VISIBLE);
		if(iBackPlayListenner!=null){
			iBackPlayListenner.onPause();
		}
	}
	
	private void resumePlay(){
		startTimer();
		startDisplayTimer();
		cancelShiftTimer();
		iv_pause.setVisibility(GONE);
		timeView.setVisibility(VISIBLE);
		if(iBackPlayListenner!=null){
			long playtime = TimeManager.GetInstance().getCurrentTime() - (totalTime - currentProgress)*1000;
			setProgress(currentProgress);
			iBackPlayListenner.onResume(playtime);
		}
	}
	
	/**
	 * 设置进度条进度
	 * @param progress
	 */
	public void setSeek(int progress){
		currentProgress = progress;
		//seekBarView.setVProgress(progress);
		setProgress(progress);
	}
	
	private float getTextSize(int id){
		float size = getContext().getResources().getDimension(id);
		Log.i("textsize","textsize::"+ size);
		return size;
	}
	
	
}
