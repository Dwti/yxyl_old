package com.yanxiu.gphone.studentold.view;

import android.content.Context;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * com.yanxiu.gphone.student.view
 * Created by cangHaiXiao.
 * Time : 2016/12/5 17:34.
 * Function :
 */

public class MyBoldTextView extends TextView {
    public MyBoldTextView(Context context) {
        super(context);
        initView(context);
    }

    public MyBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyBoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        TextPaint paint=this.getPaint();
        paint.setFakeBoldText(true);
    }
}
