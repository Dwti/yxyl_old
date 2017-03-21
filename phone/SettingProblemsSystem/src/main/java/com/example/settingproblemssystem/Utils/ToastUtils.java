package com.example.settingproblemssystem.Utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/21 15:45.
 * Function :
 */

public class ToastUtils {
    public static void show(Context context,String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static void show(Context context,@StringRes int resId){
        Toast.makeText(context,resId,Toast.LENGTH_SHORT).show();
    }
}
