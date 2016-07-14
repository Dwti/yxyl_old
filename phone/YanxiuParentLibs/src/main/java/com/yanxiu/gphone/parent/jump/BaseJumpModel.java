package com.yanxiu.gphone.parent.jump;


import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.parent.activity.MainForParentActivity;

/**
 * 跳转业务基类
 */
public abstract class BaseJumpModel implements YanxiuBaseBean {

    protected Class<?> targetActivity=MainForParentActivity.class;//默认跳转到的Activity

    public Class<?> getTargetActivity() {
        return targetActivity;
    }

    public void setTargetActivity(Class<?> targetActivity) {
        this.targetActivity = targetActivity;
    }
}
