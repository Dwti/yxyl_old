package com.yanxiu.gphone.student.view.question;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.YanxiuApplication;

/**
 * Created by Administrator on 2015/7/12.
 */
public class GuideQuestionView extends FrameLayout {

    private Context mContext;
    private RelativeLayout btnGuide;


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


    private void initView(){
        this.setOnClickListener(null);
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_guide_question, this);
        ImageView iv_guide_first_gesture = (ImageView)view.findViewById(R.id.iv_guide_first_gesture);
        Glide.with(YanxiuApplication.getInstance())
                .load(R.drawable.first_use_question)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(iv_guide_first_gesture);
        btnGuide = (RelativeLayout) this.findViewById(R.id.rl_first_gesture);
        btnGuide.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GuideQuestionView.this.setVisibility(View.GONE);
            }
        });
    }
}
