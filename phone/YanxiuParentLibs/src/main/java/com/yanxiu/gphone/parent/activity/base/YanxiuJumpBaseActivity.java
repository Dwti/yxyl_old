package com.yanxiu.gphone.parent.activity.base;

import android.content.Intent;
import android.os.Bundle;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.parent.jump.BaseJumpModel;
import com.yanxiu.gphone.parent.jump.constants.JumpModeConstants;


/**
 * 需要使用JumpMode方式跳转，统一继承此类，接受Intent的BaseJumpMode参数封装类
 */
public abstract  class YanxiuJumpBaseActivity extends YanxiuParentBaseActivity {
    private static final String TAG="YanxiuJumpBaseActivity";
    private BaseJumpModel baseJumpModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reveiceBaseJumpModel(getIntent());
        initLaunchIntentData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        reveiceBaseJumpModel(intent);
        initLaunchIntentData();
        super.onNewIntent(intent);

    }
    private BaseJumpModel getBaseJumpModeFromIntent(Intent intent){
        if(intent==null){
            LogInfo.log(TAG, "Intent is null-log from YanxiuJumpBaseaActivity");
            return null;
        }
        BaseJumpModel  baseJumpModel = (BaseJumpModel) intent
                .getSerializableExtra(JumpModeConstants.JUMP_MODEL_KEY);
        return baseJumpModel;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 通过传入的Intent 获得BaseJumpMode对象
     * @param intent
     */
    private void reveiceBaseJumpModel(Intent intent) {
        baseJumpModel=getBaseJumpModeFromIntent(intent);
    }


    /**
     * 适用于onActivityResult获得的BaseJumpMode
     * 得到SetResult返回的BaseJumpMode
     * @param intent : setResult 设置的Intent
     * @return
     */
    public BaseJumpModel getBaseJumpModeFromSetResult(Intent intent){
        return getBaseJumpModeFromIntent(intent);
    }

    /**
     * 通过从其他界面跳转而来的Intent获得BaseJumpMode参数
     * @return
     */
    public BaseJumpModel getBaseJumpModel() {
        return baseJumpModel;
    }



    protected abstract void initLaunchIntentData();

}
