package com.vad.sdk.core.view.v30;

import java.util.ArrayList;
import java.util.List;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.vad.sdk.core.R;
import com.vad.sdk.core.Utils.v30.DisplayManagers;
import com.vad.sdk.core.Utils.v30.Lg;
import com.vad.sdk.core.Utils.v30.Utils;
import com.vad.sdk.core.base.AdInfo;
import com.vad.sdk.core.base.AdPos;
import com.vad.sdk.core.base.MediaInfo;
import com.vad.sdk.core.manager.MediaHandler;
import com.vad.sdk.core.report.v30.ReportManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ExitAdView extends LinearLayout {
  private ArrayList<SimpleDraweeView> imageList;
  private ArrayList<RelativeLayout> array;
  private Context mContext;
  private ReportManager reportManager;
  private MediaHandler handler;
  private String mReportUrl;
  private ArrayList<AlwaysMarqueeTextView> textList;
  private LinearLayout mItemContainerTop;
  private LinearLayout mItemContainerBottom;
  
  private NinePatch mFocusPatch;
  private Bitmap mFocusBitmap;
  private int mLocationLeft;
  private int mLocationTop;
  private int mLocationRight;
  private int mLocationBottom;
  private boolean mIsNeedDraw = false;
  private AdInfo mAdInfo;
  private List<ExitAd> mExitAds;// 排期的广告
  private List<ExitAd> mShowExitAds;// 排期的有url的广告

  private OnItemClickListener mListener;

  public interface OnItemClickListener {
    void onItemClick(int position, MediaInfo mediaInfo);
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    mListener = listener;
  }

  public ExitAdView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ExitAdView(Context context) {
    super(context);
    mContext = context;
    DisplayManagers.getInstance().init(context);
    initView(context);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    Lg.d("ExitAdView2 , onDraw()");
    super.onDraw(canvas);
    if (mIsNeedDraw) {
      Lg.d("ExitAdView2 , onDraw() , go to draw bitmap");
      Rect bitmapRect = new Rect(mLocationLeft, mLocationTop, mLocationRight, mLocationBottom);
      mFocusPatch.draw(canvas, bitmapRect);
    }
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    Lg.d("ExitAdView2 , onAttachedToWindow()");
    if (mAdInfo != null && mAdInfo.adPostions != null && mAdInfo.adPostions.size() > 0 && mAdInfo.adPostions.get(0) != null && mAdInfo.adPostions.get(0).mediaInfoList != null
        && mAdInfo.adPostions.get(0).mediaInfoList.size() > 0) {
      boolean isReportFilm = false;// 是否汇报过该片单接口
      for (int i = 0; i < mExitAds.size(); i++) {
        if (i >= 8) {
          return;
        }
        ExitAd exitAd = mExitAds.get(i);
        if (exitAd.mMediaInfo.isFilm()) {
          if (!isReportFilm) {
            Lg.d("当前是片单,没有回报,go to report");
            reportManager.report(exitAd.mMediaInfo.getReportvalue(), 0, ReportManager.Start, mReportUrl, exitAd.mAdPosId.substring(0, 2));
            isReportFilm = true;
          } else {
            Lg.d("当前是片单,已经回报无需汇报");
          }
        } else {
          Lg.d("当前不是片单,go to report");
          reportManager.report(exitAd.mMediaInfo.getReportvalue(), 0, ReportManager.Start, mReportUrl, exitAd.mAdPosId.substring(0, 2));
        }
      }
    }
  }

  private void initView(Context context) {
    setPadding(DisplayManagers.dip2px(context, 10), 0, 0, 0);
    // setBackgroundColor(Color.RED);
    setWillNotDraw(false);
    mFocusBitmap = Utils.drawableToBitamp(context, R.drawable.focus);
    mFocusPatch = new NinePatch(mFocusBitmap, mFocusBitmap.getNinePatchChunk(), null);
    reportManager = ReportManager.getInstance();
    LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    setLayoutParams(layoutParams);
    setOrientation(LinearLayout.VERTICAL);
    mItemContainerTop = new LinearLayout(context);
    mItemContainerBottom = new LinearLayout(context);
    LayoutParams itmelayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
    mItemContainerTop.setLayoutParams(itmelayoutParams);
    mItemContainerBottom.setLayoutParams(itmelayoutParams);
    mItemContainerTop.setOrientation(HORIZONTAL);
    mItemContainerBottom.setOrientation(HORIZONTAL);
    addView(mItemContainerTop);
    addView(mItemContainerBottom);
    initItem(context, mItemContainerTop, mItemContainerBottom);
  }

  private void initItem(Context context, LinearLayout containerTop, LinearLayout containerBottom) {
    array = new ArrayList<RelativeLayout>();
    for (int i = 0; i < 8; i++) {
      RelativeLayout Layout = new RelativeLayout(context);
      Layout.setGravity(Gravity.CENTER);
      Layout.setClickable(true);
      Layout.setFocusable(true);
      Layout.setFocusableInTouchMode(true);
      LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1);
      Layout.setLayoutParams(Params);
      array.add(Layout);
      if (i > 3) {
        containerBottom.addView(Layout);
      } else {
        containerTop.addView(Layout);
      }
    }
    imageList = new ArrayList<SimpleDraweeView>();
    textList = new ArrayList<AlwaysMarqueeTextView>();
    // RoundingParams roundingParams = RoundingParams.fromCornersRadius(13.4f);// 设置边角角度[左右上角的角度]
    RoundingParams roundingParams = RoundingParams.fromCornersRadius(20f);// 设置边角角度[左右上角的角度]
    for (int index = 0; index < array.size(); index++) {
      RelativeLayout contentLayout = array.get(index);
      SimpleDraweeView imageView = new SimpleDraweeView(context);
      RelativeLayout.LayoutParams imag_params;
      if (DisplayManagers.getInstance().getScreenDpi() == 320) {
        imag_params = new RelativeLayout.LayoutParams(DisplayManagers.dip2px(context, 150), DisplayManagers.dip2px(context, 200));
      } else {
        imag_params = new RelativeLayout.LayoutParams(DisplayManagers.dip2px(context, 210), DisplayManagers.dip2px(context, 280));
      }
      imageView.setLayoutParams(imag_params);
      // imageView.setAspectRatio(0.75f);// 设置长宽比[width:height]
      GenericDraweeHierarchyBuilder builder2 = new GenericDraweeHierarchyBuilder(getResources());
      builder2.setFadeDuration(500);
      builder2.setRoundingParams(roundingParams);
      GenericDraweeHierarchy hierarchy2 = builder2.build();
      imageView.setHierarchy(hierarchy2);
      imageView.setScaleType(ImageView.ScaleType.FIT_XY);// 把图片不按比例扩大/缩小到View的大小显示
      imageView.setId(1);
      final AlwaysMarqueeTextView textView = new AlwaysMarqueeTextView(context);
      RelativeLayout.LayoutParams text_Parames = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
      text_Parames.addRule(RelativeLayout.ALIGN_BOTTOM, 1);
      text_Parames.addRule(RelativeLayout.ALIGN_LEFT, 1);
      text_Parames.addRule(RelativeLayout.ALIGN_RIGHT, 1);
      textView.setTextSize(DisplayManagers.getInstance().changeTextSize(24));
      textView.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
      textView.setPadding(0, 0, 0, DisplayManagers.dip2px(mContext, 10));
      textView.setTextColor(Color.WHITE);
      textView.setTag("itme_text");
      textView.setBackgroundResource(R.drawable.ad_image_exit);
      contentLayout.addView(imageView);
      contentLayout.addView(textView, text_Parames);
      contentLayout.setOnFocusChangeListener(new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
          if (hasFocus) {
            mIsNeedDraw = true;
            LinearLayout topOrBotton = (LinearLayout) v.getParent();
            Lg.e("left = " + v.getLeft() + " , parent top = " + topOrBotton.getTop() + " , right = " + v.getRight() + " , parent bottom = " + topOrBotton.getBottom());
            Lg.e("screenDpi = " + DisplayManagers.getInstance().getScreenDpi());
            int tmpLeft = 0;
            int tmpTop = 0;
            int tmpRight = 0;
            int tmpBottom = 0;
            // FIXME(ljs):对于.9图在不同dpi上边界的缩放比例要探寻一下规律
            // FIXME(ljs):在小米盒子上dpi = 320 属于超高清
            if (DisplayManagers.getInstance().getScreenDpi() == 240) {
              tmpLeft = DisplayManagers.dip2px(mContext, 15f);
              tmpTop = DisplayManagers.dip2px(mContext, 4f);
              tmpRight = DisplayManagers.dip2px(mContext, 15f);
              tmpBottom = DisplayManagers.dip2px(mContext, 6f);
            } else if (DisplayManagers.getInstance().getScreenDpi() == 160) {
              tmpLeft = DisplayManagers.dip2px(mContext, 25.5f);
              tmpTop = DisplayManagers.dip2px(mContext, 15f);
              tmpRight = DisplayManagers.dip2px(mContext, 25.5f);
              tmpBottom = DisplayManagers.dip2px(mContext, 15.5f);
            } else if (DisplayManagers.getInstance().getScreenDpi() == 320) {
              tmpLeft = DisplayManagers.dip2px(mContext, 8f);
              tmpTop = DisplayManagers.dip2px(mContext, 2f);
              tmpRight = DisplayManagers.dip2px(mContext, 8f);
              tmpBottom = DisplayManagers.dip2px(mContext, 2f);
            } else {
              tmpLeft = DisplayManagers.dip2px(mContext, 15f);
              tmpTop = DisplayManagers.dip2px(mContext, 4f);
              tmpRight = DisplayManagers.dip2px(mContext, 15f);
              tmpBottom = DisplayManagers.dip2px(mContext, 6f);
            }
            Lg.e("tmpLeft = " + tmpLeft + " , tmpTop = " + tmpTop + " , tmpRight = " + tmpRight + " , tmpBottom = " + tmpBottom);
            mLocationLeft = v.getLeft() - tmpLeft + DisplayManagers.dip2px(mContext, 10);
            mLocationTop = topOrBotton.getTop() - tmpTop;
            mLocationRight = v.getRight() + tmpRight + DisplayManagers.dip2px(mContext, 10);
            mLocationBottom = topOrBotton.getBottom() + tmpBottom;
            invalidate();
            textView.setMarquee(true);
          } else {
            invalidate();
            mIsNeedDraw = false;
            textView.setMarquee(false);
            mLocationLeft = 0;
            mLocationTop = 0;
            mLocationRight = 0;
            mLocationBottom = 0;
          }
        }
      });
      imageList.add(imageView);
      textList.add(textView);
    }
  }

  /**
   * 直播退出位的广告,存在是片单接口的情况,一个片单接口是一个Mediainfo节点,只汇报一次,<br>
   * 比如,此广告位配置了2个MediaInfo,一个是只正常的图片,一个是一个片单接口,该片单接口中有7个片单广告,<br>
   * 此时应该汇报2此,汇报动作针对的是MediaInfo对象
   *
   * @param data
   * @param listener
   */
  public void setData(final AdInfo data, OnItemClickListener listener) {
    Lg.d("ExitAdView2 , setData()");
    if (data == null) {
      return;
    }
    mAdInfo = data;
    mListener = listener;

    mExitAds = new ArrayList<ExitAd>();
    mShowExitAds = new ArrayList<ExitAd>();

    for (AdPos adPos : data.adPostions) {
      if (adPos != null && adPos.mediaInfoList != null && adPos.mediaInfoList.size() > 0) {
        for (MediaInfo infoTmp : adPos.mediaInfoList) {
          if (infoTmp != null) {
            mExitAds.add(new ExitAd(adPos.id, infoTmp));
            if (!TextUtils.isEmpty(infoTmp.getSource())) {
              mShowExitAds.add(new ExitAd(adPos.id, infoTmp));
            }
          }
        }
      }
    }
    Lg.d("ExitAdView2 , setData() , exitAds size = " + mExitAds.size() + " , mShowExitAds size = " + mShowExitAds.size());
    if (mShowExitAds.size() >= 1) {
      for (int i = 0; i < mShowExitAds.size(); i++) {
        if (i >= 8) {
          return;
        }
        ExitAd currentExitAd = mShowExitAds.get(i);
        final MediaInfo currentMediaInfo = currentExitAd.mMediaInfo;
        final String posId = currentExitAd.mAdPosId;

        if (!TextUtils.isEmpty(currentMediaInfo.getSource())) {
          SimpleDraweeView simpleDraweeView = imageList.get(i);
          Uri uri1 = Uri.parse(currentMediaInfo.getSource());
          ImageDecodeOptions decodeOptions1 = ImageDecodeOptions.newBuilder().setDecodeAllFrames(true).build();
          ImageRequest request1 = ImageRequestBuilder.newBuilderWithSource(uri1).setImageDecodeOptions(decodeOptions1).build();
          DraweeController draweeController1 = Fresco.newDraweeControllerBuilder().setImageRequest(request1).setAutoPlayAnimations(true).build();
          simpleDraweeView.setController(draweeController1);
          RelativeLayout itemContainLayout = array.get(i);
          final int j = i;
          itemContainLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
              if (mListener != null) {
                mListener.onItemClick(j, currentMediaInfo);
              }
              // NOTE(ljs):Skiptype 可能 = null
              if ("1".equals(currentMediaInfo.getSkiptype()) || "2".equals(currentMediaInfo.getSkiptype()) || "3".equals(currentMediaInfo.getSkiptype())) {
              // NOTE(gxk):避免一直重新开启新的页面或下载
            	  if(handler == null){ 
                 handler = new MediaHandler();
            	 }
                handler.handlerMediaInfoSkip(ExitAdView.class.getSimpleName(), mContext, currentMediaInfo, currentMediaInfo.getSkiptype(), true);
                reportManager.report(currentMediaInfo.getReportvalue(), 1, ReportManager.Start, mReportUrl, posId.substring(0, 2));
								Lg.d("直播退出推荐位,片单有二级跳转,汇报");
							} else {
								Lg.d("直播退出推荐位,片单没有二级跳转,不汇报");
              }
            }
          });
          AlwaysMarqueeTextView alwaysMarqueeTextView = textList.get(i);
          alwaysMarqueeTextView.setText(currentMediaInfo.getName());
        }
      }
    }
  }

  public void setReportUrl(String reportUrl) {
    mReportUrl = reportUrl;
  }

  public static class ExitAd {
    public String mAdPosId;
    public MediaInfo mMediaInfo;

    public ExitAd(String adPosId, MediaInfo mediaInfo) {
      super();
      mAdPosId = adPosId;
      mMediaInfo = mediaInfo;
    }
  }
}
