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

import android.os.Environment;
import android.os.StatFs;

import com.voole.android.client.UpAndAu.constants.AuxiliaryConstants;
import com.voole.android.client.UpAndAu.constants.ErrorConstants;
import com.voole.android.client.UpAndAu.constants.VersionConstants;
import com.voole.android.client.UpAndAu.model.LocalProxyInfo;
import com.voole.android.client.UpAndAu.model.UpdateInfo;
import com.voole.android.client.UpAndAu.model.parser.VooleData;
import com.voole.android.client.UpAndAu.util.MD5Util;
import com.voole.android.client.UpAndAu.util.StringUtil;
import com.voole.android.client.messagelibrary.model.parser.DataConstants;
import com.voole.android.client.messagelibrary.service.StatisticsUpdateResultDataService;

/**
 * 
 * @description 
 * 
 * 1.下载前先检测下载地址是否正确
 * 2. 检测文件长度是否正常
 * 3. 检测本地是否已经有需要下载的文件，并与服务端文件做 md5校验，检测是否需要重新下载
 * 4. 定时器任务开始启动
 * 5. 开始下载
 * 6. 下载完毕后删除 临时文件
 * 7. 下载未成功会有临时文件记录下载情况，定时任务会进行重新下载 ，一直到下载成功为止
 * 8. 检测代理：下载增加检测http代理下载功能：
 * 		如代理下载播放中则停止下载；
 * 		定时检测如代理退出或下载速度小于20k，则开始下载。
 * @version 1.0
 * @author LEE
 * @date 2013-12-12 上午10:42:08 
 * @update 2013-12-12 上午10:42:08
 */
public class FileDownloader{
	// 开始位置
	private long[] nStartPos; 
	// 结束位置
	private long[] nEndPos;
	// 子线程对象
	private DownloadThread[] fileSplitterFetch; 
	// 文件长度
	private int nFileLength; 
	// 是否第一次取文件
	private boolean bFirst = true; 
	// 停止标志
	private boolean bStop = false; 
	//标志本地是否有足够空间去容纳将要下载的文件
	private boolean isLocalSpaceEnough = true;
	// 文件下载的临时信息
	private File tmpFile; 
	// 输出到文件的输出流
	private DataOutputStream output; 
	//线程最大个数
	private int maxThreadCount=1;
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
	//md5文件校验
	private String fid="";
	private String currentVersion;
	//检测是否需要下载新版本 
	private boolean needDownLoad=true;
	//每次开始下载的时间。
	private long startDownLoadTime;
	
	private ArrayList<String> hostList = new ArrayList<String>();
	DoDownLoadUrl doDownLoadUrl = new DoDownLoadUrl();
	private String bkeDownLoadUrl;
	private String firstDownLoadUrl;
	//通过版本检测地址重新获取的新的下载地址
	private String newDownLoadUrl;
	//通过版本检测地址获取下载地址总共不能超过三次
	private final int totalReGetDownLoadUrlThroughCheckVersionUrlCount=2;
	//当前通过版本检测地址获取下载地址次数
	private int currentReGetDownLoadUrlThroughCheckVersionUrlCount=0;
	//停止下载任务
	private boolean stopTask = false;

	
	public FileDownloader(String downloadUrl, String fileSaveDirStr,String fileName,String fid,String currentVersion,FileDownLoaderListener upgradeListener) {
		this.downloadUrl = downloadUrl;
		firstDownLoadUrl = downloadUrl;
		this.fileSaveDirStr = fileSaveDirStr;
		this.fileName = fileName;
		this.fid = fid;
		this.currentVersion = currentVersion;
		this.upgradeListener = upgradeListener;
		AuxiliaryConstants.apkCurrentVersion = currentVersion;
		setHostList(downloadUrl);
		Utility.log("YP -->>  init download url:"+firstDownLoadUrl);
		Utility.log("YP -->>  fid:"+fid);
		Utility.log("YP -->>  fileSaveDirStr:"+fileSaveDirStr);
		Utility.log("YP -->>  fileName:"+fileName);
		Utility.log("YP -->>  currentVersion:"+currentVersion);

	}
	public FileDownloader(String downloadUrl, String fileSaveDirStr,String fileName, int threadNum,String fid,String currentVersion,FileDownLoaderListener upgradeListener) {
		this.downloadUrl = downloadUrl;
		firstDownLoadUrl = downloadUrl;
		this.fileSaveDirStr = fileSaveDirStr;
		this.fileName = fileName;
		this.defaultThreadCount = threadNum;
		this.fid = fid;
		this.currentVersion = currentVersion;
		this.upgradeListener = upgradeListener;
		AuxiliaryConstants.apkCurrentVersion = currentVersion;
		setHostList(downloadUrl);
		Utility.log("YP -->>  init download url:"+firstDownLoadUrl);
		Utility.log("YP -->>  fid:"+fid);
		Utility.log("YP -->>  fileSaveDirStr:"+fileSaveDirStr);
		Utility.log("YP -->>  fileName:"+fileName);
		Utility.log("YP -->>  currentVersion:"+currentVersion);
	}
	
	
	
