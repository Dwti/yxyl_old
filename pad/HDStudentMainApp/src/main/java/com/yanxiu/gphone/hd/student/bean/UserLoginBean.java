package com.yanxiu.gphone.hd.student.bean;

/**
 * Created by Administrator on 2015/5/14.
 */
public class UserLoginBean extends SrtBaseBean {

    /**
     * head : http: //utest.yanxiu.com/static/yanxiu/u/17/17/Img21717_80.jpg
     * uid : 8977403
     * code : 0
     * uname : 丰的三次方3
     * actiFlag : 1
     * desc : success
     * token : d3a815c2d14b85885726ee3669e61ed8
     */
    private String head;
    private int uid;
    private int code;
    private String uname;
    private int actiFlag;
    private String desc;
    private String token;

    public void setHead(String head) {
        this.head = head;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public void setActiFlag(int actiFlag) {
        this.actiFlag = actiFlag;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public int getCode() {
        return code;
    }

    public String getUname() {
        return uname;
    }

    public int getActiFlag() {
        return actiFlag;
    }

    public String getDesc() {
        return desc;
    }

    public String getToken() {
        return token;
    }
}
