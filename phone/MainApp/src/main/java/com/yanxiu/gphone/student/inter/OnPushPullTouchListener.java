package com.yanxiu.gphone.student.inter;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.common.core.utils.LogInfo;

/**
 * Created by JS-00 on 2016/8/3.
 */
public class OnPushPullTouchListener implements View.OnTouchListener {
    private LinearLayout mLinearLayout;
    private Context mContext;
    public OnPushPullTouchListener(LinearLayout linearLayout, Activity activity) {
        mLinearLayout = linearLayout;
        mContext = activity;
    }
    public int x;//触点X坐标
    public int y;//触点Y坐标

    public int yy;//控件高度
    public int xx;//控件宽度

    private int move_x;//x轴移动距离
    private int move_y;//y轴移动距离

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x= (int) motionEvent.getRawX();
                y= (int) motionEvent.getRawY();
                xx= (int) mLinearLayout.getWidth();
                yy= (int) mLinearLayout.getHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                int x_now= (int) motionEvent.getRawX();
                int y_now= (int) motionEvent.getRawY();

                //用来说明滑动情况，无意义
                if (Math.abs(x_now)>Math.abs(x)) {
                    //right
                    LogInfo.log("flip","right");
                    if (Math.abs(y_now)>Math.abs(y)){
                        //down
                        LogInfo.log("flip","down");
                    }else {
                        //up
                        LogInfo.log("flip","up");
                    }
                }else {
                    //left
                    LogInfo.log("flip","left");
                    if (Math.abs(y_now)>Math.abs(y)){
                        //down
                        LogInfo.log("flip","down");
                    }else {
                        //up
                        LogInfo.log("flip","up");
                    }
                }

                move_x=x_now-x;
                move_y=y_now-y;
                setMove(mLinearLayout);
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return true;
    }

    private void setMove(LinearLayout linearLayout){
        LogInfo.log("move",xx+"+XXXXXXXXX");
        LogInfo.log("move",yy-move_y+"+YYYYYYYYY");
        WindowManager wm = (WindowManager) mContext
                .getSystemService(mContext.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        LogInfo.log("move",height+"+YYYYYYYYY");
        if (yy-move_y < height*3/5) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(xx, yy - move_y);
//            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(xx,0,1.0f);
//            RelativeLayout.LayoutParams layoutParams2=new RelativeLayout.LayoutParams()
            linearLayout.setLayoutParams(layoutParams);
//            linearLayout.setLayout
        }
        //layoutParams.addRule(LinearLayout., RelativeLayout.TRUE);
    }
}
