package com.vad.sdk.core.base.interfaces;

import android.content.Context;

public interface IAdPlayer {
  public void originalSeek(int pos);

  public void originalStop();

  public void originalInit(Context context, IAdPlayerOperationHandler adPlayerOperationHandler,
      IAdPlayerEventListener adPlayerEventListener);

  public void originalPrepare(String xmlSrc);

  public void originalStart();

//  public void originalStart(int pos);

  public void originalPause();

  public void originalReset();

  // public void originalRelease();

  public int originalGetDuration();

  public int originalGetCurrentPosition();

  public boolean isAutoPushCurrentPosition();
}
