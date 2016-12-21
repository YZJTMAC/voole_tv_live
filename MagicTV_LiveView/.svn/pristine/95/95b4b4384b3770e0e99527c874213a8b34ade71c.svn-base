package com.gntv.tv.view.base;

import com.gntv.tv.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TVAlertDialog extends Dialog {
	public static boolean isShowing = false;
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
		WindowManager.LayoutParams layoutParams = window.getAttributes();
		layoutParams.width = (int) (DisplayManager.GetInstance().getScreenWidth() * 0.49)+DisplayManager.GetInstance().changeWidthSize(50);
		layoutParams.height = (int) (DisplayManager.GetInstance().getScreenHeight() * 0.41)+DisplayManager.GetInstance().changeHeightSize(80);
	}
	
	@Override
	public void show() {
		super.show();
		isShowing = true;
	}
	
	@Override
	public void cancel() {
		super.cancel();
		isShowing = false;
	}
	
	@Override
	public void dismiss() {
		super.dismiss();
		isShowing = false;
	}
	
	public static class Builder {
		private Context mContext;
		private String mTitle;
		private String mPositiveButtonText;
		private String mNegativeButtonText;
		private boolean flag = true;
		
		private DialogInterface.OnClickListener mPositiveButtonClickListener, mNegativeButtonClickListener;

		public Builder(Context context) {
			this.mContext = context;
		}

		public Builder setTitle(String content) {
			this.mTitle = content;
			return this;
		}

		public Builder setTitle(int resId) {
			this.mTitle = (String) mContext.getText(resId);
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener listener) {
			this.mPositiveButtonText = positiveButtonText;
			this.mPositiveButtonClickListener = listener;
			return this;
		}

		public Builder setCancelable(boolean flag){
			this.flag = flag;
			//setCancelable(flag);
			return this;
		}
		
		public Builder setPositiveButton(int positiveButtonText, DialogInterface.OnClickListener listener) {
			this.mPositiveButtonText = (String) mContext.getText(positiveButtonText);
			this.mPositiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText, DialogInterface.OnClickListener listener) {
			this.mNegativeButtonText = negativeButtonText;
			this.mNegativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText, DialogInterface.OnClickListener listener) {
			this.mNegativeButtonText = (String) mContext.getText(negativeButtonText);
			this.mNegativeButtonClickListener = listener;
			return this;
		}

		public TVAlertDialog create() {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			View view = inflater.inflate(R.layout.dialog, null);
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
		return DisplayManager.GetInstance().changeWidthSize(width);
	}
	
	protected static int changeHeight(int height){
		return DisplayManager.GetInstance().changeHeightSize(height);
	}
}
