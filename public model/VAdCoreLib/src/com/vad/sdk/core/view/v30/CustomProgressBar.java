/**
 *
 */
package com.vad.sdk.core.view.v30;

import com.vad.sdk.core.Utils.v30.Lg;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

/**
 *
 * @author luojunsheng
 * @date 2016年6月16日 下午6:17:10
 * @version 1.1
 */
public class CustomProgressBar extends Drawable {
  @Override
  protected boolean onLevelChange(int level) {
    Lg.e("level = " + level);
    return true;
    // level is on a scale of 0-10,000
    // where 10,000 means fully downloaded

    // your app's logic to change the drawable's
    // appearance here based on progress
  }

  @Override
  public void draw(Canvas canvas) {

  }

  @Override
  public void setAlpha(int alpha) {

  }

  @Override
  public void setColorFilter(ColorFilter cf) {

  }

  @Override
  public int getOpacity() {
    return 0;
  }
}
