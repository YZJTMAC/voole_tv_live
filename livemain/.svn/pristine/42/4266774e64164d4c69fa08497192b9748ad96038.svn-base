package com.gntv.tv.upgrade;

import java.io.File;
import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.view.WindowManager;

import com.gntv.tv.R;
import com.gntv.tv.common.ap.AuthManager;
import com.gntv.tv.common.ap.ProxyManager;
import com.gntv.tv.common.utils.DeviceUtil;
import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.model.base.VLService;
import com.gntv.tv.model.cache.CacheManager;
import com.gntv.tv.model.error.ErrorManager;
import com.gntv.tv.view.DownLoadDialog;
import com.gntv.tv.view.base.TVAlertDialog;
import com.voole.android.client.UpAndAu.constants.AuxiliaryConstants;
import com.voole.android.client.UpAndAu.downloader.FileDownLoaderListener;
import com.voole.android.client.UpAndAu.downloader.FileDownloader;
import com.voole.android.client.UpAndAu.model.UpdateInfo;
import com.voole.android.client.UpAndAu.upgrade.AppUpgradeAuxiliaryer;
import com.voole.android.client.UpAndAu.upgrade.AppUpgradeAuxiliaryer.UpgradeVersionCheckCallBack;
import com.voole.android.client.UpAndAu.upgrade.UpgradeManager;
import com.voole.statistics.report.ReportManager;

public class UpgradeService extends Service {
	private final static String APK_FILE_NAME = "VoolePlay.apk";
	private static final int ERROR_DOWNLOAD = 104;
	private static final int END_UPGRADE = 100;
	private static final int UPGRADE_PROGRESS = 1;
	private static final int START_UPGRADE = 2;
	private Intent intent;
	private boolean isCancel = false;
	private final NumberFormat numberFormat = NumberFormat.getPercentInstance();
	private FileDownloader loader;
	private MyBinder myBinder = new MyBinder();
	private float mLastProgress = 0;

	@Override
	public IBinder onBind(Intent arg0) {
		return myBinder;
	}

	public class MyBinder extends Binder {
		public UpgradeService getService() {
			return UpgradeService.this;
		}
	}

