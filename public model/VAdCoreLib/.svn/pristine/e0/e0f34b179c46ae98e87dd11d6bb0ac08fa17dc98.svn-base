package com.vad.sdk.core.view.v30;

import com.vad.sdk.core.Utils.v30.Lg;
import com.vad.sdk.core.view.v30.JavaScriptInterface.ExitListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * 自定义WebView组件
 *
 * @author libin
 */
public class AdWebView extends WebView {

  private Context mContext;
  private ProgressBar mProgressBar;
  private ViewGroup mProgressLayout = null;
  private ViewGroup.LayoutParams mLayoutParams = null;
  private JavaScriptInterface mJavaScriptInterface = null;

  public AdWebView(Context context, ViewGroup.LayoutParams layoutParams) {
    super(context);
    mContext = context;
    mLayoutParams = layoutParams;
    initWebView();
    initView();
  }

  public void setProgressVisiable(boolean flag) {
    if (mProgressLayout != null) {
      if (flag) {
        mProgressBar.setProgress(0);
        mProgressLayout.setVisibility(View.VISIBLE);
      } else {
        mProgressBar.setProgress(0);
        mProgressLayout.setVisibility(View.GONE);
      }

    }
  }

  /**
   * 初始化并设置WebView <br>
   * 1.背景透明 <br>
   * 2.取消滚动条 <br>
   * 3.开启自动屏幕适配 <br>
   * 4.启用数据库存储 <br>
   * 5.使用默认缓存 <br>
   * 6.离线应用7.获取焦点<br>
   */
  private void initWebView() {
    Lg.d("AdWebView , initWebView()");
    setWebChromeClient(new VooleWebChromeClient());

    setWebViewClient(new VooleWebViewClient());

    mJavaScriptInterface = new JavaScriptInterface(mContext);
    addJavascriptInterface(mJavaScriptInterface, "Android");

    setBackgroundColor(Color.TRANSPARENT);// 背景透明

    setScrollbarFadingEnabled(true);// 滚动栏动画开启
    setScrollBarStyle(0);// 不显示滚动栏样式

    setVerticalScrollBarEnabled(false);// 取消滚动条
    setHorizontalScrollBarEnabled(false);// 取消滚动条

    getSettings().setJavaScriptEnabled(true);// 开启javascript

    setSoundEffectsEnabled(false);// 不播放声音特效
    getSettings().setPluginState(WebSettings.PluginState.OFF);// 关闭插件加载
    getSettings().setLoadWithOverviewMode(true);// 开启概览模式
    // setInitialScale(-1);// 设置初始化缩放比例
    getSettings().setUseWideViewPort(true);// 开启viewport
    getSettings().setSupportZoom(false);// 支持缩放
    getSettings().setBuiltInZoomControls(false);// 显示缩放控制
    getSettings().setDatabaseEnabled(true);// 启用数据库
    getSettings().setDatabasePath(mContext.getDir("database", Context.MODE_PRIVATE).getPath());// 设置数据库路径
    getSettings().setDomStorageEnabled(true);// 使用localStorage则必须打开
    getSettings().setAppCacheEnabled(true);// 开启应用缓存
    getSettings().setAppCachePath(mContext.getDir("cache", Context.MODE_PRIVATE).getPath()); // 设置应用缓存的路径
    getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);// 默认使用缓存
    getSettings().setAppCacheMaxSize(8388608);// 缓存最多可以有8M
    getSettings().setAllowFileAccess(true);// 离线应用(html5 manifest)
    String userAgentString = getSettings().getUserAgentString();
    Lg.e("AdWebView , initWebView() , userAgent = " + userAgentString);
    getSettings().setUserAgentString("Mozilla/5.0 (windows nt 6.1; wow64) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Safari/534.30");
    setLayoutParams(mLayoutParams);
    requestFocus();// 获取焦点

  }

  /**
   * 初始化进度条控件
   */
  private void initView() {
    Lg.d("AdWebView , initView()");
    mProgressLayout = new RelativeLayout(mContext);
    ViewGroup.LayoutParams lpLay = new ViewGroup.LayoutParams(mLayoutParams.width, mLayoutParams.height);
    mProgressLayout.setLayoutParams(lpLay);

    mProgressBar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);
    RelativeLayout.LayoutParams pbLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    pbLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    mProgressBar.setLayoutParams(pbLp);

    mProgressLayout.addView(mProgressBar);
    addView(mProgressLayout);
  }

  public void loadEmpty() {
    AdWebView.this.setBackgroundColor(Color.TRANSPARENT);// 背景透明
    loadData("", "text/html", "utf-8");
  }

  private class VooleWebChromeClient extends WebChromeClient {

    @Override
    public void onProgressChanged(WebView view, int progress) {
      mProgressBar.setProgress(progress);
    }
    //
    // public boolean onJsPrompt(WebView view, String url, String message,
    // String defaultValue, final JsPromptResult result) {
    // return super.onJsPrompt(view, url, message, defaultValue, result);
    // }
    //
    // @Override
    // public boolean onJsAlert(WebView view, String url, String message,
    // final JsResult result) {
    // return super.onJsAlert(view, url, message, result);
    // }
    //
    // @Override
    // public boolean onJsConfirm(WebView view, String url, String message,
    // final JsResult result) {
    // return super.onJsConfirm(view, url, message, result);
    // }
    //
    // @Override
    // public void onExceededDatabaseQuota(String url,
    // String databaseIdentifier, long quota,
    // long estimatedDatabaseSize, long totalQuota,
    // QuotaUpdater quotaUpdater) {
    // super.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize,
    // totalQuota, quotaUpdater);
    // }
    //
    // @Override
    // public void onReachedMaxAppCacheSize(long requiredStorage, long quota,
    // QuotaUpdater quotaUpdater) {
    // super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
    // }
  }

  private class VooleWebViewClient extends WebViewClient {

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
      super.onPageStarted(view, url, favicon);
      Lg.d("VooleWebViewClient , onPageStarted() url = " + url);
      if (!url.startsWith("data:text")) { // 调用loadData("", "text/html", "utf-8");加载文本时不改变进度条及背景状态；
        setBackgroundColor(Color.TRANSPARENT);// 背景透明
        setProgressVisiable(true);
      }
      // mTimer = new Timer();
      // TimerTask task = new TimerTask() {
      // @Override
      // public void run() {
      // int progress = mProgressBar.getProgress();
      // Lg.e("VooleWebViewClient , onPageStarted() , progress = " + progress);
      // // 超时后,首先判断页面加载进度,超时并且进度小于100,就执行超时后的动作
      // if (progress < 100) {
      // // Looper.prepare();
      // // Toast.makeText(mContext, "啊哦，网络不给力，换个姿势努一努", Toast.LENGTH_LONG).show();
      // // Looper.loop();// 进入loop中的循环，查看消息队列
      // Lg.e("VooleWebViewClient , onPageStarted() , timeout");
      // mTimer.cancel();
      // mTimer.purge();
      // }
      // }
      // };
      // // NOTE(ljs):第二个参数:要等待这么长的时间才可以第一次执行run() 方法
      // // NOTE(ljs):第三个参数:第一次调用之后，从第二次开始每隔多长的时间调用一次 run() 方法
      // mTimer.schedule(task, 1000 * 10, 1000);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
      super.onPageFinished(view, url);
      // Lg.d("VooleWebViewClient/onPageFinished/" + url);
      if (!url.startsWith("data:text")) { // 调用loadData("", "text/html", "utf-8");加载文本时不改变进度条及背景状态；
        setProgressVisiable(false);
        AdWebView.this.requestFocus();
        setBackgroundColor(Color.WHITE);// 背景白色
      }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      view.loadUrl(url);
      return true;
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
      // NOTE(ljs):https出现空白页的问题
      // NOTE(ljs):打好签名包之后依然出现该问题,需要在混淆文件中加入如下信息:
      // -keep public class android.net.http.SslError
      // -dontwarn android.webkit.WebView
      // -dontwarn android.net.http.SslError
      // -dontwarn Android.webkit.WebViewClient

      // super.onReceivedSslError(view, handler, error);
      // handler.cancel(); //默认的处理方式,WebView变成空白页
      // handleMessage(Message msg); //其他处理
      handler.proceed();// 接受证书
    }
  }

  public void setJsExitListener(ExitListener exitListener) {
    if (mJavaScriptInterface != null && exitListener != null) {
      mJavaScriptInterface.setExitListener(exitListener);
    }
  }

  /**
   * 重定义按键键值
   */
  /*
   * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { int key = event.getKeyCode();
   * switch (keyCode) { case KeyEvent.KEYCODE_0: case KeyEvent.KEYCODE_1: case KeyEvent.KEYCODE_2:
   * case KeyEvent.KEYCODE_3: case KeyEvent.KEYCODE_4: case KeyEvent.KEYCODE_5: case
   * KeyEvent.KEYCODE_6: case KeyEvent.KEYCODE_7: case KeyEvent.KEYCODE_8: case KeyEvent.KEYCODE_9:
   * key = 48 + keyCode - KeyEvent.KEYCODE_0;// NUM KEY break; case KeyEvent.KEYCODE_BACK:// BACK
   * key = 27; break; case KeyEvent.KEYCODE_DPAD_UP:// UP key = 38; break; case
   * KeyEvent.KEYCODE_DPAD_DOWN:// DOWN key = 40; break; case KeyEvent.KEYCODE_DPAD_LEFT:// LEFT key
   * = 37; break; case KeyEvent.KEYCODE_DPAD_RIGHT:// RIGHT key = 39; break; case
   * KeyEvent.KEYCODE_ENTER: case KeyEvent.KEYCODE_DPAD_CENTER:// ENTER key = 13; break; case
   * KeyEvent.KEYCODE_MEDIA_PREVIOUS:// 适配媒体键 case KeyEvent.KEYCODE_PAGE_UP:// PREVIOUS PAGE key =
   * 33;// 分类，上一页 break; case KeyEvent.KEYCODE_MEDIA_NEXT:// 适配媒体键 case
   * KeyEvent.KEYCODE_PAGE_DOWN:// NEXT PAGE key = 34;// 分类，下一页 break; case
   * KeyEvent.KEYCODE_MEDIA_CLOSE:// MEDIA CLOSE key = 986;// 续播，遥控器按返回键，关闭视频事件 break; case
   * KeyEvent.KEYCODE_MEDIA_EJECT:// MEDIA NEXT key = 989;// 续播，视频播放结束后发送事件 break; case 67: key = 8;
   * break; case 888: key = 888;// 跳到账户管理 break; case 777: key = 777;// 刷新按钮状态 break; case 666: key
   * = 666;// 刷新按钮状态 break; } loadKeydownUrl(key); return false; }
   */

  public void loadKeydownUrl(int key) {
    Log.d("VooleWebView", "onKeyDown->keyCode = " + key);
    String execJavaScript = "javascript:(function d(el, type) {var evt = document.createEvent('Event');evt.initEvent(type,true,true);evt.keyCode = " + key
        + ";el.dispatchEvent(evt);} )(document, 'keydown');";
    loadUrl(execJavaScript);
  }
}
