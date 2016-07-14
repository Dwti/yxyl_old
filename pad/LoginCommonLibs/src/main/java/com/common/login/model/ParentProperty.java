package com.common.login.model;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by lee on 16-3-28.
 */
public class ParentProperty implements YanxiuBaseBean {

    private ParentBindInfo bindinfo;
    private ParentPassport passport;
    private ParentInfo user;
    private ParentGetChildClassInfoBean classinfo;
    private int isBind;


    public int getIsBind() {
        return isBind;
    }

    public void setIsBind(int isBind) {
        this.isBind = isBind;
    }

    public ParentGetChildClassInfoBean getClassinfo() {
        return classinfo;
    }

    public void setClassinfo(ParentGetChildClassInfoBean classinfo) {
        this.classinfo = classinfo;
    }

    public ParentPassport getPassport() {
        return passport;
    }

    public void setPassport(ParentPassport passport) {
        this.passport = passport;
    }

    public ParentBindInfo getBindinfo() {
        return bindinfo;
    }

    public void setBindinfo(ParentBindInfo bindinfo) {
        this.bindinfo = bindinfo;
    }

    public ParentInfo getUser() {
        return user;
    }

    public void setUser(ParentInfo user) {
        this.user = user;
    }
}
