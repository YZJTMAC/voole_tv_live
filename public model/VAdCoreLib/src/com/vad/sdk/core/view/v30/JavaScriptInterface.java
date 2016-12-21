package com.vad.sdk.core.view.v30;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * 默认JavaScript接口
 *
 * @author libin
 */
public class JavaScriptInterface {
  public Context mContext;

  public JavaScriptInterface(Context context) {
    mContext = context;
  }

  /**
   * 显示Toast提示
   *
   * @param msg
   *          提示内容
   */
  @JavascriptInterface
  public void showToast(String msg) {
    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
  }

  /**
   * 退出Activity
   */
  @JavascriptInterface
  public void exit() {
    if (exitListener != null) {
      exitListener.onExit();
    }
  }

  private ExitListener exitListener = null;

  public void setExitListener(ExitListener exitListener) {
    this.exitListener = exitListener;
  }

  public interface ExitListener {
    void onExit();
  }
}
