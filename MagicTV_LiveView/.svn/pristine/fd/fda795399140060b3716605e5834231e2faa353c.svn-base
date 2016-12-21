package com.gntv.tv.view;

import com.gntv.tv.R;
import com.gntv.tv.common.ap.AuthManager;
import com.gntv.tv.common.utils.LocalManager;
import com.gntv.tv.common.utils.LogUtil;
import com.gntv.tv.model.channel.ResourceManager;
import com.gntv.tv.report.PageActionReport;
import com.gntv.tv.report.PageActionReport.Action;
import com.gntv.tv.report.PageActionReport.FocusType;
import com.gntv.tv.report.PageActionReport.ModuleType;
import com.gntv.tv.report.PageActionReport.PageType;
import com.gntv.tv.view.base.DisplayManager;
import com.gntv.tv.view.base.FontSize;
import com.gntv.tv.view.base.TimerRelativeLayout;
import com.gntv.tv.view.base.ViewConst;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TVMenuView extends TimerRelativeLayout {

	/**
	 * ID 0x0001 parentTitle_tv 
	 * 0x0002 - 0x0004 parentTextViews
	 * 
	 * 0x1011 subTitle_tv 
	 * 0x1002 - 0x1004 subTextViews
	 * 
	 * 0x1101 parentLayout 
	 * 0x1102 subLayout 
	 * 0x1103 divide 
	 * 
	 * ResourceLayout 0x1104
	 * 0x0101 ResourceTitle 
	 * 0x0102 - 0x0108 ResourceTextViews
	 * 
	 * subItemLayout 0x1012-0x1018
	 */
	private String[] items_str = { "播放模式", "画面比例", "其他设置" };// "收藏管理",
//	private String[][] sub_item_str = {{},{},{}};
	private TextView parentTitle_tv;
	private TextView[] mParentTextViews = new TextView[items_str.length];// 父菜单TextView
	private TextView[] mSubTextViews = new TextView[3];// 子菜单TextView
	private ImageView[] mSubImgViews = new ImageView[3];// 子菜单TextView
//	private TextView[] mResourceTextViews = new TextView[2];
	private ImageView parentTitleBackImage, subTitleBackImage;
	private RelativeLayout mParentLayout, mSubLayout;//, mResourceLayout
	private LinearLayout mSubItemLayout[];//子菜单项
	private Context mContext = null;
	private boolean isFinishInit = false;// 界面是否初始化完成
	private int parentIndex = 0;// 父菜单焦点位置
	private int itemHeight = 0;
	private String useRatio_4_3 = null;
	private Activity mActivity = null;
	private PlayerView mPlayerView = null;
	private PopupWindow mDetectViewPopuWindow = null;
	private DetectView mDetectView = null;
	private String mVersion = null;
	private MyClickListener myClickListener = null;
	private MyFocusChangeListener myFocusChangeListener = null;
	private ColorStateList csl_light = null;
	private ColorStateList csl_deep = null;
	private TextView version_tv = null;
	private int mParentLayoutWidth = 260;
	private int mParentTextViewWidth = 200;
	private int mParentTextViewMargin ;
	private int mSubLayoutWidth =300;
//	private SharedPreferences sp;
	
	public TVMenuView(Activity activity, PlayerView playerView) {
		super(activity);
		init(getContext());
		this.mActivity = activity;
		this.mPlayerView = playerView;
	}

	public TVMenuView(Context context) {
		super(context);
		init(context);
	}

	public TVMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public TVMenuView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) { 
		useRatio_4_3 = ResourceManager.GetInstance().getResourceScale();//0表示铺满全屏，1表示原始比例
		setFocusable(true);
		setFocusableInTouchMode(true);
		setClickable(true);
		mContext = context;
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		Point outSize = new Point();
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		display.getSize(outSize);
		LogUtil.i("TVMenuView--->init--->outSize.y::"+outSize.y);
		itemHeight = (int) (outSize.y / 7.5);
		csl_light = (ColorStateList) mContext.getResources().getColorStateList(R.drawable.cs_menu_focus_textview_text_selector);
		csl_deep = (ColorStateList) mContext.getResources().getColorStateList(R.drawable.cs_menu_nofocus_textview_text_selector);
		myClickListener = new MyClickListener();
		myFocusChangeListener = new MyFocusChangeListener();
		mSubItemLayout = new LinearLayout[3];
		addView(initParentView(context));
		addView(initSubView(context));
		
		setBackgroundResource(R.drawable.cs_channel_left_bg);
		
		
	}

	/**
	 * 初始化左侧列表
	 * 
	 * @param context
	 * @return
	 */
	private View initParentView(Context context) {
		mParentLayout = new RelativeLayout(context);
		mParentLayout.setBackgroundColor(Color.parseColor("#00000000"));
		LayoutParams parentParams = new LayoutParams(changeWidth(mParentLayoutWidth), LayoutParams.MATCH_PARENT);
		parentParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		mParentLayout.setLayoutParams(parentParams);
		mParentLayout.setId(0x1101);
		//mParentLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		
		ImageView line_imgview = new ImageView(context);
		LayoutParams lineParams = new LayoutParams(changeWidth(20), LayoutParams.MATCH_PARENT);
		lineParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		line_imgview.setLayoutParams(lineParams);
		line_imgview.setBackgroundResource(R.drawable.cs_right_divide);
		line_imgview.setId(0x1103);
		int line_imgview_width = changeWidth(20);
		mParentLayout.addView(line_imgview);
		
		parentTitleBackImage = new ImageView(context);
		parentTitleBackImage.setBackgroundResource(R.drawable.cs_menu_parent_title_bg_light);
		LayoutParams backParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		backParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		backParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		backParams.addRule(RelativeLayout.LEFT_OF, 0x1103);
		parentTitleBackImage.setLayoutParams(backParams);
		mParentLayout.addView(parentTitleBackImage);
		
		// 标题
		parentTitle_tv = new TextView(context);
		parentTitle_tv.setText("菜单");
		parentTitle_tv.setTextSize(changeTextSize(FontSize.MENU_TITLE));
		parentTitle_tv.setTextColor(csl_light);
		parentTitle_tv.setGravity(Gravity.LEFT);
		LayoutParams parentTitleParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		parentTitleParams.setMargins(changeWidth(30), changeHeight(-15), 0, 0);
		parentTitle_tv.setLayoutParams(parentTitleParams);
		parentTitle_tv.setBackgroundResource(R.drawable.cs_channel_focus_off);
		parentTitle_tv.setId(0x0001);
		
		version_tv = new TextView(context);
		version_tv.setText("版本号：加载中");
		version_tv.setTextSize(changeTextSize(FontSize.MENU_VERSION));
		version_tv.setTextColor(getResources().getColor(R.color.dark_text));
		version_tv.setGravity(Gravity.CENTER_VERTICAL);
		version_tv.setBackgroundResource(R.drawable.cs_channel_focus_off);
		LayoutParams version_tvParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		version_tvParams.setMargins(changeWidth(20), 0, 0, 0);
		version_tvParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		version_tv.setLayoutParams(version_tvParams);
		mParentLayout.addView(parentTitle_tv);
		mParentLayout.addView(version_tv);
		int size = items_str.length;
		mParentTextViewMargin = (changeWidth(mParentLayoutWidth-mParentTextViewWidth)-line_imgview_width)/2;
		Log.d("--------------", "mParentTextViewMargin"+mParentTextViewMargin);
		for (int i = 0; i < size; i++) {// 循环初始化item
			mParentTextViews[i] = new TextView(context);
			mParentTextViews[i].setText(items_str[i]);
			mParentTextViews[i].setTextSize(changeTextSize(FontSize.MENU_NAME));
			mParentTextViews[i].setTextColor(csl_light);
			mParentTextViews[i].setGravity(Gravity.CENTER);
			LayoutParams params = new LayoutParams(changeWidth(mParentTextViewWidth), itemHeight);
			params.addRule(RelativeLayout.BELOW, 0x0001 + i);
			params.setMargins(mParentTextViewMargin, 0, 0, 0);
			mParentTextViews[i].setLayoutParams(params);
			mParentTextViews[i].setBackgroundResource(R.drawable.cs_menu_textview_bg_selector);
			mParentLayout.addView(mParentTextViews[i]);
			mParentTextViews[i].setFocusable(true);
			mParentTextViews[i].setId(0x0002 + i);
			mParentTextViews[i].setFocusableInTouchMode(true);
			mParentTextViews[i].setOnFocusChangeListener(myFocusChangeListener);
			if(i == 0){
				mParentTextViews[i].setNextFocusRightId(0x0102);
			}else {
				mParentTextViews[i].setNextFocusRightId(0x1002);
			}
		}
		mParentTextViews[0].requestFocus();
		mParentTextViews[0].requestFocusFromTouch();
		return mParentLayout;
	}

	/**
	 * 初始化右侧列表
	 * 
	 * @param context
	 * @return
	 */
	private View initSubView(Context context) {
		mSubLayout = new RelativeLayout(context);
		LayoutParams subParams = new LayoutParams(changeWidth(mSubLayoutWidth), LayoutParams.MATCH_PARENT);
		mSubLayout.setId(0x1102);
		subParams.addRule(RelativeLayout.RIGHT_OF, 0x1101);
		subParams.setMargins(changeWidth(-20), 0, 0, 0);
		mSubLayout.setLayoutParams(subParams);
		// 标题
		mSubTextViews[0] = new TextView(context);
		mSubTextViews[0].setTextSize(changeTextSize(FontSize.MENU_SUB_NAME));
		mSubTextViews[0].setTextColor(getResources().getColor(R.color.white));
		mSubTextViews[0].setGravity(Gravity.CENTER);
		LayoutParams subTitleParams = new LayoutParams(changeWidth(mParentTextViewWidth-10), LayoutParams.WRAP_CONTENT);
		subTitleParams.setMargins(changeWidth(30), changeHeight(-15), 0, 0);
		mSubTextViews[0].setLayoutParams(subTitleParams);
		mSubTextViews[0].setBackgroundResource(R.drawable.cs_channel_focus_off);
		mSubTextViews[0].setId(0x1011);

		mSubLayout.addView(mSubTextViews[0]);

		subTitleBackImage = new ImageView(context);
		subTitleBackImage.setBackgroundResource(R.drawable.cs_menu_sub_title_bg_light);
		LayoutParams backParams = new LayoutParams(changeWidth(mSubLayoutWidth), LayoutParams.WRAP_CONTENT);
		backParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		backParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		backParams.setMargins(changeWidth(2), 0, 0, 0);
		subTitleBackImage.setLayoutParams(backParams);
		mSubLayout.addView(subTitleBackImage);
		
		for (int i = 1; i < 3; i++) {
			mSubItemLayout[i] = new LinearLayout(mContext);
			LayoutParams itemParams = new LayoutParams(LayoutParams.WRAP_CONTENT, itemHeight);
			itemParams.addRule(RelativeLayout.BELOW, 0x1010 + i);
			itemParams.addRule(RelativeLayout.ALIGN_LEFT, 0x1010 + i);
			mSubItemLayout[i].setOrientation(LinearLayout.HORIZONTAL);
			mSubItemLayout[i].setLayoutParams(itemParams);
			mSubItemLayout[i].setId(0x1011 + i);
			
			
			
			mSubTextViews[i] = new TextView(context);
			mSubTextViews[i].setTextSize(changeTextSize(FontSize.MENU_NAME));
			mSubTextViews[i].setTextColor(csl_deep);
			mSubTextViews[i].setGravity(Gravity.CENTER);
			LayoutParams params = new LayoutParams(changeWidth(mParentTextViewWidth), itemHeight);
//			params.addRule(RelativeLayout.BELOW, 0x1000 + i);
//			params.addRule(RelativeLayout.ALIGN_LEFT, 0x1000 + i);
//			if(i == 2){
//				params.addRule(RelativeLayout.ALIGN_RIGHT, 0x1000 + i);
//			}//TODO
			mSubTextViews[i].setLayoutParams(params);
			mSubTextViews[i].setBackgroundResource(R.drawable.cs_menu_textview_bg_selector);
			mSubItemLayout[i].addView(mSubTextViews[i]);
			mSubTextViews[i].setFocusable(true);
			mSubTextViews[i].setFocusableInTouchMode(true);
			mSubTextViews[i].setId(0x1001 + i);
			mSubTextViews[i].setOnClickListener(myClickListener);
			mSubTextViews[i].setOnFocusChangeListener(myFocusChangeListener);
			
			
			
			LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			imgParams.setMargins(changeWidth(20), 0, 0,0 );
			imgParams.gravity = Gravity.CENTER_VERTICAL;  
//			imgParams.addRule(LinearLayout.)
			//imgParams.addRule(RelativeLayout.RIGHT_OF,0x1001 + i);
			mSubImgViews[i] = new ImageView(context);
			mSubImgViews[i].setLayoutParams(imgParams);
			mSubItemLayout[i].addView(mSubImgViews[i]);
			mSubLayout.addView(mSubItemLayout[i]);
		}
		mSubTextViews[1].setNextFocusUpId(0x1003);
		mSubTextViews[2].setNextFocusDownId(0x1002);
		setSubTextViewFocu();
		return mSubLayout;

	}

	/**
	 * 设置右侧子列表内容
	 * 
	 * @param str
	 */
	private void setSubViewText(boolean isIcon, String... str) {
		for (int i = 0; i < str.length; i++) {
			if(i == 1 && isIcon){
				String autostart_value = LocalManager.GetInstance().getLocal("autostart", "0");
				if("0".equals(autostart_value)){
					mSubImgViews[i]
							.setImageResource(R.drawable.auto_start_button_off_dark);
				}else {
					
					mSubImgViews[i]
							.setImageResource(R.drawable.auto_start_button_on_dark);
				}
			}
			mSubTextViews[i].setText(str[i]);
		}
	}

	private class MyFocusChangeListener implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			startDisplayTimer();
			if (hasFocus & isFinishInit) {
				switch (v.getId()) {
				case 0x0002://切换源父菜单获取焦点
					resetSubTextViewDeepColor();
					resetParentTextViewDarkColor();//调整父菜单字体选中颜色
					setSubViewText(false, "播放模式", "高清优先", "流畅优先");
					resetSubRightIcon();//重置右侧图标
					changeResourceTextViewColor(false);//TODO
					parentIndex = 0;//设置父菜单索引
					setSubTextViewFocu();//设置子菜单焦点切换规则
					break;
				case 0x0002 + 1://画面比例父菜单获取焦点
					resetSubRightIcon();//重置右侧图标
					resetParentTextViewDarkColor();
					resetSubTextViewDeepColor();
					setSubViewText(false, "画面比例", "铺满全屏", "原始比例");
					changeSubFocusColor(false);
					parentIndex = 1;
					setSubTextViewFocu();//设置子菜单焦点切换规则
					break;
				case 0x0002 + 2://其他设置父菜单获取焦点
					resetSubRightIcon();//重置右侧图标
					resetSubTextViewDeepColor();
					resetParentTextViewDarkColor();
					setSubViewText(true, "其他设置", "开机启动 ", "故障检测");
					parentIndex = 2;
					setSubTextViewFocu();
					break;
				case 0x0102:
				case 0x1002://子菜单获取焦点
					resetSubTextViewDarkColor();
					switch (parentIndex) {
					case 0:
						changeResourceTextViewColor(true);
						break;
					case 1:
						changeSubFocusColor(true);
						break;
					case 2:
						setAutoStartButonColor();
						break;
					}
					changeParentFocusColor();
					break;
				default:
					break;
				}
			}
		}
	}

	private void setAutoStartButonColor(){
		String autostart_value = LocalManager.GetInstance().getLocal("autostart", "0");
		changeAutoStartButonColor("0".equals(autostart_value),true);
	}
	private void changeAutoStartButonColor(boolean autostart_value_0,boolean isLight){
		if(autostart_value_0){
			if(isLight){
				mSubImgViews[1]
						.setImageResource(R.drawable.auto_start_button_off_light);
			}else{
				mSubImgViews[1]
						.setImageResource(R.drawable.auto_start_button_off_dark);
			}
		}else {
			if(isLight){
				mSubImgViews[1]
						.setImageResource(R.drawable.auto_start_button_on_light);
			}else{
				mSubImgViews[1]
						.setImageResource(R.drawable.auto_start_button_on_dark);
			}

		}
	}
	/**
	 * 当切换到子菜单时，重置父菜单字体颜色，并设置选中为蓝色
	 */
	private void changeParentFocusColor(){
		resetParentTextViewDeepColor();
		mParentTextViews[parentIndex].setTextColor(getResources().getColor(R.color.dark_blue_text));
	}
	
	private void resetParentTextViewDeepColor(){
		int len = items_str.length;
		for (int i = 0; i < len; i++) {
			mParentTextViews[i].setTextColor(csl_deep);
		}
		parentTitle_tv.setTextColor(csl_deep);
		parentTitleBackImage.setBackgroundResource(R.drawable.cs_menu_parent_title_bg_dark);
	}
	
	
	private void resetParentTextViewDarkColor(){
		int len = items_str.length;
		for (int i = 0; i < len; i++) {
			mParentTextViews[i].setTextColor(csl_light);
		}
		parentTitle_tv.setTextColor(csl_light);
		parentTitleBackImage.setBackgroundResource(R.drawable.cs_menu_parent_title_bg_light);
	}
	/**
	 * 改变画面比例时
	 * @param isLight
	 */
	private void changeSubFocusColor(boolean isLight){
		if("1".equals(useRatio_4_3)){
			mSubImgViews[1]
					.setImageResource(R.drawable.cs_menu_noselected);
			if(isLight){
				mSubImgViews[2]
						.setImageResource(R.drawable.cs_menu_selected_light);
			}else{
				mSubImgViews[2]
						.setImageResource(R.drawable.cs_menu_selected_dark);
			}
		}else {
			mSubImgViews[2]
					.setImageResource(R.drawable.cs_menu_noselected);
			if(isLight){
				mSubImgViews[1]
						.setImageResource(R.drawable.cs_menu_selected_light);
			}else{
				mSubImgViews[1]
						.setImageResource(R.drawable.cs_menu_selected_dark);
			}
		}
	}
	
	/**
	 * 子菜单获取焦点时设置字体颜色
	 */
	private void resetSubTextViewDarkColor(){
		for (int i = 0; i < 3; i++) {
			mSubTextViews[i].setTextColor(csl_light);
		}
		subTitleBackImage.setBackgroundResource(R.drawable.cs_menu_sub_title_bg_light);
	}
	
	/**
	 * 子菜单失去焦点时设置字体颜色
	 */
	private void resetSubTextViewDeepColor(){
		for (int i = 0; i < 3; i++) {
			mSubTextViews[i].setTextColor(csl_deep);
		}
		subTitleBackImage.setBackgroundResource(R.drawable.cs_menu_sub_title_bg_dark);
	}
	
	/**
	 * 设置切换源的选中项
	 */
	private void changeResourceTextViewColor(boolean isLight){
		String resourceState = ResourceManager.GetInstance().getResourceState();
		changeResourceTextViewColor(isLight,resourceState);
	}
	/**
	 * 切换源的选中项
	 */
	private void changeResourceTextViewColor(boolean isLight, String index){
		if("0".equals(index)){
			
			mSubImgViews[2]
					.setImageResource(R.drawable.cs_menu_noselected);
			if(isLight){
				mSubImgViews[1]
						.setImageResource(R.drawable.cs_menu_selected_light);
			}else{
				mSubImgViews[1]
						.setImageResource(R.drawable.cs_menu_selected_dark);
			}
		}else {
			mSubImgViews[1]
					.setImageResource(R.drawable.cs_menu_noselected);
			if(isLight){
				mSubImgViews[2]
						.setImageResource(R.drawable.cs_menu_selected_light);
			}else{
				mSubImgViews[2]
						.setImageResource(R.drawable.cs_menu_selected_dark);
			}
		}
	}
	/**
	 * chuon
	 */
	private void resetSubRightIcon(){
		for (int i = 1; i < mSubTextViews.length; i++) {
			mSubImgViews[i].setImageResource(R.drawable.cs_menu_noselected);
		}
	}
	private class MyClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (parentIndex) {
			case 0:// 父菜单：切换源
				switch (v.getId()) {
				case 0x1002://高清优先
						resetSubTextViewDarkColor();
						changeResourceTextViewColor(true,"0");
						mPlayerView.changeResources("0");
						PageActionReport.GetInstance().reportPageAction(PageType.PlayPage, ModuleType.SetFunc, 
								"1", FocusType.SwitchSource, Action.OKKey);
					break;
				case 0x1003://流畅优先
						resetSubTextViewDarkColor();
						changeResourceTextViewColor(true,"1");
						mPlayerView.changeResources("1");
						PageActionReport.GetInstance().reportPageAction(PageType.PlayPage, ModuleType.SetFunc, 
								"0", FocusType.SwitchSource, Action.OKKey);
					break;
				default:
					break;
				}
				break;
			case 1:// 画面比例
				switch (v.getId()) {
				case 0x1002:
					if("1".equals(useRatio_4_3)){//点击铺满全屏
						useRatio_4_3 = "0";
						ResourceManager.GetInstance().saveResourceScale(useRatio_4_3);
						mPlayerView.changeAspectRatio(useRatio_4_3);
						resetSubTextViewDarkColor();
						changeSubFocusColor(true);
						PageActionReport.GetInstance().reportPageAction(PageType.PlayPage, ModuleType.SetFunc, 
								"1", FocusType.PictureRatio, Action.OKKey);
					}
					break;
				case 0x1003:
					if("0".equals(useRatio_4_3)){//点击原始比例
						useRatio_4_3 = "1";
						ResourceManager.GetInstance().saveResourceScale(useRatio_4_3);
						mPlayerView.changeAspectRatio(useRatio_4_3);
						resetSubTextViewDarkColor();
						changeSubFocusColor(true);
						PageActionReport.GetInstance().reportPageAction(PageType.PlayPage, ModuleType.SetFunc, 
								"0", FocusType.PictureRatio, Action.OKKey);
					}
					break;
				default:
					break;
				}
				break;
			case 2:// 其他设置
				switch (v.getId()) {
				case 0x1002:
					String autostart_value = LocalManager.GetInstance().getLocal("autostart", "0");
					boolean autostart="0".equals(autostart_value);
					if(autostart){
						LocalManager.GetInstance().saveLocal("autostart", "1");
						PageActionReport.GetInstance().reportPageAction(PageType.PlayPage, ModuleType.SetFunc, 
								"1", FocusType.BootBoot, Action.OKKey);
					}else {
						LocalManager.GetInstance().saveLocal("autostart", "0");
						PageActionReport.GetInstance().reportPageAction(PageType.PlayPage, ModuleType.SetFunc, 
								"0", FocusType.BootBoot, Action.OKKey);
					}
					changeAutoStartButonColor(!autostart,true);
					break;
				case 0x1003:
					showDetectView();
					break;
				case 0x1004://帮助
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
		}

	}
	
	private void setSubTextViewFocu() {
		int len = mSubTextViews.length;
		for (int i = 1; i < len; i++) {
			mSubTextViews[i].setNextFocusLeftId(0x0002 + parentIndex);
		}
	}
	
	@Override
	protected void timeOut() {
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				hide();
			}
		});
	}

	public void show() {
		if (getVisibility() == View.GONE) {
			setVisibility(View.VISIBLE);
			mParentTextViews[0].requestFocus();
			resetParentTextViewDarkColor();
			changeResourceTextViewColor(false);
			String[] split = mVersion.split("[.]");
			if(split.length > 0){
				version_tv.setText("版本号："+split[0]+"."+split[1]);
			}
		}

	}

	public void hide() {
		if (getVisibility() == View.VISIBLE)
			setVisibility(View.GONE);
	}

	public void toggle() {
		if(ViewConst.LIVE_STATE == mPlayerView.getPlayState() && isFinishInit){
			if (getVisibility() == View.VISIBLE) {
				hide();
				PageActionReport.GetInstance().reportPageAction(PageType.PlayPage, ModuleType.SetFunc, null, null, Action.MenuKey);
			} else {
				show();
				PageActionReport.GetInstance().reportPageAction(PageType.PlayPage, ModuleType.SetFunc, null,null, Action.MenuKey);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtil.i("TVMenuView--------->onKeyDown----->" + keyCode);
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (TVMenuView.this.getVisibility() == View.VISIBLE) {
				hide();
				PageActionReport.GetInstance().reportPageAction(PageType.PlayPage, ModuleType.SetFunc,
						null, null, Action.ExitKey);
				return true;
			} else {
				return false;
			}
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private int changeWidth(int width) {
		return DisplayManager.GetInstance().changeWidthSize(width);
	}

	private int changeHeight(int height) {
		return DisplayManager.GetInstance().changeHeightSize(height);
	}

	private int changeTextSize(int size) {
		return DisplayManager.GetInstance().changeTextSize(size);
	}
	
	public void setInitFinish(boolean isFinishInit){
		this.isFinishInit = isFinishInit;
	}
	
	public void setVersion(String version){
		this.mVersion = version;
	}
	
	public String getVersion(){
		return this.mVersion;
	}
	
	private void showDetectView(){
		if(mDetectViewPopuWindow == null){
			mDetectView = new DetectView(mContext, mActivity, mVersion,AuthManager.GetInstance().getUser());
			mDetectViewPopuWindow = new PopupWindow(mDetectView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			mDetectViewPopuWindow.setBackgroundDrawable(new BitmapDrawable());
			mDetectViewPopuWindow.setFocusable(true);
			mDetectViewPopuWindow.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					hide();
					mDetectView.stopDetect();
					mPlayerView.preparePlay(true);
				}
			});
		}
		mDetectViewPopuWindow.showAtLocation(this, Gravity.CENTER, 0, 0);
		mPlayerView.release();
		mDetectView.requestButtonFocus();
	}
	
	public void stopDetectAndHide(){
		if(mDetectView!=null&&mDetectViewPopuWindow!=null){
			hide();
			mDetectView.cleanDialog();
			mDetectViewPopuWindow.dismiss();
			mDetectView.stopDetect();
		}
	}
}
