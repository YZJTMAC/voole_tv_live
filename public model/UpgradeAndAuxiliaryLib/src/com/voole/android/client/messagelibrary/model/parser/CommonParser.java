package com.voole.android.client.messagelibrary.model.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.voole.android.client.messagelibrary.model.DataResult;



/**
 * 基础的解析工具
 * @version V1.0
 * @author wujian
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
	
	

}
