package com.gntv.tv.common.xmpp;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;

import com.gntv.tv.common.ap.AuthManager;
import com.gntv.tv.common.utils.LogUtil;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class XmppService extends Service implements PacketListener {
	private MyBinder myBinder = new MyBinder();
	private int totalNum = 0;

	@Override
	public IBinder onBind(Intent intent) {
		return myBinder;
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	@Override
	public void onCreate() {
		LogUtil.d("XmppService-->onCreate");
		super.onCreate();
	}

	private String getLoginResource(String hid, String appid, String oemid, String apptype) {
		StringBuffer sb = new StringBuffer();
		sb.append("{\"hid\":");
		sb.append("\"");
		sb.append(hid);
		sb.append("\",");
		sb.append("\"appid\":");
		sb.append("\"");
		sb.append(appid);
		sb.append("\",");
		sb.append("\"oemid\":");
		sb.append("\"");
		sb.append(oemid);
		sb.append("\",");
		sb.append("\"apptype\":");
		sb.append("\"");
		sb.append(apptype);
		sb.append("\"}");
		return sb.toString();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		final String uid = intent.getStringExtra("uid");
		new Thread() {
			public void run() {
				XmppUser user = XmppManager.getInstance().getXmppUser(uid);
				if (user != null && "0".equals(user.status)) {
					String res = getLoginResource(AuthManager.GetInstance().getUser().getHid(), "-1",
							AuthManager.GetInstance().getUser().getOemid(), "2");
					LogUtil.d("XmppService-->login resorece-->" + res);
					login(AuthManager.GetInstance().getUser().getUid(), user.pwd, res);
				} else {
					LogUtil.d("XmppService-->login get pwd error");
				}
			};
		}.start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		LogUtil.d("XmppService-->onDestroy");
		if (smackAndroid != null) {
			smackAndroid.onDestroy();
		}
		new Thread() {
			public void run() {
				logout();
			};
		}.start();
		super.onDestroy();
	}

	/*
	 * private void startXmpp(){
	 * XmppManager.GetInstance().getConnection().addPacketListener(this, new
	 * PacketTypeFilter(org.jivesoftware.smack.packet.Message.class));
	 * 
	 * XmppManager.GetInstance().getConnection().addPacketListener(new
	 * PacketListener() {
	 * 
	 * @Override public void processPacket(Packet p) {
	 * LogUtil.d("processPacket-->PingIQ-->" + p.toString()); PingIQ pingIQ =
	 * (PingIQ) p; IQ pongIQ = pingIQ.createResultIQ(pingIQ); // 返回 //
	 * XmppManager.GetInstance().getConnection().sendPacket(pongIQ); } }, new
	 * PacketTypeFilter(PingIQ.class)); LogUtil.d("startXmpp"); }
	 */

	public class MyBinder extends Binder {
		public XmppService getService() {
			return XmppService.this;
		}
	}

	private XMPPConnection connection = null;
	private boolean isLogin = false;
	private SmackAndroid smackAndroid = null;

	private boolean login(String name, String pwd, String resource) {
		try {
			totalNum = 0;
			smackAndroid = SmackAndroid.init(this);
			if (connection == null) {
				ConnectionConfiguration config = new ConnectionConfiguration("xmpp.voole.com", 5222, "voole.com");
				// ConnectionConfiguration config = new
				// ConnectionConfiguration("211.90.15.214", 5222, "voole.com");
				// ConnectionConfiguration config = new
				// ConnectionConfiguration("61.54.1.156", 5222, "voole.com");
				// ConnectionConfiguration config = new
				// ConnectionConfiguration("172.16.10.195", 5222, "voole.com");
				// ConnectionConfiguration config = new
				// ConnectionConfiguration("10.9.8.242", 5222, "voole.com");
				config.setSASLAuthenticationEnabled(true);
				config.setReconnectionAllowed(true);
				config.setSendPresence(true);
				config.setDebuggerEnabled(true);
				config.setSecurityMode(SecurityMode.disabled);
				config.setCompressionEnabled(false);
				LogUtil.d("XmppManager-->login connection==null new connection-->");
				connection = new XMPPConnection(config);
				connection.connect();
				connection.addConnectionListener(new ConnectionListener() {
					@Override
					public void reconnectionSuccessful() {
						isLogin = true;
						LogUtil.d("XmppManager-->ConnectionListener-->reconnectionSuccessful");
					}

					@Override
					public void reconnectionFailed(Exception arg0) {
						isLogin = false;
						LogUtil.d("XmppManager-->ConnectionListener-->reconnectionFailed");
					}

					@Override
					public void reconnectingIn(int arg0) {
						isLogin = false;
						LogUtil.d("XmppManager-->ConnectionListener-->reconnectingIn");
					}

					@Override
					public void connectionClosedOnError(Exception arg0) {
						isLogin = false;
						LogUtil.d("XmppManager-->ConnectionListener-->connectionClosedOnError");
					}

					@Override
					public void connectionClosed() {
						isLogin = false;
						LogUtil.d("XmppManager-->ConnectionListener-->connectionClosed");
					}
				});
				connection.login(name, pwd, resource);
				// connection.login("c10002", "123456");
				connection.addPacketListener(this, new PacketTypeFilter(org.jivesoftware.smack.packet.Message.class));
				isLogin = true;
				return true;
			} else {
				if (!connection.isConnected()) {
					LogUtil.d("XmppManager-->login connection!=null -->not-->Connected");
					connection.connect();
				}
				if (!connection.isAuthenticated()) {
					LogUtil.d("XmppManager-->login connection!=null -->not-->Authenticated");
					connection.login(name, pwd);
					isLogin = true;
					return true;
				}
			}
		} catch (XMPPException e) {
			if (connection != null) {
				connection.disconnect();
			}
			LogUtil.d("XmppManager-->login Exception-->" + e.toString());
			isLogin = false;
		}
		return isLogin;
	}

	private boolean logout() {
		if (connection != null) {
			LogUtil.d("XmppManager-->logout");
			connection.disconnect();
		}
		isLogin = false;
		return true;
	}

	// @Override
	// public void processPacket(Packet arg0) {
	// org.jivesoftware.smack.packet.Message msg =
	// (org.jivesoftware.smack.packet.Message) arg0;
	// /*
	// * final String info = msg.getBody(); LogUtil.d("processPacket-->" +
	// * info); LogUtil.d("processPacket-->" + msg.toXML());
	// * LogUtil.d("processPacket-->" + msg.getExtension("content"));
	// */
	// if (totalNum == 0) {
	// Log.d("XMPP", "XMPP totalNum------------------------->" + totalNum +
	// ",time---->" + DateUtil.getDateTime());
	// } else {
	// LogUtil.d("XMPP totalNum------------------------->" + totalNum +
	// ",time---->" + DateUtil.getDateTime());
	// }
	// // LogUtil.d("currentTime------------------------->" +
	// // DateUtil.getDateTime());
	// // LogUtil.d("processPacket-->getUids-->" + msg.getUids());
	// // LogUtil.d("processPacket-->getTerminaltype-->" +
	// // msg.getTerminaltype());
	// // LogUtil.d("processPacket-->getApplication-->" +
	// // msg.getApplication());
	// // LogUtil.d("processPacket-->getFunction-->" + msg.getFunction());
	// // LogUtil.d("processPacket-->getOemid-->" + msg.getOemid());
	// // LogUtil.d("processPacket-->getAppid-->" + msg.getAppid());
	// if (msg.getParams() != null) {
	// Iterator<Map.Entry<String, String>> it =
	// msg.getParams().entrySet().iterator();
	// while (it.hasNext()) {
	// Map.Entry<String, String> entry = it.next();
	// // LogUtil.d("processPacket-->key= " + entry.getKey() + " and
	// // value= " + entry.getValue());
	// }
	// }
	// totalNum = totalNum + 1;
	// }

	@Override
	public void processPacket(Packet arg0) {
		org.jivesoftware.smack.packet.Message msg = (org.jivesoftware.smack.packet.Message) arg0;
		LogUtil.d("XmppManager-->processPacket-->getFunction--->");
		LogUtil.d("XmppManager-->processPacket-->getFunction--->" + msg.getFunction());

		// LogUtil.d("currentTime------------------------->" +
		// DateUtil.getDateTime());
		// LogUtil.d("processPacket-->getUids-->" + msg.getUids());
		// LogUtil.d("processPacket-->getTerminaltype-->" +
		// msg.getTerminaltype());
		// LogUtil.d("processPacket-->getApplication-->" +
		// msg.getApplication());
		// LogUtil.d("processPacket-->getFunction-->" + msg.getFunction());
		// LogUtil.d("processPacket-->getOemid-->" + msg.getOemid());
		// LogUtil.d("processPacket-->getAppid-->" + msg.getAppid());
		// if (msg.getParams() != null) {
		// Iterator<Map.Entry<String, String>> it =
		// msg.getParams().entrySet().iterator();
		// while (it.hasNext()) {
		// Map.Entry<String, String> entry = it.next();
		// // LogUtil.d("processPacket-->key= " + entry.getKey() + " and
		// // value= " + entry.getValue());
		// }
		// }
		XmppManager.getInstance().sendMessage(msg);
	}
}
