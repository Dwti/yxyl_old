package com.yanxiu.gphone.studentold.HtmlParser;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.yanxiu.gphone.studentold.R;

public class URLDrawable extends BitmapDrawable {
    public Drawable drawable;

    @SuppressWarnings("deprecation")
    public URLDrawable(Context context) {
        drawable = context.getResources().getDrawable(R.drawable.image_loading_in_text_24);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        this.setBounds(getDefaultImageBounds(context));
    }

    @Override
    public void draw(Canvas canvas) {
        if (drawable != null) {
            drawable.draw(canvas);
        }
    }

    @SuppressWarnings("deprecation")
    public Rect getDefaultImageBounds(Context context) {
        Rect bounds = new Rect(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        return bounds;
    }
}
