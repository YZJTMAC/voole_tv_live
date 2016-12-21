package com.voole.android.client.UpAndAu.exceptionHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.voole.android.client.UpAndAu.util.StringUtil;

import android.os.Environment;

/**
 * 保存日志信息到本地文件
 * 
 * @version V1.0
 * @author wusong
 * @time 2013-4-22下午6:26:38
 */
public class LogHandler {
	private String logPath;
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",
			Locale.CHINA);

	public LogHandler(String logPath) {
		this.logPath = logPath;
	}

	public void clearLog() {
		try {
			Runtime.getRuntime().exec("logcat -c");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeLogToFile() {
		Process process;
		try {
			// process = Runtime.getRuntime().exec("logcat -d -v time *:E");
			// process =
			// Runtime.getRuntime().exec("logcat -d -s HttpEngine:E CustomerService:E");
			process = Runtime.getRuntime().exec("logcat -d -b main *:E");
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			StringBuilder log = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				log.append(line);
				log.append("\n");
			}

			if (log.length() == 0)
				return;

			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = "log-" + time + "-" + timestamp + ".txt";
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				if (StringUtil.isNull(logPath)) {
					String sdDir = Environment.getExternalStorageDirectory()
							.toString();
					logPath = sdDir + "/voole/log/";
				}
				File dir = new File(logPath);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(logPath + fileName);
				fos.write(log.toString().getBytes());
				fos.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
