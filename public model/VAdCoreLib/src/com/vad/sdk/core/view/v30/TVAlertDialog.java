package com.vad.sdk.core.view.v30;

import com.vad.sdk.core.R;
import com.vad.sdk.core.Utils.v30.DisplayManagers;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TVAlertDialog extends Dialog {

  public TVAlertDialog(Context context) {
    super(context);
  }

  public TVAlertDialog(Context context, int theme) {
    super(context, theme);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Window window = getWindow();
    DisplayMetrics dm = new DisplayMetrics();
    window.getWindowManager().getDefaultDisplay().getMetrics(dm);
    WindowManager.LayoutParams layoutParams = window.getAttributes();
    // layoutParams.width = (int) (dm.widthPixels / 2);
    // layoutParams.height = (int) (dm.heightPixels / 2.2);
    layoutParams.width = (int) (DisplayManagers.getInstance().getScreenWidth() * 0.49) + DisplayManagers.getInstance().changeWidthSize(50);
    layoutParams.height = (int) (DisplayManagers.getInstance().getScreenHeight() * 0.41) + DisplayManagers.getInstance().changeHeightSize(80);
    window.setAttributes(layoutParams);
  }

  public static class Builder {
    private Context mContext;
    private String mTitle;
    private String mPositiveButtonText;
    private String mNegativeButtonText;
    private boolean flag = true;

    private DialogInterface.OnClickListener mPositiveButtonClickListener, mNegativeButtonClickListener;

    public Builder(Context context) {
      mContext = context;
    }

    public Builder setTitle(String content) {
      mTitle = content;
      return this;
    }

    public Builder setTitle(int resId) {
      mTitle = (String) mContext.getText(resId);
      return this;
    }

    public Builder setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener listener) {
      mPositiveButtonText = positiveButtonText;
      mPositiveButtonClickListener = listener;
      return this;
    }

    public Builder setCancelable(boolean flag) {
      this.flag = flag;
      return this;
    }

    public Builder setPositiveButton(int positiveButtonText, DialogInterface.OnClickListener listener) {
      mPositiveButtonText = (String) mContext.getText(positiveButtonText);
      mPositiveButtonClickListener = listener;
      return this;
    }

    public Builder setNegativeButton(String negativeButtonText, DialogInterface.OnClickListener listener) {
      mNegativeButtonText = negativeButtonText;
      mNegativeButtonClickListener = listener;
      return this;
    }

    public Builder setNegativeButton(int negativeButtonText, DialogInterface.OnClickListener listener) {
      mNegativeButtonText = (String) mContext.getText(negativeButtonText);
      mNegativeButtonClickListener = listener;
      return this;
    }

    public TVAlertDialog create() {
      View view = LayoutInflater.from(mContext).inflate(R.layout.dialog, null);
      final TVAlertDialog customAlertDialog = new TVAlertDialog(mContext, R.style.alertDialog);
      TextView tvAlertTitle = (TextView) view.findViewById(R.id.title);
      tvAlertTitle.setText(mTitle);

      Button btnPositive = (Button) view.findViewById(R.id.positiveButton);
      if (!TextUtils.isEmpty(mPositiveButtonText)) {
        btnPositive.setText(mPositiveButtonText);
        if (mPositiveButtonClickListener != null) {
          btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              mPositiveButtonClickListener.onClick(customAlertDialog, BUTTON_POSITIVE);
            }
          });
        }
      } else {
        btnPositive.setVisibility(View.GONE);
      }
      Button btnNegative = (Button) view.findViewById(R.id.negativeButton);
      if (!TextUtils.isEmpty(mNegativeButtonText)) {
        btnNegative.setText(mNegativeButtonText);
        if (mNegativeButtonClickListener != null) {
          btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              mNegativeButtonClickListener.onClick(customAlertDialog, BUTTON_NEGATIVE);
            }
          });
        } else {
          btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              customAlertDialog.dismiss();
            }
          });
        }
      } else {
        btnNegative.setVisibility(View.GONE);
      }
      if (View.VISIBLE == btnPositive.getVisibility() && View.GONE == btnNegative.getVisibility()) {
    	  	RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(changeWidth(154),changeHeight(69));
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
			layoutParams.bottomMargin = changeHeight(51);
			btnPositive.setLayoutParams(layoutParams);
      }
      customAlertDialog.setContentView(view);
      customAlertDialog.setCancelable(flag);
      return customAlertDialog;
    }
  }
  
	protected static int changeWidth(int width){
		return DisplayManagers.getInstance().changeWidthSize(width);
	}
	
	protected static int changeHeight(int height){
		return DisplayManagers.getInstance().changeHeightSize(height);
	}
}
