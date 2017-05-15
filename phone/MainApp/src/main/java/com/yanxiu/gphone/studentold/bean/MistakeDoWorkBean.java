package com.yanxiu.gphone.studentold.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/14 15:43.
 * Function :
 */

public class MistakeDoWorkBean implements YanxiuBaseBean {
    mStatus status;

    public class mStatus{
        String desc;
        int code;

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

    public mStatus getStatus() {
        return status;
    }

    public void setStatus(mStatus status) {
        this.status = status;
    }
}
