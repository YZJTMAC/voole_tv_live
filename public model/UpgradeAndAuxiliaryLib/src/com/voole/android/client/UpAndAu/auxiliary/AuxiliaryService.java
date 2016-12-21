package com.voole.android.client.UpAndAu.auxiliary;

import java.io.InputStream;

import android.content.Context;
import android.widget.Toast;

import com.voole.android.client.UpAndAu.model.AboutInfo;
import com.voole.android.client.UpAndAu.model.AdviceFeedBackInfo;
import com.voole.android.client.UpAndAu.model.DataResult;
import com.voole.android.client.UpAndAu.model.ExceptionFeedBackInfo;
import com.voole.android.client.UpAndAu.model.FeedBackOptionInfo;
import com.voole.android.client.UpAndAu.model.HelpInfo;
import com.voole.android.client.UpAndAu.model.parser.VooleData;
import com.voole.android.client.UpAndAu.util.HttpUtil;
import com.voole.android.client.UpAndAu.util.Logger;
import com.voole.android.client.UpAndAu.util.NetUtil;
import com.voole.android.client.UpAndAu.util.StringUtil;

/**
 * 
 * 
 * @author LEE
 * @version V1.0
 * @date 2013-4-7 上午11:49:20
 * @author wusong
 * @version V1.1
 * @update 2013-4-16下午2:31:31
 */
public class AuxiliaryService {
	private static AuxiliaryService instance = null;
	private Context context;

	/**
	 *
	 * 
	 * @param context
	 * @return AuxiliaryService
	 */
	public static AuxiliaryService getInstance(Context context) {

		if (instance == null) {
			instance = new AuxiliaryService(context);
		}

		return instance;
	}

	private AuxiliaryService(Context context) {
		this.context = context;
	}

	public AboutInfo getAboutInfo(String url) {
		if (!StringUtil.isNotNull(url)) {
			Logger.debug("getAboutInfo", "url is null");
			Toast.makeText(context, "url is null", Toast.LENGTH_SHORT).show();
			return null;
		}
		AboutInfo aboutInfo = null;
		if (NetUtil.isNetworkConnected(context)) {
			aboutInfo = VooleData.getInstance().parseAboutInfo(url);
		} else {
			Logger.debug("getAboutInfo", "no network");
			Toast.makeText(context, "no network", Toast.LENGTH_SHORT).show();
		}

		return aboutInfo;

	}

	public HelpInfo getHelpInfo(String url) {
		if (!StringUtil.isNotNull(url)) {
			Logger.debug("getAboutInfo", "url is null");
			Toast.makeText(context, "url is null", Toast.LENGTH_SHORT).show();
			return null;
		}
		HelpInfo helpInfo = null;
		if (NetUtil.isNetworkConnected(context)) {
			helpInfo = VooleData.getInstance().parseHelpInfo(url);
		} else {
			Logger.debug("getAboutInfo", "no network");
			Toast.makeText(context, "no network", Toast.LENGTH_SHORT).show();
		}
		return helpInfo;
	}

	public FeedBackOptionInfo getFeedBackOptionInfo(String url) {
		if (!StringUtil.isNotNull(url)) {
			Logger.debug("getAboutInfo", "url is null");
			Toast.makeText(context, "url is null", Toast.LENGTH_SHORT).show();
			return null;
		}
		FeedBackOptionInfo feedBackOptionInfo = null;
		if (NetUtil.isNetworkConnected(context)) {
			feedBackOptionInfo = VooleData.getInstance().parseFeedBackOption(
					url);
		} else {
			Logger.debug("getAboutInfo", "no network");
			Toast.makeText(context, "no network", Toast.LENGTH_SHORT).show();
		}
		return feedBackOptionInfo;
	}

	public DataResult reportAdviceFeedBackInfo(
			AdviceFeedBackInfo adviceFeedBackInfo, String url) {
		DataResult dataResult = null;
		if (adviceFeedBackInfo != null && StringUtil.isNotNull(url)) {
			InputStream in = null;
			HttpUtil httpUtil = new HttpUtil();
			if (NetUtil.isNetworkConnected(context)) {
				String params = VooleData.getInstance()
						.generateAdviceFeedBackInfo(adviceFeedBackInfo);
				try {
					in = httpUtil.doPost(url, params, -1, -1, null);
					dataResult = VooleData.getInstance().ParseDataResult(in);

				} finally {
					httpUtil.closeInputStream(in);
				}
			}
		} else {
			Logger.debug("getAboutInfo", "no network");
			Toast.makeText(context, "no network", Toast.LENGTH_SHORT).show();
		}

		Logger.debug("DataResultParser", url);

		return dataResult;
	}

	public DataResult reportExceptionFeedBackInfo(
			ExceptionFeedBackInfo exceptionFeedBackInfo, String url) {
		DataResult dataResult = null;
		if (exceptionFeedBackInfo != null && StringUtil.isNotNull(url)) {
			InputStream in = null;
			if (NetUtil.isNetworkConnected(context)) {
				HttpUtil httpUtil = new HttpUtil();

				try {
					String params = VooleData.getInstance()
							.generateExceptionFeedBackInfo(
									exceptionFeedBackInfo);
					in = httpUtil.doPost(url, params, -1, -1, null);
					// String result = StringUtil.convertStreamToString(in);
					// Logger.debug("DataResultParser", result);
					dataResult = VooleData.getInstance().ParseDataResult(in);

				} finally {
					httpUtil.closeInputStream(in);
				}
			}
		} else {
			Logger.debug("getAboutInfo", "no network");
			Toast.makeText(context, "no network", Toast.LENGTH_SHORT).show();
		}

		return dataResult;
	}
}
