package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * com.yanxiu.gphone.student.view
 * Created by cangHaiXiao.
 * Time : 2016/12/5 14:23.
 * Function :
 */

public class MyViewPager extends ViewPager {

    public static final int Move_To_Left=0;
    public static final int Move_To_Right=1;
    private MoveListener listener;
    private float Down_X;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                Down_X=ev.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                float Move_X=ev.getRawX();
                if (Move_X>Down_X){
                    if (listener!=null){
                        listener.movelistener(Move_To_Right);
                    }
                }else {
                    if (listener!=null){
                        listener.movelistener(Move_To_Left);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    public void setMoveListener(MoveListener listener){
        this.listener=listener;
    }

    public interface MoveListener{
        void movelistener(int state);
    }

}
