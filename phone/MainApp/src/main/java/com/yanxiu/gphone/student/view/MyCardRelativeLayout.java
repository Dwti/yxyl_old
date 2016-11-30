package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by JS-00 on 2016/11/29.
 */

public class MyCardRelativeLayout extends RelativeLayout {
    private Context mContext;
    public MyCardRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mContext=context;
    }

    public MyCardRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyCardRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Toast.makeText(mContext,"asd",Toast.LENGTH_SHORT).show();
        return super.onTouchEvent(event);
    }
}
