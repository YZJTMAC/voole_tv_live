/**
 *
 */
package com.vad.sdk.core.view.v30;

import com.vad.sdk.core.Utils.v30.Lg;
import com.voole.android.client.UpAndAu.downloader.ApkDownloader;
import com.voole.android.client.UpAndAu.downloader.FileDownLoaderListener;
import com.voole.android.client.UpAndAu.upgrade.UpgradeManager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.io.File;

/**
 *
 * @author luojunsheng
 * @date 2016年4月14日 下午5:12:23
 * @version 1.1
 */
public class DownloadAPkManager {
  private final static int DOWLOAD_START = 1;
  private final static int DOWLOAD_ERROR = 2;
  private final static int DOWLOAD_SUCCESS = 3;
  private Context mContext;
  private String mFileName;
  private String mCurrentDownloadUrl;
  private String mAppName;
  private boolean mIsLoading = false;
  private String mFileSaveDirStr;
  private ApkDownloader mApkLoader;
  private Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
      case DOWLOAD_START:
        Toast.makeText(mContext, "开始下载文件", Toast.LENGTH_LONG).show();
        break;
      case DOWLOAD_ERROR:
        Toast.makeText(mContext, "文件下载出错啦...", Toast.LENGTH_LONG).show();
        break;
      case DOWLOAD_SUCCESS:
        Toast.makeText(mContext, "文件下载结束,准备安装apk", Toast.LENGTH_LONG).show();
        installApp();
        break;
      }
    }

  };

  public DownloadAPkManager(Context context, String fileName, String appName) {
    mContext = context;
    mFileName = fileName;
    mAppName = appName;
  }

  public void startDownload(String apkDownalodUrl) {
    if (mCurrentDownloadUrl != null) {
      if (mCurrentDownloadUrl.equals(apkDownalodUrl) && mIsLoading) {
        Toast.makeText(mContext, "已经开始下载apk...", Toast.LENGTH_LONG).show();
        return;
      } else if (!mCurrentDownloadUrl.equals(apkDownalodUrl) && mIsLoading) {
        Toast.makeText(mContext, "正在下载其他apk,稍后重试", Toast.LENGTH_LONG).show();
        return;
      }
    }
    mCurrentDownloadUrl = apkDownalodUrl;
    mFileSaveDirStr = mContext.getFilesDir().getAbsolutePath();
    mApkLoader = new ApkDownloader(mCurrentDownloadUrl, mFileSaveDirStr, mFileName, new FileDownLoaderListener() {

      @Override
      public void onFileDownLoadProgress(int size) {
        Lg.d("DownloadAPkManager , startDownload()#onFileDownLoadProgress , size = " + size);
      }

      @Override
      public void onFileDownLoadError(String msg) {
        Lg.e("DownloadAPkManager , startDownload()#onFileDownLoadError , msg = " + msg);
        mIsLoading = false;
        mHandler.sendEmptyMessage(DOWLOAD_ERROR);
      }

      @Override
      public void onFileDownLoadEnd() {
        Lg.e("DownloadAPkManager , startDownload()#onFileDownLoadEnd");
        mIsLoading = false;
        mCurrentDownloadUrl = null;
        File apkFile = new File(mFileSaveDirStr + "/" + mFileName);
        apkFile.setReadable(true, false);
        mHandler.sendEmptyMessage(DOWLOAD_SUCCESS);
      }

      @Override
      public void onFileDownLoadBegin(int fileSize) {
        Lg.e("DownloadAPkManager , startDownload()#onFileDownLoadBegin , fileSize = " + fileSize);
        mIsLoading = true;
        mHandler.sendEmptyMessage(DOWLOAD_START);
      }
    });
    new Thread() {
      @Override
      public void run() {
        super.run();
        mApkLoader.download();
      }
    }.start();
  }

  private void installApp() {
    Lg.e("DownloadAPkManager , installApp()");
    File dir = new File(mFileSaveDirStr, mFileName);
    if (!dir.exists()) {
      Toast.makeText(mContext, "找不到安装文件", Toast.LENGTH_LONG).show();
      return;
    }
    try {
      UpgradeManager.GetInstance().install(mContext, dir);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