	int cycleCount = 0;
	final int totalCycleCount = 6;
	
	boolean isChangedDownUrl = false;
	private void changeDownUrl(boolean timeout) {
		Utility.log("YP -->> changeDownUrl");
		Utility.log("YP -->> timeout:"+timeout+" start change downurl current url:"+downloadUrl);
		if(downloadUrl.equals(firstDownLoadUrl) && StringUtil.isNotNull(bkeDownLoadUrl)) {
			downloadUrl = bkeDownLoadUrl;
		} else {
			downloadUrl = firstDownLoadUrl;
		}
		Utility.log("YP -->> end change downurl current url:"+downloadUrl);
		
		if(!timeout) {
			Utility.log("YP -->> timeout is false and add cycleCount");
			cycleCount++;
			Utility.log("YP -->> timeout is false and add cycleCount is "+cycleCount);
		} else {
			StatisticsUpdateResultDataService.getInstance().transferMessage(DataConstants.DOWNLOADFAILED_ROOT, "SocketTimeoutException", downloadingSize);
		}
		isChangedDownUrl = true;
		stopDownLoadTask("changeDownUrl method");
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
		Utility.log("YP -->> setHostList method bkeDownLoadUrl :"+bkeDownLoadUrl);
		
	}
	
	private void prepare() {
		isChangedDownUrl = false;
		tmpFile = new File(fileSaveDirStr + File.separator+fileName + ".info");
		downLoadFileSize = getDownLoadedFileSize(fileSaveDirStr + File.separator+fileName);
		
		if (tmpFile.exists()) {
			bFirst = false;
			readPos();
		} 
		if(tmpFile.exists()) {
			downloadingSize = downLoadFileSize;
		}
		
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
		Utility.log("YP -->> network fileLen:"+nFileLength);
		//判断 已经存在的文件是否是最新版本，并检测是否需要删除文件重新下载
		if(nFileLength ==downLoadFileSize && nFileLength !=-1) {
			delFile(tmpFile);
			//md5校验
			if(checkMd5()) {
				//callback
				Utility.log("YP -->> checkMd5 is ok");
				upgradeListener.onFileDownLoadEnd();
				need = false;
				cancelTimerTask();
				stopDownLoadTask("from checkWhetherNeedDownLoad method for not need download beacuse md5 is ok but nFileLength!=-1");
			} else {
				//删除旧文件，进入下载流程
				Utility.log("YP -->> md5 file check is different... so del apk file... and redownload");
				delFile(getFileByPath(fileSaveDirStr + File.separator+fileName));
			}
		} else {
			if(downLoadFileSize > 0 && nFileLength==-1) { //存在问题
				Utility.log("YP -->> downLoadFileSize="+downLoadFileSize+" > 0 and nFileLength="+nFileLength+"=-1 so check local download file md5");
				if(checkMd5()) {
					//callback
					delFile(tmpFile);
					Utility.log("YP -->> nFileLength=-1 and downLoadFileSize >0 but checkMd5 is ok so file download successful!");
					upgradeListener.onFileDownLoadEnd();
					need = false;
					cancelTimerTask();
					stopDownLoadTask("from checkWhetherNeedDownLoad method for not need download beacuse md5 is ok but nFileLength==-1");
				} else {
					// if tmp file exists after check local version 
					if (tmpFile.exists()) {
						dealReDownLoad();
					} else { // tmp file not exists deal apk file re download
						//删除旧文件，进入下载流程
						Utility.log("YP -->> md5 file check is different... so del apk file... and redownload");
						delFile(getFileByPath(fileSaveDirStr + File.separator+fileName));
					}
				}
			} else {
				dealReDownLoad();
			}
			//由于网络不通 需要切换地址 ，不能进行下载，所以也就没有必要开始下载
			if(nFileLength==-1) {
				Utility.log("YP -->> checkWhetherNeedDownLoad nFileLength is -1 so network is unenable so not need go to download ");
				//need = false;
			}
		}
		return need;
	}
	
