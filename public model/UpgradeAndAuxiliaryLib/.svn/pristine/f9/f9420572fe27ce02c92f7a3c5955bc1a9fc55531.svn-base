package com.voole.android.client.UpAndAu.downloader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.voole.android.client.UpAndAu.constants.AuxiliaryConstants;
import com.voole.android.client.UpAndAu.model.LocalProxyInfo;
import com.voole.android.client.UpAndAu.model.parser.VooleData;
import com.voole.android.client.UpAndAu.util.Logger;
import com.voole.android.client.UpAndAu.util.StringUtil;

/**
 * 
 * @description 
 * 不支持断点下载
 * @version 1.0
 * @author LEE
 * @date 2013-12-12 上午10:42:08 
 * @update 2013-12-12 上午10:42:08
 */
public class ApkDownloader{
	// 开始位置
	private long[] nStartPos; 
	// 结束位置
	private long[] nEndPos;
	// 子线程对象
	private ApkDownloadThread[] fileSplitterFetch; 
	// 文件长度
	private int nFileLength; 
	// 是否第一次取文件
	private boolean bFirst = true; 
	// 停止标志
	private boolean bStop = false; 
	// 文件下载的临时信息
	private File tmpFile; 
	// 输出到文件的输出流
	private DataOutputStream output; 
	//线程最大个数
	private int maxThreadCount=3;
	//默认线程个数
	private int defaultThreadCount=1;
	//已经下载的文件大小
	private int downLoadFileSize=-1;
	//网络文件下载地址
	private String downloadUrl;
	//本地文件保存地址
	private String fileSaveDirStr;
	//文件名称
	private String fileName;
	//文件下载监听
	private FileDownLoaderListener upgradeListener;
	//累计已下载大小
	private int downloadingSize=0;
	//检测是否需要下载新版本 
	private boolean needDownLoad=true;
	
	private boolean downloadSuccess = false;
	//停止下载任务
	private  boolean stopTask = false;
	
	private ArrayList<String> hostList = new ArrayList<String>();
	DoDownLoadUrl doDownLoadUrl = new DoDownLoadUrl();
	private String bkeDownLoadUrl;
	private String firstDownLoadUrl;
	
	private String tag = ApkDownloader.class.getSimpleName();
	
	public ApkDownloader(String downloadUrl, String fileSaveDirStr,String fileName,FileDownLoaderListener upgradeListener) {
		this.downloadUrl = downloadUrl;
		firstDownLoadUrl = downloadUrl;
		this.fileSaveDirStr = fileSaveDirStr;
		this.fileName = fileName;
		this.upgradeListener = upgradeListener;
		Logger.debug(tag, "YP -->> downloadUrl :"+downloadUrl);
		setHostList(downloadUrl);

	}
	
	
	
