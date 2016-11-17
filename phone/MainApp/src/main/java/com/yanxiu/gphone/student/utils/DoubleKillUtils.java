package com.yanxiu.gphone.student.utils;

import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/11/16.
 */

public class DoubleKillUtils {

    private static DoubleKillUtils killUtils;
    private boolean doublekill_flag=true;
    private static int time=300;

    public static DoubleKillUtils getInstence(){
        killUtils=new DoubleKillUtils();
        return killUtils;
    }

    public void setKillView(View view){

    }

    public void setTime(int time){
        this.time=time;
    }

    public boolean getFlag(){
        boolean flag=true;
        flag=doublekill_flag;
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
