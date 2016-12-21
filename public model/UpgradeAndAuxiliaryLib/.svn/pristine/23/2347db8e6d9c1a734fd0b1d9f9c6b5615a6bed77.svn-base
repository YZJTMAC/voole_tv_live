package com.voole.android.client.UpAndAu.model.parser;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.voole.android.client.UpAndAu.model.DataResult;
import com.voole.android.client.UpAndAu.util.HttpUtil;
import com.voole.android.client.UpAndAu.util.Logger;

import android.util.Xml;

/**
 * DataResult信息
 * 
 * @version 1.0
 * @author 吕振威
 * @date 2012-3-27 下午3:05:15
 * @update 2012-3-27 下午3:05:15
 */

public class DataResultParser extends CommonParser {

	/**
	 * 通过url解析 内部调用ParseUrlByPull
	 * 
	 * @param url
	 *            String
	 * @return DataResult
	 */
	public DataResult ParseUrl(String url) {
		return ParseUrlByPull(url);
	}

	/**
	 * 通过url路径解析
	 * 
	 * @param url
	 *            String
	 * @return DataResult
	 */
	public DataResult ParseUrlByPull(String url) {
		DataResult dataResult = null;
		InputStream in = null;
		HttpUtil httpUtil = new HttpUtil();
		try {
			in = httpUtil.doGet(url, null, -1, -1, null);
			// String result = StringUtil.convertStreamToString(in);
			// Logger.debug("DataResultParser", result);
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(in, HttpUtil.Encoding_UTF8);
			dataResult = ParseByPull(parser);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		} finally {
			httpUtil.closeInputStream(in);
		}

		return dataResult;
	}

	/**
	 * 通过输入流 解析
	 * 
	 * @param in
	 *            InputStream
	 * @return DataResult
	 */
	public DataResult ParseInputStreamByPull(InputStream in) {
		DataResult dataResult = null;

		try {

			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(in, "utf-8");
			dataResult = ParseByPull(parser);
			in.close();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return dataResult;
	}

	/**
	 * 通过解析对象 解析
	 * 
	 * @param parser
	 *            XmlPullParser
	 * @return DataResult
	 */
	public DataResult ParseByPull(XmlPullParser parser) {
		DataResult dataResult = new DataResult();
		String tagName;

		try {
			int eventType = parser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					tagName = parser.getName();
					if ("DataResult".equals(tagName)) {
						dataResult = parseDataResultByPull(parser);
					} else if ("ResultText".equals(tagName)) {
						dataResult.resultText = parser.nextText();
					}
					break;
				case XmlPullParser.END_TAG:
					tagName = parser.getName();
					break;
				}

				eventType = parser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return dataResult;

	}

}
