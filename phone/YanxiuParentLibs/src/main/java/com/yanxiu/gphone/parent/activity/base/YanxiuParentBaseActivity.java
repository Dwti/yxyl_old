package com.yanxiu.gphone.parent.activity.base;

import android.content.res.Configuration;
import android.os.Bundle;

import com.common.core.base.YanxiuCommonBaseActivity;
import com.yanxiu.gphone.parent.manager.ParentActivityManager;

/**
 * Created by Administrator on 2015/5/21.
 */
public class YanxiuParentBaseActivity extends YanxiuCommonBaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParentActivityManager.addActicity(this);
    }
    @Override protected void onDestroy() {
        ParentActivityManager.destoryActivity(this);
        super.onDestroy();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
