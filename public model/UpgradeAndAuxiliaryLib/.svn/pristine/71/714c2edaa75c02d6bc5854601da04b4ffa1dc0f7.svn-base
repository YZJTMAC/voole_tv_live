package com.voole.android.client.UpAndAu.model.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.voole.android.client.UpAndAu.util.HttpUtil;

/**
 * 接口地址解析器
 * @version V1.0
 * @author wusong
 * @time 2013-4-17上午10:55:52
 */
public class IntfaceUrlParser extends CommonParser {
	/**
	 *  通过给定的公司服务入口接口地址解析所有业务的入口地址
	 * @param url
	 *            String 公司服务入口地址
	 * @return List<Map<String, String>> map 的key 包括"key","name""url"
	 *         "key"代表所指业务接口名称
	 * 
	 */
	public List<Map<String, String>> ParseUrlByPull(String url) {
		List<Map<String, String>> urlList = new ArrayList<Map<String, String>>();
		HttpUtil httpUtil = new HttpUtil();
		InputStream in = null;
		try {
			in = httpUtil.doGet(url, null, -1, -1, null);
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(in, HttpUtil.Encoding_UTF8);
			urlList = ParseByPull(parser);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		} finally {
			httpUtil.closeInputStream(in);
		}

		return urlList;
	}

	/**
	 * 通过解析对象
	 * @param parser
	 *            XmlPullParser
	 * @return List<Map<String, String>>
	 * 
	 * 
	 */
	private List<Map<String, String>> ParseByPull(XmlPullParser parser) {

		List<Map<String, String>> urlList = null;
		Map<String, String> map = null;
		String tagName;

		try {
			int eventType = parser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					tagName = parser.getName();
					if (!"url ".equals(tagName) && parser.getDepth() == 1) {
						return null;
					} else if ("url ".equals(tagName)) {
						urlList = new ArrayList<Map<String, String>>();
					} else if ("urllist ".equals(tagName)) {
						map = parseMapByPull(parser);
						map.put("url", parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					tagName = parser.getName();
					if ("urllist".equals(tagName)) {
						urlList.add(map);
						break;
					}
					eventType = parser.next();
				}
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return urlList;
	}
}
