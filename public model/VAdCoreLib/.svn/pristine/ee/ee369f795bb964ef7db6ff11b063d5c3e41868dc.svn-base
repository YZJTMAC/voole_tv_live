package com.vad.sdk.core.view.v30;

import java.util.HashMap;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils.ScaleType;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.vad.sdk.core.R;
import com.vad.sdk.core.VAdType;
import com.vad.sdk.core.Utils.v30.Lg;
import com.vad.sdk.core.base.AdPos;
import com.vad.sdk.core.base.MediaInfo;
import com.vad.sdk.core.controller.v30.AdController;
import com.vad.sdk.core.manager.MediaHandler;
import com.vad.sdk.core.report.v30.ReportManager;

/**
 *
 * 大视野推荐位.
 *
 * @author luojunsheng
 * @date 2016年8月4日 下午3:08:47
 * @version 1.1
 */
public class RecommendedADView extends LinearLayout {
  private SimpleDraweeView imageView1;
  private SimpleDraweeView imageView2;
  private SimpleDraweeView imageView3;
  private SimpleDraweeView imageView4;
  private Context mContext;
  private LinearLayout itmelinearLayout1;
  private LinearLayout itmelinearLayout2;
  private LinearLayout itmelinearLayout3;
  private LinearLayout itmelinearLayout4;
  private String mReportUrl;
  private LinearLayout oulitmelinearLayout;
  private ReportManager reportManager;
  private int mCurrentPosition = 0;
  private HashMap<String, AdPos> hashMap;

  public RecommendedADView(Context context) {
    super(context);
    mContext = context;
    initView(context);
  }

