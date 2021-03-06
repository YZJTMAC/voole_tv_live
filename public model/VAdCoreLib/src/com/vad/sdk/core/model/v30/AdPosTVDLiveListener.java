package com.vad.sdk.core.model.v30;

import java.lang.ref.WeakReference;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vad.sdk.core.VAdType;
import com.vad.sdk.core.Utils.v30.DisplayManagers;
import com.vad.sdk.core.Utils.v30.Lg;
import com.vad.sdk.core.base.MediaInfo;
import com.vad.sdk.core.manager.MediaHandler;
import com.vad.sdk.core.report.v30.ReportManager;
import com.vad.sdk.core.view.v30.ADView;

/**
 * =============================================================<br>
 * 直播浮层广告<br>
 * 1.业务要求<br>
 * 1.1:只展示本广告位的第一个广告介质<br>
 * =============================================================<br>
 * 2.汇报先关<br>
 * 2.1:在有排期无广告介质的情况下,汇报一次<br>
 * 2.2:在有排期有广告介质时,汇报
 *
 * @author luojunsheng
 * @date 2016年9月7日 上午10:48:01
 * @version 1.1
 */
public class AdPosTVDLiveListener extends AdPosBaseListener {
  private static final int MSG_TVD_LIVE_START = 2001;
  private static final int MSG_TVD_LIVE_IMGSHOW = 2002;
  private static final int MSG_TVD_LIVE_REPORT = 2003;
  private boolean isView = false;
  private ADView mADView = null;
  private ImageView imageview;
  private byte[] picByte;
  private Thread thread;
  private boolean isReport = false;
  private RelativeLayout relativeLayout;
  private MyHandler mHandler;
  private int mCurrentTime = 0;
  private boolean mFirst = true;
  private Paint paint;
  private MediaInfo mCurrentMediaInfo;
  private MediaHandler mMediaHandler;

  static class MyHandler extends Handler {
    WeakReference<AdPosTVDLiveListener> mWeakReference;

    MyHandler(AdPosTVDLiveListener adPosTVCLive) {
      mWeakReference = new WeakReference<AdPosTVDLiveListener>(adPosTVCLive);
    }

    @Override
    public void handleMessage(Message msg) {
      AdPosTVDLiveListener adPosTVCLive = mWeakReference.get();
      if (adPosTVCLive != null) {
        switch (msg.what) {
        case MSG_TVD_LIVE_IMGSHOW:
          Lg.d("set view bitmap");
          if (adPosTVCLive.picByte != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(adPosTVCLive.picByte, 0, adPosTVCLive.picByte.length);
            adPosTVCLive.imageview.setImageBitmap(bitmap);
            adPosTVCLive.picByte = null;
            adPosTVCLive.thread = null;
            bitmap = null;
          }
          break;
        case MSG_TVD_LIVE_START:
          if (adPosTVCLive.mAdPos != null && adPosTVCLive.mAdPos.mediaInfoList != null && adPosTVCLive.mAdPos.mediaInfoList.size() > 0) {
            if (!TextUtils.isEmpty(adPosTVCLive.mAdPos.mediaInfoList.get(0).getSource())) {
              int starttime = Integer.valueOf(!TextUtils.isEmpty(adPosTVCLive.mAdPos.mediaInfoList.get(0).getStarttime()) ? adPosTVCLive.mAdPos.mediaInfoList.get(0).getStarttime() : "5");
              int length = Integer.valueOf(!TextUtils.isEmpty(adPosTVCLive.mAdPos.mediaInfoList.get(0).getLength()) ? adPosTVCLive.mAdPos.mediaInfoList.get(0).getLength() : "10");
              Lg.d("AdPosTVDLiveListener , handleMessage() , mCurrentTime " + "" + "= " + adPosTVCLive.mCurrentTime + " , firstAdStartTime = " + starttime);

              if (adPosTVCLive.mCurrentTime > starttime && adPosTVCLive.mCurrentTime < starttime + length + 1) {
                Lg.d("AdPosTVDLiveListener----->mCurrentTime-->" + adPosTVCLive.mCurrentTime);
                adPosTVCLive.showAd(adPosTVCLive.mAdPos.mediaInfoList.get(0));
                adPosTVCLive.isView = true;
                adPosTVCLive.mCurrentTime++;
                adPosTVCLive.mHandler.sendMessageDelayed(adPosTVCLive.mHandler.obtainMessage(MSG_TVD_LIVE_START), 1000);
              } else {
                if (adPosTVCLive.mCurrentTime <= starttime) {
                  adPosTVCLive.mCurrentTime++;
                  adPosTVCLive.mHandler.sendMessageDelayed(adPosTVCLive.mHandler.obtainMessage(MSG_TVD_LIVE_START), 1000);
                } else {
                  adPosTVCLive.removeAd();
                }
              }
            } else {
              if (!adPosTVCLive.isReport) {
                if (adPosTVCLive.mFirst) {
                  String Reportvalue = adPosTVCLive.mAdPos.mediaInfoList.get(0).getReportvalue();
                  ReportManager.getInstance().report(Reportvalue, 0, ReportManager.Start, adPosTVCLive.mReportUrl, adPosTVCLive.mAdPos.id);
                  adPosTVCLive.isReport = true;
                  adPosTVCLive.mFirst = false;
                } else {
                  adPosTVCLive.mHandler.sendEmptyMessageDelayed(MSG_TVD_LIVE_REPORT, 60000);
                }
              }
            }
          }
          break;
        case MSG_TVD_LIVE_REPORT:
          String Reportvalue = adPosTVCLive.mAdPos.mediaInfoList.get(0).getReportvalue();
          ReportManager.getInstance().report(Reportvalue, 0, ReportManager.Start, adPosTVCLive.mReportUrl, adPosTVCLive.mAdPos.id);
          adPosTVCLive.isReport = true;
          break;
        }
      }
    }
  }

