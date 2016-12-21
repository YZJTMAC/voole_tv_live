package com.gntv.tv.model.time;

import java.util.ArrayList;
import java.util.List;

import com.gntv.tv.common.utils.DateUtil;
import com.gntv.tv.model.channel.ProgramManager;

public class TimeManager {
	private static final String TIME_PATTERN = "yyyy-MM-dd kk:mm:ss";
	private static TimeManager instance = new TimeManager();

	private TimeManager() {
	}

	public static TimeManager GetInstance() {
		return instance;
	}
	
	private long mCurrentTime = 0;
	
	public long getCurrentTime(){
		if(mCurrentTime > 0){
			return mCurrentTime;
		}else{
			return System.currentTimeMillis();
		}
	}
	
	public void addCurrentTime(int msec){
		if(mCurrentTime > 0){
			mCurrentTime += msec;
		}
	}
	
	public long getServerTime(){
		String time = ProgramManager.GetInstance().getCurrentTime();
		if(time != null){
			mCurrentTime = DateUtil.string2Msec(time, TIME_PATTERN);
		}
		return mCurrentTime;
	}
	
	private List<IKTListener> mKtListeners = new ArrayList<TimeManager.IKTListener>();
	public void addKTListener(IKTListener l){
		mKtListeners.add(l);
	}
	
	public void notifyKT(){
		if(mKtListeners != null && mKtListeners.size()>0){
			for(IKTListener l : mKtListeners){
				l.onKT();
			}
		}
	}
	
	public void notifyUpdateChannel(){
		if(mKtListeners != null && mKtListeners.size()>0){
			for(IKTListener l : mKtListeners){
				l.onUpdateChannel();
			}
		}
	}
	
	public interface IKTListener{
		void onKT();
		void onUpdateChannel(); //定时更新频道列表
	}
	
}
