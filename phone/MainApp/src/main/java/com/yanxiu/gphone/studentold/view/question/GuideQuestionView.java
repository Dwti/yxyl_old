package com.yanxiu.gphone.studentold.view.question;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.YanxiuApplication;

/**
 * Created by Administrator on 2015/7/12.
 */
public class GuideQuestionView extends FrameLayout {

    private Context mContext;
    private RelativeLayout btnGuide;
    private ImageView iv_guide_first_gesture;
    private int visibility=GONE;


    public GuideQuestionView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public GuideQuestionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public GuideQuestionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    @Override
    public void setVisibility(int visibility) {
        this.visibility=visibility;
        super.setVisibility(visibility);
        if (visibility==VISIBLE){
            setGif();
        }
    }

    private void setGif(){
        Glide.with(YanxiuApplication.getInstance())
                .load(R.drawable.first_use_question)
//                    .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<Integer, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Integer model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Integer model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (visibility==VISIBLE) {
                            // 计算动画时长
                            GifDrawable drawable = (GifDrawable) resource;
                            GifDecoder decoder = drawable.getDecoder();
                            int duration = 0;
                            for (int i = 0; i < drawable.getFrameCount(); i++) {
                                duration += decoder.getDelay(i);
                            }
                            //发送延时消息，通知动画结束
                            handler.sendEmptyMessageDelayed(10,duration);
                        }
                        return false;
                    }
                })
//                    .into(iv_guide_first_gesture);
                .into(new GlideDrawableImageViewTarget(iv_guide_first_gesture, 1));
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==10){
                setGif();
            }
        }
    };

    private void initView(){
        this.setOnClickListener(null);
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_guide_question, this);
         iv_guide_first_gesture = (ImageView)view.findViewById(R.id.iv_guide_first_gesture);

        btnGuide = (RelativeLayout) this.findViewById(R.id.rl_first_gesture);
        btnGuide.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GuideQuestionView.this.setVisibility(View.GONE);
            }
        });
    }
}
