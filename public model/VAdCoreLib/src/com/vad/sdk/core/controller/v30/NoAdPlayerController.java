package com.vad.sdk.core.controller.v30;

import android.content.Context;

import java.util.Map;

import com.vad.sdk.core.base.AdRegister;
import com.vad.sdk.core.base.interfaces.IAdPlayer;
import com.vad.sdk.core.base.interfaces.IAdPlayerController;
import com.vad.sdk.core.base.interfaces.IAdPlayerEventListener;
import com.vad.sdk.core.base.interfaces.IAdPlayerOperationHandler;
import com.vad.sdk.core.base.interfaces.IAdPlayerUIController;

public class NoAdPlayerController implements IAdPlayerController, IAdPlayerOperationHandler,
    IAdPlayerEventListener {
  protected IAdPlayer mAdPlayer = null;
  protected IAdPlayerUIController mAdPlayerUIController = null;

  @Override
  public void init(Context context, IAdPlayer adPlayer, IAdPlayerUIController adPlayerUIController,
      AdRegister register) {
    mAdPlayer = adPlayer;
    mAdPlayerUIController = adPlayerUIController;
    adPlayer.originalInit(context, this, this);
  }

  @Override
  public void release() {
    mAdPlayer = null;
    mAdPlayerUIController = null;
  }

  @Override
  public void onPrepare(String adXml, Map<String, String> extMaps) {
    if (mAdPlayer != null) {
      mAdPlayer.originalPrepare(adXml);
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
  public void onPause(boolean isShowAd) {
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
  }

  /*
   * @Override public void onRelease() { mAdPlayer.originalRelease(); }
   */

  // from IAdPlayerEventListener
  @Override
  public void onPrepared(Runnable appListenerTask) {
    if (appListenerTask != null) {
      appListenerTask.run();
    }
  }

  @Override
  public void onCompletion(Runnable appListenerTask) {
    if (appListenerTask != null) {
      appListenerTask.run();
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
}