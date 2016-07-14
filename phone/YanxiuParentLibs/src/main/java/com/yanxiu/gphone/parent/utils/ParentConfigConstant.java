package com.yanxiu.gphone.parent.utils;

import android.util.DisplayMetrics;

import com.common.core.utils.CommonCoreUtil;

public class ParentConfigConstant {


	public final static String ROOT_DIR = "/YanxiuStudent/";
	/**
	 * 整个应用程序运行过程中用到的数据
	 */
	public static final String SDCARD_ROOT_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ROOT_DIR;//路径

	private static String VERSION;

	private static int VERSION_CODE;

	private static DisplayMetrics displayMetrics;
	private static String DEVICEID ;

	public static String getVERSION () {
		return VERSION;
	}

	public static int getVersionCode () {
		return VERSION_CODE;
	}

	public static DisplayMetrics getDisplayMetrics () {
		return displayMetrics;
	}

	public static String getDEVICEID () {
		return DEVICEID;
	}

	public static void setVERSION (String VERSION) {
		ParentConfigConstant.VERSION = VERSION;
	}

	public static void setVersionCode (int versionCode) {
		VERSION_CODE = versionCode;
	}

	public static void setDisplayMetrics (DisplayMetrics displayMetrics) {
		ParentConfigConstant.displayMetrics = displayMetrics;
	}

	public static void setDEVICEID (String DEVICEID) {
		ParentConfigConstant.DEVICEID = DEVICEID;
	}

	public static final String BRAND = CommonCoreUtil.getBrandName();

    public static final String OS = "android";

    public static final String OS_TYPE = "0";

	public static final String PCODE = CommonCoreUtil.getPcode();

	public static final String OPERTYPE = "app.upload.log";

	public static final int PRODUCTLINE = 1;

	public static final String OS_VERSION= CommonCoreUtil.getOSVersionCode();

	public static final int screenWidth = CommonCoreUtil.getScreenWidth();

	public static final int screenHeight = CommonCoreUtil.getScreenHeight();

	public static final int YX_PAGESIZE_CONSTANT = 10;

}
