package com.voole.android.client.UpAndAu.util;

import android.util.Log;


 /**
 * 日志信息工具类
 * @version 1.0
 * @author lvzhenwei
 * @date 2012-3-31 上午10:20:38 
 * @update 2012-3-31 上午10:20:38 
 */

public class Logger {
	final private static String TAG = "VooleClient";
	private static boolean debug_able = true;
	private static boolean info_able = true;
	private static boolean verbose = true;
	private static boolean error = true;

	/**
	 *  debug
	 * @param tag String
	 * @param msg String
	 */
	public static void debug(String tag,String msg){
        if (debug_able) {
            Log.d(tag, msg);
        }
    }
     
    /**
     * debug
     * @param msg String
     */
    public static void debug(String msg){
        if (debug_able) {
            Log.d(TAG, msg);
        }
    }

	/**
	 * info
	 * @param s String
	 */
	public static void info(String s) {
		if(info_able) {
			Log.i(TAG, s);
		}
		
	}
	
	/**
	 *  info
	 * @param tag String
	 * @param s String
	 */
	public static void info(String tag,String s) {
		if(info_able) {
			Log.i(tag, s);
		}
		
	}

	/**
	 *  verbose  
	 * @param String 
	 */
	/**
	 *  verbose
	 * @param s String
	 */
	public static void verbose(String s) {
		if(verbose) {
			Log.v(TAG, s);
		}
		
	}
	
	/**
	 * Log verbose 
	 * @param tag String
	 * @param s String
	 */
	public static void verbose(String tag,String s) {
		if(verbose) {
			Log.v(tag, s);
		}
		
	}
	
	/**
	 * Log error
	 * @param tag String
	 * @param s String
	 */
	public static void error(String tag,String s) {
		if(error)
			Log.e(tag, s);
	}
	
	/**
	 * Log error
	 * @param s String
	 */
	public static void error(String s) {
		if(error)
		Log.e(TAG, s);
	}

	/**
		Log error
	 * @param e Throwable
	 */
	public static void error(Throwable e) {
		if(error)
		Log.e(TAG, "", e);
	}

	/**
	 *Log error
	 * @param msg String
	 * @param e Throwable
	 */
	public static void error(String msg, Throwable e) {
		if(error)
		Log.e(TAG, msg, e);
	}
}
