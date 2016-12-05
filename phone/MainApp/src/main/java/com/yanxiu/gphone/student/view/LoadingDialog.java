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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.view.picsel.bean.ImageBucket;

/**
 * Created by JS-00 on 2016/11/8.
 */
public class LoadingDialog extends Dialog{

    private int mCurrent;
    private int mNum;
    private ImageView iv_progress_close;
    private TextView tv_progress_txt;
    private ProgressBar progressbar_progress;
    private OnCloseListener listener;

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

    public interface OnCloseListener{
        void onClose();
    }

    public void setOnCloseListener(OnCloseListener listener){
        this.listener=listener;
    }

    private void init(final Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.loading_dialog, null);
        setContentView(view);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.alpha = 0.7f; // 透明度
        dialogWindow.setAttributes(lp);

        iv_progress_close = (ImageView) view.findViewById(R.id.iv_progress_close);
        tv_progress_txt = (TextView)view.findViewById(R.id.tv_progress_txt);
        progressbar_progress = (ProgressBar) view.findViewById(R.id.progressbar_progress);

        iv_progress_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (listener!=null){
//                    listener.onClose();
//                }else {
                    ((Activity) context).finish();
//                }
            }
        });
    }

    public void updateUI() {
        tv_progress_txt.setText(getmCurrent()+" / "+getmNum());
        progressbar_progress.setProgress((getmCurrent()*100)/getmNum());
    }
}
