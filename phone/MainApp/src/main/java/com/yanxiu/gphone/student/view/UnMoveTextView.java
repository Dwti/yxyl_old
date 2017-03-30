package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/30 16:16.
 * Function :
 */

public class UnMoveTextView extends TextView {
    public UnMoveTextView(Context context) {
        super(context);
    }

    public UnMoveTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UnMoveTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(){

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
