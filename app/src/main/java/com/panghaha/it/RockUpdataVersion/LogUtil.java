package com.panghaha.it.RockUpdataVersion;

import android.util.Log;

public class LogUtil {
	private static boolean isDebug = true;
	
	public static void d(String tag,String msg){
		if(isDebug){
			Log.d(tag, msg);
		}
	}
	public static void e(String tag,String msg){
		if(isDebug){
			Log.e(tag, msg);
		}
	}
	
	public static void d(Object object,String msg){
		if(isDebug){
			Log.d(object.getClass().getSimpleName(), msg);
		}
	}
	public static void e(Object object,String msg){
		if(isDebug){
			Log.e(object.getClass().getSimpleName(), msg);
		}
	}
}
