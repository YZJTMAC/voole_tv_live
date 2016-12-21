/*
 * Copyright (C) 2009-2010 Aubort Jean-Baptiste (Rorist)
 * Licensed under GNU's GPL 2, see README
 */

//am start -a android.intent.action.MAIN -n com.android.settings/.wifi.WifiSettings
package com.voole.statistics.terminalinfo.util;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.util.Log;

// TODO: IPv6 support

public class NetInfo {
	private final String TAG = "NetInfo";
//	private static final int BUF = 8 * 1024;
//	private static final String CMD_IP = " -f inet addr show %s";
//	private static final String PTN_IP1 = "\\s*inet [0-9\\.]+\\/([0-9]+) brd [0-9\\.]+ scope global %s$";
//	private static final String PTN_IP2 = "\\s*inet [0-9\\.]+ peer [0-9\\.]+\\/([0-9]+) scope global %s$"; // FIXME: Merge with PTN_IP1
//	private static final String PTN_IF = "^%s: ip [0-9\\.]+ mask ([0-9\\.]+) flags.*";
//	private static final String NOIF = "0";
	public static final String NOIP = "0.0.0.0";
	public static final String NOMASK = "255.255.255.255";
	public static final String NOMAC = "00:00:00:00:00:00";
	//	private Context ctxt;
	private WifiInfo info;
	//	private SharedPreferences prefs;

	public String intf = "eth0";
	public String ip = NOIP;
	public static int cidr = 24;

//	public int speed = 0;
//	public String ssid = null;
//	public String bssid = null;
//	public String carrier = null;
//	public String macAddress = NOMAC;
//	public String netmaskIp = NOMASK;
//	public static String gatewayIp = NOIP;

//	public NetInfo() {
		//		this.ctxt = ctxt;
		//		prefs = PreferenceManager.getDefaultSharedPreferences(ctxt);
		//        getIp();
		//        getWifiInfo();
//	}

	//	@Override
	//	public int hashCode() {
	//		int ip_custom = prefs.getBoolean("ip_custom", false) ? 1:0;
	//		int ip_start = prefs.getString("ip_start","0.0.0.0").hashCode();
	//		int ip_end = prefs.getString("ip_end", "0.0.0.0").hashCode();
	//		int cidr_custom = prefs.getBoolean("cidr_custom", false) ? 1:0;
	//		int cidr = prefs.getString("cidr", "24").hashCode();
	//		return 42 + intf.hashCode() + ip.hashCode() + cidr + ip_custom + ip_start + ip_end + cidr_custom + cidr;
	//	}

