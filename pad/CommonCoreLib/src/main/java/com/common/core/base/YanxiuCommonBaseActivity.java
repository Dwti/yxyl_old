package com.common.core.base;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.tencent.android.tpush.XGPushManager;

/**
 * Created by Administrator on 2015/5/21.
 */
public class YanxiuCommonBaseActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
