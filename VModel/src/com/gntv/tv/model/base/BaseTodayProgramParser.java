package com.gntv.tv.model.base;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.gntv.tv.common.utils.NetUtil;
import com.gntv.tv.model.channel.TodayProgramInfo;
/**
 * 解析一个台今日的所有节目单
 * @author TMAC-J
 *
 */
public abstract class BaseTodayProgramParser {
	protected TodayProgramInfo info = null;
	private InputStream stream = null;

	public void setUrl(String url) throws Exception {
		stream = NetUtil.connectServer(url, 2, 5, 20);
		if (stream != null) {
			setInputStream(stream);
		}
	}

	public void setHttpsUrl(String url) throws Exception {
		stream = NetUtil.connectServerWithHttps(url, 2, 5, 20);
		if (stream != null) {
			setInputStream(stream);
		}
	}

	public void postUrl(String url, String param) throws Exception {
		stream = NetUtil.doPost(url, param);
		if (stream != null) {
			setInputStream(stream);
		}
	}

	public void postHttpsUrl(String url, String param) throws Exception {
		stream = NetUtil.doPostWithHttps(url, param);
		if (stream != null) {
			setInputStream(stream);
		}
	}

	public InputStream getStream() {
		return this.stream;
	}

	public abstract void setInputStream(InputStream inputStream) throws Exception;

	public TodayProgramInfo getInfo() {
		return info;
	} 

}
