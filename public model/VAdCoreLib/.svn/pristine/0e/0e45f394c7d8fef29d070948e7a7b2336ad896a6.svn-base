/**
 *
 */
package com.vad.sdk.core.manager;

import com.vad.sdk.core.Utils.v30.Utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.util.Log;

/**
 *
 * @author luojunsheng
 * @date 2016年11月17日 下午5:41:44
 * @version 1.1
 */
public class MemoryRecorder {
  private static final String TAG = "SDK";

  public static void analyze(Context context) {
    long nativeMax = Debug.getNativeHeapSize() / 1024;
    long nativeAllocated = Debug.getNativeHeapAllocatedSize() / 1024;
    long nativeFree = Debug.getNativeHeapFreeSize() / 1024;
    Runtime runtime = Runtime.getRuntime();
    long dalvikMax = runtime.maxMemory() / 1024;
    long dalvikotal = runtime.totalMemory() / 1024;
    long dalvikFree = runtime.freeMemory() / 1024;
    long dalvikAllocated = dalvikMax - dalvikFree;
    Log.i(TAG, "============================================");
    Log.i(TAG, "dalvi.vm.heapstartsize = " + Utils.getSystemProperties("dalvik.vm.heapstartsize", "0"));
    Log.i(TAG, "dalvik.vm.heapgrowthlimit = " + Utils.getSystemProperties("dalvik.vm.heapgrowthlimit", "0"));
    Log.i(TAG, "dalvik.vm.heapsize = " + Utils.getSystemProperties("dalvik.vm.heapsize", "0"));
    Log.i(TAG, "dalvik.vm.heaptargetutilization = " + Utils.getSystemProperties("dalvik.vm.heaptargetutilization", "0"));
    Log.i(TAG, "dalvik.vm.heapminfree = " + Utils.getSystemProperties("dalvik.vm.heapminfree", "0"));
    Log.i(TAG, "dalvik.vm.heapmaxfree = " + Utils.getSystemProperties("dalvik.vm.heapmaxfree", "0"));

    Log.i(TAG, "nativeMax = " + nativeMax);
    Log.i(TAG, "nativeAllocated = " + nativeAllocated);
    Log.i(TAG, "nativeFree = " + nativeFree);
    Log.i(TAG, "dalvikMax = " + dalvikMax);
    Log.i(TAG, "dalvikotal = " + dalvikotal);
    Log.i(TAG, "dalvikFree = " + dalvikFree);
    Log.i(TAG, "dalvikAllocated = " + dalvikAllocated);
    final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
    activityManager.getMemoryInfo(info);
    Log.i(TAG, "availMem = " + info.availMem / 1024);
    Log.i(TAG, "lowMemory = " + info.lowMemory);
    Log.i(TAG, "threshold = " + info.threshold / 1024);
    Log.i(TAG, "============================================");
    // ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    // List<ActivityManager.RunningAppProcessInfo> appProcessList = am.getRunningAppProcesses();
    // for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessList) {
    // int pid = appProcessInfo.pid;
    // int uid = appProcessInfo.uid;
    // String processName = appProcessInfo.processName;
    // if (!processName.contains("com.voole")) {
    // continue;
    // }
    // int[] myMempid = new int[] { pid };
    // Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(myMempid);
    // int dalvikPrivateDirty = memoryInfo[0].dalvikPrivateDirty;
    // int dalvikPss = memoryInfo[0].dalvikPss;
    // int dalvikSharedDirty = memoryInfo[0].dalvikSharedDirty;
    // int nativePrivateDirty = memoryInfo[0].nativePrivateDirty;
    // int nativePss = memoryInfo[0].nativePss;
    // int nativeSharedDirty = memoryInfo[0].nativeSharedDirty;
    // int otherPrivateDirty = memoryInfo[0].otherPrivateDirty;
    // int otherPss = memoryInfo[0].otherPss;
    // int otherSharedDirty = memoryInfo[0].otherSharedDirty;
    // Log.i("CPU", "============================================");
    // Log.i("CPU", "x:pid = " + pid);
    // Log.i("CPU", "x:uid = " + uid);
    // Log.i("CPU", "x:processName = " + processName);
    // Log.i("CPU", "0:dalvikPrivateDirty = " + dalvikPrivateDirty);
    // Log.i("CPU", "1:dalvikPss = " + dalvikPss);
    // Log.i("CPU", "2:dalvikSharedDirty = " + dalvikSharedDirty);
    // Log.i("CPU", "3:nativePrivateDirty = " + nativePrivateDirty);
    // Log.i("CPU", "4:nativePss = " + nativePss);
    // Log.i("CPU", "5:nativeSharedDirty = " + nativeSharedDirty);
    // Log.i("CPU", "6:otherPrivateDirty = " + otherPrivateDirty);
    // Log.i("CPU", "7:otherPss = " + otherPss);
    // Log.i("CPU", "8:otherSharedDirty = " + otherSharedDirty);
    // Log.i("CPU", "============================================");
    // }
  }
}
