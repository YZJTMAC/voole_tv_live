package com.vad.sdk.core.Utils.v30;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.Log;

public class DeviceUtil {
  public static String getLocalIpAddress() {
    try {
      for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
        NetworkInterface intf = en.nextElement();
        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
          InetAddress inetAddress = enumIpAddr.nextElement();
          if (inetAddress instanceof Inet6Address)
            continue;
          if (!inetAddress.isLoopbackAddress()) {
            return inetAddress.getHostAddress().toString().trim();
          }
        }
      }
    } catch (SocketException ex) {
      Log.e("IpAddress", ex.toString());
    }
    return null;
  }

  public static String getSequenceNo() {
    String sequenceno = null;
    long time = System.currentTimeMillis();
    final Calendar mCalendar = Calendar.getInstance();
    mCalendar.setTimeInMillis(time);
    int mYear = mCalendar.get(Calendar.YEAR);
    int mHour = mCalendar.get(Calendar.HOUR);
    int mMinutes = mCalendar.get(Calendar.MINUTE);
    int mSeconds = mCalendar.get(Calendar.SECOND);
    int mMiliSeconds = mCalendar.get(Calendar.MILLISECOND);
    sequenceno = String.format("%s_%4d%02d%02d%02d%07d", "10000101", mYear, mHour, mMinutes, mSeconds, mMiliSeconds);
    return sequenceno;
  }

  public static String getAppVersionName(Context context) {
    PackageManager pm = context.getPackageManager();
    try {
      PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
      return info.versionName;
    } catch (NameNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  public static int getAppVersionCode(Context context) {
    PackageManager pm = context.getPackageManager();
    try {
      PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
      return info.versionCode;
    } catch (NameNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return -1;
  }

  public static String getAppName(Context context) {
    return context.getPackageName();
  }

  public static boolean hasEnoughSpaceOnCache(File path, long updateSize) {
    StatFs stat = new StatFs(path.getPath());
    long blockSize = stat.getBlockSize();
    long availableBlocks = stat.getAvailableBlocks();
    return (updateSize < availableBlocks * blockSize);
  }

  public static void installApk(Context context, File apkFile) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
    context.startActivity(intent);
  }

  public static void setCurrentPlayerMute(Context context) {
    AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    if (getCurrentPlayerVolume(context) <= 0) {
      audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
    } else {
      audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
    }
  }

  public static void setCurrentPlayerMute(Context context, boolean b) {
    AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, b);
  }

  public static int getCurrentSystemVolume(Context context) {
    AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    return audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
  }

  public static int getCurrentPlayerVolume(Context context) {
    AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
  }

  public static void increasePlayerVolume(Context context) {
    AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    if (getCurrentPlayerVolume(context) <= 0) {
      audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
    }
    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FX_FOCUS_NAVIGATION_UP);
  }

  public static void decreasePlayerVolume(Context context) {
    AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    if (getCurrentPlayerVolume(context) <= 0) {
      audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
    }
    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FX_FOCUS_NAVIGATION_UP);
  }

  public static void setPlayerVolume(Context context, int v) {
    AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, v, 0);
  }

  public static void setSystemVolume(Context context, int v) {
    AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, v, 0);
  }

  public static int getPlayerMaxVolume(Context context) {
    AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    return audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
  }

  public static int getSystemMaxVolume(Context context) {
    AudioManager audioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    return audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
  }

  public static void setPlayerMaxVolume(Context context) {
    int max = getPlayerMaxVolume(context);
    setPlayerVolume(context, max);
  }

  public static void setSystemMaxVolume(Context context) {
    int max = getSystemMaxVolume(context);
    setSystemVolume(context, max);
  }

  public static String findPidOfAgent(String pName) {
    String pid;
    String temp;
    pid = "";
    Process p = null;
    try {
      Lg.d("DeviceUtil-->findPidOfAgent ps-->start");
      p = new ProcessBuilder("/system/bin/ps").start();
      Lg.d("DeviceUtil-->findPidOfAgent ps");
      BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
      BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
      while ((temp = stdInput.readLine()) != null) {
        if (temp.contains(pName)) {
          String[] cmdArray = temp.split(" +");
          pid = cmdArray[1];
        }
      }
      while ((temp = stdError.readLine()) != null) {
        Lg.d("stdError-->" + temp);
      }
    } catch (Exception e) {
      Lg.d("DeviceUtil-->findPidOfAgent Exception-->" + e.toString());
    } /*
       * finally{ try { p.getOutputStream().close(); p.getInputStream().close();
       * p.getErrorStream().close(); p.destroy(); } catch (IOException e) {
       * Lg.d("DeviceUtil-->findPidOfAgent finally Exception-->" + e.toString()); } }
       */
    return pid;
  }

  /*
   * public static String findPidOfAgent(String pName) { String pid; String temp; pid = ""; Process
   * p = null; try { // String[] cmd = { "/bin/sh", "-c", "ps > /dev/null 2>&1" }; // Process p =
   * Runtime.getRuntime().exec(cmd); Lg.d("DeviceUtil-->findPidOfAgent ps-->start"); p =
   * Runtime.getRuntime().exec("/system/bin/ps"); Lg.d("DeviceUtil-->findPidOfAgent ps"); //
   * p.waitFor(); // Lg.d("DeviceUtil-->findPidOfAgent waitFor"); BufferedReader stdInput = new
   * BufferedReader(new InputStreamReader( p.getInputStream())); while ((temp = stdInput.readLine())
   * != null) { // Lg.d("DeviceUtil-->temp-->" + temp); if (temp.contains(pName)) { String[]
   * cmdArray = temp.split(" +"); // for (int i = 0; i < cmdArray.length; i++) { //
   * Log.d("DDDDDDDDDDD", "loop i=" + i + " => " + cmdArray[i]); // } pid = cmdArray[1]; } }
   * stdInput.close(); // p.waitFor(); // Lg.d("DeviceUtil-->findPidOfAgent waitFor"); } catch
   * (Exception e) { Lg.d("DeviceUtil-->findPidOfAgent Exception-->" + e.toString()); } finally{ try
   * { p.getOutputStream().close(); p.getInputStream().close(); p.getErrorStream().close(); } catch
   * (IOException e) { Lg.d("DeviceUtil-->findPidOfAgent finally Exception-->" + e.toString()); } }
   * return pid; }
   */

  public static boolean checkPackageExist(Context context, String packageName) {
    if (packageName == null || packageName.equals("")) {
      return false;
    }
    try {
      context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
      return true;
    } catch (Exception e) {
      // TODO: handle exception
      return false;
    }
  }

  public static void setFileToPermission(final String fileName) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        Process process = null;
        try {
          process = Runtime.getRuntime().exec("chmod 777 " + fileName);
          process.waitFor();
        } catch (IOException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          if (process != null) {
            try {
              process.getInputStream().close();
              process.getOutputStream().close();
              process.getErrorStream().close();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }

      }
    }).start();
  }

  public static String getIMEI(Context context) {
    TelephonyManager manager = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
    if (PackageManager.PERMISSION_GRANTED == context.getPackageManager().checkPermission(Manifest.permission.READ_PHONE_STATE, context.getPackageName())) {
      return manager.getDeviceId();
    } else {
      return null;
    }
  }

  public static String getSystemProcValue(String key) {
    // ro.lycoo.vooleplay.name
    try {
      BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("getprop " + key).getInputStream()), 1024);
      if (null != localBufferedReader) {
        String str = localBufferedReader.readLine();
        localBufferedReader.close();
        return str;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;

  }

  public static int getSDKVersionNumber() {
    int sdkVersion;
    try {
      sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
    } catch (NumberFormatException e) {
      sdkVersion = 0;
    }
    return sdkVersion;
  }

  /**
   * 返回无线或者有线网卡地址
   *
   * @param context
   * @return
   */
  public static String getMacAddressNew(Context context) {
    // 在wifi未开启状态下，仍然可以获取MAC地址，但是IP地址必须在已连接状态下否则为0
    String macAddress = "";
    WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    Lg.d("DeviceUtil , getMacAddressNew() , WifiState = " + wifiMgr.getWifiState());
    if (wifiMgr.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
      WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
      if (null != info) {
        macAddress = info.getMacAddress();
      }
      Lg.d("DeviceUtil , getMacAddressNew() , macAddress = " + macAddress);
      String str2 = "";
      if (macAddress.contains(":")) {
        String s[] = macAddress.split(":");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length; i++) {
          sb.append(s[i]);
        }
        String str1 = sb.toString();// 拆分后转换回字符串
        str2 = str1.toUpperCase(Locale.getDefault());
        System.out.println(str2);
      } else {
        str2 = macAddress.toUpperCase(Locale.getDefault());
      }
      return str2;
    } else {
      return getMacAddress(false);
    }

  }

  public static String getWifiMacAddress(Context context) {
    String macAddress = "";
    WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
    if (null != info) {
      macAddress = info.getMacAddress();
    }
    Lg.d("mac:" + macAddress);
    String str2 = "";
    if (macAddress.contains(":")) {
      String s[] = macAddress.split(":");
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < s.length; i++) {
        sb.append(s[i]);
      }
      String str1 = sb.toString();// 拆分后转换回字符串
      str2 = str1.toUpperCase(Locale.getDefault());
      System.out.println(str2);
    } else {
      str2 = macAddress.toUpperCase(Locale.getDefault());
    }
    return str2;
  }

  /**
   * 返回硬件地址
   *
   * @return
   */
  public static String getMacAddress() {
    try {
      for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
        NetworkInterface intf = en.nextElement();
        if (intf.getName().equals("eth0")) {
          StringBuffer sb = new StringBuffer();
          byte[] macBytes = intf.getHardwareAddress();
          for (int i = 0; i < macBytes.length; i++) {
            String sTemp = Integer.toHexString(0xFF & macBytes[i]);
            if (sTemp.length() == 1) {
              sb.append("0");
            }
            sb.append(sTemp);
          }
          return sb.toString();
        }
      }
    } catch (SocketException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 返回硬件地址大写
   *
   * @param separate
   * @return
   */
  public static String getMacAddress(boolean separate) {
    byte[] bytes = getHardwareAddress();
    StringBuffer hexBuffer = new StringBuffer();
    String temp = "";
    for (int n = 0; n < bytes.length; n++) {
      temp = (java.lang.Integer.toHexString(bytes[n] & 0XFF));
      if (temp.length() == 1) {
        hexBuffer.append("0");
      }
      hexBuffer.append(temp.toUpperCase());
      if (separate) {
        hexBuffer.append(":");
      }
    }
    if (separate) {
      return hexBuffer.substring(0, hexBuffer.length() - 1);
    } else {
      return hexBuffer.toString();
    }
  }

  public static String getMacAddress(boolean separate, Context context) {
    byte[] bytes = getHardwareAddress();
    if (bytes == null) {
      return getMacAddressNew(context);
    }
    StringBuffer hexBuffer = new StringBuffer();
    String temp = "";
    for (int n = 0; n < bytes.length; n++) {
      temp = (java.lang.Integer.toHexString(bytes[n] & 0XFF));
      if (temp.length() == 1) {
        hexBuffer.append("0");
      }
      hexBuffer.append(temp.toUpperCase());
      if (separate) {
        hexBuffer.append(":");
      }
    }
    if (separate) {
      return hexBuffer.substring(0, hexBuffer.length() - 1);
    } else {
      return hexBuffer.toString();
    }

  }

  private static byte[] getHardwareAddress() {
    try {
      for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
        NetworkInterface intf = en.nextElement();
        String s = intf.getName();
        if (intf.getName().equals("eth0")) {
          return intf.getHardwareAddress();
        }
      }
    } catch (SocketException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 查看是否还有可用空间
   *
   * @param context
   * @param updateSize
   * @return
   */
  public static boolean hasEnoughSpaceOnCache(Context context, long updateSize) {
    File path = context.getFilesDir();
    StatFs stat = new StatFs(path.getPath());
    long blockSize = stat.getBlockSize();
    long availableBlocks = stat.getAvailableBlocks();
    return (updateSize < availableBlocks * blockSize);
  }

  /**
   * 设置播放状态
   *
   * @param context
   * @param state
   */
  public static void setPlayerMute(Context context, boolean state) {
    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, state);
  }

  /**
   * 设置静音
   *
   * @param context
   */
  public static void doPlayMute(Context context) {
    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    if (getCurrentPlayerVolume(context) <= 0) {
      audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
    } else {
      audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
    }
  }

}
