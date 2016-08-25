package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2016/8/23.
 */
public class FillBlankAnswerView extends LinearLayout {
    private Context mContext;

    public FillBlankAnswerView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public FillBlankAnswerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public FillBlankAnswerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        this.setOrientation(VERTICAL);
    }

    public void setAnswerTemplate(int count) {
        removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        for (int i = 0; i < count; i++) {
            View itemView = inflater.inflate(R.layout.fill_blank_answer_item, this, false);
            TextView tv_num = (TextView) itemView.findViewById(R.id.tv_num);
            tv_num.setText("(" + (i + 1) + ")");
            this.addView(itemView);
        }
    }

    public ArrayList<String> getAnswerList() {
        ArrayList<String> listAnswer = new ArrayList<>();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View itemView = getChildAt(i);
            EditText et_answer = (EditText) itemView.findViewById(R.id.et_answer);
            String answer = et_answer.getText().toString();
            if (!TextUtils.isEmpty(answer))
                listAnswer.add(et_answer.getText().toString());
        }
        return listAnswer;
    }

    public void setAnswerList(List<String> list) {
        setAnswerList(list, true);
    }

    public void setAnswerList(List<String> list, boolean editable) {
        if (list == null || list.isEmpty())
            return;
        int count;
        if (list.size() < getChildCount()) {
            count = list.size();
        } else {
            count = getChildCount();
        }
        for (int i = 0; i < count; i++) {
            View itemView = getChildAt(i);
            EditText et_answer = (EditText) itemView.findViewById(R.id.et_answer);
            et_answer.setText(list.get(i));
            et_answer.setEnabled(editable);
        }
    }

}
