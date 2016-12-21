package com.vad.sdk.core.controller.v30;

import android.content.Context;

import java.util.Map;

import com.vad.sdk.core.Utils.v30.Lg;
import com.vad.sdk.core.base.AdEvent;
import com.vad.sdk.core.base.AdEvent.AdStatus;
import com.vad.sdk.core.base.AdEvent.AdType;
import com.vad.sdk.core.base.AdRegister;
import com.vad.sdk.core.base.interfaces.IAdPlayer;
import com.vad.sdk.core.base.interfaces.IAdPlayerController;
import com.vad.sdk.core.base.interfaces.IAdPlayerEventListener;
import com.vad.sdk.core.base.interfaces.IAdPlayerOperationHandler;
import com.vad.sdk.core.base.interfaces.IAdPlayerUIController;

abstract class AdBasePlayerController implements IAdPlayerController, IAdPlayerOperationHandler, IAdPlayerEventListener {

  protected IAdPlayer mAdPlayer = null;
  protected IAdPlayerUIController mAdPlayerUIController = null;
  protected AdRegister mAdRegister = null;
  private Runnable mPrepareTask;
  protected boolean mIsPlayerLoading = false;

  @Override
  public void init(Context context, IAdPlayer adPlayer, IAdPlayerUIController adPlayerUIController, AdRegister adRegister) {
    mAdPlayer = adPlayer;
    mAdPlayerUIController = adPlayerUIController;
    mAdRegister = adRegister;
    adPlayer.originalInit(context, this, this);
  }

  @Override
  public void release() {
    mAdPlayer = null;
    mAdPlayerUIController = null;
  }

  @Override
  public void onPrepare(String adXml, Map<String, String> extMaps) {// 广告接口
    Lg.d("AdBasePlayerController , onPrepare()");
    // NOTE(ljs):StandardEpgPlayer<--StandardPlayer<--AdPlayer.originalPrepare()
    if (mAdPlayer != null) {// mAdPlayer = class com.voole.player.lib.core.ad.AdPlayer$2
      mAdPlayer.originalPrepare(adXml);// -->AdPlayer#mVooleAdPlayer.originalPrepare()
    }
  }

  @Override
  public void onStart(int pos) {
    if (mAdPlayer != null) {
      mAdPlayer.originalStart();
    }
  }

  @Override
  public void onStart() {
    if (mAdPlayer != null) {
      mAdPlayer.originalStart();
    }
  }

  @Override
  public void onPause(boolean isAllowShowAd) {
    if (mAdPlayer != null) {
      mAdPlayer.originalPause();
    }
  }

  @Override
  public void onStop() {
    if (mAdPlayer != null) {
      mAdPlayer.originalStop();
    }
  }

  @Override
  public void onSeek(int pos) {
    if (mAdPlayer != null) {
      mAdPlayer.originalSeek(pos);
    }
  }

  @Override
  public int getDuration() {
    if (mAdPlayer != null) {
      return mAdPlayer.originalGetDuration();
    }
    return 0;
  }

  @Override
  public int getCurrentPosition() {
    if (mAdPlayer != null) {
      return mAdPlayer.originalGetCurrentPosition();
    }
    return 0;
  }

  @Override
  public boolean onKeyDown(int keyCode) {
    return false;
  }

  @Override
  public void onReset() {
    if (mAdPlayer != null) {
      mAdPlayer.originalReset();
    }
    mPrepareTask = null;
  }

  @Override
  public void onPrepared(Runnable appListenerTask) {
    Lg.d("AdBasePlayerController , onPrepared() , mIsPlayerLoading = " + mIsPlayerLoading);
    mPrepareTask = appListenerTask;
    if (appListenerTask != null && !mIsPlayerLoading) {
      appListenerTask.run();// BasePlayer.notifyOnPrepared()
    }
  }

  public void onCountComplete() {
    Lg.d("AdBasePlayerController , onCountComplete()");
    // NOTE(ljs):通过mPrepareTask是否为null来判断MediaPlayer是否处于prepared状态
    if (mPrepareTask != null) {
      mPrepareTask.run();
    }
  }

  @Override
  public void onCompletion(Runnable appListenerTask) {
    if (appListenerTask != null) {
      appListenerTask.run();
    } else {
      notifyAdEvent(new AdEvent(AdType.LOADING, AdStatus.AD_END));
    }
  }

  @Override
  public boolean onInfo(int what, int extra) {
    return false;
  }

  @Override
  public boolean onError(int what, int extra) {
    return false;
  }

  @Override
  public void onPositionChanged(int currentPos) {

  }

  protected void notifyAdEvent(AdEvent e) {
    if (mAdPlayerUIController != null) {
      mAdPlayerUIController.onAdEvent(e);
    }
  }
}
