package com.yanxiu.gphone.student.HtmlParser;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;

import com.yanxiu.gphone.student.HtmlParser.Interface.ImageSpanOnclickListener;
import com.yanxiu.gphone.student.HtmlParser.Span.ImageClickableSpan;
import com.yanxiu.gphone.student.view.ClozzTextview;

/**
 * Created by Administrator on 2016/9/5.
 */
public class MyImageSpan extends ImageClickableSpan {

    public static final int width=120;
    private Context context;
    private ImageSpanOnclickListener listener;
    private ClozzTextview.Buttonbean buttonbean;

    public MyImageSpan(Drawable d, int verticalAlignment) {
        super(d, verticalAlignment);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setObject(Object object) {
        this.buttonbean = (ClozzTextview.Buttonbean) object;
    }

    public void setMyImageSpanOnclickListener(ImageSpanOnclickListener listener) {
        this.listener = listener;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getDrawable();
        Rect rect=b.getBounds();

        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        int transY = (y + fm.descent + y + fm.ascent) / 2 - b.getBounds().bottom / 2;

        b.setBounds(0,0,rect.width(),(fm.descent-fm.ascent));

        canvas.save();
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();

        Rect targetRect = new Rect((int) x, fm.ascent + y, width + (int) x, fm.descent + y);

        Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint1.setStrokeWidth(3);
        paint1.setTextSize(buttonbean.getTextsize()*2-5);
        paint1.setColor(Color.parseColor("#00000000"));
        canvas.drawRect(targetRect, paint1);
        paint1.setColor(Color.RED);
        Paint.FontMetricsInt fontMetrics = paint1.getFontMetricsInt();
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        paint1.setTextAlign(Paint.Align.CENTER);
        String string = "";
        if (TextUtils.isEmpty(buttonbean.getText())) {
            string = buttonbean.getQuestion_id() + "";
        } else {
            string = buttonbean.getQuestion_id() + "." + buttonbean.getText();
        }
        canvas.drawText(string, targetRect.centerX(), baseline, paint1);
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(buttonbean);
        }
    }
}
