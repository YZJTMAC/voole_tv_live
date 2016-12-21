package com.gntv.tv.model.channel;

import com.gntv.tv.common.utils.LocalManager;

public class ResourceManager {
	private static ResourceManager instance = new ResourceManager();

	private ResourceManager() {
	}

	public static ResourceManager GetInstance() {
		return instance;
	}
	
	public void saveResource(String channelId, String resourceId){
		LocalManager.GetInstance().saveLocal(channelId, resourceId);
	}
	
	public String getResource(String channelId){
		return LocalManager.GetInstance().getLocal(channelId, "");
	}
	
	/**
	 * 0 高清优先
	 * 1 标清优先
	 * @param isHD
	 */
	public void saveResourceState(String isHD){
		LocalManager.GetInstance().saveLocal("is_hd", isHD);
	}
	
	public String getResourceState(){
		return LocalManager.GetInstance().getLocal("is_hd", "0");
	}
	
	
	/**
	 * //0表示铺满全屏，1表示原始比例
	 * @param isMatchParent
	 */
	public void saveResourceScale(String isMatchParent){
		LocalManager.GetInstance().saveLocal("isMatchParent", isMatchParent);
	}
	
	public String getResourceScale(){
		return LocalManager.GetInstance().getLocal("isMatchParent", "0");
	}
}
