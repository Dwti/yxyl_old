package com.yanxiu.gphone.studentold.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by sunpeng on 2016/7/5.
 * 此类定义：解决Toast重复显示问题。关于是否需要加同步锁的处理，待定。
 */
public class ToastMaster {
    private static Toast toast;

    public static void showShortToast(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public static void showShortToast(Context context, int id) {
        if (toast == null) {
            toast = Toast.makeText(context, context.getString(id), Toast.LENGTH_SHORT);
        } else {
            toast.setText(context.getString(id));
        }
        toast.show();
    }
    public static void showLongToast(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        } else {
            toast.setText(text);
        }
        toast.show();
    }
    public static void showLongToast(Context context, int id) {
        if (toast == null) {
            toast = Toast.makeText(context, context.getString(id), Toast.LENGTH_LONG);
        } else {
            toast.setText(context.getString(id));
        }
        toast.show();
    }

    public static void showToast(Context context, String text, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, text, duration);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public static void showToast(Context context, int id, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, context.getString(id), duration);
        } else {
            toast.setText(context.getString(id));
        }
        toast.show();
    }
}
