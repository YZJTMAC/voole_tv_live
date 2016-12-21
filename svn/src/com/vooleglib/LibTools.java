package com.vooleglib;


public class LibTools {
	static{
		System.loadLibrary("voolelibtools10");
	}
	
    /* get box mac address
     */

    public static native String getBoxMac();
}
