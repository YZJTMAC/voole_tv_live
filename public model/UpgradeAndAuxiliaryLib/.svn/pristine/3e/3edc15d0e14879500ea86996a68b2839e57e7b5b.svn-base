package com.voole.android.client.UpAndAu.model.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.voole.android.client.UpAndAu.model.DataResult;
import com.voole.android.client.UpAndAu.model.HelpInfo;
import com.voole.android.client.UpAndAu.util.HttpUtil;


/**
 * 
 *  帮助信息解析
 * @version 1.0
 * @author LEE
 * @date 2013-4-3 下午2:34:07 
 * @update 2013-4-3 下午2:34:07
 */
public class HelpInfoParser extends CommonParser {

	/**
	 *  通过文件 解析HelpInfo内部调用ParseFileByPull
	 * @param file  File
	 * @return HelpInfo
	 */
	public HelpInfo ParseFile(File file) {
		return ParseFileByPull(file);
	}
	
	/**
	 *  通过url解析 内部调用了 ParseUrlByPull(String url)
	 * @param url
	 * @return HelpInfo
	 */
	public HelpInfo ParseUrl(String url) {
		return ParseUrlByPull(url);
	}
	
	/**
	 *  通过文件解析
	 * @param file
	 * @return HelpInfo
	 */
	public HelpInfo ParseFileByPull(File file) {
		HelpInfo helpInfo = null;
		
		try {
			InputStream in = new FileInputStream(file);
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(in, "utf-8");
			helpInfo = ParseByPull(parser);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		}

		return helpInfo;
	}

	/**
	 *  通过url解析
	 * @param url
	 * @return HelpInfo
	 */
	public HelpInfo ParseUrlByPull(String url) {
		HelpInfo helpInfo = null;
		HttpUtil httpUtil = new HttpUtil();
		InputStream in = null;
		try {
			in = httpUtil.doGet(url, null, -1, -1, null);
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(in, HttpUtil.Encoding_UTF8);
			helpInfo = ParseByPull(parser);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		} finally {
			httpUtil.closeInputStream(in);
		}
		
		return helpInfo;
	}
	
	/**
	 *  通过输入流解析 
	 * @param in
	 * @return HelpInfo
	 */
	public HelpInfo ParseInByPull(InputStream in) {
		HelpInfo helpInfo = null;
		HttpUtil httpUtil = new HttpUtil();
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(in, HttpUtil.Encoding_UTF8);
			helpInfo = ParseByPull(parser);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		} finally {
			httpUtil.closeInputStream(in);
		}
		
		return helpInfo;
	}
	
	/**
	 * 通过解析对象解析 
	 * @param parser
	 * @return HelpInfo
	 */
	private HelpInfo ParseByPull(XmlPullParser parser) {
		
		DataResult dataResult = null;
		HelpInfo helpInfo = null;
		String tagName;
		try {
			int eventType=parser.getEventType();
			
			while(eventType != XmlPullParser.END_DOCUMENT) {
				switch(eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					tagName = parser.getName();
					if(!"Help".equals(tagName) && parser.getDepth() == 1) {
						return null;
					} else if("Help".equals(tagName)) {
						helpInfo = parseHelpAttrsByPull(parser);
					} else if("DataResult".equals(tagName)) {
						dataResult = parseDataResultByPull(parser);
					} else if("ResultText".equals(tagName)) {
						dataResult.resultText = parser.nextText();
					} else if("Introduction".equals(tagName)) {
						helpInfo.setIntroduction(parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					tagName = parser.getName();
					if ("DataResult".equals(tagName)) {
						helpInfo.setDataResult(dataResult);
					} 
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
		}
		
		return helpInfo;
	}
}
