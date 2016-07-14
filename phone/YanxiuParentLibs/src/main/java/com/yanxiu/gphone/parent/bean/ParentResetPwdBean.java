package com.yanxiu.gphone.parent.bean;

import java.util.List;

/**
 * Created by lidongming on 16/4/7.
 */
public class ParentResetPwdBean extends ParentRequestBean{


    /**
     * deviceid : 1276405606-499
     * mobile : 15665656656
     * token : reTH08KJD933LFKDLLF;D93KLSD;
     * uid : 234
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String deviceid;
        private String mobile;
        private String token;
        private int uid;

        public String getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(String deviceid) {
            this.deviceid = deviceid;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
    }
}
