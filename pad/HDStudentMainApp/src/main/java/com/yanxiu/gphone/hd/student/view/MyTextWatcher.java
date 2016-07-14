package com.yanxiu.gphone.hd.student.view;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Administrator on 2015/7/24.
 */
public class MyTextWatcher implements TextWatcher {

    public MyTextWatcher(){

    }

    @Override public void beforeTextChanged(CharSequence s, int start,
            int count, int after) {

    }

    @Override public void onTextChanged(CharSequence s, int start, int before,
            int count) {

    }

    @Override public void afterTextChanged(Editable s) {
        for (int i = s.length(); i > 0; i--) {
            if (s.subSequence(i - 1, i).toString().equals("\n"))
                s.replace(i - 1, i, "");
        }
    }
}
