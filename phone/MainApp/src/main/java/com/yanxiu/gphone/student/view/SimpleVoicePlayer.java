package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.core.utils.NetWorkTypeUtils;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.utils.ToastMaster;
import com.yanxiu.gphone.student.utils.Util;

import java.io.IOException;

/**
 * Created by sp on 17-2-8.
 */

public class SimpleVoicePlayer extends FrameLayout {
    View view_voice_icon, rl_voice;
    ImageView iv_voice;
    TextView tv_duration;
    MediaPlayer mediaPlayer;
    AnimationDrawable drawable;
    public boolean isPlaying;
    String voiceUrl;

    OnPalyCompleteListener onPalyCompleteListener;

    public SimpleVoicePlayer(Context context) {
        super(context);
        initView(context);
    }

    public SimpleVoicePlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SimpleVoicePlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.simple_voice_player, this);
        rl_voice = view.findViewById(R.id.rl_voice);
        iv_voice = (ImageView) view.findViewById(R.id.iv_voice);
        tv_duration = (TextView) view.findViewById(R.id.tv_duration);
        view_voice_icon = view.findViewById(R.id.view_voice_icon);
        view_voice_icon.setBackgroundResource(R.drawable.voice_play_animation);
        drawable = (AnimationDrawable) view_voice_icon.getBackground();

//        rl_voice.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isPlaying) {
//                    stopAndRelease();
//                } else {
//                    start();
//                }
//            }
//        });

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
//            mediaPlayer = MediaPlayer.create(getContext(), uri);
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
                drawable.start();
                view_voice_icon.setVisibility(VISIBLE);
                iv_voice.setVisibility(GONE);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (drawable.isRunning())
                    drawable.stop();
                isPlaying = false;
                mediaPlayer.release();
                mediaPlayer = null;
                view_voice_icon.setVisibility(GONE);
                iv_voice.setVisibility(VISIBLE);
                if(onPalyCompleteListener != null)
                    onPalyCompleteListener.onComplete(SimpleVoicePlayer.this);
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                ToastMaster.showShortToast(getContext(), "播放出错！");
                if (drawable.isRunning())
                    drawable.stop();
                isPlaying = false;
                return false;
            }
        });
    }

    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
            drawable.stop();
            view_voice_icon.setVisibility(GONE);
            iv_voice.setVisibility(VISIBLE);
        }
    }

    public void stopAndRelease() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
            drawable.stop();
            view_voice_icon.setVisibility(GONE);
            iv_voice.setVisibility(VISIBLE);
        }
    }

    public void setOnPalyCompleteListener(OnPalyCompleteListener onPalyCompleteListener) {
        this.onPalyCompleteListener = onPalyCompleteListener;
    }

    public interface OnPalyCompleteListener{
        void onComplete(SimpleVoicePlayer simpleVoicePlayer);
    }
}
