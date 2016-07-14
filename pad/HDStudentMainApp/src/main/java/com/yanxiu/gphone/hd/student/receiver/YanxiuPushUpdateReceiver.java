package com.yanxiu.gphone.hd.student.receiver;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.common.core.utils.LogInfo;
import com.common.login.LoginModel;
import com.yanxiu.gphone.hd.student.YanxiuApplication;
import com.yanxiu.gphone.hd.student.activity.MainActivity;
import com.yanxiu.gphone.hd.student.bean.PushMsgBean;

import java.util.List;

/**
 * Created by Administrator on 2015/9/18.
 */
public class YanxiuPushUpdateReceiver extends BroadcastReceiver {
    public static final String PUSH_RECEIVER_INTENT_ACTION = "com.yanxiu.gphone.hd.student.yanxiu_push_update_receiver";
    private final static String TAG=YanxiuPushUpdateReceiver.class.getSimpleName();
    @Override public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        isAppOnForeground(MainActivity.getInstance() != null ?
                MainActivity.getInstance() :
                context);
        if(LoginModel.getUserinfoEntity() == null || LoginModel.getUserinfoEntity() == null || YanxiuApplication.getInstance().isForceUpdate()){
            LogInfo.log(TAG, "-----------isForceUpdate------LoginBeanIsNull------UserInfoIsNull-----");
            return;
        }
        PushMsgBean mPushMsgBean = (PushMsgBean) intent.getSerializableExtra("mPushMsgBean");
        if(mPushMsgBean != null) {
            LogInfo.log("haitian", "onReceive  mPushMsgBean ="+mPushMsgBean.toString());
            if (MainActivity.getInstance() != null && mPushMsgBean.getMsg_type() != MainActivity.NOTIFICATION_ACTION_JOIN_THE_CLASS) {
                LogInfo.log("haitian", "-----MainActivity-judgeToJump-----");
                MainActivity.getInstance().judgeToJump(intent);
            } else {
                LogInfo.log("haitian", "-----MainActivity-launch-----");
                MainActivity.launch(context, mPushMsgBean);
            }
        }
    }

    /**
     * 推送使应用回到前台
     *
     * @return
     */
    @SuppressLint("NewApi") public boolean isAppOnForeground(Context mContext) {
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = mContext.getPackageName();
        LogInfo.log(TAG, "packageName =" + packageName);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null) {
            LogInfo.log(TAG, "------appProcesses == null-----");
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            LogInfo.log(TAG,
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

}
