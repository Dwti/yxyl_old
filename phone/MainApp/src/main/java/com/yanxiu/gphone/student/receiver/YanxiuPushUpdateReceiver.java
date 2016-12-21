package com.yanxiu.gphone.student.receiver;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.common.core.utils.LogInfo;
import com.common.login.LoginModel;
import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.activity.MainActivity;
import com.yanxiu.gphone.student.bean.PushMsgBean;

import java.util.List;

/**
 * Created by Administrator on 2015/9/18.
 */
public class YanxiuPushUpdateReceiver extends BroadcastReceiver {
    public static final String PUSH_RECEIVER_INTENT_ACTION = "com.yanxiu.gphone.student.yanxiu_push_update_receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        isAppOnForeground(MainActivity.getInstance() != null ?
                MainActivity.getInstance() :
                context);
        if (LoginModel.getLoginBean() == null || LoginModel.getUserinfoEntity() == null || YanxiuApplication.getInstance().isForceUpdate()) {
            LogInfo.log("haitian", "-----------isForceUpdate------LoginBeanIsNull------UserInfoIsNull-----");
            return;
        }
        PushMsgBean mPushMsgBean = (PushMsgBean) intent.getSerializableExtra("mPushMsgBean");
        if (mPushMsgBean != null) {
            if (MainActivity.getInstance() != null && mPushMsgBean.getMsg_type() != MainActivity.NOTIFICATION_ACTION_JOIN_THE_CLASS) {
                MainActivity.getInstance().judgeToJump(intent);
            } else {
                MainActivity.launch(context, mPushMsgBean);
            }
        }
    }

    /**
     * 推送使应用回到前台
     *
     * @return
     */
    @SuppressLint("NewApi")
    public boolean isAppOnForeground(Context mContext) {
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = mContext.getPackageName();
        LogInfo.log("PushReceiver", "packageName =" + packageName);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null) {
            LogInfo.log("PushReceiver", "------appProcesses == null-----");
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            LogInfo.log("PushReceiver",
                    "------appProcess.processName =" + appProcess.processName);
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance
                    == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            } else {
                List<ActivityManager.RunningTaskInfo> taskList = activityManager
                        .getRunningTasks(100);
                for (ActivityManager.RunningTaskInfo rti : taskList) {
                    if (rti != null && rti.baseActivity != null
                            && mContext.getPackageName() != null) {
                        if (mContext.getPackageName()
                                .equals(rti.baseActivity.getPackageName())) {
                            if (Build.VERSION.SDK_INT >= 11) {
                                activityManager.moveTaskToFront(rti.id,
                                        ActivityManager.MOVE_TASK_WITH_HOME);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private void show(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
