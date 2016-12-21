package com.gntv.tv.view.base;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public abstract class BaseChannelRelativeLayout extends RelativeLayout{
	protected Context ctx = null;
	private boolean isIn = false;
	public BaseChannelRelativeLayout(Context context) {
		super(context);
		ctx = context;
		setFocusable(true);
		setFocusableInTouchMode(true);
		setWillNotDraw(false);  
		//init(context);
	}
	
	protected abstract void init(Context context);
	
	protected int changeWidth(int width){
		return DisplayManager.GetInstance().changeWidthSize(width);
	}
	
	protected int changeHeight(int height){
		return DisplayManager.GetInstance().changeHeightSize(height);
	}
	
	protected int changeTextSize(int size){
		return DisplayManager.GetInstance().changeTextSize(size);
	}
	
	protected int getColor(int id) {
		return ctx.getResources().getColor(id);
	}
	
	public boolean isIn() {
		return isIn;
	}

	public void setIn(boolean isIn) {
		this.isIn = isIn;
	}

	public abstract void focusEvent(boolean isFocus);
	
	public abstract void clearAllSelected();

}
