package com.vad.sdk.core.base;

public class AdEvent {
	public enum AdType{
		LOADING,
		TVC,
		PAUSE,
		FLOATING,
		EXIT
	}
	
	public enum AdStatus{
		AD_START,
		AD_END,
	}
	
	private AdType mAdType = null;
	private AdStatus mAdStatus = null;
	
	public AdEvent(AdType adType, AdStatus adStatus){
		mAdType = adType;
		mAdStatus = adStatus;
	}
	
	public AdType getAdType(){
		return mAdType;
	}
	
	public AdStatus getAdStatus(){
		return mAdStatus;
	}
}
