package com.vad.sdk.core.model.v30;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnLayoutChangeListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import com.vad.sdk.core.Utils.v30.DisplayManagers;
import com.vad.sdk.core.Utils.v30.Lg;
import com.vad.sdk.core.Utils.v30.Utils;
import com.vad.sdk.core.base.MediaInfo;
import com.vad.sdk.core.manager.MediaHandler;
import com.vad.sdk.core.report.v30.ReportManager;

/**
 * 前贴片
 *
 * 1.不可快进,不可后退,可暂停,可退出;<br>
 * 2.前贴片退出无退出广告;<br>
 * 3.与暂停广告互斥<br>
 * 4.与浮层广告互斥<br>
 * 5.多条前贴片分别汇报开始+结束;<br>
 * 6.二级跳转时汇报
 *
 * @author luojunsheng
 * @date 2016年8月23日 下午5:47:30
 * @version 1.1
 */
public class AdPosTVCListener extends AdPosBaseListener {
  private static final int MSG_TIME_COUNTDOWN = 1000;
  private static final int MSG_TIME_REPORT_START = 1001;
  private static final int MSG_TIME_REPORT_END = 1002;
  private RelativeLayout mTVCAdLayoutContainer;
  private TextView mTimeCountDownView;
  private TextView mPromptView;
  private int mReportStartIndex = 0;
  private int mReportEndIndex = 0;
  private boolean mHasLink;// 二级链接
  private MediaInfo mCurrentMediaInfo;
  private ReportManager reportManager;
  private boolean mHasInitView = false;
  private boolean mHasMedium = false;// 有排期的情况下是否有广告介质
  private boolean mHasPause = false;
  private MyHandler mHandler;

  static class MyHandler extends Handler {
    WeakReference<AdPosTVCListener> mWeakReference;

    MyHandler(AdPosTVCListener adPosTVC) {
      mWeakReference = new WeakReference<AdPosTVCListener>(adPosTVC);
    }

    @Override
    public void handleMessage(android.os.Message msg) {
      AdPosTVCListener adPosTVC = mWeakReference.get();
      if (adPosTVC != null) {
        switch (msg.what) {
        case MSG_TIME_COUNTDOWN:
          adPosTVC.showCountDownTime((Integer) msg.obj);
          break;
        case MSG_TIME_REPORT_START:
          adPosTVC.reportStart((Integer) msg.obj);
          break;
        case MSG_TIME_REPORT_END:
          adPosTVC.reportEnd((Integer) msg.obj);
          break;

        default:
          break;
        }
      }
    };
  }

