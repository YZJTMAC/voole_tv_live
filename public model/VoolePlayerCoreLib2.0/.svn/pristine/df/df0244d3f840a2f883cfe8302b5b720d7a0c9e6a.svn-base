package com.voole.player.lib.core.standard;

import java.util.Map;

import com.gntv.tv.common.ap.AuthManager;
import com.gntv.tv.common.ap.ProxyManager;

public class StandardLivePlayer extends StandardPlayer{
	
	@Override
	public void prepare(String param, Map<String, String> extMap) {
		reset();
		super.prepare(param, extMap);
	}

	@Override
	protected String getPlayUrl(String url) {
//		String url = ad.getPlayUrl();
		if(!url.startsWith("http:")){
			url = "http://127.0.0.1:" + ProxyManager.GetInstance().getProxyPort() + "/play?url='" + url + "'&authport=" + AuthManager.GetInstance().getAuthPort();
		}
		return url;
	}

	@Override
	protected boolean isLive() {
		return true;
	}

}