	/**
	 * 
	 * 
	 * @description 处理是否需要重新下载  存在临时文件的情况下
	 * @version 1.0
	 * @author LEE
	 * @date 2013-12-16 下午2:46:47 
	 * @update 2013-12-16 下午2:46:47
	 */
	private void dealReDownLoad() {
		//检测最新版本 与本地的版本是否匹配
		//如果版本不匹配 删除临时文件以及包文件 重新下载
		if(!checkVersion()) {
			Utility.log("YP -->>different version so del apk file .....");
			delFile(tmpFile);
			//删除旧文件，进入下载流程
			delFile(getFileByPath(fileSaveDirStr + File.separator+fileName));
		}
		this.writeVersionFile(fileSaveDirStr + File.separator+fileName+".version.info", currentVersion);
	}
	
	
	private String downloadedFileFid="";
	/**
	 * 
	 * @return
	 * @description md5文件校验
	 * @version 1.0
	 * @author LEE
	 * @date 2013-12-16 下午1:34:32 
	 * @update 2013-12-16 下午1:34:32
	 */
	private boolean checkMd5() {
		String fileMD5Str;
		try {
			Utility.log("YP -->>in checkMd5 method file path:"+fileSaveDirStr + File.separator+fileName);
			fileMD5Str = MD5Util.getFileMD5String(getFileByPath(fileSaveDirStr + File.separator+fileName));
			Utility.log("YP -->>in checkMd5 method file fileMD5Str:"+fileMD5Str);
			Utility.log("YP -->>in checkMd5 method file fid:"+fid);
			downloadedFileFid = fileMD5Str;
			if(fid.equals(fileMD5Str)) {
				return true;
			}
			Utility.log("YP -->>in checkMd5 check md5 over!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	
	/**
	 * 
	 * @return
	 * @description 检测服务器上版本是否与本地的版本一致
	 * @version 1.0
	 * @author LEE
	 * @date 2013-12-16 下午2:01:05 
	 * @update 2013-12-16 下午2:01:05
	 */
	private boolean checkVersion() {
		boolean sameVersion=false;
		try {
			//存在版本文件
			if (tmpFile.exists()) {
				String localVersion = getLocalVersion(fileSaveDirStr + File.separator+fileName+".version.info");
				if(currentVersion.equals(localVersion)) {
					sameVersion = true;
				}
				Utility.log("YP -->>checkVersion currentversion:"+currentVersion+"--localVersion:"+localVersion);
			} else {
				Utility.log("YP -->>checkVersion tmpFile not exists................");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return sameVersion;
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
			Utility.log("YP -->>getLocalVersion version:"+version);
		}
		
		return version;
	}
	
	private void writeVersionFile(String fileName,String version) {
		BufferedWriter writer = null;
		try {
			Utility.log("YP -->>writeVersionFile current version:"+version);
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
	private synchronized void  startDownload() {
		prepare();
		long startTime=System.currentTimeMillis();
		try {
			needDownLoad = checkWhetherNeedDownLoad();
			if(!needDownLoad) {
				return;
			}
			//空间不足直接返回 如果是false 肯定是已经检测过一次本地空间 
			if(!isLocalSpaceEnough) {
				Utility.log("YP -->> local free space is not enough so direct return; arrvied here express had checked local space! ");
				return;
			}
			//判断是否有足够的空间去保存数据
			Utility.log("YP -->> local free space size : "+getAvailableInternalMemorySize());
			if(getAvailableInternalMemorySize() < (nFileLength-downLoadFileSize)) {
				isLocalSpaceEnough = false;
				//上报没有足够空间
				StatisticsUpdateResultDataService.getInstance().transferMessage(DataConstants.DOWNLOADFAILED_NODEVICE, "", downloadingSize);
				stopDownLoadTask("from startDownload method for local free space is not enough");
				cancelTimerTask();
				upgradeListener.onFileDownLoadError(ErrorConstants.MSG_NO_ENOUGH_STORAGE_SPACE);
				return;
			} else {
				isLocalSpaceEnough = true;
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
			Utility.log("YP -->>down onFileDownLoadBegin");
			upgradeListener.onFileDownLoadBegin(nFileLength);
			try {
				StatisticsUpdateResultDataService.getInstance().transferMessage(DataConstants.STARTDOWNLOAD_ROOT, null, 0);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			// 启动子线程
			fileSplitterFetch = new DownloadThread[nStartPos.length];
			for (int i = 0; i < nStartPos.length; i++) {
				fileSplitterFetch[i] = new DownloadThread(downloadIp, fileSaveDirStr + File.separator+fileName,nStartPos[i], nEndPos[i], i,this);
				Utility.log("Thread " + i + " , nStartPos = " + nStartPos[i]+ ", nEndPos = " + nEndPos[i]);
				fileSplitterFetch[i].start();
			}
			// 等待子线程结束
			// 是否结束 while 循环
			boolean breakWhile = false;
			int sleepTime=500;
			while (!bStop) {
				writePos();
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
				if(checkProxySpeed(sleepTime) && AuxiliaryConstants.isCheckProxySpeed) {
					this.stopDownLoadTask("from startDownload method for  proxySpeed >20kb");
					return;
				}
			}
			if(isChangedDownUrl) {
				return;
			}
			long endTime=System.currentTimeMillis();
			long costTime=(endTime-startTime)/1000;
			Utility.log("download over casttime:"+costTime+"s");

			if(getDownLoadedFileSize(fileSaveDirStr + File.separator+fileName)==nFileLength) {
				if(!checkMd5()) {
					Utility.log("YP -->> MD5 check  error! server fid:"+fid+"--download file fid:"+downloadedFileFid);
					upgradeListener.onFileDownLoadError(ErrorConstants.MSG_MD5_CHECK_ERROR_CONTENT);
					cancelTimerTask();
					delFile(tmpFile);
				} else {
					Utility.log("YP -->> MD5 check  ok! server fid:"+fid+"--download file fid:"+downloadedFileFid);
					upgradeListener.onFileDownLoadEnd();
					Utility.log("YP -->>down onFileDownLoadEnd");
					try {
						StatisticsUpdateResultDataService.getInstance().transferMessage(DataConstants.DOWNLOADSUCCESS_ROOT, null, nFileLength);
					} catch(Exception e) {
						e.printStackTrace();
					}
					
					cancelTimerTask();
					delFile(tmpFile);
				}
			} else {
				responseError("startDownload1","YP -->> download over but file error!",null,false);
				//如果不是第一次 则 删除临时文件重新下载
				if(!bFirst) {
					responseError("startDownload2","YP -->> bFirst"+bFirst,null,false);
					downloadingSize = 0;
					delFile(tmpFile);
					delFile(getFileByPath(fileSaveDirStr + File.separator+fileName));
					bFirst = true;
				}
			}

			
			
		} catch (Exception e) {
			responseError("startDownload3",getExceptionInfo(e),null,false);
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
	//重定向后 真正的下载文件的ip地址
	private String downloadIp="";
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
			Utility.log("YP -->> getFileSizeFromNetWork ：downloadUrl >>>:"+downloadUrl);
			AuxiliaryConstants.downLoadUrl = downloadUrl;
			httpConnection = (HttpURLConnection) url.openConnection();
			//LEE重定向 start
			httpConnection.setInstanceFollowRedirects(false);
			//LEE重定向 end
			httpConnection.setRequestProperty("User-Agent", "NetFox");
			httpConnection.setConnectTimeout(Utility.connectTimeout);
			httpConnection.setReadTimeout(Utility.readTimeout);
			
			boolean from302=false;
			//重定向处理 start
			 int code = httpConnection.getResponseCode();
	            while(true) {
	            	if(code ==302) {
	            		from302 = true;
	            		downloadIp = httpConnection.getHeaderField("Location");
	            		 httpConnection.disconnect();
	            		 
	            		 url = new URL(downloadIp);
	            		 httpConnection = (HttpURLConnection) url.openConnection();   
	            		 httpConnection.setInstanceFollowRedirects(false);
	                     code = httpConnection.getResponseCode();
	            	} 
	            	if(code == 200) {
	            		if(!from302) {
	            			downloadIp = downloadUrl;
	            		}
	            		Utility.log("YP -->> downLoadIp>>>>>>>>>>>>>>>>>>:"+downloadIp);
	            		break;
	            	} else if(code !=302) {
	            		Utility.log("YP -->> redirect router error code >>>>>>>>>>>>>>>>>>:"+code);
	            		throw new Exception();
	            	}
	            }
			//重定向处理 end
			
			
			
			AuxiliaryConstants.downLoadIp = downloadIp;
			int responseCode = httpConnection.getResponseCode();
			if (responseCode >= 400) {
				this.responseError("getFileSizeFromNetWork","Error code 400",DataConstants.STARTDOWNLOADFAILED_ROOT,false);
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
			Utility.log("YP -->> getFileSizeFromNetWork in catch");
			responseError("getFileSizeFromNetWork",getExceptionInfo(e),DataConstants.STARTDOWNLOADFAILED_ROOT,false);
			//e.printStackTrace();
		}
		Utility.log("YP -->> nFileLength:"+nFileLength);
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

	/**
	 * @description 保存下载信息（文件指针位置）
	 * @version 1.0
	 * @author LEE
	 * @date 2013-12-11 下午1:51:02 
	 * @update 2013-12-11 下午1:51:02
	 */
	private void writePos() {
		try {
			output = new DataOutputStream(new FileOutputStream(tmpFile));
			output.writeInt(nStartPos.length);
			for (int i = 0; i < nStartPos.length; i++) {
				output.writeLong(fileSplitterFetch[i].nStartPos);
				output.writeLong(fileSplitterFetch[i].nEndPos);
			}
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 
	 * @description 读取保存的下载信息（文件指针位置）
	 * @version 1.0
	 * @author LEE
	 * @date 2013-12-11 下午1:51:25 
	 * @update 2013-12-11 下午1:51:25
	 */
	private void readPos() {
		try {
			DataInputStream input = new DataInputStream(new FileInputStream(
					tmpFile));
			int nCount = input.readInt();
			nStartPos = new long[nCount];
			nEndPos = new long[nCount];
			for (int i = 0; i < nStartPos.length; i++) {
				nStartPos[i] = input.readLong();
				nEndPos[i] = input.readLong();
			}
			nStartPos[0] = downLoadFileSize;
			input.close();
		} catch (Exception e) {
			//若临时文件读取有问题，则需要重新下载
			//所以删除临时文件 以及已经下载的文件
			delFile(tmpFile);
			delFile(getFileByPath(fileSaveDirStr + File.separator+fileName));
			this.bFirst = true;
			e.printStackTrace();
		}
	}
	
	private  final String lock="Lock";
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
	protected  void showDownloadingSize(int size) {
		synchronized(lock) {
			downloadingSize = size;
			//Utility.log("thread:"+threadName+"--downloadSize:"+downloadSize);
			if(downloadingSize>nFileLength) {
				downloadingSize = nFileLength;
			}
			upgradeListener.onFileDownLoadProgress(downloadingSize);
		}
	}
	
	

	private void processErrorCode(int nErrorCode) {
		Utility.log("YP -->> Error Code : " + nErrorCode);
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
	protected void stopDownLoadTask(String fromWhere) {
		Utility.log("YP -->> stopDownLoadTask from : "+fromWhere);
		j++;
		if(nStartPos != null && fileSplitterFetch != null) {
			for (int i = 0; i < nStartPos.length; i++) {
				if(fileSplitterFetch[i] != null) {
					fileSplitterFetch[i].stopSelf();
				}
			}
		}

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
			Utility.log("YP -->>  delete "+fileName+" file success....");
		} catch(Exception e) {
			Utility.log("YP -->>  delete "+fileName+" file failed....");
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
		Utility.log("YP -->> getDownLoadedFileSize:"+fileSize);
		return fileSize;
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
	protected int getDownLoadedFileSize() {
		int fileSize=0;
		try {
			File f = new File(fileSaveDirStr + File.separator+fileName);
			if(f.exists()) {
				fileSize = (int)f.length();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		Utility.log("YP -->> getDownLoadedFileSize:"+fileSize);
		return fileSize;
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
	protected int getDownLoadedFileSize2() {
		return downloadingSize;
	}
	
	
	private File getFileByPath(String filePath) {
		File f = null;
		try {
			f = new File(filePath);
			
		} catch(Exception e) {
			Utility.log("YP -->> in getFileByPath method occur exception  content is :");
			e.printStackTrace();
		}
		return f;
	}
	protected void responseError(String errorFromWhere,final String error,String errorType,final boolean isSocketTimeOut) {
		Utility.log("YP -->> errorFromWhere:"+errorFromWhere);
		Utility.log("YP -->> responseError:"+error);
		if(!checkDownUrlExpire()) {
			Utility.log("YP -->> in responseError method in 5min ");
			Utility.log("YP -->> in responseError method check cycleCount: "+cycleCount);
			if(cycleCount>=this.totalCycleCount) {
				Utility.log("YP -->> onFileDownLoadError:"+error);
				Utility.log("YP -->> responseError and  cycleCount is "+cycleCount);
				upgradeListener.onFileDownLoadError(ErrorConstants.MSG_CHANGE_URL_TIME_GREATER_6_CONTENT); 
				try {
					if(DataConstants.STARTDOWNLOADFAILED_ROOT.equals(errorType)) {
						StatisticsUpdateResultDataService.getInstance().transferMessage(DataConstants.STARTDOWNLOADFAILED_ROOT, error, downloadingSize);
						Utility.log("YP -->> no start download:"+error);
					} else {
						StatisticsUpdateResultDataService.getInstance().transferMessage(DataConstants.DOWNLOADFAILED_ROOT, error, downloadingSize);
					}
					
				} catch(Exception e) {
					e.printStackTrace();
				}
				
				cancelTimerTask();
				return;
			} else {
				try {
					if(DataConstants.STARTDOWNLOADFAILED_ROOT.equals(errorType)) {
						StatisticsUpdateResultDataService.getInstance().transferMessage(DataConstants.STARTDOWNLOADFAILED_ROOT, error, downloadingSize);
						Utility.log("YP -->> no start download:"+error);
					} else {
						StatisticsUpdateResultDataService.getInstance().transferMessage(DataConstants.DOWNLOADFAILED_ROOT, error, downloadingSize);
					}
					Thread.sleep(5000);
					Utility.log("YP -->> sleep 5 sec and continue execute changeDownUrl and StartDownload.");
				} catch(Exception e) {
					e.printStackTrace();
				}
				changeDownUrl(isSocketTimeOut);
			}
		} else {
			
			Utility.log("YP -->> in responseError method out 5min and start regain download url");
			//一次的下载过程切换数重置
			cycleCount = 0;
			//下载串到了5分钟到期了开始重新获取下载地址
			if(currentReGetDownLoadUrlThroughCheckVersionUrlCount >=totalReGetDownLoadUrlThroughCheckVersionUrlCount) {
				Utility.log("YP -->> in responseError method CheckVersionUrlCount >"+totalReGetDownLoadUrlThroughCheckVersionUrlCount+"  so cancelTimerTask game over!");
				upgradeListener.onFileDownLoadError(ErrorConstants.MSG_GET_DOWN_URL_TIME_GREATER_2_CONTENT); 
				cancelTimerTask();
				return;
			}
			
			Utility.log("YP -->> in responseError method start new Thead to getFileDownLoadUrl");
			/**
			 * 启动一个子线程 获取下载地址
			 * 如果第一次获取失败 ，休息5秒钟后开始第二次获取
			 * 如果第二次获取成功则开始切换地址
			 */
			new Thread(){
				public void run() {
					try {
						String downUrlTmp = getFileDownLoadUrl();
						if(StringUtil.isNull(downUrlTmp)) {
							Utility.log("YP -->> in responseError method get download url error is null so  after sleep 5s reget");
							Thread.sleep(5000);
							downUrlTmp = getFileDownLoadUrl();
						} else {
							Utility.log("YP -->> in responseError method get download url successful and downUrl is :"+downUrlTmp);
						}
						
						firstDownLoadUrl = downUrlTmp;
						if(StringUtil.isNotNull(firstDownLoadUrl)) {
							setHostList(firstDownLoadUrl);
							Utility.log("YP -->> in responseError method changeDownUrl after reGet downloadUrl..");
							changeDownUrl(isSocketTimeOut);
						} else {
							upgradeListener.onFileDownLoadError(ErrorConstants.MSG_GET_DOWN_URL_TIME_GREATER_2_CONTENT); 
							cancelTimerTask();
							return;
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
			
			//重新设置开始下载时间
			this.startDownLoadTime = System.currentTimeMillis();
		}
		
		
		
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
		task = new TimerTask() {
			@Override
			public void run() {
				if(!isStopDownLoadTask() || !AuxiliaryConstants.isCheckProxySpeed ) {
					if(isFirstTime) {
						isFirstTime = false;
						//第一次启动时开始设置开始下载时间，如果不重新获取下载地址 则此时间不会进行重新设置。2015.8.6
						//为了 下载5分钟后出现下载问题而记录的下载时间
						//当重新获取到新的下载地址后 重置此时间
						startDownLoadTime = System.currentTimeMillis();
						startDownload();
					}
					synchronized(this) {
						if(bStop && needDownLoad && isLocalSpaceEnough &&!stopTask) {
							k++;
							bStop = false;
							Utility.log("YP -->> reDownLoad..........................");
							startDownload();
						} else {
							Utility.log("YP -->>no reDownLoad reason is :");
							Utility.log("YP -->> bStop value is :"+bStop);
							Utility.log("YP -->> needDownLoad value is :"+needDownLoad);
							Utility.log("YP -->> isLocalSpaceEnough value is :"+isLocalSpaceEnough);
						}
					}
				} else {
					Utility.log("YP -->> player proxy is working >20kb so  stop download......");
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
		timer.cancel();
		task.cancel();
		System.out.println("YP -->> restart count:"+k);
		System.out.println("YP -->> stopDownLoadTask count:"+j);
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
			Utility.log("YP -->> current proxyUrl:"+proxyUrl);
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
				Utility.log("YP -->> isStopDownLoadTask player proxy is working "+downSpeed+"kb >20kb so  stop download......");
			} else {
				Utility.log("YP -->> isStopDownLoadTask player proxy is notworking "+downSpeed+"kb <20kb so  go download......");
			}
		} catch(Exception e) {
			//e.printStackTrace();
			Utility.log("YP -->> cant connect "+proxyUrl);
		}
		
		return isStopDownLoadTask;
	}
	
	private boolean testStop = false;
	private int time = 0;
	/**
	 * 
	 * @param tmpTime
	 * @return
	 * @description 检测是否需要停止下载 5秒检测一次
	 * @version 1.0
	 * @author LEE
	 * @date 2013-12-17 下午2:55:43 
	 * @update 2013-12-17 下午2:55:43
	 */
	private boolean checkProxySpeed(int tmpTime) {
		time+=tmpTime;
		boolean stop = false;
		if(time>tmpTime*20) {
			//需要停止下载..
			if(isStopDownLoadTask()) {
				stop = true;
				Utility.log("YP -->> stop down beacuse proxy download speed >20kb in checkProxySpeed method !");
			}
			time = 0;
		}
		/*if(testStop) {
			Utility.log("stop down beacuse proxy download speed >20kb in checkProxySpeed method !");
			stop = true;
			testStop = false;
		} else {
			Utility.log("isStopDownLoadTask player proxy is notworking  <20kb so  go download......");
			stop = false;
			testStop = true;
		}*/
		return stop;
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
		stopTask = true;
		this.cancelTimerTask();
		stopDownLoadTask("from stopFileDownLoader method for invoker do this....");
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
	
	/**
	 * 
	 * @return 1
	 * @description  获取可用存儲空間
	 * @version 1.0
	 * @author LEE
	 * @date 2014-3-4 下午6:17:34 
	 * @update 2014-3-4 下午6:17:34
	 */
	public long getAvailableInternalMemorySize(){  
        File path = Environment.getDataDirectory();  //获取数据目录  
        StatFs stat = new StatFs(path.getPath());  
        long blockSize = stat.getBlockSize();  
        long availableBlocks = stat.getAvailableBlocks();  
        return availableBlocks*blockSize;  
	}
	
	public String getVersion() {
		return VersionConstants.currentVersion;
	}
	
	/**
	 * 
	 * @return
	 * @description 通过检测版本url获取下载地址
	 * 总共获取两次 ，如果第一次正常获取
	 * @version 1.0
	 * @author LEE
	 * @date 2015-1-28 上午11:51:59 
	 * @update 2015-1-28 上午11:51:59
	 */
	private String getFileDownLoadUrl() {
		Utility.log("YP -->> FileDownloader start get new downurl ");
		try {
			UpdateInfo updateInfo = null;
			updateInfo = VooleData.getInstance()
					.parseUpdateInfo(AuxiliaryConstants.checkVersionUrlReRequest);
			if(updateInfo != null) {
				newDownLoadUrl = updateInfo.getDownloadUrl();
			}
		} catch (Exception e) {
			Utility.log("YP -->> getFileDownLoadUrl error: ");
			e.printStackTrace();
		}
		
		currentReGetDownLoadUrlThroughCheckVersionUrlCount++;
		Utility.log("YP -->> FileDownloader get new downurl : "+newDownLoadUrl);
		return newDownLoadUrl;
	}
	
	/**
	 * 
	 * @return
	 * @description 检测下载地址是否到期
	 * @version 1.0
	 * @author LEE
	 * @date 2015-1-28 下午12:07:55 
	 * @update 2015-1-28 下午12:07:55
	 */
	private boolean checkDownUrlExpire() {
		Utility.log("YP -->> checkDownUrlExpire ");
		long min = (System.currentTimeMillis() - startDownLoadTime)/60000;
		Utility.log("YP -->> checkDownUrlExpire downTime:"+min);
		if(min>=5) {
			return true;
		}
		return false;
	}
	//////////////////////////////////////////////////////////

	
	
}