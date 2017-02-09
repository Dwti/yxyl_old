package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by sp on 17-2-8.
 */

public class UnMoveListView extends ListView {
    public UnMoveListView(Context context) {
        super(context);
    }

    public UnMoveListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnMoveListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
