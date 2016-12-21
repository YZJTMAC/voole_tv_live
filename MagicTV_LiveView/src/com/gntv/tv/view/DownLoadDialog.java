package com.gntv.tv.view;

import com.gntv.tv.R;
import com.gntv.tv.view.base.DisplayManager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/***
 * 自定义进度条
 * 
 */
public class DownLoadDialog extends Dialog {

	public DownLoadDialog(Context context, int theme) {
		super(context, theme);
	}

	public DownLoadDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window window = getWindow();
		WindowManager.LayoutParams layoutParams = window.getAttributes();
		layoutParams.width = (int) (DisplayManager.GetInstance().getScreenWidth() * 0.49)+DisplayManager.GetInstance().changeWidthSize(50);
		layoutParams.height = (int) (DisplayManager.GetInstance().getScreenHeight() * 0.41)+DisplayManager.GetInstance().changeHeightSize(80);
	}

	public static class Builder {
		private DownLoadProgressView downLoadProgressView;
		private Context context;
		private TextView progressTitle;
		private boolean isForceUpgrade = false;
		private boolean flag = true;

		private DialogInterface.OnClickListener positiveButtonClickListener, negativeButtonClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setCancelable(boolean flag) {
			this.flag = flag;
			return this;
		}

		public void setProgress(float progress){
			downLoadProgressView.setCurrentCount(progress);
		}
		
		public float getProgress(){
			return downLoadProgressView.getCurrentCount();
		}
		
		public void setMessage(String title){
			progressTitle.setText(title);
		}
		public void setMaxProgress(float progress){
			downLoadProgressView.setMaxCount((float)progress);
		}
		
		public void setPositiveButtonClickListener(DialogInterface.OnClickListener listener){
			positiveButtonClickListener = listener;
		}
		
		public float getMaxProgress(){
			return downLoadProgressView.getMaxCount();
		}
		
		public void setNegativeButtonClickListener(DialogInterface.OnClickListener listener){
			negativeButtonClickListener = listener;
		}
		
		public void setForceUpGrade(){
			isForceUpgrade = true;
		}
		public DownLoadDialog create() {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final DownLoadDialog downLoadDialog = new DownLoadDialog(context, R.style.ProgramDetailDialog);
			View layout = inflater.inflate(R.layout.downloaddialog, null);
			downLoadProgressView = (DownLoadProgressView) layout.findViewById(R.id.downloaddialog_progressView);
			progressTitle = (TextView) layout.findViewById(R.id.downloaddialog_progress_title);
			Button okBtn = (Button) layout.findViewById(R.id.positiveButton);
			Button cancleBtn = (Button) layout.findViewById(R.id.negativeButton);
			if (positiveButtonClickListener != null) {
				okBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						downLoadDialog.dismiss();
						positiveButtonClickListener.onClick(downLoadDialog, DialogInterface.BUTTON_POSITIVE);
					}
				});
			}
			if (negativeButtonClickListener != null) {
				cancleBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						downLoadDialog.dismiss();
						negativeButtonClickListener.onClick(downLoadDialog, DialogInterface.BUTTON_NEGATIVE);
					}
				});
			}
			if (View.VISIBLE == okBtn.getVisibility() && View.GONE == cancleBtn.getVisibility()) {
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
				okBtn.setLayoutParams(layoutParams);
			}
			if(isForceUpgrade){
				okBtn.setVisibility(View.GONE);
				cancleBtn.setVisibility(View.GONE);
				RelativeLayout.LayoutParams progress_layoutParams = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				progress_layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
				progress_layoutParams.setMargins(DisplayManager.GetInstance().changeWidthSize(28), 0, DisplayManager.GetInstance().changeWidthSize(28), 0);
				downLoadProgressView.setLayoutParams(progress_layoutParams);
				TextView tip = (TextView) layout.findViewById(R.id.upgrade_tip_tv);
				tip.setVisibility(View.VISIBLE);
			}
			downLoadDialog.setContentView(layout);
			downLoadDialog.setCancelable(flag);
			return downLoadDialog;
		}
	}
}