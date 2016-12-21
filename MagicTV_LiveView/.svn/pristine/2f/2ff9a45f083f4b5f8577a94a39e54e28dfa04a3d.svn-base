package com.gntv.tv.view;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.gntv.tv.R;
import com.gntv.tv.view.base.AlwaysMarqueeTextView;
import com.gntv.tv.view.base.BaseChannelLinearLayout;
import com.gntv.tv.view.base.FontSize;

public class AssortItemView extends BaseChannelLinearLayout {
	private AlwaysMarqueeTextView sortTextView = null;
	private boolean tempPlaying = false;
	public AssortItemView(Context context) {
		super(context);
		init(context);
	}

	@Override
	protected void init(Context context) {
		setOrientation(VERTICAL);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		this.setGravity(Gravity.CENTER);
		sortTextView = new AlwaysMarqueeTextView(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		//params.setMargins(changeWidth(25), 0, 0, 0);
		//sortTextView.setPadding(changeWidth(25), 0, 0, 0);
		sortTextView = new AlwaysMarqueeTextView(context);
		sortTextView.setGravity(Gravity.CENTER);
		sortTextView.setLayoutParams(params);
		sortTextView.setTextSize(changeTextSize(FontSize.CHANNEL_NAME));
		sortTextView.setTextColor(getColor(R.color.deep_text));
		addView(sortTextView);
	}
	
	public void setData(String sortName){
		sortTextView.setText(sortName);
	}
	
	public void setPlaying(boolean isPlaying){
		tempPlaying = isPlaying;
		if(isPlaying){
			sortTextView.setTextColor(getColor(R.color.dark_blue_text));
		}else{
			sortTextView.setTextColor(getColor(R.color.deep_text));
		}
	}
	
	public void setMarquee(boolean state){
		if(state){
			sortTextView.setEllipsize(TruncateAt.MARQUEE);
			sortTextView.setHorizontallyScrolling(true);
			sortTextView.setMarqueeRepeatLimit(-1);
			
		}else{
			sortTextView.setEllipsize(TruncateAt.END);
		}
	}
	
	public void setSelected(boolean isSelected){
		if(isSelected){
			sortTextView.setTextColor(getColor(R.color.light_blue_text));
			this.setBackgroundResource(R.drawable.border_shape);
		}else{
			sortTextView.setTextColor(getColor(R.color.dark_text));
 			this.setBackgroundResource(0);
		}
	}
	
	public String getText(){
		return sortTextView.getText().toString().trim();
	}

	@Override
	public void focusEvent(boolean isFocus) {
		if(isFocus){
			sortTextView.setTextColor(getColor(R.color.dark_text));
		}else{
			sortTextView.setTextColor(getColor(R.color.deep_text));
		}
	}

	@Override
	public void clearAllSelected() {
		// TODO Auto-generated method stub
		
	}
	
	/*@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.i("ly33", "AssortItemView--->onDraw");
	}*/

}
