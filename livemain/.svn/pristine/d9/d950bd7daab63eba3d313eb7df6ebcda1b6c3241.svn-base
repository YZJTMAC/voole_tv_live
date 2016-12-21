package com.gntv.tv.upgrade;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gntv.tv.R;
import com.gntv.tv.view.base.DisplayManager;

public class UpgradeDialog extends Dialog{

	public UpgradeDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	public UpgradeDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window window = getWindow();
		WindowManager.LayoutParams layoutParams = window.getAttributes();
		layoutParams.width = (int) (DisplayManager.GetInstance().getScreenWidth() * 0.49)+DisplayManager.GetInstance().changeWidthSize(50);
		layoutParams.height = (int) (DisplayManager.GetInstance().getScreenHeight() * 0.41)+DisplayManager.GetInstance().changeHeightSize(80);
		Log.i("dialog", "dialog--->w:"+layoutParams.width+",h:"+layoutParams.height);
	}

	public static class Builder{
		private Context context;
		private String title;
		private String upgradeContent ;
		private boolean flag = true;
		private String positiveButtonText;
		private String negativeButtonText;
		
		private DialogInterface.OnClickListener
		                        positiveButtonClickListener,
		                        negativeButtonClickListener;
		
		public Builder(Context context){
			this.context = context;
		}
		
		public Builder setTitle(String title){
			this.title = title;
			return this;
		}
		
		public Builder setTitle(int resId){
			this.title = (String)context.getText(resId);
			return this;
		}
		
		public Builder setUpgradeContent(String upgradeContent){
			this.upgradeContent = upgradeContent;
			return this;
		}
		
		public Builder setUpgradeContent(int resId){
			this.upgradeContent = (String)context.getText(resId);
			return this;
		}
		
		public Builder setCancelable(boolean flag){
			this.flag = flag;
			return this;
		}
		
		public Builder setPositiveButton(String positiveButtonText,
				DialogInterface.OnClickListener listener){
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}
		
		public Builder setPositiveButton(int positiveButtonText,
				DialogInterface.OnClickListener listener){
			this.positiveButtonText = (String)context.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}
		
		public Builder setNegativeButton(String negativeButtonText,
				DialogInterface.OnClickListener listener){
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}
		
		public Builder setNegativeButton(int negativeButtonText,
				DialogInterface.OnClickListener listener){
			this.negativeButtonText = (String)context.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}
		
		public UpgradeDialog create() {
			LayoutInflater inflater = LayoutInflater.from(context);
			final UpgradeDialog epgDialog = new UpgradeDialog(context,
					R.style.ProgramDetailDialog);
			View layout = inflater.inflate(R.layout.upgradedialog, null);
			TextView title = (TextView)layout.findViewById(R.id.title);
			title.setText(this.title);
			TextView content1 = (TextView)layout.findViewById(R.id.content_tv) ;
			content1.setFocusable(true);
			content1.setTextColor(Color.WHITE);
			content1.setMovementMethod(ScrollingMovementMethod.getInstance());
			content1.setText(this.upgradeContent) ;
			Button okBtn = (Button) layout.findViewById(R.id.positiveButton);
			Button cancleBtn = (Button) layout.findViewById(R.id.negativeButton);
			if (positiveButtonText != null) {
				okBtn.setText(this.positiveButtonText);
				if (positiveButtonClickListener != null) {
					okBtn.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									epgDialog.dismiss();
									positiveButtonClickListener.onClick(
											epgDialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				layout.findViewById(R.id.positiveButton).setVisibility(
						View.GONE);
			}
			if (negativeButtonText != null) {
				cancleBtn.setText(this.negativeButtonText);
				if (negativeButtonClickListener != null) {
					cancleBtn.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									epgDialog.dismiss();
									negativeButtonClickListener.onClick(
											epgDialog,
											DialogInterface.BUTTON_NEGATIVE);
								}                                             
							});
				}
			} else {
				layout.findViewById(R.id.negativeButton).setVisibility(
						View.GONE);
			}
			if (View.VISIBLE == okBtn.getVisibility() && View.GONE == cancleBtn.getVisibility()) {
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
				okBtn.setLayoutParams(layoutParams);
			}
			epgDialog.setContentView(layout);
			epgDialog.setCancelable(flag);
			return epgDialog;
		}
	}


}
