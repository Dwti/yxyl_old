package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.inter.SetAnswerCallBack;

/**
 * Created by JS-00 on 2016/11/22.
 */

public class MyEdittext extends AppCompatEditText {

    private Context mContext;
    private SetAnswerCallBack callBack;
    private boolean isStateHollow = false;   //两种背景  一个是实心的，一个是空心的

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

    public void setMistakeCallBack(SetAnswerCallBack callBack) {
        this.callBack = callBack;
    }

    public void setTextData(String text) {
        this.setText(text);
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() > 0) {
                if (!isStateHollow)
                    setStateHollow();
            } else {
                if (!hasFocus() && isStateHollow)
                    setStateFull();
            }
        }
    };

    private void init(Context context) {
        this.mContext = context;
        setTextColor(Color.BLACK);
        setStateFull();
        addTextChangedListener(watcher);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        int len = MyEdittext.this.getText().length();
        if (focused) {
            if (!isStateHollow)
                setStateHollow();
        } else {
            if (TextUtils.isEmpty(MyEdittext.this.getText().toString()) && isStateHollow)
                setStateFull();
            MyEdittext.this.setSelection(0);
        }
    }

    /**
     * 设置背景为实心
     */
    private void setStateFull() {
        MyEdittext.this.setBackground(mContext.getResources().getDrawable(R.drawable.fill_full_bg));
        isStateHollow = false;
    }

    /**
     * 设置背景为空心带边框的
     */
    private void setStateHollow() {
        MyEdittext.this.setBackground(mContext.getResources().getDrawable(R.drawable.fill_empty_bg));
        isStateHollow = true;
    }
}
