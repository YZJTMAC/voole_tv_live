package com.gntv.tv.view;

import com.gntv.tv.R;
import com.gntv.tv.common.utils.DateUtil;
import com.gntv.tv.model.time.TimeManager;
import com.gntv.tv.view.base.DisplayManager;
import com.gntv.tv.view.base.ViewConst;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class VSeekBar extends RelativeLayout {
	private OnVSeekBarListener mSeekBarListener;

	private TextView timeTextView;
	private TextView currentTimeView;
	private TextView nameTextView;
	private SeekBar seekBar ;
	private String totalTime = "23:59:59";
	private int max = 100;
	private Context ctx = null;

	public VSeekBar(Context context) {
		this(context, null,0);
	}
	public VSeekBar(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	@SuppressLint("NewApi")
	public VSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, 0);
		ctx = context;
		this.setDescendantFocusability(FOCUS_AFTER_DESCENDANTS) ;
		//setBackgroundResource(R.drawable.cs_back_background);
		setBackgroundColor(0xe6303030);
		this.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT));
		setClipChildren(false);
		initSeekView(context) ;
	}

	public int getSeekWidth(){
		return seekBar.getMeasuredWidth();
	}
	
	/**
	 * 初始化seekview
	 * @param context
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void initSeekView(Context context) {
		seekBar = (SeekBar) LayoutInflater.from(context).inflate(R.layout.myseekbar, null);
		seekBar.setId(1001);
		RelativeLayout.LayoutParams seekbarParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT) ;
		seekbarParams.topMargin = changeHeight(-6);
		seekBar.setLayoutParams(seekbarParams) ;
		seekBar.setFocusable(true) ;
		seekBar.requestFocus() ;
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				if(mSeekBarListener != null){
					mSeekBarListener.onStopTrackingTouch();
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				if(mSeekBarListener != null){
					mSeekBarListener.onStartTrackingTouch();
				}
			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				//setVProgress(arg1) ;
				if(mSeekBarListener != null){
					mSeekBarListener.onProgressChanged(arg1,arg2);
				}
			}
		}) ;
		addView(seekBar) ;
		
		nameTextView = new TextView(context);
		nameTextView.setId(111);
		nameTextView.setTextColor(Color.WHITE);
		nameTextView.setText("疯狂动物城");
		nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,getTextSize(R.dimen.BACK_SIZE));
		RelativeLayout.LayoutParams nameParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		nameParams.addRule(RelativeLayout.CENTER_VERTICAL);
		nameParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		nameParams.setMargins(changeWidth(40),0,0, 0);
		nameTextView.setLayoutParams(nameParams);
		addView(nameTextView) ;
		
		timeTextView = new TextView(context);
		timeTextView.setId(112);
		timeTextView.setTextColor(Color.WHITE);
		timeTextView.setText(totalTime);
		timeTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,getTextSize(R.dimen.BACK_SIZE));
		RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		textParams.addRule(RelativeLayout.CENTER_VERTICAL);
		textParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		textParams.setMargins(0,0,changeWidth(40), 0);
		timeTextView.setLayoutParams(textParams);
		addView(timeTextView);
		
		currentTimeView = new TextView(context);
		currentTimeView.setId(113);
		currentTimeView.setTextColor(Color.WHITE);
		currentTimeView.setText("00:00:00/");
		currentTimeView.setTextSize(TypedValue.COMPLEX_UNIT_PX,getTextSize(R.dimen.BACK_SIZE));
		RelativeLayout.LayoutParams curParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		curParams.addRule(RelativeLayout.CENTER_VERTICAL);
		curParams.addRule(RelativeLayout.LEFT_OF,112);
		currentTimeView.setLayoutParams(curParams);
		addView(currentTimeView);
		
	}

	public void setEnabled(boolean enabled) {
		seekBar.setEnabled(enabled) ;
	}
	/**
	 *  设置最大值和比例
	 * @param max 最大值
	 * @param increment	进度比例
	 */
	public void setMaxSeek(int max,int increment,int mode) {
		this.max = max ;
		seekBar.setMax(max) ;
		seekBar.setKeyProgressIncrement(increment) ;
		if(mode == ViewConst.BACK_STATE){
			setMaxTime(secondToString(max));
		}
	}
	
	public void setOnVSeekBarListener(OnVSeekBarListener l){
		this.mSeekBarListener = l;
	}

	/**
	 * total时间
	 * @param totalTime
	 */
	private void setMaxTime(String totalTime) {
		timeTextView.setText(totalTime) ;
	}
	
	public void setTimeStr(String playStr,String curStr){
		currentTimeView.setText(playStr+"/");
		timeTextView.setText(curStr);
	}

	/**
	 * 设置进度和显示的时间
	 * @param seek	当前进度
	 */
	public void setVProgress(int seek,int mode) {
		seekBar.setProgress(seek);
		if(mode == ViewConst.BACK_STATE){
			currentTimeView.setText(secondToString(seek)+"/");
		}
	}
	
	public int getVProgress() {
		return seekBar.getProgress() ;
	}

	public void setTotalTextColor(int color) {
		timeTextView.setTextColor(color) ;
	}

	
	private String secondToString(int second){
		int s = second % 60;
		int m = second / 60 % 60;
		int h = second / 60 / 60;
		return (h<10 ? "0" + h : h)  + ":" + (m<10 ? "0" + m : m ) + ":" + (s<10 ? "0" + s : s);  
	}

	public static interface OnVSeekBarListener {
		  void onProgressChanged(int progress, boolean isFromUser);
		  void onStartTrackingTouch();
	      void onStopTrackingTouch();
	}
	
	public void setData(String text){
		nameTextView.setText(text);
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
	
	private float getTextSize(int id){
		float size = getContext().getResources().getDimension(id);
		Log.i("textsize","textsize::"+ size);
		return size;
	}
	
	
}
