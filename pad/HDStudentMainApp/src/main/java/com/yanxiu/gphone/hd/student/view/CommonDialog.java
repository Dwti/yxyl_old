package com.yanxiu.gphone.hd.student.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

/**
 * Created by Administrator on 2015/6/3.
 */
public class CommonDialog extends DelDialog {

    @SuppressLint("ResourceAsColor")
    public CommonDialog(Context context, String title, String sure, String cel, DelCallBack callBack) {
        super(context, title, sure, cel, callBack);
        mContext = context;
//        setTopTextViewColor(mContext.getResources().getColor(R.color.color_ff323232));
//        setMiddleTextViewColor(mContext.getResources().getColor(R.color.color_ff40c0fd));
//        setBottomTextViewColor(mContext.getResources().getColor(R.color.color_ff40c0fd));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window win = getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        title_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delCallBack != null) {
                    delCallBack.sure();
                }
            }
        });
        cel_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (delCallBack != null) {
                    delCallBack.cancel();
                }
            }
        });
        del_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (delCallBack != null) {
                    delCallBack.del();
                }
            }
        });
    }
}
