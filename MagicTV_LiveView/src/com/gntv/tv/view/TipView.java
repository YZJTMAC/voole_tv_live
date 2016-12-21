package com.gntv.tv.view;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.gntv.tv.R;
import com.gntv.tv.view.base.DisplayManager;
import com.gntv.tv.view.base.TimerLinearLayout;
import com.gntv.tv.view.base.TimerRelativeLayout;

public class TipView extends TimerRelativeLayout {
	private Context context = null;
	private TextView tv_tip = null;
	
	public TipView(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context){
		tv_tip = new TextView(context);
		tv_tip.setBackgroundResource(R.drawable.cs_back_bottom);
		tv_tip.setGravity(Gravity.CENTER);
		tv_tip.setTextSize(changeTextSize(30));
		tv_tip.setText(getString(R.string.back_tip));
		tv_tip.setTextColor(getColor(R.color.light_white));
		LayoutParams tv_params = new LayoutParams(changeWidth(150),
				changeHeight(50));
		tv_params.bottomMargin = changeHeight(20);
		tv_params.addRule(ALIGN_PARENT_BOTTOM);
		tv_params.addRule(ALIGN_PARENT_RIGHT);
		tv_tip.setLayoutParams(tv_params);
		tv_tip.setVisibility(GONE);
		addView(tv_tip);
	}

	@Override
	protected void timeOut() {
		// TODO Auto-generated method stub

	}
	
	private int changeTextSize(int size) {
		return DisplayManager.GetInstance().changeTextSize(size);
	}
	
	private int getColor(int id) {
		return context.getResources().getColor(id);
	}
	
	private String getString(int id) {
		return context.getResources().getString(id);
	}
	
	private int changeWidth(int width) {
		return DisplayManager.GetInstance().changeWidthSize(width);
	}

	private int changeHeight(int height) {
		return DisplayManager.GetInstance().changeHeightSize(height);
	}
}
