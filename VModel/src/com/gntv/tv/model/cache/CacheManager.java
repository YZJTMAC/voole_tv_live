package com.gntv.tv.model.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.URL;
import java.net.URLConnection;

import android.text.TextUtils;

import com.gntv.tv.common.utils.DateUtil;
import com.gntv.tv.common.utils.LocalManager;
import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.model.channel.ChannelProgramInfo;
import com.gntv.tv.model.channel.TodayProgramInfo;
import com.gntv.tv.model.time.TimeManager;
/**
 * 缓存各种节目单等文件，保存在本地
 * @author TMAC-J
 *
 */
public class CacheManager {
	private final static String TODAY_PROGRAM_FILE_NAME = "today";
	private final static String TODAY_PROGRAM_DATE = "programDate";
	private final static String TIME_PATTERN = "yyyy-MM-dd";
	private final static String MOST_VIEWED_FILE_NAME = "most";
	
	private static CacheManager instance = new CacheManager();

	private CacheManager() {
	}

	public static CacheManager GetInstance() {
		return instance;
	}
	
	private String mRootPath = "sdcard/voole/live";
	public void setRootPath(String path){
		mRootPath = path;
	}
	
	public void clear(){
		LogUtil.d("CacheManager--->clear-->");
		File f = new File(mRootPath);
		recursionDeleteFile(f);
	}
	
	public void clearToday(){
		LogUtil.d("CacheManager--->clearToday-->");
		File f = new File(mRootPath + "/" + TODAY_PROGRAM_FILE_NAME);
		recursionDeleteFile(f);
	}
	
	public boolean saveTodayProgramInfo(String url){
		if(saveToLocal(url, mRootPath, TODAY_PROGRAM_FILE_NAME)){
			long date = TimeManager.GetInstance().getCurrentTime();
			LocalManager.GetInstance().saveLocal(TODAY_PROGRAM_DATE, date + "");
			return true;
		}
		return false;
	}
	
	public boolean saveMostViewedInfo(String url){
		return saveToLocal(url, mRootPath, MOST_VIEWED_FILE_NAME);
	}
	
	public InputStream getTodayProgramInfo(){
		return getFileFromLocal(mRootPath + "/" + TODAY_PROGRAM_FILE_NAME);
	}
	
	public InputStream getMostViewedInfo(){
		return getFileFromLocal(mRootPath + "/" + MOST_VIEWED_FILE_NAME);
	}
	
	public boolean checkTodayProgramInfoExists(){
		if(checkExists(mRootPath + "/" + TODAY_PROGRAM_FILE_NAME)){
			String time = LocalManager.GetInstance().getLocal(TODAY_PROGRAM_DATE, "");
			LogUtil.d("checkTodayProgramInfoExists save--->time-->" + time);
			if(TextUtils.isEmpty(time)){
				LogUtil.d("checkTodayProgramInfoExists save--->time-->null");
				return false;
			}else{
				try {
					long t = Long.parseLong(time);
					String st = DateUtil.msec2String(t, TIME_PATTERN);
					LogUtil.d("checkTodayProgramInfoExists save--->time" + st);
					long ct = TimeManager.GetInstance().getCurrentTime();
					String sct = DateUtil.msec2String(ct, TIME_PATTERN);
					LogUtil.d("checkTodayProgramInfoExists current--->time" + sct);
					if(sct.equals(st)){
						return true;
					}else{
						return false;
					}
				} catch (Exception e) {
					LogUtil.d("checkTodayProgramInfoExists Exception--->timen" + time);
					return false;
				}
			}
		}else{
			return false;
		}
	}
	
