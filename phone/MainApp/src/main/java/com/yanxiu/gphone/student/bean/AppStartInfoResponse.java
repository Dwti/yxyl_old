package com.yanxiu.gphone.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by sunpeng on 2017/3/30.
 */

public class AppStartInfoResponse implements YanxiuBaseBean {
    private StatusInfo status = new StatusInfo();
    private String result;

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "AppStartInfoResponse{" +
                "status=" + status.toString() +
                ", result='" + result + '\'' +
                '}';
    }

    private class StatusInfo implements YanxiuBaseBean{
        private String status;
        private String code;
        private String desc;

        @Override
        public String toString() {
            return "StatusInfo{" +
                    "status='" + status + '\'' +
                    ", code='" + code + '\'' +
                    ", desc='" + desc + '\'' +
                    '}';
        }
    }
}
