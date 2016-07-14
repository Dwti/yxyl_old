package com.yanxiu.gphone.student.bean.statistics;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by frc on 16-6-7.
 */
public class UploadAllPointFileBean implements YanxiuBaseBean {
    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "UploadAllPointFileBean{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
