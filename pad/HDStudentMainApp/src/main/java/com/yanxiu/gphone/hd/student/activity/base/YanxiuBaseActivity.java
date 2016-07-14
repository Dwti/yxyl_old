package com.yanxiu.gphone.hd.student.activity.base;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.common.core.utils.LogInfo;
import com.tencent.android.tpush.XGPushManager;
import com.tendcloud.tenddata.TCAgent;
import com.yanxiu.gphone.hd.student.bean.statistics.StatisticHashMap;
import com.yanxiu.gphone.hd.student.manager.ActivityManager;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;
import com.yanxiu.gphone.hd.student.utils.statistics.DataStatisticsUploadManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2015/5/21.
 */
public class YanxiuBaseActivity extends FragmentActivity {
    private boolean isActive = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActicity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 页面开始
        TCAgent.onPageStart(this, this.getClass().getName());
        XGPushManager.onActivityStarted(this);
        if (!isActive) {
            //app 从后台唤醒，进入前台
            LogInfo.log("frc", "front front front ");
            isActive = true;
            onForgroundStatistic();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        // 页面结束
        TCAgent.onPageEnd(this, this.getClass().getName());
        XGPushManager.onActivityStoped(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppOnForeground()) {
            //app 进入后台
            isActive = false;
            onBackgroundStatistic();

            //全局变量isActive = false 记录当前已经进入后台
            LogInfo.log("frc", "back back back");
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

    @Override protected void onDestroy() {
        super.onDestroy();
        ActivityManager.destoryActivity(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 程序是否在后台运行
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

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

        return false;
    }

    // 获取点击事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    // 判定是否需要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
    // 隐藏软键盘
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
