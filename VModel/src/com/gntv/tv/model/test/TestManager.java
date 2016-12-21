package com.gntv.tv.model.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.gntv.tv.common.ap.Ad;
import com.gntv.tv.common.ap.AuthManager;
import com.gntv.tv.common.ap.ProxyManager;
import com.gntv.tv.common.ap.UrlList;
import com.gntv.tv.common.ap.UrlMap;
import com.gntv.tv.common.ap.User;
import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.model.cache.CacheManager;
import com.gntv.tv.model.channel.ProgramManager;
import com.gntv.tv.model.channel.ProgramManager.ProgramType;
import com.gntv.tv.model.channel.TodayProgramInfo;
import com.gntv.tv.model.error.ErrorManager;
/**
 * 用于故障检测
 * @author voole
 *
 */
public class TestManager {
	private static TestManager instance = new TestManager();

	private TestManager() {
	}

	public static TestManager GetInstance() {
		return instance;
	}
	
	public TestResult testUser(){
		TestResult testResult = new TestResult();
		User user = AuthManager.GetInstance().getUser();
		if(user!=null&&"0".equals(user.getStatus())){
			testResult.setUser(user);
			//UrlMap urlMap = AuthManager.GetInstance().getUrlMap();
			UrlList urlList = AuthManager.GetInstance().getUrlList();
			if(checkUrlList(urlList)){
				testResult.setStatus("0");
			}else{
				testResult.setStatus(ErrorManager.ERROR_LOGIN_TEST);
				/*if(urlMap!=null){
					testResult.setOtherStatus(urlMap.getStatus());
					testResult.setOtherDesc(urlMap.getResultDesc());
				}*/
				if(urlList!=null){
					testResult.setOtherStatus(urlList.getStatus());
					testResult.setOtherDesc(urlList.getResultDesc());
				}
			}
		}else{
			testResult.setStatus(ErrorManager.ERROR_LOGIN_TEST);
			if(user!=null){
				testResult.setOtherStatus(user.getStatus());
				testResult.setOtherDesc(user.getResultDesc());
			}
		}
		
		return testResult;
	}
	
/*	private boolean checkUrlMap(UrlMap urlMap){
		if(urlMap!=null&&("0".equals(urlMap.getStatus()) || urlMap.getStatus() == null || "1".equals(urlMap.getStatus()))){
			return true;
		}else{
			return false;
		}
	}*/
	
	private boolean checkUrlList(UrlList urlList){
		if(urlList!=null&&("0".equals(urlList.getStatus()) || urlList.getStatus() == null || "1".equals(urlList.getStatus()))){
			return true;
		}else{
			return false;
		}
	}
	
	public TestResult testPorgram(){
		TestResult testResult = new TestResult();
		TodayProgramInfo info = ProgramManager.GetInstance().getTodayProgramInfo(ProgramType.ALL);
		if(info!=null&&"0".equals(info.getStatus())&&info.getAssortList()!=null&&info.getAssortList().size()>0){
			testResult.setStatus("0");
		}else{
			testResult.setStatus(ErrorManager.ERROR_CHANNEL_TEST);
			if(info!=null){
				testResult.setOtherStatus(info.getStatus());
				testResult.setOtherDesc(info.getResultDesc());
			}
		}
		return testResult;
	}
	
