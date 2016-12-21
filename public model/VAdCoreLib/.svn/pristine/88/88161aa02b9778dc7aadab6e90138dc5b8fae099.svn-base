package com.vad.sdk.core.model.v30;

import com.vad.sdk.core.Utils.v30.DisplayManagers;
import com.vad.sdk.core.Utils.v30.Lg;
import com.vad.sdk.core.base.MediaInfo;
import com.vad.sdk.core.manager.MediaHandler;
import com.vad.sdk.core.report.v30.ReportManager;
import com.vad.sdk.core.view.v30.ADView;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnKeyListener;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;

public class AdPosEPGListener extends AdPosBaseListener {
  private int mCurrentTime = 0;//
  private ADView mADView = null;
  private boolean isReport = false;
  private Context mContext;
  private IAdPosEPGListener mIAdPosEPGListener = null;
  private RelativeLayout mRelativeLayout;
  private WindowManager mWindowManager;
  private boolean isAdd = false;
  private int mAdSize = 0;// 当前广告位的广告介质数量
  private int mCurrentAdIndex;// 当前在播放第几个广告
  private boolean mIsShowCurrentAd = false;// 当前广告是否在展示
  private boolean mIsFirstAd = false;// 展示的第一个广告

  // private DownloadAPkManager mAPkManager;

