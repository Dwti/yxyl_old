package com.yanxiu.gphone.hd.student.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.common.core.utils.LogInfo;

/**
 * Created by Administrator on 2015/7/10.
 *
 */
public class AnswerViewProgressLayout extends LinearLayout {

    public Context mCtx;
    public AnswerViewProgressLayout(Context context) {
        super(context);
        mCtx = context;
        initView();
    }

    public AnswerViewProgressLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCtx = context;
        initView();
    }

    public AnswerViewProgressLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCtx = context;
        initView();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 初始化view
     */
    protected void initView(){
        this.setGravity(Gravity.CENTER_VERTICAL);
    }

    public void setWeight(int position, int count){
        ImageView firstView = (ImageView) this.getChildAt(0);
        ImageView secondView = (ImageView) this.getChildAt(1);
        LayoutParams firstLp = (LayoutParams) firstView.getLayoutParams();
        firstLp.weight = count - position;
        firstView.setLayoutParams(firstLp);
        LayoutParams secondeLp = (LayoutParams) secondView.getLayoutParams();
        secondeLp.weight = position;
        secondView.setLayoutParams(secondeLp);
        LogInfo.log("geny", count - position + "--- count - position----" + position);
    }


}
