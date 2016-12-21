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
import com.vad.sdk.core.base.AdPos;

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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 *
 * @version 1
 * @author zhangzexin
 * @since 2015-9-7 上午10:00:35
 */

public class EpgExitView extends RelativeLayout {
  private LayoutParams mRootParams;
  private RelativeLayout relativeLayout;
  private Context context;
  private String url;
  private int second = 10;
  private GenericDraweeHierarchyBuilder builder;
  private GenericDraweeHierarchy hierarchy;
  private DraweeController draweeController;
  private SimpleDraweeView imageView;
  private Timer timer;
  private int mPosition = 5;
  private int mWidth = -1;
  private int mHeight = -1;
  private boolean isText = true;
  private TimerText timerText;
  private static final int ID_BTN1 = 1;
  private TextView mTimerText;
  private GenericDraweeHierarchyBuilder embeddedbuilder;
  private GenericDraweeHierarchy embeddedhierarchy;
  private SimpleDraweeView embedded;
  //
  private ViewGroup mPlayerViewGroup;// 传递的VooleMediaPlayer

  public interface TimerText {
    public void removeView();

    public void success();

    public void getCurrentTime(int second);
  }

  public void setOnTimerTextListener(TimerText timerText) {
    this.timerText = timerText;
  }

  @SuppressLint("NewApi")
  public EpgExitView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public EpgExitView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
  }

  public EpgExitView(ViewGroup parent, Context context, int postion) {
    super(context);
    mPlayerViewGroup = parent;
    this.context = context;
    mPosition = postion;
    init();
  }

  public EpgExitView(Context context, int w, int h, int postion, boolean isText) {
    super(context);
    this.context = context;
    mPosition = postion;
    mWidth = w;
    mHeight = h;
    this.isText = isText;
    init();
  }

  public EpgExitView(Context context) {
    super(context);
    this.context = context;
    init();
  }

  private void init() {
    float rate = 1.0f;
    if (mPlayerViewGroup != null) {
      DisplayManagers.getInstance().init(context);
      rate = mPlayerViewGroup.getWidth() * 1.0f / DisplayManagers.getInstance().getScreenWidth();
      Lg.e("________rate = " + rate);
    }
    LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    setLayoutParams(params);
    if (mWidth == -1 && mHeight == -1) {
      mRootParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    } else {
      if (mWidth == 0 && mHeight == 0) {
        mWidth = DisplayManagers.getInstance().changeWidthSize((int) (950 * rate));
        mHeight = DisplayManagers.getInstance().changeHeightSize((int) (530 * rate));
      }
      mRootParams = new RelativeLayout.LayoutParams(mWidth, mHeight);
    }
    Lg.e("EpgExitView , init() , mPosition = " + mPosition);
    switch (mPosition) {
    case 0:
      mRootParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
      break;
    case 1:
      mRootParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      break;
    case 2:
      mRootParams.addRule(RelativeLayout.CENTER_IN_PARENT);
      mRootParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
      break;
    case 3:
      mRootParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      break;
    case 4:
      mRootParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      mRootParams.addRule(RelativeLayout.CENTER_IN_PARENT);
      break;
    case 5:
      // params1 = new
      // RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
      mRootParams.addRule(RelativeLayout.CENTER_IN_PARENT);
      break;
    case 6:
      mRootParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      mRootParams.addRule(RelativeLayout.CENTER_IN_PARENT);
      break;
    case 7:
      mRootParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      mRootParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      break;
    case 8:
      mRootParams.addRule(RelativeLayout.CENTER_IN_PARENT);
      mRootParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      break;
    case 9:
      mRootParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      mRootParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      break;
    case 10:
      int ImageWidth = DisplayManagers.getInstance().changeWidthSize((int) (950 * rate));
      int ImageHeight = DisplayManagers.getInstance().changeHeightSize((int) (530 * rate));
      mRootParams = new RelativeLayout.LayoutParams(ImageWidth, ImageHeight);
      mRootParams.addRule(RelativeLayout.CENTER_IN_PARENT);
      break;

    default:
      break;
    }

    imageView = new SimpleDraweeView(context);
    imageView.setId(1);
    LayoutParams imgParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    imgParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    imageView.setLayoutParams(imgParams);
    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    imageView.setTag("item_image");
    // 设置图片属性
    if (builder == null) {
      builder = new GenericDraweeHierarchyBuilder(getResources());
      hierarchy = builder.setFadeDuration(500).setProgressBarImage(new CustomProgressBar()).build();
      hierarchy.setActualImageScaleType(ScaleType.FIT_XY);
    }
    // hierarchy.setPlaceholderImage(R.drawable.ic_launcher);//默认图片
    imageView.setHierarchy(hierarchy);

    mTimerText = new TextView(context);
    RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    textParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    textParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
    mTimerText.setBackgroundDrawable(Utils.createRoundedRectDrawable("#9a000000", 4, mTimerText.getHeight()));
    mTimerText.setTextColor(Color.WHITE);
    mTimerText.setGravity(Gravity.CENTER);
    mTimerText.setPadding(DisplayManagers.dip2px(context, (int) (15 * rate)), DisplayManagers.dip2px(context, (int) (15 * rate)), DisplayManagers.dip2px(context, (int) (15 * rate)),
        DisplayManagers.dip2px(context, (int) (15 * rate)));
    mTimerText.setTextSize((int) (25 * rate));
    mTimerText.setLayoutParams(textParams);
    mTimerText.setVisibility(View.INVISIBLE);
    // 填充到RelativeLayout中
    relativeLayout = new RelativeLayout(context);
    LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    relativeLayout.setId(ID_BTN1);
    params2.addRule(RelativeLayout.ALIGN_TOP, ID_BTN1);
    params2.addRule(RelativeLayout.ALIGN_RIGHT, ID_BTN1);
    params2.setMargins(DisplayManagers.dip2px(context, (int) (16 * rate)), DisplayManagers.dip2px(context, (int) (13 * rate)), DisplayManagers.dip2px(context, (int) (16 * rate)), 0);
    relativeLayout.addView(imageView);
    addView(relativeLayout, mRootParams);
    if (isText) {
      addView(mTimerText, params2);
    }
    //
    if (mPlayerViewGroup != null) {
      mPlayerViewGroup.addOnLayoutChangeListener(new OnLayoutChangeListener() {

        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
          // NOTE(ljs):暂时依据宽度变化改变布局的变化以后修复成依据width和height的变化最大率
          if ((right - left) != (oldRight - oldLeft)) {
            float changeRate = mPlayerViewGroup.getWidth() * 1.0f / DisplayManagers.getInstance().getScreenWidth();
            RelativeLayout.LayoutParams txtParams = (LayoutParams) mTimerText.getLayoutParams();
            mTimerText.setTextSize((int) (25 * changeRate));
            txtParams.setMargins(DisplayManagers.dip2px(context, (int) (16 * changeRate)), DisplayManagers.dip2px(context, (int) (13 * changeRate)),
                DisplayManagers.dip2px(context, (int) (16 * changeRate)), 0);
            mTimerText.setPadding(DisplayManagers.dip2px(context, (int) (15 * changeRate)), DisplayManagers.dip2px(context, (int) (15 * changeRate)),
                DisplayManagers.dip2px(context, (int) (15 * changeRate)), DisplayManagers.dip2px(context, (int) (15 * changeRate)));
            mTimerText.requestLayout();

            RelativeLayout.LayoutParams layoutParams = (LayoutParams) relativeLayout.getLayoutParams();
            if (mPosition == 10) {
              layoutParams.width = DisplayManagers.getInstance().changeWidthSize((int) (950 * changeRate));
              layoutParams.height = DisplayManagers.getInstance().changeHeightSize((int) (530 * changeRate));
            } else if (mPosition == 0) {
              layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
              layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            }
          }
        }
      });
    }
  }

  public void setData(String innerText, String url, int second, int inp, String inurl, String TextPosition, AdPos mAdPos) {
    this.second = second;
    if (second == 0) {
      this.second = 3;
    }
    timer = new Timer();
    Uri uri = Uri.parse(url);
    if (inurl != null) {
      int h = Integer.valueOf(!TextUtils.isEmpty(mAdPos.mediaInfoList.get(0).getInnerheight()) ? mAdPos.mediaInfoList.get(0).getInnerheight() : "180");
      int w = Integer.valueOf(!TextUtils.isEmpty(mAdPos.mediaInfoList.get(0).getInnerwidth()) ? mAdPos.mediaInfoList.get(0).getInnerwidth() : "180");
      embedded = (SimpleDraweeView) embedded(inp, TextPosition, innerText, w, h);
      embedded.setVisibility(INVISIBLE);
      embedded.setImageURI(Uri.parse(inurl));
    }
    ImageDecodeOptions decodeOptions = ImageDecodeOptions.newBuilder().setDecodeAllFrames(true).build();
    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).setImageDecodeOptions(decodeOptions).build();
    if (draweeController == null) {
      draweeController = Fresco.newDraweeControllerBuilder().setImageRequest(request).setAutoPlayAnimations(true).setControllerListener(new BaseControllerListener<ImageInfo>() {
        @Override
        public void onSubmit(String id, Object callerContext) {
          super.onSubmit(id, callerContext);
          Lg.e("EpgExitView , setData()#onSubmit() , id = " + id);
        }

        @Override
        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
          Lg.e("EpgExitView , setData()#onFinalImageSet()");
          timer.schedule(task, 1000, 1000);
          mTimerText.setVisibility(View.VISIBLE);
          if (embedded != null) {
            embedded.setVisibility(View.VISIBLE);
          }
          timerText.success();
          super.onFinalImageSet(id, imageInfo, animatable);
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
          super.onFailure(id, throwable);
          timerText.removeView();
          Lg.e("EpgExitView , setData()#onFailure() , throwable = " + throwable.getMessage());
        }
      }).build();
    }
    imageView.setController(draweeController);
    if (second != -1) {
      if (second > 9) {
        mTimerText.setText("广告剩余:" + String.valueOf(second));
      } else if (second > 0 && second <= 9) {
        mTimerText.setText("广告剩余:0" + String.valueOf(second));
      }
    }
  }

  TimerTask task = new TimerTask() {
    @Override
    public void run() {
      ((Activity) context).runOnUiThread(new Runnable() {
        @Override
        public void run() {
          second--;
          timerText.getCurrentTime(second);
          if (second > 9) {
            mTimerText.setText("广告剩余:" + String.valueOf(second));
          } else if (second > 0 && second <= 9) {
            mTimerText.setText("广告剩余:0" + String.valueOf(second));
          }
          if (second <= 0) {
            timer.cancel();
            mTimerText.setVisibility(View.GONE);
            timerText.removeView();
          }
        }
      });
    }
  };

  public void StopTask() {
    task.cancel();
  }

  public RelativeLayout getRelativeLayout() {
    return relativeLayout;
  }

  @SuppressLint("NewApi")
  private View embedded(int n, String textPosition, String innerText, int w2, int h2) {
    LayoutParams embeddedlayoutParams = new RelativeLayout.LayoutParams(w2, h2);
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
      DisplayManagers.getInstance().init(context);
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
    SimpleDraweeView embeddedimageView = new SimpleDraweeView(context);
    embeddedimageView.setId(12);
    // imageView.setAspectRatio(0.45f);
    // imag_params.addRule(RelativeLayout.CENTER_IN_PARENT);
    embeddedimageView.setLayoutParams(embeddedlayoutParams);
    // embeddedimageView.setScaleType(ImageView.ScaleType.FIT_XY);
    // 设置图片属性
    if (embeddedbuilder == null) {
      embeddedbuilder = new GenericDraweeHierarchyBuilder(getResources());
      embeddedhierarchy = embeddedbuilder.setFadeDuration(500).build();
      // embeddedhierarchy.setActualImageScaleType(ScaleType.FIT_XY);
      // embeddedhierarchy.setActualImageScaleType(ScaleType.CENTER_INSIDE);
    }
    // hierarchy.setPlaceholderImage(R.drawable.ic_launcher);//默认图片
    embeddedimageView.setHierarchy(embeddedhierarchy);
    if (innerText != null) {
      TextView textView = new TextView(context);
      if (TextUtils.isEmpty(textPosition)) {
        textPosition = "9";
      }
      if ((Integer.valueOf(textPosition)) % 2 == 0) {
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
    }
    relativeLayout.addView(embeddedimageView);
    return embeddedimageView;
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    Lg.e("EpgExitView , onAttachedToWindow() , height = " + getHeight() + " , width = " + getWidth());
  }
}
