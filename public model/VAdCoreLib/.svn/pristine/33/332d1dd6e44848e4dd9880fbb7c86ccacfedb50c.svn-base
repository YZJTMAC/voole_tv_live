package com.vad.sdk.core.base.interfaces;

public interface IAdPlayerEventListener {
  /**
   * 当播放器回调 onPrepared 中调用，处理相关广告业务；
   *
   * @param appListenerTask
   *          实际上层应用要执行的动作；
   */
  public void onPrepared(Runnable appListenerTask);

  /**
   * 当播放器回调 onCompletion 中调用，处理相关广告业务；
   *
   * @param appListenerTask
   *          实际上层应用要执行的动作；
   */
  public void onCompletion(Runnable appListenerTask);

  /**
   * 当播放器回调 onInfo 中调用，处理相关广告业务；
   */
  public boolean onInfo(int what, int extra);

  /**
   * 当播放器回调 onError 中调用，处理相关广告业务；
   */
  public boolean onError(int what, int extra);

  /**
   * 当播放进度发生变化时调用，
   *
   * @param currentPos
   */
  public void onPositionChanged(int currentPos);

  /**
   * 标准开始缓冲code，当适配非标准播放器时，在适配层要把适配播放器缓冲消息代号转换成标准消息传递到SDK；
   *
   * @see android.media.MediaPlayer.OnInfoListener
   */
  public static final int MEDIA_INFO_BUFFERING_START = 701;

  /**
   * 标准结束缓冲code，当适配非标准播放器时，在适配层要把适配播放器缓冲消息代号转换成标准消息传递到SDK；
   *
   * @see android.media.MediaPlayer.OnInfoListener
   */
  public static final int MEDIA_INFO_BUFFERING_END = 702;
}
