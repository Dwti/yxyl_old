package com.yanxiu.gphone.studentold.HtmlParser;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.widget.TextView;

import com.yanxiu.gphone.studentold.HtmlParser.Span.ImageClickableSpan;

/**
 * Created by Administrator on 2016/9/5.
 */
public class MyImageSpanLinkMovementMethod extends LinkMovementMethod {

    private static MyImageSpanLinkMovementMethod Instance;

    public static MyImageSpanLinkMovementMethod getInstance() {
        if (Instance == null) {
            Instance = new MyImageSpanLinkMovementMethod();
        }
        return Instance;
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ImageClickableSpan[] link = buffer.getSpans(off, off, ImageClickableSpan.class);

            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    link[0].onClick(widget);
                } else if (action == MotionEvent.ACTION_DOWN) {
                    Selection.setSelection(buffer,
                            buffer.getSpanStart(link[0]),
                            buffer.getSpanEnd(link[0]));
                }

                return true;
            } else {
                Selection.removeSelection(buffer);
            }
        }
        return false;
    }
}
