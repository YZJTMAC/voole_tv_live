package com.voole.android.client.UpAndAu.downloader;
/**
 * 消息处理对象
 * 
 * @author LEE
 */
public interface FileDownLoaderListener {
    /**
     * 版本更新错误
     * 
     * @param msg 错误信息
     */
    public void onFileDownLoadError(String msg);
    /**
     * 
     * @param fileSize
     * @description 文件大小
     * @version 1.0
     * @author LEE
     * @date 2013-12-13 下午1:15:01 
     * @update 2013-12-13 下午1:15:01
     */
    public void onFileDownLoadBegin(int fileSize);

    /**
     * 文件下载进度
     * 
     * @param size 已下载大小
     */
    public void onFileDownLoadProgress(int size);

    /**
     * 文件下载完成
     * 
     */
    public void onFileDownLoadEnd();
}