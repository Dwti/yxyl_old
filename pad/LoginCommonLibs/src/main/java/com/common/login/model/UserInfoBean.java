package com.common.login.model;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/16.
 */
public class UserInfoBean extends SrtBaseBean implements YanxiuBaseBean {

    private DataStatusEntityBean status;

    private ArrayList<UserInfo> data;

    private boolean isThridLogin =false;

    public DataStatusEntityBean getStatus() {
        return status;
    }

    public void setStatus(DataStatusEntityBean status) {
        this.status = status;
    }

    public ArrayList<UserInfo> getData() {
        return data;
    }

    public void setData(ArrayList<UserInfo> data) {
        this.data = data;
    }

    public boolean isThridLogin() {
        return isThridLogin;
    }

    public void setIsThridLogin(boolean isThridLogin) {
        this.isThridLogin = isThridLogin;
    }
}
