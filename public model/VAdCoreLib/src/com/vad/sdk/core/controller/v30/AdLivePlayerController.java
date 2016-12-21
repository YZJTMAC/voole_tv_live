package com.vad.sdk.core.controller.v30;

import java.util.List;
import java.util.Map;
import android.view.KeyEvent;

import com.vad.sdk.core.VAdSDK;
import com.vad.sdk.core.VAdType;
import com.vad.sdk.core.Utils.v30.Lg;
import com.vad.sdk.core.base.AdPos;
import com.vad.sdk.core.model.v30.AdPosBaseListener.IAdPosStatusListener;
import com.vad.sdk.core.model.v30.AdPosTVDLiveListener;

public class AdLivePlayerController extends AdBasePlayerController {
  private AdPosTVDLiveListener adposScene = null;
  private boolean isView = false;
  private int mErrorCount = 0;
  private String mPreChannelCode = "";

  @Override
  public void onPrepare(String adXml, Map<String, String> extMaps) {
    // NOTE(ljs):直播与点播不同,在adXml没有广告节点,需要用adposid主动向导流平台获取广告数据
    Lg.d("AdLivePlayerController , onPrepare()");
    if (mAdRegister != null && mAdRegister.defaultreporturl != null && mAdRegister.defaultadinf != null) {
      String channelCode = "";
      String channelType = "";
      if (extMaps != null && extMaps.size() > 0) {
        channelCode = extMaps.get("channelCode") == null ? "" : extMaps.get("channelCode").toString();
        channelType = extMaps.get("channelType") == null ? "" : extMaps.get("channelType").toString();
      }
      Lg.e("AdLivePlayerController , onPrepare() , channelCode = " + channelCode);
      Lg.e("AdLivePlayerController , onPrepare() , channelType = " + channelType);
      // NOTE(ljs):在不换台的情况下,发生2次错误后不在请求导流数据
      if (channelCode.equals(mPreChannelCode)) {
        if (mErrorCount <= 2) {
          getAdPosData(channelCode, channelType);
        }
      } else {
        mPreChannelCode = channelCode;
        getAdPosData(channelCode, channelType);
      }
    }
    super.onPrepare(adXml, extMaps);
  }

  @Override
  public void onStart() {
    Lg.d("AdLivePlayerController , onStart()");
    super.onStart();
  }

  @Override
  public void onStop() {
    Lg.d("AdLivePlayerController , onStop()");
    super.onStop();
  }

  @Override
  public void onReset() {
    Lg.d("AdLivePlayerController , onReset()");
    if (adposScene != null) {
      adposScene.removeAd();
    }
    // stopPlayerTimer();
    super.onReset();
  }

  private void getAdPosData(final String channelCode, final String channelType) {
    if (adposScene == null) {
      adposScene = new AdPosTVDLiveListener();
    }
    adposScene.setReportUrl(mAdRegister.defaultreporturl);
    adposScene.setViewGroup(mAdPlayerUIController.getAdView());
    adposScene.setAdPosStatusListener(new IAdPosStatusListener() {
      @Override
      public void onAdStart() {
        Lg.d("AdPosTVDLiveListener , onAdStart()");
        isView = true;
        if (mAdPlayerUIController != null) {
          mAdPlayerUIController.setCanExit(false);
        }
      }

      @Override
      public void onAdEnd() {
        Lg.d("AdPosTVDLiveListener , onAdEnd()");
        isView = false;
        if (mAdPlayerUIController != null) {
          mAdPlayerUIController.setCanExit(true);
        }
      }
    });
    new Thread(new Runnable() {
      @Override
      public void run() {
        List<AdPos> datas = VAdSDK.getInstance().getAdLiveInfos(VAdType.AD_PLAY_Z_TVD_LIVE, "", "", channelCode, "", "");
        if (datas != null && datas.size() > 0) {
          for (AdPos adPos : datas) {
            if (adPos != null && VAdType.AD_PLAY_Z_TVD_LIVE.equals(adPos.id)) {
              adposScene.setData(adPos);
              adposScene.onTime();
            }
          }
        }
      }
    }).start();
  }

  @Override
  public boolean onKeyDown(int keyCode) {
    Lg.d("AdLivePlayerController , onKeyDown() , keyCode = " + keyCode);
    if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE) {
      if (adposScene != null) {
        return adposScene.removeAd();
      }
    }
    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
      if (isView && adposScene != null) {
        return adposScene.open();
      }
    }
    return super.onKeyDown(keyCode);
  }

  @Override
  public boolean onError(int what, int extra) {
    Lg.d("AdLivePlayerController , onError() , what = " + what + " , extra = " + extra);
    mErrorCount++;
    if (adposScene != null) {
      return adposScene.removeAd();
    }
    return super.onError(what, extra);
  }
}
