package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.yanxiu.gphone.student.R;

/**
 * Created by sunpeng on 2016/7/28.
 */
public class SimpleAudioPlayer extends FrameLayout {
    private View view;
    private ProgressBar mProgressBar;
    private ImageView mIvControl;
    public boolean isPlaying = false;
    private int totalLength=0;

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
                if (isPlaying) {
                    mIvControl.setImageResource(R.drawable.play);
                } else {
                    mIvControl.setImageResource(R.drawable.pause);
                }

                if (onControlButtonClickListener != null) {
                    onControlButtonClickListener.onClick(mIvControl);
                }
                isPlaying = !isPlaying;
            }
        });
    }


    public void setMax(int max) {
        mProgressBar.setMax(max);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
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
        mProgressBar.setProgress(0);
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
