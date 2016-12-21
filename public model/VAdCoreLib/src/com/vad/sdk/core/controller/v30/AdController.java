package com.vad.sdk.core.controller.v30;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.vad.sdk.core.VAdSDK;
import com.vad.sdk.core.VAdType;
import com.vad.sdk.core.Utils.v30.Lg;
import com.vad.sdk.core.base.AdInfo;
import com.vad.sdk.core.base.AdPos;
import com.vad.sdk.core.base.AdRegister;
import com.vad.sdk.core.base.AdRegister.Adposinfo;
import com.vad.sdk.core.base.MediaInfo;
import com.vad.sdk.core.base.interfaces.IAdController;
import com.vad.sdk.core.base.interfaces.IAdEpgOperationHandler;
import com.vad.sdk.core.base.interfaces.IAdHomeEpgOperationHandler;
import com.vad.sdk.core.base.interfaces.IAdPlayer;
import com.vad.sdk.core.base.interfaces.IAdPlayerController;
import com.vad.sdk.core.base.interfaces.IAdPlayerUIController;
import com.vad.sdk.core.base.interfaces.IAdStartupListener;
import com.vad.sdk.core.manager.MediaHandler;
import com.vad.sdk.core.model.v30.AdPosBootListener;
import com.vad.sdk.core.model.v30.parser.ApiDataParser;
import com.vad.sdk.core.model.v30.parser.ApiResponseListener;
import com.vad.sdk.core.model.v30.parser.RegisterParser;
import com.vad.sdk.core.report.v30.ReportManager;
import com.vad.sdk.core.view.v30.AdWebView;
import com.vad.sdk.core.view.v30.EpgExitView;
import com.vad.sdk.core.view.v30.EpgExitView.TimerText;
import com.vad.sdk.core.view.v30.ExitAdView;
import com.vad.sdk.core.view.v30.ExitAdView.OnItemClickListener;
import com.vad.sdk.core.view.v30.JavaScriptInterface.ExitListener;
import com.vad.sdk.core.view.v30.RecommendedADView;

public class AdController implements IAdController {
  // private static final String AD_INTERFACE_VERSION = "3.0.0";

  private IAdPlayerController mAdPlayerController = null;
  private IAdEpgOperationHandler mAdEpgOperationHandler = null;
  private IAdHomeEpgOperationHandler mAdHomeEpgOperationHandler = null;
  private MediaHandler handler = null;
  private AdRegister mRegister;
  private AdWebView mDialogWv;
  int n = 0;

  @Override
  public boolean register(String apkId) {
    return register(apkId, null, null);
  }

