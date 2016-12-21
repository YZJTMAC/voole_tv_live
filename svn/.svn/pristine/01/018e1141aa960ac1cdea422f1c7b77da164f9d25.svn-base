package com.gntv.tv.common.utils;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.BitmapGlobalConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

public class ImageManager {
	private static ImageManager instance = new ImageManager();
	private BitmapUtils utils = null;

	private ImageManager() {
	}

	public static ImageManager GetInstance() {
		return instance;
	}

	public void init(Context context) {
		init(context, "sdcard/voole/image");
	}

	public void init(Context context, String path) {
		if (utils != null) {
			return;
		}
		utils = new BitmapUtils(context, path, 6 * 1024 * 1024, 1024 * 1024 * 100);
		BitmapGlobalConfig globalConfig = BitmapGlobalConfig.getInstance(context, path);
		globalConfig.setMemoryCacheEnabled(true);
		globalConfig.setMemoryCacheSize(6 * 1024 * 1024);
		globalConfig.setDiskCacheEnabled(true);
		globalConfig.setThreadPoolSize(5);
		globalConfig.setDiskCacheSize(1024 * 1024 * 50);
		utils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
	}

	public void clearCache() {
		if (utils != null) {
			utils.clearMemoryCache();
			utils.clearDiskCache();
		}
	}

	public void clearMemoryCache() {
		if (utils != null) {
			utils.clearMemoryCache();
		}
	}

	// public void displayImage(String url, ImageView imageView, Drawable
	// drawable) {
	// BitmapDisplayConfig config = new BitmapDisplayConfig();
	// config.setLoadingDrawable(drawable);
	// config.setLoadFailedDrawable(drawable);
	// utils.display(imageView, url, config);
	// }

	public void displayImage(String url, ImageView imageView, final ImageListener listener) {

		if (listener != null) {
			utils.display(imageView, url, new BitmapLoadCallBack<ImageView>() {
				@Override
				public void onLoadCompleted(ImageView imageView, String uri, Bitmap bitmap,
						BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
					listener.onLoadingComplete(uri, imageView, bitmap);
				}

				@Override
				public void onLoadFailed(ImageView arg0, String arg1, Drawable arg2) {
					listener.onLoadingFailed(arg1, arg0);
				}
			});
		}
	}

	public void displayImage(final String url, ImageView imageView, final ImageListener listener, Drawable drawable) {
		if (listener != null) {
			BitmapDisplayConfig config = new BitmapDisplayConfig();
			config.setLoadingDrawable(drawable);
			config.setLoadFailedDrawable(drawable);
			utils.display(imageView, url, config, new BitmapLoadCallBack<ImageView>() {
				@Override
				public void onLoadCompleted(ImageView imageView, String uri, Bitmap bitmap,
						BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
					listener.onLoadingComplete(uri, imageView, bitmap);
				}

				@Override
				public void onLoadFailed(ImageView arg0, String arg1, Drawable arg2) {
					listener.onLoadingFailed(arg1, arg0);
				}
			});

		}
	}

	public void displayImage(String url, View view, final ImageListener listener) {
		if (listener != null) {
			utils.display(view, url, new BitmapLoadCallBack<View>() {
				@Override
				public void onLoadCompleted(View imageView, String uri, Bitmap bitmap,
						BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
					listener.onLoadingComplete(uri, imageView, bitmap);
				}

				@Override
				public void onLoadFailed(View arg0, String arg1, Drawable arg2) {
					listener.onLoadingFailed(arg1, arg0);
				}
			});
		}
	}

	public void displayImage(String url, final ImageView imageView) {
		utils.display(imageView, url);
	}

	public void displayImage(String url, final View imageView) {
		utils.display(imageView, url);

	}

	public interface ImageListener {
		public void onLoadingStarted(String uri, View view);

		public void onLoadingFailed(String uri, View view);

		public void onLoadingComplete(String uri, View view, Bitmap bitmap);
	}
}
