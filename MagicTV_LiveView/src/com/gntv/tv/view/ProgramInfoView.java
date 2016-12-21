package com.gntv.tv.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings.TextSize;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gntv.tv.R;
import com.gntv.tv.common.utils.DateUtil;
import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.model.channel.ChannelItem;
import com.gntv.tv.model.channel.ProgramItem;
import com.gntv.tv.model.time.TimeManager;
import com.gntv.tv.report.PageActionReport;
import com.gntv.tv.report.PageActionReport.Action;
import com.gntv.tv.report.PageActionReport.ModuleType;
import com.gntv.tv.report.PageActionReport.PageType;
import com.gntv.tv.view.base.AlwaysMarqueeTextView;
import com.gntv.tv.view.base.DisplayManager;
import com.gntv.tv.view.base.FontSize;
import com.gntv.tv.view.base.TimerLinearLayout;

public class ProgramInfoView extends TimerLinearLayout {
	
	private TextView channelCode = null;
	private AlwaysMarqueeTextView channelName = null;
	
	private AlwaysMarqueeTextView currentPlay = null;
	private AlwaysMarqueeTextView nextPlay = null;
	private Context ctx = null;
	private IProgramInfoViewListener iProgramInfoViewListener = null;
	private PlayerView playerView = null;
	private TextView tv_timer = null;
	private String timeStr = "";
	//private ChannelItem channelItem = null;
	private static final int UPDATE_TIME = 50000;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_TIME:
				if(tv_timer!=null){
					tv_timer.setText(timeStr);
				}
				break;

