package com.voole.android.client.UpAndAu.exceptionHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.voole.android.client.UpAndAu.auxiliary.AuxiliaryService;
import com.voole.android.client.UpAndAu.model.DataResult;
import com.voole.android.client.UpAndAu.model.ExceptionFeedBackInfo;
import com.voole.android.client.UpAndAu.util.Logger;
import com.voole.android.client.UpAndAu.util.NetUtil;
import com.voole.android.client.UpAndAu.util.StringUtil;

/**
 * 用来处理未捕获的异常信息，如果程序出现了未捕获异常默认情况下则会出现强行关闭对话框 ，实现该接口并注册为程序中的默认未捕获异常处理
 * ，这样当未捕获异常发生时，就可以做些异常处理操作，例如：收集异常信息，发送错误报告 等
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.
 * 
 * @version V1.0
 * @author wusong
 * @time 2013-4-22下午3:22:36
 */
public class VooleCrashHandler implements UncaughtExceptionHandler {
	public static final String TAG = "VooleCrashHandler";
	private String finalUrl;

	// 系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	private ReportExceptionAuxiliaryer rea;

	// VooleCrashHandler实例
	private static VooleCrashHandler INSTANCE = new VooleCrashHandler();

	// 程序的Context对象
	private Context mContext;
	// 用来存储设备信息和异常信息
	private Map<String, String> infos = new HashMap<String, String>();

	// 用于格式化日期,作为日志文件名的一部分
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",
			Locale.CHINA);

	/** 保证只有一个CrashHandler实例 */
	private VooleCrashHandler() {
		super();
	}

	/**
	 * 获取CrashHandler实例 ,单例模式
	 * 
	 * @return VooleCrashHandler
	 */
	public static VooleCrashHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 * @param rea
	 */
	public void init(Context context, ReportExceptionAuxiliaryer rea) {
		mContext = context;
		this.rea = rea;
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(TAG, "error : ", e);
			}
			// 退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		final Throwable exp = ex;
		if (ex == null) {
			return false;
		}
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "应用出现问题,请稍后再试", Toast.LENGTH_LONG)
						.show();
				Looper.loop();
			}
		}.start();
		// 收集设备参数信息
		collectDeviceInfo(mContext);

		new AsyncTask<Integer, Integer, Integer>() {
			@Override
			protected Integer doInBackground(Integer... params) {
				// 保存日志文件
				saveCrashInfo2Server(exp);
				return null;
			}

		}.execute();

		Stack<Activity> activityStack = new Stack<Activity>();
		while (activityStack != null && !activityStack.empty()) {
			Activity activity = activityStack.lastElement();
			if (activity == null) {
				break;
			}
			if (activity != null) {
				activity.finish();
				activity = null;
			}
		}
		return true;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}

	/**
	 * 保存异常信息到服务器
	 * 
	 * @param ex
	 */
	protected void saveCrashInfo2Server(Throwable ex) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		ex.getMessage();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		finalUrl = rea.getUrl();
		if (!StringUtil.isNull(finalUrl)) {
			ExceptionFeedBackInfo exFBI = rea.getExFBI();
			if (ex instanceof NullPointerException) {
				exFBI.setErrCode("102");
				exFBI.setPriority("0");
			} else if (ex instanceof OutOfMemoryError) {
				exFBI.setErrCode("101");
				exFBI.setPriority("0");
			} else if (ex instanceof IllegalArgumentException) {
				exFBI.setErrCode("103");
				exFBI.setPriority("0");
			} else if (ex instanceof IOException) {
				exFBI.setErrCode("100");
				exFBI.setPriority("1");
			} else {
				exFBI.setErrCode("100");
				exFBI.setPriority("2");
			}
			exFBI.setExcepInfo(result);
			if (NetUtil.isNetworkConnected(mContext)) {
				DataResult dataResult = AuxiliaryService.getInstance(mContext)
						.reportExceptionFeedBackInfo(exFBI, finalUrl);
				if (!"0".equals(dataResult.getResultCode())) {
					saveCrashInfoXML2File(exFBI);
				}
			} else {
				saveCrashInfoXML2File(exFBI);
			}
		}
	}

	/**
	 * 没有网络或者上传至服务器失败后保存错误信息到本地
	 * 
	 * @param exFBI
	 */
	private void saveCrashInfoXML2File(ExceptionFeedBackInfo exFBI) {
		Properties prop = new Properties();
		prop.setProperty("appname", exFBI.getAppName());
		prop.setProperty("version", exFBI.getVersion());
		prop.setProperty("errCode", exFBI.getErrCode());
		prop.setProperty("priority", exFBI.getPriority());
		prop.setProperty("excepInfo", exFBI.getExcepInfo());

		long timestamp = System.currentTimeMillis();
		String time = formatter.format(new Date());
		String fileName = "crash-" + time + "-" + timestamp + ".log";
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String path = rea.getLogPath();
			if (StringUtil.isNotNull(path)) {
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(path + fileName);
					prop.store(fos, "exFile");
					fos.close();
				} catch (FileNotFoundException e) {
					Logger.error("保存异常信息时文件创建出错", e);
					e.printStackTrace();
				} catch (IOException e) {
					Logger.error("保存异常信息时流出错", e);
					e.printStackTrace();
				} finally {
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				Logger.debug("保存路径为空无法保存异常信息");
			}

		}
	}
}
