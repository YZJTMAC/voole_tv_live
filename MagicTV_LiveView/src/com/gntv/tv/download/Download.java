package com.gntv.tv.download;

import java.io.File;
import java.text.NumberFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.Toast;

import com.gntv.tv.R;
import com.gntv.tv.common.ap.AuthManager;
import com.gntv.tv.common.ap.ProxyManager;
import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.view.DownLoadDialog;
import com.gntv.tv.view.base.TVAlertDialog;
import com.voole.android.client.UpAndAu.downloader.ApkDownloader;
import com.voole.android.client.UpAndAu.downloader.FileDownLoaderListener;

public class Download {
	private static final int END_UPGRADE = 100;
	public static final int HAS_UPGRADE = 101;
	private static final int ERROR_UPGRADE = 102;
	private static final int UPGRADE_PROGRESS = 103; 
	private static final int START_UPGRADE = 104;
	private String apkName = null;
	private String apkPath = null;
	private boolean isCancel = false;
	private Activity mActivity = null;
	
	private DownLoadDialog.Builder downLoadDialogBuild = null;
	private DownLoadDialog downloadProgressDialog = null;
	private final NumberFormat numberFormat = NumberFormat.getPercentInstance();
	private ApkDownloader apkLoader;
	private int fileSize = 0;
	private int currentSize = 0;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(isCancel){
				return;
			}
			switch (msg.what) {
			case END_UPGRADE:
				if (downloadProgressDialog != null) {
					downLoadDialogBuild.setProgress(downLoadDialogBuild.getMaxProgress());
					downLoadDialogBuild.setMessage("下载完毕");
					downloadProgressDialog.dismiss();
					downloadProgressDialog = null;
				}
	            new TVAlertDialog.Builder(mActivity)
				.setTitle("软件包下载完毕,请您安装")
		        .setCancelable(false)
		        .setPositiveButton("确定", new DialogInterface.OnClickListener(){
		            @Override
		            public void onClick(DialogInterface dialog, int whichButton){
		            	installApp();
		            	dialog.cancel();
		            	//mActivity.finish();
		            }
		        }).create().show();
				break;
			case ERROR_UPGRADE:
	        	new TVAlertDialog.Builder(mActivity)
	        	.setCancelable(false)
	            .setTitle("下载文件时出现异常")
//	            .setMessage(updateInfo.getIntroduction())
	            .setPositiveButton("确定", new DialogInterface.OnClickListener(){
	                @Override
	                public void onClick(DialogInterface dialog, int whichButton){
	                	if(downloadProgressDialog != null){
	    		            downloadProgressDialog.dismiss();
	    		            downloadProgressDialog = null;
	    				}
	                	killMyself();
	                }
	            }).create().show();
				break;
			case UPGRADE_PROGRESS:
				downLoadDialogBuild.setProgress(currentSize);
		        String result = numberFormat.format((float)currentSize / (float)fileSize); 
		        downLoadDialogBuild.setMessage("下载中"+result);
				break;
			case START_UPGRADE:
				downLoadDialogBuild.setMaxProgress(fileSize);
				break;
			default:
				break;
			}
		}
	};
	
	public Download(Activity context) {
		this.mActivity = context;
		numberFormat.setMaximumFractionDigits(0);  
	}

	/**
	 * 
	 * @param downLoadUrl 下载地址
	 * @param saveFilePath  本地保存apk路径
	 * @param apkName 下载后保存到本地的apk名称
	 */
	public synchronized void startToDownload(final String downLoadUrl,
			final String saveFilePath, final String apkName) {
		LogUtil.d("startToDownload--->downLoadUrl-->" + downLoadUrl + "---saveFilePath-->" + saveFilePath + "--apkName-->" + apkName);
		this.apkPath = saveFilePath;
		this.apkName = apkName;
		// 这里开启一个线程避免ANR错误
		initProgressDialog();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					apkLoader = new ApkDownloader(downLoadUrl, saveFilePath, apkName, new FileDownLoaderListener() {
						@SuppressLint("NewApi")
						@Override
						public void onFileDownLoadProgress(int size) {
							// 实时获知文件已经下载的数据长度
							LogUtil.d("startToDownload-->onFileDownLoadProgress-->" + size);
							currentSize = size / 1024;
							handler.sendEmptyMessage(UPGRADE_PROGRESS);
						}
						@Override
						public void onFileDownLoadError(String msg) {
							LogUtil.d("startToDownload-->onFileDownLoadError");
							Message message = Message.obtain();
							message.what = ERROR_UPGRADE;
							message.obj = msg;
							handler.sendMessage(message);
						}
						
						@SuppressLint("NewApi")
						@Override
						public void onFileDownLoadEnd() {
							File apkFile = new File(saveFilePath + "/" + apkName);
							apkFile.setReadable(true, false);
							handler.sendEmptyMessage(END_UPGRADE);
						}
						
						@Override
						public void onFileDownLoadBegin(int size) {
							LogUtil.d("startToDownload-->onFileDownLoadBegin---fileSize--->" + fileSize);
							fileSize = size / 1024;
							handler.sendEmptyMessage(START_UPGRADE);
						}
					});
					apkLoader.download();
				} catch (Exception e) {
					// 发送一个空消息到消息队列
					Message message = Message.obtain();
					message.what = ERROR_UPGRADE;
					message.obj = mActivity.getResources().getString(
							R.string.server_conn_error);
					handler.sendMessage(message);
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/**
	 * 初始化进度条弹窗
	 */
	private void initProgressDialog() {
		if (downLoadDialogBuild == null) {
			downLoadDialogBuild = new DownLoadDialog.Builder(mActivity);
			downLoadDialogBuild.setCancelable(false);
			downLoadDialogBuild.setNegativeButtonClickListener(new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//TODO 取消下载
					isCancel = true;
					dialog.cancel();
					apkLoader.stopFileDownLoader();
				}
			});
			downLoadDialogBuild.setPositiveButtonClickListener(new OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//TODO 后台下载
					dialog.cancel();
					
				}
			});
			downloadProgressDialog = downLoadDialogBuild.create();
			downloadProgressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		}
		downloadProgressDialog.show();
	}
	
	private void installApp(){
		/*new Thread(new Runnable() {
			@Override
			public void run() {
				ProxyManager.GetInstance().exitProxy();
				ProxyManager.GetInstance().deleteProxyFiles();
				//LocalManager.GetInstance().deleteFiles();
			}
		}).start();*/
    	File dir = new File(apkPath, apkName);
        if (!dir.exists()) {  
        	Toast.makeText(mActivity, "找不到安装文件", Toast.LENGTH_SHORT).show();
        	return;
        }
        install(mActivity, dir);
    	//killMyself();
	}
	
	private void install(Context context, File apkFile){
		Intent intent = new Intent(Intent.ACTION_VIEW);
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
    	context.startActivity(intent);    	
	}
	
	private void killMyself(){
		AuthManager.GetInstance().exitAuth();
		ProxyManager.GetInstance().exitProxy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
