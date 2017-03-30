package com.yanxiu.gphone.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by sunpeng on 2017/3/30.
 */

public class AppStartInfo extends APPCommonInfo {
    private ExtraInfo reserved = new ExtraInfo();

    public ExtraInfo getReserved() {
        return reserved;
    }

    public void setReserved(ExtraInfo reserved) {
        this.reserved = reserved;
    }
}
