package com.yanxiu.gphone.student.view.question.readquestion;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.activity.AnswerViewActivity;
import com.yanxiu.gphone.student.activity.WrongAnswerViewActivity;


/**
 * Created by Administrator on 2015/7/15.
 */
public class InterViewPager extends ViewPager {


    private float DOWN_X = 0;
    private float DOWN_Y = 0;
    private static final int HORIZONTAL_DISTANCE = 30;
    private static final int VERTICAL_DISTANCE = 50;
    private Context mContext;

    public InterViewPager(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public InterViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

    private void initView() {
//        this.setOnPageChangeListener(new OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//                if (InterViewPager.this.getAdapter().getCount() == i + 1) {
//                    isLastPage = true;
//                    LogInfo.log("geny", "last item");
//                } else {
//                    isLastPage = false;
//                    LogInfo.log("geny", "no last item");
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });
    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//
//
//    }
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        // while an animation is in progress, intercept all the touch events to children to
//        // prevent extra clicks during the animation
//        return isLastPage;
//    }


//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if(InterViewPager.this.getAdapter().getCount() == this.getCurrentItem() + 1 && isLastOne){
//            isLastOne = false;
//            return false;
//        }
//        return super.dispatchKeyEvent(event);
//    }

//    @Override
//    public boolean onInterceptHoverEvent(MotionEvent event) {
//        switch (event.getAction())
//        {
//            case MotionEvent.ACTION_DOWN:
//                DOWN_X = event.getX();
//                DOWN_Y = event.getY();
//                LogInfo.log("geny", "DOWN_X" + DOWN_X);
//                LogInfo.log("geny", "DOWN_Y" + DOWN_Y);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float currentX = event.getX();
//                float currentY = event.getY();
//                if ((Math.abs((DOWN_Y - currentY)) > VERTICAL_DISTANCE
//                        && Math.abs(DOWN_X - currentX) < HORIZONTAL_DISTANCE)){
//                    LogInfo.log("geny", "currentX" + currentX);
//                    LogInfo.log("geny", "currentY" + currentY);
//                    this.getParent().requestDisallowInterceptTouchEvent(false);
//                    return true;
//                }
//                break;
//            case MotionEvent.ACTION_UP:// 如果按下的地方和手指起来的地方在一起的话就认为是用户进行了点击
//                break;
//            default:
//                break;
//        }
//        this.getParent().requestDisallowInterceptTouchEvent(true);//
//        // 让事件交给父控件来处理，整个Tab就可以滑动了。
//        return false;
//    }
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent event) {
//        /*switch (event.getAction())
//        {
//            case MotionEvent.ACTION_DOWN:
//                DOWN_X = event.getX();
//                DOWN_Y = event.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float currentX = event.getX();
//                float currentY = event.getY();
//                LogInfo.log("king", "DOWN_X - currentX = " + (DOWN_X-currentX));
//                LogInfo.log("king", "DOWN_Y - currentY = " + (DOWN_Y-currentY));
//                if ((Math.abs((DOWN_Y - currentY)) > VERTICAL_DISTANCE
//                        && Math.abs(DOWN_X - currentX) < HORIZONTAL_DISTANCE)){
//                    this.getParent().requestDisallowInterceptTouchEvent(false);
//                    return true;
//                }
//                break;
//            case MotionEvent.ACTION_UP:// 如果按下的地方和手指起来的地方在一起的话就认为是用户进行了点击
//                break;
//            default:
//                break;
//        }
//        this.getParent().requestDisallowInterceptTouchEvent(true);//
//        // 让事件交给父控件来处理，整个Tab就可以滑动了。
//        return false;*/
//        return super.onInterceptTouchEvent(event);
//    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean b = super.dispatchKeyEvent(event);
        LogInfo.log("geny", "dispatchKeyEvent--------------------------" + b);
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean b = super.onTouchEvent(ev);
        LogInfo.log("geny", "oonTouchEvent--------------------------" + b);
        return b;
    }

    public void toParentViewPager(){
        if(mContext != null ){
            if (mContext instanceof AnswerViewActivity) {
                ((AnswerViewActivity) mContext).selectViewPager();
            }else if (mContext instanceof WrongAnswerViewActivity){
                ((WrongAnswerViewActivity) mContext).selectViewPager();
            }
        }
//        this.requestFocus();
//        LogInfo.log("geny", "toParentViewPager last item");
//        isLastOne = true;
//        this.getParent().requestDisallowInterceptTouchEvent(true);
//        KeyEvent keyEvent;
//        keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT);
//        dispatchKeyEvent(keyEvent);
//
//        keyEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_RIGHT);
//        dispatchKeyEvent(keyEvent);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent event) {
//        // 在这里做手势的判断
//        // 如果是左边滑动
//
//        // 记住手指点下去的那个点
//        // 判断移动的位置和距离
//        switch (event.getAction())
//        {
//            case MotionEvent.ACTION_DOWN:
//                DOWN_X = event.getX();
////                DOWN_Y = event.getY();
////                LogInfo.log("geny----", "ACTION_DOWN 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰" + DOWN_X);
//                //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
////                getParent().requestDisallowInterceptTouchEvent(true);
////                LogInfo.log("geny", "onTouchEvent x DOWN_X zhou" + DOWN_X);
////                LogInfo.log("geny", "onTouchEvent y zhou" + DOWN_Y);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float currentX = event.getX();
////                float currentY = event.getY();
////                LogInfo.log("geny", "onTouchEvent x currentX zhou" + currentX);
////                LogInfo.log("geny", "onTouchEvent y currentX zhou" + currentY);
////                LogInfo.log("geny", "onTouchEvent y zhou" + Math.abs((DOWN_Y - currentY)));
////                LogInfo.log("geny", "onTouchEvent x zhou" + Math.abs((DOWN_X - currentX)));
//                if (Math.abs(DOWN_X - currentX) > HORIZONTAL_DISTANCE){
//                    if (0 == this.getCurrentItem() || this.getCurrentItem() == this.getAdapter().getCount() - 1) {
//                        getParent().requestDisallowInterceptTouchEvent(false);
//                        LogInfo.log("geny----", "last item.............");
//                    }else{
//                        getParent().requestDisallowInterceptTouchEvent(true);
//                        LogInfo.log("geny----", "no last item.......");
//                    }
////                    //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
////                    LogInfo.log("geny----", "ACTION_MOVE 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰" + DOWN_X);
////                    getParent().requestDisallowInterceptTouchEvent(true);
////                    return true;
//                }
//                break;
//            case MotionEvent.ACTION_UP://抬起
//                LogInfo.log("geny----", "realese.......");
//            case MotionEvent.ACTION_CANCEL:
//                getParent().requestDisallowInterceptTouchEvent(false);
//                LogInfo.log("geny----", "realese.......");
//                break;
//            default:
//                break;
//        }
//        LogInfo.log("geny----", "default 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰");
//        this.getParent().requestDisallowInterceptTouchEvent(false);//

        // 让事件交给父控件来处理，整个Tab就可以滑动了。
//        return super.onInterceptTouchEvent(event);
//    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        if (0 == this.getCurrentItem() || this.getCurrentItem() == this.getAdapter().getCount() - 1) {
//            super.onTouchEvent(ev);
//            LogInfo.log("geny----", "last item  onTouchEvent.............");
//            return false;
//        }else{
//            super.onTouchEvent(ev);
//            LogInfo.log("geny----", "no last item.onTouchEvent......");
//            return true;
//        }
//    }

    //    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if(isLastPage){
//            return false;
//        }else{
//            return super.dispatchKeyEvent(event);
//        }
//    }
}
