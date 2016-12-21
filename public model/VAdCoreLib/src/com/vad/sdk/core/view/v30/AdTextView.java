package com.vad.sdk.core.view.v30;

import com.vad.sdk.core.Utils.v30.DisplayManagers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class AdTextView extends TextView implements Runnable {
  /** 当前位置坐标 */
  private int currentScrollX;
  /** 停止标志 */
  private boolean isStop = false;
  /** 文本长度 */
  private int textWidth;
  /** 可测量标志 */
  private boolean isMeasure = false;

  /** 三个构造函数 */
  public AdTextView(Context context) {
    super(context);
    setVisibility(INVISIBLE);
  }

  public AdTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public AdTextView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    // 查看可测量标志
    // 若不可测量的话，我们就获得文本信息得宽度，改变状态标志为可测量
    if (!isMeasure) {
      getTextWidth();
      isMeasure = true;
    }
  }

  /** 测量文本的宽度 */
  private void getTextWidth() {
    Paint paint = getPaint();
    String text = getText().toString();
    textWidth = (int) paint.measureText(text);
  }

  @Override
  public void run() {
    currentScrollX += 4;
    scrollTo(currentScrollX, 0);// 偏移至(currentScrollX, 0)
    if (isStop) {
      return;
    }
    // Log.e("", "run------------this.getwidth:" + this.getWidth());// ---244
    // Log.e("", "run------------textWidth:" + textWidth);// ---0
    // Log.d("", "currentScrollX:" + currentScrollX);
    // Log.e("", "getScrollX:" + getScrollX());
    // if (getScrollX() <= -(this.getWidth())) {// -255<-244
    // scrollTo(textWidth, 0);// 移动到(0,0)位置
    // currentScrollX = textWidth ;// 改变当前位置=0
    // // rollend.end();
    // }

    if (getScrollX() > getWidth()) {
      scrollTo(0 - textWidth, 0);
      currentScrollX = 0 - textWidth;
    }

    postDelayed(this, 36);// 第二个参数控制滚动速度,数值越大滚动越慢
  }

  /** 开始滚动 */
  public void startScroll() {
    getTextWidth();
    scrollTo(0 - textWidth, 0);
    // FIXME(ljs):解决文字滚动时突然闪以下的问题
    currentScrollX = -DisplayManagers.getInstance().getScreenWidth();// 起始坐标X=0
    // currentScrollX = 0;
    isStop = false;// 停止标志=false,标示开始滚动
    removeCallbacks(this);
    setVisibility(VISIBLE);
    post(this);// 开始滚动
  }

  /** 停止滚动 */
  public void stopScroll() {
    isStop = true;// 停止
  }

  // /** 轮播一轮回调 */
  // public interface Rollend{
  // public void end();
  // }
  // private Rollend rollend;
  // public void setOnListener(Rollend rollend){
  // this.rollend = rollend;
  // }
  // @Override
  // protected void onFocusChanged(boolean focused, int direction,
  // Rect previouslyFocusedRect) {
  // // TODO Auto-generated method stub
  // if(focused){
  // this.setBackgroundColor(android.graphics.Color.RED);
  // }else{
  // this.setBackgroundColor(android.graphics.Color.GRAY);
  // }
  // super.onFocusChanged(focused, direction, previouslyFocusedRect);
  // }
}
