package com.voole.android.client.UpAndAu.exceptionHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.voole.android.client.UpAndAu.auxiliary.AuxiliaryService;
import com.voole.android.client.UpAndAu.model.DataResult;
import com.voole.android.client.UpAndAu.model.ExceptionFeedBackInfo;
import com.voole.android.client.UpAndAu.util.AppInfoUtil;
import com.voole.android.client.UpAndAu.util.Logger;
import com.voole.android.client.UpAndAu.util.StringUtil;

import android.content.Context;
import android.os.AsyncTask;

/**
 * 辅助提交日志文件,到网络服务器
 * @version V1.0
 * @author wusong
 * @time 2013-4-23上午11:05:48
 */
public class ReportLogAuxiliaryer {
	private static Context context;

	/**
	 * 提交本地文件中保存的log信息 到服务器
	 * 
	 * @param ctx
	 *            Context
	 * @param url
	 *            要提交异常信息的地址,包含各请求参数的
	 * @param logPath
	 *            保存了log信息的地址
	 */
	public static void ReportLog4File(Context ctx, String url, String logPath) {
		context = ctx;
		if (StringUtil.isNotNull(logPath) && StringUtil.isNotNull(url)) {
			File path = new File(logPath);
			String appName = AppInfoUtil.getAppName(context);
			String version = AppInfoUtil.getAppVersionName(context);
			ExceptionFeedBackInfo eFBI = new ExceptionFeedBackInfo(version,
					appName, "2", "100", "");
			File[] files = path.listFiles();

			for (File file : files) {
				String fName = file.getAbsolutePath();
				String end = fName.substring(fName.lastIndexOf("\\.") + 1,
						fName.length());
				if ("log".equals(end)) {
					report(file, eFBI, url);
				}
			}

		}

	}

	/**
	 * 异步提交从文件里读出的日志信息
	 * 
	 * @param file
	 * @param eFBI
	 * @param url
	 */
	private static void report(final File file,
			final ExceptionFeedBackInfo eFBI, final String url) {
		new AsyncTask<Integer, Integer, Integer>() {

			@Override
			protected Integer doInBackground(Integer... params) {
				StringBuffer sb = new StringBuffer();
				try {
					FileReader fr = new FileReader(file);
					BufferedReader br = new BufferedReader(fr);
					String inLine = null;
					while ((inLine = br.readLine()) != null) {
						sb.append(inLine);
					}
					br.close();
					fr.close();
					String logInfo = sb.toString();
					eFBI.setExcepInfo(logInfo);
					DataResult dataResult = AuxiliaryService.getInstance(
							context).reportExceptionFeedBackInfo(eFBI, url);
					if (dataResult != null) {
						if ("0".equals(dataResult.getResultCode())) {
							file.delete();
						}
					}
				} catch (FileNotFoundException e) {
					Logger.error("提交log日志信息时打开日志文件失败");
					e.printStackTrace();
				} catch (IOException e) {
					Logger.error("提交log日志信息时读取文件内容失败");
					e.printStackTrace();
				}

				return null;
			}
		}.execute();
	}

	/**
	 * 设置保存log信息到本地
	 * 
	 * @param logPath
	 *            保存log信息的路径 如果传入的值为空,则保存在sd卡下voole/log下
	 */
	public static void activateLogToFile(String logPath) {
		LogHandler logHandler = new LogHandler(logPath);
		logHandler.writeLogToFile();
	}
}
