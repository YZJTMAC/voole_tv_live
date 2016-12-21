package com.voole.android.client.UpAndAu.downloader;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {
	public static final int connectTimeout=10000;
	public static final int readTimeout=10000;
	public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	public Utility() {
	}

	public static void sleep(int nSecond) {
		try {
			Thread.sleep(nSecond);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void log(String sMsg) {
		System.out.println("YP -->>--"+sMsg);
	}

	public static void log(int sMsg) {
		System.out.println(sMsg);
	}
}