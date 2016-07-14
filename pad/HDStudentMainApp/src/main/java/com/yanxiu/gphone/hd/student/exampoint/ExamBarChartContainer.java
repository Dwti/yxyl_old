package com.yanxiu.gphone.hd.student.exampoint;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2015/11/16.
 */
public class ExamBarChartContainer extends RelativeLayout {

    public ExamBarChartContainer(Context context) {
        super(context);
    }

    public ExamBarChartContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExamBarChartContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExamBarChartContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    private void initView(Context context){

    }


}
