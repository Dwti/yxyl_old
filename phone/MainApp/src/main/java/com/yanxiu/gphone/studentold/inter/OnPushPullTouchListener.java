package com.yanxiu.gphone.studentold.inter;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by JS-00 on 2016/8/3.
 */
public class OnPushPullTouchListener implements View.OnTouchListener ,View.OnClickListener{

    private final int height;
    private LinearLayout bottom_view;
    private RelativeLayout top_view;
    private float top_height;
    private float bottom_height;
    private final int MIN_HEIGHT = 100;
    private final int MAX_HEIGHT = 300;

    public OnPushPullTouchListener(LinearLayout bottom_view, RelativeLayout top_view, Activity activity) {
        this.bottom_view = bottom_view;
        this.top_view = top_view;
        WindowManager wm = (WindowManager) activity.getSystemService(activity.WINDOW_SERVICE);
        height = wm.getDefaultDisplay().getHeight();
    }

    public float y;//触点Y坐标

//    public float yy;//控件高度

    private float move_y;//y轴移动距离

    private float tital_height;//总高度

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y = (int) motionEvent.getRawY();
                top_height=top_view.getHeight();
                bottom_height=bottom_view.getHeight();
//                yy = (int) bottom_view.getHeight();
                tital_height = top_height + bottom_height;
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

//        if (yy - move_y>height * 1 / 20&&yy - move_y < height * 3 / 5) {

        float height=bottom_height-move_y;

        if (move_y>0){
            if (height>MIN_HEIGHT){
                float bottom_weight=(height)/tital_height;
                float top_weight=1-bottom_weight;

                LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) bottom_view.getLayoutParams();
                layoutParams.weight=bottom_weight;
                bottom_view.setLayoutParams(layoutParams);

                LinearLayout.LayoutParams layoutParams1= (LinearLayout.LayoutParams) top_view.getLayoutParams();
                layoutParams1.weight=top_weight;
                top_view.setLayoutParams(layoutParams1);
            }
        }else if (move_y<0){
            if (top_height+move_y>MAX_HEIGHT){
                float bottom_weight=(height)/tital_height;
                float top_weight=1-bottom_weight;

                LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) bottom_view.getLayoutParams();
                layoutParams.weight=bottom_weight;
                bottom_view.setLayoutParams(layoutParams);

                LinearLayout.LayoutParams layoutParams1= (LinearLayout.LayoutParams) top_view.getLayoutParams();
                layoutParams1.weight=top_weight;
                top_view.setLayoutParams(layoutParams1);
            }
        }else {
            return;
        }

//        if (top_height>200&&bottom_height>200){
            //        }
    }

    @Override
    public void onClick(View v) {

    }
}
