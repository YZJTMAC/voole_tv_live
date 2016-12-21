package com.voole.statistics.report;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.text.TextUtils;
import android.util.Log;

public class ReportMessageUtil {
	private final int CAPACITY = 1000;
	//	private ArrayBlockingQueue<ReportInfo> msgQueue = new ArrayBlockingQueue<ReportInfo>(CAPACITY,true);
	private ConcurrentLinkedQueue<ReportInfo> msgQueue = new ConcurrentLinkedQueue<ReportInfo>();
	private static ReportMessageUtil reportMessageUtil = new ReportMessageUtil();
	private boolean isThreadLiving = false;

	public static ReportMessageUtil getInstance(){
		return reportMessageUtil;
	}

	private boolean sendMessage(String urlStr,String params, StringBuffer httpMessage, int connectTimes, int connectTimeOut, int readTimeOut) throws IOException{
		HttpURLConnection httpurlconnection = null;
		for (int i = 0; i < connectTimes; i++) {
			try {
				byte[] requestStringBytes = params.getBytes("utf-8");
				URL url = new URL(urlStr);
				httpurlconnection = (HttpURLConnection) url .openConnection();
				httpurlconnection.setConnectTimeout(1000 * connectTimeOut);
				httpurlconnection.setReadTimeout(1000 * readTimeOut);
				httpurlconnection.setDoInput(true);
				httpurlconnection.setDoOutput(true);
				httpurlconnection.setUseCaches(false);
				httpurlconnection.setRequestMethod("POST");
				httpurlconnection.setRequestProperty("Content-length", "" + requestStringBytes.length);
				httpurlconnection.setRequestProperty("Content-Type", "application/octet-stream");
				httpurlconnection.setRequestProperty("Connection", "Keep-Alive");// 长连接
				// httpurlconnection.setRequestProperty("Charset", "UTF-8");

				OutputStream outputStream = httpurlconnection.getOutputStream();
				outputStream.write(requestStringBytes);
				outputStream.flush();
				outputStream.close();

				int code = httpurlconnection.getResponseCode();

				if (code == 200) {
					InputStream urlStream = httpurlconnection.getInputStream();
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream));
					String sCurrentLine = "";
					while ((sCurrentLine = bufferedReader.readLine()) != null) {
						httpMessage.append(sCurrentLine);
					}
				}
				return isReportSuccess(httpMessage.toString());
//				return true;
			} catch (Exception e) {
				Log.e("ReportMessageUtil","Connect Exception-->\n/exception msg/" + e.getMessage() + "\n/request url/" + urlStr);
				Log.i(ReportConfig.REPORTTAG,"ReportMessageUtil-----》sendMessage--get-->Connect Exception-->\n/exception msg/"  + e.getMessage() + "\n/request url/" + urlStr);
				if (i >= connectTimes - 1) {
					return false;
				}
			}finally{
				if(httpurlconnection != null){
					httpurlconnection.disconnect();
					httpurlconnection = null;
				}
			}
		}
		return false;
	}

	private boolean sendMessage(String url, StringBuffer httpMessage, int connectTimes, int connectTimeOut, int readTimeOut) {
		HttpURLConnection httpConnection = null;
		for(int i = 0; i < connectTimes; i++){
			try {
				URL httpUrl = new URL(url);
				URLConnection connection = httpUrl.openConnection();
				connection.setConnectTimeout(1000 * connectTimeOut);
				connection.setReadTimeout(1000 * readTimeOut);
				httpConnection = (HttpURLConnection) connection;
				int responseCode = httpConnection.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					InputStream urlStream = httpConnection.getInputStream();
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream));
					String sCurrentLine = "";
					while ((sCurrentLine = bufferedReader.readLine()) != null) {
						httpMessage.append(sCurrentLine);
					}
					httpConnection.disconnect();
					httpConnection = null;
					return isReportSuccess(httpMessage.toString());
//					return true;
				}else {
					httpConnection.disconnect();
					httpConnection = null;
				}
			} catch (Exception e) {
				Log.i(ReportConfig.REPORTTAG,"ReportMessageUtil-----》sendMessage--post-->Connect Exception-->\n/exception msg/" + e.getMessage() + "\n/request url/" + url);
				if (httpConnection != null) {
					httpConnection.disconnect();
					httpConnection = null;
				}
				if(i >= connectTimes - 1){
					return false;
				}
			}
		}
		return false;
	}

	public void addMessage(ReportInfo msg){
		Log.i(ReportConfig.TAG,"ReportMessageUtil-----》addMessage---->" + msgQueue.size());
		Log.i(ReportConfig.REPORTTAG,"ReportMessageUtil-----》addMessage---->" + msgQueue.size());
//		synchronized (msgQueue) {
//			while(msgQueue.size() > CAPACITY){
//				Log.i(ReportConfig.REPORTTAG,"ReportMessageUtil-----》addMessage---->msgQueue.size()>1000  poll" );
//				msgQueue.poll();
//			}
			boolean result = msgQueue.add(msg);
			Log.i(ReportConfig.REPORTTAG,"ReportMessageUtil-----》addMessage---->result:" + result);
			Log.i(ReportConfig.REPORTTAG,"ReportMessageUtil-----》addMessage---->isThreadLiving:" + isThreadLiving);
			if(!isThreadLiving){
				Log.i(ReportConfig.REPORTTAG,"ReportMessageUtil-----》addMessage---->reportMessage:start");
				reportMessage();
			}	
//		}
	}

	public void reportMessage(){
		isThreadLiving = true;
		new SendMsgThread().start();
	}

	/*public void reportMessage(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String msg = null;
				try {
					while((msg=msgQueue.poll(2,TimeUnit.SECONDS))!=null){
						try {
							sendMessage(msg);
						} catch (IOException e) {
							e.printStackTrace();
							Lg.e("reportMessage--->"+e.toString());
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					Lg.e("reportMessage--->"+e.toString());
				}
			}
		}).start();
	}*/

	class SendMsgThread extends Thread{
		@Override
		public void run() {
			ReportInfo msg = null;
			while((msg=msgQueue.poll())!=null){
				Log.i(ReportConfig.REPORTTAG,"ReportMessageUtil-----》SendMsgThread---->msgQueue.poll()---->" + msgQueue.size());
				try {
					StringBuffer sb = new StringBuffer();
					Log.i(ReportConfig.REPORTTAG,"ReportMessageUtil-----》SendMsgThread---->reportMessage--->url::"+msg.toString());
					if(TextUtils.isEmpty(msg.getMsg())){
						Log.i(ReportConfig.REPORTTAG,"ReportMessageUtil-----》SendMsgThread---->sendMessage--->post");
						sendMessage(msg.getUrl(),msg.getData(),sb,2,2,5);
					}else{
						Log.i(ReportConfig.REPORTTAG,"ReportMessageUtil-----》SendMsgThread---->sendMessage--->get");
						sendMessage(msg.getMsg(),sb,2,2,5);
					}
					Log.i(ReportConfig.REPORTTAG,"ReportMessageUtil-----》SendMsgThread---->reportMessage--->resultMessage::"+sb.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			isThreadLiving = false;
		}
	}
	
	private boolean isReportSuccess(String reportResult) {
		if (null != reportResult && !"".equals(reportResult.trim())) {
			if (-1 != reportResult.indexOf("\"status\":0")) {
				// 表示成功
				Log.e(ReportConfig.TAG, "ReportManager---------sendMessage------->SUCCESS");
				return true;
			} 
		}
		Log.e(ReportConfig.TAG, "ReportManager---------sendMessage------->FAIL");
		return false;
	}
}