	public boolean checkMostViewedInfoExists(){
		if(checkExists(mRootPath + "/" + MOST_VIEWED_FILE_NAME)){
			String time = LocalManager.GetInstance().getLocal(TODAY_PROGRAM_DATE, "");
			LogUtil.d("checkMostViewedInfoExists save--->time-->" + time);
			if(TextUtils.isEmpty(time)){
				LogUtil.d("checkMostViewedInfoExists save--->time-->null");
				return false;
			}else{
				try {
					long t = Long.parseLong(time);
					String st = DateUtil.msec2String(t, TIME_PATTERN);
					LogUtil.d("checkMostViewedInfoExists save--->time" + st);
					long ct = TimeManager.GetInstance().getCurrentTime();
					String sct = DateUtil.msec2String(ct, TIME_PATTERN);
					LogUtil.d("checkMostViewedInfoExists current--->time" + sct);
					if(sct.equals(st)){
						return true;
					}else{
						return false;
					}
				} catch (Exception e) {
					LogUtil.d("checkMostViewedInfoExists Exception--->timen" + time);
					return false;
				}
			}
		}else{
			return false;
		}
	}
	
	public boolean saveChannelProgramInfo(String channelID, String url){
		return saveToLocal(url, mRootPath, channelID);
	}
	
	public InputStream getChannelProgramInfo(String channelID){
		return getFileFromLocal(mRootPath + "/" + channelID);
	}
	
	public boolean checkChannelProgramInfoExists(String channelID){
		if(checkExists(mRootPath + "/" + channelID)){
			String time = LocalManager.GetInstance().getLocal(TODAY_PROGRAM_DATE, "");
			LogUtil.d("checkChannelProgramInfoExists save--->time-->" + time);
			if(TextUtils.isEmpty(time)){
				LogUtil.d("checkChannelProgramInfoExists save--->time-->null");
				return false;
			}else{
				try {
					long t = Long.parseLong(time);
					String st = DateUtil.msec2String(t, TIME_PATTERN);
					LogUtil.d("checkChannelProgramInfoExists save--->time" + st);
					long ct = TimeManager.GetInstance().getCurrentTime();
					String sct = DateUtil.msec2String(ct, TIME_PATTERN);
					LogUtil.d("checkChannelProgramInfoExists current--->time" + sct);
					if(sct.equals(st)){
						return true;
					}else{
						return false;
					}
				} catch (Exception e) {
					LogUtil.d("checkTodayProgramInfoExists Exception--->time" + time);
					return false;
				}
			}
		}else{
			return false;
		}
	}
	
