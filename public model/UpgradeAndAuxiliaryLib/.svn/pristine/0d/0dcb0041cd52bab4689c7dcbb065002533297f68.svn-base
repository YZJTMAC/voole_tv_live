package com.voole.android.client.UpAndAu.model.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.voole.android.client.UpAndAu.model.AboutInfo;
import com.voole.android.client.UpAndAu.model.DataResult;
import com.voole.android.client.UpAndAu.model.HelpInfo;
import com.voole.android.client.UpAndAu.model.Option;


/**
 * 基础的解析工具
 * @version V1.0
 * @author wusong
 * @time 2013-4-17上午10:39:21
 */
public class CommonParser {

	/**
	 *  通过解析对象,获取返回数据对象
	 * @param parser
	 * @return DataResult
	 */
	public DataResult parseDataResultByPull(XmlPullParser parser) {
		int attrCount = 0;
		String attrName = null;
		
		DataResult dataResult = new DataResult();
		attrCount = parser.getAttributeCount();
		for (int i = 0; i < attrCount; ++i) {
			attrName = parser.getAttributeName(i);
			if (attrName.equals("resultCode")) {
				dataResult.resultCode = parser.getAttributeValue(i);
			}
		}
		
		return dataResult;
	}
	/**
	 * 通过解析对象,获取返回数据的map集合
	 * @param parser XmlPullParser
	 * @return map   Map<String, String>
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public Map<String, String> parseMapByPull(XmlPullParser parser)throws XmlPullParserException, IOException  {
		Map<String, String>  map = new HashMap<String, String>();
		int attrCount = 0;
		String attrName = null;
		attrCount = parser.getAttributeCount();
		for (int i=0; i < attrCount; ++i) {
			attrName = parser.getAttributeName(i);
			String value=parser.getAttributeValue(i);
			map.put(attrName, value);
		}
		return map;
	}
	/**
	 * 通过解析对象,解析出选项对象 Option
	 * @param parser XmlPullParser
	 * @return Option
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public Option parseOptionByPull(XmlPullParser parser) throws XmlPullParserException, IOException {
		Option option = new Option();
		int attrCount = 0;
		String attrName = null;
		attrCount = parser.getAttributeCount();
		for (int i=0; i < attrCount; ++i) {
			attrName = parser.getAttributeName(i);
			if (attrName.equals("code"))
				option.code = parser.getAttributeValue(i);
		}
		return option;
	}
	
	/**
	 *  通过解析对象 解析出aboutInfo 
	 * @param parser XmlPullParser
	 * @return AboutInfo
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public AboutInfo parseAboutAttrsByPull(XmlPullParser parser) throws XmlPullParserException, IOException {
		AboutInfo aboutInfo = new AboutInfo();
		int attrCount = 0;
		String attrName = null;
		attrCount = parser.getAttributeCount();
		for (int i=0; i < attrCount; ++i) {
			attrName = parser.getAttributeName(i);
			if (attrName.equals("type"))
				aboutInfo.setType(parser.getAttributeValue(i));
		}
		return aboutInfo;
	}
	/**
	 * 通过解析对象 解析出 HelpInfo对象
	 * @param parser XmlPullParser
	 * @return HelpInfo
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public HelpInfo parseHelpAttrsByPull(XmlPullParser parser) throws XmlPullParserException, IOException {
		HelpInfo helpInfo = new HelpInfo();
		int attrCount = 0;
		String attrName = null;
		attrCount = parser.getAttributeCount();
		for (int i=0; i < attrCount; ++i) {
			attrName = parser.getAttributeName(i);
			if (attrName.equals("type"))
				helpInfo.setType(parser.getAttributeValue(i));
		}
		return helpInfo;
	}
}
