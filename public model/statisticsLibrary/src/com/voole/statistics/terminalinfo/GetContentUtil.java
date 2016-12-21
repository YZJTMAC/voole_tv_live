package com.voole.statistics.terminalinfo;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.voole.statistics.util.StringPrint;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

public class GetContentUtil {
	private static final String TAG = "GetContentUtil";

	public static int getNumberOfCores() {
		if(Build.VERSION.SDK_INT >= 17) {
			return Runtime.getRuntime().availableProcessors();
		}
		else {
			// Use saurabh64's answer
			return getNumCoresOldPhones();
		}
	}
	
	@SuppressLint("NewApi")
	public static String getMac() {
		byte[] mac = null;
		StringBuffer sb = new StringBuffer();
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> address = ni.getInetAddresses();

				while (address.hasMoreElements()) {
					InetAddress ip = address.nextElement();
					if (ip.isAnyLocalAddress() || !(ip instanceof Inet4Address) || ip.isLoopbackAddress())
						continue;
					if (ip.isSiteLocalAddress())
						mac = ni.getHardwareAddress();
					else if (!ip.isLinkLocalAddress()) {
						mac = ni.getHardwareAddress();
						break;
					}
				}
			}
			for (int i = 0; i < mac.length; i++) {
				String sTemp = Integer.toHexString(0xFF & mac[i]);
				if (sTemp.length() == 1) {
					sb.append("0");
				}
				sb.append(sTemp);
			}
			String rb = sb.toString();
			if (null != rb && !"".equals(rb.trim())) {
				return rb;
			} else {
				return "-1";
			}
		} catch (SocketException e) {
			e.printStackTrace();
			return "-1";
		}
	}
	// 获取版本号
	public static String getVersion(Context context){
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			StringPrint.print(pi.versionCode + "");
			return pi.versionCode + "";
		} catch (NameNotFoundException e) {
			return "";
		}
	}
	
	// 获取包名
	public static String getPackageName(Context context){
		try {
			String baoming = context.getPackageName();
			return baoming;
		} catch (Exception e) {
			return "";
		}
	}

	public static String getSystem() {
		try {
			String version = android.os.Build.VERSION.RELEASE;
			if (null == version || "".equals(version) || "null".equals(version)) {
				return "";
			}
			return version;
		} catch (Exception e) {
			return "";
		}
	}
	public static String getMemorySize() {
		final Pattern PATTERN = Pattern.compile("([a-zA-Z]+):\\s*(\\d+)");

		String memorySize = null;
		String line;
		try {
			RandomAccessFile reader = new RandomAccessFile("/proc/meminfo", "r");
			while ((line = reader.readLine()) != null) {
				Matcher m = PATTERN.matcher(line);
				if (m.find()) {
					String name = m.group(1);
					String size = m.group(2);

					if (name.equalsIgnoreCase("MemTotal")) {
						memorySize = size;
					} 
				}
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return memorySize;
	}

	/**
	 * Gets the number of cores available in this device, across all processors.
	 * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
	 * @return The number of cores, or 1 if failed to get result
	 */
	private static int getNumCoresOldPhones() {
		class CpuFilter implements FileFilter {
			@Override
			public boolean accept(File pathname) {
				if(Pattern.matches("cpu[0-9]+", pathname.getName())) {
					return true;
				}
				return false;
			}      
		}
		try {
			//Get directory containing CPU info
			File dir = new File("/sys/devices/system/cpu/");
			//Filter to only list the devices we care about
			File[] files = dir.listFiles(new CpuFilter());
			//Return the number of cores (virtual CPU devices)
			return files.length;
		} catch(Exception e) {
			//Default to return 1 core
			return 1;
		}
	}

	public static String runCommand(String command) {
		String result = null;
		String str = "";
		try{
			Process pp = Runtime.getRuntime().exec(command);
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			for (; null != str;){
				str = input.readLine();
				if (str != null){
					result = str.trim();// 去空格
					break;
				}
			}
		} catch (IOException ex) {
			// 赋予默认值
			ex.printStackTrace();
		}
		return result;
	}

	public static String getSystemProperties(String property, String defaultvalue) {
		String value;
		try {
			Class<?> clazz = Class.forName("android.os.SystemProperties");
			Method method = clazz.getMethod("get", new Class[] { java.lang.String.class });
			value = (String) method.invoke(null, new Object[] { property });
			if (value != null && value.length() > 0) {
				return value;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return defaultvalue;
	}

	public static String getOpengl(Context context) {
		ActivityManager activityManager = 
				(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo configurationInfo = 
				activityManager.getDeviceConfigurationInfo();
//		if (configurationInfo.reqGlEsVersion >= 0x30000) {
//			return "3.0";
//		} else if (configurationInfo.reqGlEsVersion >= 0x20000) {
//			return "2.0";
//		} else if (configurationInfo.reqGlEsVersion >= 0x10000) {
//			return "1.0";
//		} else {
//			return "高版本";
//		}
		return configurationInfo.getGlEsVersion();
	}
	
	/**
	 * 获得手机型号
	 * 
	 * @return
	 */
	public static String getModel() {
		try {
			String str = android.os.Build.MODEL + "";
			str = str.replaceAll(" ", "");
			str.trim();
			return str;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String getChip() {
		try {
			String str = android.os.Build.HARDWARE + "";
			str = str.replaceAll(" ", "");
			str.trim();
			return str;
		} catch (Exception e) {
			return null;
		}
	}

	public static String getMac(String ip) {
		String thisMac = null;
		String line ;
		String[] s = new String[6] ;
		try {
			RandomAccessFile reader = new RandomAccessFile("/proc/net/arp", "r");
			Matcher m = null;
			String ips = null;
			String mac = null;

			while ((line = reader.readLine()) != null) {
				s = line.split("\\s+");
				ips = s[0];
				mac = s[3];
				if (ip.equalsIgnoreCase(ips)) {
					thisMac = mac;
					break;
				} 
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return thisMac;
	}
	
	public static String isLinker64(){
		try{
			File f=new File("/system/bin/linker64");
			if(!f.exists()){
				return "32";
			}
		}catch (Exception e) {
			return "32";
		}
		return "64";
	}

}
