package com.gntv.tv.view.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.Button;

import com.gntv.tv.R;

public class BaseButton extends Button {

	public BaseButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BaseButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public BaseButton(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context){
		setFocusable(true);
		setFocusableInTouchMode(true);
		setTextColor(Color.WHITE);
		setPadding(0, 2, 0, 0);
		setTextSize(DisplayManager.GetInstance().changeTextSize(28));
		setBackgroundResource(R.drawable.cs_base_button_unfocused);
	}
	
	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
		if(focused){
			setBackgroundResource(R.drawable.cs_channel_focus);
		}else{
			setBackgroundResource(R.drawable.cs_base_button_unfocused);
		}
	}

}
