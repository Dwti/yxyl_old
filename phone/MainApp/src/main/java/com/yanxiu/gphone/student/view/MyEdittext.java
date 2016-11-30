package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by JS-00 on 2016/11/22.
 */

public class MyEdittext extends EditText {

    private Context mContext;
    private boolean focused;

    public MyEdittext(Context context) {
        super(context);
        init(context);
    }

    public MyEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        this.focused = focused;
        if (focused) {
            this.setSelection(this.getText().toString().length());
        } else {
            this.setText(this.getText().toString());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return super.onTouchEvent(event);
        }

        if (!focused) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                MyEdittext.this.setFocusable(true);
                MyEdittext.this.setFocusableInTouchMode(true);
                MyEdittext.this.requestFocus();
                InputMethodManager imm = (InputMethodManager) MyEdittext.this.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(MyEdittext.this, 0);
                MyEdittext.this.setSelection(MyEdittext.this.getText().toString().length());
            }
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }
}
