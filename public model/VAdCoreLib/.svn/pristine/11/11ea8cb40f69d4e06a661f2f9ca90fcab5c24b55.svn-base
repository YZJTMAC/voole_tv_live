package com.vad.sdk.core.base;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MediaInfo implements Serializable {
  private String viewtype;// 显示类型,0:不显示,1:视频,2:图片,3:文字,4:网页,5:接口
  // NOTE(ljs):SourceType=1时此处是一个片单链接,需要二次解析
  private String sourcetype;
  private String amid;
  private String name;
  private String length;// 广告展示时间
  private String starttime;// 广告延迟展示时间

  private String source;// url路径
  private String mediapos;// 九宫格位置
  private String width;// 宽
  private String height;// 高
  private String namepos;//
  private String content;

  private String reportvalue;// 汇报值,当广告为排期是此字段不为空
  // NOTE(ljs):二级跳转
  // 此段位有可能在MediaInfo的下一级,也有可能在innermedia的同级;
  // 但是xml解析器只解析开始结点,层级关系不会影响数据问题
  private String skiptype;// 跳转类型
  private String keyvalue;
  private String tips;
  private String url;// url
  private String apkinfo;// 包信息,样例:<![CDATA[ {"intentMid":"?","fromApp":"com.voole.magictv"} ]]>
  private String pkgname;// 包名
  private String action;// 包的action
  private String activity;// 跳转页
  // NOTE(ljs):内嵌广告
  private String innerstarttime;
  private String innerlength;// 内嵌广告中只有一个广告,
  private String innerviewtype;// 内嵌显示类型
  private String innerSourceType;
  private String innername;
  private String innersource;// 内嵌url路径
  private String innermediapos;// 内嵌九宫格位置
  private String innerwidth;// 内嵌图片宽
  private String innerheight;// 内嵌图片高
  private String innernamepos;
  private String inneramid;
  // NOTE(ljs):二次解析时影片专用属性
  private String mid;// 专门用来解析影片的
  private boolean mIsFilm = false;// 该MediaInfo是不是片单接口解析而成的

  public String getInneramid() {
    return inneramid;
  }

  public void setInneramid(String inneramid) {
    this.inneramid = inneramid;
  }

  public String getMid() {
    return mid;
  }

  public void setMid(String mid) {
    this.mid = mid;
  }

  public String getPkgname() {
    return pkgname;
  }

  public void setPkgname(String pkgname) {
    this.pkgname = pkgname;
  }

  public String getApkinfo() {
    return apkinfo;
  }

  public void setApkinfo(String apkinfo) {
    this.apkinfo = apkinfo;
  }

  public String getKeyvalue() {
    return keyvalue;
  }

  public void setKeyvalue(String keyvalue) {
    this.keyvalue = keyvalue;
  }

  public String getTips() {
    return tips;
  }

  public void setTips(String tips) {
    this.tips = tips;
  }

  public String getWidth() {
    return width;
  }

  public void setWidth(String width) {
    this.width = width;
  }

  public String getHeight() {
    return height;
  }

  public void setHeight(String height) {
    this.height = height;
  }

  public String getNamepos() {
    return namepos;
  }

  public void setNamepos(String namepos) {
    this.namepos = namepos;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getMediapos() {
    return mediapos;
  }

  public void setMediapos(String mediapos) {
    this.mediapos = mediapos;
  }

  private String innercontent;

  public String getStarttime() {
    return starttime;
  }

  public void setStarttime(String starttime) {
    this.starttime = starttime;
  }

  public String getInnercontent() {
    return innercontent;
  }

  public void setInnercontent(String innercontent) {
    this.innercontent = innercontent;
  }

  public String getInnername() {
    return innername;
  }

  public void setInnername(String innername) {
    this.innername = innername;
  }

  public String getInnerviewtype() {
    return innerviewtype;
  }

  public void setInnerviewtype(String innerviewtype) {
    this.innerviewtype = innerviewtype;
  }

  public String getInnerstarttime() {
    return innerstarttime;
  }

  public void setInnerstarttime(String innerstarttime) {
    this.innerstarttime = innerstarttime;
  }

  public String getInnerlength() {
    return innerlength;
  }

  public void setInnerlength(String innerlength) {
    this.innerlength = innerlength;
  }

  public String getInnermediapos() {
    return innermediapos;
  }

  public void setInnermediapos(String innermediapos) {
    this.innermediapos = innermediapos;
  }

  public String getInnerwidth() {
    return innerwidth;
  }

  public void setInnerwidth(String innerwidth) {
    this.innerwidth = innerwidth;
  }

  public String getInnerheight() {
    return innerheight;
  }

  public void setInnerheight(String innerheight) {
    this.innerheight = innerheight;
  }

  public String getInnernamepos() {
    return innernamepos;
  }

  public void setInnernamepos(String innernamepos) {
    this.innernamepos = innernamepos;
  }

  public String getInnersource() {
    return innersource;
  }

  public void setInnersource(String innersource) {
    this.innersource = innersource;
  }

  public String getAmid() {
    return amid;
  }

  public void setAmid(String amid) {
    this.amid = amid;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getActivity() {
    return activity;
  }

  public void setActivity(String activity) {
    this.activity = activity;
  }

  public String getSkiptype() {
    return skiptype;
  }

  public void setSkiptype(String skiptype) {
    this.skiptype = skiptype;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getViewtype() {
    return viewtype;
  }

  public void setViewtype(String viewtype) {
    this.viewtype = viewtype;
  }

  public String getSourcetype() {
    return sourcetype;
  }

  public void setSourcetype(String sourcetype) {
    this.sourcetype = sourcetype;
  }

  public String getLength() {
    return length;
  }

  public void setLength(String length) {
    this.length = length;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getReportvalue() {
    return reportvalue;
  }

  public void setReportvalue(String reportvalue) {
    this.reportvalue = reportvalue;
  }

  public String getInnerSourceType() {
    return innerSourceType;
  }

  public void setInnerSourceType(String innerSourceType) {
    this.innerSourceType = innerSourceType;
  }

  public boolean isFilm() {
    return mIsFilm;
  }

  public void setIsFilm(boolean isfilm) {
    mIsFilm = isfilm;
  }

  @Override
  public String toString() {
    return "{viewtype=" + viewtype + ", sourcetype=" + sourcetype + ", length=" + length + ", name=" + name + ", source=" + source + ", reportvalue=" + reportvalue + "}";
  }
}
