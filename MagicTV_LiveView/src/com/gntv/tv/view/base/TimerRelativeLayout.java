package com.gntv.tv.view.base;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

public abstract class TimerRelativeLayout extends RelativeLayout{
	protected int TIME_INTERVAL = 1000 * 10;
	public TimerRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TimerRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TimerRelativeLayout(Context context) {
		super(context);
	}
	
	private Timer displayTimer = null;
	private TimerTask displayTimerTask = null;
	private class DisplayTimeTask extends TimerTask{
		@Override
		public void run() {
			timeOut();
		}
	}
	
	protected abstract void timeOut();
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		startDisplayTimer();
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return super.onKeyUp(keyCode, event);
	}
	
	@Override
	protected void onAttachedToWindow() {
		startDisplayTimer();
		super.onAttachedToWindow();
	}
	
	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		if(visibility == View.VISIBLE){
			startDisplayTimer();
		}else{
			cancelDisplayTimer();
		}
		super.onVisibilityChanged(changedView, visibility);
	}
	
	public void startDisplayTimer(){
		cancelDisplayTimer();
		displayTimer = new Timer();
		displayTimerTask = new DisplayTimeTask();
		displayTimer.schedule(displayTimerTask, TIME_INTERVAL);
	}
	
	public void cancelDisplayTimer(){
		if (displayTimer != null) {
			displayTimer.cancel();
			displayTimer = null;
		}
		if (displayTimerTask != null) {
			displayTimerTask.cancel();
			displayTimerTask = null;
		}
	}
}
