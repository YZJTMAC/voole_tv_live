package com.vad.sdk.core.base.interfaces;

import com.vad.sdk.core.base.AdPos;

public interface IAdPosListener {
  public void setData(AdPos adpos);

  public void notifyAd(int currentPos);

  public void release();

  public void setAdPosStatusListener(IAdPosStatusListener l);

  public interface IAdPosStatusListener {
    public void onAdStart();

    public void onAdEnd();
  }
}
