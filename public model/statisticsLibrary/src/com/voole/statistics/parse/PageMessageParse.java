package com.voole.statistics.parse;

import java.io.IOException;
import java.io.InputStream;

import com.voole.statistics.util.BaseHttpInputStream;

/**
 * 发送页面统计消息解析类
 * @author Jacky
 *
 */
public class PageMessageParse
{
	
	public String parse(String url,String messageParam)
			throws IOException, Exception {
		InputStream is = BaseHttpInputStream.getInstance().getInputStreamNoKeep(
				url, messageParam);
		String str=null;
		//DataResultBean dataResultBean = new DataResultBean();
		try {
			StringBuffer sb = new StringBuffer();
			byte b[] = new byte[1024];
			int i = 0;
			while (-1 != (i = is.read(b))) {
				sb.append(new String(b, 0, i, "utf-8"));
			}
			str=sb.toString();	
			/*
			XmlPullParser pull = Xml.newPullParser();
			pull.setInput(is, "UTF-8");
			for (int eventType = pull.next(); eventType != XmlPullParser.END_DOCUMENT ; eventType = pull.next()) {
				String name = pull.getName();
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if (name.equals("DataResult")) {
						dataResultBean.setResultCode(pull.getAttributeValue(null, "resultCode"));
						break;
					} else if (name.equals("ResultText")) {
						pull.next();
						dataResultBean.setResultText(pull.getText());
						break;
					}
				case XmlPullParser.END_TAG:
					break;
				}
			}
		*/} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		return str;
	}
	
	/**
	 * 没有长连接的 解析
	 * @param url
	 * @param messageParam
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public String parseNokeep(String url,String messageParam)
			throws IOException, Exception {
		InputStream is = BaseHttpInputStream.getInstance().getInputStreamNoKeep(
				url, messageParam);
		String str=null;
		//DataResultBean dataResultBean = new DataResultBean();
		try {
			StringBuffer sb = new StringBuffer();
			byte b[] = new byte[1024];
			int i = 0;
			while (-1 != (i = is.read(b))) {
				sb.append(new String(b, 0, i, "utf-8"));
			}
			str=sb.toString();	
			/*
			XmlPullParser pull = Xml.newPullParser();
			pull.setInput(is, "UTF-8");
			for (int eventType = pull.next(); eventType != XmlPullParser.END_DOCUMENT ; eventType = pull.next()) {
				String name = pull.getName();
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if (name.equals("DataResult")) {
						dataResultBean.setResultCode(pull.getAttributeValue(null, "resultCode"));
						break;
					} else if (name.equals("ResultText")) {
						pull.next();
						dataResultBean.setResultText(pull.getText());
						break;
					}
				case XmlPullParser.END_TAG:
					break;
				}
			}
		*/} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		return str;
	}
	
	
}
