package com.yanxiu.gphone.studentold.utils;

import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.yanxiu.gphone.studentold.HtmlParser.ConnectImageSpan;

/**
 * com.yanxiu.gphone.student.utils
 * Created by cangHaiXiao.
 * Time : 2016/12/1 17:20.
 * Function :
 */

public class SpanCenterUtils {

    private static SpanCenterUtils utils;

    public static SpanCenterUtils getInstence(){
        if (utils==null){
            utils=new SpanCenterUtils();
        }
        return utils;
    }


    public Spanned getSpan(Spanned spanned){
        if (spanned instanceof SpannableStringBuilder) {
            ImageSpan[] imageSpans = spanned.getSpans(0, spanned.length(), ImageSpan.class);
            for (ImageSpan imageSpan : imageSpans) {
                int start = spanned.getSpanStart(imageSpan);
                int end = spanned.getSpanEnd(imageSpan);
                Drawable d = imageSpan.getDrawable();
                ConnectImageSpan newImageSpan = new ConnectImageSpan(d, ImageSpan.ALIGN_BOTTOM);
                ((SpannableStringBuilder) spanned).setSpan(newImageSpan, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                ((SpannableStringBuilder) spanned).removeSpan(imageSpan);
            }
        }
        return spanned;
    }

}
