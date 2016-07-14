package com.yanxiu.gphone.hd.student.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.hd.student.utils.ResetManager;

/**
 * Created by Administrator on 2016/3/2.
 */

public class ResetMessageReceiver extends BroadcastReceiver
{
    public static final String INTERFILTER_ACTION="onRestMessageReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {

        if(INTERFILTER_ACTION.equals(intent.getAction())){

            ResetManager.getInstance().notifyAllObservers();
        }
    }
}
