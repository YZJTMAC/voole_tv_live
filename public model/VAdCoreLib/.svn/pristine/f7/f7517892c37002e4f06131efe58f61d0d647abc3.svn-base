package com.vad.sdk.core.view.v30;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.voole.android.client.UpAndAu.downloader.ApkDownloader;
import com.voole.android.client.UpAndAu.upgrade.UpgradeManager;

/**
 *
 * @version 1
 * @author zhangzexin
 * @since 2015-8-24 下午4:37:43
 */

public class DownDialog {
  private Context context;
  private String fileName;
  private String url;
  private String appName;
  private ProgressDialog progressDialog;
  private String fileSaveDirStr;
  private DownloadForTVD downDialog;

  public DownDialog(Context context) {
	    super();
	    this.context = context;
	  }
  
//  public DownDialog(Context context, String url, String fileName, String appName) {
//    super();
//    this.appName = appName;
//    this.fileName = fileName;
//    this.url = url;
//    this.context = context;
//  }

	public void start(String url, String fileName, String appName) {
		initDialog(url, fileName, appName);
	}

  
  private void startDownload() {
		if (downDialog == null) {
			downDialog = new DownloadForTVD((Activity) context);
		}
    downDialog.startToDownload(url, context.getFilesDir().getAbsolutePath(), appName);
  }

  private void initDialog(String url, String fileName, String appName ) {
		this.url = url;
		this.fileName = fileName;
		this.appName = appName;

		TVAlertDialog.Builder builder = new TVAlertDialog.Builder(context);
		builder.setTitle("请下载安装应用，内容更精彩哦~");
		builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				startDownload();
			}
		});
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		TVAlertDialog dialog = builder.create();
		dialog.show();
	}
}
