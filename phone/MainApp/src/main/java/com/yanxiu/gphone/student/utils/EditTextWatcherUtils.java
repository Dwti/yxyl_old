package com.yanxiu.gphone.student.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by CangHaiXiao on 2016/11/14.
 * 密码过滤器（正则）
 */

public class EditTextWatcherUtils {

    private static final String regs = "[A-Za-z\\d!@#$%^&*()'\"=_:;?~|+-\\/\\[\\]\\{\\}<>]+";
    private static EditTextWatcherUtils watcherUtils = new EditTextWatcherUtils();
    private EditText view;
    private String TextBeforeChange = "";

    public static EditTextWatcherUtils getInstence() {
        return watcherUtils;
    }

    public void setEditText(EditText view) {
        this.view = view;
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
        }
    };
}
