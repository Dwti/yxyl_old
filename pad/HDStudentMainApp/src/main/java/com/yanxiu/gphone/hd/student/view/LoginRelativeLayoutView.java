package com.yanxiu.gphone.hd.student.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2015/7/7.
 */
public class LoginRelativeLayoutView extends RelativeLayout{

    private WindowSoftListener windowSoftListener;

    public LoginRelativeLayoutView(Context context) {
        super(context);
    }

    public LoginRelativeLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoginRelativeLayoutView(Context context, AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(windowSoftListener!=null && oldh != 0){
            if(h > oldh){
                windowSoftListener.windowSoftHide();
            }else{
                windowSoftListener.windowSoftShow();
            }
        }
    }

    public void setWindowSoftListener(WindowSoftListener softListener){
        windowSoftListener = softListener;
    }

    public interface WindowSoftListener{
        void  windowSoftShow();
        void  windowSoftHide();
    }
}
