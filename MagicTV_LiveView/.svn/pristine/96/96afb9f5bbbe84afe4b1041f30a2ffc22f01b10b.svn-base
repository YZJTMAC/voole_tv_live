package com.gntv.tv.view;

import com.gntv.tv.R;
import com.gntv.tv.view.base.DisplayManager;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ErrorView extends LinearLayout {
	private TextView tipText = null;
	private TextView contactText = null;
	private Context ctx = null;
	
	public ErrorView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context){
		ctx = context;
		setOrientation(VERTICAL);
		setGravity(Gravity.CENTER);
		setBackgroundResource(R.drawable.bg_detect);
		tipText = new TextView(context);
		LayoutParams tipParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		tipText.setLayoutParams(tipParams);
		tipText.setId(1001);
		tipText.setTextSize(changeTextSize(30));
		tipText.setGravity(Gravity.CENTER);
		tipText.setText("节目出错，请切台重试");
		tipText.setTextColor(getColor(R.color.light_white)); 
		
		contactText = new TextView(context);
		LayoutParams contactParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		contactText.setLayoutParams(contactParams);
		contactText.setTextSize(changeTextSize(22));
		contactText.setGravity(Gravity.CENTER);
		contactText.setTextColor(getColor(R.color.dark_text)); 
		addView(tipText);
		addView(contactText);
	}
	
	public void setTipInfo(String info,String contactInfo){
		tipText.setText(info);
		contactText.setText(contactInfo);
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
	
	protected int getColor(int id) {
		return ctx.getResources().getColor(id);
	}
	
	public void hide(){
		setVisibility(GONE);
	}
}
