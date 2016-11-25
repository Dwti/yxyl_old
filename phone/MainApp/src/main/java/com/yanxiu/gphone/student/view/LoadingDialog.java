package com.yanxiu.gphone.student.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;

/**
 * Created by JS-00 on 2016/11/8.
 */
public class LoadingDialog extends Dialog{

    private int mCurrent;
    private int mNum;
    private TextView tv_progress_txt;
    private ProgressBar progressbar_progress;

    public int getmCurrent() {
        return mCurrent;
    }

    public void setmCurrent(int mCurrent) {
        this.mCurrent = mCurrent;
    }

    public int getmNum() {
        return mNum;
    }

    public void setmNum(int mNum) {
        this.mNum = mNum;
    }

    public LoadingDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.loading_dialog, null);
        setContentView(view);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.alpha = 0.7f; // 透明度
        dialogWindow.setAttributes(lp);

        tv_progress_txt = (TextView)view.findViewById(R.id.tv_progress_txt);
        progressbar_progress = (ProgressBar) view.findViewById(R.id.progressbar_progress);
    }

    public void updateUI() {
        tv_progress_txt.setText(getmCurrent()+" / "+getmNum());
        progressbar_progress.setProgress(getmCurrent()/getmNum()*100);
    }
}
