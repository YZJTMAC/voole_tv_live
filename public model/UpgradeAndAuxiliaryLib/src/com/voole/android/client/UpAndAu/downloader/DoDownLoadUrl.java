package com.voole.android.client.UpAndAu.downloader;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.voole.android.client.UpAndAu.util.StringUtil;

public class DoDownLoadUrl {
	
	/**
	 * 
	 * @param url
	 * @return null
	 * @description 返回下载地址
	 * @version 1.0
	 * @author LEE
	 * @throws Exception 
	 * @date 2014-1-10 下午12:25:24 
	 * @update 2014-1-10 下午12:25:24
	 */
	public ArrayList<String> prepareDownLoadUrl(String url) throws Exception {
		ArrayList<String> dowonLoadUrlList = new ArrayList<String>();
		dowonLoadUrlList.add(url);
		//问号前 ip地址后的请求路径 包含 "/"
		String paramsMiddle="";
		//问号后的url参数串 包含问号
		String paramsLast="";
		String params [] = getDownUrlParams(url);
		if(params != null && params.length==3) {
			paramsMiddle = params[1];
			paramsLast = params[2];
		}
		
		String bkeHost = getBkeDomain(url);
		if(bkeHost != null) {
			dowonLoadUrlList.add(bkeHost+paramsMiddle+paramsLast);
		}
		
		
		/*String bkeHostIpArray[] = getBkeHostIp(url);
		if(bkeHostIpArray != null && bkeHostIpArray.length>1) {
			for(String bkeHostIp : bkeHostIpArray) {
				dowonLoadUrlList.add(bkeHostIp+paramsMiddle+paramsLast);
			}
		}*/
		return dowonLoadUrlList;
	}
	
	
	
	/**
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 * @description 截取下载地址参数
	 * @version 1.0
	 * @author LEE
	 * @date 2014-1-10 下午12:15:18 
	 * @update 2014-1-10 下午12:15:18
	 */
	private String [] getDownUrlParams(String url) throws Exception{
		if(url == null)return null;
		String params [] = new String [3];
		//ip地址
		String hostIp="";
		//问号前 ip地址后的请求路径 包含 "/"
		String paramsMiddle="";
		//问号后的url参数串 包含问号
		String paramsLast="";
		int indexWenHao = url.indexOf("?");
		hostIp = url.substring(0, indexWenHao);
		paramsLast = url.substring(indexWenHao);
		int lastSplite = hostIp.lastIndexOf("/");
		paramsMiddle = hostIp.substring(lastSplite);
		hostIp = hostIp.substring(0, lastSplite);
		params[0] = hostIp;
		params[1] = paramsMiddle;
		params[2] = paramsLast;
		
		return params;
	}
	
	
	
	/**
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 * @description 截取下载ip地址
	 * @version 1.0
	 * @author LEE
	 * @date 2014-1-10 下午12:14:16 
	 * @update 2014-1-10 下午12:14:16
	 */
	private String[] getBkeHostIp(String url) throws Exception{
		String bkeHostIpArray[] = null;
		try {
			if(url == null)return null;
			
			String paramArray [] = url.split("&");
			
			String bkeParam=null;
			
			if(paramArray != null&&paramArray.length>0) {
				for(String param : paramArray) {
					if(param.indexOf("bke=")>-1) {
						bkeParam = param;
						break;
					}
				}
				
			}
			String arrayBke[] = null;
			if(bkeParam != null) {
				arrayBke = bkeParam.split("=");
			}
			
			if(arrayBke != null && arrayBke.length>1) {
				bkeHostIpArray = arrayBke[1].split(",");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return bkeHostIpArray;
	}
	
	/**
	 * 
	 * @param url
	 * @return null
	 * @description 截取下载ip地址
	 * @version 1.0
	 * @author LEE
	 * @date 2014-1-10 下午12:14:16 
	 * @update 2014-1-10 下午12:14:16
	 */
	public String getBkeDomain(String url){
		String bkeDomain = null;
		try {
			if(url == null)return null;
			
			String paramArray [] = url.split("&");
			
			String bkeParam=null;
			
			if(paramArray != null&&paramArray.length>0) {
				for(String param : paramArray) {
					if(param.indexOf("bke=")>-1) {
						bkeParam = param;
						break;
					}
				}
				
			}
			String arrayBke[] = null;
			if(bkeParam != null) {
				arrayBke = bkeParam.split("=");
			}
			if(arrayBke != null && arrayBke.length>1) {
				bkeDomain = arrayBke[1];
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return bkeDomain;
	}
	
	private String getHostIP(String url){
		String address=null;
        try {
        	String domain=getBkeDomain(url);
        	
        	if(StringUtil.isNotNull(domain)) {
        		InetAddress myServer = InetAddress.getByName(domain);
            	address = myServer.getHostAddress();
        	}
        	
        } catch (UnknownHostException e) {
        	e.printStackTrace();
        }
        return address;
    }

}
