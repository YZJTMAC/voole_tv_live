package com.vad.sdk.core.Utils.v30;

import android.util.Log;

/**
 * Log封装类，可设置 tag 和 要打印的级别 level；
 *
 * @author Administrator
 *
 */
public class Lg {
  private static final byte LV = 0x20;
  private static final byte LD = 0x10;
  private static final byte LI = 0x08;
  private static final byte LW = 0x04;
  private static final byte LE = 0x02;
  private static final byte LA = 0x01;

  private static String sTag = "SDK";
  private static int sLevel = 0x3f;

  public static void setTag(String tag) {
    if (tag != null && tag.length() > 0) {
      sTag = tag;
    }
  }

  public static void setLevel(int level) {
    if (level >= 0 && level <= 0x3f) {
      sLevel = level;
    }
  }

  public static void v(String msg) {
    if ((LV & sLevel) > 0) {
      Log.v(sTag, msg);
    }
  }

  public static void d(String msg) {
    if ((LD & sLevel) > 0) {
      Log.d(sTag, msg);
    }
  }

  public static void i(String msg) {
    if ((LI & sLevel) > 0) {
      Log.i(sTag, msg);
    }
  }

  public static void w(String msg) {
    if ((LW & sLevel) > 0) {
      Log.w(sTag, msg);
    }
  }

  public static void e(String msg) {
    if ((LE & sLevel) > 0) {
      Log.e(sTag, msg);
    }
  }

  public static void e(String msg, Throwable tr) {
    if ((LE & sLevel) > 0) {
      Log.e(sTag, msg, tr);
    }
  }
}
