package com.yanxiu.gphone.studentold.HtmlParser.Span;

import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import android.view.View;

/**
 * Created by Administrator on 2016/8/30.
 */
public abstract class ImageClickableSpan extends ImageSpan {

    public ImageClickableSpan(Drawable d, int verticalAlignment) {
        super(d, verticalAlignment);
    }

    public abstract void onClick(View view);
}
