package com.yanxiu.gphone.student.view.question;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.yanxiu.gphone.student.R;

/**
 * Created by Administrator on 2015/7/12.
 */
public class GuideClassfyQuestionView extends FrameLayout {

    private Context mContext;
    private Button btnGuide;


    public GuideClassfyQuestionView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public GuideClassfyQuestionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public GuideClassfyQuestionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }


    private void initView(){
        this.setOnClickListener(null);
        LayoutInflater.from(mContext).inflate(R.layout.layout_guide_question, this);
        btnGuide = (Button) this.findViewById(R.id.btn_guide_btn);
        btnGuide.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GuideClassfyQuestionView.this.setVisibility(View.GONE);
            }
        });
    }
}
