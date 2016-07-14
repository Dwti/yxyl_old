package com.common.core.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;

/**
 * 错误上报
 */
public class SubmitExceptionHandler {

	public static final String ERROR_QUIT = "4005" ;
	public static final String ERROR_ACT_UNDEFINED = "1003";
	private static final  String WIFI_TYPE="wifi";
	private static final String MOBILE_3G_TYPE="3G";
	private static final String MOBILE_4G_TYPE="4G";
	public static ArrayList<String> submitCrashExcrption(final Context context , final String err_log){

		ArrayList<String> list = new ArrayList<String>() ;

		list.add("");
		list.add("");
		list.add("");
		list.add(getNetType(context));
		list.add("");
		list.add("");
		list.add("");
		list.add(ERROR_ACT_UNDEFINED);
		list.add(ERROR_QUIT);
		list.add(err_log);
		list.add("android");
		list.add(getOSVersionName());
		list.add(getBrandName());
		list.add(getDeviceName());

		return list;
	}
	/**
	 * 得到设备名字
	 * */
	public static String getDeviceName() {
		String model = android.os.Build.MODEL;
		if (model == null || model.length() <= 0) {
			return "";
		} else {
			return model;
		}
	}
	/**
	 * 得到品牌名字
	 * */
	public static String getBrandName() {
		String brand = android.os.Build.BRAND;
		if (brand == null || brand.length() <= 0) {
			return "";
		} else {
			return getData(brand);
		}
	}
	public static String getData(String data) {
		if (data == null || data.length() <= 0) {
			return "-";
		} else {
			return data.replace(" ", "_");
		}
	}
	/**
	 * 得到操作系统版本号
	 */
	public static String getOSVersionName() {
		return android.os.Build.VERSION.RELEASE;
	}
	/**
	 * 返回联网类型
	 *
	 * @param context
	 * @return wifi或3G
	 */
	public static String getNetType(Context context) {
		String netType = null;
		ConnectivityManager connectivityMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityMgr != null) {
			NetworkInfo networkInfo = connectivityMgr.getActiveNetworkInfo();
			if (networkInfo != null) {
				if (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
					netType = WIFI_TYPE;
				} else if (ConnectivityManager.TYPE_MOBILE == networkInfo.getType()) {
					netType = MOBILE_3G_TYPE;
				}else if (ConnectivityManager.TYPE_MOBILE_DUN == networkInfo.getType()) {
					netType = MOBILE_4G_TYPE;
				}
				else {
					netType = WIFI_TYPE;
				}
			}
		}

		return netType;
	}
}
