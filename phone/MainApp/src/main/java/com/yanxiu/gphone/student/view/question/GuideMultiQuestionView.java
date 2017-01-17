package com.yanxiu.gphone.student.view.question;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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
public class GuideMultiQuestionView extends FrameLayout {

    private Context mContext;
    private RelativeLayout btnGuide;
    private ImageView iv_guide_multi_gesture;


    public GuideMultiQuestionView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public GuideMultiQuestionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public GuideMultiQuestionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility==VISIBLE){
            Glide.with(YanxiuApplication.getInstance())
                    .load(R.drawable.first_multi_question)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(iv_guide_multi_gesture);
        }else {
            iv_guide_multi_gesture.setImageResource(0);
        }
    }

    private void initView(){
        this.setOnClickListener(null);
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_guide_multi_question, this);
         iv_guide_multi_gesture = (ImageView)view.findViewById(R.id.iv_guide_multi_gesture);
        btnGuide = (RelativeLayout) this.findViewById(R.id.rl_multi_gesture);
        btnGuide.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GuideMultiQuestionView.this.setVisibility(View.GONE);
            }
        });
    }
}
