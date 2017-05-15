package com.yanxiu.gphone.studentold.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.utils.Util;

/**
 * 描边
 * Created by Administrator on 2015/12/23.
 */
public class StrokeTextView extends TextView {

    private TextView borderText = null;

    public StrokeTextView(Context context) {
        super(context);
        borderText = new TextView(context);
        init();
    }

    public StrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        borderText = new TextView(context,attrs);
        init();
    }

    public StrokeTextView(Context context, AttributeSet attrs,
                          int defStyle) {
        super(context, attrs, defStyle);
        borderText = new TextView(context,attrs,defStyle);
        init();
    }

    public void init(){
        Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.ARAL_ROUNDED_BOLD, this);
        Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.ARAL_ROUNDED_BOLD, borderText);
        TextPaint tp1 = borderText.getPaint();
        tp1.setStrokeWidth(2);
        tp1.setStyle(Paint.Style.STROKE);
        borderText.setTextColor(getResources().getColor(R.color.color_007373));
        borderText.setGravity(getGravity());
    }

    @Override
    public void setLayoutParams (ViewGroup.LayoutParams params){
        super.setLayoutParams(params);
        borderText.setLayoutParams(params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        CharSequence tt = borderText.getText();

        if(tt== null || !tt.equals(this.getText())){
            borderText.setText(getText());
            this.postInvalidate();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        borderText.measure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout (boolean changed, int left, int top, int right, int bottom){
        super.onLayout(changed, left, top, right, bottom);
        borderText.layout(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        borderText.draw(canvas);
        super.onDraw(canvas);
    }

}