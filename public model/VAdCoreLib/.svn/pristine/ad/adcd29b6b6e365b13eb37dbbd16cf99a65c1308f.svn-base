package com.vad.sdk.core.report.v30;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.vad.sdk.core.Utils.v30.Lg;
import android.annotation.SuppressLint;

public class ReportManager {
  public class Position {
    public static final String Boot = "1";
    public static final String ApkStart = "2";
    public static final String Navigation = "3";
    public static final String Poster = "4";
    public static final String VideoView = "5";
    public static final String Paster = "6";
    public static final String Playercontrolbar = "7";
    public static final String Floatinglayer = "8";
  }

  public static final String Start = "0";
  public static final String Stop = "1";

  private static ReportManager reportManager = new ReportManager();

  public static ReportManager getInstance() {
    return reportManager;
  }

  /**
   * @param context
   * @param Reportvalue
   *          请求值
   * @param uv
   *          用户交互
   * @param type
   *          start or end 状态
   * @param mReportUrl
   *          汇报地址
   * @param posid
   *          汇报类型,17 15等
   */
  @SuppressLint("SimpleDateFormat")
  public void report(final String Reportvalue, final int uv, final String type, final String mReportUrl, String posid) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    String StartDate = simpleDateFormat.format(new Date());
    if (posid.length() > 2) {
      posid = posid.substring(0, 2);
    }
    String mUrl = mReportUrl.replace("{bigtype}", posid);
    mUrl = mUrl.replace("<uv>", String.valueOf(uv));
    mUrl = mUrl.replace("<type>", type);
    mUrl = mUrl.replace("<starttime>", StartDate);
    String sessionid = creatSessionId();
    mUrl = mUrl.replace("<sessionid>", sessionid);
    mUrl = mUrl + "&" + Reportvalue;
    Lg.d("ReportManager , report() , mUrl------------->" + mUrl);
    com.voole.statistics.report.ReportManager.getInstance().doGetReport(mUrl);
    // com.gntv.report.manager.ReportManager.getInstance().reportPlayer(mUrl);
  }

  // public void init(Context context) {
  // // NOTE(ljs):ReportManager上层已初始化,在导流不需要初始化
  // // com.voole.statistics.report.ReportManager.getInstance().init(baseInfo);
  // // com.gntv.report.manager.ReportManager.getInstance().init(context);
  // }

  private String creatSessionId() {
    long curTime = System.currentTimeMillis();
    Random random = new Random();
    int randomInt = random.nextInt(10000000);
    return ("" + curTime + randomInt);
  }
}
