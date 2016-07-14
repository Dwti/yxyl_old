package com.yanxiu.gphone.hd.student.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/7/16.
 */
public class RegisterBean extends SrtBaseBean{

    /**
     * data : [{"head":"http://utest.yanxiu.com/static/yanxiu/u/17/17/Img21717_80.jpg","uid":8977403,"uname":"丰的三次方3","actiFlag":1,"token":"63b07d487eba1ee3b4ab7c5411f743db"}]
     * page : null
     * status : {"code":0,"desc":"success"}
     */
    private List<DataEntity> data;
    private String page;
    private DataStatusEntityBean status;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setStatus(DataStatusEntityBean status) {
        this.status = status;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public String getPage() {
        return page;
    }

    public DataStatusEntityBean getStatus() {
        return status;
    }

    public class DataEntity {
        /**
         * head : http://utest.yanxiu.com/static/yanxiu/u/17/17/Img21717_80.jpg
         * uid : 8977403
         * uname : 丰的三次方3
         * actiFlag : 1
         * token : 63b07d487eba1ee3b4ab7c5411f743db
         */
        private String head;
        private int uid;
        private String uname;
        private int actiFlag;
        private String token;

        public void setHead(String head) {
            this.head = head;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public void setActiFlag(int actiFlag) {
            this.actiFlag = actiFlag;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getHead() {
            return head;
        }

        public int getUid() {
            return uid;
        }

        public String getUname() {
            return uname;
        }

        public int getActiFlag() {
            return actiFlag;
        }

        public String getToken() {
            return token;
        }
    }
}
