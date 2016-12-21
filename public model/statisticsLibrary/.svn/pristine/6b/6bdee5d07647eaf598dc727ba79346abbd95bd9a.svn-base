package com.voole.statistics.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import com.voole.statistics.config.Config;

/**
 * 基础获得网络数据包 协议http
 * @author Jacky 2012-10-30下午1:47:02
 */
public class BaseHttpInputString {
	public static String read_web(String address) throws IOException {
		StringBuffer sb = new StringBuffer();
		InputStream is = null;
		try {
			URL url;
			url = new URL(address);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			conn.setConnectTimeout(Config.connectTimeou);
			conn.setReadTimeout(Config.readTimeout);
			is = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, Charset.forName("UTF-8")));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} finally {
			if (null != is) {
				is.close();
			}
		}
		return sb.toString();
	}
}
