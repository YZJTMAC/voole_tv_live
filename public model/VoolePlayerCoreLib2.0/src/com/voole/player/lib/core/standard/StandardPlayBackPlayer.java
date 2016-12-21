package com.voole.player.lib.core.standard;

import com.gntv.tv.common.ap.AuthManager;
import com.gntv.tv.common.ap.ProxyManager;

public class StandardPlayBackPlayer extends StandardPlayer{

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
		return false;
	}
}
