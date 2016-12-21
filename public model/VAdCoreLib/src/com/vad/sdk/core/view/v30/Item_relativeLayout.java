package com.vad.sdk.core.view.v30;

import java.io.IOException;
import java.io.InputStream;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.vad.sdk.core.Utils.v30.DisplayManagers;

@SuppressLint("NewApi")
public class Item_relativeLayout extends RelativeLayout {

  private GenericDraweeHierarchyBuilder builder;
  private GenericDraweeHierarchy hierarchy;
  private DraweeController draweeController;
  private RelativeLayout relativeLayout;
  private Context context;
  private Boolean boolean1 = false;
  private Uri uri;
  private SimpleDraweeView imageView;
  private AlwaysMarqueeTextView textView;
  private BitmapDrawable ad_selected_image;
  private InputStream is = null;
  private RelativeLayout layout_poster3;

  public Item_relativeLayout(Context context, AttributeSet attrs, int defStyle, String url,
      String text) {
    super(context, attrs, defStyle);
    initView(context, url, text);
  }

  public Item_relativeLayout(Context context, AttributeSet attrs, String url, String text) {
    super(context, attrs);
    initView(context, url, text);
  }

  public Item_relativeLayout(Context context, String url, String text, Boolean boolean1) {
    super(context);
    this.boolean1 = boolean1;
    initView(context, url, text);
  }

  public Item_relativeLayout(Context context, String url, String text) {
    super(context);
    initView(context, url, text);
    // staterAnim();
  }

  /**
	 *
	 */
  private void staterAnim() {
    ObjectAnimator//
        .ofFloat(relativeLayout, "rotationY", 0.0F, 360.0F)//
        .setDuration(1500)//
        .start();
  }

  @SuppressWarnings("deprecation")
  @SuppressLint("NewApi")
  private void initView(Context context, String url, String text) {
    // StringBuffer Text = new StringBuffer(text);
    // text = Text.reverse().toString();
    setVisibility(GONE);
    this.context = context;
    DisplayManagers.getInstance().init(context);
    int showImageWidth = boolean1 ? LayoutParams.MATCH_PARENT : DisplayManagers.getInstance()
        .changeWidthSize(190);
    int showImageHeight = boolean1 ? LayoutParams.MATCH_PARENT : DisplayManagers.getInstance()
        .changeHeightSize(350);
    LayoutParams Parent_layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
        LayoutParams.WRAP_CONTENT);
    setLayoutParams(Parent_layoutParams);
    // assets图片资源加载
    // AssetManager assetManager = context.getAssets();
    // try {
    // is = assetManager.open("image/ad_image_text.png");
    // Bitmap bitmap = getImageFromAssetsFile("image/ad_selected_image.9.png");
    // is .close();
    // ad_selected_image = new BitmapDrawable(null,bitmap);
    // } catch (IOException e) {
    // Lg.e("assetManager----------------Exception");
    // e.printStackTrace();
    // }

    // 动态添加选择器
    StateListDrawable drawable = new StateListDrawable();
    drawable.addState(new int[] { android.R.attr.state_pressed },
        getResources().getDrawable(android.R.color.transparent));
    drawable.addState(
        new int[] { android.R.attr.state_selected },
        getResources().getDrawable(
            context.getResources().getIdentifier("ad_selected_image", "drawable",
                context.getPackageName())));
    drawable.addState(
        new int[] { android.R.attr.state_focused },
        getResources().getDrawable(
            context.getResources().getIdentifier("ad_selected_image", "drawable",
                context.getPackageName())));
    drawable.addState(new int[] { android.R.attr.state_window_focused }, getResources()
        .getDrawable(android.R.color.transparent));
    drawable.addState(
        new int[0],
        getResources().getDrawable(
            context.getResources().getIdentifier("ad_selected_image", "drawable",
                context.getPackageName())));
    if (relativeLayout == null) {
      relativeLayout = getRelaView();
    }
    relativeLayout.setFocusable(true);
    relativeLayout.setFocusableInTouchMode(true);
    relativeLayout.setClickable(true);
    LayoutParams Params = new RelativeLayout.LayoutParams(
        boolean1 ? RelativeLayout.LayoutParams.MATCH_PARENT
            : RelativeLayout.LayoutParams.WRAP_CONTENT,
        boolean1 ? RelativeLayout.LayoutParams.MATCH_PARENT
            : RelativeLayout.LayoutParams.WRAP_CONTENT);
    // relativeLayout.setPadding(2, 2, 2, 2);
    // if((Build.VERSION.SDK_INT)>=Build.VERSION_CODES.JELLY_BEAN){
    // relativeLayout.setBackground(drawable);
    // }else{
    // relativeLayout.setBackgroundDrawable(drawable);
    // }
    relativeLayout.setLayoutParams(Params);

    RelativeLayout layout_poster_1 = new RelativeLayout(context);
    RelativeLayout.LayoutParams relative_poster_param_1 = new RelativeLayout.LayoutParams(
        LayoutParams.WRAP_CONTENT, DisplayManagers.getInstance().changeHeightSize(375));
    layout_poster_1.setLayoutParams(relative_poster_param_1);
    this.addView(layout_poster_1);

