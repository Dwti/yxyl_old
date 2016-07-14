/**
 * @file XListViewHeader.java
 * @create Apr 18, 2012 5:22:27 PM
 * @author Maxwin
 * @description XListView's header
 */
package com.common.core.view.xlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.core.R;


public class XListViewHeaderNew extends LinearLayout {
    private LinearLayout mContainer;
    private ImageView mArrowImageView;
    private ImageView mProgressBar;
    private TextView mHintTextView;
    private int mState = STATE_NORMAL;

//    private Animation mRotateUpAnim;
//    private Animation mRotateDownAnim;

//    private final int ROTATE_ANIM_DURATION = 180;

    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;
    public final static int STATE_SUCC = 3;
    public final static int STATE_FAIL = 4;
    public final static int STATE_DONE = 5;

    private Animation operatingAnim;
    public String str_ing;
    public String str_succ;


    public XListViewHeaderNew(Context context) {
        super(context);
        initView(context,false);
    }

    public XListViewHeaderNew(Context context,boolean setHeight) {
        super(context);
        initView(context,setHeight);
    }

    /**
     * @param context
     * @param attrs
     */
    public XListViewHeaderNew(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,false);
    }

    private void initView(Context context,boolean setHeight) {
        // 初始情况，设置下拉刷新view高度为0,使用PullToRefreshView需要设置高度
        LayoutParams lp = null;
        if(setHeight == false){
            lp = new LayoutParams(
                    LayoutParams.MATCH_PARENT, 0);
        }else{
            lp = new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }

        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.xlistview_header, null);

        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);

        if(getInterceptView() != null){
            getInterceptView().onIntercept();
        }

        mArrowImageView = (ImageView)findViewById(R.id.xlistview_header_arrow);
        mHintTextView = (TextView)findViewById(R.id.xlistview_header_hint_textview);
        mProgressBar = (ImageView)findViewById(R.id.xlistview_header_progressbar);

        operatingAnim = AnimationUtils.loadAnimation(context, R.anim
                .xlistview_header_progress);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);

        if (operatingAnim != null) {
            mArrowImageView.startAnimation(operatingAnim);
        }
//        mRotateUpAnim = new RotateAnimation(0.0f, 360.0f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//                0.5f);
//        mRotateUpAnim.setRepeatMode(Animation.INFINITE);
//        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
//        mRotateUpAnim.setFillAfter(true);
//        mRotateDownAnim = new RotateAnimation(360.0f, 0.0f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//                0.5f);
//        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
//        mRotateDownAnim.setRepeatMode(Animation.INFINITE);
//        mRotateDownAnim.setFillAfter(true);
    }



    private InterceptView interceptView;

    public InterceptView getInterceptView() {
        return interceptView;
    }

    public void setInterceptView(InterceptView interceptView) {
        this.interceptView = interceptView;
    }

    public interface InterceptView{
        void onIntercept();
    }


    public void setState(int state) {
        if (state == mState) return ;

        if (state == STATE_REFRESHING) {	// 显示进度
            mArrowImageView.clearAnimation();
            mArrowImageView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.startAnimation(operatingAnim);
        } else if(state == STATE_SUCC){
            mProgressBar.setVisibility(View.INVISIBLE);
            mProgressBar.clearAnimation();
        } else{	// 显示箭头图片
            mArrowImageView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
            mProgressBar.clearAnimation();
        }

        switch(state){
            case STATE_NORMAL:
                if (mState == STATE_READY) {
                    mArrowImageView.startAnimation(operatingAnim);
                }
                if (mState == STATE_REFRESHING) {
                    mArrowImageView.clearAnimation();
                }
                mHintTextView.setText(R.string.xlistview_header_hint_normal);
                break;
            case STATE_READY:
                if (mState != STATE_READY) {
                    mArrowImageView.clearAnimation();
                    mArrowImageView.startAnimation(operatingAnim);
                    mHintTextView.setText(R.string.xlistview_header_hint_ready);
                }
                break;
            case STATE_REFRESHING:
                mHintTextView.setText(R.string.xlistview_header_hint_loading);
                break;
            case STATE_SUCC:
                mHintTextView.setText(R.string.xlistview_toast_succ);

                break;
            case STATE_FAIL:
                mHintTextView.setText(R.string.xlistview_toast_fail);
                mProgressBar.setVisibility(View.INVISIBLE);
                mProgressBar.clearAnimation();
                break;
            default:
        }

        mState = state;
    }

    public void setVisiableHeight(int height) {
        if (height < 0)
            height = 0;
        LayoutParams lp = (LayoutParams) mContainer
                .getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisiableHeight() {
        return mContainer.getHeight();
    }

    public int getState() {
        return mState;
    }

}
