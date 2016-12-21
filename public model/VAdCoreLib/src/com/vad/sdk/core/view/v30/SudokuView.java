package com.vad.sdk.core.view.v30;

import java.util.Timer;
import java.util.TimerTask;

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
import com.vad.sdk.core.Utils.v30.Utils;
import com.vad.sdk.core.base.MediaInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 *
 * @version 1
 * @author zhangzexin
 * @since 2015-8-25 下午4:38:21
 */

public class SudokuView extends RelativeLayout {
  private Context context;
  private String url;
  private int second;
  private int position = 5;
  private int mWidth;
  private int mHeight;
  private GenericDraweeHierarchyBuilder builder;
  private GenericDraweeHierarchy hierarchy;
  private GenericDraweeHierarchyBuilder embeddedbuilder;
  private GenericDraweeHierarchy embeddedhierarchy;
  private DraweeController draweeController;
  private SimpleDraweeView imageView;
  private Timer timer;
  private boolean isText = true;

  @SuppressLint("NewApi")
  public SudokuView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    // TODO Auto-generated constructor stub
  }

  public SudokuView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    // TODO Auto-generated constructor stub
  }

  public SudokuView(Context context, int i, boolean isText) {
    super(context);
    this.context = context;
    position = i;
    this.isText = isText;
    init();
  }

  public SudokuView(Context context, int width, int height, int i, boolean isText) {
    super(context);
    this.context = context;
    position = i;
    this.isText = isText;
    mWidth = width;
    mHeight = height;
    init();
  }

  public SudokuView(Context context) {
    super(context);
    this.context = context;
    init();
  }

  private static final int ID_BTN1 = 1;
  private TextView mTimerText;

  private void init() {
    Lg.d("SudokuView , init() , position = " + position);
    LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    setLayoutParams(params);
    if (mWidth == 0 && mHeight == 0) {
      showImageWidth = DisplayManagers.getInstance().changeWidthSize(400);
      showImageHeight = DisplayManagers.getInstance().changeHeightSize(240);
    } else {
      showImageWidth = DisplayManagers.getInstance().changeWidthSize(mWidth);
      showImageHeight = DisplayManagers.getInstance().changeHeightSize(mHeight);
    }
    Lg.d("SudokuView , init() , showImageWidth = " + showImageWidth + " , showImageHeight = " + showImageHeight);
    params1 = new RelativeLayout.LayoutParams(showImageWidth, showImageHeight);
    int a = DisplayManagers.dip2px(context, 50);
    setPadding(DisplayManagers.getInstance().changeWidthSize(a), DisplayManagers.getInstance().changeWidthSize(a), DisplayManagers.getInstance().changeWidthSize(a), DisplayManagers.getInstance()
        .changeWidthSize(a));
    switch (position) {
    case 1:
      params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      break;
    case 2:
      params1.addRule(RelativeLayout.CENTER_IN_PARENT);
      params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
      break;
    case 3:
      params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      break;
    case 4:
      params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      params1.addRule(RelativeLayout.CENTER_IN_PARENT);
      break;
    case 5:
      params1.addRule(RelativeLayout.CENTER_IN_PARENT);
      break;
    case 6:
      params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      params1.addRule(RelativeLayout.CENTER_IN_PARENT);
      break;
    case 7:
      params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      break;
    case 8:
      params1.addRule(RelativeLayout.CENTER_IN_PARENT);
      params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      break;
    case 9:
      params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      break;
    case 10:
      int ImageWidth = DisplayManagers.getInstance().changeWidthSize(800);
      int ImageHeight = DisplayManagers.getInstance().changeHeightSize(480);
      params1 = new RelativeLayout.LayoutParams(ImageWidth, ImageHeight);
      params1.addRule(RelativeLayout.CENTER_IN_PARENT);
      break;

    default:
      break;
    }

    imageView = new SimpleDraweeView(context);
    // imageView.setId(1);
    LayoutParams imag_params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    // imageView.setAspectRatio(0.45f);
    // imag_params.addRule(RelativeLayout.CENTER_IN_PARENT);
    imageView.setLayoutParams(imag_params);
    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    imageView.setTag("item_image");
    // 设置图片属性
    if (builder == null) {
      builder = new GenericDraweeHierarchyBuilder(getResources());
      hierarchy = builder.setFadeDuration(500).setProgressBarImage(new CustomProgressBar()).build();
      hierarchy.setActualImageScaleType(ScaleType.FIT_XY);
      hierarchy.setActualImageScaleType(ScaleType.CENTER_INSIDE);
      ;
    }
    // hierarchy.setPlaceholderImage(R.drawable.ic_launcher);//默认图片
    imageView.setHierarchy(hierarchy);

    mTimerText = new TextView(context);
    // RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
    // LayoutParams.WRAP_CONTENT);
    // lp4.addRule(RelativeLayout.ALIGN_RIGHT);
    // lp4.addRule(RelativeLayout.ALIGN_PARENT_TOP);

    mTimerText.setGravity(Gravity.CENTER);
    mTimerText.setBackgroundDrawable(Utils.createRoundedRectDrawable("#646463", 4, mTimerText.getHeight()));
    mTimerText.setTextColor(Color.WHITE);
    mTimerText.setTextSize(26);
    mTimerText.setVisibility(View.INVISIBLE);
    // 填充到RelativeLayout中
    relativeLayout = new RelativeLayout(context);
    relativeLayout.setId(ID_BTN1);
    LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    // imageView.setId(ID_BTN1);
    params2.addRule(RelativeLayout.ALIGN_TOP, ID_BTN1);
    // params2.addRule(RelativeLayout.ALIGN_LEFT, ID_BTN1);
    params2.addRule(RelativeLayout.ALIGN_RIGHT, ID_BTN1);
    params2.setMargins(0, DisplayManagers.dip2px(context, 20), DisplayManagers.dip2px(context, 16), 0);
    mTimerText.setLayoutParams(params2);
    // mTimerText.setPadding(DisplayManagers.GetInstance().dip2px(context, 15), 0,
    // DisplayManagers.GetInstance().dip2px(context, 15), 0);
    relativeLayout.addView(imageView);
    // SimpleDraweeView embedded = (SimpleDraweeView) embedded(5);
    addView(relativeLayout, params1);
    if (isText) {
      addView(mTimerText);
    }
    // embedded.setImageURI(Uri.parse("http://imgadmin.voole.com/img/new_img/2015/09/28/2015092817380151305.png"));
    // ininView();
  }

  public void setData(String Text, String url, final int second) {
    Lg.e("SudokuView , setData()");
    this.second = second;
    timer = new Timer();
    String[] Str = url.split(":");
    if ("http".equals(Str[0])) {
      uri = Uri.parse(url);
    } else {
      uri = new Uri.Builder().scheme("res").path(url).build();
    }
    ImageDecodeOptions decodeOptions = ImageDecodeOptions.newBuilder().setDecodeAllFrames(true).build();
    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).setImageDecodeOptions(decodeOptions).build();
    if (draweeController == null) {
      draweeController = Fresco.newDraweeControllerBuilder().setImageRequest(request).setAutoPlayAnimations(true).setControllerListener(new BaseControllerListener<ImageInfo>() {
        @Override
        public void onSubmit(String id, Object callerContext) {
          super.onSubmit(id, callerContext);
          Lg.e("SudokuView , setData() , onSubmit()");
        }

        @Override
        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
          super.onFinalImageSet(id, imageInfo, animatable);
          Lg.e("SudokuView , setData() , onFinalImageSet()");
          if (second != 0) {
            timer.schedule(task, 1000, 1000);
            mTimerText.setVisibility(View.VISIBLE);
          }
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
          Lg.e("SudokuView , setData() , onFailure()");
          mTimerText.setVisibility(View.GONE);
          timerText.RemoverView();
          super.onFailure(id, throwable);
        }
      }).build();
    }
    imageView.setController(draweeController);
    if (second != 0) {
      mTimerText.setText("广告剩余:" + String.valueOf(second));
    }

  }

  public void setDataEmbedded(String innerText, String url, final int second, int Position, String EmbeddedUrl, String TextPosition, MediaInfo mediaInfo) {
    Lg.d("SudokuView , setDataEmbedded()");
    if (!TextUtils.isEmpty(url)) {
      this.second = second;
      timer = new Timer();
      int w = Integer.valueOf(!TextUtils.isEmpty(mediaInfo.getInnerwidth()) ? mediaInfo.getInnerwidth() : "180");
      int h = Integer.valueOf(!TextUtils.isEmpty(mediaInfo.getInnerheight()) ? mediaInfo.getInnerheight() : "180");
      Lg.d("SudokuView , setDataEmbedded() , innerwidth = " + w + " , innerheight = " + h);
      final SimpleDraweeView embedded = (SimpleDraweeView) embedded(Position, TextPosition, innerText, w, h);
      if (EmbeddedUrl != null) {
        embedded.setVisibility(INVISIBLE);
        embedded.setImageURI(Uri.parse(EmbeddedUrl));
      }
      String[] Str = url.split(":");
      if ("http".equals(Str[0])) {
        uri = Uri.parse(url);
      } else {
        uri = new Uri.Builder().scheme("res").path(url).build();
      }
      ImageDecodeOptions decodeOptions = ImageDecodeOptions.newBuilder().setDecodeAllFrames(true).build();
      ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).setImageDecodeOptions(decodeOptions).build();
      if (draweeController == null) {
        draweeController = Fresco.newDraweeControllerBuilder().setImageRequest(request).setAutoPlayAnimations(true).setControllerListener(new BaseControllerListener<ImageInfo>() {
          @Override
          public void onSubmit(String id, Object callerContext) {
            super.onSubmit(id, callerContext);
            Lg.e("SudokuView , onSubmit()");
          }

          @Override
          public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
            super.onFinalImageSet(id, imageInfo, animatable);
            Lg.e("SudokuView , onFinalImageSet()");
            if (second != 0) {
              timer.schedule(task, 1000, 1000);
              mTimerText.setVisibility(View.VISIBLE);
              embedded.setVisibility(View.VISIBLE);
            }
          }

          @Override
          public void onFailure(String id, Throwable throwable) {
            super.onFailure(id, throwable);
            Lg.e("SudokuView , setDataEmbedded() , onFailure() throwable = " + throwable.getMessage());
            mTimerText.setVisibility(View.GONE);
            timerText.RemoverView();
          }
        }).build();
      }
      imageView.setController(draweeController);
      if (second != 0) {
        mTimerText.setText("广告剩余:" + String.valueOf(second));
      }
    }

  }

  TimerTask task = new TimerTask() {
    @Override
    public void run() {

      ((Activity) context).runOnUiThread(new Runnable() { // UI thread
            @Override
            public void run() {
              second--;
              mTimerText.setText("广告剩余:" + String.valueOf(second));
              timerText.getCurrentTime(second);
              if (second < 0) {
                timer.cancel();
                mTimerText.setVisibility(View.GONE);
                timerText.RemoverView();
              }
            }
          });
    }
  };

  public interface TimerText {
    public void RemoverView();

    public void getCurrentTime(int currentTime);
  }

  private TimerText timerText;
  private LayoutParams params1;
  private RelativeLayout relativeLayout;
  private Uri uri;
  private int showImageWidth;
  private int showImageHeight;
  private SimpleDraweeView embeddedimageView;

  public void setOnTimerTextListener(TimerText timerText) {
    this.timerText = timerText;
  }

  public void StopTask() {
    if (timer != null) {
      timer.cancel();
      timer = null;
    }
    if (task != null) {
      task.cancel();
      task = null;
    }
  }

  public RelativeLayout getRelativeLayout() {
    return relativeLayout;
  }

  @SuppressLint("NewApi")
  private View embedded(int n, String textPosition, String innerText, int w, int h) {
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
    embeddedimageView = new SimpleDraweeView(context);
    embeddedimageView.setId(1);
    // imageView.setAspectRatio(0.45f);
    // imag_params.addRule(RelativeLayout.CENTER_IN_PARENT);
    embeddedimageView.setLayoutParams(embeddedlayoutParams);
    embeddedimageView.setScaleType(ImageView.ScaleType.FIT_XY);
    // 设置图片属性
    if (embeddedbuilder == null) {
      embeddedbuilder = new GenericDraweeHierarchyBuilder(getResources());
      embeddedhierarchy = embeddedbuilder.setFadeDuration(500).build();
      embeddedhierarchy.setActualImageScaleType(ScaleType.FIT_XY);
      embeddedhierarchy.setActualImageScaleType(ScaleType.CENTER_INSIDE);
      ;
    }
    // hierarchy.setPlaceholderImage(R.drawable.ic_launcher);//默认图片
    embeddedimageView.setHierarchy(embeddedhierarchy);
    embeddedimageView.setId(1);
    TextView textView = new TextView(context);
    if ((Integer.valueOf(TextUtils.isEmpty(textPosition) ? "10" : textPosition)) % 2 == 0) {
      LayoutParams textlayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
      textlayoutParams.addRule(RelativeLayout.ALIGN_TOP, embeddedimageView.getId());
      textlayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, embeddedimageView.getId());
      textView.setEms(1);
      textView.setSingleLine(false);
      if ("10".equals(textPosition)) {
        textlayoutParams.addRule(RelativeLayout.LEFT_OF, embeddedimageView.getId());
      } else if ("12".equals(textPosition)) {
        textlayoutParams.addRule(RelativeLayout.RIGHT_OF, embeddedimageView.getId());
      }
      textView.setLayoutParams(textlayoutParams);
    } else {
      LayoutParams textlayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
      textlayoutParams.addRule(RelativeLayout.ALIGN_RIGHT, embeddedimageView.getId());
      textlayoutParams.addRule(RelativeLayout.ALIGN_LEFT, embeddedimageView.getId());
      if ("11".equals(textPosition)) {
        textlayoutParams.addRule(RelativeLayout.ABOVE, embeddedimageView.getId());
      } else if ("13".equals(textPosition)) {
        textlayoutParams.addRule(RelativeLayout.BELOW, embeddedimageView.getId());
      }
      textView.setLayoutParams(textlayoutParams);
    }
    textView.setText(innerText);
    textView.setTextColor(Color.WHITE);
    relativeLayout.addView(textView);
    relativeLayout.addView(embeddedimageView);
    return embeddedimageView;
  }
}
