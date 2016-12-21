package com.gntv.tv.common.xmpp;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.packet.Message;

import com.gntv.tv.common.utils.LogUtil;

public class XmppManager {

	private List<XmppListener> listeners = new ArrayList<XmppListener>();

	private static XmppManager instance = new XmppManager();

	private XmppManager() {

	}

	public static XmppManager getInstance() {
		return instance;
	}

	public XmppUser getXmppUser(String uid) {
		String url = new StringBuilder("http://mobileauth.voole.com/people/user/getUserByUid.htm?token=FREECHECK&uid=")
				.append(uid).toString();
		LogUtil.d("getXmppUser--->" + url);
		XmppUserParser parser = new XmppUserParser();
		try {
			parser.setUrl(url);
			return parser.getUser();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void addListenr(XmppListener listener) {
		listeners.add(listener);
	}

	public void sendMessage(Message msg) {
		int len = listeners.size();
		for (int i = 0; i < len; i++) {
			listeners.get(i).recevie(msg);
		}
	}

	public void removeListener() {
		int len = listeners.size();
		for (int i = 0; i < len; i++) {
			listeners.remove(i);
		}
	}

	public void removeListener(XmppListener listener) {
		int len = listeners.size();
		if (len > 0) {
			listeners.remove(listener);
		}
	}

	public interface XmppListener {
		void recevie(Message msg);
	}

}
