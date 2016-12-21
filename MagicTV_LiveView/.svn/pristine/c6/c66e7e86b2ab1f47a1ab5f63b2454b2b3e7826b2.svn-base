package com.gntv.tv.view;

import com.gntv.tv.R;
import com.gntv.tv.model.channel.ChannelItem;
import com.gntv.tv.view.base.AlwaysMarqueeTextView;
import com.gntv.tv.view.base.BaseChannelRelativeLayout;
import com.gntv.tv.view.base.DisplayManager;
import com.gntv.tv.view.base.FontSize;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ChannelItemView extends BaseChannelRelativeLayout {
	private AlwaysMarqueeTextView channelNameView = null;
	private AlwaysMarqueeTextView currentPlayView = null;
	private String cid = null;
	
	private ImageView iv_shift = null;
	private ImageView iv_back = null;
	private ImageView iv_hd = null;

	public ChannelItemView(Context context) {
		super(context);
		init(context);
	}
	
	@Override
	protected void init(Context context){
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		RelativeLayout top_rl = new RelativeLayout(context);
		top_rl.setId(200010);
		LayoutParams top_params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		top_rl.setLayoutParams(top_params);
		top_rl.setPadding(changeWidth(15), 0, changeWidth(15), 0);
		int width = DisplayManager.GetInstance().getScreenWidth() *3/7; 
		channelNameView = new AlwaysMarqueeTextView(context);
		channelNameView.setId(20001);
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(width*3/7-changeWidth(75), LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		//params1.setMargins(changeWidth(15), 0, 0, 0);
		params1.addRule(RelativeLayout.CENTER_VERTICAL);
		channelNameView.setTextSize(changeTextSize(FontSize.CHANNEL_NAME));
		channelNameView.setTextColor(getColor(R.color.deep_text));
		channelNameView.setLayoutParams(params1);
		channelNameView.setSingleLine(true);
		channelNameView.setEllipsize(TruncateAt.END);
		top_rl.addView(channelNameView);
		
		iv_shift = new ImageView(context);
		iv_shift.setId(2000201);
		iv_shift.setVisibility(GONE);
		iv_shift.setBackgroundResource(R.drawable.cs_shift_unfocus);
		LayoutParams shift_params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		shift_params.addRule(ALIGN_PARENT_RIGHT);
		shift_params.addRule(RelativeLayout.CENTER_VERTICAL);
		iv_shift.setLayoutParams(shift_params);
		top_rl.addView(iv_shift);
		
		iv_back = new ImageView(context);
		iv_back.setId(20002);
		iv_back.setBackgroundResource(R.drawable.cs_back_unfocus);
		LayoutParams back_params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		back_params.addRule(LEFT_OF,2000201);
		back_params.addRule(RelativeLayout.CENTER_VERTICAL);
		//back_params.setMargins(0, changeHeight(6), changeWidth(15), 0);
		iv_back.setLayoutParams(back_params);
		top_rl.addView(iv_back);
		
		iv_hd = new ImageView(context);
		iv_hd.setId(20003);
		iv_hd.setBackgroundResource(R.drawable.cs_hd_unfocus);
		LayoutParams hd_params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		iv_hd.setLayoutParams(hd_params);
		hd_params.setMargins(0, 0, changeWidth(5), 0);
		hd_params.addRule(RelativeLayout.CENTER_VERTICAL);
		hd_params.addRule(LEFT_OF,20002);
		//hd_params.addRule(ALIGN_TOP, 20002);
		top_rl.addView(iv_hd);
		addView(top_rl);
		LayoutParams params2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		currentPlayView = new AlwaysMarqueeTextView(context);
		currentPlayView.setId(20004);
		currentPlayView.setTextSize(changeTextSize(FontSize.CHANNEL_SUB_NAME));
		currentPlayView.setTextColor(getColor(R.color.deep_text));
		currentPlayView.setLayoutParams(params2);
		params2.addRule(BELOW, 200010);
		currentPlayView.setPadding(changeWidth(15), 0,changeWidth(15), 0);
		currentPlayView.setSingleLine(true);
		currentPlayView.setEllipsize(TruncateAt.END);
		addView(currentPlayView);
	}
	
	public void setData(ChannelItem item){
		if(item!=null){
			channelNameView.setText(item.getTitle());
		/*	if("1".equals(item.getIsBackView())){
				iv_shift.setVisibility(VISIBLE);
			}else{
				iv_shift.setVisibility(GONE);
			}*/
			if("1".equals(item.getBackEnable())){
				iv_back.setVisibility(VISIBLE);
				LayoutParams back_params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				if(iv_shift.getVisibility()!=VISIBLE){
					back_params.addRule(ALIGN_PARENT_RIGHT);
					back_params.addRule(RelativeLayout.CENTER_VERTICAL);
				}else{
					back_params.setMargins(0, 0, changeWidth(5), 0);
					back_params.addRule(LEFT_OF,2000201);
					back_params.addRule(RelativeLayout.CENTER_VERTICAL);
				}
				iv_back.setLayoutParams(back_params);
			}else{
				iv_back.setVisibility(GONE);
			}
			if("1".equals(item.getHasHD())){
				iv_hd.setVisibility(VISIBLE);
				LayoutParams hd_params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				if(iv_back.getVisibility()!=VISIBLE){
					if(iv_shift.getVisibility()!=VISIBLE){
						hd_params.addRule(ALIGN_PARENT_RIGHT);
						hd_params.addRule(RelativeLayout.CENTER_VERTICAL);
					}else{
						hd_params.setMargins(0, 0, changeWidth(5), 0);
						hd_params.addRule(LEFT_OF,2000201);
						hd_params.addRule(RelativeLayout.CENTER_VERTICAL);
					}
					
				}else{
					hd_params.setMargins(0, 0, changeWidth(5), 0);
					hd_params.addRule(LEFT_OF,20002);
					hd_params.addRule(RelativeLayout.CENTER_VERTICAL);
				}
				iv_hd.setLayoutParams(hd_params);
			}else{
				iv_hd.setVisibility(GONE);
			}
			this.cid = item.getChannelId();
		}
	}
	
	public void setContent(String content){
		currentPlayView.setText(content);
	}
	
	public void setPlaying(boolean isPlaying){
		if(isPlaying){
			channelNameView.setTextColor(getColor(R.color.dark_blue_text));
			currentPlayView.setTextColor(getColor(R.color.dark_blue_text));
		}else{
			channelNameView.setTextColor(getColor(R.color.deep_text));
			currentPlayView.setTextColor(getColor(R.color.deep_text));
		}
	}
	
	public void setMarquee(boolean state){
		if(state){
			channelNameView.setEllipsize(TruncateAt.MARQUEE);
			channelNameView.setHorizontallyScrolling(true);
			channelNameView.setMarqueeRepeatLimit(-1);
			currentPlayView.setEllipsize(TruncateAt.MARQUEE);
			currentPlayView.setHorizontallyScrolling(true);
			currentPlayView.setMarqueeRepeatLimit(-1);
		}else{
			channelNameView.setEllipsize(TruncateAt.END);
			currentPlayView.setEllipsize(TruncateAt.END);
		}
	}
	
	public void setSelected(boolean isSelected){
		if(isSelected){
			channelNameView.setTextColor(getColor(R.color.light_blue_text));
			currentPlayView.setTextColor(getColor(R.color.light_blue_text));
			this.setBackgroundResource(R.drawable.border_shape);
		}else{
			channelNameView.setTextColor(getColor(R.color.dark_text));
			currentPlayView.setTextColor(getColor(R.color.dark_text));
			this.setBackgroundResource(0);
		}
	}

	@Override
	public void focusEvent(boolean isFocus) {
		channelNameView.setEllipsize(TruncateAt.END);
		currentPlayView.setEllipsize(TruncateAt.END);
		if(isFocus){
			channelNameView.setTextColor(getColor(R.color.dark_text));
			currentPlayView.setTextColor(getColor(R.color.dark_text));
			iv_shift.setBackgroundResource(R.drawable.cs_shift_focus);
			iv_back.setBackgroundResource(R.drawable.cs_back_focus);
			iv_hd.setBackgroundResource(R.drawable.cs_hd_focus);
		}else{
			channelNameView.setTextColor(getColor(R.color.deep_text));
			currentPlayView.setTextColor(getColor(R.color.deep_text));
			iv_shift.setBackgroundResource(R.drawable.cs_shift_unfocus);
			iv_back.setBackgroundResource(R.drawable.cs_back_unfocus);
			iv_hd.setBackgroundResource(R.drawable.cs_hd_unfocus);
		}
	}
	
	public void changeIcon(boolean isFocus){
		if(isFocus){
			iv_shift.setBackgroundResource(R.drawable.cs_shift_focus);
			iv_back.setBackgroundResource(R.drawable.cs_back_focus);
			iv_hd.setBackgroundResource(R.drawable.cs_hd_focus);
		}else{
			iv_shift.setBackgroundResource(R.drawable.cs_shift_unfocus);
			iv_back.setBackgroundResource(R.drawable.cs_back_unfocus);
			iv_hd.setBackgroundResource(R.drawable.cs_hd_unfocus);
		}
	}

	public String getCid() {
		return cid;
	}

	@Override
	public void clearAllSelected() {
		// TODO Auto-generated method stub
		
	}

	public AlwaysMarqueeTextView getChannelNameView() {
		return channelNameView;
	}

	public void setChannelNameView(AlwaysMarqueeTextView channelNameView) {
		this.channelNameView = channelNameView;
	}

	public AlwaysMarqueeTextView getCurrentPlayView() {
		return currentPlayView;
	}

	public void setCurrentPlayView(AlwaysMarqueeTextView currentPlayView) {
		this.currentPlayView = currentPlayView;
	}

	
}
