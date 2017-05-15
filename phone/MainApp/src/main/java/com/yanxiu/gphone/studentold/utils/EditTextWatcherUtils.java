package com.yanxiu.gphone.studentold.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by CangHaiXiao on 2016/11/14.
 * 密码过滤器（正则）
 */

public class EditTextWatcherUtils {

    private static final String regs = "[A-Za-z\\d!@#$%^&*()'\"=_:;?~|+-\\/\\[\\]\\{\\}<>]+";
    private static EditTextWatcherUtils watcherUtils;
    private OnTextChangedListener listener;
    private EditText view;
    private String TextBeforeChange = "";

    public static EditTextWatcherUtils getInstence() {
        watcherUtils = new EditTextWatcherUtils();
        return watcherUtils;
    }

    public interface OnTextChangedListener{
        void onTextChanged(String text);
    }

    public void setEditText(EditText view,OnTextChangedListener listener) {
        this.view = view;
        this.listener=listener;
        view.addTextChangedListener(watcher);
    }

    public void clearEditText() {
        this.view = null;
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            TextBeforeChange = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString();
            if (!text.equals("") && !text.matches(regs)) {
                if (view != null) {
                    view.setText(TextBeforeChange);
                    int index=view.getText().length();
                    if (index>0) {
                        view.setSelection(index);
                    }
                }
            }
            if (listener!=null){
                listener.onTextChanged(view.getText().toString());
            }
        }
    };
}
