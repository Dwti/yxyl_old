package com.yanxiu.gphone.hd.student.view;

import android.view.View;

/**
 * Created by hai8108 on 16/3/2.
 */
public class WrapperView {
    private View mTarget;

    public WrapperView (View target) {
        mTarget = target;
    }

    public int getWidth() {
        return mTarget.getLayoutParams().width;
    }

    public void setWidth(int width) {
        mTarget.getLayoutParams().width = width;
        mTarget.requestLayout();
    }

    public int getHeight() {
        return mTarget.getLayoutParams().height;
    }

    public void setHeight(int height) {
        mTarget.getLayoutParams().height = height;
        mTarget.requestLayout();
    }
}
