package com.yanxiu.basecore.utils;

import android.util.Log;

public class BaseCoreLogInfo {
    public static boolean isDebug = false;
    private static String TAG = "LogInfo";
    private final static int DEBUG_LEVEL = 4;

    public static void setIsDebug(boolean isDebug_) {
        isDebug = isDebug_;
    }

    public static void log(String msg) {
        if (isDebug) {
            switch (DEBUG_LEVEL) {
            case 0:
                Log.i(TAG, msg);
                break;
            case 1:
                Log.d(TAG, msg);
                break;
            case 2:
                Log.v(TAG, msg);
                break;
            case 3:
                Log.w(TAG, msg);
                break;
            case 4:
                Log.e(TAG, msg);
                break;
            default:
                Log.i(TAG, msg);
                break;
            }
        }
    }

    public static void log(String Tag, String msg) {
        if (isDebug) {
            switch (DEBUG_LEVEL) {
            case 0:
                Log.i(Tag, msg);
                break;
            case 1:
                Log.d(Tag, msg);
                break;
            case 2:
                Log.v(Tag, msg);
                break;
            case 3:
                Log.w(Tag, msg);
                break;
            case 4:
                Log.e(Tag, msg);
                break;
            default:
                Log.i(Tag, msg);
                break;
            }
        }
    }

    public static void err(String logmsg) {
        if (isDebug) {
            Log.e("YanxiuHttp", logmsg);
        }
    }
}
