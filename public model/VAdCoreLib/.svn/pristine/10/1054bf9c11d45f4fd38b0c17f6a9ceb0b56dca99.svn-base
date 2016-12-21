package com.vad.sdk.core.controller.v30;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.vad.sdk.core.VAdType;
import com.vad.sdk.core.Utils.v30.Lg;
import com.vad.sdk.core.base.AdInfo;
import com.vad.sdk.core.base.AdRegister;
import com.vad.sdk.core.base.interfaces.IAdEpgOperationHandler;
import com.vad.sdk.core.model.v30.AdPosEPGListener;
import com.vad.sdk.core.model.v30.AdPosEPGListener.IAdPosEPGListener;
import com.vad.sdk.core.model.v30.parser.ApiDataParser;
import com.vad.sdk.core.model.v30.parser.ApiResponseListener;

public class AdEpgController implements IAdEpgOperationHandler {
  private AdRegister mAdRegister = null;
  private Context mContext = null;
  private AdPosEPGListener adPosEPGListener;

  public AdEpgController(AdRegister register) {
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
    Lg.d("AdEpgController , onActivityPause()");
    releaseAdListener();
  }

  @Override
  public void onActivityStop() {
    Lg.d("AdEpgController , onActivityStop()");
    // FIXME(ljs):在切换activity时(A--->B),生命周期如下:A.onPause()-->B.onCreate()-->B.onStart()-->B.onResume-->A.onStop()
    // FIXME(ljs):如果在onStop()释放Context,是释放的B的Context
    // releaseAdListener();
  }

  private void releaseAdListener() {
    Lg.d("AdEpgController , releaseAdListener()");
    if (adPosEPGListener != null) {
      adPosEPGListener.hideAd();
      adPosEPGListener = null;
    }
  }

  @Override
  public void onActivityDestroy() {
    mContext = null;
  }

  private void startPlayerTimer() {
    adPosEPGListener.startShowAd();
  }

  private void onShowAd(String mid, String fid, String channelCode, String mtype, String adpos) {
    Lg.i("AdEpgController , onShowAd() , adpos = " + adpos);
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
      defaultadinf = mAdRegister.defaultadinf.replace("<adpos>", adpos);
      defaultadinf = defaultadinf.replace("<mid>", TextUtils.isEmpty(mid) ? "" : mid);
      defaultadinf = defaultadinf.replace("<fid>", TextUtils.isEmpty(fid) ? "" : fid);
      defaultadinf = defaultadinf.replace("<catcode>", TextUtils.isEmpty(channelCode) ? "" : channelCode);
      defaultadinf = defaultadinf.replace("<mtype>", TextUtils.isEmpty(mtype) ? "" : mtype);
      defaultadinf = defaultadinf.replace("<version>", VAdType.AD_INTERFACE_VERSION);
      Lg.i("AdEpgController , onShowAd() , url = " + defaultadinf);
      ApiDataParser apiDataParser = new ApiDataParser();
      apiDataParser.asynRequestApiData(defaultadinf, new ApiResponseListener<AdInfo>() {

        @Override
        public void onApiCompleted(final AdInfo data) {
          if (data != null && data.adPostions != null && data.adPostions.size() > 0 && mContext != null) {
            ((Activity) mContext).runOnUiThread(new Runnable() {

              @Override
              public void run() {
                adPosEPGListener = new AdPosEPGListener();
                adPosEPGListener.setContext(mContext);
                adPosEPGListener.setReportUrl(mAdRegister.defaultreporturl);
                adPosEPGListener.setData(data.adPostions.get(0));
                adPosEPGListener.setAdPosEPGListener(new IAdPosEPGListener() {
                  @Override
                  public void onAdEnd() {
                  }

                  @Override
                  public void onAdStart() {
                  };
                });
                startPlayerTimer();
              }
            });
          }
        }
      });
    }
  }

  @Override
  public void onShowEpgAd(String mid, String fid, String channelCode, String mtype) {
    Lg.d("onShowEpgAd() , mid = " + mid + " , fid = " + fid + " , channelCode = " + channelCode + " , mtype = " + mtype);
    if (mContext != null && !((Activity) mContext).isFinishing()) {
      onShowAd(mid, fid, channelCode, mtype, VAdType.AD_EPG_D_MOVIE);
    }
  }
}
