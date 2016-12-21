package com.vad.sdk.core.model.v30;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.vad.sdk.core.Utils.v30.Lg;
import com.vad.sdk.core.report.v30.ReportManager;
import com.vad.sdk.core.view.v30.EpgExitView;
import com.vad.sdk.core.view.v30.EpgExitView.TimerText;

public class AdPosExitListener extends AdPosBaseListener {
  private EpgExitView epgExitView;
  boolean isView;
  private ReportManager reportManager;

  @SuppressLint("NewApi")
  public void show() {
    Lg.d("AdPosExitListener , show()");
    reportManager = ReportManager.getInstance();
    if (!TextUtils.isEmpty(mAdPos.mediaInfoList.get(0).getSource())) {
      mAdPosStatusListener.onAdStart();
      epgExitView = new EpgExitView(mViewGroup, mViewGroup.getContext(), 0);
      String name = mAdPos.mediaInfoList.get(0).getInnercontent();
      String source = mAdPos.mediaInfoList.get(0).getSource();
      String Length = mAdPos.mediaInfoList.get(0).getLength();
      int n;
      if (!TextUtils.isEmpty(mAdPos.mediaInfoList.get(0).getInnermediapos())) {
        n = Integer.valueOf(mAdPos.mediaInfoList.get(0).getInnermediapos());
      } else {
        n = 5;
      }
      // epgExitView.setData(name, source,
      // Integer.parseInt(Length),9,"http://imgadmin.voole.com/img/new_img/2015/09/28/2015092817380151305.png");
      epgExitView.setOnTimerTextListener(new TimerText() {

        @Override
        public void removeView() {
          // mViewGroup.removeView(epgExitView);
          mAdPosStatusListener.onAdEnd();
        }

        @Override
        public void getCurrentTime(int second) {
          Lg.d("AdPosExitListener , show()#TimerText.getCurrentTime() , second = " + second);
        }

        @Override
        public void success() {
          reportManager.report(mAdPos.mediaInfoList.get(0).getReportvalue(), 0, ReportManager.Start, mReportUrl, mAdPos.id.substring(0, 2));
        }
      });
      epgExitView.setData(name, source, Integer.parseInt(TextUtils.isEmpty(Length) ? "5" : Length), n, mAdPos.mediaInfoList.get(0).getInnersource(), mAdPos.mediaInfoList.get(0).getInnernamepos(),
          mAdPos);
      mViewGroup.addView(epgExitView);
      isView = true;
    } else {
      reportManager.report(mAdPos.mediaInfoList.get(0).getReportvalue(), 0, ReportManager.Start, mReportUrl, mAdPos.id.substring(0, 2));
      mAdPosStatusListener.onAdEnd();
    }
  }

  public void stop() {
    epgExitView.StopTask();
    mViewGroup.removeView(epgExitView);
    isView = false;
  }

  public void reset() {
    epgExitView.StopTask();
    mViewGroup.removeView(epgExitView);
    isView = false;
  }
}
