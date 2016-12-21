package com.voole.android.client.UpAndAu.model.parser;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.voole.android.client.UpAndAu.model.DataResult;
import com.voole.android.client.UpAndAu.model.UpdateInfo;
import com.voole.android.client.UpAndAu.upgrade.AppUpgradeAuxiliaryer;
import com.voole.android.client.UpAndAu.util.HttpUtil;


/**
 * 解析版本升级信息
 * @author LEE
 * 
 *
 */
public class UpdateInfoParser extends CommonParser{
	
	public UpdateInfo ParseUrl(String url) {
		return ParseUrlByPull(url);
	}
	
	/**
	 *  通过url 解析信息
	 * @param url String
	 * @return UpdateInfo
	 * 
	 * 
	 */
	private UpdateInfo ParseUrlByPull(String url) {
		UpdateInfo updateInfo = null;
		HttpUtil httpUtil = new HttpUtil();
		InputStream in = null;
		try {
			in = httpUtil.doGet(url, null, AppUpgradeAuxiliaryer.httpConnTimeOut, AppUpgradeAuxiliaryer.httpReadTimeOut, null);
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(in, HttpUtil.Encoding_UTF8);
			updateInfo = ParseByPull(parser);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			httpUtil.closeInputStream(in);
		}
		
		return updateInfo;
	}
	
	
	/**
	 *  通过解析对象解析
	 * @param parser XmlPullParser
	 * @return UpdateInfo
	 *  
	 * 
	 */
	private UpdateInfo ParseByPull(XmlPullParser parser) {
		UpdateInfo updateInfo = null;
		String tagName;
		DataResult dataResult = null;
		try {
			int eventType=parser.getEventType();
			
			while(eventType != XmlPullParser.END_DOCUMENT) {
				switch(eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					tagName = parser.getName();
					if(!"UpdateInfo".equals(tagName) && parser.getDepth() == 1) {
						return null;
					} else if("UpdateInfo".equals(tagName)) {
						updateInfo = parseUpdateInfoAttributes(parser);
					} else if("DataResult".equals(tagName)) {
						dataResult = parseDataResultByPull(parser);
					} else if("ResultText".equals(tagName)) {
						dataResult.resultText = parser.nextText();
					} else if("DownloadUrl".equals(tagName)) {
						updateInfo.setDownloadUrl(parser.nextText());
					} else if("DetailUrl".equals(tagName)) {
						updateInfo.setDetailUrl(parser.nextText());
					} else if("Introduction".equals(tagName)) {
						updateInfo.setIntroduction(parser.nextText());
					} 
					break;
				case XmlPullParser.END_TAG:
					tagName = parser.getName();
					if ("DataResult".equals(tagName)) {
						updateInfo.setDataResult(dataResult);
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
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return updateInfo;
	}
	
	/**
	 *  解析升级信息属性
	 * @param parser XmlPullParser
	 * @return  UpdateInfo
	 */
	private UpdateInfo parseUpdateInfoAttributes(XmlPullParser parser) {
		String attrName;
		int attrCount = 0;
		
		UpdateInfo updateInfo = new UpdateInfo();
		attrCount = parser.getAttributeCount();
		for (int i = 0; i < attrCount; ++i) {
			attrName = parser.getAttributeName(i);
			if (attrName.equals("updateAvailabe")) {
				updateInfo.setUpdateAvailabe(parser.getAttributeValue(i));
			} else if(attrName.equals("currentVersion")) {
				updateInfo.setCurrentVersion(parser.getAttributeValue(i));
			} else if(attrName.equals("updateStyle")) {
				updateInfo.setUpdateStyle(parser.getAttributeValue(i));
			} else if(attrName.equals("fid")) {
				updateInfo.setFid(parser.getAttributeValue(i));
			} else if(attrName.equals("updateTime")) {
				updateInfo.setUpdateTime(parser.getAttributeValue(i));
			} else if(attrName.equals("appSize")) {
				updateInfo.setAppSize(parser.getAttributeValue(i));
			} else if(attrName.equals("requiredSdk")) {
				updateInfo.setRequiredSdk(parser.getAttributeValue(i));
			} else if(attrName.equals("sendtime")) {
				updateInfo.setSendtime(parser.getAttributeValue(i));
			} else if(attrName.equals("appid")) {
				updateInfo.setAppid(parser.getAttributeValue(i));
			} else if(attrName.equals("appname")) {
				updateInfo.setAppname(parser.getAttributeValue(i));
			} else if(attrName.equals("packagename")) {
				updateInfo.setPackageName(parser.getAttributeValue(i));
			}
			
			
		}
		return updateInfo;
	}

}
