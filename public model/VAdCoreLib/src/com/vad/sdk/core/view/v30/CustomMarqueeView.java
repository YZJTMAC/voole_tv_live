/**
 *
 */
package com.vad.sdk.core.view.v30;

import com.vad.sdk.core.Utils.v30.DisplayManagers;
import com.vad.sdk.core.Utils.v30.Lg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 *
 * @author luojunsheng
 * @date 2016年11月14日 下午2:07:22
 * @version 1.1
 */
public class CustomMarqueeView extends View {
  private Context mContext;
  private String mText;
  private int mScreenWidth = 0;
  private float mContentWidth;// 文字宽度
  private float mX;// x坐标
  private float mY;// y坐标
  private Paint mPaint;

  public CustomMarqueeView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    Lg.d("CustomMarqueeView, constructor(3)");
    mContext = context;
    init();
  }

  public CustomMarqueeView(Context context, AttributeSet attrs) {
    super(context, attrs);
    Lg.d("CustomMarqueeView, constructor(2)");
    mContext = context;
    init();
  }

  public CustomMarqueeView(Context context) {
    super(context);
    Lg.d("CustomMarqueeView, constructor(1)");
    mContext = context;
    init();
  }

  private void init() {
    mScreenWidth = DisplayManagers.getInstance().getScreenWidth();
    mPaint = new Paint();
    mPaint.setColor(Color.WHITE);
    mPaint.setTextSize(26f);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    Lg.d("CustomMarqueeView, onMeasure()");
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    mX = mScreenWidth;
    mY = (int) (getMeasuredHeight() / 2 - ((mPaint.descent() + mPaint.ascent()) / 2) + 5);
    Lg.d("CustomMarqueeView, onMeasure() , mY = " + mY);
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    Lg.d("CustomMarqueeView, onLayout()");
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.drawText(mText, mX, mY, mPaint);
    invalidateAfter(36);
  }

  public void setText(String text) {
    Lg.d("CustomMarqueeView, setText()");
    mText = text;
    mContentWidth = mPaint.measureText(mText);
  }

  public void stopScroll() {
    removeCallbacks(invalidateRunnable);
  }

  void invalidateAfter(long delay) {
    removeCallbacks(invalidateRunnable);
    postDelayed(invalidateRunnable, delay);
  }

  Runnable invalidateRunnable = new Runnable() {
    @Override
    public void run() {
      mX = mX - 4;
      invalidate();
      if (mX == -(mScreenWidth + mContentWidth)) {
        mX = mScreenWidth;
      }
    }
  };
}
