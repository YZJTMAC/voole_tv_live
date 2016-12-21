/**
 *
 */
package com.vad.sdk.core.manager;

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
import com.vad.sdk.core.Utils.v30.DeviceUtil;
import com.vad.sdk.core.Utils.v30.DisplayManagers;
import com.vad.sdk.core.Utils.v30.Lg;
import com.vad.sdk.core.base.MediaInfo;
import com.vad.sdk.core.view.v30.AdWebView;
import com.vad.sdk.core.view.v30.CustomProgressBar;
import com.vad.sdk.core.view.v30.DownDialog;
import com.vad.sdk.core.view.v30.DownloadAPkManager;
import com.vad.sdk.core.view.v30.JavaScriptInterface.ExitListener;
import com.voole.android.client.UpAndAu.util.MD5Util;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 *
 * @author luojunsheng
 * @date 2016年6月19日 上午9:57:53
 * @version 1.1
 */
public class MediaHandler {
  private DownloadAPkManager mAPkManager;
  private SimpleDraweeView mSkipPicView;
  private  DownDialog downDialog;

  public void handlerMediaInfoSkip(String tag, Context context, final MediaInfo info, String skiptype) {
    handlerMediaInfoSkip(tag, context, info, skiptype, false);
  }

  /**
   * 处理广告介质的二级跳转,根据skiptype(存放在inner中)类型跳转
   *
   *
   * @param tag
   *          追踪方法被调用对象,传递ClassSimpleName
   * @param context
   *          在skiptype会利用Dialog弹出图片广告 因此context必输
   * @param info
   * @param skiptype
   *          0. 不跳转 1. 图片 2. 跳转到网页 3. 打开APK
   * @param isShowLoading
   *          是否展示下载框
   */
  public void handlerMediaInfoSkip(String tag, Context context, final MediaInfo info, String skiptype, boolean isShowLoading) {
    Lg.i("MediaHandler , handlerMediaInfoSkip() , tag = " + tag);
    Lg.i("MediaHandler , handlerMediaInfoSkip() , skiptype = " + skiptype);
    if ("1".equals(info.getSkiptype())) {// 图片
      if (context instanceof Application) {
        return;
      }

      AlertDialog dialog = new AlertDialog.Builder(context).create();
      dialog.show();
      DisplayManagers.getInstance().init(context);
      int width = (int) (DisplayManagers.getInstance().getScreenWidth() * 0.3);
      // int height = (int) (DisplayManagers.GetInstance().getScreenHeight() * 0.6);
      Lg.e("handlerMediaInfoSkip() , width = " + width);
      // dialog.getWindow().setLayout(android.app.ActionBar.LayoutParams.MATCH_PARENT,
      // LayoutParams.MATCH_PARENT);
      dialog.getWindow().setLayout(width, width);
      mSkipPicView = new SimpleDraweeView(context);
      LayoutParams imageParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
      mSkipPicView.setLayoutParams(imageParams);
      dialog.setContentView(mSkipPicView);
      String url = info.getUrl();
      // url = "http://imgstbox.voole.com/img/new_img/2016/03/15/2016031514465037395.jpg";
      Lg.e("handlerMediaInfoSkip() , pic skip url = " + url);
      mSkipPicView.setTag(url);
      GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
      GenericDraweeHierarchy hierarchy = builder.setFadeDuration(500).setProgressBarImage(new CustomProgressBar()).build();
      hierarchy.setActualImageScaleType(ScaleType.FIT_XY);
      Uri uri = Uri.parse(url);
      ImageDecodeOptions decodeOptions = ImageDecodeOptions.newBuilder().setDecodeAllFrames(true).build();
      ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri).setImageDecodeOptions(decodeOptions).build();
      DraweeController draweeController = Fresco.newDraweeControllerBuilder().setImageRequest(request).setAutoPlayAnimations(true).setControllerListener(new BaseControllerListener<ImageInfo>() {
        @Override
        public void onSubmit(String id, Object callerContext) {
          super.onSubmit(id, callerContext);
          Lg.e("handlerMediaInfoSkip()#onSubmit() , id = " + id);
        }

        @Override
        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
          super.onFinalImageSet(id, imageInfo, animatable);
          Lg.e("handlerMediaInfoSkip()#onFinalImageSet() , id = " + id + " , imageInfo = " + imageInfo);
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
          super.onFailure(id, throwable);
          Lg.d("handlerMediaInfoSkip()#onFailure , messsage = " + throwable.getMessage());
        }

        @Override
        public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
          super.onIntermediateImageSet(id, imageInfo);
          Lg.e("handlerMediaInfoSkip()#onIntermediateImageSet()");
        }

        @Override
        public void onIntermediateImageFailed(String id, Throwable throwable) {
          super.onIntermediateImageFailed(id, throwable);
          Lg.e("handlerMediaInfoSkip()#onIntermediateImageFailed() , message = " + throwable.getMessage());
        }
      }).build();
      mSkipPicView.setController(draweeController);
    } else if ("2".equals(info.getSkiptype())) {// 跳转网页
      AlertDialog.Builder builder = new AlertDialog.Builder(context);
      final AlertDialog mWebDialog = builder.create();
      mWebDialog.show();
      mWebDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
      LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
      AdWebView mDialogWv = new AdWebView(context, params);
      mDialogWv.setJsExitListener(new ExitListener() {
        @Override
        public void onExit() {
          mWebDialog.cancel();
        }
      });
      mWebDialog.setContentView(mDialogWv);
      mDialogWv.loadUrl(info.getUrl());
      mDialogWv.setVisibility(View.VISIBLE);
    } else if ("3".equals(info.getSkiptype())) {// 跳转apk页面或者下载apk
      final String Package = info.getPkgname();
      Lg.e("package = " + Package + " , action = " + info.getAction() + " , activity = " + info.getActivity());
      final String url = info.getUrl();
      String fileName = "";
      if (url != null) {
        fileName = MD5Util.getMD5String(url).substring(8, 24) + ".apk";
      }
      final String appName = info.getPkgname();
      boolean isInstall = DeviceUtil.checkPackageExist(context, Package);
      // NOTE(ljs):<apkinfo pkgname="com.suning.tv.ebuy" action="5615" activity="">
      // NOTE(ljs):首先依据action启动,如果action为空,则启动包名
      if (isInstall) {
        Intent intent = new Intent();
        if (!TextUtils.isEmpty(info.getAction())) {
          intent.setAction(info.getAction());
        } else {
          intent = context.getPackageManager().getLaunchIntentForPackage(Package);
        }
        try {
          if (!TextUtils.isEmpty(info.getApkinfo())) {
            JSONObject jsonObject = new JSONObject(info.getApkinfo());
            // NOTE(ljs):不关心key-value 全部传过去,此处为单层的json没有嵌套情况,
            // NOTE(ljs):业务需求在value是"?"时换成mid
            Lg.i("handlerMediaInfoSkip() , jsonObject = " + jsonObject);
            Iterator it = jsonObject.keys();
            while (it.hasNext()) {
              String key = (String) it.next();
              String value = (String) jsonObject.get(key);
              if ("?".equals(value)) {
                value = info.getMid();
                Lg.e("? value = " + value);
              }
              intent.putExtra(key, value);
            }
          }
          // String intentMid = jsonObject.getString("intentMid");
          // intent.putExtra("intentMid", intentMid);
          // String fromApp = jsonObject.getString("fromApp");
          // intent.putExtra("fromApp", fromApp);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
      } else {
        if (isShowLoading) {
			if (downDialog == null) {
				downDialog = new DownDialog(context);
				}

			downDialog.start(url, fileName, appName);
        } else {
          if (mAPkManager == null) {
            mAPkManager = new DownloadAPkManager(context, fileName, appName);
          }
          mAPkManager.startDownload(url);
        }
      }
    }
  }
}
