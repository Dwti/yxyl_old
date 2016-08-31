package com.yanxiu.gphone.student.inter;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.common.core.utils.LogInfo;

/**
 * Created by JS-00 on 2016/8/3.
 */
public class OnPushPullTouchListener implements View.OnTouchListener ,View.OnClickListener{

    private final int height;
    private LinearLayout bottom_view;
    private RelativeLayout top_view;

    public OnPushPullTouchListener(LinearLayout bottom_view, RelativeLayout top_view, Activity activity) {
        this.bottom_view = bottom_view;
        this.top_view = top_view;
        WindowManager wm = (WindowManager) activity.getSystemService(activity.WINDOW_SERVICE);
        height = wm.getDefaultDisplay().getHeight();
    }

    public float y;//触点Y坐标

    public float yy;//控件高度

    private float move_y;//y轴移动距离

    private float tital_height;//总高度

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y = (int) motionEvent.getRawY();
                yy = (int) bottom_view.getHeight();
                tital_height = yy + top_view.getHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                int y_now = (int) motionEvent.getRawY();
                move_y = y_now - y;
                setMove(bottom_view, top_view);
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    private void setMove(LinearLayout bottom_view, RelativeLayout top_view) {
        int top_height=top_view.getHeight();
        int bottom_height=bottom_view.getHeight();
//        if (yy - move_y>height * 1 / 20&&yy - move_y < height * 3 / 5) {

        if (move_y>0&&bottom_height>100){

        }else if (move_y<0&&top_height>300){

        }else {
            return;
        }

//        if (top_height>200&&bottom_height>200){
            float bottom_weight=(yy - move_y)/tital_height;
            float top_weight=1-bottom_weight;

            LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) bottom_view.getLayoutParams();
            layoutParams.weight=bottom_weight;
            bottom_view.setLayoutParams(layoutParams);

            LinearLayout.LayoutParams layoutParams1= (LinearLayout.LayoutParams) top_view.getLayoutParams();
            layoutParams1.weight=top_weight;
            top_view.setLayoutParams(layoutParams1);
//        }
    }

    @Override
    public void onClick(View v) {

    }
}
