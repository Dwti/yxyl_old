package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.IntRange;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import com.yanxiu.gphone.student.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/1/19 17:07.
 * Function :
 */

public class MyMaxHeightLinearLayout extends LinearLayout {

//    private Context mContext;
    private int downY;
    private int mTouchSlop;
    private int maxheight=-1;

    public MyMaxHeightLinearLayout(Context context) {
        this(context,null);
    }

    public MyMaxHeightLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public MyMaxHeightLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
//        this.mContext=context;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        TypedArray array=context.obtainStyledAttributes(attrs,R.styleable.MyMaxHeightLinearLayout);
        maxheight=array.getDimensionPixelSize(R.styleable.MyMaxHeightLinearLayout_maxHeight,-1);
        array.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int height=b-t;
        if (maxheight<height){
            super.onLayout(changed,l,t,r,t+maxheight);
        }else {
            super.onLayout(changed, l, t, r, b);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height_mode= MeasureSpec.getMode(heightMeasureSpec);
        @IntRange int height_size= MeasureSpec.getSize(heightMeasureSpec);
        int should_height=0;
        for (int i=0;i<getChildCount();i++){
            View child_view=getChildAt(i);
            LayoutParams params= (LayoutParams) child_view.getLayoutParams();
            child_view.measure(widthMeasureSpec,0);
            int height=child_view.getMeasuredHeight()<=params.height?params.height:child_view.getMeasuredHeight();
            should_height+=height;
        }

        int padding_top=this.getPaddingTop();
        int padding_bottom=this.getPaddingBottom();

        should_height+=padding_top;
        should_height+=padding_bottom;

        if (maxheight!=-1) {
            height_size=should_height<=maxheight?should_height:maxheight;
        }

        heightMeasureSpec= MeasureSpec.makeMeasureSpec(height_size,height_mode);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }
}
