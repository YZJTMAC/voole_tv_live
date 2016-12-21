package com.vad.sdk.core.model.v30.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.vad.sdk.core.Utils.v30.HttpHelp;
import com.vad.sdk.core.Utils.v30.Lg;
import com.vad.sdk.core.base.AdRegister;
import com.vad.sdk.core.base.AdRegister.Adposinfo;

import android.text.TextUtils;

public class RegisterParser {
  /**
   * 需要开线程自己处理
   *
   * @param url
   * @return
   */
  public AdRegister requestApiData(String url) {
    Lg.i("RegisterParser , parseRawData() , url = " + url);
    AdRegister data = null;
    Adposinfo item = null;

    String xmlRawData = null;
    try {
      if (!TextUtils.isEmpty(url)) {
        xmlRawData = new HttpHelp().Get(url);
      }
      Lg.i("RegisterParser , parseRawData() , xmlRawData = " + xmlRawData);
      if (TextUtils.isEmpty(xmlRawData)) {
        return null;
      }
      XmlPullParser parser = getPullParser(xmlRawData);
      int eventType = parser.getEventType();
      while (eventType != XmlPullParser.END_DOCUMENT) {
        String tagName = parser.getName();
        switch (eventType) {
        case XmlPullParser.START_DOCUMENT:
          data = new AdRegister();
          item = data.getAdpos();
          break;
        case XmlPullParser.START_TAG:
          if ("status".equals(tagName)) {
            data.status = parser.nextText().trim();
          }
          if ("defaultreporturl".equals(tagName)) {
            data.defaultreporturl = parser.nextText().trim();
          }
          if ("defaultadinf".equals(tagName)) {
            data.defaultadinf = parser.nextText().trim();
          }
          if ("adparams".equals(tagName)) {
            data.adparams = parser.nextText().trim();
          }
          if ("adpos".equals(tagName)) {
            item.pos = parser.getAttributeValue(null, "pos");
            item.frequency = parser.getAttributeValue(null, "frequency");
            item.type = parser.getAttributeValue(null, "type");
            item.gtype = parser.getAttributeValue(null, "gtype");
          }
          break;
        case XmlPullParser.END_TAG:
          if ("adpos".equals(tagName)) {
            data.adposs.add(item);
            data.posIds.add(item.pos);
            item = data.getAdpos();
          }
          break;
        default:
          break;
        }
        eventType = parser.next();
      }
    } catch (XmlPullParserException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return data;
  }

  private XmlPullParser getPullParser(String xmlRawData) throws XmlPullParserException, IOException {
    XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xmlRawData.getBytes());
    parser.setInput(byteArrayInputStream, "UTF-8");
    byteArrayInputStream.close();
    return parser;
  }
}