	public TestResult testServer(final String channelCode, final String resourceId){
		TestResult testResult = new TestResult();
		String mid = "1";
		String sid = "1";
		String fid = "";
		try {
			fid = URLEncoder.encode(channelCode + "_" + resourceId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String pid = "1";
		String playtype = "1000";
		Ad ad = AuthManager.GetInstance().getPlayUrl("1", sid, fid, pid, playtype, null, null, null, null, null, null, null, null, null, null, null, null, "2.0", "3.0");
		if(ad == null || !"0".equals(ad.getStatus())){
			testResult.setStatus(ErrorManager.ERROR_PLAYURL_TEST);
			if(ad!=null){
				testResult.setOtherStatus(ad.getStatus());
				testResult.setOtherDesc(ad.getResultDesc());
			}
		}else{
			boolean result = false;
			if(ad.getPlayUrl()!=null){
				result = ProxyManager.GetInstance().checkUrl(ad.getPlayUrl());
				if(result){
					testResult.setStatus("0");
				}else{
					testResult.setStatus(ErrorManager.ERROR_PLAYURL_TEST);
				}
				
			}else{
				testResult.setStatus(ErrorManager.ERROR_PLAYURL_TEST);
			}
		}
		
		return testResult;
	}
	
	public TestResult testSpeed(){
		TestResult testResult = new TestResult();
		//String urlStr = getUrlMap()!=null?getUrlMap().get("speedtest"):null;
		String urlStr = AuthManager.GetInstance().getUrlList().getSpeedTest();
		urlStr +="&apptype=1&format=1&version=1.0&ruletype=1";
		LogUtil.i("testSpeed---->urlstr---->"+urlStr);
		SpeedParser speedParser = new SpeedParser();
		try {
			speedParser.setUrl(urlStr);
		} catch (Exception e1) {
			LogUtil.e("speedDetect---->"+e1.toString());
			testResult.setStatus(ErrorManager.GetInstance().ERROR_NETSPEED_TEST);
			testResult.setSpeed(0);
			return testResult;
		}
		SpeedModel spModel = speedParser.getSpeedModel();
		if(spModel == null || !"0".equals(spModel.getStatus())){
			testResult.setStatus(ErrorManager.ERROR_NETSPEED_TEST);
			testResult.setSpeed(0);
			if(spModel!=null){
				testResult.setOtherDesc(spModel.getResultdesc());
				testResult.setOtherStatus(spModel.getStatus());
			}
			return testResult;
		}
		List<String> urls = spModel.getHostList();
		if(urls == null || urls.size()==0){
			LogUtil.d("speedDetect---->no file urls found");
			testResult.setStatus(ErrorManager.ERROR_NETSPEED_TEST);
			testResult.setSpeed(0);
			return testResult;
		}
		int connectTimes = 3;
		if(urls.size()<connectTimes){
			connectTimes = urls.size();
		}
		URL url;
		InputStream inputStream = null;
		int fileSize = 0;
		long startTime = new Date().getTime();
		for(int i = 0; i < connectTimes; i++){
			try {
				LogUtil.d("speedDetect---->number "+i+" url is" + urls.get(i));
				url = new URL(urls.get(i));
				URLConnection connection = url.openConnection();
				connection.setConnectTimeout(1000 * 2);
				connection.setReadTimeout(1000 * 30);
				inputStream = connection.getInputStream();
				
				fileSize = connection.getContentLength();
				if (fileSize < 0) {
					testResult.setStatus(ErrorManager.ERROR_NETSPEED_TEST);
					testResult.setSpeed(0);
					return testResult;
				}
				
				
				byte[] buffer = new byte[1024*4];
				int count = 0;
				while ((count = inputStream.read(buffer)) > 0) {
					LogUtil.d("fileDown--->" + count);
				}
				LogUtil.d("fileDown--->finish");
				break;
				
			}catch (Exception e) {
				LogUtil.e("speedDetect---->"+e.toString());
				if(i >= connectTimes - 1){
					testResult.setStatus(ErrorManager.ERROR_NETSPEED_TEST);
					testResult.setSpeed(0);
					return testResult;
				}
			}finally{
				if (inputStream != null) {
					try {
						inputStream.close();
						inputStream = null;
					} catch (IOException e1) {
						LogUtil.e("speedDetect---->"+e1.toString());
					}
				}
				
			}
		}

		long endTime = new Date().getTime();
		double totalTime = (endTime - startTime)/1000.0;
		LogUtil.i("speedDetect---->totalTime---->"+totalTime);
		testResult.setStatus("0");
		testResult.setSpeed(fileSize/(totalTime*1024*8));
		return testResult; //单位为KB/s
	}
	
	public void repairUser(final RepairInterface repair){
		new Thread(new Runnable() {
			@Override
			public void run() {
				AuthManager.GetInstance().clear();
				AuthManager.GetInstance().deleteAuthFiles();
				ProxyManager.GetInstance().deleteProxyFiles();
				boolean result = AuthManager.GetInstance().startAuth();
				if(!result){
					repair.doRepair(result);
				}else{
					User user = AuthManager.GetInstance().getUser();
					if(user!=null&&"0".equals(user.getStatus())){
						//UrlMap urlMap = AuthManager.GetInstance().getUrlMap();
						UrlList urlList = AuthManager.GetInstance().getUrlList();
						if(checkUrlList(urlList)){
							repair.doRepair(true);
						}else{
							repair.doRepair(false);
						}
					}else{
						repair.doRepair(false);
					}
				}
				
				
				
			}
		}).start();
	}
	
	public void repairMovie(final RepairInterface repair){
		new Thread(new Runnable() {
			@Override
			public void run() {
				CacheManager.GetInstance().clear();
				TodayProgramInfo info = ProgramManager.GetInstance().getTodayProgramInfo(ProgramType.ALL);
				boolean result = false;
				if(info!=null&&"0".equals(info.getStatus())){
					result = true;
				}
				repair.doRepair(result);
			}
		}).start();
	}
	
	public void repairConnect(final RepairInterface repair){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				ProxyManager.GetInstance().exitProxy();
				ProxyManager.GetInstance().startProxy();
				repair.doRepair(false);
			}
		}).start();
	}
	
	public interface RepairInterface{
		void doRepair(boolean result);
	}
	
	/*private Map<String,String> getUrlMap(){
		UrlMap urlMap = AuthManager.GetInstance().getUrlMap();
		return urlMap!=null?urlMap.getUrlMap():null;
	}*/
}
