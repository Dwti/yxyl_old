package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.inter.SetAnswerCallBack;

import java.lang.reflect.Field;

/**
 * Created by JS-00 on 2016/11/22.
 */

public class MyEdittext extends AppCompatEditText {

    private Context mContext;
    private boolean focused;
    private SetAnswerCallBack callBack;

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

    public void setMistakeCallBack(SetAnswerCallBack callBack){
        this.callBack=callBack;
    }

    public void setTextData(String text){
        this.removeTextChangedListener(watcher);
        this.setText(text);
        this.addTextChangedListener(watcher);
    }

    TextWatcher watcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
//            if (MyEdittext.this.getText().length()>0){
//                MyEdittext.this.setBackground(mContext.getResources().getDrawable(R.drawable.fill_empty_bg));
//            }else {
//                if (focused){
//                    MyEdittext.this.setBackground(mContext.getResources().getDrawable(R.drawable.fill_empty_bg));
//                }else {
//                    MyEdittext.this.setBackground(mContext.getResources().getDrawable(R.drawable.fill_full_bg));
//                }
//            }

//            if (focused){
//                MyEdittext.this.setBackground(mContext.getResources().getDrawable(R.drawable.fill_empty_bg));
//            }else {
//                MyEdittext.this.setBackground(mContext.getResources().getDrawable(R.drawable.fill_full_bg));
//            }
            if (callBack!=null){
                callBack.callback();
            }
        }
    };

    private void init(Context context) {
        this.mContext = context;
        setTextColor(Color.BLACK);
        setBackground(mContext.getResources().getDrawable(R.drawable.fill_full_bg));
        addTextChangedListener(watcher);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
//        this.focused = focused;
//        if (focused) {
//            this.setSelection(this.getText().toString().length());
//            MyEdittext.this.setBackground(mContext.getResources().getDrawable(R.drawable.fill_empty_bg));
//        } else {
//            this.setText(this.getText().toString());
//            if (MyEdittext.this.getText().length()>0){
//                MyEdittext.this.setBackground(mContext.getResources().getDrawable(R.drawable.fill_empty_bg));
//            }else {
//                MyEdittext.this.setBackground(mContext.getResources().getDrawable(R.drawable.fill_full_bg));
//            }
//        }

        if (focused){
            MyEdittext.this.setBackground(mContext.getResources().getDrawable(R.drawable.fill_empty_bg));
        }else {
            MyEdittext.this.setBackground(mContext.getResources().getDrawable(R.drawable.fill_full_bg));
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (!isEnabled()) {
//            return super.onTouchEvent(event);
//        }
//
//        if (!focused) {
//            if (event.getAction() == MotionEvent.ACTION_UP) {
//                MyEdittext.this.setFocusable(true);
//                MyEdittext.this.setFocusableInTouchMode(true);
//                MyEdittext.this.requestFocus();
//                InputMethodManager imm = (InputMethodManager) MyEdittext.this.getContext()
//                        .getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(MyEdittext.this, 0);
//                MyEdittext.this.setSelection(MyEdittext.this.getText().toString().length());
//            }
//            return true;
//        } else {
//            return super.onTouchEvent(event);
//        }
//    }
}
