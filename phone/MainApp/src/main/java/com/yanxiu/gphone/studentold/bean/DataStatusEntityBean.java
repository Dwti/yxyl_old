package com.yanxiu.gphone.studentold.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by Administrator on 2015/7/16.
 */
public class DataStatusEntityBean implements YanxiuBaseBean {
    public static final int REQUEST_SUCCESS=0;//请求成功
    /**
     * code : 0
     * desc : get schools success
     */
    private int code;
    private String desc;
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DataStatusEntityBean{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
