package com.yanxiu.gphone.studentold.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/4/10 15:15.
 * Function :
 */

public class MistakeAllFragmentBean implements YanxiuBaseBean {

    private List<MistakeAllFragBean> data;
    private mStatus status;

    public List<MistakeAllFragBean> getData() {
        return data;
    }

    public void setData(List<MistakeAllFragBean> data) {
        this.data = data;
    }

    public mStatus getStatus() {
        return status;
    }

    public void setStatus(mStatus status) {
        this.status = status;
    }

    public class mStatus{
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
    }
}
