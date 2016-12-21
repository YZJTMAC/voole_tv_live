package com.vad.sdk.core.controller.v30;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.vad.sdk.core.VAdType;
import com.vad.sdk.core.Utils.v30.Lg;
import com.vad.sdk.core.base.AdInfo;
import com.vad.sdk.core.base.AdRegister;
import com.vad.sdk.core.base.interfaces.IAdHomeEpgOperationHandler;
import com.vad.sdk.core.model.v30.AdPosEPGListener;
import com.vad.sdk.core.model.v30.AdPosEPGListener.IAdPosEPGListener;
import com.vad.sdk.core.model.v30.parser.ApiDataParser;
import com.vad.sdk.core.model.v30.parser.ApiResponseListener;

/**
 *
 * 首页Epg广告,要求立即展示
 *
 * @author luojunsheng
 * @date 2016年4月8日 下午3:30:47
 * @version 1.1
 */
public class AdHomeEpgController implements IAdHomeEpgOperationHandler {
  private AdRegister mAdRegister = null;
  private Context mContext = null;
  private AdPosEPGListener mAdPosHomeEPGListener;

  public AdHomeEpgController(AdRegister register) {
    mAdRegister = register;
  }

  @Override
  public void onActivityCreate(final Context context) {
    mContext = context;
  }

  @Override
  public void onActivityStart() {
  }

  @Override
  public void onActivityPause() {
    releaseAdListener();
  }

  @Override
  public void onActivityStop() {
  }

  @Override
  public void onActivityDestroy() {
    mContext = null;
  }

  @Override
  public void onShowHomeEpgAd(String mid, String fid, String channelCode, String mtype) {
    Lg.d("AdHomeEpgController , onShowHomeEpgAd()");
    if (!((Activity) mContext).isFinishing()) {
      onShowAd(mid, fid, channelCode, mtype, VAdType.AD_EPG_D_HOME);
    }
  }

  private void releaseAdListener() {
    Lg.d("AdHomeEpgController , releaseAdListener()");
    if (mAdPosHomeEPGListener != null) {
      mAdPosHomeEPGListener.hideAd();
      mAdPosHomeEPGListener = null;
    }
  }

  private void startPlayerTimer() {
    mAdPosHomeEPGListener.startShowAd();
  }

  private void onShowAd(String mid, String fid, String channelCode, String mtype, String adpos) {
    Lg.d("AdHomeEpgController , onShowHomeEpgAd() , adpos = " + adpos);
    releaseAdListener();
    if (mAdRegister == null) {
      return;
    }
    if (mAdRegister.posIds == null || mAdRegister.posIds.size() == 0) {
      return;
    }
    if (!mAdRegister.isContainPosId(adpos)) {
      Lg.e("注册接口无此adposId = " + adpos);
      return;
    }
    if (mAdRegister != null) {
      String defaultadinf = null;
      defaultadinf = mAdRegister.defaultadinf.replace("<adpos>", TextUtils.isEmpty(adpos) ? "" : adpos);
      defaultadinf = defaultadinf.replace("<mid>", TextUtils.isEmpty(mid) ? "" : mid);
      defaultadinf = defaultadinf.replace("<fid>", TextUtils.isEmpty(fid) ? "" : fid);
      defaultadinf = defaultadinf.replace("<catcode>", TextUtils.isEmpty(channelCode) ? "" : channelCode);
      defaultadinf = defaultadinf.replace("<mtype>", TextUtils.isEmpty(mtype) ? "" : mtype);
      defaultadinf = defaultadinf.replace("<cattype>", "");
      defaultadinf = defaultadinf.replace("<version>", VAdType.AD_INTERFACE_VERSION);

      ApiDataParser apiDataParser = new ApiDataParser();
      apiDataParser.asynRequestApiData(defaultadinf, new ApiResponseListener<AdInfo>() {

        @Override
        public void onApiCompleted(final AdInfo data) {
          if (mContext != null && data != null && data.adPostions != null && data.adPostions.size() > 0) {
            ((Activity) mContext).runOnUiThread(new Runnable() {

              @Override
              public void run() {
                // 1.设置数据
                mAdPosHomeEPGListener = new AdPosEPGListener();
                mAdPosHomeEPGListener.setContext(mContext);
                mAdPosHomeEPGListener.setReportUrl(mAdRegister.defaultreporturl);
                mAdPosHomeEPGListener.setData(data.adPostions.get(0));
                mAdPosHomeEPGListener.setAdPosEPGListener(new IAdPosEPGListener() {

                  @Override
                  public void onAdEnd() {
                    Lg.d("AdHomeEpgController#IAdPosEPGListener , onAdEnd()");
                  }

                  @Override
                  public void onAdStart() {
                    Lg.d("AdHomeEpgController#IAdPosEPGListener , onAdStart()");
                  };
                });
                // 2.显示并开启计时器
                startPlayerTimer();
              }
            });
          }
        }
      });
    }
  }
}