	int cycleCount = 0;
	int totalCycleCount = 3;
	boolean isChangedDownUrl = false;
	public void changeDownUrl(boolean timeout) {
		
		Utility.log("timeout:"+timeout+" start change downurl current url:"+downloadUrl);
		if(downloadUrl.equals(firstDownLoadUrl) && StringUtil.isNotNull(bkeDownLoadUrl)) {
			downloadUrl = bkeDownLoadUrl;
		} else {
			downloadUrl = firstDownLoadUrl;
		}
		Utility.log("end change downurl current url:"+downloadUrl);
		//if(!timeout) {
			cycleCount++;
		//}
			
		if(cycleCount>=this.totalCycleCount) {
			responseError("timeout");
		} else {
			isChangedDownUrl = true;
			stopDownLoadTask();
		}
			
		
		
	}
	
	
	private void setHostList(String downloadUrl) {
		try {
			hostList = doDownLoadUrl.prepareDownLoadUrl(downloadUrl);
			if(hostList.size()>1) {
				bkeDownLoadUrl = hostList.get(1);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void prepare() {
		isChangedDownUrl = false;
		/*tmpFile = new File(fileSaveDirStr + File.separator+fileName + ".info");
		downLoadFileSize = getDownLoadedFileSize(fileSaveDirStr + File.separator+fileName);
		
		if (tmpFile.exists()) {
			bFirst = false;
			readPos();
		} 
		if(tmpFile.exists()) {
			downloadingSize = downLoadFileSize;
		}*/
		
		if(bFirst){
			if(defaultThreadCount>maxThreadCount) {
				defaultThreadCount=maxThreadCount;
			}
			nStartPos = new long[defaultThreadCount];
			nEndPos = new long[defaultThreadCount];
		}
		
		
	}

	
	/**
	 * 
	 * @return
	 * @description 检测是否需要下载（如果本地已经存在需要下载的文件）
	 * @version 1.0
	 * @author LEE
	 * @date 2013-12-12 上午10:22:05 
	 * @update 2013-12-12 上午10:22:05
	 */
	private boolean checkWhetherNeedDownLoad() {
		boolean need=true;
		nFileLength = getFileSizeFromNetWork();
		dealReDownLoad();
		return need;
	}
	
	/**
	 * 
	 * 
	 * @description 删除旧文件
	 * @version 1.0
	 * @author LEE
	 * @date 2013-12-16 下午2:46:47 
	 * @update 2013-12-16 下午2:46:47
	 */
	private void dealReDownLoad() {
		delFile(tmpFile);
		//删除旧文件，进入下载流程
		delFile(getFileByPath(fileSaveDirStr + File.separator+fileName));
	}
	
	
	
	
	
	/**
	 * 
	 * @param fileName
	 * @return
	 * @description something
	 * @version 1.0
	 * @author LEE
	 * @date 2013-12-16 下午2:39:14 
	 * @update 2013-12-16 下午2:39:14
	 */
	private String getLocalVersion(String fileName) {
		BufferedReader br = null;
		String version="";
		try {
			br = new BufferedReader(new FileReader(fileName));
			version = br.readLine();//一次读入一行，直到读入null为文件结束   
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return version;
	}
	
	private void writeVersionFile(String fileName,String version) {
		BufferedWriter writer = null;
		try {
			writer  = new BufferedWriter(new FileWriter(fileName));
			writer.write(version);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @return
	 * @description 检测被现在的网络文件是否合法
	 * @version 1.0
	 * @author LEE
	 * @date 2013-12-12 上午10:17:45 
	 * @update 2013-12-12 上午10:17:45
	 */
	private boolean checkNetworkFileLegality(){
		boolean isLegal=true;
		if (nFileLength == -1) {
			//callback error
			//cancelTimerTask();
			//upgradeListener.onFileDownLoadError("未知文件长度!");
			isLegal = false;
		} else if (nFileLength == -2) {
			//callback error
			//cancelTimerTask();
			//upgradeListener.onFileDownLoadError("请求下载地址存在问题!");
			isLegal = false;
		} 
		//this.changeDownUrl(false);
		return isLegal;
	}

	/**
	 * 
	 * 
	 * @description 开始进入下载
	 * @version 1.0
	 * @author LEE
	 * @date 2013-12-12 上午9:56:23 
	 * @update 2013-12-12 上午9:56:23
	 */
	public void startDownload() {
		prepare();
		long startTime=System.currentTimeMillis();
		try {
			needDownLoad = checkWhetherNeedDownLoad();
			if(!needDownLoad) {
				return;
			}
			if(!checkNetworkFileLegality()) {
				return;
			}
			
			
			if (bFirst) {
				for (int i = 0; i < nStartPos.length; i++) {
					nStartPos[i] = (long) (i * (nFileLength / nStartPos.length));
				}
				for (int i = 0; i < nEndPos.length - 1; i++) {
					nEndPos[i] = nStartPos[i + 1];
				}
				nEndPos[nEndPos.length - 1] = nFileLength;
			}
			
			upgradeListener.onFileDownLoadBegin(nFileLength);
			// 启动子线程
			fileSplitterFetch = new ApkDownloadThread[nStartPos.length];
			for (int i = 0; i < nStartPos.length; i++) { 
				fileSplitterFetch[i] = new ApkDownloadThread(downloadUrl, fileSaveDirStr + File.separator+fileName,nStartPos[i], nEndPos[i], i,this);
				Utility.log("YP -->>  Thread " + i + " , nStartPos = " + nStartPos[i]+ ", nEndPos = " + nEndPos[i]);
				fileSplitterFetch[i].start();
			}
			// 等待子线程结束
			// 是否结束 while 循环
			boolean breakWhile = false;
			int sleepTime=500;
			while (!bStop) {
				//writePos();
				Utility.sleep(sleepTime);
				breakWhile = true;
				for (int i = 0; i < nStartPos.length; i++) {
					if (!fileSplitterFetch[i].bDownOver) {
						breakWhile = false;
						break;
					}
				}
				if (breakWhile) {
					break;
				}
			}
			if(isChangedDownUrl || stopTask) {
				return;
			}
			long endTime=System.currentTimeMillis();
			long costTime=(endTime-startTime)/1000;
			Utility.log("YP -->> download over casttime:"+costTime+"s");

			if(getDownLoadedFileSize(fileSaveDirStr + File.separator+fileName)==nFileLength) {
				upgradeListener.onFileDownLoadEnd();
				downloadSuccess = true;
				cancelTimerTask();
				delFile(tmpFile);
			} else {
				responseError("YP -->> download over but file error!");
				delFile(tmpFile);
				delFile(getFileByPath(fileSaveDirStr + File.separator+fileName));
			}

			
			
		} catch (Exception e) {
			responseError(getExceptionInfo(e));
			e.printStackTrace();
		}
	} 
	
	/**
	 * 获取文件大小
	 * @return int
	 */
	public int getFileSize() {
		return nFileLength;
	}
	
	/**
	 * 
	 * @return
	 * @description 从网络获取文件总长度
	 * @version 1.0
	 * @author LEE
	 * @date 2013-12-11 下午12:51:07 
	 * @update 2013-12-11 下午12:51:07
	 */
	private int getFileSizeFromNetWork() {
		int nFileLength = -1;
		HttpURLConnection httpConnection=null;
		try {
			if(downloadUrl.indexOf("http") <0) {
				downloadUrl = "http://"+downloadUrl;
			}
			URL url = new URL(downloadUrl);
			httpConnection = (HttpURLConnection) url
					.openConnection();
			httpConnection.setRequestProperty("User-Agent", "NetFox");
			httpConnection.setConnectTimeout(Utility.connectTimeout);
			httpConnection.setReadTimeout(Utility.readTimeout);
			int responseCode = httpConnection.getResponseCode();
			if (responseCode >= 400) {
				this.changeDownUrl(false);
				processErrorCode(responseCode);
				return -2; // -2 represent access is error
			}
			String sHeader;
			for (int i = 1;; i++) {
				sHeader = httpConnection.getHeaderFieldKey(i);
				if (sHeader != null) {
					if (sHeader.equals("Content-Length")) {
						nFileLength = Integer.parseInt(httpConnection
								.getHeaderField(sHeader));
						break;
					}
				} else {
					break;
				}
			}
			disConnect(httpConnection);
		} catch (Exception e) {
			disConnect(httpConnection);
			responseError(getExceptionInfo(e));
			e.printStackTrace();
		}
		Utility.log(nFileLength);
		return nFileLength;
	}
	
	private void disConnect(HttpURLConnection httpConnection) {
		try {
			if(httpConnection != null) {
				httpConnection.disconnect();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	
	private  static final String lock="Lock";
	/**
	 * 
	 * @param size
	 * @param threadName
	 * @description 累计已下载大小
	 * @version 1.0
	 * @author LEE
	 * @date 2013-12-11 下午1:53:06 
	 * @update 2013-12-11 下午1:53:06
	 */
	protected  void append(int size,String threadName) {
		synchronized(lock) {
			downloadingSize += size;
			//Utility.log("thread:"+threadName+"--downloadSize:"+downloadSize);
			if(downloadingSize>nFileLength) {
				downloadingSize = nFileLength;
			}
			upgradeListener.onFileDownLoadProgress(downloadingSize);
		}
		
	}

	private void processErrorCode(int nErrorCode) {
		System.err.println("Error Code : " + nErrorCode);
	}

	/**
	 * 
	 * 
	 * @description 停止下载任务
	 * @version 1.0
	 * @author LEE
	 * @date 2013-12-11 下午12:53:35 
	 * @update 2013-12-11 下午12:53:35
	 */
	protected void stopDownLoadTask() {
		j++;
		if(nStartPos != null && fileSplitterFetch != null) {
			for (int i = 0; i < nStartPos.length; i++) {
				if(fileSplitterFetch[i] != null) {
					fileSplitterFetch[i].stopSelf();
				}
			}
		}
		Utility.log("in stopDownLoadTask() nStartPos.length is " +nStartPos.length);
		bStop = true;
			
	}
	/**
	 * 
	 * @param f
	 * @description 删除文件
	 * @version 1.0
	 * @author LEE
	 * @date 2013-12-11 上午10:55:36 
	 * @update 2013-12-11 上午10:55:36
	 */
	private void delFile(File f) {
		String fileName="";
		try {
			if(f.exists()) {
				fileName = f.getName();
				f.delete();
			}
			Utility.log("delete "+fileName+" file success....");
		} catch(Exception e) {
			Utility.log("delete "+fileName+" file failed....");
			e.printStackTrace();
		}
		
	}
	/**
	 * 
	 * @param f
	 * @return
	 * @description 获取文件大小
	 * @version 1.0
	 * @author LEE
	 * @date 2013-12-11 下午12:41:22 
	 * @update 2013-12-11 下午12:41:22
	 */
	private int getDownLoadedFileSize(String filePath) {
		int fileSize=0;
		try {
			File f = new File(filePath);
			if(f.exists()) {
				fileSize = (int)f.length();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return fileSize;
	}
	private File getFileByPath(String filePath) {
		File f = null;
		try {
			f = new File(filePath);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return f;
	}
	protected void responseError(String error) {
		Utility.log(">>>>>>>>>>>>>>>>>>>>responseError:"+error);
		//表明是固件升级 
		/*if(AuxiliaryConstants.isCheckProxySpeed) {
			totalCycleCount = 100;
			if(cycleCount>this.totalCycleCount) {
				upgradeListener.onFileDownLoadError(error);
				cancelTimerTask();
			}
		} else {
			if(cycleCount>this.totalCycleCount) {
				upgradeListener.onFileDownLoadError(error);
				cancelTimerTask();
			}
		}*/
		
		if(cycleCount>this.totalCycleCount) {
			upgradeListener.onFileDownLoadError(error);
			cancelTimerTask();
		} else {
			changeDownUrl(false);
		}
		
		
		
		//stopDownLoadTask();
	}
	private boolean checkNeedCallBackDownLoadError(String error) {
		boolean response=false;
		if(error.indexOf("IOException")>-1) {
			response = true;
		}
		if(error.indexOf("MalformedURLException")>-1) {
			response = true;
		}
		if(error.indexOf("UnknownHostException")>-1) {
			response = true;
		}
		if(error.indexOf("ConnectException")>-1) {
			response = true;
		}
		return response;
	}
	//定时任务检测 到 下载成功为止////////////////////////////////
	Timer timer = new Timer();
	TimerTask task;
	boolean isFirstTime=true;
	int k=0;
	int j=0;
	public void download(){
		stopTask = false;
//		if(stopTask) {
//			Utility.log("YP -->>invoke stopFileDownloader method before invoke download method,so can't start download ");
//			return;
//		}
		task = new TimerTask() {
			@Override
			public void run() {
					if(isFirstTime) {
						isFirstTime = false;
						startDownload();
					}
					synchronized(this) {
						if(bStop && needDownLoad && !downloadSuccess&&!stopTask) {
							k++;
							bStop = false;
							Utility.log("YP -->>reDownLoad..........................");
							startDownload();
						}
					}
			}
		};
		timer.schedule(task, 1, 10000);
	}
	/**
	 * 
	 * 
	 * @description 当下载完成后 取消定时任务
	 * @version 1.0
	 * @author LEE
	 * @date 2013-12-11 下午7:12:54 
	 * @update 2013-12-11 下午7:12:54
	 */
	private void cancelTimerTask() {
		if (timer !=null) {
			timer.cancel();
			timer=null;
		}
		if (task!=null) {
			task.cancel();
			task = null;
		}
		stopTask = true;
		this.stopDownLoadTask();
		
		System.out.println("restart count:"+k);
		System.out.println("stopDownLoadTask count:"+j);
		
	}
	
	private String proxyUrl="http://127.0.0.1:5656/info";
	private String proxyUrl5657="http://127.0.0.1:5657/info";
	private String proxyUrl5656="http://127.0.0.1:5656/info";
	/**
	 * 
	 * @return
	 * @description 检测代理下载速度：若 大于20Kb/s停止下载
	 * @version 1.0  test:"http://172.16.10.199:35122/proxy.xml"
	 * @author LEE
	 * @date 2013-12-13 上午11:38:00 
	 * @update 2013-12-13 上午11:38:00
	 */
	private boolean isStopDownLoadTask() {
		boolean isStopDownLoadTask = false;
		try {
			
			LocalProxyInfo localProxyInfo = VooleData.getInstance().parserLocalProxyInfo(proxyUrl);
			if(localProxyInfo == null) {
				
				if(proxyUrl.equals(proxyUrl5657)) {
					proxyUrl = proxyUrl5656;
				} else {
					proxyUrl = proxyUrl5657;
				}
			}
			Utility.log("current proxyUrl:"+proxyUrl);
			float downSpeed=0.0f;
			if(localProxyInfo != null) {
				String realtimeSpeedStr = localProxyInfo.getRealtimeSpeed();
				if(StringUtil.isNotNull(realtimeSpeedStr)) {
					downSpeed = StringUtil.getDownSpeed(realtimeSpeedStr);
				} else {
					String downSpeedStr = localProxyInfo.getDownspeed();
					if(StringUtil.isNotNull(downSpeedStr)) {
						downSpeed = StringUtil.getDownSpeed(downSpeedStr);
					}
				}
			}
			if(downSpeed>20) {
				isStopDownLoadTask = true;
				Utility.log("isStopDownLoadTask player proxy is working "+downSpeed+"kb >20kb so  stop download......");
			} else {
				Utility.log("isStopDownLoadTask player proxy is notworking "+downSpeed+"kb <20kb so  go download......");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return isStopDownLoadTask;
	}

	
	
	private String getExceptionInfo(Exception e) {
		String result="";
		try {
			Writer writer = new StringWriter();  
	        PrintWriter printWriter = new PrintWriter(writer);  
	        e.printStackTrace(printWriter);  
	        Throwable cause = e.getCause();  
	        while (cause != null) {  
	            cause.printStackTrace(printWriter);  
	            cause = cause.getCause();  
	        }  
	        printWriter.close();  
	        result = writer.toString();
	        writer.close();
		} catch(Exception ex) {
			
		}
		
        return result;
	}
	
	/**
	 * 
	 * 
	 * @description 停止下载任务
	 * @version 1.0
	 * @author LEE
	 * @date 2014-1-13 上午11:18:23 
	 * @update 2014-1-13 上午11:18:23
	 */
	public void stopFileDownLoader() {
		this.cancelTimerTask();
	}

	/**
	 * 
	 * @param isCheckProxySpeed
	 * @description 是否检测本地代理服务器速度 默认为检测
	 * @version 1.0
	 * @author LEE
	 * @date 2014-1-13 上午11:28:23 
	 * @update 2014-1-13 上午11:28:23
	 */
	public void setCheckProxySpeed(boolean isCheckProxySpeed) {
		AuxiliaryConstants.isCheckProxySpeed = isCheckProxySpeed;
	}
	//////////////////////////////////////////////////////////


	public boolean isStopTask() {
		return stopTask;
	}


	public void setStopTask(boolean stopTask) {
		this.stopTask = stopTask;
	}

	
	
}