  private void initView(Context context) {
    Lg.d("RecommendedADView , initView()");
    reportManager = ReportManager.getInstance();
    // LinearLayout linearLayout = new LinearLayout(context);
    // this.setBackgroundColor(Color.BLACK);
    setOrientation(HORIZONTAL);
    LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    setLayoutParams(layoutParams);
    itmelinearLayout1 = new LinearLayout(context);
    itmelinearLayout2 = new LinearLayout(context);
    itmelinearLayout3 = new LinearLayout(context);
    itmelinearLayout4 = new LinearLayout(context);
    // 隐藏控件
    itmelinearLayout1.setVisibility(INVISIBLE);
    itmelinearLayout2.setVisibility(INVISIBLE);
    itmelinearLayout3.setVisibility(INVISIBLE);
    itmelinearLayout4.setVisibility(INVISIBLE);

    LayoutParams itmelayoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
    itmelayoutParams.setMargins(1, 1, 1, 1);
    itmelinearLayout1.setPadding(0, 0, 0, 0);
    itmelinearLayout1.setLayoutParams(itmelayoutParams);
    itmelinearLayout2.setLayoutParams(itmelayoutParams);
    itmelinearLayout3.setLayoutParams(itmelayoutParams);
    itmelinearLayout4.setLayoutParams(itmelayoutParams);
    // 配制选择器
    // StateListDrawable drawable2 = new StateListDrawable();
    // drawable2.addState(new int[]{android.R.attr.state_pressed},
    // getResources().getDrawable(android.R.color.transparent));
    // drawable2.addState(new
    // int[]{android.R.attr.state_selected},getResources().getDrawable(R.drawable.dashiye));
    // drawable2.addState(new
    // int[]{android.R.attr.state_focused},getResources().getDrawable(R.drawable.dashiye));
    // drawable2.addState(new int[]{android.R.attr.state_window_focused},
    // getResources().getDrawable(android.R.color.transparent));
    // drawable2.addState(new int[0], getResources().getDrawable(R.drawable.dashiye));
    // 设置item背景选择器
    Drawable drawable1 = getResources().getDrawable(R.drawable.dashiye_selector);
    itmelinearLayout1.setBackgroundDrawable(drawable1);
    itmelinearLayout1.setClickable(true);
    itmelinearLayout1.setFocusable(true);
    itmelinearLayout1.setFocusableInTouchMode(true);
    // itmelinearLayout1.requestFocus();
    // itmelinearLayout1.requestFocusFromTouch();
    Drawable drawable2 = getResources().getDrawable(R.drawable.dashiye_selector);
    itmelinearLayout2.setBackgroundDrawable(drawable2);
    itmelinearLayout2.setClickable(true);
    itmelinearLayout2.setFocusable(true);
    itmelinearLayout2.setFocusableInTouchMode(true);
    Drawable drawable3 = getResources().getDrawable(R.drawable.dashiye_selector);
    itmelinearLayout3.setBackgroundDrawable(drawable3);
    itmelinearLayout3.setClickable(true);
    itmelinearLayout3.setFocusable(true);
    itmelinearLayout3.setFocusableInTouchMode(true);
    Drawable drawable4 = getResources().getDrawable(R.drawable.dashiye_selector);
    itmelinearLayout4.setBackgroundDrawable(drawable4);
    itmelinearLayout4.setClickable(true);
    itmelinearLayout4.setFocusable(true);
    itmelinearLayout4.setFocusableInTouchMode(true);
    // 设置内容居中
    itmelinearLayout1.setGravity(Gravity.CENTER);
    itmelinearLayout2.setGravity(Gravity.CENTER);
    itmelinearLayout3.setGravity(Gravity.CENTER);
    itmelinearLayout4.setGravity(Gravity.CENTER);
    imageView1 = new SimpleDraweeView(context);
    imageView2 = new SimpleDraweeView(context);
    imageView3 = new SimpleDraweeView(context);
    imageView4 = new SimpleDraweeView(context);
    LayoutParams imag_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    // imag_params.addRule(RelativeLayout.CENTER_IN_PARENT);
    imageView1.setLayoutParams(imag_params);
    imageView2.setLayoutParams(imag_params);
    imageView3.setLayoutParams(imag_params);
    imageView4.setLayoutParams(imag_params);
    // imageView.setScaleType(ImageView.ScaleType.FIT_XY);

    // Uri uri1 = Uri.parse("http://imga1.pic21.com/bizhi/131220/05508/s11.jpg");
    // Uri uri2 =
    // Uri.parse("http://c.hiphotos.baidu.com/zhidao/pic/item/c75c10385343fbf2d8a16ab4b07eca8064388f9c.jpg");
    // Uri uri3 =
    // Uri.parse("http://f.hiphotos.baidu.com/image/pic/item/8d5494eef01f3a29a8b0ef3e9b25bc315d607cc1.jpg");
    // Uri uri4 = Uri.parse("http://pic34.nipic.com/20131105/12314404_095513513171_2.jpg");
    // 设置边角角度
    RoundingParams roundingParams = RoundingParams.fromCornersRadius(13.4f);

    // 第一个
    PointF focusPoint = new PointF(0.5f, 0.5f);
    GenericDraweeHierarchyBuilder builder1 = new GenericDraweeHierarchyBuilder(getResources());
    GenericDraweeHierarchy hierarchy1 = builder1.setFadeDuration(500).setActualImageFocusPoint(focusPoint).setActualImageScaleType(ScaleType.FOCUS_CROP).setRoundingParams(roundingParams).build();
    // hierarchy1.setActualImageScaleType(ScaleType.FIT_XY);
    imageView1.setHierarchy(hierarchy1);
    // 第二个
    GenericDraweeHierarchyBuilder builder2 = new GenericDraweeHierarchyBuilder(getResources());
    GenericDraweeHierarchy hierarchy2 = builder2.setFadeDuration(500).setRoundingParams(roundingParams).build();
    // hierarchy2.setActualImageScaleType(ScaleType.FIT_XY);
    imageView2.setHierarchy(hierarchy2);
    imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
    // 第三个
    GenericDraweeHierarchyBuilder builder3 = new GenericDraweeHierarchyBuilder(getResources());
    GenericDraweeHierarchy hierarchy3 = builder3.setFadeDuration(500).setRoundingParams(roundingParams).build();
    // hierarchy3.setActualImageScaleType(ScaleType.FIT_XY);
    imageView3.setHierarchy(hierarchy3);
    imageView3.setScaleType(ImageView.ScaleType.FIT_XY);
    // 第四个
    GenericDraweeHierarchyBuilder builder4 = new GenericDraweeHierarchyBuilder(getResources());
    GenericDraweeHierarchy hierarchy4 = builder4.setFadeDuration(500).setRoundingParams(roundingParams).build();
    // hierarchy4.setActualImageScaleType(ScaleType.FIT_XY);
    imageView4.setHierarchy(hierarchy4);
    imageView4.setScaleType(ImageView.ScaleType.FIT_XY);

    // 添加到平分的linearLayout中
    itmelinearLayout1.addView(imageView1);
    itmelinearLayout2.addView(imageView2);
    itmelinearLayout3.addView(imageView3);
    itmelinearLayout4.addView(imageView4);

    // hierarchy.setPlaceholderImage(R.drawable.ic_launcher);//默认图片
    addView(itmelinearLayout1);
    addView(itmelinearLayout2);
    addView(itmelinearLayout3);
    addView(itmelinearLayout4);
  }

