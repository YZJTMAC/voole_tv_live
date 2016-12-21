package com.gntv.tv.view;

import com.gntv.tv.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetectItemView extends LinearLayout{
	private AttributeSet attrs;
	private TextView midTextView;
	private TextView stateTipTextView;
	private ImageView markImageView;
	private Context context;
//	public static final int STATE_ERROR = 1;
//	public static final int STATE_DETECTING = STATE_ERROR+1;
//	public static final int STATE_SUCCESS = STATE_DETECTING+1;
	public DetectItemView(Context context) {
		super(context);
		this.context = context;
		init();
	}
	private int textColor;
	private int textSize;
	private String midTextStr;
	private String stateTipTextStr;
	private int midTextPaddingLeft;


	public DetectItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		this.attrs =attrs;
		init();
	}

	public DetectItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.attrs =attrs;
		init();
	}
	
	private void init() {
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.detect_item); 
		textColor = a.getColor(R.styleable.detect_item_textColor, R.color.dark_text);
		textSize = a.getDimensionPixelOffset(R.styleable.detect_item_textSize, 20);
		midTextStr= a.getString(R.styleable.detect_item_midText);
		stateTipTextStr = a.getString(R.styleable.detect_item_stateTipText);
		midTextPaddingLeft = a.getDimensionPixelOffset(R.styleable.detect_item_midTextPaddingLeft, 0);
		a.recycle();
		
		midTextView = new TextView(context);
		midTextView.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
		LayoutParams layoutParams1 =new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,3);
		midTextView.setLayoutParams(layoutParams1);
		midTextView.setTextColor(textColor);
		midTextView.setText(midTextStr);
		midTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
		midTextView.setPadding(midTextPaddingLeft, 0, 0, 0);
		
		stateTipTextView = new TextView(context);
		stateTipTextView.setGravity(Gravity.CENTER);
		LayoutParams layoutParams2 =new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1);
		stateTipTextView.setLayoutParams(layoutParams2);
		stateTipTextView.setTextColor(textColor);
		stateTipTextView.setText(midTextStr);
		stateTipTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
		
		markImageView = new ImageView(context);
		LayoutParams layoutParams3 =new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1);
		markImageView.setLayoutParams(layoutParams3);
		markImageView.setImageResource(R.drawable.detect);
		markImageView.setScaleType(ImageView.ScaleType.CENTER);
		
		
		addView(markImageView);
		addView(midTextView);
		addView(stateTipTextView);
	}
	
	public void setMidText(String text){
			midTextView.setText(text.toString());
	}
	public void setMidText(int id){
		midTextView.setText(id);
	}
	public void setTextColor(int color){
			midTextView.setTextColor(color);
			stateTipTextView.setTextColor(color);
	}
	public void setStateTipText(String text){
			stateTipTextView.setText(text.toString());
	}
	public void setMarkImageResource(int resId){
			markImageView.setImageResource(resId);
	}
	public void setMarkImageAnimation(Animation animation){
		if(animation!=null){
			markImageView.setAnimation(animation);
		}
		
	}
	public void clearMarkImageAnimation(){
		markImageView.clearAnimation();
	}
	
	
	public void reset(){
//		midTextView.setText(midTextStr);
		midTextView.setTextColor(textColor);
		stateTipTextView.setTextColor(textColor);
		stateTipTextView.setText(stateTipTextStr);
		markImageView.setImageResource(R.drawable.detect);
		markImageView.clearAnimation();
	}
	
	
}