  @Override
  public boolean register(String apkId, String defaultadinf, String defaultreporturl) {
    Lg.d("AdController , register() , apkId = " + apkId);
    String url = null;
    if (apkId.length() > 4 && "http".equals(apkId.subSequence(0, 4))) {
      url = apkId;
      Uri uri = Uri.parse(url);
      VAdType.initAdPosId(uri.getQueryParameter("apkpid"));
    } else {
      VAdType.initAdPosId(apkId);
      StringBuilder sb = new StringBuilder();
      sb.append("http://127.0.0.1:" + VAdSDK.getInstance().getAuthPort());
      sb.append("/query?reqinfo=<req><data>{\"action\":\"adregister\",");
      sb.append("\"apkpid\":\"" + apkId + "\",");
      sb.append("\"version\":\"" + VAdType.AD_INTERFACE_VERSION + "\"}</data></req>");
      url = sb.toString();
    }

    if (mRegister == null) {
      mRegister = new RegisterParser().requestApiData(url);
    }
    if (mRegister != null && "0".equals(mRegister.status)) {
      if (!TextUtils.isEmpty(defaultadinf) && !TextUtils.isEmpty(mRegister.defaultadinf)) {
        mRegister.defaultadinf = mRegister.defaultadinf.replaceFirst("\\/\\/[^:|/]+", "//" + defaultadinf);
      }
      if (!TextUtils.isEmpty(defaultreporturl) && !TextUtils.isEmpty(mRegister.defaultreporturl)) {
        mRegister.defaultreporturl = mRegister.defaultreporturl.replaceFirst("\\/\\/[^:|/]+", "//" + defaultreporturl);
      }
      Lg.d("AdController , register() , defaultadinf = " + mRegister.defaultadinf);
      Lg.d("AdController , register() , defaultreporturl = " + mRegister.defaultreporturl);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public List<AdPos> getAdInfos(String posId, String mid, String fid, String channelCode, String mtype, String cattype) {
    final List<AdPos> datas = new ArrayList<AdPos>();
    datas.clear();
    if (mRegister != null && mRegister.defaultadinf != null && posId != null) {
      if (!mRegister.isContainPosId(posId)) {
        Lg.e("注册接口无此adposId = " + posId);
        return null;
      }
      String defaultadinf = "";
      String PosId = null;
      // NOTE(ljs):6位的广告位表示不清楚后续有多少,要支持平台的扩展
      if (posId.length() <= 6) {
        for (Adposinfo adposs : mRegister.adposs) {
          if (posId.equals(adposs.pos.length() >= 6 ? adposs.pos.subSequence(0, 6) : adposs.pos)) {
            if (TextUtils.isEmpty(PosId)) {
              PosId = adposs.pos;
            } else {
              PosId = PosId + "," + adposs.pos;
            }
          }
        }
      } else {
        PosId = posId;
      }
      defaultadinf = mRegister.defaultadinf.replace("<adpos>", TextUtils.isEmpty(PosId) ? "" : PosId);
      defaultadinf = defaultadinf.replace("<mid>", TextUtils.isEmpty(mid) ? "" : mid);
      defaultadinf = defaultadinf.replace("<fid>", TextUtils.isEmpty(fid) ? "" : fid);
      defaultadinf = defaultadinf.replace("<version>", VAdType.AD_INTERFACE_VERSION);
      defaultadinf = defaultadinf.replace("<mtype>", TextUtils.isEmpty(mtype) ? "" : mtype);
      // FIXME(ljs):catcode和cattype的关系?
      defaultadinf = defaultadinf.replace("<catcode>", TextUtils.isEmpty(channelCode) ? "" : channelCode);
      if (defaultadinf.contains("<cattype>")) {
        defaultadinf = defaultadinf.replace("<cattype>", TextUtils.isEmpty(cattype) ? "" : cattype);
      } else {
        if (!TextUtils.isEmpty(cattype)) {
          defaultadinf += "&cattype=" + cattype;
        } else {
          defaultadinf += "&cattype=";
        }
      }
      Lg.i("AdController , getAdInfos() , url = " + defaultadinf);
      if (!TextUtils.isEmpty(defaultadinf)) {
        ApiDataParser apiDataParser = new ApiDataParser();
        apiDataParser.setReportUrl(mRegister.defaultreporturl);
        apiDataParser.synRequestApiData(defaultadinf, new ApiResponseListener<AdInfo>() {

          @Override
          public void onApiCompleted(AdInfo data) {
            if (data != null && data.adPostions != null && data.adPostions.size() > 0) {
              datas.addAll(data.adPostions);
            }
          }
        });
      }
    }
    return datas;
  }

  @Override
  public List<AdPos> getAdInfos(String rawData) {
    Lg.d("AdController , getAdInfos() ,  rawData = " + rawData);
    if (!TextUtils.isEmpty(rawData)) {
      ApiDataParser apiDataParser = new ApiDataParser();
      apiDataParser.setReportUrl(mRegister.defaultreporturl);
      AdInfo apiData = apiDataParser.synGetApiData(rawData);
      if (apiData != null && apiData.adPostions != null) {
        return apiData.adPostions;
      }
    }
    return null;
  }

  public void showAd(String posId, final ViewGroup parent, final String mid, final String fid, String channelCode, String mtype) {
    showAd(posId, parent.getContext(), parent, mid, fid, channelCode, mtype, null);
  }

  @Override
  public void showAd(String posId, final Context context, final ViewGroup parent, final String mid, final String fid, String channelCode, String mtype, final OnItemClickListener listener) {
    Lg.d("AdController , showAd() , posId = " + posId);
    if (mRegister != null && mRegister.defaultreporturl != null && mRegister.defaultadinf != null) {
      if (mRegister.adposs == null || mRegister.adposs.size() == 0 || mRegister.adposs.get(0) == null) {
        return;
      }
      if (!mRegister.isContainPosId(posId)) {
        Lg.e("posId[" + posId + "]不在注册接口内,无法展示广告");
        return;
      }
      final Activity activity = (Activity) context;
      if (VAdType.AD_EPG_Z_NAVI.equals(posId)) {

      } else if (VAdType.AD_EPG_Z_EXIT.equals(posId)) {
        final ExitAdView exitAdView2 = new ExitAdView(context);
        exitAdView2.setReportUrl(mRegister.defaultreporturl);
        final ApiDataParser apiDataParser = new ApiDataParser();
        String defaultadinf = null;
        defaultadinf = mRegister.defaultadinf.replace("<adpos>", VAdType.AD_EPG_Z_EXIT);
        defaultadinf = defaultadinf.replace("<mid>", mid != null ? mid : "");
        defaultadinf = defaultadinf.replace("<fid>", fid != null ? fid : "");
        defaultadinf = defaultadinf.replace("<catcode>", TextUtils.isEmpty(channelCode) ? "" : channelCode);
        defaultadinf = defaultadinf.replace("<mtype>", TextUtils.isEmpty(mtype) ? "" : mtype);
        defaultadinf = defaultadinf.replace("<version>", VAdType.AD_INTERFACE_VERSION);
        Lg.d("ad_epg_z_exit[直播退出推荐] , url = " + defaultadinf);
        apiDataParser.asynRequestApiData(defaultadinf, new ApiResponseListener<AdInfo>() {

          @Override
          public void onApiCompleted(final AdInfo data) {
            if (data != null) {
              activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                  exitAdView2.setData(data, listener);
                  parent.addView(exitAdView2);
                }
              });
            }
          }
        });
      } else if (VAdType.AD_EPG_D_MOVIE.equals(posId)) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        width2 = dm.widthPixels;
        height2 = dm.heightPixels;

        RelativeLayout relativeLayout = new RelativeLayout(context);
        LayoutParams layoutParams = new LayoutParams(width2, height2);
        relativeLayout.setLayoutParams(layoutParams);

        dialog.setOnKeyListener(new OnKeyListener() {
          @Override
          public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
              final AlertDialog mWebDialog = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();
              mWebDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
              dialog.dismiss();
              mWebDialog.show();
              ReportManager.getInstance().report(AdPoslist.get(0).mediaInfoList.get(0).getReportvalue(), 1, ReportManager.Start, mRegister.defaultreporturl, AdPoslist.get(0).id.substring(0, 2));
              LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
              AdWebView mDialogWv = new AdWebView(context, lp);
              LinearLayout linearLayout = new LinearLayout(context);
              linearLayout.setBackgroundColor(Color.WHITE);
              LayoutParams linearparams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
              linearLayout.setLayoutParams(linearparams);
              linearLayout.addView(mDialogWv);
              mDialogWv.setJsExitListener(new ExitListener() {
                @Override
                public void onExit() {
                  mWebDialog.cancel();
                }
              });
              mWebDialog.setContentView(linearLayout);
              if (AdPoslist != null) {
                mDialogWv.loadUrl(AdPoslist.get(0).mediaInfoList.get(0).getUrl());
              }
              mDialogWv.setVisibility(View.VISIBLE);
              return true;
            }
            return false;
          }
        });
        if (mRegister != null) {
          onXml(context, dialog, relativeLayout, mDialogWv, channelCode, mtype);
        }
      } else if (VAdType.AD_DSY_1.equals(posId)) {
        final RecommendedADView recommendedADView = new RecommendedADView(context);
        recommendedADView.setReportUrl(mRegister.defaultreporturl);
        String[] adDSYPosIds = { VAdType.AD_DSY_1, VAdType.AD_DSY_2, VAdType.AD_DSY_3, VAdType.AD_DSY_4 };
        for (final String adDSYPosId : adDSYPosIds) {
          String defaultadinf = mRegister.defaultadinf.replace("<adpos>", adDSYPosId);
          defaultadinf = defaultadinf.replace("<mid>", mid != null ? mid : "");
          defaultadinf = defaultadinf.replace("<fid>", fid != null ? fid : "");
          defaultadinf = defaultadinf.replace("<catcode>", TextUtils.isEmpty(channelCode) ? "" : channelCode);
          defaultadinf = defaultadinf.replace("<mtype>", TextUtils.isEmpty(mtype) ? "" : mtype);
          defaultadinf = defaultadinf.replace("<version>", VAdType.AD_INTERFACE_VERSION);
          ApiDataParser apiDataParser = new ApiDataParser();
          apiDataParser.asynRequestApiData(defaultadinf, new ApiResponseListener<AdInfo>() {

            @Override
            public void onApiCompleted(final AdInfo data) {
              if (data != null && data.adPostions != null && data.adPostions.size() > 0) {
                activity.runOnUiThread(new Runnable() {

                  @Override
                  public void run() {
                    HashMap<String, AdPos> hashMap = recommendedADView.getHashMap();
                    hashMap.put(adDSYPosId, data.adPostions.get(0));
                    recommendedADView.setData(hashMap);
                  }
                });
              }
            }
          });
        }
        parent.addView(recommendedADView);
      }
    }
  }

  @Override
  public String getPlayerAdUrl(int type, String mid, String fid, String channelCode, String mtype, boolean isSupportTVC) {
    String defaultadinf = "";
    String adposid = "";
    if (mRegister != null && !TextUtils.isEmpty(mRegister.adparams) && mRegister.adposs != null && mRegister.adposs.size() > 0) {
      for (int i = 0; i < mRegister.adposs.size(); i++) {
        String a = mRegister.adposs.get(i).pos;
        if (type == IAdController.PLAY_TYPE_EPG) {// 点播
          // NOTE(ljs):对于点播在拼串时增加对前贴片的判断
          List<String> mEpgPosIds = new ArrayList<String>();
          mEpgPosIds.add(VAdType.AD_PLAY_D_LOADING);
          mEpgPosIds.add(VAdType.AD_PLAY_D_PAUSE);
          mEpgPosIds.add(VAdType.AD_PLAY_D_EXIT);
          mEpgPosIds.add(VAdType.AD_PLAY_D_TVD);
          if (isSupportTVC) {
            mEpgPosIds.add(VAdType.AD_PLAY_D_TVC_START);
          }
          if (mEpgPosIds.contains(a)) {
            if (TextUtils.isEmpty(adposid)) {
              adposid = mRegister.adposs.get(i).pos;
            } else {
              adposid = adposid + "," + mRegister.adposs.get(i).pos;
            }
          }
        } else if (type == IAdController.PLAY_TYPE_LIVE) {// 直播
          if (VAdType.AD_PLAY_Z_TVD_LIVE.equals(a)) {
            if (TextUtils.isEmpty(adposid)) {
              adposid = mRegister.adposs.get(i).pos;
            } else {
              adposid = adposid + "," + mRegister.adposs.get(i).pos;
            }
          }
        }
      }
      defaultadinf = mRegister.adparams.replace("<adpos>", adposid);
      defaultadinf = defaultadinf.replace("<version>", VAdType.AD_INTERFACE_VERSION);
    } else {
      Lg.e("mRegister.adparams------>" + "<是空的>" + "-------<mRegister---------->" + "<也是空的>");
    }
    return defaultadinf;
  }

  // 返回给播放器，广告请求串
  /*
   * @SuppressLint("NewApi")
   *
   * @Override public String getPlayerAdUrl(int type, String mid, String fid, String channelCode,
   * String mtype) { String defaultadinf = ""; String adid = ""; if (mRegister != null &&
   * mRegister.defaultadinf != null) { if (type == IAdController.PLAY_TYPE_EPG) { for (int i = 0; i
   * < mRegister.adposs.size(); i++) { String a = mRegister.adposs.get(i).pos; if
   * (VAdType.AD_PLAY_D_TVC_START.equals(a) || VAdType.AD_PLAY_D_LOADING.equals(a) ||
   * VAdType.AD_PLAY_D_TVD.equals(a) || VAdType.AD_PLAY_D_PAUSE.equals(a) ||
   * VAdType.AD_PLAY_D_EXIT.equals(a)) { if (TextUtils.isEmpty(adid)) { adid =
   * mRegister.adposs.get(i).pos; } else { adid = adid + "," + mRegister.adposs.get(i).pos; } } } }
   * else if (type == IAdController.PLAY_TYPE_LIVE) { for (int i = 0; i < mRegister.adposs.size();
   * i++) { String a = mRegister.adposs.get(i).pos; if (VAdType.AD_PLAY_Z_TVD_LIVE.equals(a)) { if
   * (adid == null) { adid = mRegister.adposs.get(i).pos; } else { adid = adid + "," +
   * mRegister.adposs.get(i).pos; } } } } defaultadinf = mRegister.defaultadinf.replace("<adpos>",
   * adid); defaultadinf = defaultadinf.replace("<mid>", TextUtils.isEmpty(mid) ? "" : mid);
   * defaultadinf = defaultadinf.replace("<fid>", TextUtils.isEmpty(fid) ? "" : fid); defaultadinf =
   * defaultadinf.replace("<catcode>", TextUtils.isEmpty(channelCode) ? "" : channelCode);
   * defaultadinf = defaultadinf.replace("<mtype>", TextUtils.isEmpty(mtype) ? "" : mtype);
   * defaultadinf = defaultadinf.replace("<version>", VAdType.AD_INTERFACE_VERSION); defaultadinf =
   * Base64.encodeToString(defaultadinf.getBytes(), Base64.NO_WRAP); return defaultadinf; } else {
   * Lg.e("mRegister.defaultadinf------>" + "<是空的>" + "-------<mRegister---------->" + "<也是空的>"); }
   * return defaultadinf; }
   */

  // 初始化播放广告
  @Override
  public void initPlayerAd(int type, Context context, IAdPlayer adPlayer, IAdPlayerUIController adPlayerUIController) {
    if (mAdPlayerController == null) {
      if (type == IAdController.PLAY_TYPE_LIVE) {
        // mAdPlayerController = new AdLivePlayerController();
        mAdPlayerController = new AdLivePlayerController();
      } else {
        mAdPlayerController = new AdEpgPlayerController();
      }
    }
    mAdPlayerController.init(context, adPlayer, adPlayerUIController, mRegister);
  }

  @Override
  public void releasePlayerAd() {
    if (mAdPlayerController != null) {
      mAdPlayerController.release();
      mAdPlayerController = null;
    }
  }

  @Override
  public void release() {

  }

  List<AdPos> AdPoslist = null;

  private int width2;

  private int height2;

  private List<AdPos> onXml(final Context context, final AlertDialog dialog, final RelativeLayout relativeLayout, final AdWebView mDialogWv, String channelCode, String mtype) {
    String defaultadinf = null;
    defaultadinf = mRegister.defaultadinf.replace("<adpos>", VAdType.AD_EPG_D_MOVIE);
    defaultadinf = defaultadinf.replace("<mid>", "");
    defaultadinf = defaultadinf.replace("<fid>", "");
    defaultadinf = defaultadinf.replace("<catcode>", TextUtils.isEmpty(channelCode) ? "" : channelCode);
    defaultadinf = defaultadinf.replace("<mtype>", TextUtils.isEmpty(mtype) ? "" : mtype);
    defaultadinf = defaultadinf.replace("<version>", VAdType.AD_INTERFACE_VERSION);
    Lg.i("onXml() , url = " + defaultadinf);
    ApiDataParser apiDataParser = new ApiDataParser();
    apiDataParser.setReportUrl(mRegister.defaultreporturl);
    apiDataParser.asynRequestApiData(defaultadinf, new ApiResponseListener<AdInfo>() {

      @Override
      public void onApiCompleted(final AdInfo data) {
        if (data != null && data.adPostions != null && data.adPostions.size() > 0) {
          AdPoslist = data.adPostions;
          final int mediapos = Integer.valueOf(TextUtils.isEmpty(AdPoslist.get(0).mediaInfoList.get(0).getMediapos()) ? "1" : AdPoslist.get(0).mediaInfoList.get(0).getMediapos());
          final int w = Integer.valueOf(TextUtils.isEmpty(AdPoslist.get(0).mediaInfoList.get(0).getWidth()) ? "500" : AdPoslist.get(0).mediaInfoList.get(0).getWidth());
          final int h = Integer.valueOf(TextUtils.isEmpty(AdPoslist.get(0).mediaInfoList.get(0).getHeight()) ? "500" : AdPoslist.get(0).mediaInfoList.get(0).getHeight());
          final String innerText = AdPoslist.get(0).mediaInfoList.get(0).getInnercontent();
          final String url = AdPoslist.get(0).mediaInfoList.get(0).getSource();
          final String second = AdPoslist.get(0).mediaInfoList.get(0).getLength();
          final int inp = Integer.valueOf(AdPoslist.get(0).mediaInfoList.get(0).getInnermediapos() != null ? AdPoslist.get(0).mediaInfoList.get(0).getInnermediapos() : "0");
          final String inurl = AdPoslist.get(0).mediaInfoList.get(0).getInnersource();
          final String TextPosition = AdPoslist.get(0).mediaInfoList.get(0).getInnernamepos();
          ((Activity) context).runOnUiThread(new Runnable() {

            @Override
            public void run() {
              EpgExitView sudokuView = new EpgExitView(context, w, h, mediapos, false);
              sudokuView.setOnTimerTextListener(new TimerText() {

                @Override
                public void removeView() {
                  dialog.dismiss();
                }

                @Override
                public void getCurrentTime(int second) {
                  Lg.d("AdController , show()#TimerText.getCurrentTime() , second = " + second);
                }

                @Override
                public void success() {
                  ReportManager.getInstance().report(AdPoslist.get(0).mediaInfoList.get(0).getReportvalue(), 0, ReportManager.Start, mRegister.defaultreporturl, AdPoslist.get(0).id.substring(0, 2));
                }
              });
              sudokuView.setData(innerText, url, Integer.valueOf((second != null ? second : "0")), inp, inurl, TextPosition, data.adPostions.get(0));
              relativeLayout.addView(sudokuView);
              // mDialogWv.loadUrl(AdPoslist.get(0).mediaInfoList.get(0).getUrl());
              if (!TextUtils.isEmpty(url)) {
                dialog.show();
                dialog.getWindow().setLayout(width2, height2);
                dialog.setContentView(relativeLayout);
              }
            }
          });

        }
      }
    });
    return AdPoslist;
  }

  @Override
  public String getAdVersion() {
    return VAdType.AD_INTERFACE_VERSION;
  }

  private AdPosBootListener mAdPosBootListener = null;

  @Override
  public boolean showApkStartAd(ViewGroup parent, IAdStartupListener listener, boolean isShowTime) {
    if (mAdPosBootListener == null) {
      mAdPosBootListener = new AdPosBootListener();
    }
    return mAdPosBootListener.show(parent, listener, isShowTime);
  }

  @Override
  public void updateApkStartAd(Context context) {
    if (mAdPosBootListener == null) {
      mAdPosBootListener = new AdPosBootListener();
    }
    if (mRegister != null) {
      mAdPosBootListener.updateApkBootAd(context, mRegister, VAdType.AD_APK_STARTUP);
    }

  }

  @Override
  public IAdEpgOperationHandler getEpgOperationHandler() {
    if (mAdEpgOperationHandler == null) {
      mAdEpgOperationHandler = new AdEpgController(mRegister);
    }
    return mAdEpgOperationHandler;
  }

  @Override
  public IAdHomeEpgOperationHandler getHomeEpgOperationHandler() {
    if (mAdHomeEpgOperationHandler == null) {
      mAdHomeEpgOperationHandler = new AdHomeEpgController(mRegister);
    }
    return mAdHomeEpgOperationHandler;
  }

  @Override
  public List<MediaInfo> getApkExitAdInfos(String adPosid) {
    Lg.e("VAdSDK , getEpgApkExitAdInfos()");
    List<MediaInfo> infos = new ArrayList<MediaInfo>();
    List<AdPos> datas = getAdInfos(adPosid, "", "", "", "", "");
    if (datas == null || datas.size() == 0) {
      return null;
    }
    infos.clear();
    for (int i = 0; i < datas.size(); i++) {
      AdPos adPos = datas.get(i);
      if (adPos != null && adPos.mediaInfoList != null && adPos.mediaInfoList.size() > 0) {
        infos.addAll(adPos.mediaInfoList);
        // 汇报
        ReportManager.getInstance().report(adPos.mediaInfoList.get(0).getReportvalue(), 0, "0", adPos.reportUrl, adPosid);
      }
    }
    return infos;
  }

  @Override
  public void open(final MediaInfo mediaInfo, final Context context, String AdPosid) {
    String skipType = mediaInfo.getSkiptype();
    Lg.d("AdController , open() , skipType = " + skipType);

    if (handler == null) {
      handler = new MediaHandler();
    }
    handler.handlerMediaInfoSkip(AdController.class.getSimpleName(), context, mediaInfo, skipType);

    if (mRegister != null && !TextUtils.isEmpty(mRegister.defaultreporturl)) {
      if ("1".equals(skipType) || "2".equals(skipType) || "3".equals(skipType)) {
        ReportManager.getInstance().report(mediaInfo.getReportvalue(), 1, ReportManager.Start, mRegister.defaultreporturl, AdPosid.substring(0, 2));
      }
    }
  }
}
