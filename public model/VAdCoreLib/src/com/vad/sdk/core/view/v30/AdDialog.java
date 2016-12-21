package com.vad.sdk.core.view.v30;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.vad.sdk.core.base.AdPos;

public class AdDialog {

  private AdPos mAdpos;
  private Context mContext;
  private static final String ADdialog_LAYOUT = "addialog_layout";
  private RelativeLayout mLayout = null;

  public AdDialog(AdPos adpos, final ViewGroup viewgroup) {
    mAdpos = adpos;
    mContext = viewgroup.getContext();
    mLayout = (RelativeLayout) viewgroup.findViewWithTag(ADdialog_LAYOUT);
    if (mLayout == null) {
      initAdViewContainer(viewgroup);
    }
  }

  private void initAdViewContainer(ViewGroup viewgroup) {
    mLayout = new RelativeLayout(viewgroup.getContext());
    mLayout.setTag(ADdialog_LAYOUT);
    mLayout.setBackgroundColor(Color.TRANSPARENT);
  }
}
