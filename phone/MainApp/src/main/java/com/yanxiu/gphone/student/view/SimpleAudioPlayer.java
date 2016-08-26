package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.common.core.utils.NetWorkTypeUtils;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.utils.Util;

/**
 * Created by sunpeng on 2016/7/28.
 */
public class SimpleAudioPlayer extends FrameLayout {
    private View view;
    private ProgressBar mProgressBar;
    private ImageView mIvControl;
    public boolean isPlaying = false;  //当前是否是播放状态
    private int totalLength=0;
    public static final int PLAY=0,RESUME=1,PAUSE=-1;
    private boolean flag=true;   //设置标记位，避免多次点击卡死界面

    private OnControlButtonClickListener onControlButtonClickListener;

    public SimpleAudioPlayer(Context context) {
        super(context);
        init(context);
    }

    public SimpleAudioPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SimpleAudioPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.simple_audio_player, this, true);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mIvControl = (ImageView) view.findViewById(R.id.iv_control);
        mIvControl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    flag=false;
                    checkNetWorkState();
                    if (isPlaying) {
                        mIvControl.setImageResource(R.drawable.play);
                    } else {
                        mIvControl.setImageResource(R.drawable.pause);
                    }

                    if (onControlButtonClickListener != null) {
                        onControlButtonClickListener.onClick(mIvControl);
                    }
                    isPlaying = !isPlaying;
                    flag=true;
                }
            }
        });
        Log.i("init",mProgressBar.getProgress()+"");
    }

    private void checkNetWorkState() {
        if(!NetWorkTypeUtils.isNetAvailable()){
            Util.showToast("网络无法连接");
            //这里return了 但是播放没有return 不能这样写
            return;
        }else{
            if(!NetWorkTypeUtils.isWifi() && !YanxiuApplication.hasShowed){
                YanxiuApplication.hasShowed=true;
                Util.showToast("当前网络非wifi状态");
                //弹框 如果确定 就继续播放，取消的话就返回
            }
        }
    }


    public void setMax(int max) {
        mProgressBar.setMax(max);
    }

    public void setProgress(int progress) {
        mProgressBar.setProgress(progress);
        if (totalLength == 0)
            totalLength = mProgressBar.getWidth()-mIvControl.getWidth();
//        float xRatio = Float.parseFloat(mProgressBar.getProgress()+"")/Float.parseFloat(mProgressBar.getMax()+"");
        float xRatio = (float) progress / mProgressBar.getMax();
        translateIvControl(totalLength * xRatio);

    }

    public void setPlayOver() {
        isPlaying = false;
        mIvControl.setImageResource(R.drawable.play);
        mIvControl.setTranslationX(0);
        mProgressBar.setProgress(0);
    }

    public void setState(int state){
        if(state==PLAY){
            mIvControl.setImageResource(R.drawable.pause);
            isPlaying=true;
        }else if(state == PAUSE){
            mIvControl.setImageResource(R.drawable.play);
            isPlaying=false;
        }
    }
    public int getProgress() {
        return mProgressBar.getProgress();
    }

    private void translateIvControl(float x) {
        mIvControl.setTranslationX(x);
    }

    public OnControlButtonClickListener getOnControlButtonClickListener() {
        return onControlButtonClickListener;
    }

    public void setOnControlButtonClickListener(OnControlButtonClickListener onControlButtonClickListener) {
        this.onControlButtonClickListener = onControlButtonClickListener;
    }

    public interface OnControlButtonClickListener {
        void onClick(ImageView imageButton);
    }
}
