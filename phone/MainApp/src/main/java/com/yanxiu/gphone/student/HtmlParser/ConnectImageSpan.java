package com.yanxiu.gphone.student.HtmlParser;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

/**
 * com.yanxiu.gphone.student.HtmlParser
 * Created by cangHaiXiao.
 * Time : 2016/12/1 16:44.
 * Function :
 */

public class ConnectImageSpan extends ImageSpan {
    public ConnectImageSpan(Drawable d, int verticalAlignment) {
        super(d, verticalAlignment);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text,
                     int start, int end, float x,
                     int top, int y, int bottom, Paint paint) {
        Drawable b = getDrawable();

        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        if (b.getBounds().bottom-b.getBounds().top>fm.descent-fm.ascent){
            super.draw(canvas, text, start, end, x, top, y, bottom, paint);
        }else {
            int transY = (y + fm.descent + y + fm.ascent) / 2 - b.getBounds().bottom / 2;
            canvas.save();
            canvas.translate(x, transY);
            b.draw(canvas);
            canvas.restore();
        }
    }
}
