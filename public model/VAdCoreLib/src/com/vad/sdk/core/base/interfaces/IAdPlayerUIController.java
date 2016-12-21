package com.vad.sdk.core.base.interfaces;

import com.vad.sdk.core.base.AdEvent;

import android.view.ViewGroup;

public interface IAdPlayerUIController {
  /**
   * 取得一个全屏的View容器，并且在应用View层次的最上层，供SDK添加广告子view使用；
   *
   * @return
   */
  public ViewGroup getAdView();

  /**
   * 通知上层应用，是否可以seek；
   *
   * @param seekable
   *          ：true 可以seek； false 不可以seek；
   */
  public void setCanSeek(boolean seekable);

  /**
   * 当应用续播时，拦截续播方法，从头播放前贴片，当前贴片播完后调这个方法，通知上层seek到相应位置；
   *
   * @param pos
   */
  public void onSeek(int pos);

  /**
   * 通知上层正片起播后调用,续播或第一次播放都调用
   */
  public void onMovieStart();

  /**
   * 通知上层应用，是否可以退出；
   *
   * @param canExit
   *          ：true 可以退出； false 不可以退出；
   */
  public void setCanExit(boolean canExit);

  /**
   * 当退出广告结束后，通知上层进行退出操作；
   *
   * @param pos
   */
  public void onExit();

  /**
   * 通知上层应用，一个广告事件发生；
   *
   * @param adEvent
   */
  public void onAdEvent(AdEvent adEvent);
}
