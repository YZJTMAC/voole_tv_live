/**
 *
 */
package com.vad.sdk.core.model.v30.parser;

import com.vad.sdk.core.Utils.v30.HttpHelp;
import com.vad.sdk.core.Utils.v30.Lg;
import com.vad.sdk.core.base.AdInfo;
import com.vad.sdk.core.base.AdPos;
import com.vad.sdk.core.base.MediaInfo;

import android.text.TextUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * parser.getName(); //获得当前指向的标签的标签名 <br>
 * parser.nextText(); //获得当前标签的标签值<br>
 * parser.getAttributeValue(0); //获得当前指向的标签的第1个属性值 <br>
 * parser.getAttributeValue(String namespace,String name);//Namespace of the attribute if namespaces
 * are enabled otherwise must be null
 *
 * @author luojunsheng
 * @date 2016年5月5日 下午2:02:19
 * @version 1.1
 */
public class ApiDataParser {
  String mReportUrl = null;

  public void setReportUrl(String reportUrl) {
    mReportUrl = reportUrl;
  }

  /**
   * 异步解析数据
   *
   * @param xmlRawData
   * @param listener
   */
  public void asynGetApiData(final String xmlRawData, final ApiResponseListener<AdInfo> listener) {
    new Thread() {
      @Override
      public void run() {
        if (null != listener) {
          listener.onApiCompleted(parseRawData(xmlRawData));
        }
      };
    }.start();
  }

  /**
   * 异步请求数据并解析
   *
   * @param url
   * @param listener
   */
  public void asynRequestApiData(final String url, final ApiResponseListener<AdInfo> listener) {
    Lg.i("ApiDataParser , asynRequestApiData() , url = " + url);
    new Thread() {
      @Override
      public void run() {
        String xmlRawData = null;
        try {
          xmlRawData = new HttpHelp().Get(url);
        } catch (IOException e) {
          e.printStackTrace();
        }
        if (null != listener) {
          listener.onApiCompleted(parseRawData(xmlRawData));
        }
      };
    }.start();
  }

  /**
   * 同步解析数据
   *
   * @param xmlRawData
   * @param listener
   */
  public void synGetApiData(final String xmlRawData, final ApiResponseListener<AdInfo> listener) {
    listener.onApiCompleted(parseRawData(xmlRawData));
  }

  public AdInfo synGetApiData(final String xmlRawData) {
    return parseRawData(xmlRawData);
  }

  /**
   * 同步请求数据
   *
   * @param url
   * @param listener
   */
  public void synRequestApiData(final String url, final ApiResponseListener<AdInfo> listener) {
    String xmlRawData = null;
    try {
      xmlRawData = new HttpHelp().Get(url);
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (null != listener) {
      listener.onApiCompleted(parseRawData(xmlRawData));
    }
  }

  private XmlPullParser getPullParser(String xmlRawData) throws XmlPullParserException, IOException {
    XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xmlRawData.getBytes());
    parser.setInput(byteArrayInputStream, "UTF-8");
    byteArrayInputStream.close();
    return parser;
  }

