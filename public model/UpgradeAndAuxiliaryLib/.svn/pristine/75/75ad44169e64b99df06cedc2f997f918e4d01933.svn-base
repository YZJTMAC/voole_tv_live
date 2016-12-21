package com.voole.android.client.messagelibrary.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Bundle;

/**
 * http请求工具
 * @version 1.0
 * @author LEE
 * @date 2012-3-31 上午11:14:20
 * @update 2012-3-31 上午11:14:20
 */

public class HttpUtil {

	public static final String REQUEST_MEEHOD_POST = "POST";
	public static final String REQUEST_MEEHOD_GET = "GET";
	public static final int defaultConnectTimeou = 5 * 1000;
	public static final int defaultReadTimeout = 30 * 1000;
	public static final String Encoding_UTF8 = "utf-8";
	private static final String TAG = "HttpUtil";
	HttpURLConnection httpurlconnection = null;

	/**
	 * @param urlStr
	 *            请求地址
	 * @param params
	 *            请求参数字符串：param1=arg1&param2=arg2...
	 * @param connectTimeou
	 *            连接超时时间 如果设置为 <0 则默认 10秒
	 * @param readTimeout
	 *            读超时时间 如果设置为 <0 则默认 10秒
	 * @param otherParams
	 *            备用 暂时设置为null
	 * @return InputStream
	 *  httpPost请求 如果失败情况下 is流返回null
	 */
	public InputStream doPost(String urlStr, String params, int connectTimeou,
			int readTimeout, Bundle otherParams) {
		Logger.debug(TAG, "doPost : url:" + urlStr + "|params:" + params
				+ "|connectTimeou:" + connectTimeou + "|readTimeout:"
				+ readTimeout);
		if (connectTimeou < 0) {
			connectTimeou = defaultConnectTimeou;
		}
		if (readTimeout < 0) {
			readTimeout = defaultReadTimeout;
		}
		InputStream is = null;
		URL url = null;
		try {
			byte[] requestStringBytes = params.getBytes(Encoding_UTF8);
			url = new URL(urlStr);
			httpurlconnection = (HttpURLConnection) url.openConnection();
			httpurlconnection.setConnectTimeout(connectTimeou);
			httpurlconnection.setReadTimeout(readTimeout);
			httpurlconnection.setDoInput(true);
			httpurlconnection.setDoOutput(true);
			httpurlconnection.setUseCaches(false);
			httpurlconnection.setRequestMethod(REQUEST_MEEHOD_POST);

			httpurlconnection.setRequestProperty("Content-length", ""
					+ requestStringBytes.length);
			 httpurlconnection.setRequestProperty("Content-Type","application/octet-stream");
			httpurlconnection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接

			// 建立输出流，并写入数据
			OutputStream outputStream = httpurlconnection.getOutputStream();
			outputStream.write(requestStringBytes);
			outputStream.flush();
			outputStream.close();

			int code = httpurlconnection.getResponseCode();

			if (code == 200) {
				is = httpurlconnection.getInputStream();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// httpurlconnection.disconnect();
		}
		return is;
	}

	public static byte[] writeString(String string) {
		ByteArrayOutputStream bos = null;
		DataOutputStream dos = null;
		byte[] postdata = null;
		try {
			bos = new ByteArrayOutputStream();
			dos = new DataOutputStream(bos);
			dos.writeUTF(string);
			dos.flush();
			dos.close();
			postdata = bos.toByteArray();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postdata;
	}

	/**
	 * @param urlStr
	 *            请求地址
	 * @param params
	 *            请求参数字符串：param1=arg1&param2=arg2...
	 * @param connectTimeou
	 *            连接超时时间 如果设置为 <0 则默认 10秒
	 * @param readTimeout
	 *            读超时时间 如果设置为 <0 则默认 10秒
	 * @param otherParams
	 *            备用 暂时设置为null
	 * @return
	 *  httpGet请求 如果失败情况下 is流返回null
	 */
	public InputStream doGet(String urlStr, String params, int connectTimeou,
			int readTimeout, Bundle otherParams) {
		Logger.debug(TAG, "doGet : url:" + urlStr + "|params:" + params
				+ "|connectTimeou:" + connectTimeou + "|readTimeout:"
				+ readTimeout);

		if (connectTimeou < 0) {
			connectTimeou = defaultConnectTimeou;
		}
		if (readTimeout < 0) {
			readTimeout = defaultReadTimeout;
		}

		URL url = null;
		InputStream is = null;

		try {
			if (StringUtil.isNotNull(params)) {
				urlStr = urlStr + "&" + params;
			}
			url = new URL(urlStr);
			httpurlconnection = (HttpURLConnection) url.openConnection();
			httpurlconnection.setConnectTimeout(connectTimeou);
			httpurlconnection.setReadTimeout(readTimeout);
			int code = httpurlconnection.getResponseCode();

			if (code == 200) {
				is = httpurlconnection.getInputStream();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// httpurlconnection.disconnect();
		}
		return is;
	}

	public void closeInputStream(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (httpurlconnection != null) {
			httpurlconnection.disconnect();
		}

	}

}
