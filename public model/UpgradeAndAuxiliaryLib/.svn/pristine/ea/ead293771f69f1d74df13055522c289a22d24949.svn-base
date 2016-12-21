package com.voole.android.client.UpAndAu.downloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class ApkDownloadThread extends Thread {
	// File URL
	String sURL; 
	// File Snippet Start Position
	long nStartPos; 
	// File Snippet End Position
	long nEndPos; 
	// Thread's ID
	int nThreadID; 
	// Downing is over
	boolean bDownOver = false; 
	// Stop identical
	boolean bStop = false; 
	// File Access interface
	FileAccessI fileAccessI = null; 
	private ApkDownloader fileDownloader;
//	int testI=0;

	public ApkDownloadThread(String sURL, String sName, long nStart, long nEnd,
			int id,ApkDownloader fileDownloader) throws IOException {
		this.sURL = sURL;
		this.nStartPos = nStart;
		this.nEndPos = nEnd;
		nThreadID = id;
		this.fileDownloader = fileDownloader;
		fileAccessI = new FileAccessI(sName, nStartPos);
	} 



	public void run(){
		if(nStartPos < nEndPos && !bStop) {
			// gotodown....
		} else {
			Utility.log("don't need download");
			bDownOver = true;
			return;
		}
		
		HttpURLConnection httpConnection = null;
		
		while (nStartPos < nEndPos && !bStop) {
			try {
				URL url = new URL(sURL);
				httpConnection = (HttpURLConnection) url.openConnection();
				
				Utility.log("YP -->>in run() while 000000 bstop   is " + bStop );
				httpConnection.setConnectTimeout(Utility.connectTimeout);
				httpConnection.setReadTimeout(Utility.readTimeout);
				httpConnection.setRequestProperty("User-Agent", "NetFox");
				String sProperty = "bytes=" + nStartPos + "-";
				httpConnection.setRequestProperty("Range", sProperty);
				Utility.log(sProperty);
				InputStream input = httpConnection.getInputStream();
				// logResponseHead(httpConnection);
				byte[] b = new byte[2048];
				int nRead;
				while ((nRead = input.read(b, 0, 2048)) > 0
						&& nStartPos < nEndPos && !bStop) {
					Utility.log("YP -->>in run() while 111111 bstop   " + bStop  + "ApkDownloader.stopTask is " +fileDownloader.isStopTask());
					if (fileDownloader.isStopTask()) {
						Utility.log("YP -->> in run() isStopTask is true  ");
						return;
					}
					nStartPos += fileAccessI.write(b, 0, nRead);
					fileDownloader.append(nRead,this.getName());
					Thread.sleep(2);
				}
				Utility.log("YP -->>Thread " + nThreadID + " is over!");
				bDownOver = true;
				// nPos = fileAccessI.write (b,0,nRead);
			} catch (SocketTimeoutException e) {
				stopSelf();
				disConnect(httpConnection);
				fileAccessI.closeRandomAccessFile();
				fileDownloader.changeDownUrl(true);
				//continue 
				e.printStackTrace();
			} catch (MalformedURLException e) {
				//error
				stopSelf();
				disConnect(httpConnection);
				fileDownloader.responseError(getExceptionInfo(e));
				e.printStackTrace();
			} catch (IOException e) {
				//error
				stopSelf();
				disConnect(httpConnection);
				fileDownloader.responseError(getExceptionInfo(e));
				e.printStackTrace();
			} catch (Exception e) {
				//error
				Utility.log("YP -->>error bstop is  " + bStop);
				stopSelf();
				disConnect(httpConnection);
				fileDownloader.responseError(getExceptionInfo(e));
				e.printStackTrace();
			}
		}
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

	// 打印回应的头信息
	private void logResponseHead(HttpURLConnection con) {
		for (int i = 1;; i++) {
			String header = con.getHeaderFieldKey(i);
			if (header != null)
				// responseHeaders.put(header,httpConnection.getHeaderField(header));
				Utility.log(header + " : " + con.getHeaderField(header));
			else
				break;
		}
	}
	protected void stopSelf() {
		bStop = true;
		Utility.log("in stopSelf()..........");
		if (fileAccessI!=null) {
			fileAccessI.closeRandomAccessFile();
		}
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
}