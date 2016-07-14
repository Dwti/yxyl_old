package com.yanxiu.gphone.student.view.stickhome;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by lidm on 2015/12/4.
 *  home首页自定义view  滑到固定位置回调
 */
public class ScrollHomeView extends ScrollView{
    private ScrollFixPositionListener scrollFixPositionListener;
    private int fixPosition = -1;
    private int lastPosition = 0;
    private boolean isScrolling = false;

    private boolean isFromUserClick = false;

    public ScrollHomeView(Context context) {
        super(context);
    }

    public ScrollHomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollHomeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt){
        lastPosition = t;
//        LogInfo.log("geny", "fixPosition---" + fixPosition);
//        LogInfo.log("geny", "t + getHeight()---" + t + getHeight());
        if(isFromUserClick && t ==  fixPosition && scrollFixPositionListener != null){
            //ScrollView滑动到底部了
            scrollFixPositionListener.scrollFixPosition();
        }
    }

    public void setIsFromUserClick(boolean isFromUserClick) {
        this.isFromUserClick = isFromUserClick;
    }

    public int getLastPosition() {
        return lastPosition;
    }

    public void setScrollFixPositionListener(ScrollFixPositionListener scrollFixPositionListener) {
        this.scrollFixPositionListener = scrollFixPositionListener;
    }

    public void setFixPosition(int fixPosition) {
        this.fixPosition = fixPosition;
    }

    public interface ScrollFixPositionListener{
        void scrollFixPosition();
    }

    public boolean isScrolling() {
        return isScrolling;
    }

    public void setIsScrolling(boolean isScrolling) {
        this.isScrolling = isScrolling;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(isScrolling){
            return ev.getAction() == MotionEvent.ACTION_MOVE;
        }else {
            return super.onTouchEvent(ev);
        }
    }
}
