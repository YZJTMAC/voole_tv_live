package com.vad.sdk.core.controller.v30;

import java.util.List;

import android.content.Context;
import android.view.ViewGroup;

import com.vad.sdk.core.base.AdPos;
import com.vad.sdk.core.base.MediaInfo;
import com.vad.sdk.core.base.interfaces.IAdController;
import com.vad.sdk.core.base.interfaces.IAdEpgOperationHandler;
import com.vad.sdk.core.base.interfaces.IAdHomeEpgOperationHandler;
import com.vad.sdk.core.base.interfaces.IAdPlayer;
import com.vad.sdk.core.base.interfaces.IAdPlayerController;
import com.vad.sdk.core.base.interfaces.IAdPlayerUIController;
import com.vad.sdk.core.base.interfaces.IAdStartupListener;
import com.vad.sdk.core.view.v30.ExitAdView.OnItemClickListener;

public class NoAdController implements IAdController {
  private IAdPlayerController mAdPlayerController = null;

  @Override
  public boolean register(String apkId) {
    return false;
  }

  @Override
  public void release() {

  }

  @Override
  public void initPlayerAd(int type, Context context, IAdPlayer adPlayer, IAdPlayerUIController adPlayerUIController) {
    if (mAdPlayerController == null) {
      mAdPlayerController = new NoAdPlayerController();
    }
    mAdPlayerController.init(context, adPlayer, adPlayerUIController, null);
  }

  @Override
  public void releasePlayerAd() {
    if (mAdPlayerController != null) {
      mAdPlayerController.release();
      mAdPlayerController = null;
    }
  }

  @Override
  public String getAdVersion() {
    return null;
  }

  @Override
  public boolean register(String apkId, String defaultadinf, String defaultreporturl) {
    return false;
  }

  @Override
  public boolean showApkStartAd(ViewGroup parent, IAdStartupListener listener, boolean isShowTime) {
    return false;
  }

  @Override
  public IAdEpgOperationHandler getEpgOperationHandler() {
    return null;
  }

  @Override
  public void open(MediaInfo mediaInfo, Context context, String AdPosid) {

  }

  @Override
  public void updateApkStartAd(Context context) {

  }

  @Override
  public List<AdPos> getAdInfos(String posId, String mid, String fid, String channelCode, String mtype, String cattype) {
    return null;
  }

  @Override
  public List<AdPos> getAdInfos(String rawData) {
    return null;
  }

  @Override
  public IAdHomeEpgOperationHandler getHomeEpgOperationHandler() {
    return null;
  }

  @Override
  public void showAd(String posId, Context context, ViewGroup parent, String mid, String fid, String channelCode, String mtype, OnItemClickListener listener) {
  }

  @Override
  public String getPlayerAdUrl(int type, String mid, String fid, String channelCode, String mtype, boolean isSupportTVC) {
    return null;
  }

  @Override
  public List<MediaInfo> getApkExitAdInfos(String adPosid) {
    return null;
  }
}
