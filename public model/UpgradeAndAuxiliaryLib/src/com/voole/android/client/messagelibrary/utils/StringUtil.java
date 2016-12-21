package com.voole.android.client.messagelibrary.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Locale;

/**
 * String 工具
 * @version V1.0
 * @author LEE
 * @author wusong
 * @time 2013-4-17下午3:03:43
 */
public class StringUtil {

	/**
	 *  功能：检测字符串是否为空
	 * @param str String
	 * @return boolean
	 * 
	 * 
	 */
	public static boolean isNotNull(String str) {
		boolean result = false;
		if (str != null && !str.equals("") && !"null".equals(str)
				&& str.trim().length() > 0) {
			result = true;
		}
		return result;
	}

	/**
	 *功能：检测字符串是否为空
	 * @param str String
	 * @return boolean
	 */
	public static boolean isNull(String str) {
		if (str == null || "".equals(str) || " ".equals(str)
				|| "null".equals(str) || "NULL".equals(str)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 *  变大写
	 * @param str
	 * @return String
	 */
	public static String strToUpper(String str) {
		String result = null;
		if (str != null && !str.equals("") && !"null".equals(str)) {
			result = str.toUpperCase(Locale.ENGLISH);
		}
		return result;
	}

	/**
	 *  获得id加逗号 的id字符串.
	 * @param idsStr String
	 * @return String
	 */
	public static String getIdsStr(String idsStr) {
		if (idsStr.endsWith(",")) {
			idsStr = idsStr.substring(0, idsStr.length() - 1);
		} else if (isNull(idsStr)) {
			idsStr = "0";
		}
		return idsStr;
	}

	/**
	 *  把数字变为时间显示 
	 * @param time long
	 * @return String
	 */
	public static String changeDigitalToDateStr(long time) {
		int hour = 60 * 60 * 1000;
		int min = 60 * 1000;
		int sec = 1000;
		String hourStr = "";
		String minStr = "";
		String secStr = "";
		String resultStr = "";
		if (time / hour >= 1) {
			hourStr = String.valueOf(time / hour) + "小时";
			time = time % hour;
			if (time / min > 1) {
				minStr = String.valueOf(time / min) + "分";
				time = time % min;
				secStr = time / sec + "秒";
			} else {
				minStr = "0分";
				secStr = String.valueOf(time / sec) + "秒";
			}
		} else if (time / min >= 1) {
			minStr = String.valueOf(time / min) + "分";
			time = time % min;
			secStr = time / sec + "秒";
		} else {
			secStr = String.valueOf(time / sec) + "秒";
		}
		resultStr = hourStr + minStr + secStr;
		return resultStr;
	}


	/**
	 * 字符串转换为GB2312编码
	 * @param str
	 * @return String
	 */
	public static String getEncodeGB2312AsStr(String str) {
		String resultStr = "";
		try {
			resultStr = URLEncoder.encode(str, "gb2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return resultStr;
	}
	/**
	 * 根据InputStream得到返回的结果字符串
	 * @param in 输入流
	 * @return String
	 * 
	 * 
	 */
	public static String convertStreamToString(InputStream in) {

		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		int n;
		if (in == null) {
			return null;
		} else {
			try {
				while ((n = in.read(b)) != -1) {
					out.append(new String(b, 0, n));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return out.toString();
		}
	}

	/**
	 *根据Url得到返回的结果字符串
	 * @param url
	 * @return String
	 */
	public static String getStringByUrl(String url) {
		InputStream in = new HttpUtil().doGet(url, null, 0, 0, null);
		String str = convertStreamToString(in);
		return str;
	}

	/**
	 * 将字符串转为InputStream
	 * @param str
	 * @return InputStream
	 */
	public static InputStream convertStrToInputStream(String str) {
		ByteArrayInputStream stream = null;
		if (str != null && str.length() > 0) {
			stream = new ByteArrayInputStream(str.getBytes());
		}
		return stream;
	}
	/**
	 * 字符串转换为utf-8编码
	 * @param str
	 * @return String
	 */
	public static String getEncodeUTF8AsStr(String str) {
		String resultStr = "";
		try {
			resultStr = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return resultStr;
	}
	/**
	 * 去除换行符
	 * @param str
	 * @return String
	 */
	public static String removeNewLineSymbol(String str) {
		if (isNotNull(str)) {
			if (str.endsWith("\n")) {
				str = str.substring(0, str.length() - 1);
			}
		}
		return str;
	}

	/**
	 * 属性拷贝
	 * @param from
	 * @param to
	 */
	public static void propertyCopy(Object from, Object to) {
		Field[] fields = from.getClass().getFields();
		for (int i = 0; i < fields.length; i++) {
			String name = fields[i].getName();
			Field field;
			try {
				field = to.getClass().getField(name);
				field.set(to, fields[i].get(from));
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

		}
	}

}
