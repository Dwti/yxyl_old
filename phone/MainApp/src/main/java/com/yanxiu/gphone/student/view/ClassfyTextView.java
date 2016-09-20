package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;

/**
 * Created by JS-00 on 2016/9/20.
 */
public class ClassfyTextView extends TextView {

    private Context context;

    public ClassfyTextView(Context context) {
        this(context,null);
    }

    public ClassfyTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClassfyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context=context;
        this.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
        this.setGravity(Gravity.CENTER);
        this.setBackgroundResource(R.drawable.judge_item_pre);
        this.setTextColor(getResources().getColor(R.color.color_333333));
    }
}