	public void getIp() {
		//		intf = prefs.getString("interface", null);
		try {
			// Automatic interface selection
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
					.hasMoreElements();) {
				NetworkInterface ni = en.nextElement();
				intf = ni.getName();
				ip = getInterfaceFirstIp(ni);
				if (ip != NOIP) {
					Log.e(TAG, "ip------>" + ip);
					break;
				}
			}
		} catch (SocketException e) {
			Log.e(TAG, e.getMessage());
		}
		//		getCidr();
	}

	private String getInterfaceFirstIp(NetworkInterface ni) {
		if (ni != null) {
			for (Enumeration<InetAddress> nis = ni.getInetAddresses(); nis.hasMoreElements();) {
				InetAddress ia = nis.nextElement();
				if (!ia.isLoopbackAddress()) {
					if (ia instanceof Inet6Address) {
						Log.i(TAG, "IPv6 detected and not supported yet!");
						continue;
					}
					return ia.getHostAddress();
				}
			}
		}
		return NOIP;
	}
	public SupplicantState getSupplicantState() {
		return info.getSupplicantState();
	}

	public static boolean isConnected(Context ctxt) {
		NetworkInfo nfo = ((ConnectivityManager) ctxt
				.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		if (nfo != null) {
			return nfo.isConnected();
		}
		return false;
	}

	public static long getUnsignedLongFromIp(String ip_addr) {
		String[] a = ip_addr.split("\\.");
		return (Integer.parseInt(a[0]) * 16777216 + Integer.parseInt(a[1]) * 65536
				+ Integer.parseInt(a[2]) * 256 + Integer.parseInt(a[3]));
	}

	public static String getIpFromIntSigned(int ip_int) {
		String ip = "";
		for (int k = 0; k < 4; k++) {
			ip = ip + ((ip_int >> k * 8) & 0xFF) + ".";
		}
		return ip.substring(0, ip.length() - 1);
	}

	public static String getIpFromLongUnsigned(long ip_long) {
		String ip = "";
		for (int k = 3; k > -1; k--) {
			ip = ip + ((ip_long >> k * 8) & 0xFF) + ".";
		}
		return ip.substring(0, ip.length() - 1);
	}
	//	private void getCidr() {
	//		String match;
	//		// Running ip tools
	//		try {
	//			if ((match = runCommand("/system/xbin/ip", String.format(CMD_IP, intf), String.format(PTN_IP1, intf))) != null) {
	//				cidr = Integer.parseInt(match);
	//				return;
	//			} else if ((match = runCommand("/system/xbin/ip", String.format(CMD_IP, intf), String.format(PTN_IP2, intf))) != null) {
	//				cidr = Integer.parseInt(match);
	//				return;
	//			} else if ((match = runCommand("/system/bin/ifconfig", " " + intf, String.format(PTN_IF, intf))) != null) {
	//				cidr = IpToCidr(match);
	//				return;
	//			} else {
	//				Log.i(TAG, "cannot find cidr, using default /24");
	//			}
	//		} catch (NumberFormatException e) {
	//			Log.i(TAG, e.getMessage()+ " -> cannot find cidr, using default /24");
	//		}
	//	}

	// FIXME: Factorize, this isn't a generic runCommand()
//	private String runCommand(String path, String cmd, String ptn) {
//		try {
//			if (new File(path).exists() == true) {
//				String line;
//				Matcher matcher;
//				Pattern ptrn = Pattern.compile(ptn);
//				Process p = Runtime.getRuntime().exec(path + cmd);
//				BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()), BUF);
//				while ((line = r.readLine()) != null) {
//					matcher = ptrn.matcher(line);
//					if (matcher.matches()) {
//						return matcher.group(1);
//					}
//				}
//			}
//		} catch (Exception e) {
//			Log.e(TAG, "Can't use native command: " + e.getMessage());
//			return null;
//		}
//		return null;
//	}

	//    public boolean getMobileInfo() {
	//        TelephonyManager tm = (TelephonyManager) ctxt.getSystemService(Context.TELEPHONY_SERVICE);
	//        if (tm != null) {
	//            carrier = tm.getNetworkOperatorName();
	//        }
	//        return false;
	//    }

	//    public boolean getWifiInfo() {
	//        WifiManager wifi = (WifiManager) ctxt.getSystemService(Context.WIFI_SERVICE);
	//        if (wifi != null) {
	//            info = wifi.getConnectionInfo();
	//            // Set wifi variables
	//            speed = info.getLinkSpeed();
	//            ssid = info.getSSID();
	//            bssid = info.getBSSID();
	//            macAddress = info.getMacAddress();
	//			gatewayIp = getIpFromIntSigned(wifi.getDhcpInfo().gateway);
	//            // broadcastIp = getIpFromIntSigned((dhcp.ipAddress & dhcp.netmask)
	//            // | ~dhcp.netmask);
	//            netmaskIp = getIpFromIntSigned(wifi.getDhcpInfo().netmask);
	//            return true;
	//        }
	//        return false;
	//    }

	//    public String getNetIp() {
	//        int shift = (32 - cidr);
	//        int start = ((int) getUnsignedLongFromIp(ip) >> shift << shift);
	//        return getIpFromLongUnsigned((long) start);
	//    }

//	private int IpToCidr(String ip) {
//		double sum = -2;
//		String[] part = ip.split("\\.");
//		for (String p : part) {
//			sum += 256D - Double.parseDouble(p);
//		}
//		return 32 - (int) (Math.log(sum) / Math.log(2d));
//	}

	// public int getIntFromInet(InetAddress ip_addr) {
	// return getIntFromIp(ip_addr.getHostAddress());
	// }

	// private InetAddress getInetFromInt(int ip_int) {
	// byte[] quads = new byte[4];
	// for (int k = 0; k < 4; k++)
	// quads[k] = (byte) ((ip_int >> k * 8) & 0xFF); // 0xFF=255
	// try {
	// return InetAddress.getByAddress(quads);
	// } catch (java.net.UnknownHostException e) {
	// return null;
	// }
	// }
}