	@Override
	public void onCreate() {
		LogUtil.d("UpgradeService--->onCreate");
		super.onCreate();
		numberFormat.setMaximumFractionDigits(0);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			if (isCancel) {
				cancleTask();
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
				cancleTask();
				installApp();
				break;
			case ERROR_DOWNLOAD:
				cancleTask();
				// String info="下载文件时出现异常";
				if (msg.obj != null && "0001".equals(String.valueOf(msg.obj))) {
					showUpgradeErrorDialog("磁盘空间不足，无法下载");
				} else {
					showUpgradeErrorDialog(ErrorManager.GetInstance().getErrorMsg(ErrorManager.ERROR_UPGRADE_DOWNLOAD)
							.getErroeMessageAndCode());
				}
				break;
			case UPGRADE_PROGRESS:
				downLoadDialogBuild.setProgress(currentSize);
				LogUtil.d("UpgradeService----->UPGRADE_PROGRESS------currentSize:"+currentSize);
				String result = numberFormat.format((float) currentSize / fileSize);
				downLoadDialogBuild.setMessage("下载中" + result);
				break;
			case START_UPGRADE:
				downLoadDialogBuild.setMaxProgress(fileSize);
				LogUtil.d("UpgradeService----->START_UPGRADE------fileSize:"+fileSize);
				break;
			default:
				break;
			}
		}

	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogUtil.d("UpgradeService--->onStartCommand");
		if (intent != null) {
			this.intent = intent;
			getData(intent);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void getData(Intent i) {
		final String checkVersionUrl = i.getStringExtra("baseUrl");
		final String appid = i.getStringExtra("appId");
		final String versionCode = i.getStringExtra("versionCode");
		final String oemid = i.getStringExtra("oemid");
		final String uid = i.getStringExtra("uid");
		final String hid = i.getStringExtra("hid");
		LogUtil.d("UpgradeService-->getdata-->checkVersionUrl-->" + checkVersionUrl);
		LogUtil.d("UpgradeService-->getdata-->appid-->" + appid);
		LogUtil.d("UpgradeService-->getdata-->versionCode-->" + versionCode);
		LogUtil.d("UpgradeService-->getdata-->oemid-->" + oemid);
		LogUtil.d("UpgradeService-->getdata-->uid-->" + uid);
		LogUtil.d("UpgradeService-->getdata-->hid-->" + hid);
		// String checkVersionUrl =
		// "http://appinterface.voole.com/?&uid=131467656&oemid=817&hid=acdbda005c8d0000000000000000000000000000&app_version=vooleplay_gen_2.4";
		if (!TextUtils.isEmpty(checkVersionUrl)) {
			checkVersion(checkVersionUrl, versionCode, appid, oemid, uid, hid);
		}
	}

	private void checkVersion(String checkVersionUrl, String versionCode, String appid, String oemid, String uid,
			String hid) {
		String host = null;
		if (checkVersionUrl != null && checkVersionUrl.contains("?")) {
			host = checkVersionUrl.substring(0, checkVersionUrl.indexOf("?"));
		}

		String packageName = DeviceUtil.getAppName(this);
		String ip = DeviceUtil.getLocalIpAddress();

		AuxiliaryConstants.isCheckProxySpeed = false;
		if (host != null) {
			appUpgradeAuxiliaryer = new AppUpgradeAuxiliaryer(host, oemid, appid, packageName, versionCode, hid, uid,
					ip, new MyUpgradeVersionCheckCallBack()).checkVersion();
		}
	}

	private AppUpgradeAuxiliaryer appUpgradeAuxiliaryer = null;
	private UpdateInfo updateInfo = null;

	private class MyUpgradeVersionCheckCallBack implements UpgradeVersionCheckCallBack {
		@Override
		public void onHasUpgrade(UpdateInfo updateInfoArg) {
			LogUtil.d("UpgradeService-->Upgrade-->onHasUpgrade");
			updateInfo = updateInfoArg;
			appUpgradeAuxiliaryer.setSendTime(updateInfo);
			showUpgradeDialog();
		}

		@Override
		public void onError(String errorMsg) {
			LogUtil.d("UpgradeService-->Upgrade-->onError");
			// showUpgradeDialog();
		}

		@Override
		public void onNOUpgrade(UpdateInfo updateInfo) {
			LogUtil.d("UpgradeService-->Upgrade-->onNOUpgrade-1111111111111111111111111111111111->"
					+ updateInfo.getAppid());
			appUpgradeAuxiliaryer.setSendTime(updateInfo);
			// showUpgradeDialog();
		}
	}

	private float fileSize = 0;
	private float currentSize = 0;

	private synchronized void startToUpdate(final String downLoadUrl, final String saveFilePath, final String apkName,
			final String fid, final String currentversion) {

		// 这里开启一个线程避免ANR错误
		initProgressDialog();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					final NumberFormat numberFormat = NumberFormat.getPercentInstance();
					numberFormat.setMaximumFractionDigits(0);
					loader = new FileDownloader(downLoadUrl, saveFilePath, apkName, 1, fid, currentversion,
							new FileDownLoaderListener() {
						@Override
						public void onFileDownLoadBegin(int size) {
							fileSize = size / 1024;
							handler.sendEmptyMessage(START_UPGRADE);
						}

						@Override
						public void onFileDownLoadProgress(int size) {
							currentSize = size / 1024;
							handler.sendEmptyMessage(UPGRADE_PROGRESS);
						}

						@Override
						public void onFileDownLoadEnd() {
							LogUtil.d("Upgrade-->onFileDownLoadEnd");
							File apkFile = new File(saveFilePath + "/" + apkName);
							apkFile.setReadable(true, false);
							handler.sendEmptyMessage(END_UPGRADE);
						}

						@Override
						public void onFileDownLoadError(String errorMsg) {
							LogUtil.d("Upgrade-->onFileDownLoadError");
							Message message = Message.obtain();
							message.what = ERROR_DOWNLOAD;
							message.obj = errorMsg;
							handler.sendMessage(message);
						}
					});
					LogUtil.d("startToUpdate--->downLoadUrl--->" + downLoadUrl);
					loader.download();
					mTimer = new Timer();
					mTask = new TimerTask() {

						@Override
						public void run() {
							LogUtil.d("UpgradeService----->TimerTask------>currentSize:"+currentSize);
							LogUtil.d("UpgradeService----->TimerTask------>mLastProgress:"+mLastProgress);
							if (currentSize - mLastProgress > 0) {
								mLastProgress = currentSize;
							} else {
								Message message = Message.obtain();
								message.what = ERROR_DOWNLOAD;
								handler.sendMessage(message);
							}
						}
					};
					mTimer.schedule(mTask, 90 * 1000, 90 * 1000);
				} catch (Exception e) {
					LogUtil.d("Upgrade-->Exception--->" + e.toString());
					Message message = Message.obtain();
					message.what = ERROR_DOWNLOAD;
					message.obj = getResources().getString(R.string.server_conn_error);
					handler.sendMessage(message);
				}
			}
		}).start();
	}

	private boolean isForceUpgrade = false;

	private void showUpgradeDialog() {
		if (upgradeTipDialog != null) {
			if (upgradeTipDialog.isShowing()) {
				return;
			}
		}
		if (downloadProgressDialog != null) {
			if (downloadProgressDialog.isShowing()) {
				return;
			}
		}
		isForceUpgrade = updateInfo.upgradeRequired();
		// isUpgrade = true;
		UpgradeDialog.Builder upGradeBuilder = new UpgradeDialog.Builder(this);
		upGradeBuilder.setCancelable(false).setTitle("有新版本请更新").setUpgradeContent(updateInfo.getIntroduction())
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
						startToUpdate(updateInfo.getDownloadUrl(), getCacheDir().getPath(), APK_FILE_NAME,
								updateInfo.getFid(), updateInfo.getCurrentVersion());
					}
				});
		if (!isForceUpgrade) {
			upGradeBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					isCancel = true;
				}
			});
		}
		if (isForceUpgrade) {
			sendBroadcast(new Intent("com.voole.magictv.upgradereceiver"));
		}
		upgradeTipDialog = upGradeBuilder.create();
		upgradeTipDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		upgradeTipDialog.show();
	}

	private UpgradeDialog upgradeTipDialog = null;
	private TVAlertDialog upgradeErrorDialog = null;

	private void showUpgradeErrorDialog(String info) {
		TVAlertDialog.Builder dialogBuilder = new TVAlertDialog.Builder(this);
		dialogBuilder.setCancelable(false);
		/* dialogBuilder.setDialogContent(info); */
		dialogBuilder.setTitle(info);
		dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				if (downloadProgressDialog != null) {
					if (downloadProgressDialog.isShowing()) {
						downloadProgressDialog.dismiss();
						isCancel = true;
						loader.stopFileDownLoader();
					}
				}
				if (isForceUpgrade) {
					new Thread(new Runnable() {
						@Override
						public synchronized void run() {
							ProxyManager.GetInstance().exitProxy();
							AuthManager.GetInstance().exitAuth();
							stopVLService();
							ReportManager.getInstance().release();
							android.os.Process.killProcess(android.os.Process.myPid());
						}
					}).start();
				}
				stopUpgradeCheck();
			}
		});
		/* TVAlertDialog dialog = dialogBuilder.create1(); */
		upgradeErrorDialog = dialogBuilder.create();
		upgradeErrorDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		upgradeErrorDialog.show();
	}

	private DownLoadDialog.Builder downLoadDialogBuild = null;
	private DownLoadDialog downloadProgressDialog = null;

	private void initProgressDialog() {
		if (downLoadDialogBuild == null) {
			downLoadDialogBuild = new DownLoadDialog.Builder(this);
			downLoadDialogBuild.setCancelable(false);
			if (isForceUpgrade) {
				downLoadDialogBuild.setForceUpGrade();
			} else {
				downLoadDialogBuild.setNegativeButtonClickListener(new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 取消下载
						cancleTask();
						isCancel = true;
						loader.stopFileDownLoader();
						isForceStop = false;
						dialog.cancel();
					}
				});
				downLoadDialogBuild.setPositiveButtonClickListener(new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						// 后台下载
					}
				});
			}
			downloadProgressDialog = downLoadDialogBuild.create();
			downloadProgressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		}
		if (!downloadProgressDialog.isShowing()) {
			if (upgradeTipDialog != null) {
				if (upgradeTipDialog.isShowing()) {
					upgradeTipDialog.dismiss();
				}
			}
			downloadProgressDialog.show();
			isCancel = false;
		}
	}

	private void installApp() {
		File dir = new File(getCacheDir().getPath(), APK_FILE_NAME);
		if (!dir.exists()) {
			showUpgradeErrorDialog("找不到安装文件");
			return;
		}
		cancleTask();
		UpgradeManager.GetInstance().install(this, dir);
		new Thread(new Runnable() {
			@Override
			public synchronized void run() {
				ProxyManager.GetInstance().exitProxy();
				AuthManager.GetInstance().exitAuth();
				ProxyManager.GetInstance().deleteProxyFiles();
				AuthManager.GetInstance().deleteAuthFiles();
				CacheManager.GetInstance().clear();
				stopVLService();
				stopUpgradeCheck();
				ReportManager.getInstance().release();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		}).start();
	}

	private void stopVLService() {
		Intent timeIntent = new Intent(this, VLService.class);
		stopService(timeIntent); // 停止获取时间服务
	}

	private void stopUpgradeCheck() {
		Intent i = new Intent();
		i.setClass(this, UpgradeService.class);
		stopService(i);
	}

	private boolean isForceStop = false;

	public boolean cancleDialog() {
		LogUtil.d("UpgradeService------->cancleDialog()");
		if (upgradeErrorDialog != null) {
			if (upgradeErrorDialog.isShowing()) {
				LogUtil.d("UpgradeService------->cancleDialog()---->upgradeErrorDialog.isShowing()");
				upgradeErrorDialog.dismiss();
				isForceStop = true;
			}
		}
		if (upgradeTipDialog != null) {
			if (upgradeTipDialog.isShowing()) {
				LogUtil.d("UpgradeService------->cancleDialog()------->upgradeTipDialog.isShowing()");
				upgradeTipDialog.dismiss();
				isForceStop = true;
			}
		}
		if (downloadProgressDialog != null) {
			if (downloadProgressDialog.isShowing()) {
				LogUtil.d("UpgradeService------->cancleDialog()------->downloadProgressDialog.isShowing()");
				downloadProgressDialog.dismiss();
				isForceStop = true;
			}
			if (loader != null) {
				LogUtil.d("UpgradeService------->cancleDialog()------>loader != null");
				loader.stopFileDownLoader();
				isForceStop = true;
			}
		}
		cancleTask();
		if (isCancel) {
			LogUtil.d("UpgradeService------->cancleDialog()--------->isCancel=true");
			return false;
		}
		isCancel = true;
		return isForceStop;
	}

	public void reCheck() {
		LogUtil.d("UpgradeService------->reCheck()");
		if (intent != null) {
			if (isForceStop) {
				LogUtil.d("UpgradeService------->reCheck()----->isForceStop");
				isForceStop = false;
				getData(intent);
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		cancleTask();
	}

	private TimerTask mTask;
	private Timer mTimer;

	private void cancleTask() {
		LogUtil.d("UpgradeService------->cancleTask()");
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		if (mTask != null) {
			mTask.cancel();
			mTask = null;
		}
	}
}
