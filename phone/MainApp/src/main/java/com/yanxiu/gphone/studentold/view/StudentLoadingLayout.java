package com.yanxiu.gphone.studentold.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.*;
import com.yanxiu.gphone.studentold.R;

/**
 * Created by Administrator on 2015/7/10.
 */
public class StudentLoadingLayout extends FrameLayout {

    private Context mContext;
    private RelativeLayout rlCommonLoading;

    private TextView tvContent;
    private ImageView pbLoading;
    private Animation operatingAnim;
    public enum LoadingType{
        //通用loading
        LAODING_COMMON,
        //答题提交
        LAODING_SUBMMIT,
        //智能出题中loading
        LAODING_INTELLI_EXE
    }

    public StudentLoadingLayout(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public StudentLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public StudentLoadingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView();
    }

    private void initView(){
        setViewGone();
        LayoutInflater.from(mContext).inflate(R.layout.layout_student_loading, this);
        rlCommonLoading = (RelativeLayout) this.findViewById(R.id.rl_common_loading);
        FrameLayout.LayoutParams params = (LayoutParams) rlCommonLoading.getLayoutParams();
        params.gravity = Gravity.CENTER;
        rlCommonLoading.setLayoutParams(params);

        tvContent = (TextView) findViewById(R.id.tv_loading_content);
        pbLoading = (ImageView) findViewById(R.id.pb_loaing);
        operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim
                .xlistview_header_progress);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);

        if (operatingAnim != null) {
            pbLoading.startAnimation(operatingAnim);
        }
    }

    public void setViewType(LoadingType type){
        switch (type) {
            case LAODING_COMMON:
                this.setVisibility(View.VISIBLE);
                setOperatingAnim();
                tvContent.setVisibility(View.GONE);
                break;
            case LAODING_INTELLI_EXE:
                this.setVisibility(View.VISIBLE);
                setOperatingAnim();
                tvContent.setVisibility(View.VISIBLE);
                tvContent.setText(mContext.getResources().getString(R.string.intelligent_questions));
                break;
            case LAODING_SUBMMIT:
                this.setVisibility(View.VISIBLE);
                setOperatingAnim();
                tvContent.setVisibility(View.VISIBLE);
//                tvContent.setText(mContext.getResources().getString(R.string.submmit_questions));
                break;
        }
    }

    private void setOperatingAnim(){
        if (operatingAnim != null) {
            pbLoading.clearAnimation();
            pbLoading.startAnimation(operatingAnim);
        }
        pbLoading.setVisibility(View.VISIBLE);
    }
    public void setViewGone(){
        this.setVisibility(View.GONE);
        if(pbLoading != null){
            pbLoading.clearAnimation();
        }
    }


//    public boolean isVisibiliy(){
//        return this.isShown()
//    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