			default:
				break;
			}
		};
	};

	public ProgramInfoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context,null);
	}

	public ProgramInfoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,null);
	}

	public ProgramInfoView(Context context,PlayerView parent,String isShowTimer) {
		super(context);
		init(context,isShowTimer);
		playerView = parent;
	}

	@Override
	protected void timeOut() {
		if(iProgramInfoViewListener!=null){
			iProgramInfoViewListener.onTimeOut();
		}
	}
	
	private void init(Context context,String isShowTimer){
		setFocusable(true);
		setFocusableInTouchMode(true);
		setClickable(true);
		ctx = context;
		//TIME_INTERVAL = 4000;
		
		setOrientation(HORIZONTAL);
		setGravity(Gravity.CENTER);
		setBackgroundResource(R.drawable.cs_programmenu_bg);
		
		RelativeLayout left_layout = new RelativeLayout(context);
		//left_layout.setBackgroundColor(0xaaff0000);
		left_layout.setPadding(changeWidth(20), 0, changeWidth(20), 0);
		//left_layout.setGravity(Gravity.CENTER);
		//left_layout.setOrientation(VERTICAL);
		RelativeLayout.LayoutParams left_param = new RelativeLayout.LayoutParams(changeWidth(200),LayoutParams.MATCH_PARENT);
		left_layout.setLayoutParams(left_param);
		addView(left_layout);
		
		View lview =  new View(getContext());
		//lview.setBackgroundColor(0xff0000ff);
		lview.setId(100003);
		RelativeLayout.LayoutParams lviewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,1);
		lviewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		lview.setLayoutParams(lviewParams);
		left_layout.addView(lview);
		
		RelativeLayout.LayoutParams txt_param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		channelCode = new TextView(context);
		channelCode.setId(100001);
		channelCode.setGravity(Gravity.CENTER);
		channelCode.setTextSize(TypedValue.COMPLEX_UNIT_PX,getTextSize(R.dimen.CHANNEL_CODE_SIZE));
		//channelCode.getPaint().setFakeBoldText(true);
		channelCode.setTextColor(getColor(R.color.light_white));
		txt_param.addRule(RelativeLayout.ABOVE,100003);
		txt_param.bottomMargin = changeHeight(-10);
		channelCode.setLayoutParams(txt_param);
		left_layout.addView(channelCode);
		
		channelName = new AlwaysMarqueeTextView(context);
		channelName.setGravity(Gravity.CENTER);
		channelName.setTextSize(TypedValue.COMPLEX_UNIT_PX,getTextSize(R.dimen.CHANNEL_INFO_NAME_SIZE));
		channelName.setTextColor(getColor(R.color.light_white));
		//channelName.getPaint().setFakeBoldText(true);
		RelativeLayout.LayoutParams name_param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		name_param.addRule(RelativeLayout.BELOW,100003);
		//name_param.topMargin = changeHeight(-15);
		channelName.setLayoutParams(name_param);
		channelName.setMarqueeRepeatLimit(-1);
		channelName.setEllipsize(TruncateAt.MARQUEE);
		channelName.setSingleLine(true);
		left_layout.addView(channelName);
		
		initMiddleLine(context);
		
		RelativeLayout right_layout=new RelativeLayout(context);
		//right_layout.setBackgroundColor(0xaa00ff00);
		//right_layout.setOrientation(VERTICAL);
		//right_layout.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
		RelativeLayout.LayoutParams right_param=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		right_layout.setPadding(changeWidth(20), 0, changeWidth(20), 0);
		right_layout.setLayoutParams(right_param);
		addView(right_layout);
		if("1".equals(isShowTimer)){
			/*LinearLayout ll_timer = new LinearLayout(context);
			ll_timer.setOrientation(HORIZONTAL);
			ll_timer.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams ll_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			ll_timer.setLayoutParams(ll_params);
			right_layout.addView(ll_timer);
			
			currentPlay = new AlwaysMarqueeTextView(context);
			currentPlay.setTextSize(changeTextSize(FontSize.CHANNEL_INFO_PLAY,currentPlay));
			currentPlay.setSingleLine(true);
			currentPlay.setHorizontallyScrolling(true);
			currentPlay.setEllipsize(TruncateAt.MARQUEE);
			currentPlay.setMarqueeRepeatLimit(-1);
			currentPlay.setHold();
			currentPlay.setTextColor(getColor(R.color.light_white));
			LinearLayout.LayoutParams param_currentPlay = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT,3);
			currentPlay.setLayoutParams(param_currentPlay);
			ll_timer.addView(currentPlay);
			
			tv_timer = new TextView(context);
			tv_timer.setGravity(Gravity.CENTER);
			tv_timer.setTextSize(changeTextSize(25,tv_timer));
			tv_timer.setSingleLine(true);
			tv_timer.setTextColor(getColor(R.color.light_white));
			LinearLayout.LayoutParams tv_params = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT,2);
			tv_timer.setLayoutParams(tv_params);
			ll_timer.addView(tv_timer);*/
			tv_timer = new TextView(context);
			tv_timer.setGravity(Gravity.CENTER);
			tv_timer.setTextSize(TypedValue.COMPLEX_UNIT_PX,getTextSize(R.dimen.TIMER_SIZE));
			tv_timer.setSingleLine(true);
			tv_timer.setTextColor(getColor(R.color.light_white));
			RelativeLayout.LayoutParams tv_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			tv_params.setMargins(0, changeHeight(5), changeWidth(5),0);
			tv_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			tv_timer.setLayoutParams(tv_params);
			right_layout.addView(tv_timer);
			
		}/*else{
			currentPlay = new AlwaysMarqueeTextView(context);
			currentPlay.setTextSize(changeTextSize(FontSize.CHANNEL_INFO_PLAY,currentPlay));
			currentPlay.setSingleLine(true);
			currentPlay.setHorizontallyScrolling(true);
			currentPlay.setEllipsize(TruncateAt.MARQUEE);
			currentPlay.setMarqueeRepeatLimit(-1);
			currentPlay.setHold();
			currentPlay.setTextColor(getColor(R.color.light_white));
			LinearLayout.LayoutParams param_currentPlay = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			currentPlay.setLayoutParams(param_currentPlay);
			right_layout.addView(currentPlay);
		}
		*/
		View rview =  new View(getContext());
		//rview.setBackgroundColor(0xff0000ff);
		rview.setId(200003);
		RelativeLayout.LayoutParams viewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,1);
		viewParams.addRule(RelativeLayout.CENTER_VERTICAL);
		rview.setLayoutParams(viewParams);
		right_layout.addView(rview);
		currentPlay = new AlwaysMarqueeTextView(context);
		currentPlay.setTextSize(TypedValue.COMPLEX_UNIT_PX,getTextSize(R.dimen.CHANNEL_INFO_NAME_SIZE));
		currentPlay.setSingleLine(true);
		currentPlay.setHorizontallyScrolling(true);
		currentPlay.setEllipsize(TruncateAt.MARQUEE);
		currentPlay.setMarqueeRepeatLimit(-1);
		currentPlay.setHold();
		currentPlay.setTextColor(getColor(R.color.light_white));
		RelativeLayout.LayoutParams param_currentPlay = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		param_currentPlay.addRule(RelativeLayout.ABOVE,200003);
		param_currentPlay.bottomMargin = changeHeight(-2);
		currentPlay.setLayoutParams(param_currentPlay);
		right_layout.addView(currentPlay);

		nextPlay = new AlwaysMarqueeTextView(context);
		nextPlay.setTextSize(TypedValue.COMPLEX_UNIT_PX,getTextSize(R.dimen.CHANNEL_INFO_NEXTPLAY_SIZE));
		nextPlay.setSingleLine(true);
		nextPlay.setEllipsize(TruncateAt.MARQUEE);
		nextPlay.setMarqueeRepeatLimit(-1);
		nextPlay.setTextColor(getColor(R.color.dark_text));
		RelativeLayout.LayoutParams param_nextPlay = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		param_nextPlay.addRule(RelativeLayout.BELOW,200003);
		param_nextPlay.topMargin = changeHeight(8);
		//param_nextPlay.leftMargin= changeWidth(20);
		nextPlay.setLayoutParams(param_nextPlay);
		right_layout.addView(nextPlay);

	}
	
	private void initMiddleLine(Context context){	
		ImageView line = new ImageView(context);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		line.setLayoutParams(param);
		param.setMargins(0, changeHeight(25), 0, changeHeight(25));
		line.setBackgroundResource(R.drawable.menu_info_line);
		addView(line);
	}
	
	/*private int changeTextSize(int size,TextView view){
		//view.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
		//return DisplayManager.GetInstance().changeTextSize(size);
		return size;
	}*/
	private float getTextSize(int id){
		float size = ctx.getResources().getDimension(id);
		Log.i("textsize","textsize::"+ size);
		return size;
	}
	
	private int changeWidth(int width){
		return DisplayManager.GetInstance().changeWidthSize(width);
	}
	
	private int changeHeight(int height){
		return DisplayManager.GetInstance().changeHeightSize(height);
	}
	
	public void hide(){
		if(getVisibility() == VISIBLE){
			setVisibility(View.GONE);
			cancelShowTimer(false);
		}
	}

	public void show(){
		if(getVisibility()!=View.VISIBLE){
			synchronized (this) {
				setVisibility(View.VISIBLE);
				requestFocus();
				if(tv_timer!=null){
					//long time = TimeManager.GetInstance().getCurrentTime();
					startShowTimer(0);
				}
			}
		}
	}
	
	public void setData(ChannelItem channelItem,long timeMsec){
		if(channelItem!=null&&channelItem.getChannelNo()!=null&&channelItem.getTitle()!=null){
			channelCode.setText(channelItem.getChannelNo());
			channelName.setText(channelItem.getTitle());
			ProgramItem currentItem = getCurrentContent(channelItem, timeMsec);
			ProgramItem nextItem = getNextContent(channelItem, timeMsec);
			if(currentItem!=null && !TextUtils.isEmpty(currentItem.getProgramName())){
				currentPlay.setText(currentItem.getProgramName());
			}else{
				currentPlay.setText(R.string.none_program);
			}
			
			if(nextItem!=null){
				String startTime = nextItem.getStartTime();
				startTime = getTime(startTime);
				nextPlay.setText(startTime+" " + nextItem.getProgramName());
			}else{
				nextPlay.setText(R.string.none_program);
			}
			
		}
		//testTxt(channelItem);
	}
	
	private String getTime(String timeStr){
		String str = "00:00";
		try {
			str = timeStr.substring(11, 16);
		} catch (Exception e) {
			LogUtil.e("ProgramInfoView--->getTime--->"+e.toString());
		}
		return str;
	}
	
	private ProgramItem getCurrentContent(ChannelItem channelItem,long time){
		if(channelItem.getDateList()!=null&&channelItem.getDateList().size()>0){
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
	
	private ProgramItem getNextContent(ChannelItem channelItem,long timeMsec){
		if(channelItem.getDateList()!=null&&channelItem.getDateList().size()>0){
			List<ProgramItem> programItems = channelItem.getDateList().get(0).getProgramItemList();
			if (programItems != null && programItems.size() > 0) {
				for(ProgramItem item:programItems){
					long msec = item.getlStartTime();
					if(msec>timeMsec){
						return item;
					}
				}
			}
		}
		
		return null;
	}
	
	public void setiProgramInfoViewListener(
			IProgramInfoViewListener iProgramInfoViewListener) {
		this.iProgramInfoViewListener = iProgramInfoViewListener;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtil.i("ProgramInfoView--------->onKeyDown----->"+keyCode);
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			hide();
			PageActionReport.GetInstance().reportPageAction(PageType.PlayPage,null,
					null, null, Action.ExitKey);
			break;
		case KeyEvent.KEYCODE_MENU:
			hide();
			return false;
		default:
			playerView.onKeyDown(keyCode, event);
			break;
		}
		return true;
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		LogUtil.i("ProgramInfoView--------->onKeyUp----->"+keyCode);
		return playerView.onKeyUp(keyCode, event);
	}
	
	protected int getColor(int id) {
		return ctx.getResources().getColor(id);
	}
	
	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		Log.i("ly", "programInfoView---visibe::"+visibility);
	}
	
	private Timer showTimer = null;
	private showTimerTask showTimerTask = null;
	
	private class showTimerTask extends TimerTask{
		private long time = 0;
		@SuppressLint("SimpleDateFormat")
		private SimpleDateFormat sdf = new SimpleDateFormat("mm:ss.SSS"); 
		public showTimerTask(long time) {
			super();
			this.time = time;
		}

		@Override
		public void run() {
			timeStr = sdf.format(new Date(time));
			time+=100;
			Message msg = handler.obtainMessage();
			msg.what = UPDATE_TIME;
			handler.sendMessage(msg);
		}
	}
	private void startShowTimer(long time){
		cancelShowTimer(false);
		LogUtil.i("ProgramInfoView--->startShowTimer");
		showTimer = new Timer();
		showTimerTask = new showTimerTask(time);
		showTimer.schedule(showTimerTask,0,100);
	}
	
	public void cancelShowTimer(boolean isLog){
		LogUtil.i("ProgramInfoView--->cancelShowTimer");
		if(isLog){
			LogUtil.i("changeTime::"+channelCode.getText()+"--->"+channelName.getText()+"--->"+timeStr);
		}
		if (showTimer != null) {
			showTimer.cancel();
			showTimer = null;
		}
		if (showTimerTask != null) {
			showTimerTask.cancel();
			showTimerTask = null;
		}
	}
}
