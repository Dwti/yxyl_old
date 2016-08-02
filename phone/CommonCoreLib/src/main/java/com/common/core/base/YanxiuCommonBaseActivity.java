package com.common.core.base;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.WindowManager;

import com.tencent.android.tpush.XGPushManager;

import java.util.List;

/**
 * Created by Administrator on 2015/5/21.
 */
public class YanxiuCommonBaseActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("当前",this.getClass().getName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 页面开始
        XGPushManager.onActivityStarted(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // 页面结束
        XGPushManager.onActivityStoped(this);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
