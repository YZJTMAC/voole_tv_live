package com.gntv.tv.view;

import com.gntv.tv.R;
import com.gntv.tv.view.base.DisplayManager;
import com.gntv.tv.view.base.FontSize;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class TVBufferView extends RelativeLayout {
	private ProgressBar progressBar = null;
	private TextView txtMsg = null;
	private TextView txtSpeed = null;

	public TVBufferView(Context context) {
		super(context);
		init(context);
	}

	public TVBufferView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public TVBufferView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context){
		progressBar = new ProgressBar(getContext());
		progressBar.setId(1001);
		progressBar.setIndeterminateDrawable(getContext().getResources().getDrawable(R.drawable.progress_wait));
		progressBar.setIndeterminate(true);
		//LinearInterpolator interpolator = new LinearInterpolator();
		progressBar.setInterpolator(new LinearInterpolator());
		RelativeLayout.LayoutParams param_bar = new RelativeLayout.LayoutParams(DisplayManager.GetInstance().changeWidthSize(100),
								DisplayManager.GetInstance().changeHeightSize(100));
		param_bar.addRule(RelativeLayout.CENTER_IN_PARENT);
		progressBar.setLayoutParams(param_bar);
		addView(progressBar);
		
		txtSpeed = new TextView(getContext());
		txtSpeed.setId(1002);
		txtSpeed.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DisplayManager.GetInstance().changeTextSize(FontSize.BUFFER_SPEED));
		RelativeLayout.LayoutParams param_txtSpeed = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT, android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		param_txtSpeed.topMargin = DisplayManager.GetInstance().changeHeightSize(15);
		param_txtSpeed.addRule(RelativeLayout.BELOW,1001);
		param_txtSpeed.addRule(RelativeLayout.CENTER_HORIZONTAL);
		txtSpeed.setLayoutParams(param_txtSpeed);
		addView(txtSpeed);
		
		txtMsg = new TextView(getContext());
		txtMsg.setText("提示：OK键呼出节目单");
		txtMsg.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DisplayManager.GetInstance().changeTextSize(FontSize.BUFFER_TEXT));
		RelativeLayout.LayoutParams param_txt = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT, android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		param_txt.topMargin = DisplayManager.GetInstance().changeHeightSize(10);
		param_txt.addRule(RelativeLayout.BELOW, 1002);
		param_txt.addRule(RelativeLayout.CENTER_HORIZONTAL);
		txtMsg.setLayoutParams(param_txt);
		addView(txtMsg);
	}
	
	public void setHintInfo(String speed,String hint){
		txtSpeed.setVisibility(VISIBLE);
		txtSpeed.setText(speed);
		if(!TextUtils.isEmpty(hint)){
			txtMsg.setText(hint);
		}
	}
	
	public void setHintInfo(String hint){
		txtSpeed.setVisibility(GONE);
		if(!TextUtils.isEmpty(hint)){
			txtMsg.setText(hint);
		}
	}
	
}
