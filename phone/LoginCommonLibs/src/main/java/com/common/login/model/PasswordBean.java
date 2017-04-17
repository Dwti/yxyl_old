package com.common.login.model;

/**
 * Created by Administrator on 2015/7/17.
 */
public class PasswordBean {

    /**
     * uid : 8
     * password :
     * mobile : 18642802671
     * id : 0
     * token : 111020a985d63918caa9fdbffc088eab
     */
    private int uid;
    private String password;
    private String mobile;
    private int id;
    private String token;
    private String loginName;
    private String type; // 0:手机/账号  1：三方登陆

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

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
