package com.gntv.tv.model.error;

import java.util.HashMap;
import java.util.Map;

import com.gntv.tv.common.utils.LogUtil;
import com.voole.error.code.container.BaseConfig;
import com.voole.error.code.log.ILog;
import com.voole.error.code.main.StartService;
import com.voole.error.code.pojo.ErrorCodeOemPojo;
import com.voole.error.code.ser.ErrorCodeQuerySer;

import android.text.TextUtils;

public class ErrorManager {
	public static final String ERROR_NET_FAIL = "0102100001";
	public static final String ERROR_GET_CHANNEL_NULL = "0102100002";
	public static final String ERROR_GET_CHANNEL = "0102100003";
	public static final String ERROR_LOGIN_TEST = "0102100008";
	public static final String ERROR_CHANNEL_TEST = "0102100009";
	public static final String ERROR_PLAYURL_TEST = "0102100010";
	public static final String ERROR_NETSPEED_TEST = "0102100011";
	public static final String ERROR_LIVE_COMPLETION = "0102100012";
	public static final String ERROR_START_AUTH = "0191100001";
	public static final String ERROR_GET_USER_NULL = "0191100002";
	public static final String ERROR_GET_USER = "0191100003";
	public static final String ERROR_URLLIST_NULL = "0191100004";
	public static final String ERROR_URLLIST = "0191100005";
	public static final String ERROR_GET_PLAYURL_NULL= "0191100007";
	public static final String ERROR_GET_PLAYURL = "0191100008";
	public static final String ERROR_START_PROXY = "0191100009";
	public static final String ERROR_UPGRADE_CHECK = "0192100001";
	public static final String ERROR_UPGRADE_DOWNLOAD = "0192100002";
	public static final String ERROR_PLAYER = "0194100001";
	public static final String ERROR_PLAYER_TIMEOUT = "0194100002";
	public static final String ERROR_PLAYER_PROXY = "0194100003";
	
	private Map<String, String> error_map = new HashMap<String, String>();
	private static ErrorManager instance = new ErrorManager();
	private boolean isStarted = false;
	private String qq;
	private ErrorManager() {
		error_map.put(ERROR_NET_FAIL, "亲，连不上网啦！请您检查一下网络。");
		error_map.put(ERROR_GET_CHANNEL_NULL, "亲，出错啦！请点击故障检测按钮检查问题。");
		error_map.put(ERROR_GET_CHANNEL, "亲，出错啦！请点击故障检测按钮检查问题。");
		error_map.put(ERROR_LOGIN_TEST, "用户登录异常，请重试。");
		error_map.put(ERROR_CHANNEL_TEST, "影片内容获取异常，请重试。");
		error_map.put(ERROR_PLAYURL_TEST, "服务器连接异常，请重试。");
		error_map.put(ERROR_NETSPEED_TEST, "网络连接异常，请重试。");
		error_map.put(ERROR_LIVE_COMPLETION, "亲，当前频道播放异常，换个台试试吧。");
		error_map.put(ERROR_START_AUTH, "亲，出错啦！请点击故障检测按钮检查问题。");
		error_map.put(ERROR_GET_USER_NULL, "亲，出错啦！请点击故障检测按钮检查问题。");
		error_map.put(ERROR_GET_USER, "亲，出错啦！请点击故障检测按钮检查问题。");
		error_map.put(ERROR_URLLIST_NULL, "亲，出错啦！请点击故障检测按钮检查问题。");
		error_map.put(ERROR_URLLIST, "亲，出错啦！请点击故障检测按钮检查问题。");
		error_map.put(ERROR_GET_PLAYURL_NULL, "亲，当前频道播放异常，换个台试试吧。");
		error_map.put(ERROR_GET_PLAYURL, "亲，当前频道播放异常，换个台试试吧。");
		error_map.put(ERROR_START_PROXY, "亲，出错啦！请点击故障检测按钮检查问题。");
		error_map.put(ERROR_UPGRADE_CHECK, "检测升级版本时出现异常！");
		error_map.put(ERROR_UPGRADE_DOWNLOAD, "安装包下载异常，请重试。");
		error_map.put(ERROR_PLAYER, "亲，当前频道播放异常，换个台试试吧。");
		error_map.put(ERROR_PLAYER_TIMEOUT, "亲，当前频道播放异常，换个台试试吧。");
		error_map.put(ERROR_PLAYER_PROXY, "亲，当前频道播放异常，换个台试试吧。");
		
	}

	public static ErrorManager GetInstance() {
		return instance;
	}

	public boolean init(String oemid, String qq) {
		this.qq = qq;
		BaseConfig.ERRORCODE.setReadTimeout(10 * 1000);
		isStarted = StartService.getInstance().start(null , null,"[{\"oemInfo\":\"" + oemid + "\",\"oemType\":\"0\"}]", "01", new ILog() {

					@Override
					public void warn(String arg0, Throwable arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void warn(String arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void trace(String arg0, Throwable arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void trace(String arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void info(String arg0, Throwable arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void info(String arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void fatal(String arg0, Throwable arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void fatal(String arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void error(String arg0, Throwable arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void error(String arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void debug(String arg0, Throwable arg1) {
					}

					@Override
					public void debug(String arg0) {
					}
				}, null, 0, true, false);
		return isStarted;
	}

	public ErrorInfo getErrorMsg(String errorCodeApk, String errorCodeOther, String messageOther) {
		ErrorInfo errorInfo = new ErrorInfo();
		if(isStarted){
			ErrorCodeOemPojo queryByApk = ErrorCodeQuerySer.queryByApk(errorCodeApk, errorCodeOther, null, messageOther, null);
			if(queryByApk != null){
				errorInfo.setErrorCode(queryByApk.getErrorCode());
				String errorMessage = null;
				if(TextUtils.isEmpty(queryByApk.getMessage())){
					errorMessage = error_map.get(errorCodeApk);
				}else {
					errorMessage = queryByApk.getMessage();
				}
				if(!"0000000000".equals(errorCodeOther)){
					errorInfo.setErroeMessageAndCode(errorMessage+"\n错误码："+queryByApk.getErrorCode()+","+errorCodeOther);
					errorInfo.setErrorCodeOther(errorCodeOther);
				}else {
					errorInfo.setErroeMessageAndCode(errorMessage+"\n错误码："+queryByApk.getErrorCode());
					errorInfo.setErrorCodeOther("");
				}
				errorInfo.setQq(qq);
				LogUtil.e("ErrorManager----->net_data_message:"+errorMessage+",net_data_code:"+queryByApk.getErrorCode());
				return errorInfo;
			}
			errorInfo.setErrorCode(errorCodeApk);
			errorInfo.setErroeMessageAndCode(error_map.get(errorCodeApk)+"\n错误码："+errorCodeApk);
			errorInfo.setQq(qq);
			LogUtil.e("ErrorManager----->local_data_message:"+error_map.get(errorCodeApk)+",net_data_code:"+errorCodeApk);
			return errorInfo;
		}else {
			errorInfo.setErrorCode(errorCodeApk);
			errorInfo.setErroeMessageAndCode(error_map.get(errorCodeApk)+"\n错误码："+errorCodeApk);
			errorInfo.setQq(qq);
			LogUtil.e("ErrorManager----->local_data_message:"+error_map.get(errorCodeApk)+",net_data_code:"+errorCodeApk);
			return errorInfo;
		}
		
	}
	
	public ErrorInfo getErrorMsg(String errorCodeApk){
		return getErrorMsg(errorCodeApk, "0000000000", "");
	}
	
}