  @SuppressLint("NewApi")
  public void initView() {
    Lg.d("AdPosTVCListener , initView()");
    mHandler = new MyHandler(this);
    reportManager = ReportManager.getInstance();
    if (!TextUtils.isEmpty(mAdPos.mediaInfoList.get(0).getSource())) {
      mHasInitView = true;
      mHasMedium = true;
      float rate = 1.0f;
      if (mViewGroup != null) {
        // FIXME(ljs):rate=Infinity(无穷大)有时候在app第一次播放的时候
        float Viewidth = mViewGroup.getWidth() * 1.0f;
        int screenWidth = DisplayManagers.getInstance().getScreenWidth();
        rate = Viewidth / screenWidth;
        Lg.e("________Viewidth = " + Viewidth);
        Lg.e("________screenWidth = " + screenWidth);
        Lg.e("________rate = " + rate);
      }
      mTVCAdLayoutContainer = new RelativeLayout(mViewGroup.getContext());
      LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
      mTVCAdLayoutContainer.setLayoutParams(params);
      // 1.右上角倒计时View
      mTimeCountDownView = new TextView(mViewGroup.getContext());
      RelativeLayout.LayoutParams rightTextParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
      rightTextParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      rightTextParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
      mTimeCountDownView.setGravity(Gravity.CENTER);
      mTimeCountDownView.setBackgroundDrawable(Utils.createRoundedRectDrawable("#9a000000", 4, mTimeCountDownView.getHeight()));
      mTimeCountDownView.setPadding((int) (15 * rate), (int) (15 * rate), (int) (15 * rate), (int) (15 * rate));
      mTimeCountDownView.setTextColor(Color.WHITE);
      mTimeCountDownView.setTextSize(30 * rate);
      mTimeCountDownView.setLayoutParams(rightTextParams);
      // String Length = mAdPos.mediaInfoList.get(0).getLength();
      // mTimerText.setText("广告剩余:"+Length+"秒");
      LayoutParams params1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

      params1.setMargins((int) (22 * rate), (int) (22 * rate), (int) (22 * rate), (int) (22 * rate));
      params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      mTVCAdLayoutContainer.addView(mTimeCountDownView, params1);
      // 2.左上角提示View
      mPromptView = new TextView(mViewGroup.getContext());
      RelativeLayout.LayoutParams lp5 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
      lp5.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      lp5.addRule(RelativeLayout.ALIGN_PARENT_TOP);
      mPromptView.setGravity(Gravity.CENTER);
      mPromptView.setBackgroundDrawable(Utils.createRoundedRectDrawable("#9a000000", 4, mPromptView.getHeight()));
      mPromptView.setPadding((int) (15 * rate), (int) (15 * rate), (int) (15 * rate), (int) (15 * rate));
      mPromptView.setTextColor(Color.WHITE);
      mPromptView.setTextSize(30 * rate);
      mPromptView.setLayoutParams(lp5);
      LayoutParams params2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
      params2.setMargins((int) (22 * rate), (int) (22 * rate), (int) (22 * rate), (int) (22 * rate));
      params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      mPromptView.setVisibility(View.INVISIBLE);
      mTVCAdLayoutContainer.addView(mPromptView, params2);
      mViewGroup.addView(mTVCAdLayoutContainer);
      //
      mViewGroup.addOnLayoutChangeListener(new OnLayoutChangeListener() {

        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
          if ((right - left) != (oldRight - oldLeft)) {
            float changeRate = mViewGroup.getWidth() * 1.0f / DisplayManagers.getInstance().getScreenWidth();
            mTimeCountDownView.setTextSize(30 * changeRate);
            mTimeCountDownView.setPadding((int) (15 * changeRate), (int) (15 * changeRate), (int) (15 * changeRate), (int) (15 * changeRate));
            RelativeLayout.LayoutParams rightTxtParams = (LayoutParams) mTimeCountDownView.getLayoutParams();
            rightTxtParams.setMargins((int) (22 * changeRate), (int) (22 * changeRate), (int) (22 * changeRate), (int) (22 * changeRate));
            mPromptView.setTextSize(30 * changeRate);
            mPromptView.setPadding((int) (15 * changeRate), (int) (15 * changeRate), (int) (15 * changeRate), (int) (15 * changeRate));
            RelativeLayout.LayoutParams leftTxtParams = (LayoutParams) mPromptView.getLayoutParams();
            leftTxtParams.setMargins((int) (22 * changeRate), (int) (22 * changeRate), (int) (22 * changeRate), (int) (22 * changeRate));
          }
        }
      });
    } else {
      mHasInitView = false;
      mHasMedium = false;
      Lg.d("没有前贴片排期,汇报....");
      // NOTE(ljs):没有广告的时候只汇报一次start就可以
      reportManager.report(mAdPos.mediaInfoList.get(0).getReportvalue(), 0, ReportManager.Start, mReportUrl, mAdPos.id.substring(0, 2));
    }
  }

  public int getAdAllLength() {
    return Integer.parseInt(mAdPos.allLength);
  }

  public void setTVCPause(boolean isPause) {
    mHasPause = isPause;
  }

  /**
   * 依靠MediaPlayer的倒计时
   *
   * @deprecated
   */
  @Deprecated
  public void setCountDownTime(int mpCurrentPosition) {
    // if (!mHasInitView) {
    // mHasInitView = true;
    // initView();
    // }
    // if (mpCurrentPosition == 0) {
    // mAdPosStatusListener.onAdStart();
    // }
    // int adPosAllLength = Integer.parseInt(mAdPos.allLength);
    // int adCountDownTime = adPosAllLength - mpCurrentPosition;
    // if (mTimeCountDownView != null) {
    // SpannableStringBuilder style;
    // if (adCountDownTime >= 100) {
    // String text = "广告剩余:" + adCountDownTime + "秒";
    // style = new SpannableStringBuilder(text);
    // style.setSpan(new ForegroundColorSpan(Color.parseColor("#ffcb63")), 5, 8,
    // Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
    // } else if (adCountDownTime >= 10) {
    // String text = "广告剩余:" + adCountDownTime + "秒";
    // style = new SpannableStringBuilder(text);
    // style.setSpan(new ForegroundColorSpan(Color.parseColor("#ffcb63")), 5, 7,
    // Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
    // } else {
    // String text = "广告剩余:0" + adCountDownTime + "秒";
    // style = new SpannableStringBuilder(text);
    // style.setSpan(new ForegroundColorSpan(Color.parseColor("#ffcb63")), 5, 7,
    // Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
    // }
    // if (adCountDownTime == 0) {
    // mAdPosStatusListener.onAdEnd();
    // }
    // mTimeCountDownView.setText(style);
    // if (mpCurrentPosition == 0 && mCurrentAdIndex == 0 && old == -1) {
    // adCountDownTime +=
    // Integer.valueOf(TextUtils.isEmpty(mAdPos.mediaInfoList.get(mCurrentAdIndex).getLength()) ?
    // "0" : mAdPos.mediaInfoList.get(mCurrentAdIndex).getLength());
    // mCurrentMediaInfo = mAdPos.mediaInfoList.get(mCurrentAdIndex);
    // if (mPromptView.getVisibility() != View.VISIBLE) {
    // if (mCurrentMediaInfo.getTips() != null) {
    // mPromptView.setText(mCurrentMediaInfo.getTips());
    // mPromptView.setVisibility(View.VISIBLE);
    // }
    // }
    // if (mAdPos.mediaInfoList.get(mCurrentAdIndex).getInnersource() != null) {
    // int w =
    // Integer.valueOf(!TextUtils.isEmpty(mAdPos.mediaInfoList.get(mCurrentAdIndex).getInnerwidth())
    // ? mAdPos.mediaInfoList.get(mCurrentAdIndex).getInnerwidth() : "50");
    // int h =
    // Integer.valueOf(!TextUtils.isEmpty(mAdPos.mediaInfoList.get(mCurrentAdIndex).getInnerheight())
    // ? mAdPos.mediaInfoList.get(mCurrentAdIndex).getInnerheight() : "50");
    // embedded = (SimpleDraweeView) embedded(
    // Integer.valueOf(!TextUtils.isEmpty(mAdPos.mediaInfoList.get(mCurrentAdIndex).getInnermediapos())
    // ? mAdPos.mediaInfoList.get(mCurrentAdIndex).getInnermediapos() : "9"), w, h);
    // // embedded.setImageURI(Uri.parse(mAdPos.mediaInfoList.get(0).getInnersource()));
    // Uri uri = Uri.parse(mAdPos.mediaInfoList.get(mCurrentAdIndex).getInnersource());
    // ImageDecodeOptions decodeOptions =
    // ImageDecodeOptions.newBuilder().setDecodeAllFrames(true).build();
    // ImageRequest request =
    // ImageRequestBuilder.newBuilderWithSource(uri).setImageDecodeOptions(decodeOptions).build();
    // DraweeController draweeController1 =
    // Fresco.newDraweeControllerBuilder().setImageRequest(request).setAutoPlayAnimations(true).build();
    // embedded.setController(draweeController1);
    // }
    // Lg.e("第一个前贴片播放的开始汇报");
    // reportManager.report(mAdPos.mediaInfoList.get(mCurrentAdIndex).getReportvalue(), 0,
    // ReportManager.Start, mReportUrl, mAdPos.id.substring(0, 2));
    // old = mCurrentAdIndex;
    // }
    // if (adCountDownTime > 0 && adCountDownTime <= mpCurrentPosition && old == mCurrentAdIndex) {
    // reportManager.report(mAdPos.mediaInfoList.get(mCurrentAdIndex).getReportvalue(), 0,
    // ReportManager.Stop, mReportUrl, mAdPos.id.substring(0, 2));
    // mCurrentAdIndex++;
    // if (mCurrentMediaInfo.getTips() == null) {
    // mPromptView.setVisibility(View.INVISIBLE);
    // } else {
    // mPromptView.setText(mCurrentMediaInfo.getTips() + "");
    // }
    // Lg.d("AdposTVCListener======>end========>" + mCurrentAdIndex);
    // }
    // if (mCurrentAdIndex > 0 && mAdPos.mediaInfoList.size() > mCurrentAdIndex && mCurrentAdIndex
    // != old) {
    // if (mCurrentAdIndex > 0 && mAdPos.mediaInfoList.get(mCurrentAdIndex - 1).getInnersource() !=
    // null) {
    // if (embedded != null) {
    // mTVCAdLayoutContainer.removeView(embedded);
    // embedded = null;
    // }
    // }
    // adCountDownTime +=
    // Integer.valueOf(TextUtils.isEmpty(mAdPos.mediaInfoList.get(mCurrentAdIndex).getLength()) ?
    // "0" : mAdPos.mediaInfoList.get(mCurrentAdIndex).getLength());
    // mCurrentMediaInfo = mAdPos.mediaInfoList.get(mCurrentAdIndex);
    // if (mPromptView.getVisibility() != View.VISIBLE) {
    // if (mCurrentMediaInfo.getTips() != null) {
    // mPromptView.setText(mCurrentMediaInfo.getTips());
    // mPromptView.setVisibility(View.VISIBLE);
    // }
    // }
    // if (mAdPos.mediaInfoList.get(mCurrentAdIndex).getInnersource() != null) {
    // int w =
    // Integer.valueOf((!TextUtils.isEmpty(mAdPos.mediaInfoList.get(mCurrentAdIndex).getInnerwidth()))
    // ? mAdPos.mediaInfoList.get(mCurrentAdIndex).getInnerwidth() : "50");
    // int h =
    // Integer.valueOf((!TextUtils.isEmpty(mAdPos.mediaInfoList.get(mCurrentAdIndex).getInnerheight()))
    // ? mAdPos.mediaInfoList.get(mCurrentAdIndex).getInnerheight() : "50");
    // Lg.d("w==========>" + mAdPos.mediaInfoList.get(mCurrentAdIndex).getInnerwidth() + "&W==>" + w
    // + " : h========>" + mAdPos.mediaInfoList.get(mCurrentAdIndex).getInnerheight() + "&H==>" + h
    // + " : n===>" + mAdPos.mediaInfoList.get(mCurrentAdIndex).getInnermediapos());
    // embedded = (SimpleDraweeView) embedded(
    // Integer.valueOf(!TextUtils.isEmpty(mAdPos.mediaInfoList.get(mCurrentAdIndex).getInnermediapos())
    // ? mAdPos.mediaInfoList.get(mCurrentAdIndex).getInnermediapos() : "9"), w, h);
    // // embedded.setImageURI(Uri.parse(mAdPos.mediaInfoList.get(0).getInnersource()));
    // Uri uri = Uri.parse(mAdPos.mediaInfoList.get(mCurrentAdIndex).getInnersource());
    // ImageDecodeOptions decodeOptions =
    // ImageDecodeOptions.newBuilder().setDecodeAllFrames(true).build();
    // ImageRequest request =
    // ImageRequestBuilder.newBuilderWithSource(uri).setImageDecodeOptions(decodeOptions).build();
    // DraweeController draweeController1 =
    // Fresco.newDraweeControllerBuilder().setImageRequest(request).setAutoPlayAnimations(true).build();
    // embedded.setController(draweeController1);
    // }
    // reportManager.report(mAdPos.mediaInfoList.get(mCurrentAdIndex).getReportvalue(), 0,
    // ReportManager.Start, mReportUrl, mAdPos.id.substring(0, 2));
    // old = mCurrentAdIndex;
    // Lg.d("AdposTVCListener======>=====start=====>" + mCurrentAdIndex);
    // }
    // }
  }

  /**
   * 自己计时
   */
  public void showCountDownTime(int adCountDownTime) {
    Lg.d("AdPosTVCListener , showCountDownTime() , adCountDownTime = " + adCountDownTime);
    if (!mHasInitView) {
      initView();
    }
    if (adCountDownTime == Integer.parseInt(mAdPos.allLength)) {
      mAdPosStatusListener.onAdStart();
    }
    // 排期内无广告直接直接调用onAdEnd();
    if (!mHasMedium) {
      mAdPosStatusListener.onAdEnd();
      return;
    }
    if (mHasInitView && mHasMedium && adCountDownTime == Integer.parseInt(mAdPos.allLength)) {
      reportEnd(adCountDownTime);
      reportStart(adCountDownTime);
    }
    if (adCountDownTime == 0) {
      mAdPosStatusListener.onAdEnd();
      return;
    }
    SpannableStringBuilder style;
    if (adCountDownTime >= 100) {
      String text = "广告剩余:" + adCountDownTime + "秒";
      style = new SpannableStringBuilder(text);
      style.setSpan(new ForegroundColorSpan(Color.parseColor("#ffcb63")), 5, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
    } else if (adCountDownTime >= 10) {
      String text = "广告剩余:" + adCountDownTime + "秒";
      style = new SpannableStringBuilder(text);
      style.setSpan(new ForegroundColorSpan(Color.parseColor("#ffcb63")), 5, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
    } else {
      String text = "广告剩余:0" + adCountDownTime + "秒";
      style = new SpannableStringBuilder(text);
      style.setSpan(new ForegroundColorSpan(Color.parseColor("#ffcb63")), 5, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
    }
    mTimeCountDownView.setText(style);
    if (adCountDownTime != 0) {
      mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_TIME_COUNTDOWN, mHasPause ? adCountDownTime : adCountDownTime - 1), 1000);
    }
  }

  public void reportStart(int adCountDownTime) {
    if (mAdPos.mediaInfoList.size() >= mReportStartIndex + 1) {
      int startReportTime = 0;
      for (int i = 0; i < mReportStartIndex; i++) {
        startReportTime = startReportTime + Integer.parseInt(mAdPos.mediaInfoList.get(i).getLength());
      }
      if (adCountDownTime == Integer.parseInt(mAdPos.allLength) - startReportTime) {
        Lg.e("____________________第" + mReportStartIndex + "个前贴片播放的开始汇报");
        mCurrentMediaInfo = mAdPos.mediaInfoList.get(mReportStartIndex);
        reportManager.report(mAdPos.mediaInfoList.get(mReportStartIndex).getReportvalue(), 0, ReportManager.Start, mReportUrl, mAdPos.id.substring(0, 2));
        if (!TextUtils.isEmpty(mAdPos.mediaInfoList.get(mReportStartIndex).getUrl())) {
          mHasLink = true;
        } else {
          mHasLink = false;
        }
        if (TextUtils.isEmpty(mAdPos.mediaInfoList.get(mReportStartIndex).getTips())) {
          mPromptView.setVisibility(View.INVISIBLE);
        } else {
          mPromptView.setVisibility(View.VISIBLE);
          mPromptView.setText(mAdPos.mediaInfoList.get(mReportStartIndex).getTips() + "");
        }
        mReportStartIndex++;
      }
      mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_TIME_REPORT_START, mHasPause ? adCountDownTime : adCountDownTime - 1), 1000);
    }
  }

  public void reportEnd(int adCountDownTime) {
    if (mAdPos.mediaInfoList.size() >= mReportEndIndex + 1) {
      int endReportTime = 0;
      for (int i = 0; i <= mReportEndIndex; i++) {
        endReportTime = endReportTime + Integer.parseInt(mAdPos.mediaInfoList.get(i).getLength());
      }
      if (adCountDownTime == Integer.parseInt(mAdPos.allLength) - endReportTime) {
        Lg.e("____________________第" + mReportEndIndex + "个前贴片播放的结束汇报");
        reportManager.report(mAdPos.mediaInfoList.get(mReportEndIndex).getReportvalue(), 0, ReportManager.Stop, mReportUrl, mAdPos.id.substring(0, 2));
        mReportEndIndex++;
      }
      mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_TIME_REPORT_END, mHasPause ? adCountDownTime : adCountDownTime - 1), 1000);
    }
  }

  public void stop() {
    mHasLink = false;
    if (mHandler != null) {
      if (mHandler.hasMessages(MSG_TIME_COUNTDOWN)) {
        mHandler.removeMessages(MSG_TIME_COUNTDOWN);
      }
      if (mHandler.hasMessages(MSG_TIME_REPORT_START)) {
        mHandler.removeMessages(MSG_TIME_REPORT_START);
      }
      if (mHandler.hasMessages(MSG_TIME_REPORT_END)) {
        mHandler.removeMessages(MSG_TIME_REPORT_END);
      }
    }
    mViewGroup.removeView(mTVCAdLayoutContainer);
  }

  public void open() {
    if (mHasLink) {
      if (mCurrentMediaInfo != null) {
        Lg.e("AdPosTVCListener , open() , Skiptype = " + mCurrentMediaInfo.getSkiptype());
        MediaHandler handler = new MediaHandler();
        handler.handlerMediaInfoSkip(AdPosTVCListener.class.getSimpleName(), mViewGroup.getContext(), mCurrentMediaInfo, mCurrentMediaInfo.getSkiptype());

        if ("1".equals(mCurrentMediaInfo.getSkiptype()) || "2".equals(mCurrentMediaInfo.getSkiptype()) || "3".equals(mCurrentMediaInfo.getSkiptype())) {
          Lg.d("前贴片配了二级跳转,需要汇报");
          ReportManager.getInstance().report(mCurrentMediaInfo.getReportvalue(), 1, ReportManager.Start, mReportUrl, mAdPos.id.substring(0, 2));
        }
      }
    }
  }
}
