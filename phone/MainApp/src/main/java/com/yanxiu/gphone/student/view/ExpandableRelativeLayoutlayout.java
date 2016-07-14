/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright 2014 Manabu Shimobe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yanxiu.gphone.student.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.common.core.utils.LogInfo;


public class ExpandableRelativeLayoutlayout extends RelativeLayout{

    /* The default number of lines */
    private static final int MAX_COLLAPSED_LINES = 8;

    /* The default animation duration */
    private static final int DEFAULT_ANIM_DURATION = 300;

    /* The default alpha value when the animation starts */
    private static final float DEFAULT_ANIM_ALPHA_START = 0.7f;


    private boolean mCollapsed = true; // Show short version as default.

    private int mCollapsedHeight;

    private int mAnimationDuration = DEFAULT_ANIM_DURATION;

    private boolean mAnimating;

    /* Listener for callback */
    private OnExpandStateChangeListener mListener;


    public ExpandableRelativeLayoutlayout(Context context) {
        this(context, null);
    }

    public ExpandableRelativeLayoutlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ExpandableRelativeLayoutlayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

//    private View tagView;

    public void onExpandable(final View tagView) {


//        this.tagView = tagView;
        mCollapsed = !mCollapsed;

        // mark that the animation is in progress
        mAnimating = true;

        Animation animation = null;
        if (mCollapsed) {
            LogInfo.log("geny", "收起" + mCollapsedHeight + "---" + getHeight());
            animation = new ExpandCollapseAnimation(this, mCollapsedHeight / 2 + mCollapsedHeight, mCollapsedHeight);
        } else {
            LogInfo.log("geny", "打开"+ + mCollapsedHeight + "---" + getHeight());
            animation = new ExpandCollapseAnimation(this, mCollapsedHeight, mCollapsedHeight / 2 + mCollapsedHeight);
        }

        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                // clear animation here to avoid repeated applyTransformation() calls
                clearAnimation();
                // clear the animation flag
                mAnimating = false;

                // notify the listener
                if (mListener != null) {
                    mListener.onExpandStateChanged(tagView, !mCollapsed);
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        clearAnimation();
        startAnimation(animation);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // while an animation is in progress, intercept all the touch events to children to
        // prevent extra clicks during the animation
        return mAnimating;
    }

    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @SuppressWarnings("deprecation")
        @Override
        public void onGlobalLayout() {
            getViewTreeObserver().removeGlobalOnLayoutListener(this);
            mCollapsedHeight = ExpandableRelativeLayoutlayout.this.getHeight();
            LogInfo.log("geny", " MyOnGlobalLayoutListener  mCollapsedHeight"+  "---" + mCollapsedHeight);
        }

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setOnExpandStateChangeListener(@Nullable OnExpandStateChangeListener listener) {
        mListener = listener;
    }

    private void init(AttributeSet attrs) {
        ViewTreeObserver.OnGlobalLayoutListener listener = new MyOnGlobalLayoutListener();
        ExpandableRelativeLayoutlayout.this.getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }


    class ExpandCollapseAnimation extends Animation {
        private final View mTargetView;
        private final int mStartHeight;
        private final int mEndHeight;

        public ExpandCollapseAnimation(View view, int startHeight, int endHeight) {
            mTargetView = view;
            mStartHeight = startHeight;
            mEndHeight = endHeight;
            LogInfo.log("geny", "ExpandCollapseAnimation" + startHeight + "------------" + endHeight);
            setDuration(mAnimationDuration);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
//            final int newHeight = (int)((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
//            LogInfo.log("geny", "newHeight"+newHeight);
            LinearLayout.LayoutParams params  = (LinearLayout.LayoutParams) mTargetView.getLayoutParams();
//            params.height = newHeight;
            if(mEndHeight > mStartHeight){
                params.weight = (float) (1.5 + interpolatedTime * 3.5);
            }else{
                params.weight = (float) (5 - interpolatedTime * 3.5);
            }
            mTargetView.requestLayout();
            LogInfo.log("geny", "requestLayout" + "---" + getHeight());
        }

        @Override
        public void initialize( int width, int height, int parentWidth, int parentHeight ) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds( ) {
            return true;
        }
    }

    public interface OnExpandStateChangeListener {
        /**
         * Called when the expand/collapse animation has been finished
         *
         * @param isExpanded - true if the TextView has been expanded
         */
        void onExpandStateChanged(View view, boolean isExpanded);
    }
}