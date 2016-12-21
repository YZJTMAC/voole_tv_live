package com.vad.sdk.core.base.interfaces;

import android.content.Context;

public interface IAdEpgOperationHandler {
  public void onActivityCreate(Context context);

  public void onActivityStart();

  public void onActivityPause();

  public void onActivityStop();

  public void onActivityDestroy();

  public void onShowEpgAd(String mid, String fid, String channelCode, String mtype);

}
