package com.vad.sdk.core.base.interfaces;

import java.util.Map;

/**
 * 广告业务接口，由适配层拦截播放器调用事件，调用本接口实例方法触发广告业务；
 *
 * @author Administrator
 *
 */
public interface IAdPlayerOperationHandler {

  public void onPrepare(String adXml, Map<String, String> extMaps);

  /**
   * MediaPlayer第一次起播或续播时调用
   *
   * @param pos
   *          是正片上次播放的时间点
   */
  public void onStart(int pos);

  /**
   * MediaPlayer 由暂停-->播放时回调
   */
  public void onStart();

  /**
   * @param isAllowShowAd
   *          场景下是否允许弹出暂停广告;在用户退出MediaPlayer时,弹出确认对话框,此时MP现处于暂停状态,不能弹出暂停广告
   */
  public void onPause(boolean isAllowShowAd);

  public void onStop();

  public void onSeek(int pos);

  public void onReset();

  // public void onRelease();

  public int getDuration();

  public int getCurrentPosition();

  public boolean onKeyDown(int keyCode);
}