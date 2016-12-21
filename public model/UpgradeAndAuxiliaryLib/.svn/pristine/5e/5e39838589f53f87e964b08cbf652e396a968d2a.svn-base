package com.voole.android.client.UpAndAu.model.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.voole.android.client.UpAndAu.model.DataResult;
import com.voole.android.client.UpAndAu.model.FeedBackOptionInfo;
import com.voole.android.client.UpAndAu.model.Option;
import com.voole.android.client.UpAndAu.util.HttpUtil;
import com.voole.android.client.UpAndAu.util.StringUtil;

import android.util.Xml;



/**
 *反馈选项解析器
 * @version V1.0
 * @author wusong
 * @time 2013-4-17上午10:50:37
 */
public class FeedBackOptionParser extends CommonParser {

	/**
	 * 通过file解析 内部调用ParseFileByPull
	 * @param file File
	 * @return FeedBackOptionInfo
	 */
	public FeedBackOptionInfo ParseFile(File file) {
		return ParseFileByPull(file);
	}
	
	/**
	 *  通过url解析 内部调用ParseUrlByPull
	 * @param url String
	 * @return FeedBackOptionInfo
	 */
	public FeedBackOptionInfo ParseUrl(String url) {
		return ParseUrlByPull(url);
	}
	
	/**
	 * 通过file解析 
	 * @param file File
	 * @return FeedBackOptionInfo
	 */
	public FeedBackOptionInfo ParseFileByPull(File file) {
		FeedBackOptionInfo feedBackType = null;
		
		try {
			InputStream in = new FileInputStream(file);
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(in, "utf-8");
			feedBackType = ParseByPull(parser);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		}

		return feedBackType;
	}

	/**
	 *  通过url解析
	 * @param url String
	 * @return FeedBackOptionInfo
	 */
	public FeedBackOptionInfo ParseUrlByPull(String url) {
		FeedBackOptionInfo feedBackType = null;
		HttpUtil httpUtil = new HttpUtil();
		InputStream in = null;
		try {
			in = httpUtil.doGet(url, null, -1, -1, null);
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(in, HttpUtil.Encoding_UTF8);
			feedBackType = ParseByPull(parser);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		} finally {
			httpUtil.closeInputStream(in);
		}
		
		return feedBackType;
	}
	
	/**
	 * 通过输入流 解析
	 * @param in InputStream
	 * @return FeedBackOptionInfo
	 */
	public FeedBackOptionInfo ParseInByPull(InputStream in) {
		FeedBackOptionInfo feedBackType = null;
		HttpUtil httpUtil = new HttpUtil();
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(in, HttpUtil.Encoding_UTF8);
			feedBackType = ParseByPull(parser);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		} finally {
			httpUtil.closeInputStream(in);
		}
		
		return feedBackType;
	}
	
	/**
	 *  通过解析对象解析
	 * @param parser XmlPullParser
	 * @return FeedBackOptionInfo
	 */
	private FeedBackOptionInfo ParseByPull(XmlPullParser parser) {
		
		DataResult dataResult = null;
		FeedBackOptionInfo feedBackType = new FeedBackOptionInfo();
		String tagName;
		Option option = null;
		ArrayList<Option> optionArray = null;

		
		try {
			int eventType=parser.getEventType();
			
			while(eventType != XmlPullParser.END_DOCUMENT) {
				switch(eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					tagName = parser.getName();
					if(!"FeedBack".equals(tagName) && parser.getDepth() == 1) {
						return null;
					} else if("FeedBack".equals(tagName)) {
					} else if("DataResult".equals(tagName)) {
						dataResult = parseDataResultByPull(parser);
					} else if("ResultText".equals(tagName)) {
						dataResult.resultText = parser.nextText();
					} else if("OptionArray".equals(tagName)) {
						optionArray = new ArrayList<Option>();
					} else if("Option".equals(tagName)) {
						option = parseOptionByPull(parser);
					} else if("OptionText".equals(tagName)) {
						String text = parser.nextText();
					    option.text=StringUtil.isNotNull(text)?text.trim():"";
				}
					break;
				case XmlPullParser.END_TAG:
					tagName = parser.getName();
					if ("Option".equals(tagName)) {
						optionArray.add(option);
					} else if ("OptionArray".equals(tagName)) {
						feedBackType.optionArray = optionArray;
					} else if ("DataResult".equals(tagName)) {
						feedBackType.dataResult = dataResult;
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
		
		return feedBackType;
	}
}
