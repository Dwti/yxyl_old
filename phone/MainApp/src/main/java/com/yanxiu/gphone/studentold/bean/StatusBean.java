package com.yanxiu.gphone.studentold.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by sunpeng on 2017/3/13.
 */

public class StatusBean implements YanxiuBaseBean {
    private String desc;
    private int code;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
