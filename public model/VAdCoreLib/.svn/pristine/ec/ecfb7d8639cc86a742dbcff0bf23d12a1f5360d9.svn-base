package com.vad.sdk.core.model.v30;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils.ScaleType;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.vad.sdk.core.Utils.v30.DisplayManagers;
import com.vad.sdk.core.Utils.v30.Lg;
import com.vad.sdk.core.report.v30.ReportManager;
import com.vad.sdk.core.view.v30.CustomProgressBar;

public class AdPosPauseListener extends AdPosBaseListener {
  boolean isView;
  private GenericDraweeHierarchyBuilder builder;
  private GenericDraweeHierarchy hierarchy;
  private RelativeLayout mContainerLayout;
  private DraweeController draweeController;
  private GenericDraweeHierarchyBuilder embeddedbuilder;
  private GenericDraweeHierarchy embeddedhierarchy;
  private RelativeLayout relativeLayout2;
  private String innernamepos;
  private ReportManager reportManager;

  public void showPauseAd() {
    Lg.e("AdPosPauseListener , showPauseAd()");
    reportManager = ReportManager.getInstance();
    if (!TextUtils.isEmpty(mAdPos.mediaInfoList.get(0).getSource())) {
      String name = mAdPos.mediaInfoList.get(0).getName();
      String source = mAdPos.mediaInfoList.get(0).getSource();
      Lg.i("AdPosPauseListener , showPauseAd() , source = " + source);
      mContainerLayout = new RelativeLayout(mViewGroup.getContext());
      mContainerLayout.setVisibility(View.INVISIBLE);
      LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
      mContainerLayout.setLayoutParams(params);

      SimpleDraweeView imageView = new SimpleDraweeView(mViewGroup.getContext());
      imageView.setId(1);
      float rate = 1.0f;
      if (mViewGroup != null) {
        DisplayManagers.getInstance().init(mViewGroup.getContext());
        // rate = mViewGroup.getWidth() * 1.0f / DisplayManagers.GetInstance().getScreenWidth();
        Lg.e("________rate = " + rate);
      }
      int showImageWidth = DisplayManagers.getInstance().changeWidthSize((int) (950 * rate));
      int showImageHeight = DisplayManagers.getInstance().changeHeightSize((int) (530 * rate));
      Lg.e("width = " + showImageWidth + " , height = " + showImageHeight + " , rate = " + rate);
      LayoutParams imagParams = new LayoutParams(showImageWidth, showImageHeight);
      imagParams.addRule(RelativeLayout.CENTER_IN_PARENT);
      imageView.setLayoutParams(imagParams);
      imageView.setScaleType(ImageView.ScaleType.FIT_XY);
      imageView.setTag("item_image");
      // 设置图片属性
      if (builder == null) {
        builder = new GenericDraweeHierarchyBuilder(mViewGroup.getContext().getResources());
        hierarchy = builder.setFadeDuration(500).setProgressBarImage(new CustomProgressBar()).build();
        hierarchy.setActualImageScaleType(ScaleType.FIT_XY);
      }
      // hierarchy.setPlaceholderImage(R.drawable.ic_launcher);//默认图片
      imageView.setHierarchy(hierarchy);
      relativeLayout2 = new RelativeLayout(mViewGroup.getContext());

      RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(showImageWidth, showImageHeight);
      RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
      relativeLayout2.setLayoutParams(params2);
      relativeLayout2.addView(imageView);
      // NOTE(ljs):内嵌广告
      if (mAdPos.mediaInfoList.get(0).getInnersource() != null) {
        int innermediapos = Integer.valueOf(!TextUtils.isEmpty(mAdPos.mediaInfoList.get(0).getInnermediapos()) ? mAdPos.mediaInfoList.get(0).getInnermediapos() : "9");
        int w = Integer.valueOf(!TextUtils.isEmpty(mAdPos.mediaInfoList.get(0).getInnerwidth()) ? mAdPos.mediaInfoList.get(0).getInnerwidth() : "180");
        int h = Integer.valueOf(!TextUtils.isEmpty(mAdPos.mediaInfoList.get(0).getInnerheight()) ? mAdPos.mediaInfoList.get(0).getInnerheight() : "180");
        SimpleDraweeView innerImgView = (SimpleDraweeView) embedded(innermediapos, w, h);
        // embedded.setImageURI(Uri.parse("http://imgadmin.voole.com/img/new_img/2015/09/28/2015092817380151305.png"));
        Uri uri = Uri.parse(mAdPos.mediaInfoList.get(0).getInnersource());
        ImageDecodeOptions decodeOptions = ImageDecodeOptions.newBuilder().setDecodeAllFrames(true).build();
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).setImageDecodeOptions(decodeOptions).build();
        DraweeController draweeController1 = Fresco.newDraweeControllerBuilder().setImageRequest(request).setAutoPlayAnimations(true).build();
        innerImgView.setController(draweeController1);
        // embedded.setImageURI(Uri.parse(mAdPos.mediaInfoList.get(0).getInnersource()));
      }
      params1.addRule(RelativeLayout.CENTER_IN_PARENT);
      mContainerLayout.addView(relativeLayout2, params1);
      mContainerLayout.setOnFocusChangeListener(new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
          mContainerLayout.bringToFront();
        }
      });
      Uri uri = Uri.parse(source);
      ImageDecodeOptions decodeOptions = ImageDecodeOptions.newBuilder().setDecodeAllFrames(true).build();
      ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).setImageDecodeOptions(decodeOptions).build();
      if (draweeController == null) {
        draweeController = Fresco.newDraweeControllerBuilder().setImageRequest(request).setAutoPlayAnimations(true).setControllerListener(new BaseControllerListener<ImageInfo>() {
          @Override
          public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
            super.onFinalImageSet(id, imageInfo, animatable);
            mContainerLayout.setVisibility(View.VISIBLE);
            reportManager.report(mAdPos.mediaInfoList.get(0).getReportvalue(), 0, ReportManager.Start, mReportUrl, mAdPos.id.substring(0, 2));
            Lg.e("AdPosPauseListener , showPauseAd()#onFinalImageSet , 暂停广告显示 , id = " + id + " , imageInfo = " + imageInfo);
            Lg.e("AdPosPauseListener , showPauseAd(), success to report");
          }

          @Override
          public void onFailure(String id, Throwable throwable) {
            super.onFailure(id, throwable);
            Lg.d("AdPosPauseListener , showPauseAd()#onFailure , 暂停广告展示失败 messsage = " + throwable.getMessage());
          }

          @Override
          public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
            super.onIntermediateImageSet(id, imageInfo);
            Lg.e("AdPosPauseListener , showPauseAd()#onIntermediateImageSet");
          }

          @Override
          public void onIntermediateImageFailed(String id, Throwable throwable) {
            super.onIntermediateImageFailed(id, throwable);
            Lg.e("AdPosPauseListener , showPauseAd()#onIntermediateImageFailed , message = " + throwable.getMessage());
          }
        }).build();
      }
      mViewGroup.addView(mContainerLayout);
      imageView.setController(draweeController);
      isView = true;
    } else {
      Lg.e("AdPosBaseListener , showPauseAd(), no ad to report");
      reportManager.report(mAdPos.mediaInfoList.get(0).getReportvalue(), 0, ReportManager.Start, mReportUrl, mAdPos.id.substring(0, 2));
    }
  }

  public void stop() {
    if (isView) {
      mViewGroup.removeView(mContainerLayout);
      isView = false;
    }
  }

  public void reset() {
    if (isView) {
      mViewGroup.removeView(mContainerLayout);
      isView = false;
    }
  }

  @SuppressLint("NewApi")
  private View embedded(int n, int w, int h) {
    LayoutParams embeddedlayoutParams = new RelativeLayout.LayoutParams(w, h);
    switch (n) {
    case 1:
      embeddedlayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      break;
    case 2:
      embeddedlayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
      embeddedlayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
      break;
    case 3:
      embeddedlayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      break;
    case 4:
      embeddedlayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      embeddedlayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
      break;
    case 5:
      DisplayManagers.getInstance().init(mViewGroup.getContext());
      embeddedlayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
      break;
    case 6:
      embeddedlayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      embeddedlayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
      break;
    case 7:
      embeddedlayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      embeddedlayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      break;
    case 8:
      embeddedlayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
      embeddedlayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      break;
    case 9:
      embeddedlayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      embeddedlayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      break;
    }
    SimpleDraweeView embeddedimageView = new SimpleDraweeView(mViewGroup.getContext());
    embeddedimageView.setId(4);
    // imageView.setAspectRatio(0.45f);
    // imag_params.addRule(RelativeLayout.CENTER_IN_PARENT);
    embeddedimageView.setLayoutParams(embeddedlayoutParams);
    // embeddedimageView.setScaleType(ImageView.ScaleType.FIT_XY);
    // 设置图片属性
    if (embeddedbuilder == null) {
      embeddedbuilder = new GenericDraweeHierarchyBuilder(mViewGroup.getContext().getResources());
      embeddedhierarchy = embeddedbuilder.setFadeDuration(500).setActualImageScaleType(ScaleType.CENTER_CROP).build();
      // embeddedhierarchy.setActualImageScaleType(ScaleType.FIT_XY);
      // embeddedhierarchy.setActualImageScaleType(ScaleType.CENTER_INSIDE);
      // embeddedimageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }
    // hierarchy.setPlaceholderImage(R.drawable.ic_launcher);//默认图片
    embeddedimageView.setHierarchy(embeddedhierarchy);
    if (TextUtils.isEmpty(mAdPos.mediaInfoList.get(0).getInnernamepos())) {
      innernamepos = "0";
    } else {
      innernamepos = mAdPos.mediaInfoList.get(0).getInnernamepos();
    }
    TextView textView = new TextView(mViewGroup.getContext());
    if ((Integer.valueOf(innernamepos) % 2) == 0) {
      LayoutParams textlayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
      textlayoutParams.addRule(RelativeLayout.ALIGN_TOP, embeddedimageView.getId());
      textlayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, embeddedimageView.getId());
      textView.setEms(1);
      textView.setSingleLine(false);
      if ("10".equals(innernamepos)) {
        textlayoutParams.addRule(RelativeLayout.LEFT_OF, embeddedimageView.getId());
      } else if ("12".equals(innernamepos)) {
        textlayoutParams.addRule(RelativeLayout.RIGHT_OF, embeddedimageView.getId());
      }
      textView.setLayoutParams(textlayoutParams);
    } else {
      LayoutParams textlayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
      textlayoutParams.addRule(RelativeLayout.ALIGN_RIGHT, embeddedimageView.getId());
      textlayoutParams.addRule(RelativeLayout.ALIGN_LEFT, embeddedimageView.getId());
      if ("11".equals(innernamepos)) {
        textlayoutParams.addRule(RelativeLayout.ABOVE, embeddedimageView.getId());
      } else if ("13".equals(innernamepos)) {
        textlayoutParams.addRule(RelativeLayout.BELOW, embeddedimageView.getId());
      }
      textView.setLayoutParams(textlayoutParams);
    }
    textView.setText(mAdPos.mediaInfoList.get(0).getInnercontent());
    textView.setTextColor(Color.WHITE);
    relativeLayout2.addView(textView);
    relativeLayout2.addView(embeddedimageView);
    return embeddedimageView;
  }
}
