package com.vad.sdk.core.view.v30;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RelativeLayout;
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
import com.vad.sdk.core.VAdType;
import com.vad.sdk.core.Utils.v30.DisplayManagers;
import com.vad.sdk.core.Utils.v30.Lg;
import com.vad.sdk.core.base.MediaInfo;

public class ADView extends RelativeLayout {
  private Context mContext;
  private boolean isTextView = false;
  private CustomMarqueeView mCustomMarqueeView;

  public ADView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    mContext = context;
  }

  public ADView(Context context, AttributeSet attrs) {
    super(context, attrs);
    mContext = context;
  }

  public ADView(Context context) {
    super(context);
    mContext = context;
  }

  public void setAdMedia(MediaInfo info) {
    // FIXME(ljs):当viewType=2时,并没有对jpg和gif做处理,需要修复,目前建议server端直接把gif放在网页里展示
    if (VAdType.AD_MEDIA_TYPE_PIC.equals(info.getViewtype())) {// =2,图片
      int width = Integer.valueOf(!TextUtils.isEmpty(info.getWidth()) ? info.getWidth() : "0");
      int height = Integer.valueOf(!TextUtils.isEmpty(info.getHeight()) ? info.getHeight() : "0");
      int position = Integer.valueOf(!TextUtils.isEmpty(info.getMediapos()) ? info.getMediapos() : "9");
      showImage(width, height, position, info.getSource(), info);
      // FIXME(ljs):内嵌
      if (!TextUtils.isEmpty(info.getInnersource())) {
        int Emeddedwidth = Integer.valueOf(!TextUtils.isEmpty(info.getInnerwidth()) ? info.getInnerwidth() : "0");
        int Emeddedheight = Integer.valueOf(!TextUtils.isEmpty(info.getInnerheight()) ? info.getInnerheight() : "0");
        int EmbeddedImagePosition = Integer.valueOf(!TextUtils.isEmpty(info.getInnermediapos()) ? info.getInnermediapos() : "9");
        String textPosition = info.getInnernamepos();
        String innerText = info.getInnercontent();
        String url = info.getInnersource();
        showEmbedded(Emeddedwidth, Emeddedheight, EmbeddedImagePosition, textPosition, innerText, url);
      }
    } else if (VAdType.AD_MEDIA_TYPE_TEXT.equals(info.getViewtype())) {// =3,文字
      String text = info.getSource();
      showText(text, info);
    } else if (VAdType.AD_MEDIA_TYPE_WEB.equals(info.getViewtype())) {// =4,网页
      String url = info.getSource();
      // FIXME(ljs):待修复String url = "www.baidu.com";
      showWebView(url);
    }
  }

  public void setShowCountDownHint(boolean isShowCountDown) {

  }

  public void onTimeTick(int time) {

  }

  /**
   *
   * @param EmbeddedWidth
   *          内嵌宽
   * @param EmbeddedHeight
   *          内嵌高
   * @param EmbeddedImagePosition
   *          内嵌图片位置
   * @param textPosition
   *          内嵌文字位置
   * @param innerText
   *          内嵌文字内容
   * @param url
   *          内嵌图片地址
   */
  @SuppressLint("NewApi")
  private void showEmbedded(int EmbeddedWidth, int EmbeddedHeight, int EmbeddedImagePosition, String textPosition, String innerText, String url) {
    int EmbeddedID = 10000001;
    SimpleDraweeView embeddedimageView = new SimpleDraweeView(getContext());
    embeddedimageView.setId(EmbeddedID);
    // 设置图片属性
    GenericDraweeHierarchyBuilder embeddedbuilder = new GenericDraweeHierarchyBuilder(getResources());
    GenericDraweeHierarchy embeddedhierarchy = embeddedbuilder.setFadeDuration(500).build();
    embeddedhierarchy.setActualImageScaleType(ScaleType.FIT_XY);
    embeddedimageView.setHierarchy(embeddedhierarchy);
    LayoutParams embeddedlayoutParams = new RelativeLayout.LayoutParams(EmbeddedWidth, EmbeddedHeight);
    embeddedlayoutParams = setSudokuParams(EmbeddedImagePosition, embeddedlayoutParams);
    embeddedimageView.setLayoutParams(embeddedlayoutParams);
    // 设置图片
    Uri uri = Uri.parse(url);
    ImageDecodeOptions decodeOptions = ImageDecodeOptions.newBuilder().setDecodeAllFrames(true).build();
    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).setImageDecodeOptions(decodeOptions).build();
    DraweeController draweeController = Fresco.newDraweeControllerBuilder().setImageRequest(request).setAutoPlayAnimations(true).build();
    embeddedimageView.setController(draweeController);

    TextView textView = new TextView(getContext());
    LayoutParams textlayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    if ((Integer.valueOf(TextUtils.isEmpty(textPosition) ? "10" : textPosition)) % 2 == 0) {
      textlayoutParams.addRule(RelativeLayout.ALIGN_TOP, EmbeddedID);
      textlayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, EmbeddedID);
      textView.setEms(1);
      textView.setSingleLine(false);
      if ("10".equals(textPosition)) {
        textlayoutParams.addRule(RelativeLayout.LEFT_OF, EmbeddedID);
      } else if ("12".equals(textPosition)) {
        textlayoutParams.addRule(RelativeLayout.RIGHT_OF, EmbeddedID);
      }
    } else {
      textlayoutParams.addRule(RelativeLayout.ALIGN_RIGHT, EmbeddedID);
      textlayoutParams.addRule(RelativeLayout.ALIGN_LEFT, EmbeddedID);
      if ("11".equals(textPosition)) {
        textlayoutParams.addRule(RelativeLayout.ABOVE, EmbeddedID);
      } else if ("13".equals(textPosition)) {
        textlayoutParams.addRule(RelativeLayout.BELOW, EmbeddedID);
      }
    }
    textView.setLayoutParams(textlayoutParams);

    textView.setText(innerText);
    textView.setTextColor(Color.RED);
    addView(textView);
    addView(embeddedimageView);
  }

  /**
   *
   * @param Width
   *          宽
   * @param Height
   *          高
   * @param position
   *          位置
   * @param url
   *          图片地址
   * @param info
   */
  private void showImage(int Width, int Height, int position, String url, MediaInfo info) {
    DisplayManagers.getInstance().init(getContext());
    SimpleDraweeView imageView = new SimpleDraweeView(getContext());
    imageView.setTag("item_image");
    // 设置图片属性
    GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
    GenericDraweeHierarchy hierarchy = builder.setFadeDuration(500).setProgressBarImage(new CustomProgressBar()).build();
    hierarchy.setActualImageScaleType(ScaleType.FIT_XY);
    imageView.setHierarchy(hierarchy);
    // this.setonClick(imageView, info);
    int showImageWidth;
    int showImageHeight;
    if (Width == 0 && Height == 0) {
      if (position == 10) {
        showImageWidth = DisplayManagers.getInstance().changeWidthSize(800);
        showImageHeight = DisplayManagers.getInstance().changeHeightSize(480);
      } else {
        showImageWidth = DisplayManagers.getInstance().changeWidthSize(540);
        showImageHeight = DisplayManagers.getInstance().changeHeightSize(360);
      }
    } else {
      showImageWidth = DisplayManagers.getInstance().changeWidthSize(Width);
      showImageHeight = DisplayManagers.getInstance().changeHeightSize(Height);
    }
    LayoutParams imageViewparams = new LayoutParams(showImageWidth, showImageHeight);
    // imageViewparams = changeSudoku(position, imageViewparams);
    imageView.setLayoutParams(imageViewparams);
    // 设置图片
    Uri uri = Uri.parse(url);
    ImageDecodeOptions decodeOptions = ImageDecodeOptions.newBuilder().setDecodeAllFrames(true).build();
    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).setImageDecodeOptions(decodeOptions).build();
    DraweeController draweeController = Fresco.newDraweeControllerBuilder().setImageRequest(request).setAutoPlayAnimations(true).setControllerListener(new BaseControllerListener<ImageInfo>() {
      @Override
      public void onSubmit(String id, Object callerContext) {
        super.onSubmit(id, callerContext);
        Lg.e("ADView , showImage()#onSubmit()");
      }

      @Override
      public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
        super.onFinalImageSet(id, imageInfo, animatable);
        Lg.e("ADView , showImage()#onFinalImageSet()");
      }

      @Override
      public void onFailure(String id, Throwable throwable) {
        super.onFailure(id, throwable);
        Lg.e("ADView , showImage()#onFailure() , id = " + id + " , e = " + throwable.getMessage());
      }
    }).build();
    imageView.setController(draweeController);
    addView(imageView);
  }

  private void showText(String text, MediaInfo info) {
    if (text != null) {
      isTextView = true;
      setGravity(Gravity.RIGHT);
      setBackgroundColor(Color.parseColor("#4b000000"));
      setPadding(0, 0, 0, DisplayManagers.dip2px(getContext(), 30));
      LayoutParams layoutParames = (LayoutParams) new LayoutParams(LayoutParams.MATCH_PARENT, DisplayManagers.dip2px(mContext, 70));
      layoutParames.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      setLayoutParams(layoutParames);
      //
      mCustomMarqueeView = new CustomMarqueeView(mContext);
      mCustomMarqueeView.setText(text);
      addView(mCustomMarqueeView);
    }
  }

  /**
   * 停止走马灯
   */
  public void stopAd() {
    if (isTextView) {
      isTextView = false;
      mCustomMarqueeView.stopScroll();
    }
  }

  private void showWebView(String url) {
    LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    AdWebView mDialogWv = new AdWebView(getContext(), lp);
    this.addView(mDialogWv);
    mDialogWv.setFocusable(false);
    mDialogWv.setFocusableInTouchMode(false);
    if (url.length() > 4 && "http".equals(url.subSequence(0, 4))) {
      mDialogWv.loadUrl(url);
    } else {
      mDialogWv.loadUrl("http://" + url);
    }

  }

  /**
   * 九宫格位置配制
   *
   * @param position
   *          所放位置
   * @return
   */
  public RelativeLayout.LayoutParams setSudokuParams(int position, LayoutParams params) {
    switch (position) {
    case 1:
      params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
      params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      return params;
    case 2:
      params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
      params.addRule(RelativeLayout.CENTER_IN_PARENT);
      return params;
    case 3:
      params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
      params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      return params;
    case 4:
      params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      params.addRule(RelativeLayout.CENTER_IN_PARENT);
      return params;
    case 5:
      params.addRule(RelativeLayout.CENTER_IN_PARENT);
      return params;
    case 6:
      params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      params.addRule(RelativeLayout.CENTER_IN_PARENT);
      return params;
    case 7:
      params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      return params;
    case 8:
      params.addRule(RelativeLayout.CENTER_IN_PARENT);
      params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      return params;
    case 9:
      params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      return params;
    default:
      params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      return params;
    }
  }
}
