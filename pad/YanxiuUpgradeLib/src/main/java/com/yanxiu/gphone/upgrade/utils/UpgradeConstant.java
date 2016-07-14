package com.yanxiu.gphone.upgrade.utils;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.ContextProvider;

/**
 * Created by hai8108 on 16/3/28.
 */
public class UpgradeConstant {
//    public final static String ROOT_DIR = "/YanxiuStudent/";
//
//    //分享的ICON的父目录
//    public final static String SHARE_ICON_PATH =
//            Environment.getExternalStorageDirectory() + ROOT_DIR+ "image/";
//    //分享ICON的文件名
//    public final static String SHARE_LOGO_NAME = "share_logo.png";
//    /**
//     * 整个应用程序运行过程中用到的数据
//     */
//    public static final String SDCARD_ROOT_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ROOT_DIR;//路径

    private static boolean isForTest = false;
    private static String DEVICEID;

    private static String appName;

    public static final String BRAND = CommonCoreUtil.getBrandName();

    public static final String OS = "android";

    public static String OS_TYPE = "0";

    public static final String OPERTYPE = "app.upload.log";

    public static final int PRODUCTLINE = 1;

    public static final String VERSION = CommonCoreUtil.getClientVersionName(
            ContextProvider.getApplicationContext());

    public static boolean isForTest () {
        return isForTest;
    }

    public static void setIsForTest (boolean isForTest) {
        UpgradeConstant.isForTest = isForTest;
    }

    public static String getDEVICEID () {
        return DEVICEID;
    }

    public static void setDEVICEID (String DEVICEID) {
        UpgradeConstant.DEVICEID = DEVICEID;
    }
    public static void setOsType(String osType){
        UpgradeConstant.OS_TYPE = osType;
    }

    public static String getAppName () {
        return appName;
    }

    public static void setAppName (String appName) {
        UpgradeConstant.appName = appName;
    }
}