  public AdPosTVDLiveListener() {
    mHandler = new MyHandler(this);
  }

  public void onTime() {
    Lg.d("AdPosTVDLiveListener----->onTime()-->Start");
    removeAllMessageQueue();
    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_TVD_LIVE_START), 1000);
  }

  private void removeAllMessageQueue() {
    if (mHandler != null) {
      mHandler.removeMessages(MSG_TVD_LIVE_START);
      mHandler.removeMessages(MSG_TVD_LIVE_IMGSHOW);
      mHandler.removeMessages(MSG_TVD_LIVE_REPORT);
      mCurrentTime = 0;
      isReport = false;
    }
  }

  private void showAd(MediaInfo mediaInfo) {
    mCurrentMediaInfo = mediaInfo;
    if (mADView == null) {
      Lg.d("AdPosTVDLiveListener , showAd()");
      if (!TextUtils.isEmpty(mediaInfo.getSource())) {
        mAdPosStatusListener.onAdStart();
        relativeLayout = new RelativeLayout(mViewGroup.getContext());
        RelativeLayout.LayoutParams rootLayoutParams;
        if (VAdType.AD_MEDIA_TYPE_TEXT.equals(mediaInfo.getViewtype())) {
          paint = new Paint();
          int textWidth = (int) paint.measureText(mediaInfo.getSource());
          rootLayoutParams = new RelativeLayout.LayoutParams(DisplayManagers.getInstance().getScreenWidth() + textWidth, LayoutParams.WRAP_CONTENT);
        } else {
          rootLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
        relativeLayout.setLayoutParams(rootLayoutParams);

        mADView = new ADView(mViewGroup.getContext());
        int width = Integer.valueOf(!TextUtils.isEmpty(mediaInfo.getWidth()) ? mediaInfo.getWidth() : "0");
        int height = Integer.valueOf(!TextUtils.isEmpty(mediaInfo.getHeight()) ? mediaInfo.getHeight() : "0");
        int position = Integer.valueOf(!TextUtils.isEmpty(mediaInfo.getMediapos()) ? mediaInfo.getMediapos() : "7");
        if (width == 0 && height == 0) {
          width = DisplayManagers.getInstance().changeWidthSize(400);
          height = DisplayManagers.getInstance().changeHeightSize(240);
        } else {
          width = DisplayManagers.getInstance().changeWidthSize(width);
          height = DisplayManagers.getInstance().changeHeightSize(height);
        }
        RelativeLayout.LayoutParams adViewLayoutParams = new RelativeLayout.LayoutParams(width, height);
        adViewLayoutParams = mADView.setSudokuParams(position, adViewLayoutParams);
        mADView.setLayoutParams(adViewLayoutParams);
        mADView.setAdMedia(mediaInfo);

        // NOTE(ljs):浮层类广告在没有焦点的时候不响应setOnKeyListener()事件,
        // FIXME(ljs):暂时无法实现无二级跳转不获取焦点的需求
        // String skipType = mediaInfo.getSkiptype();
        // if ("1".equals(skipType) || "2".equals(skipType) || "3".equals(skipType)) {
        // relativeLayout.setFocusable(true);
        // relativeLayout.setFocusableInTouchMode(true);
        // relativeLayout.requestFocus();
        // }
        // relativeLayout.setFocusable(true);
        // relativeLayout.setFocusableInTouchMode(true);
        // relativeLayout.requestFocus();
        // setonClick(relativeLayout, mediaInfo);

        relativeLayout.addView(mADView);
        mViewGroup.addView(relativeLayout);
      }
      if (!isReport) {
        isReport = true;
        String Reportvalue = mediaInfo.getReportvalue();
        ReportManager.getInstance().report(Reportvalue, 0, ReportManager.Start, mReportUrl, mAdPos.id);
      }
    }
  }

  public boolean removeAd() {
    Lg.d("AdPosTVDLiveListener , removeAd()");
    removeAllMessageQueue();
    if (mADView != null && relativeLayout != null) {
      if (picByte != null || thread != null) {
        picByte = null;
        thread = null;
      }
      mAdPosStatusListener.onAdEnd();
      isView = false;
      mADView.setVisibility(View.GONE);
      mADView.stopAd();
      relativeLayout.removeView(mADView);
      mViewGroup.removeView(relativeLayout);
      relativeLayout = null;
      mADView = null;
      return true;
    }
    return false;
  }

  public boolean open() {
    Lg.d("AdPosTVDLiveListener , open()");
    if (mMediaHandler == null) {
      mMediaHandler = new MediaHandler();
    }
    String skipType = mCurrentMediaInfo.getSkiptype();
    mMediaHandler.handlerMediaInfoSkip(AdPosTVDLiveListener.class.getSimpleName(), mViewGroup.getContext(), mCurrentMediaInfo, skipType, true);
    if ("1".equals(skipType) || "2".equals(skipType) || "3".equals(skipType)) {
      Lg.d("直播播放中浮层,有二级跳转,汇报");
      ReportManager.getInstance().report(mCurrentMediaInfo.getReportvalue(), 1, ReportManager.Start, mReportUrl, mAdPos.id.substring(0, 2));
      return true;
    } else {
      Lg.d("直播播放中浮层,无二级跳转,不汇报");
      return false;
    }
  }
}
