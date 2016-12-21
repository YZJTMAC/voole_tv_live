/**
 *
 */
package com.vad.sdk.core.view.v30;

import com.vad.sdk.core.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 *
 * @author luojunsheng
 * @date 2016年3月18日 上午11:13:57
 * @version 1.1
 */
public class BootTimeView extends RelativeLayout {
  private TextView mTimeView;
  public BootTimeView(Context context) {
    super(context);
  }

  public BootTimeView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public BootTimeView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    mTimeView = (TextView) findViewById(R.id.boot_time_num);
  }

  public void setContent(String time) {
    mTimeView.setText("" + time);
  }
}
