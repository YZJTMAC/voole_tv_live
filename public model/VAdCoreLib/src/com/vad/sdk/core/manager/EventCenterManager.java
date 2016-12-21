/**
 *
 */
package com.vad.sdk.core.manager;

import com.vad.sdk.core.Utils.v30.Lg;
import com.vad.sdk.core.Utils.v30.Utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 *
 * @author luojunsheng
 * @date 2016年11月17日 下午6:30:15
 * @version 1.1
 */
public class EventCenterManager {
  private static final int HANDLER_MESSAGE_ID = 0;
  private static Handler sHandler;

  public static void initEventCenterManager(final Context context) throws RuntimeException {
    if (sHandler == null && Utils.isMainProcess(context) && Utils.isRunningForeground(context)) {
      if (Looper.myLooper() != Looper.getMainLooper()) {
        throw new RuntimeException("please init in main ui thread !");
      }
      sHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
          if (msg != null && msg.what == HANDLER_MESSAGE_ID) {
            MemoryRecorder.analyze(context);
            sHandler.sendEmptyMessageDelayed(HANDLER_MESSAGE_ID, 1000);
          }
        }
      };
      sHandler.sendEmptyMessageDelayed(HANDLER_MESSAGE_ID, 0);
    }
  }

  public static void stopSendEvent() {
    Lg.d("EventCenterManager , stopSendEvent()");
    if (sHandler != null && sHandler.hasMessages(HANDLER_MESSAGE_ID)) {
      sHandler.removeMessages(HANDLER_MESSAGE_ID);
      sHandler = null;
    }
  }
}
