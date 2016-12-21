package com.gntv.tv.view;

import com.gntv.tv.R;
import com.gntv.tv.view.base.FontSize;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

public class VTimeView extends View {

	private Bitmap timeBitmap ;
	private String testString = "99:99:99";
	private float left ;
	private float width ;
	private float height ;
	private Paint mPaint ;
	private Rect bounds ;
	private RectF imageRect ;
	//private NinePatch timePatch;

	public VTimeView(Context context) {
		this(context, null, 0);
	}
	public VTimeView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	@SuppressLint("NewApi")
	public VTimeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle) ;
		initTimeView(context) ;
	}
	
	private void initTimeView(Context context) {
		timeBitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.cs_back_tip) ;
		//timePatch = new NinePatch(timeBitmap,timeBitmap.getNinePatchChunk(), null);
		mPaint = new Paint(); 
		mPaint.setAntiAlias(true) ;
		mPaint.setStrokeWidth(2);    
		mPaint.setTextSize(getTextSize(R.dimen.BACK_SIZE));    
		mPaint.setColor(Color.WHITE);  
		mPaint.setTextAlign(Align.CENTER); 
		bounds = new Rect();
		mPaint.getTextBounds(testString, 0, testString.length(), bounds);
		width = (float) (bounds.width()*1.6);
		height = (float) (bounds.height()*3.0);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas) ;
		mPaint.getTextBounds(testString, 0, testString.length(), bounds);
		//imageRect = new Rect(left,0,left+width,height) ;
		imageRect = new RectF(left, 0f, left+width, height);
		canvas.drawBitmap(timeBitmap, null, imageRect, null) ;
		//timePatch.draw(canvas, imageRect);
		canvas.drawText(testString, left+imageRect.width()/2,(int)(imageRect.height()/1.7), mPaint);
	}
	
	public void setTimeTextColor(int color) {
		mPaint.setColor(color) ;
	}
	
	public void setProgress(float left,String seek){
		this.left = left ;
		testString = seek ;
		invalidate() ;
	}
	
	public void updateTimeText(String text){
		testString = text;
		invalidate();
	}
	
	public float getVTWidth() {
		return width ;
	}
	
	/*public int getVTHeight() {
		return height ;
	}*/
	private float getTextSize(int id){
		float size = getContext().getResources().getDimension(id);
		Log.i("textsize","textsize::"+ size);
		return size;
	}
}