	/**
	 * 反向序化读取节目单
	 * @param channelID
	 * @return
	 */
	public ChannelProgramInfo getChannelProgramInfoObject(String channelID){
		FileInputStream fiStream = null;
		ObjectInputStream oiStream = null;
		ChannelProgramInfo channelProgramInfo = null;
		File file = new File(mRootPath + "/" + channelID);
		if (file.exists()) {
			try {
				fiStream = new FileInputStream(file);
				oiStream = new ObjectInputStream(fiStream);
				channelProgramInfo = (ChannelProgramInfo) oiStream.readObject();

			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (fiStream != null)
					try {
						fiStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				if (oiStream != null) {
					try {
						oiStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return channelProgramInfo;
	}
	
	/**
	 * 反序列化读取片单
	 * @return
	 */
	public TodayProgramInfo getTodayProgramInfoObject(){
		FileInputStream fiStream = null;
		ObjectInputStream oiStream = null;
		TodayProgramInfo todayProgramInfo = null;
		File file = new File(mRootPath + "/" + TODAY_PROGRAM_FILE_NAME);
		if (file.exists()) {
			try {
				fiStream = new FileInputStream(file);
				oiStream = new ObjectInputStream(fiStream);
				todayProgramInfo = (TodayProgramInfo) oiStream.readObject();

			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (fiStream != null)
					try {
						fiStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				if (oiStream != null) {
					try {
						oiStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return todayProgramInfo;
	}
	
	/**
	 * 序列化存入片单
	 * @param url
	 * @return
	 */
	public boolean saveTodayProgramInfo(TodayProgramInfo todayProgramInfo){
		if(saveToChannel(todayProgramInfo, mRootPath, TODAY_PROGRAM_FILE_NAME)){
			long date = TimeManager.GetInstance().getCurrentTime();
			LocalManager.GetInstance().saveLocal(TODAY_PROGRAM_DATE, date + "");
			return true;
		}
		return false;
		
	}
	
	/**
	 * 序列化存入节目单
	 * @param url
	 * @return
	 */
	public boolean saveChannelProgramInfo(ChannelProgramInfo channelProgramInfo,String channelId){
		boolean result = false;
		LogUtil.d("XMLHelper--->saveChannelProgramInfo-->filePath-->" + mRootPath + "fileName-->" + channelId);
		if(channelProgramInfo!=null){
			File destDir = new File(mRootPath);
			if(!destDir.exists()) {
			    destDir.mkdirs();
			}
			File file = new File(mRootPath , channelId);
			if (file.exists()) {
				file.delete();
			}
			FileOutputStream fos = null;
	        ObjectOutputStream ooS = null;
	        try {
	            fos = new FileOutputStream(file);
	            ooS = new ObjectOutputStream(fos);
	            ooS.writeObject(channelProgramInfo);
	            result = true;
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            if (file.exists()) {
					file.delete();
				}
	        } finally {
	            if (fos != null){
	            	 try {
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	if (ooS != null) {
	 	                try {
							ooS.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	 	            }
	            }
	        }
		}
		return result;
	}
	
	private boolean saveToChannel(TodayProgramInfo todayProgramInfo, String filePath, String fileName){
		boolean result = false;
		LogUtil.d("XMLHelper--->saveToChannel-->filePath-->" + filePath + "fileName-->" + fileName);
		if(todayProgramInfo!=null){
			File destDir = new File(filePath);
			if(!destDir.exists()) {
			    destDir.mkdirs();
			}
			File file = new File(filePath , fileName);
			if (file.exists()) {
				file.delete();
			}
			FileOutputStream fos = null;
	        ObjectOutputStream ooS = null;
	        try {
	            fos = new FileOutputStream(file);
	            ooS = new ObjectOutputStream(fos);
	            ooS.writeObject(todayProgramInfo);
	            result = true;
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            if (file.exists()) {
					file.delete();
				}
	        } finally {
	            if (fos != null){
	            	 try {
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	if (ooS != null) {
	 	                try {
							ooS.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	 	            }
	            }
	        }
		}
		return result;
	}
	
	private boolean checkExists(String filePath){
		File file = new File(filePath);
		if (file.exists()) {
			return true;
		}else{
			LogUtil.d("checkExists--->" + filePath + "-->notExist");
			return false;
		}
	}
	
	private InputStream getFileFromLocal(String filePath){
		File file = new File(filePath);
		if (file.exists()) {
			try {
				return new FileInputStream(file);
			} catch (Exception e) {
				return null;
			}  
		}else{
			return null;
		}
	}
	
	private boolean saveToLocal(String urlstr, String filePath, String fileName){
		LogUtil.d("XMLHelper--->saveToLocal-->filePath-->" + filePath + "fileName-->" + fileName + "-->url-->" + urlstr);
		File destDir = new File(filePath);
		if(!destDir.exists()) {
		    destDir.mkdirs();
		}
		URL url;
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		File file = new File(filePath , fileName);
		if (file.exists()) {
			file.delete();
		}
		try {
			url = new URL(urlstr);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(1000 * 10);
			connection.setReadTimeout(1000 * 10);
			inputStream = connection.getInputStream();
			fileOutputStream = new FileOutputStream(file);
			byte[] buffer = new byte[1024*4];
			int count = 0;
			while ((count = inputStream.read(buffer)) > 0) {
				fileOutputStream.write(buffer, 0, count);
			}
		} catch (Exception e) {
			LogUtil.d("XMLHelper--->saveToLocal-->exception-->" + filePath + "-->" + fileName + "-->url-->" + urlstr);
			LogUtil.d("XMLHelper--->saveToLocal-->exception-->" + e.toString());
			if (file.exists()) {
				file.delete();
			}
			return false;
		} finally {
			try {
				if(fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
//		LogUtil.d("XMLHelper--->saveToLocal-->success-->" + filePath + "-->" + fileName + "-->url-->" + urlstr);
		return true;
	}
	
	private void recursionDeleteFile(File file){
        if(file.isFile()){
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
            	recursionDeleteFile(f);
            }
            file.delete();
        }
    }
}
