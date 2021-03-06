package com.vad.sdk.core.model.v30;

import android.view.ViewGroup;

import com.vad.sdk.core.base.AdPos;

public abstract class AdPosBaseListener {
  protected IAdPosStatusListener mAdPosStatusListener = null;
  protected AdPos mAdPos = null;
  protected ViewGroup mViewGroup = null;
  protected String mReportUrl = null;

  // protected Context context = null;
  public void setAdPosStatusListener(IAdPosStatusListener l) {
    mAdPosStatusListener = l;
  }

  public void setData(AdPos adpos) {
    mAdPos = adpos;
  }

  public void setViewGroup(ViewGroup v) {
    mViewGroup = v;
  }

  public void setReportUrl(String reportUrl) {
    mReportUrl = reportUrl;
  }

  public interface IAdPosStatusListener {
    /**
     * 广告开始播放时回调
     */
    public void onAdStart();

    public void onAdEnd();

    // public void onAdShowError(int status, String message);
  }
}