  private Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
      case 1000:
        mCurrentTime += 1000;
        // NOTE(ljs):在多个广告的情况下,广告有优先级,优先播放优先级最高的广告,
        // NOTE(ljs):在播放完成此广告后,如果后面广告的startTime>starttime[0]+length[0],继续播放;
        // NOTE(ljs):如果startTime<starttime[0]+length[0],则跳过此广告
        // FIXME(ljs):同时存在网速不好的情况下,图片未下载完成,图片的倒计时已展示完毕
        if (mAdPos != null && mAdPos.mediaInfoList != null && mAdPos.mediaInfoList.size() > 0 && mAdPos.mediaInfoList.get(0).getSource() != null) {
          mAdSize = mAdPos.mediaInfoList.size();
          Lg.d("AdPosEPGListener , handleMessage() , ad size = " + mAdSize);
          Lg.d("AdPosEPGListener , handleMessage() , mCurrentTime = " + mCurrentTime);

          int firstAdStartTime = Integer.valueOf(!TextUtils.isEmpty(mAdPos.mediaInfoList.get(0).getStarttime()) ? mAdPos.mediaInfoList.get(0).getStarttime() : "5") * 1000;

          if (mCurrentTime < firstAdStartTime) {
            mIsShowCurrentAd = false;
            Lg.d("AdPosEPGListener , handleMessage() , mCurrentTime = " + mCurrentTime + " , firstAdStartTime = " + firstAdStartTime);
            sendEmptyMessageDelayed(1000, 1000);
            return;
          }

          int firstAdLength = Integer.valueOf(!TextUtils.isEmpty(mAdPos.mediaInfoList.get(0).getLength()) ? mAdPos.mediaInfoList.get(0).getLength() : "10") * 1000;

          if (mCurrentTime >= firstAdStartTime && mCurrentTime <= firstAdStartTime + firstAdLength) {
            showAd(mAdPos.mediaInfoList.get(0));
            mIsShowCurrentAd = true;
            mIsFirstAd = true;
            sendEmptyMessageDelayed(1000, 1000);
            return;
          }

          if (mIsFirstAd && mCurrentTime > firstAdStartTime + firstAdLength) {
            Lg.e("AdPosEPGListener , firstAd hideAd() , mCurrentAdIndex = " + (mCurrentAdIndex - 1));
            mIAdPosEPGListener.onAdEnd();
            hideAd();
            mIsFirstAd = false;
            mIsShowCurrentAd = false;
          }

          if (mCurrentAdIndex <= mAdSize) {
            if (mCurrentAdIndex == mAdSize) {
              mCurrentAdIndex = mCurrentAdIndex - 1;
            }
            int nextAdStartTime = Integer.valueOf(!TextUtils.isEmpty(mAdPos.mediaInfoList.get(mCurrentAdIndex).getStarttime()) ? mAdPos.mediaInfoList.get(mCurrentAdIndex).getStarttime() : "0");
            int nextAdLength = Integer.valueOf(!TextUtils.isEmpty(mAdPos.mediaInfoList.get(mCurrentAdIndex).getLength()) ? mAdPos.mediaInfoList.get(mCurrentAdIndex).getLength() : "10") * 1000;
            Lg.d("AdPosEPGListener , mCurrentTime = " + mCurrentTime + " , nextAdStartTime = " + nextAdStartTime + " , isShowCurrentAd = " + mIsShowCurrentAd);
            if (mCurrentTime < nextAdStartTime) {
              mIsShowCurrentAd = false;
              sendEmptyMessageDelayed(1000, 1000);
              return;
            } else if (!mIsShowCurrentAd && mCurrentTime > nextAdStartTime) {
              // NOTE(ljs):忽略优先级低并且startTime小的广告,继续播放下一个
              mCurrentAdIndex = mCurrentAdIndex + 1;
              mIsShowCurrentAd = false;
              if (mCurrentAdIndex == mAdSize) {
                if (handler != null && handler.hasMessages(1000)) {
                  handler.removeMessages(1000);
                  mContext = null;
                }
              } else {
                sendEmptyMessageDelayed(1000, 1000);
              }
              return;
            } else if (!mIsShowCurrentAd && mCurrentTime == nextAdStartTime) {
              showAd(mAdPos.mediaInfoList.get(mCurrentAdIndex));
              mIsShowCurrentAd = true;
              sendEmptyMessageDelayed(1000, 1000);
              return;
            } else if (mIsShowCurrentAd && mCurrentTime > nextAdStartTime && mCurrentTime <= nextAdStartTime + nextAdLength) {
              showAd(mAdPos.mediaInfoList.get(mCurrentAdIndex));
              mIsShowCurrentAd = true;
              sendEmptyMessageDelayed(1000, 1000);
              return;
            } else if (mIsShowCurrentAd && mCurrentTime > nextAdStartTime + nextAdLength) {
              Lg.e("AdPosEPGListener , hideAd() , mCurrentAdIndex = " + mCurrentAdIndex);
              mIAdPosEPGListener.onAdEnd();
              hideAd();
              mIsShowCurrentAd = false;
              if (mCurrentAdIndex + 1 == mAdSize) {
                if (handler != null && handler.hasMessages(1000)) {
                  handler.removeMessages(1000);
                  mContext = null;
                }
              } else if (mCurrentAdIndex + 1 < mAdSize) {
                sendEmptyMessageDelayed(1000, 1000);
              }
            }
          } else {
            if (handler != null && handler.hasMessages(1000)) {
              handler.removeMessages(1000);
            }
          }
        }
        break;
      }
    }
  };

  private void showAd(MediaInfo mediaInfo) {
    if (mADView == null) {
      mCurrentAdIndex = mCurrentAdIndex + 1;
      Lg.e("AdPosEPGListener , showAd() , amid = " + mediaInfo.getAmid());
      if (!TextUtils.isEmpty(mediaInfo.getSource())) {
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wmParams.type = android.view.WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
        wmParams.format = PixelFormat.RGBA_8888;// 设置图片格式，效果为背景透明
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.x = 0;
        wmParams.y = 0;
        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        mRelativeLayout = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mRelativeLayout.setLayoutParams(layoutParams);
        mADView = new ADView(mContext);
        int width = Integer.valueOf(!TextUtils.isEmpty(mediaInfo.getWidth()) ? mediaInfo.getWidth() : "0");
        int height = Integer.valueOf(!TextUtils.isEmpty(mediaInfo.getHeight()) ? mediaInfo.getHeight() : "0");
        int position = Integer.valueOf(!TextUtils.isEmpty(mediaInfo.getMediapos()) ? mediaInfo.getMediapos() : "7");
        if (width == 0 || height == 0) {
          // NOTE(ljs):默认值:400*240
          width = DisplayManagers.getInstance().changeWidthSize(400);
          height = DisplayManagers.getInstance().changeHeightSize(240);
        } else {
          width = DisplayManagers.getInstance().changeWidthSize(width);
          height = DisplayManagers.getInstance().changeHeightSize(height);
        }
        RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(width, height);
        contentParams = mADView.setSudokuParams(position, contentParams);
        mADView.setLayoutParams(contentParams);
        mADView.setAdMedia(mediaInfo);
        mRelativeLayout.addView(mADView);
        // NOTE(ljs):当一个事件产生后,传递顺序如下:Activity-->Window-->DecorView-->按照事件分发机制分发事件,
        // FIXME(ljs):当广告没有二级跳转不获取焦点时,用户点击返回键,会导致当前activity直接退出
        // NOTE(ljs):原因:当焦点不在View身上时无法响应View的setOnKeyListener()事件
        // String skipType = mediaInfo.getSkiptype();
        // if ("1".equals(skipType) || "2".equals(skipType) || "3".equals(skipType)) {
        // mRelativeLayout.setFocusable(true);
        // mRelativeLayout.setFocusableInTouchMode(true);
        // mRelativeLayout.requestFocus();
        // }
        // FIXME(ljs):浮层广告交互二级跳转,焦点问题:当广告图层是文字时依然响应返回键
        wmParams.flags = android.view.WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM; // 有焦点
        mRelativeLayout.setFocusable(true);
        mRelativeLayout.setFocusableInTouchMode(true);
        mRelativeLayout.requestFocus();
        setonClick(mRelativeLayout, mediaInfo);
        if (!((Activity) mContext).isFinishing()) {
          mWindowManager.addView(mRelativeLayout, wmParams);
          isAdd = true;
          Lg.i("AdPosEPGListener ADView MeasuredWidth = " + mADView.getMeasuredWidth() + " , MeasuredHeight = " + mADView.getMeasuredHeight());
          Lg.i("AdPosEPGListener ADView width = " + mADView.getWidth() + " , height = " + mADView.getHeight());
          mIAdPosEPGListener.onAdStart();
        } else {
          isAdd = false;
        }
      }
      if (!isReport) {
        isReport = true;
        String Reportvalue = mediaInfo.getReportvalue();
        ReportManager.getInstance().report(Reportvalue, 0, ReportManager.Start, mReportUrl, mAdPos.id);
      }
    }
  }

  private void setonClick(View view, final MediaInfo info) {
    view.setOnKeyListener(new OnKeyListener() {
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        Lg.e("onKey() , keyCode = " + keyCode + " , event = " + event.getAction());
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
          // NOTE(ljs):有些机型上在按back键时先返回KeyEvent.KEYCODE_ESCAPE,然后如果不拦截事件在返回KeyEvent.KEYCODE_BACK
          if (keyCode == KeyEvent.KEYCODE_BACK) {
            hideAd();
            if (handler != null && handler.hasMessages(1000)) {
              handler.removeMessages(1000);
            }
            return true;
          }
          if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
            // NOTE(ljs):在没有二级跳转的时候只响应返回键不进行上报数据
            if (TextUtils.isEmpty(info.getSkiptype())) {
              return true;// true,由自己处理,false,由系统处理
            }
            MediaHandler handler = new MediaHandler();
            handler.handlerMediaInfoSkip(AdPosEPGListener.class.getSimpleName(), mContext, info, info.getSkiptype());
            Lg.e("AdPosEPGListener , ReportManager.getInstance().report()");
            ReportManager.getInstance().report(info.getReportvalue(), 1, ReportManager.Start, mReportUrl, mAdPos.id.substring(0, 2));
            return true;
          }
          return false;
        }
        return false;
      }
    });
  }

  public void hideAd() {
    Lg.d("AdPosEPGListener , hideAd()");
    if (mADView != null && mRelativeLayout != null && mIsShowCurrentAd) {
      mIsShowCurrentAd = false;
      mADView.setVisibility(View.GONE);
      mADView.stopAd();
      mRelativeLayout.removeView(mADView);
      if (isAdd) {
        mWindowManager.removeView(mRelativeLayout);
      }
      mWindowManager = null;
      mRelativeLayout = null;
      mADView = null;
    }
  }

  public void startShowAd() {
    handler.sendEmptyMessageDelayed(1000, 0);
  }

  public void setContext(Context context) {
    mContext = context;
  }

  public void setAdPosEPGListener(IAdPosEPGListener i) {
    mIAdPosEPGListener = i;
  }

  public interface IAdPosEPGListener {
    public void onAdEnd();

    public void onAdStart();
  }
}