  public synchronized void setData(HashMap<String, AdPos> hashMap) {
    Lg.d("RecommendedADView , setData()");
    LinearLayout itmelinearLayout = null;
    // NOTE(ljs):只有配齐4个广告的时候才开始真正展示
    if (hashMap.size() == 4) {
      String[] poss = { VAdType.AD_DSY_1, VAdType.AD_DSY_2, VAdType.AD_DSY_3, VAdType.AD_DSY_4 };
      for (int j = 0; j < hashMap.size(); j++) {
        AdPos adInfos = hashMap.get(poss[j]);
        if (adInfos != null) {
          if (mCurrentPosition == 0) {
            if (adInfos.mediaInfoList != null && adInfos.mediaInfoList.get(0) != null && !TextUtils.isEmpty(adInfos.mediaInfoList.get(0).getSource())) {
              Uri uri1 = Uri.parse(adInfos.mediaInfoList.get(0).getSource());
              ImageDecodeOptions decodeOptions1 = ImageDecodeOptions.newBuilder().setDecodeAllFrames(true).build();
              ImageRequest request1 = ImageRequestBuilder.newBuilderWithSource(uri1).setImageDecodeOptions(decodeOptions1).build();
              DraweeController draweeController1 = Fresco.newDraweeControllerBuilder().setImageRequest(request1).setAutoPlayAnimations(true).build();
              imageView1.setController(draweeController1);
              itmelinearLayout1.setVisibility(VISIBLE);
              itmelinearLayout = itmelinearLayout1;
              reportManager.report(adInfos.mediaInfoList.get(0).getReportvalue(), 0, ReportManager.Start, mReportUrl, adInfos.id.substring(0, 2));
              setClick(itmelinearLayout, adInfos);
              mCurrentPosition++;
            } else {
              reportManager.report(adInfos.mediaInfoList.get(0).getReportvalue(), 0, ReportManager.Start, mReportUrl, adInfos.id.substring(0, 2));
            }
          } else if (mCurrentPosition == 1) {
            if (adInfos.mediaInfoList != null && adInfos.mediaInfoList.get(0) != null && !TextUtils.isEmpty(adInfos.mediaInfoList.get(0).getSource())) {
              Uri uri2 = Uri.parse(adInfos.mediaInfoList.get(0).getSource());
              ImageDecodeOptions decodeOptions2 = ImageDecodeOptions.newBuilder().setDecodeAllFrames(true).build();
              ImageRequest request2 = ImageRequestBuilder.newBuilderWithSource(uri2).setImageDecodeOptions(decodeOptions2).build();
              DraweeController draweeController2 = Fresco.newDraweeControllerBuilder().setImageRequest(request2).setAutoPlayAnimations(true).build();
              imageView2.setController(draweeController2);
              itmelinearLayout2.setVisibility(VISIBLE);
              itmelinearLayout = itmelinearLayout2;
              reportManager.report(adInfos.mediaInfoList.get(0).getReportvalue(), 0, ReportManager.Start, mReportUrl, adInfos.id.substring(0, 2));
              setClick(itmelinearLayout, adInfos);
              mCurrentPosition++;
            } else {
              reportManager.report(adInfos.mediaInfoList.get(0).getReportvalue(), 0, ReportManager.Start, mReportUrl, adInfos.id.substring(0, 2));
            }
          } else if (mCurrentPosition == 2) {
            if (adInfos.mediaInfoList != null && adInfos.mediaInfoList.get(0) != null && !TextUtils.isEmpty(adInfos.mediaInfoList.get(0).getSource())) {
              Uri uri3 = Uri.parse(adInfos.mediaInfoList.get(0).getSource());
              ImageDecodeOptions decodeOptions3 = ImageDecodeOptions.newBuilder().setDecodeAllFrames(true).build();
              ImageRequest request3 = ImageRequestBuilder.newBuilderWithSource(uri3).setImageDecodeOptions(decodeOptions3).build();
              DraweeController draweeController3 = Fresco.newDraweeControllerBuilder().setImageRequest(request3).setAutoPlayAnimations(true).build();
              imageView3.setController(draweeController3);
              itmelinearLayout3.setVisibility(VISIBLE);
              itmelinearLayout = itmelinearLayout3;
              reportManager.report(adInfos.mediaInfoList.get(0).getReportvalue(), 0, ReportManager.Start, mReportUrl, adInfos.id.substring(0, 2));
              setClick(itmelinearLayout, adInfos);
              mCurrentPosition++;
            } else {
              reportManager.report(adInfos.mediaInfoList.get(0).getReportvalue(), 0, ReportManager.Start, mReportUrl, adInfos.id.substring(0, 2));
            }
          } else if (mCurrentPosition == 3) {
            if (adInfos.mediaInfoList != null && adInfos.mediaInfoList.get(0) != null && !TextUtils.isEmpty(adInfos.mediaInfoList.get(0).getSource())) {
              Uri uri4 = Uri.parse(adInfos.mediaInfoList.get(0).getSource());
              ImageDecodeOptions decodeOptions4 = ImageDecodeOptions.newBuilder().setDecodeAllFrames(true).build();
              ImageRequest request4 = ImageRequestBuilder.newBuilderWithSource(uri4).setImageDecodeOptions(decodeOptions4).build();
              DraweeController draweeController4 = Fresco.newDraweeControllerBuilder().setImageRequest(request4).setAutoPlayAnimations(true).build();
              imageView4.setController(draweeController4);
              itmelinearLayout4.setVisibility(VISIBLE);
              itmelinearLayout = itmelinearLayout4;
              reportManager.report(adInfos.mediaInfoList.get(0).getReportvalue(), 0, ReportManager.Start, mReportUrl, adInfos.id.substring(0, 2));
              setClick(itmelinearLayout, adInfos);
              mCurrentPosition++;
            } else {
              reportManager.report(adInfos.mediaInfoList.get(0).getReportvalue(), 0, ReportManager.Start, mReportUrl, adInfos.id.substring(0, 2));
            }
          }
        }
      }
    }
  }

