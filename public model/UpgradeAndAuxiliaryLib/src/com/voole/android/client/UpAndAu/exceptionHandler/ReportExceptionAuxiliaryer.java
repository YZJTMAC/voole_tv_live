package com.voole.android.client.UpAndAu.exceptionHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Environment;

import com.voole.android.client.UpAndAu.auxiliary.AuxiliaryService;
import com.voole.android.client.UpAndAu.model.DataResult;
import com.voole.android.client.UpAndAu.model.ExceptionFeedBackInfo;
import com.voole.android.client.UpAndAu.util.Logger;
import com.voole.android.client.UpAndAu.util.StringUtil;

/**
 * 异常收集汇报辅助类
 * 
 * @version V1.0
 * @author wusong
 * @time 2013-4-12下午2:51:00
 */
public class ReportExceptionAuxiliaryer {
	private static ReportExceptionAuxiliaryer INSTANCE;
	private String appName;
	private String version;
	private Context context;
	/**
	 * 网络提交异常的返回结果
	 */
	private DataResult dataResult;
	/**
	 * 定义保存异常信息的路径
	 */
	private String logPath;
	/**
	 * 保存异常的文件
	 */

	/**
	 * Url : 提交异常的地址包括参数
	 */
	private String url;

	/**
	 * 获取提交异常的地址包括参数
	 * 
	 * @return String Url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 保证只有一个CrashHandler实例
	 * 
	 * @param logPath
	 * @param url
	 */
	private ReportExceptionAuxiliaryer(Context context, String url,
			String logPath) {
		this.context = context;
		this.url = url;
		this.logPath = logPath;
	}

	/**
	 * 获取CrashHandler实例 ,单例模式
	 * 
	 * @param context
	 * @param url
	 * @param logPath
	 * @return ReportExceptionAuxiliaryer
	 */
	public static ReportExceptionAuxiliaryer getInstance(Context context,
			String url, String logPath) {
		if (INSTANCE == null) {
			INSTANCE = new ReportExceptionAuxiliaryer(context, url, logPath);
		}
		return INSTANCE;
	}

	/**
	 * 启动异常捕获
	 */
	public void activateExceptionCrash() {
		initInfo();
		VooleCrashHandler crashHandler = VooleCrashHandler.getInstance();
		crashHandler.init(context, INSTANCE);
		initInfo();

	}

	/**
	 * 初始化应用信息
	 */
	private void initInfo() {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				version = pi.versionName == null ? "null" : pi.versionName;
			}
			CharSequence cs = pm.getApplicationLabel(context
					.getApplicationInfo());
			appName = cs.toString();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 得到异常信息封装类的model
	 * 
	 * @return ExceptionFeedBackInfo
	 */
	public ExceptionFeedBackInfo getExFBI() {
		ExceptionFeedBackInfo exFBI = new ExceptionFeedBackInfo();
		exFBI.setAppName(appName);
		exFBI.setVersion(version);
		return exFBI;
	}

	/**
	 * 获取返回结果信息
	 * 
	 * @return DataResult
	 */
	public DataResult getDataResult() {
		return dataResult;
	}

	/**
	 * 设置返回结果
	 * 
	 * @param dataResult
	 */
	public void setDataResult(DataResult dataResult) {
		this.dataResult = dataResult;
	}

	/**
	 * @return the logPath
	 */
	public String getLogPath() {
		if (StringUtil.isNull(logPath)) {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				String sdDir = Environment.getExternalStorageDirectory()
						.toString();
				logPath = sdDir + "/voole/errorlog";
			}
		}
		return logPath;
	}

	/**
	 * 获取给定路径下,的所有异常信息文件
	 */
	public void ReportException4File() {
		if (StringUtil.isNotNull(getLogPath())) {
			File file = new File(getLogPath());
			File[] files = file.listFiles();
			for (File logFile : files) {
				String fName = logFile.getAbsolutePath();
				String end = fName.substring(fName.lastIndexOf("\\.") + 1,
						fName.length());
				if ("log".equals(end)) {
					retort(logFile);
				}
			}
		}
	}

	/**
	 * 异步提交异常信息
	 * 
	 * @param logFile
	 */
	private void retort(File logFile) {
		final File file = logFile;

		new AsyncTask<Integer, Integer, Integer>() {

			@Override
			protected Integer doInBackground(Integer... params) {
				// 上传异常信息
				FileInputStream in = null;
				Properties prop = null;
				ExceptionFeedBackInfo exFBI = null;
				try {
					in = new FileInputStream(file);
					prop = new Properties();
					prop.load(in);
					if (in != null) {
						in.close();
					}
					exFBI = new ExceptionFeedBackInfo();
					exFBI.setAppName(prop.getProperty("Appname"));
					exFBI.setVersion(prop.getProperty("version"));
					exFBI.setErrCode(prop.getProperty("errCode"));
					exFBI.setPriority(prop.getProperty("priority"));
					exFBI.setExcepInfo(prop.getProperty("excepInfo"));

					DataResult dataResult = AuxiliaryService.getInstance(
							context).reportExceptionFeedBackInfo(exFBI, url);
					if (dataResult != null) {
						if ("0".equals(dataResult.getResultCode())) {
							file.delete();
						} else {
							Logger.error("提交文件里的异常信息失败");
						}
					} else {
						Logger.error("未能够提交从文件里读取的异常信息");
					}

				} catch (FileNotFoundException ex) {
					Logger.error("读取属性文件--->失败！- 原因：文件路径错误或者文件不存在");
					ex.printStackTrace();
				} catch (IOException e) {
					Logger.error("读取文件里的异常信息失败");
					e.printStackTrace();
				} finally {
					if (in != null)
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
				return null;
			}
		}.execute();
	}

}
