/**
 *
 */
package com.vad.sdk.core.Utils.v30;

import com.vad.sdk.core.Config;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.text.TextUtils;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author luojunsheng
 * @date 2016年4月13日 下午5:12:03
 * @version 1.1
 */
public final class Utils {
  /**
   * 设置圆角
   *
   * @param color
   *          背景色值
   * @param radius
   *          圆角大小
   * @param insetWidth
   *          设置圆角view的宽度
   * @return
   */
  public static ShapeDrawable createRoundedRectDrawable(String color, float radius, float insetHeight) {
    float[] outerRadii = new float[] { radius, radius, radius, radius, radius, radius, radius, radius };
    float[] innerRadii = new float[] { radius, radius, radius, radius, radius, radius, radius, radius };
    RectF inset = new RectF(insetHeight, insetHeight, insetHeight, insetHeight);
    RoundRectShape roundRectShape = new RoundRectShape(outerRadii, inset, innerRadii);
    ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
    shapeDrawable.getPaint().setColor(Color.parseColor(color));
    return shapeDrawable;
  }

  public static Bitmap drawableToBitamp(Context context, int resId) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
    options.inPurgeable = true;
    options.inInputShareable = true;
    InputStream is = context.getResources().openRawResource(resId);
    return BitmapFactory.decodeStream(is, null, options);
  }

  // 获取版本号
  public static String getVersionName(Context context) {
    try {
      PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      return pi.versionName;
    } catch (NameNotFoundException e) {
      e.printStackTrace();
      return "";
    }
  }

  /**
   * 获取内部版本号
   *
   * @return
   */
  public static String getInternalVersionName() {
    return Config.SDK_VERSION_CODE;
  }

  // 获取版本号(内部识别号)
  public static int getVersionCode(Context context) {
    try {
      PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      return pi.versionCode;
    } catch (NameNotFoundException e) {
      e.printStackTrace();
      return 0;
    }
  }

  public static boolean isValidUrl(String url) {
    try {
      new URL(url);
      return true;
    } catch (MalformedURLException e) {
      return false;
    }
  }

  // second 秒
  public static String secondToString(long second) {
    int s = (int) (second % 60);
    int m = (int) (second / 60 % 60);
    int h = (int) (second / 60 / 60);
    return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
  }

  public static boolean isMainProcess(Context context) {
    int pid = android.os.Process.myPid();
    Lg.e("android.os.Process.myPid() = " + pid);
    String processName = "";
    ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
      if (appProcess.pid == pid) {
        processName = appProcess.processName;
        Lg.e("processName = " + processName);
        break;
      }
    }
    String packageName = context.getPackageName();
    return processName.equals(packageName);
  }

  public static String getSystemProperties(String property, String defaultvalue) {
    String value;
    try {
      Class<?> clazz = Class.forName("android.os.SystemProperties");
      Method method = clazz.getMethod("get", new Class[] { java.lang.String.class });
      value = (String) method.invoke(null, new Object[] { property });
      if (value != null && value.length() > 0) {
        return value;
      }
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return defaultvalue;
  }

  /**
   * 判断当前界面是自己的app
   *
   * @param : void
   * @return : 返回判断结果
   * @author : luojunsheng
   */
  public static boolean isRunningForeground(Context context) {
    if (context != null) {
      try {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String currentPackageName = "";
        // NOTE(junsheng.luo) <uses-permission android:name="android.permission.GET_TASKS" />has
        // been deprecated in API 21
        // see more information：
        // http://stackoverflow.com/questions/29018739/publisheradview-loadad-throwing-securityexception-gettasks-requires-androi
        // http://stackoverflow.com/questions/28066231/how-to-gettopactivity-name-or-get-current-running-application-package-name-in-lo/28066580#28066580
        Lg.e("Build.VERSION.SDK_INT = " + Build.VERSION.SDK_INT);
        // if (Build.VERSION.SDK_INT > 22) {
        // currentPackageName = activityManager.getRunningAppProcesses().get(0).processName;
        // } else {
        // currentPackageName =
        // activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
        // }
        currentPackageName = activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
        Lg.e("currentPackageName = " + currentPackageName);
        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName())) {
          return true;
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return false;
  }
}
