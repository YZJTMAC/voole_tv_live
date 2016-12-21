package com.voole.statistics.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.voole.statistics.config.Config;


/**
 * 基础获得网络数据包
 *   协议http
 * @author Jacky
 * 2012-10-30下午1:47:02
 */
public class BaseHttpInputStream {
	
	/**
	 * 获得此对象实例
	 * @return BaseHttpInputStreamBaseHttpInputStream
	 */
	public static BaseHttpInputStream getInstance(){
		return new BaseHttpInputStream();
	}
	private BaseHttpInputStream (){};
	
	
	/**
	 * 获得网络访问流
	 * @param urlPath
	 * @param data
	 * @return
	 * @throws IOException  寻找服务器超时会抛出异常
	 */
	
	
	public InputStream getInputStream(String urlPath,String data) throws IOException{
		 return getInputStreamNoKeep(urlPath, data);
	}
	
	
	/*public InputStream getInputStream(String urlPath,String data) throws IOException{
		 URL url = new URL(urlPath);
		 HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();   
		 httpurlconnection.setConnectTimeout(Config.connectTimeou);
	     httpurlconnection.setReadTimeout(Config.readTimeout);
	     if(null!=data){
	    	 //设置utf-8的发送模式
	    	 byte[] requestStringBytes = data.getBytes("UTF-8");
	    	 httpurlconnection.setRequestProperty("Content-length", ""
						+ requestStringBytes.length);
				 httpurlconnection.setRequestProperty("Content-Type","application/octet-stream");
				httpurlconnection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
	    	 httpurlconnection.setDoInput(true);   
	         httpurlconnection.setDoOutput(true);   
	         httpurlconnection.setRequestMethod("POST");
	     	 httpurlconnection.getOutputStream().write(requestStringBytes);
	         httpurlconnection.getOutputStream().flush();   
	         httpurlconnection.getOutputStream().close();  
	     }
         int code = httpurlconnection.getResponseCode();  
         if (code == 200) { 
        	return httpurlconnection.getInputStream();
         }
	     return null;
	}
	*/
	/**
	 * 去掉长连接的保持流
	 * @param urlPath
	 * @param data
	 * @return
	 * @throws IOException
	 */
	public InputStream getInputStreamNoKeep(String urlPath,String data) throws IOException{
		 URL url = new URL(urlPath);
		 HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();   
		 httpurlconnection.setConnectTimeout(Config.connectTimeou);
	     httpurlconnection.setReadTimeout(Config.readTimeout);
	     if(null!=data){
	    	 byte[] requestStringBytes = data.getBytes("UTF-8");
				httpurlconnection.setRequestProperty("Accept-Encoding", "gzip, deflate");//GZIP加入
				 httpurlconnection.setRequestProperty("Content-length", ""
							+ requestStringBytes.length);
					 httpurlconnection.setRequestProperty("Content-Type","application/octet-stream");
	    	 //设置utf-8的发送模式
	    	 httpurlconnection.setDoInput(true);   
	         httpurlconnection.setDoOutput(true);   
	         httpurlconnection.setRequestMethod("POST");
	     	 httpurlconnection.getOutputStream().write(requestStringBytes);
	         httpurlconnection.getOutputStream().flush();   
	         httpurlconnection.getOutputStream().close();  
	     }
        int code = httpurlconnection.getResponseCode();  
        if (code == 200) { 
       	return httpurlconnection.getInputStream();
        }
	     return null;
	}
	
}
