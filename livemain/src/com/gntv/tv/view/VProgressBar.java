package com.gntv.tv.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gntv.tv.R;

public class VProgressBar extends RelativeLayout {

	private int cricleSize ;
	private Drawable logo ;
	private ImageView cricleView ;
	private ImageView logoView ;
	private RelativeLayout.LayoutParams cricleParams ;
	private RelativeLayout.LayoutParams logoParams ;

	public VProgressBar(Context context) {
		this(context,null,0);
	}

	public VProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public VProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context, attrs) ;
	}

	private void initView(Context context, AttributeSet attrs) {
		TypedArray taArray = context.obtainStyledAttributes(attrs, R.styleable.MyProgressBar) ;
		cricleSize = (int) taArray.getDimension(R.styleable.MyProgressBar_cricle_size, 300) ;
		logo = taArray.getDrawable(R.styleable.MyProgressBar_logo_image_drawable) ;

		cricleParams = new RelativeLayout.LayoutParams(cricleSize,cricleSize) ;
		cricleParams.addRule(CENTER_IN_PARENT) ;
		cricleView = new ImageView(context) ;
		cricleView.setLayoutParams(cricleParams) ;
		Drawable cricleDrawable = context.getResources().getDrawable(R.drawable.cs_tv_circle) ;
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.cricle) ;
		cricleView.startAnimation(animation) ;
		cricleView.setAnimation(animation) ;
		cricleView.setImageDrawable(cricleDrawable) ;

		logoParams = new RelativeLayout.LayoutParams((int) (cricleSize*0.5),(int) (cricleSize*0.5)) ;
		logoView = new ImageView(context) ;
		logoParams.addRule(CENTER_IN_PARENT) ;
		logoView.setLayoutParams(logoParams) ;
		logoView.setImageDrawable(logo);

		addView(logoView) ;
		addView(cricleView) ;
		taArray.recycle() ;
	}

}
