package com.yanxiu.gphone.studentold.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by cangHaiXiao on 2016/11/16.
 *
 * 防双击
 */

public class DoubleKillUtils {

    private static DoubleKillUtils killUtils;
    private boolean doublekill_flag=true;
    private static int time=300;

    public static DoubleKillUtils getInstence(){
        killUtils=new DoubleKillUtils();
        return killUtils;
    }

    public void setTime(int time){
        this.time=time;
    }

    public boolean getFlag(){
        boolean flag=doublekill_flag;
        if (doublekill_flag) {
            setPreDoubleKill();
        }
        return flag;
    }

    private void setPreDoubleKill(){
        doublekill_flag=false;
        Timer timer=new Timer();
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                doublekill_flag=true;
            }
        };
        timer.schedule(task,time);
    }


}
