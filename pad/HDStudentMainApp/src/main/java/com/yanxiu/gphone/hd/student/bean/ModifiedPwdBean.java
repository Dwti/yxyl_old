package com.yanxiu.gphone.hd.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.List;

/**
 * Created by Administrator on 2015/7/21.
 */
public class ModifiedPwdBean implements YanxiuBaseBean {

    /**
     * data : [{"uid":1,"password":"","mobile":"","id":1,"token":"qwertyuiop"}]
     * status : {"code":0,"desc":"success"}
     */
    private List<DataEntity> data;
    private DataStatusEntityBean status;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public void setStatus(DataStatusEntityBean status) {
        this.status = status;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public DataStatusEntityBean getStatus() {
        return status;
    }

    public class DataEntity {
        /**
         * uid : 1
         * password :
         * mobile :
         * id : 1
         * token : qwertyuiop
         */
        private int uid;
        private String password;
        private String mobile;
        private int id;
        private String token;

        public void setUid(int uid) {
            this.uid = uid;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getUid() {
            return uid;
        }

        public String getPassword() {
            return password;
        }

        public String getMobile() {
            return mobile;
        }

        public int getId() {
            return id;
        }

        public String getToken() {
            return token;
        }
    }

    public class StatusEntity {
        /**
         * code : 0
         * desc : success
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
}
