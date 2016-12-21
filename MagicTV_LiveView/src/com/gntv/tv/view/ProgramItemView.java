package com.gntv.tv.view;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.gntv.tv.R;
import com.gntv.tv.model.channel.ProgramItem;
import com.gntv.tv.view.base.AlwaysMarqueeTextView;
import com.gntv.tv.view.base.BaseChannelRelativeLayout;
import com.gntv.tv.view.base.FontSize;

public class ProgramItemView extends BaseChannelRelativeLayout {
	private AlwaysMarqueeTextView programView = null;
	private AlwaysMarqueeTextView programTimeView = null;
	private ImageView ivState = null;
	//private RelativeLayout rl_top = null;
	private boolean isShow = false;
	private boolean state = false;
	private boolean isSupport = false;
	private ProgramItem programItem = null;
	
	public ProgramItemView(Context context) {
		super(context);
		init(context);
	}

	@Override
	protected void init(Context context) {
		RelativeLayout top_rl = new RelativeLayout(context);
		top_rl.setId(300010);
		LayoutParams top_params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		top_rl.setLayoutParams(top_params);
		top_rl.setPadding(changeWidth(15), 0, changeWidth(15), 0);
		
		programTimeView = new AlwaysMarqueeTextView(context);
		programTimeView.setId(30001);
		programTimeView.setGravity(Gravity.CENTER_VERTICAL);
		RelativeLayout.LayoutParams ptv_params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		ptv_params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		ptv_params.addRule(RelativeLayout.CENTER_VERTICAL);
		programTimeView.setTextSize(changeTextSize(FontSize.CHANNEL_SUB_NAME));
		programTimeView.setTextColor(getColor(R.color.deep_text));
		programTimeView.setLayoutParams(ptv_params);
		top_rl.addView(programTimeView);
		
		ivState = new ImageView(context);
		ivState.setId(30002);
		RelativeLayout.LayoutParams iv_params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		iv_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		iv_params.addRule(RelativeLayout.CENTER_VERTICAL);
		ivState.setLayoutParams(iv_params);
		top_rl.addView(ivState);
		addView(top_rl);
		
		programView = new AlwaysMarqueeTextView(context);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		programView.setTextSize(changeTextSize(FontSize.CHANNEL_NAME));
		programView.setTextColor(getColor(R.color.deep_text));
		params.addRule(BELOW, 300010);
		//params.addRule(CENTER_VERTICAL);
		programView.setPadding(changeWidth(15), 0, changeWidth(15), 0);
		programView.setLayoutParams(params);
		programView.setSingleLine(true);
		programView.setEllipsize(TruncateAt.END);
		addView(programView);
		
	}
	
	public void setData(String content,String date,boolean isShow,boolean state,boolean isSupport,ProgramItem item){
		programView.setText(content);
		programTimeView.setText(date);
		this.isShow = isShow;
		this.state = state;
		this.isSupport = isSupport;
		if(isShow){
			if(state){
				ivState.setBackgroundResource(R.drawable.cs_play_unfocus);
			}else{
				if(isSupport){
					ivState.setBackgroundResource(R.drawable.cs_back_unfocus);
				}else{
					ivState.setBackgroundResource(0);
				}
			}
		}else{
			ivState.setBackgroundResource(0);
		}
		this.programItem = item;
	}
	
	public void setPlaying(boolean isPlaying){
		if(isPlaying){
			programView.setTextColor(getColor(R.color.dark_blue_text));
			programTimeView.setTextColor(getColor(R.color.dark_blue_text));
			if(isShow){
				if(state){
					ivState.setBackgroundResource(R.drawable.cs_play_focus);
				}else{
					if(isSupport){
						ivState.setBackgroundResource(R.drawable.cs_back_focus);
					}else{
						ivState.setBackgroundResource(0);
					}
				}
			}else{
				ivState.setBackgroundResource(0);
			}
		}else{
			programView.setTextColor(getColor(R.color.deep_text));
			programTimeView.setTextColor(getColor(R.color.deep_text));
			if(isShow){
				if(state){
					ivState.setBackgroundResource(R.drawable.cs_play_focus);
				}else{
					if(isSupport){
						ivState.setBackgroundResource(R.drawable.cs_back_focus);
					}else{
						ivState.setBackgroundResource(0);
					}
				}
			}else{
				ivState.setBackgroundResource(0);
			}
		}
	}
	
	public void setMarquee(boolean state){
		if(state){
			programView.setEllipsize(TruncateAt.MARQUEE);
			programView.setHorizontallyScrolling(true);
			programView.setMarqueeRepeatLimit(-1);
		}else{
			programView.setEllipsize(TruncateAt.END);
		}
	}
	
	public void setSelected(boolean isSelected){
		if(isSelected){
			
			programView.setTextColor(getColor(R.color.light_blue_text));
			programTimeView.setTextColor(getColor(R.color.light_blue_text));
			if(isShow){
				if(state){
					ivState.setBackgroundResource(R.drawable.cs_play_focus);
				}else{
					if(isSupport){
						ivState.setBackgroundResource(R.drawable.cs_back_focus);
					}else{
						ivState.setBackgroundResource(0);
					}
				}
			}else{
				ivState.setBackgroundResource(0);
			}
			this.setBackgroundResource(R.drawable.border_shape);
		}else{
			programView.setTextColor(getColor(R.color.dark_text));
			programTimeView.setTextColor(getColor(R.color.dark_text));
			if(isShow){
				if(state){
					ivState.setBackgroundResource(R.drawable.cs_play_focus);
				}else{
					if(isSupport){
						ivState.setBackgroundResource(R.drawable.cs_back_focus);
					}else{
						ivState.setBackgroundResource(0);
					}
				}
			}else{
				ivState.setBackgroundResource(0);
			}
			this.setBackgroundResource(0);
		}
	}

	@Override
	public void focusEvent(boolean isFocus) {
		programView.setEllipsize(TruncateAt.END);
		if(isFocus){
			programView.setTextColor(getColor(R.color.dark_text));
			programTimeView.setTextColor(getColor(R.color.dark_text));
		}else{
			programView.setTextColor(getColor(R.color.deep_text));
			programTimeView.setTextColor(getColor(R.color.deep_text));
		}
		
		if(isShow){
			if(state){
				if(isFocus){
					ivState.setBackgroundResource(R.drawable.cs_play_focus);
				}else{
					ivState.setBackgroundResource(R.drawable.cs_play_unfocus);
				}
			}else{
				if(isSupport){
					if(isFocus){
						ivState.setBackgroundResource(R.drawable.cs_back_focus);
					}else{
						ivState.setBackgroundResource(R.drawable.cs_back_unfocus);
					}
				}else{
					ivState.setBackgroundResource(0);
				}
			}
		}else{
			ivState.setBackgroundResource(0);
		}
	}
	
	public void changeIcon(boolean isFocus){
		if(isShow){
			if(state){
				if(isFocus){
					ivState.setBackgroundResource(R.drawable.cs_play_focus);
				}else{
					ivState.setBackgroundResource(R.drawable.cs_play_unfocus);
				}
			}else{
				if(isSupport){
					if(isFocus){
						ivState.setBackgroundResource(R.drawable.cs_back_focus);
					}else{
						ivState.setBackgroundResource(R.drawable.cs_back_unfocus);
					}
				}else{
					ivState.setBackgroundResource(0);
				}
			}
		}else{
			ivState.setBackgroundResource(0);
		}
	}

	public ProgramItem getProgramItem() {
		return programItem;
	}

	@Override
	public void clearAllSelected() {
		// TODO Auto-generated method stub
		
	}
}
