package com.vad.sdk.core.view.v30;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.vad.sdk.core.Utils.v30.DeviceUtil;
import com.vad.sdk.core.Utils.v30.DisplayManagers;
import com.vad.sdk.core.Utils.v30.Lg;
import com.vad.sdk.core.base.AdPos;
import com.vad.sdk.core.view.v30.JavaScriptInterface.ExitListener;
import com.voole.android.client.UpAndAu.util.MD5Util;

public class VerticalAdView extends LinearLayout {

  Activity context;
  AdPos anfo;
  private DownDialog downDialog;

  @SuppressLint("NewApi")
  public VerticalAdView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    this.context = (Activity) context;
    initView(context);
    fillData();
  }

  public VerticalAdView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = (Activity) context;
    initView(context);
    fillData();
  }

  public VerticalAdView(Context context) {
    super(context);
    this.context = (Activity) context;
    initView(context);
    fillData();
  }

  private List<Item_relativeLayout> item = new ArrayList<Item_relativeLayout>();
  private List<RelativeLayout> item_list = new ArrayList<RelativeLayout>();
  private TextView text = null;
  private LinearLayout layout_recommend;
  private LinearLayout layout_poster1;
  private DisplayManagers instance;
  private String imgUrlB;
  private String name;

  @SuppressLint("NewApi")
  private void initView(Context context) {
    setFocusable(true);
    setFocusableInTouchMode(true);
    layout_recommend = new LinearLayout(context);
    // layout_recommend.setBackgroundColor(Color.BLACK);
    // layout_recommend.getBackground().setAlpha(60);
    layout_recommend.setBackgroundResource(context.getResources().getIdentifier(
        "ad_image_listview", "drawable", context.getPackageName()));
    layout_recommend.setOrientation(VERTICAL);
    addOnLayoutChangeListener(new OnLayoutChangeListener() {

      @Override
      public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
          int oldTop, int oldRight, int oldBottom) {
        // TODO Auto-generated method stub

      }
    });
    LinearLayout.LayoutParams param_layout_recommend = new LinearLayout.LayoutParams(
        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
    layout_recommend.setLayoutParams(param_layout_recommend);
    addView(layout_recommend);

    // String imageUri10 =
    // "http://b.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=d3b9d489bf096b63814c56543903ab72/b64543a98226cffcc7e8ac14b8014a90f703ea52.jpg";
    instance = DisplayManagers.getInstance();
    instance.init(context);
    setOrientation(VERTICAL);
    setFocusable(true);
    setFocusableInTouchMode(true);
    layout_recommend.setGravity(Gravity.CENTER);
    // 标题
    text = new TextView(context);
    LinearLayout.LayoutParams text_param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
        LayoutParams.WRAP_CONTENT);
    text.setText("为您推荐");
    text.setGravity(Gravity.CENTER);
    text.setTextColor(Color.WHITE);
    // text_param.topMargin =
    // DisplayManagers.GetInstance().changeHeightSize(1);
    text.setLayoutParams(text_param);
    text.setTextSize(instance.changeTextSize(25));
    layout_recommend.addView(text);

    layout_poster1 = new LinearLayout(context);
    layout_poster1.setOrientation(VERTICAL);
    LinearLayout.LayoutParams param_Layout_poster1 = new LinearLayout.LayoutParams(
        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    layout_poster1.setLayoutParams(param_Layout_poster1);
    layout_recommend.addView(layout_poster1);

    // 类容
    // for (int i = 0; i < 3; i++) {
    //
    // RelativeLayout layout_poster3 = new RelativeLayout(context);
    // LinearLayout.LayoutParams layout_poster_param = new LinearLayout.LayoutParams(
    // LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    // layout_poster3.setPadding(3, 3, 3, 3);
    // layout_poster_param.setMargins(0, 6, 0, 6);
    // layout_poster3.setLayoutParams(layout_poster_param);
    // layout_poster3.setBackgroundColor(Color.TRANSPARENT);
    // layout_poster1.addView(layout_poster3);
    // Rela_list.add(layout_poster3);
    // int poster_height = instance.changeHeightSize(200);
    // int poster_width = instance.changeWidthSize(145);
    // Item_relativeLayout item_relativeLayout = new Item_relativeLayout(
    // context, imgUrlB, name);
    // item_list.add(item_relativeLayout.getRelaView());
    // if (i == 0)
    // item_relativeLayout.requestFocus();
    // RelativeLayout.LayoutParams image_param = new RelativeLayout.LayoutParams(
    // poster_width, poster_height);
    // image_param.addRule(RelativeLayout.CENTER_IN_PARENT);
    // item_relativeLayout.setLayoutParams(image_param);
    // layout_poster3.addView(item_relativeLayout);
    // }
  }

  private void fillData() {
    for (int i = 0; i < 3; i++) {
      LinearLayout layout_poster3 = new LinearLayout(context);
      LinearLayout.LayoutParams layout_poster_param = new LinearLayout.LayoutParams(
          LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
      layout_poster3.setPadding(0, 0, 0, 0);
      layout_poster_param
          .setMargins(0, instance.dip2px(context, 3), 0, instance.dip2px(context, 3));
      layout_poster3.setLayoutParams(layout_poster_param);
      layout_poster3.setBackgroundColor(Color.TRANSPARENT);
      layout_poster1.addView(layout_poster3);
      // if (info.getRecommlist().size() > 0) {
      // if(i<info.getRecommlist().size()){
      // imgUrlB = info.getRecommlist().get(i).getImgUrlB();
      // name = info.getRecommlist().get(i).getFilmName();
      // Lg.d(info.getRecommlist().get(i).toString());
      //
      // }else {
      // imgUrlB = info.getMediainfolist().get(1).getSource();
      // name = info.getMediainfolist().get(1).getName();
      // Lg.d(info.getMediainfolist().get(1).toString());
      // }
      // } else{
      // imgUrlB = info.getMediainfolist().get(i).getSource();
      // name = info.getMediainfolist().get(i).getName();
      // Lg.d(info.getMediainfolist().get(i).toString());
      // }
      int poster_height = instance.changeHeightSize(220);
      int poster_width = instance.changeWidthSize(170);
      Item_relativeLayout item_relativeLayout = new Item_relativeLayout(context, imgUrlB, name);
      item_list.add(item_relativeLayout.getView());
      item.add(item_relativeLayout);
      if (i == 0)
        item_relativeLayout.requestFocus();
      RelativeLayout.LayoutParams image_param = new RelativeLayout.LayoutParams(poster_width,
          poster_height);
      image_param.addRule(RelativeLayout.CENTER_IN_PARENT);
      item_relativeLayout.setLayoutParams(image_param);
      layout_poster3.addView(item_relativeLayout);
    }
  }

  // String[] split = null;
  public void setView(AdPos onfo) {
    anfo = onfo;
    for (int j = 0; j < anfo.mediaInfoList.size(); j++) {
      if ("1".equals(anfo.mediaInfoList.get(j).getSourcetype())) {
        anfo.mediaInfoList.remove(j);
      }
    }
    for (int i = 0; i < 3; i++) {
      final int j = i;
      // if (anfo.mediaInfoList.size() > 0) {
      // if(i<anfo.mediaInfoList.size()){
      // imgUrlB = anfo.mediaInfoList.get(i).getSource();
      // name = anfo.mediaInfoList.get(i).getName();
      // Lg.d(anfo.mediaInfoList.get(i).toString());
      //
      // }else {
      // imgUrlB = anfo.mediaInfoList.get(1).getSource();
      // name = anfo.mediaInfoList.get(1).getName();
      // Lg.d(anfo.mediaInfoList.get(1).toString());
      // }
      // } else{
      imgUrlB = anfo.mediaInfoList.get(i).getSource();
      name = anfo.mediaInfoList.get(i).getName();
      Lg.d(anfo.mediaInfoList.get(i).toString());
      // }
      item.get(i).setData(name, imgUrlB);
      // TODO Auto-generated method stub

      item_list.get(i).setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          // Toast.makeText(context, "点击了一下"+j, Toast.LENGTH_SHORT).show();
          String type = null;
          if (anfo.mediaInfoList.get(j).getSkiptype() != null) {
            type = anfo.mediaInfoList.get(j).getSkiptype();
          }

          if ("2".equals(type)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            final AlertDialog mWebDialog = builder.create();
            mWebDialog.show();
            mWebDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            AdWebView mDialogWv = new AdWebView(context, lp);
            mDialogWv.setJsExitListener(new ExitListener() {
              @Override
              public void onExit() {
                mWebDialog.cancel();
              }
            });
            mWebDialog.setContentView(mDialogWv);

            mDialogWv.loadUrl("http://" + anfo.mediaInfoList.get(j).getUrl());
            mDialogWv.setVisibility(View.VISIBLE);
          } else if ("3".equals(type)) {
            final String Pkgname = anfo.mediaInfoList.get(j).getPkgname();
            final String url = anfo.mediaInfoList.get(j).getUrl();
            // final String fileName = new Date().getTime()+".apk";
            // final String fileName = Base64.encodeToString(url.getBytes(), Base64.NO_WRAP)+".apk";
            final String fileName = MD5Util.getMD5String(url).substring(8, 24) + ".apk";
            final String appName = anfo.mediaInfoList.get(j).getName();

            /*
             * if(anfo.mediaInfoList.get(j).getApkinfo() != null){ String Split =
             * anfo.mediaInfoList.get(j).getApkinfo(); int i=Split.indexOf("{\""); int
             * j=Split.indexOf("\"}"); Split = Split.substring(i+2, j); split =
             * Split.split("\":\""); }
             */
            Lg.d("item_list:" + j + "点击了点击了点击了点击了点击了点击了点击了点击了点击了点击了点击了点击了点击了");
            boolean isInstall = DeviceUtil.checkPackageExist(context, Pkgname);
            if (isInstall) {
              // Intent intent = context.getPackageManager().getLaunchIntentForPackage(Pkgname);
              Intent intent = new Intent();
              if (!TextUtils.isEmpty(anfo.mediaInfoList.get(j).getAction())) {
                intent.setAction(anfo.mediaInfoList.get(j).getAction());
              } else {
                intent = context.getPackageManager().getLaunchIntentForPackage(Pkgname);
              }
              /*
               * if(split.length >0){ if("?".equals(split[1])){ intent.putExtra(split[0],
               * anfo.mediaInfoList.get(j).getMid()); }else{ intent.putExtra(split[0], split[1]); }
               * }
               */
              try {
                JSONObject jsonObject = new JSONObject(anfo.mediaInfoList.get(j).getApkinfo());
                String intentMid = jsonObject.getString("intentMid");
                if ("?".equals(intentMid)) {
                  intent.putExtra("intentMid", anfo.mediaInfoList.get(j).getMid());
                } else {
                  intent.putExtra("intentMid", intentMid);
                }
                String fromApp = jsonObject.getString("fromApp");
                intent.putExtra("fromApp", fromApp);
              } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
              context.startActivity(intent);
            } else {
				if (downDialog == null) {
					downDialog = new DownDialog(context);
					}
				
              downDialog.start( url,fileName, appName);
            }

          }
        }
      });
    }
  }

  public List<RelativeLayout> getPosterLayout() {
    return item_list;
  }

  public List<Item_relativeLayout> getItem() {
    return item;
  }

}
