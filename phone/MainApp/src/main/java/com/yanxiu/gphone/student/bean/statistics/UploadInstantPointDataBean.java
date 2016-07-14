package com.yanxiu.gphone.student.bean.statistics;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by frc on 16-6-7.
 */
public class UploadInstantPointDataBean implements YanxiuBaseBean {
    //private String code;
    //private String desc;
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
/*@Override
    public String toString() {
        return "UploadInstantPointDataBean{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

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
    }*/
}
