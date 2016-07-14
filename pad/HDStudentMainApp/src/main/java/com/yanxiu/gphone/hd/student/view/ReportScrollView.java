package com.yanxiu.gphone.hd.student.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by lidm on 2015/12/4.
 */
public class ReportScrollView extends ScrollView{
    public ReportScrollView(Context context) {
        super(context);
    }

    public ReportScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReportScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt){
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }
}
