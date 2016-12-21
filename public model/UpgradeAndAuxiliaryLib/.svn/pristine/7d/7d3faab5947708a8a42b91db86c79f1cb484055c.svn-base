package com.voole.android.client.UpAndAu.model.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.voole.android.client.UpAndAu.model.AboutInfo;
import com.voole.android.client.UpAndAu.model.DataResult;
import com.voole.android.client.UpAndAu.util.HttpUtil;

import android.util.Xml;


/**
 * 
 *  关于信息解析
 * @version 1.0
 * @author LEE
 * @date 2013-4-3 下午2:34:07 
 * @update 2013-4-3 下午2:34:07
 */
public class AboutInfoParser extends CommonParser {

	/**
	 *  解析文件 
	 * @param file
	 * @return AboutInfo
	 * 
	 * 
	 */
	public AboutInfo ParseFile(File file) {
		return ParseFileByPull(file);
	}
	
	/**
	 * 解析通过url地址
	 * @param url
	 * @return AboutInfo
	 * 
	 * 
	 */
	public AboutInfo ParseUrl(String url) {
		return ParseUrlByPull(url);
	}
	
	/**
	 *  从文件解析
	 * @param file
	 * @return AboutInfo
	 * 
	 * 
	 */
	public AboutInfo ParseFileByPull(File file) {
		AboutInfo aboutInfo = null;
		
		try {
			InputStream in = new FileInputStream(file);
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(in, "utf-8");
			aboutInfo = ParseByPull(parser);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		}

		return aboutInfo;
	}

	/**
	 *  从url链接解析
	 * @param url
	 * @return AboutInfo
	 * 
	 * 
	 */
	public AboutInfo ParseUrlByPull(String url) {
		AboutInfo aboutInfo = null;
		HttpUtil httpUtil = new HttpUtil();
		InputStream in = null;
		try {
			in = httpUtil.doGet(url, null, -1, -1, null);
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(in, HttpUtil.Encoding_UTF8);
			aboutInfo = ParseByPull(parser);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		} finally {
			httpUtil.closeInputStream(in);
		}
		
		return aboutInfo;
	}
	
	/**
	 *  通过输入流解析
	 * @param in InputStream
	 * @return AboutInfo
	 * 
	 * 
	 */
	public AboutInfo ParseInByPull(InputStream in) {
		AboutInfo aboutInfo = null;
		HttpUtil httpUtil = new HttpUtil();
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(in, HttpUtil.Encoding_UTF8);
			aboutInfo = ParseByPull(parser);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		} finally {
			httpUtil.closeInputStream(in);
		}
		
		return aboutInfo;
	}
	
	/**
	 *  通过解析对象解析
	 * @param parser XmlPullParser
	 * @return AboutInfo
	 */
	private AboutInfo ParseByPull(XmlPullParser parser) {
		
		DataResult dataResult = null;
		AboutInfo aboutInfo = null;
		String tagName;
		try {
			int eventType=parser.getEventType();
			
			while(eventType != XmlPullParser.END_DOCUMENT) {
				switch(eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					tagName = parser.getName();
					if(!"About".equals(tagName) && parser.getDepth() == 1) {
						return null;
					} else if("About".equals(tagName)) {
						aboutInfo = parseAboutAttrsByPull(parser);
					} else if("DataResult".equals(tagName)) {
						dataResult = parseDataResultByPull(parser);
					} else if("ResultText".equals(tagName)) {
						dataResult.resultText = parser.nextText();
					} else if("Introduction".equals(tagName)) {
						aboutInfo.setIntroduction(parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					tagName = parser.getName();
					if ("DataResult".equals(tagName)) {
						aboutInfo.setDataResult(dataResult);
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
		
		return aboutInfo;
	}
}