    layout_poster3 = new RelativeLayout(context);
    layout_poster3.setClickable(true);
    layout_poster3.setFocusable(true);
    layout_poster3.setFocusableInTouchMode(true);
    // LinearLayout.LayoutParams layout_poster_param = new
    // LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,1);
    LinearLayout.LayoutParams layout_poster_param = new LinearLayout.LayoutParams(DisplayManagers
        .getInstance().changeWidthSize(215), LayoutParams.MATCH_PARENT);
    // layout_poster3.setPadding(3, 3, 3, 3);
    // layout_poster_param.setMargins(0, 6, 0, 6);
    layout_poster3.setLayoutParams(layout_poster_param);
    if ((Build.VERSION.SDK_INT) >= Build.VERSION_CODES.JELLY_BEAN) {
      layout_poster3.setBackground(drawable);
    } else {
      layout_poster3.setBackgroundDrawable(drawable);
    }
    layout_poster3.setPadding(DisplayManagers.getInstance().dip2px(context, 25), DisplayManagers
        .getInstance().dip2px(context, 26), DisplayManagers.getInstance().dip2px(context, 25),
        DisplayManagers.getInstance().dip2px(context, 26));
    // uri = Uri.parse(url);
    imageView = new SimpleDraweeView(context);
    imageView.setId(1);
    LayoutParams imag_params = new LayoutParams(showImageWidth, showImageHeight);
    imag_params.addRule(RelativeLayout.CENTER_IN_PARENT);
    imageView.setLayoutParams(imag_params);
    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    imageView.setTag("item_image");
    // 设置图片属性
    if (builder == null) {
      builder = new GenericDraweeHierarchyBuilder(getResources());
      hierarchy = builder.setFadeDuration(500).build();
    }
    // hierarchy.setPlaceholderImage(R.drawable.ic_launcher);//默认图片
    imageView.setHierarchy(hierarchy);
    layout_poster3.addView(imageView);
    textView = new AlwaysMarqueeTextView(context);
    LayoutParams text_Parames = (LayoutParams) new LayoutParams(showImageWidth,
        LayoutParams.WRAP_CONTENT);
    text_Parames.addRule(RelativeLayout.ALIGN_BOTTOM, 1);
    text_Parames.addRule(RelativeLayout.ALIGN_LEFT, 1);
    text_Parames.addRule(RelativeLayout.ALIGN_RIGHT, 1);
    textView.setPadding(0, DisplayManagers.getInstance().dip2px(context, 5), 0, DisplayManagers
        .getInstance().dip2px(context, 5));
    textView.setTextSize(DisplayManagers.getInstance().changeTextSize(20));
    textView.setGravity(Gravity.CENTER);
    textView.setTextColor(Color.WHITE);
    textView.setTag("itme_text");
    textView.setBackgroundColor(Color.BLACK);
    textView.getBackground().setAlpha(168);
    // textView.setSingleLine(true);
    // textView.startScroll();
    layout_poster3.setOnFocusChangeListener(new OnFocusChangeListener() {

      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
          textView.setMarquee(true);
        } else {
          textView.setMarquee(false);
        }
      }
    });

    // textView.setText(text);//设置文字类容
    layout_poster3.addView(textView, text_Parames);
    layout_poster3.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

      @Override
      public void onGlobalLayout() {
        // ImageDecodeOptions decodeOptions =
        // ImageDecodeOptions.newBuilder().setDecodeAllFrames(true).build();
        // ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
        // .setImageDecodeOptions(decodeOptions).build();
        // if(draweeController == null){
        // draweeController =
        // Fresco.newDraweeControllerBuilder().setImageRequest(request).setAutoPlayAnimations(true).build();
        // }
        // imageView.setController(draweeController);
        // if(getVisibility()!=View.INVISIBLE){
        // ObjectAnimator//
        // .ofFloat(relativeLayout, "rotationY", 0.0F, 360.0F)//
        // .setDuration(1500)//
        // .start();
        // }else{
        // ObjectAnimator//
        // .ofFloat(relativeLayout, "rotationY", 360.0F, 0.0F)//
        // .setDuration(1500)//
        // .start();
        // }
      }
    });
    addView(layout_poster3);
    // relativeLayout.addOnLayoutChangeListener(new OnLayoutChangeListener() {
    //
    // @Override
    // public void onLayoutChange(View v, int left, int top, int right,
    // int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
    // if(getVisibility()!=View.INVISIBLE){
    // ObjectAnimator//
    // .ofFloat(relativeLayout, "rotationY", 0.0F, 360.0F)//
    // .setDuration(1500)//
    // .start();
    // }
    // }
    // });
  }

  public RelativeLayout getRelaView() {
    if (relativeLayout == null) {
      relativeLayout = new RelativeLayout(context);
    }
    return relativeLayout;
  }

  public RelativeLayout getView() {
    return layout_poster3;
  }

  public void setData(String Text, String url) {
    if (url != null) {
      Uri uri = Uri.parse(url);
      if (draweeController == null) {
        ImageDecodeOptions decodeOptions = ImageDecodeOptions.newBuilder().setDecodeAllFrames(true)
            .build();
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
            .setImageDecodeOptions(decodeOptions).build();
        draweeController = Fresco.newDraweeControllerBuilder().setImageRequest(request)
            .setAutoPlayAnimations(true)
            .setControllerListener(new BaseControllerListener<ImageInfo>() {
              @Override
              public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
                Item_relativeLayout.this.setVisibility(GONE);
              }

              @Override
              public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                Item_relativeLayout.this.setVisibility(VISIBLE);
              }
            }).build();
        imageView.setController(draweeController);
      } else {
        imageView.setImageURI(uri);
      }
    }
    if (Text != null) {
      textView.setText(Text);
    }
  }

  private Bitmap getImageFromAssetsFile(String fileName) {
    Bitmap image = null;
    AssetManager am = context.getResources().getAssets();
    try {
      InputStream is = am.open(fileName);
      image = BitmapFactory.decodeStream(is);
      is.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return image;

  }

}
