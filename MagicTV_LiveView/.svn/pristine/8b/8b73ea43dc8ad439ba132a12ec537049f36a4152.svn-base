package com.gntv.tv.view.base;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

public abstract class TimerLinearLayout extends LinearLayout{
	protected int TIME_INTERVAL = 1000 * 10;
	public TimerLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TimerLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TimerLinearLayout(Context context) {
		super(context);
	}
	
	private Timer displayTimer = null;
	private TimerTask displayTimerTask = null;
	private static class DisplayTimeTask extends TimerTask{
		
		private  WeakReference<TimerLinearLayout> layoutRef;
		
		public DisplayTimeTask(TimerLinearLayout timerLinearLayout) {
			super();
			layoutRef = new WeakReference<TimerLinearLayout>(timerLinearLayout);
		}


		@Override
		public void run() {
			TimerLinearLayout layout = layoutRef.get();
			
			if(layout!=null){
				layout.timeOut();
			}
		}
	}
	
	protected abstract void timeOut();
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//startDisplayTimer();
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
	
	protected void startDisplayTimer(){
		cancelDisplayTimer();
		displayTimer = new Timer();
		displayTimerTask = new DisplayTimeTask(this);
		displayTimer.schedule(displayTimerTask, TIME_INTERVAL);
	}
	
	protected void cancelDisplayTimer(){
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
