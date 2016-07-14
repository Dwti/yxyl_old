package com.yanxiu.gphone.parent.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by Administrator on 2015/7/16.
 */
public class ParentDataStatusEntityBean implements YanxiuBaseBean {
    public static final int REQUEST_SUCCESS=0;//请求成功
    /**
     * code : 0
     * desc : get schools success
     */
    private int code;
    private String desc;

    public void setCode(int code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
