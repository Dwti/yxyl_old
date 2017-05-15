package com.yanxiu.gphone.studentold.view;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;

import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.utils.Util;

/**
 * com.yanxiu.gphone.student.view
 * Created by cangHaiXiao.
 * Time : 2016/12/5 16:57.
 * Function :
 */

public class MySearchSchoolEdittext extends EditText{
    public MySearchSchoolEdittext(Context context) {
        super(context);
        initView(context);
    }

    public MySearchSchoolEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MySearchSchoolEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        Drawable drawable=context.getResources().getDrawable(R.drawable.search_icon);
        int height=getFontHeight(Util.dipToPx(14));
        drawable.setBounds(0,0,height,height);
        setCompoundDrawables(drawable,null,null,null);
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
    }

    public int getFontHeight(float fontSize){
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

}