  /**
   * 解析数据,注意:有二次解析的情况,会有HTTP请求耗时操作
   *
   * @param xmlRawData
   * @return
   */
  private AdInfo parseRawData(String xmlRawData) {
    Lg.i("ApiDataParser , parseRawData() , xmlRawData = " + xmlRawData);
    if (TextUtils.isEmpty(xmlRawData)) {
      return null;
    }
    AdInfo adInfo = null;
    AdPos adPos = null;
    MediaInfo mediaInfo = null;
    List<MediaInfo> mediaInfoItems = null;
    ByteArrayInputStream baips = null;
    try {
      XmlPullParser parser = getPullParser(xmlRawData);

      int eventType = parser.getEventType();

      while (eventType != XmlPullParser.END_DOCUMENT) {
        String tagName = parser.getName();
        switch (eventType) {
        case XmlPullParser.START_DOCUMENT:// 文档开始
          adInfo = new AdInfo();
          adPos = new AdPos();
          mediaInfo = new MediaInfo();
          mediaInfoItems = new ArrayList<MediaInfo>();
          mediaInfoItems.clear();
          break;
        case XmlPullParser.START_TAG:// 开始节点
          if ("status".equals(tagName)) {
            adInfo.status = parser.nextText().trim();
          }
          if ("resultdesc".equals(tagName)) {
            adInfo.resultDesc = parser.nextText().trim();// 有时数据会放在CDATA区中
          }

          if ("adinfo".equals(tagName)) {
            adInfo.version = parser.getAttributeValue(null, "version");
            adInfo.cdnType = parser.getAttributeValue(null, "cdntype");
          }
          if ("adpos".equals(tagName)) {
            if (!TextUtils.isEmpty(mReportUrl)) {
              adPos.reportUrl = mReportUrl;
            }
            String idTmp = parser.getAttributeValue(null, "pos");
            adPos.id = idTmp;
            adPos.startTime = parser.getAttributeValue(null, "starttime");
            adPos.allLength = parser.getAttributeValue(null, "alllength");
            adPos.indexId = Integer.parseInt(idTmp.substring(idTmp.length() - 2, idTmp.length())) - 9;
          }
          if ("mediainfo".equals(tagName)) {
            mediaInfo.setSourcetype(parser.getAttributeValue(null, "sourcetype"));
            mediaInfo.setName(parser.getAttributeValue(null, "name"));
            mediaInfo.setViewtype(parser.getAttributeValue(null, "viewtype"));
            mediaInfo.setLength(parser.getAttributeValue(null, "length"));
            mediaInfo.setStarttime(parser.getAttributeValue(null, "starttime"));
            mediaInfo.setAmid(parser.getAttributeValue(null, "amid"));
          }
          if ("source".equals(tagName)) {
            mediaInfo.setWidth(parser.getAttributeValue(null, "width"));
            mediaInfo.setHeight(parser.getAttributeValue(null, "height"));
            mediaInfo.setContent(parser.getAttributeValue(null, "content"));
            mediaInfo.setNamepos(parser.getAttributeValue(null, "namepos"));
            mediaInfo.setMediapos(parser.getAttributeValue(null, "mediapos"));
            // NOTE(ljs):必须把属性信息解析完成再parser.nextText()
            mediaInfo.setSource(parser.nextText().trim());
          }
          if ("reportvalue".equals(tagName)) {
            mediaInfo.setReportvalue(parser.nextText().trim());
          }
          if ("skipinfo".equals(tagName)) {
            mediaInfo.setSkiptype(parser.getAttributeValue(null, "skiptype"));
            mediaInfo.setKeyvalue(parser.getAttributeValue(null, "keyvalue"));
            mediaInfo.setTips(parser.getAttributeValue(null, "tips"));
          }
          if ("url".equals(tagName)) {
            mediaInfo.setUrl(parser.nextText().trim());
          }
          if ("apkinfo".equals(tagName)) {
            mediaInfo.setPkgname(parser.getAttributeValue(null, "pkgname"));
            mediaInfo.setAction(parser.getAttributeValue(null, "action"));
            mediaInfo.setActivity(parser.getAttributeValue(null, "activity"));
            // NOTE(ljs):必须把属性信息解析完成再parser.nextText()
            mediaInfo.setApkinfo(parser.nextText().trim());
          }
          if ("innerpos".equals(tagName)) {
            mediaInfo.setInnerstarttime(parser.getAttributeValue(null, "starttime"));
            mediaInfo.setInnerlength(parser.getAttributeValue(null, "length"));
          }
          if ("innermedia".equals(tagName)) {
            mediaInfo.setInnerviewtype(parser.getAttributeValue(null, "viewtype"));
            mediaInfo.setInnerSourceType(parser.getAttributeValue(null, "sourcetype"));
            mediaInfo.setInnername(parser.getAttributeValue(null, "name"));
            // FIXME(ljs):innerAmid这个字段没有?
            mediaInfo.setInneramid(parser.getAttributeValue(null, "amid"));
          } else if ("innersource".equals(tagName)) {
            mediaInfo.setInnermediapos(parser.getAttributeValue(null, "mediapos"));
            mediaInfo.setInnercontent(parser.getAttributeValue(null, "content"));
            mediaInfo.setInnerwidth(parser.getAttributeValue(null, "width"));
            mediaInfo.setInnerheight(parser.getAttributeValue(null, "height"));
            mediaInfo.setInnernamepos(parser.getAttributeValue(null, "namepos"));
            // NOTE(ljs):必须把属性信息解析完成再parser.nextText()
            mediaInfo.setInnersource(parser.nextText().trim());
          }
          break;
        case XmlPullParser.END_TAG:// -------------------------结束节点
          if ("mediainfo".equals(tagName)) {
            if ("1".equals(mediaInfo.getSourcetype())) {
              // NOTE(ljs):注意:有二次解析的情况,会有HTTP请求耗时操作
              List<MediaInfo> parseFilmInfos = parseFilmInfos(mediaInfo.getSource(), mediaInfo);
              if (parseFilmInfos != null) {
                mediaInfoItems.addAll(parseFilmInfos);
              }
            } else {
              mediaInfoItems.add(mediaInfo);
            }
            mediaInfo = new MediaInfo();
          }

          if ("adpos".equals(tagName)) {
            adPos.mediaInfoList = mediaInfoItems;
            adInfo.adPostions.add(adPos);

            adPos = new AdPos();
            mediaInfoItems = new ArrayList<MediaInfo>();
            mediaInfoItems.clear();
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
    } finally {
      try {
        if (null != baips) {
          baips.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return adInfo;
  }

  /**
   * 当SourceType=1时,二次解析url中的片单列表
   *
   * 注意:有二次解析的情况,会有HTTP请求耗时操作
   *
   * @param url
   * @param mediaInfoSkip
   * @return
   * @throws IOException
   * @throws XmlPullParserException
   */
  private List<MediaInfo> parseFilmInfos(String url, MediaInfo mediaInfoSkip) throws IOException, XmlPullParserException {
    Lg.e("ApiDataParser , parseFilmInfos() , url = " + url);
    List<MediaInfo> mediaInfos = null;
    MediaInfo mediaInfo = null;
    if (!TextUtils.isEmpty(url)) {
      // 注意:有二次解析的情况,会有HTTP请求耗时操作
      String xmlRawData = new HttpHelp().Get(url);
      if (!TextUtils.isEmpty(xmlRawData)) {
        XmlPullParser parser = getPullParser(xmlRawData);
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
          String tagName = parser.getName();
          switch (eventType) {
          case XmlPullParser.START_DOCUMENT:
            mediaInfo = new MediaInfo();
            mediaInfos = new ArrayList<MediaInfo>();
            break;
          case XmlPullParser.START_TAG:
            if ("Film".equals(tagName)) {
              mediaInfo.setSource(parser.getAttributeValue(null, "ImgUrlB"));
              mediaInfo.setMid(parser.getAttributeValue(null, "Mid"));
            }
            if ("FilmName".equals(tagName)) {
              mediaInfo.setName(parser.nextText().trim());
            }
            break;
          case XmlPullParser.END_TAG:
            if ("Film".equals(tagName)) {
              if (mediaInfoSkip != null) {
                mediaInfo.setAction(mediaInfoSkip.getAction());
                mediaInfo.setSkiptype(mediaInfoSkip.getSkiptype());
                mediaInfo.setUrl(mediaInfoSkip.getUrl());
                mediaInfo.setPkgname(mediaInfoSkip.getPkgname());
                mediaInfo.setActivity(mediaInfoSkip.getActivity());
                mediaInfo.setKeyvalue(mediaInfoSkip.getKeyvalue());
                mediaInfo.setApkinfo(mediaInfoSkip.getApkinfo());
                mediaInfo.setReportvalue(mediaInfoSkip.getReportvalue());
              }
              mediaInfo.setIsFilm(true);
              mediaInfos.add(mediaInfo);
              mediaInfo = new MediaInfo();
            }
            break;
          default:
            break;
          }
          eventType = parser.next();
        }
      }
    }
    return mediaInfos;
  }

}