  private void setClick(LinearLayout itmelinearLayout, final AdPos adInfos) {
    if (itmelinearLayout != null && oulitmelinearLayout != itmelinearLayout) {
      itmelinearLayout.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          if (adInfos != null && adInfos.mediaInfoList != null && adInfos.mediaInfoList.get(0) != null) {
            MediaInfo mediaInfo = adInfos.mediaInfoList.get(0);
            String skipType = mediaInfo.getSkiptype();
            Lg.d("AdController , open() , skipType = " + skipType);
            MediaHandler handler = new MediaHandler();
            handler.handlerMediaInfoSkip(AdController.class.getSimpleName(), mContext, mediaInfo, skipType, true);
            return;
          }
          Lg.d("二级跳转汇报");
          reportManager.report(adInfos.mediaInfoList.get(0).getReportvalue(), 1, ReportManager.Start, mReportUrl, adInfos.id.substring(0, 2));
        }
      });
      oulitmelinearLayout = itmelinearLayout;
      itmelinearLayout = null;
    }
  }

  public HashMap<String, AdPos> getHashMap() {
    if (hashMap == null) {
      hashMap = new HashMap<String, AdPos>();
    }
    return hashMap;
  }

  public void setReportUrl(String reportUrl) {
    mReportUrl = reportUrl;
  }
}
