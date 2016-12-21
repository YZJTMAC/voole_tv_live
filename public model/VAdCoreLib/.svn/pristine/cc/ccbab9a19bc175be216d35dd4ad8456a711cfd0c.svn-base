package com.vad.sdk.core.model.v30;

import android.text.TextUtils;
import com.vad.sdk.core.Utils.v30.DisplayManagers;
import com.vad.sdk.core.Utils.v30.Lg;
import com.vad.sdk.core.report.v30.ReportManager;
import com.vad.sdk.core.view.v30.EpgExitView;
import com.vad.sdk.core.view.v30.EpgExitView.TimerText;

/**
 * 影片缓冲广告
 *
 * 1.在loading阶段用户点击back退出无退出广告
 *
 * @author luojunsheng
 * @date 2016年8月26日 上午9:16:17
 * @version 1.1
 */
public class AdPosLoadingListener extends AdPosBaseListener {
  private EpgExitView mEpgExitView;
  boolean isView;
  private ReportManager reportManager;

  public void show() {
    Lg.d("AdPosLoadingListener , show()");
    reportManager = ReportManager.getInstance();
    // NOTE(ljs):如果广告位对应的source不为空,往VooleMediaPlayer添加sudokuView,在倒计时结束时移除
    // NOTE(ljs):如果广告位对应的source为空直接回调AdPosStatusListener.onAdEnd(),并且汇报广告位
    if (!TextUtils.isEmpty(mAdPos.mediaInfoList.get(0).getSource())) {
      mAdPosStatusListener.onAdStart();
      Lg.i("AdPosLoadingListener , show() , mViewGroup width = " + mViewGroup.getWidth() + ", height = " + mViewGroup.getHeight());
      Lg.i("AdPosLoadingListener , show() , width = " + DisplayManagers.getInstance().getScreenWidth() + ", height = " + DisplayManagers.getInstance().getScreenHeight());
      mEpgExitView = new EpgExitView(mViewGroup, mViewGroup.getContext(), 10);
      String innerText = mAdPos.mediaInfoList.get(0).getInnercontent();
      String source = mAdPos.mediaInfoList.get(0).getSource();
      // FIXME(ljs):广告时间提示的时间对应的是AdPos的alllength字段,但是目前逻辑只支持缓冲片为一个
      String Length = mAdPos.mediaInfoList.get(0).getLength();
      // String Length = mAdPos.alllength;
      int innerMediaPosition = 0;
      if (!TextUtils.isEmpty(mAdPos.mediaInfoList.get(0).getInnermediapos())) {
        innerMediaPosition = Integer.valueOf(mAdPos.mediaInfoList.get(0).getInnermediapos());
      }
      mEpgExitView.setData(innerText, source, Integer.parseInt(!TextUtils.isEmpty(Length) ? Length : "5"), innerMediaPosition, mAdPos.mediaInfoList.get(0).getInnersource(), mAdPos.mediaInfoList
          .get(0).getInnernamepos(), mAdPos);
      mEpgExitView.setOnTimerTextListener(new TimerText() {

        @Override
        public void removeView() {
          Lg.d("AdPosLoadingListener , show()#TimerText.RemoverView()==>mAdPosStatusListener.onAdEnd()");
          mViewGroup.removeView(mEpgExitView);
          mAdPosStatusListener.onAdEnd();
        }

        @Override
        public void success() {
          Lg.d("AdPosLoadingListener , show()#TimerText.success()-->成功后汇报");
          reportManager.report(mAdPos.mediaInfoList.get(0).getReportvalue(), 0, ReportManager.Start, mReportUrl, mAdPos.id.substring(0, 2));
        }

        @Override
        public void getCurrentTime(int second) {
          Lg.d("AdPosLoadingListener , show()#TimerText.getCurrentTime() , second = " + second);
        }
      });
      mViewGroup.addView(mEpgExitView);
      isView = true;
    } else {
      reportManager.report(mAdPos.mediaInfoList.get(0).getReportvalue(), 0, ReportManager.Start, mReportUrl, mAdPos.id.substring(0, 2));
      mAdPosStatusListener.onAdEnd();
    }
  }

  public void stop() {
    Lg.e("AdPosLoadingListener , stop()");
    if (mEpgExitView != null) {
      mEpgExitView.StopTask();
      mViewGroup.removeView(mEpgExitView);
      isView = false;
    }
  }

  public void reset() {
    Lg.e("AdPosLoadingListener , reset()");
    if (mEpgExitView != null) {
      mEpgExitView.StopTask();
      mViewGroup.removeView(mEpgExitView);
      isView = false;
    }
  }
}
