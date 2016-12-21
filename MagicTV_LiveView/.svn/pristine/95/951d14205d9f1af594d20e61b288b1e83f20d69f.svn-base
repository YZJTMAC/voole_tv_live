package com.gntv.tv.view;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gntv.tv.R;
import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.model.channel.ChannelItem;
import com.gntv.tv.view.base.DisplayManager;
import com.gntv.tv.view.base.FontSize;
import com.gntv.tv.view.base.TimerLinearLayout;

public class NumChangeChannelView extends TimerLinearLayout {
	private TextView numShowView = null;
	private INumChangeChannelViewListener iNumChangeChannelViewListener = null;
	private Timer showNumTimer = null;
	private ShowTimeTask showNumTimerTask = null;
	private static final int SHOW_NUM_INTERVAL = 1000 * 2;
	private String num = "";
	private static final int SHOW_TIP_INFO = 1;
	private Context ctx = null;
	
	private Handler ncHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_TIP_INFO:
				setVisibility(View.VISIBLE);
				numShowView.setText(R.string.num_tip);
				numShowView.setTextSize(DisplayManager.GetInstance().changeTextSize(FontSize.NUMBER_SIZE2));
				break;

			default:
				break;
			}
		};
	};

	public NumChangeChannelView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public NumChangeChannelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public NumChangeChannelView(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context){
		//setBackgroundColor(0xaacccccc);
		ctx = context;
		setBackgroundResource(R.drawable.cs_programmenu_bg);
		setGravity(Gravity.CENTER);
		numShowView = new TextView(context);
		numShowView.setGravity(Gravity.CENTER);
		//numShowView.setBackgroundColor(0xaaff0000);
		numShowView.setTextColor(Color.WHITE);
		numShowView.setTextSize(DisplayManager.GetInstance().changeTextSize(FontSize.NUMBER_SIZE1));
		LinearLayout.LayoutParams param_num = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		numShowView.setLayoutParams(param_num);
		TIME_INTERVAL = 2000;
		addView(numShowView);
	}

	@Override
	protected void timeOut() {
		if(iNumChangeChannelViewListener!=null){
			iNumChangeChannelViewListener.onTimeOut();
		}
	}

	public void setiNumChangeChannelViewListener(
			INumChangeChannelViewListener iNumChangeChannelViewListener) {
		this.iNumChangeChannelViewListener = iNumChangeChannelViewListener;
	}
	
	public void hide(){
		setVisibility(View.GONE);
	}
	
	public void show(String num){
		if(iNumChangeChannelViewListener!=null){
			this.num = num;
			cancelShowNumTimer();
			if(num.length()>3){
				iNumChangeChannelViewListener.clearNum();
				numShowView.setText(R.string.num_tip);
				numShowView.setTextSize(DisplayManager.GetInstance().changeTextSize(FontSize.NUMBER_SIZE2));
			}else{
				numShowView.setText(num);
				numShowView.setTextSize(DisplayManager.GetInstance().changeTextSize(FontSize.NUMBER_SIZE1));
				startShowNumTimer();
			}
			
			setVisibility(View.VISIBLE);
		}
	}
	
	private class ShowTimeTask extends TimerTask {
		@Override
		public void run() {
			if(iNumChangeChannelViewListener!=null){
				int showNumInt = Integer.parseInt(num);
				ChannelItem item = iNumChangeChannelViewListener.isChannelAvailable(showNumInt+"");
				if(item!=null){
					iNumChangeChannelViewListener.onChannelChange(item);
				}else{
					startDisplayTimer();
					Message msg = Message.obtain();
					msg.what = SHOW_TIP_INFO;
					ncHandler.sendMessage(msg);
				}
				iNumChangeChannelViewListener.clearNum();
			}
		}
	}
	
	private void startShowNumTimer() {
		LogUtil.d("startExitTimer");
		if(showNumTimer == null){
			showNumTimer = new Timer();
			showNumTimerTask = new ShowTimeTask();
			showNumTimer.schedule(showNumTimerTask, SHOW_NUM_INTERVAL);
		}
	}

	private void cancelShowNumTimer() {
		LogUtil.d("cancelExitTimer");
		if (showNumTimerTask != null) {
			showNumTimerTask.cancel();
			showNumTimerTask = null;
		}
		if (showNumTimer != null) {
			showNumTimer.cancel();
			showNumTimer.purge();
			showNumTimer = null;
		}
	}
	
	public void setBackMode(){
		setVisibility(VISIBLE);
		numShowView.setText("回看");
		cancelDisplayTimer();
	}
	
	public void setShiftingMode(){
		setVisibility(VISIBLE);
		numShowView.setText("时移");
		cancelDisplayTimer();
	}

}
