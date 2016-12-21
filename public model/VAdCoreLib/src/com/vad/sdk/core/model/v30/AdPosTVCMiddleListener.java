/**
 *
 */
package com.vad.sdk.core.model.v30;

import com.vad.sdk.core.VAdType;
import com.vad.sdk.core.Utils.v30.Lg;
import com.vad.sdk.core.base.AdPos;
import com.vad.sdk.core.base.MediaInfo;
import com.vad.sdk.core.base.Slice;
import com.vad.sdk.core.manager.MediaHandler;
import com.vad.sdk.core.report.v30.ReportManager;

import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;

/**
 * 中贴片
 *
 * 1.不可快进,不可后退,可暂停,可退出;<br>
 * 2.中贴片优先级高于暂停广告,即广告中暂停不再展示暂停广告;<br>
 * 3.中贴片与播放中浮层非互斥;<br>
 * 3.广告中退出显示退出广告;<br>
 * 4.观众快进过了广告时间点,即快进至16:00分钟后,不播广告,上报数据;<br>
 * 5.同次播放的多次快进操作,只上报第一次经过广告时间点的数据;<br>
 * 6.退出重新播放正片,有经过广告时间点的观看和快进行为,则上报数据;无经过广告时间点的行为,则不上报数据<br>
 * 7.<br>
 *
 * @author luojunsheng
 * @date 2016年8月19日 上午10:56:09
 * @version 1.1
 */
public class AdPosTVCMiddleListener extends AdPosBaseListener {
  private List<Slice> mSlices;
  private int mTVCAdAllLength;
  private RelativeLayout mTVCMiddleAdLayoutContainer;
  private TextView mTimeCountDownView;
  private ReportManager mReportManager;
  private MediaInfo mCurrentMediaInfo;
  private boolean mHasInitView = false;

  public void setData(AdPos adpos, List<Slice> slices, int tVCAdAllLength) {
    mAdPos = adpos;
    mSlices = slices;
    mTVCAdAllLength = tVCAdAllLength;
  }

  @Override
  public void setReportUrl(String reportUrl) {
    mReportUrl = reportUrl;
  }

  public void initView() {
    mHasInitView = true;
    if (mReportManager == null) {
      mReportManager = new ReportManager();
    }
  }

  public void showCountDownTime(int mpCurrentPosition) {
    Lg.e("AdPosTVCMiddleListener , showCountDownTime() , mpCurrentPosition = " + mpCurrentPosition);
    if (!mHasInitView) {
      initView();
    }
    for (int i = 0; i < mSlices.size(); i++) {
      Slice slice = mSlices.get(i);
      // NOTE(ljs):在无中贴片的时候不汇报
      // NOTE(ljs):用户在看中贴片的时候,很可能快进跳过中贴片的最后一秒,导致此中贴片没有结束法汇报的情况
      // NOTE(ljs):针对这种情况在正片中在做一次汇报
      if (mpCurrentPosition >= slice.mStartTime && (mpCurrentPosition <= slice.mEndTime || slice.mEndTime == -1)) {
        if (slice.mIsTVCMiddle) {// 中贴片
        	mCurrentMediaInfo = slice.mCurrentMediaInfo;
          int adCountDownTime = slice.mLength - (mpCurrentPosition - mSlices.get(i).mStartTime);
          Lg.e("AdPosTVCMiddleListener , showCountDownTime() , adCountDownTime = " + adCountDownTime);
          if (!slice.mHasReportStart && slice.mCurrentMediaInfo != null) {
            slice.mHasReportStart = true;
            Lg.e("中贴片--->中贴片--开始汇报-- , 在整个片中的位置 = " + i);
            mReportManager.report(slice.mCurrentMediaInfo.getReportvalue(), 0, ReportManager.Start, mReportUrl, VAdType.AD_PLAY_D_TVC_MIDDLE.substring(0, 2));
          } else {
            Lg.e("中贴片--->中贴片--开始汇报--已经汇报过 , 在整个片中的位置 = " + i);
          }
          if (adCountDownTime == slice.mLength) {
            mAdPosStatusListener.onAdStart();
          }
          if (adCountDownTime == 0) {
            mAdPosStatusListener.onAdEnd();
            if (!slice.mHasReportStop && slice.mCurrentMediaInfo != null) {
              Lg.e("中贴片---->中贴片--结束汇报-- , 在整个片中的位置 = " + i);
              slice.mHasReportStop = true;
              mReportManager.report(slice.mCurrentMediaInfo.getReportvalue(), 0, ReportManager.Stop, mReportUrl, VAdType.AD_PLAY_D_TVC_MIDDLE.substring(0, 2));
            } else {
              Lg.e("中贴片---->中贴片--结束汇报--已经汇报过 , 在整个片中的位置 = " + i);
            }
          }
        } else {// 正片
          Lg.e("正片中....." + i);
          // NOTE(ljs):对于seek过去的中贴片要上报数据
          if (i >= 2) {
            for (int j = 0; j < i; j++) {
              Slice preSlice = mSlices.get(j);
              if (preSlice.mIsTVCMiddle) {
                if (!preSlice.mHasReportStart) {
                  preSlice.mHasReportStart = true;
                  Lg.e("正片---->中贴片--开始汇报-- , 在整个片中的位置 = " + j + " , 此汇报在正片中汇报");
                  mReportManager.report(preSlice.mCurrentMediaInfo.getReportvalue(), 0, ReportManager.Start, mReportUrl, VAdType.AD_PLAY_D_TVC_MIDDLE.substring(0, 2));
                }
                if (!preSlice.mHasReportStop) {
                  preSlice.mHasReportStop = true;
                  Lg.e("正片---->中贴片--结束汇报-- , 在整个片中的位置 = " + j + " , 此汇报在正片中汇报");
                  mReportManager.report(preSlice.mCurrentMediaInfo.getReportvalue(), 0, ReportManager.Stop, mReportUrl, VAdType.AD_PLAY_D_TVC_MIDDLE.substring(0, 2));
                }
                if (preSlice.mHasReportStart && preSlice.mHasReportStop) {
                  Lg.e("正片---->中贴片--开始和结束都已汇报 , 在整个片中的位置 = " + j);
                }
              }
            }
          }
        }
      }
    }
  }
  
  public void open() {
	      if (mCurrentMediaInfo != null) {
	        Lg.e("AdPosTVCListener , open() , Skiptype = " + mCurrentMediaInfo.getSkiptype());
	        MediaHandler handler = new MediaHandler();
	        handler.handlerMediaInfoSkip(AdPosTVCListener.class.getSimpleName(), mViewGroup.getContext(), mCurrentMediaInfo, mCurrentMediaInfo.getSkiptype());

	        if ("1".equals(mCurrentMediaInfo.getSkiptype()) || "2".equals(mCurrentMediaInfo.getSkiptype()) || "3".equals(mCurrentMediaInfo.getSkiptype())) {
	          Lg.d("中贴片配了二级跳转,需要汇报");
	          ReportManager.getInstance().report(mCurrentMediaInfo.getReportvalue(), 1, ReportManager.Start, mReportUrl, mAdPos.id.substring(0, 2));
	        }
	    }
	  }
  
  
  
  public void reset() {
    // mViewGroup.removeView(mTVCMiddleAdLayoutContainer);
  }
  
  
}
