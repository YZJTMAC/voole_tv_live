/**
 *
 */
package com.vad.sdk.core.base;

/**
 *
 * @author luojunsheng
 * @date 2016年8月19日 下午12:59:42
 * @version 1.1
 */
public class Slice {
  public boolean mIsTVCMiddle;
  public boolean mHasReportStart;
  public boolean mHasReportStop;
  public int mStartTime;
  public int mEndTime;
  public int mLength;
  public MediaInfo mCurrentMediaInfo;

  /**
   * 当startTime=endTime=-1时,表示对正片没有分割
   *
   * @param startTime
   * @param endTime
   */
  public Slice(int startTime, int endTime, boolean isTVCMiddle, MediaInfo currentMediaInfo) {
    super();
    mStartTime = startTime;
    mEndTime = endTime;
    mLength = mEndTime - mStartTime;
    mIsTVCMiddle = isTVCMiddle;
    mHasReportStart = false;
    mHasReportStop = false;
    mCurrentMediaInfo = currentMediaInfo;
  }

  @Override
  public String toString() {
    return "{start=" + mStartTime + " , end=" + mEndTime + " , isTVCM= " + mIsTVCMiddle + "}";
  }
}
