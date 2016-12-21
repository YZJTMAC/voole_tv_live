package com.gntv.tv.view.base;

import com.gntv.tv.common.utils.LogUtil;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class DisplayManager {
	private static DisplayManager instance = new DisplayManager();
	private float value;
	private DisplayMetrics displayMetrics;
	private float density = 0;

	public void init(Context context){
		displayMetrics = new DisplayMetrics();
		displayMetrics = context.getResources().getDisplayMetrics();
		screenWidth = displayMetrics.widthPixels;
		screenHeight = displayMetrics.heightPixels;
		density = displayMetrics.density;
		LogUtil.d("DisplayManager real size-->"+screenWidth+"x"+screenHeight+",screenDpi:"+screenDpi+",density:"+density);
		if(screenHeight < 720 && screenHeight > 480){
			screenHeight = 720;
		}else if(screenHeight>720&&screenHeight<1080){
			screenHeight = 1080;
		}
		screenDpi = displayMetrics.densityDpi;
		value = displayMetrics.scaledDensity;
		LogUtil.d("DisplayManager adapt size-->"+screenWidth+"x"+screenHeight+",screenDpi:"+screenDpi);
	}
	
	private DisplayManager() {
	 
	}

	public static DisplayManager GetInstance() {
		return instance;
	}

	private static final int STANDARD_WIDTH = 1280;
	private static final int STANDARD_HEIGHT = 720;
	private static final int STANDARD_DENSITY = 160;

	private int screenWidth = 0;
	private int screenHeight = 0;
	private int screenDpi = 0;

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public int getScreenDpi() {
		return screenDpi;
	}

	public int changeTextSize(int size) {
		/*float rate_width = (float) screenWidth / STANDARD_WIDTH;
		//float rate_height = (float) screenHeight / STANDARD_HEIGHT;
		if (value != 1.0 && STANDARD_WIDTH == screenWidth) {
			return (int) (size*value);
		}
		return (int) (size*rate_width);*/
		return factWidthProportion((int)(size/density));

	}
	
	public int factHeightProportion(int currentHeight){
		return currentHeight*screenHeight/1080;

	}
	
	/**
	 * 计算宽度比例
	 * @param currentWidth 输入UI效果图上的宽度(按照1080P来切图)
	 * @return 实际需要设置的宽度
	 */
	public int factWidthProportion(int currentWidth){
		return currentWidth*screenWidth/1920;
	}

	public int changePaintSize(int size) {
		return changeWidthSize(size);
	}

	public int changeWidthSize(int size) {
		float rate = (float) screenWidth / STANDARD_WIDTH;
		return (int) (size * rate);
	}

	public int changeHeightSize(int size) {
		float rate = (float) screenHeight / STANDARD_HEIGHT;
		return (int) (size * rate);
	}
}
