package com.gntv.tv.view.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gntv.tv.R;

public class TVProgressDialog extends Dialog{
	private String hint = "";
	private ProgressBar progressBar = null;
	private TextView txtMsg = null;
	public TVProgressDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public TVProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public TVProgressDialog(Context context) {
		super(context, R.style.progressDialog);
	}
	
	public void setHint(String hint){
		this.hint = hint;
	}
	
	public void setProgress(int progress){
		progressBar.setProgress(progress);
	}
	
	public void setMax(int max){
		progressBar.setMax(max);
	}
	
/*	public void setFor(){
		progressBar.setpr
	}*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RelativeLayout layout = new RelativeLayout(getContext());
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		param.addRule(RelativeLayout.CENTER_IN_PARENT);
		layout.setLayoutParams(param);
		
		progressBar = new ProgressBar(getContext());
		progressBar.setId(1001);
		progressBar.setIndeterminateDrawable(getContext().getResources().getDrawable(R.drawable.cs_progress_loading));
		RelativeLayout.LayoutParams param_bar = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT, android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		param_bar.addRule(RelativeLayout.CENTER_HORIZONTAL);
		progressBar.setLayoutParams(param_bar);
		layout.addView(progressBar);
		
		txtMsg = new TextView(getContext());
		txtMsg.setText("努力加载中，请稍候...");
		txtMsg.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
		txtMsg.setId(1002);
		RelativeLayout.LayoutParams param_txt = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT, android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		param_txt.topMargin = DisplayManager.GetInstance().changeHeightSize(15);
		param_txt.addRule(RelativeLayout.BELOW, 1001);
		txtMsg.setLayoutParams(param_txt);
		layout.addView(txtMsg);
		setContentView(layout);
		
		txtMsg.setText(hint);
		if("".equals(this.hint)){
			progressBar.setVisibility(View.GONE);
			txtMsg.setVisibility(View.GONE);
		}else{
			progressBar.setVisibility(View.VISIBLE);
			txtMsg.setVisibility(View.VISIBLE);
		}
	}
}
