package com.gntv.tv.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class DownLoadProgressView extends View {

		/** 分段颜色 */
		private int[] SECTION_COLORS = { Color.parseColor("#79c5ff"), Color.parseColor("#147dce") };
		/** 进度条最大值 */
		private float maxCount;
		/** 进度条当前值 */
		private float currentCount;
		/** 画笔 */
		private Paint mPaint;
		private int mWidth, mHeight;
		private RectF rectBlackBg, rectProgressBg;
		private float section;

		public DownLoadProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
			super(context, attrs, defStyleAttr);
			initView(context);
		}

		public DownLoadProgressView(Context context, AttributeSet attrs) {
			super(context, attrs);
			initView(context);
		}

		public DownLoadProgressView(Context context) {
			super(context);
			initView(context);
		}

		private void initView(Context context) {
			mPaint = new Paint();
			rectBlackBg = new RectF(3, 3, 3, 3);
			rectProgressBg = new RectF(3, 3, 3, 3);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			mPaint.reset();
			mPaint.setAntiAlias(true);
			mPaint.setColor(Color.WHITE);
			canvas.drawRoundRect(rectBlackBg, 0, 0, mPaint);
			LinearGradient shader = new LinearGradient(3, 3, (mWidth - 3) * section, mHeight - 3, SECTION_COLORS, null,
					Shader.TileMode.MIRROR);
			mPaint.setShader(shader);
			canvas.drawRoundRect(rectProgressBg, 0, 0, mPaint);
		}

		private int dipToPx(int dip) {
			float scale = getContext().getResources().getDisplayMetrics().density;
			return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
		}

		/***
		 * 设置最大的进度值
		 * 
		 * @param maxCount
		 */
		public void setMaxCount(float maxCount) {
			this.maxCount = maxCount;
		}

		/***
		 * 设置当前的进度值
		 * 
		 * @param currentCount
		 */
		public void setCurrentCount(float currentCount) {
			if(currentCount > maxCount){
				this.currentCount = maxCount;
			}else {
				this.currentCount = currentCount;
			}
			if(maxCount != 0){
				section = currentCount / maxCount;
			}else {
				section = 0;
			}
			rectProgressBg.right = (mWidth - 3) * section;
			invalidate();
		}

		public float getMaxCount() {
			return maxCount;
		}

		public float getCurrentCount() {
			return currentCount;
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
			int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
			int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
			int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
			if (widthSpecMode == MeasureSpec.EXACTLY || widthSpecMode == MeasureSpec.AT_MOST) {
				mWidth = widthSpecSize;
			} else {
				mWidth = 0;
			}
			if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
				mHeight = dipToPx(22);
			} else {
				mHeight = heightSpecSize;
			}
			setMeasuredDimension(mWidth, mHeight);
			rectBlackBg.right = mWidth - 3;
			rectBlackBg.bottom = mHeight - 3;
			rectProgressBg.bottom = mHeight - 3;
		}

	}