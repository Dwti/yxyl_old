package com.yanxiu.gphone.student.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.YanxiuApplication;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/15 10:34.
 * Function :
 */

public class LoadingDialogUtils{

    private static LoadingDialogUtils viewUtils;
    private Dialog dialog;

    private LoadingDialogUtils(Context context){
        dialog=new Dialog(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_loading, null);
        ImageView iv_loading= (ImageView) view.findViewById(R.id.iv_loading);
        dialog.setContentView(view);

        Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.xlistview_header_progress);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if (operatingAnim!=null) {
            iv_loading.setAnimation(operatingAnim);
        }

//        Window window = dialog.getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        window.setGravity(Gravity.CENTER);
//        lp.alpha = 0.7f;
//        window.setAttributes(lp);
    }

    public static LoadingDialogUtils getInstence(Context context){
        viewUtils=new LoadingDialogUtils(context);
        return viewUtils;
    }

    public void show(){
        if (dialog!=null){
            dialog.show();
        }
    }

    public void dismiss(){
        if (dialog!=null){
            dialog.dismiss();
        }
    }

}
