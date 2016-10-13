package com.yanxiu.gphone.student.view.question;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.yanxiu.gphone.student.R;

/**
 * Created by Administrator on 2015/7/12.
 */
public class GuideMultiQuestionView extends FrameLayout {

    private Context mContext;
    private RelativeLayout btnGuide;


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


    private void initView(){
        this.setOnClickListener(null);
        LayoutInflater.from(mContext).inflate(R.layout.layout_guide_multi_question, this);
        btnGuide = (RelativeLayout) this.findViewById(R.id.rl_multi_gesture);
        btnGuide.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GuideMultiQuestionView.this.setVisibility(View.GONE);
            }
        });
    }
}
