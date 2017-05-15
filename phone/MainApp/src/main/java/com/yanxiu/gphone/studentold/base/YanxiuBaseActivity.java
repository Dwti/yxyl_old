package com.yanxiu.gphone.studentold.base;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.common.core.base.YanxiuCommonBaseActivity;
import com.yanxiu.gphone.studentold.bean.statistics.StatisticHashMap;
import com.yanxiu.gphone.studentold.manager.ActivityManager;
import com.yanxiu.gphone.studentold.utils.Util;
import com.yanxiu.gphone.studentold.utils.YanXiuConstant;
import com.yanxiu.gphone.studentold.utils.statistics.DataStatisticsUploadManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2015/5/21.
 */
public class YanxiuBaseActivity extends YanxiuCommonBaseActivity {
    private boolean isActive = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActicity(this);
//        Log.d("asd",this.getClass().getName());
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!isActive) {
            //app 从后台唤醒，进入前台
            Log.e("frc", "front front front ");
            isActive = true;
            onForgroundStatistic();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppOnForeground()) {
            //app 进入后台
            isActive = false;
            onBackgroundStatistic();

            //全局变量isActive = false 记录当前已经进入后台
            Log.e("frc", "back back back");
        }
    }
    //进入前台
    private void onForgroundStatistic() {
        StatisticHashMap statisticHashMap = new StatisticHashMap();
        statisticHashMap.put(YanXiuConstant.eventID, "20:event_7");//7:进入前台
        ArrayList<StatisticHashMap> arrayList = new ArrayList<StatisticHashMap>();
        arrayList.add(statisticHashMap);
        HashMap<String, String> forgroundHashMap = new HashMap<>();
        forgroundHashMap.put(YanXiuConstant.content, Util.listToJson(arrayList));
        DataStatisticsUploadManager.getInstance().NormalUpLoadData(this, forgroundHashMap);
    }
    @Override protected void onDestroy() {
        super.onDestroy();
        ActivityManager.destoryActivity(this);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    //进入后台
    private void onBackgroundStatistic() {
        StatisticHashMap statisticHashMap = new StatisticHashMap();
        statisticHashMap.put(YanXiuConstant.eventID, "20:event_6");//6:进入后台
        ArrayList<StatisticHashMap> arrayList = new ArrayList<StatisticHashMap>();
        arrayList.add(statisticHashMap);
        HashMap<String, String> backgroundHashMap = new HashMap<>();
        backgroundHashMap.put(YanXiuConstant.content, Util.listToJson(arrayList));
        DataStatisticsUploadManager.getInstance().NormalUpLoadData(this, backgroundHashMap);
    }

    /**
     * 程序是否在后台运行
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device
        try {
            android.app.ActivityManager activityManager = (android.app.ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            String packageName = getApplicationContext().getPackageName();

            List<android.app.ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                    .getRunningAppProcesses();
            if (appProcesses == null)
                return false;

            for (android.app.ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                // The name of the process that this object is associated with.
                if (appProcess.processName.equals(packageName)
                        && appProcess.importance == android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                }
            }
        }catch (Exception e){}
        return false;
    }

}
