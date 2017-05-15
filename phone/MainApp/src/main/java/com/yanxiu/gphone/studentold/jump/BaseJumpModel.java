package com.yanxiu.gphone.studentold.jump;

import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.studentold.activity.MainActivity;


/**
 * 跳转业务基类
 */
public abstract class BaseJumpModel implements YanxiuBaseBean {

    protected Class<?> targetActivity= MainActivity.class;//默认跳转到的Activity

    public Class<?> getTargetActivity() {
        return targetActivity;
    }

    public void setTargetActivity(Class<?> targetActivity) {
        this.targetActivity = targetActivity;
    }
}
