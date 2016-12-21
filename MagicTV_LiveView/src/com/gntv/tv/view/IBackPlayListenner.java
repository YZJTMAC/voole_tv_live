package com.gntv.tv.view;

public interface IBackPlayListenner {
	void onTimeOut();
	void seekTo(long msec);
	void onPause();
	void onResume(long stime);
	void onBackToLive();
}
