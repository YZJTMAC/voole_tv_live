package com.gntv.tv.view;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;

import com.gntv.tv.R;
import com.gntv.tv.view.base.AlwaysMarqueeTextView;
import com.gntv.tv.view.base.BaseChannelLinearLayout;
import com.gntv.tv.view.base.FontSize;

public class DateItemView extends BaseChannelLinearLayout {
	private AlwaysMarqueeTextView txtWeek = null;
	private AlwaysMarqueeTextView txtTime = null;
	private boolean tempPlaying = false;
	
	public DateItemView(Context context) {
		super(context);
		init(context);
	}

	@Override
	protected void init(Context context) {
		setOrientation(VERTICAL);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		txtWeek = new AlwaysMarqueeTextView(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(changeWidth(25), 0, 0, 0);
		txtWeek.setTextSize(changeTextSize(FontSize.CHANNEL_NAME));
		txtWeek.setTextColor(getColor(R.color.deep_text));
		txtWeek.setLayoutParams(params);
		addView(txtWeek);
		
		txtTime = new AlwaysMarqueeTextView(context);
		txtTime.setTextSize(changeTextSize(FontSize.CHANNEL_SUB_NAME));
		txtTime.setTextColor(getColor(R.color.deep_text));
		txtTime.setLayoutParams(params);
		addView(txtTime);
	}

	public void setData(String week,String time){
		txtWeek.setText(week);
		txtTime.setText(time);
	}
	
	public String getDataStr(){
		return txtWeek.getText()+" "+txtTime.getText()+" 节目";
	}
	
	public void setPlaying(boolean isPlaying){
		tempPlaying = isPlaying;
		if(isPlaying){
			txtWeek.setTextColor(getColor(R.color.dark_blue_text));
			txtTime.setTextColor(getColor(R.color.dark_blue_text));
		}else{
			txtWeek.setTextColor(getColor(R.color.deep_text));
			txtTime.setTextColor(getColor(R.color.deep_text));
		}
	}
	
	public void setSelected(boolean isSelected){
		if(isSelected){
			txtWeek.setTextColor(getColor(R.color.light_blue_text));
			txtTime.setTextColor(getColor(R.color.light_blue_text));
			this.setBackgroundResource(R.drawable.border_shape);
		}else{
			txtWeek.setTextColor(getColor(R.color.dark_text));
			txtTime.setTextColor(getColor(R.color.dark_text));
			this.setBackgroundResource(0);
		}
	}

	@Override
	public void focusEvent(boolean isFocus) {
		//Log.i("ly", "tempPlaying--->"+tempPlaying);
		if(isFocus){
			txtWeek.setTextColor(getColor(R.color.dark_text));
			txtTime.setTextColor(getColor(R.color.dark_text));
		}else{
			txtWeek.setTextColor(getColor(R.color.deep_text));
			txtTime.setTextColor(getColor(R.color.deep_text));
		}
	}

	@Override
	public void clearAllSelected() {
		// TODO Auto-generated method stub
		
	}
}
