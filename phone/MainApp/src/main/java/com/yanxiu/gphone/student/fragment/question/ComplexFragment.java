package com.yanxiu.gphone.student.fragment.question;

import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.common.core.utils.LogInfo;

/**
 * Created by Yjj on 2016/7/25.
 */
public class ComplexFragment extends BaseQuestionFragment implements View.OnTouchListener {

    public int x;//触点X坐标
    public int y;//触点Y坐标
    
    public int yy;//控件高度
    public int xx;//控件宽度

    private int move_x;//x轴移动距离
    private int move_y;//y轴移动距离

    private RelativeLayout mRelativeLayout;
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mRelativeLayout = (RelativeLayout)view;
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x= (int) motionEvent.getRawX();
                y= (int) motionEvent.getRawY();
                xx= (int) mRelativeLayout.getWidth();
                yy= (int) mRelativeLayout.getHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                int x_now= (int) motionEvent.getRawX();
                int y_now= (int) motionEvent.getRawY();

                //用来说明滑动情况，无意义
                if (Math.abs(x_now)>Math.abs(x)) {
                    //向友
                    LogInfo.log("asd","右");
                    if (Math.abs(y_now)>Math.abs(y)){
                        //向下
                        LogInfo.log("asd","下");
                    }else {
                        //向上
                        LogInfo.log("asd","上");
                    }
                }else {
                    //向左
                    LogInfo.log("asd","左");
                    if (Math.abs(y_now)>Math.abs(y)){
                        //向下
                        LogInfo.log("asd","下");
                    }else {
                        //向上
                        LogInfo.log("asd","上");
                    }
                }

                move_x=x_now-x;
                move_y=y_now-y;
                setMove();
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return true;
    }

    private void setMove(){
        LogInfo.log("xx",xx+"+XXXXXXXXX");
        LogInfo.log("xx",yy+"+YYYYYYYYY");
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(xx,yy-move_y);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        mRelativeLayout.setLayoutParams(layoutParams);
    }
}
