package com.vad.sdk.core.model.v30;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.vad.sdk.core.Config;
import com.vad.sdk.core.R;
import com.vad.sdk.core.VAdType;
import com.vad.sdk.core.Utils.v30.Lg;
import com.vad.sdk.core.base.AdInfo;
import com.vad.sdk.core.base.AdRegister;
import com.vad.sdk.core.base.interfaces.IAdStartupListener;
import com.vad.sdk.core.manager.EventCenterManager;
import com.vad.sdk.core.manager.SharedPreferencesManager;
import com.vad.sdk.core.model.v30.parser.ApiDataParser;
import com.vad.sdk.core.model.v30.parser.ApiResponseListener;
import com.vad.sdk.core.report.v30.ReportManager;
import com.vad.sdk.core.view.v30.BootTimeView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class AdPosBootListener {
  private BootTimeView mBootTimeView;

  /**
   * @param parent
   * @param listener
   * @param isShowTime
   *          是否展示倒计时
   * @return
   */
  public boolean show(ViewGroup parent, IAdStartupListener listener, boolean isShowTime) {
    String adposid = SharedPreferencesManager.getString(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_BOOT_AD_ADPOSID, "");
    int length = SharedPreferencesManager.getInt(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_BOOT_AD_LENGTH, 0);
    if (checkDownLoadFile(parent.getContext(), length, adposid)) {
      Lg.e("AdPosBootListener , show()#checkDownLoadFile() =  true");
      setBackgroundForView(parent, listener, length, adposid, isShowTime);
      return true;
    } else {
      Lg.e("AdPosBootListener , show()#checkDownLoadFile() =  fasle , go to report");
      EventCenterManager.stopSendEvent();
      String reportvalue = SharedPreferencesManager.getString(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_BOOT_AD_REPORTVALUE, "");
      String reportUrl = SharedPreferencesManager.getString(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_BOOT_AD_REPORTURL, "");
      String pos = SharedPreferencesManager.getString(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_BOOT_AD_ADPOS, "");
      Lg.e("AdPosBootListener , reportvalue = " + reportvalue + " , reportUrl = " + reportUrl);
      boolean isOemidBan = SharedPreferencesManager.getBoolean(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_APP_OEMID_BAN, false);
      if (!isOemidBan) {
        Lg.e("AdPosBootListener , report for fail");
        ReportManager.getInstance().report(reportvalue, 0, ReportManager.Start, reportUrl, pos);
      } else {
        Lg.d("oemid禁止导流,不上报");
      }
      return false;
    }
  }

  private void setBackgroundForView(final ViewGroup parent, final IAdStartupListener listener, final int length, final String adposid, final boolean isShowTime) {
    Lg.d("AdPosBootListener , setBackgroundForView() , length = " + length);
    new Thread(new Runnable() {
      private CountDownTimer countDownTimer;

      @Override
      public void run() {
        try {
          Bitmap bitmap = getBitmap(parent.getContext(), SharedPreferencesManager.getString(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_BOOT_AD_ADPOSID, ""));
          Lg.e("bitmap width = " + bitmap.getWidth());
          Lg.e("bitmap heght = " + bitmap.getHeight());

          Lg.e("setBackgroundForView() , bitmap size = " + bitmap.getRowBytes() * bitmap.getHeight() / 1024 + "kb");
          // NOTE(ljs):使用Drawable保存图片对象,占用更小的内存空间
          // WeakReference<Bitmap> bitmapTmp = new WeakReference<Bitmap>(bitmap);
          final BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
          final int second = length == 0 ? 5 : length;
          Lg.d("AdPosBootListener , setBackgroundForView() , second = " + second + " , defaultsecond = 5");

          // Handler mainThread = new Handler(Looper.getMainLooper());
          // mainThread.post(new Runnable() {
          ((Activity) parent.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
              if (isShowTime) {
                mBootTimeView = (BootTimeView) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_boot_time, null);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                params.topMargin = 30;
                params.rightMargin = 30;
                mBootTimeView.setLayoutParams(params);
                mBootTimeView.setContent("广告倒计时:" + second);
                ((FrameLayout) parent.getParent()).addView(mBootTimeView, 0);
              }
              // parent.setBackgroundDrawable(bitmapDrawable);
              // ((Activity)
              // parent.getContext()).getWindow().getDecorView().findViewById(android.R.id.content).setBackgroundDrawable(bitmapDrawable);
              ((FrameLayout) parent.getParent()).setBackgroundDrawable(bitmapDrawable);
              countDownTimer = new CountDownTimer(second * 1000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                  Lg.d("AdPosBootListener , setBackgroundForView()#CountDownTimer.onTick() , millisUntilFinished = " + millisUntilFinished / 1000L);
                  if (isShowTime && mBootTimeView != null) {
                    mBootTimeView.setContent("广告倒计时:" + millisUntilFinished / 1000L);
                  }
                }

                @Override
                public void onFinish() {
                  Lg.d("AdPosBootListener , setBackgroundForView()#onFinish()");
                  EventCenterManager.stopSendEvent();
                  if (parent != null) {
                    ((Activity) parent.getContext()).runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                        if (isShowTime) {
                          ((FrameLayout) parent.getParent()).removeViewAt(0);
                        }
                        ((FrameLayout) parent.getParent()).setBackgroundResource(0);
                        listener.onAdEnd();
                        // NOTE(ljs):在CountDownTimer(millisInFuture,countDownInterval)中,当millisInFuture=0时,
                        // 调用start()会立马调用onFinish()导致countDownTimer为null
                        if (countDownTimer != null) {
                          countDownTimer.cancel();
                          countDownTimer = null;
                        }
                        // NOTE(ljs):http://developer.android.com/intl/zh-cn/training/displaying-bitmaps/cache-bitmap.html
                        bitmapDrawable.getBitmap().recycle();
                      }
                    });
                  }
                }
              };
              countDownTimer.start();
            }
          });

        } catch (IOException e) {
          Lg.d("setBackgroundForView() , getBitmap-->exception-->" + e.toString());
          ((Activity) parent.getContext()).runOnUiThread(new Runnable() {

            @Override
            public void run() {
              EventCenterManager.stopSendEvent();
              listener.onAdEnd();
            }
          });
        }
        String amid = SharedPreferencesManager.getString(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_BOOT_AD_AMID, "");
        String reportvalue = SharedPreferencesManager.getString(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_BOOT_AD_REPORTVALUE, "");
        String reportUrl = SharedPreferencesManager.getString(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_BOOT_AD_REPORTURL, "");
        String pos = SharedPreferencesManager.getString(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_BOOT_AD_ADPOS, "");
        if (!TextUtils.isEmpty(reportvalue) && !TextUtils.isEmpty(reportUrl) && !TextUtils.isEmpty(pos)) {
          ReportManager.getInstance().report(reportvalue, 0, ReportManager.Start, reportUrl, pos);
        }

      }
    }).start();
  }

  /**
   * 取出本地图片bitmap,路径/data/data/[packagename]/files/
   *
   * <p>
   * NOTE(ljs):此处最好根据view的大小返回 bitmap
   * </p>
   *
   * @param context
   * @param posid
   * @return
   * @throws IOException
   */
  private Bitmap getBitmap(Context context, String posid) throws IOException {
    String path = context.getFilesDir() + File.separator;
    Lg.d("getBitmap----->path-->" + path);
    Lg.d("getBitmap----->posid-->" + posid);

    Bitmap decodeBitmap = null;
    File bitmapFile = new File(path + posid);
    // FIXME(ljs):BitmapFactory.decodeStream()存在OOM隐患
    if (bitmapFile != null && bitmapFile.isFile() && bitmapFile.exists()) {
      BufferedInputStream bips = new BufferedInputStream(new FileInputStream(bitmapFile));
      decodeBitmap = BitmapFactory.decodeStream(bips);
      bips.close();
    }
    return decodeBitmap;
  }

  private Drawable getDrawable(Context context, String posid) throws IOException {
    String path = context.getFilesDir() + File.separator;
    Lg.d("getDrawable----->path-->" + path);
    Lg.d("getDrawable----->posid-->" + posid);

    Drawable decodeDrawable = null;
    File bitmapFile = new File(path + posid);
    // FIXME(ljs):BitmapFactory.decodeStream()存在OOM隐患
    if (bitmapFile != null && bitmapFile.isFile() && bitmapFile.exists()) {
      BufferedInputStream bins = new BufferedInputStream(new FileInputStream(bitmapFile));
      decodeDrawable = Drawable.createFromPath(path + posid);
      bins.close();
    }
    return decodeDrawable;
  }

  // 下载
  private boolean downLoad(final String url, Context context, String posid) {
    if (TextUtils.isEmpty(url)) {
      return false;
    }
    try {
      Lg.d("downLoad() , bitmapUrl----->" + url);
      Bitmap downLoadBitmap = downLoadBitmap(url);
      if (downLoadBitmap != null) {
        return saveBitmap(downLoadBitmap, context, posid, url);
      } else {
        Lg.d("downLoadBitmap----->saveBitmap------->false-----downLoadBitmap=null");
      }
    } catch (IOException e) {
      Lg.d("downLoadBitmap----->saveBitmap------->false" + e.getMessage());
      e.printStackTrace();
    }
    return false;
  }

  // 下载图片文件
  private Bitmap downLoadBitmap(String url) {
    URL imgUrl = null;
    Bitmap bitmap = null;
    try {
      imgUrl = new URL(url);
      try {
        HttpURLConnection urlConn = (HttpURLConnection) imgUrl.openConnection();
        urlConn.setDoInput(true);
        urlConn.connect();
        // 将得到的数据转化成InputStream
        InputStream is = urlConn.getInputStream();
        // 将InputStream转换成Bitmap
        bitmap = BitmapFactory.decodeStream(is);
        is.close();
        return bitmap;
      } catch (IOException e) {
        Lg.d("downLoadBitmap------------>false");
        e.printStackTrace();
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 检查下载的文件是否存在<br>
   * NOTE(ljs):
   *
   * 1.此处仅仅靠sp来盘点文件是否存在逻辑上存在不合理性<br>
   * 2.后续要求添加对alllength==0做判断,这方面是否合理有待商定
   *
   * @return 存在返回true;
   */
  private boolean checkDownLoadFile(Context context, int length, String adposid) {
    String path = context.getFilesDir() + File.separator + adposid;
    File bitmapFile = new File(path);
    return SharedPreferencesManager.getBoolean(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_BOOT_AD_SAVE, false) && bitmapFile.exists() && length > 0;
  }

  /**
   * 保存图片到/data/data/packagename/files/文件下
   *
   * @param bitmap
   *          图片
   * @param url
   *
   * @throws IOException
   */
  private boolean saveBitmap(Bitmap bitmap, Context context, String adposid, String url) throws IOException {
    String path = context.getFilesDir() + File.separator;
    File dirFile = new File(path);
    if (!dirFile.exists()) {
      boolean mkdir = dirFile.mkdir();
      if (!mkdir) {
        return false;
      }
    }
    File bitmapFile = new File(path + adposid);
    Lg.d("path + BitmapName:" + path + adposid);
    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(bitmapFile));
    if (url.toLowerCase().contains(".png")) {
      bitmap.compress(Bitmap.CompressFormat.PNG, 80, bos);
      Lg.d("downLoadBitmap----->Bitmap------->PNG");
    } else {
      bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
      Lg.d("downLoadBitmap----->Bitmap------->JPEG");
    }
    bos.flush();
    bos.close();
    // bitmap.recycle();
    // bitmap = null;
    Lg.d("downLoadBitmap----->saveBitmap------->true");
    return true;
  }

  /**
   * 更新开机广告
   *
   * @param context
   * @param adRegister
   * @param adposId
   */
  public void updateApkBootAd(final Context context, final AdRegister adRegister, final String adposId) {
    Lg.d("AdPosBootListener , updateApkBootAd() , adposId = " + adposId);
    if (adRegister == null) {
      return;
    }
    if (adRegister.posIds == null || adRegister.posIds.size() == 0) {
      return;
    }
    if (!adRegister.isContainPosId(adposId)) {
      Lg.e("注册接口无此adposId = " + adposId);
      Lg.e("删除开机广告介质");
      String path = context.getFilesDir() + File.separator + adposId;
      File bitmapFile = new File(path);
      if (bitmapFile != null && bitmapFile.exists()) {
        deleteFile(bitmapFile);
        SharedPreferencesManager.putBoolean(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_BOOT_AD_SAVE, false);
      }
      SharedPreferencesManager.putBoolean(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_APP_OEMID_BAN, true);
      return;
    }
    if (TextUtils.isEmpty(adRegister.defaultadinf)) {
      return;
    }
    String defaultadinf = adRegister.defaultadinf.replace("<adpos>", adposId);
    defaultadinf = defaultadinf.replace("<mid>", "");
    defaultadinf = defaultadinf.replace("<fid>", "");
    defaultadinf = defaultadinf.replace("<catcode>", "");
    defaultadinf = defaultadinf.replace("<mtype>", "");
    defaultadinf = defaultadinf.replace("<version>", VAdType.AD_INTERFACE_VERSION);
    Lg.i("updateApkBootAd() , url = " + defaultadinf);
    ApiDataParser apiDataParser = new ApiDataParser();
    apiDataParser.synRequestApiData(defaultadinf, new ApiResponseListener<AdInfo>() {

      @Override
      public void onApiCompleted(AdInfo data) {
        if (data != null && data.adPostions != null && data.adPostions.size() > 0) {
          SharedPreferencesManager.putBoolean(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_APP_OEMID_BAN, false);
          // NOTE(ljs):APK第一次启动要上报库存
          // FIXME(ljs):不确定在apk升级的时候会不会对SharedPreferences有影响?需要测试调查
          if (SharedPreferencesManager.getBoolean(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_APP_IS_FIRST, true)) {
            Lg.d("app 第一次启动要上报库存");
            SharedPreferencesManager.putBoolean(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_APP_IS_FIRST, false);
            String reportvalue = data.adPostions.get(0).mediaInfoList.get(0).getReportvalue();
            String reporturl = adRegister.defaultreporturl;
            ReportManager.getInstance().report(reportvalue, 0, ReportManager.Start, reporturl, adposId);
          }

          String savedAmid = SharedPreferencesManager.getString(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_BOOT_AD_AMID, "");
          String savedSource = SharedPreferencesManager.getString(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_BOOT_AD_SOURCE, "");
          int saveLength = SharedPreferencesManager.getInt(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_BOOT_AD_LENGTH, 0);

          String currentAmid = data.adPostions.get(0).mediaInfoList.get(0).getAmid();
          String currentSource = data.adPostions.get(0).mediaInfoList.get(0).getSource();
          int currentLength = Integer.parseInt(data.adPostions.get(0).mediaInfoList.get(0).getLength());
          String currentPos = data.adPostions.get(0).id.substring(0, 2);
          String currentPosId = data.adPostions.get(0).id;
          Lg.e("updateApkBootAd() , savedSource = " + savedSource + " , currentSource = " + currentSource);
          Lg.e("updateApkBootAd() , saveLength = " + saveLength + " , currentLength = " + currentLength);
          Lg.e("updateApkBootAd() , savedAmid = " + savedAmid + " , currentAmid = " + currentAmid);
          if (TextUtils.isEmpty(savedAmid) || !savedAmid.equals(currentAmid) || saveLength != currentLength || !savedSource.equals(currentSource)) {
            Lg.e("apk启动广告属性变更,保存当前属性");
            SharedPreferencesManager.putString(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_BOOT_AD_REPORTVALUE, data.adPostions.get(0).mediaInfoList.get(0).getReportvalue());
            SharedPreferencesManager.putString(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_BOOT_AD_REPORTURL, adRegister.defaultreporturl);
            Lg.i("save reportUrl = " + adRegister.defaultreporturl);
            SharedPreferencesManager.putInt(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_BOOT_AD_LENGTH, currentLength);
            SharedPreferencesManager.putString(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_BOOT_AD_AMID, currentAmid);
            SharedPreferencesManager.putString(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_BOOT_AD_ADPOS, currentPos);
            SharedPreferencesManager.putString(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_BOOT_AD_ADPOSID, currentPosId);
            SharedPreferencesManager.putString(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_BOOT_AD_SOURCE, currentSource);
            boolean isSuccess = false;

            if (TextUtils.isEmpty(currentSource)) {
              Lg.e("media info source is null , go to deletefile======>adposId = " + adposId);
              // NOTE(ljs):/data/data/packagename/files/
              String path = context.getFilesDir() + File.separator + adposId;
              File bitmapFile = new File(path);
              if (bitmapFile != null && bitmapFile.exists()) {
                deleteFile(bitmapFile);
              }
            } else {
              Lg.e("start download apk boot ad");
              isSuccess = downLoad(currentSource, context, adposId);
            }
            SharedPreferencesManager.putBoolean(Config.SHARED_PREFERENCES_SET, Config.SHARED_PREFERENCES_BOOT_AD_SAVE, isSuccess);
          }
        }
      }
    });
  }

  public void deleteFile(File file) {
    if (file.exists()) { // 判断文件是否存在
      if (file.isFile()) { // 判断是否是文件
        file.delete();
      } else if (file.isDirectory()) { // 否则如果它是一个目录
        File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
        for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
          deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
        }
      }
      file.delete();
    } else {
      Lg.d("the file not exist！" + "\n");
    }
  }
}