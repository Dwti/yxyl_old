package com.yanxiu.gphone.student.HtmlParser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.style.ImageSpan;

/**
 * Created by JS-00 on 2016/9/19.
 */
public class PictureImageSpan extends ImageSpan {
    public PictureImageSpan(Bitmap b) {
        super(b);
    }

    public PictureImageSpan(Bitmap b, int verticalAlignment) {
        super(b, verticalAlignment);
    }

    public PictureImageSpan(Context context, Bitmap b) {
        super(context, b);
    }

    public PictureImageSpan(Context context, Bitmap b, int verticalAlignment) {
        super(context, b, verticalAlignment);
    }

    public PictureImageSpan(Drawable d) {
        super(d);
    }

    public PictureImageSpan(Drawable d, int verticalAlignment) {
        super(d, verticalAlignment);
    }

    public PictureImageSpan(Drawable d, String source) {
        super(d, source);
    }

    public PictureImageSpan(Drawable d, String source, int verticalAlignment) {
        super(d, source, verticalAlignment);
    }

    public PictureImageSpan(Context context, Uri uri) {
        super(context, uri);
    }

    public PictureImageSpan(Context context, Uri uri, int verticalAlignment) {
        super(context, uri, verticalAlignment);
    }

    public PictureImageSpan(Context context, int resourceId) {
        super(context, resourceId);
    }

    public PictureImageSpan(Context context, int resourceId, int verticalAlignment) {
        super(context, resourceId, verticalAlignment);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        Drawable d = getDrawable();
        Rect rect = d.getBounds();
        if (fm != null) {
            Paint.FontMetricsInt fmPaint=paint.getFontMetricsInt();
            //获得文字、图片高度
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight=rect.bottom-rect.top;
            //对于这段算法LZ表示也不解，正常逻辑应该同draw中的计算一样但是显示的结果不居中，经过几次调试之后才发现这么算才会居中
            int top= drHeight/2 - fontHeight/4;
            int bottom=drHeight/2 + fontHeight/4;

            fm.ascent=-bottom;
            fm.top=-bottom;
            fm.bottom=top;
            fm.descent=top;
        }
        return rect.right;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getDrawable();
        canvas.save();
        int transY = 0;
        //获得将要显示的文本高度-图片高度除2等居中位置+top(换行情况)
        transY = ((bottom-top) - b.getBounds().bottom)/2+top;
        //偏移画布后开始绘制
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }

    @Override
    public Drawable getDrawable() {
        return super.getDrawable();
    }
}
