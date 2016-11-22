package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
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

    private void init(Context context){
        this.mContext=context;
        setOnFocusChangeListener(listener);
    }

//    @Override
//    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
//        super.onFocusChanged(focused, direction, previouslyFocusedRect);
//        this.focused=focused;
//        if (focused){
//            this.setSelection(this.getText().toString().length());
//        }else {
//            this.setText(this.getText().toString());
//        }
//    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        super.setOnFocusChangeListener(l);
    }

    private OnFocusChangeListener listener=new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            MyEdittext.this.setSelection(MyEdittext.this.getText().toString().length());
        }
    };

    //
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction()==MotionEvent.ACTION_DOWN&&!focused){
//            this.setSelection(this.getText().toString().length());
////            return true;
//        }
//        return super.onTouchEvent(event);
//    }
}
