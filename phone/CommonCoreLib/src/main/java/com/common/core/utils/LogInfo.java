package com.common.core.utils;

import android.text.TextUtils;
import android.util.Log;

import com.common.core.CoreConfiguration;

public class LogInfo {
    private static boolean isDebug = CoreConfiguration.isDebug();
//    private static boolean isDebug = true;
    private static String TAG = "yanxiuSrt";
    private final static int DEBUG_LEVEL = 4;

    public static void log(String msg) {
        if (isDebug) {
            if (!TextUtils.isEmpty(msg)) {
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
            } else {
                Log.e(TAG, "msg is null");
            }
        }
    }

    public static void log(String Tag, String msg) {
        if (true) {
            if (!TextUtils.isEmpty(msg)) {
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
            } else {
                Log.e(Tag, "msg is null");
            }
        }
    }

}
