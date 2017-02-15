package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.core.utils.NetWorkTypeUtils;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.utils.ToastMaster;
import com.yanxiu.gphone.student.utils.Util;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by sp on 17-2-8.
 */

public class AudioCommentPlayer extends FrameLayout {
    View rl_voice;
    ImageView iv_voice;
    TextView tv_duration;
    MediaPlayer mediaPlayer;
    public boolean isPlaying;
    String voiceUrl;
    int indexToPlay = 0;

    OnPalyCompleteListener onPalyCompleteListener;

    public AudioCommentPlayer(Context context) {
        super(context);
        initView(context);
    }

    public AudioCommentPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AudioCommentPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.simple_voice_player, this);
        rl_voice = view.findViewById(R.id.rl_voice);
        iv_voice = (ImageView) view.findViewById(R.id.iv_voice);
        tv_duration = (TextView) view.findViewById(R.id.tv_duration);
        iv_voice.setImageResource(R.drawable.voice3);
    }

    Handler mHandler = new Handler();

    Runnable task = new Runnable() {
        @Override
        public void run() {
            switch (indexToPlay){
                case 0:
                    iv_voice.setImageResource(R.drawable.voice1);
                    indexToPlay = 1;
                    break;
                case 1:
                    iv_voice.setImageResource(R.drawable.voice2);
                    indexToPlay = 2;
                    break;
                case 2:
                    iv_voice.setImageResource(R.drawable.voice3);
                    indexToPlay = 0;
                    break;
                default:
                    break;
            }
            mHandler.postDelayed(task,500);
        }
    };


    private void startAnimation(){
        indexToPlay = 0;
        mHandler.post(task);
    }

    private void stopAnimation(){
        mHandler.removeCallbacks(task);
        iv_voice.setImageResource(R.drawable.voice3);
    }
    public void setDataSource(String url) {
        voiceUrl = url;
    }

    public void setDuration(int millionSeconds){
        if(millionSeconds >= 0)
            tv_duration.setText(millionSeconds + "''");
        else tv_duration.setText("0''");
    }

    public void start() {
        if (TextUtils.isEmpty(voiceUrl)) {
            ToastMaster.showShortToast(getContext(), "播放地址不合法！");
            return;
        }
        if (!NetWorkTypeUtils.isNetAvailable()) {
            Util.showToast("网络无法连接");
            return;
        } else {
            if (!NetWorkTypeUtils.isWifi() && !YanxiuApplication.hasShowed) {
                YanxiuApplication.hasShowed = true;
                TipsDialog tipsDialog = new TipsDialog(getContext());
                tipsDialog.show("当前非WiFi网络,继续试听将会消耗手机流量");
            } else {
                play();
            }
        }
    }

    private void play() {
        Uri uri = Uri.parse(voiceUrl);
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(voiceUrl);
            } catch (IOException e) {
                e.printStackTrace();
                ToastMaster.showShortToast(getContext(), "初始化出错！");
                return;
            }
            mediaPlayer.prepareAsync();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
                isPlaying = true;
                startAnimation();
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
                mediaPlayer.release();
                mediaPlayer = null;
                stopAnimation();
                if(onPalyCompleteListener != null)
                    onPalyCompleteListener.onComplete(AudioCommentPlayer.this);
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                ToastMaster.showShortToast(getContext(), "播放出错！");
                stopAnimation();
                isPlaying = false;
                return false;
            }
        });
    }

    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
            stopAnimation();
        }
    }

    public void stopAndRelease() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
            stopAnimation();
        }
    }

    public void setOnPalyCompleteListener(OnPalyCompleteListener onPalyCompleteListener) {
        this.onPalyCompleteListener = onPalyCompleteListener;
    }

    public interface OnPalyCompleteListener{
        void onComplete(AudioCommentPlayer audioCommentPlayer);
    }
}
